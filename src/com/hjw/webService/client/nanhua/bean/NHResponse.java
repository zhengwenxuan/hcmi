package com.hjw.webService.client.nanhua.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Response")  
@XmlType(propOrder = {})
public class NHResponse{
	@XmlElement(name = "Head")
	private Head Head;
	
	@XmlElement(name = "Info")
	private List<Info> Info;

	public Head getHead() {
		return Head;
	}

	public void setHead(Head head) {
		Head = head;
	}

	public List<Info> getInfo() {
		return Info;
	}

	public void setInfo(List<Info> info) {
		Info = info;
	}

	public static void main(String[] args) throws Exception {
		String messages = ""
				+"<Response>"
			    +"<Head>"
			        +"<ResultCode>0</ResultCode>"
			        +"<ErrorMsg></ErrorMsg>"
			        +"<ExeDate>2018-07-13</ExeDate>"
			        +"<IDName>1</IDName>"
			        +"<IDCHECK>1234</IDCHECK>"
			    +"</Head>"
			    +"<Info>"
			        +"<MxItemcode>0</MxItemcode>"
			        +"<MxItemdj>0.00</MxItemdj>"
			        +"<MxItemdw></MxItemdw>"
			        +"<MxItemId>11</MxItemId>"
			        +"<MxItemName>西药</MxItemName>"
			        +"<Num>1.00</Num>"
			        +"<ZhItemcode>7657</ZhItemcode>"
			        +"<ZhItemdj>460.00</ZhItemdj>"
			        +"<ZhItemdw></ZhItemdw>"
			        +"<ZhItemId>25415</ZhItemId>"
			        +"<ZhItemName>双上肢动脉彩色多普勒超声</ZhItemName>"
			    +"</Info>"
			    +"<Info>"
			        +"<MxItemcode>100000000</MxItemcode>"
			        +"<MxItemdj>5.00</MxItemdj>"
			        +"<MxItemdw>次</MxItemdw>"
			        +"<MxItemId>18</MxItemId>"
			        +"<MxItemName>妇科检查材料费</MxItemName>"
			        +"<Num>1.00</Num>"
			        +"<ZhItemcode>7712</ZhItemcode>"
			        +"<ZhItemdj>355.00</ZhItemdj>"
			        +"<ZhItemdw>次        </ZhItemdw>"
			        +"<ZhItemId>25478</ZhItemId>"
			        +"<ZhItemName>盆底功能（+子宫、附件）超声检查</ZhItemName>"
			    +"</Info>"
			+"</Response>";
		NHResponse response = JaxbUtil.converyToJavaBean(messages, NHResponse.class);
		
		System.out.println(response.getInfo().get(0).getMxItemName());
		System.out.println(response.getInfo().get(1).getMxItemName());
	}
}
