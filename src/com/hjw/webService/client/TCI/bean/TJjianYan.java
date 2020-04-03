package com.hjw.webService.client.TCI.bean;

public class TJjianYan {
	
	private String lsh = "";//流水号	字符(30)	是	医院编号+观察编号:主键
	private String shenfenz = "";//身份证	字符(18)	是	
	private String yiyuanbh = "";//医院编号	字符(6)	是	915245
	private String tijianxh = "";//体检序号	字符(20)	是	关联体检记录
	private String keshibm = "";//科室编码	字符(20)	是 CVX_DepartmentCode科室代码字典
	private String keshimc = "";//科室名称	字符(20)	是	
	private String zuhebh = "";//组合编码	字符(10)	否	
	private String zuhemc = "";//组合名称	字符(20)	否	
	private String yangbenbh = "";//样本编号	字符(20)	否
	private String guanchabh = "";//观察编号	字符(28)	是	医院内体检检验记录主键
	private String guanchamc = "";//观察名称	字符(100)	是
	private String jiliangdw = "";//计量单位	字符(30)	否	
	private String tishi = "";//异常提示符	字符(20)	否
	private String jianyanjg = "";//检验结果	字符(1000)	是
	private String cankaofw = "";//参考范围	字符(200)	否
	private String jianyanys = "";//检验医生	字符(30)	否
	private String jianyanrq = "";//检查日期	日期	是	格式YYYYMMDD
	private String shenheys = "";//审核医生	字符(30)	否	
	private String shenherq = "";//审核日期	日期	否	格式YYYYMMDD
	private long keshixssx;//科室显示顺序	数字(8)	否	
	private long zuhexssx;//组合显示顺序	数字(8)	否
	private long shiyanxssx;//项目显示顺序	数字(8)	否
	private String jilusj = "";//数据记录时间	日期	是	yyyymmdd hh24miss，本条数据的最后操作时间
	private String guanchabm = "";//观察编码	字符(20)	是	检验项目编码
	
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
	public String getZuhebh() {
		return zuhebh;
	}
	public void setZuhebh(String zuhebh) {
		this.zuhebh = zuhebh;
	}
	public String getZuhemc() {
		return zuhemc;
	}
	public void setZuhemc(String zuhemc) {
		this.zuhemc = zuhemc;
	}
	public String getYangbenbh() {
		return yangbenbh;
	}
	public void setYangbenbh(String yangbenbh) {
		this.yangbenbh = yangbenbh;
	}
	public String getGuanchabh() {
		return guanchabh;
	}
	public void setGuanchabh(String guanchabh) {
		this.guanchabh = guanchabh;
	}
	public String getGuanchamc() {
		return guanchamc;
	}
	public void setGuanchamc(String guanchamc) {
		this.guanchamc = guanchamc;
	}
	public String getJiliangdw() {
		return jiliangdw;
	}
	public void setJiliangdw(String jiliangdw) {
		this.jiliangdw = jiliangdw;
	}
	public String getTishi() {
		return tishi;
	}
	public void setTishi(String tishi) {
		this.tishi = tishi;
	}
	public String getJianyanjg() {
		return jianyanjg;
	}
	public void setJianyanjg(String jianyanjg) {
		this.jianyanjg = jianyanjg;
	}
	public String getCankaofw() {
		return cankaofw;
	}
	public void setCankaofw(String cankaofw) {
		this.cankaofw = cankaofw;
	}
	public String getJianyanys() {
		return jianyanys;
	}
	public void setJianyanys(String jianyanys) {
		this.jianyanys = jianyanys;
	}
	public String getJianyanrq() {
		return jianyanrq;
	}
	public void setJianyanrq(String jianyanrq) {
		this.jianyanrq = jianyanrq;
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
	public long getZuhexssx() {
		return zuhexssx;
	}
	public void setZuhexssx(long zuhexssx) {
		this.zuhexssx = zuhexssx;
	}
	public long getShiyanxssx() {
		return shiyanxssx;
	}
	public void setShiyanxssx(long shiyanxssx) {
		this.shiyanxssx = shiyanxssx;
	}
	public String getJilusj() {
		return jilusj;
	}
	public void setJilusj(String jilusj) {
		this.jilusj = jilusj;
	}
	public String getGuanchabm() {
		return guanchabm;
	}
	public void setGuanchabm(String guanchabm) {
		this.guanchabm = guanchabm;
	}
}
