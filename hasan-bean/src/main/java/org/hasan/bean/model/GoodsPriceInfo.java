package org.hasan.bean.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hasan.bean.entity.CfgGoodsPrice;
import org.hasan.bean.entity.CfgMember;

public class GoodsPriceInfo implements Serializable {

	private static final long serialVersionUID = -8581710508034728451L;

	private int id;
	private int memberId;
	private BigDecimal price;
	private String memberName;
	
	public GoodsPriceInfo() {}
	
	public GoodsPriceInfo(CfgGoodsPrice price, CfgMember member) {
		this.id = price.getId();
		this.price = price.getPrice();
		this.memberName = member.getName();
		this.memberId = price.getMemberId();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberId() {
		return memberId;
	}
	
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
}
