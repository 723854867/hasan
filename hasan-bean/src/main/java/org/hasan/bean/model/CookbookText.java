package org.hasan.bean.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class CookbookText implements Serializable {

	private static final long serialVersionUID = -2679317372720501310L;

	private Set<Integer> goods;
	private List<CuisineGroup> cuisineGroups;
	
	public Set<Integer> getGoods() {
		return goods;
	}
	
	public void setGoods(Set<Integer> goods) {
		this.goods = goods;
	}
	
	public List<CuisineGroup> getCuisineGroups() {
		return cuisineGroups;
	}
	
	public void setCuisineGroups(List<CuisineGroup> cuisineGroups) {
		this.cuisineGroups = cuisineGroups;
	}
}
