package org.hasan.bean.enums;

import org.gatlin.util.bean.IEnum;

public enum SchedulerType implements IEnum {

	PACKAGE(1),
	DELIVERY(2);
	
	private int mark;
	
	private SchedulerType(int mark) {
		this.mark = mark;
	}
	
	@Override
	public int mark() {
		return mark;
	}
}
