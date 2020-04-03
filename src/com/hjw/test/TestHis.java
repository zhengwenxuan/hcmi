package com.hjw.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.Bean.HisGetBodyBean;
import com.hjw.webService.client.tj180.Bean.HisGetResItemBean;

import net.sf.json.JSONObject;

public class TestHis {
	
	public static String parse5(String param) {
		Date date = new Date();
		if ((param != null) && (param.trim().length() == 8)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setLenient(false);
			try {
				date = sdf.parse(param);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
		

	public static void main(String[] args) {
		
		System.out.println(parse5("20171215"));
		
		// TODO Auto-generated method stub
		HisGetBodyBean hgb= new HisGetBodyBean();
		hgb.setReserveId("1712130001");
		List<HisGetResItemBean> hisItemsList=new ArrayList<HisGetResItemBean>();//申请单号
		HisGetResItemBean h= new HisGetResItemBean();
		h.setAmount(20);
		h.setUnionProjectId("TJ000010");
		hisItemsList.add(h);

		h= new HisGetResItemBean();
		h.setUnionProjectId("TJ000014");
		h.setAmount(65.0);
		hisItemsList.add(h);
		hgb.setHisItemsList(hisItemsList);
		
		
		JSONObject json = JSONObject.fromObject(hgb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt("hisresteset", "res:" + str);
		String result = HttpUtil.doPost("http://localhost/hisresControl.action",hgb,"utf-8");
		TranLogTxt.liswriteEror_to_txt("hisresteset", "res:" + result);
	}

}
