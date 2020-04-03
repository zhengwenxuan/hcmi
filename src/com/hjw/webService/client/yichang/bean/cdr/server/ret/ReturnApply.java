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
public class ReturnApply {

	@XmlElement
	private String message = "SUCCESS";
	@XmlElement
	private String status = "AA";
	@XmlElement
	private QueryRegularApplicList queryRegularApplicList = new QueryRegularApplicList();
	
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

	public QueryRegularApplicList getQueryRegularApplicList() {
		return queryRegularApplicList;
	}

	public void setQueryRegularApplicList(QueryRegularApplicList queryRegularApplicList) {
		this.queryRegularApplicList = queryRegularApplicList;
	}

	public static void main(String[] args) throws Exception {
		ReturnApply returnYC = new ReturnApply();
//		QueryRegularApplic queryRegularApplic = new QueryRegularApplic();
//		queryRegularApplic.getItemList().getItem().add(new ItemYC());
//		queryRegularApplic.getItemList().getItem().add(new ItemYC());
//		returnYC.getQueryRegularApplicList().getQueryRegularApplic().add(queryRegularApplic);
//		returnYC.getQueryRegularApplicList().getQueryRegularApplic().add(queryRegularApplic);
		System.out.println(JaxbUtil.convertToXml(returnYC, true));
	}
	
}
