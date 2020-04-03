package com.hjw.webService.client.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ITEM")  
@XmlType(propOrder = {})  
public class YbCustom {
	@XmlElement  
	public String PATIENT_ID;//	病人标识号
	@XmlElement  
	public String NAME;//	姓名
	@XmlElement  
	public String NAME_PHONETIC;//	姓名拼音
	@XmlElement  
	public String SEX;//	性别
	@XmlElement  
	public String DATE_OF_BIRTH;//	出生日期
	@XmlElement  
	public String BIRTH_PLACE;//	出生地
	@XmlElement  
	public String CITIZENSHIP;//	国籍
	@XmlElement  
	public String NATION;//	民族:
	@XmlElement  
	public String ID_NO;//	身份证号
	@XmlElement  
	public String IDENTITY;//	身份:
	@XmlElement  
	public String UNIT_IN_CONTRACT;//	合同单位
	@XmlElement  
	public String MAILING_ADDRESS;//	通信地址
	@XmlElement  
	public String ZIP_CODE;//	邮政编码
	@XmlElement  
	public String PHONE_NUMBER_HOME;//	家庭电话号码
	@XmlElement  
	public String PHONE_NUMBER_BUSINESS;//	单位电话号码
	@XmlElement  
	public String NEXT_OF_KIN;//	联系人姓名
	@XmlElement  
	public String RELATIONSHIP;//	与联系人关系 
	@XmlElement  
	public String NEXT_OF_KIN_ADDR;//	联系人地址
	@XmlElement  
	public String NEXT_OF_KIN_ZIP_CODE;//	联系人邮政编码
	@XmlElement  
	public String NEXT_OF_KIN_PHONE;//	联系人电话号码
	@XmlElement  
	public String OPERATOR;//	操作员
	@XmlElement  
	public String BUSINESS_ZIP_CODE;//	邮编
	@XmlElement  
	public String PHOTO;//	照片
	@XmlElement  
	public String PATIENT_CLASS;//	患者来源
	@XmlElement  
	public String DEGREE;//	学历
	@XmlElement  
	public String E_NAME;//	电子邮件地址
	@XmlElement  
	public String OCCUPATION;//	职业
	@XmlElement  
	public String NATIVE_PLACE;//	籍贯（区划代码）
	@XmlElement  
	public String MAILING_ADDRESS_CODE;//	户口地址行政区划
	@XmlElement  
	public String MAILING_STREET_CODE;//	户口地址乡镇/街道代码
	@XmlElement  
	public String ALERGY;//	过敏史
	@XmlElement  
	public String MARITAL_STATUS;//	婚姻状况
	@XmlElement  
	public String NEXT_OF_SEX;//	联系人性别
	@XmlElement  
	public String NEXT_OF_BATH;//	联系人出生日期
	@XmlElement  
	public String NEXT_OF_ID;//	联系人身份证号
	@XmlElement  
	public String AGE;//	年龄
	@XmlElement  
	public String CHARGE_TYPE;//	费别
	@XmlElement  
	public String VISIT_DEPT;//	就诊科室
	@XmlElement  
	public String CARD_NAME;//	卡名
	@XmlElement  
	public String CARD_NO;//	卡号
	@XmlElement  
	public String INVOICE_NO;//	发票号码
	@XmlElement  
	public String CLINIC_NO;//	门诊号
	@XmlElement  
	public String CLINIC_DATE_SCHEDULED;//	预约就诊时间
	
	@XmlElement  
	public String RESULT;//	返回成功为0，失败为失败信息
	@XmlElement  
	public String VISIT_DATE;//	就诊日期
	@XmlElement  
	public String VISIT_NO;//	就诊号	
	
