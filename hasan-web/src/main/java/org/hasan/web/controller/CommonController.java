package org.hasan.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.User;
import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.config.bean.entity.CfgGlobal;
import org.gatlin.soa.config.bean.model.Configs;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.enums.UserMod;
import org.gatlin.soa.user.bean.param.LoginParam;
import org.gatlin.soa.user.bean.param.PwdModifyParam;
import org.gatlin.soa.user.bean.param.PwdResetParam;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.gatlin.soa.user.bean.param.UsernameParam;
import org.gatlin.web.util.bean.info.UserTips;
import org.hasan.service.CommonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("common")
public class CommonController {
	
	@Resource
	private UserService userService;
	@Resource
	private ConfigService configService;
	@Resource
	private CommonService commonService;
	@Resource
	private ResourceService resourceService;

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
	@RequestMapping("user/mod")
	public Object userMod(@RequestBody @Valid UsernameParam param) {
		User user = userService.user(param.getUsernameType(), param.getUsername());
		int mod = 0;
		if (null == user)
			return mod;
		mod |= UserMod.EXIST.mark();
		return mod;
	}
	
	@ResponseBody
	@RequestMapping("user/tips")
	public Object userTips(@RequestBody @Valid SoaParam param) {
		User user = param.getUser();
		Query query = new Query().eq("cfg_id", 50).eq("owner", user.getId());
		return new UserTips(user, resourceService.resource(query));
	}
	
	@ResponseBody
	@RequestMapping("pwd/reset")
	public Object pwdReset(@RequestBody @Valid PwdResetParam param) {
		commonService.pwdReset(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("pwd/modify")
	public Object pwdModify(@RequestBody @Valid PwdModifyParam param) {
		commonService.pwdModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("captcha/obtain")
	public Object captchaObtain(@RequestBody @Valid UsernameParam param) {
		return commonService.captchaObtain(param.getUsername(), param.getUsernameType());
	}
	
	@ResponseBody
	@RequestMapping("configs")
	public Object configs(@RequestBody @Valid SoaParam param) {
		Configs configs = configService.configs(new Query());
		Map<String, String> map = new HashMap<String, String>();
		for (Entry<String, CfgGlobal> entry : configs.getGlobals().entrySet()) 
			map.put(entry.getKey(), entry.getValue().getValue());
		return map;
	}
	
	@ResponseBody
	@RequestMapping("configs/visible")
	public Object configsVisible(@RequestBody @Valid SoaParam param) {
		Configs configs = configService.configs(new Query().eq("visible", 1));
		Map<String, String> map = new HashMap<String, String>();
		for (Entry<String, CfgGlobal> entry : configs.getGlobals().entrySet()) 
			map.put(entry.getKey(), entry.getValue().getValue());
		return map;
	}
}
