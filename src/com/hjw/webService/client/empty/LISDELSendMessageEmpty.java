package com.hjw.webService.client.empty;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;

public class LISDELSendMessageEmpty {
	private LisMessageBody lismessage;
	public LISDELSendMessageEmpty(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	public ResultLisBody getMessage(String dllName, String logName) {
		ResultLisBody rb = new ResultLisBody();
		ControlActLisProcess ca = new ControlActLisProcess();
		List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
		for (LisComponents liscoms : lismessage.getComponents()) {
			ApplyNOBean an = new ApplyNOBean();
			an.setApplyNO(liscoms.getReq_no());
			list.add(an);
		}
		if(list.size() > 0) {
			ca.setList(list);
			rb.setControlActProcess(ca);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("撤销lis申请接口，空实现，直接成功");
		}
		TranLogTxt.liswriteEror_to_txt(logName,"res:"+new Gson().toJson(rb, ResultLisBody.class));
		return rb;
	}
}
