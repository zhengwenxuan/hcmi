package com.hjw.webService.client.yichang.bean.caixueguan.out;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "job")  
@XmlType(propOrder = {})
public class JobOut {

	//1,2,3,4接口共用
	@XmlElement
	private int ret;//返回值，1：成功，0：失败
	@XmlElement
	private String errormsg = "";//错误信息
	
	//3.获取检验科采血人员信息
	@XmlElement
	private String userid;//用户id
	@XmlElement
	private String username;//用户姓名
	
	//1.获取病人所有项目信息及条码信息
	@XmlElement
	private String name;//患者名
	@XmlElement
	private String sex;//患者性别
	@XmlElement
	private String bdate;//患者生日
	@XmlElement
	private String age;//患者年龄
	@XmlElement
	private String numlabel;//labels中的item项目个数
	@XmlElement
	private LabelsReqForm labels;
	
	public static void main(String[] args) throws Exception {
		JobOut jobUserOut = new JobOut();
		LabelsReqForm labels = new LabelsReqForm();
		labels.getItem().add(new ItemReqForm());
		labels.getItem().add(new ItemReqForm());
		jobUserOut.setLabels(labels);
		String xml = JaxbUtil.convertToXml(jobUserOut, true);
		System.out.println(xml);
	}

	public int getRet() {
		return ret;
	}

	public String getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public String getName() {
		return name;
	}

	public String getSex() {
		return sex;
	}

	public String getBdate() {
		return bdate;
	}

	public String getAge() {
		return age;
	}

	public String getNumlabel() {
		return numlabel;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setBdate(String bdate) {
		this.bdate = bdate;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setNumlabel(String numlabel) {
		this.numlabel = numlabel;
	}

	public LabelsReqForm getLabels() {
		return labels;
	}

	public void setLabels(LabelsReqForm labels) {
		this.labels = labels;
	}
}
