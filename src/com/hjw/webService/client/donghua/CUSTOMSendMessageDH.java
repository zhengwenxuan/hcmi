package com.hjw.webService.client.donghua;

import com.hjw.util.DateTimeUtil;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.donghua.bean.ResponseDH;
import com.hjw.webService.client.donghua.bean.PatAdmInfo.Request_PatAdm;
import com.hjw.webService.client.donghua.gencode.WebPEService;
import com.hjw.webService.client.donghua.gencode.WebPEServiceLocator;
import com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_PortType;
import com.hjw.wst.DTO.ExamInfoUserDTO;

public class CUSTOMSendMessageDH {

	private static ConfigService configService;
	private Custom custom=new Custom();
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public CUSTOMSendMessageDH(Custom custom){
		this.custom=custom;
	}

	public ResultBody getMessage(String url,String logname) {
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		
		ResultBody rb = new ResultBody();
		ExamInfoUserDTO eu= configService.getExamInfoForNum(custom.getEXAM_NUM());
		String sexcode = "";
		if("男".equals(custom.getSEX()) || "男性".equals(custom.getSEX())) {
			sexcode = "M";
		} else if("女".equals(custom.getSEX()) || "女性".equals(custom.getSEX())) {
			sexcode = "F";
		} else {
			sexcode = "F";
		}
		
		try {
			Request_PatAdm request_PatAdm = new Request_PatAdm();
			request_PatAdm.setRegNo(eu.getArch_num());//体检登记号/体检档案号
			request_PatAdm.setAdmNo(eu.getExam_num());
			request_PatAdm.setName(eu.getUser_name());//病人姓名
			request_PatAdm.setSex(sexcode);//病人性别代码 M：男，F:女
			request_PatAdm.setBirth(custom.getDATE_OF_BIRTH());//出生日期 YY-MM-DD
			request_PatAdm.setPhone(custom.getPHONE_NUMBER_HOME());//电话
			request_PatAdm.setAddress(custom.getBIRTH_PLACE());//地址
			request_PatAdm.setAdmDate(DateTimeUtil.getDate2());//就诊日期
			request_PatAdm.setAdmTime(DateTimeUtil.getDateTime().substring(11));//就诊时间
			request_PatAdm.setIdentityNo(custom.getID_NO());//身份证号
			
			xml = JaxbUtil.convertToXmlWithOutHead(request_PatAdm, true);
			TranLogTxt.liswriteEror_to_txt(logname,"request:"+xml);
			
			WebPEService WebPEService = new WebPEServiceLocator(url);
			WebPEServiceSoap_PortType webPEServiceSoap = WebPEService.getWebPEServiceSoap();
			String responseStr = webPEServiceSoap.savePatAdmInfo(xml);
			
			TranLogTxt.liswriteEror_to_txt(logname,"response:"+responseStr);
			ResponseDH response = JaxbUtil.converyToJavaBean(responseStr, ResponseDH.class);
			
			if("0".equals(response.getResultCode())) {
				TranLogTxt.liswriteEror_to_txt(logname,custom.getPATIENT_ID()+"-"+custom.getNAME()+"-"+"发送病人信息成功");
			} else {
				TranLogTxt.liswriteEror_to_txt(logname,"发送病人信息失败:"+response.getResultContent());
			}
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("发送病人信息-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"发送病人信息-调用webservice错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rb;
	}
}
