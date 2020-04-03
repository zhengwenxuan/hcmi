package com.hjw.webBaseData.client;

import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.BaseData.GetDeptMessage;

/**
 * 获取部门数据
 * @author yangm
 *
 */
public class BaseDataDeptMessage {
	
	public BaseDataDeptMessage(){
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader deptGet(String url, String userType, boolean debug) {
		ResultHeader rs=new ResultHeader();
		String logname = "resHisDept";
		if ("1".equals(userType)) {//180 手工同步his部门
			GetDeptMessage dsm = new GetDeptMessage();
			rs = dsm.getDept(url, "utf-8", logname);
		} else {
			rs.setTypeCode("AE");
			rs.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rs;
	}

}
