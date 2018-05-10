package org.hasan.bean;

import org.gatlin.core.bean.model.option.IntegerOption;
import org.gatlin.core.bean.model.option.StrOption;

public interface HasanConsts {

	// 默认会员名称
	final StrOption DEFAULT_MEMBER_TITLE					= new StrOption("default.member.title", "普通会员");
	// 订单打包最小时间间隔
	final IntegerOption ORDER_ADVANCE_MINUTES				= new IntegerOption("order.advance.minutes", 30);
	// 默认显示评价数
	final IntegerOption DEFAULT_EVALUATION_NUM				= new IntegerOption("default.evaluation.num", 3);
}
