package org.hasan.web.controller;

import java.io.File;

import javax.validation.Valid;

import org.gatlin.core.CoreCode;
import org.gatlin.core.bean.model.message.Response;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.entity.Resource;
import org.gatlin.soa.resource.bean.param.ResourceModifyParam;
import org.gatlin.web.util.Uploader;
import org.gatlin.web.util.bean.param.ResourceSearcher;
import org.gatlin.web.util.bean.param.ResourceUploadParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("resource")
public class ResourceController {
	
	@javax.annotation.Resource
	private Uploader uploader;
	@javax.annotation.Resource
	private ResourceService resourceService;
	
	
	@ResponseBody
	@RequestMapping("list")
	public Object pictures(@RequestBody @Valid ResourceSearcher param) {
		Query query = new Query().page(param.getPage()).pageSize(param.getPageSize());
		query.eq("type", param.getType()).orderByAsc("priority");
		return resourceService.pictures(query);
	}

	// banner 上传
	@ResponseBody
	@RequestMapping("upload")
	public Object upload(@Valid ResourceUploadParam param) {
		Assert.hasText(CoreCode.PARAM_ERR, param.getName());
		Assert.notNull(CoreCode.PARAM_ERR, param.getSource(), param.getPriority());
		return uploader.upload(param.getSource(), "banner", resource -> {
			resource.setName(param.getName());
			resource.setOwner(param.getOwner());
			resource.setPriority(param.getPriority());
			resource.setCfgId(param.getCfgResourceId());
			resourceService.upload(resource);
			return resource;
		});
	}
	
	@ResponseBody
	@RequestMapping("modify")
	public Object modify(@RequestBody @Valid ResourceModifyParam param) {
		resourceService.modify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("delete")
	public Object delete(@RequestBody @Valid SoaIdParam param) {
		Resource resource = resourceService.delete(param.getId());
		_deleteResource(resource);
		return Response.ok();
	}
	
	private void _deleteResource(Resource resource) {
		File file = new File(resource.getPath());
		file.delete();
	}
}
