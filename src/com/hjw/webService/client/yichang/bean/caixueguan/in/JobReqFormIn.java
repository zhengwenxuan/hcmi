package com.hjw.webService.client.yichang.bean.caixueguan.in;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "job")  
@XmlType(propOrder = {})
public class JobReqFormIn {

	@XmlElement
	private String cid = "";//患者卡号
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}

	public static void main(String[] args) throws Exception {
		String xml = "<job>  <cid>12345789</cid><!--患者卡号--></job>";
		JobReqFormIn user = JaxbUtil.converyToJavaBean(xml, JobReqFormIn.class);
		System.out.println(user.getCid());
	}
	
}
