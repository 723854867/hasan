package org.hasan.bean;

import org.gatlin.core.bean.model.code.Code;

public interface HasanCode {

	final Code ORDER_STATE_ERR					= new Code("code.hasan.order.state.err", "订单状态已改变");
	final Code ORDER_NOT_EXIST 					= new Code("code.hasan.order.not.exist", "订单不存在");
	final Code GOODS_NOT_EXIST 					= new Code("code.hasan.goods.not.exist", "商品不存在");
	final Code GOODS_INVENTORY_LACK				= new Code("code.hasan.goods.inventory.lack", "商品库存不足");
	final Code GOODS_OFF_SHELVES 				= new Code("code.hasan.goods.off.shelves", "商品已下架");
	final Code MEMBER_NOT_EXIST 				= new Code("code.hasan.member.not.exist", "会员配置不存在");
	final Code TIME_RANGE_CROSS 				= new Code("code.time.scheduler.cross", "时间区间有重叠");
	final Code SCHEDULER_NOT_EXIST 				= new Code("code.scheduler.not.exist", "日程配置不存在");
	final Code CUISINE_NOT_EXIST 				= new Code("code.hasan.cuisine.not.exist", "食材不存在");
	final Code COOKBOOK_NOT_EXIST 				= new Code("code.hasan.cookbook.not.exist", "菜谱不存在");
	final Code COOKBOOK_STEP_NOT_EXIST 			= new Code("code.hasan.cookbook.step.not.exist", "菜谱步骤不存在");
	final Code CUISINE_DELETE_CATEGORY_LINKED	= new Code("code.hasan.cuisine.delete.category.linked", "食材类型已经绑定食材，请先将食材解绑再删除");
	final Code CUISINE_CATEGORY_NOT_EXIST 		= new Code("code.hasan.cuisine.category.not.exist", "食材类型不存在");
}
