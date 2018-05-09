package org.hasan.bean.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.gatlin.soa.account.bean.entity.UserAccount;
import org.gatlin.soa.bean.User;
import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.enums.MemberType;

public class Wallet implements Serializable {

	private static final long serialVersionUID = -2155718980686318491L;

	private long uid;
	private String avatar;
	private BigDecimal usable;
	private long memberExpiry;
	private String memberTitle;
	private MemberType memberType;
	
	public Wallet() {}
	
	public Wallet(User user, ResourceInfo avatar, UserAccount account, UserCustom custom) {
		this.uid = user.getId();
		if (null != avatar)
			this.avatar = avatar.getUrl();
		this.usable = account.getUsable();
		this.memberTitle = custom.getMemberTitle();
		this.memberExpiry = custom.getMemberExpiry();
		this.memberType = MemberType.match(custom.getMemberType());
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
	
	public long getMemberExpiry() {
		return memberExpiry;
	}
	
	public void setMemberExpiry(long memberExpiry) {
		this.memberExpiry = memberExpiry;
	}
	
	public String getMemberTitle() {
		return memberTitle;
	}
	
	public void setMemberTitle(String memberTitle) {
		this.memberTitle = memberTitle;
	}
	
	public MemberType getMemberType() {
		return memberType;
	}
	
	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}
}
