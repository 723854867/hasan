package org.hasan.bean.model;

import java.util.List;
import java.util.Map;

import org.gatlin.soa.bean.model.ResourceInfo;
import org.hasan.bean.entity.CfgGoods;

public class GoodsDetail extends GoodsInfo {

	private static final long serialVersionUID = -9017758222248986236L;

	private String desc;
	private int priority;
	private int cookbookId;
	private List<GoodsPriceInfo> prices;
	private List<EvaluationInfo> evaluations;
	private Map<Integer, List<ResourceInfo>> resources;
	
	public GoodsDetail() {}
	
	public GoodsDetail(CfgGoods goods) {
		super(goods, null);
		this.desc = goods.getDesc();
		this.priority = goods.getPriority();
		this.cookbookId = goods.getCookbookId();
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getCookbookId() {
		return cookbookId;
	}
	
	public void setCookbookId(int cookbookId) {
		this.cookbookId = cookbookId;
	}
	
	public List<GoodsPriceInfo> getPrices() {
		return prices;
	}
	
	public void setPrices(List<GoodsPriceInfo> prices) {
		this.prices = prices;
	}
	
	public List<EvaluationInfo> getEvaluations() {
		return evaluations;
	}
	
	public void setEvaluations(List<EvaluationInfo> evaluations) {
		this.evaluations = evaluations;
	}
	
	public Map<Integer, List<ResourceInfo>> getResources() {
		return resources;
	}
	
	public void setResources(Map<Integer, List<ResourceInfo>> resources) {
		this.resources = resources;
	}
}
