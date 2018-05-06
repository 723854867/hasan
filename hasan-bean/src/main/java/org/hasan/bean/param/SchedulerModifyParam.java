package org.hasan.bean.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.gatlin.core.CoreCode;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.bean.param.SoaIdParam;

public class SchedulerModifyParam extends SoaIdParam {

	private static final long serialVersionUID = 1073725017586324737L;

	@Min(0)
	@Max(1439)
	private int stop;
	@Min(0)
	@Max(1439)
	private int start;
	
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
	
	@Override
	public void verify() {
		super.verify();
		Assert.isTrue(CoreCode.PARAM_ERR, start < stop);
	}
}
