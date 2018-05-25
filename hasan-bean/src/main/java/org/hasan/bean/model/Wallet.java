package org.hasan.bean.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.gatlin.soa.account.bean.entity.Account;
import org.gatlin.soa.bean.User;
import org.gatlin.soa.bean.model.ResourceInfo;
import org.hasan.bean.entity.UserCustom;

public class Wallet implements Serializable {

	private static final long serialVersionUID = -2155718980686318491L;

	private long uid;
	private String avatar;
	private BigDecimal usable;
	private int memberExpiry;
	private String memberTitle;
	private int memberId;
	
	public Wallet() {}
	
	public Wallet(User user, ResourceInfo avatar, Account account, UserCustom custom, String memberTitle) {
		this.uid = user.getId();
		if (null != avatar)
			this.avatar = avatar.getUrl();
		this.usable = account.getUsable();
		this.memberId = custom.getMemberId();
		this.memberTitle = memberTitle;
		this.memberExpiry = (int) (custom.getMemberExpiry() / 1000);
	}
	
	public long getUid() {
		return uid;
	}
	
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public BigDecimal getUsable() {
		return usable;
	}
	
	public void setUsable(BigDecimal usable) {
		this.usable = usable;
	}
	
	public int getMemberExpiry() {
		return memberExpiry;
	}
	
	public void setMemberExpiry(int memberExpiry) {
		this.memberExpiry = memberExpiry;
	}
	
	public String getMemberTitle() {
		return memberTitle;
	}
	
	public void setMemberTitle(String memberTitle) {
		this.memberTitle = memberTitle;
	}
	
	public int getMemberId() {
		return memberId;
	}
	
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
}
