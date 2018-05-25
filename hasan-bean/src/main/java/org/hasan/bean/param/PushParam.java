package org.hasan.bean.param;

import javax.validation.constraints.NotEmpty;

import org.gatlin.soa.bean.param.SoaParam;

public class PushParam extends SoaParam {

	private static final long serialVersionUID = 7081783873868075357L;

	@NotEmpty
	private String title;
	@NotEmpty
	private String content;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
