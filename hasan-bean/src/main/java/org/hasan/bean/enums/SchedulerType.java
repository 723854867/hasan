package org.hasan.bean.enums;

public enum SchedulerType {

	PACKAGE(1),
	DELIVERY(2);
	
	private int mark;
	
	private SchedulerType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
}
