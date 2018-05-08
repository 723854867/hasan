package org.hasan.bean.param;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.util.bean.enums.TimeUnit;
import org.hasan.bean.enums.MemberType;

public class MemberAddParam extends SoaParam {

	private static final long serialVersionUID = -9156619347431298393L;

	@NotEmpty
	private String name;
	@Min(1)
	private int expiry;
	private boolean sale;
	@NotNull
	@DecimalMin("0.01")
	private BigDecimal price;
	@NotNull
	private TimeUnit timeUnit;
	@NotNull
	private MemberType memberType;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getExpiry() {
		return expiry;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	public boolean isSale() {
		return sale;
	}

	public void setSale(boolean sale) {
		this.sale = sale;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	public MemberType getMemberType() {
		return memberType;
	}
	
	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}
}
