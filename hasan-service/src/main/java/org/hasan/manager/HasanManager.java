package org.hasan.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.account.api.AccountService;
import org.gatlin.soa.account.bean.entity.Account;
import org.gatlin.soa.account.bean.entity.Recharge;
import org.gatlin.soa.account.bean.enums.AccountOwnerType;
import org.gatlin.soa.account.bean.enums.AccountType;
import org.gatlin.soa.account.bean.model.AccountDetail;
import org.gatlin.soa.bean.param.SoaLidParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.user.bean.model.UserListInfo;
import org.gatlin.util.DateUtil;
import org.gatlin.util.bean.enums.TimeUnit;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.UserAssistant;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.enums.HasanBizType;
import org.hasan.bean.param.AssistantAllocateParam;
import org.hasan.bean.param.AssistantUserListParam;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.MemberModifyParam;
import org.hasan.mybatis.dao.CfgMemberDao;
import org.hasan.mybatis.dao.UserAssistantDao;
import org.hasan.mybatis.dao.UserCustomDao;
import org.springframework.dao.DuplicateKeyException;
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
	@Resource
	private UserAssistantDao userAssistantDao;
	
	public void register(long uid) {
		UserCustom custom = EntityGenerator.newUserCustom(uid);
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
		member.setUpdated(DateUtil.current());
		cfgMemberDao.update(member);
	}
	
	// 购买会员成功
	@Transactional
	public void memberBuy(Recharge recharge) {
		CfgMember member = member(Integer.valueOf(recharge.getGoodsId()));
		Query query = new Query().eq("uid", recharge.getRechargee()).forUpdate();
		UserCustom custom = userCustomDao.queryUnique(query);
		custom.setMemberId(member.getId());
		custom.setUpdated(DateUtil.current());
		TimeUnit unit = TimeUnit.match(member.getTimeUnit());
		long duration = unit.millis() * member.getExpiry();
		if (0 == custom.getMemberExpiry())
			custom.setMemberExpiry(System.currentTimeMillis() + duration);
		else
			custom.setMemberExpiry(custom.getMemberExpiry() + duration);
		userCustomDao.update(custom);
		AccountDetail detail = AccountDetail.userUsable(recharge.getRechargee()).incr(member.getPrice())
				.bizId(recharge.getId()).bizType(HasanBizType.MEMBER_BUY_OK.mark());
		accountService.process(detail);
	}
	
	@Transactional
	public UserCustom userCustom(long uid) {
		Query query = new Query().eq("owner_type", AccountOwnerType.USER.mark()).eq("owner", uid).eq("type", AccountType.BASIC.mark()).forUpdate();
		Account account = accountService.account(query);
		query = new Query().eq("uid", uid).forUpdate();
		UserCustom custom = userCustomDao.queryUnique(query);
		// 会员到期或者余额为0都会变成普通会员
		if (0 != custom.getMemberId() &&
				(custom.getMemberExpiry() <= System.currentTimeMillis() 
				|| account.getUsable().compareTo(BigDecimal.ZERO) == 0)) {
			if (account.getUsable().compareTo(BigDecimal.ZERO) > 0) {
				AccountDetail detail = AccountDetail.userUsable(uid).decr(account.getUsable())
						.bizId(custom.getMemberId()).bizType(HasanBizType.MEMBER_EXPIRY.mark());
				accountService.process(detail);
			}
			custom.setMemberId(0);
			custom.setMemberExpiry(0);
			custom.setUpdated(DateUtil.current());
			userCustomDao.update(custom);
		}
		return custom;
	}
	
	public void assistantAllocate(AssistantAllocateParam param) {
		try {
			UserAssistant assistant = EntityGenerator.newUserAssistant(param);
			userAssistantDao.insert(assistant);
		} catch (DuplicateKeyException e) {
			throw new CodeException(HasanCode.ASSISTANT_ALLOCATE_DUPLICATED, e);
		}
	}
	
	public void assistantDelete(SoaLidParam param) {
		userAssistantDao.deleteByKey(param.getId());
	}
	
	public CfgMember member(int id) {
		return cfgMemberDao.getByKey(id);
	}
	
	public Map<Integer, CfgMember> members() {
		return cfgMemberDao.query(new Query());
	}
	
	public List<UserListInfo> assistantUsers(AssistantUserListParam param) {
		return userAssistantDao.assistantUsers(param);
	}
	
	public List<UserListInfo> allocatableUsers(AssistantUserListParam param) {
		return userAssistantDao.allocatableUsers(param);
	}
}
