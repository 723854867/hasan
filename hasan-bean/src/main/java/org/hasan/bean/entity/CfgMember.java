package org.hasan.bean.entity;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.gatlin.util.bean.Identifiable;
import org.gatlin.util.bean.enums.TimeUnit;

public class CfgMember implements Identifiable<Integer> {

	private static final long serialVersionUID = 2559795863023962405L;

	@Id
	@GeneratedValue
	private int id;
	private int expiry;
	private String name;
	private boolean sale;
	private TimeUnit timeUnit;
	private BigDecimal price;
	private int created;
	private int updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getExpiry() {
		return expiry;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isSale() {
		return sale;
	}

	public void setSale(boolean sale) {
		this.sale = sale;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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
	public Integer key() {
		return this.id;
	}
}
