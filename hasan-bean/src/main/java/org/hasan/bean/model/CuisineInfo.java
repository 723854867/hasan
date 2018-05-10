package org.hasan.bean.model;

import java.io.Serializable;

import org.hasan.bean.entity.CfgCuisine;
import org.hasan.bean.entity.CfgCuisineCategory;

public class CuisineInfo implements Serializable {

	private static final long serialVersionUID = 7851992257892707389L;

	private int id;
	private String name;
	private int categoryId;
	private String category;
	private String dosage;
	private int created;
	private int updated;
	
	public CuisineInfo() {}
	
	public CuisineInfo(CfgCuisine cuisine, CfgCuisineCategory category) {
		this.id = cuisine.getId();
		this.name = cuisine.getName();
		this.dosage = cuisine.getDosage();
		this.category = category.getName();
		this.categoryId = category.getId();
		this.created = cuisine.getCreated();
		this.updated = cuisine.getUpdated();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}
}
