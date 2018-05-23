package org.hasan.service;

import java.util.List;

import javax.annotation.Resource;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaSidParam;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.model.OrderDetail;
import org.hasan.bean.param.AssistantOrdersParam;
import org.hasan.bean.param.EvaluateParam;
import org.hasan.bean.param.OrderMakeParam;
import org.hasan.manager.OrderManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

@Service
public class OrderService {

	@Resource
	private OrderManager orderManager;
	@Resource
	private SchedulerService schedulerService;
	
	@Transactional
	public Order make(OrderMakeParam param) {
		return orderManager.make(param);
	}
	
	@Transactional
	public void payBalance(SoaSidParam param) {
		
	}
	
	public void deliver(String orderId) {
		orderManager.deliver(orderId);
	}
	
	public void receive(SoaSidParam param) {
		orderManager.receive(param);
	}
	
	public void evaluate(EvaluateParam param) {
		orderManager.evaluate(param);
	}
	
	public OrderDetail detail(SoaSidParam param) {
		Order order = orderManager.order(param.getId());
		Assert.notNull(HasanCode.ORDER_NOT_EXIST, order);
		List<OrderGoods> goods = orderManager.orderGoodses(new Query().eq("order_id", order.getId()));
		return new OrderDetail(order, goods);
	}
	
	public Pager<Order> orders(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		return new Pager<Order>(orderManager.orders(query));
	}
	
	public Pager<Order> assistantOrders(AssistantOrdersParam param) {
		if (null != param.getPage())
			PageHelper.startPage(param.getPage(), param.getPageSize());
		return new Pager<Order>(orderManager.assistantOrders(param));
	}
}
