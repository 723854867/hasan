package org.hasan.bean.model;

import java.io.Serializable;
import java.util.List;

import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgCookbookStep;
import org.hasan.bean.entity.CfgCuisine;
import org.hasan.bean.entity.CfgCuisineCategory;
import org.hasan.bean.entity.CfgGoods;

public class CookbookDetail implements Serializable {

	private static final long serialVersionUID = 8194897929856383140L;

	private int id;
	private String name;
	private List<Step> steps;
	private List<Goods> goods;
	private List<Cuisine> cuisines;
	private List<ResourceInfo> images;
	
	public CookbookDetail() {}

	public CookbookDetail(CfgCookbook cookbook, List<Step> steps, List<Goods> goods, List<Cuisine> cuisines,List<ResourceInfo> images) {
		this.id = cookbook.getId();
		this.name = cookbook.getName();
		this.steps = steps;
		this.goods = goods;
		this.cuisines = cuisines;
		this.images = images;
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

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	public List<Goods> getGoods() {
		return goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}

	public List<Cuisine> getCuisines() {
		return cuisines;
	}

	public void setCuisines(List<Cuisine> cuisines) {
		this.cuisines = cuisines;
	}

	public List<ResourceInfo> getImages() {
		return images;
	}

	public void setImages(List<ResourceInfo> images) {
		this.images = images;
	}

	public static class Cuisine implements Serializable {

		private static final long serialVersionUID = 494554426087951841L;

		private int categoryId;
		private String category;
		private List<CfgCuisine> list;

		public Cuisine() {
		}

		public Cuisine(CfgCuisineCategory category, List<CfgCuisine> list) {
			this.list = list;
			this.categoryId = category.getId();
			this.category = category.getName();
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

		public List<CfgCuisine> getList() {
			return list;
		}

		public void setList(List<CfgCuisine> list) {
			this.list = list;
		}
	}

	public static class Step implements Serializable {

		private static final long serialVersionUID = 6510078337724789599L;

		private int id;
		private String name;
		private String content;
		private ResourceInfo image;

		public Step() {
		}

		public Step(CfgCookbookStep step, ResourceInfo resource) {
			this.image = resource;
			this.id = step.getId();
			this.name = step.getName();
			this.content = step.getContent();
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

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public ResourceInfo getImage() {
			return image;
		}

		public void setImage(ResourceInfo image) {
			this.image = image;
		}
	}

	public static class Goods implements Serializable {

		private static final long serialVersionUID = 7575714829439897590L;

		private int id;
		private ResourceInfo icon;

		public Goods() {}

		public Goods(CfgGoods goods, ResourceInfo icon) {
			this.icon = icon;
			this.id = goods.getId();
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public ResourceInfo getIcon() {
			return icon;
		}

		public void setIcon(ResourceInfo icon) {
			this.icon = icon;
		}
	}
}
