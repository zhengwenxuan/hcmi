package com.hjw.webService.client.tiantan;

import java.util.ArrayList;
import java.util.List;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
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
public class PacsDELSendMessageTT {
	private PacsMessageBody lismessage;
	public PacsDELSendMessageTT(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logName) {
		ResultPacsBody rb = new ResultPacsBody();
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + JSONObject.fromObject(lismessage));
		TranLogTxt.liswriteEror_to_txt(logName, "url:" + url);
		List<ApplyNOBean> appList = new ArrayList<ApplyNOBean>();
		for (PacsComponents lcs : lismessage.getComponents()) {
				try {
					StringBuffer sb = new StringBuffer();
					sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
					sb.append("<REQUEST COMMAND=\"CancelApplication\">");
					sb.append("<ApplicationID>"+lcs.getReq_no()+"</ApplicationID>");
					sb.append("</REQUEST>");
					
					String result = HttpUtil.doPost_Str(url, sb.toString(), "utf-8");
					TranLogTxt.liswriteEror_to_txt(logName, "res:" + result + "\r\n");
					ResLisBodyTiantan rbcomm = new ResLisBodyTiantan();
					if ((result != null) && (result.trim().length() > 0)) {
						result = result.trim();
						rbcomm = JaxbUtil.converyToJavaBean(result, ResLisBodyTiantan.class);
						if ("0".equals(rbcomm.getSuccess_flag())) {
							ApplyNOBean ab = new ApplyNOBean();
							ab.setApplyNO(lcs.getReq_no());
							appList.add(ab);
						} 
					}
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logName, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex) + "\r\n");
				}
			}
        rb.getControlActProcess().setList(appList);
   	    rb.getResultHeader().setTypeCode("AA");

		TranLogTxt.liswriteEror_to_txt(logName, "res:" + JSONObject.fromObject(rb));
		return rb;
	}
}
