package com.hjw.webService.client.dbgj.test;

import com.hjw.webService.client.YBCustomSendMessage;
import com.hjw.webService.client.body.YbCustomMessage;
import com.hjw.webService.client.body.YbCustomResultBody;

public class YbCustomSendMessage_test {

	public static void main(String[] args) {
		String url = "http://192.168.111.46:8086/services/Mirth?wsdl";
		YbCustomMessage fm = new YbCustomMessage();		
		
		fm.setMC_NO("221237282160");		
		fm.setCARD_CLASS("0");//医保卡和就诊卡类别，医保卡为1，就诊卡为0		
		fm.setVISIT_DATE("2016-11-05");
		fm.setVISIT_DEPT("01010401");
		
		YBCustomSendMessage fsm= new YBCustomSendMessage(fm);
		YbCustomResultBody fr = fsm.customSend(url,"1", true);
		
		System.out.println(fr.getResultHeader().getTypeCode() + "-" + fr.getResultHeader().getText());
	}
}
