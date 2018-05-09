package org.hasan.bean.enums;

public enum OrderState {

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
	
	public int mark() {
		return mark;
	}
	
	public static final OrderState match(int state) {
		for (OrderState temp : OrderState.values()) {
			if (temp.mark == state)
				return temp;
		}
		return null;
	}
}
