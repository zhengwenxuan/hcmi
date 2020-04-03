package com.hjw.webService.client.TCI.bean;

public class TransmitData2 {
	private String yiyuanbh = "";//医院编号
	private String jiaoyi = "";//传入code值
	private String jiaoyilsh = "";//为业务数据传输是的lsh唯一值，
	private String picilsh = "";//是通过交易号获取平台数据中心返回的picilsh
	public String getYiyuanbh() {
		return yiyuanbh;
	}
	public void setYiyuanbh(String yiyuanbh) {
		this.yiyuanbh = yiyuanbh;
	}
	public String getJiaoyi() {
		return jiaoyi;
	}
	public void setJiaoyi(String jiaoyi) {
		this.jiaoyi = jiaoyi;
	}
	public String getJiaoyilsh() {
		return jiaoyilsh;
	}
	public void setJiaoyilsh(String jiaoyilsh) {
		this.jiaoyilsh = jiaoyilsh;
	}
	public String getPicilsh() {
		return picilsh;
	}
	public void setPicilsh(String picilsh) {
		this.picilsh = picilsh;
	}
}
