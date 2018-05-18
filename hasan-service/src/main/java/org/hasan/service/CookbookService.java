package org.hasan.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.gatlin.util.lang.CollectionUtil;
import org.gatlin.util.serial.SerializeUtil;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgCookbookStep;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.enums.HasanResourceType;
import org.hasan.bean.model.CookbookDetail;
import org.hasan.bean.model.CookbookDetail.Goods;
import org.hasan.bean.model.CookbookDetail.Step;
import org.hasan.bean.model.CookbookText;
import org.hasan.bean.param.CookbookAddParam;
import org.hasan.bean.param.CookbookModifyParam;
import org.hasan.bean.param.CookbookStepAddParam;
import org.hasan.bean.param.CookbookStepModifyParam;
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
		Query query = new Query().in("cfg_id", HasanResourceType.cookbookResourceTypes()).eq("owner", id);
		List<ResourceInfo> images = resourceService.resources(query).getList();
		CookbookText text = SerializeUtil.GSON.fromJson(cookbook.getText(), CookbookText.class);
		List<Goods> goods = new ArrayList<Goods>();
		if (!CollectionUtil.isEmpty(text.getGoods())) {
			query = new Query().in("id", text.getGoods()).orderByAsc("priority");
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
						int owner = Integer.valueOf(res.getOwner());
						if (owner == item.getId()) {
							resource = res;
							itr.remove();
							break;
						}
					}
				}
				goods.add(new Goods(item, resource));
			});
		}
		// 获取烹饪步骤
		List<CfgCookbookStep> cfgSteps = cookbookManager.steps(id);
		List<Step> steps = new ArrayList<Step>();
		if (!CollectionUtil.isEmpty(cfgSteps)) {
			Set<Integer> temp = new HashSet<Integer>();
			cfgSteps.forEach(item -> temp.add(item.getId()));
			query = new Query().eq("cfg_id", HasanResourceType.COOKBOOK_STEP.mark()).in("owner", temp);
			List<ResourceInfo> resources = resourceService.resources(query).getList();
			cfgSteps.forEach(step -> {
				ResourceInfo resource = null;
				if (!CollectionUtil.isEmpty(resources)) {
					Iterator<ResourceInfo> iterator = resources.iterator();
					while (iterator.hasNext()) {
						ResourceInfo res = iterator.next();
						int owner = Integer.valueOf(res.getOwner());
						if (owner == step.getId()) {
							resource = res;
							iterator.remove();
							break;
						}
					}
				}
				steps.add(new Step(step, resource));
			});
		}
		CookbookDetail detail = new CookbookDetail(cookbook, steps, goods, images);
		detail.setCuisineGroups(text.getCuisineGroups());
		return detail;
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
		cookbookManager.cookbookDelete(param);
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
