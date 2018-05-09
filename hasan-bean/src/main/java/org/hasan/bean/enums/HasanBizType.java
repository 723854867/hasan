package org.hasan.bean.enums;

public enum HasanBizType {

	// 会员到期
	MEMBER_EXPIRY(1000);
	
	private int mark;
	
	private HasanBizType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
}
