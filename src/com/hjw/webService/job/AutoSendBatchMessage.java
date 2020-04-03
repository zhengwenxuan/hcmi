package com.hjw.webService.job;

import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ExamItemUpdateMessage;
import com.hjw.webService.client.ExamUpdateMessage;
import com.hjw.webService.client.GroupDeleteMessage;
import com.hjw.webService.client.GroupSendMessage;
import com.hjw.webService.client.LISDELSendMessage;
import com.hjw.webService.client.LISSendMessage;
import com.hjw.webService.client.PACSDELSendMessage;
import com.hjw.webService.client.PACSSendMessage;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.ThridReq;
import com.hjw.webService.client.body.ExamItemUpdateMessageBody;
import com.hjw.webService.client.body.ExamTDeleteMessageBody;
import com.hjw.webService.client.body.ExamUpdateMessageBody;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 *  自动发送批次数据给第三方，团体结帐前发送
 */
public class AutoSendBatchMessage {
	
	public ResultHeader getMessage() {
		ResultHeader rh = new ResultHeader();
		String logName = "autoBatchReq";
		TranLogTxt.liswriteEror_to_txt(logName, "--------------------" + DateTimeUtil.getDate() + "--------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		JdbcPersistenceManager jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		ConfigService configService = (ConfigService) wac.getBean("configService");
		
		String sql = "select id,company_id as exam_info_id from batch where is_Active='Y' and overflag='2' and treated=0";
		TranLogTxt.liswriteEror_to_txt(logName, "sql: " + sql);
		List<ThridReq> thridReqList = jdbcQueryManager.getList(sql, ThridReq.class);
		//int success = 0;
		for(ThridReq thridReq : thridReqList) {
			try{
				ExamTDeleteMessageBody body = new ExamTDeleteMessageBody();
				body.setBarch_id(thridReq.getId());
				GroupDeleteMessage msg = new GroupDeleteMessage(body);
				ResultHeader brh = msg.send();
				if("AA".equals(brh.getTypeCode())){
					sql = "select a.id from exam_info a where a.is_Active='Y' and a.company_id='"+thridReq.getExam_info_id()+"' and a.batch_id='" + thridReq.getId()+ "' ";
					List<ExamInfoDTO> examList = jdbcQueryManager.getList(sql, ExamInfoDTO.class);
					if(examList.size()>0){
						GroupSendMessage gsm= new GroupSendMessage(thridReq.getId());
						gsm.sendByBatch();
						for (ExamInfoDTO examInfoDTO : examList) {
							ExamUpdateMessageBody bodys = new ExamUpdateMessageBody();
							bodys.setExam_id(examInfoDTO.getId());
							ExamUpdateMessage msgs = new ExamUpdateMessage(bodys);
							ResultHeader rhs = msgs.send();
							
							ExamItemUpdateMessageBody itembody = new ExamItemUpdateMessageBody();
							itembody.setExam_id(examInfoDTO.getId());
							ExamItemUpdateMessage itemmsg = new ExamItemUpdateMessage(itembody);
							ResultHeader itemrh = itemmsg.send();
							
						}
				}
				sql = "update batch set treated='1' where id='"+thridReq.getId()+"'";
				jdbcPersistenceManager.executeSql(sql);
			}				
		}catch(Exception ex){
			
		}
		}
		return rh;
	}
}
