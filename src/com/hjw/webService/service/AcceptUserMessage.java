package com.hjw.webService.service;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.service.Databean.UserDateMessage;
import com.hjw.wst.service.DataService;

	public class AcceptUserMessage extends ServletEndpointSupport {
		private DataService dataService;
		protected void onInit() {	
			this.dataService = (DataService) getWebApplicationContext().getBean("dataService");
		}

	   public String acceptMessageTest(String xmlmessage){	   
		   return "返回ok----用户字典"+xmlmessage; 
	   }
	   
	   /**
	    * 
	        * @Title: acceptMessageFee   
	        * @Description: 2.16	收费成功返回
	        * @param: @param xmlmessage
	        * @param: @return      
	        * @return: String      
	        * @throws
	    */
	    public String acceptUserMessage(String xmlmessage){
	    	String filetype="resUserData";    	
	    	UserDateMessage rb =new UserDateMessage();
	    	ResultHeader ResultHeader=new ResultHeader();
	    	ResultHeader.setSourceMsgId("");//消息源id需要赋值
			try {	
				TranLogTxt.liswriteEror_to_txt(filetype,"req:"+xmlmessage);
				rb = JaxbUtil.converyToJavaBean(xmlmessage,UserDateMessage.class);
				try {
                    this.dataService.saveUserData(rb.getControlActProcess().getList());
					//信息入账				
					ResultHeader.setTypeCode("AA");
					ResultHeader.setText("交易成功");			
					
				} catch (Exception ex) {
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText("费用信息调用webservice错误");
				}

			} catch (Exception ex){

				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("费用信息 xml解析错误");
			}
			String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
			TranLogTxt.liswriteEror_to_txt(filetype,"req:"+reqxml);
			return reqxml;   	    	   
	   }
}
