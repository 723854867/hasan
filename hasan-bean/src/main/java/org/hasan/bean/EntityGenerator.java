package org.hasan.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.soa.bean.param.SoaNameParam;
import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.gatlin.soa.user.bean.entity.UserAddress;
import org.gatlin.util.DateUtil;
import org.gatlin.util.IDWorker;
import org.gatlin.util.lang.CollectionUtil;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgCookbookMapping;
import org.hasan.bean.entity.CfgCookbookStep;
import org.hasan.bean.entity.CfgCuisine;
import org.hasan.bean.entity.CfgCuisineCategory;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.CfgScheduler;
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.entity.UserEvaluation;
import org.hasan.bean.enums.CookbookMappingType;
import org.hasan.bean.enums.GoodsState;
import org.hasan.bean.enums.MemberType;
import org.hasan.bean.enums.OrderState;
import org.hasan.bean.param.CookbookStepAddParam;
import org.hasan.bean.param.CuisineAddParam;
import org.hasan.bean.param.CuisineCategoryAddParam;
import org.hasan.bean.param.EvaluateParam;
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
		instance.setCookbookId(param.getCookbookId());
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
	
	public static final Order newOrder(String orderId, OrderMakeParam param, BigDecimal price, UserAddress address) {
		Order instance = new Order();
		instance.setId(orderId);
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
	
	public static final List<OrderGoods> newOrderGoods(String orderId, Map<Integer, Integer> buys, Map<Integer, CfgGoods> goods, List<ResourceInfo> resources, UserCustom custom) {
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		for (Entry<Integer, Integer> entry : buys.entrySet()) {
			CfgGoods temp = goods.get(entry.getKey());
			ResourceInfo resource = null;
			if (!CollectionUtil.isEmpty(resources)) {
				Iterator<ResourceInfo> itr = resources.iterator();
				while (itr.hasNext()) {
					ResourceInfo rtemp = itr.next();
					if (rtemp.getOwner() == temp.getId()) {
						itr.remove();
						resource = rtemp;
						break;
					}
				}
			}
			OrderGoods instance = new OrderGoods();
			instance.setOrderId(orderId);
			instance.setGoodsId(temp.getId());
			instance.setGoodsName(temp.getName());
			instance.setGoodsDesc(temp.getDesc());
			instance.setGoodsNum(entry.getValue());
			instance.setEvaluationId(StringUtil.EMPTY);
			instance.setIcon(null == resource ? StringUtil.EMPTY : resource.getUrl());
			MemberType memberType = MemberType.match(custom.getMemberType());
			switch (memberType) {
			case ORIGINAL:
				instance.setUnitPrice(temp.getOriginalPrice());
				break;
			case GENERAL:
				instance.setUnitPrice(temp.getGeneralPrice());
				break;
			case VIP:
				instance.setUnitPrice(temp.getVIPPrice());
				break;
			default:
				throw new CodeException();
			}
			instance.setCreated(DateUtil.current());
			list.add(instance);
		}
		return list;
	}
	
	public static final UserEvaluation newUserEvaluation(EvaluateParam param, OrderGoods og) {
		UserEvaluation instance = new UserEvaluation();
		instance.setId(IDWorker.INSTANCE.nextSid());
		instance.setContent(param.getContent());
		instance.setType(param.getType().mark());
		instance.setGoddsId(og.getGoodsId());
		instance.setOrderGoodsId(og.getId());
		instance.setUid(param.getUser().getId());
		instance.setCreated(DateUtil.current());
		return instance;
	}
	
	public static final CfgCuisineCategory newCfgCuisineCategory(CuisineCategoryAddParam param) {
		CfgCuisineCategory instance = new CfgCuisineCategory();
		instance.setName(param.getName());
		instance.setPriority(param.getPriority());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final CfgCuisine newCfgCuisine(CuisineAddParam param) {
		CfgCuisine instance = new CfgCuisine();
		instance.setCategory(param.getCategory());
		instance.setName(param.getName());
		instance.setDosage(param.getDosage());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final CfgCookbook newCfgCookbook(SoaNameParam param) {
		CfgCookbook instance = new CfgCookbook();
		instance.setName(param.getName());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final CfgCookbookStep newCfgCookbookStep(CookbookStepAddParam param) {
		CfgCookbookStep instance = new CfgCookbookStep();
		instance.setPriority(param.getPriority());
		instance.setContent(param.getContent());
		instance.setCookbookId(param.getCookbookId());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final List<CfgCookbookMapping> newCfgCookbookMapping(CfgCookbook cookbook, Set<Integer> tids, CookbookMappingType type) {
		List<CfgCookbookMapping> list = new ArrayList<CfgCookbookMapping>();
		tids.forEach(tid -> {
			CfgCookbookMapping instance = new CfgCookbookMapping();
			instance.setTid(tid);
			instance.setType(type.mark());
			instance.setCookbookId(cookbook.getId());
			instance.setCreated(DateUtil.current());
			list.add(instance);
		});
		return list;
		
	}
}
