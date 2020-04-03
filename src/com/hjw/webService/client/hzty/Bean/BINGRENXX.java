package com.hjw.webService.client.hzty.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "BINGRENXX")  
@XmlType(propOrder = {})  
public class BINGRENXX {
		
	@XmlElement 
	private String JIUZHENKH="";// 就诊卡号 
	
	@XmlElement 
	private String BINGRENXM="";// 病人姓名   
	
	@XmlElement 
	private String ZHENGJIANLX="";// 证件类型   
	
	@XmlElement 
	private String ZHENJIANHM="";// 证件号码  
	
	@XmlElement 
	private String LIANXIDH="";// 联系电话   
	
	@XmlElement 
	private String BINGANH="";// 病案号   
	
	@XmlElement 
	private String ZUOFEIPB="";// 作废判别   
	
	@XmlElement 
	private String BINGRENLB="";// 病人类别  详见附录 
	
	@XmlElement 
	private String FAKABZ="";// 发卡标志  0 或空：未发卡 1：已发卡

	@XmlElement 
	private String XINGBIE="";// 性别  详见附录
	
	@XmlElement 
	private String CHUSHENGRQ="";// 出生日期   

	@XmlElement 
	private String JIATINGZZ="";// 家庭地址     

	public String getJIUZHENKH() {
		return JIUZHENKH;
	}

	public void setJIUZHENKH(String jIUZHENKH) {
		JIUZHENKH = jIUZHENKH;
	}

	public String getBINGRENXM() {
		return BINGRENXM;
	}

	public void setBINGRENXM(String bINGRENXM) {
		BINGRENXM = bINGRENXM;
	}

	public String getZHENGJIANLX() {
		return ZHENGJIANLX;
	}

	public void setZHENGJIANLX(String zHENGJIANLX) {
		ZHENGJIANLX = zHENGJIANLX;
	}

	public String getZHENJIANHM() {
		return ZHENJIANHM;
	}

	public void setZHENJIANHM(String zHENJIANHM) {
		ZHENJIANHM = zHENJIANHM;
	}

	public String getLIANXIDH() {
		return LIANXIDH;
	}

	public void setLIANXIDH(String lIANXIDH) {
		LIANXIDH = lIANXIDH;
	}

	public String getBINGANH() {
		return BINGANH;
	}

	public void setBINGANH(String bINGANH) {
		BINGANH = bINGANH;
	}

	public String getZUOFEIPB() {
		return ZUOFEIPB;
	}

	public void setZUOFEIPB(String zUOFEIPB) {
		ZUOFEIPB = zUOFEIPB;
	}

	public String getBINGRENLB() {
		return BINGRENLB;
	}

	public void setBINGRENLB(String bINGRENLB) {
		BINGRENLB = bINGRENLB;
	}

	public String getFAKABZ() {
		return FAKABZ;
	}

	public void setFAKABZ(String fAKABZ) {
		FAKABZ = fAKABZ;
	}

	public String getXINGBIE() {
		return XINGBIE;
	}

	public void setXINGBIE(String xINGBIE) {
		if("1".equals(xINGBIE)){
			this.XINGBIE="男";
		}else if("2".equals(xINGBIE)){
			this.XINGBIE="女";
		}else{
			this.XINGBIE="";
		}
	}

	public String getCHUSHENGRQ() {
		return CHUSHENGRQ;
	}

	public void setCHUSHENGRQ(String cHUSHENGRQ) {
		CHUSHENGRQ = cHUSHENGRQ;
	}

	public String getJIATINGZZ() {
		return JIATINGZZ;
	}

	public void setJIATINGZZ(String jIATINGZZ) {
		JIATINGZZ = jIATINGZZ;
	}

	
}
