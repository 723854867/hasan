package org.hasan.manager;

import javax.annotation.Resource;

import org.hasan.bean.entity.Order;
import org.hasan.bean.param.OrderMakeParam;
import org.hasan.mybatis.dao.OrderDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderManager {

	@Resource
	private OrderDao orderDao;
	
	@Transactional
	public void make(OrderMakeParam param) {
	}
	
	public Order order(String id) {
		return orderDao.getByKey(id);
	}
}
