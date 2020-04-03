package com.hjw.webService.client.donghua.bean.PEOrd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Request")  
@XmlType(propOrder = {})
public class Request_PEOrd {

	private String RegNo = "";//体检登记号
	private String AdmNo = "";//体检就诊号 标识此次体检的ID
	private String RegDateTime = "";//体检登记时间 YYYY-MM-DD hh:mm:ss
	private String SendFlag = "";//申请单状态
	private OrdList_Request OrdList = new OrdList_Request();//医嘱信息集合节点
	
	public static void main(String[] args) throws Exception {
		Request_PEOrd request = new Request_PEOrd();
		request.getOrdList().getOrdInfo().add(new OrdInfo_Request());
		request.getOrdList().getOrdInfo().add(new OrdInfo_Request());
		request.getOrdList().getOrdInfo().add(new OrdInfo_Request());
		String xml = JaxbUtil.convertToXmlWithOutHead(request, true);
		System.out.println(xml);
	}

	public String getRegNo() {
		return RegNo;
	}

	public String getAdmNo() {
		return AdmNo;
	}

	public String getRegDateTime() {
		return RegDateTime;
	}

	public String getSendFlag() {
		return SendFlag;
	}

	public void setRegNo(String regNo) {
		RegNo = regNo;
	}

	public void setAdmNo(String admNo) {
		AdmNo = admNo;
	}

	public void setRegDateTime(String regDateTime) {
		RegDateTime = regDateTime;
	}

	public void setSendFlag(String sendFlag) {
		SendFlag = sendFlag;
	}

	public OrdList_Request getOrdList() {
		return OrdList;
	}

	public void setOrdList(OrdList_Request ordList) {
		OrdList = ordList;
	}
}
