package org.hasan.bean.model;

import java.io.Serializable;

import org.gatlin.soa.bean.model.ResourceInfo;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.enums.GoodsState;

public class GoodsInfo implements Serializable {

	private static final long serialVersionUID = -5212597522523888585L;

	private int id;
	private int sold;
	private String name;
	private int created;
	private int inventory;
	private GoodsState state;
	private ResourceInfo icon;
	
	public GoodsInfo() {}
	
	public GoodsInfo(CfgGoods goods, ResourceInfo icon) {
		this.icon = icon;
		this.id = goods.getId();
		this.sold = goods.getSold();
		this.name = goods.getName();
		this.created = goods.getCreated();
		this.inventory = goods.getInventory();
		this.state = GoodsState.match(goods.getState());
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

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
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

	public ResourceInfo getIcon() {
		return icon;
	}

	public void setIcon(ResourceInfo icon) {
		this.icon = icon;
	}
}
