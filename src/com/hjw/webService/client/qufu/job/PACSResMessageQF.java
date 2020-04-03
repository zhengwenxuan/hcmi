package com.hjw.webService.client.qufu.job;

import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.PacsPictureDecodeBase64Util;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.qufu.job.bean.RequestBody;
import com.hjw.webService.client.qufu.job.gencode.DataWebService;
import com.hjw.webService.client.qufu.job.gencode.DataWebServiceLocator;
import com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType;
import com.hjw.webService.client.qufu.job.gencode.Request;
import com.hjw.webService.client.qufu.job.gencode.ResponseQF;
import com.hjw.webService.client.qufu.job.pacsbean.ResPacsMessageQF;
import com.hjw.webService.client.qufu.job.pacsbean.RetPacsCustomeQF;
import com.hjw.webService.client.qufu.job.pacsbean.RetPacsItemQF;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSResMessageQF{
   private static ConfigService configService;
   private ThridInterfaceLog til;
   static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public ResultPacsBody getMessage(String url, String exam_num, String req_code) {
		String message_id = "TJ"+DateTimeUtil.getDateTimes();
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setReq_no(req_code);
    	til.setExam_no(exam_num);
    	til.setMessage_id(message_id);
    	til.setMessage_name("PACS_READ");
    	til.setMessage_type("webservice");
    	til.setSender("PEIS");
    	til.setReceiver("PF");
    	til.setFlag(2);
    	til.setXtgnb_id("2");//程序自动，设置为2-登记台首页
    	til.setMessage_inout(0);
		configService.insert_log(til);
		
		ResultPacsBody rb = new ResultPacsBody();
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "req:" + exam_num +"-"+req_code);
		try {
			Request request = new Request();
			request.getRequestHeader().setSender("2.16.840.1.113883.4.487.4.46.16");//TJ 体检系统
			request.getRequestHeader().setReceiver("2.16.840.1.113883.4.487.4.46.5");//RIS	放射科信息系统
			request.getRequestHeader().setRequestTime(DateTimeUtil.getDateTimes());
			request.getRequestHeader().setMsgType("DocInfo");
			request.getRequestHeader().setMsgId(message_id);
			request.getRequestHeader().setMsgPriority("Normal");
			request.getRequestHeader().setMsgVersion("1.0.0");
			
			RequestBody requestBody = new RequestBody();
			requestBody.setPAYLOADTYPE("RISREPORT");//检查报告
			requestBody.setPATIENTTYPE("3");
			requestBody.setPATIENTID(exam_num);
			requestBody.setVISITFLOWID(req_code);
			requestBody.setVISITFLOWDOMAINID("2.16.840.1.113883.4.487.4.46.16");
//			requestBody.setEXTEND_CONDITION("");
			
			String requestBodyStr = JaxbUtil.convertToXml(requestBody, false);
			requestBodyStr = requestBodyStr.split("<requestBody>")[1].split("</requestBody>")[0].trim();
			request.setRequestBody(requestBodyStr);
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"requestBody:"+requestBodyStr);
			til.setMessage_request(requestBodyStr);
			
			DataWebService dws = new DataWebServiceLocator(url);
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"url:"+url);
			DataWebServiceSoap_PortType dataWebServiceSoap = dws.getDataWebServiceSoap();
			ResponseQF response = dataWebServiceSoap.checkInfoQuery(request);
			if("0".equals(response.getResponseHeader().getErrCode())) {
				String reportWithOutBase64 = response.getResponseBody();
				if(response.getResponseBody() != null) {
					reportWithOutBase64 = response.getResponseBody().replaceAll("<IMAGE>.*</IMAGE>", "<IMAGE></IMAGE>");
				}
				
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"reportWithOutBase64:"+reportWithOutBase64);
				til.setMessage_response(reportWithOutBase64);
				
				if(response.getResponseBody().length() > 20) {
					ResPacsMessageQF rpm = new ResPacsMessageQF(response.getResponseBody(), true);
					RetPacsCustomeQF rc = rpm.getRetPacsCustome();
					try {
						PacsResult pacsResult = new PacsResult();
						pacsResult.setTil_id(til.getId());
						pacsResult.setExam_num(exam_num);
						pacsResult.setReq_no(req_code);
						pacsResult.setPacs_checkno(rc.getPacs_checkno());
						pacsResult.setReg_doc(rc.getReg_doc());
						pacsResult.setCheck_doc(rc.getCheck_doc());
						pacsResult.setCheck_date(rc.getCheck_date());
						pacsResult.setReport_doc(rc.getReport_doc());
						pacsResult.setReport_date(rc.getReport_date());
						pacsResult.setAudit_doc(rc.getAudit_doc());
						pacsResult.setAudit_date(rc.getAudit_date());
						String datetime = rc.getEffectiveTime().substring(0,8);
						
						RetPacsItemQF rpi = rc.getPacsItem();
						pacsResult.setClinic_symptom(rpi.getChargingItem_ms());
						pacsResult.setClinic_diagnose(rpi.getChargingItem_jl());
						pacsResult.setStudy_body_part(rpi.getBodyPart());
						pacsResult.setStudy_type(rpi.getExamMethod());
						pacsResult.setItem_name(rpi.getChargingItem_name());
						pacsResult.setPacs_item_code(rpi.getChargingItem_num());
						
						TranLogTxt.liswriteEror_to_txt("GetReport", "长度:" +rpi.getBase64_bg().length());
						TranLogTxt.liswriteEror_to_txt("GetReport", "内容:" +rpi.getBase64_bg());
						if (rpi.getBase64_bg().length() > 10) {
							String picname = PacsPictureDecodeBase64Util.decodeBase64JPG(exam_num, req_code, datetime, rpi.getBase64_bg());
							
							pacsResult.setIs_tran_image(0);
							pacsResult.setReport_img_path(picname);
						} else {
							pacsResult.setIs_tran_image(0);
						}
						boolean succ = this.configService.insert_pacs_result(pacsResult);
						
						if (succ) {
							rb.getResultHeader().setTypeCode("AA");
							rb.getResultHeader().setText("交易成功");
							til.setFlag(0);
						} else {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("pacs 入库失败");
						}
					} catch (Exception ex) {
						configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
					}
				} else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(response.getResponseBody());
				}
			} else {
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"平台返回报错："+response.getResponseHeader().getErrMessage());
				til.setMessage_response(response.getResponseHeader().getErrMessage());
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(response.getResponseHeader().getErrMessage());
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "pacs调用webservice错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			til.setFlag(1);
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("pacs调用webservice错误");
		}
		
    	configService.update_log(til);
		return rb;
	}

}
