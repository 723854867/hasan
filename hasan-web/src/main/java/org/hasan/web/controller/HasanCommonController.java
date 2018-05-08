package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.MemberModifyParam;
import org.hasan.bean.param.MembersParam;
import org.hasan.service.CommonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("common")
public class HasanCommonController {
	
	@Resource
	private UserService userService;
	@Resource
	private CommonService commonService;
	@Resource
	private ResourceService resourceService;

	@ResponseBody
	@RequestMapping("register")
	public Object login(@RequestBody @Valid RegisterParam param) {
		return commonService.register(param);
	}
	
	@ResponseBody
	@RequestMapping("members")
	public Object members(@RequestBody @Valid MembersParam param) {
		return commonService.members(param.query());
	}
	
	@ResponseBody
	@RequestMapping("member/add")
	public Object memberAdd(@RequestBody @Valid MemberAddParam param) {
		return commonService.memberAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("member/modify")
	public Object memberModify(@RequestBody @Valid MemberModifyParam param) {
		commonService.memberModify(param);
		return Response.ok();
	}
}
