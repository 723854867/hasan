package org.hasan.hook;

import javax.annotation.Resource;

import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.resource.bean.entity.CfgResource;
import org.gatlin.web.bean.param.ResourceUploadParam;
import org.springframework.stereotype.Component;

@Component("resourceHook")
public class ResourceHook extends org.gatlin.web.util.hook.ResourceHook {
	
	@Resource
	private ConfigService configService;

	@Override
	public void verify(CfgResource resource, ResourceUploadParam param) {
		switch (resource.getType()) {
		case 3:
			
			break;
		default:
			break;
		}
	}
}
