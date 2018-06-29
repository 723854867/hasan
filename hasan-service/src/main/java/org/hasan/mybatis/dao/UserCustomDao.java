package org.hasan.mybatis.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.gatlin.dao.mybatis.DBDao;
import org.hasan.bean.entity.UserCustom;

public interface UserCustomDao extends DBDao<Long, UserCustom> {

	@Select("SELECT COUNT(*) FROM recharge WHERE goods_type=100 AND rechargee=#{uid} AND goods_id=#{memberId} AND state IN(1,2,3,5)")
	int memberBuyCount(@Param("uid") long uid, @Param("memberId") int memberId);
}
