package org.hasan.bean;

import org.gatlin.core.bean.model.code.Code;

public interface HasanCode {

	final Code ORDER_STATE_ERR							= new Code("code.hasan.order.state.err", "订单状态已改变");
	final Code ORDER_NOT_EXIST 							= new Code("code.hasan.order.not.exist", "订单不存在");
	final Code GOODS_NOT_EXIST 							= new Code("code.hasan.goods.not.exist", "商品不存在");
	final Code GOODS_PRICE_NOT_EXIST 					= new Code("code.hasan.goods.price.not.exist", "商品价格配置不存在");
	final Code GOODS_INVENTORY_LACK						= new Code("code.hasan.goods.inventory.lack", "商品库存不足");
	final Code GOODS_OFF_SHELVES 						= new Code("code.hasan.goods.off.shelves", "商品已下架");
	final Code MEMBER_NOT_EXIST 						= new Code("code.hasan.member.not.exist", "会员配置不存在");
	final Code TIME_RANGE_CROSS 						= new Code("code.hasan.time.scheduler.cross", "时间区间有重叠");
	final Code SCHEDULER_NOT_EXIST 						= new Code("code.hasan.scheduler.not.exist", "日程配置不存在");
	final Code ASSISTANT_ALLOCATE_DUPLICATED			= new Code("code.hasan.assiatant.allocate.duplicated", "用户已被分配给其他客服");
	final Code COOKBOOK_NOT_EXIST 						= new Code("code.hasan.cookbook.not.exist", "菜谱不存在");
	final Code COOKBOOK_STEP_PRIORITY_DUPLICATED		= new Code("code.hasan.cookbook.step.priority.duplicated", "菜谱序号重复");
	final Code COOKBOOK_STEP_NOT_EXIST 					= new Code("code.hasan.cookbook.step.not.exist", "菜谱步骤不存在");
	final Code VERSE_NOT_EXIST		 					= new Code("code.hasan.verse.not.exist", "诗句不存在");
	final Code MEMBER_COUNT_LIMIT	 					= new Code("code.hasan.member.count.limit", "会员购买次数限制");
}
