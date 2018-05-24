package org.hasan.bean.entity;

import java.math.BigDecimal;

import javax.persistence.Id;

import org.gatlin.util.bean.Identifiable;

public class LogOrderPay implements Identifiable<String> {

	private static final long serialVersionUID = 5503021558004788847L;

	@Id
	private String id;
	private long uid;
	private String orderId;
	private BigDecimal expAmount;
	private BigDecimal expressFee;
	private BigDecimal basicAmount;
	private BigDecimal rechargeAmount;
	private int created;
	private int updated;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public long getUid() {
		return uid;
	}
	
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public BigDecimal getExpAmount() {
		return expAmount;
	}

	public void setExpAmount(BigDecimal expAmount) {
		this.expAmount = expAmount;
	}
	
	public BigDecimal getExpressFee() {
		return expressFee;
	}
	
	public void setExpressFee(BigDecimal expressFee) {
		this.expressFee = expressFee;
	}

	public BigDecimal getBasicAmount() {
		return basicAmount;
	}

	public void setBasicAmount(BigDecimal basicAmount) {
		this.basicAmount = basicAmount;
	}
	
	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}
	
	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
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
	public String key() {
		return this.id;
	}
}
