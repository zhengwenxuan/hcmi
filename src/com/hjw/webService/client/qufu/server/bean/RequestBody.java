package com.hjw.webService.client.qufu.server.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.webService.client.Bean.ThridInterfaceLog;


import org.dom4j.Document;
import org.dom4j.Node;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "requestBody")  
@XmlType(propOrder = {})
public class RequestBody {

	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	@XmlElement
	private String STARTTIME;//查询时间起（非空 格式：yyyyMMddHHmmss）
	
	@XmlElement
	private String ENDTIME;//查询时间止（非空 格式：yyyyMMddHHmmss）
	
	@XmlElement
	private String EXAM_NO;//体检编号
	
	@XmlElement
	private String PROJECT_TYPE;//判断字段(0-PACS,1 -LIS)
	
	@XmlElement
	private String EXECDEPTID;//执行科室编码
	
	@XmlElement
	private String BAR_CODE;//申请单号
	
	@XmlElement
	private String FLAG;//检查标志 0-未检查 1-已检查 2-已完成报告

	private RequestBody() {};
			
	public RequestBody(String xmlmessage, ThridInterfaceLog til) {
		try {
			xmlmessage = "<TEMP>" + xmlmessage + "</TEMP>";
			InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			Node node = document.selectSingleNode("/TEMP/STARTTIME");
			if(node != null) {
				this.STARTTIME = document.selectSingleNode("/TEMP/STARTTIME").getText().trim();  
			}
			node = document.selectSingleNode("/TEMP/ENDTIME");
			if(node != null) {
				this.ENDTIME = document.selectSingleNode("/TEMP/ENDTIME").getText().trim();  
			}
			node = document.selectSingleNode("/TEMP/EXAM_NO");
			if(node != null) {
				this.EXAM_NO = document.selectSingleNode("/TEMP/EXAM_NO").getText().trim();  
			}
			node = document.selectSingleNode("/TEMP/PROJECT_TYPE");
			if(node != null) {
				this.PROJECT_TYPE = document.selectSingleNode("/TEMP/PROJECT_TYPE").getText().trim();  
			}
			node = document.selectSingleNode("/TEMP/EXECDEPTID");
			if(node != null) {
				this.EXECDEPTID = document.selectSingleNode("/TEMP/EXECDEPTID").getText().trim();  
			}
			node = document.selectSingleNode("/TEMP/BAR_CODE");
			if(node != null) {
				this.BAR_CODE = document.selectSingleNode("/TEMP/BAR_CODE").getText().trim();  
			}
			node = document.selectSingleNode("/TEMP/FLAG");
			if(node != null) {
				this.FLAG = document.selectSingleNode("/TEMP/FLAG").getText().trim();  
			}
		} catch (Throwable e) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new RequestBody("<STARTTIME>查询时间起（非空 格式：yyyyMMddHHmmss）</STARTTIME><ENDTIME>查询时间止（非空 格式：yyyyMMddHHmmss）</ENDTIME><EXAM_NO>体检编号</EXAM_NO><PROJECT_TYPE>判断字段(0-PACS,1 -LIS)</PROJECT_TYPE><EXECDEPTID>执行科室编码</EXECDEPTID>", new ThridInterfaceLog());
		System.out.println();
	}

	public String getSTARTTIME() {
		return STARTTIME;
	}

	public void setSTARTTIME(String sTARTTIME) {
		STARTTIME = sTARTTIME;
	}

	public String getENDTIME() {
		return ENDTIME;
	}

	public void setENDTIME(String eNDTIME) {
		ENDTIME = eNDTIME;
	}

	public String getEXAM_NO() {
		return EXAM_NO;
	}

	public void setEXAM_NO(String eXAM_NO) {
		EXAM_NO = eXAM_NO;
	}

	public String getPROJECT_TYPE() {
		return PROJECT_TYPE;
	}

	public void setPROJECT_TYPE(String pROJECT_TYPE) {
		PROJECT_TYPE = pROJECT_TYPE;
	}

	public String getEXECDEPTID() {
		return EXECDEPTID;
	}

	public void setEXECDEPTID(String eXECDEPTID) {
		EXECDEPTID = eXECDEPTID;
	}

	public String getBAR_CODE() {
		return BAR_CODE;
	}

	public void setBAR_CODE(String bAR_CODE) {
		BAR_CODE = bAR_CODE;
	}

	public String getFLAG() {
		return FLAG;
	}

	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}
}
