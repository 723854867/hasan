package org.hasan.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.EntityGenerator;
import org.gatlin.soa.resource.bean.entity.PubResource;
import org.gatlin.soa.resource.bean.enums.UploadType;
import org.gatlin.web.util.Uploader;
import org.gatlin.web.util.param.UploadParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("upload")
public class UploadController {
	
	@Resource
	private Uploader uploader;
	@Resource
	private ResourceService resourceService;

	@RequestMapping("banner")
	public Object banner(@Valid UploadParam param) {
		if (null == param.getLink()) {
			PubResource resource = uploader.upload(param.getFile(), "banner", url -> {
				PubResource pr = EntityGenerator.newPubResource("", UploadType.BANNER.mark(), 0, param.getPriority(), param.getLinkUrl());
				return pr;
			});
		} else {
			Map<String, MultipartFile> files = new HashMap<String, MultipartFile>();
			files.put("link", param.getLink());
			files.put("image", param.getFile());
			PubResource resource = uploader.upload(files, "banner", urls -> {
				PubResource pr = EntityGenerator.newPubResource("", UploadType.BANNER.mark(), 0, param.getPriority(), param.getLinkUrl());
				return pr;
			});
		}
		return Response.ok();
	}
}
