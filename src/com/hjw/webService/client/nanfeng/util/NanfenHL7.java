package com.hjw.webService.client.nanfeng.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.Bean.Person;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.nanfeng.bean.ItemResultMsgNF;
import com.hjw.webService.client.nanfeng.bean.ReportMsgNF;
import com.synjones.framework.persistence.JdbcQueryManager;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.group.ORM_O01_ORDER;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;
import ca.uhn.hl7v2.model.v251.message.ORM_O01;
import ca.uhn.hl7v2.model.v251.segment.DG1;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.NTE;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.model.v251.segment.PV1;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

public class NanfenHL7 {
	
	
	private static Map<String,String> getDepInterNum(String chargitem_id,String logName){
		Map<String,String> depM = new HashMap<String,String>();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql="select d.id as dep_id,d.dep_num,d.dep_name,d.dep_inter_num,d.remark from charging_item c  " + 
				"left join department_dep d on c.dep_id = d.id " + 
				"where c.id = "+chargitem_id;
		TranLogTxt.liswriteEror_to_txt(logName,sql);
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				depM.put("dep_id", rs.getString("dep_id"));
				depM.put("dep_num", rs.getString("dep_num"));
				depM.put("dep_name", rs.getString("dep_name"));
				String dep_interNum = rs.getString("dep_inter_num");
				String[] interNum = dep_interNum.split("\\^");
				depM.put("dep_inter_num", interNum[0]);
				depM.put("dep_inter_type", interNum[1]);
				depM.put("dep_pacs_type", interNum[2]);
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return depM;
	}
	
	public static String OrmO01hl7_getLis(LisMessageBody lis, LisComponents liscoms,int liscoms_index, String orderControl, String logName,String guid) {
		String nowtime = DateTimeUtil.getDateTimes();
		String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		String messageId1=String.valueOf(random.nextInt(99));
		
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			
			ORM_O01 orm = new ORM_O01();
			MSH msh = orm.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PEIS");
			msh.getReceivingApplication().getNamespaceID().setValue("LIS");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMsg1_MessageCode().setValue("ORM");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("O01");
			msh.getMessageControlID().setValue(guid);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			
			Person custom = lis.getCustom();
			PID pid = orm.getPATIENT().getPID();
			
			pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getExam_num());
			pid.getPid3_PatientIdentifierList(0).getIdentifierTypeCode().setValue("PI");
			pid.getPid3_PatientIdentifierList(1).getCx1_IDNumber().setValue(custom.getPersonidnum());
			pid.getPid3_PatientIdentifierList(1).getIdentifierTypeCode().setValue("PN");
			
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getSurname().setValue(custom.getName());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(custom.getName());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getBirthtime().replaceAll("-", ""));
			if (custom.getSexname().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");
			} else if (custom.getSexname().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			pid.getPid11_PatientAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(custom.getAddress());
			pid.getPid11_PatientAddress(0).getAddressType().setValue("H");
			pid.getPid13_PhoneNumberHome(0).getTelephoneNumber().setValue(custom.getTel());
			if(custom.getMeritalcode().equals("已婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("M");
			}else if(custom.getMeritalcode().equals("未婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}else {
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}
			
			Doctor doctor = lis.getDoctor();
			
			PV1 pv1 = orm.getPATIENT().getPATIENT_VISIT().getPV1();
			pv1.getPv12_PatientClass().setValue("T");
			
			pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());//科室id
			pv1.getPv13_AssignedPatientLocation().getPl2_Room().setValue("");//病区ID
			pv1.getPv13_AssignedPatientLocation().getPl3_Bed().setValue("");//床号
			pv1.getPv17_AttendingDoctor(0).getIDNumber().setValue(doctor.getDoctorCode());
			pv1.getPv17_AttendingDoctor(0).getFamilyName().getSurname().setValue(doctor.getDoctorName());
			pv1.getPv119_VisitNumber().getCx1_IDNumber().setValue(custom.getExam_num());
			pv1.getPv142_PendingLocation().getPointOfCare().setValue(doctor.getDept_code());
			pv1.getPv142_PendingLocation().getRoom().setValue(doctor.getDept_name());
			pv1.getPv144_AdmitDateTime().getTime().setValue(nowtime);
			
			
			ORC orc = orm.getORDER(0).getORC();
			orc.getOrderControl().setValue(orderControl);
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(liscoms.getReq_no());// 申请单号
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());//开申请单的医生信息——id
			orc.getOrc12_OrderingProvider(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctor.getDoctorName());   //开申请单的医生信息——姓名
			orc.getOrc13_EntererSLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());
			orc.getOrc13_EntererSLocation().getPl2_Room().setValue(doctor.getDept_name());
			orc.getOrc15_OrderEffectiveDateTime().getTime().setValue(doctor.getTime());
			
			List<LisComponent> component = liscoms.getItemList();
			LisComponent liscom = component.get(liscoms_index);
			
			OBR obr = orm.getORDER(0).getORDER_DETAIL().getOBR();
			obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(liscom.getExtension());// 申请单号
			obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(liscom.getItemCode());// 项目代码
			obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(liscom.getItemName());// 项目名称
			obr.getObr15_SpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(liscom.getSpecimenNaturalname());  //检查部位代码
