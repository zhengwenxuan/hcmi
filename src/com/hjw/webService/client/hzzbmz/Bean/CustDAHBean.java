package com.hjw.webService.client.hzzbmz.Bean;

public class CustDAHBean {
	private String mzhm="";//	门诊号码（体检有自己的规则生成一个）
	private String brxm="";//	病人姓名，不能为空
	private String sfzh="";//	身份证号
	private String brxz="";//	病人性质
	private String brxb="1";//	病人性别 1:男，2：女，不能为空
	private String csny="";//	出生年月 格式yyyymmddhh24miss
	private String mzdm="";//	民族代码 select*From GY_DMZD  where dmlb=1;
	private String xxdm="";//	血型代码 select*From GY_DMZD  where dmlb=21;
	private String dwmc="";//	单位名称
	private String dwdh="";//	单位电话
	private String hkdz="";//	户口地址
	private String jtdh="";//	家庭电话
	private String jzkh="";//	就诊卡号（市民卡号码）
	private String lxdh="";//	联系电话
	private String ybkh="";//	医保卡号
	private String jdjg="";//	建档机构
	private String jdsj="";//	建档时间 不能为空
	private String jdr="";//	建档人  不能为空
	private String xzz_sqs	="";//现住址_省 编码
	private String xzz_s="";//	现住址_市 编码
	private String xzz_x="";//	现住址_县 编码
	private String xzz_qtdz="";//	现住址_具体地址
	private String jhrm="";//	监护人姓名
	private String jhdh="";//	监护人电话
	private String jhdz="";//	监护人地址
	private String smdj="";//	实名登记  1实名（读卡）2非实名
	private String wsfzhyy="";//	无身份证号原因
	public String getMzhm() {
		return mzhm;
	}
	public void setMzhm(String mzhm) {
		this.mzhm = mzhm;
	}
	public String getBrxm() {
		return brxm;
	}
	public void setBrxm(String brxm) {
		this.brxm = brxm;
	}
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public String getBrxz() {
		return brxz;
	}
	public void setBrxz(String brxz) {
		this.brxz = brxz;
	}
	public String getBrxb() {
		return brxb;
	}
	public void setBrxb(String brxb) {
		this.brxb = brxb;
	}
	public String getCsny() {
		return csny;
	}
	public void setCsny(String csny) {
		this.csny = csny;
	}
	public String getMzdm() {
		return mzdm;
	}
	public void setMzdm(String mzdm) {
		this.mzdm = mzdm;
	}
	public String getXxdm() {
		return xxdm;
	}
	public void setXxdm(String xxdm) {
		this.xxdm = xxdm;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getDwdh() {
		return dwdh;
	}
	public void setDwdh(String dwdh) {
		this.dwdh = dwdh;
	}
	public String getHkdz() {
		return hkdz;
	}
	public void setHkdz(String hkdz) {
		this.hkdz = hkdz;
	}
	public String getJtdh() {
		return jtdh;
	}
	public void setJtdh(String jtdh) {
		this.jtdh = jtdh;
	}
	public String getJzkh() {
		return jzkh;
	}
	public void setJzkh(String jzkh) {
		this.jzkh = jzkh;
	}
	public String getLxdh() {
		return lxdh;
	}
	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	public String getYbkh() {
		return ybkh;
	}
	public void setYbkh(String ybkh) {
		this.ybkh = ybkh;
	}
	public String getJdjg() {
		return jdjg;
	}
	public void setJdjg(String jdjg) {
		this.jdjg = jdjg;
	}
	public String getJdsj() {
		return jdsj;
	}
	public void setJdsj(String jdsj) {
		this.jdsj = jdsj;
	}
	public String getJdr() {
		return jdr;
	}
	public void setJdr(String jdr) {
		this.jdr = jdr;
	}
	public String getXzz_sqs() {
		return xzz_sqs;
	}
	public void setXzz_sqs(String xzz_sqs) {
		this.xzz_sqs = xzz_sqs;
	}
	public String getXzz_s() {
		return xzz_s;
	}
	public void setXzz_s(String xzz_s) {
		this.xzz_s = xzz_s;
	}
	public String getXzz_x() {
		return xzz_x;
	}
	public void setXzz_x(String xzz_x) {
		this.xzz_x = xzz_x;
	}
	public String getXzz_qtdz() {
		return xzz_qtdz;
	}
	public void setXzz_qtdz(String xzz_qtdz) {
		this.xzz_qtdz = xzz_qtdz;
	}
	public String getJhrm() {
		return jhrm;
	}
	public void setJhrm(String jhrm) {
		this.jhrm = jhrm;
	}
	public String getJhdh() {
		return jhdh;
	}
	public void setJhdh(String jhdh) {
		this.jhdh = jhdh;
	}
	public String getJhdz() {
		return jhdz;
	}
	public void setJhdz(String jhdz) {
		this.jhdz = jhdz;
	}
	public String getSmdj() {
		return smdj;
	}
	public void setSmdj(String smdj) {
		this.smdj = smdj;
	}
	public String getWsfzhyy() {
		return wsfzhyy;
	}
	public void setWsfzhyy(String wsfzhyy) {
		this.wsfzhyy = wsfzhyy;
	}

}
