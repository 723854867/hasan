package org.hasan.bean.entity;

import javax.persistence.Id;

import org.gatlin.core.bean.Entity;

public class UserCustom implements Entity<Long> {

	private static final long serialVersionUID = -3952053635287820560L;

	@Id
	private long uid;
	private int memberId;
	private long memberExpiry;
	private int created;
	private int updated;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}
	
	public int getMemberId() {
		return memberId;
	}
	
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public long getMemberExpiry() {
		return memberExpiry;
	}

	public void setMemberExpiry(long memberExpiry) {
		this.memberExpiry = memberExpiry;
	}
	
	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	@Override
	public Long key() {
		return this.uid;
	}
}
