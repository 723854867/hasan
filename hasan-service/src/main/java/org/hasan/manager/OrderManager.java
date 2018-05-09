package org.hasan.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.account.bean.param.RechargeParam;
import org.gatlin.soa.user.api.GeoService;
import org.gatlin.soa.user.bean.UserCode;
import org.gatlin.soa.user.bean.entity.UserAddress;
import org.gatlin.util.DateUtil;
import org.gatlin.util.IDWorker;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.enums.OrderState;
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
	
	// 下订单
	@Transactional
	public void make(OrderMakeParam param) {
		UserAddress address = geoService.address(param.getAddressId());
		Assert.notNull(UserCode.USER_ADDRESS_NOT_EXIST, address);
		Map<Integer, CfgGoods> goods = goodsManager.buy(param.getGoods());
		UserCustom custom = hasanManager.userCustom(param.getUser().getId());
		String orderId = IDWorker.INSTANCE.nextSid();
		List<OrderGoods> list = EntityGenerator.newOrderGoods(orderId, param.getGoods(), goods, custom);
		BigDecimal price = BigDecimal.ZERO;
		for (OrderGoods temp : list)
			price = price.add(temp.getUnitPrice().multiply(BigDecimal.valueOf(temp.getGoodsNum())));
		Order order = EntityGenerator.newOrder(orderId, param, price, address);
		orderDao.insert(order);
		orderGoodsDao.batchInsert(list);
	}
	
	// 支付订单
	@Transactional
	public Order pay(RechargeParam param) {
		Query query = new Query().eq("id", param.getGoodsId()).forUpdate();
		Order order = orderDao.queryUnique(query);
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		OrderState state = OrderState.match(order.getState());
		Assert.isTrue(HasanCode.ORDER_STATE_ERR, state == OrderState.INIT);
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
	
	public void update(Order order) { 
		orderDao.update(order);
	}
}
