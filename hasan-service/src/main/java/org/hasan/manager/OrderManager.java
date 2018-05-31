package org.hasan.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.account.bean.entity.Account;
import org.gatlin.soa.account.bean.entity.Recharge;
import org.gatlin.soa.account.bean.model.AccountDetail;
import org.gatlin.soa.bean.enums.AccountType;
import org.gatlin.soa.bean.enums.TargetType;
import org.gatlin.soa.bean.param.SoaSidParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.user.api.GeoService;
import org.gatlin.soa.user.bean.UserCode;
import org.gatlin.soa.user.bean.entity.UserAddress;
import org.gatlin.util.DateUtil;
import org.gatlin.util.IDWorker;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.HasanConsts;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.CfgGoodsPrice;
import org.hasan.bean.entity.LogOrderPay;
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.entity.UserEvaluation;
import org.hasan.bean.enums.HasanBizType;
import org.hasan.bean.enums.OrderState;
import org.hasan.bean.param.AssistantOrdersParam;
import org.hasan.bean.param.DeliverParam;
import org.hasan.bean.param.EvaluateParam;
import org.hasan.bean.param.OrderMakeParam;
import org.hasan.bean.param.PayPreviewParam;
import org.hasan.mybatis.dao.LogOrderPayDao;
import org.hasan.mybatis.dao.OrderDao;
import org.hasan.mybatis.dao.OrderGoodsDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderManager {

	@Resource
	private OrderDao orderDao;
	@Resource
	private GeoService geoService;
	@Resource
	private GoodsManager goodsManager;
	@Resource
	private HasanManager hasanManager;
	@Resource
	private OrderGoodsDao orderGoodsDao;
	@Resource
	private ConfigService configService;
	@Resource
	private LogOrderPayDao logOrderPayDao;
	@Resource
	private AccountService accountService;
	@Resource
	private ResourceService resourceService;
	
	// 下订单
	@Transactional
	public Order make(OrderMakeParam param) {
		UserAddress address = geoService.address(param.getAddressId());
		Assert.notNull(UserCode.USER_ADDRESS_NOT_EXIST, address);
		Assert.isTrue(CoreCode.FORBID, address.getUid() == param.getUser().getId());
		Map<Integer, CfgGoods> goods = goodsManager.buy(param.getGoods());
		UserCustom custom = hasanManager.userCustom(param.getUser().getId());
		String orderId = IDWorker.INSTANCE.nextSid();
		Map<Integer, CfgGoodsPrice> prices = goodsManager.goodsPrice(param.getGoods().keySet(), custom);
		List<OrderGoods> list = EntityGenerator.newOrderGoods(orderId, param.getGoods(), goods, prices);
		BigDecimal price = BigDecimal.ZERO;
		for (OrderGoods temp : list)
			price = price.add(temp.getUnitPrice().multiply(BigDecimal.valueOf(temp.getGoodsNum())));
		Order order = EntityGenerator.newOrder(orderId, param, price, address, configService.geo(address.getCounty(), false));
		orderDao.insert(order);
		orderGoodsDao.batchInsert(list);
		return order;
	}
	
	public LogOrderPay payPreview(PayPreviewParam param) {
		UserCustom custom = null == param.getUser() ? null : hasanManager.userCustom(param.getUser().getId());
		Map<Integer, CfgGoodsPrice> prices = goodsManager.goodsPrice(param.getGoods().keySet(), custom);
		BigDecimal price = BigDecimal.ZERO;
		for (Entry<Integer, Integer> entry : param.getGoods().entrySet()) {
			CfgGoodsPrice goodsPrice = prices.get(entry.getKey());
			if (null == goodsPrice)
				continue;
			price = price.add(goodsPrice.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
		}
		return _orderPayLog(custom, price);
	}
	
	// 支付订单
	@Transactional
	public LogOrderPay pay(SoaSidParam param) {
		Query query = new Query().eq("id", param.getId()).forUpdate();
		Order order = orderDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		Assert.isTrue(CoreCode.FORBID, order.getUid() == param.getUser().getId());
		OrderState state = OrderState.match(order.getState());
		Assert.isTrue(HasanCode.ORDER_STATE_ERR, state == OrderState.INIT);
		UserCustom custom = hasanManager.userCustom(order.getUid());
		LogOrderPay log = _orderPayLog(custom, order.getPrice());
		log.setUid(order.getUid());
		log.setOrderId(order.getId());
		log.setId(IDWorker.INSTANCE.nextSid());
		log.setCreated(DateUtil.current());
		log.setUpdated(log.getCreated());
		_orderPay(log);
		if (log.getRechargeAmount().compareTo(BigDecimal.ZERO) > 0)
			order.setState(OrderState.PAYING.mark());
		else
			order.setState(OrderState.PAID.mark());
		order.setExpressFee(log.getExpressFee());
		order.setUpdated(DateUtil.current());
		orderDao.update(order);
		logOrderPayDao.insert(log);
		return log;
	}
	
	private LogOrderPay _orderPayLog(UserCustom custom, BigDecimal price) {
		LogOrderPay log = new LogOrderPay();
		BigDecimal fee = BigDecimal.ZERO;
		if (null != custom) {
			if (custom.getMemberId() == 0) 
				fee = configService.config(HasanConsts.EXPRESS_FEE);
			log.setExpressFee(fee);
			BigDecimal total = price.add(fee);
			// 先用体验金支付
			Query query = new Query().eq("owner_type", TargetType.USER.mark()).eq("owner", custom.getUid()).eq("type", AccountType.EXP.mark()).forUpdate();
			Account account = accountService.account(query);
			BigDecimal delt = total.min(account.getUsable());
			total = total.subtract(delt);
			log.setExpAmount(delt);
			
			// 体验金不足再用余额支付
			if (total.compareTo(BigDecimal.ZERO) > 0) {
				query = new Query().eq("owner_type", TargetType.USER.mark()).eq("owner", custom.getUid()).eq("type", AccountType.BASIC.mark()).forUpdate();
				account = accountService.account(query);
				delt = total.min(account.getUsable());
				total = total.subtract(delt);
				log.setBasicAmount(delt);
			}
			log.setRechargeAmount(total);
		} else {
			fee = configService.config(HasanConsts.EXPRESS_FEE);
			log.setExpressFee(fee);
			BigDecimal total = price.add(fee);
			log.setRechargeAmount(total);
		}
		return log;
	}
	
	private void _orderPay(LogOrderPay log) {
		AccountDetail detail = new AccountDetail(log.getId(), HasanBizType.ORDER_PAY.mark());
		if (log.getExpAmount().compareTo(BigDecimal.ZERO) > 0) {
			detail.userUsableDecr(log.getUid(), AccountType.EXP, log.getExpAmount());
			if (log.getRechargeAmount().compareTo(BigDecimal.ZERO) > 0) 			// 需要充值则需要先冻结
				detail.userFrozenIncr(log.getUid(), AccountType.EXP, log.getExpAmount());
		}
		if (log.getBasicAmount().compareTo(BigDecimal.ZERO) > 0) {
			detail.userUsableDecr(log.getUid(), AccountType.BASIC, log.getBasicAmount());
			if (log.getRechargeAmount().compareTo(BigDecimal.ZERO) > 0) 			// 需要充值则需要先冻结
				detail.userFrozenIncr(log.getUid(), AccountType.BASIC, log.getBasicAmount());
		}
		accountService.process(detail);
	}
	
	@Transactional
	public Order payNotice(Recharge recharge, boolean success) {
		Query query = new Query().eq("id", recharge.getGoodsId()).forUpdate();
		Order order = orderDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		OrderState state = OrderState.match(order.getState());
		Assert.isTrue(HasanCode.ORDER_STATE_ERR, state == OrderState.PAYING);
		order.setState(success ? OrderState.PAID.mark() : OrderState.INIT.mark());
		order.setUpdated(DateUtil.current());
		orderDao.update(order);
		LogOrderPay log = logOrderPayDao.getByKey(recharge.getId());
		AccountDetail detail = new AccountDetail(log.getId(), success ? HasanBizType.ORDER_PAY_SUCCESS.mark() : HasanBizType.ORDER_PAY_FAILURE.mark());
		if (log.getBasicAmount().compareTo(BigDecimal.ZERO) > 0) {
			detail.userFrozenDecr(log.getUid(), AccountType.BASIC, log.getBasicAmount());
			if (!success)
				detail.userUsableIncr(log.getUid(), AccountType.BASIC, log.getBasicAmount());
		}
		if (log.getExpAmount().compareTo(BigDecimal.ZERO) > 0) {
			detail.userFrozenDecr(log.getUid(), AccountType.EXP, log.getExpAmount());
			if (!success)
				detail.userUsableIncr(log.getUid(), AccountType.EXP, log.getExpAmount());
		}
		accountService.process(detail);
		return order;
	}
	
	@Transactional
	public void deliver(DeliverParam param) {
		Query query = new Query().eq("id", param.getId()).forUpdate();
		Order order = orderDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		OrderState state = OrderState.match(order.getState());
		Assert.isTrue(HasanCode.ORDER_STATE_ERR, state == OrderState.PAID);
		order.setExpressNo(param.getExpressNo());
		order.setState(OrderState.DELIVERED.mark());
		order.setUpdated(DateUtil.current());
		orderDao.update(order);
	}
	
	@Transactional
	public void receive(SoaSidParam param) {
		Query query = new Query().eq("id", param.getId()).forUpdate();
		Order order = orderDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		Assert.isTrue(CoreCode.FORBID, order.getUid() == param.getUser().getId());
		OrderState state = OrderState.match(order.getState());
		Assert.isTrue(HasanCode.ORDER_STATE_ERR, state == OrderState.DELIVERED);
		order.setState(OrderState.RECEIVED.mark());
		order.setUpdated(DateUtil.current());
		orderDao.update(order);
	}
	
	@Transactional
	public void evaluate(EvaluateParam param) {
		Query query = new Query().eq("id", param.getId()).forUpdate();
		OrderGoods orderGoods = orderGoodsDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, orderGoods);
		Assert.isTrue(HasanCode.ORDER_NOT_EXIST, StringUtil.hasText(orderGoods.getEvaluationId()));
		query = new Query().eq("id", orderGoods.getId()).forUpdate();
		Order order = orderDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		Assert.isTrue(CoreCode.FORBID, order.getUid() == param.getUser().getId());
		OrderState state = OrderState.match(order.getState());
		Assert.isTrue(HasanCode.ORDER_STATE_ERR, state == OrderState.RECEIVED);
		UserEvaluation evaluation = goodsManager.evaluate(param, orderGoods);
		orderGoods.setEvaluationId(evaluation.getId());
		orderGoodsDao.update(orderGoods);
		query = new Query().eq("order_id", orderGoods.getOrderId());
		List<OrderGoods> list = orderGoodsDao.queryList(query);
		boolean allEvaluated = true;
		for (OrderGoods temp : list) {
			if (!StringUtil.hasText(temp.getEvaluationId()))
				allEvaluated = false;
		}
		if (allEvaluated) {
			order.setState(OrderState.FINISH.mark());
			order.setUpdated(DateUtil.current());
			orderDao.update(order);
		}
	}
	
	public Order order(String id) {
		return orderDao.getByKey(id);
	}
	
	public List<Order> orders(Query query) {
		return orderDao.queryList(query);
	}
	
	public List<OrderGoods> orderGoodses(Query query) {
		return orderGoodsDao.queryList(query);
	}
	
	public List<Order> assistantOrders(AssistantOrdersParam param) {
		return orderDao.assistantList(param);
	}
}
