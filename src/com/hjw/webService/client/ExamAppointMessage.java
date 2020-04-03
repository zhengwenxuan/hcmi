package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ExamAppMessageBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.tj180.ExamAppointSendMessageTJ80;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package  
     * @Description:  病理
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class ExamAppointMessage {

	private ExamAppMessageBody examAppMessageBody;

	public ExamAppointMessage(ExamAppMessageBody examAppMessageBody){
		this.examAppMessageBody=examAppMessageBody;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultBody appointSend(String url,String userType,boolean flags) {
		String logname="examAppoint";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		
		System.out.println("userType--0--"+userType);
		ResultBody rb = new ResultBody();
		if ("1".equals(userType)) {//tj180
			ExamAppointSendMessageTJ80 lis= new ExamAppointSendMessageTJ80(examAppMessageBody);
			rb = lis.sendMessage(url, logname);
		}else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
