package org.hasan.bean.param;

import org.gatlin.soa.bean.param.SoaParam;

public class MembersParam extends SoaParam {

	private static final long serialVersionUID = -4528070460920780186L;

	private Boolean sale;
	
	public Boolean getSale() {
		return sale;
	}
	
	public void setSale(Boolean sale) {
		this.sale = sale;
	}
	
	@Override
	public void verify() {
		super.verify();
		if (null != sale)
			this.query.eq("sale", sale ? 1 : 0);
	}
}
