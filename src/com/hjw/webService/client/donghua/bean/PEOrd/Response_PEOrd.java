package com.hjw.webService.client.donghua.bean.PEOrd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Response")  
@XmlType(propOrder = {})
public class Response_PEOrd {

	@XmlElement
	private String ResultCode = "";//0：成功 非0：失败
	@XmlElement
	private String ResultContent = "";//失败原因
	@XmlElement
	private OrdList_Response OrdList = new OrdList_Response();//医嘱信息集合节点
	
	public static void main(String[] args) throws Exception {
		String xml = ""
+"<Response>"
+"<ResultCode>0</ResultCode>"
+"<ResultContent></ResultContent>"
+"<OrdList>"
+"<OrdInfo>"
+"<OrdID>oid1</OrdID>"
+"<OrdRowID></OrdRowID>"
+"</OrdInfo>"
+"<OrdInfo>"
+"<OrdID></OrdID>"
+"<OrdRowID></OrdRowID>"
+"</OrdInfo>"
+"</OrdList>"
+"</Response>";
		Response_PEOrd response = JaxbUtil.converyToJavaBean(xml, Response_PEOrd.class);
		System.out.println(response.getResultCode());
		System.out.println(response.getOrdList().getOrdInfo().get(0).getOrdID());
	}
	
	public String getResultCode() {
		return ResultCode;
	}
	public String getResultContent() {
		return ResultContent;
	}
	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}
	public void setResultContent(String resultContent) {
		ResultContent = resultContent;
	}
	public OrdList_Response getOrdList() {
		return OrdList;
	}
	public void setOrdList(OrdList_Response ordList) {
		OrdList = ordList;
	}
}
