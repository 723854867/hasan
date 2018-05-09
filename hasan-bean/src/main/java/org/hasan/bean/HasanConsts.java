package org.hasan.bean;

import org.gatlin.core.bean.model.option.IntegerOption;
import org.gatlin.core.bean.model.option.StrOption;

public interface HasanConsts {

	final StrOption DEFAULT_MEMBER_TITLE					= new StrOption("default.member.title", "普通会员");
	// 订单打包最小时间间隔
	final IntegerOption ORDER_ADVANCE_MINUTES				= new IntegerOption("order.advance.minutes", 30);
}
