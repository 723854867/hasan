package org.hasan.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.account.bean.param.RechargeParam;
import org.gatlin.soa.bean.param.SoaSidParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.model.ResourceInfo;
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
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.entity.UserEvaluation;
import org.hasan.bean.enums.HasanResourceType;
import org.hasan.bean.enums.MemberType;
import org.hasan.bean.enums.OrderState;
import org.hasan.bean.param.AssistantOrdersParam;
import org.hasan.bean.param.EvaluateParam;
import org.hasan.bean.param.OrderMakeParam;
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
	private ResourceService resourceService;
	
	// 下订单
	@Transactional
	public Order make(OrderMakeParam param) {
		UserAddress address = geoService.address(param.getAddressId());
		Assert.notNull(UserCode.USER_ADDRESS_NOT_EXIST, address);
		Map<Integer, CfgGoods> goods = goodsManager.buy(param.getGoods());
		UserCustom custom = hasanManager.userCustom(param.getUser().getId());
		String orderId = IDWorker.INSTANCE.nextSid();
		Query query = new Query().in("owner", goods.keySet()).eq("cfg_id", HasanResourceType.GOODS_ICON.mark());
		List<ResourceInfo> resources = resourceService.resources(query).getList();
		List<OrderGoods> list = EntityGenerator.newOrderGoods(orderId, param.getGoods(), goods, resources, custom);
		BigDecimal price = BigDecimal.ZERO;
		for (OrderGoods temp : list)
			price = price.add(temp.getUnitPrice().multiply(BigDecimal.valueOf(temp.getGoodsNum())));
		Order order = EntityGenerator.newOrder(orderId, param, price, address);
		orderDao.insert(order);
		orderGoodsDao.batchInsert(list);
		return order;
	}
	
	// 支付订单
	@Transactional
	public Order pay(RechargeParam param) {
		Query query = new Query().eq("id", param.getGoodsId()).forUpdate();
		Order order = orderDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		Assert.isTrue(CoreCode.FORBID, order.getUid() == param.getUser().getId());
		OrderState state = OrderState.match(order.getState());
		Assert.isTrue(HasanCode.ORDER_STATE_ERR, state == OrderState.INIT);
		UserCustom custom = hasanManager.userCustom(order.getUid());
		MemberType type = MemberType.match(custom.getMemberType());
		if (type == MemberType.ORIGINAL) 
			order.setExpressFee(configService.config(HasanConsts.EXPRESS_FEE));
		order.setState(OrderState.PAYING.mark());
		order.setUpdated(DateUtil.current());
		orderDao.update(order);
		return order;
	}
	
	@Transactional
	public Order payNotice(String orderId, boolean success) {
		Query query = new Query().eq("id", orderId).forUpdate();
		Order order = orderDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		OrderState state = OrderState.match(order.getState());
		Assert.isTrue(HasanCode.ORDER_STATE_ERR, state == OrderState.PAYING);
		order.setState(success ? OrderState.PAID.mark() : OrderState.INIT.mark());
		order.setUpdated(DateUtil.current());
		orderDao.update(order);
		return order;
	}
	
	@Transactional
	public void deliver(String orderId) {
		Query query = new Query().eq("id", orderId).forUpdate();
		Order order = orderDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		OrderState state = OrderState.match(order.getState());
		Assert.isTrue(HasanCode.ORDER_STATE_ERR, state == OrderState.PAID);
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
