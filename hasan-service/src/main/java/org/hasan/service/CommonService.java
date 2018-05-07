package org.hasan.service;

import javax.annotation.Resource;

import org.gatlin.core.Gatlin;
import org.gatlin.core.GatlinConfigration;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.courier.api.EmailService;
import org.gatlin.soa.courier.api.SmsService;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.entity.UserInvitation;
import org.gatlin.soa.user.bean.model.RegisterModel;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.gatlin.web.util.WebConsts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonService {

	@Resource
	private Gatlin gatlin;
	@Resource
	private SmsService smsService;
	@Resource
	private UserService userService;
	@Resource	
	private EmailService emailService;
	@Resource
	private AccountService accountService;
	
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
		int accountMod = GatlinConfigration.get(WebConsts.Options.ACCOUNT_MOD);
		if (0 != accountMod)
			accountService.init(model.getUid(), accountMod);
		UserInvitation invitation = model.getInvitation();
		if (null != invitation) {
			// TODO:处理邀请
		}
		return model.getUid();
	}
}
