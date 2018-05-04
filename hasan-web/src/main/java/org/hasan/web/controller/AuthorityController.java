package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.soa.authority.api.AuthService;
import org.gatlin.soa.authority.bean.param.ApiAddParam;
import org.gatlin.soa.authority.bean.param.ApiModifyParam;
import org.gatlin.soa.authority.bean.param.ApisParam;
import org.gatlin.soa.authority.bean.param.ModularAddParam;
import org.gatlin.soa.authority.bean.param.NameIdParam;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.soa.bean.param.SoaSidParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("authority")
public class AuthorityController {
	
	@Resource
	private AuthService authService;
	
	@ResponseBody
	@RequestMapping("api/list")
	public Object apis(ApisParam param) {
		return authService.apis(param.query());
	}

	@ResponseBody
	@RequestMapping("api/add")
	public Object apiAdd(@RequestBody @Valid ApiAddParam param) {
		return authService.apiAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("api/modify")
	public Object apiAdd(@RequestBody @Valid ApiModifyParam param) {
		authService.apiModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("api/delete")
	public Object apiDelete(@RequestBody @Valid SoaIdParam param) {
		authService.apiDelete(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("modular/add")
	public Object modularAdd(@RequestBody @Valid ModularAddParam param) {
		return authService.modularAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("modular/modify")
	public Object modularModify(@RequestBody @Valid NameIdParam param) {
		authService.modularModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("modular/delete")
	public Object modularDelete(@RequestBody @Valid SoaIdParam param) {
		return authService.modularDelete(param);
	}
	
	@ResponseBody
	@RequestMapping("role/list")
	public Object roles(@RequestBody @Valid SoaParam param) {
		return authService.roles(param.query());
	}
	
	@ResponseBody
	@RequestMapping("role/add")
	public Object roleAdd(@RequestBody @Valid SoaSidParam param) {
		return authService.roleAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("role/modify")
	public Object roleModify(@RequestBody @Valid NameIdParam param) {
		authService.roleModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("role/delete")
	public Object roleDelete(@RequestBody @Valid SoaIdParam param) {
		authService.roleDelete(param);
		return Response.ok();
	}
}
