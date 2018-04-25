package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.user.bean.param.LoginParam;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.gatlin.soa.user.bean.param.UsernameParam;
import org.hasan.service.CommonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("common")
public class CommonController {
	
	@Resource
	private ConfigService configService;
	@Resource
	private CommonService commonService;

	@ResponseBody
	@RequestMapping("login")
	public Object login(@RequestBody @Valid LoginParam param) {
		return commonService.login(param);
	}
	
	@ResponseBody
	@RequestMapping("register")
	public Object login(@RequestBody @Valid RegisterParam param) {
		return commonService.register(param);
	}

	@ResponseBody
	@RequestMapping("user/tips")
	public Object userTips(@RequestBody @Valid UsernameParam param) {
		return commonService.userTips(param.getUsername(), param.getUsernameType());
	}
	
	@ResponseBody
	@RequestMapping("captcha/obtain")
	public Object captchaObtain(@RequestBody @Valid UsernameParam param) {
		return commonService.captchaObtain(param.getUsername(), param.getUsernameType());
	}
	
	@ResponseBody
	@RequestMapping("configs")
	public Object configs(@RequestBody @Valid SoaParam param) {
		return configService.configs(new Query().eq("visible", 1));
	}
}
