package com.hjw.webService.client.insertDataToDB;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.ThridReq;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CUSTOMSendMessageInsertDataToDB {

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
	
	public CUSTOMSendMessageInsertDataToDB(Custom custom){
		this.custom=custom;
		this.ei = configService.getExamInfoForNum(custom.getEXAM_NUM());
	}

	public ResultBody getMessage(String ser_config_key,String logname) {
		String jsonString = JSONObject.fromObject(this.custom).toString();
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
		
		ExamInfoUserDTO eu = configService.getExamInfoForNum(this.custom.getEXAM_NUM());
		ThridReq thridReq = new ThridReq();
		thridReq.setExam_info_id(eu.getId());
		thridReq.setExam_num(eu.getExam_num());
		thridReq.setArch_num(eu.getArch_num());
//		thridReq.setReq_no();
		thridReq.setDatabody(jsonString);
		thridReq.setSer_config_key(ser_config_key);
		
		ResultBody rb = new ResultBody();
		boolean success = configService.insert_thrid_req(thridReq);
		if(success) {
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("参数入中间表 thrid_req，直接返回成功");
		} else {
			rb.getResultHeader().setText("提交检验申请失败");
			rb.getResultHeader().setTypeCode("AE");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb));
		return rb;
	}
}
