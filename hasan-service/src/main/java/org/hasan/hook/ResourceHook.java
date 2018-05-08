package org.hasan.hook;

import javax.annotation.Resource;

import org.gatlin.core.util.Assert;
import org.gatlin.soa.resource.bean.entity.CfgResource;
import org.gatlin.web.bean.param.ResourceUploadParam;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.manager.GoodsManager;
import org.springframework.stereotype.Component;

@Component
public class ResourceHook extends org.gatlin.web.util.hook.ResourceHook {
	
	@Resource
	private GoodsManager goodsManager;

	@Override
	public void verify(CfgResource resource, ResourceUploadParam param) {
		switch (resource.getType()) {
		case 3:
			CfgGoods goods = goodsManager.goods((int) param.getOwner());
			Assert.notNull(HasanCode.GOODS_NOT_EXIST, goods);
			break;
		default:
			break;
		}
	}
}
