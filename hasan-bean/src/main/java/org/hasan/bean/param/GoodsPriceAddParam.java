package org.hasan.bean.param;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.gatlin.soa.bean.param.SoaIdParam;

public class GoodsPriceAddParam extends SoaIdParam {

	private static final long serialVersionUID = -5721012066256821006L;

	@Min(0)
	private int memberId;
	@NotNull
	@DecimalMin("0.01")
	private BigDecimal price;
	
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
	
	@Override
	public void verify() {
		super.verify();
		price.setScale(2, RoundingMode.UP);
	}
}
