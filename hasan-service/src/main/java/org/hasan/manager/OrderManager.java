package org.hasan.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.user.api.GeoService;
import org.gatlin.soa.user.bean.UserCode;
import org.gatlin.soa.user.bean.entity.UserAddress;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.param.OrderMakeParam;
import org.hasan.mybatis.dao.OrderDao;
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
	
	@Transactional
	public void make(OrderMakeParam param) {
		UserAddress address = geoService.address(param.getAddressId());
		Assert.notNull(UserCode.USER_ADDRESS_NOT_EXIST, address);
		Map<Integer, CfgGoods> goods = goodsManager.buy(param.getGoods());
		UserCustom custom = hasanManager.userCustom(param.getUser().getId());
		Order order = EntityGenerator.newOrder(param, null, address);
	}
	
	public void update(Order order) { 
		orderDao.update(order);
	}
	
	public Order order(String id) {
		return orderDao.getByKey(id);
	}
}
