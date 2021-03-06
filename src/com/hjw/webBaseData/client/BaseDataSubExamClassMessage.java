package com.hjw.webBaseData.client;

import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.BaseData.GetExamSubClassMessage;

/**
 * 检查类别字典
 * @author yangm
 *
 */
public class BaseDataSubExamClassMessage {
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader deptGet(String url, String userType, boolean debug) {
		ResultHeader rs=new ResultHeader();
		String logname = "resExamSubClass";
		if ("1".equals(userType)) {//180 手工检查子类别字典
			GetExamSubClassMessage dsm = new GetExamSubClassMessage();
			rs = dsm.getData(url, "utf-8", logname);
		} else {
			rs.setTypeCode("AE");
			rs.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rs;
	}

}
