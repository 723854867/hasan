package org.hasan.bean.enums;

public enum EvaluateType {

	BAD(1),
	GENERAL(2),
	GOOD(3);
	
	private int mark;
	
	private EvaluateType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public static final EvaluateType match(int type) {
		for (EvaluateType temp : EvaluateType.values()) {
			if (temp.mark == type)
				return temp;
		}
		return null;
	}
}
