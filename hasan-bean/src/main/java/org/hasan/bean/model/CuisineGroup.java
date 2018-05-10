package org.hasan.bean.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CuisineGroup implements Serializable {

	private static final long serialVersionUID = -5339501593849387969L;

	@NotEmpty
	@Size(min = 2, max = 20)
	private String name;
	@Valid
	@NotEmpty
	@Size(min = 1, max = 20)
	private List<Cuisine> cuisines;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Cuisine> getCuisines() {
		return cuisines;
	}
	
	public void setCuisines(List<Cuisine> cuisines) {
		this.cuisines = cuisines;
	}
}
