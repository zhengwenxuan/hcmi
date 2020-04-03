package com.hjw.webService.service;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.service.Databean.DiagnosisPriceDataMessage;
import com.hjw.wst.service.DataService;

	public class AcceptDiagnosisPriceMessage extends ServletEndpointSupport {
		private DataService dataService;
		protected void onInit() {	
			this.dataService = (DataService) getWebApplicationContext().getBean("dataService");
		}

	   public String acceptMessageTest(String xmlmessage){	   
		   return "返回ok----临床诊疗项目与价表项目对照表"+xmlmessage; 
	   }
	   
	   /**
	    * 
	        * @Title: acceptMessageFee   
	        * @Description: 临床诊疗项目与价表项目对照表
	        * @param: @param xmlmessage
	        * @param: @return      
	        * @return: String      
	        * @throws
	    */
	    public String acceptDiagnosisPriceMessage(String xmlmessage){
	    	String filetype="resDiagnosisItemData";    	
	    	DiagnosisPriceDataMessage rb =new DiagnosisPriceDataMessage();
	    	ResultHeader ResultHeader=new ResultHeader();
	    	ResultHeader.setSourceMsgId("");//消息源id需要赋值
			try {	
				TranLogTxt.liswriteEror_to_txt(filetype,"req:"+xmlmessage);
				rb = JaxbUtil.converyToJavaBean(xmlmessage,DiagnosisPriceDataMessage.class);
				try {
					String messeage = this.dataService.saveAcceptDate(rb.getControlActProcess().getList());
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
