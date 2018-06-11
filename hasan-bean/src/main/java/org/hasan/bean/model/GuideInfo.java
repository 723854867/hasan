package org.hasan.bean.model;

import java.io.Serializable;

public class GuideInfo implements Serializable {

	private static final long serialVersionUID = -4901204615824683581L;

	private String year;
	private String month;
	private String day;
	private String week;
	private String lunaryear;
	private String lunarmonth;
	private String lunarday;
	private String shengxiao;
	private String[] suici;
	private String verse;
	private Integer hour;
	private String jieqi;
	private int jieQiPassDay;
	private String nextJieQi;
	private int nextJieQiDay;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getLunaryear() {
		return lunaryear;
	}

	public void setLunaryear(String lunaryear) {
		this.lunaryear = lunaryear;
	}

	public String getLunarmonth() {
		return lunarmonth;
	}

	public void setLunarmonth(String lunarmonth) {
		this.lunarmonth = lunarmonth;
	}

	public String getLunarday() {
		return lunarday;
	}

	public void setLunarday(String lunarday) {
		this.lunarday = lunarday;
	}

	public String getShengxiao() {
		return shengxiao;
	}

	public void setShengxiao(String shengxiao) {
		this.shengxiao = shengxiao;
	}

	public String[] getSuici() {
		return suici;
	}

	public void setSuici(String[] suici) {
		this.suici = suici;
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

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public int getJieQiPassDay() {
		return jieQiPassDay;
	}
	
	public void setJieQiPassDay(int jieQiPassDay) {
		this.jieQiPassDay = jieQiPassDay;
	}

	public String getNextJieQi() {
		return nextJieQi;
	}

	public void setNextJieQi(String nextJieQi) {
		this.nextJieQi = nextJieQi;
	}

	public int getNextJieQiDay() {
		return nextJieQiDay;
	}

	public void setNextJieQiDay(int nextJieQiDay) {
		this.nextJieQiDay = nextJieQiDay;
	}
}
