package org.hasan.bean.enums;

public enum CookbookMappingType {

	GOODS(1),
	CUISINE(2);
	
	private int mark;
	
	private CookbookMappingType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
}
