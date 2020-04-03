package com.hjw.webService.service.Databean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
     * @Title:     
     * @Package com.hjw.webService.service.Databean   
     * @Description:   人员   
     * @author: yangm     
     * @date:   2016年10月31日 上午11:14:38   
     * @version V3.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "List")  
@XmlType(propOrder = {})  
public class User {
	private String ACTION_TYPE="";//1新增2更新0删除
	private String ID="";// ID
	private String STAFF_CODE="";// 	N	VARCHAR2(32)	操作人员登陆编码
	private String HSP_CONFIG_BASEINFO_ID="";// 	N	VARCHAR2(32)	卫生机构ID
	private String NAME="";// 	N	VARCHAR2(100)	姓名
	private String NAME_EN="";// 	N	VARCHAR2(40)	英文姓名
	private String COMM_CONFIG_SEX_ID="";// 	N	VARCHAR2(32)	性别
	private String DATE_OF_BIRTH="";// 	N	DATE	出生日期
	private String COMM_CONFIG_STAFFTYPE_ID="";// 	N	VARCHAR2(32)	操作员类别
	private String ID_NO="";// 	N	VARCHAR2(40)	身份证件号码
	private String PHONE="";// 	N	VARCHAR2(40)	联系电话
	private String ISLOCATION="";// 	N	NUMBER(1)	在位标志
	private String COMMENTS="";// 	N	VARCHAR2(100)	备注
	private String INPUT_CODE="";// 	N	VARCHAR2(8)	输入码
	private String CREATE_DATE="";// 	N	DATE	记录日期
	private String CREATE_USER_ID="";// 	N	VARCHAR2(32)	记录人员ID
	private String CREATE_USER_NAME="";// 	N	VARCHAR2(40)	记录人员名称
	private String SEQ_NO="";// 	N	NUMBER(11)	序号
	private String HOMEPAGE_TYPE="";// 	N	VARCHAR2(2)	主界面类型
	private String E_MAIL="";// 	N	VARCHAR2(100)	E_MAIL
	private String STAFF_TYPE="";// 	N	NUMBER(1)	操作员类别
	private String NAME_SPELL="";// 	N	VARCHAR2(40)	用户姓名全拼
	private String PINYIN_INPUT_CODE="";// 	N	VARCHAR2(40)	姓名全拼
	private String HSP_STAFF_BASEINFO_ID="";// 	N	VARCHAR2(32)	卫生人员ID
	private String TENANT_ID="";// 	N	VARCHAR2(32)	租户ID
	private String WORKSTATION_DESC="";// 	N	VARCHAR2(100)	
	private String HSP_DEPT_BASEINFO_ID="";// 	N	VARCHAR2(32)	
	private String COMM_CONFIG_DEGREE_ID="";// 	N	VARCHAR2(32)	
	private String EMP_POSITION="";// 	N	VARCHAR2(100)	
	private String COMM_CONFIG_EMPTITLE_ID="";// 	N	VARCHAR2(100)	
	private String COMM_CONFIG_PROFESSION_ID="";// 	N	VARCHAR2(32)	
	private String INPUT_CODE_PY="";// 	N	VARCHAR2(10)	拼音码
	private String INPUT_CODE_WB="";// 	N	VARCHAR2(20)	五笔码
	private String START_DATE="";// 	N	DATE	生效日期
	private String STATUS="";// 	N	VARCHAR2(1)	启用标志：0，启用；1，停用（默认为0）
	private String PASSWD="";//	
	
