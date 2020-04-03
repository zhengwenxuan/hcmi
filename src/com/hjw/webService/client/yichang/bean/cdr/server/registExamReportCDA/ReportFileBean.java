package com.hjw.webService.client.yichang.bean.cdr.server.registExamReportCDA;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "examinationFileList")  
@XmlType(propOrder = {})
public class ReportFileBean {
	@XmlElement
	private String	fileType	 = "";//	文件类型
	@XmlElement
	private String	fileUrl	 = "";//	文件地址
	
	public String getFileType() {
		return fileType;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}
