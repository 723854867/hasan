package org.hasan.bean.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.gatlin.core.CoreCode;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.bean.param.SoaParam;
import org.hasan.bean.enums.SchedulerType;

public class SchedulerAddParam extends SoaParam {

	private static final long serialVersionUID = -7261812160872863316L;

	@Min(1)
	@Max(31)
	private int day;
	@Min(0)
	@Max(1439)
	private int stop;
	@Min(0)
	@Max(1439)
	private int start;
	@NotNull
	private SchedulerType type;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
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

	public SchedulerType getType() {
		return type;
	}

	public void setType(SchedulerType type) {
		this.type = type;
	}
	
	@Override
	public void verify() {
		super.verify();
		Assert.isTrue(CoreCode.PARAM_ERR, start < stop);
	}
}
