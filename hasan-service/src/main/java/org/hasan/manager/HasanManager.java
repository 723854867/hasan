package org.hasan.manager;

import java.util.List;

import javax.annotation.Resource;

import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.account.bean.AccountUtil;
import org.gatlin.soa.account.bean.entity.LogUserAccount;
import org.gatlin.soa.account.bean.entity.UserAccount;
import org.gatlin.soa.account.bean.entity.UserRecharge;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.util.DateUtil;
import org.gatlin.util.bean.enums.TimeUnit;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.HasanConsts;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.enums.HasanBizType;
import org.hasan.bean.enums.MemberType;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.MemberModifyParam;
import org.hasan.mybatis.dao.CfgMemberDao;
import org.hasan.mybatis.dao.UserCustomDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class HasanManager {

	@Resource
	private CfgMemberDao cfgMemberDao;
	@Resource
	private UserCustomDao userCustomDao;
	@Resource
	private ConfigService configService;
	@Resource
	private AccountService accountService;
	
	public void register(long uid) {
		String memberTitle = configService.config(HasanConsts.DEFAULT_MEMBER_TITLE);
		UserCustom custom = EntityGenerator.newUserCustom(uid, memberTitle);
		userCustomDao.insert(custom);
	}
	
	public int memberAdd(MemberAddParam param) {
		CfgMember member = EntityGenerator.newCfgMember(param);
		cfgMemberDao.insert(member);
		return member.getId();
	}
	
	public void memberModify(MemberModifyParam param) {
		CfgMember member = cfgMemberDao.getByKey(param.getId());
		Assert.notNull(HasanCode.MEMBER_NOT_EXIST, member);
		if (null != param.getExpiry()) {
			member.setExpiry(param.getExpiry());
			member.setTimeUnit(param.getTimeUnit().mark());
		}
		if (null != param.getSale())
			member.setSale(param.getSale());
		if (null != param.getPrice())
			member.setPrice(param.getPrice());
		if (StringUtil.hasText(param.getName()))
			member.setName(param.getName());
		if (null != param.getMemberType())
			member.setMemberType(param.getMemberType().mark());
		member.setUpdated(DateUtil.current());
		cfgMemberDao.update(member);
	}
	
	// 购买会员成功
	@Transactional
	public void memberBuy(UserRecharge recharge) {
		CfgMember member = member(Integer.valueOf(recharge.getGoodsId()));
		Query query = new Query().eq("uid", recharge.getRechargee()).forUpdate();
		UserCustom custom = userCustomDao.queryUnique(query);
		custom.setMemberType(member.getMemberType());
		custom.setMemberTitle(member.getName());
		custom.setUpdated(DateUtil.current());
		TimeUnit unit = TimeUnit.match(member.getTimeUnit());
		long duration = unit.millis() * member.getExpiry();
		custom.setMemberExpiry(System.currentTimeMillis() + duration);
		userCustomDao.update(custom);
		LogUserAccount log = AccountUtil.log(recharge.getRechargee(), member.getPrice(), HasanBizType.MEMBER_BUY_OK.mark(), recharge.getId());
		accountService.process(log);
	}
	
	@Transactional
	public UserCustom userCustom(long uid) {
		Query query = new Query().eq("uid", uid).forUpdate();
		UserCustom custom = userCustomDao.queryUnique(query);
		if (custom.getMemberExpiry() <= System.currentTimeMillis()) {
			custom.setMemberType(MemberType.ORIGINAL.mark());
			custom.setUpdated(DateUtil.current());
			userCustomDao.update(custom);
			UserAccount account = accountService.account(new Query().eq("uid", uid));
			LogUserAccount log = AccountUtil.log(uid, account.getUsable().negate(), HasanBizType.MEMBER_EXPIRY.mark(), custom.getMemberType());
			accountService.process(log);
		}
		return custom;
	}
	
	public CfgMember member(int id) {
		return cfgMemberDao.getByKey(id);
	}
	
	public List<CfgMember> members(Query query) {
		return cfgMemberDao.queryList(query);
	}
}