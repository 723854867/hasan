package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.soa.bean.param.SoaParam;
import org.hasan.service.CommonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("task")
public class TaskController {
	
	@Resource
	private CommonService commonService;

	@ResponseBody
	@RequestMapping("daily")
	public Object add(@RequestBody @Valid SoaParam param) {
		commonService.dailyTask();
		return Response.ok();
	}
}
