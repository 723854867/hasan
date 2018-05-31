package org.hasan.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gatlin.core.util.Assert;
import org.gatlin.soa.bean.model.Geo;
import org.gatlin.soa.user.bean.entity.UserAddress;
import org.gatlin.util.DateUtil;
import org.gatlin.util.IDWorker;
import org.gatlin.util.lang.StringUtil;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgCookbookStep;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.CfgGoodsPrice;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.CfgScheduler;
import org.hasan.bean.entity.CfgVerse;
import org.hasan.bean.entity.Order;
import org.hasan.bean.entity.OrderGoods;
import org.hasan.bean.entity.UserAssistant;
import org.hasan.bean.entity.UserCustom;
import org.hasan.bean.entity.UserEvaluation;
import org.hasan.bean.enums.GoodsState;
import org.hasan.bean.enums.OrderState;
import org.hasan.bean.param.AssistantAllocateParam;
import org.hasan.bean.param.CookbookStepAddParam;
import org.hasan.bean.param.EvaluateParam;
import org.hasan.bean.param.GoodsAddParam;
import org.hasan.bean.param.MemberAddParam;
import org.hasan.bean.param.OrderMakeParam;
import org.hasan.bean.param.SchedulerAddParam;

public class EntityGenerator {
	
	public static final UserCustom newUserCustom(long uid) {
		UserCustom instance = new UserCustom();
		instance.setUid(uid);
		instance.setMemberId(0);
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
		instance.setCookbookId(param.getCookbookId());
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
		instance.setTimeUnit(param.getTimeUnit().mark());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final Order newOrder(String orderId, OrderMakeParam param, BigDecimal price, UserAddress address, Geo geo) {
		Order instance = new Order();
		instance.setId(orderId);
		instance.setPrice(price);
		instance.setIp(param.meta().getIp());
		instance.setUid(param.getUser().getId());
		instance.setState(OrderState.INIT.mark());
		instance.setExpressFee(BigDecimal.ZERO);
		instance.setExpressNo(StringUtil.EMPTY);
		instance.setRecipients(address.getContacts());
		instance.setDeliverStop(param.getDeliverStop());
		instance.setDeliverStart(param.getDeliverStart());
		instance.setRecipientsMobile(address.getContactsMobile());
		instance.setRecipientsAddr(geo.getProvince() + geo.getCity() + geo.getCounty() + address.getDetail());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final List<OrderGoods> newOrderGoods(String orderId, Map<Integer, Integer> buys, Map<Integer, CfgGoods> goods, Map<Integer, CfgGoodsPrice> prices) {
		List<OrderGoods> list = new ArrayList<OrderGoods>();
		for (Entry<Integer, Integer> entry : buys.entrySet()) {
			CfgGoods temp = goods.get(entry.getKey());
			OrderGoods instance = new OrderGoods();
			instance.setOrderId(orderId);
			instance.setGoodsId(temp.getId());
			instance.setGoodsName(temp.getName());
			instance.setGoodsDesc(temp.getDesc());
			instance.setGoodsNum(entry.getValue());
			CfgGoodsPrice price = prices.get(temp.getId());
			Assert.notNull(HasanCode.GOODS_PRICE_NOT_EXIST, price);
			instance.setUnitPrice(price.getPrice());
			instance.setEvaluationId(StringUtil.EMPTY);
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
		instance.setGoodsId(og.getGoodsId());
		instance.setOrderGoodsId(og.getId());
		instance.setUid(param.getUser().getId());
		instance.setCreated(DateUtil.current());
		return instance;
	}
	
	public static final CfgCookbook newCfgCookbook(String name, String text) {
		CfgCookbook instance = new CfgCookbook();
		instance.setText(text);
		instance.setName(name);
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final CfgCookbookStep newCfgCookbookStep(CookbookStepAddParam param) {
		CfgCookbookStep instance = new CfgCookbookStep();
		instance.setName(param.getName());
		instance.setPriority(param.getPriority());
		instance.setContent(param.getContent());
		instance.setCookbookId(param.getCookbookId());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final UserAssistant newUserAssistant(AssistantAllocateParam param) {
		UserAssistant instance = new UserAssistant();
		instance.setUid(param.getId());
		instance.setAssistant(param.getAssistant());
		instance.setCreated(DateUtil.current());
		return instance;
	}
	
	public static final CfgGoodsPrice newCfgGoodsPrice(int memberId, int goodsId, BigDecimal price) {
		CfgGoodsPrice instance = new CfgGoodsPrice();
		instance.setGoodsId(goodsId);
		instance.setMemberId(memberId);
		instance.setPrice(price);
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final CfgVerse newCfgVerse(String content) {
		CfgVerse instance = new CfgVerse();
		instance.setContent(content);
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
}
