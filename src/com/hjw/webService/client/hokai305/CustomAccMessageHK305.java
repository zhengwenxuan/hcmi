package com.hjw.webService.client.hokai305;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.CustAccBody;
import com.hjw.webService.client.Bean.CustAccResBean;
import com.hjw.webService.client.Bean.CustAccResBody;
import com.hjw.webService.client.hokai305.bean.ResCustomBeanHK305;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CustomAccMessageHK305{
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	/*public CustAccResBody getMessage(String url,CustAccBody custom,String logname) {		
		CustAccResBody resdah=new CustAccResBody();		
		try {			
			JSONObject json = JSONObject.fromObject(custom);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
			String result = HttpUtil.doPost(url,custom,"utf-8");
			//String result="{\"errorInfo\":\"\",\"status\":\"200\",\"patNum\":1,\"customerInfo\":[{\"customerName\":\"丁满足\",\"occupation\":null,\"customerCardNo\":\"0007101827\",\"phone\":\"13506062789\",\"customerOrganization\":\"网络预约—01\",\"customerIdentityNo\":\"350582196812260510\",\"customerChargeType\":\"自费\",\"customerSex\":\"1\",\"customerBirthPlace\":\"350582\",\"customerBirthday\":\"1968-12-26\",\"customerMarriedAge\":\"22\",\"unitInContract\":\"43\",\"customerWebbed\":\"已婚\",\"address\":\"福建省泉州市晋江区(县)\",\"customerPatientId\":\"4800009\",\"customerIdentity\":\"地方\",\"birthPlaceName\":\"福建晋江市\",\"customerNation\":\"汉族\",\"customerSSid\":null}]}";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
            if((result!=null)&&(result.trim().length()>0)){	                    
	                    result = result.trim();
	    				JSONObject jsonobject = JSONObject.fromObject(result);
	    				Map classMap = new HashMap();
						classMap.put("customerInfo", CustAccResBean.class);	 
						resdah = (CustAccResBody)JSONObject.toBean(jsonobject,CustAccResBody.class,classMap);
						if("200".equals(resdah.getStatus())){
							resdah.setStatus("AA");
						}else{
							resdah.setStatus("AE");
						}
	                }else{
	                	resdah.setStatus("AE");
	                	resdah.setErrorInfo(url  +" 返回无返回");
	                }

		} catch (Exception ex) {
			ex.printStackTrace();
			resdah.setStatus("AE");
        	resdah.setErrorInfo("");
		}
		return resdah;
	}*/
	
	
	/**
	 * 查询
	 * @return
	 */
	public CustAccResBody getMessage(String url,CustAccBody custom,String logname){	
		CustAccResBody resdah=new CustAccResBody();		
		ArrayList<CustAccResBean> customerInfo = new ArrayList<CustAccResBean>();
		 
		try{
		StringBuffer sb0=new StringBuffer();
		sb0.append("<PRPA_IN201305UV02 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");  
		sb0.append("  <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/> "); 
		sb0.append("  <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/> "); 
		sb0.append("  <interactionId  extension=\"S0004\"/>"); 
		sb0.append("  <receiver  code=\"SYS001\"/>"); 
		sb0.append("  <sender  code=\"SYS009\"/>");
		sb0.append("  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">  ");
		sb0.append("  <queryByParameter> "); 
		sb0.append("  <id>");
		sb0.append("  <item extension=\""+custom.getCustomerIdentityNo()+"\" root=\"2.16.156.10011.1.3\"/> ");
		sb0.append("  </id>   ");
		
		String sexCode="";
		if("男".equals(custom.getCustomerSex())) {
			sexCode="1";
		} else if("女".equals(custom.getCustomerSex())) {
			sexCode="0";
		} else {
			sexCode="%";
		}
			
		sb0.append("  <name value=\""+custom.getCustomerName()+"\" /> ");
		sb0.append("  <administrativeGenderCode code=\""+sexCode+"\" displayName=\""+custom.getCustomerSex()+"\"/> ");
		sb0.append("  <birthTime value=\""+custom.getCustomerBirthday()+"\" /> ");
		
		sb0.append("  <identityCode code=\"I\" displayName=\"未知\"/> ");
			
		sb0.append("  </queryByParameter>  "); 
		sb0.append("  </controlActProcess>   "); 
		sb0.append("</PRPA_IN201305UV02>	 "); 
		TranLogTxt.liswriteEror_to_txt(logname,"request:"+sb0.toString());
		 String result = HttpUtil.doPost_Str(url,sb0.toString(), "utf-8");
		  TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
		 if(result.trim().length()>0) {
			 try{
					InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
					Map<String, String> xmlMap = new HashMap<>();
					xmlMap.put("abc", "urn:hl7-org:v3");
					SAXReader sax = new SAXReader();
					
					sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
					Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
					resdah.setStatus(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
					boolean Faly = (document.selectNodes("abc:MCCI_IN000002UV01/abc:controlActProcess").size()==0);
					
					String patid = document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension").getText();
					
					//如果"abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension" 为空  则去人员信息中拿患者id
					System.err.println("判断是否有人员信息节点:"+Faly);
					System.err.println("判断是否为空串:"+patid.equals(""));
					if(!Faly && patid.equals("") && patid.length()<=0){
						TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入1111");
						
						Document document2 = document.selectSingleNode("abc:MCCI_IN000002UV01/abc:controlActProcess").getDocument();
						
						TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入2222");
						Element rootElement = document2.getRootElement();
						
						TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入3333:"+rootElement.getName());
						
						Element element = rootElement.element("controlActProcess");
						List<Element> subject1List = element.elements("subject");
						
						
						TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入4444");
						
						for (int i = 0; i < subject1List.size(); i++) {
							CustAccResBean customer = new CustAccResBean();
							
							TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入5555");
							String name = subject1List.get(i).getName();
							TranLogTxt.liswriteEror_to_txt(logname,  subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension").getText());
							TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入6666");
							
							//患者id
							customer.setCustomerPatientId(subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension").getText());
							//身份证号
							customer.setCustomerIdentityNo(subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:patientPerson/abc:id/abc:item[@root='2.16.156.10011.1.3']/@extension").getText());
							//姓名
							customer.setCustomerName(subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:patientPerson/abc:name/@value").getText());
							//性别
							customer.setCustomerSex(subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:patientPerson/abc:administrativeGenderCode/@code").getText());
							//身份类型  军人
							customer.setCustomerIdentity(subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:patientPerson/abc:identityCode/@displayName").getText());
							//年龄
							//customer.setAge(subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:patientPerson/abc:citizenCode/@value").getText());
							//地址
							customer.setBirthPlaceName(subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:patientPerson/abc:addr/abc:part[@type='SAL']/@value").getText());
						
							customerInfo.add(customer);
							
							TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入6666");
							TranLogTxt.liswriteEror_to_txt(logname, "获取人员信息节点的患者id:" + subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension").getText() + "\r\n");
							TranLogTxt.liswriteEror_to_txt(logname, "下面节点患者id:" + customer.getCustomerPatientId() + "\r\n");
						}
						
					}else{
						//如果"abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension" 不为空  则直接拿此节点的 患者id
						resdah.getCustomerInfo().get(0).setCustomerPatientId(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension").getText());
						TranLogTxt.liswriteEror_to_txt(logname, "上面节点患者id:" + resdah.getCustomerInfo().get(0).getCustomerPatientId() + "\r\n");
					}
					resdah.setCustomerInfo(customerInfo);
					}catch(Exception ex){
						try{
							InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
							Map<String, String> xmlMap = new HashMap<>();
							xmlMap.put("abc", "urn:hl7-org:v3");
							SAXReader sax = new SAXReader();
							sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
							Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
							resdah.setStatus(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
							resdah.setErrorInfo(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:acknowledgementDetail/abc:text/@value").getText());
							}catch(Exception ext){
								resdah.setStatus("AE");
								resdah.setErrorInfo(com.hjw.interfaces.util.StringUtil.formatException(ex));
								
								
							}
					}
		  }else{
			  resdah.setStatus("AE");
		  }
		 
		}catch(Exception ex){
			resdah.setStatus("AE");
		}
		return resdah;
	}
	
}
