package org.hasan.bean.param;

import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.gatlin.soa.bean.param.SoaParam;

public class GoodsAddParam extends SoaParam {

	private static final long serialVersionUID = 247310712232423359L;

	@NotEmpty
	private String name;
	@NotEmpty
	private String desc;
	@Min(0)
	private int priority;
	@Min(0)
	private int inventory;
	@Min(1)
	private int cookbookId;
	@NotEmpty
	private Map<Integer, BigDecimal> prices;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getInventory() {
		return inventory;
	}
	
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	
	public int getCookbookId() {
		return cookbookId;
	}
	
	public void setCookbookId(int cookbookId) {
		this.cookbookId = cookbookId;
	}
	
	public Map<Integer, BigDecimal> getPrices() {
		return prices;
	}
	
	public void setPrices(Map<Integer, BigDecimal> prices) {
		this.prices = prices;
	}

	@Override
	public void verify() {
		super.verify();
	}
}
