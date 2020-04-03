package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.dashiqiao.LisResMessageDSQ;
import com.hjw.webService.client.hokai.LisResMessageHK;
import com.hjw.webService.client.hokai305.LisResMessageHK305;
import com.hjw.webService.client.hokai305.LisResMessageHK305_1222;
import com.hjw.webService.client.hokai305.test;
import com.hjw.webService.client.sxwn.LISResMessageSxWn;
import com.hjw.webService.client.tj180.LISResMessageTj180;
import com.hjw.webService.client.xhhk.LisResMessageXHHK;
import com.hjw.webService.client.xintong.LisResMessageQH;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISResMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody lisSend(String url,String userType,String exam_num) {
		String logName="resLis";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		ResultLisBody rb=new ResultLisBody();
		if ("3".equals(userType)) {//山西人民 卫宁
			LISResMessageSxWn lis= new LISResMessageSxWn();
			rb = lis.getMessage(url, logName,exam_num);
		}else if ("6".equals(userType)) {//tj180
			LISResMessageTj180 lis= new LISResMessageTj180();
			rb = lis.getMessage(url, logName,exam_num);
		}else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

	public String lisSendString(String xmlstr,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		String rb="";
		if ("18".equals(userType)) {//银川-星华惠康
			LisResMessageQH lis= new LisResMessageQH();
			rb = lis.getMessage(xmlstr, logName);
		}else if ("19".equals(userType)) {//银川-星华惠康
			LisResMessageXHHK lis= new LisResMessageXHHK();
			rb = lis.getMessage(xmlstr, logName);
		} else if ("21".equals(userType)) {//和佳-常德二院
			LisResMessageHK lis= new LisResMessageHK();
			rb = lis.getMessage(xmlstr, logName);
		} else if ("21.1".equals(userType)) {//和佳-305
			LisResMessageHK305_1222 lis= new LisResMessageHK305_1222();//
			rb = lis.getMessage(xmlstr, logName);
		}else if ("27".equals(userType)) {//大石桥
			LisResMessageDSQ lis= new LisResMessageDSQ();//
			rb = lis.getMessage(xmlstr, logName);
		}else if ("30".equals(userType)) {//大石桥
			test lis= new test();//
			String searchPatid = test.searchPatid(xmlstr, logName);
		}else {
			rb=("AE");
		}
		return rb;
	}
	
}
