package com.hjw.webService.client.bjxy;

import java.util.Arrays;

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
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.WebserviceConfigurationService;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CUSTOMEDITSendMessageBJXY {
	private Custom custom=new Custom();
	private ExamInfoUserDTO ei=new ExamInfoUserDTO();
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	
	public CUSTOMEDITSendMessageBJXY(Custom custom){
		this.custom=custom;
		this.ei = configService.getExamInfoForNum(custom.getEXAM_NUM());
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url,String logname) {
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"url:"+url);
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		
		FeeResultBody rb = new FeeResultBody();
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
            
            rb.getResultHeader().setTypeCode("AA");
		    rb.getResultHeader().setText(result);
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
