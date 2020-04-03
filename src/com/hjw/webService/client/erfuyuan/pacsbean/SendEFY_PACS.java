package com.hjw.webService.client.erfuyuan.pacsbean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Send")  
@XmlType(propOrder = {})
public class SendEFY_PACS {
	@XmlElement
    private String IHISORDER_IID = "";//HIS系统主键
	@XmlElement
    private String CORDER_INDEX = "";//申请单号
	@XmlElement
    private String CPATWL_KEY = "";//病人主索引
	@XmlElement
    private String CTRIGGER_DTTM = DateTimeUtil.getDateTimes();//开单时间 格式为yyyyMMddHH24miss
	@XmlElement
    private String CREPLICA_DTTM = "ANY";//固定值ANY
	@XmlElement
    private String CPATIENT_ID = "";//病人号
	@XmlElement
    private String COUTPATIENT_ID = "";//门诊号
	@XmlElement
    private String CINPATIENT_ID = "";//住院号
	@XmlElement
    private String CHPATIENT_ID = "";//医保编号
	@XmlElement
    private String CNAME = "";//姓名
	@XmlElement
    private String CSEX = "";//性别
	@XmlElement
    private String CAGE = "";//年龄
	@XmlElement
    private String CAGEDW = "岁";//年龄单位
	@XmlElement
    private String DDATE_OF_BIRTH = "";//出生日期
	@XmlElement
    private String CNATION = "";//民族
	@XmlElement
    private String CID_NO = "";//身份证号
	@XmlElement
    private String CMAILING_ADDRESS = "";//通讯地址
	@XmlElement
    private String CPHONE_NUMBER_HOME = "";//联系电话
	@XmlElement
    private String CFSOURCE = "体检";//病人类型
//	@XmlElement
//    private String CKDYQ_ID = "";//开单医生工号
//	@XmlElement
//    private String CKDYQ_NAME = "";//开单医生姓名
	@XmlElement
	private String creq_physician;//开单医生姓名
	@XmlElement
    private String CWARD_NAME = "";//病区名称
	@XmlElement
    private String CDEPT_CODE = "";//开单科室编码
	@XmlElement
    private String CREQ_DEPT = "";//开单科室名称
	@XmlElement
    private String CBED_NO = "";//床号
	@XmlElement
    private String CCLIN_SYNP = "";//临床描述
	@XmlElement
    private String CCLIN_DIAG = "";//临床诊断
	@XmlElement
    private String CPHYS_SIGN = "";//检查描述
	@XmlElement
    private String CRELEVANT_DIAG = "";//检查备注
	@XmlElement
    private String CEXAM_CLASS = "";//检查类型
	@XmlElement
    private String CEXAM_GROUP_NO = "45537898";//检查分组编码
	@XmlElement
    private String CEXAM_GROUP = "体检彩超";//检查分组名称
	@XmlElement
    private String CEXAM_ITEM_NO = "";//检查项目编码 逗号分隔
	@XmlElement
    private String CEXAM_ITEM = "";//检查项目名称 逗号分隔
	@XmlElement
    private String CEXAM_ROOM_ID = "45537984";//检查房间ID
	@XmlElement
    private String CEXAM_ROOM = "体检彩超室";//检查房间
	@XmlElement
    private String CEXAM_SCHE_DATE = "";//检查预约时间
	@XmlElement
    private String CEXAM_SCHE_NUM = "";//检查排队号
	@XmlElement
    private String CCOSTS = "";//检查费用
	@XmlElement
    private String CCOSTS_FLAG = "";//收费类型
	
