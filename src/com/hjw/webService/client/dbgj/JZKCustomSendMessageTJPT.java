package com.hjw.webService.client.dbgj;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.JzkCustom;
import com.hjw.webService.client.body.YbCustomMessage;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessage;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageService;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageServiceLocator;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.21	就诊卡查询服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class JZKCustomSendMessageTJPT {

	private YbCustomMessage custom;
	
	public JZKCustomSendMessageTJPT(YbCustomMessage custom){
		this.custom=custom;
	}  

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public JzkCustom getMessage(String url,String logname) {
		JzkCustom rb = new JzkCustom();
		String xml = "";
		try {
			this.custom.setMSG_TYPE("TJ608");
			xml = JaxbUtil.convertToXml(this.custom, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
			try {
				DefaultAcceptMessageService dam = new DefaultAcceptMessageServiceLocator(url);
				DefaultAcceptMessage dams = dam.getDefaultAcceptMessagePort();
				String messages = dams.acceptMessage(xml);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+messages);
				try {
					rb = JaxbUtil.converyToJavaBean(messages, JzkCustom.class);
				} catch (Exception ex) {
					rb.setRESULT("1");
				}
			} catch (Exception ex) {
				rb.setRESULT("2");
			}

		} catch (Exception ex){
			rb.setRESULT("3");
		}
		return rb;
	}

}
