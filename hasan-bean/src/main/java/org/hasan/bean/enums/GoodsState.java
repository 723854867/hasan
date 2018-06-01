package org.hasan.bean.enums;

import org.gatlin.util.bean.IEnum;

public enum GoodsState implements IEnum {

	SALE(1),
	OFF_SHELVES(2);
	
	private int mark;
	
	private GoodsState(int mark) {
		this.mark = mark;
	}
	
	@Override
	public int mark() {
		return mark;
	}
}
