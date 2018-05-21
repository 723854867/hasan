package org.hasan.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.util.DateUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.CfgGoodsPrice;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.entity.UserEvaluation;
import org.hasan.bean.enums.GoodsState;
import org.hasan.bean.param.EvaluateParam;
import org.hasan.bean.param.GoodsAddParam;
import org.hasan.bean.param.GoodsModifyParam;
import org.hasan.mybatis.dao.CfgGoodsDao;
import org.hasan.mybatis.dao.CfgGoodsPriceDao;
import org.hasan.mybatis.dao.UserEvaluationDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GoodsManager {

	@Resource
	private CfgGoodsDao cfgGoodsDao;
	@Resource
	private HasanManager hasanManager;
	@Resource
	private CookbookManager cookbookManager;
	@Resource
	private CfgGoodsPriceDao cfgGoodsPriceDao;
	@Resource
	private UserEvaluationDao userEvaluationDao;
	
	@Transactional
	public int goodsAdd(GoodsAddParam param) {
		CfgGoods goods = EntityGenerator.newCfgGoods(param);
		cfgGoodsDao.insert(goods);
		Map<Integer, CfgMember> members = hasanManager.members();
		List<CfgGoodsPrice> prices = new ArrayList<CfgGoodsPrice>();
		prices.add(EntityGenerator.newCfgGoodsPrice(0, goods.getId(), param.getPrices().get(0)));
		for (CfgMember member : members.values()) {
			BigDecimal price = param.getPrices().get(member.getId());
			Assert.isTrue(CoreCode.PARAM_ERR, null != price && price.compareTo(BigDecimal.valueOf(0.01)) >= 0);
			price.setScale(2, RoundingMode.UP);
			prices.add(EntityGenerator.newCfgGoodsPrice(member.getId(), goods.getId(), price));
		}
		cfgGoodsPriceDao.batchInsert(prices);
		return goods.getId();
	}
	
	@Transactional
	public void goodsModify(GoodsModifyParam param) { 
		CfgGoods goods = goods(param.getId());
		Assert.notNull(HasanCode.GOODS_NOT_EXIST, goods);
		if (null != param.getCookbookId() && goods.getCookbookId() != param.getCookbookId()) {
			CfgCookbook cookbook = cookbookManager.cookbook(param.getCookbookId());
			Assert.notNull(HasanCode.COOKBOOK_NOT_EXIST, cookbook);
			goods.setCookbookId(param.getCookbookId());
		}
		goods.setName(param.getName());
		goods.setDesc(param.getDesc());
		goods.setInventory(param.getInventory());
		goods.setPriority(param.getPriority());
		goods.setState(param.getState().mark());
		goods.setUpdated(DateUtil.current());
		cfgGoodsDao.update(goods);
		cfgGoodsPriceDao.deleteByGoodsId(goods.getId());
		Map<Integer, CfgMember> members = hasanManager.members();
		List<CfgGoodsPrice> prices = new ArrayList<CfgGoodsPrice>();
		prices.add(EntityGenerator.newCfgGoodsPrice(0, goods.getId(), param.getPrices().get(0)));
		for (CfgMember member : members.values()) {
			BigDecimal price = param.getPrices().get(member.getId());
			Assert.isTrue(CoreCode.PARAM_ERR, null != price && price.compareTo(BigDecimal.valueOf(0.01)) >= 0);
			price.setScale(2, RoundingMode.UP);
			prices.add(EntityGenerator.newCfgGoodsPrice(member.getId(), goods.getId(), price));
		}
		cfgGoodsPriceDao.batchInsert(prices);
	}
	
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
	
	UserEvaluation evaluate(EvaluateParam param, OrderGoods orderGoods) {
		UserEvaluation evaluation = EntityGenerator.newUserEvaluation(param, orderGoods);
		userEvaluationDao.insert(evaluation);
		return evaluation;
	}
	
	public CfgGoods goods(int id) {
		return cfgGoodsDao.getByKey(id);
	}
	
	public List<CfgGoods> goods(Query query) {
		return cfgGoodsDao.queryList(query);
	}
	
	public List<CfgGoodsPrice> goodsPrices(long goodsId) {
		return cfgGoodsPriceDao.queryList(new Query().eq("goods_id", goodsId));
	}
	
	public List<UserEvaluation> evaluations(Query query) {
		return userEvaluationDao.queryList(query);
	}
	
	public Map<Integer, CfgGoodsPrice> goodsPrice(Set<Integer> goodsIds, int memberId) {
		List<CfgGoodsPrice> prices = cfgGoodsPriceDao.queryList(new Query().in("goods_id", goodsIds).eq("member_id", memberId));
		Map<Integer, CfgGoodsPrice> map = new HashMap<Integer, CfgGoodsPrice>();
		prices.forEach(item -> map.put(item.getGoodsId(), item));
		return map;
	}
}
