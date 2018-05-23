package org.hasan.hook.recharge;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.gatlin.core.CoreCode;
import org.gatlin.core.bean.exceptions.CodeException;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.account.bean.entity.Recharge;
import org.gatlin.soa.bean.enums.PlatType;
import org.gatlin.soa.bean.param.RechargeParam;
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
	protected Recharge verify(RechargeParam param, PlatType plat) {
		switch (param.getGoodsType()) {
		case 100:					// 购买会员
			CfgMember member = hasanManager.member(Integer.valueOf(param.getGoodsId()));
			Assert.notNull(HasanCode.MEMBER_NOT_EXIST, member);
			Assert.isTrue(CoreCode.PARAM_ERR, member.isSale());
			param.setFee(BigDecimal.ZERO);
			param.setAmount(member.getPrice());
			return _recharge(param, plat);
		case 101:					// 支付订单
			Assert.isTrue(CoreCode.PARAM_ERR, null == param.getAmount());
			Order order = orderManager.pay(param);
			Recharge recharge = _recharge(param, plat);
			// 算上快递费
			recharge.setFee(order.getExpressFee());
			recharge.setAmount(order.getPrice().add(order.getExpressFee()));
			return recharge;
		default:
			throw new CodeException(CoreCode.PARAM_ERR);
		}
	}
}
