package com.hjw.webBaseData.client;

import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.BaseData.GetUnitInContractMessage;

/**
 * 合同单位
 * @author yangm
 *
 */
public class BaseDataUnitInContractMessage {
	
	public BaseDataUnitInContractMessage(){
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getUicontract(String url, String userType, boolean debug) {
		ResultHeader rs=new ResultHeader();
		String logname = "reUicontract";
		if ("1".equals(userType)) {//180 手工同步费别
			GetUnitInContractMessage dsm = new GetUnitInContractMessage();
			rs = dsm.getUnitInContract(url, "utf-8", logname);
		} else {
			rs.setTypeCode("AE");
			rs.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rs;
	}

}
