package org.hasan.bean.param;

import org.gatlin.soa.user.bean.param.UserListParam;

public class AssistantUserListParam extends UserListParam {

	private static final long serialVersionUID = -8901231221551081459L;
	
	private Long assistant;

	public Long getAssistant() {
		return assistant;
	}
	
	public void setAssistant(Long assistant) {
		this.assistant = assistant;
	}
}
