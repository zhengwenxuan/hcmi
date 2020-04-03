package com.hjw.webService.client.yichang;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.yichang.bean.cdr.client.createDoc.RegistPatientCreateDocInfo;
import com.hjw.webService.client.yichang.bean.cdr.client.header.PRPA_IN000001UV03;
import com.hjw.webService.client.yichang.bean.cdr.client.list.PatientAddress;
import com.hjw.webService.client.yichang.bean.cdr.client.list.PatientMainList;
import com.hjw.webService.client.yichang.bean.cdr.client.registration.RegistPatientRegistrationInformation;
import com.hjw.webService.client.yichang.bean.cdr.client.ret.DataSourceCDR;
import com.hjw.webService.client.yichang.bean.cdr.client.ret.DataSourceZSY;
import com.hjw.webService.client.yichang.gencode.SERVDelegate;
import com.hjw.webService.client.yichang.gencode.SERVService;
import com.hjw.webService.client.yichang.gencode.SERVServiceLocator;
import com.hjw.webService.client.yichang.gencode2.EMPI_Service;
import com.hjw.webService.client.yichang.gencode2.EMPI_ServiceLocator;
import com.hjw.webService.client.yichang.gencode2.EmpiWebService;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CUSTOMSendMessageYC {

	private Custom custom=new Custom();
	private ExamInfoUserDTO ei=new ExamInfoUserDTO();
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	
	public CUSTOMSendMessageYC(Custom custom){
		this.custom=custom;
		this.ei = configService.getExamInfoForNum(custom.getEXAM_NUM());
	}

	public ResultBody getMessage(String url,String logname) {
		String xml = JaxbUtil.convertToXml(this.custom, true);			
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		ResultBody rb = new ResultBody();
		
		String sexcode = "";
		if("男".equals(custom.getSEX()) || "男性".equals(custom.getSEX())) {
			sexcode = "1";
			custom.setSEX("男性");
		} else if("女".equals(custom.getSEX()) || "女性".equals(custom.getSEX())) {
			sexcode = "2";
			custom.setSEX("女性");
		} else {
			sexcode = "0";
			custom.setSEX("未知的性别");
		}
		String maritalStatusCode = "";
		if("已婚".equals(custom.getMARITAL_STATUS()) || "是".equals(custom.getMARITAL_STATUS())) {
			maritalStatusCode = "20";
			custom.setMARITAL_STATUS("已婚");
		} else if("未婚".equals(custom.getMARITAL_STATUS()) || "否".equals(custom.getMARITAL_STATUS())) {
			maritalStatusCode = "10";
			custom.setMARITAL_STATUS("未婚");
		} else {
			maritalStatusCode = "90";
			custom.setMARITAL_STATUS("未说明的婚姻状况");
		}
		
		//0.患者主索引
		try {
			String patientXML = ""//<?xml version=\"1.0\" encoding=\"UTF-8\"?>
					+"<PATIENTINFO> "
					+"	 <PATIENT>"
					+"		 <NAME>"+ei.getUser_name()+"</NAME>"
					+"		 <IDNO>"+ei.getId_num()+"</IDNO>"
					+"		 <SEX>"+sexcode+"</SEX>"
					+"		 <BIRTHDAY>"+ei.getBirthday()+"</BIRTHDAY>"
					+"		 <CNY></CNY>"
					+"		 <CNYNAME></CNYNAME>"
					+"		 <ACT></ACT>"
					+"		 <ADDR></ADDR>"
					+"		 <ZPCODE></ZPCODE>"
					+"		 <ABOBLD></ABOBLD> "
					+"		 <RHBLD></RHBLD>"
					+"		 <NTN></NTN>"
					+"		 <BCP></BCP>"
					+"		 <CTOR></CTOR>"
					+"		 <CTORTEL></CTORTEL>"
					+"		 <CTORLTN></CTORLTN>"
					+"		 <HMTEL></HMTEL>"
					+"		 <MOBILE></MOBILE>"
					+"		 <EML></EML>"
					+"		 <CPY></CPY>"
					+"		 <CPYTEL></CPYTEL>"
					+"		 <MRG>"+maritalStatusCode+"</MRG>"
					+"		 <PFSN></PFSN>"
					+"		 <MEMO></MEMO>"
					+"		 <STATE></STATE>	"
					+"		 <CITY></CITY>	"
					+"		 <COUNTY></COUNTY>	"
					+"		 <STREETNAMEBASE></STREETNAMEBASE>	"
					+"		 <STREETNAME></STREETNAME> "
					+"		 <HOUSENUMBER></HOUSENUMBER>"
					+"		 <INSURANCECODE></INSURANCECODE>	"
					+"		 <INSURANCENAME></INSURANCENAME>"
					+"	 </PATIENT> "
					+"	 <CARDINFOS>"
					+"		<CARD>"
					+"		  <CARDNO>"+ei.getId_num()+"</CARDNO> "
					+"		  <CARDTYPE>01</CARDTYPE> "
					+"		  <OPERCODE></OPERCODE> "
					+"		  <OPERNAME></OPERNAME> "
					+"		</CARD> "
					+"	 </CARDINFOS> "
					+"	 <DOMAIN>005</DOMAIN> "
					+"	 <LOCALID>"+ei.getArch_num()+"</LOCALID>"
					+"	 <PROVIDERORGANIZATIONCODE>42517807-5</PROVIDERORGANIZATIONCODE>"	
					+"	 <PROVIDERORGANIZATIONNAME>宜昌市第一人民医院</PROVIDERORGANIZATIONNAME>"
					+"	 <REGISTRANTCARD></REGISTRANTCARD>"
					+"	 <REGISTRANTNAME></REGISTRANTNAME>"
					+"</PATIENTINFO>";
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("GET_PATIENT_ID");
			EMPI_Service empiService = new EMPI_ServiceLocator(wcd.getConfig_url());
			EmpiWebService empiWebService = empiService.getEMPI();
			TranLogTxt.liswriteEror_to_txt(logname,"patientXML:"+patientXML);
			String regEmpi = empiWebService.regEmpi(patientXML);
			TranLogTxt.liswriteEror_to_txt(logname,"regEmpi:"+regEmpi);
			DataSourceZSY resData = new DataSourceZSY(regEmpi, true);
			
			if("1".equals(resData.getMARK())) {
				TranLogTxt.liswriteEror_to_txt(logname,custom.getPATIENT_ID()+"-"+custom.getNAME()+"-"+"获取患者主索引成功");
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText(resData.getEMPI());
				
				CustomResBean cr = new CustomResBean();
				cr.setCLINIC_NO(resData.getEMPI());
				cr.setPATIENT_ID(resData.getEMPI());
				cr.setVISIT_DATE(DateTimeUtil.getDate2());
				cr.setVISIT_NO(resData.getEMPI());
				rb.getControlActProcess().getLIST().add(cr);
			} else {
				TranLogTxt.liswriteEror_to_txt(logname,"AE:"+resData.getEMPI());
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(resData.getEMPI());
			}
		} catch (Exception ex){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("登记-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"登记-调用webservice错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		
		//1.注册患者建档信息
		try {
			RegistPatientCreateDocInfo createDoc = getCreateDocData(sexcode, maritalStatusCode);
			PRPA_IN000001UV03 reqCreateDoc = new PRPA_IN000001UV03(ei.getExam_num());
			reqCreateDoc.getControlActProcess().getSubject().getRequest().setRegistPatientCreateDocInfo(createDoc);
			xml = JaxbUtil.convertToXml(reqCreateDoc, true);
			SERVService servService = new SERVServiceLocator(url);
			SERVDelegate servDelegate = servService.getSERVPort();
			TranLogTxt.liswriteEror_to_txt(logname,"reqCreateDoc:"+xml);
			String resCreateDoc = servDelegate.HIPMessageServer("registPatientCreateDocInfo", xml);
			TranLogTxt.liswriteEror_to_txt(logname,"resCreateDoc:"+resCreateDoc);	
			DataSourceCDR resData = new DataSourceCDR(resCreateDoc, true);
			if("AA".equals(resData.getStatus())) {
				TranLogTxt.liswriteEror_to_txt(logname,custom.getPATIENT_ID()+"-"+custom.getNAME()+"-"+"建档成功");
			} else {
				TranLogTxt.liswriteEror_to_txt(logname,"AE:"+resData.getMessage());
			}
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("建档-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"建档-调用webservice错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		
		//2.注册患者登记信息
		try {
			RegistPatientRegistrationInformation registration = getRegistrationData(sexcode, maritalStatusCode);
			PRPA_IN000001UV03 reqRegistration = new PRPA_IN000001UV03(ei.getExam_num());
			reqRegistration.getControlActProcess().getSubject().getRequest().setRegistPatientRegistrationInformation(registration);
			xml = JaxbUtil.convertToXml(reqRegistration, true);
			SERVService servService = new SERVServiceLocator(url);
			SERVDelegate servDelegate = servService.getSERVPort();
			TranLogTxt.liswriteEror_to_txt(logname,"reqRegistration:"+xml);
			String resRegistration = servDelegate.HIPMessageServer("registPatientRegistrationInformation", xml);
			TranLogTxt.liswriteEror_to_txt(logname,"resRegistration:"+resRegistration);	
			DataSourceCDR resData = new DataSourceCDR(resRegistration, true);
			if("AA".equals(resData.getStatus())) {
				TranLogTxt.liswriteEror_to_txt(logname,custom.getPATIENT_ID()+"-"+custom.getNAME()+"-"+"登记成功");
			} else {
				TranLogTxt.liswriteEror_to_txt(logname,"AE:"+resData.getMessage());
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(resData.getMessage());
			}
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("登记-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"登记-调用webservice错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rb;
	}

	private RegistPatientCreateDocInfo getCreateDocData(String sexcode, String maritalStatusCode) {
		String birthday = "";
		if(!StringUtil.isEmpty(ei.getBirthday())) {
			birthday = ei.getBirthday().replace(" ", "T");
		}
		RegistPatientCreateDocInfo createDoc = new RegistPatientCreateDocInfo();
		createDoc.getRegistPatientCreateDocInfoReq().setPatientID(this.ei.getArch_num());
//			createDoc.getRegistPatientCreateDocInfoReq().setEmpiId();
		createDoc.getRegistPatientCreateDocInfoReq().setPatientName(custom.getNAME());
		createDoc.getRegistPatientCreateDocInfoReq().setAge(custom.getAGE()+"岁");
		createDoc.getRegistPatientCreateDocInfoReq().setSexCode(sexcode);
		createDoc.getRegistPatientCreateDocInfoReq().setSex(custom.getSEX());
		createDoc.getRegistPatientCreateDocInfoReq().setBirthdate(birthday);
		createDoc.getRegistPatientCreateDocInfoReq().setMaritalStatusCode(maritalStatusCode);
		createDoc.getRegistPatientCreateDocInfoReq().setMaritalStatusName(custom.getMARITAL_STATUS());
//			createDoc.getRegistPatientCreateDocInfoReq().setNationCode();
//			createDoc.getRegistPatientCreateDocInfoReq().setNation();
//			createDoc.getRegistPatientCreateDocInfoReq().setNationalityCode();
//			createDoc.getRegistPatientCreateDocInfoReq().setNationalityName();
//			createDoc.getRegistPatientCreateDocInfoReq().setAboBloodTypeCode();
//			createDoc.getRegistPatientCreateDocInfoReq().setRhBloodTypeCode();
//			createDoc.getRegistPatientCreateDocInfoReq().setBirthPlace(custom.getBIRTH_PLACE());
//			createDoc.getRegistPatientCreateDocInfoReq().setOccupationalCode();
//			createDoc.getRegistPatientCreateDocInfoReq().setOccupationalName();
//			createDoc.getRegistPatientCreateDocInfoReq().setCulturalLevelCode();
		createDoc.getRegistPatientCreateDocInfoReq().setCulturalLevelName(custom.getDEGREE());
		createDoc.getRegistPatientCreateDocInfoReq().setWorkHomeName(custom.getUNIT_IN_CONTRACT());
		createDoc.getRegistPatientCreateDocInfoReq().setWorkTelecom(custom.getPHONE_NUMBER_BUSINESS());
		createDoc.getRegistPatientCreateDocInfoReq().setHomePhone(custom.getPHONE_NUMBER_HOME());
		createDoc.getRegistPatientCreateDocInfoReq().setMobilePhone(custom.getNEXT_OF_KIN_PHONE());
		createDoc.getRegistPatientCreateDocInfoReq().setPostCode(custom.getZIP_CODE());
		createDoc.getRegistPatientCreateDocInfoReq().setPatientEmailAddress(custom.getE_NAME());
//			createDoc.getRegistPatientCreateDocInfoReq().setOrganzCode();
//			createDoc.getRegistPatientCreateDocInfoReq().setOrganz();
		createDoc.getRegistPatientCreateDocInfoReq().setIdNumber(custom.getID_NO());
		PatientAddress patientAddress = new PatientAddress();
		patientAddress.setFullAddress(custom.getMAILING_ADDRESS());
		patientAddress.setAddressType("04");
		patientAddress.setAddressName("通讯地址");
		createDoc.getRegistPatientCreateDocInfoReq().getPatientAddressList().add(patientAddress);
		PatientMainList patientMainList = new PatientMainList();
		patientMainList.setContactName(custom.getNEXT_OF_KIN());
		patientMainList.setContactsPhonenumber(custom.getNEXT_OF_KIN_PHONE());
		patientMainList.setContactAddressl(custom.getNEXT_OF_KIN_ADDR());
		patientMainList.setContactRelationship(custom.getRELATIONSHIP());
		createDoc.getRegistPatientCreateDocInfoReq().getPatientMainListList().add(patientMainList);
//			createDoc.getRegistPatientCreateDocInfoReq().setPhoto();
		return createDoc;
	}

	private RegistPatientRegistrationInformation getRegistrationData(String sexcode, String maritalStatusCode) {
		String birthday = "";
		if(!StringUtil.isEmpty(ei.getBirthday())) {
			birthday = ei.getBirthday().replace(" ", "T");
		}
		RegistPatientRegistrationInformation registration = new RegistPatientRegistrationInformation();
		registration.getRegistPatientRegistrationInformationReq().setDiagnosisNO(ei.getArch_num());
		registration.getRegistPatientRegistrationInformationReq().setDiagnosisSerialNO(custom.getEXAM_NUM());
		registration.getRegistPatientRegistrationInformationReq().setPatientID(this.ei.getArch_num());
		registration.getRegistPatientRegistrationInformationReq().setClinicDatetime(ei.getJoin_date().replace(" ", "T"));
//			registration.getRegistPatientRegistrationInformationReq().setMajorDiagName("");
		registration.getRegistPatientRegistrationInformationReq().setPatientName(custom.getNAME());
		registration.getRegistPatientRegistrationInformationReq().setSexCode(sexcode);
		registration.getRegistPatientRegistrationInformationReq().setSex(custom.getSEX());
		registration.getRegistPatientRegistrationInformationReq().setBirthdate(birthday);
		registration.getRegistPatientRegistrationInformationReq().setAge(custom.getAGE()+"岁");
		registration.getRegistPatientRegistrationInformationReq().setMaritalStatusCode(maritalStatusCode);
		registration.getRegistPatientRegistrationInformationReq().setMaritalStatusName(custom.getMARITAL_STATUS());
//			registration.getRegistPatientRegistrationInformationReq().setNationCode();
//			registration.getRegistPatientRegistrationInformationReq().setNation();
//			registration.getRegistPatientRegistrationInformationReq().setNationalityCode();
//			registration.getRegistPatientRegistrationInformationReq().setNationalityName();
		registration.getRegistPatientRegistrationInformationReq().setPostCode(custom.getZIP_CODE());
		registration.getRegistPatientRegistrationInformationReq().setBirthPlace(custom.getBIRTH_PLACE());
//			registration.getRegistPatientRegistrationInformationReq().setOccupationalCode();
		registration.getRegistPatientRegistrationInformationReq().setOccupationalName(custom.getOCCUPATION());
//			registration.getRegistPatientRegistrationInformationReq().setCulturalLevelCode();
		registration.getRegistPatientRegistrationInformationReq().setCulturalLevelName(custom.getDEGREE());
		registration.getRegistPatientRegistrationInformationReq().setWorkHomeName(custom.getUNIT_IN_CONTRACT());
		registration.getRegistPatientRegistrationInformationReq().setWorkHomeNameTel(custom.getPHONE_NUMBER_BUSINESS());
		registration.getRegistPatientRegistrationInformationReq().setHomePhone(custom.getPHONE_NUMBER_HOME());
		registration.getRegistPatientRegistrationInformationReq().setMobilePhone(custom.getNEXT_OF_KIN_PHONE());
		registration.getRegistPatientRegistrationInformationReq().setPatientEmailAddress(custom.getE_NAME());
//			registration.getRegistPatientRegistrationInformationReq().setRegisteredDoctorCode();
//			registration.getRegistPatientRegistrationInformationReq().setRegisteredDoctorName();
//			registration.getRegistPatientRegistrationInformationReq().setOrganizationCode();
//			registration.getRegistPatientRegistrationInformationReq().setOrganz();
		registration.getRegistPatientRegistrationInformationReq().setIdNumber(custom.getID_NO());
		PatientAddress patientAddress = new PatientAddress();
		patientAddress.setFullAddress(custom.getMAILING_ADDRESS());
		patientAddress.setAddressType("04");
		patientAddress.setAddressName("通讯地址");
		registration.getRegistPatientRegistrationInformationReq().getPatientAddressList().add(patientAddress);
		PatientMainList patientMainList = new PatientMainList();
		patientMainList.setContactName(custom.getNEXT_OF_KIN());
		patientMainList.setContactsPhonenumber(custom.getNEXT_OF_KIN_PHONE());
		patientMainList.setContactAddressl(custom.getNEXT_OF_KIN_ADDR());
		patientMainList.setContactRelationship(custom.getRELATIONSHIP());
		registration.getRegistPatientRegistrationInformationReq().getPatientMainListList().add(patientMainList);
//			registration.getRegistPatientRegistrationInformationReq().setVisitDeptCode();
//			registration.getRegistPatientRegistrationInformationReq().setVisitDeptName();
//			registration.getRegistPatientRegistrationInformationReq().setAttendingDoctorDeptCode();
//			registration.getRegistPatientRegistrationInformationReq().setAttendingDoctorDeptName();
//			registration.getRegistPatientRegistrationInformationReq().setDirectorDeptCode();
//			registration.getRegistPatientRegistrationInformationReq().setDirectorDeptName();
//			registration.getRegistPatientRegistrationInformationReq().setInHospitalDocDeptCode();
//			registration.getRegistPatientRegistrationInformationReq().setInHospitalDocDeptName();
		return registration;
	}
}
