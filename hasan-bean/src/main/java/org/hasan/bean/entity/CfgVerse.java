package org.hasan.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.gatlin.util.bean.Identifiable;

public class CfgVerse implements Identifiable<Integer> {

	private static final long serialVersionUID = -723028625803844048L;

	@Id
	@GeneratedValue
	private int id;
	private String content;
	private int created;
	private int updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
