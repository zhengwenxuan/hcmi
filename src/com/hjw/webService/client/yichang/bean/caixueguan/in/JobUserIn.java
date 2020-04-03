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
public class JobUserIn {

	@XmlElement
	private String userid = "";
	@XmlElement
	private String password  = "";
	
	public String getUserid() {
		return userid;
	}
	public String getPassword() {
		return password;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public static void main(String[] args) throws Exception {
		String xml = "<job><userid>用户id</userid><!--用户id--><password>用户密码</password><!--密码--></job>";
		JobUserIn user = JaxbUtil.converyToJavaBean(xml, JobUserIn.class);
		System.out.println(user.getUserid());
	}
	
}
