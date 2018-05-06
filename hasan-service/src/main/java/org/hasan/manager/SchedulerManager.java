package org.hasan.manager;

import java.util.BitSet;
import java.util.List;

import javax.annotation.Resource;

import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.util.DateUtil;
import org.hasan.bean.EntityGenerator;
import org.hasan.bean.HasanCode;
import org.hasan.bean.entity.CfgScheduler;
import org.hasan.bean.param.SchedulerAddParam;
import org.hasan.bean.param.SchedulerModifyParam;
import org.hasan.mybatis.dao.CfgSchedulerDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SchedulerManager {

	@Resource
	private CfgSchedulerDao cfgSchedulerDao;
	
	@Transactional
	public int add(SchedulerAddParam param) {
		Query query = new Query().eq("day", param.getDay()).eq("type", param.getType().mark()).forUpdate();
		List<CfgScheduler> schedulers = cfgSchedulerDao.queryList(query);
		BitSet bitSet = new BitSet();
		for (CfgScheduler scheduler : schedulers) {
			for (int i = scheduler.getStart(); i <= scheduler.getStop(); i++)
				bitSet.set(i);
		}
		for (int i = param.getStart(); i <= param.getStop(); i++)
			Assert.isTrue(HasanCode.TIME_RANGE_CROSS, !bitSet.get(i));
		CfgScheduler scheduler = EntityGenerator.newCfgScheduler(param);
		cfgSchedulerDao.insert(scheduler);
		return scheduler.getId();
	}
	
	@Transactional
	public void modify(SchedulerModifyParam param) {
		CfgScheduler scheduler = cfgSchedulerDao.getByKey(param.getId());
		Assert.notNull(HasanCode.SCHEDULER_NOT_EXIST, scheduler);
		Query query = new Query().eq("day", scheduler.getDay()).eq("type", scheduler.getType()).forUpdate();
		List<CfgScheduler> schedulers = cfgSchedulerDao.queryList(query);
		BitSet bitSet = new BitSet();
		for (CfgScheduler temp : schedulers) {
			for (int i = temp.getStart(); i <= temp.getStop(); i++)
				bitSet.set(i);
		}
		for (int i = param.getStart(); i <= param.getStop(); i++)
			Assert.isTrue(HasanCode.TIME_RANGE_CROSS, !bitSet.get(i));
		scheduler.setStop(param.getStop());
		scheduler.setStart(param.getStart());
		scheduler.setUpdated(DateUtil.current());
		cfgSchedulerDao.update(scheduler);
	}
	
	@Transactional
	public void delete(SoaIdParam param) {
		cfgSchedulerDao.deleteByKey(param.getId());
	}
	
	public List<CfgScheduler> schedulers(Query query) {
		return cfgSchedulerDao.queryList(query);
	}
}
