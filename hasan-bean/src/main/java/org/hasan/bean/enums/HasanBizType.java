package org.hasan.bean.enums;

public enum HasanBizType {

	// 会员购买成功
	MEMBER_BUY_OK(1000),
	// 会员到期
	MEMBER_EXPIRY(1001);
	
	private int mark;
	
	private HasanBizType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
}
