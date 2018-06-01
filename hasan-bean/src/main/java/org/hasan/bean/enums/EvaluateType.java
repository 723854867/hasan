package org.hasan.bean.enums;

import org.gatlin.util.bean.IEnum;

public enum EvaluateType implements IEnum {

	BAD(1),
	GENERAL(2),
	GOOD(3);
	
	private int mark;
	
	private EvaluateType(int mark) {
		this.mark = mark;
	}
	
	@Override
	public int mark() {
		return mark;
	}
}
