package com.hjw.webService.client.insertDataToDB;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.Bean.ThridReq;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;

import net.sf.json.JSONObject;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Description: Lis数据写入中间表
     * @author: zwx  
 */
public class PacsSendMessageInsertDataToDB{
	private PacsMessageBody lismessage;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public PacsSendMessageInsertDataToDB(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	public ResultPacsBody getMessage(String ser_config_key,String logName) {
		String jsonString = JSONObject.fromObject(lismessage).toString();
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + jsonString);
		
		ExamInfoUserDTO eu = configService.getExamInfoForNum(lismessage.getCustom().getExam_num());
		
		ThridReq thridReq = new ThridReq();
		thridReq.setExam_info_id(eu.getId());
		thridReq.setExam_num(eu.getExam_num());
		thridReq.setArch_num(eu.getArch_num());
		thridReq.setReq_no(lismessage.getComponents().get(0).getReq_no());
		thridReq.setDatabody(jsonString);
		thridReq.setSer_config_key(ser_config_key);
		
		ResultPacsBody rb = new ResultPacsBody();
		boolean success = configService.insert_thrid_req(thridReq);
		if(success) {
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			for (PacsComponents liscoms : lismessage.getComponents()) {
				ApplyNOBean an = new ApplyNOBean();
				an.setApplyNO(liscoms.getReq_no());
				list.add(an);
			}
			ControlActPacsProcess ca = new ControlActPacsProcess();
			ca.setList(list);
			rb.setControlActProcess(ca);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("写入中间表成功");
		} else {
			rb.getResultHeader().setText("提交检验申请失败");
			rb.getResultHeader().setTypeCode("AE");
		}
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + JSONObject.fromObject(rb));
		return rb;
	}
}
