package org.hasan.bean.entity;

import java.math.BigDecimal;

import org.gatlin.core.bean.Entity;

public class CfgGoods implements Entity<Integer> {

	private static final long serialVersionUID = -8365325728478524537L;
	
	private int id;
	private int type;
	private String name;
	private BigDecimal price;

	@Override
	public Integer key() {
		return null;
	}
}
