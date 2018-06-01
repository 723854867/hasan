package org.hasan.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.gatlin.util.bean.Identifiable;
import org.hasan.bean.enums.GoodsState;

public class CfgGoods implements Identifiable<Integer> {

	private static final long serialVersionUID = -8365325728478524537L;

	@Id
	@GeneratedValue
	private int id;
	private int sold;
	private String name;
	private String desc;
	private int priority;
	private int inventory;
	private int cookbookId;
	private GoodsState state;
	private int created;
	private int updated;

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
	
	public int getCookbookId() {
		return cookbookId;
	}
	
	public void setCookbookId(int cookbookId) {
		this.cookbookId = cookbookId;
	}
	
	public GoodsState getState() {
		return state;
	}
	
	public void setState(GoodsState state) {
		this.state = state;
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
