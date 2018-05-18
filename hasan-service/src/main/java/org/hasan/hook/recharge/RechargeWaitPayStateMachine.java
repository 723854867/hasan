package org.hasan.hook.recharge;

import javax.annotation.Resource;

import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.soa.account.bean.entity.Recharge;
import org.hasan.manager.HasanManager;
import org.hasan.manager.OrderManager;
import org.springframework.stereotype.Component;

@Component
public class RechargeWaitPayStateMachine extends org.gatlin.soa.account.istate.RechargeWaitPayStateMachine {
	
	@Resource
	private OrderManager orderManager;
	@Resource
	private HasanManager hasanManager;

	@Override
	protected void rechargeFailure(Recharge recharge) {
		switch (recharge.getGoodsType()) {
		case 100:					// 购买会员失败：什么都不做
			break;
		case 101:					// 订单支付失败
			orderManager.payNotice(recharge.getGoodsId(), false);
			break;
		default:
			throw new CodeException();
		}
	}

	@Override
	protected void rechargeSuccess(Recharge recharge) {
		switch (recharge.getGoodsType()) {
		case 100:					// 购买会员成功：
			hasanManager.memberBuy(recharge);
			break;
		case 101:					// 支付订单成功
			orderManager.payNotice(recharge.getGoodsId(), true);
			break;
		default:
			throw new CodeException();
		}
	}
}
