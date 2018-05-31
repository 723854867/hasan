package org.hasan.bean.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.hasan.bean.entity.CfgGoodsPrice;
import org.hasan.bean.entity.LogOrderPay;

public class PayPreview implements Serializable {

	private static final long serialVersionUID = -1029931028443629209L;

	private BigDecimal expAmount;
	private BigDecimal expressFee;
	private BigDecimal basicAmount;
	private BigDecimal rechargeAmount;
	private Map<Integer, BigDecimal> prices = new HashMap<Integer, BigDecimal>();
	
	public PayPreview() {
	}
	
	public PayPreview(LogOrderPay pay, Map<Integer, CfgGoodsPrice> prices) {
		this.expAmount = pay.getExpAmount();
		this.expressFee = pay.getExpressFee();
		this.basicAmount = pay.getBasicAmount();
		this.rechargeAmount = pay.getRechargeAmount();
		for (CfgGoodsPrice price : prices.values())
			this.prices.put(price.getGoodsId(), price.getPrice());
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

	public Map<Integer, BigDecimal> getPrices() {
		return prices;
	}

	public void setPrices(Map<Integer, BigDecimal> prices) {
		this.prices = prices;
	}
}
