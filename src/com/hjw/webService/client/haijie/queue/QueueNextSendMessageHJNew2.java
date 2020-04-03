package com.hjw.webService.client.haijie.queue;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueNextBean;
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
public class QueueNextSendMessageHJNew2 {

	private static ConfigService configService;
	static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public QueueResBody getMessage(String url,String tokenurl,QueueNextBean eu,String logname) {
		QueueResBody rb = new QueueResBody();
		try {
			JSONObject json = JSONObject.fromObject(eu);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
			String[] tokenurls= tokenurl.split("&");
			QueueHttpUtil qh= new QueueHttpUtil();
			qh.getToken(tokenurls[0], tokenurls[1], tokenurls[2],logname);
		    Thread.sleep(100);
		    long exam_id = Long.parseLong(eu.getCode());
		    ExamInfoUserDTO ei = configService.getExamInfoForExam_id(exam_id);
			String res =qh.next(url,  tokenurls[1], ei.getExam_num(),"utf-8");		
			TranLogTxt.liswriteEror_to_txt(logname, "res:-res" +  res);
			rb.setRestext("");
			rb.setRescode("AA");
			rb.setIdnumber("");
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
