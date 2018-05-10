package org.hasan.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgCuisine;
import org.hasan.bean.entity.CfgCuisineCategory;
import org.hasan.bean.model.CuisineInfo;
import org.hasan.bean.param.CookbookAddParam;
import org.hasan.bean.param.CookbookModifyParam;
import org.hasan.bean.param.CookbookStepAddParam;
import org.hasan.bean.param.CookbookStepModifyParam;
import org.hasan.bean.param.CuisineAddParam;
import org.hasan.bean.param.CuisineCategoryAddParam;
import org.hasan.bean.param.CuisineCategoryModifyParam;
import org.hasan.bean.param.CuisineModifyParam;
import org.hasan.manager.CookbookManager;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

@Service
public class CookbookService {

	@Resource
	private CookbookManager cookbookManager;
	
	public Collection<CfgCuisineCategory> cuisineCategories() {
		return cookbookManager.cuisineCategories().values();
	}
	
	public int cuisineCategoryAdd(CuisineCategoryAddParam param) {
		return cookbookManager.cuisineCategoryAdd(param);
	}

	public void cuisineCategoryModify(CuisineCategoryModifyParam param) {
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
