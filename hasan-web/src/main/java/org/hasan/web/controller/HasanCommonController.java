package org.hasan.web.controller;

import java.util.Iterator;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.bean.model.message.Response;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.account.bean.entity.Account;
import org.gatlin.soa.account.bean.enums.AccountType;
import org.gatlin.soa.bean.User;
import org.gatlin.soa.bean.model.ResourceInfo;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaLidParam;
import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.enums.ResourceType;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.gatlin.util.bean.enums.DeviceType;
import org.gatlin.util.lang.CollectionUtil;
import org.hasan.bean.HasanConsts;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.model.Wallet;
import org.hasan.bean.param.AssistantAllocateParam;
import org.hasan.bean.param.AssistantUserListParam;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.MemberModifyParam;
import org.hasan.bean.param.MembersParam;
import org.hasan.bean.param.VersesParam;
import org.hasan.manager.HasanManager;
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
	private HasanManager hasanManager;
	@Resource
	private CommonService commonService;
	@Resource
	private ConfigService configService;
	@Resource
	private AccountService accountService;
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
		Pager<CfgMember> pager = commonService.members(param.query());
		if (null != param.getUser() && !CollectionUtil.isEmpty(pager.getList())) {
			DeviceType type = param.getUser().getDeviceType();
			if (type != DeviceType.PC) {
				Iterator<CfgMember> iterator = pager.getList().iterator();
				while (iterator.hasNext()) {
					CfgMember member = iterator.next();
					if (!hasanManager.checkMemberCount(param.getUser().getId(), member))
						iterator.remove();
				}
			}
		}
		return pager;
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

	@ResponseBody
	@RequestMapping("member/buy")
	public Object memberBuy(@RequestBody @Valid SoaIdParam param) {
		return commonService.memberBuy(param);
	}
	
	@ResponseBody
	@RequestMapping("wallet")
	public Object wallet(@RequestBody @Valid SoaParam param) {
		User user = param.getUser();
		Query query = new Query().eq("cfg_id", ResourceType.AVATAR.mark()).eq("owner", user.getId());
		ResourceInfo resource = resourceService.resource(query);
		UserCustom custom = hasanManager.userCustom(user.getId());
		Account account = accountService.userAccount(user.getId(), AccountType.BASIC);
		CfgMember member = 0 != custom.getMemberId() ? hasanManager.member(custom.getMemberId()) : null;
		String memberTitle = null == member ? configService.config(HasanConsts.DEFAULT_MEMBER_TITLE) : member.getName();
		return new Wallet(user, resource, account, custom, memberTitle);
	}

	@ResponseBody
	@RequestMapping("assistant/allocate")
	public Object assistantAllocate(@RequestBody @Valid AssistantAllocateParam param) {
		commonService.assistantAllocate(param);
		return Response.ok();
	}

	@ResponseBody
	@RequestMapping("assistant/delete")
	public Object assistantDelete(@RequestBody @Valid SoaLidParam param) {
		commonService.assistantDelete(param);
		return Response.ok();
	}

	// 客服用户列表
	@ResponseBody
	@RequestMapping("assistant/users")
	public Object assistantUsers(@RequestBody @Valid AssistantUserListParam param) {
		if (null == param.getAssistant())
			param.setAssistant(param.getUser().getId());
		return commonService.assistantUsers(param);
	}

	// 可分配用户列表
	@ResponseBody
	@RequestMapping("allocatable/users")
	public Object allocatableUsers(@RequestBody @Valid AssistantUserListParam param) {
		return commonService.allocatableUsers(param);
	}
	
	@ResponseBody
	@RequestMapping("verse/add")
	public Object verseAdd(@RequestBody @Valid VersesParam param) {
		return commonService.verseAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("verse/modify")
	public Object verseModify(@RequestBody @Valid VersesParam param) {
		commonService.verseModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("verse/delete")
	public Object verseDelete(@RequestBody @Valid SoaIdParam param) {
		commonService.verseDelete(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("verses")
	public Object verses(@RequestBody @Valid SoaParam param) {
		return commonService.verses(param.query());
	}
	
	@ResponseBody
	@RequestMapping("guide")
	public Object guid(@RequestBody @Valid SoaParam param) {
		return commonService.guide();
	}
}