	public String getRESULT() {
		return RESULT;
	}
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}
	public String getVISIT_DATE() {
		return VISIT_DATE;
	}
	public void setVISIT_DATE(String vISIT_DATE) {
		VISIT_DATE = vISIT_DATE;
	}
	public String getVISIT_NO() {
		return VISIT_NO;
	}
	public void setVISIT_NO(String vISIT_NO) {
		VISIT_NO = vISIT_NO;
	}
	
	public String getPATIENT_ID() {
		return PATIENT_ID;
	}
	public void setPATIENT_ID(String pATIENT_ID) {
		PATIENT_ID = pATIENT_ID;
	}
	
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getNAME_PHONETIC() {
		return NAME_PHONETIC;
	}
	public void setNAME_PHONETIC(String nAME_PHONETIC) {
		NAME_PHONETIC = nAME_PHONETIC;
	}
	public String getSEX() {
		return SEX;
	}
	public void setSEX(String sEX) {
		SEX = sEX;
	}
	public String getDATE_OF_BIRTH() {
		return DATE_OF_BIRTH;
	}
	public void setDATE_OF_BIRTH(String dATE_OF_BIRTH) {
		DATE_OF_BIRTH = dATE_OF_BIRTH;
	}
	public String getBIRTH_PLACE() {
		return BIRTH_PLACE;
	}
	public void setBIRTH_PLACE(String bIRTH_PLACE) {
		BIRTH_PLACE = bIRTH_PLACE;
	}
	public String getCITIZENSHIP() {
		return CITIZENSHIP;
	}
	public void setCITIZENSHIP(String cITIZENSHIP) {
		CITIZENSHIP = cITIZENSHIP;
	}
	public String getNATION() {
		return NATION;
	}
	public void setNATION(String nATION) {
		NATION = nATION;
	}
	public String getID_NO() {
		return ID_NO;
	}
	public void setID_NO(String iD_NO) {
		ID_NO = iD_NO;
	}
	public String getIDENTITY() {
		return IDENTITY;
	}
	public void setIDENTITY(String iDENTITY) {
		IDENTITY = iDENTITY;
	}
	public String getUNIT_IN_CONTRACT() {
		return UNIT_IN_CONTRACT;
	}
	public void setUNIT_IN_CONTRACT(String uNIT_IN_CONTRACT) {
		UNIT_IN_CONTRACT = uNIT_IN_CONTRACT;
	}
	public String getMAILING_ADDRESS() {
		return MAILING_ADDRESS;
	}
	public void setMAILING_ADDRESS(String mAILING_ADDRESS) {
		MAILING_ADDRESS = mAILING_ADDRESS;
	}
	public String getZIP_CODE() {
		return ZIP_CODE;
	}
	public void setZIP_CODE(String zIP_CODE) {
		ZIP_CODE = zIP_CODE;
	}
	public String getPHONE_NUMBER_HOME() {
		return PHONE_NUMBER_HOME;
	}
	public void setPHONE_NUMBER_HOME(String pHONE_NUMBER_HOME) {
		PHONE_NUMBER_HOME = pHONE_NUMBER_HOME;
	}
	public String getPHONE_NUMBER_BUSINESS() {
		return PHONE_NUMBER_BUSINESS;
	}
	public void setPHONE_NUMBER_BUSINESS(String pHONE_NUMBER_BUSINESS) {
		PHONE_NUMBER_BUSINESS = pHONE_NUMBER_BUSINESS;
	}
	public String getNEXT_OF_KIN() {
		return NEXT_OF_KIN;
	}
	public void setNEXT_OF_KIN(String nEXT_OF_KIN) {
		NEXT_OF_KIN = nEXT_OF_KIN;
	}
	public String getRELATIONSHIP() {
		return RELATIONSHIP;
	}
	public void setRELATIONSHIP(String rELATIONSHIP) {
		RELATIONSHIP = rELATIONSHIP;
	}
	public String getNEXT_OF_KIN_ADDR() {
		return NEXT_OF_KIN_ADDR;
	}
	public void setNEXT_OF_KIN_ADDR(String nEXT_OF_KIN_ADDR) {
		NEXT_OF_KIN_ADDR = nEXT_OF_KIN_ADDR;
	}
	public String getNEXT_OF_KIN_ZIP_CODE() {
		return NEXT_OF_KIN_ZIP_CODE;
	}
	public void setNEXT_OF_KIN_ZIP_CODE(String nEXT_OF_KIN_ZIP_CODE) {
		NEXT_OF_KIN_ZIP_CODE = nEXT_OF_KIN_ZIP_CODE;
	}
	public String getNEXT_OF_KIN_PHONE() {
		return NEXT_OF_KIN_PHONE;
	}
	public void setNEXT_OF_KIN_PHONE(String nEXT_OF_KIN_PHONE) {
		NEXT_OF_KIN_PHONE = nEXT_OF_KIN_PHONE;
	}
	public String getOPERATOR() {
		return OPERATOR;
	}
	public void setOPERATOR(String oPERATOR) {
		OPERATOR = oPERATOR;
	}
	public String getBUSINESS_ZIP_CODE() {
		return BUSINESS_ZIP_CODE;
	}
	public void setBUSINESS_ZIP_CODE(String bUSINESS_ZIP_CODE) {
		BUSINESS_ZIP_CODE = bUSINESS_ZIP_CODE;
	}
	public String getPHOTO() {
		return PHOTO;
	}
	public void setPHOTO(String pHOTO) {
		PHOTO = pHOTO;
	}
	public String getPATIENT_CLASS() {
		return PATIENT_CLASS;
	}
	public void setPATIENT_CLASS(String pATIENT_CLASS) {
		PATIENT_CLASS = pATIENT_CLASS;
	}
	public String getDEGREE() {
		return DEGREE;
	}
	public void setDEGREE(String dEGREE) {
		DEGREE = dEGREE;
	}
	public String getE_NAME() {
		return E_NAME;
	}
	public void setE_NAME(String e_NAME) {
		E_NAME = e_NAME;
	}
	public String getOCCUPATION() {
		return OCCUPATION;
	}
	public void setOCCUPATION(String oCCUPATION) {
		OCCUPATION = oCCUPATION;
	}
	public String getNATIVE_PLACE() {
		return NATIVE_PLACE;
	}
	public void setNATIVE_PLACE(String nATIVE_PLACE) {
		NATIVE_PLACE = nATIVE_PLACE;
	}
	public String getMAILING_ADDRESS_CODE() {
		return MAILING_ADDRESS_CODE;
	}
	public void setMAILING_ADDRESS_CODE(String mAILING_ADDRESS_CODE) {
		MAILING_ADDRESS_CODE = mAILING_ADDRESS_CODE;
	}
	public String getMAILING_STREET_CODE() {
		return MAILING_STREET_CODE;
	}
	public void setMAILING_STREET_CODE(String mAILING_STREET_CODE) {
		MAILING_STREET_CODE = mAILING_STREET_CODE;
	}
	public String getALERGY() {
		return ALERGY;
	}
	public void setALERGY(String aLERGY) {
		ALERGY = aLERGY;
	}
	public String getMARITAL_STATUS() {
		return MARITAL_STATUS;
	}
	public void setMARITAL_STATUS(String mARITAL_STATUS) {
		MARITAL_STATUS = mARITAL_STATUS;
	}
	public String getNEXT_OF_SEX() {
		return NEXT_OF_SEX;
	}
	public void setNEXT_OF_SEX(String nEXT_OF_SEX) {
		NEXT_OF_SEX = nEXT_OF_SEX;
	}
	public String getNEXT_OF_BATH() {
		return NEXT_OF_BATH;
	}
	public void setNEXT_OF_BATH(String nEXT_OF_BATH) {
		NEXT_OF_BATH = nEXT_OF_BATH;
	}
	public String getNEXT_OF_ID() {
		return NEXT_OF_ID;
	}
	public void setNEXT_OF_ID(String nEXT_OF_ID) {
		NEXT_OF_ID = nEXT_OF_ID;
	}
	public String getAGE() {
		return AGE;
	}
	public void setAGE(String aGE) {
		AGE = aGE;
	}
	public String getCHARGE_TYPE() {
		return CHARGE_TYPE;
	}
	public void setCHARGE_TYPE(String cHARGE_TYPE) {
		CHARGE_TYPE = cHARGE_TYPE;
	}
	public String getVISIT_DEPT() {
		return VISIT_DEPT;
	}
	public void setVISIT_DEPT(String vISIT_DEPT) {
		VISIT_DEPT = vISIT_DEPT;
	}
	public String getCARD_NAME() {
		return CARD_NAME;
	}
	public void setCARD_NAME(String cARD_NAME) {
		CARD_NAME = cARD_NAME;
	}
	public String getCARD_NO() {
		return CARD_NO;
	}
	public void setCARD_NO(String cARD_NO) {
		CARD_NO = cARD_NO;
	}
	public String getINVOICE_NO() {
		return INVOICE_NO;
	}
	public void setINVOICE_NO(String iNVOICE_NO) {
		INVOICE_NO = iNVOICE_NO;
	}
	public String getCLINIC_NO() {
		return CLINIC_NO;
	}
	public void setCLINIC_NO(String cLINIC_NO) {
		CLINIC_NO = cLINIC_NO;
	}
	public String getCLINIC_DATE_SCHEDULED() {
		return CLINIC_DATE_SCHEDULED;
	}
	public void setCLINIC_DATE_SCHEDULED(String cLINIC_DATE_SCHEDULED) {
		CLINIC_DATE_SCHEDULED = cLINIC_DATE_SCHEDULED;
	}

	
}
