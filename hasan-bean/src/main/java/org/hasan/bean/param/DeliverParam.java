package org.hasan.bean.param;

import javax.validation.constraints.NotEmpty;

import org.gatlin.soa.bean.param.SoaSidParam;

public class DeliverParam extends SoaSidParam {

	private static final long serialVersionUID = -5751565535377328062L;

	@NotEmpty
	private String expressNo;
	
	public String getExpressNo() {
		return expressNo;
	}
	
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
}
