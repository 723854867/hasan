package org.hasan.bean.info;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.enums.GoodsState;
import org.hasan.bean.enums.HasanResourceType;

public class CfgGoodsInfo implements Serializable {

	private static final long serialVersionUID = -5212597522523888585L;

	private int id;
	private int sold;
	private String name;
	private String desc;
	private int priority;
	private int inventory;
	private GoodsState state;
	private BigDecimal VIPPrice;
	private BigDecimal generalPrice;
	private BigDecimal originalPrice;
	private Map<HasanResourceType, List<ResourceInfo>> resources;
	private int created;
	
	public CfgGoodsInfo() {}
	
	public CfgGoodsInfo(CfgGoods goods) {
		this(goods, new HashMap<HasanResourceType, List<ResourceInfo>>());
	}
	
	public CfgGoodsInfo(CfgGoods goods, Map<HasanResourceType, List<ResourceInfo>> resources) {
		this.id = goods.getId();
		this.sold = goods.getSold();
		this.name = goods.getName();
		this.desc = goods.getDesc();
		this.priority = goods.getPriority();
		this.inventory = goods.getInventory();
		this.state = GoodsState.match(goods.getState());
		this.VIPPrice = goods.getVIPPrice();
		this.generalPrice = goods.getGeneralPrice();
		this.originalPrice = goods.getOriginalPrice();
		this.created = goods.getCreated();
		this.resources = resources;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

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
	
	public GoodsState getState() {
		return state;
	}
	
	public void setState(GoodsState state) {
		this.state = state;
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

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public Map<HasanResourceType, List<ResourceInfo>> getResources() {
		return resources;
	}

	public void setResources(Map<HasanResourceType, List<ResourceInfo>> resources) {
		this.resources = resources;
	}
}
