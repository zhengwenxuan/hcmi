package com.hjw.webService.client.bjxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.interfaces.util.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.bjxy.bean.PATIENT;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;

public class CUSTOMSendMessageBJXY {

	private static ConfigService configService;
	private Custom custom=new Custom();
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public CUSTOMSendMessageBJXY(Custom custom){
		this.custom=custom;
	}
	
	public static void main(String[] args) {/*
		try {
            String endpoint = "http://10.2.30.102:8090/CHISWebSvr.dll/soap/IHISimpl";

            
             * Create new Service and Call objects. These are the standard
             * JAX-RPC objects that are used to store metadata about the service
             * to invoke.
             
            Service service = new Service();
            Call call = (Call) service.createCall();

            // Set up endpoint URL - this is the destination for our SOAP
            // message.
            call.setTargetEndpointAddress(endpoint);

            // 设置调用WS的方法
            call.setOperationName(new QName("http://schemas.xmlsoap.org/wsdl/", "FounderChisRequireData"));

            // 注册序列化/反序列化器
//            QName qn = new QName("http://webservice.baointl.com/", "pushRespMsg");
//            call.registerTypeMapping(PushRespMsg.class, qn, null, new BeanDeserializerFactory(
//                    PushRespMsg.class, qn));

            // 设置参数名:
            call.addParameter("userName", // 参数名
                    org.apache.axis.Constants.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'
            call.addParameter("passWord", // 参数名
                    org.apache.axis.Constants.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'
            call.addParameter("businessType", // 参数名
                    org.apache.axis.Constants.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'
            call.addParameter("requestData", // 参数名
                    org.apache.axis.Constants.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'
            // 设置返回值类型
            call.setReturnType(new QName("http://schemas.xmlsoap.org/wsdl/", "FounderChisRequireData"), String.class);

            Object[] object = new Object[4];
            object[0] = "20002";
            object[1] = "1";
            object[2] = "TJ0001";
            object[3] = "<root>"
            		+ "<row>"
            		+ "<birthday>1985-01-04 00:00:00</birthday>"
            		+ "<home_street>北京市海淀区西苑操场1号</home_street>"
            		+ "<home_street>home_district</home_street>"
            		+ "<home_tel>15811597887</home_tel>"
            		+ "<name>张三</name>"
            		+ "<sex>1</sex>"
            		+ "<social_no>130222198001045213</social_no>"
            		+ "</row>"
            		+ "</root>";
            String result = (String) call.invoke(object);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
	*/}

	public ResultBody getMessage(String url,String logname) {
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"url:"+url);
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		
		ResultBody rb = new ResultBody();
		ExamInfoUserDTO eu= configService.getExamInfoForNum(custom.getEXAM_NUM());
		String sexcode = "0";
		if("男".equals(eu.getSex()) || "男性".equals(eu.getSex())) {
			sexcode = "1";
		} else if("女".equals(eu.getSex()) || "女性".equals(eu.getSex())) {
			sexcode = "2";
		}
		if(StringUtil.isEmpty(eu.getAddress())) {
			eu.setAddress("北京");
		}
		if(StringUtil.isEmpty(eu.getPhone())) {
			eu.setPhone("12345678901");
		}
		if(StringUtil.isEmpty(eu.getId_num())) {
			eu.setId_num(eu.getExam_num());
		}
		try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(url);
            // 设置调用WS的方法
            call.setOperationName(new QName("http://schemas.xmlsoap.org/wsdl/", "FounderChisRequireData"));
	            // 注册序列化/反序列化器
//	            QName qn = new QName("http://webservice.baointl.com/", "pushRespMsg");
//	            call.registerTypeMapping(PushRespMsg.class, qn, null, new BeanDeserializerFactory(
//	                    PushRespMsg.class, qn));
            // 设置参数名:
            call.addParameter("userName", // 参数名
                    org.apache.axis.Constants.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'
            call.addParameter("passWord", // 参数名
                    org.apache.axis.Constants.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'
            call.addParameter("businessType", // 参数名
                    org.apache.axis.Constants.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'
            call.addParameter("requestData", // 参数名
                    org.apache.axis.Constants.XSD_STRING,// 参数类型:String
                    ParameterMode.IN);// 参数模式：'IN' or 'OUT'
            // 设置返回值类型
            call.setReturnType(new QName("http://schemas.xmlsoap.org/wsdl/", "FounderChisRequireData"), String.class);

            Object[] object = new Object[4];
            object[0] = "20002";
            object[1] = "1";
            object[2] = "TJ0001";
            object[3] = "<root>"
		            		+ "<row>"
		            		+ "<birthday>"+eu.getBirthday()+"</birthday>"
		            		+ "<home_street>"+eu.getAddress()+"</home_street>"
		            		+ "<home_tel>"+eu.getPhone()+"</home_tel>"
		            		+ "<name>"+eu.getUser_name()+"</name>"
		            		+ "<patient_id>"+eu.getPatient_id()+"</patient_id>"
		            		+ "<sex>"+sexcode+"</sex>"
		            		+ "<social_no>"+eu.getId_num()+"</social_no>"
		            		+ "</row>"
		        		+ "</root>";
            TranLogTxt.liswriteEror_to_txt(logname,"request:"+Arrays.toString(object));
            String result = (String) call.invoke(object);
            TranLogTxt.liswriteEror_to_txt(logname,"发送病人信息返回:"+result);
            System.out.println(result);
            
            PATIENT paitent = JaxbUtil.converyToJavaBean(result, PATIENT.class);
            List<CustomResBean> LIST = new ArrayList<CustomResBean>();
			CustomResBean cr = new CustomResBean();
			cr.setPATIENT_ID(paitent.getPatient_id());
			LIST.add(cr);
			ControlActProcess ca = new ControlActProcess();
			ca.setLIST(LIST);
			rb.setControlActProcess(ca);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("交易成功");
		} catch (Exception ex){
			ex.printStackTrace();
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("发送病人信息-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"发送病人信息-调用webservice错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		xml = JaxbUtil.convertToXmlWithOutHead(rb, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"res:"+xml);
		return rb;
	}
}
