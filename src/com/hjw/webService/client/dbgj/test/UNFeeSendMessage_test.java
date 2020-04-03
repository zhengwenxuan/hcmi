package com.hjw.webService.client.dbgj.test;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.UNFEESendMessage;
import com.hjw.webService.client.Bean.ReqUnNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;
import com.hjw.webService.service.bean.RetReqNo;

public class UNFeeSendMessage_test {

	public static void main(String[] args) {
		String url = "http://192.168.111.46:8086/services/Mirth?wsdl";
		UnFeeMessage fm = new UnFeeMessage();
		
		List<String> REQ_NOS= new ArrayList<String>();//申请单号
		REQ_NOS.add("S88888884");
		REQ_NOS.add("S88888885");
		REQ_NOS.add("S88888886");
		
		ReqUnNo rrn=new ReqUnNo();
		rrn.setREQ_NO(REQ_NOS);
		
		fm.setREQ_NOS(rrn);//申请单号
		fm.setRCPT_NO("20161025776066");//收据号
		fm.setEXAM_NUM("T666666");//体检号
		
		UNFEESendMessage fsm= new UNFEESendMessage(fm);		
		FeeReqBody frb= new FeeReqBody();
		frb = fsm.feeSend(url,"1", true);
		System.out.println(frb.getResultHeader().getTypeCode() + "-" + frb.getResultHeader().getText());
	}
}
