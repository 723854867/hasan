package org.hasan.bean.model;

import java.io.Serializable;

public class TimeRange implements Serializable {

	private static final long serialVersionUID = 5515643397258603376L;

	private int stop;
	private int start;
	
	public TimeRange() {}
	
	public TimeRange(int stop, int start) {
		this.stop = stop;
		this.start = start;
	}

	public int getStop() {
		return stop;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
