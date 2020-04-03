package com.hjw.webService.client.qufu.job.lisbean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class ResLisMessageQF {
	public RetLisCustomeQF rc = new RetLisCustomeQF();
	private Document document;
	public ResLisMessageQF(String xmlmessage,boolean flags) throws Exception{
		String xmlmess="";
		if(flags){
			xmlmess=xmlmessage;
		}else{
			xmlmess=getXml_test();
		}
		 InputStream is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
         SAXReader sax = new SAXReader();
		 this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		 this.getdoctor();
		 this.getdoctor_Item();// 关联检查项目
	}

	private String getXml_test() {
		String xml = 
"<List><LISDOCINFO>"
  +"<DOCTYPE>XDS.LISBG</DOCTYPE>"
  +"<DOCUNIQUEID>2840797</DOCUNIQUEID>"
  +"<CREATETIME>20180810145340</CREATETIME>"
  +"<PATIENTNAME>张苏</PATIENTNAME>"
  +"<TITLE>粪便分析仪LJ3000</TITLE>"
  +"<AUTHORNAME>孔天</AUTHORNAME>"
  +"<AUTHORTIME>20180810145520</AUTHORTIME>"
  +"<LEGALAUTHENTICATORNAME>孔天</LEGALAUTHENTICATORNAME>"
  +"<LEGALAUTHENTICATORTIME>20180810145340</LEGALAUTHENTICATORTIME>"
  +"<REQUESTDOCID>周滕</REQUESTDOCID>"
  +"<REQUESTDOCNAME>孔天</REQUESTDOCNAME>"
  +"<REQUESTTIME>20180810145323</REQUESTTIME>"
  +"<REQUESTDEPCODE>30003</REQUESTDEPCODE>"
  +"<REQUESTDEPNAME>30003</REQUESTDEPNAME>"
  +"<PATIENTTYPEID>3</PATIENTTYPEID>"
  +"<PATIENTAGE>29岁</PATIENTAGE>"
  +"<PATIENTSEXID>1</PATIENTSEXID>"
  +"<PATIENTSEX>M</PATIENTSEX>"
  +"<BIRTHDAY>19880819000000</BIRTHDAY>"
  +"<HOSPNUM>1808100003</HOSPNUM>"
  +"<HISID>1808100003</HISID>"
  +"<FLOWID>8180810012</FLOWID>"
  +"<ORDERTIME>20180810145340</ORDERTIME>"
  +"<LABNUM>70001</LABNUM>"
  +"<SPECIMENTYPE>粪便</SPECIMENTYPE>"
  +"<SPECIMENID>70001</SPECIMENID>"
  +"<RECEIVETIME>20180810145337</RECEIVETIME>"
  +"<DICOMSTUDYTIME>20180810145340</DICOMSTUDYTIME>"
  +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
  +"<PATIENTSEXNAME>M</PATIENTSEXNAME>"
  +"<RECEIVEBY>孔天</RECEIVEBY>"
  +"<ORDERBY>周滕</ORDERBY>"
  +"<PATIENTTYPEDESC>体检</PATIENTTYPEDESC>"
  +"<LOCATIONID>30003</LOCATIONID>"
  +"<TESTITEM>"
  +"<RECEIVETIME>20180810145337</RECEIVETIME>"
  +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
    +"<SUBTESTS>"
	  +"<TESTITEMINFO>"
        +"<TESTDESC>隐血试验</TESTDESC>"
        +"<RESULT>阴性</RESULT>"
        +"<TESTCODE>0103055</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<REFERENCEFLAG>M</REFERENCEFLAG>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>便脓细胞</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/HP</UNIT>"
        +"<TESTCODE>0103006</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>便吞噬细胞</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/HP</UNIT>"
        +"<TESTCODE>0103009</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>便红细胞</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/HP</UNIT>"
        +"<TESTCODE>0103007</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>虫卵</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/HP</UNIT>"
        +"<TESTCODE>0103011</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>脂肪球</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/LP</UNIT>"
        +"<TESTCODE>0103014</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>便白细胞</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/HP</UNIT>"
        +"<TESTCODE>0103008</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>上皮细胞</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/LP</UNIT>"
        +"<TESTCODE>0103217</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
		+"<TESTITEMINFO>"
        +"<TESTDESC>淀粉颗粒</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<TESTCODE>0103015</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>酵母样菌</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/HP</UNIT>"
        +"<TESTCODE>0103211</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>寄生虫原虫</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/HP</UNIT>"
        +"<TESTCODE>0103210</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>大便性状</TESTDESC>"
        +"<RESULT>软</RESULT>"
        +"<TESTCODE>0103003</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<REFERENCEFLAG>M</REFERENCEFLAG>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>颜色</TESTDESC>"
        +"<RESULT>黄色</RESULT>"
        +"<TESTCODE>0103002</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
      +"<TESTITEMINFO>"
        +"<TESTDESC>结晶</TESTDESC>"
        +"<RESULT>未见</RESULT>"
        +"<UNIT>/LP</UNIT>"
        +"<TESTCODE>0103216</TESTCODE>"
        +"<DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>"
        +"<RELEASEBY>孔天</RELEASEBY>"
        +"<RELEASEDATE>20180810145340</RELEASEDATE>"
        +"<AUTHORIZEBY>孔天</AUTHORIZEBY>"
        +"<AUTHORIZEDATE>20180810145337</AUTHORIZEDATE>"
      +"</TESTITEMINFO>"
    +"</SUBTESTS>"
  +"</TESTITEM>"
+"</LISDOCINFO>"
+"</List>";
		return xml;
	}

	public void getdoctor() throws Exception {
		Node node = document.selectSingleNode("/List/LISDOCINFO/AUTHORNAME");
		if(node != null) {
        	this.rc.setCheck_doc(node.getText());
        }
        
		node = document.selectSingleNode("/List/LISDOCINFO/RECEIVETIME");// 获取根节点
		if(node != null) {
			String time = node.getText();
        	time = time.substring(0, 4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8, 10)+":"+time.substring(10, 12)+":"+time.substring(12, 14);
        	this.rc.setCheck_date(time);
        }
        
        node = document.selectSingleNode("/List/LISDOCINFO/LEGALAUTHENTICATORNAME");
        if(node != null) {
        	this.rc.setAudit_doc(node.getText());
        }
	}

	/**
	 * 
	 * @Title: getdoctor_Item @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_Item() throws Exception {
		Node Items = document.selectSingleNode("/List/LISDOCINFO/TESTITEM");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		getNodes_doctor_coms(listElement);
	}

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_coms(List<Element> listElement)  throws Exception {	
		List<RetLisChargeItemQF> rtlischarge=new ArrayList<RetLisChargeItemQF>();// 收费项目 
		for(Element e:listElement){
			String componentxml=e.asXML();
			if(componentxml.indexOf("<TESTITEM>")==0){
				try{
			//System.out.println("当前节点名称：" + e.getName());// 当前节点名称
			//System.out.println("当前节点的内容：" + e.asXML());// 当前节点名称
			RetLisChargeItemQF  retlisch = new RetLisChargeItemQF();
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			Node node = documentItem.selectSingleNode("/TESTITEM/INTCODE");
			if(node != null) {
				retlisch.setChargingItem_num(node.getText());
			}
			node = documentItem.selectSingleNode("/TESTITEM/PROFILEDESC");
			if(node != null) {
				retlisch.setChargingItem_name(node.getText());
			}
			
			Node Items = documentItem.selectSingleNode("/TESTITEM/SUBTESTS/TESTITEMINFO");// 获取根节点
			List<Element> listItemElement = Items.getParent().elements();// 所有一级子节点的list
			List<RetLisItemQF> listRetLisItem =new ArrayList<RetLisItemQF>();
			listRetLisItem = getNodes_doctor_items(listItemElement);
			retlisch.setListRetLisItem(listRetLisItem);
			rtlischarge.add(retlisch);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		this.rc.setListRetLisChargeItem(rtlischarge);
	}

	@SuppressWarnings("unchecked")
	private List<RetLisItemQF> getNodes_doctor_items(List<Element> listElement)  throws Exception {	
		List<RetLisItemQF> listRetLisItem =new ArrayList<RetLisItemQF>();
		for(Element e:listElement){
			RetLisItemQF  retlisch = new RetLisItemQF();
			//System.out.println(e.asXML());
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			Node node = documentItem.selectSingleNode("/TESTITEMINFO/TESTCODE");
			if(node != null) {
				retlisch.setItem_id(node.getText());
			}
			node = documentItem.selectSingleNode("/TESTITEMINFO/TESTDESC");
			if(node != null) {
				retlisch.setItem_name(node.getText());
			}
			node = documentItem.selectSingleNode("/TESTITEMINFO/RESULT");
			if(node != null) {
				retlisch.setValues(node.getText());
			}
			node = documentItem.selectSingleNode("/TESTITEMINFO/UNIT");
			if(node != null) {
				retlisch.setValues_dw(node.getText());
			}
			node = documentItem.selectSingleNode("/TESTITEMINFO/REFLOWVALUE");
			String fw_low = "";
			if(node != null) {
				fw_low = node.getText();
			}
			String fw_high = "";
			node = documentItem.selectSingleNode("/TESTITEMINFO/REFHIGHVALUE");
			if(node != null) {
				fw_high = node.getText();
			}
			retlisch.setValue_fw(fw_low+"--"+fw_high);
			node = documentItem.selectSingleNode("/TESTITEMINFO/REFERENCEFLAG");
			if(node != null) {
				retlisch.setValue_ycbz(node.getText());
			}
			listRetLisItem.add(retlisch);	
		}
		return listRetLisItem;
	}

	public static void main(String[] args) throws Exception {
		
		ResLisMessageQF rpm = new ResLisMessageQF("<List><LISDOCINFO>   <DOCTYPE>XDS.LISBG</DOCTYPE>   <DOCUNIQUEID>2909039</DOCUNIQUEID>   <CREATETIME>20180824132452</CREATETIME>   <PATIENTNAME>孟姝含</PATIENTNAME>   <TITLE>生化仪AU5800</TITLE>   <AUTHORNAME>赵灿灿</AUTHORNAME>   <AUTHORTIME>20180824132635</AUTHORTIME>   <LEGALAUTHENTICATORNAME>颜超</LEGALAUTHENTICATORNAME>   <LEGALAUTHENTICATORTIME>20180824132452</LEGALAUTHENTICATORTIME>   <REQUESTDOCID>周滕</REQUESTDOCID>   <REQUESTDOCNAME>颜超</REQUESTDOCNAME>   <REQUESTTIME>20180824092551</REQUESTTIME>   <REQUESTDEPCODE>30003</REQUESTDEPCODE>   <REQUESTDEPNAME>30003</REQUESTDEPNAME>   <PATIENTTYPEID>3</PATIENTTYPEID>   <PATIENTAGE>11岁</PATIENTAGE>   <PATIENTSEXID>2</PATIENTSEXID>   <PATIENTSEX>F</PATIENTSEX>   <BIRTHDAY>20070824000000</BIRTHDAY>   <HOSPNUM>1808240011</HOSPNUM>   <HISID>1808240011</HISID>   <FLOWID>8180824031</FLOWID>   <ORDERTIME>20180824132452</ORDERTIME>   <LABNUM>137</LABNUM>   <SPECIMENTYPE>全血</SPECIMENTYPE>   <SPECIMENID>137</SPECIMENID>   <RECEIVETIME>20180824092626</RECEIVETIME>   <DICOMSTUDYTIME>20180824132452</DICOMSTUDYTIME>   <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>   <PATIENTSEXNAME>F</PATIENTSEXNAME>   <RECEIVEBY>赵灿灿</RECEIVEBY>   <ORDERBY>周滕</ORDERBY>   <PATIENTTYPEDESC>体检</PATIENTTYPEDESC>   <LOCATIONID>30003</LOCATIONID>   <TESTITEM>     <RECEIVETIME>20180824092626</RECEIVETIME>     <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>     <SUBTESTS>       <TESTITEMINFO>         <TESTDESC>总胆汁酸</TESTDESC>         <RESULT>1.80</RESULT>         <UNIT>umol/L</UNIT>         <REFLOWVALUE>0</REFLOWVALUE>         <REFHIGHVALUE>10</REFHIGHVALUE>         <TESTCODE>0301010</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>M</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>       <TESTITEMINFO>         <TESTDESC>谷氨酰转肽酶</TESTDESC>         <RESULT>18.0</RESULT>         <UNIT>U/L</UNIT>         <REFLOWVALUE>0</REFLOWVALUE>         <REFHIGHVALUE>55</REFHIGHVALUE>         <TESTCODE>0301012</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>M</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>       <TESTITEMINFO>         <TESTDESC>白蛋白</TESTDESC>         <RESULT>45.9</RESULT>         <UNIT>g/L</UNIT>         <REFLOWVALUE>35</REFLOWVALUE>         <REFHIGHVALUE>55</REFHIGHVALUE>         <TESTCODE>0301002</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>M</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>       <TESTITEMINFO>         <TESTDESC>谷丙转氨酶</TESTDESC>         <RESULT>14.5</RESULT>         <UNIT>U/L</UNIT>         <REFLOWVALUE>0</REFLOWVALUE>         <REFHIGHVALUE>40</REFHIGHVALUE>         <TESTCODE>0301013</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>M</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>       <TESTITEMINFO>         <TESTDESC>总蛋白</TESTDESC>         <RESULT>74.4</RESULT>         <UNIT>g/L</UNIT>         <REFLOWVALUE>60</REFLOWVALUE>         <REFHIGHVALUE>85</REFHIGHVALUE>         <TESTCODE>0301001</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>M</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>       <TESTITEMINFO>         <TESTDESC>前白蛋白</TESTDESC>         <RESULT>320.8</RESULT>         <UNIT>mg/L</UNIT>         <REFLOWVALUE>85</REFLOWVALUE>         <REFHIGHVALUE>225</REFHIGHVALUE>         <TESTCODE>0301005</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>H</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>       <TESTITEMINFO>         <TESTDESC>白球比</TESTDESC>         <RESULT>1.6</RESULT>         <REFLOWVALUE>1.5</REFLOWVALUE>         <REFHIGHVALUE>2.5</REFHIGHVALUE>         <TESTCODE>0301004</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>M</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>       <TESTITEMINFO>         <TESTDESC>谷草/谷丙</TESTDESC>         <RESULT>0.71</RESULT>         <TESTCODE>0301015</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>M</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>       <TESTITEMINFO>         <TESTDESC>球蛋白</TESTDESC>         <RESULT>28.5</RESULT>         <UNIT>g/L</UNIT>         <REFLOWVALUE>20</REFLOWVALUE>         <REFHIGHVALUE>35</REFHIGHVALUE>         <TESTCODE>0301003</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>M</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>       <TESTITEMINFO>         <TESTDESC>谷草转氨酶</TESTDESC>         <RESULT>10.3</RESULT>         <UNIT>U/L</UNIT>         <REFLOWVALUE>0</REFLOWVALUE>         <REFHIGHVALUE>40</REFHIGHVALUE>         <TESTCODE>0301014</TESTCODE>         <DOCDOMAINID>2.16.840.1.113883.4.487.4.46.7</DOCDOMAINID>         <REFERENCEFLAG>M</REFERENCEFLAG>         <RELEASEBY>颜超</RELEASEBY>         <RELEASEDATE>20180824132452</RELEASEDATE>         <AUTHORIZEBY>赵灿灿</AUTHORIZEBY>         <AUTHORIZEDATE>20180824092626</AUTHORIZEDATE>       </TESTITEMINFO>     </SUBTESTS>   </TESTITEM> </LISDOCINFO></List>", false);
		
	}

}
