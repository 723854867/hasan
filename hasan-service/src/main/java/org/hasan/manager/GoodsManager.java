package org.hasan.manager;

import java.util.List;

import javax.annotation.Resource;

import org.gatlin.dao.bean.model.Query;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.mybatis.dao.CfgGoodsDao;
import org.springframework.stereotype.Component;

@Component
public class GoodsManager {

	@Resource
	private CfgGoodsDao cfgGoodsDao;
	
	public void insert(CfgGoods goods)  {
		cfgGoodsDao.insert(goods);
	}
	
	public void update(CfgGoods goods) {
		cfgGoodsDao.update(goods);
	}
	
	public CfgGoods goods(int id) {
		return cfgGoodsDao.getByKey(id);
	}
	
	public List<CfgGoods> goods(Query query) {
		return cfgGoodsDao.queryList(query);
	}
}
