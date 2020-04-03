package com.hjw.webService.client.body;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.Person;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description: 消息体 
     * @author: yangm     
     * @date:   2016年10月7日 上午11:08:27   
     * @version V2.0.0.0
 */
public class LisMessageBody extends MessageHeader {

	private String part_name;// 医院名称
	private Person custom=new Person();//人员信息
	private Doctor doctor=new Doctor();//医生科室	
	
	private List<LisComponents> components=new ArrayList<LisComponents>();//一个检查消息中可以由多个申请单		
	private String cjfs="";//采集方式 中联使用
    private String zxksid="";//执行科室编码 中联使用   
    
	public String getZxksid() {
		return zxksid;
	}

	public void setZxksid(String zxksid) {
		this.zxksid = zxksid;
	}

	public String getCjfs() {
		return cjfs;
	}

	public void setCjfs(String cjfs) {
		this.cjfs = cjfs;
	}
	public List<LisComponents> getComponents() {
		return components;
	}
	public void setComponents(List<LisComponents> components) {
		this.components = components;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public String getPart_name() {
		return part_name;
	}
	public void setPart_name(String part_name) {
		this.part_name = part_name;
	}
	public Person getCustom() {
		return custom;
	}
	public void setCustom(Person custom) {
		this.custom = custom;
	}

}
