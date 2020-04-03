package com.hjw.webService.client.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "PROJECT")  
@XmlType(propOrder = {})  
public class Fee {
	@XmlElement
	  private String PATIENT_ID;//                 	ID号
	@XmlElement
	  private String EXAM_NUM;// 						体检号
	@XmlElement
	  private String USER_NAME;//                        录入医生编码
	@XmlElement
	  private String VISIT_DATE;//                   	就诊日期
	@XmlElement
	  private String VISIT_NO;//                    	就诊序号
	@XmlElement
	  private String SERIAL_NO;//                    	流水号
	@XmlElement
	  private String ORDER_CLASS;//                  	诊疗项目类别
	@XmlElement
	  private String ORDER_NO;//                      	医嘱号
	@XmlElement
	  private String ORDER_SUB_NO ;//                  	子医嘱号
	@XmlElement
	  private String ITEM_NO ;//                       	顺序号
	@XmlElement
	  private String ITEM_CLASS;//                   	收费项目类别
	@XmlElement
	  private String ITEM_NAME ;//                  	项目名称
	@XmlElement
	  private String ITEM_CODE ;//                   	项目代码
	@XmlElement
	  private String ITEM_SPEC ;//                   	项目规格
	@XmlElement
	  private String UNITS   ;//                     	单位
	@XmlElement
	  private String REPETITION ;//                 	付数
	@XmlElement
	  private String AMOUNT ;//                      	数量
	@XmlElement
	  private String ORDERED_BY_DEPT ;//            	录入科室
	@XmlElement
	  private String ORDERED_BY_DOCTOR ;//           	录入医生
	@XmlElement
	  private String PERFORMED_BY ;//                	执行科室
	@XmlElement
	  private String CLASS_ON_RCPT;//                	收费项目分类
	@XmlElement
	  private String COSTS;//                        	计价金额
	@XmlElement
	  private String CHARGES;//                      	实收费用
	@XmlElement
	  private String RCPT_NO ;//                     	收据号码
	@XmlElement
	  private String CHARGE_INDICATOR ;//            	收费标记
	@XmlElement
	  private String CLASS_ON_RECKONING;//           	核算项目分类
	@XmlElement
	  private String SUBJ_CODE ;//                   	会计科目
	@XmlElement
	  private String PRICE_QUOTIETY ;//              	收费系数
	@XmlElement
	  private String ITEM_PRICE;//                   	单价
	@XmlElement
	  private String CLINIC_NO ;//                   	门诊号
	@XmlElement
	  private String BILL_DATE;//                    	项目收费日期
	@XmlElement
	  private String BILL_NO ;//                     	项目收费编号 
	@XmlElement
	private String SKINTEST;//皮试标志
	@XmlElement
	private String PRESC_PSNO;//皮试结果
	@XmlElement
    private String INSURANCE_FLAG;//适用医保内外标志：0自费，1医保，2免费
	@XmlElement
    private String INSURANCE_CONSTRAINED_LEVEL;//公费用药级别
	@XmlElement
    private String SKIN_SAVE;  //皮试记录时间
	@XmlElement
    private String SKIN_START;//皮试时间
	@XmlElement
    private String SKIN_BATH;// 药品批号	
	@XmlElement
	private String YB_DIAG;//医保诊断	
	@XmlElement
	private String YB_DOCTOR;//医保医师
	@XmlElement
	private String exam_chargeItem_code;//对应体检系统编码
	@XmlElement
	private String codeClass="1";//	收费项目编码类别 1：价表项目，2：诊疗项目
	
