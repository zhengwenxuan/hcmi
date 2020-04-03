package com.hjw.webService.service;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.service.bean.ResultSerBody;
import com.hjw.wst.DTO.UserFeeDTO;
import com.hjw.wst.service.CommService;


public class AcceptFeeMessage extends ServletEndpointSupport {
	private CommService commService;

	protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
	}


   public String acceptMessageTest(String xmlmessage){	   
	   return "返回ok----接受缴费"+xmlmessage; 
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
    public String acceptMessageFee(String xmlmessage){
    	String filetype="resHis";    	
    	ResultSerBody rb =new ResultSerBody();
    	ResultHeader ResultHeader=new ResultHeader();
    	ResultHeader.setSourceMsgId("");//消息源id需要赋值
    	FeeReqBody frb= new FeeReqBody();
    	String orderid="";
		try {	
			TranLogTxt.liswriteEror_to_txt(filetype,"req:"+xmlmessage);
			rb = JaxbUtil.converyToJavaBean(xmlmessage,ResultSerBody.class);
			try {
				//信息入账	
				UserFeeDTO uf = new UserFeeDTO();
				uf = this.commService.saveFeesResult(rb);
				if(uf.isFlags()){
				   this.commService.sendPacsLis(uf);
					ResultHeader.setTypeCode("AA");
					ResultHeader.setText("交易成功");
				}else{
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText(uf.getError());
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("费用信息调用webservice错误");
			}

		} catch (Exception ex){

			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("费用信息 xml解析错误");
		}
		frb.setResultHeader(ResultHeader);
		String reqxml = JaxbUtil.convertToXml(frb, true);
		TranLogTxt.liswriteEror_to_txt(filetype,"req:"+orderid+":"+reqxml);
		return reqxml;   	   
   }
}
