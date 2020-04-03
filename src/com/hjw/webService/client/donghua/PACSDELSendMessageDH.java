package com.hjw.webService.client.donghua;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.donghua.bean.PEStop.Request_PEStop;
import com.hjw.webService.client.donghua.bean.PEStop.Response_PEStop;
import com.hjw.webService.client.donghua.gencode.WebPEService;
import com.hjw.webService.client.donghua.gencode.WebPEServiceLocator;
import com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_PortType;
import com.hjw.wst.DTO.ExamInfoUserDTO;

import net.sf.json.JSONObject;

public class PACSDELSendMessageDH {

	private PacsMessageBody lismessage;
	private static ConfigService configService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	public PACSDELSendMessageDH(PacsMessageBody lismessage){
		this.lismessage = lismessage;
	}
	public ResultPacsBody getMessage(String url,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName,"lismessage:"+new Gson().toJson(lismessage, PacsMessageBody.class));
		List<ApplyNOBean> appList = new ArrayList<ApplyNOBean>();
		try {
			for (PacsComponents pcs : lismessage.getComponents()) {
				ExamInfoUserDTO eu= configService.getExamInfoForNum(this.lismessage.getCustom().getExam_num());
				List<ZlReqPacsItemDTO> itemList = configService.select_zl_req_pacs_item(eu.getId(), pcs.getReq_no(), logName);
				boolean suc = true;
				for(ZlReqPacsItemDTO item : itemList) {
					TranLogTxt.liswriteEror_to_txt(logName,"2.6. 停医嘱-开始");
					Request_PEStop request_PEStop = new Request_PEStop();
					request_PEStop.setOEORowid(item.getReq_id());
					String requestStr = JaxbUtil.convertToXmlWithOutHead(request_PEStop, true);
					TranLogTxt.liswriteEror_to_txt(logName,"request:"+requestStr);
					WebPEService WebPEService = new WebPEServiceLocator(url);
					WebPEServiceSoap_PortType webPEServiceSoap = WebPEService.getWebPEServiceSoap();
					String responseStr = webPEServiceSoap.saveStopPEOrdInfo(requestStr);
					TranLogTxt.liswriteEror_to_txt(logName,"response:"+responseStr);
					Response_PEStop response = JaxbUtil.converyToJavaBean(responseStr, Response_PEStop.class);
					if("0".equals(response.getResultCode())){
						TranLogTxt.liswriteEror_to_txt(logName,"2.6. 停医嘱-成功");
					} else {
						suc = false;
						TranLogTxt.liswriteEror_to_txt(logName,"2.6. 停医嘱-失败，对方返回："+response.getResultContent());
					}
				}
				if(suc) {
					ApplyNOBean an= new ApplyNOBean();
					an.setApplyNO(pcs.getReq_no());
					appList.add(an);
				}
			}
		} catch (Exception ex){
			TranLogTxt.liswriteEror_to_txt(logName, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		ResultPacsBody rb = new ResultPacsBody();
		if(appList.size()>0) {
			ControlActPacsProcess ca = new ControlActPacsProcess();
			ca.setList(appList);
			rb.setControlActProcess(ca);
			rb.getResultHeader().setTypeCode("AA");
		} else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("撤销lis申请失败");
		}
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + JSONObject.fromObject(rb));
		return rb;
	}
}
