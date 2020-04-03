package com.hjw.webService.service;


import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.service.Databean.DeptDataMessage;
import com.hjw.wst.service.DataService;

public class AcceptDeptMessage extends ServletEndpointSupport {
	private DataService dataService;
	protected void onInit() {	
		this.dataService = (DataService) getWebApplicationContext().getBean("dataService");
	}

   public String acceptMessageTest(String xmlmessage){	   
	   return "返回ok----科室字典"+xmlmessage; 
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
    public String acceptDeptMessage(String xmlmessage){
    	String filetype="resDeptData";    	
    	DeptDataMessage rb =new DeptDataMessage();
    	ResultHeader ResultHeader=new ResultHeader();
    	ResultHeader.setSourceMsgId("");//消息源id需要赋值
		try {	
			TranLogTxt.liswriteEror_to_txt(filetype,"req:"+xmlmessage);
			rb = JaxbUtil.converyToJavaBean(xmlmessage,DeptDataMessage.class);
			try {
				//信息入账				
				String messeage = this.dataService.saveHisDept(rb.getControlActProcess().getList());
				if(messeage.split("-")[0].equals("ok")){
					ResultHeader.setTypeCode("AA");
					ResultHeader.setText("交易成功");
				}else{
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText(messeage.split("-")[1]);
				}
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
