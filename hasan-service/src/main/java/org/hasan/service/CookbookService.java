package org.hasan.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.model.ResourceInfo;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.param.ResourcesParam;
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
		ResourcesParam rp = new ResourcesParam();
		rp.addOwner(String.valueOf(id));
		rp.setCfgIds(HasanResourceType.cookbookResourceTypes());
		List<ResourceInfo> images = resourceService.list(rp).getList();
		CookbookText text = SerializeUtil.GSON.fromJson(cookbook.getText(), CookbookText.class);
		List<Goods> goods = new ArrayList<Goods>();
		if (!CollectionUtil.isEmpty(text.getGoods())) {
			Query query = new Query().in("id", text.getGoods()).orderByAsc("priority");
			List<CfgGoods> list = goodsManager.goods(query);
			Set<String> temp = new HashSet<String>();
			list.forEach(item -> temp.add(String.valueOf(item.getId())));
			rp = new ResourcesParam();
			rp.setOwners(temp);
			rp.addCfgId(HasanResourceType.GOODS_ICON.mark());
			Map<String, ResourceInfo> goodsIcons = resourceService.ownerMap(rp);
			list.forEach(item -> goods.add(new Goods(item, goodsIcons.get(String.valueOf(item.getId())))));
		}
		// 获取烹饪步骤
		List<CfgCookbookStep> cfgSteps = cookbookManager.steps(id);
		List<Step> steps = new ArrayList<Step>();
		if (!CollectionUtil.isEmpty(cfgSteps)) {
			Set<String> temp = new HashSet<String>();
			cfgSteps.forEach(item -> temp.add(String.valueOf(item.getId())));
			rp = new ResourcesParam();
			rp.setOwners(temp);
			rp.addCfgId(HasanResourceType.COOKBOOK_STEP.mark());
			Map<String, List<ResourceInfo>> resources = resourceService.ownerListMap(rp);
			cfgSteps.forEach(step -> steps.add(new Step(step, resources.get(String.valueOf(step.getId())))));
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
