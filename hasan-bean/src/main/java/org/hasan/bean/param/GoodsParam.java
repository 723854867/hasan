package org.hasan.bean.param;

import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.enums.GoodsState;

public class GoodsParam extends SoaParam {

	private static final long serialVersionUID = 7748692473804257125L;

	private String name;
	private GoodsState state;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public GoodsState getState() {
		return state;
	}
	
	public void setState(GoodsState state) {
		this.state = state;
	}
	
	@Override
	public void verify() {
		super.verify();
		if (StringUtil.hasText(name))
			this.query.like("name", name);
		if (null != state)
			this.query.eq("state", state.mark());
		this.query.orderByAsc("priority", "created");
	}
}
