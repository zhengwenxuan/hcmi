package com.hjw.test;

import java.util.HashMap;
import java.util.Map;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.Bean.ChargeCusItemInfo;
import com.hjw.webService.client.tj180.Bean.ResChargeCusBean;

import net.sf.json.JSONObject;

public class TestJson {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String result = "";
		result="{\"chargeTypeInfo\"=[{\"identityName\"=\"地方\", \"chargeTypeName\"=\"农村居民\"}, {\"identityName\"=\"武警人员\", \"chargeTypeName\"=\"武警人员\"}, {\"identityName\"=\"普通军属\", \"chargeTypeName\"=\"农村居民\"}, {\"identityName\"=\"义务兵\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"士官\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"战士\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"外宾\", \"chargeTypeName\"=\"自费\"}, {\"identityName\"=\"地方\", \"chargeTypeName\"=\"自费\"}, {\"identityName\"=\"军以上干部\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"师职干部\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"团以下干部\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"免减费家属\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"城镇职工\", \"chargeTypeName\"=\"职工医保\"}, {\"identityName\"=\"城镇居民\", \"chargeTypeName\"=\"居民医保\"}, {\"identityName\"=\"农民\", \"chargeTypeName\"=\"新农合\"}, {\"identityName\"=\"地方\", \"chargeTypeName\"=\"体检\"}, {\"identityName\"=\"公务员\", \"chargeTypeName\"=\"职工医保\"}, {\"identityName\"=\"团职干部\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"普通军属\", \"chargeTypeName\"=\"自费\"}, {\"identityName\"=\"普通军属\", \"chargeTypeName\"=\"新农合\"},{\"identityName\"=\"普通军属\",\"chargeTypeName\"=\"职工医保\"},{\"identityName\"=\"普通军属\",\"chargeTypeName\"=\"眼科农保\"}, {\"identityName\"=\"地方\",\"chargeTypeName\"=\"眼科农保\"}, {\"identityName\"=\"农民\",\"chargeTypeName\"=\"眼科农保\"}, {\"identityName\"=\"城乡居民\",\"chargeTypeName\"=\"居民医保\"}, {\"identityName\"=\"普通军属\",\"chargeTypeName\"=\"居民医保\"}, {\"identityName\"=\"义务兵\",\"chargeTypeName\"=\"体检\"}], \"errorinfo\"=\"\", \"status\"=\"200\"}";
		//result = EntityUtils.toString(resEntity, charset);
		result = result.trim();
		JSONObject jsonobject = JSONObject.fromObject(result);

		Map classMap = new HashMap();
		classMap.put("chargeTypeInfo", ChargeCusItemInfo.class);
		ResChargeCusBean rdb = new ResChargeCusBean();
		rdb = (ResChargeCusBean) JSONObject.toBean(jsonobject, ResChargeCusBean.class, classMap);
		
	}

}
