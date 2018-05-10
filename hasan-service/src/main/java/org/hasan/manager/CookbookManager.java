package org.hasan.manager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaNameIdParam;
import org.gatlin.util.DateUtil;
import org.gatlin.util.lang.CollectionUtil;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgCookbookMapping;
import org.hasan.bean.entity.CfgCookbookStep;
import org.hasan.bean.entity.CfgCuisine;
import org.hasan.bean.entity.CfgCuisineCategory;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.enums.CookbookMappingType;
import org.hasan.bean.param.CookbookAddParam;
import org.hasan.bean.param.CookbookModifyParam;
import org.hasan.bean.param.CookbookStepAddParam;
import org.hasan.bean.param.CookbookStepModifyParam;
import org.hasan.bean.param.CuisineAddParam;
import org.hasan.bean.param.CuisineModifyParam;
import org.hasan.mybatis.dao.CfgCookbookDao;
import org.hasan.mybatis.dao.CfgCookbookMappingDao;
import org.hasan.mybatis.dao.CfgCookbookStepDao;
import org.hasan.mybatis.dao.CfgCuisineCategoryDao;
import org.hasan.mybatis.dao.CfgCuisineDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CookbookManager {

	@Resource
	private GoodsManager goodsManager;
	@Resource
	private CfgCuisineDao cfgCuisineDao;
	@Resource
	private CfgCookbookDao cfgCookbookDao;
	@Resource
	private CfgCookbookStepDao cfgCookbookStepDao;
	@Resource
	private CfgCookbookMappingDao cfgCookbookMappingDao;
	@Resource
	private CfgCuisineCategoryDao cfgCuisineCategoryDao;
	
	public int cuisineCategoryAdd(SoaNameIdParam param) {
		CfgCuisineCategory category = EntityGenerator.newCfgCuisineCategory(param);
		cfgCuisineCategoryDao.insert(category);
		return category.getId();
	}
	
	public void cuisineCategoryModify(SoaNameIdParam param) {
		CfgCuisineCategory category = cfgCuisineCategoryDao.getByKey(param.getId());
		Assert.notNull(HasanCode.CUISINE_CATEGORY_NOT_EXIST, category);
		category.setName(param.getName());
		category.setUpdated(DateUtil.current());
		cfgCuisineCategoryDao.update(category);
	}
	
	@Transactional
	public void cuisineCategoryDelete(SoaIdParam param) {
		List<CfgCuisine> cuisines = cuisines(new Query().eq("category", param.getId()));
		Assert.isTrue(HasanCode.CUISINE_DELETE_CATEGORY_LINKED, CollectionUtil.isEmpty(cuisines));
		cfgCuisineCategoryDao.deleteByKey(param.getId());
	}
	
	public int cuisineAdd(CuisineAddParam param) {
		CfgCuisineCategory category = cfgCuisineCategoryDao.getByKey(param.getCategory());
		Assert.notNull(HasanCode.CUISINE_CATEGORY_NOT_EXIST, category);
		CfgCuisine cuisine = EntityGenerator.newCfgCuisine(param);
		cfgCuisineDao.insert(cuisine);
		return cuisine.getId();
	}
	
	public void cuisineModify(CuisineModifyParam param) {
		CfgCuisine cuisine = cfgCuisineDao.getByKey(param.getId());
		Assert.notNull(HasanCode.CUISINE_NOT_EXIST, cuisine);
		if (null != param.getCategory()) {
			CfgCuisineCategory category = cfgCuisineCategoryDao.getByKey(param.getCategory());
			Assert.notNull(HasanCode.CUISINE_CATEGORY_NOT_EXIST, category);
			cuisine.setCategory(param.getCategory());
		}
		if (StringUtil.hasText(param.getName()))
			cuisine.setName(param.getName());
		if (StringUtil.hasText(param.getDosage()))
			cuisine.setDosage(param.getDosage());
		cuisine.setUpdated(DateUtil.current());
		cfgCuisineDao.update(cuisine);
	}
	
	@Transactional
	public void cuisineDelete(SoaIdParam param) {
		long deleted = cfgCuisineDao.deleteByKey(param.getId());
		Assert.isTrue(HasanCode.CUISINE_NOT_EXIST, deleted == 1);
		cfgCookbookMappingDao.deleteByQuery(new Query().eq("type", CookbookMappingType.CUISINE.mark()).eq("tid", param.getId()));
	}
	
	@Transactional
	public int cookbookAdd(CookbookAddParam param) { 
		CfgCookbook cookbook = EntityGenerator.newCfgCookbook(param);
		cfgCookbookDao.insert(cookbook);
		List<CfgCuisine> cuisines = cfgCuisineDao.queryList(new Query().in("id", param.getCusines()));
		Assert.isTrue(HasanCode.CUISINE_NOT_EXIST, cuisines.size() == param.getCusines().size());
		List<CfgCookbookMapping> mappings = EntityGenerator.newCfgCookbookMapping(cookbook, param.getCusines(), CookbookMappingType.CUISINE);
		if (CollectionUtil.isEmpty(param.getGoods())) {
			List<CfgGoods> goods = goodsManager.goods(new Query().in("id", param.getGoods()));
			Assert.isTrue(HasanCode.GOODS_NOT_EXIST, goods.size() == param.getGoods().size());
			mappings.addAll(EntityGenerator.newCfgCookbookMapping(cookbook, param.getGoods(), CookbookMappingType.GOODS));
		}
		cfgCookbookMappingDao.batchInsert(mappings);
		return cookbook.getId();
	}
	
	@Transactional
	public void cookbookModify(CookbookModifyParam param) { 
		CfgCookbook cookbook = cfgCookbookDao.getByKey(param.getId());
		Assert.notNull(HasanCode.COOKBOOK_NOT_EXIST, cookbook);
		cookbook.setName(param.getName());
		cookbook.setUpdated(DateUtil.current());
		cfgCookbookMappingDao.deleteByQuery(new Query().eq("cookbook_id", param.getId()));
		List<CfgCuisine> cuisines = cfgCuisineDao.queryList(new Query().in("id", param.getCusines()));
		Assert.isTrue(HasanCode.CUISINE_NOT_EXIST, cuisines.size() == param.getCusines().size());
		List<CfgCookbookMapping> mappings = EntityGenerator.newCfgCookbookMapping(cookbook, param.getCusines(), CookbookMappingType.CUISINE);
		if (CollectionUtil.isEmpty(param.getGoods())) {
			List<CfgGoods> goods = goodsManager.goods(new Query().in("id", param.getGoods()));
			Assert.isTrue(HasanCode.GOODS_NOT_EXIST, goods.size() == param.getGoods().size());
			mappings.addAll(EntityGenerator.newCfgCookbookMapping(cookbook, param.getGoods(), CookbookMappingType.GOODS));
		}
		cfgCookbookMappingDao.batchInsert(mappings);
	}
	
	@Transactional
	public void cookbookDelete(SoaIdParam param) {
		long deleted = cfgCookbookDao.deleteByKey(param.getId());
		Assert.isTrue(HasanCode.COOKBOOK_NOT_EXIST, deleted == 1);
		cfgCookbookStepDao.deleteByQuery(new Query().eq("cookbook_id", param.getId()));
		cfgCookbookMappingDao.deleteByQuery(new Query().eq("cookbook_id", param.getId()));
	}
	
	public int cookbookStepAdd(CookbookStepAddParam param) { 
		CfgCookbook cookbook = cfgCookbookDao.getByKey(param.getCookbookId());
		Assert.notNull(HasanCode.COOKBOOK_NOT_EXIST, cookbook);
		CfgCookbookStep step = EntityGenerator.newCfgCookbookStep(param);
		cfgCookbookStepDao.insert(step);
		return step.getId();
	}
	
	public void cookbookStepModify(CookbookStepModifyParam param) { 
		CfgCookbookStep step = cfgCookbookStepDao.getByKey(param.getId());
		Assert.notNull(HasanCode.COOKBOOK_STEP_NOT_EXIST, step);
		if (null != param.getPriority())
			step.setPriority(param.getPriority());
		if (null != param.getContent())
			step.setContent(param.getContent());
		step.setUpdated(DateUtil.current());
		cfgCookbookStepDao.update(step);
	}
	
	public void cookbookStepDelete(SoaIdParam param) { 
		cfgCookbookStepDao.deleteByKey(param.getId());
	}
	
	public CfgCookbook cookbook(int cookbookId) {
		return cfgCookbookDao.getByKey(cookbookId);
	}
	
	public List<CfgCookbookStep> steps(int cookbookId) {
		return cfgCookbookStepDao.queryList(new Query().eq("cookbook_id", cookbookId));
	}
	
	public List<CfgCuisine> cuisines(Query query) {
		return cfgCuisineDao.queryList(query);
	}
	
	public List<CfgCookbook> cookbooks(Query query) {
		return cfgCookbookDao.queryList(query);
	}
	
	public Map<Integer, CfgCuisineCategory> cuisineCategories() {
		return cfgCuisineCategoryDao.getAll();
	}
	
	public Set<Integer> cookbookMapps(CookbookMappingType type, int cookbookId) {
		return cfgCookbookMappingDao.tids(type.mark(), cookbookId);
	}
}
