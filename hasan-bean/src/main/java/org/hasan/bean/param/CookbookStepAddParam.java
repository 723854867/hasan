package org.hasan.bean.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.gatlin.soa.bean.param.SoaParam;

public class CookbookStepAddParam extends SoaParam {

	private static final long serialVersionUID = -4233702544885908482L;

	@NotEmpty
	@Size(min = 2, max = 10)
	private String name;
	@Min(1)
	private int priority;
	@Min(1)
	private int cookbookId;
	@NotEmpty
	private String content;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getCookbookId() {
		return cookbookId;
	}
	
	public void setCookbookId(int cookbookId) {
		this.cookbookId = cookbookId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
