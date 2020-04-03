package com.hjw.webService.client.bdyx.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "response")  
@XmlType(propOrder = {})
public class ResponseData {

	private int code = 0;
	private String msg = "";
	private Datalist datalist = new Datalist();
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public Datalist getDatalist() {
		return datalist;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setDatalist(Datalist datalist) {
		this.datalist = datalist;
	}
	
	public static void main(String[] args) throws Exception {
		String xml = ""
+ "<response>"
	+ "<code>0</code>"
	+ "<msg></msg>"
	+ "<pageinfo>"
		+ "<pagesize>页大小</pagesize>"
		+ "<pagecount>总页数</pagecount>"
		+ "<pageindex>页号</pageindex>"
		+ "<recordsCount>总条数</recordsCount>"
	+ "</pageinfo>"
	+ "<datalist>"
		+"<data>"
			+"<name_pkgu_med>项</name_pkgu_med>"
			+"<wbcode>-EXGIP.</wbcode>"
			+"<name>δ-胆红素测定</name>"
			+"<price_std>2</price_std>"
			+"<code>250305004</code>"
			+"<pycode>-DHSCD.</pycode>"
			+"<fg_active>Y</fg_active>"
		+"</data>"
		+"<data>"
			+"<name_pkgu_med>项</name_pkgu_med>"
			+"<wbcode>-EXGIP.</wbcode>"
			+"<name>δ-胆红素测定</name>"
			+"<price_std>2</price_std>"
			+"<code>250305004</code>"
			+"<pycode>-DHSCD.</pycode>"
			+"<fg_active>Y</fg_active>"
		+"</data>"
	+ "</datalist>"
+ "</response>"
	;
		ResponseData data = JaxbUtil.converyToJavaBean(xml, ResponseData.class);
		System.out.println(data.getDatalist().getData().get(0).getName());
	}
}
