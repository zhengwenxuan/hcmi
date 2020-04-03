package com.hjw.webService.xyyyService.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.group.OML_O21_ORDER;
import ca.uhn.hl7v2.model.v251.group.ORL_O22_ORDER;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.MFK_M01;
import ca.uhn.hl7v2.model.v251.message.OML_O21;
import ca.uhn.hl7v2.model.v251.message.ORG_O20;
import ca.uhn.hl7v2.model.v251.message.ORL_O22;
import ca.uhn.hl7v2.model.v251.segment.MFI;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.model.v251.segment.PV1;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class XYYYMsgUtil {
	public static String ACKDICT(String messagecode,String typecode,String msgId,String ackCode,String strs){
		Parser p = new GenericParser();
		Message msg;
		String nowtime=DateTimeUtil.getDateTimes();
		String str="";
		StringBuilder sb=new StringBuilder();
		try {
			MFK_M01 ack=new MFK_M01();
			
			MSH msh=ack.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PES");
			msh.getSendingFacility().getNamespaceID().setValue("10");
			msh.getReceivingApplication().getNamespaceID().setValue("ESB");
			msh.getReceivingFacility().getNamespaceID().setValue("10");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMessageCode().setValue(messagecode);
			msh.getMessageType().getTriggerEvent().setValue(typecode);
			msh.getMessageControlID().setValue(msgId);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.5.1");
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("UNICODE UTF-8");
			
			MSA msa=ack.getMSA();
			msa.getAcknowledgmentCode().setValue(ackCode);
			msa.getMessageControlID().setValue(msgId);
			
			MFI mfi=ack.getMFI();
			mfi.getMasterFileIdentifier().getCe1_Identifier().setValue(strs);
			mfi.getFileLevelEventCode().setValue("UPD");
			mfi.getResponseLevelCode().setValue("AL");
			
			Parser parser=new PipeParser();
			str=parser.encode(ack);
			System.out.println(str);
			sb.append(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static String ACKLIS(String messagecode,String typecode,String strs){
		Parser p = new GenericParser();
		Message msg;
		String nowtime=DateTimeUtil.getDateTimes();
		String str="";
		StringBuilder sb=new StringBuilder();
		try {
			msg=p.parse(strs);
			OML_O21 oml=(OML_O21) msg;
			ORL_O22 orl=new ORL_O22();
			
			MSH msh=orl.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PES");
			msh.getSendingFacility().getNamespaceID().setValue("10");
			msh.getReceivingApplication().getNamespaceID().setValue("ESB");
			msh.getReceivingFacility().getNamespaceID().setValue("10");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMessageCode().setValue(messagecode);
			msh.getMessageType().getTriggerEvent().setValue(typecode);
			msh.getMessageControlID().setValue(oml.getMSH().getMessageControlID().getValue());
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.5.1");
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("UNICODE UTF-8");
			
			
			MSA msa=orl.getMSA();
			msa.getAcknowledgmentCode().setValue("CA");
			msa.getMessageControlID().setValue(oml.getMSH().getMessageControlID().getValue());
			
			PID omgPid=oml.getPATIENT().getPID();
			PID orgPid=orl.getRESPONSE().getPATIENT().getPID();
			orgPid.getPid1_SetIDPID().setValue(omgPid.getPid1_SetIDPID().getValue());
			orgPid.getPid2_PatientID().getCx1_IDNumber().setValue(omgPid.getPid2_PatientID().getCx1_IDNumber().getValue());
			orgPid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(omgPid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().getValue());
			orgPid.getPid4_AlternatePatientIDPID(0).getCx1_IDNumber().setValue(omgPid.getPid4_AlternatePatientIDPID(0).getCx1_IDNumber().getValue());
			orgPid.getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().setValue(omgPid.getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().getValue());
			orgPid.getPid7_DateTimeOfBirth().getTime().setValue(omgPid.getPid7_DateTimeOfBirth().getTime().getValue());
			orgPid.getPid8_AdministrativeSex().setValue(omgPid.getPid8_AdministrativeSex().getValue());
			orgPid.getPid11_PatientAddress(0).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(omgPid.getPid11_PatientAddress(0).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().getValue());
			orgPid.getPid11_PatientAddress(1).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(omgPid.getPid11_PatientAddress(1).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().getValue());
			orgPid.getPid13_PhoneNumberHome(0).getXtn9_AnyText().setValue(omgPid.getPid13_PhoneNumberHome(0).getXtn9_AnyText().getValue());
			orgPid.getPid14_PhoneNumberBusiness(0).getXtn9_AnyText().setValue(omgPid.getPid14_PhoneNumberBusiness(0).getXtn9_AnyText().getValue());
			orgPid.getPid16_MaritalStatus().getCe1_Identifier().setValue(omgPid.getPid16_MaritalStatus().getCe1_Identifier().getValue());
			orgPid.getPid16_MaritalStatus().getCe2_Text().setValue(omgPid.getPid16_MaritalStatus().getCe2_Text().getValue());
			orgPid.getPid17_Religion().getCe1_Identifier().setValue("12");
			orgPid.getPid22_EthnicGroup(0).getCe1_Identifier().setValue(omgPid.getPid22_EthnicGroup(0).getCe1_Identifier().getValue());
			orgPid.getPid22_EthnicGroup(0).getCe2_Text().setValue(omgPid.getPid22_EthnicGroup(0).getCe2_Text().getValue());
			PV1 pv1=oml.getPATIENT().getPATIENT_VISIT().getPV1();
			List<OML_O21_ORDER> omglist=oml.getORDERAll();
			for(int i=0;i<omglist.size();i++){
				ORL_O22_ORDER order=orl.getRESPONSE().getPATIENT().getORDER();
				ORC omgOrc=omglist.get(i).getORC();
				ORC orgOrc=order.getORC();
				orgOrc.getOrc1_OrderControl().setValue(omgOrc.getOrc1_OrderControl().getValue());
				orgOrc.getOrc2_PlacerOrderNumber().getEi3_UniversalID().setValue(omgOrc.getOrc2_PlacerOrderNumber().getEi3_UniversalID().getValue());
				orgOrc.getOrc2_PlacerOrderNumber().getEi4_UniversalIDType().setValue(omgOrc.getOrc2_PlacerOrderNumber().getEi4_UniversalIDType().getValue());
				orgOrc.getOrc3_FillerOrderNumber().getEi3_UniversalID().setValue(omgOrc.getOrc3_FillerOrderNumber().getEi3_UniversalID().getValue());
				orgOrc.getOrc3_FillerOrderNumber().getEi4_UniversalIDType().setValue(omgOrc.getOrc3_FillerOrderNumber().getEi4_UniversalIDType().getValue());
				orgOrc.getOrc13_EntererSLocation().getPl1_PointOfCare().setValue("123123");
				OBR obr2=order.getOBSERVATION_REQUEST().getOBR();
				obr2.getObr10_CollectorIdentifier(0).getIDNumber().setValue("123123123");
				obr2.getObr10_CollectorIdentifier(0).getFamilyName().getSurname().setValue("孙蓓");
				obr2.getObr10_CollectorIdentifier(0).getAssigningAgencyOrDepartment().getIdentifier().setValue("dept");
				obr2.getObr10_CollectorIdentifier(0).getAssigningAgencyOrDepartment().getText().setValue("体检科");
				obr2.getObr34_Technician(0).getNdl4_PointOfCare().setValue("123456");
			}
			
			Parser parser=new PipeParser();
			str=parser.encode(orl);
			System.out.println(str);
			sb.append(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static String ACKPACS(String messagecode,String typecode,String str){
		 String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		 Random random = new Random();
		 String messageId1=String.valueOf(random.nextInt(99));
		String nowtime=DateTimeUtil.getDateTimes();
		StringBuilder sb=new StringBuilder();
		try {
			ORG_O20 org=new ORG_O20();
			
			MSH msh=org.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PES");
			msh.getSendingFacility().getNamespaceID().setValue("10");
			msh.getReceivingApplication().getNamespaceID().setValue("ESB");
			msh.getReceivingFacility().getNamespaceID().setValue("10");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMessageCode().setValue("ORG");
			msh.getMessageType().getTriggerEvent().setValue("O20");
			msh.getMessageControlID().setValue(messageId+messageId1);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.5.1");
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("UNICODE UTF-8");
			
			MSA msa=org.getMSA();
			msa.getAcknowledgmentCode().setValue(typecode);
			msa.getMessageControlID().setValue(messagecode);
			msa.getMsa3_TextMessage().setValue(str);
			Parser parser=new PipeParser();
			str=parser.encode(org);
			System.out.println(str);
			sb.append(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static String ACKCommon(String messagecode,String typecode,String msgId,String ackCode){
		String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		 Random random = new Random();
		 String messageId1=String.valueOf(random.nextInt(99));
		String nowtime=DateTimeUtil.getDateTimes();
		String str="";
		StringBuilder sb=new StringBuilder();
		try {
			ACK ack=new ACK();
			
			MSH msh=ack.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PES");
			msh.getSendingFacility().getNamespaceID().setValue("10");
			msh.getReceivingApplication().getNamespaceID().setValue("ESB");
			msh.getReceivingFacility().getNamespaceID().setValue("10");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMessageCode().setValue(messagecode);
			msh.getMessageType().getTriggerEvent().setValue(typecode);
			msh.getMessageControlID().setValue(messageId+messageId1);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.5.1");
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("UNICODE UTF-8");
			
			MSA msa=ack.getMSA();
			msa.getMsa1_AcknowledgmentCode().setValue(ackCode);
			msa.getMsa2_MessageControlID().setValue(msgId);
			Parser parser=new PipeParser();
			str=parser.encode(ack);
			sb.append(str);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt("xyAllLog","\r\n响应平台时报错"+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		}
		return sb.toString();
	}
	

}
