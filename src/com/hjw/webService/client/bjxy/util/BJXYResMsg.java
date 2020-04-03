package com.hjw.webService.client.bjxy.util;

import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.ADT_A05;
import ca.uhn.hl7v2.model.v251.message.ORG_O20;
import ca.uhn.hl7v2.model.v251.message.ORL_O22;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

public class BJXYResMsg {
	public static ResultHeader resFeeMsg(String str){
		ResultHeader rest=new ResultHeader();
		PipeParser pipeParser = new PipeParser();
		Message message;
		try {
			if(!str.equals("")){
				message=pipeParser.parse(str);
				Terser terser = new Terser(message);
				String adt = terser.get("/.MSH-9-1");
				String adt1 = terser.get("/.MSH-9-2");
				StringBuilder sb=new StringBuilder();
				sb.append(adt);
				sb.append("^");
				sb.append(adt1);
				String strs=sb.toString();
				switch (strs) {
				case "ACK^A31":
					rest=AckA31HL7(str);
					break;
				case "ORL^O22":
					rest=ORLO22HL7(str);
					break;
				case "ORG^O20":
					rest=ORGO20HL7(str);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rest;
	}
	public static ResultBody resMsg(String str){
		ResultBody rest=new ResultBody();
		ResultHeader rh=new ResultHeader();
		PipeParser pipeParser = new PipeParser();
		Message message;
		try {
			if(!str.equals("")){
				message=pipeParser.parse(str);
				Terser terser = new Terser(message);
				String adt = terser.get("/.MSH-9-1");
				String adt1 = terser.get("/.MSH-9-2");
				StringBuilder sb=new StringBuilder();
				sb.append(adt);
				sb.append("^");
				sb.append(adt1);
				String strs=sb.toString();
				switch (strs) {
				case "ACK^A28":
					rest=AckA28HL7(str);
					break;
				default:
					rest=AckA28HL7(str);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rest;
	}
	private static ResultHeader AckA31HL7(String str){
		ResultHeader rh=new ResultHeader();
		Parser p = new GenericParser();
		Message msg;
		String res="";
		try {
			msg=p.parse(str);
			ACK ack=(ACK)msg;
			String Msa1_AcknowledgmentCode=ack.getMSA().getMsa1_AcknowledgmentCode().getValue();
			rh.setTypeCode(Msa1_AcknowledgmentCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rh;
	}
	private static ResultHeader ORLO22HL7(String str){
		ResultHeader rh=new ResultHeader();
		Parser p = new GenericParser();
		Message msg;
		String res="";
		try {
			msg=p.parse(str);
			ORL_O22 ack=(ORL_O22)msg;
			String Msa1_AcknowledgmentCode=ack.getMSA().getMsa1_AcknowledgmentCode().getValue();
			rh.setTypeCode(Msa1_AcknowledgmentCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rh;
	}
	private static ResultHeader ORGO20HL7(String str){
		ResultHeader rh=new ResultHeader();
		Parser p = new GenericParser();
		Message msg;
		String res="";
		try {
			msg=p.parse(str);
			ORG_O20 ack=(ORG_O20)msg;
			String Msa1_AcknowledgmentCode=ack.getMSA().getMsa1_AcknowledgmentCode().getValue();
			rh.setTypeCode(Msa1_AcknowledgmentCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rh;
	}
	private static ResultBody AckA28HL7(String str){
		ResultBody rest=new ResultBody();
		ResultHeader rh=new ResultHeader();
		ControlActProcess controlActProcess=new ControlActProcess();
		Parser p = new GenericParser();
		Message msg;
		String res="";
		try {
			msg=p.parse(str);
			ACK ack=(ACK)msg;
			String Msa1_AcknowledgmentCode=ack.getMSA().getMsa1_AcknowledgmentCode().getValue();
			String patient_id="";
			if(Msa1_AcknowledgmentCode.equals("AA")){
				patient_id=ack.getMSA().getMsa3_TextMessage().getValue();
			}
			rh.setTypeCode(Msa1_AcknowledgmentCode);
			List<CustomResBean> list=new ArrayList<CustomResBean>();
			CustomResBean resBean=new CustomResBean();
			resBean.setPATIENT_ID(patient_id);
			resBean.setCLINIC_NO(patient_id);
			resBean.setVISIT_DATE(DateTimeUtil.getDateTime());
			resBean.setVISIT_NO(patient_id);
			list.add(resBean);
			rest.setResultHeader(rh);
			controlActProcess.setLIST(list);
			rest.setControlActProcess(controlActProcess);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rest;
	}

}
