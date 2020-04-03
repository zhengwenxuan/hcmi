package com.hjw.webService.client.donghua.bean.PEStop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Response")  
@XmlType(propOrder = {})
public class Response_PEStop {

	@XmlElement
	private String ResultCode = "";//0：成功 非0：失败
	@XmlElement
	private String ResultContent = "";//失败原因
	@XmlElement
	private PEStopOrdInfo PEStopOrdInfo = new PEStopOrdInfo();
	
	public static void main(String[] args) throws Exception {
		String xml = ""
+"<Response>"
+"<ResultCode>0</ResultCode>"
+"<ResultContent>成功</ResultContent>"
+"<PEStopOrdInfo>"
+"<PEOrdRowID>21265991||16</PEOrdRowID>"
+"<RowIDCode>0</RowIDCode>"
+"<RowIDContent>修改成功</RowIDContent>"
+"</PEStopOrdInfo>"
+"</Response>";
		Response_PEStop response = JaxbUtil.converyToJavaBean(xml, Response_PEStop.class);
		System.out.println(response.getPEStopOrdInfo().getPEOrdRowID());
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
	public PEStopOrdInfo getPEStopOrdInfo() {
		return PEStopOrdInfo;
	}
	public void setPEStopOrdInfo(PEStopOrdInfo pEStopOrdInfo) {
		PEStopOrdInfo = pEStopOrdInfo;
	}
}
