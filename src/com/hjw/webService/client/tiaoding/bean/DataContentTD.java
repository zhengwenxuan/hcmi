package com.hjw.webService.client.tiaoding.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "DataContent")  
@XmlType(propOrder = {})
public class DataContentTD {

	@XmlAttribute(name = "content")
	private String content = "";
	@XmlElement(name = "err")
	private String err;
	@XmlElement(name = "client")
	private client client;
	
	public static void main(String[] args) throws Exception {
		String xml = ""
+ "<DataContent content=\"client_add\">"
+ "<err msg=\"操作成功\">0</err>"
+ "<client id=\"130959\" number=\"002\" categoryid=\"采血室\" categoryName=\"采血室\"  state=\"0\"  time_enter=\"2019-04-23 18:25:11\" name=\"测试55\" card=\"9190423055\" wp=\"TD_k碳13和14;TD_a检验科;TD_h空腹彩超;TD_c内科;TD_g心电图室;TD_l骨密度室;TD_m经颅多普勒室;TD_h非空腹彩超;TD_k动脉硬化室;TD_i放射(DR);TD_b一般检查室;TD_e耳鼻喉科;TD_d外科;\"   ><pos float=\"50000\">0</pos><score wnd=\"\" staffer=\"\"></score></client>"
+ "</DataContent>";
		DataContentTD dataContent = JaxbUtil.converyToJavaBean(xml, DataContentTD.class);
		System.out.println(dataContent.getClient().getCategoryid());
	}
	
	public String getContent() {
		return content;
	}
	public client getClient() {
		return client;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setClient(client client) {
		this.client = client;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
}
