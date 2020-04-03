package com.hjw.webService.client.TCI.bean;

public class TJzongJian {
	
	private String lsh = ""; //流水号	字符(30)	是	医院编号+体检序号:主键
	private String shenfenz = ""; //身份证	字符(18)	是	
	private String yiyuanbh = ""; //医院编号	字符(6)	是	
	private String tijianxh = ""; //体检序号	字符(20)	是	医院内体检记录主键
	private String xingming = ""; //姓名	字符(20)	是	
	private String xingbie = ""; //性别	字符(1)	是		GB_T2261_1:性别代码字典    1男 2女
	private String nianling = ""; //年龄	字符(10)	是	
	private String zongjianbh = "";//总检编号	字符(20)	否	
	private String zhujianys = "";//总检医生	字符(30)	否
	private String zhujianrq = "";//总检日期	日期	是	格式YYYYMMDD
	private String shenheys = "";//审核医生	字符(30)	否	
	private String shenherq = "";//审核日期	日期	否	格式YYYYMMDD
	private String fafangz = "";//发放者	字符(30)	否	
	private String fafangrq = ""; //发放日期	日期	否	格式YYYYMMDD
	private String zhujianzd = "";//总检诊断	字符(4000)	是	如:未见异常
	private String zhujianxj = "";//总检小结	字符(4000)	是	
	private String zhujianyj = "";//总检建议	字符(4000)	否	建议指导
	private String jiatingdz = "";//家庭地址	字符(200)	否	联系地址或工作单位
	private String tijianlx = "";//体检类型	字符(2)	否		CV0001_15体检类型代码
	private String chushengrq = "";//出生日期	日期	否	YYYYMMDD
	private String jilusj = "";//数据记录时间	日期	是	yyyymmdd hh24miss，本条数据的最后操作时间
	private String kahao = "";//卡号	字符(32)	是	就诊时所使用的卡号
	private String kalx = "";//卡类型	字符(2)	是	就诊时使用的就诊卡类型，如市民卡、医保卡等	CVX_CardType卡类型字典
	private String tijianrq = "";//体检日期	日期	是	本次体检开始日期,YYYYMMDD
	private String baojianhao = "";//保健号	字符(32)	否	进行健康体检时所持有的保健号
	
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
	public String getXingming() {
		return xingming;
	}
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getXingbie() {
		return xingbie;
	}
	public void setXingbie(String xingbie) {
		this.xingbie = xingbie;
	}
	public String getNianling() {
		return nianling;
	}
	public void setNianling(String nianling) {
		this.nianling = nianling;
	}
	public String getZongjianbh() {
		return zongjianbh;
	}
	public void setZongjianbh(String zongjianbh) {
		this.zongjianbh = zongjianbh;
	}
	public String getZhujianys() {
		return zhujianys;
	}
	public void setZhujianys(String zhujianys) {
		this.zhujianys = zhujianys;
	}
	public String getZhujianrq() {
		return zhujianrq;
	}
	public void setZhujianrq(String zhujianrq) {
		this.zhujianrq = zhujianrq;
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
	public String getFafangz() {
		return fafangz;
	}
	public void setFafangz(String fafangz) {
		this.fafangz = fafangz;
	}
	public String getFafangrq() {
		return fafangrq;
	}
	public void setFafangrq(String fafangrq) {
		this.fafangrq = fafangrq;
	}
	public String getZhujianzd() {
		return zhujianzd;
	}
	public void setZhujianzd(String zhujianzd) {
		this.zhujianzd = zhujianzd;
	}
	public String getZhujianxj() {
		return zhujianxj;
	}
	public void setZhujianxj(String zhujianxj) {
		this.zhujianxj = zhujianxj;
	}
	public String getZhujianyj() {
		return zhujianyj;
	}
	public void setZhujianyj(String zhujianyj) {
		this.zhujianyj = zhujianyj;
	}
	public String getJiatingdz() {
		return jiatingdz;
	}
	public void setJiatingdz(String jiatingdz) {
		this.jiatingdz = jiatingdz;
	}
	public String getTijianlx() {
		return tijianlx;
	}
	public void setTijianlx(String tijianlx) {
		this.tijianlx = tijianlx;
	}
	public String getChushengrq() {
		return chushengrq;
	}
	public void setChushengrq(String chushengrq) {
		this.chushengrq = chushengrq;
	}
	public String getJilusj() {
		return jilusj;
	}
	public void setJilusj(String jilusj) {
		this.jilusj = jilusj;
	}
	public String getKahao() {
		return kahao;
	}
	public void setKahao(String kahao) {
		this.kahao = kahao;
	}
	public String getKalx() {
		return kalx;
	}
	public void setKalx(String kalx) {
		this.kalx = kalx;
	}
	public String getTijianrq() {
		return tijianrq;
	}
	public void setTijianrq(String tijianrq) {
		this.tijianrq = tijianrq;
	}
	public String getBaojianhao() {
		return baojianhao;
	}
	public void setBaojianhao(String baojianhao) {
		this.baojianhao = baojianhao;
	}
}
