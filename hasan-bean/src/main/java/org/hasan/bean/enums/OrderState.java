package org.hasan.bean.enums;

public enum OrderState {

	// 待支付
	PAID(1);
	
	private int mark;
	
	private OrderState(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
}
