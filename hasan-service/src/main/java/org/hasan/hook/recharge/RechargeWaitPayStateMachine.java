package org.hasan.hook.recharge;

import org.gatlin.soa.account.bean.entity.UserRecharge;
import org.springframework.stereotype.Component;

@Component
public class RechargeWaitPayStateMachine extends org.gatlin.soa.account.istate.RechargeWaitPayStateMachine {

	@Override
	protected void rechargeFailure(UserRecharge recharge) {
		
	}

	@Override
	protected void rechargeSuccess(UserRecharge recharge) {
		
	}

}
