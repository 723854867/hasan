package org.hasan.hook.recharge;

import org.gatlin.soa.account.bean.entity.Recharge;
import org.springframework.stereotype.Component;

@Component
public class RechargeSuccessStateMachine extends org.gatlin.soa.account.istate.RechargeSuccessStateMachine {

	@Override
	protected void rechargeReback(Recharge recharge) {
		// 退款操作
	}
}
