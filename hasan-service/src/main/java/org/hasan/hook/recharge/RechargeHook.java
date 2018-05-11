package org.hasan.hook.recharge;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.account.bean.entity.UserRecharge;
import org.gatlin.soa.account.bean.param.RechargeParam;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.Order;
import org.hasan.manager.HasanManager;
import org.hasan.manager.OrderManager;
import org.springframework.stereotype.Component;

@Component
public class RechargeHook extends org.gatlin.web.util.hook.RechargeHook {
	
	@Resource
	private HasanManager hasanManager;
	@Resource
	private OrderManager orderManager;

	@Override
	protected UserRecharge verify(RechargeParam param) {
		switch (param.getGoodsType()) {
		case 100:					// 购买会员
			rechargeeVerify(param);
			Assert.isTrue(CoreCode.PARAM_ERR, null == param.getAmount());
			CfgMember member = hasanManager.member(Integer.valueOf(param.getGoodsId()));
			Assert.notNull(HasanCode.MEMBER_NOT_EXIST, member);
			Assert.isTrue(CoreCode.PARAM_ERR, member.isSale());
			param.setAmount(member.getPrice());
			return _userRecharge(param);
		case 101:					// 支付订单
			Assert.isTrue(CoreCode.PARAM_ERR, null == param.getAmount());
			Assert.isTrue(CoreCode.PARAM_ERR, null == param.getRechargee());
			Order order = orderManager.pay(param);
			UserRecharge recharge = _userRecharge(param, order.getExpressFee());
			// 算上快递费
			recharge.setFee(order.getExpressFee());
			recharge.setAmount(recharge.getAmount().add(recharge.getFee()));
			return recharge;
		default:
			throw new CodeException(CoreCode.PARAM_ERR);
		}
	}
}
