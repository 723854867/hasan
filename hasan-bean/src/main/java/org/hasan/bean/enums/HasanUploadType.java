package org.hasan.bean.enums;

import org.gatlin.soa.resource.bean.enums.DefaultUploadType;
import org.gatlin.soa.resource.bean.enums.UploadType;

public enum HasanUploadType implements UploadType {

	// 登录注册资源
	LOGIN_REGISTER(100);
	
	private int mark;
	
	private HasanUploadType(int mark) {
		this.mark = mark;
	}
	
	@Override
	public int key() {
		return mark;
	}
	
	@Override
	public String directory() {
		return name().toLowerCase();
	}
	
	public static final UploadType match(int type) {
		for (HasanUploadType temp : HasanUploadType.values()) {
			if (temp.mark == type)
				return temp;
		}
		return DefaultUploadType.match(type);
	}
}
