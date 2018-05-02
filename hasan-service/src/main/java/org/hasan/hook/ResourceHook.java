package org.hasan.hook;

import org.gatlin.soa.resource.bean.entity.CfgResource;
import org.gatlin.web.util.bean.param.ResourceUploadParam;
import org.springframework.stereotype.Component;

@Component("resourceHook")
public class ResourceHook extends org.gatlin.web.util.hook.ResourceHook {

	@Override
	public void verify(CfgResource resource, ResourceUploadParam param) {
	}
}
