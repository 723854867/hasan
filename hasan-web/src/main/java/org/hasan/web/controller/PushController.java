package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.sdk.jpush.bean.model.Message;
import org.gatlin.sdk.jpush.bean.model.PushBody;
import org.gatlin.soa.jpush.api.JPushService;
import org.gatlin.util.serial.SerializeUtil;
import org.hasan.bean.model.message.PushMessage;
import org.hasan.bean.param.PushParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("push")
public class PushController {
	
	@Resource
	private JPushService JPushService;

	@ResponseBody
	@RequestMapping("send")
	public Object send(@RequestBody @Valid PushParam param) {
		PushMessage<Void> message = new PushMessage<Void>();
		message.setTitle(param.getTitle());
		message.setContent(param.getContent());
		PushBody body = new PushBody();
		body.platformAll();
		body.audienceAll();
		body.message(new Message().title(param.getTitle()).content(param.getContent()).extras(SerializeUtil.GSON.toJson(message)));
		JPushService.push(body);
		return Response.ok();
	}
}
