package org.hasan.hook;

import javax.annotation.Resource;

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
public class ResourceHook extends org.gatlin.web.util.hook.ResourceHook {
	
	@Resource
	private GoodsManager goodsManager;
	@Resource
	private CookbookManager cookbookManager;

	@Override
	public void verify(CfgResource resource, ResourceUploadParam param) {
		switch (resource.getType()) {
		case 3:
			CfgGoods goods = goodsManager.goods((int) param.getOwner());
			Assert.notNull(HasanCode.GOODS_NOT_EXIST, goods);
			break;
		case 4:
			CfgCookbook cookbook = cookbookManager.cookbook((int) param.getOwner());
			Assert.notNull(HasanCode.COOKBOOK_NOT_EXIST, cookbook);
			break;
		case 5:
			CfgCookbookStep step = cookbookManager.cookbookStep((int) param.getOwner());
			Assert.notNull(HasanCode.COOKBOOK_STEP_NOT_EXIST, step);
			break;
		default:
			break;
		}
	}
}
