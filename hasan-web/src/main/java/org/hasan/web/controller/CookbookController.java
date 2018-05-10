package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.hasan.bean.param.CookbookAddParam;
import org.hasan.bean.param.CookbookModifyParam;
import org.hasan.bean.param.CookbookStepAddParam;
import org.hasan.bean.param.CookbookStepModifyParam;
import org.hasan.bean.param.CookbooksParam;
import org.hasan.service.CookbookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("cookbook")
public class CookbookController {
	
	@Resource
	private CookbookService cookbookService;
	
	@ResponseBody
	@RequestMapping("list")
	public Object list(@RequestBody @Valid CookbooksParam param) {
		return cookbookService.cookbooks(param.query());
	}
	
	@ResponseBody
	@RequestMapping("detail")
	public Object detail(@RequestBody @Valid SoaIdParam param) {
		return cookbookService.cookbookDetail(param.getId());
	}
	
	@ResponseBody
	@RequestMapping("add")
	public Object cookbookAdd(@RequestBody @Valid CookbookAddParam param) {
		return cookbookService.cookbookAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("modify")
	public Object cookbookModify(@RequestBody @Valid CookbookModifyParam param) {
		cookbookService.cookbookModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("delete")
	public Object cookbookDelete(@RequestBody @Valid SoaIdParam param) {
		cookbookService.cookbookDelete(param);
		return Response.ok();
	}

	@ResponseBody
	@RequestMapping("cookbook/step/add")
	public Object cookbookStepAdd(@RequestBody @Valid CookbookStepAddParam param) {
		return cookbookService.cookbookStepAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("cookbook/step/modify")
	public Object cookbooStepkModify(@RequestBody @Valid CookbookStepModifyParam param) {
		cookbookService.cookbooStepkModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("cookbook/step/delete")
	public Object cookbookStepDelete(@RequestBody @Valid SoaIdParam param) {
		cookbookService.cookbookStepDelete(param);
		return Response.ok();
	}
}
