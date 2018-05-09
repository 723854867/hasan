package org.hasan.bean.entity;

import javax.persistence.Id;

import org.gatlin.core.bean.Entity;

public class UserCustom implements Entity<Long> {

	private static final long serialVersionUID = -3952053635287820560L;

	@Id
	private long uid;
	private int memberType;
	private long memberExpiry;
	private String memberTitle;
	private int created;
	private int updated;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public long getMemberExpiry() {
		return memberExpiry;
	}

	public void setMemberExpiry(long memberExpiry) {
		this.memberExpiry = memberExpiry;
	}
	
	public String getMemberTitle() {
		return memberTitle;
	}
	
	public void setMemberTitle(String memberTitle) {
		this.memberTitle = memberTitle;
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
