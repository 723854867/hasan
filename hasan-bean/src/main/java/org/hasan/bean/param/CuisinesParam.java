package org.hasan.bean.param;

import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.util.lang.StringUtil;

public class CuisinesParam extends SoaParam {

	private static final long serialVersionUID = 7483997745451085114L;

	private String name;
	private Integer category;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getCategory() {
		return category;
	}
	
	public void setCategory(Integer category) {
		this.category = category;
	}
	
	@Override
	public void verify() {
		super.verify();
		if (null != category)
			this.query.eq("category", category);
		if (StringUtil.hasText(name))
			this.query.like("name", name);
	}
}
