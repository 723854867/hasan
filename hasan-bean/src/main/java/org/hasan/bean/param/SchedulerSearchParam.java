package org.hasan.bean.param;

import org.gatlin.dao.bean.Searcher;

public class SchedulerSearchParam extends Searcher {

	private static final long serialVersionUID = -7261812160872863316L;

	private Integer day;
	private Integer type;

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public void verify() {
		super.verify();
		if(null != day)
			query.eq("day", day);
		if(null != type)
			query.eq("type", type);
		query.orderByAsc("day","start");
	}
}
