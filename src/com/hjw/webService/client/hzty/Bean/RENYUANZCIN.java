package com.hjw.webService.client.hzty.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "RENYUANZC_IN")  
@XmlType(propOrder = {})  
public class RENYUANZCIN {
	@XmlElement 
	private BASEINFO BASEINFO= new BASEINFO();
	
	@XmlElement 
	private String JIUZHENKLX="7";// 就诊卡类型  详见附录  身份证
	
	@XmlElement 
	private String JIUZHENKH="";// 就诊卡号   
	
	@XmlElement 
	private String BINGRENLB="1";// 病人类别  	详见附录(就诊卡类 型为健康龙卡、就诊 卡时可以传空)  自费
	
	@XmlElement 
	private String YIBAOKLX="3";// 医保卡类型  详见附录（可直接传 3） 
	
	@XmlElement 
	private String YIBAOKXX="";// 医保卡信息  指通过社保提供的 DLL 读取卡内信息 
	
	@XmlElement 
	private String YIBAOKH="";// 医保卡号   
	
	@XmlElement 
	private String GERENBH="";// 个人编号  
	
	@XmlElement 
	private String BINGLIBH="";// 病历本号   
	
	@XmlElement 
	private String XINGMING="";// 姓名   
	
	@XmlElement 
	private String XINGBIE="";// 性别  详见附录 1男 2女 传编码
	
	@XmlElement 
	private String MINZU="";// 民族   
	
	@XmlElement 
	private String CHUSHENGRQ="";// 出生日期   
	
	@XmlElement 
	private String ZHENGJIANLX="1";// 证件类型   
	
	@XmlElement 
	private String ZHENGJIANHM="";// 证件号码   
	
	@XmlElement 
	private String DANWEILX="";// 单位类型  
	
	@XmlElement 
	private String DANWEIBH="";// 单位编号   
	
	@XmlElement 
	private String DANWEIMC="";// 单位名称   
	
	@XmlElement 
	private String JIATINGZZ="";// 家庭地址   
	
	@XmlElement 
	private String RENYUANLB="普通";// 人员类别   
	
	@XmlElement 
	private String LIANXIDH="";// 联系电话   
	
	@XmlElement 
	private String YINHANGKH="";// 银行号卡  
	
	@XmlElement 
	private String QIANYUEBZ="0";// 签约标志  0 未签约 1 签约 
	
	@XmlElement 
	private String YILIAOLB="00";// 医疗类别  详见附录（可直接传 00) 
	
	@XmlElement 
	private String JIESUANLB="02";// 结算类别  详见附录(可直接传 02) 
	
	@XmlElement 
	private String YIBAOKMM="";// 医保卡密码  	可是为空，有部分杭 州市医保人是有密码 的  HIS服务平台接口说明文档V1.1  
	
	@XmlElement 
	private String CHONGFUJYMX="";// 重复交易信息   
	
	@XmlElement 
	private String PHOTO="";// 照片  64 位二进制编码 
	
	@XmlElement 
	private String SHIFOUYK="0";// 是否有卡  0：无卡，1：有卡 
	
	@XmlElement 
	private String BANGDINGYHK="0";// 绑定银行卡  0：否，1：是 
	
	@XmlElement 
	private String ZHONGDUANSBXX="";// 终端设备信息   
	
	@XmlElement 
	private String XUNIZHBZ="";// 虚拟账户标志  2、联众账户 3、市民卡账户 4、工行账户 6、广济卡账户 
	
	@XmlElement 
	private String XUNIZH="";// 虚拟账户号码   
	
	@XmlElement 
	private String BINGANH="";// 病案号   
	
