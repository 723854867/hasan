package org.hasan.service;

import javax.annotation.Resource;

import org.hasan.bean.param.OrderMakeParam;
import org.hasan.manager.OrderManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	@Resource
	private OrderManager orderManager;
	@Resource
	private SchedulerService schedulerService;
	
	@Transactional
	public void make(OrderMakeParam param) {
		
	}
}
