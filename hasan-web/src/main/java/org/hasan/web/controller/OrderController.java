package org.hasan.web.controller;

import javax.validation.Valid;

import org.gatlin.soa.bean.param.SoaParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("order")
public class OrderController {

	@ResponseBody
	@RequestMapping("make")
	public Object make(@RequestBody @Valid SoaParam param) {
		return null;
	}
}
