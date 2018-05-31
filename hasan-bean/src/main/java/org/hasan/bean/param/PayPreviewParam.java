package org.hasan.bean.param;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.gatlin.soa.bean.param.SoaParam;

public class PayPreviewParam extends SoaParam {

	private static final long serialVersionUID = 7699782012296787794L;
	
	@NotEmpty
	private Map<Integer, Integer> goods;
	
	public Map<Integer, Integer> getGoods() {
		return goods;
	}
	
	public void setGoods(Map<Integer, Integer> goods) {
		this.goods = goods;
	}
}
