package com.hjw.webService.client.yichang.bean.cdr.server.ret;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic.ItemYC;
import com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic.QueryRegularApplic;
import com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic.QueryRegularApplicList;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "return")  
@XmlType(propOrder = {})
public class ReturnReport {

	@XmlElement
	private String message = "SUCCESS";
	@XmlElement
	private String status = "AA";
	
	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static void main(String[] args) throws Exception {
		ReturnReport returnYC = new ReturnReport();
		QueryRegularApplicList queryRegularApplicList = new QueryRegularApplicList();
		QueryRegularApplic queryRegularApplic = new QueryRegularApplic();
		queryRegularApplic.getItemList().getItem().add(new ItemYC());
		queryRegularApplic.getItemList().getItem().add(new ItemYC());
		queryRegularApplicList.getQueryRegularApplic().add(queryRegularApplic);
		queryRegularApplicList.getQueryRegularApplic().add(new QueryRegularApplic());
		System.out.println(JaxbUtil.convertToXml(returnYC, true));
	}
	
}
