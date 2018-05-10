package org.hasan.bean.model;

import java.util.List;
import java.util.Map;

import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.enums.HasanResourceType;

public class GoodsDetail extends GoodsInfo {

	private static final long serialVersionUID = -9017758222248986236L;

	private List<EvaluationInfo> evaluations;
	
	public GoodsDetail() {}
	
	public GoodsDetail(CfgGoods goods) {
		super(goods);
	}
	
	public GoodsDetail(CfgGoods goods, Map<HasanResourceType, List<ResourceInfo>> resources) {
		super(goods, resources);
	}
	
	public List<EvaluationInfo> getEvaluations() {
		return evaluations;
	}
	
	public void setEvaluations(List<EvaluationInfo> evaluations) {
		this.evaluations = evaluations;
	}
}
