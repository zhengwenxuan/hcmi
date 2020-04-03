package com.hjw.webService.client.bjxy.util;

import com.hjw.webService.client.body.ResultHeader;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ORL_O22;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class Test {

	public static void main(String[] args) {
		String res="MSA|AA|2018060608532187818";
			 String	 msh="MSH|^~\\&|ESB|10|PES|10|20180606085743785+0800||ORL^O22|104369975|P|2.5.1||||||";
		    	String	 mshg="MSA|AA|2018060608532187818";
	    	if(msh.contains("MSH")){
  			 String[] strinfos=msh.split("\\|");
  			 if(!strinfos[6].equals("")){
  				strinfos[6]=strinfos[6].substring(0, 14);
  			 }
  			 StringBuilder sbstr=new StringBuilder();
  			 for(int i=0;i<strinfos.length;i++){
  				 sbstr.append(strinfos[i]);
  				 if((i+1)!=strinfos.length){
  					 sbstr.append("|");
  				 }
  			 }
  			msh=sbstr.toString();
  		 }
	     String  sendString1=msh+mshg;
		ResultHeader rh=new ResultHeader();
		Parser p = new GenericParser();
		Message msg;
		try {
			msg=p.parse(sendString1);
			ORL_O22 ack=(ORL_O22)msg;
			String Msa1_AcknowledgmentCode=ack.getMSA().getMsa1_AcknowledgmentCode().getValue();
			rh.setTypeCode(Msa1_AcknowledgmentCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
