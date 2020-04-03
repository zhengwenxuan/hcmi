package com.hjw.webService.client.dbgj.test;

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.DELFEESendMessage;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;

public class DelFeeSendMessage_test {

	public static void main(String[] args) {
		String url = "http://192.168.111.46:8086/services/Mirth?wsdl";
		DelFeeMessage fm = new DelFeeMessage();		
		
		fm.setREQ_NO("S88888884");
		fm.setPATIENT_ID("T666666");//
		fm.setVISIT_DATE(DateTimeUtil.getDate2());
		fm.setVISIT_NO("2016102583153");
		
		DELFEESendMessage fsm= new DELFEESendMessage(fm);		
		FeeReqBody frb= new FeeReqBody();
		frb = fsm.feeSend(url,"1", true);
		System.out.println(frb.getResultHeader().getTypeCode() + "-" + frb.getResultHeader().getText());
	}

}
