package org.hasan.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.gatlin.core.bean.Entity;

public class CfgCuisineCategory implements Entity<Integer> {

	private static final long serialVersionUID = 1841694795186836083L;

	@Id
	@GeneratedValue
	private int id;
	private String name;
	private int priority;
	private int created;
	private int updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
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
	public Integer key() {
		return this.id;
	}
}
