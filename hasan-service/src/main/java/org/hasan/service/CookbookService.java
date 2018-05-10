package org.hasan.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaNameIdParam;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.gatlin.util.lang.CollectionUtil;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgCookbookStep;
import org.hasan.bean.entity.CfgCuisine;
import org.hasan.bean.entity.CfgCuisineCategory;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.enums.CookbookMappingType;
import org.hasan.bean.enums.HasanResourceType;
import org.hasan.bean.model.CookbookDetail;
import org.hasan.bean.model.CookbookDetail.Cuisine;
import org.hasan.bean.model.CookbookDetail.Goods;
import org.hasan.bean.model.CookbookDetail.Step;
import org.hasan.bean.model.CuisineInfo;
import org.hasan.bean.param.CookbookAddParam;
import org.hasan.bean.param.CookbookModifyParam;
import org.hasan.bean.param.CookbookStepAddParam;
import org.hasan.bean.param.CookbookStepModifyParam;
import org.hasan.bean.param.CuisineAddParam;
import org.hasan.bean.param.CuisineModifyParam;
import org.hasan.manager.CookbookManager;
import org.hasan.manager.GoodsManager;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

@Service
public class CookbookService {

	@Resource
	private GoodsManager goodsManager;
	@Resource
	private CookbookManager cookbookManager;
	@Resource
	private ResourceService resourceService;
	
	public CookbookDetail cookbookDetail(int id) {
		CfgCookbook cookbook = cookbookManager.cookbook(id);
		Assert.notNull(HasanCode.COOKBOOK_NOT_EXIST, cookbook);
		// 获取菜谱轮播图
		Query query = new Query().eq("cfg_id", HasanResourceType.COOKBOOK_IMAGE.mark()).eq("owner", id);
		List<ResourceInfo> images = resourceService.resources(query).getList();
		// 获取菜谱关联商品
		Set<Integer> set = cookbookManager.cookbookMapps(CookbookMappingType.GOODS, id);
		List<Goods> goods = new ArrayList<Goods>();
		if (!CollectionUtil.isEmpty(set)) {
			query = new Query().in("id", set).orderByAsc("priority");
			List<CfgGoods> list = goodsManager.goods(query);
			Set<Integer> temp = new HashSet<Integer>();
			list.forEach(item -> temp.add(item.getId()));
			query = new Query().eq("cfg_id", HasanResourceType.GOODS_ICON.mark()).in("owner", temp);
			List<ResourceInfo> goodsIcons = resourceService.resources(query).getList();
			list.forEach(item -> {
				ResourceInfo resource = null;
				if (!CollectionUtil.isEmpty(goodsIcons)) {
					Iterator<ResourceInfo> itr = goodsIcons.iterator();
					while (itr.hasNext()) {
						ResourceInfo res = itr.next();
						if (res.getOwner() == item.getId()) {
							resource = res;
							itr.remove();
							break;
						}
					}
				}
				goods.add(new Goods(item, resource));
			});
		}
		// 获取菜谱关联食材
		set = cookbookManager.cookbookMapps(CookbookMappingType.CUISINE, id);
		List<Cuisine> cuisines = new ArrayList<Cuisine>();
		if (!CollectionUtil.isEmpty(set)) {
			query = new Query().in("id", set);
			List<CfgCuisine> temp = cookbookManager.cuisines(query);
			Map<Integer, CfgCuisineCategory> categories = cookbookManager.cuisineCategories();
			List<CfgCuisineCategory> sorts = new ArrayList<CfgCuisineCategory>();
			Map<CfgCuisineCategory, List<CfgCuisine>> map = new HashMap<CfgCuisineCategory, List<CfgCuisine>>();
			temp.forEach(cuisine -> {
				CfgCuisineCategory category = categories.get(cuisine.getCategory());
				List<CfgCuisine> list = map.get(category);
				if (null == list) {
					list = new ArrayList<CfgCuisine>();
					map.put(category, list);
					sorts.add(category);
				}
				list.add(cuisine);
			});
			for (CfgCuisineCategory category : sorts) {
				List<CfgCuisine> list = map.get(category);
				cuisines.add(new Cuisine(category, list));
			}
		}
		// 获取烹饪步骤
		List<CfgCookbookStep> cfgSteps = cookbookManager.steps(id);
		Set<Integer> temp = new HashSet<Integer>();
		cfgSteps.forEach(item -> temp.add(item.getId()));
		query = new Query().eq("cfg_id", HasanResourceType.COOKBOOK_STEP.mark()).in("owner", temp);
		List<ResourceInfo> resources = resourceService.resources(query).getList();
		List<Step> steps = new ArrayList<Step>();
		cfgSteps.forEach(step -> {
			ResourceInfo resource = null;
			if (!CollectionUtil.isEmpty(resources)) {
				Iterator<ResourceInfo> iterator = resources.iterator();
				while (iterator.hasNext()) {
					ResourceInfo res = iterator.next();
					if (res.getOwner() == step.getId()) {
						resource = res;
						iterator.remove();
						break;
					}
				}
				steps.add(new Step(step, resource));
			}
		});
		return new CookbookDetail(cookbook, steps, goods, cuisines, images);
	}
	
	public Collection<CfgCuisineCategory> cuisineCategories() {
		return cookbookManager.cuisineCategories().values();
	}
	
	public int cuisineCategoryAdd(SoaNameIdParam param) {
		return cookbookManager.cuisineCategoryAdd(param);
	}

	public void cuisineCategoryModify(SoaNameIdParam param) {
		cookbookManager.cuisineCategoryModify(param);
	}
	
	public void cuisineCategoryDelete(SoaIdParam param) {
		cookbookManager.cuisineCategoryDelete(param);
	}
	
	public Pager<CuisineInfo> cuisines(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		List<CfgCuisine> cuisines = cookbookManager.cuisines(query);
		Map<Integer, CfgCuisineCategory> map = cookbookManager.cuisineCategories();
		return Pager.<CuisineInfo, CfgCuisine>convert(cuisines, () -> {
			List<CuisineInfo> infos = new ArrayList<CuisineInfo>();
			cuisines.forEach(cuisine -> infos.add(new CuisineInfo(cuisine, map.get(cuisine.getCategory()))));
			return infos;
		});
	}

	public int cuisineAdd(CuisineAddParam param) {
		return cookbookManager.cuisineAdd(param);
	}
	
	public void cuisineModify(CuisineModifyParam param) {
		cookbookManager.cuisineModify(param);
	}
	
	public void cuisineDelete(SoaIdParam param) {
		cookbookManager.cuisineDelete(param);
	}
	
	public Pager<CfgCookbook> cookbooks(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		return new Pager<CfgCookbook>(cookbookManager.cookbooks(query));
	}
	
	public int cookbookAdd(CookbookAddParam param) {
		return cookbookManager.cookbookAdd(param);
	}
	
	public void cookbookModify(CookbookModifyParam param) {
		cookbookManager.cookbookModify(param);
	}
	
	public void cookbookDelete(SoaIdParam param) {
		cookbookManager.cuisineDelete(param);
	}
	
	public int cookbookStepAdd(CookbookStepAddParam param) {
		return cookbookManager.cookbookStepAdd(param);
	}
	
	public void cookbooStepkModify(CookbookStepModifyParam param) {
		cookbookManager.cookbookStepModify(param);
	}
	
	public void cookbookStepDelete(SoaIdParam param) {
		cookbookManager.cookbookStepDelete(param);
	}
}
