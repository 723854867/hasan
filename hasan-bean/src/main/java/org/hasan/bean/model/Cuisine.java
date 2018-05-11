package org.hasan.bean.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Cuisine implements Serializable {

	private static final long serialVersionUID = 4253969090943162055L;

	@NotEmpty
	@Size(min = 1, max = 20)
	private String name;
	@NotEmpty
	@Size(min = 1, max = 20)
	private String dosage;
	
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
