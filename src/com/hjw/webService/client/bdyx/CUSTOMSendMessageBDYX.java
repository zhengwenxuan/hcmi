package com.hjw.webService.client.bdyx;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.bdyx.bean.RegisterReq;
import com.hjw.webService.client.bdyx.bean.RequestPost;
import com.hjw.webService.client.bdyx.bean.ResponsePost;
import com.hjw.webService.client.bdyx.bean.ResponsePatReg;
import com.hjw.webService.client.bdyx.gencode.IApiService;
import com.hjw.webService.client.bdyx.gencode.IApiServiceLocator;
import com.hjw.webService.client.bdyx.gencode.IApiServicePortType;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.UserDTO;

import net.sf.json.JSONObject;

public class CUSTOMSendMessageBDYX {

	private static ConfigService configService;
	private Custom custom=new Custom();
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public CUSTOMSendMessageBDYX(Custom custom){
		this.custom=custom;
	}

	public ResultBody getMessage(String url,String logname) {
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"url:"+url);
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		
		ResultBody rb = new ResultBody();
		ExamInfoUserDTO eu= configService.getExamInfoForNum(custom.getEXAM_NUM());
		if(!StringUtils.isEmpty(eu.getBirthday()) && eu.getBirthday().length() >10) {
			eu.setBirthday(eu.getBirthday().substring(0, 10));
		}
		try {
			String reqXml = ""
					+"<request>"
					+"  <tradeNo>ZT003</tradeNo>"
					+"  <cardNo>00009527</cardNo>"
					+"  <cardType>0</cardType>"
					+"  <name>"+eu.getUser_name()+"</name>"
					+"  <idNo>"+eu.getId_num()+"</idNo>"
					+"  <birthday>"+eu.getBirthday()+"</birthday>"
					+"  <sex>"+eu.getSex()+"</sex>"
					+"  <address>"+eu.getAddress()+"</address>"
					+"  <phone>"+eu.getPhone()+"</phone>"
					+"  <cost>0</cost>"
					+"  <termNo>8888</termNo>"
					+"</request>"
					+ "";
			TranLogTxt.liswriteEror_to_txt(logname,"request:"+reqXml);
			
			IApiService iApiService = new IApiServiceLocator(url.split("&&")[0]);
			IApiServicePortType iApiServicePort = iApiService.getIApiServicePort();
			String responsePatRegStr = iApiServicePort.apiEntry("patRegDistrCard", reqXml);
			
			TranLogTxt.liswriteEror_to_txt(logname,"responsePatRegStr:"+responsePatRegStr);
			ResponsePatReg responsePatReg = JaxbUtil.converyToJavaBean(responsePatRegStr, ResponsePatReg.class);
			
			if(1 == responsePatReg.getResponseCode()) {
				TranLogTxt.liswriteEror_to_txt(logname,custom.getPATIENT_ID()+"-"+custom.getNAME()+"-"+"发送病人信息成功");
				
				HttpSession session = ServletActionContext.getRequest().getSession();
				UserDTO user = (UserDTO) session.getAttribute("username");
				
				String sexcode = "0";
				if("男".equals(custom.getSEX()) || "男性".equals(custom.getSEX())) {
					sexcode = "1";
				} else if("女".equals(custom.getSEX()) || "女性".equals(custom.getSEX())) {
					sexcode = "2";
				}
				
				RegisterReq request = new RegisterReq();
				request.setVisitOrdNo(eu.getExam_num());
				request.setPatientLid(responsePatReg.getPatientId());
				request.setPatientName(eu.getUser_name());
				request.setGenderCode(sexcode);
				request.setBirthDate(custom.getDATE_OF_BIRTH());
				request.setAge(""+eu.getAge());
				request.setRegisteredDoctorName(user.getUsername());
				String str = new Gson().toJson(request, RegisterReq.class);
				
				RequestPost requestBDYX = new RequestPost();
				requestBDYX.setService_id("BS001");
				requestBDYX.setExec_uint_id("0101004");
				requestBDYX.setOrder_exec_id("");
				requestBDYX.setExtend_sub_id("");
				requestBDYX.setBody(str);
				String requestStr = JSONObject.fromObject(requestBDYX).toString();
				TranLogTxt.liswriteEror_to_txt(logname,"req:"+requestStr);
				String responseStr = HttpUtil.doPost(url.split("&&")[1],requestStr,"utf-8");
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+responseStr);
				ResponsePost responseBDYX = new Gson().fromJson(responseStr, ResponsePost.class);
				if(1 == responseBDYX.getStatus()) {
					List<CustomResBean> LIST = new ArrayList<CustomResBean>();
					CustomResBean cr = new CustomResBean();
					cr.setPATIENT_ID(responsePatReg.getPatientId());
					cr.setCLINIC_NO(responsePatReg.getPatientId());
					cr.setVISIT_NO(responsePatReg.getPatientId());
					cr.setVISIT_DATE(DateTimeUtil.getDateTimes());
					LIST.add(cr);
					ControlActProcess ca = new ControlActProcess();
					ca.setLIST(LIST);
					rb.setControlActProcess(ca);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("交易成功");
				} else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("平台返回错误:"+responseBDYX.getErrMsg());
				}
			} else {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("平台返回失败:"+responsePatReg.getResponseMsg());
				TranLogTxt.liswriteEror_to_txt(logname,"平台返回失败:"+responsePatReg.getResponseMsg());
			}
		} catch (Exception ex){
			ex.printStackTrace();
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("发送病人信息-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"发送病人信息-调用webservice错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		xml = JaxbUtil.convertToXmlWithOutHead(rb, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"res:"+xml);
		return rb;
	}
}
