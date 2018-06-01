package org.hasan.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.gatlin.util.bean.Identifiable;
import org.hasan.bean.enums.SchedulerType;

public class CfgScheduler implements Identifiable<Integer> {

	private static final long serialVersionUID = -6372093585559648077L;

	@Id
	@GeneratedValue
	private int id;
	private int day;
	private int start;
	private int stop;
	private SchedulerType type;
	private int created;
	private int updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStop() {
		return stop;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}
	
	public SchedulerType getType() {
		return type;
	}
	
	public void setType(SchedulerType type) {
		this.type = type;
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
