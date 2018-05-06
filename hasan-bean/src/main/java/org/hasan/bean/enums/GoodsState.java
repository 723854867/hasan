package org.hasan.bean.enums;

public enum GoodsState {

	SALE(1),
	OFF_SHELVES(2);
	
	private int mark;
	
	private GoodsState(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public static final GoodsState match(int mark) {
		for (GoodsState temp : GoodsState.values()) {
			if (temp.mark == mark)
				return temp;
		}
		return null;
	}
}
