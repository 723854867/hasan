package org.hasan.bean.entity;

import java.math.BigDecimal;

import javax.persistence.Id;
import javax.persistence.Table;

import org.gatlin.util.bean.Identifiable;
import org.hasan.bean.enums.OrderState;

@Table(name="`order`")
public class Order implements Identifiable<String> {

	private static final long serialVersionUID = 2327138052108096069L;

	@Id
	private String id;
	private long uid;
	private String ip;
	private OrderState state;
	private BigDecimal price;
	private String recipients;
	private String expressNo;
	private BigDecimal expressFee;
	private String recipientsAddr;
	private String recipientsMobile;
	private int deliverStart;
	private int deliverStop;
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
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public OrderState getState() {
		return state;
	}
	
	public void setState(OrderState state) {
		this.state = state;
	}

	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getRecipients() {
		return recipients;
	}
	
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	
	public String getExpressNo() {
		return expressNo;
	}
	
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	
	public BigDecimal getExpressFee() {
		return expressFee;
	}
	
	public void setExpressFee(BigDecimal expressFee) {
		this.expressFee = expressFee;
	}

	public String getRecipientsAddr() {
		return recipientsAddr;
	}

	public void setRecipientsAddr(String recipientsAddr) {
		this.recipientsAddr = recipientsAddr;
	}

	public String getRecipientsMobile() {
		return recipientsMobile;
	}

	public void setRecipientsMobile(String recipientsMobile) {
		this.recipientsMobile = recipientsMobile;
	}
	
	public int getDeliverStart() {
		return deliverStart;
	}
	
	public void setDeliverStart(int deliverStart) {
		this.deliverStart = deliverStart;
	}
	
	public int getDeliverStop() {
		return deliverStop;
	}
	
	public void setDeliverStop(int deliverStop) {
		this.deliverStop = deliverStop;
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
