package org.hasan.bean.param;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.gatlin.soa.bean.param.SoaNameParam;

public class CookbookAddParam extends SoaNameParam {

	private static final long serialVersionUID = -1054961894705916389L;

	private Set<Integer> goods;
	@NotEmpty
	private Set<Integer> cusines;
	
	public Set<Integer> getGoods() {
		return goods;
	}
	
	public void setGoods(Set<Integer> goods) {
		this.goods = goods;
	}
	
	public Set<Integer> getCusines() {
		return cusines;
	}
	
	public void setCusines(Set<Integer> cusines) {
		this.cusines = cusines;
	}
}
