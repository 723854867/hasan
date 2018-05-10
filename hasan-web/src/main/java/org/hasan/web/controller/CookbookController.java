package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaParam;
import org.hasan.bean.param.CookbookAddParam;
import org.hasan.bean.param.CookbookModifyParam;
import org.hasan.bean.param.CookbookStepAddParam;
import org.hasan.bean.param.CookbookStepModifyParam;
import org.hasan.bean.param.CuisineAddParam;
import org.hasan.bean.param.CuisineCategoryAddParam;
import org.hasan.bean.param.CuisineCategoryModifyParam;
import org.hasan.bean.param.CuisineModifyParam;
import org.hasan.bean.param.CuisinesParam;
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
	@RequestMapping("cuisine/categories")
	public Object cuisineCategories(@RequestBody @Valid SoaParam param) {
		return cookbookService.cuisineCategories();
	}

	@ResponseBody
	@RequestMapping("cuisine/category/add")
	public Object cuisineCategoryAdd(@RequestBody @Valid CuisineCategoryAddParam param) {
		return cookbookService.cuisineCategoryAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("cuisine/category/modify")
	public Object cuisineCategoryModify(@RequestBody @Valid CuisineCategoryModifyParam param) {
		cookbookService.cuisineCategoryModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("cuisine/category/delete")
	public Object cuisineCategoryDelete(@RequestBody @Valid SoaIdParam param) {
		cookbookService.cuisineCategoryDelete(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("cuisines")
	public Object cuisines(@RequestBody @Valid CuisinesParam param) {
		return cookbookService.cuisines(param.query());
	}

	@ResponseBody
	@RequestMapping("cuisine/add")
	public Object cuisineAdd(@RequestBody @Valid CuisineAddParam param) {
		return cookbookService.cuisineAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("cuisine/modify")
	public Object cuisineModify(@RequestBody @Valid CuisineModifyParam param) {
		cookbookService.cuisineModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("cuisine/delete")
	public Object cuisineDelete(@RequestBody @Valid SoaIdParam param) {
		cookbookService.cuisineDelete(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("cookbooks")
	public Object cookbooks(@RequestBody @Valid SoaParam param) {
		return cookbookService.cookbooks(param.query());
	}
	
	@ResponseBody
	@RequestMapping("cookbook/add")
	public Object cookbookAdd(@RequestBody @Valid CookbookAddParam param) {
		return cookbookService.cookbookAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("cookbook/modify")
	public Object cookbookModify(@RequestBody @Valid CookbookModifyParam param) {
		cookbookService.cookbookModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("cookbook/delete")
	public Object cookbookDelete(@RequestBody @Valid SoaIdParam param) {
		cookbookService.cuisineDelete(param);
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
