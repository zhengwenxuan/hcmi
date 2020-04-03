package com.hjw.webService.client.tiantan;

import java.util.ArrayList;
import java.util.List;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.tiantan.bean.ResLisBodyTiantan;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.client.tiantan   
     * @Description: 2.7	检查申请撤销信息服务
     * @author: zwx  
     * @version V2.0.0.0
 */
public class LISDELSendMessageTT {
	private LisMessageBody lismessage;
	public LISDELSendMessageTT(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logName) {
		//url="http://172.28.10.97:8089/cancellistest";
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + JSONObject.fromObject(lismessage));
		TranLogTxt.liswriteEror_to_txt(logName, "url:" + url);
		
		List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
		for (LisComponents lcs : lismessage.getComponents()) {
			List<String> itemCodeList = new ArrayList<>();
			boolean flag = true;
			for (LisComponent lc : lcs.getItemList()) {
				try {
					itemCodeList.add(lc.getItemCode());
					StringBuffer sb = new StringBuffer();
					sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
					sb.append("<root>");
					sb.append("<HIS>" + lismessage.getCustom().getExam_num() + "" + lc.getChargingItemid() + "</HIS>");
					sb.append("</root>");
					TranLogTxt.liswriteEror_to_txt(logName, "req:" + sb.toString() + "\r\n");
					String result = HttpUtil.doPost_Str(url, sb.toString(), "utf-8");
					TranLogTxt.liswriteEror_to_txt(logName, "res:" + result + "\r\n");
					ResLisBodyTiantan rbcomm = new ResLisBodyTiantan();
					if ((result != null) && (result.trim().length() > 0)) {
						result = result.trim();
						rbcomm = JaxbUtil.converyToJavaBean(result, ResLisBodyTiantan.class);
						if ("0".equals(rbcomm.getSuccess_flag())) {

						} else {
							flag = false;
						}
					}
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logName, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex) + "\r\n");
					flag = false;
				}
			}
			if(flag){
				ApplyNOBean an= new ApplyNOBean();
				an.setApplyNO(lcs.getReq_no());
				appList.add(an);
			}
		}
		ResultLisBody rb = new ResultLisBody();
		ControlActLisProcess ca = new ControlActLisProcess();
		ca.setList(appList);
		rb.setControlActProcess(ca);
		rb.getResultHeader().setTypeCode("AA");
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + JSONObject.fromObject(rb));
		return rb;
	}
}
