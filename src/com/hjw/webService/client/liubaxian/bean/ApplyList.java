package com.hjw.webService.client.liubaxian.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ApplyList")  
@XmlType(propOrder = {})
public class ApplyList {

	@XmlElement
	private List<Apply> Apply = new ArrayList<>();

	public List<Apply> getApply() {
		return Apply;
	}

	public void setApply(List<Apply> apply) {
		Apply = apply;
	}	

	public static void main(String[] args) {
		ApplyList applyList = new ApplyList();
		applyList.getApply().add(new Apply());
		applyList.getApply().add(new Apply());
		System.out.println(JaxbUtil.convertToXml(applyList, true));
	}
}
