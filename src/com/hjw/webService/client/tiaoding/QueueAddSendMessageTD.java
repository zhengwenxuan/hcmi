package com.hjw.webService.client.tiaoding;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ExamQueueLog;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueAddBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.tiaoding.bean.DataContentTD;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class QueueAddSendMessageTD {
	
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public QueueResBody getMessage(String url, QueueAddBean queueAddBean, String logname) {
		QueueResBody rb = new QueueResBody();
		try {
			JSONObject json = JSONObject.fromObject(queueAddBean);// 将java对象转换为json对象
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + json);
			ExamInfoUserDTO eu = configService.getExamInfoForExam_id(queueAddBean.getExam_id());
			if(!configService.hasItem(eu.getId(), logname)) {
				TranLogTxt.liswriteEror_to_txt(logname, "此时没有项目，直接返回成功");
				rb.setRestext("成功");
				rb.setRescode("AA");
				return rb;
			}
			
			url = url+"?cardNo="+eu.getExam_num();
			TranLogTxt.liswriteEror_to_txt(logname, "实施可在浏览器测试这个url:" + url);
			String result = HttpUtil.doGet(url, "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "result:" + result);
			DataContentTD response = JaxbUtil.converyToJavaBean(result, DataContentTD.class);
			if ("0".equals(response.getErr())) {
				boolean suc = configService.selectExamQueueLog(eu.getExam_num(), logname);
				if(suc) {//今天已调用过第一接口
					suc = configService.updateExamQueueLog(response.getClient().getCategoryid(), eu.getExam_num(), logname);
				} else {
					ExamQueueLog examQueueLog = new ExamQueueLog();
					examQueueLog.setExam_num(eu.getExam_num());
					examQueueLog.setQueue_no(response.getClient().getCategoryid());
					suc = configService.insertExamQueueLog(examQueueLog, logname);
				}
				if(suc) {
					rb.setRestext("成功");
					rb.setRescode("AA");
				} else {
					TranLogTxt.liswriteEror_to_txt(logname, "更新exam_queue_log表失败");
				}
			}else {
				rb.setRestext("不识别的错误代码："+response.getErr());
				rb.setRescode("AE");
			}
		} catch (Exception ex) {
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + json);
		return rb;
	}
}
