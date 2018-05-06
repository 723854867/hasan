package org.hasan.bean;

import org.gatlin.util.DateUtil;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.CfgScheduler;
import org.hasan.bean.enums.GoodsState;
import org.hasan.bean.param.GoodsAddParam;
import org.hasan.bean.param.SchedulerAddParam;

public class EntityGenerator {

	public static final CfgGoods newCfgGoods(GoodsAddParam param) {
		CfgGoods instance = new CfgGoods();
		instance.setName(param.getName());
		instance.setDesc(param.getDesc());
		instance.setInventory(param.getInventory());
		instance.setPriority(param.getPriority());
		instance.setState(GoodsState.SALE.mark());
		instance.setVIPPrice(param.getVIPPrice());
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
}
