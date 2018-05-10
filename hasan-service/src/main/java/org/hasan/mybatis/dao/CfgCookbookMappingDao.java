package org.hasan.mybatis.dao;

import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.gatlin.dao.mybatis.DBDao;
import org.hasan.bean.entity.CfgCookbookMapping;

public interface CfgCookbookMappingDao extends DBDao<Integer, CfgCookbookMapping> {

	@Select("SELECT tid FROM cfg_cookbook_mapping WHERE `type`=#{type} AND cookbook_id=#{cookbookId}")
	Set<Integer> tids(@Param("type") int type, @Param("cookbookId") int cookbookId);
}
