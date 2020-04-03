package com.hjw.webService.client.sxwn.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.util.StringUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Table")  
@XmlType(propOrder = {})  
public class TableItemBean {
	@XmlElement
  private String Num="";
	@XmlElement
  private String Name="";
	
	public String getNum() {
		return Num;
	}
	public void setNum(String num) {
		this.Num = num;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
		this.Name=StringUtil.escapeExprSpecialWord(this.Name);  
	}
	
}
