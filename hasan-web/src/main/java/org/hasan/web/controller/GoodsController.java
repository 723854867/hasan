package org.hasan.web.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.gatlin.core.bean.model.message.Response;
import org.gatlin.soa.bean.param.SoaIdParam;
import org.gatlin.soa.bean.param.SoaQuotaLIdParam;
import org.gatlin.soa.bean.param.SoaSidParam;
import org.hasan.bean.param.GoodsAddParam;
import org.hasan.bean.param.GoodsModifyParam;
import org.hasan.bean.param.GoodsParam;
import org.hasan.bean.param.GoodsPriceAddParam;
import org.hasan.service.GoodsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("goods")
public class GoodsController {
	
	@Resource
	private GoodsService goodsService;
	
	@ResponseBody
	@RequestMapping("list")
	public Object list(@RequestBody @Valid GoodsParam param) {
		return goodsService.goods(param.query());
	}
	
	@ResponseBody
	@RequestMapping("detail")
	public Object detail(@RequestBody @Valid SoaIdParam param) {
		return goodsService.goodsDetail(param.getId());
	}
	
	@ResponseBody
	@RequestMapping("evaluations")
	public Object evaluations(@RequestBody @Valid SoaSidParam param) {
		return goodsService.evaluations(param.query());
	}
	
	@ResponseBody
	@RequestMapping("add")
	public Object add(@RequestBody @Valid GoodsAddParam param) {
		return goodsService.add(param);
	}
	
	@ResponseBody
	@RequestMapping("modify")
	public Object modify(@RequestBody @Valid GoodsModifyParam param) {
		goodsService.modify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("price/add")
	public Object priceAdd(@RequestBody @Valid GoodsPriceAddParam param) {
		return goodsService.priceAdd(param);
	}
	
	@ResponseBody
	@RequestMapping("price/modify")
	public Object priceModify(@RequestBody @Valid SoaQuotaLIdParam param) {
		goodsService.priceModify(param);
		return Response.ok();
	}
	
	@ResponseBody
	@RequestMapping("price/delete")
	public Object priceDelete(@RequestBody @Valid SoaIdParam param) {
		goodsService.priceDelete(param);
		return Response.ok();
	}
}
