package org.hasan.manager;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.util.DateUtil;
import org.gatlin.util.lang.CollectionUtil;
import org.gatlin.util.lang.StringUtil;
import org.gatlin.util.serial.SerializeUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgCookbookStep;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.model.CookbookText;
import org.hasan.bean.model.CuisineGroup;
import org.hasan.bean.param.CookbookAddParam;
import org.hasan.bean.param.CookbookModifyParam;
import org.hasan.bean.param.CookbookStepAddParam;
import org.hasan.bean.param.CookbookStepModifyParam;
import org.hasan.mybatis.dao.CfgCookbookDao;
import org.hasan.mybatis.dao.CfgCookbookStepDao;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CookbookManager {

	@Resource
	private GoodsManager goodsManager;
	@Resource
	private CfgCookbookDao cfgCookbookDao;
	@Resource
	private CfgCookbookStepDao cfgCookbookStepDao;
	
	@Transactional
	public int cookbookAdd(CookbookAddParam param) { 
		String text = _cookbookText(param.getGoods(), param.getCuisineGroups());
		CfgCookbook cookbook = EntityGenerator.newCfgCookbook(param.getName(), text);
		cfgCookbookDao.insert(cookbook);
		return cookbook.getId();
	}
	
	@Transactional
	public void cookbookModify(CookbookModifyParam param) { 
		CfgCookbook cookbook = cfgCookbookDao.getByKey(param.getId());
		Assert.notNull(HasanCode.COOKBOOK_NOT_EXIST, cookbook);
		cookbook.setName(param.getName());
		cookbook.setText(_cookbookText(param.getGoods(), param.getCuisineGroups()));
		cookbook.setUpdated(DateUtil.current());
		cfgCookbookDao.update(cookbook);
	}
	
	private String _cookbookText(Set<Integer> goods, List<CuisineGroup> cuisineGroups) {
		if (!CollectionUtil.isEmpty(goods)) {
			List<CfgGoods> list = goodsManager.goods(new Query().in("id", goods));
			Assert.isTrue(HasanCode.GOODS_NOT_EXIST, goods.size() == list.size());
		}
		CookbookText ct = new CookbookText();
		ct.setGoods(goods);
		ct.setCuisineGroups(cuisineGroups);
		return SerializeUtil.GSON.toJson(ct);
	}
	
	@Transactional
	public void cookbookDelete(SoaIdParam param) {
		long deleted = cfgCookbookDao.deleteByKey(param.getId());
		Assert.isTrue(HasanCode.COOKBOOK_NOT_EXIST, deleted == 1);
		cfgCookbookStepDao.deleteByQuery(new Query().eq("cookbook_id", param.getId()));
	}
	
	public int cookbookStepAdd(CookbookStepAddParam param) { 
		try {
			CfgCookbook cookbook = cfgCookbookDao.getByKey(param.getCookbookId());
			Assert.notNull(HasanCode.COOKBOOK_NOT_EXIST, cookbook);
			CfgCookbookStep step = EntityGenerator.newCfgCookbookStep(param);
			cfgCookbookStepDao.insert(step);
			return step.getId();
		} catch (DuplicateKeyException e) {
			throw new CodeException(HasanCode.COOKBOOK_STEP_PRIORITY_DUPLICATED, e);
		}
	}
	
	public void cookbookStepModify(CookbookStepModifyParam param) { 
		try {
			CfgCookbookStep step = cfgCookbookStepDao.getByKey(param.getId());
			Assert.notNull(HasanCode.COOKBOOK_STEP_NOT_EXIST, step);
			if (null != param.getPriority())
				step.setPriority(param.getPriority());
			if (StringUtil.hasText(param.getContent()))
				step.setContent(param.getContent());
			if (StringUtil.hasText(param.getName()))
				step.setName(param.getName());
			step.setUpdated(DateUtil.current());
			cfgCookbookStepDao.update(step);
		} catch (DuplicateKeyException e) {
			throw new CodeException(HasanCode.COOKBOOK_STEP_PRIORITY_DUPLICATED, e);
		}
	}
	
	public void cookbookStepDelete(SoaIdParam param) { 
		cfgCookbookStepDao.deleteByKey(param.getId());
	}
	
	public CfgCookbook cookbook(int cookbookId) {
		return cfgCookbookDao.getByKey(cookbookId);
	}
	
	public CfgCookbookStep cookbookStep(int id) {
		return cfgCookbookStepDao.getByKey(id);
	}
	
	public List<CfgCookbookStep> steps(int cookbookId) {
		return cfgCookbookStepDao.queryList(new Query().eq("cookbook_id", cookbookId));
	}
	
	public List<CfgCookbook> cookbooks(Query query) {
		return cfgCookbookDao.queryList(query);
	}
}
