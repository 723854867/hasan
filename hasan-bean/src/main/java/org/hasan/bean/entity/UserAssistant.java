package org.hasan.bean.entity;

import javax.persistence.Id;

import org.gatlin.core.bean.Entity;

public class UserAssistant implements Entity<Long> {

	private static final long serialVersionUID = -6906548432171602826L;

	@Id
	private long uid;
	private long assistant;
	private int created;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getAssistant() {
		return assistant;
	}
	
	public void setAssistant(long assistant) {
		this.assistant = assistant;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	@Override
	public Long key() {
		return this.uid;
	}
}
