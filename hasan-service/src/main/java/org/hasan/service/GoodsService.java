package org.hasan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.gatlin.util.DateUtil;
import org.gatlin.util.lang.CollectionUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.enums.HasanResourceType;
import org.hasan.bean.model.CfgGoodsInfo;
import org.hasan.bean.param.GoodsAddParam;
import org.hasan.bean.param.GoodsModifyParam;
import org.hasan.manager.GoodsManager;
import org.hasan.mybatis.dao.CfgGoodsDao;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

@Service
public class GoodsService {
	
	@javax.annotation.Resource
	private CfgGoodsDao cfgGoodsDao;
	@javax.annotation.Resource
	private GoodsManager goodsManager;
	@javax.annotation.Resource
	private ResourceService resourceService;
	
	public CfgGoodsInfo goodsDetail(int id) {
		CfgGoods goods = cfgGoodsDao.getByKey(id);
		Assert.notNull(HasanCode.GOODS_NOT_EXIST, goods);
		Query query = new Query().eq("owner", id).in("cfg_id", HasanResourceType.goodsResourceTypes());
		List<ResourceInfo> resources = resourceService.resources(query).getList();
		if (!CollectionUtil.isEmpty(resources)) {
			Map<HasanResourceType, List<ResourceInfo>> map = new HashMap<HasanResourceType, List<ResourceInfo>>();
			for (ResourceInfo resource : resources) {
				HasanResourceType type = HasanResourceType.match(resource.getCfgId());
				List<ResourceInfo> list = map.get(type);
				if (null == list) {
					list = new ArrayList<ResourceInfo>();
					map.put(type, list);
				}
				list.add(resource);
			}
			return new CfgGoodsInfo(goods, map);
		} else
			return new CfgGoodsInfo(goods);
	}
	
	public Pager<CfgGoodsInfo> goods(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		List<CfgGoods> goods = goodsManager.goods(query);
		Set<Integer> ids = new HashSet<Integer>();
		goods.forEach(item -> ids.add(item.getId()));
		query = new Query().in("owner", ids).in("cfg_id", HasanResourceType.goodsResourceTypes());
		List<ResourceInfo> resources = resourceService.resources(query).getList();
		return Pager.<CfgGoodsInfo, CfgGoods>convert(goods, () -> {
			List<CfgGoodsInfo> infos = new ArrayList<CfgGoodsInfo>();
			for (CfgGoods cfgGoods : goods) {
				if (null != resources) {
					Iterator<ResourceInfo> iterator = resources.iterator();
					Map<HasanResourceType, List<ResourceInfo>> map = new HashMap<HasanResourceType, List<ResourceInfo>>();
					while (iterator.hasNext()) {
						ResourceInfo info = iterator.next();
						if (info.getOwner() == cfgGoods.getId()) {
							iterator.remove();
							HasanResourceType type = HasanResourceType.match(info.getCfgId());
							List<ResourceInfo> list = map.get(type);
							if (null == list) {
								list = new ArrayList<ResourceInfo>();
								map.put(type, list);
							}
							list.add(info);
						}
					}
					infos.add(new CfgGoodsInfo(cfgGoods, map));
				} else 
					infos.add(new CfgGoodsInfo(cfgGoods));
			}
			return infos;
		});
	}
	
	public int add(GoodsAddParam param) { 
		CfgGoods goods = EntityGenerator.newCfgGoods(param);
		goodsManager.insert(goods);
		return goods.getId();
	}
	
	public void modify(GoodsModifyParam param) {
		CfgGoods goods = goodsManager.goods(param.getId());
		Assert.notNull(HasanCode.GOODS_NOT_EXIST, goods);
		goods.setName(param.getName());
		goods.setDesc(param.getDesc());
		goods.setInventory(param.getInventory());
		goods.setPriority(param.getPriority());
		goods.setState(param.getState().mark());
		goods.setVIPPrice(param.getVIPPrice());
		goods.setGeneralPrice(param.getGeneralPrice());
		goods.setOriginalPrice(param.getOriginalPrice());
		goods.setUpdated(DateUtil.current());
		goodsManager.update(goods);
	}
}
