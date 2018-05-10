package org.hasan.bean.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.gatlin.soa.bean.param.SoaParam;

public class CuisineAddParam extends SoaParam {

	private static final long serialVersionUID = 7013155446983136841L;

	@Min(1)
	private int category;
	@NotEmpty
	private String name;
	@NotEmpty
	private String dosage;

	public int getCategory() {
		return category;
	}
	
	public void setCategory(int category) {
		this.category = category;
	}

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
}
