package org.hasan.service;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.bean.User;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.courier.api.EmailService;
import org.gatlin.soa.courier.api.SmsService;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.UserCode;
import org.gatlin.soa.user.bean.UserUtil;
import org.gatlin.soa.user.bean.entity.UserDevice;
import org.gatlin.soa.user.bean.entity.UserInvitation;
import org.gatlin.soa.user.bean.enums.UsernameType;
import org.gatlin.soa.user.bean.info.LoginInfo;
import org.gatlin.soa.user.bean.model.LoginModel;
import org.gatlin.soa.user.bean.model.RegisterModel;
import org.gatlin.soa.user.bean.param.LoginParam;
import org.gatlin.soa.user.bean.param.PwdModifyParam;
import org.gatlin.soa.user.bean.param.PwdResetParam;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.gatlin.web.util.WebConsts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonService {

	@Resource
	private SmsService smsService;
	@Resource
	private UserService userService;
	@Resource	
	private EmailService emailService;
	@Resource
	private ConfigService configService;
	@Resource
	private AccountService accountService;
	
	public LoginInfo login(LoginParam param) {
		LoginModel model = userService.login(param);
		UserDevice odevice = model.getOdevice();
		if (null != odevice) {				// 需要判断老设备和新设备是否是同一个设备，如果不是同一个设备则需要给老设备推送离线通知
			// TODO: 换设备登录推送
		}
		return model.getInfo();
	}
	
	@Transactional
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
		int accountMod = configService.config(WebConsts.Options.ACCOUNT_MOD);
		if (0 != accountMod)
			accountService.init(model.getUid(), accountMod);
		UserInvitation invitation = model.getInvitation();
		if (null != invitation) {
			// TODO:处理邀请
		}
		return model.getUid();
	}
	
	public void pwdReset(PwdResetParam param) {
		User user = userService.user(param.getUsernameType(), param.getUsername());
		Assert.notNull(UserCode.USERNAME_NOT_EXIST, user);
		switch (param.getUsernameType()) {
		case EMAIL:
			emailService.captchaVerify(param.getUsername(), param.getCaptcha());
			break;
		case MOBILE:
			smsService.captchaVerify(param.getUsername(), param.getCaptcha());
			break;
		case COMMON:
			String cpwd = UserUtil.pwd(param.getOpwd(), param.getUser().getSalt());
			Assert.isTrue(UserCode.LOGIN_PWD_ERROR, cpwd.equalsIgnoreCase(param.getUser().getPwd()));
			break;
		default:
			throw new CodeException(CoreCode.FORBID);
		}
		user.setPwd(UserUtil.pwd(param.getPwd(), user.getSalt()));
		userService.update(user);
	}
	
	public void pwdModify(PwdModifyParam param) {
		User user = param.getUser();
		String cpwd = UserUtil.pwd(param.getOpwd(), param.getUser().getSalt());
		Assert.isTrue(UserCode.LOGIN_PWD_ERROR, cpwd.equalsIgnoreCase(param.getUser().getPwd()));
		user.setPwd(UserUtil.pwd(param.getPwd(), param.getUser().getSalt()));
		userService.update(user);
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
