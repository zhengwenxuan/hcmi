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
public class JobClearSampleIn {

	@XmlElement
	private String barcode = "";//条码号
	@XmlElement
	private String userID = "";//用户id
	@XmlElement
	private String windowNo = "";//窗口号

	public static void main(String[] args) throws Exception {
		String xml = "<job><barcode>2016102000001</barcode><!--条码号--><userID>用户id</userID><!--用户id--><windowNo>窗口号</windowNo><!--窗口号--></job>";
		JobClearSampleIn user = JaxbUtil.converyToJavaBean(xml, JobClearSampleIn.class);
		System.out.println(user.getBarcode());
	}

	public String getBarcode() {
		return barcode;
	}

	public String getUserID() {
		return userID;
	}

	public String getWindowNo() {
		return windowNo;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setWindowNo(String windowNo) {
		this.windowNo = windowNo;
	}
}
