package org.hasan.bean.enums;

public enum OrderState {

	// 待支付
	INIT(1),
	// 待发货
	PAID(2),
	// 待收货
	DELIVERED(4),
	// 待评价;
	RECEIVED(8),
	// 已完结
	FINISH(16);
	
	private int mark;
	
	private OrderState(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
}
