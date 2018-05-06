package org.hasan.bean;

import org.gatlin.core.bean.model.code.Code;

public interface HasanCode {

	final Code GOODS_NOT_EXIST 			= new Code("code.hasan.goods.not.exist", "商品不存在");
	final Code TIME_RANGE_CROSS 		= new Code("code.time.scheduler.cross", "时间区间有重叠");
	final Code SCHEDULER_NOT_EXIST 		= new Code("code.scheduler.not.exist", "日程配置不存在");
}
