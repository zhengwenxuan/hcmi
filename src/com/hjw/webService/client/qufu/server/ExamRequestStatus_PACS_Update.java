package com.hjw.webService.client.qufu.server;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.webService.client.qufu.job.PACSResMessageQF;
import com.hjw.webService.client.qufu.server.bean.RequestBody;
import com.hjw.webService.client.qufu.server.gencode.Response;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;


public class ExamRequestStatus_PACS_Update {
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	private ThridInterfaceLog til;
	static {
		init();
	}

	public ExamRequestStatus_PACS_Update(ThridInterfaceLog til) {
		this.til = til;
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}

	public Response checkInfoQuery(RequestBody requestBody) {
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "====Status_PACS_Update====");
		Response response = new Response();
		
		if("0".equals(requestBody.getFLAG())) {//0-未检查
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "检查标志-未检查，体检系统不做任何处理");
		} else if("1".equals(requestBody.getFLAG())) {//1-已检查
			configService.setExamInfoChargeItemPacsStatus(requestBody.getBAR_CODE(), "C");
		} else if("2".equals(requestBody.getFLAG())) {//2-已完成报告
			configService.setExamInfoChargeItemPacsStatus(requestBody.getBAR_CODE(), "C");
			//this.commService.setExamInfoChargeItemPacsStatus(requestBody.getBAR_CODE(), "Y");
			
			final String exam_num = requestBody.getEXAM_NO();
			final String bar_code = requestBody.getBAR_CODE();
			WebserviceConfigurationDTO wcd = webserviceConfigurationService.getWebServiceConfig("PACS_READ");
			final String url = wcd.getConfig_url().trim();
			new Thread(new Runnable() {
				//之前测试，这里不另开一个线程的话，平台报错：nested exception is:   java.net.SocketTimeoutException: Read timed out
				@Override
				public void run() {
					WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
					ConfigService configService_inner = (ConfigService) wac.getBean("configService");
					try {
						Thread.sleep(10 * 60 * 1000L);
						
						PACSResMessageQF prm = new PACSResMessageQF();
						ResultPacsBody rb = prm.getMessage(url, exam_num, bar_code);
						if("AA".equals(rb.getResultHeader().getTypeCode())) {
							configService_inner.insert_message_log(til.getId(), til.getMessage_seq_code(), "更新状态后主动获取PACS报告结果-成功");
						} else {
							configService_inner.insert_message_log(til.getId(), til.getMessage_seq_code(), "更新状态后主动获取PACS报告结果-失败");
						}
					} catch (Throwable e) {
						configService_inner.insert_message_log(til.getId(), til.getMessage_seq_code(), "更新状态后主动获取PACS报告结果-异常："+com.hjw.interfaces.util.StringUtil.formatException(e));
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "不支持的检查标志————FLAG:" + requestBody.getFLAG());
			response.getResponseHeader().setErrCode("2000");
			response.getResponseHeader().setErrMessage("不支持的检查标志————FLAG:" + requestBody.getFLAG());
			return response;
		}
		String responseBodyStr = "<CODE>"+0+"</CODE>";
		response.setResponseBody(responseBodyStr);
		response.getResponseHeader().setErrCode("0");
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"responseBody:"+responseBodyStr);
		
		til.setExam_no(requestBody.getEXAM_NO());
		til.setReq_no(requestBody.getBAR_CODE());
		return response;
	}

}
