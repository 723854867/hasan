package org.hasan.mybatis.dao;

import org.apache.ibatis.annotations.Delete;
import org.gatlin.dao.mybatis.DBDao;
import org.hasan.bean.entity.CfgGoodsPrice;

public interface CfgGoodsPriceDao extends DBDao<Integer, CfgGoodsPrice> {

	@Delete("DELETE FROM cfg_goods_price WHERE goods_id=#{goodsId}")
	int deleteByGoodsId(int goodsId);
}
