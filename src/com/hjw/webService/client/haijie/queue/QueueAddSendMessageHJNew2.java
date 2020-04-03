package com.hjw.webService.client.haijie.queue;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ExamQueueLog;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueAddBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class QueueAddSendMessageHJNew2 {

	private static ConfigService configService;
	static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public QueueResBody getMessage(String url,String tokenurl,QueueAddBean eu,String logname) {
		QueueResBody rb = new QueueResBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +  eu.getExam_id());
			String[] tokenurls= tokenurl.split("&");
			QueueHttpUtil qh= new QueueHttpUtil();
			qh.getToken(tokenurls[0], tokenurls[1], tokenurls[2],logname);
		    Thread.sleep(100);
		    ExamInfoUserDTO ei = configService.getExamInfoForExam_id(eu.getExam_id());
			String res = qh.addToQueue(url+"/"+ei.getExam_num(),tokenurls[1],"utf-8");	
			TranLogTxt.liswriteEror_to_txt(logname, "res:-res" +  res);
			
			org.json.JSONObject jsonObject = new org.json.JSONObject(res);
			boolean success = jsonObject.optBoolean("success");
			if(success) {
				org.json.JSONObject resultValues = jsonObject.optJSONObject("resultValues");
				org.json.JSONObject queue = resultValues.optJSONObject("queue");
				if(queue != null && !"".equals(queue) && !"null".equals(queue)) {
					org.json.JSONObject queuePoint = queue.optJSONObject("queuePoint");
					String name = queuePoint.optString("name");
					
					boolean suc = configService.selectExamQueueLog(ei.getExam_num(), logname);
					if(suc) {//今天已调用过第一接口
						suc = configService.updateExamQueueLog(name, ei.getExam_num(), logname);
						rb.setRestext("");
						rb.setRescode("AA");
						rb.setIdnumber("");
					} else {
						ExamQueueLog examQueueLog = new ExamQueueLog();
						examQueueLog.setExam_num(ei.getExam_num());
						examQueueLog.setQueue_no(name);
						suc = configService.insertExamQueueLog(examQueueLog, logname);
						rb.setRestext("");
						rb.setRescode("AA");
						rb.setIdnumber("");
					}
				} else {
					rb.setRestext("对方返回："+resultValues.optString("message"));
					rb.setRescode("AA");
					rb.setIdnumber("");
				}
			} else {
				rb.setRestext("对方返回："+res);
				rb.setRescode("AA");
				rb.setIdnumber("");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");
			rb.setIdnumber("");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "re1:" + str);
		return rb;
	}
}
