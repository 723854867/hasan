package org.hasan.bean.param;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import org.gatlin.core.CoreCode;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.util.bean.enums.TimeUnit;

public class MemberModifyParam extends SoaIdParam {

	private static final long serialVersionUID = 1058898741601663742L;

	private String name;
	@Min(1)
	private Integer expiry;
	private Boolean sale;
	@DecimalMin("0.01")
	private BigDecimal price;
	private TimeUnit timeUnit;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Integer getExpiry() {
		return expiry;
	}

	public void setExpiry(Integer expiry) {
		this.expiry = expiry;
	}

	public Boolean getSale() {
		return sale;
	}

	public void setSale(Boolean sale) {
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
	
	@Override
	public void verify() {
		super.verify();
		if (null != expiry) 
			Assert.notNull(CoreCode.PARAM_ERR, timeUnit);
	}
}
