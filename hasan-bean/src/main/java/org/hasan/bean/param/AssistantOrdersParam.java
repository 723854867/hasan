package org.hasan.bean.param;

import org.gatlin.soa.bean.param.SoaParam;

public class AssistantOrdersParam extends SoaParam {

	private static final long serialVersionUID = -6411075723321778662L;

	private String id;
	private Long uid;
	private Integer state;
	private Long assistant;
	private Integer deliverStart;
	private Integer deliverStop;
	private Integer timeStart;
	private Integer timeStop;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getAssistant() {
		return assistant;
	}

	public void setAssistant(Long assistant) {
		this.assistant = assistant;
	}

	public Integer getDeliverStart() {
		return deliverStart;
	}

	public void setDeliverStart(Integer deliverStart) {
		this.deliverStart = deliverStart;
	}

	public Integer getDeliverStop() {
		return deliverStop;
	}

	public void setDeliverStop(Integer deliverStop) {
		this.deliverStop = deliverStop;
	}

	public Integer getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Integer timeStart) {
		this.timeStart = timeStart;
	}

	public Integer getTimeStop() {
		return timeStop;
	}

	public void setTimeStop(Integer timeStop) {
		this.timeStop = timeStop;
	}
}
