package com.hjw.webService.client.chongqing.util;

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
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.Bean.Person;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.bean.ResLisStatusBeanHK;
import com.hjw.webService.client.nanfeng.bean.ItemResultMsgNF;
import com.hjw.webService.client.nanfeng.bean.ReportMsgNF;
import com.hjw.webService.service.lisbean.RetLisChargeItem;
import com.hjw.webService.service.lisbean.RetLisCustome;
import com.hjw.webService.service.lisbean.RetLisItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.datatype.CX;
import ca.uhn.hl7v2.model.v251.datatype.ST;
import ca.uhn.hl7v2.model.v251.datatype.XCN;
import ca.uhn.hl7v2.model.v251.group.ORM_O01_ORDER;
import ca.uhn.hl7v2.model.v251.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v251.group.OUL_R21_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;
import ca.uhn.hl7v2.model.v251.message.OML_O21;
import ca.uhn.hl7v2.model.v251.message.ORL_O22;
import ca.uhn.hl7v2.model.v251.message.ORM_O01;
import ca.uhn.hl7v2.model.v251.message.ORR_O02;
import ca.uhn.hl7v2.model.v251.message.ORU_R01;
import ca.uhn.hl7v2.model.v251.message.OUL_R21;
import ca.uhn.hl7v2.model.v251.segment.DG1;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.NTE;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.model.v251.segment.PV1;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class ChongQingSetHL7 {
	
	
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
		String guid	= UUID.randomUUID().toString().replaceAll("-", "");
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

	/*
	 * 重庆九院 拼接人员新增 HL7  入参数据
	 */
	public static String setCustomerSend(Custom custom,String logname) {
		
		String senreq = "";
		try {
			
			ADT_A01 adt = new ADT_A01();
			MSH msh = adt.getMSH();
			
			//=========MSH
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getHd1_NamespaceID().setValue("ZLHIS");
			//msh.getSendingFacility().getHd1_NamespaceID().setValue("");
			msh.getReceivingApplication().getHd1_NamespaceID().setValue("MIS");
			msh.getReceivingApplication().getHd2_UniversalID().setValue("2008");
			//msh.getReceivingFacility().getHd1_NamespaceID().setValue("");
			msh.getDateTimeOfMessage().getTime().setValue(DateTimeUtil.getDateTimes());
			//msh.getSecurity().setValue("");
			msh.getMessageType().getMsg1_MessageCode().setValue("ADT");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("A04");
			msh.getMessageControlID().setValue("ADT_A04-"+DateTimeUtil.getDateTimes());
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			msh.getAcceptAcknowledgmentType().setValue("NE");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			
			//======PID
			PID pid = adt.getPID();
			pid.getSetIDPID().setValue("1");
			pid.getPatientID().getCx1_IDNumber().setValue(custom.getEXAM_NUM());
			pid.getPatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getID_NO());
		/*	pid.getPatientIdentifierList(1).getCx1_IDNumber().setValue(this.custom.getID_NO());
			pid.getPatientIdentifierList(2).getCx1_IDNumber().setValue(this.custom.getID_NO());
			pid.getPatientIdentifierList(3).getCx1_IDNumber().setValue(this.custom.getID_NO());*/
			
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().setValue(custom.getNAME());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(custom.getNAME());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getDATE_OF_BIRTH().replaceAll("-", ""));
			if (custom.getSEX().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");
			} else if (custom.getSEX().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			pid.getPid11_PatientAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(custom.getMAILING_ADDRESS_CODE());
			pid.getPid13_PhoneNumberHome(0).getTelephoneNumber().setValue(custom.getPHONE_NUMBER_HOME());
			pid.getPid16_MaritalStatus().getCe1_Identifier().setValue(custom.getMARITAL_STATUS());
			/*if(custom.getMARITAL_STATUS().equals("已婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("M");
			}else if(custom.getMARITAL_STATUS().equals("未婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}else {
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}*/
			
			
			Parser parser = new PipeParser();
			senreq = parser.encode(adt);
			
			TranLogTxt.liswriteEror_to_txt(logname, "人员注册入参:" + senreq + "\r\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return senreq;
		
	}

	/*
	 * 重庆九院 拼接人员信息更新 HL7  入参数据
	 */
	public static String updateCustomerSend(Custom custom,String logname) {
		
		String senreq = "";
		try {
			ADT_A01 adt = new ADT_A01();
			MSH msh = adt.getMSH();
			
			//=========MSH
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getHd1_NamespaceID().setValue("ZLHIS");
			//msh.getSendingFacility().getHd1_NamespaceID().setValue("");
			msh.getReceivingApplication().getHd1_NamespaceID().setValue("MIS");
			msh.getReceivingApplication().getHd2_UniversalID().setValue("2008");
			//msh.getReceivingFacility().getHd1_NamespaceID().setValue("");
			msh.getDateTimeOfMessage().getTime().setValue(DateTimeUtil.getDateTimes());
			//msh.getSecurity().setValue("");
			msh.getMessageType().getMsg1_MessageCode().setValue("ADT");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("A08");
			msh.getMessageControlID().setValue("ADT_A08-"+DateTimeUtil.getDateTimes());
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			msh.getAcceptAcknowledgmentType().setValue("NE");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			
			//======PID
			PID pid = adt.getPID();
			pid.getSetIDPID().setValue("1");
			pid.getPatientID().getCx1_IDNumber().setValue(custom.getEXAM_NUM());
			pid.getPatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getID_NO());
		/*	pid.getPatientIdentifierList(1).getCx1_IDNumber().setValue(this.custom.getID_NO());
			pid.getPatientIdentifierList(2).getCx1_IDNumber().setValue(this.custom.getID_NO());
			pid.getPatientIdentifierList(3).getCx1_IDNumber().setValue(this.custom.getID_NO());*/
			
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().setValue(custom.getNAME());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(custom.getNAME());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getDATE_OF_BIRTH().replaceAll("-", ""));
			if (custom.getSEX().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");
			} else if (custom.getSEX().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			pid.getPid11_PatientAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(custom.getMAILING_ADDRESS_CODE());
			pid.getPid13_PhoneNumberHome(0).getTelephoneNumber().setValue(custom.getPHONE_NUMBER_HOME());
			pid.getPid16_MaritalStatus().getCe1_Identifier().setValue(custom.getMARITAL_STATUS());
			/*if(custom.getMARITAL_STATUS().equals("已婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("M");
			}else if(custom.getMARITAL_STATUS().equals("未婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}else {
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}*/
			
			
			Parser parser = new PipeParser();
			 senreq = parser.encode(adt);
			 
			 TranLogTxt.liswriteEror_to_txt(logname, "人员更新入参:" + senreq + "\r\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return senreq;
		
	}

	/*
	 * 重庆九院  发送pacs申请
	 */
	public static String SendPacs(PacsComponents pcs, ExamInfoUserDTO ei, String logname) {
		String senreq="";
		
		try {
			ORM_O01 orm = new ORM_O01();
			MSH msh = orm.getMSH();
			
			
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getHd1_NamespaceID().setValue("PEIS");
			msh.getReceivingApplication().getHd1_NamespaceID().setValue("PACS");
			msh.getReceivingApplication().getHd2_UniversalID().setValue("5001");
			msh.getDateTimeOfMessage().getTime().setValue(DateTimeUtil.getDateTimes());
			msh.getMessageType().getMsg1_MessageCode().setValue("ORM");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("001");
			msh.getMessageControlID().setValue("2001-"+DateTimeUtil.getDateTimes());
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			msh.getAcceptAcknowledgmentType().setValue("NE");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			
			PID pid = orm.getPATIENT().getPID();
			pid.getSetIDPID().setValue("1");
			pid.getPatientID().getCx1_IDNumber().setValue(ei.getExam_num());
			pid.getPatientIdentifierList(0).getCx1_IDNumber().setValue(ei.getId_num());
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().setValue(ei.getUser_name());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(ei.getUser_name());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			pid.getPid7_DateTimeOfBirth().getTime().setValue(ei.getBirthday().replaceAll("-", ""));//出生日期
			if (ei.getSex().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");//性别
			} else if (ei.getSex().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			
			PV1 pv1 = orm.getPATIENT().getPATIENT_VISIT().getPV1();
			pv1.getSetIDPV1().setValue("");
			pv1.getPatientClass().setValue("H"); //病人来源 O门诊 I住院 H体检
			//pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue("");//科室id
			//pv1.getAssignedPatientLocation().getRoom().setValue("");//病区ID
			//pv1.getAssignedPatientLocation().getPl3_Bed().setValue("");//床号
																		//门诊诊室^科室名称^病区名
			//pv1.getPatientType().setValue("普通病人^^");
			pv1.getPv119_VisitNumber().getCx1_IDNumber().setValue(pcs.getReq_no());//就诊号
			pv1.getAdmitDateTime().getTime().setValue(DateTimeUtil.getDateTimes());//就诊时间
			
			
			ORC orc = orm.getORDER(0).getORC();
			orc.getOrderControl().setValue("NW");//NW"表示生成新医嘱
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(pcs.getReq_no());//医嘱编号（申请单号）
			orc.getFillerOrderNumber().getEntityIdentifier().setValue("");//执行系统医嘱编号
			orc.getOrderStatus().setValue("IP");
			orc.getDateTimeOfTransaction().getTime().setValue(DateTimeUtil.getDateTimes());//开单时间
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue("");//医嘱提供者
			orc.getOrderEffectiveDateTime().getTime().setValue("");//医嘱生效时间
			
			//开立医嘱的机构名称
			orc.getOrderingFacilityName(0).getOrganizationName().setValue("");//名称
			orc.getOrderingFacilityName(0).getOrganizationNameTypeCode();//编码
			
			
			List<PacsComponent> pacsComponent = pcs.getPacsComponent();
			for (PacsComponent pacs : pacsComponent) {
			
				
				//执行医嘱的机构名称（Filler Order Place）--自定义
				OBR obr = orm.getORDER(0).getORDER_DETAIL().getOBR();
				obr.getSetIDOBR().setValue("1");
				obr.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(pcs.getReq_no());////医嘱编号（申请单号）
				obr.getFillerOrderNumber().getEi1_EntityIdentifier().setValue("");//执行系统医嘱编号
				obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(pacs.getHis_num());// 项目代码
				obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(pacs.getItemName());// 项目名称
				//检查项目类型
				//医嘱提供者（Ordering Provider）
				//送检科室(Order Callback Phone Number)
				
			}
			
			
			NTE nte = orm.getORDER(0).getORDER_DETAIL().getNTE();
			nte.getNte1_SetIDNTE().setValue("1");
			//nte.getComment(0).setValue("注释");
			
			DG1 dg1 = orm.getORDER(0).getORDER_DETAIL().getDG1();
			dg1.getDg11_SetIDDG1().setValue("1");
			
			
			Parser parser = new PipeParser();
			senreq = parser.encode(orm);
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return senreq;
	}

	public static String delPacs(PacsComponents comps, Person custom, String logname) {
		// TODO Auto-generated method stub
		String senreq="";
		
		try {
			ORM_O01 orm = new ORM_O01();
			MSH msh = orm.getMSH();
			
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getHd1_NamespaceID().setValue("PEIS");
			msh.getReceivingApplication().getHd1_NamespaceID().setValue("PACS");
			msh.getReceivingApplication().getHd2_UniversalID().setValue("5002");
			msh.getDateTimeOfMessage().getTime().setValue(DateTimeUtil.getDateTimes());
			msh.getMessageType().getMsg1_MessageCode().setValue("ORM");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("001");
			msh.getMessageControlID().setValue("2001-"+DateTimeUtil.getDateTimes());
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			msh.getAcceptAcknowledgmentType().setValue("NE");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			
			
			
			PID pid = orm.getPATIENT().getPID();
			pid.getSetIDPID().setValue("1");
			pid.getPatientID().getCx1_IDNumber().setValue(custom.getExam_num());
			pid.getPatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getPersonidnum());
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().setValue(custom.getName());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(custom.getName());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getBirthtime().replaceAll("-", ""));//出生日期
			
			if (custom.getSexcode().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");//性别
			} else if (custom.getSexcode().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			
			PV1 pv1 = orm.getPATIENT().getPATIENT_VISIT().getPV1();
			pv1.getSetIDPV1().setValue("");
			pv1.getPatientClass().setValue("H"); //病人来源 O门诊 I住院 H体检
			//pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue("");//科室id
			//pv1.getAssignedPatientLocation().getRoom().setValue("");//病区ID
			//pv1.getAssignedPatientLocation().getPl3_Bed().setValue("");//床号
																		//门诊诊室^科室名称^病区名
			//pv1.getPatientType().setValue("普通病人^^");
			pv1.getPv119_VisitNumber().getCx1_IDNumber().setValue(comps.getReq_no());//就诊号
			pv1.getAdmitDateTime().getTime().setValue(DateTimeUtil.getDateTimes());//就诊时间
			
			
			ORC orc = orm.getORDER(0).getORC();
			orc.getOrderControl().setValue("NW");//NW"表示生成新医嘱
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(comps.getReq_no());//医嘱编号（申请单号）
			orc.getFillerOrderNumber().getEntityIdentifier().setValue("");//执行系统医嘱编号
			orc.getOrderStatus().setValue("IP");
			orc.getDateTimeOfTransaction().getTime().setValue(DateTimeUtil.getDateTimes());//开单时间
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue("");//医嘱提供者
			orc.getOrderEffectiveDateTime().getTime().setValue("");//医嘱生效时间
			
			//开立医嘱的机构名称
			orc.getOrderingFacilityName(0).getOrganizationName().setValue("");//名称
			orc.getOrderingFacilityName(0).getOrganizationNameTypeCode();//编码
			
			List<PacsComponent> pacsComponent = comps.getPacsComponent();
			//可以注释 暂时无此节点
			for (PacsComponent pacslist : pacsComponent) {
				//执行医嘱的机构名称（Filler Order Place）--自定义
				
				//暂时没有这个节点
				/*OBR obr = orm.getORDER(0).getORDER_DETAIL().getOBR();
				obr.getSetIDOBR().setValue("1");
				obr.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(comps.getReq_no());////医嘱编号（申请单号）
				obr.getFillerOrderNumber().getEi1_EntityIdentifier().setValue("");//执行系统医嘱编号
				obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(pacslist.getHis_num());// 项目代码
				obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(pacslist.getItemName());// 项目名称
				*/			
				}
			
			Parser parser = new PipeParser();
			senreq = parser.encode(orm);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		return senreq;
	}
	public static PacsResult resPacs(String xmlmessage, String logname) {
		PacsResult pacsResult = new PacsResult();
		
		ORU_R01 oru = new Gson().fromJson(xmlmessage, ORU_R01.class);
	//	MSH msh = fromJson.getMSH();
	//	String value = msh.getMessageControlID().getValue();
	//患者标识列表，格式：门诊号^体检编号^住院号^就诊卡号^身份证号
		
		try {
			PID pid = oru.getPATIENT_RESULT(0).getPATIENT().getPID();
			String patient_id = pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().getValue();
			
			
			ORC orc = oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC();
			//申请单号
			String pacs_req_num = orc.getOrc2_PlacerOrderNumber().getEi3_UniversalID().getValue();
			
			
			OBR obr = oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getOBR();
			//报告时间
			String check_date = obr.getObr22_ResultsRptStatusChngDateTime().getTime().getValue();
			String audit_date="";
			String exam_result = "";
			String exam_desc = "";
			XCN[] check_doct_audit_doct = null;
			List<ORU_R01_OBSERVATION>	list = oru.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getOBSERVATIONAll();
			for (int i = 0; i < list.size(); i++) {
				OBX obx = list.get(i).getOBX();
				String type = obx.getObx2_ValueType().getValue();
				//审核时间
				audit_date = obx.getDateTimeOfTheAnalysis().getTime().toString();
				check_doct_audit_doct = obx.getResponsibleObserver();
				
				if (type.equals("TX")) {
					exam_desc = obx.getObx5_ObservationValue(0).getData().toString();
					if(exam_desc!=null&&exam_desc.length()>0){
						exam_desc=exam_desc;
					}else{
						exam_desc="";
					}
				} else if (type.equals("ST")) {
					exam_result = obx.getObx5_ObservationValue(0).getData().toString();
					if(exam_result!=null&&exam_result.length()>0){
						exam_result=exam_result;
					}else{
						exam_result="";
					}
				}
			}
			
			
			//plr.setExam_num(exam_num);
			//plr.setPacs_req_code(pacs_req_num);
			//plr.setCheck_date(check_date);
			//检查医生
			//plr.setCheck_doct(check_doct_audit_doct[0]+"");
			//plr.setAudit_date(audit_date);
			//审核医生
			//plr.setAudit_doct(check_doct_audit_doct[1]+"");
			//plr.setExam_result(exam_result);
			//plr.setExam_desc(exam_desc);
			//plr.setImg_file("");//图像文件路径以分号隔开
			
			
			
			
			
			pacsResult.setTil_id("");
			pacsResult.setReq_no(pacs_req_num);
			pacsResult.setPacs_checkno("");
			pacsResult.setReg_doc(check_doct_audit_doct[0]+"");//记录医生
			pacsResult.setCheck_doc(check_doct_audit_doct[0]+"");//检查医生姓名	
			pacsResult.setCheck_date(check_date);//检查时间
			pacsResult.setReport_doc(check_doct_audit_doct[1]+"");//报告医生
			pacsResult.setReport_date(audit_date);//报告时间
			pacsResult.setAudit_doc(check_doct_audit_doct[1]+"");//审核医生
			pacsResult.setAudit_date(audit_date);//审核时间
			
			pacsResult.setClinic_symptom(exam_desc);//描述
			pacsResult.setClinic_diagnose(exam_result);//结论
			pacsResult.setStudy_body_part("");//部位
			pacsResult.setStudy_type("");//类型
			pacsResult.setItem_name("");//项目名称
			pacsResult.setPacs_item_code("");//项目编码
			pacsResult.setIs_tran_image(0);//是否取图	默认为1,0 - 不取,1 - 取
			pacsResult.setReport_img_path("");//图片地址

		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return pacsResult;
	}

	public static String SendLis(LisComponents comps, String logname, LisMessageBody lismessage) {
		String nowtime = DateTimeUtil.getDateTimes();
		String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		String messageId1=String.valueOf(random.nextInt(99));
		
		String str = "";
		StringBuilder sb = new StringBuilder();
		OML_O21 oml_O21 = new OML_O21();
		try {
			MSH msh = oml_O21.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PEIS");
			msh.getReceivingApplication().getHd1_NamespaceID().setValue("LIS");
			msh.getReceivingApplication().getHd2_UniversalID().setValue("4001");
			msh.getDateTimeOfMessage().getTime().setValue(DateTimeUtil.getDateTimes());
			msh.getMessageType().getMsg1_MessageCode().setValue("ADT");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("A04");
			msh.getMessageControlID().setValue("ADT_A04-"+DateTimeUtil.getDateTimes());
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			PID pid = oml_O21.getPATIENT().getPID();
			pid.getSetIDPID().setValue("1");
			pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(lismessage.getCustom().getExam_num());
			pid.getPid3_PatientIdentifierList(0).getIdentifierTypeCode().setValue("PI");
			pid.getPid3_PatientIdentifierList(1).getCx1_IDNumber().setValue(lismessage.getCustom().getPersonidnum());
			pid.getPid3_PatientIdentifierList(1).getIdentifierTypeCode().setValue("PN");
			
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getSurname().setValue(lismessage.getCustom().getName());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(lismessage.getCustom().getName());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			pid.getPid7_DateTimeOfBirth().getTime().setValue(lismessage.getCustom().getBirthtime().replaceAll("-", ""));
			if (lismessage.getCustom().getSexname().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");
			} else if (lismessage.getCustom().getSexname().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			pid.getPid11_PatientAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(lismessage.getCustom().getAddress());
			pid.getPid11_PatientAddress(0).getAddressType().setValue("H");
			pid.getPid13_PhoneNumberHome(0).getTelephoneNumber().setValue(lismessage.getCustom().getTel());
			if(lismessage.getCustom().getMeritalcode().equals("已婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("M");
			}else if(lismessage.getCustom().getMeritalcode().equals("未婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}else {
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("B");
			}
			
			Doctor doctor = lismessage.getDoctor();
			
			PV1 pv1 = oml_O21.getPATIENT().getPATIENT_VISIT().getPV1();
			pv1.getPv12_PatientClass().setValue("T");
			
			pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());//科室id
			pv1.getPv13_AssignedPatientLocation().getPl2_Room().setValue("");//病区ID
			pv1.getPv13_AssignedPatientLocation().getPl3_Bed().setValue("");//床号
			pv1.getPv17_AttendingDoctor(0).getIDNumber().setValue(doctor.getDoctorCode());
			pv1.getPv17_AttendingDoctor(0).getFamilyName().getSurname().setValue(doctor.getDoctorName());
			pv1.getPv119_VisitNumber().getCx1_IDNumber().setValue(lismessage.getCustom().getExam_num());
			pv1.getPv142_PendingLocation().getPointOfCare().setValue(doctor.getDept_code());
			pv1.getPv142_PendingLocation().getRoom().setValue(doctor.getDept_name());
			pv1.getPv144_AdmitDateTime().getTime().setValue(DateTimeUtil.getDateTimes());
			
			
			ORC orc = oml_O21.getORDER(0).getORC();
			orc.getOrderControl().setValue("NW");
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(comps.getReq_no());// 申请单号
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());//开申请单的医生信息——id
			orc.getOrc12_OrderingProvider(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctor.getDoctorName());   //开申请单的医生信息——姓名
			orc.getOrc13_EntererSLocation().getPl1_PointOfCare().setValue(doctor.getDept_code());
			orc.getOrc13_EntererSLocation().getPl2_Room().setValue(doctor.getDept_name());
			orc.getOrc15_OrderEffectiveDateTime().getTime().setValue(doctor.getTime());
			
			List<LisComponent> component = comps.getItemList();
			for (int i = 0; i < component.size(); i++) {
				
				OBR obr = oml_O21.getORDERAll().get(0).getOBSERVATION_REQUEST().getOBR();
				obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(component.get(i).getExtension());// 申请单号
				obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(component.get(i).getItemCode());// 项目代码
				obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(component.get(i).getItemName());// 项目名称
				obr.getObr15_SpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(component.get(i).getSpecimenNaturalname());  //检查部位代码
//					obr.getObr15_SpecimenSource().getSpecimenSourceNameOrCode().getText().setValue("部位");//检查部位名称
				obr.getObr16_OrderingProvider(0).getIDNumber().setValue(doctor.getDoctorCode());
				obr.getObr16_OrderingProvider(0).getFamilyName().getSurname().setValue(doctor.getDoctorName());
				obr.getObr23_ChargeToPractice().getMonetaryAmount().getQuantity().setValue(String.valueOf(component.get(i).getItemamount()));
				obr.getObr24_DiagnosticServSectID().setValue(comps.getYzCode());
			}
			
			
			//Map<String,String> depM = getDepInterNum(liscom.getChargingItemid(), logname);
			
			//NTE nte = oml_O21.getORDER(0).getORDER_DETAIL().getNTE();
			NTE nte = oml_O21.getNTE();
			nte.getNte1_SetIDNTE().setValue("");
			nte.getNte2_SourceOfComment().setValue("P");
			/*nte.getNte3_Comment(0).setValue(depM.get("dep_inter_num"));
			nte.getNte4_CommentType().getIdentifier().setValue(depM.get("dep_inter_num"));
			nte.getNte4_CommentType().getText().setValue(depM.get("dep_inter_type"));*/
			
			DG1 dg1 = oml_O21.getORDERAll().get(0).getOBSERVATION_REQUEST().getDG1();
			dg1.getDg11_SetIDDG1().setValue(String.valueOf(1));
			/*dg1.getDg13_DiagnosisCodeDG1().getIdentifier().setValue("");
			dg1.getDg13_DiagnosisCodeDG1().getText().setValue("");*/
			dg1.getDg16_DiagnosisType().setValue("F");
			//dg1.getDg118_ConfidentialIndicator().setValue("");
			
			Parser parser = new PipeParser();
			str = parser.encode(oml_O21);
			TranLogTxt.liswriteEror_to_txt(logname,str);
			sb.append(str);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	public static String DelLis(LisComponents comps, ExamInfoUserDTO eu, String logname) {
		
		String str="";
		StringBuilder sb = new StringBuilder();
		ORM_O01 orm = new ORM_O01();
		
		try {
			MSH msh = orm.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("ZLHIS");
			msh.getReceivingApplication().getHd1_NamespaceID().setValue("MIS");
			msh.getReceivingApplication().getHd2_UniversalID().setValue("2008");
			msh.getDateTimeOfMessage().getTime().setValue(DateTimeUtil.getDateTimes());
			msh.getMessageType().getMsg1_MessageCode().setValue("ADT");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("A04");
			msh.getMessageControlID().setValue("ADT_A04-"+DateTimeUtil.getDateTimes());
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			
			PID pid = orm.getPATIENT().getPID();
			pid.getSetIDPID().setValue("1");
			pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(eu.getExam_num());
			pid.getPatientIdentifierList(0).getCx1_IDNumber().setValue(eu.getId_num());
			//年龄
			
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().setValue(eu.getUser_name());
			String name_pingyin = PinyinUtil.getTheAllMathedPinYin(eu.getUser_name());
			pid.getPid5_PatientName(0).getGivenName().setValue(name_pingyin==null?"":name_pingyin);
			pid.getPid7_DateTimeOfBirth().getTime().setValue(eu.getBirthday().replaceAll("-", ""));
			if (eu.getSex().equals("男")) {
				pid.getPid8_AdministrativeSex().setValue("M");
			} else if (eu.getSex().equals("女")) {
				pid.getPid8_AdministrativeSex().setValue("F");
			} else {
				pid.getPid8_AdministrativeSex().setValue("U");
			}
			pid.getPid11_PatientAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(eu.getAddress());
			pid.getPid13_PhoneNumberHome(0).getTelephoneNumber().setValue(eu.getPhone());
			pid.getPid16_MaritalStatus().getCe1_Identifier().setValue(eu.getIs_marriage());
			
			
			ORC orc = orm.getORDER(0).getORC();
			orc.getOrderControl().setValue("NW");//NW"表示生成新医嘱
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(comps.getReq_no());//医嘱编号（申请单号）
			orc.getFillerOrderNumber().getEntityIdentifier().setValue("");//执行系统医嘱编号
			orc.getOrderStatus().setValue("IP");
			orc.getDateTimeOfTransaction().getTime().setValue(DateTimeUtil.getDateTimes());//开单时间
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue("");//医嘱提供者
			orc.getOrderEffectiveDateTime().getTime().setValue("");//医嘱生效时间
			
			//开立医嘱的机构名称
			orc.getOrderingFacilityName(0).getOrganizationName().setValue("");//名称
			orc.getOrderingFacilityName(0).getOrganizationNameTypeCode();//编码
			Parser parser = new PipeParser();
			str = parser.encode(orm);
			TranLogTxt.liswriteEror_to_txt(logname,str);
			sb.append(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return sb.toString();
	}
	public static ResultHeader getResCodeCQ(String res, String logname) {
		ORL_O22 fromJson = new Gson().fromJson(res, ORL_O22.class);
		ResultHeader rhone = new ResultHeader();
		if (fromJson != null && fromJson.toString().length() > 10) {
			MSH msh = fromJson.getMSH();
			MSA msa = fromJson.getMSA();
			String code = msa.getAcknowledgmentCode().toString();
			if (code.equals("AA")) {
				rhone.setTypeCode("AA");
			} else {
				rhone.setTypeCode("AE");
			}
		} else {
			rhone.setTypeCode("AE");
		}
		
		return rhone;
	}

	public static ResLisStatusBeanHK resLisStatus(String strbody, String logname) {
		ResLisStatusBeanHK rh = new ResLisStatusBeanHK();
		ORM_O01 orm = new Gson().fromJson(strbody, ORM_O01.class);
	
		ORC orc = orm.getORDER(0).getORC();
		String Lis_req_num = orc.getOrc2_PlacerOrderNumber().getEi3_UniversalID().getValue();
		String lisStatus = orc.getOrderStatus().getValue();
		PID pid = orm.getPATIENT().getPID();
		
		String patient_id = pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().getValue();
		
		if(Lis_req_num!=null && lisStatus!=null && Lis_req_num.equals("")&& lisStatus.equals("") ){
			rh.setCode("AA");
			rh.setStatus(lisStatus);
			rh.setReqno(Lis_req_num);
			rh.setPersionid(patient_id);
			rh.setText("成功");
		}else{
			rh.setCode("AE");
			rh.setText("失败");
			
		}
		return rh;
	}

	public static RetLisCustome resLisMessage(String xmlmessage, String logName) {
		
		RetLisCustome rc = new RetLisCustome();
		
		try {
			OUL_R21 oul = new Gson().fromJson(xmlmessage, OUL_R21.class);
			
			PID pid  = oul.getPATIENT().getPID();
			String patientID = pid.getPatientID().toString();
			String exam_num = pid.getPid3_PatientIdentifierList(1).getCx1_IDNumber().getValue();
			//
			
			
			ORC orc = oul.getORDER_OBSERVATION(0).getORC();
			//申请单号
			String pacs_req_num = orc.getOrc2_PlacerOrderNumber().getEi3_UniversalID().getValue();
			OBR obr = oul.getORDER_OBSERVATION(0).getOBR();
			
			
			
			
			
			//审核时间
		
			List<OUL_R21_ORDER_OBSERVATION> list = oul.getORDER_OBSERVATIONAll();
			
			List<RetLisChargeItem> listRetLisChargeItem = new ArrayList<RetLisChargeItem>();
			RetLisChargeItem ChargeItem = new RetLisChargeItem();
			List<RetLisItem> listRetLisItem =new ArrayList<RetLisItem>();
			
			
			rc.setPatient_id(patientID);
			rc.setExam_num(exam_num);
			rc.setSample_barcode(pacs_req_num);
			
			
			for (int i = 0; i < list.size(); i++) {
				OBX obx = list.get(i).getOBSERVATION().getOBX();
				String type = obx.getObx2_ValueType().getValue();
				
				RetLisItem retLisItem = new RetLisItem();
				//审核人  
				XCN responsibleObserver = obx.getResponsibleObserver(0);
				String doctor_name_sh = responsibleObserver.toString();
				
				//审核时间
				String  doctor_time_sh = obx.getDateTimeOfTheAnalysis().getTime().toString();
				
				
				//编码
				//Observation Identifier
				String item_id = obx.getObservationIdentifier().toString();
				retLisItem.setItem_id(item_id);
				/*private String item_id;//检查项目编码
				
				private String item_name;//检查项目编码

				private String values;//检查项目值
				
				private String values_dw;//值单位

			    private String value_fw;//参考范围
			    private String value_ycbz;//异常标志
			    */
			    
				//小项名称
				//Observation Sub-Id
				String  item_name= obx.getObservationSubID().toString();
				retLisItem.setItem_name(item_name);
				
				//小项结果
				//Observation Value
				String values = obx.getObservationValue().toString();
				retLisItem.setValues(values);
				
				//结果值单位
				//Units
				String values_dw = obx.getUnits().toString();
				retLisItem.setValues_dw(values_dw);
				
				//参考值
				//References Range
				String value_fw = obx.getReferencesRange().toString();
				retLisItem.setValue_fw(value_fw);
				//高低标识
				//Abnormal Flags
				 String value_ycbz = obx.getAbnormalFlags(0).toString();
				 retLisItem.setValue_ycbz(value_ycbz);
				// 状态  报告状态，F-审核报告 F
				//Observation Result Status
				 listRetLisItem.add(retLisItem);
				
			}
			ChargeItem.setListRetLisItem(listRetLisItem);
			listRetLisChargeItem.add(ChargeItem);
			rc.setListRetLisChargeItem(listRetLisChargeItem);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return rc;
	}
}
