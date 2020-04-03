package com.hjw.webService.client.empty;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;

public class PACSDELSendMessageEmpty {

	private PacsMessageBody lismessage;
	public PACSDELSendMessageEmpty(PacsMessageBody lismessage){
		this.lismessage = lismessage;
	}
	public ResultPacsBody getMessage(String url,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName,"lismessage:"+new Gson().toJson(lismessage, PacsMessageBody.class));
		ResultPacsBody rb = new ResultPacsBody();
		ControlActPacsProcess ca = new ControlActPacsProcess();
		List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
		for (PacsComponents pcs : lismessage.getComponents()) {
			ApplyNOBean an = new ApplyNOBean();
			an.setApplyNO(pcs.getReq_no());	
			list.add(an);
		}
		TranLogTxt.liswriteEror_to_txt(logName,"size:"+list.size());
		if(list.size() > 0) {
			ca.setList(list);
			rb.setControlActProcess(ca);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("撤销pacs申请接口，空实现，直接成功");
		}
		TranLogTxt.liswriteEror_to_txt(logName,"res:"+new Gson().toJson(rb, ResultPacsBody.class));
		return rb;
	}
}