	public String getACTION_TYPE() {
		return ACTION_TYPE;
	}
	public void setACTION_TYPE(String aCTION_TYPE) {
		ACTION_TYPE = aCTION_TYPE;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSTAFF_CODE() {
		return STAFF_CODE;
	}
	public void setSTAFF_CODE(String sTAFF_CODE) {
		STAFF_CODE = sTAFF_CODE;
	}
	public String getHSP_CONFIG_BASEINFO_ID() {
		return HSP_CONFIG_BASEINFO_ID;
	}
	public void setHSP_CONFIG_BASEINFO_ID(String hSP_CONFIG_BASEINFO_ID) {
		HSP_CONFIG_BASEINFO_ID = hSP_CONFIG_BASEINFO_ID;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getNAME_EN() {
		return NAME_EN;
	}
	public void setNAME_EN(String nAME_EN) {
		NAME_EN = nAME_EN;
	}
	public String getCOMM_CONFIG_SEX_ID() {
		return COMM_CONFIG_SEX_ID;
	}
	public void setCOMM_CONFIG_SEX_ID(String cOMM_CONFIG_SEX_ID) {
		COMM_CONFIG_SEX_ID = cOMM_CONFIG_SEX_ID;
	}
	public String getDATE_OF_BIRTH() {
		return DATE_OF_BIRTH;
	}
	public void setDATE_OF_BIRTH(String dATE_OF_BIRTH) {
		DATE_OF_BIRTH = dATE_OF_BIRTH;
	}
	public String getCOMM_CONFIG_STAFFTYPE_ID() {
		return COMM_CONFIG_STAFFTYPE_ID;
	}
	public void setCOMM_CONFIG_STAFFTYPE_ID(String cOMM_CONFIG_STAFFTYPE_ID) {
		COMM_CONFIG_STAFFTYPE_ID = cOMM_CONFIG_STAFFTYPE_ID;
	}
	public String getID_NO() {
		return ID_NO;
	}
	public void setID_NO(String iD_NO) {
		ID_NO = iD_NO;
	}
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	public String getISLOCATION() {
		return ISLOCATION;
	}
	public void setISLOCATION(String iSLOCATION) {
		ISLOCATION = iSLOCATION;
	}
	public String getCOMMENTS() {
		return COMMENTS;
	}
	public void setCOMMENTS(String cOMMENTS) {
		COMMENTS = cOMMENTS;
	}
	public String getINPUT_CODE() {
		return INPUT_CODE;
	}
	public void setINPUT_CODE(String iNPUT_CODE) {
		INPUT_CODE = iNPUT_CODE;
	}
	public String getCREATE_DATE() {
		return CREATE_DATE;
	}
	public void setCREATE_DATE(String cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}
	public String getCREATE_USER_ID() {
		return CREATE_USER_ID;
	}
	public void setCREATE_USER_ID(String cREATE_USER_ID) {
		CREATE_USER_ID = cREATE_USER_ID;
	}
	public String getCREATE_USER_NAME() {
		return CREATE_USER_NAME;
	}
	public void setCREATE_USER_NAME(String cREATE_USER_NAME) {
		CREATE_USER_NAME = cREATE_USER_NAME;
	}
	public String getSEQ_NO() {
		return SEQ_NO;
	}
	public void setSEQ_NO(String sEQ_NO) {
		SEQ_NO = sEQ_NO;
	}
	public String getHOMEPAGE_TYPE() {
		return HOMEPAGE_TYPE;
	}
	public void setHOMEPAGE_TYPE(String hOMEPAGE_TYPE) {
		HOMEPAGE_TYPE = hOMEPAGE_TYPE;
	}
	public String getE_MAIL() {
		return E_MAIL;
	}
	public void setE_MAIL(String e_MAIL) {
		E_MAIL = e_MAIL;
	}
	public String getSTAFF_TYPE() {
		return STAFF_TYPE;
	}
	public void setSTAFF_TYPE(String sTAFF_TYPE) {
		STAFF_TYPE = sTAFF_TYPE;
	}
	public String getNAME_SPELL() {
		return NAME_SPELL;
	}
	public void setNAME_SPELL(String nAME_SPELL) {
		NAME_SPELL = nAME_SPELL;
	}
	public String getPINYIN_INPUT_CODE() {
		return PINYIN_INPUT_CODE;
	}
	public void setPINYIN_INPUT_CODE(String pINYIN_INPUT_CODE) {
		PINYIN_INPUT_CODE = pINYIN_INPUT_CODE;
	}
	public String getHSP_STAFF_BASEINFO_ID() {
		return HSP_STAFF_BASEINFO_ID;
	}
	public void setHSP_STAFF_BASEINFO_ID(String hSP_STAFF_BASEINFO_ID) {
		HSP_STAFF_BASEINFO_ID = hSP_STAFF_BASEINFO_ID;
	}
	public String getTENANT_ID() {
		return TENANT_ID;
	}
	public void setTENANT_ID(String tENANT_ID) {
		TENANT_ID = tENANT_ID;
	}
	public String getWORKSTATION_DESC() {
		return WORKSTATION_DESC;
	}
	public void setWORKSTATION_DESC(String wORKSTATION_DESC) {
		WORKSTATION_DESC = wORKSTATION_DESC;
	}
	public String getHSP_DEPT_BASEINFO_ID() {
		return HSP_DEPT_BASEINFO_ID;
	}
	public void setHSP_DEPT_BASEINFO_ID(String hSP_DEPT_BASEINFO_ID) {
		HSP_DEPT_BASEINFO_ID = hSP_DEPT_BASEINFO_ID;
	}
	public String getCOMM_CONFIG_DEGREE_ID() {
		return COMM_CONFIG_DEGREE_ID;
	}
	public void setCOMM_CONFIG_DEGREE_ID(String cOMM_CONFIG_DEGREE_ID) {
		COMM_CONFIG_DEGREE_ID = cOMM_CONFIG_DEGREE_ID;
	}
	public String getEMP_POSITION() {
		return EMP_POSITION;
	}
	public void setEMP_POSITION(String eMP_POSITION) {
		EMP_POSITION = eMP_POSITION;
	}
	public String getCOMM_CONFIG_EMPTITLE_ID() {
		return COMM_CONFIG_EMPTITLE_ID;
	}
	public void setCOMM_CONFIG_EMPTITLE_ID(String cOMM_CONFIG_EMPTITLE_ID) {
		COMM_CONFIG_EMPTITLE_ID = cOMM_CONFIG_EMPTITLE_ID;
	}
	public String getCOMM_CONFIG_PROFESSION_ID() {
		return COMM_CONFIG_PROFESSION_ID;
	}
	public void setCOMM_CONFIG_PROFESSION_ID(String cOMM_CONFIG_PROFESSION_ID) {
		COMM_CONFIG_PROFESSION_ID = cOMM_CONFIG_PROFESSION_ID;
	}
	public String getINPUT_CODE_PY() {
		return INPUT_CODE_PY;
	}
	public void setINPUT_CODE_PY(String iNPUT_CODE_PY) {
		INPUT_CODE_PY = iNPUT_CODE_PY;
	}
	public String getINPUT_CODE_WB() {
		return INPUT_CODE_WB;
	}
	public void setINPUT_CODE_WB(String iNPUT_CODE_WB) {
		INPUT_CODE_WB = iNPUT_CODE_WB;
	}
	public String getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(String sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getPASSWD() {
		return PASSWD;
	}
	public void setPASSWD(String pASSWD) {
		PASSWD = pASSWD;
	}
}
