package org.hasan.bean.param;

import javax.validation.constraints.Min;

import org.gatlin.soa.bean.param.SoaIdParam;

public class CookbookStepModifyParam extends SoaIdParam {

	private static final long serialVersionUID = 7968430014952615741L;

	private String name;
	private String content;
	@Min(1)
	private Integer priority;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
