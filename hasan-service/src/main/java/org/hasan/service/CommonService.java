package org.hasan.service;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.soa.courier.api.EmailService;
import org.gatlin.soa.courier.api.SmsService;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.entity.UserDevice;
import org.gatlin.soa.user.bean.entity.UserInvitation;
import org.gatlin.soa.user.bean.enums.UsernameType;
import org.gatlin.soa.user.bean.info.LoginInfo;
import org.gatlin.soa.user.bean.info.UserTips;
import org.gatlin.soa.user.bean.model.LoginModel;
import org.gatlin.soa.user.bean.model.RegisterModel;
import org.gatlin.soa.user.bean.param.LoginParam;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

	@Resource
	private SmsService smsService;
	@Resource
	private UserService userService;
	@Resource	
	private EmailService emailService;
	
	public LoginInfo login(LoginParam param) {
		LoginModel model = userService.login(param);
		UserDevice odevice = model.getOdevice();
		if (null != odevice) {				// 需要判断老设备和新设备是否是同一个设备，如果不是同一个设备则需要给老设备推送离线通知
			// TODO: 换设备登录推送
		}
		return model.getInfo();
	}
	
	public long register(RegisterParam param) { 
		switch (param.getUsernameType()) {
		case EMAIL:
			emailService.captchaVerify(param.getUsername(), param.getCaptcha());
			break;
		case MOBILE:
			smsService.captchaVerify(param.getUsername(), param.getCaptcha());
			break;
		default:
			break;
		}
		RegisterModel model = userService.register(param);
		UserInvitation invitation = model.getInvitation();
		if (null != invitation) {
			// TODO:处理邀请
		}
		return model.getUid();
	}
	
	public UserTips userTips(String username, UsernameType type) {
		return userService.user(type, username);
	}
	
	public String captchaObtain(String username, UsernameType type) { 
		switch (type) {
		case EMAIL:
			return emailService.captchaAcquire(username);
		case MOBILE:
			return smsService.captchaAcquire(username);
		default:
			throw new CodeException(CoreCode.FORBID);
		}
	}
}
