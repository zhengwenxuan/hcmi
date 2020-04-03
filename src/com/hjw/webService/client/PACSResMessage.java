package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.dashiqiao.PacsResMessageDSQ;
import com.hjw.webService.client.dashiqiao.PacsResMessageDSQ_GetFtp;
import com.hjw.webService.client.hokai.PacsResMessageHK;
import com.hjw.webService.client.hokai305.PacsResMessageHK305;
import com.hjw.webService.client.tj180.PACSResMessageTj180;
import com.hjw.webService.client.xhhk.PacsResMessageXHHK;
import com.hjw.webService.client.xhhk.PacsXDResMessageXHHK;
import com.hjw.webService.client.xintong.PacsResMessageQH;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSResMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody pacsSend(String url,String userType,String exam_num) {
		String logName="resPacs";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		ResultPacsBody rb=new ResultPacsBody();
		if ("6".equals(userType)) {//tj180
			PACSResMessageTj180 lis= new PACSResMessageTj180();
			rb = lis.getMessage(url, logName,false,"all",false,"all",true,exam_num);
		}else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
	public String pacsSendString(String xmlstr,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		String rb="";
		if ("18".equals(userType)) {//青海---信通
			PacsResMessageQH lis= new PacsResMessageQH();
			rb = lis.getMessage(xmlstr, logName);
		} else if ("19".equals(userType)) {//银川-星华惠康
			PacsResMessageXHHK lis= new PacsResMessageXHHK();
			rb = lis.getMessage(xmlstr, logName);
		} else if ("21".equals(userType)) {//和佳-常德二院
			PacsResMessageHK lis= new PacsResMessageHK();
			rb = lis.getMessage(xmlstr, logName);
		} else if ("21.1".equals(userType)) {//和佳-305
			PacsResMessageHK305 lis= new PacsResMessageHK305();
			rb = lis.getMessage(xmlstr, logName);
		}else if ("27".equals(userType)) {//大石桥市
			PacsResMessageDSQ_GetFtp lis= new PacsResMessageDSQ_GetFtp();
			rb = lis.getMessage(xmlstr, logName);
		} else {
			rb="接口无对应厂家,请联系体检工程师检查webservice_configuration表config_method字段";
		}
		return rb;
	}
	
	public String pacsXDSendString(String xmlstr,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		String rb="";
		if ("19".equals(userType)) {//银川-星华惠康
			PacsXDResMessageXHHK lis= new PacsXDResMessageXHHK();
			rb = lis.getMessage(xmlstr, logName);
		} else {
			rb="接口无对应厂家,请联系体检工程师检查webservice_configuration表config_method字段";
		}
		return rb;
	}
	
}
