package com.hjw.webService.client.xianning;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.xianning.bean.DeleteParam;
import com.hjw.webService.client.xianning.bean.ResultXN;
import com.synjones.framework.persistence.JdbcPersistenceManager;

public class PACSDELSendMessageXN {

	private static JdbcPersistenceManager jdbcPersistenceManager;
	static {
		init();
	}
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}
	private PacsMessageBody lismessage;
	public PACSDELSendMessageXN(PacsMessageBody lismessage){
		this.lismessage = lismessage;
	}
	public ResultPacsBody getMessage(String url,String logName) {
		ResultPacsBody rb = new ResultPacsBody();
		ControlActPacsProcess ca = new ControlActPacsProcess();
		List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
		try {
			TranLogTxt.liswriteEror_to_txt(logName,"lismessage:"+new Gson().toJson(lismessage, PacsMessageBody.class));
			for (PacsComponents pcs : lismessage.getComponents()) {
				DeleteParam deleteParam = new DeleteParam();
				deleteParam.setEXAM_NO(pcs.getReq_no());
				deleteParam.setEXAM_CODE(lismessage.getCustom().getExam_num());
				url = url+deleteParam.toString();
				TranLogTxt.liswriteEror_to_txt(logName,"url:"+url);
//				String jsonString = new Gson().toJson(deleteParam, DeleteParam.class);
//				TranLogTxt.liswriteEror_to_txt(logName,"req:"+jsonString);
				
				String responseString = HttpUtil.doDelete(url,"utf-8");
				TranLogTxt.liswriteEror_to_txt(logName,"res:"+responseString);
				if (responseString != null) {
					ResultXN result = new Gson().fromJson(responseString, ResultXN.class);
					if("true".equals(result.getSuccess())) {
						ApplyNOBean an = new ApplyNOBean();
						an.setApplyNO(pcs.getReq_no());	
						list.add(an);
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("PACS返回错误:" + result.getMsg());
						TranLogTxt.liswriteEror_to_txt(logName, "PACS返回错误:" + result.getMsg());
					}
				} else {
					rb.getResultHeader().setTypeCode("AE");
					TranLogTxt.liswriteEror_to_txt(logName, "HTTP无返回");
				}
			}
			if(list.size() > 0) {
				ca.setList(list);
				rb.setControlActProcess(ca);
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("pacs调用成功");
			}
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装pacs xml格式文件错误");
		}
		return rb;
	}
}
