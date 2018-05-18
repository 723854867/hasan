package org.hasan.service;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.gatlin.core.GatlinConfigration;
import org.gatlin.core.bean.info.Pager;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.bean.param.SoaLidParam;
import org.gatlin.soa.courier.api.EmailService;
import org.gatlin.soa.courier.api.SmsService;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.entity.UserInvitation;
import org.gatlin.soa.user.bean.model.RegisterModel;
import org.gatlin.soa.user.bean.model.UserListInfo;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.gatlin.web.WebConsts;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.param.AssistantAllocateParam;
import org.hasan.bean.param.AssistantUserListParam;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.MemberModifyParam;
import org.hasan.manager.HasanManager;
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
	private HasanManager hasanManager;
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
		hasanManager.register(model.getUid());
		int accountMod = GatlinConfigration.get(WebConsts.Options.ACCOUNT_MOD_USER);
		if (0 != accountMod)
			accountService.userCreate(model.getUid(), accountMod);
		UserInvitation invitation = model.getInvitation();
		if (null != invitation) {
			// TODO:处理邀请
		}
		return model.getUid();
	}
	
	public int memberAdd(MemberAddParam param) {
		return hasanManager.memberAdd(param);
	}
	
	public void memberModify(MemberModifyParam param) {
		hasanManager.memberModify(param);
	}
	
	public Pager<CfgMember> members(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		return new Pager<CfgMember>(new ArrayList<CfgMember>(hasanManager.members().values()));
	}
	
	public void assistantAllocate(AssistantAllocateParam param) {
		hasanManager.assistantAllocate(param);
	}
	
	public void assistantDelete(SoaLidParam param) {
		hasanManager.assistantDelete(param);
	}
	
	public Pager<UserListInfo> assistantUsers(AssistantUserListParam param) {
		if (null != param.getPage())
			PageHelper.startPage(param.getPage(), param.getPageSize());
		return new Pager<UserListInfo>(hasanManager.assistantUsers(param));
	}
	
	public Pager<UserListInfo> allocatableUsers(AssistantUserListParam param) {
		if (null != param.getPage())
			PageHelper.startPage(param.getPage(), param.getPageSize());
		return new Pager<UserListInfo>(hasanManager.allocatableUsers(param));
	}
}
