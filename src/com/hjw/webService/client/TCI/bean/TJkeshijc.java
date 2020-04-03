package com.hjw.webService.client.TCI.bean;

public class TJkeshijc {
	private String lsh = "";//流水号	字符(30)	是	医院编号+体检识别号:主键
	private String shenfenz = "";//身份证	字符(18)	是
	private String yiyuanbh = "";//医院编号	字符(6)	是
	private String tijianxh = "";//体检序号	字符(20)	是	关联体检记录
	private String tijiansb = "";//体检识别号	字符(20)	是	医院内体检项目记录流水号
	private String keshibh = "";//科室编码	字符(10)	否 CVX_DepartmentCode科室代码字典
	private String keshimc = "";//科室名称	字符(50)	是
	private String xiangmubh = "";//项目编码	字符(10)	否
	private String xiangmumc = "";//项目名称	字符(100)	是
	private String yichangbz = "";//是否异常	字符(10)	是	1 是 0 否
	private String tijianjg = "";//体检结果	字符(1000)	是
	private String jianchays = "";//检查医生	字符(30)	否
	private String jiancharq = "";//检查日期	日期	是
	private long keshixssx;//科室显示顺序	数字(8)	否
	private long xiangmuxssx;//项目显示顺序	数字(8)	否
	private String tijianjgdw = "";//体检结果单位	字符(30)	否
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
	public String getTijiansb() {
		return tijiansb;
	}
	public void setTijiansb(String tijiansb) {
		this.tijiansb = tijiansb;
	}
	public String getKeshibh() {
		return keshibh;
	}
	public void setKeshibh(String keshibh) {
		this.keshibh = keshibh;
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
	public String getYichangbz() {
		return yichangbz;
	}
	public void setYichangbz(String yichangbz) {
		this.yichangbz = yichangbz;
	}
	public String getTijianjg() {
		return tijianjg;
	}
	public void setTijianjg(String tijianjg) {
		this.tijianjg = tijianjg;
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
	public long getKeshixssx() {
		return keshixssx;
	}
	public void setKeshixssx(long keshixssx) {
		this.keshixssx = keshixssx;
	}
	public long getXiangmuxssx() {
		return xiangmuxssx;
	}
	public void setXiangmuxssx(long xiangmuxssx) {
		this.xiangmuxssx = xiangmuxssx;
	}
	public String getTijianjgdw() {
		return tijianjgdw;
	}
	public void setTijianjgdw(String tijianjgdw) {
		this.tijianjgdw = tijianjgdw;
	}
	public String getJilusj() {
		return jilusj;
	}
	public void setJilusj(String jilusj) {
		this.jilusj = jilusj;
	}
}
