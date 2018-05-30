package org.hasan.hook;

import javax.annotation.Resource;

import org.gatlin.soa.account.bean.entity.Recharge;
import org.gatlin.soa.account.hook.DefaultRechargeNoticeHook;
import org.hasan.manager.HasanManager;
import org.hasan.manager.OrderManager;
import org.springframework.stereotype.Component;

@Component
public class RechargeNoticeHook extends DefaultRechargeNoticeHook {
	
	@Resource
	private OrderManager orderManager;
	@Resource
	private HasanManager hasanManager;
	
	@Override
	protected void failure(Recharge recharge) {
		super.failure(recharge);
		switch (recharge.getGoodsType()) {
		case 100:				// 会员购买
			break;
		case 101:				// 订单支付
			orderManager.payNotice(recharge, false);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void success(Recharge recharge) {
		super.success(recharge);
		switch (recharge.getGoodsType()) {
		case 100:				// 会员购买			
			hasanManager.memberBuy(recharge);
			break;
		case 101:				// 订单支付
			orderManager.payNotice(recharge, true);
			break;
		default:
			break;
		}
	}
}
