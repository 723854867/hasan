package org.hasan.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.model.ResourceInfo;
import org.gatlin.soa.bean.param.SoaSidParam;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.util.lang.CollectionUtil;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.enums.HasanResourceType;
import org.hasan.bean.model.GoodsInfo;
import org.hasan.bean.model.OrderDetail;
import org.hasan.bean.model.OrderInfo;
import org.hasan.bean.param.AssistantOrdersParam;
import org.hasan.bean.param.EvaluateParam;
import org.hasan.bean.param.OrderMakeParam;
import org.hasan.manager.GoodsManager;
import org.hasan.manager.OrderManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

@Service
public class OrderService {

	@Resource
	private OrderManager orderManager;
	@Resource
	private GoodsManager goodsManager;
	@Resource
	private ResourceService resourceService;
	@Resource
	private SchedulerService schedulerService;
	
	@Transactional
	public Order make(OrderMakeParam param) {
		return orderManager.make(param);
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
		List<OrderGoods> orderGoods = orderManager.orderGoodses(new Query().eq("order_id", order.getId()));
		Set<Integer> goodIds = new HashSet<>();
		orderGoods.forEach(item2 -> goodIds.add(item2.getGoodsId()));
		List<CfgGoods> goods = goodsManager.goods(new Query().in("id", goodIds));
		List<ResourceInfo> resources = resourceService.resources(
				new Query().in("owner", goodIds).eq("cfg_id", HasanResourceType.GOODS_ICON.mark())).getList();
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
		
		return new OrderDetail(order, infos,orderGoods);
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
			List<ResourceInfo> resources = resourceService.resources(
					new Query().in("owner", goodIds).eq("cfg_id", HasanResourceType.GOODS_ICON.mark())).getList();
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
