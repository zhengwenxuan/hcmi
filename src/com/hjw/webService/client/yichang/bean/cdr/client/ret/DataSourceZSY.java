package com.hjw.webService.client.yichang.bean.cdr.client.ret;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class DataSourceZSY {

	private Document document;
	private String MARK = "";
	private String EMPI = "";
	private String CONTENT = "";

	public DataSourceZSY(String xmlmessage,boolean flags) throws Exception{
		String xmlmess="";
		if(flags){
			xmlmess=xmlmessage;
		}else{
			xmlmess=getXml_test();
		}
		InputStream is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
        SAXReader sax = new SAXReader();
		
		is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
		this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		Node node = document.selectSingleNode("/MSG/MARK");
		if(node != null) {
			this.MARK = node.getText();
		}
		node = document.selectSingleNode("/MSG/EMPI");
		if(node != null) {
			this.EMPI = node.getText();
        }
		node = document.selectSingleNode("/MSG/CONTENT");
		if(node != null) {
			this.CONTENT = node.getText();
		}
	}
	
	private String getXml_test() {
		String xml = "<MSG><MARK>1</MARK><EMPI>0001000089</EMPI><CONTENT><PATIENTINFO><PATIENT><NAME>姓名</NAME><IDNO>身份证号</IDNO><SEX>性别</SEX><BIRTHDAY>哈哈</BIRTHDAY><CNY>国际编码</CNY><CNYNAME>国籍名称</CNYNAME><ACT>户籍</ACT><ADDR>住址</ADDR><ZPCODE>邮编</ZPCODE><ABOBLD>ABO血型</ABOBLD><RHBLD>RH血型</RHBLD><NTN>民族</NTN><BCP>出生地</BCP><CTOR>联系人</CTOR><CTORTEL>联系人电话</CTORTEL><CTORLTN>联系人关系</CTORLTN><HMTEL>家庭电话</HMTEL><MOBILE>患者手机号码</MOBILE><EML>邮箱</EML><CPY>公司名称</CPY><CPYTEL>公司电话</CPYTEL><MRG>婚姻状况</MRG><PFSN>职业代码</PFSN><MEMO>备注</MEMO><STATE>地址-省（自治区、直辖市）</STATE><CITY>地址-市（地区）</CITY><COUNTY>地址-县（区）</COUNTY><STREETNAMEBASE>地址-乡（镇、街道办事处）</STREETNAMEBASE><STREETNAME>地址-村（街、路、弄等）</STREETNAME><HOUSENUMBER>门牌号</HOUSENUMBER><INSURANCECODE>保险编码</INSURANCECODE></PATIENT><CARDINFOS><CARD><CARDNO>卡号码</CARDNO><CARDTYPE>卡类型</CARDTYPE><OPERCODE>操作员编号</OPERCODE><OPERNAME>操作员姓名</OPERNAME></CARD></CARDINFOS><DOMAIN>系统域编码</DOMAIN><LOCALID>本地系统的患者ID</LOCALID><PROVIDERORGANIZATIONCODE>服务机构编码</PROVIDERORGANIZATIONCODE><PROVIDERORGANIZATIONNAME>服务机构名称</PROVIDERORGANIZATIONNAME><REGISTRANTCARD>注册人卡号</REGISTRANTCARD><REGISTRANTNAME>注册人名称</REGISTRANTNAME></PATIENTINFO></CONTENT></MSG>";
		return xml;
	}

	public String getMARK() {
		return MARK;
	}

	public String getEMPI() {
		return EMPI;
	}

	public String getCONTENT() {
		return CONTENT;
	}

	public void setMARK(String mARK) {
		MARK = mARK;
	}

	public void setEMPI(String eMPI) {
		EMPI = eMPI;
	}

	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(new DataSourceZSY("", false).getEMPI());
	}
}
