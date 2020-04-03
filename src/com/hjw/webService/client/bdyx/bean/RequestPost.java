package com.hjw.webService.client.bdyx.bean;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;

import net.sf.json.JSONObject;

public class RequestPost {

	private static String HOSPITAL_ID = "";
	private static String REGISTREED_DEPT = "";
	private static String SEND_SYS_ID = "";
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		HOSPITAL_ID = configService.getCenterconfigByKey("HOSPITAL_ID").getConfig_value();
		REGISTREED_DEPT = configService.getCenterconfigByKey("REGISTREED_DEPT").getConfig_value();
		SEND_SYS_ID = configService.getCenterconfigByKey("SEND_SYS_ID").getConfig_value();
	}
	
	private String hospital_id = HOSPITAL_ID;//医疗机构编码
	private String service_id = "";//服务ID
	private String visit_type = "04";//数据参照就诊类型字典 就诊类型编码
	private String apply_unit_id = REGISTREED_DEPT;//申请科室编码
	private String exec_uint_id = "";//执行科室编码
	private String send_sys_id = SEND_SYS_ID;//发送系统ID		体检系统id
	private String order_exec_id = "";//医嘱执行分类编码		检查项目字典、检验项目字典、医嘱字典	中医嘱小分类字段对应的内容，需要提前梳理
	private String extend_sub_id = "";//扩展码	预留扩展码，EMPI为域ID
	private boolean isJson = true;//body是否为json格式
	
//	private String head = "112672:BS007:01::2010100:IIH:0:";
	private String body = "";
	
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	public void setExec_uint_id(String exec_uint_id) {
		this.exec_uint_id = exec_uint_id;
	}
	public void setOrder_exec_id(String order_exec_id) {
		this.order_exec_id = order_exec_id;
	}
	public void setExtend_sub_id(String extend_sub_id) {
		this.extend_sub_id = extend_sub_id;
	}
	public String getHead() {
		return hospital_id+":"+service_id+":"+visit_type+":"+apply_unit_id+":"+exec_uint_id+":"+send_sys_id+":"+order_exec_id+":"+extend_sub_id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public boolean getIsJson() {
		return isJson;
	}
	public void setIsJson(boolean isJson) {
		this.isJson = isJson;
	}
	public static void main(String[] args) {
		RegisterReq req = new RegisterReq();
		String str = JSONObject.fromObject(req).toString();
		
		RequestPost requestBDYX = new RequestPost();
		requestBDYX.setService_id("BS001");
		requestBDYX.setExec_uint_id("0101004");
		requestBDYX.setOrder_exec_id("");
		requestBDYX.setExtend_sub_id("");
//		requestBDYX.setBody(str);
		String requestStr = JSONObject.fromObject(requestBDYX).toString();
		System.out.println(requestStr);
	}
}
