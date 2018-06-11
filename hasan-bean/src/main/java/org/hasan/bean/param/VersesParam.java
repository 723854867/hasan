package org.hasan.bean.param;

import javax.validation.constraints.NotNull;

import org.gatlin.soa.bean.param.SoaParam;

public class VersesParam extends SoaParam {

	private static final long serialVersionUID = -9072043847804254279L;
	
	private Integer id;
	@NotNull
	private String content1;
	@NotNull
	private String content2;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent1() {
		return content1;
	}

	public void setContent1(String content1) {
		this.content1 = content1;
	}

	public String getContent2() {
		return content2;
	}

	public void setContent2(String content2) {
		this.content2 = content2;
	}

	@Override
	public void verify() {
		super.verify();
	}
}
