package org.hasan.bean.enums;

import org.gatlin.util.bean.IEnum;

public enum OrderState implements IEnum {

	// 待支付
	INIT(1),
	// 支付中
	PAYING(2),
	// 待发货
	PAID(3),
	// 待收货
	DELIVERED(4),
	// 待评价;
	RECEIVED(5),
	// 已完结
	FINISH(6);
	
	private int mark;
	
	private OrderState(int mark) {
		this.mark = mark;
	}
	
	@Override
	public int mark() {
		return mark;
	}
}
