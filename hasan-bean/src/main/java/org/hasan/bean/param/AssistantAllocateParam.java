package org.hasan.bean.param;

import javax.validation.constraints.Min;

import org.gatlin.soa.bean.param.SoaLidParam;

public class AssistantAllocateParam extends SoaLidParam {

	private static final long serialVersionUID = -442471364541258601L;

	@Min(1)
	private long assistant;
	
	public long getAssistant() {
		return assistant;
	}
	
	public void setAssistant(long assistant) {
		this.assistant = assistant;
	}
}
