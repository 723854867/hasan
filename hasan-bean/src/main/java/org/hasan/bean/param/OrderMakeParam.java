package org.hasan.bean.param;

import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.gatlin.soa.bean.param.SoaParam;

public class OrderMakeParam extends SoaParam {

	private static final long serialVersionUID = -9024211217370539105L;

	@Min(1)
	private long addressId;
	private Integer deliverSchedulerId;
	@NotEmpty
	private Map<Integer, Integer> goods;
	
	public long getAddressId() {
		return addressId;
	}
	
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	
	public Integer getDeliverSchedulerId() {
		return deliverSchedulerId;
	}
	
	public void setDeliverSchedulerId(Integer deliverSchedulerId) {
		this.deliverSchedulerId = deliverSchedulerId;
	}
	
	public Map<Integer, Integer> getGoods() {
		return goods;
	}
	
	public void setGoods(Map<Integer, Integer> goods) {
		this.goods = goods;
	}
}