	public BASEINFO getBASEINFO() {
		return BASEINFO;
	}
	public void setBASEINFO(BASEINFO bASEINFO) {
		BASEINFO = bASEINFO;
	}
	public String getJIUZHENKLX() {
		return JIUZHENKLX;
	}
	public void setJIUZHENKLX(String jIUZHENKLX) {
		JIUZHENKLX = jIUZHENKLX;
	}
	public String getJIUZHENKH() {
		return JIUZHENKH;
	}
	public void setJIUZHENKH(String jIUZHENKH) {
		JIUZHENKH = jIUZHENKH;
	}
	public String getBINGRENLB() {
		return BINGRENLB;
	}
	public void setBINGRENLB(String bINGRENLB) {
		BINGRENLB = bINGRENLB;
	}
	public String getYIBAOKLX() {
		return YIBAOKLX;
	}
	public void setYIBAOKLX(String yIBAOKLX) {
		YIBAOKLX = yIBAOKLX;
	}
	public String getYIBAOKXX() {
		return YIBAOKXX;
	}
	public void setYIBAOKXX(String yIBAOKXX) {
		YIBAOKXX = yIBAOKXX;
	}
	public String getYIBAOKH() {
		return YIBAOKH;
	}
	public void setYIBAOKH(String yIBAOKH) {
		YIBAOKH = yIBAOKH;
	}
	public String getGERENBH() {
		return GERENBH;
	}
	public void setGERENBH(String gERENBH) {
		GERENBH = gERENBH;
	}
	public String getBINGLIBH() {
		return BINGLIBH;
	}
	public void setBINGLIBH(String bINGLIBH) {
		BINGLIBH = bINGLIBH;
	}
	public String getXINGMING() {
		return XINGMING;
	}
	public void setXINGMING(String xINGMING) {
		XINGMING = xINGMING;
	}
	public String getXINGBIE() {
		return XINGBIE;
	}
	public void setXINGBIE(String xINGBIE) {
		if("男".equals(xINGBIE)){
			this.XINGBIE="1";
		}else if("女".equals(xINGBIE)){
			this.XINGBIE="2";
		}else{
			this.XINGBIE="0";
		}
	}
	public String getMINZU() {
		return MINZU;
	}
	public void setMINZU(String mINZU) {
		MINZU = mINZU;
	}
	public String getCHUSHENGRQ() {
		return CHUSHENGRQ;
	}
	public void setCHUSHENGRQ(String cHUSHENGRQ) {
		CHUSHENGRQ = cHUSHENGRQ;
	}
	public String getZHENGJIANLX() {
		return ZHENGJIANLX;
	}
	public void setZHENGJIANLX(String zHENGJIANLX) {
		ZHENGJIANLX = zHENGJIANLX;
	}
	public String getZHENGJIANHM() {
		return ZHENGJIANHM;
	}
	public void setZHENGJIANHM(String zHENGJIANHM) {
		ZHENGJIANHM = zHENGJIANHM;
	}
	public String getDANWEILX() {
		return DANWEILX;
	}
	public void setDANWEILX(String dANWEILX) {
		DANWEILX = dANWEILX;
	}
	public String getDANWEIBH() {
		return DANWEIBH;
	}
	public void setDANWEIBH(String dANWEIBH) {
		DANWEIBH = dANWEIBH;
	}
	public String getDANWEIMC() {
		return DANWEIMC;
	}
	public void setDANWEIMC(String dANWEIMC) {
		DANWEIMC = dANWEIMC;
	}
	public String getJIATINGZZ() {
		return JIATINGZZ;
	}
	public void setJIATINGZZ(String jIATINGZZ) {
		JIATINGZZ = jIATINGZZ;
	}
	public String getRENYUANLB() {
		return RENYUANLB;
	}
	public void setRENYUANLB(String rENYUANLB) {
		RENYUANLB = rENYUANLB;
	}
	public String getLIANXIDH() {
		return LIANXIDH;
	}
	public void setLIANXIDH(String lIANXIDH) {
		LIANXIDH = lIANXIDH;
	}
	public String getYINHANGKH() {
		return YINHANGKH;
	}
	public void setYINHANGKH(String yINHANGKH) {
		YINHANGKH = yINHANGKH;
	}
	public String getQIANYUEBZ() {
		return QIANYUEBZ;
	}
	public void setQIANYUEBZ(String qIANYUEBZ) {
		QIANYUEBZ = qIANYUEBZ;
	}
	public String getYILIAOLB() {
		return YILIAOLB;
	}
	public void setYILIAOLB(String yILIAOLB) {
		YILIAOLB = yILIAOLB;
	}
	public String getJIESUANLB() {
		return JIESUANLB;
	}
	public void setJIESUANLB(String jIESUANLB) {
		JIESUANLB = jIESUANLB;
	}
	public String getYIBAOKMM() {
		return YIBAOKMM;
	}
	public void setYIBAOKMM(String yIBAOKMM) {
		YIBAOKMM = yIBAOKMM;
	}
	public String getCHONGFUJYMX() {
		return CHONGFUJYMX;
	}
	public void setCHONGFUJYMX(String cHONGFUJYMX) {
		CHONGFUJYMX = cHONGFUJYMX;
	}
	public String getPHOTO() {
		return PHOTO;
	}
	public void setPHOTO(String pHOTO) {
		PHOTO = pHOTO;
	}
	public String getSHIFOUYK() {
		return SHIFOUYK;
	}
	public void setSHIFOUYK(String sHIFOUYK) {
		SHIFOUYK = sHIFOUYK;
	}
	public String getBANGDINGYHK() {
		return BANGDINGYHK;
	}
	public void setBANGDINGYHK(String bANGDINGYHK) {
		BANGDINGYHK = bANGDINGYHK;
	}
	public String getZHONGDUANSBXX() {
		return ZHONGDUANSBXX;
	}
	public void setZHONGDUANSBXX(String zHONGDUANSBXX) {
		ZHONGDUANSBXX = zHONGDUANSBXX;
	}
	public String getXUNIZHBZ() {
		return XUNIZHBZ;
	}
	public void setXUNIZHBZ(String xUNIZHBZ) {
		XUNIZHBZ = xUNIZHBZ;
	}
	public String getXUNIZH() {
		return XUNIZH;
	}
	public void setXUNIZH(String xUNIZH) {
		XUNIZH = xUNIZH;
	}
	public String getBINGANH() {
		return BINGANH;
	}
	public void setBINGANH(String bINGANH) {
		BINGANH = bINGANH;
	}
	
	
}
