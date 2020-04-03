package com.hjw.webService.client.dbgj;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.YbCustom;
import com.hjw.webService.client.body.YbCustomMessage;
import com.hjw.webService.client.body.YbCustomResultBody;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessage;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageService;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageServiceLocator;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.20	医保卡查询服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class YBCustomSendMessageTJPT {

	private YbCustomMessage custom;
	
	public YBCustomSendMessageTJPT(YbCustomMessage custom){
		this.custom=custom;
	}


	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public YbCustomResultBody getMessage(String url,String logname) {
		YbCustomResultBody rb = new YbCustomResultBody();
		String xml = "";
		try {
			this.custom.setMSG_TYPE("TJ607");
			xml = JaxbUtil.convertToXml(this.custom, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
			try {
				DefaultAcceptMessageService dam = new DefaultAcceptMessageServiceLocator(url);
				DefaultAcceptMessage dams = dam.getDefaultAcceptMessagePort();
				String messages = dams.acceptMessage(xml);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+messages);
				try {
					rb = JaxbUtil.converyToJavaBean(messages, YbCustomResultBody.class);
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("pacs解析返回值错误");
				}
			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("pacs调用webservice错误");
			}

		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装pacs xml格式文件错误");
		}
		return rb;
	}

}
