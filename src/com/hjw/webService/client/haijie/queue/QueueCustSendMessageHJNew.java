package com.hjw.webService.client.haijie.queue;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueCustomerBean;
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
public class QueueCustSendMessageHJNew {

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
	public QueueResBody getMessage(String url,String tokenurl,QueueCustomerBean eu,String logname) {
		QueueResBody rb = new QueueResBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + eu.getExam_id());
			QueueHttpUtil qh= new QueueHttpUtil();
			System.out.println("3.1");
			System.out.println(tokenurl);
			System.out.println("3.1.1");
			String[] tokenurls= tokenurl.split("&");
			
			qh.getToken(tokenurls[0], tokenurls[1], tokenurls[2],logname);
			System.out.println(tokenurls[0]);
			System.out.println(tokenurls[1]);
			
			System.out.println("3.2");
		    Thread.sleep(100);
		    ExamInfoUserDTO ei = configService.getExamInfoForExam_id(eu.getExam_id());
		    System.out.println("3.3");
			String res = qh.updateAppointmentInfo(url+"/"+ei.getExam_num(),tokenurls[1],"utf-8");	
			System.out.println("3.4");
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
