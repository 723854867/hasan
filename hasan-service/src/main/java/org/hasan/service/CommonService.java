package org.hasan.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.GatlinConfigration;
import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.sdk.jisu.bean.model.JieQiTips;
import org.gatlin.sdk.jisu.request.CalendarRequest;
import org.gatlin.sdk.jisu.request.JieQiRequest;
import org.gatlin.sdk.jisu.result.Calendar;
import org.gatlin.sdk.jisu.result.JieQi;
import org.gatlin.soa.SoaConsts;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.account.bean.AccountConsts;
import org.gatlin.soa.account.bean.AccountUtil;
import org.gatlin.soa.account.bean.entity.Recharge;
import org.gatlin.soa.alipay.api.AlipayAccountService;
import org.gatlin.soa.bean.enums.PlatType;
import org.gatlin.soa.bean.enums.TargetType;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaLidParam;
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
import org.gatlin.util.bean.enums.TimeUnit;
import org.gatlin.util.serial.SerializeUtil;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.CfgVerse;
import org.hasan.bean.model.GuideInfo;
import org.hasan.bean.param.AssistantAllocateParam;
import org.hasan.bean.param.AssistantUserListParam;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.MemberModifyParam;
import org.hasan.bean.param.VersesParam;
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
		int accountMod = GatlinConfigration.get(AccountConsts.ACCOUNT_MOD_USER);
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
		Assert.isTrue(HasanCode.MEMBER_COUNT_LIMIT, hasanManager.checkMemberCount(param.getUser().getId(), member));
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
	
	public int verseAdd(VersesParam param) {
		return hasanManager.verseAdd(param);
	}
	
	public void verseModify(VersesParam param) {
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
		info.setContent1(verse.getContent1());
		info.setContent2(verse.getContent2());
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		info.setHour(calendar.get(java.util.Calendar.HOUR_OF_DAY));
		return info;
	}
	
	public void dailyTask() {
		// 同步节气数据
		JieQiRequest request = new JieQiRequest();
		JieQi jieQi = request.sync().getResult();
		jieQi.timeConvert();
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
		info.setJieqi(jieQi.getNow().getName().trim());
		info.setJieQiPassDay(Math.abs((int) DateUtil.interval(jieQi.getNow().getDay(), DateUtil.yyyyMMdd, TimeUnit.DAY)));
		List<JieQiTips> tips = jieQi.getList();
		for (JieQiTips temp : tips) {
			if (temp.getDay() <= Integer.valueOf(DateUtil.getDate(DateUtil.yyyyMMdd)))
				continue;
			info.setNextJieQi(temp.getName());
			info.setNextJieQiDay((int) DateUtil.interval(temp.getDay(), DateUtil.yyyyMMdd, TimeUnit.DAY));
			break;
		}
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
