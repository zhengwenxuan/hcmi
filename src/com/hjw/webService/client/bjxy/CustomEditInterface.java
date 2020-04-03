package com.hjw.webService.client.bjxy;

import java.io.IOException;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.bjxy.util.BJXYhl7;
import com.hjw.webService.client.bjxy.util.XyyyClient;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;

public class CustomEditInterface {
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	private Custom custom=new Custom();
	public CustomEditInterface(Custom custom){
		this.custom=custom;
	}
	
	public FeeResultBody getMessage(String logname) {
		FeeResultBody rb = new FeeResultBody();
		try {
		String exam_num = this.custom.getEXAM_NUM();
		String patient_id=this.custom.getPATIENT_ID();
		long exam_id = 0;
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else if(StringUtil.isEmpty(patient_id)){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("patientId为空");
		}else{
			String str=BJXYhl7.adtA28hl7(custom, "A31",logname);
			ResultHeader rh=new XyyyClient().talkFee(str,"CUSTOMEDIT");
			rb.setResultHeader(rh);
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rb;
	}

}
