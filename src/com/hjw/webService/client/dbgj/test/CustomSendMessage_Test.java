package com.hjw.webService.client.dbgj.test;

import com.hjw.webService.client.CUSTOMSendMessage;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;

public class CustomSendMessage_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://192.168.111.46:8086/services/Mirth?wsdl";		
		Custom st = new Custom();
		st.setPATIENT_ID("T666666");
		st.setEXAM_NUM("T666666");
		st.setNAME("魏朝6");
		st.setNAME_PHONETIC("");
		st.setSEX("男");// 男  女
		st.setDATE_OF_BIRTH("1990-10-01");
		st.setBIRTH_PLACE("");
		st.setNATION("汉族");
		st.setCITIZENSHIP("");
		st.setID_NO("612322199001011220");
		st.setIDENTITY("一般人员");
		st.setUNIT_IN_CONTRACT("");
		st.setMAILING_ADDRESS("");
		st.setZIP_CODE("");
		st.setPHONE_NUMBER_BUSINESS("");
		st.setPHONE_NUMBER_HOME("");
		st.setNEXT_OF_KIN("");
		st.setRELATIONSHIP("");		
		st.setNEXT_OF_KIN_ADDR("");
		st.setNEXT_OF_KIN_PHONE("");
		st.setNEXT_OF_KIN_ZIP_CODE("");
		st.setOPERATOR("127");
		st.setBUSINESS_ZIP_CODE("");
		st.setPHOTO("");
		st.setPATIENT_CLASS("");
		st.setDEGREE("");
		st.setE_NAME("");
		st.setOCCUPATION("技术人员");
		st.setNATIVE_PLACE("");
		st.setMAILING_ADDRESS_CODE("");
		st.setMAILING_STREET_CODE("");
		st.setALERGY("");
		st.setMARITAL_STATUS("已婚");
		st.setNEXT_OF_SEX("");
		st.setVISIT_DEPT("010159");
		st.setOPERATORS("");
		st.setCARD_NAME("");
		st.setCARD_NO("1");//1-非院内卡挂号，院内卡挂号就传卡号
		st.setINVOICE_NO("");
		st.setCLINIC_NO("");
		st.setCLINIC_DATE_SCHEDULED("");
		st.setCHARGE_TYPE("自费");		
		st.setAGE("26");
		st.setNEXT_OF_BATH("");
		
		CUSTOMSendMessage csm= new CUSTOMSendMessage(st);
		
		ResultBody fr = new ResultBody();
		fr = csm.customSend(url,"1",true);
		System.out.println(fr.getResultHeader().getTypeCode()+"-"+fr.getResultHeader().getText());
	}

}
