package com.hjw.webService.client.bjxy.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.Fees;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.Bean.Person;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.wst.DTO.BatchDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import ca.uhn.hl7v2.model.v251.group.DFT_P03_COMMON_ORDER;
import ca.uhn.hl7v2.model.v251.group.DFT_P03_FINANCIAL;
import ca.uhn.hl7v2.model.v251.group.DFT_P03_ORDER;
import ca.uhn.hl7v2.model.v251.group.OMG_O19_ORDER;
import ca.uhn.hl7v2.model.v251.group.OML_O21_ORDER;
import ca.uhn.hl7v2.model.v251.group.OML_O21_SPECIMEN;
import ca.uhn.hl7v2.model.v251.message.ADT_A05;
import ca.uhn.hl7v2.model.v251.message.DFT_P03;
import ca.uhn.hl7v2.model.v251.message.OMG_O19;
import ca.uhn.hl7v2.model.v251.message.OML_O21;
import ca.uhn.hl7v2.model.v251.segment.DG1;
import ca.uhn.hl7v2.model.v251.segment.EVN;
import ca.uhn.hl7v2.model.v251.segment.FT1;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.model.v251.segment.PV1;
import ca.uhn.hl7v2.model.v251.segment.SPM;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class BJXYhl7 {
	private static Custom getCustom(String id,String logName){
		Custom custom =new Custom();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql="select c.id_num as id_num,e.patient_id as patient_id, c.user_name as name,"
				+ "CONVERT(varchar(100),c.birthday,20) as birthday,c.sex,c.address,c.phone,e.is_marriage,c.nation from exam_info e "
				+ "left join  customer_info c on c.id=e.customer_id where e.exam_num='"+id+"' ";
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
				custom.setID_NO(rs.getString("id_num"));
				custom.setPATIENT_ID(rs.getString("patient_id"));
				custom.setNAME(rs.getString("name"));
				custom.setDATE_OF_BIRTH(rs.getString("birthday").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
				custom.setSEX(rs.getString("sex"));
				custom.setMAILING_ADDRESS(rs.getString("address"));
				custom.setPHONE_NUMBER_HOME(rs.getString("phone"));
				custom.setMARITAL_STATUS(rs.getString("is_marriage"));
				custom.setNATION(rs.getString("nation"));
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
		return custom;
	}
	
	private static String getCustomArchNum(String id,String logName){
		String str="";
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql="select c.arch_num as arch_num "
				+ " from exam_info e "
				+ "left join  customer_info c on c.id=e.customer_id where e.exam_num='"+id+"' ";
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
				str=rs.getString("arch_num");
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
		return str;
	}
	public static String dftP03hl7(PacsMessageBody fee,String messageType,String orderControl,int i,int j,String logName){
		 String nowtime=DateTimeUtil.getDateTimes();
		 String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		 Random random = new Random();
	  String messageId1=String.valueOf(random.nextInt(99));
		String str="";
		StringBuilder sb=new StringBuilder();
		try {
			DFT_P03 dft=new DFT_P03();
			
			MSH msh=dft.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PES");
			msh.getSendingFacility().getNamespaceID().setValue("10");
			msh.getReceivingApplication().getNamespaceID().setValue("ESB");
			msh.getReceivingFacility().getNamespaceID().setValue("10");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMessageCode().setValue("DFT");
			msh.getMessageType().getTriggerEvent().setValue(messageType);
			msh.getMessageControlID().setValue(messageId+messageId1);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.5.1");
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("UNICODE UTF-8");
			Doctor doctor = fee.getDoctor();
			EVN evn=dft.getEVN();
			evn.getRecordedDateTime().getTime().setValue(nowtime);
			evn.getOperatorID(0).getIDNumber().setValue(doctor.getDoctorCode());
			evn.getOperatorID(0).getFamilyName().getFn1_Surname().setValue(doctor.getDoctorName());
			
			
			Person custom = fee.getCustom();
			PID pid = dft.getPID();
			pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getPersonid());
			pid.getPid3_PatientIdentifierList(0).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(0).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(0).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(0).getCx5_IdentifierTypeCode().setValue("21");
			pid.getPid3_PatientIdentifierList(1).getCx1_IDNumber().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx5_IdentifierTypeCode().setValue("51");
			if(custom.getPersonidnum()!=null&&custom.getPersonidnum().length()>0){
				pid.getPid3_PatientIdentifierList(2).getCx1_IDNumber().setValue(custom.getPersonidnum());
			}else{
				pid.getPid3_PatientIdentifierList(2).getCx1_IDNumber().setValue(custom.getArch_num());
			}
			pid.getPid3_PatientIdentifierList(2).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(2).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(2).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(2).getCx5_IdentifierTypeCode().setValue("1");
			pid.getPid3_PatientIdentifierList(3).getCx1_IDNumber().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx5_IdentifierTypeCode().setValue("53");
			pid.getPid4_AlternatePatientIDPID(0).getCx1_IDNumber().setValue(custom.getPersonid());
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getSurname().setValue(custom.getName());
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getBirthtime().replaceAll("-", "").substring(0,8) + "000000");
			if (custom.getSexname().equals("男")) {
				pid.getAdministrativeSex().setValue("1");
			} else if (custom.getSexname().equals("女")) {
				pid.getAdministrativeSex().setValue("2");
			} else {
				pid.getAdministrativeSex().setValue("0");
			}
			pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("");

			
			PV1 pv1 = dft.getPV1();
			pv1.getPv11_SetIDPV1().setValue("1");
			pv1.getPv12_PatientClass().setValue("T");
			pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue("体检科");
			pv1.getPv13_AssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().setValue("10");
			pv1.getPv17_AttendingDoctor(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			pv1.getPv17_AttendingDoctor(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctor.getDoctorName());
			pv1.getPv118_PatientType().setValue("3");
			pv1.getPv119_VisitNumber().getCx2_CheckDigit().setValue("1");
			pv1.getPv144_AdmitDateTime().getTime().setValue(nowtime);

			List<PacsComponents> list = fee.getComponents();
			ORC orc = dft.getCOMMON_ORDER(0).getORC();
			orc.getOrderControl().setValue(orderControl);
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(list.get(i).getReq_no());
			orc.getPlacerOrderNumber().getEi2_NamespaceID().setValue("PES");
			orc.getPlacerOrderNumber().getEi3_UniversalID().setValue(list.get(i).getReq_no());// 申请单号
			orc.getPlacerOrderNumber().getEi4_UniversalIDType().setValue("PES");// 开立系统
			orc.getOrc9_DateTimeOfTransaction().getTime().setValue(nowtime);
			orc.getOrc10_EnteredBy(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			orc.getOrc10_EnteredBy(0).getXcn2_FamilyName().getSurname().setValue(doctor.getDoctorName());
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			orc.getOrc12_OrderingProvider(0).getXcn2_FamilyName().getSurname().setValue(doctor.getDoctorName());
			orc.getOrc13_EntererSLocation().getPl1_PointOfCare().setValue("1010901");
			orc.getOrc17_EnteringOrganization().getCe1_Identifier().setValue("10");
			orc.getOrc17_EnteringOrganization().getCe4_AlternateIdentifier().setValue("体检科");
			orc.getOrc29_OrderType().getCwe1_Identifier().setValue("01");
			orc.getOrc29_OrderType().getCwe2_Text().setValue("诊疗");

			OBR obr = dft.getCOMMON_ORDER(0).getORDER().getOBR();
			obr.getSetIDOBR().setValue("1");
			obr.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(list.get(i).getReq_no());
			obr.getPlacerOrderNumber().getEi2_NamespaceID().setValue("PES");
			obr.getPlacerOrderNumber().getEi3_UniversalID().setValue(list.get(i).getReq_no());// 申请单号
			obr.getPlacerOrderNumber().getEi4_UniversalIDType().setValue("PES");// 开立系统
			String strs=list.get(i).getPacsComponent().get(j).getPacs_num();
			String code=strs.substring(0,2);
			String classcode=strs.substring(2);
			obr.getUniversalServiceIdentifier().getCe1_Identifier()
					.setValue(classcode);// 项目编码
			obr.getUniversalServiceIdentifier().getCe2_Text()
					.setValue(list.get(i).getPacsComponent().get(j).getItemName());// 项目名称
			obr.getUniversalServiceIdentifier().getCe4_AlternateIdentifier().setValue(code);// 类型编码
			String classStrs=QuerySqlData.getPacsClassStrs(code,logName);
			obr.getUniversalServiceIdentifier().getCe5_AlternateText().setValue(classStrs);// 类型名称
			obr.getRequestedDateTime().getTime().setValue(nowtime);// 申请时间
			obr.getOrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());// 申请医生id
			obr.getOrderingProvider(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctor.getDoctorName());// 申请医生姓名
			obr.getObr31_ReasonForStudy(0).getCe2_Text().setValue("体检");
			//obr.getObr34_Technician(0).getNdl4_PointOfCare()
			//		.setValue(list.get(i).getPacsComponent().get(j).getServiceDeliveryLocation_code());
			String execUnit=QuerySqlData.getExecUnit(classcode, code,logName);
				execUnit=execUnit.substring(0,7);
			obr.getObr34_Technician(0).getNdl4_PointOfCare().setValue(execUnit);
			
				FT1 ft1=dft.getFINANCIAL(0).getFT1();
				ft1.getSetIDFT1().setValue("1");
				ft1.getTransactionDate().getDr1_RangeStartDateTime().getTime().setValue(nowtime);
				ft1.getFt16_TransactionType().setValue("CG");
				ft1.getTransactionCode().getCe1_Identifier().setValue(list.get(i).getReq_no());//交易编码
				String feestr=String.valueOf(fee.getComponents().get(i).getCosts());
				String feestr1=feestr.substring(0, feestr.indexOf("."));
				ft1.getFt112_TransactionAmountUnit().getCp1_Price().getMo1_Quantity().setValue(feestr1);//合计金额
				ft1.getPatientType().setValue("3");//患者类型
			Parser parser=new PipeParser();
			str=parser.encode(dft);
			TranLogTxt.liswriteEror_to_txt(logName,str);
			sb.append(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	/**
	 * lis申请
	 * @param lis
	 * @param messageType
	 * @param orderControl
	 * @param i
	 * @param j
	 * @param logName
	 * @return
	 */
	public static String omlO21hl7(LisMessageBody lis, String messageType, String orderControl, int i, int j,String logName) {
		 String nowtime=DateTimeUtil.getDateTimes();
		 String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		 Random random = new Random();
	  String messageId1=String.valueOf(random.nextInt(99));
		String str = "";
		StringBuilder sb = new StringBuilder();
		try {
			OML_O21 oml = new OML_O21();
			MSH msh = oml.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PES");
			msh.getSendingFacility().getNamespaceID().setValue("10");
			msh.getReceivingApplication().getNamespaceID().setValue("ESB");
			msh.getReceivingFacility().getNamespaceID().setValue("10");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMessageCode().setValue("OML");
			msh.getMessageType().getTriggerEvent().setValue(messageType);
			msh.getMessageType().getMessageStructure().setValue("OML_O21");
			msh.getMessageControlID().setValue(messageId + messageId1);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.5.1");
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("UNICODE UTF-8");

			Person custom = lis.getCustom();
			PID pid = oml.getPATIENT().getPID();
			
			pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getPersonid());
			pid.getPid3_PatientIdentifierList(0).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(0).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(0).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(0).getCx5_IdentifierTypeCode().setValue("21");
			pid.getPid3_PatientIdentifierList(1).getCx1_IDNumber().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx5_IdentifierTypeCode().setValue("51");
			if(custom.getPersonidnum()!=null&&custom.getPersonidnum().length()>0){
				pid.getPid3_PatientIdentifierList(2).getCx1_IDNumber().setValue(custom.getPersonidnum());
			}else{
				pid.getPid3_PatientIdentifierList(2).getCx1_IDNumber().setValue(custom.getArch_num());
			}
			pid.getPid3_PatientIdentifierList(2).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(2).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(2).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(2).getCx5_IdentifierTypeCode().setValue("1");
			
			pid.getPid3_PatientIdentifierList(3).getCx1_IDNumber().setValue(custom.getExam_num());
			pid.getPid3_PatientIdentifierList(3).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx5_IdentifierTypeCode().setValue("58");
			
			pid.getPid4_AlternatePatientIDPID(0).getCx1_IDNumber().setValue(custom.getPersonid());
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getSurname().setValue(custom.getName());
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getBirthtime().replaceAll("-", "").substring(0,8) + "000000");
			if (custom.getSexname().equals("男")) {
				pid.getAdministrativeSex().setValue("1");
			} else if (custom.getSexname().equals("女")) {
				pid.getAdministrativeSex().setValue("2");
			} else {
				pid.getAdministrativeSex().setValue("0");
			}
			pid.getPid11_PatientAddress(0).getXad1_StreetAddress().getSad1_StreetOrMailingAddress()
					.setValue(custom.getAddress());
			pid.getPid13_PhoneNumberHome(0).getXtn9_AnyText().setValue(custom.getTel());
			pid.getPid14_PhoneNumberBusiness(0).getXtn9_AnyText().setValue(custom.getTel());
			if(custom.getMeritalcode()==null||custom.getMeritalcode().equals("")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("9");
				pid.getPid16_MaritalStatus().getCe2_Text().setValue("其他");
			}else if(custom.getMeritalcode().equals("已婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("2");
				pid.getPid16_MaritalStatus().getCe2_Text().setValue("已婚");
			}else if(custom.getMeritalcode().equals("未婚")){
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("1");
				pid.getPid16_MaritalStatus().getCe2_Text().setValue("未婚");
			}else{
				pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("9");
				pid.getPid16_MaritalStatus().getCe2_Text().setValue("其他");
			}
			String birthYear = custom.getBirthtime().substring(0, 4);
			String nowyear = new SimpleDateFormat("yyyy").format(new Date());
			BigDecimal bd = new BigDecimal(birthYear);
			BigDecimal bd1 = new BigDecimal(nowyear);
			BigDecimal result = bd1.subtract(bd);
			pid.getPid17_Religion().getCe1_Identifier().setValue(String.valueOf(result));
			pid.getPid17_Religion().getCe2_Text().setValue("岁");
//			pid.getEthnicGroup(0).getCe1_Identifier().setValue("1");
//			pid.getEthnicGroup(0).getCe2_Text().setValue("汉族");
			pid.getCitizenship(0).getCe1_Identifier().setValue("CN");
			pid.getCitizenship(0).getCe2_Text().setValue("中国");
			Doctor doctor=lis.getDoctor();
			PV1 pv1 = oml.getPATIENT().getPATIENT_VISIT().getPV1();
			pv1.getPv11_SetIDPV1().setValue("1");
			pv1.getPv12_PatientClass().setValue("T");
			
			pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue("1010901");//科室ID 固定值
			pv1.getPv13_AssignedPatientLocation().getPl2_Room().setValue("");//病区ID
			pv1.getPv13_AssignedPatientLocation().getPl3_Bed().setValue("");//床号
			pv1.getPv13_AssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().setValue("10");
			pv1.getPv13_AssignedPatientLocation().getPl5_LocationStatus().setValue("");//病区名称
			pv1.getPv13_AssignedPatientLocation().getPl6_PersonLocationType().setValue("");
			pv1.getPv13_AssignedPatientLocation().getPl7_Building().setValue("");
			pv1.getPv13_AssignedPatientLocation().getPl8_Floor().setValue("");
			pv1.getPv13_AssignedPatientLocation().getPl9_LocationDescription().setValue("体检科");//
			
			pv1.getPv17_AttendingDoctor(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			
			pv1.getPv117_AdmittingDoctor(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			
			String doctorname=QuerySqlData.getUser_name(doctor.getDoctorCode(),logName);
			pv1.getPv17_AttendingDoctor(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctorname);
			pv1.getPv118_PatientType().setValue("03");
			
			pv1.getPv119_VisitNumber().getCx1_IDNumber().setValue(custom.getExam_num());
			pv1.getPv119_VisitNumber().getCx2_CheckDigit().setValue("1");
			pv1.getPv144_AdmitDateTime().getTime().setValue(nowtime);

			List<LisComponents> listLis = lis.getComponents();
			ORC orc = oml.getORDER(0).getORC();
			orc.getOrderControl().setValue(orderControl);
			String yizhuhao=QuerySqlData.getOrderNo(listLis.get(i).getItemList().get(j).getChargingItemid(), listLis.get(i).getReq_no(),logName);
			yizhuhao=QuerySqlData.getYiZhuHao(yizhuhao);
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue("TYZ"+"_"+yizhuhao);// 医嘱号，按照自己的规则生成
			orc.getPlacerOrderNumber().getEi2_NamespaceID().setValue("PES");
			orc.getPlacerOrderNumber().getEi3_UniversalID().setValue("TSQ"+"_"+yizhuhao);// 申请单号
			orc.getPlacerOrderNumber().getEi4_UniversalIDType().setValue("PES");// 开立系统
			orc.getFillerOrderNumber().getEi1_EntityIdentifier().setValue(listLis.get(i).getReq_no());
			orc.getFillerOrderNumber().getEi2_NamespaceID().setValue("LIS");
			orc.getOrc9_DateTimeOfTransaction().getTime().setValue(listLis.get(i).getItemList().get(j).getItemtime());//采样时间
			orc.getOrc15_OrderEffectiveDateTime().getTime().setValue(nowtime);
			orc.getEnteredBy(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			orc.getEnteredBy(0).getXcn2_FamilyName().getSurname().setValue(doctorname);
			orc.getOrc29_OrderType().getCwe1_Identifier().setValue("3");
			orc.getOrc29_OrderType().getCwe2_Text().setValue("检验");
			
			orc.getVerifiedBy(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			orc.getVerifiedBy(0).getXcn2_FamilyName().getSurname().setValue(doctorname);
			orc.getOrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			orc.getOrderingProvider(0).getXcn2_FamilyName().getSurname().setValue(doctorname);
			orc.getEnteredBy(0).getIDNumber().setValue(doctor.getDoctorCode());
			
			orc.getEntererSLocation().getPl1_PointOfCare().setValue("1010901");
			orc.getEnteringOrganization().getCe1_Identifier().setValue("10");
			orc.getEnteringOrganization().getCe2_Text().setValue("西苑医院-本院");
			OBR obr = oml.getORDER(0).getOBSERVATION_REQUEST().getOBR();
			obr.getObr1_SetIDOBR().setValue("1");
			obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue("TYZ"+"_"+yizhuhao);// 医嘱号，按照自己的规则生成
			obr.getObr2_PlacerOrderNumber().getEi2_NamespaceID().setValue("PES");
			obr.getObr2_PlacerOrderNumber().getEi3_UniversalID().setValue("TSQ"+"_"+yizhuhao);// 申请单号
			obr.getObr2_PlacerOrderNumber().getEi4_UniversalIDType().setValue("PES");// 开立系统
			obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(listLis.get(i).getItemList().get(j).getItemCode());// 项目代码
			obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(listLis.get(i).getItemList().get(j).getItemName());// 项目名称
			//obr.getEnteredBy(0).getIDNumber().setValue(doctor.getDoctorCode());
			
			String[] classStrs=QuerySqlData.getClassStrs(listLis.get(i).getItemList().get(j).getItemCode(),logName);
			obr.getObr4_UniversalServiceIdentifier().getCe4_AlternateIdentifier().setValue(classStrs[0]);// 项目类型编码
			obr.getObr4_UniversalServiceIdentifier().getCe5_AlternateText().setValue(classStrs[1]);// 项目类型
			obr.getObr6_RequestedDateTime().getTime().setValue(nowtime);// 申请时间
			
			
			String[] sample=QuerySqlData.getSample(listLis.get(i).getReq_no(),logName);
			obr.getObr10_CollectorIdentifier(0).getXcn1_IDNumber().setValue(sample[0]);
			obr.getObr10_CollectorIdentifier(0).getXcn2_FamilyName().getSurname().setValue(sample[4]);
			
			//obr.getCollectorIdentifier(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			//obr.getCollectorIdentifier(0).getXcn2_FamilyName().getSurname().setValue(doctorname);
			obr.getObr10_CollectorIdentifier(0).getXcn23_AssigningAgencyOrDepartment().getCwe1_Identifier()
					.setValue("1010901");
			obr.getObr10_CollectorIdentifier(0).getXcn23_AssigningAgencyOrDepartment().getCwe2_Text().setValue("体检科");
			obr.getObr14_SpecimenReceivedDateTime().getTs1_Time().setValue(nowtime);
			
			obr.getObr16_OrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			obr.getObr16_OrderingProvider(0).getXcn2_FamilyName().getSurname().setValue(doctorname);
			obr.getObr23_ChargeToPractice().getMoc1_MonetaryAmount().getMo1_Quantity()
					.setValue(String.valueOf(listLis.get(i).getItemList().get(j).getItemamount()));
			obr.getObr3_FillerOrderNumber().getEi1_EntityIdentifier().setValue("");
			obr.getObr3_FillerOrderNumber().getEi2_NamespaceID().setValue("PES");
			
			
			
			obr.getObr10_CollectorIdentifier(0).getXcn1_IDNumber().setValue("");
			obr.getObr10_CollectorIdentifier(0).getXcn2_FamilyName().getFn1_Surname().setValue("");
			obr.getObr10_CollectorIdentifier(0).getXcn23_AssigningAgencyOrDepartment().getText().setValue("");
			obr.getObr10_CollectorIdentifier(0).getXcn23_AssigningAgencyOrDepartment().getCwe1_Identifier().setValue("");
			obr.getObr10_CollectorIdentifier(0).getXcn23_AssigningAgencyOrDepartment().getText().setValue("");
			
			obr.getObr34_Technician(0).getNdl4_PointOfCare().setValue("1400100");
			obr.getObr34_Technician(0).getNdl5_Room().setValue("临床检验");
			
			DG1 dg1 = oml.getORDER(i).getOBSERVATION_REQUEST().getDG1(0);
			dg1.getDg11_SetIDDG1().setValue("1");
			dg1.getDg116_DiagnosingClinician(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			dg1.getDg116_DiagnosingClinician(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctorname);
			
			SPM spm = oml.getORDER(i).getOBSERVATION_REQUEST().getSPECIMEN(0).getSPM();
			TranLogTxt.liswriteEror_to_txt(logName,listLis.get(i).getReq_no());
			spm.getSpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()
					.setValue(listLis.get(i).getReq_no());
//			spm.getSpm4_SpecimenType().getCwe1_Identifier().setValue(classStrs[2]);
			String[] sampleTypeStrs=QuerySqlData.getSampleTypeStrs(listLis.get(i).getItemList().get(j).getItemCode(),logName);
			spm.getSpm4_SpecimenType().getCwe1_Identifier().setValue(sampleTypeStrs[1]);//直接传中文名称，不传代码
			spm.getSpecimenCollectionDateTime().getDr1_RangeStartDateTime().getTime().setValue(sample[1].substring(0, 19).replaceAll("-", "").replaceAll("-", "").replaceAll(" ", "").replaceAll(":",""));// 采集时间

			Parser parser = new PipeParser();
			str = parser.encode(oml);
			TranLogTxt.liswriteEror_to_txt(logName,str);
			sb.append(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String omgO19hl7(PacsMessageBody pacs, String messageType, String orderControl, int i, int j,String logName) {
		 String nowtime=DateTimeUtil.getDateTimes();
		 String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		 Random random = new Random();
	  String messageId1=String.valueOf(random.nextInt(99));
		String str = "";
		StringBuilder sb = new StringBuilder();

		try {
			OMG_O19 omg = new OMG_O19();

			MSH msh = omg.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PES");
			msh.getSendingFacility().getNamespaceID().setValue("10");
			msh.getReceivingApplication().getNamespaceID().setValue("ESB");
			msh.getReceivingFacility().getNamespaceID().setValue("10");
			msh.getDateTimeOfMessage().getTime().setValue(nowtime);
			msh.getMessageType().getMessageCode().setValue("OMG");
			msh.getMessageType().getTriggerEvent().setValue(messageType);
			msh.getMessageType().getMessageStructure().setValue("OMG_O19");
			msh.getMessageControlID().setValue(messageId + messageId1);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.5.1");
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("UNICODE UTF-8");

			Person custom = pacs.getCustom();
			PID pid = omg.getPATIENT().getPID();
			pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getPersonid());
			pid.getPid3_PatientIdentifierList(0).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(0).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(0).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(0).getCx5_IdentifierTypeCode().setValue("21");
			pid.getPid3_PatientIdentifierList(1).getCx1_IDNumber().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(1).getCx5_IdentifierTypeCode().setValue("51");
			if(custom.getPersonidnum()!=null&&custom.getPersonidnum().length()>0){
				pid.getPid3_PatientIdentifierList(2).getCx1_IDNumber().setValue(custom.getPersonidnum());
			}else{
				pid.getPid3_PatientIdentifierList(2).getCx1_IDNumber().setValue(custom.getArch_num());
			}
			pid.getPid3_PatientIdentifierList(2).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(2).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(2).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(2).getCx5_IdentifierTypeCode().setValue("1");
			pid.getPid3_PatientIdentifierList(3).getCx1_IDNumber().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx2_CheckDigit().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx3_CheckDigitScheme().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("");
			pid.getPid3_PatientIdentifierList(3).getCx5_IdentifierTypeCode().setValue("53");
			pid.getPid4_AlternatePatientIDPID(0).getCx1_IDNumber().setValue(custom.getPersonid());
			pid.getPid5_PatientName(0).getXpn1_FamilyName().getSurname().setValue(custom.getName());
			pid.getPid7_DateTimeOfBirth().getTime().setValue(custom.getBirthtime().replaceAll("-", "").substring(0,8) + "000000");
			if (custom.getSexname().equals("男")) {
				pid.getAdministrativeSex().setValue("1");
			} else if (custom.getSexname().equals("女")) {
				pid.getAdministrativeSex().setValue("2");
			} else {
				pid.getAdministrativeSex().setValue("0");
			}
			pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("");
			Doctor doctor = pacs.getDoctor();
			PV1 pv1 = omg.getPATIENT().getPATIENT_VISIT().getPV1();
			pv1.getPv11_SetIDPV1().setValue("1");
			pv1.getPv12_PatientClass().setValue("T");
			pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue("体检科");
			pv1.getPv13_AssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().setValue("10");
			pv1.getPv17_AttendingDoctor(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			pv1.getPv17_AttendingDoctor(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctor.getDoctorName());
			pv1.getPv118_PatientType().setValue("3");
			pv1.getPv119_VisitNumber().getCx2_CheckDigit().setValue("1");
			pv1.getPv144_AdmitDateTime().getTime().setValue(nowtime);

			List<PacsComponents> list = pacs.getComponents();
			ORC orc = omg.getORDER(0).getORC();
			orc.getOrderControl().setValue(orderControl);
			orc.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(list.get(i).getReq_no());
			orc.getPlacerOrderNumber().getEi2_NamespaceID().setValue("PES");
			orc.getPlacerOrderNumber().getEi3_UniversalID().setValue(list.get(i).getReq_no());// 申请单号
			orc.getPlacerOrderNumber().getEi4_UniversalIDType().setValue("PES");// 开立系统
			orc.getOrc9_DateTimeOfTransaction().getTime().setValue(nowtime);
			orc.getOrc10_EnteredBy(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			orc.getOrc10_EnteredBy(0).getXcn2_FamilyName().getSurname().setValue(doctor.getDoctorName());
			orc.getOrc12_OrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());
			orc.getOrc12_OrderingProvider(0).getXcn2_FamilyName().getSurname().setValue(doctor.getDoctorName());
			orc.getOrc13_EntererSLocation().getPl1_PointOfCare().setValue("1010901");
			orc.getOrc17_EnteringOrganization().getCe1_Identifier().setValue("10");
			orc.getOrc17_EnteringOrganization().getCe4_AlternateIdentifier().setValue("体检科");
			orc.getOrc29_OrderType().getCwe1_Identifier().setValue("03");
			orc.getOrc29_OrderType().getCwe2_Text().setValue("检验");

			OBR obr = omg.getORDER(0).getOBR();
			obr.getSetIDOBR().setValue("1");
			obr.getPlacerOrderNumber().getEi1_EntityIdentifier().setValue(list.get(i).getReq_no());
			obr.getPlacerOrderNumber().getEi2_NamespaceID().setValue("PES");
			obr.getPlacerOrderNumber().getEi3_UniversalID().setValue(list.get(i).getReq_no());// 申请单号
			obr.getPlacerOrderNumber().getEi4_UniversalIDType().setValue("PES");// 开立系统
			String strs=list.get(i).getPacsComponent().get(j).getPacs_num();
			String code=strs.substring(0,2);
			String classcode=strs.substring(2);
			obr.getUniversalServiceIdentifier().getCe1_Identifier()
					.setValue(classcode);// 项目编码
			obr.getUniversalServiceIdentifier().getCe2_Text()
					.setValue(list.get(i).getPacsComponent().get(j).getItemName());// 项目名称
			obr.getUniversalServiceIdentifier().getCe4_AlternateIdentifier().setValue(code);// 类型编码
			String classStrs=QuerySqlData.getPacsClassStrs(code,logName);
			obr.getObr4_UniversalServiceIdentifier().getCe5_AlternateText().setValue(classStrs);// 类型名称
			obr.getObr6_RequestedDateTime().getTime().setValue(nowtime);// 申请时间
			obr.getObr16_OrderingProvider(0).getXcn1_IDNumber().setValue(doctor.getDoctorCode());// 申请医生id
			obr.getObr16_OrderingProvider(0).getXcn2_FamilyName().getFn1_Surname().setValue(doctor.getDoctorName());// 申请医生姓名
			obr.getObr31_ReasonForStudy(0).getCe2_Text().setValue("体检科");
			//obr.getObr34_Technician(0).getNdl4_PointOfCare()
			//		.setValue(list.get(i).getPacsComponent().get(j).getServiceDeliveryLocation_code());
			String execUnit=QuerySqlData.getExecUnit(classcode, code,logName);
			
			execUnit=execUnit.substring(0, 7);

			obr.getObr34_Technician(0).getNdl4_PointOfCare().setValue(execUnit);
			Parser parser = new PipeParser();
			str = parser.encode(omg);
			TranLogTxt.liswriteEror_to_txt(logName,str);
			sb.append(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static String adtA28hl7(Custom custom,String messageType,String logName){
		 String nowtime=DateTimeUtil.getDateTimes();
		 String messageId=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		 Random random = new Random();
	  String messageId1=String.valueOf(random.nextInt(99));
		String str="";
		StringBuilder sb=new StringBuilder();
		try {
		ADT_A05 adt=new ADT_A05();
		
		MSH msh=adt.getMSH();
		msh.getFieldSeparator().setValue("|");
		msh.getEncodingCharacters().setValue("^~\\&");
		msh.getSendingApplication().getNamespaceID().setValue("PES");
		msh.getSendingFacility().getNamespaceID().setValue("10");
		msh.getReceivingApplication().getNamespaceID().setValue("ESB");
		msh.getReceivingFacility().getNamespaceID().setValue("10");
		msh.getDateTimeOfMessage().getTime().setValue(nowtime);
		msh.getMessageType().getMessageCode().setValue("ADT");
		msh.getMessageType().getTriggerEvent().setValue(messageType);
		msh.getMessageControlID().setValue(messageId+messageId1);
		msh.getProcessingID().getProcessingID().setValue("P");
		msh.getVersionID().getVersionID().setValue("2.5.1");
		msh.getAcceptAcknowledgmentType().setValue("AL");
		msh.getApplicationAcknowledgmentType().setValue("AL");
		msh.getCountryCode().setValue("CHN");
		msh.getCharacterSet(0).setValue("UNICODE UTF-8");
		
		EVN evn=adt.getEVN();
		evn.getEvn2_RecordedDateTime().getTime().setValue(nowtime);
		evn.getEvn5_OperatorID(0).getXcn1_IDNumber().setValue(custom.getOPERATOR());
		String operator_name=QuerySqlData.getUser_name(custom.getOPERATOR(),logName);
		evn.getEvn5_OperatorID(0).getXcn2_FamilyName().getSurname().setValue(operator_name);
		
		PID pid=adt.getPID();
		if(custom.getID_NO()!=null&&custom.getID_NO().length()>0){
			pid.getPatientIdentifierList(0).getCx1_IDNumber().setValue(custom.getID_NO());
		}else{
			pid.getPatientIdentifierList(0).getCx1_IDNumber().setValue(getCustomArchNum(custom.getEXAM_NUM(),logName));
		}
		Custom c=new Custom();
		c=getCustom(custom.getEXAM_NUM(),logName);
		pid.getAlternatePatientIDPID(0).getCx1_IDNumber().setValue(c.getPATIENT_ID());
		pid.getPatientName(0).getFamilyName().getFn1_Surname().setValue(custom.getNAME());
		pid.getDateTimeOfBirth().getTs1_Time().setValue(custom.getDATE_OF_BIRTH().replaceAll("-", "").substring(0,8)+"000000");
		if(custom.getSEX().equals("男")){
			pid.getAdministrativeSex().setValue("1");
		}else if(custom.getSEX().equals("女")){
			pid.getAdministrativeSex().setValue("2");
		}else{
			pid.getAdministrativeSex().setValue("0");
		}
		String[] address=QuerySqlData.getAddresss(custom.getEXAM_NUM(),logName);
		if(address[0].equals("")||address[0]==null){
			pid.getPatientAddress(0).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue("未知");	
		}else{
			pid.getPatientAddress(0).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(address[0]);
		}
		if(address[1].equals("")||address[1]==null){
			pid.getPhoneNumberHome(0).getXtn9_AnyText().setValue("11111111111");
			pid.getPhoneNumberBusiness(0).getXtn9_AnyText().setValue("11111111111");
		}else{
			pid.getPhoneNumberHome(0).getXtn9_AnyText().setValue(address[1]);
			pid.getPhoneNumberBusiness(0).getXtn9_AnyText().setValue(address[1]);
		}
		if(custom.getMARITAL_STATUS()==null||custom.getMARITAL_STATUS().equals("")){
			pid.getMaritalStatus().getCe1_Identifier().setValue("9");
		}else if(custom.getMARITAL_STATUS().equals("已婚")){
			pid.getMaritalStatus().getCe1_Identifier().setValue("2");
		}else if(custom.getMARITAL_STATUS().equals("未婚")){
			pid.getMaritalStatus().getCe1_Identifier().setValue("1");
		}else{
			pid.getMaritalStatus().getCe1_Identifier().setValue("9");
		}
		pid.getMaritalStatus().getCe2_Text().setValue(custom.getMARITAL_STATUS());
		String birthYear=custom.getDATE_OF_BIRTH().substring(0,4);
		String nowyear=new SimpleDateFormat("yyyy").format(new Date());
		BigDecimal bd=new BigDecimal(birthYear);
		BigDecimal bd1=new BigDecimal(nowyear);
		BigDecimal result=bd1.subtract(bd);
		pid.getPid17_Religion().getCe1_Identifier().setValue(String.valueOf(result));
		pid.getPid17_Religion().getCe2_Text().setValue("岁");
		
		pid.getEthnicGroup(0).getCe1_Identifier().setValue("1");
		pid.getEthnicGroup(0).getCe2_Text().setValue("汉族");
		pid.getCitizenship(0).getCe1_Identifier().setValue("CN");
		pid.getCitizenship(0).getCe2_Text().setValue("中国");
		
		PV1 pv1=adt.getPV1();
		pv1.getPv11_SetIDPV1().setValue("1");
		pv1.getPv12_PatientClass().setValue("T");
		pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue("体检科");
		pv1.getPv13_AssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().setValue("10");
		pv1.getPv17_AttendingDoctor(0).getXcn1_IDNumber().setValue(custom.getOPERATOR());
		pv1.getPv17_AttendingDoctor(0).getXcn2_FamilyName().getFn1_Surname().setValue(operator_name);
		pv1.getPv118_PatientType().setValue("3");
		pv1.getPv119_VisitNumber().getIDNumber().setValue("1");
		pv1.getPv144_AdmitDateTime().getTime().setValue(nowtime);
		 
		Parser parser=new PipeParser();
		str=parser.encode(adt);
		TranLogTxt.liswriteEror_to_txt(logName,str);
		sb.append(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
