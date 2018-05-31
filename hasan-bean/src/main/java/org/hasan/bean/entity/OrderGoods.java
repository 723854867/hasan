package org.hasan.bean.entity;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.gatlin.util.bean.Identifiable;

public class OrderGoods implements Identifiable<Long> {

	private static final long serialVersionUID = 2267726928328574642L;

	@Id
	@GeneratedValue
	private long id;
	private int goodsId;
	private int goodsNum;
	private String orderId;
	private String goodsName;
	private String goodsDesc;
	private String evaluationId;
	private BigDecimal unitPrice;
	private int created;
	private int updated;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	
	public String getEvaluationId() {
		return evaluationId;
	}
	
	public void setEvaluationId(String evaluationId) {
		this.evaluationId = evaluationId;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
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
		return this.id;
	}
}
