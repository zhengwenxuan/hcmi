package com.hjw.webService.client.TCI.bean;

public class TJxiaoJie {

	private String lsh = "";//流水号	字符(30)	是	医院编号+小结编号:主键 915245
	private String shenfenz = "";//身份证	字符(18)	是	
	private String yiyuanbh = "";//医院编号	字符(6)	是	915245
	private String tijianxjbh = "";//小结编号	字符(20)	是	医院内科室小结记录主键
	private String tijianxh = "";//体检序号	字符(20)	是	关联体检记录
	private String keshibm = "";//科室编码	字符(20)	是		CVX_DepartmentCode科室代码字典
	private String keshimc = "";//科室名称	字符(20)	是	
	private String keshixj = "";//科室小结	字符(1000)	是
	private String tijianrq = "";//小结日期	日期	是	格式YYYYMMDD
	private String tijianys = "";//小结医生	字符(12)	是
	private String yichangms = "";//异常描述	字符(1000)	否	有异常内容时填写
	private String jilusj = "";//数据记录时间	日期	是	yyyymmdd hh24miss，本条数据的最后操作时间
	
	public String getLsh() {
		return lsh;
	}
	public void setLsh(String lsh) {
		this.lsh = lsh;
	}
	public String getShenfenz() {
		return shenfenz;
	}
	public void setShenfenz(String shenfenz) {
		this.shenfenz = shenfenz;
	}
	public String getYiyuanbh() {
		return yiyuanbh;
	}
	public void setYiyuanbh(String yiyuanbh) {
		this.yiyuanbh = yiyuanbh;
	}
	public String getTijianxjbh() {
		return tijianxjbh;
	}
	public void setTijianxjbh(String tijianxjbh) {
		this.tijianxjbh = tijianxjbh;
	}
	public String getTijianxh() {
		return tijianxh;
	}
	public void setTijianxh(String tijianxh) {
		this.tijianxh = tijianxh;
	}
	public String getKeshibm() {
		return keshibm;
	}
	public void setKeshibm(String keshibm) {
		this.keshibm = keshibm;
	}
	public String getKeshimc() {
		return keshimc;
	}
	public void setKeshimc(String keshimc) {
		this.keshimc = keshimc;
	}
	public String getKeshixj() {
		return keshixj;
	}
	public void setKeshixj(String keshixj) {
		this.keshixj = keshixj;
	}
	public String getTijianrq() {
		return tijianrq;
	}
	public void setTijianrq(String tijianrq) {
		this.tijianrq = tijianrq;
	}
	public String getTijianys() {
		return tijianys;
	}
	public void setTijianys(String tijianys) {
		this.tijianys = tijianys;
	}
	public String getYichangms() {
		return yichangms;
	}
	public void setYichangms(String yichangms) {
		this.yichangms = yichangms;
	}
	public String getJilusj() {
		return jilusj;
	}
	public void setJilusj(String jilusj) {
		this.jilusj = jilusj;
	}
}
