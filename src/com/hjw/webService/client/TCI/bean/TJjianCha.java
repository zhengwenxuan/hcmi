package com.hjw.webService.client.TCI.bean;

public class TJjianCha {
	private String lsh = "";//流水号	字符(30)	是	医院编号+观察编号:主键
	private String shenfenz = "";//身份证	字符(18)	是	
	private String yiyuanbh = "";//医院编号	字符(6)	是	915245
	private String tijianxh = "";//体检序号	字符(20)	是	关联体检记录
	private String jianchasb = "";//检查识别	字符(64)	是	医院内检查记录主键值
	private String keshibm = "";//科室编码	字符(20)	是 CVX_DepartmentCode科室代码字典
	private String keshimc = "";//科室名称	字符(20)	是	
	private String xiangmubh = "";//项目编码	字符(10)	是
	private String xiangmumc = "";//项目名称	字符(100)	是
	private String jianchajgsj = "";//结果所见（客观）	字符(1000)	否	有客观检查结果时填写
	private String jianchajl = "";//检查结论（主观诊断）	字符(2000)	否	有主观检查结果时填写
	private String jianchays = "";//检查医生	字符(30)	否	
	private String jiancharq = "";//检查日期	日期	是	格式YYYYMMDD
	private String shenheys = "";//审核医生	字符(30)	否	
	private String shenherq = "";//审核日期	日期	否	格式YYYYMMDD
	private long keshixssx;//科室显示顺序	数字(8)	是
	private long xiangmusxsx;//项目显示顺序	数字(8)	是
	private String beizhu = "";//备注	字符(100)	否	
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
	public String getTijianxh() {
		return tijianxh;
	}
	public void setTijianxh(String tijianxh) {
		this.tijianxh = tijianxh;
	}
	public String getJianchasb() {
		return jianchasb;
	}
	public void setJianchasb(String jianchasb) {
		this.jianchasb = jianchasb;
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
	public String getXiangmubh() {
		return xiangmubh;
	}
	public void setXiangmubh(String xiangmubh) {
		this.xiangmubh = xiangmubh;
	}
	public String getXiangmumc() {
		return xiangmumc;
	}
	public void setXiangmumc(String xiangmumc) {
		this.xiangmumc = xiangmumc;
	}
	public String getJianchajgsj() {
		return jianchajgsj;
	}
	public void setJianchajgsj(String jianchajgsj) {
		this.jianchajgsj = jianchajgsj;
	}
	public String getJianchajl() {
		return jianchajl;
	}
	public void setJianchajl(String jianchajl) {
		this.jianchajl = jianchajl;
	}
	public String getJianchays() {
		return jianchays;
	}
	public void setJianchays(String jianchays) {
		this.jianchays = jianchays;
	}
	public String getJiancharq() {
		return jiancharq;
	}
	public void setJiancharq(String jiancharq) {
		this.jiancharq = jiancharq;
	}
	public String getShenheys() {
		return shenheys;
	}
	public void setShenheys(String shenheys) {
		this.shenheys = shenheys;
	}
	public String getShenherq() {
		return shenherq;
	}
	public void setShenherq(String shenherq) {
		this.shenherq = shenherq;
	}
	public long getKeshixssx() {
		return keshixssx;
	}
	public void setKeshixssx(long keshixssx) {
		this.keshixssx = keshixssx;
	}
	public long getXiangmusxsx() {
		return xiangmusxsx;
	}
	public void setXiangmusxsx(long xiangmusxsx) {
		this.xiangmusxsx = xiangmusxsx;
	}
	public String getBeizhu() {
		return beizhu;
	}
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}
	public String getJilusj() {
		return jilusj;
	}
	public void setJilusj(String jilusj) {
		this.jilusj = jilusj;
	}
}
