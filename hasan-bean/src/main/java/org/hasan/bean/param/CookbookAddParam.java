package org.hasan.bean.param;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.gatlin.soa.bean.param.SoaNameParam;
import org.hasan.bean.model.CuisineGroup;

public class CookbookAddParam extends SoaNameParam {

	private static final long serialVersionUID = -1054961894705916389L;

	private Set<Integer> goods;
	@Valid
	@NotEmpty
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
