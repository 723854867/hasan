package org.hasan.bean.model;

import java.io.Serializable;

import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.gatlin.soa.user.bean.entity.Username;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.entity.UserEvaluation;
import org.hasan.bean.enums.EvaluateType;

public class EvaluationInfo implements Serializable {

	private static final long serialVersionUID = -4495636190747943680L;

	private long uid;
	private String content;
	private String avatar;
	private EvaluateType type;
	private String mobile;
	private int created;
	
	public EvaluationInfo() {}
	
	public EvaluationInfo(UserEvaluation evaluation, ResourceInfo avatar, Username username) {
		this.uid = evaluation.getUid();
		this.content = evaluation.getContent();
		this.type = EvaluateType.match(evaluation.getType());
		this.created = evaluation.getCreated();
		if (null != avatar)
			this.avatar = avatar.getUrl();
		if (null != username)
			this.mobile = StringUtil.mask(username.getUsername(), 5, 4);
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public EvaluateType getType() {
		return type;
	}

	public void setType(EvaluateType type) {
		this.type = type;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}
}
