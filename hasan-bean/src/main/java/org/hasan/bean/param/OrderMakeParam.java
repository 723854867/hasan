package org.hasan.bean.param;

import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.gatlin.core.CoreCode;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.util.DateUtil;

public class OrderMakeParam extends SoaParam {

	private static final long serialVersionUID = -9024211217370539105L;

	@Min(1)
	private long addressId;
	@Min(0)
	private int deliverStart;
	@Min(0)
	private int deliverStop;
	@NotEmpty
	private Map<Integer, Integer> goods;
	
	public long getAddressId() {
		return addressId;
	}
	
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	
	public Map<Integer, Integer> getGoods() {
		return goods;
	}
	
	public void setGoods(Map<Integer, Integer> goods) {
		this.goods = goods;
	}
	
	public int getDeliverStart() {
		return deliverStart;
	}
	
	public void setDeliverStart(int deliverStart) {
		this.deliverStart = deliverStart;
	}
	
	public int getDeliverStop() {
		return deliverStop;
	}
	
	public void setDeliverStop(int deliverStop) {
		this.deliverStop = deliverStop;
	}
	
	@Override
	public void verify() {
		super.verify();
		Assert.isTrue(CoreCode.PARAM_ERR, deliverStop>= deliverStart && DateUtil.current() < deliverStart);
	}
}
