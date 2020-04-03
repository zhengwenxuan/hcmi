package com.hjw.webService.client.dbgj;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessage;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageService;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageServiceLocator;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.14	挂号信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CUSTOMSendMessageTJPT {

	private Custom custom=new Custom();
	
	public CUSTOMSendMessageTJPT(Custom custom){
		this.custom=custom;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultBody getMessage(String url,String logname) {
		ResultBody rb = new ResultBody();
		String xml = "";
		try {
			this.custom.setMSG_TYPE("TJ601");
			xml = JaxbUtil.convertToXml(this.custom, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
			try {
				DefaultAcceptMessageService dam = new DefaultAcceptMessageServiceLocator(url);
				DefaultAcceptMessage dams = dam.getDefaultAcceptMessagePort();
				String messages = dams.acceptMessage(xml);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+messages);				
				try {
					rb = JaxbUtil.converyToJavaBean(messages, ResultBody.class);
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("人员信息解析返回值错误");
				}
			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("人员信息调用webservice错误");
			}

		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装人员信息 xml格式文件错误");
		}
		return rb;
	}
}
