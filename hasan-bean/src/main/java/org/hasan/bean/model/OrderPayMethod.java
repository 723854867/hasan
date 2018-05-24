package org.hasan.bean.model;

import java.io.Serializable;

import org.hasan.bean.entity.LogOrderPay;

public class OrderPayMethod implements Serializable {

	private static final long serialVersionUID = 2682621523390081371L;

	private String auth;
	private LogOrderPay detail;
	
	public String getAuth() {
		return auth;
	}
	
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	public LogOrderPay getDetail() {
		return detail;
	}
	
	public void setDetail(LogOrderPay detail) {
		this.detail = detail;
	}
}
