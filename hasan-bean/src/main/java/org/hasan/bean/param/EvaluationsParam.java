package org.hasan.bean.param;

import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.enums.EvaluateType;

public class EvaluationsParam extends SoaParam {

	private static final long serialVersionUID = -2569745244547966926L;

	private String id;
	private Long uid;
	private Long goodsId;
	private String content;
	private EvaluateType type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public EvaluateType getType() {
		return type;
	}

	public void setType(EvaluateType type) {
		this.type = type;
	}
	
	@Override
	public void verify() {
		super.verify();
		if (StringUtil.hasText(id))
			this.query.eq("id", id);
		if (null != uid)
			this.query.eq("uid", uid);
		if (null != goodsId)
			this.query.eq("goods_id", goodsId);
		if (null != type)
			this.query.eq("type", type.mark());
		if (StringUtil.hasText(content))
			this.query.like("content", content);
		this.query.orderByDesc("created");
	}
}
