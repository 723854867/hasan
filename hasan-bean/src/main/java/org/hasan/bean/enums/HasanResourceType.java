package org.hasan.bean.enums;

import java.util.HashSet;
import java.util.Set;

public enum HasanResourceType {

	GOODS_ICON(1000),
	GOODS_IMAGE(1001),
	GOODS_DISH(1002),
	
	COOKBOOK_ICON(1010),
	COOKBOOK_IMAGE(1011),
	COOKBOOK_STEP(1020);
	
	private int mark;
	
	private HasanResourceType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public static final HasanResourceType match(int mark) {
		for (HasanResourceType temp : HasanResourceType.values()) {
			if (temp.mark == mark)
				return temp;
		}
		return null;
	}
	
	public static final Set<Integer> goodsResourceTypes() {
		return new HashSet<Integer>() {
			private static final long serialVersionUID = 4048065548774118088L;
			{
				add(GOODS_ICON.mark);
				add(GOODS_IMAGE.mark);
				add(GOODS_DISH.mark);
			}
		};
	}
}
