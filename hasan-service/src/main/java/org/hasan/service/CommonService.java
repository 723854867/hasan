package org.hasan.service;

import javax.annotation.Resource;

import org.gatlin.core.GatlinConfigration;
import org.gatlin.core.bean.info.Pager;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.courier.api.EmailService;
import org.gatlin.soa.courier.api.SmsService;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.entity.UserInvitation;
import org.gatlin.soa.user.bean.model.RegisterModel;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.gatlin.web.util.WebConsts;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.MemberModifyParam;
import org.hasan.manager.CommonManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

@Service
public class CommonService {

	@Resource
	private SmsService smsService;
	@Resource
	private UserService userService;
	@Resource	
	private EmailService emailService;
	@Resource
	private CommonManager commonManager;
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
	
	public int memberAdd(MemberAddParam param) {
		return commonManager.memberAdd(param);
	}
	
	public void memberModify(MemberModifyParam param) {
		commonManager.memberModify(param);
	}
	
	public Pager<CfgMember> members(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		return new Pager<CfgMember>(commonManager.members(query));
	}
}
