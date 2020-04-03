package com.hjw.webBaseData.client;

import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.BaseData.GetChargeTypeMessage;

/**
 * 获取诊疗项目
 * @author yangm
 *
 */
public class BaseDataChargeTypeMessage {
	
	public BaseDataChargeTypeMessage(){
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getChargeType(String url, String userType, boolean debug) {
		ResultHeader rs=new ResultHeader();
		String logname = "reChargeType";
		if ("1".equals(userType)) {//180 手工同步费别
			GetChargeTypeMessage dsm = new GetChargeTypeMessage();
			rs = dsm.getChargeType(url, "utf-8", logname);
		} else {
			rs.setTypeCode("AE");
			rs.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rs;
	}

}
