package org.hasan.bean.model;

import java.util.List;

import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;

public class OrderDetail extends OrderInfo {

	private static final long serialVersionUID = 7082981059293144737L;

	private List<OrderGoods> goods;
	
	public OrderDetail() {}
	
	public OrderDetail(Order order, List<OrderGoods> goods) {
		super(order);
		this.goods = goods;
	}

	public List<OrderGoods> getGoods() {
		return goods;
	}

	public void setGoods(List<OrderGoods> goods) {
		this.goods = goods;
	}
}
