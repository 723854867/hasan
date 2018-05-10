package org.hasan.bean.param;

import javax.validation.constraints.Min;

import org.gatlin.soa.bean.param.SoaIdParam;

public class CuisineModifyParam extends SoaIdParam {

	private static final long serialVersionUID = 5572066177904274948L;

	private String name;
	private String dosage;
	@Min(1)
	private Integer category;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	
	public Integer getCategory() {
		return category;
	}
	
	public void setCategory(Integer category) {
		this.category = category;
	}
}
