package com.hjw.webService.client.body;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Bean.PacsComponents;
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
public class PacsMessageBody extends MessageHeader {

	private String part_name;// 医院名称
	private Person custom= new Person();//人员信息
	private Doctor doctor= new Doctor();//医生科室	
	private List<PacsComponents> components=new ArrayList<PacsComponents>();//一个检查消息中可以由多个申请单		
	public List<PacsComponents> getComponents() {
		return components;
	}
	public void setComponents(List<PacsComponents> components) {
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
