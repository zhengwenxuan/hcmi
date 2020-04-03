package com.hjw.webService.client.erfuyuan;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceLocator;
import com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_PortType;
import com.hjw.webService.client.erfuyuan.pacsbean.ResponseEFY_PACS;
import com.hjw.wst.config.GetNumContral;
import com.synjones.framework.persistence.JdbcPersistenceManager;

public class PACSDELSendMessageEFY {

	private static JdbcPersistenceManager jdbcPersistenceManager;
	static {
		init();
	}
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}
	private PacsMessageBody lismessage;
	public PACSDELSendMessageEFY(PacsMessageBody lismessage){
		this.lismessage = lismessage;
	}
	public ResultPacsBody getMessage(String url,String logName) {
		ResultPacsBody rb = new ResultPacsBody();
		ControlActPacsProcess ca = new ControlActPacsProcess();
		List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
		try {
			for (PacsComponents pcs : lismessage.getComponents()) {
				TranLogTxt.liswriteEror_to_txt(logName,"req:申请单号:"+pcs.getReq_no());
				try {
					OtherSysServiceLocator osl = new OtherSysServiceLocator(url);
					OtherSysServiceSoap_PortType oss = osl.getOtherSysServiceSoap();
					String messages = oss.cancelFeeApp(pcs.getReq_no());
					TranLogTxt.liswriteEror_to_txt(logName,"res:"+pcs.getReq_no()+":"+messages);
					ResponseEFY_PACS response = JaxbUtil.converyToJavaBean(messages, ResponseEFY_PACS.class);
					if("0".equals(response.getResult())) {
						ApplyNOBean an = new ApplyNOBean();
						String req_no = GetNumContral.getInstance().getParamNum("pacs_req_num");
						String updatesql = "update pacs_summary set pacs_req_code = '"+req_no+"' where pacs_req_code = '"+pcs.getReq_no()+"'";
						TranLogTxt.liswriteEror_to_txt(logName, "updatesql:"+updatesql);
						jdbcPersistenceManager.executeSql(updatesql);
						an.setApplyNO(pcs.getReq_no());
						list.add(an);
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("WebService错误" + response.getContent());
						TranLogTxt.liswriteEror_to_txt(logName, "WebService错误" + response.getContent());
					}
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("pacs调用webservice错误");
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
