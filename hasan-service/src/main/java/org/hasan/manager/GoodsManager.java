package org.hasan.manager;

import javax.annotation.Resource;

import org.hasan.mybatis.dao.CfgGoodsDao;
import org.springframework.stereotype.Component;

@Component
public class GoodsManager {

	@Resource
	private CfgGoodsDao cfgGoodsDao;
	
}
