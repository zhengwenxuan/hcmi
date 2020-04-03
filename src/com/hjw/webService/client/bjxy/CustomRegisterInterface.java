package com.hjw.webService.client.bjxy;

import java.io.IOException;
import java.net.UnknownHostException;
import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.bjxy.util.BJXYhl7;
import com.hjw.webService.client.bjxy.util.XyyyClient;
import com.hjw.webService.client.body.ResultBody;


public class CustomRegisterInterface {
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	private Custom custom=new Custom();
	public CustomRegisterInterface(Custom custom){
		this.custom=custom;
	}
	
	public ResultBody getMessage(String logname) {
		
		ResultBody rb = new ResultBody();
		try {
		String exam_num = this.custom.getEXAM_NUM();
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			String str=BJXYhl7.adtA28hl7(custom, "A28",logname);
				rb=new XyyyClient().talk(str,"CUSTOMADD");
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rb;
	}
	
}
