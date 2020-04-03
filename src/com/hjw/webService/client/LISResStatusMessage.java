package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.hokai.LISResStatusMessageHK;
import com.hjw.webService.client.hokai305.LISResStatusMessageHK305;
import com.hjw.webService.client.tj180.LISResStatusMessageTj180;
import com.hjw.webService.client.xhhk.LISResStatusMessageXHHK;
import com.hjw.webService.client.xintong.LISResStatusMessageQH;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISResStatusMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody lisSend(String url,String userType,String exam_num,long exam_id) {
		String logName="resLisStatus";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		ResultLisBody rb=new ResultLisBody();
		if ("6".equals(userType)) {//tj180
			LISResStatusMessageTj180 lis= new LISResStatusMessageTj180();
			rb = lis.getMessage(url, logName,exam_num,exam_id);
		}else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
	
	public String lisSendString(String strbody,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		String rb="";
		if ("19".equals(userType)) {//银川-星华惠康
			LISResStatusMessageXHHK lis= new LISResStatusMessageXHHK();
			rb = lis.getMessage(strbody, logName);
		}else if ("21".equals(userType)) {//和佳-常德二院
			LISResStatusMessageHK lis= new LISResStatusMessageHK();
			rb = lis.getMessage(strbody, logName);
		}else if ("21.1".equals(userType)) {//和佳-305
			LISResStatusMessageHK305 lis= new LISResStatusMessageHK305();
			rb = lis.getMessage(strbody, logName);
		}else if ("18".equals(userType)) {//和佳-305
			LISResStatusMessageQH lis= new LISResStatusMessageQH();
			rb = lis.getMessage(strbody, logName);
		}else {
			rb="";
		}
		return rb;
	}

}
