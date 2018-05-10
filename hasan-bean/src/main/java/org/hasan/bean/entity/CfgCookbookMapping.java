package org.hasan.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.gatlin.core.bean.Entity;

public class CfgCookbookMapping implements Entity<Integer> {

	private static final long serialVersionUID = 3086872745847659691L;

	@Id
	@GeneratedValue
	private int id;
	private int tid;
	private int type;
	private int cookbookId;
	private int created;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTid() {
		return tid;
	}
	
	public void setTid(int tid) {
		this.tid = tid;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public int getCookbookId() {
		return cookbookId;
	}

	public void setCookbookId(int cookbookId) {
		this.cookbookId = cookbookId;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	@Override
	public Integer key() {
		return this.id;
	}
}
