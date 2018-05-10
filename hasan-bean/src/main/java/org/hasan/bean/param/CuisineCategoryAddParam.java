package org.hasan.bean.param;

import javax.validation.constraints.Min;

import org.gatlin.soa.bean.param.SoaNameIdParam;

public class CuisineCategoryAddParam extends SoaNameIdParam {

	private static final long serialVersionUID = -2469294047819951094L;

	@Min(1)
	private int priority;
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
}