	public String getIHISORDER_IID() {
		return IHISORDER_IID;
	}
	public String getCORDER_INDEX() {
		return CORDER_INDEX;
	}
	public String getCPATWL_KEY() {
		return CPATWL_KEY;
	}
	public String getCTRIGGER_DTTM() {
		return CTRIGGER_DTTM;
	}
	public String getCREPLICA_DTTM() {
		return CREPLICA_DTTM;
	}
	public String getCPATIENT_ID() {
		return CPATIENT_ID;
	}
	public String getCOUTPATIENT_ID() {
		return COUTPATIENT_ID;
	}
	public String getCINPATIENT_ID() {
		return CINPATIENT_ID;
	}
	public String getCHPATIENT_ID() {
		return CHPATIENT_ID;
	}
	public String getCNAME() {
		return CNAME;
	}
	public String getCSEX() {
		return CSEX;
	}
	public String getCAGE() {
		return CAGE;
	}
	public String getCAGEDW() {
		return CAGEDW;
	}
	public String getDDATE_OF_BIRTH() {
		return DDATE_OF_BIRTH;
	}
	public String getCNATION() {
		return CNATION;
	}
	public String getCID_NO() {
		return CID_NO;
	}
	public String getCMAILING_ADDRESS() {
		return CMAILING_ADDRESS;
	}
	public String getCPHONE_NUMBER_HOME() {
		return CPHONE_NUMBER_HOME;
	}
	public String getCFSOURCE() {
		return CFSOURCE;
	}
//	public String getCKDYQ_ID() {
//		return CKDYQ_ID;
//	}
//	public String getCKDYQ_NAME() {
//		return CKDYQ_NAME;
//	}
	public String getCWARD_NAME() {
		return CWARD_NAME;
	}
	public String getCDEPT_CODE() {
		return CDEPT_CODE;
	}
	public String getCREQ_DEPT() {
		return CREQ_DEPT;
	}
	public String getCBED_NO() {
		return CBED_NO;
	}
	public String getCCLIN_SYNP() {
		return CCLIN_SYNP;
	}
	public String getCCLIN_DIAG() {
		return CCLIN_DIAG;
	}
	public String getCPHYS_SIGN() {
		return CPHYS_SIGN;
	}
	public String getCRELEVANT_DIAG() {
		return CRELEVANT_DIAG;
	}
	public String getCEXAM_CLASS() {
		return CEXAM_CLASS;
	}
	public String getCEXAM_GROUP_NO() {
		return CEXAM_GROUP_NO;
	}
	public String getCEXAM_GROUP() {
		return CEXAM_GROUP;
	}
	public String getCEXAM_ITEM_NO() {
		return CEXAM_ITEM_NO;
	}
	public String getCEXAM_ITEM() {
		return CEXAM_ITEM;
	}
	public String getCEXAM_ROOM_ID() {
		return CEXAM_ROOM_ID;
	}
	public String getCEXAM_ROOM() {
		return CEXAM_ROOM;
	}
	public String getCEXAM_SCHE_DATE() {
		return CEXAM_SCHE_DATE;
	}
	public String getCEXAM_SCHE_NUM() {
		return CEXAM_SCHE_NUM;
	}
	public String getCCOSTS() {
		return CCOSTS;
	}
	public String getCCOSTS_FLAG() {
		return CCOSTS_FLAG;
	}
	public void setIHISORDER_IID(String iHISORDER_IID) {
		IHISORDER_IID = iHISORDER_IID;
	}
	public void setCORDER_INDEX(String cORDER_INDEX) {
		CORDER_INDEX = cORDER_INDEX;
	}
	public void setCPATWL_KEY(String cPATWL_KEY) {
		CPATWL_KEY = cPATWL_KEY;
	}
	public void setCTRIGGER_DTTM(String cTRIGGER_DTTM) {
		CTRIGGER_DTTM = cTRIGGER_DTTM;
	}
	public void setCREPLICA_DTTM(String cREPLICA_DTTM) {
		CREPLICA_DTTM = cREPLICA_DTTM;
	}
	public void setCPATIENT_ID(String cPATIENT_ID) {
		CPATIENT_ID = cPATIENT_ID;
	}
	public void setCOUTPATIENT_ID(String cOUTPATIENT_ID) {
		COUTPATIENT_ID = cOUTPATIENT_ID;
	}
	public void setCINPATIENT_ID(String cINPATIENT_ID) {
		CINPATIENT_ID = cINPATIENT_ID;
	}
	public void setCHPATIENT_ID(String cHPATIENT_ID) {
		CHPATIENT_ID = cHPATIENT_ID;
	}
	public void setCNAME(String cNAME) {
		CNAME = cNAME;
	}
	public void setCSEX(String cSEX) {
		CSEX = cSEX;
	}
	public void setCAGE(String cAGE) {
		CAGE = cAGE;
	}
	public void setCAGEDW(String cAGEDW) {
		CAGEDW = cAGEDW;
	}
	public void setDDATE_OF_BIRTH(String dDATE_OF_BIRTH) {
		DDATE_OF_BIRTH = dDATE_OF_BIRTH;
	}
	public void setCNATION(String cNATION) {
		CNATION = cNATION;
	}
	public void setCID_NO(String cID_NO) {
		CID_NO = cID_NO;
	}
	public void setCMAILING_ADDRESS(String cMAILING_ADDRESS) {
		CMAILING_ADDRESS = cMAILING_ADDRESS;
	}
	public void setCPHONE_NUMBER_HOME(String cPHONE_NUMBER_HOME) {
		CPHONE_NUMBER_HOME = cPHONE_NUMBER_HOME;
	}
	public void setCFSOURCE(String cFSOURCE) {
		CFSOURCE = cFSOURCE;
	}
//	public void setCKDYQ_ID(String cKDYQ_ID) {
//		CKDYQ_ID = cKDYQ_ID;
//	}
//	public void setCKDYQ_NAME(String cKDYQ_NAME) {
//		CKDYQ_NAME = cKDYQ_NAME;
//	}
	public void setCWARD_NAME(String cWARD_NAME) {
		CWARD_NAME = cWARD_NAME;
	}
	public void setCDEPT_CODE(String cDEPT_CODE) {
		CDEPT_CODE = cDEPT_CODE;
	}
	public void setCREQ_DEPT(String cREQ_DEPT) {
		CREQ_DEPT = cREQ_DEPT;
	}
	public void setCBED_NO(String cBED_NO) {
		CBED_NO = cBED_NO;
	}
	public void setCCLIN_SYNP(String cCLIN_SYNP) {
		CCLIN_SYNP = cCLIN_SYNP;
	}
	public void setCCLIN_DIAG(String cCLIN_DIAG) {
		CCLIN_DIAG = cCLIN_DIAG;
	}
	public void setCPHYS_SIGN(String cPHYS_SIGN) {
		CPHYS_SIGN = cPHYS_SIGN;
	}
	public void setCRELEVANT_DIAG(String cRELEVANT_DIAG) {
		CRELEVANT_DIAG = cRELEVANT_DIAG;
	}
	public void setCEXAM_CLASS(String cEXAM_CLASS) {
		CEXAM_CLASS = cEXAM_CLASS;
	}
	public void setCEXAM_GROUP_NO(String cEXAM_GROUP_NO) {
		CEXAM_GROUP_NO = cEXAM_GROUP_NO;
	}
	public void setCEXAM_GROUP(String cEXAM_GROUP) {
		CEXAM_GROUP = cEXAM_GROUP;
	}
	public void setCEXAM_ITEM_NO(String cEXAM_ITEM_NO) {
		CEXAM_ITEM_NO = cEXAM_ITEM_NO;
	}
	public void setCEXAM_ITEM(String cEXAM_ITEM) {
		CEXAM_ITEM = cEXAM_ITEM;
	}
	public void setCEXAM_ROOM_ID(String cEXAM_ROOM_ID) {
		CEXAM_ROOM_ID = cEXAM_ROOM_ID;
	}
	public void setCEXAM_ROOM(String cEXAM_ROOM) {
		CEXAM_ROOM = cEXAM_ROOM;
	}
	public void setCEXAM_SCHE_DATE(String cEXAM_SCHE_DATE) {
		CEXAM_SCHE_DATE = cEXAM_SCHE_DATE;
	}
	public void setCEXAM_SCHE_NUM(String cEXAM_SCHE_NUM) {
		CEXAM_SCHE_NUM = cEXAM_SCHE_NUM;
	}
	public void setCCOSTS(String cCOSTS) {
		CCOSTS = cCOSTS;
	}
	public void setCCOSTS_FLAG(String cCOSTS_FLAG) {
		CCOSTS_FLAG = cCOSTS_FLAG;
	}
	public String getCreq_physician() {
		return creq_physician;
	}
	public void setCreq_physician(String creq_physician) {
		this.creq_physician = creq_physician;
	}
	public static void main(String[] args) {
		System.out.println(JaxbUtil.convertToXml(new SendEFY_PACS(), true));
	}
}
