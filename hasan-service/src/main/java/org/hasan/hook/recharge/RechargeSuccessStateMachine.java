package org.hasan.hook.recharge;

import org.gatlin.soa.account.bean.entity.UserRecharge;
import org.springframework.stereotype.Component;

@Component
public class RechargeSuccessStateMachine extends org.gatlin.soa.account.istate.RechargeSuccessStateMachine {

	@Override
	protected void rechargeReback(UserRecharge recharge) {
		// 退款操作
	}
}
