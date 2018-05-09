package org.hasan.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.util.DateUtil;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.enums.GoodsState;
import org.hasan.mybatis.dao.CfgGoodsDao;
import org.springframework.stereotype.Component;

@Component
public class GoodsManager {

	@Resource
	private CfgGoodsDao cfgGoodsDao;
	
	Map<Integer, CfgGoods> buy(Map<Integer, Integer> map) {
		Query query = new Query().in("id", map.keySet()).forUpdate();
		List<CfgGoods> goods = goods(query);
		Assert.isTrue(HasanCode.GOODS_NOT_EXIST, goods.size() == map.size());
		Map<Integer, CfgGoods> m = new HashMap<Integer, CfgGoods>();
		goods.forEach(item -> {
			Assert.isTrue(HasanCode.GOODS_OFF_SHELVES, item.getState() == GoodsState.SALE.mark());
			int num = map.get(item.getId());
			Assert.isTrue(CoreCode.PARAM_ERR, num > 0);
			Assert.isTrue(HasanCode.GOODS_INVENTORY_LACK, item.getInventory() >= num);
			item.setInventory(item.getInventory() - num);
			item.setSold(item.getSold() + num);
			item.setUpdated(DateUtil.current());
			m.put(item.getId(), item);
		});
		return m;
	}
	
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
