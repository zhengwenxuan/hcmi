package com.hjw.webService.client.yichang.bean.cdr.server.ret;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ROOT")  
@XmlType(propOrder = {})
public class ReturnStatus {

	@XmlElement
	private int MARK = 1;//<!-- -1 失败 --><!-- 1 成功 -->
	@XmlElement
	private String MSG = "";

	public int getMARK() {
		return MARK;
	}
	public String getMSG() {
		return MSG;
	}
	public void setMARK(int mARK) {
		MARK = mARK;
	}
	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public static void main(String[] args) throws Exception {
		ReturnStatus returnYC = new ReturnStatus();
		returnYC.setMSG("错误信息");
		System.out.println(JaxbUtil.convertToXmlWithOutHead(returnYC, true));
	}
}
