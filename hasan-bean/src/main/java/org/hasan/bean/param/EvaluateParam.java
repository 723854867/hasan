package org.hasan.bean.param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.gatlin.soa.bean.param.SoaLidParam;
import org.hasan.bean.enums.EvaluateType;

public class EvaluateParam extends SoaLidParam {

	private static final long serialVersionUID = -3743582373120299367L;

	@NotEmpty
	private String content;
	@NotNull
	private EvaluateType type;
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public EvaluateType getType() {
		return type;
	}
	
	public void setType(EvaluateType type) {
		this.type = type;
	}
}
