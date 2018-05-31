package org.hasan.bean;

import java.math.BigDecimal;

import org.gatlin.core.bean.model.option.DecimalOption;
import org.gatlin.core.bean.model.option.IntegerOption;
import org.gatlin.core.bean.model.option.StrOption;

public interface HasanConsts {

	// 默认会员名称
	final StrOption DEFAULT_MEMBER_TITLE					= new StrOption("default_member_title", "普通会员");
	final IntegerOption DEFAULT_MEMBER_ID					= new IntegerOption("default_member_id", 0);
	// 订单打包最小时间间隔
	final IntegerOption ORDER_ADVANCE_MINUTES				= new IntegerOption("order_advance_minutes", 30);
	// 默认显示评价数
	final IntegerOption DEFAULT_EVALUATION_NUM				= new IntegerOption("default_evaluation_num", 3);
	// 默认快递费
	final DecimalOption EXPRESS_FEE							= new DecimalOption("express_fee", BigDecimal.valueOf(15));
	// 配送可预约时间最大显示数
	final IntegerOption DELIVERY_TIME_MAXIMUM				= new IntegerOption("delivery_time_maximum", 3);
}
