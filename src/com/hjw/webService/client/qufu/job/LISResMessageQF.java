package com.hjw.webService.client.qufu.job;

import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.qufu.job.bean.RequestBody;
import com.hjw.webService.client.qufu.job.gencode.DataWebService;
import com.hjw.webService.client.qufu.job.gencode.DataWebServiceLocator;
import com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType;
import com.hjw.webService.client.qufu.job.gencode.Request;
import com.hjw.webService.client.qufu.job.gencode.ResponseQF;
import com.hjw.webService.client.qufu.job.lisbean.ResLisMessageQF;
import com.hjw.webService.client.qufu.job.lisbean.RetLisChargeItemQF;
import com.hjw.webService.client.qufu.job.lisbean.RetLisCustomeQF;
import com.hjw.webService.client.qufu.job.lisbean.RetLisItemQF;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISResMessageQF{
   private static ConfigService configService;
   private ThridInterfaceLog til;
   
   static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public ResultLisBody getMessage(String url, String exam_num, String sample_barcode, int days) {
		String message_id = "TJ"+DateTimeUtil.getDateTimes();
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setReq_no(sample_barcode);
    	til.setExam_no(exam_num);
    	til.setMessage_id(message_id);
    	til.setMessage_name("LIS_READ");
    	til.setMessage_type("webservice");
    	til.setSender("PEIS");
    	til.setReceiver("PF");
    	til.setFlag(2);
    	til.setXtgnb_id("2");//程序自动，设置为2-登记台首页
    	til.setMessage_inout(0);
		configService.insert_log(til);
		
		ResultLisBody rb = new ResultLisBody();
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "req:" + exam_num +"-"+sample_barcode);
		try {
			Request request = new Request();
			request.getRequestHeader().setSender("2.16.840.1.113883.4.487.4.46.16");//TJ 体检系统
			request.getRequestHeader().setReceiver("2.16.840.1.113883.4.487.4.46.7");//LIS 实验室信息系统
			request.getRequestHeader().setRequestTime(DateTimeUtil.getDateTimes());
			request.getRequestHeader().setMsgType("DocInfo");
			request.getRequestHeader().setMsgId("TJ"+DateTimeUtil.getDateTimes());
			request.getRequestHeader().setMsgPriority("Normal");
			request.getRequestHeader().setMsgVersion("1.0.0");
			
			RequestBody requestBody = new RequestBody();
			requestBody.setPAYLOADTYPE("LISREPORT");//检验报告
			requestBody.setPATIENTTYPE("3");
			requestBody.setPATIENTID(exam_num);
			requestBody.setVISITFLOWID(sample_barcode);
			requestBody.setVISITFLOWDOMAINID("2.16.840.1.113883.4.487.4.46.16");
			requestBody.setEXTEND_CONDITION("<STARTTIME>"+DateTimeUtil.DateDiff(days)+"000000"+"</STARTTIME><ENDTIME>"+DateTimeUtil.getDateTimes()+"</ENDTIME>");
			String requestBodyStr = JaxbUtil.convertToXmlWithCDATA(requestBody, "^EXTEND_CONDITION");
			requestBodyStr = requestBodyStr.split("<requestBody>")[1].split("</requestBody>")[0].trim();
			request.setRequestBody(requestBodyStr);
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"requestBody:"+requestBodyStr);
			til.setMessage_request(requestBodyStr);
			
			DataWebService dws = new DataWebServiceLocator(url);
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"url:"+url);
			DataWebServiceSoap_PortType dataWebServiceSoap = dws.getDataWebServiceSoap();
			ResponseQF response = dataWebServiceSoap.checkInfoQuery(request);
			if("0".equals(response.getResponseHeader().getErrCode())) {
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"responseBody:"+response.getResponseBody());
				til.setMessage_response(response.getResponseBody());
				
				if(response.getResponseBody().length() > 20) {
					ResLisMessageQF rpm = new ResLisMessageQF(response.getResponseBody(), true);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"返回信息解析成功！");
					RetLisCustomeQF rc = rpm.rc;
					if ((rc == null) || (rc.getListRetLisChargeItem() == null) || (rc.getListRetLisChargeItem().size() <= 0)) {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("lis信息解析为空");
					} else {
						LisResult lisResult = new LisResult();
						lisResult.setTil_id(til.getId());
						lisResult.setExam_num(exam_num);
						lisResult.setSample_barcode(sample_barcode);
						lisResult.setDoctor(rc.getCheck_doc());
						lisResult.setExam_date(rc.getCheck_date());
						lisResult.setSh_doctor(rc.getAudit_doc());
						boolean flagss = true;
						int seq_code = 0;
						for (RetLisChargeItemQF rlcharg : rc.getListRetLisChargeItem()) {
							lisResult.setLis_item_code(rlcharg.getChargingItem_num());
							lisResult.setLis_item_name(rlcharg.getChargingItem_name());
							for (RetLisItemQF rlis : rlcharg.getListRetLisItem()) {
								lisResult.setSeq_code(seq_code++);
								lisResult.setReport_item_code(rlis.getItem_id());
								lisResult.setReport_item_name(rlis.getItem_name());
								lisResult.setItem_result(rlis.getValues());
								lisResult.setRef(rlis.getValue_fw());
								lisResult.setItem_unit(rlis.getValues_dw());
								lisResult.setFlag(rlis.getValue_ycbz());
								
								//曲阜		H-高		L-低		M-正常\N-阴性	P-阳性\Q-弱阳性	
								//火箭蛙		H-高		L-低		N-正常			P-阳性			C-危急	HH-偏高报警	LL-偏低报警
								if ("M".equals(lisResult.getFlag())) {
									lisResult.setFlag("N");
								} else if ("Q".equals(lisResult.getFlag())) {
									lisResult.setFlag("P");
								}
								
								boolean succ = this.configService.insert_lis_result(lisResult);
								if (!succ) {
									flagss = false;
								}
							}
						}
						if (flagss) {
							configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"lis信息 入库成功");
							rb.getResultHeader().setTypeCode("AA");
							rb.getResultHeader().setText("lis信息 入库成功");
							til.setFlag(0);
						} else {
							configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"lis信息 入库错误");
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("lis信息 入库错误");
						}
					}
				} else {
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(),response.getResponseBody());
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(response.getResponseBody());
				}
			} else {
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"平台返回报错："+response.getResponseHeader().getErrMessage());
				til.setMessage_response(response.getResponseHeader().getErrMessage());
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(response.getResponseHeader().getErrMessage());
			}
			rb.getResultHeader().setTypeCode("AA");
		} catch (Exception ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "lis调用webservice错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			ex.printStackTrace();
			til.setFlag(1);
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("lis调用webservice错误");
		}
		configService.update_log(til);
		return rb;
	}
	
}
