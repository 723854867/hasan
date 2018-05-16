package org.hasan.bean.entity;

import javax.persistence.Id;

import org.gatlin.util.bean.Identifiable;

public class UserEvaluation implements Identifiable<String> {

	private static final long serialVersionUID = -8757395400671425645L;

	@Id
	private String id;
	private long uid;
	private int type;
	private int goodsId;
	private String content;
	private long orderGoodsId;
	private int created;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGoodsId() {
		return goodsId;
	}
	
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getOrderGoodsId() {
		return orderGoodsId;
	}

	public void setOrderGoodsId(long orderGoodsId) {
		this.orderGoodsId = orderGoodsId;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	@Override
	public String key() {
		return this.id;
	}
}
