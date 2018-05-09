package org.hasan.hook.recharge;

import javax.annotation.Resource;

import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.account.bean.entity.UserRecharge;
import org.gatlin.util.DateUtil;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.Order;
import org.hasan.bean.enums.OrderState;
import org.hasan.manager.HasanManager;
import org.hasan.manager.OrderManager;
import org.springframework.stereotype.Component;

@Component
public class RechargeInitStateMachine extends org.gatlin.soa.account.istate.RechargeInitStateMachine {
	
	@Resource
	private OrderManager orderManager;
	@Resource
	private HasanManager hasanManager;

	@Override
	protected void rechargeFailure(UserRecharge recharge) {
		switch (recharge.getGoodsType()) {
		case 100:					// 购买会员失败：什么都不做
		case 101:					// 支付订单失败：什么都不做(订单可以二次支付)
			break;
		default:
			throw new CodeException();
		}
	}

	@Override
	protected void rechargeSuccess(UserRecharge recharge) {
		switch (recharge.getGoodsType()) {
		case 100:					// 购买会员成功：
			hasanManager.memberBuy(recharge.getRechargee(), Integer.valueOf(recharge.getGoodsId()));
			break;
		case 101:					// 支付订单成功
			Order order = orderManager.order(recharge.getGoodsId());
			Assert.isTrue(HasanCode.ORDER_STATE_ERR, order.getState() == OrderState.INIT.mark());
			order.setState(OrderState.PAID.mark());
			order.setUpdated(DateUtil.current());
			orderManager.update(order);
			break;
		default:
			throw new CodeException();
		}
	}
}
