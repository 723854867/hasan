package org.hasan.bean.param;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
	@NotNull
	@DecimalMin("0.01")
	private BigDecimal VIPPrice;
	@NotNull
	@DecimalMin("0.01")
	private BigDecimal generalPrice;
	@NotNull
	@DecimalMin("0.01")
	private BigDecimal originalPrice;

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

	public BigDecimal getVIPPrice() {
		return VIPPrice;
	}

	public void setVIPPrice(BigDecimal vIPPrice) {
		VIPPrice = vIPPrice;
	}

	public BigDecimal getGeneralPrice() {
		return generalPrice;
	}

	public void setGeneralPrice(BigDecimal generalPrice) {
		this.generalPrice = generalPrice;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	@Override
	public void verify() {
		super.verify();
		VIPPrice.setScale(2, RoundingMode.DOWN);
		generalPrice.setScale(2, RoundingMode.DOWN);
		originalPrice.setScale(2, RoundingMode.DOWN);
	}
}
