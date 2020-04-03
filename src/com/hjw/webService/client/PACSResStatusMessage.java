package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.hokai.PacsResStatusMessageHK;
import com.hjw.webService.client.hokai305.PacsResStatusMessageHK305;
import com.hjw.webService.client.xhhk.PacsResStatusMessageXHHK;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSResStatusMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody pacsSend(String url,String userType,String exam_num,long exam_id) {
		String logName="resLisStatus";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		ResultPacsBody rb=new ResultPacsBody();
		if ("6".equals(userType)) {//tj180
			
		}else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
	
	public String pacsSendString(String strbody,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		String rb="";
		if ("21".equals(userType)) {//和佳-常德二院
			PacsResStatusMessageHK lis= new PacsResStatusMessageHK();
			rb = lis.getMessage(strbody, logName);
		} else if ("21.1".equals(userType)) {//和佳-305
			PacsResStatusMessageHK305 lis= new PacsResStatusMessageHK305();
			rb = lis.getMessage(strbody, logName);
		}else if ("19".equals(userType)) {//银川 星华慧康
			PacsResStatusMessageXHHK lis= new PacsResStatusMessageXHHK();
			rb = lis.getMessage(strbody, logName);
		}else {
			rb="";
		}
		return rb;
	}

}
