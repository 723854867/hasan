package org.hasan.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaQuotaLIdParam;
import org.gatlin.util.DateUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.CfgGoodsPrice;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.entity.UserEvaluation;
import org.hasan.bean.enums.GoodsState;
import org.hasan.bean.param.EvaluateParam;
import org.hasan.bean.param.GoodsPriceAddParam;
import org.hasan.mybatis.dao.CfgGoodsDao;
import org.hasan.mybatis.dao.CfgGoodsPriceDao;
import org.hasan.mybatis.dao.UserEvaluationDao;
import org.springframework.stereotype.Component;

@Component
public class GoodsManager {

	@Resource
	private CfgGoodsDao cfgGoodsDao;
	@Resource
	private HasanManager hasanManager;
	@Resource
	private CfgGoodsPriceDao cfgGoodsPriceDao;
	@Resource
	private UserEvaluationDao userEvaluationDao;
	
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
	
	public long priceAdd(GoodsPriceAddParam param) { 
		CfgGoods goods = cfgGoodsDao.getByKey(param.getId());
		Assert.notNull(HasanCode.GOODS_NOT_EXIST, goods);
		if (0 != param.getMemberId()) {
			CfgMember member = hasanManager.member(param.getMemberId());
			Assert.notNull(HasanCode.MEMBER_NOT_EXIST, member);
		}
		CfgGoodsPrice price = EntityGenerator.newCfgGoodsPrice(param);
		cfgGoodsPriceDao.insert(price);
		return price.getId();
	}
	
	public void priceModify(SoaQuotaLIdParam param) { 
		CfgGoodsPrice price = cfgGoodsPriceDao.getByKey(param.getId());
		Assert.notNull(HasanCode.GOODS_PRICE_NOT_EXIST, price);
		price.setPrice(param.getQuota());
		price.setUpdated(DateUtil.current());
		cfgGoodsPriceDao.update(price);
	}
	
	public void priceDelete(SoaIdParam param) { 
		cfgGoodsPriceDao.deleteByKey(param.getId());
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
