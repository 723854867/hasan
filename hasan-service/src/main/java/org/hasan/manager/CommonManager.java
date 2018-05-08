package org.hasan.manager;

import java.util.List;

import javax.annotation.Resource;

import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.util.DateUtil;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.MemberModifyParam;
import org.hasan.mybatis.dao.CfgMemberDao;
import org.springframework.stereotype.Component;

@Component
public class CommonManager {
	
	@Resource
	private CfgMemberDao cfgMemberDao;

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
	
	public CfgMember member(int id) {
		return cfgMemberDao.getByKey(id);
	}
	
	public List<CfgMember> members(Query query) {
		return cfgMemberDao.queryList(query);
	}
}
