package org.hasan.bean.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.enums.OrderState;

public class OrderListInfo implements Serializable{

	private static final long serialVersionUID = -6908273169778600632L;
	
	private String id;
	private long uid;
	private String ip;
	private OrderState state;
	private BigDecimal price;
	private String recipients;
	private BigDecimal expressFee;
	private String recipientsAddr;
	private String recipientsMobile;
	private int deliverStart;
	private int deliverStop;
	private int created;
	private int count;
	private List<GoodsInfo> goods;
	
	public OrderListInfo(){}
	
	public OrderListInfo(Order order,List<GoodsInfo> goods,List<OrderGoods> orderGoods) {
		this.id = order.getId();
		this.uid = order.getUid();
		this.ip = order.getIp();
		this.expressFee = order.getExpressFee();
		this.state = OrderState.match(order.getState());
		this.price = order.getPrice();
		this.recipients = order.getRecipients();
		this.recipientsAddr = order.getRecipientsAddr();
		this.recipientsMobile = order.getRecipientsMobile();
		this.deliverStart = order.getDeliverStart();
		this.deliverStop = order.getDeliverStop();
		this.created = order.getCreated();
		this.goods = goods;
		orderGoods.forEach(item->count=count+item.getGoodsNum());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<GoodsInfo> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsInfo> goods) {
		this.goods = goods;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public BigDecimal getExpressFee() {
		return expressFee;
	}

	public void setExpressFee(BigDecimal expressFee) {
		this.expressFee = expressFee;
	}

	public String getRecipientsAddr() {
		return recipientsAddr;
	}

	public void setRecipientsAddr(String recipientsAddr) {
		this.recipientsAddr = recipientsAddr;
	}

	public String getRecipientsMobile() {
		return recipientsMobile;
	}

	public void setRecipientsMobile(String recipientsMobile) {
		this.recipientsMobile = recipientsMobile;
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
	
}
