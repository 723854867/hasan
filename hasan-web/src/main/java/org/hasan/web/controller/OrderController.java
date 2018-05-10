package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaSidParam;
import org.hasan.bean.param.EvaluateParam;
import org.hasan.bean.param.OrderMakeParam;
import org.hasan.bean.param.OrdersParam;
import org.hasan.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("order")
public class OrderController {
	
	@Resource
	private OrderService orderService;

	@ResponseBody
	@RequestMapping("make")
	public Object make(@RequestBody @Valid OrderMakeParam param) {
		return orderService.make(param);
	}
	
	// 发货
	@ResponseBody
	@RequestMapping("deliver")
	public Object deliver(@RequestBody @Valid SoaSidParam param) {
		orderService.deliver(param.getId());
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("receive")
	public Object receive(@RequestBody @Valid SoaSidParam param) {
		orderService.receive(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("evaluate")
	public Object evaluate(@RequestBody @Valid EvaluateParam param) {
		orderService.evaluate(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("list")
	public Object list(@RequestBody @Valid OrdersParam param) {
		Query query = new Query().eq("uid", param.getUser().getId());
		if (null != param.getState())
			query.eq("state", param.getState().mark());
		query.orderByDesc("created");
		return orderService.orders(query);
	}
	
	@ResponseBody
	@RequestMapping("list/all")
	public Object listAll(@RequestBody @Valid OrdersParam param) {
		return orderService.orders(param.query());
	}
	
	@ResponseBody
	@RequestMapping("detail")
	public Object detail(@RequestBody @Valid SoaSidParam param) {
		return orderService.detail(param);
	}
}
