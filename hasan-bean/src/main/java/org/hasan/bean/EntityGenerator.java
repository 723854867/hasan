package org.hasan.bean;

import java.math.BigDecimal;

import org.gatlin.soa.user.bean.entity.UserAddress;
import org.gatlin.util.DateUtil;
import org.gatlin.util.IDWorker;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.CfgScheduler;
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.enums.GoodsState;
import org.hasan.bean.enums.MemberType;
import org.hasan.bean.enums.OrderState;
import org.hasan.bean.param.GoodsAddParam;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.OrderMakeParam;
import org.hasan.bean.param.SchedulerAddParam;

public class EntityGenerator {
	
	public static final UserCustom newUserCustom(long uid, String memberTitle) {
		UserCustom instance = new UserCustom();
		instance.setUid(uid);
		instance.setMemberTitle(memberTitle);
		instance.setMemberType(MemberType.ORIGINAL.mark());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}

	public static final CfgGoods newCfgGoods(GoodsAddParam param) {
		CfgGoods instance = new CfgGoods();
		instance.setName(param.getName());
		instance.setDesc(param.getDesc());
		instance.setInventory(param.getInventory());
		instance.setPriority(param.getPriority());
		instance.setState(GoodsState.SALE.mark());
		instance.setVIPPrice(param.getVIPPrice());
		instance.setGeneralPrice(param.getGeneralPrice());
		instance.setOriginalPrice(param.getOriginalPrice());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final CfgScheduler newCfgScheduler(SchedulerAddParam param) {
		CfgScheduler instance = new CfgScheduler();
		instance.setDay(param.getDay());
		instance.setStop(param.getStop());
		instance.setStart(param.getStart());
		instance.setType(param.getType().mark());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final CfgMember newCfgMember(MemberAddParam param) {
		CfgMember instance = new CfgMember();
		instance.setName(param.getName());
		instance.setSale(param.isSale());
		instance.setPrice(param.getPrice());
		instance.setExpiry(param.getExpiry());
		instance.setMemberType(param.getMemberType().mark());
		instance.setTimeUnit(param.getTimeUnit().mark());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final Order newOrder(OrderMakeParam param, BigDecimal price, UserAddress address) {
		Order instance = new Order();
		instance.setId(IDWorker.INSTANCE.nextSid());
		instance.setUid(param.getUser().getId());
		instance.setIp(param.meta().getIp());
		instance.setState(OrderState.INIT.mark());
		instance.setPrice(price);
		instance.setRecipients(address.getContacts());
		instance.setRecipients(address.getProvince() + address.getCity() + address.getCounty() + address.getDetail());
		instance.setRecipientsMobile(address.getContactsMobile());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
}
