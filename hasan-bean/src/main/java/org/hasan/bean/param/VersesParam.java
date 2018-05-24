package org.hasan.bean.param;

import org.gatlin.soa.bean.param.SoaParam;
import org.gatlin.util.lang.StringUtil;

public class VersesParam extends SoaParam {

	private static final long serialVersionUID = -9072043847804254279L;
	
	private Integer id;
	private String content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void verify() {
		super.verify();
		if (null != id)
			this.query.eq("id", id);
		if (StringUtil.hasText(content))
			this.query.like("content", content);
		this.query.orderByDesc("created");
	}
}
