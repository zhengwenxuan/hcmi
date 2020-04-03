package com.hjw.webService.client.donghua;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.donghua.bean.PEStop.Request_PEStop;
import com.hjw.webService.client.donghua.bean.PEStop.Response_PEStop;
import com.hjw.webService.client.donghua.gencode.WebPEService;
import com.hjw.webService.client.donghua.gencode.WebPEServiceLocator;
import com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_PortType;
import com.hjw.wst.DTO.ExamInfoUserDTO;

import net.sf.json.JSONObject;

public class LISDELSendMessageDH {
	private LisMessageBody lismessage;
	private static ConfigService configService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	public LISDELSendMessageDH(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + JSONObject.fromObject(lismessage));
		TranLogTxt.liswriteEror_to_txt(logName, "url:" + url);
		
		List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
		for (LisComponents lcs : lismessage.getComponents()) {
			try {
				ExamInfoUserDTO eu= configService.getExamInfoForNum(this.lismessage.getCustom().getExam_num());
				List<ZlReqItemDTO> itemList = configService.select_zl_req_item(eu.getId(), lcs.getReq_no(), logName);
				boolean suc = true;
				for(ZlReqItemDTO item : itemList) {
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
					an.setApplyNO(lcs.getReq_no());
					appList.add(an);
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logName, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}
		ResultLisBody rb = new ResultLisBody();
		if(appList.size()>0) {
			ControlActLisProcess ca = new ControlActLisProcess();
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
