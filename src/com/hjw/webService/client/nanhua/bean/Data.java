package com.hjw.webService.client.nanhua.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Data")  
@XmlType(propOrder = {})
public class Data{
	
	@XmlElement
	private String tjno;//体检号
	
	@XmlElement
	private String name;//姓名
	
	@XmlElement
	private String sex;//性别
	
	@XmlElement
	private String age;//年龄
	
	@XmlElement
	private String hisno;//挂号序号
	
	@XmlElement
	private String hisfph;//发票号
	
	@XmlElement
	private String zje;//总金额
	
	@XmlElement
	private String tfsqr;//退费申请人
	
	@XmlElement
	private String tfsqrq;//退费申请日期
	
	@XmlElement
	private String cxrq;//查询日期
	
	@XmlElement
	private String qshm;//起始号码
	
	@XmlElement
	private String jshm;//结束号码

	public String getTjno() {
		return tjno;
	}

	public void setTjno(String tjno) {
		this.tjno = tjno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getHisno() {
		return hisno;
	}

	public void setHisno(String hisno) {
		this.hisno = hisno;
	}

	public String getHisfph() {
		return hisfph;
	}

	public void setHisfph(String hisfph) {
		this.hisfph = hisfph;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getCxrq() {
		return cxrq;
	}

	public void setCxrq(String cxrq) {
		this.cxrq = cxrq;
	}

	public String getTfsqr() {
		return tfsqr;
	}

	public void setTfsqr(String tfsqr) {
		this.tfsqr = tfsqr;
	}

	public String getTfsqrq() {
		return tfsqrq;
	}

	public void setTfsqrq(String tfsqrq) {
		this.tfsqrq = tfsqrq;
	}

	public String getQshm() {
		return qshm;
	}

	public void setQshm(String qshm) {
		this.qshm = qshm;
	}

	public String getJshm() {
		return jshm;
	}

	public void setJshm(String jshm) {
		this.jshm = jshm;
	}

}
