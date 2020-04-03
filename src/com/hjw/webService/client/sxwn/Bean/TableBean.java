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
public class TableBean {
	@XmlElement
  private String num="";
	@XmlElement
  private String name="";
	@XmlElement
  private String iSampleId="";//样本ID
	@XmlElement
  private String TubeID="";//试管ID
	@XmlElement
  private String testtubeName="";//试管名称
	
	public String getiSampleId() {
		return iSampleId;
	}
	public void setiSampleId(String iSampleId) {
		this.iSampleId = iSampleId;
	}

	public String getTubeID() {
		return TubeID;
	}
	public void setTubeID(String tubeID) {
		TubeID = tubeID;
	}
	public String getTesttubeName() {
		return testtubeName;
	}
	public void setTesttubeName(String testtubeName) {
		this.testtubeName = testtubeName;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		this.name=StringUtil.escapeExprSpecialWord(this.name);  
	}	

}
