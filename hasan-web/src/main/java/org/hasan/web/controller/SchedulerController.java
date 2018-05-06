package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaParam;
import org.hasan.bean.param.SchedulerAddParam;
import org.hasan.bean.param.SchedulerModifyParam;
import org.hasan.service.SchedulerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("scheduler")
public class SchedulerController {
	
	@Resource
	private SchedulerService schedulerService;

	@ResponseBody
	@RequestMapping("add")
	public Object add(@RequestBody @Valid SchedulerAddParam param) {
		return schedulerService.add(param);
	}
	
	@ResponseBody
	@RequestMapping("modify")
	public Object modify(@RequestBody @Valid SchedulerModifyParam param) {
		schedulerService.modify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("delete")
	public Object delete(@RequestBody @Valid SoaIdParam param) {
		schedulerService.delete(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("list")
	public Object list(@RequestBody @Valid SoaParam param) {
		return schedulerService.schedulers(new Query());
	}
}
