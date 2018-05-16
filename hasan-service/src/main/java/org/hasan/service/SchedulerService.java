package org.hasan.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.util.lang.CollectionUtil;
import org.hasan.bean.HasanConsts;
import org.hasan.bean.entity.CfgScheduler;
import org.hasan.bean.enums.SchedulerType;
import org.hasan.bean.model.TimeRange;
import org.hasan.bean.param.SchedulerAddParam;
import org.hasan.bean.param.SchedulerModifyParam;
import org.hasan.manager.SchedulerManager;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

	@Resource
	private ConfigService configService;
	@Resource
	private SchedulerManager schedulerManager;
	
	public int add(SchedulerAddParam param) {
		return schedulerManager.add(param);
	}
	
	public void modify(SchedulerModifyParam param) {
		schedulerManager.modify(param);
	}
	
	public void delete(SoaIdParam param) {
		schedulerManager.delete(param);
	}
	
	public List<TimeRange> deliverySchedulers() {
		int minutes = configService.config(HasanConsts.ORDER_ADVANCE_MINUTES);
		long start = System.currentTimeMillis() + minutes * 60000;
		List<TimeRange> packageRanges = schedulers(start, SchedulerType.PACKAGE, 1);
		if (CollectionUtil.isEmpty(packageRanges))
			return CollectionUtil.emptyList();
		TimeRange range = packageRanges.get(0);
		int maximum = configService.config(HasanConsts.DELIVERY_TIME_MAXIMUM);
		return schedulers(range.getStop() * 1000l, SchedulerType.DELIVERY, maximum);
	}
	
	public List<TimeRange> schedulers(long start, SchedulerType type, int maximum) {
		List<CfgScheduler> schedulers = schedulerManager.schedulers(new Query().eq("type", type.mark()).orderByAsc("day", "start"));
		if (CollectionUtil.isEmpty(schedulers))
			return CollectionUtil.emptyList();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		List<TimeRange> ranges = new ArrayList<TimeRange>();
		a : while (true) {
			calendar.add(Calendar.MONTH, 1);
			for (CfgScheduler scheduler : schedulers) {
				int dayMax = calendar.getActualMaximum(Calendar.DATE);
				if (scheduler.getDay() > dayMax)
					continue;
				Calendar temp = Calendar.getInstance();
				temp.setTimeInMillis(calendar.getTimeInMillis());
				temp.set(Calendar.DAY_OF_MONTH, scheduler.getDay());
				temp.add(Calendar.MINUTE, scheduler.getStart());
				if (temp.getTimeInMillis() >= start) {
					int rangeStart = (int) (temp.getTimeInMillis() / 1000);
					int rangeStop = rangeStart + (scheduler.getStop() - scheduler.getStart()) * 60;
					ranges.add(new TimeRange(rangeStop, rangeStart));
					if (ranges.size() >= maximum)
						break a;
				}
			}
		}
		return ranges;
	}
	
	public List<CfgScheduler> schedulers(Query query) {
		return schedulerManager.schedulers(query);
	}
}
