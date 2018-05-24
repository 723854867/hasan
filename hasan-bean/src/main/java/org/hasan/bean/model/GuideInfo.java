package org.hasan.bean.model;

import java.io.Serializable;

import org.gatlin.util.DateUtil;
import org.hasan.bean.entity.CfgVerse;

public class GuideInfo implements Serializable {

	private static final long serialVersionUID = -4901204615824683581L;

	private int time;
	private String jieqi;
	private String verse;
	
	public GuideInfo() {}
	
	public GuideInfo(String jieqi, CfgVerse verse) {
		this.jieqi = jieqi;
		this.time = DateUtil.current();
		if (null != verse)
			this.verse = verse.getContent();
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getJieqi() {
		return jieqi;
	}

	public void setJieqi(String jieqi) {
		this.jieqi = jieqi;
	}

	public String getVerse() {
		return verse;
	}

	public void setVerse(String verse) {
		this.verse = verse;
	}
}
