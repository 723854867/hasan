package org.hasan.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.CoreCode;
import org.gatlin.core.bean.model.message.Response;
import org.gatlin.core.util.Assert;
import org.gatlin.soa.bean.param.SoaLidParam;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.entity.CfgResource;
import org.gatlin.soa.resource.bean.entity.PubResource;
import org.gatlin.soa.resource.bean.enums.UploadType;
import org.gatlin.web.util.Uploader;
import org.gatlin.web.util.param.UploadParam;
import org.hasan.bean.enums.HasanUploadType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("resource")
public class ResourceController {
	
	@Resource
	private Uploader uploader;
	@Resource
	private ResourceService resourceService;

	@ResponseBody
	@RequestMapping("upload")
	public Object upload(@Valid UploadParam param) {
		UploadType type = HasanUploadType.match(param.getUploadType());
		Assert.notNull(CoreCode.PARAM_ERR, type);
		return _upload(type, param);
	}
	
	@ResponseBody
	@RequestMapping("unload")
	public Object unload(@Valid SoaLidParam param) {
		List<PubResource> resources = resourceService.unload(param.getId());
		for (PubResource resource : resources) {
			File file = new File(resource.getPath());
			file.delete();
		}
		return Response.ok();
	}
	
	private List<PubResource> _upload(UploadType type, UploadParam param) {
		List<PubResource> list = new ArrayList<PubResource>();
		CfgResource source = resourceService.uploadVerify(type.key(), param.getResourceId(), param.getSource().getSize(), param.getMajor());
		uploader.upload(param.getSource(), type.directory(), pair -> {
			PubResource resource = resourceService.upload(pair.getValue(), pair.getKey(), source, type.key(), param.getPriority(), param.getMajor());
			list.add(resource);
		});
		return list;
	}
}
