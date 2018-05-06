package org.hasan.bean;

import org.gatlin.core.bean.model.option.IntegerOption;

public interface HasanConsts {

	// 订单打包最小时间间隔
	final IntegerOption ORDER_ADVANCE_MINUTES				= new IntegerOption("order.advance.minutes", 30);
}
