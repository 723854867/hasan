package org.hasan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.SoaConsts;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.account.bean.entity.Recharge;
import org.gatlin.soa.account.bean.enums.RechargeState;
import org.gatlin.soa.alipay.api.AlipayAccountService;
import org.gatlin.soa.bean.User;
import org.gatlin.soa.bean.enums.PlatType;
import org.gatlin.soa.bean.enums.TargetType;
import org.gatlin.soa.bean.model.ResourceInfo;
import org.gatlin.soa.bean.param.SoaSidParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.util.DateUtil;
import org.gatlin.util.lang.CollectionUtil;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.LogOrderPay;
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.enums.HasanResourceType;
import org.hasan.bean.model.GoodsInfo;
import org.hasan.bean.model.OrderDetail;
import org.hasan.bean.model.OrderInfo;
import org.hasan.bean.model.OrderPayMethod;
import org.hasan.bean.model.PayPreview;
import org.hasan.bean.param.AssistantOrdersParam;
import org.hasan.bean.param.DeliverParam;
import org.hasan.bean.param.EvaluateParam;
import org.hasan.bean.param.OrderMakeParam;
import org.hasan.bean.param.PayPreviewParam;
import org.hasan.manager.GoodsManager;
import org.hasan.manager.OrderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Resource
	private OrderManager orderManager;
	@Resource
	private GoodsManager goodsManager;
	@Resource
	private ConfigService configService;
	@Resource
	private AccountService accountService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private SchedulerService schedulerService;
	@Resource
	private AlipayAccountService alipayAccountService;

	@Transactional
	public Order make(OrderMakeParam param) {
		return orderManager.make(param);
	}

	@Transactional
	public PayPreview payPreview(PayPreviewParam param) {
		return orderManager.payPreview(param);
	}

	@Transactional
	public OrderPayMethod pay(SoaSidParam param) {
		OrderPayMethod payMethod = new OrderPayMethod();
		LogOrderPay log = orderManager.pay(param);
		payMethod.setDetail(log);
		if (log.getRechargeAmount().compareTo(BigDecimal.ZERO) > 0) {
			Recharge recharge = new Recharge();
			recharge.setId(log.getId());
			User user = param.getUser();
			recharge.setOs(user.getOs());
			recharge.setPlat(PlatType.ALIPAY);
			recharge.setIp(param.meta().getIp());
			recharge.setState(RechargeState.INIT);
			recharge.setGoodsType(101);
			recharge.setGoodsId(log.getOrderId());
			recharge.setRechargee(log.getUid());
			recharge.setRechargeeType(TargetType.USER);
			recharge.setRecharger(log.getUid());
			recharge.setRechargerType(TargetType.USER);
			recharge.setOperator(param.getUser().getId());
			recharge.setFee(log.getExpressFee());
			recharge.setAmount(log.getRechargeAmount());
			int timeout = configService.config(SoaConsts.RECHARGE_TIMEOUT);
			timeout = Math.max(0, timeout);
			int time = DateUtil.current();
			recharge.setExpiry(time + timeout * 60);
			recharge.setCreated(time);
			recharge.setUpdated(time);
			payMethod.setAuth(alipayAccountService.recharge(recharge));
		}
		return payMethod;
	}

	public void deliver(DeliverParam param) {
		orderManager.deliver(param);
	}

	public void receive(SoaSidParam param) {
		orderManager.receive(param);
	}

	public void evaluate(EvaluateParam param) {
		orderManager.evaluate(param);
	}

	public void payTimeoutTask() {
		Query query = new Query().eq("state", RechargeState.INIT.mark()).eq("goods_type", 101).lte("expiry", DateUtil.current());
		List<Recharge> recharges = accountService.recharges(query).getList();
		if (CollectionUtil.isEmpty(recharges))
			return;
		recharges.forEach(recharge -> {
			try {
				accountService.rechargeNotice(recharge.getId(), RechargeState.TIMEOUT);
			} catch (CodeException e) {
				logger.warn("订单支付超时任务业务异常 -{}!", e.code().defaultValue());
			} catch (Exception e) {
				logger.error("订单支付超时任务异常！", e);
			}
		});
	}

	public OrderDetail detail(SoaSidParam param) {
		Order order = orderManager.order(param.getId());
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		List<OrderGoods> orderGoods = orderManager.orderGoodses(new Query().eq("order_id", order.getId()));
		Set<Integer> goodIds = new HashSet<>();
		orderGoods.forEach(item2 -> goodIds.add(item2.getGoodsId()));
		List<CfgGoods> goods = goodsManager.goods(new Query().in("id", goodIds));
		List<ResourceInfo> resources = resourceService
				.resources(new Query().in("owner", goodIds).eq("cfg_id", HasanResourceType.GOODS_ICON.mark()))
				.getList();
		List<GoodsInfo> infos = new ArrayList<GoodsInfo>();
		for (CfgGoods cfgGoods : goods) {
			ResourceInfo icon = null;
			if (!CollectionUtil.isEmpty(resources)) {
				Iterator<ResourceInfo> iterator = resources.iterator();
				while (iterator.hasNext()) {
					ResourceInfo info = iterator.next();
					int owner = Integer.valueOf(info.getOwner());
					if (owner == cfgGoods.getId()) {
						icon = info;
						iterator.remove();
						break;
					}
				}
			}
			infos.add(new GoodsInfo(cfgGoods, icon));
		}

		return new OrderDetail(order, infos, orderGoods);
	}

	public Pager<OrderInfo> orders(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		List<Order> list = orderManager.orders(query);
		List<OrderInfo> orderInfos = new ArrayList<>();
		list.forEach(item -> {
			List<OrderGoods> orderGoods = orderManager.orderGoodses(new Query().eq("order_id", item.getId()));
			Set<Integer> goodIds = new HashSet<>();
			orderGoods.forEach(item2 -> goodIds.add(item2.getGoodsId()));
			List<CfgGoods> goods = goodsManager.goods(new Query().in("id", goodIds));
			List<ResourceInfo> resources = resourceService
					.resources(new Query().in("owner", goodIds).eq("cfg_id", HasanResourceType.GOODS_ICON.mark()))
					.getList();
			List<GoodsInfo> infos = new ArrayList<GoodsInfo>();
			for (CfgGoods cfgGoods : goods) {
				ResourceInfo icon = null;
				if (!CollectionUtil.isEmpty(resources)) {
					Iterator<ResourceInfo> iterator = resources.iterator();
					while (iterator.hasNext()) {
						ResourceInfo info = iterator.next();
						int owner = Integer.valueOf(info.getOwner());
						if (owner == cfgGoods.getId()) {
							icon = info;
							iterator.remove();
							break;
						}
					}
				}
				infos.add(new GoodsInfo(cfgGoods, icon));
			}
			orderInfos.add(new OrderInfo(item, infos, orderGoods));
		});
		return new Pager<OrderInfo>(orderInfos);
	}

	public Pager<Order> assistantOrders(AssistantOrdersParam param) {
		if (null != param.getPage())
			PageHelper.startPage(param.getPage(), param.getPageSize());
		return new Pager<Order>(orderManager.assistantOrders(param));
	}
}
