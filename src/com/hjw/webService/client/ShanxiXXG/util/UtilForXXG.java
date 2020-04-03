package com.hjw.webService.client.ShanxiXXG.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hjw.webService.client.ShanxiXXG.bean.ReqHeadXXG;

public class UtilForXXG {
	
	/**
	 * 生日格式化
	 * @param birthday
	 * @return
	 */
	public static String formatBirthday(String birthday) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date bir;
		try {
			bir = sdf.parse(birthday);
			birthday = sdf.format(bir);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return birthday;
	}
	
	/**
	 * 生成报文头信息
	 * @param xxg_key
	 * @param fromtype
	 * @param req_type
	 * @return
	 */
	public static ReqHeadXXG getReqHead(String xxg_key,String fromtype,String req_type) {
		ReqHeadXXG reqHead = new ReqHeadXXG();
		String time = String.valueOf(new Date().getTime());
		String token = MD5ForXXG.getToken(xxg_key, fromtype, time);
		String version = "1.0";
		String sessionid = MD5ForXXG.getSessionId(req_type);
		reqHead.setToken(token);
		reqHead.setVersion(version);
		reqHead.setFromtype(fromtype);
		reqHead.setSessionid(sessionid);
		reqHead.setTime(time);
		return reqHead;
	}
	
}