//					obr.getObr15_SpecimenSource().getSpecimenSourceNameOrCode().getText().setValue("部位");//检查部位名称
			obr.getObr16_OrderingProvider(0).getIDNumber().setValue(doctor.getDoctorCode());
			obr.getObr16_OrderingProvider(0).getFamilyName().getSurname().setValue(doctor.getDoctorName());
			obr.getObr23_ChargeToPractice().getMonetaryAmount().getQuantity().setValue(String.valueOf(liscom.getItemamount()));
			obr.getObr24_DiagnosticServSectID().setValue(liscoms.getYzCode());
			
			Map<String,String> depM = getDepInterNum(liscom.getChargingItemid(), logName);
			
			NTE nte = orm.getORDER(0).getORDER_DETAIL().getNTE();
			nte.getNte1_SetIDNTE().setValue(String.valueOf(1));
			nte.getNte2_SourceOfComment().setValue("P");
			nte.getNte3_Comment(0).setValue(depM.get("dep_inter_num"));
			nte.getNte4_CommentType().getIdentifier().setValue(depM.get("dep_inter_num"));
			nte.getNte4_CommentType().getText().setValue(depM.get("dep_inter_type"));
			
			DG1 dg1 = orm.getORDER(0).getORDER_DETAIL().getDG1();
			dg1.getDg11_SetIDDG1().setValue(String.valueOf(1));
			dg1.getDg13_DiagnosisCodeDG1().getIdentifier().setValue("");
			dg1.getDg13_DiagnosisCodeDG1().getText().setValue("");
			dg1.getDg16_DiagnosisType().setValue("F");
			dg1.getDg118_ConfidentialIndicator().setValue("");
			
			Parser parser = new PipeParser();
			str = parser.encode(orm);
			TranLogTxt.liswriteEror_to_txt(logName,str);
			sb.append(str);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static String OrmO01hl7_cancelLis(LisMessageBody lis, LisComponents liscoms,int liscoms_index, String orderControl, String logName,String guid) {
		
		String nowtime = DateTimeUtil.getDateTimes();
		String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		String messageId1=String.valueOf(random.nextInt(99));
		
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			
			ORM_O01 orm = new ORM_O01();
			MSH msh = orm.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PEIS");
			msh.getReceivingApplication().getNamespaceID().setValue("LIS");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMsg1_MessageCode().setValue("ORM");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("O01");
			msh.getMessageControlID().setValue(guid);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			
			Person custom = lis.getCustom();
			PID pid = orm.getPATIENT().getPID();
			
			pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getExam_num());
			pid.getPid3_PatientIdentifierList(0).getIdentifierTypeCode().setValue("PI");
			pid.getPid3_PatientIdentifierList(1).getCx1_IDNumber().setValue(custom.getPersonidnum());
			pid.getPid3_PatientIdentifierList(1).getIdentifierTypeCode().setValue("PN");
			
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getSurname().setValue(custom.getName());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(custom.getName());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getBirthtime().replaceAll("-", ""));
			if (custom.getSexname().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");
			} else if (custom.getSexname().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			pid.getPid11_PatientAddress(0).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(custom.getAddress());
			pid.getPid13_PhoneNumberHome(0).getXtn9_AnyText().setValue(custom.getTel());
			pid.getPid14_PhoneNumberBusiness(0).getXtn9_AnyText().setValue(custom.getTel());
			if(custom.getMeritalcode().equals("已婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("M");
				pid.getPid16_MaritalStatus().getCe2_Text().setValue("结婚");
			}else if(custom.getMeritalcode().equals("未婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
				pid.getPid16_MaritalStatus().getCe2_Text().setValue("未婚");
			}
			
			Doctor doctor = lis.getDoctor();
			
			PV1 pv1 = orm.getPATIENT().getPATIENT_VISIT().getPV1();
			pv1.getPv12_PatientClass().setValue("T");
			
			pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());//科室id
			pv1.getPv13_AssignedPatientLocation().getPl2_Room().setValue("");//病区ID
			pv1.getPv13_AssignedPatientLocation().getPl3_Bed().setValue("");//床号
			pv1.getPv17_AttendingDoctor(0).getIDNumber().setValue(doctor.getDoctorCode());
			pv1.getPv17_AttendingDoctor(0).getFamilyName().getSurname().setValue(doctor.getDoctorName());
			pv1.getPv119_VisitNumber().getCx1_IDNumber().setValue(custom.getExam_num());
			pv1.getPv142_PendingLocation().getPointOfCare().setValue(doctor.getDept_code());
			pv1.getPv142_PendingLocation().getRoom().setValue(doctor.getDept_name());
			pv1.getPv144_AdmitDateTime().getTime().setValue(nowtime);
			
		
			ORC orc = orm.getORDER(0).getORC();
			orc.getOrderControl().setValue(orderControl);
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(liscoms.getReq_no());// 申请单号
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());//开申请单的医生信息——id
			orc.getOrc12_OrderingProvider(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctor.getDoctorName());   //开申请单的医生信息——姓名
			orc.getOrc13_EntererSLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());
			orc.getOrc13_EntererSLocation().getPl2_Room().setValue(doctor.getDept_name());
			orc.getOrc15_OrderEffectiveDateTime().getTime().setValue(doctor.getTime());
			
			List<LisComponent> component = liscoms.getItemList();
			for (int j = 0; j < component.size(); j++) {
				LisComponent liscom = component.get(j);
				
				OBR obr = orm.getORDER(0).getORDER_DETAIL().getOBR();
				obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(liscom.getExtension());// 申请单号
				obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(liscom.getItemCode());// 项目代码
				obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(liscom.getItemName());// 项目名称
				obr.getObr15_SpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue("");  //检查部位代码
				obr.getObr16_OrderingProvider(j).getIDNumber().setValue(doctor.getDoctorCode());
				obr.getObr16_OrderingProvider(j).getFamilyName().getSurname().setValue(doctor.getDoctorName());
				obr.getObr23_ChargeToPractice().getMonetaryAmount().getQuantity().setValue(String.valueOf(liscom.getItemamount()));
				obr.getObr24_DiagnosticServSectID().setValue(liscoms.getYzCode());
				
				
//				NTE nte = orm.getORDER(liscoms_index).getORDER_DETAIL().getNTE();
//				nte.getNte1_SetIDNTE().setValue(String.valueOf(j+1));
//				nte.getNte2_SourceOfComment().setValue("P");
//				nte.getNte3_Comment(0).setValue("E001");
//				
//				DG1 dg1 = orm.getORDER(liscoms_index).getORDER_DETAIL().getDG1();
//				dg1.getDg11_SetIDDG1().setValue(String.valueOf(j+1));
//				dg1.getDg13_DiagnosisCodeDG1().getIdentifier().setValue("");
//				dg1.getDg13_DiagnosisCodeDG1().getText().setValue("");
//				dg1.getDg16_DiagnosisType().setValue("F");
//				dg1.getDg118_ConfidentialIndicator().setValue("");
			}
				
			Parser parser = new PipeParser();
			str = parser.encode(orm);
			TranLogTxt.liswriteEror_to_txt(logName,str);
			sb.append(str);
			
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logName, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		
		return sb.toString();
	}
	
	
	public static String OrmO01hl7_getPacs(PacsMessageBody lis, PacsComponents liscoms,int liscoms_index, String orderControl, String logName,String guid) {
		
		String nowtime = DateTimeUtil.getDateTimes();
		String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		String messageId1=String.valueOf(random.nextInt(99));
		
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			
			String depType = "RIS";
			Map<String,String> depM = getDepInterNum(String.valueOf(liscoms.getPacsComponent().get(0).getItemId()), logName);
			depType = depM.get("dep_pacs_type");
			ORM_O01 orm = new ORM_O01();
			MSH msh = orm.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PEIS");
			msh.getReceivingApplication().getNamespaceID().setValue(depType);
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMsg1_MessageCode().setValue("ORM");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("O01");
			msh.getMessageControlID().setValue(guid);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			
			Person custom = lis.getCustom();
			PID pid = orm.getPATIENT().getPID();
			
			pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getExam_num());
			pid.getPid3_PatientIdentifierList(0).getIdentifierTypeCode().setValue("PI");
			pid.getPid3_PatientIdentifierList(1).getCx1_IDNumber().setValue(custom.getPersonidnum());
			pid.getPid3_PatientIdentifierList(1).getIdentifierTypeCode().setValue("PN");
			
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getSurname().setValue(custom.getName());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(custom.getName());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getBirthtime().replaceAll("-", ""));
			if (custom.getSexname().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");
			} else if (custom.getSexname().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			pid.getPid11_PatientAddress(0).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(custom.getAddress());
			pid.getPid13_PhoneNumberHome(0).getTelephoneNumber().setValue(custom.getTel());
			if(custom.getMeritalcode().equals("已婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("M");
			}else if(custom.getMeritalcode().equals("未婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}
			
			Doctor doctor = lis.getDoctor();
			
			PV1 pv1 = orm.getPATIENT().getPATIENT_VISIT().getPV1();
			pv1.getPv12_PatientClass().setValue("T");
			
			pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());//科室id
			pv1.getPv13_AssignedPatientLocation().getPl2_Room().setValue("");//病区ID
			pv1.getPv13_AssignedPatientLocation().getPl3_Bed().setValue("");//床号
			pv1.getPv119_VisitNumber().getCx1_IDNumber().setValue(custom.getExam_num());
			pv1.getPv144_AdmitDateTime().getTime().setValue(nowtime);
			
			
			ORC orc = orm.getORDER(0).getORC();
			orc.getOrderControl().setValue(orderControl);
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(liscoms.getReq_no());// 申请单号
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());//开申请单的医生信息——id
			orc.getOrc12_OrderingProvider(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctor.getDoctorName());   //开申请单的医生信息——姓名
			orc.getOrc13_EntererSLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());
			orc.getOrc13_EntererSLocation().getPl9_LocationDescription().setValue(doctor.getDept_name());
			orc.getOrc15_OrderEffectiveDateTime().getTime().setValue(doctor.getTime());
			
			List<PacsComponent> component = liscoms.getPacsComponent();
			for (int j = 0; j < component.size(); j++) {
				PacsComponent liscom = component.get(j);
				
				OBR obr = orm.getORDER(0).getORDER_DETAIL().getOBR();
				obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(liscoms.getReq_no());// 申请单号
				obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(liscom.getItemCode());// 项目代码
				obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(liscom.getItemName());// 项目名称
				obr.getObr15_SpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue("");  //检查部位代码
				obr.getObr16_OrderingProvider(j).getIDNumber().setValue(doctor.getDoctorCode());
				obr.getObr16_OrderingProvider(j).getFamilyName().getSurname().setValue(doctor.getDoctorName());
				obr.getObr23_ChargeToPractice().getMonetaryAmount().getQuantity().setValue(String.valueOf(liscom.getItemamount()));
				obr.getObr24_DiagnosticServSectID().setValue(liscoms.getYzCode());
				
				
				NTE nte = orm.getORDER(0).getORDER_DETAIL().getNTE();
				nte.getNte1_SetIDNTE().setValue(String.valueOf(j+1));
				nte.getNte2_SourceOfComment().setValue("P");
				nte.getNte3_Comment(0).setValue(depM.get("dep_inter_num"));
				nte.getNte4_CommentType().getIdentifier().setValue(depM.get("dep_inter_num"));
				nte.getNte4_CommentType().getText().setValue(depM.get("dep_inter_type"));
				
				
				DG1 dg1 = orm.getORDER(0).getORDER_DETAIL().getDG1();
				dg1.getDg11_SetIDDG1().setValue(String.valueOf(j+1));
				dg1.getDg13_DiagnosisCodeDG1().getIdentifier().setValue("");
				dg1.getDg13_DiagnosisCodeDG1().getText().setValue("");
				dg1.getDg16_DiagnosisType().setValue("F");
				dg1.getDg118_ConfidentialIndicator().setValue("");
			}
				
			Parser parser = new PipeParser();
			str = parser.encode(orm);
			TranLogTxt.liswriteEror_to_txt(logName,str);
			sb.append(str);
			
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logName, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		
		return sb.toString();
	}
	
	
	public static String OrmO01hl7_cancelPacs(PacsMessageBody lis, PacsComponents liscoms,int liscoms_index, String orderControl, String logName,String guid) {
		
		String nowtime = DateTimeUtil.getDateTimes();
		String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		String messageId1=String.valueOf(random.nextInt(99));
		
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			String depType = "RIS";
			Map<String,String> depM = getDepInterNum(String.valueOf(liscoms.getPacsComponent().get(0).getItemId()), logName);
			depType = depM.get("dep_pacs_type");
			ORM_O01 orm = new ORM_O01();
			MSH msh = orm.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PEIS");
			msh.getReceivingApplication().getNamespaceID().setValue(depType);
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMsg1_MessageCode().setValue("ORM");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("O01");
			msh.getMessageControlID().setValue(guid);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			
			Person custom = lis.getCustom();
			PID pid = orm.getPATIENT().getPID();
			
			pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getExam_num());
			pid.getPid3_PatientIdentifierList(0).getIdentifierTypeCode().setValue("PI");
			pid.getPid3_PatientIdentifierList(1).getCx1_IDNumber().setValue(custom.getPersonidnum());
			pid.getPid3_PatientIdentifierList(1).getIdentifierTypeCode().setValue("PN");
			
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getSurname().setValue(custom.getName());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(custom.getName());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getBirthtime().replaceAll("-", ""));
			if (custom.getSexname().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");
			} else if (custom.getSexname().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			pid.getPid11_PatientAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(custom.getAddress());
			pid.getPid11_PatientAddress(0).getAddressType().setValue("H");
			pid.getPid13_PhoneNumberHome(0).getTelephoneNumber().setValue(custom.getTel());
			if(custom.getMeritalcode().equals("已婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("M");
			}else if(custom.getMeritalcode().equals("未婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}else {
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}
			
			Doctor doctor = lis.getDoctor();
			
			PV1 pv1 = orm.getPATIENT().getPATIENT_VISIT().getPV1();
			pv1.getPv12_PatientClass().setValue("T");
			
			pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());//科室id
			pv1.getPv13_AssignedPatientLocation().getPl2_Room().setValue("");//病区ID
			pv1.getPv13_AssignedPatientLocation().getPl3_Bed().setValue("");//床号
			pv1.getPv119_VisitNumber().getCx1_IDNumber().setValue(custom.getExam_num());
			pv1.getPv144_AdmitDateTime().getTime().setValue(nowtime);
			
			
			ORC orc = orm.getORDER(0).getORC();
			orc.getOrderControl().setValue(orderControl);
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(liscoms.getReq_no());// 申请单号
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());//开申请单的医生信息——id
			orc.getOrc12_OrderingProvider(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctor.getDoctorName());   //开申请单的医生信息——姓名
			orc.getOrc13_EntererSLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());
			orc.getOrc13_EntererSLocation().getPl2_Room().setValue(doctor.getDept_name());
			orc.getOrc15_OrderEffectiveDateTime().getTime().setValue(doctor.getTime());
			
			List<PacsComponent> component = liscoms.getPacsComponent();
			for (int j = 0; j < component.size(); j++) {
				PacsComponent liscom = component.get(j);
				
				OBR obr = orm.getORDER(0).getORDER_DETAIL().getOBR();
				obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(liscoms.getReq_no());// 申请单号
				obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(liscom.getItemCode());// 项目代码
				obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(liscom.getItemName());// 项目名称
				obr.getObr15_SpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue("");  //检查部位代码
				obr.getObr16_OrderingProvider(j).getIDNumber().setValue(doctor.getDoctorCode());
				obr.getObr16_OrderingProvider(j).getFamilyName().getSurname().setValue(doctor.getDoctorName());
				obr.getObr23_ChargeToPractice().getMonetaryAmount().getQuantity().setValue(String.valueOf(liscom.getItemamount()));
				obr.getObr24_DiagnosticServSectID().setValue(liscoms.getYzCode());
				
				
//				NTE nte = orm.getORDER(liscoms_index).getORDER_DETAIL().getNTE();
//				nte.getNte1_SetIDNTE().setValue(String.valueOf(j+1));
//				nte.getNte2_SourceOfComment().setValue("P");
//				nte.getNte3_Comment(0).setValue("E001");
//				
//				DG1 dg1 = orm.getORDER(liscoms_index).getORDER_DETAIL().getDG1();
//				dg1.getDg11_SetIDDG1().setValue(String.valueOf(j+1));
//				dg1.getDg13_DiagnosisCodeDG1().getIdentifier().setValue("");
//				dg1.getDg13_DiagnosisCodeDG1().getText().setValue("");
//				dg1.getDg16_DiagnosisType().setValue("F");
//				dg1.getDg118_ConfidentialIndicator().setValue("");
			}
				
			Parser parser = new PipeParser();
			str = parser.encode(orm);
			TranLogTxt.liswriteEror_to_txt(logName,str);
			sb.append(str);
			
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logName, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		
		return sb.toString();
	}
	
	
	public static String getReq_Lis(String str) {
		System.out.println(str);
		ReportMsgNF msgNF = new ReportMsgNF();
		PipeParser pipeParser = new PipeParser();
		Message msg;
		String res="";
		try {
			msg = pipeParser.parse(str);
			ORM_O01 orm = (ORM_O01)msg;
			String req_no = orm.getORDER().getORC().getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().getValue();
			msgNF.setLabNo(req_no);
			System.out.println(req_no);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getResCode(String str) {
		PipeParser pipeParser = new PipeParser();
		String res_code = "";
		try {
//			Message message = pipeParser.parse(str);
//			Terser terser = new Terser(message);
//			res_code = terser.get("/.MSA-3");
			
			String[] res = str.split("\\|");
			
			res_code = res[res.length-1];
			
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt("reqLis", "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			res_code = "error:解析LIS返回信息失败！";
		}
		
		return res_code;
	}
	
	
	public static String getMSGHl7(String str) {
		
		Parser p = new GenericParser();
//		System.out.println(str);
		try {
			Message hapi = p.parse(str);
			ORM_O01 orm = (ORM_O01) hapi;
			String ss = orm.getPATIENT().getPID().getPid2_PatientID().getIDNumber().getValue();
			System.out.println(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 解析hl7（ORM_O01）数据
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static List<ReportMsgNF> getMSGHl7_ORM(String str) throws Exception {
		System.out.println(str);
		Parser p = new GenericParser();
		List<ReportMsgNF> msgnftList = new ArrayList<ReportMsgNF>();
		Message hapi = p.parse(str);
		ORM_O01 orm = (ORM_O01) hapi;
		
		String exam_num = orm.getPATIENT().getPID().getPid3_PatientIdentifierList(0).getIDNumber().getValue();
		
		List<ORM_O01_ORDER> orderList = orm.getORDERAll();
		for (int i = 0; i < orderList.size(); i++) {
			ReportMsgNF msgnf = new ReportMsgNF();
			//申请单号（条码号）
			String labNo = orm.getORDER(0).getORC().getOrc2_PlacerOrderNumber().getEntityIdentifier().getValue();
			msgnf.setLabNo(labNo);
			
			msgnf.setExam_num(exam_num);
			
			List<ItemResultMsgNF> resMsgList = new ArrayList<ItemResultMsgNF>();
			ItemResultMsgNF resMsg = new ItemResultMsgNF();
			OBR obr = orm.getORDER(0).getORDER_DETAIL().getOBR();
			
			String item_labNo = obr.getPlacerOrderNumber().getEntityIdentifier().getValue();
			resMsg.setLabNo(item_labNo);
			
			String reportNo = obr.getObr3_FillerOrderNumber().getEntityIdentifier().getValue();
			resMsg.setReportNo(reportNo);
			
			String testCode = obr.getObr4_UniversalServiceIdentifier().getIdentifier().getValue();
			resMsg.setTestCode(testCode);
			
			String testName = obr.getObr4_UniversalServiceIdentifier().getText().getValue();
			resMsg.setTestName(testName);
			
			String exam_status = obr.getObr25_ResultStatus().getValue();
			resMsg.setExam_status(exam_status);
			
			resMsgList.add(resMsg);
			msgnf.setResMsg(resMsgList);
			msgnftList.add(msgnf);
		}
		System.out.println("eeee");
		return msgnftList;
	}
	
	public static String createResultMsg(String resStatus,String resMsg,String logName) {
		String nowtime = DateTimeUtil.getDateTimes();
		String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		String guid = GetGUID.getGUID();
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			
			ACK ack = new ACK();
			MSH msh = ack.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PEIS");
			msh.getReceivingApplication().getNamespaceID().setValue("LIS");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMsg1_MessageCode().setValue("ACK");
			msh.getMessageControlID().setValue(guid);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			
			MSA msa = ack.getMSA();
			msa.getMsa1_AcknowledgmentCode().setValue(resStatus);
			msa.getMsa2_MessageControlID().setValue(guid);
			msa.getMsa3_TextMessage().setValue(resMsg);
				
			Parser parser = new PipeParser();
			str = parser.encode(ack);
			
			sb.append(str);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		
		String str = "MSH|^~\\&|LIS||PEIS||20140604150202||ORM^O01|f791da16-fe1b-4bd6-9df0-177d2624f178|P|2.5.1\r\n" + 
				"PID|||0003852856^^^^PI~214046068001^^^^SS||王江明^Wang Jiang Ming||||||||||\r\n" + 
				"PV1||I|0241^^1007|||| |||||||||||214046068001\r\n" + 
				"ORC|SC|122530309|||RC||||||||\r\n" + 
				"OBR||122530309|pacs01200|0315^CT检查|||||||||||||||||||||I";
		
		List<ReportMsgNF> res_code;
		try {
			res_code = getMSGHl7_ORM(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name_pingyin = "裘千仞021";
		name_pingyin = PinyinUtil.getTheAllMathedPinYin(name_pingyin);
		System.out.println(name_pingyin==null?"55":name_pingyin);
		
	}

}
