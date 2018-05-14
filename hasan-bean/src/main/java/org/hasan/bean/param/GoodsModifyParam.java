package org.hasan.bean.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.gatlin.soa.bean.param.SoaIdParam;
import org.hasan.bean.enums.GoodsState;

public class GoodsModifyParam extends SoaIdParam {

	private static final long serialVersionUID = 3347172202547661563L;

	@NotEmpty
	private String name;
	@NotEmpty
	private String desc;
	@Min(0)
	private int priority;
	@Min(0)
	private int inventory;
	@Min(0)
	private Integer cookbookId;
	@NotNull
	private GoodsState state;

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
	
	public GoodsState getState() {
		return state;
	}
	
	public void setState(GoodsState state) {
		this.state = state;
	}
	
	public int getInventory() {
		return inventory;
	}
	
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	
	public Integer getCookbookId() {
		return cookbookId;
	}
	
	public void setCookbookId(Integer cookbookId) {
		this.cookbookId = cookbookId;
	}

	@Override
	public void verify() {
		super.verify();
	}
}
