package org.hasan.service;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.util.DateUtil;
import org.gatlin.util.lang.CollectionUtil;
import org.hasan.bean.HasanConsts;
import org.hasan.bean.entity.CfgScheduler;
import org.hasan.bean.enums.SchedulerType;
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
	
	public CfgScheduler orderScheduler(Integer schedulerId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int minutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
		return null;
	}
	
	public List<CfgScheduler> availableSchedulers() {
		List<CfgScheduler> schedulers = schedulerManager.schedulers(new Query());
		if (CollectionUtil.isEmpty(schedulers))
			return CollectionUtil.emptyList();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentMonth = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int minutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
		int orderAdvanceTime = Math.max(0, configService.config(HasanConsts.ORDER_ADVANCE_MINUTES));
		CfgScheduler packageScheduler = null;
		CfgScheduler first = schedulers.get(0);
		int iteratorMonth = currentMonth;
		while (true) {
			for (CfgScheduler temp : schedulers) {
				if (iteratorMonth == currentMonth) {
					
				} else {
					
				}
			}
		}
	}
	
	private CfgScheduler recentlyPackageScheduler() {
		Query query = new Query().eq("type", SchedulerType.PACKAGE.mark()).orderByAsc("day", "start");
		List<CfgScheduler> schedulers = schedulerManager.schedulers(query);
		if (CollectionUtil.isEmpty(schedulers))
			return null;
		int minTimeW = DateUtil.current() + Math.max(0, configService.config(HasanConsts.ORDER_ADVANCE_MINUTES)) * 60;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentMonth = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int minutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
		int orderAdvanceTime = Math.max(0, configService.config(HasanConsts.ORDER_ADVANCE_MINUTES));
		CfgScheduler scheduler = null;
		while (true) {
			for (CfgScheduler temp : schedulers) {
				
			}
		}
	}
	
	public List<CfgScheduler> schedulers(Query query) {
		return schedulerManager.schedulers(query);
	}
}
