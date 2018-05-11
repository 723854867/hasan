package org.hasan.mybatis.dao;

import java.util.List;

import org.gatlin.dao.mybatis.DBDao;
import org.gatlin.soa.user.bean.model.UserListInfo;
import org.hasan.bean.entity.UserAssistant;
import org.hasan.bean.param.AssistantUserListParam;

public interface UserAssistantDao extends DBDao<Long, UserAssistant> {

	List<UserListInfo> list(AssistantUserListParam param);
}
