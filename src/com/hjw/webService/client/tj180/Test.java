package com.hjw.webService.client.tj180;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.Bean.PacsGetResBean;
import com.hjw.webService.client.tj180.Bean.PacsGetResItemBean;

import net.sf.json.JSONObject;

public class Test {

	private static String parse5(String param) {
		Date date = null;
		if ((param != null) && (param.trim().length() == 14)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			try {
				date = sdf.parse(param);
			} catch (ParseException ex) {
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static void main(String[] args) {
		String result="{\"status\":\"200\",\"errorinfo\":\"\",\"examResultInfo\":[{\"examClass\":\"US\",\"examSubClass\":\"US\",\"examNo\":\"16668758\",\"description\":\"1111\",\"impression\":\"3333\",\"abnormalIndicator\":\"1\",\"resultDateTime\":\"20180813095147\",\"operator\":\"李耀华\",\"checkPerson\":\"李耀华\",\"reportDateTime\":\"2018-08-13T09:51:47\",\"examItemName\":\"B超\",\"examPdfPath\":\"http://10.180.180.100:8015/PdfGenerateWebapi.ashx?exam_no=16668758\"}]}";
		JSONObject jsonobject = JSONObject.fromObject(result);
		Map classMap = new HashMap();
		classMap.put("examResultInfo", PacsGetResItemBean.class);
   
	}
	

}
