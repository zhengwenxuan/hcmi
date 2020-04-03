package com.hjw.webService.client.xintong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;

public class Gettoken {

	public static String Gettokens(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "验证登录开始---统一登录的tokens" );
		StringBuffer sb = new StringBuffer();
		HIPMessageServiceService damQuery = new HIPMessageServiceServiceLocator(url);
		IHIPMessageService damQuerys;
		
		try {
			damQuerys = damQuery.getHIPMessageServicePort();
			
			String tokenXT10004 = getTokenXT10004(logname);
			TranLogTxt.liswriteEror_to_txt(logname, "验证入参----统一登录的tokens====" + tokenXT10004);
			
			String res = damQuerys.HIPMessageServer2016Ext("XT00004", "",tokenXT10004);
			
			TranLogTxt.liswriteEror_to_txt(logname, "验证返回---统一登录的tokens====" + res);
			
			if ((res != null) && (res.trim().length() > 0)) {
				
				ResultHeader rh = restoken(res);
				if(rh.getTypeCode().equals("1")){
					return  rh.getText();
				}else{
					return "AE";
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "AE";
			
	}
	
	
		public static ResultHeader restoken(String resgetjzh) {
			ResultHeader rh= new ResultHeader();
			rh.setTypeCode("AE");
			try{
			InputStream is = new ByteArrayInputStream(resgetjzh.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			
			rh.setTypeCode(document.selectSingleNode("RESPONSE/RETURN_CODE").getText());// 获取消息成功失败 节点;
			rh.setSourceMsgId(document.selectSingleNode("RESPONSE/RESP_MSGID").getText());//消息ID
			rh.setText(document.selectSingleNode("RESPONSE/REG_INFO/ACCESS_TOKEN").getText());//获取token
			}catch(Exception ex){
				rh.setTypeCode("AE");
				rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		   
		    return rh;
		}
	
		
		
		//登录认证
		public static String getTokenXT10004(String logname) {
			
			StringBuffer bufferXml = new StringBuffer();
			bufferXml.append(" <REQUEST>                                                             ");
			bufferXml.append(" <TOKENID></TOKENID>                                                     ");
			bufferXml.append(" <!--消息发送方在平台上的注册标识-->                                   ");
			bufferXml.append(" <SENDER>HJW</SENDER>                                                     ");
			bufferXml.append(" <!--请求消息ID,由消息发送方生成单次请求唯一的标识，建议用uuid 32位--> ");
			bufferXml.append(" <REQ_MSGID>"+UUID.randomUUID().toString().toLowerCase()+"</REQ_MSGID>                                               ");
			bufferXml.append(" 	<REQ_PARAMS>                                                         ");
			bufferXml.append(" <!--用户名-->                                                         ");
			bufferXml.append(" <USERNAME>HJW</USERNAME>                                                 ");
			bufferXml.append(" <!--密码（未加密的密码）-->                                           ");
			bufferXml.append(" <PASSWORD>123456</PASSWORD>                                                 ");
			bufferXml.append(" <!--机构编码 -->                                                      ");
			bufferXml.append(" <ORG_CODE></ORG_CODE>                                                 ");
			bufferXml.append(" </REQ_PARAMS>                                                         ");
			bufferXml.append(" </REQUEST>                                                            ");
			TranLogTxt.liswriteEror_to_txt(logname, "验证拼接入参---统一登录的tokens====" +  bufferXml.toString());
			return bufferXml.toString();
		}
}
