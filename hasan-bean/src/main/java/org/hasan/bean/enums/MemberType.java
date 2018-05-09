package org.hasan.bean.enums;

public enum MemberType {

	ORIGINAL(1),
	GENERAL(2),
	VIP(3);
	
	private int mark;
	
	private MemberType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public static final MemberType match(int type) { 
		for (MemberType temp : MemberType.values()) {
			if (temp.mark == type)
				return temp;
		}
		return null;
	}
}
