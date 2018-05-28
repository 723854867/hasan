package org.hasan.service;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.GatlinConfigration;
import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.sdk.jisu.request.CalendarRequest;
import org.gatlin.sdk.jisu.request.JieQiRequest;
import org.gatlin.sdk.jisu.result.Calendar;
import org.gatlin.sdk.jisu.result.JieQi;
import org.gatlin.soa.SoaConsts;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.account.bean.AccountUtil;
import org.gatlin.soa.account.bean.entity.Recharge;
import org.gatlin.soa.alipay.api.AlipayAccountService;
import org.gatlin.soa.bean.enums.PlatType;
import org.gatlin.soa.bean.enums.TargetType;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaLidParam;
import org.gatlin.soa.bean.param.SoaNameIdParam;
import org.gatlin.soa.bean.param.SoaSidParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.config.bean.entity.CfgGlobal;
import org.gatlin.soa.courier.api.EmailService;
import org.gatlin.soa.courier.api.SmsService;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.entity.UserInvitation;
import org.gatlin.soa.user.bean.model.RegisterModel;
import org.gatlin.soa.user.bean.model.UserListInfo;
import org.gatlin.soa.user.bean.param.RegisterParam;
import org.gatlin.util.DateUtil;
import org.gatlin.util.lang.StringUtil;
import org.gatlin.util.serial.SerializeUtil;
import org.gatlin.web.WebConsts;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.CfgVerse;
import org.hasan.bean.model.GuideInfo;
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
	private ConfigService configService;
	@Resource
	private AccountService accountService;
	@Resource
	private AlipayAccountService alipayAccountService;
	
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
			accountService.init(TargetType.USER, model.getUid(), accountMod);
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
	
	@Transactional
	public String memberBuy(SoaIdParam param) { 
		CfgMember member = hasanManager.member(param.getId());
		Assert.notNull(HasanCode.MEMBER_NOT_EXIST, member);
		Assert.isTrue(CoreCode.PARAM_ERR, member.isSale());
		int timeout = configService.config(SoaConsts.RECHARGE_TIMEOUT);
		Recharge recharge = AccountUtil.newRecharge(param, PlatType.ALIPAY, 100, member.getId(), member.getPrice(), timeout);
		return alipayAccountService.recharge(recharge);
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
	
	public int verseAdd(SoaSidParam param) {
		return hasanManager.verseAdd(param);
	}
	
	public void verseModify(SoaNameIdParam param) {
		hasanManager.verseModify(param);
	}
	
	public void verseDelete(SoaIdParam param) {
		hasanManager.verseDelete(param);
	}
	
	public Pager<CfgVerse> verses(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		return new Pager<CfgVerse>(hasanManager.verses(query));
	}
	
	public GuideInfo guide() {
		CfgVerse verse = hasanManager.verse();
		CfgGlobal global = configService.cfgGlobal("jie_qi");
		GuideInfo info = SerializeUtil.GSON.fromJson(global.getValue(), GuideInfo.class);
		if (null == info)
			info = new GuideInfo();
		info.setVerse(verse.getContent());
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		info.setHour(calendar.get(java.util.Calendar.HOUR_OF_DAY));
		return info;
	}
	
	public void dailyTask() {
		// 同步节气数据
		JieQiRequest request = new JieQiRequest();
		JieQi jieQi = request.sync().getResult();
		String time = DateUtil.getDate(DateUtil.yyyy_MM_dd);
		String ntime = DateUtil.convert(jieQi.getNow().getTime(), DateUtil.YYYY_MM_DD_HH_MM_SS, DateUtil.yyyy_MM_dd);
		GuideInfo info = new GuideInfo();
		CfgGlobal global = configService.cfgGlobal("jie_qi");
		CalendarRequest crequest = new CalendarRequest();
		Calendar calendar = crequest.sync().getResult();
		info.setYear(calendar.getYear().trim());
		info.setMonth(calendar.getMonth().trim());
		info.setDay(calendar.getDay().trim());
		info.setWeek(calendar.getWeek().trim());
		info.setLunarday(calendar.getLunarday().trim());
		info.setLunarmonth(calendar.getLunarmonth().trim());
		info.setLunaryear(calendar.getLunaryear().trim());
		info.setShengxiao(calendar.getShengxiao().trim());
		info.setSuici(calendar.getHuangli().getSuici());
		info.setJieqi(time.equals(ntime) ? jieQi.getNow().getName().trim() : StringUtil.EMPTY);
		global.setValue(SerializeUtil.GSON.toJson(info));
		global.setUpdated(DateUtil.current());
		configService.updateConfig(global);
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