	private double discount;	
   
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getExam_chargeItem_code() {
		return exam_chargeItem_code;
	}
	public void setExam_chargeItem_code(String exam_chargeItem_code) {
		this.exam_chargeItem_code = exam_chargeItem_code;
	}
	public String getCodeClass() {
		return codeClass;
	}
	public void setCodeClass(String codeClass) {
		this.codeClass = codeClass;
	}
	public String getUSER_NAME() {
		return USER_NAME;
	}
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}
	public String getPATIENT_ID() {
		return PATIENT_ID;
	}
	public void setPATIENT_ID(String pATIENT_ID) {
		PATIENT_ID = pATIENT_ID;
	}
	public String getEXAM_NUM() {
		return EXAM_NUM;
	}
	public void setEXAM_NUM(String eXAM_NUM) {
		EXAM_NUM = eXAM_NUM;
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
	public String getSERIAL_NO() {
		return SERIAL_NO;
	}
	public void setSERIAL_NO(String sERIAL_NO) {
		SERIAL_NO = sERIAL_NO;
	}
	public String getORDER_CLASS() {
		return ORDER_CLASS;
	}
	public void setORDER_CLASS(String oRDER_CLASS) {
		ORDER_CLASS = oRDER_CLASS;
	}
	public String getORDER_NO() {
		return ORDER_NO;
	}
	public void setORDER_NO(String oRDER_NO) {
		ORDER_NO = oRDER_NO;
	}
	public String getORDER_SUB_NO() {
		return ORDER_SUB_NO;
	}
	public void setORDER_SUB_NO(String oRDER_SUB_NO) {
		ORDER_SUB_NO = oRDER_SUB_NO;
	}
	public String getITEM_NO() {
		return ITEM_NO;
	}
	public void setITEM_NO(String iTEM_NO) {
		ITEM_NO = iTEM_NO;
	}
	public String getITEM_CLASS() {
		return ITEM_CLASS;
	}
	public void setITEM_CLASS(String iTEM_CLASS) {
		ITEM_CLASS = iTEM_CLASS;
	}
	public String getITEM_NAME() {
		return ITEM_NAME;
	}
	public void setITEM_NAME(String iTEM_NAME) {
		ITEM_NAME = iTEM_NAME;
	}
	public String getITEM_CODE() {
		return ITEM_CODE;
	}
	public void setITEM_CODE(String iTEM_CODE) {
		ITEM_CODE = iTEM_CODE;
	}
	public String getITEM_SPEC() {
		return ITEM_SPEC;
	}
	public void setITEM_SPEC(String iTEM_SPEC) {
		ITEM_SPEC = iTEM_SPEC;
	}
	public String getUNITS() {
		return UNITS;
	}
	public void setUNITS(String uNITS) {
		UNITS = uNITS;
	}
	public String getREPETITION() {
		return REPETITION;
	}
	public void setREPETITION(String rEPETITION) {
		REPETITION = rEPETITION;
	}
	public String getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(String aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getORDERED_BY_DEPT() {
		return ORDERED_BY_DEPT;
	}
	public void setORDERED_BY_DEPT(String oRDERED_BY_DEPT) {
		ORDERED_BY_DEPT = oRDERED_BY_DEPT;
	}
	public String getORDERED_BY_DOCTOR() {
		return ORDERED_BY_DOCTOR;
	}
	public void setORDERED_BY_DOCTOR(String oRDERED_BY_DOCTOR) {
		ORDERED_BY_DOCTOR = oRDERED_BY_DOCTOR;
	}
	public String getPERFORMED_BY() {
		return PERFORMED_BY;
	}
	public void setPERFORMED_BY(String pERFORMED_BY) {
		PERFORMED_BY = pERFORMED_BY;
	}
	public String getCLASS_ON_RCPT() {
		return CLASS_ON_RCPT;
	}
	public void setCLASS_ON_RCPT(String cLASS_ON_RCPT) {
		CLASS_ON_RCPT = cLASS_ON_RCPT;
	}
	public String getCOSTS() {
		return COSTS;
	}
	public void setCOSTS(String cOSTS) {
		COSTS = cOSTS;
	}
	public String getCHARGES() {
		return CHARGES;
	}
	public void setCHARGES(String cHARGES) {
		CHARGES = cHARGES;
	}
	public String getRCPT_NO() {
		return RCPT_NO;
	}
	public void setRCPT_NO(String rCPT_NO) {
		RCPT_NO = rCPT_NO;
	}
	public String getCHARGE_INDICATOR() {
		return CHARGE_INDICATOR;
	}
	public void setCHARGE_INDICATOR(String cHARGE_INDICATOR) {
		CHARGE_INDICATOR = cHARGE_INDICATOR;
	}
	public String getCLASS_ON_RECKONING() {
		return CLASS_ON_RECKONING;
	}
	public void setCLASS_ON_RECKONING(String cLASS_ON_RECKONING) {
		CLASS_ON_RECKONING = cLASS_ON_RECKONING;
	}
	public String getSUBJ_CODE() {
		return SUBJ_CODE;
	}
	public void setSUBJ_CODE(String sUBJ_CODE) {
		SUBJ_CODE = sUBJ_CODE;
	}
	public String getPRICE_QUOTIETY() {
		return PRICE_QUOTIETY;
	}
	public void setPRICE_QUOTIETY(String pRICE_QUOTIETY) {
		PRICE_QUOTIETY = pRICE_QUOTIETY;
	}
	public String getITEM_PRICE() {
		return ITEM_PRICE;
	}
	public void setITEM_PRICE(String iTEM_PRICE) {
		ITEM_PRICE = iTEM_PRICE;
	}
	public String getCLINIC_NO() {
		return CLINIC_NO;
	}
	public void setCLINIC_NO(String cLINIC_NO) {
		CLINIC_NO = cLINIC_NO;
	}
	public String getBILL_DATE() {
		return BILL_DATE;
	}
	public void setBILL_DATE(String bILL_DATE) {
		BILL_DATE = bILL_DATE;
	}
	public String getBILL_NO() {
		return BILL_NO;
	}
	public void setBILL_NO(String bILL_NO) {
		BILL_NO = bILL_NO;
	}
	public String getSKINTEST() {
		return SKINTEST;
	}
	public void setSKINTEST(String sKINTEST) {
		SKINTEST = sKINTEST;
	}
	public String getPRESC_PSNO() {
		return PRESC_PSNO;
	}
	public void setPRESC_PSNO(String pRESC_PSNO) {
		PRESC_PSNO = pRESC_PSNO;
	}
	public String getINSURANCE_FLAG() {
		return INSURANCE_FLAG;
	}
	public void setINSURANCE_FLAG(String iNSURANCE_FLAG) {
		INSURANCE_FLAG = iNSURANCE_FLAG;
	}
	public String getINSURANCE_CONSTRAINED_LEVEL() {
		return INSURANCE_CONSTRAINED_LEVEL;
	}
	public void setINSURANCE_CONSTRAINED_LEVEL(String iNSURANCE_CONSTRAINED_LEVEL) {
		INSURANCE_CONSTRAINED_LEVEL = iNSURANCE_CONSTRAINED_LEVEL;
	}
	public String getSKIN_SAVE() {
		return SKIN_SAVE;
	}
	public void setSKIN_SAVE(String sKIN_SAVE) {
		SKIN_SAVE = sKIN_SAVE;
	}
	public String getSKIN_START() {
		return SKIN_START;
	}
	public void setSKIN_START(String sKIN_START) {
		SKIN_START = sKIN_START;
	}
	public String getSKIN_BATH() {
		return SKIN_BATH;
	}
	public void setSKIN_BATH(String sKIN_BATH) {
		SKIN_BATH = sKIN_BATH;
	}
	public String getYB_DIAG() {
		return YB_DIAG;
	}
	public void setYB_DIAG(String yB_DIAG) {
		YB_DIAG = yB_DIAG;
	}
	public String getYB_DOCTOR() {
		return YB_DOCTOR;
	}
	public void setYB_DOCTOR(String yB_DOCTOR) {
		YB_DOCTOR = yB_DOCTOR;
	}
	
	

}
