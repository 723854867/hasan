package org.hasan.hook;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.resource.bean.entity.CfgResource;
import org.gatlin.web.bean.param.ResourceUploadParam;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgCookbookStep;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.manager.CookbookManager;
import org.hasan.manager.GoodsManager;
import org.springframework.stereotype.Component;

@Component
public class ResourceHook extends org.gatlin.web.util.validator.ResourceHook {
	
	@Resource
	private GoodsManager goodsManager;
	@Resource
	private CookbookManager cookbookManager;

	@Override
	public void verify(CfgResource resource, ResourceUploadParam param) {
		switch (resource.getType()) {
		case 100:
			int cfgGoodsId = param.intOnwer();
			CfgGoods goods = goodsManager.goods(cfgGoodsId);
			Assert.notNull(HasanCode.GOODS_NOT_EXIST, goods);
			break;
		case 101:
			int id = param.intOnwer();
			CfgCookbook cookbook = cookbookManager.cookbook(id);
			Assert.notNull(HasanCode.COOKBOOK_NOT_EXIST, cookbook);
			break;
		case 102:
			int stepId = param.intOnwer();
			CfgCookbookStep step = cookbookManager.cookbookStep(stepId);
			Assert.notNull(HasanCode.COOKBOOK_STEP_NOT_EXIST, step);
			break;
		default:
			throw new CodeException(CoreCode.FORBID);
		}
	}
}
