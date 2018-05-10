package org.hasan.bean.param;

import org.gatlin.soa.bean.param.SoaParam;
import org.hasan.bean.enums.OrderState;

public class OrdersParam extends SoaParam {

	private static final long serialVersionUID = 5327448828504100379L;

	private String id;
	private Long uid;
	private OrderState state;
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

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
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

	@Override
	public void verify() {
		super.verify();
		if (null != id)
			this.query.eq("id", id);
		if (null != uid)
			this.query.eq("uid", uid);
		if (null != state)
			this.query.eq("state", state.mark());
		if (null != deliverStart)
			this.query.gte("deliver_start", deliverStart);
		if (null != deliverStop)
			this.query.lte("deliver_stop", deliverStop);
		if (null != timeStart)
			this.query.gte("created", timeStart);
		if (null != timeStop)
			this.query.lte("created", timeStop);
		this.query.orderByDesc("created");
	}
}
