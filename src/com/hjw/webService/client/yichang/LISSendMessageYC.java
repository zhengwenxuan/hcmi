package com.hjw.webService.client.yichang;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.Person;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.yichang.bean.cdr.client.header.PRPA_IN000001UV03;
import com.hjw.webService.client.yichang.bean.cdr.client.requestForm.ItemCDRYC;
import com.hjw.webService.client.yichang.bean.cdr.client.requestForm.RegistRequestForm;
import com.hjw.webService.client.yichang.bean.cdr.client.ret.DataSourceCDR;
import com.hjw.webService.client.yichang.gencode.SERVDelegate;
import com.hjw.webService.client.yichang.gencode.SERVService;
import com.hjw.webService.client.yichang.gencode.SERVServiceLocator;
import com.hjw.wst.DTO.ExamInfoUserDTO;

import net.sf.json.JSONSerializer;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.client.yichang
 * @Description: 检验申请信息服务 宜昌CDR
 * @author: zwx
 * @version V2.0.0.0
 */
public class LISSendMessageYC {
	private LisMessageBody lismessage;
	private static ConfigService configService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}

	public LISSendMessageYC(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		String jsonString = JSONSerializer.toJSON(lismessage).toString();
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
		
		ResultLisBody rb = new ResultLisBody();
		//发送申请-lis
		try {
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			
			ExamInfoUserDTO ei = configService.getExamInfoForNum(this.lismessage.getCustom().getExam_num());
			
			
			for (LisComponents lcs : this.lismessage.getComponents()) {
				RegistRequestForm requestForm = getRequestReqData(lcs, ei);
				PRPA_IN000001UV03 reqRequestForm = new PRPA_IN000001UV03(ei.getExam_num());
				reqRequestForm.getControlActProcess().getSubject().getRequest().setRegistRequestForm(requestForm);
				String xml = JaxbUtil.convertToXml(reqRequestForm, true);
				SERVService servService = new SERVServiceLocator(url);
				SERVDelegate servDelegate = servService.getSERVPort();
				TranLogTxt.liswriteEror_to_txt(logname,"reqRequestForm:"+xml);
				String resRequestForm = servDelegate.HIPMessageServer("registRequestForm", xml);
				TranLogTxt.liswriteEror_to_txt(logname,"resRequestForm:"+resRequestForm);	
				DataSourceCDR resData = new DataSourceCDR(resRequestForm, true);
				
				ApplyNOBean ap = new ApplyNOBean();
				ap.setApplyNO(lcs.getReq_no());
				ap.setLis_id(lcs.getLis_id());
				ap.setBarcode(lcs.getReq_no());
				list.add(ap);
				if("AA".equals(resData.getStatus())) {
					TranLogTxt.liswriteEror_to_txt(logname,this.lismessage.getCustom().getExam_num()+"-"+this.lismessage.getCustom().getName()+"-"+lcs.getReq_no()+"-"+"lis申请发送成功");
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText(this.lismessage.getCustom().getExam_num()+"-"+this.lismessage.getCustom().getName()+"-"+lcs.getReq_no()+"-"+"lis申请发送成功");
					rb.getControlActProcess().setList(list);
				} else {
					TranLogTxt.liswriteEror_to_txt(logname,"AE:"+resData.getMessage());
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(resData.getMessage());
				}
			}
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("发送lis申请-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"发送lis申请-调用webservice错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		jsonString = JSONSerializer.toJSON(rb).toString();
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + jsonString);
		return rb;
	}

	private RegistRequestForm getRequestReqData(LisComponents lcs,ExamInfoUserDTO ei) {
		Person custom = this.lismessage.getCustom();
		String sexcode = "";
		if("男".equals(custom.getSexname()) || "男性".equals(custom.getSexname())) {
			sexcode = "1";
			custom.setSexname("男性");
		} else if("女".equals(custom.getSexname()) || "女性".equals(custom.getSexname())) {
			sexcode = "2";
			custom.setSexname("女性");
		} else {
			sexcode = "0";
			custom.setSexname("未知的性别");
		}
		
		String birthday = "";
		if(!StringUtil.isEmpty(ei.getBirthday())) {
			birthday = ei.getBirthday().replace(" ", "T");
		}
		
		RegistRequestForm requestForm = new RegistRequestForm();
		requestForm.getRegistRequestReq().setApplyNo(lcs.getReq_no());
		requestForm.getRegistRequestReq().setApplyType("05");//检验申请单
		requestForm.getRegistRequestReq().setDiagnosisNo(ei.getArch_num());
		requestForm.getRegistRequestReq().setDiagnosisSerialNo(this.lismessage.getCustom().getExam_num());
//		requestForm.getRegistRequestReq().setEquipmentTypeCode();
//		requestForm.getRegistRequestReq().setEquipmentTypeName();
//		requestForm.getRegistRequestReq().setDocumentTypeCode();
//		requestForm.getRegistRequestReq().setDocumentTypeName();
		requestForm.getRegistRequestReq().setLocalId(lcs.getReq_no());
//		requestForm.getRegistRequestReq().setDocumentTitle();
		requestForm.getRegistRequestReq().setPatientName(this.lismessage.getCustom().getName());
		requestForm.getRegistRequestReq().setPatientLocalId(this.lismessage.getCustom().getExam_num());
		requestForm.getRegistRequestReq().setPatientPhonenumber(this.lismessage.getCustom().getTel());
		requestForm.getRegistRequestReq().setSexCode(sexcode);
		requestForm.getRegistRequestReq().setSexName(custom.getSexname());
		requestForm.getRegistRequestReq().setAge(custom.getOld()+"岁");
		requestForm.getRegistRequestReq().setBirthdate(birthday);
//		requestForm.getRegistRequestReq().setSpecimenCollectDatetime();
//		requestForm.getRegistRequestReq().setPatientSource();
//		requestForm.getRegistRequestReq().setApplicationStartTime();
//		requestForm.getRegistRequestReq().setApplicationDeptName();
//		requestForm.getRegistRequestReq().setApplicationDeptCode();
//		requestForm.getRegistRequestReq().setApplicationDoctorName();
//		requestForm.getRegistRequestReq().setApplicationDoctorCode();
//		requestForm.getRegistRequestReq().setExecutiveDepartmentName();
//		requestForm.getRegistRequestReq().setExecutiveDepartmentCode();
//		requestForm.getRegistRequestReq().setNursingStationName();
//		requestForm.getRegistRequestReq().setNursingStationCode();
//		requestForm.getRegistRequestReq().setApplyReason();
//		requestForm.getRegistRequestReq().setApplyContent();
//		requestForm.getRegistRequestReq().setHistorySummary();
//		requestForm.getRegistRequestReq().setClinicalDiagnosis();
//		requestForm.getRegistRequestReq().setSpecimenType();
//		requestForm.getRegistRequestReq().setSpecimenTypeName();
//		requestForm.getRegistRequestReq().setSpecimenCollecCode();
//		requestForm.getRegistRequestReq().setSpecimenCollecName();
//		requestForm.getRegistRequestReq().setOrganz();
//		requestForm.getRegistRequestReq().setOrganizationCode();
//		requestForm.getRegistRequestReq().setBedNo();
//		requestForm.getRegistRequestReq().setUrgentFlag();
//		requestForm.getRegistRequestReq().setApplyExecutiveDatetime();
		
		for (LisComponent lc : lcs.getItemList()) {
			ItemCDRYC item = new ItemCDRYC();
			item.setItemCode(lc.getItemCode());
			item.setItemName(lc.getItemName());
			item.setItemPrice(""+lc.getItemprice());
//			item.setExaminationSiteName(lc.getTargetSiteCode());
			requestForm.getRegistRequestReq().getItemList().add(item);
		}
//		requestForm.getRegistRequestReq().setXmlDocument();
//		requestForm.getRegistRequestReq().setFileBase64Content();
//		requestForm.getRegistRequestReq().setFileType();
//		requestForm.getRegistRequestReq().setOrderRemarks();
//		requestForm.getRegistRequestReq().setCodeExpand();
		return requestForm;
	}

}
