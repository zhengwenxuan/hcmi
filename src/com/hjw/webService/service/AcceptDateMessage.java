package com.hjw.webService.service;


import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.service.Databean.DataMessage;
import com.hjw.webService.service.Databean.DeptDataMessage;
import com.hjw.webService.service.Databean.DiagnosisItemDataMessage;
import com.hjw.webService.service.Databean.DiagnosisPriceDataMessage;
import com.hjw.webService.service.Databean.PriceDataMessage;
import com.hjw.webService.service.Databean.UserDateMessage;
import com.hjw.wst.service.DataService;

public class AcceptDateMessage extends ServletEndpointSupport {
	private DataService dataService;
	
	protected void onInit() {	
		this.dataService = (DataService) getWebApplicationContext().getBean("dataService");
	}

   public String acceptMessageTest(String xmlmessage){	   
	   return "返回ok----字典"+xmlmessage; 
   }
   
   /**
    * 
        * @Title: acceptMessage   
        * @Description: TODO(这里用一句话描述这个方法的作用)   
        * @param: @param xmlmessage
        * @param: @return      
        * @return: String      
        * @throws
    */
   public String acceptMessage(String xmlmessage){
	   TranLogTxt.liswriteEror_to_txt("DATA","req:"+xmlmessage);
	   ResultHeader ResultHeader=new ResultHeader();
	   DataMessage ah= new DataMessage();
	   try{		   
		   ah = JaxbUtil.converyToJavaBean(xmlmessage,DataMessage.class);
		   if("MS022".equals(ah.getAuthHeader().getMsgType())){//价表
			   return acceptPriceMessage(xmlmessage);
		   }else if("MS040".equals(ah.getAuthHeader().getMsgType())){//临床诊疗项目与价表项目对照表
			   return acceptDiagnosisPriceMessage(xmlmessage);
		   }else if("MS045".equals(ah.getAuthHeader().getMsgType())){//临床诊疗项目字典
			   return acceptDiagnosisItemMessage(xmlmessage);
		   }else if("MS032".equals(ah.getAuthHeader().getMsgType())){//科室字典
			   return acceptDeptMessage(xmlmessage);
		   }/*else if("MS048".equals(ah.getAuthHeader().getMsgType())){//人员字典
			   return acceptUserMessage(xmlmessage);
		   }*/
		   else{
			   ResultHeader.setTypeCode("AE");
			   ResultHeader.setText("信息调用webservice错误");
			   String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
			   return reqxml;
		   }
	   }catch(Exception ex){
		   ResultHeader.setTypeCode("AE");
		   ResultHeader.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		   String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
		   return reqxml;
	   }	   
   }
   
   /**
    * 
        * @Title: acceptMessageFee   
        * @Description:科室
        * @param: @param xmlmessage
        * @param: @return      
        * @return: String      
        * @throws
    */
    private String acceptDeptMessage(String xmlmessage){
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

    
    /**
	    * 
	        * @Title: acceptMessageFee   
	        * @Description: 人员
	        * @param: @param xmlmessage
	        * @param: @return      
	        * @return: String      
	        * @throws
	    */
	    private String acceptUserMessage(String xmlmessage){
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
	    
	    /**
	     * 
	         * @Title: acceptPriveMessage   
	         * @Description: 价表   
	         * @param: @param xmlmessage
	         * @param: @return      
	         * @return: String      
	         * @throws
	     */
	    private String acceptPriceMessage(String xmlmessage){
	    	String filetype="resPriceData";    	
	    	PriceDataMessage rb =new PriceDataMessage();
	    	ResultHeader ResultHeader=new ResultHeader();
	    	ResultHeader.setSourceMsgId("");//消息源id需要赋值
			try {	
				TranLogTxt.liswriteEror_to_txt(filetype,"req:"+xmlmessage);
				rb = JaxbUtil.converyToJavaBean(xmlmessage,PriceDataMessage.class);
				try {
					String messeage = this.dataService.savePrice(rb.getControlActProcess().getList());
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
	    
	    
	    /**
		    * 
		        * @Title: acceptMessageFee   
		        * @Description: 临床诊疗项目与价表项目对照表
		        * @param: @param xmlmessage
		        * @param: @return      
		        * @return: String      
		        * @throws
		    */
	private String acceptDiagnosisPriceMessage(String xmlmessage) {
		String filetype = "resDiagnosisItemData";
		DiagnosisPriceDataMessage rb = new DiagnosisPriceDataMessage();
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		try {
			TranLogTxt.liswriteEror_to_txt(filetype, "req:" + xmlmessage);
			rb = JaxbUtil.converyToJavaBean(xmlmessage, DiagnosisPriceDataMessage.class);
			try {
				String messeage = this.dataService.saveAcceptDate(rb.getControlActProcess().getList());
				if (messeage.split("-")[0].equals("ok")) {
					ResultHeader.setTypeCode("AA");
					ResultHeader.setText("交易成功");
				} else {
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText(messeage.split("-")[1]);
				}

			} catch (Exception ex) {
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("费用信息调用webservice错误");
			}

		} catch (Exception ex) {

			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("费用信息 xml解析错误");
		}
		String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
		TranLogTxt.liswriteEror_to_txt(filetype, "req:" + reqxml);
		return reqxml;
	}
		    
		    /**
			    * 
			        * @Title: acceptMessageFee   
			        * @Description:临床诊疗项目字典
			        * @param: @param xmlmessage
			        * @param: @return      
			        * @return: String      
			        * @throws
			    */
	private String acceptDiagnosisItemMessage(String xmlmessage) {
		String filetype = "resDiagnosisItemData";
		DiagnosisItemDataMessage rb = new DiagnosisItemDataMessage();
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		try {
			TranLogTxt.liswriteEror_to_txt(filetype, "req:" + xmlmessage);
			rb = JaxbUtil.converyToJavaBean(xmlmessage, DiagnosisItemDataMessage.class);
			try {
				String messeage = this.dataService.saveDiagnosisItem(rb.getControlActProcess().getList());
				if (messeage.split("-")[0].equals("ok")) {
					ResultHeader.setTypeCode("AA");
					ResultHeader.setText("交易成功");
				} else {
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText(messeage.split("-")[1]);
				}

			} catch (Exception ex) {
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}

		} catch (Exception ex) {
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
		TranLogTxt.liswriteEror_to_txt(filetype, "req:" + reqxml);
		return reqxml;
	}
}
