package com.hjw.webService.client.fangzheng.Bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hjw.webService.service.pacsbean.RetPacsCustome;
import com.hjw.webService.service.pacsbean.RetPacsItem;

public class PacsMessageAnalyzeFZ {
	private RetPacsCustome rc = new RetPacsCustome();
	private Document document;

	public PacsMessageAnalyzeFZ(String xmlmessage) throws Exception {
		String xmlmessagess = "";
		xmlmessagess = xmlmessage;
		InputStream is = new ByteArrayInputStream(xmlmessagess.getBytes("utf-8"));
		Map<String, String> xmlMap = new HashMap<>();
		xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束

		this.getCustom();// 获取检查人员信息
		this.getdoctor_bg();// 获取报告医生
		this.getdoctor_sh();// 获取审核医生
		this.getdoctor_orderid();// 关联医嘱号或者清单号
		this.getdoctor_effectiveTime();// 获取报告时间
		this.getdoctor_deptcode();// 关联科室编码对应类别编码
		this.getdoctor_Diagnosis();// 获取阴阳性
		this.getdoctor_Item();// 关联检查项目
	}

	public RetPacsCustome getRetPacsCustome() {
		return this.rc;
	}


	/**
	 * 
	 * @Title: getCustom @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws Exception @return:
	 * String @throws
	 */
	public void getCustom() throws Exception {
		Node root = document.selectSingleNode("abc:ClinicalDocument/abc:recordTarget/abc:patientRole/abc:id");
		getNodes_custom(root.getParent());
	}

	@SuppressWarnings("unchecked")
	private void getNodes_custom(Element node) {
		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			// 当前节点的名称、文本内容和属性
			// System.out.println("当前节点名称：" + e.getName());// 当前节点名称
			// System.out.println("当前节点的内容：" + e.getTextTrim());// 当前节点名称
			List<Attribute> listAttr = e.attributes();// 当前节点的所有属性的list
			for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
				// String name = attr.getName();// 属性名称
				// String value = attr.getValue();// 属性的值
				// System.out.println("属性名称：" + name + "属性值：" + value);
				if ("1.2.156.112649.1.2.1.3".equals(attr.getValue().trim())) {
					for (Attribute attrone : listAttr) {// 遍历当前节点的所有属性
						if ("extension".equals(attrone.getName().trim().toLowerCase())) {
							this.rc.setCustome_id(attrone.getValue());
							break;
						}
					}
				}
				if ("1.2.156.112649.1.2.1.12".equals(attr.getValue().trim())) {
					for (Attribute attrone : listAttr) {// 遍历当前节点的所有属性
						if ("extension".equals(attrone.getName().trim().toLowerCase())) {
							this.rc.setCoustom_jzh(attrone.getValue());
							break;
						}
					}
				}
				if ("exam_num".equals(attr.getValue().trim())) {
					for (Attribute attrone : listAttr) {// 遍历当前节点的所有属性
						if ("extension".equals(attrone.getName().trim().toLowerCase())) {
							this.rc.setExam_num(attrone.getValue());
							break;
						}
					}
				}
			}
		}

	}

	/**
	 * 
	 * @Title: getdoctor_bg @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws Exception @return:
	 * String @throws
	 */
	public void getdoctor_bg() throws Exception {
		String time = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:assignedAuthor/abc:assignedPerson/abc:name")
				.getText();// 获取根节点
		this.rc.setDoctor_name_bg(name);
		this.rc.setDoctor_time_bg(time);
		// getNodes_doctor_bg(root, "author",flags);// 从根节点开始遍历所有节点
	}

	/**
	 * 
	 * @Title: getdoctor_sh @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws Exception @return:
	 * String @throws
	 */
	public void getdoctor_sh() throws Exception {
		String time = document.selectSingleNode("abc:ClinicalDocument/abc:authenticator/abc:time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("abc:ClinicalDocument/abc:authenticator/abc:assignedEntity/abc:assignedPerson/abc:name")
				.getText();// 获取根节点
		this.rc.setDoctor_name_sh(name);
		this.rc.setDoctor_time_sh(time);
	}

	/**
	 * 
	 * @Title: getdoctor_orderid @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws Exception @return:
	 * String @throws
	 */
	public void getdoctor_effectiveTime() throws Exception {
		String time = document.selectSingleNode("abc:ClinicalDocument/abc:effectiveTime/@value").getText();// 获取根节点
		this.rc.setEffectiveTime(time);
	}

	public void getdoctor_orderid() throws Exception {
		String time = document.selectSingleNode("abc:ClinicalDocument/abc:req/abc:id/@extension").getText();// 获取根节点
		this.rc.setPacs_summary_id(time);
	}

	/**
	 * 
	 * @Title: getdoctor_orderid @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws Exception @return:
	 * String @throws
	 */
	public void getdoctor_deptcode() throws Exception {
		String dept_code = document
				.selectSingleNode(
						"abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:code/@code")
				.getText();// 获取根节点
		String dept_name = document
				.selectSingleNode(
						"abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:code/@displayName")
				.getText();// 获取根节点
		this.rc.setDept_code(dept_code);
		this.rc.setDept_name(dept_name);
	}

	/**
	 * 
	 * @Title: getdoctor_orderid @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws Exception @return:
	 * String @throws
	 */
	public void getdoctor_Diagnosis() throws Exception {
		Node yyang = document.selectSingleNode(
				"abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:act/abc:entryRelationship/abc:observation/abc:value");// 获取根节点
		List<Element> listElement = yyang.getParent().elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			List<Attribute> listAttr = e.attributes();// 当前节点的所有属性的list
			for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
				String name = attr.getName();// 属性名称
				String value = attr.getValue();// 属性的值
				// System.out.println("属性名称：" + name + "属性值：" + value);
				if ("yyang".equals(value.trim().toLowerCase())) {
					for (Attribute attrnew : listAttr) {// 遍历当前节点的所有属性
						String namenew = attrnew.getName();// 属性名称
						String valuenew = attrnew.getValue();// 属性的值
						if ("displayname".equals(namenew.trim().toLowerCase())) {
							this.rc.setNpositive(valuenew);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Title: getdoctor_Item @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws Exception @return:
	 * String @throws
	 */
	public void getdoctor_Item() throws Exception {
		Node Items = document.selectSingleNode(
				"abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observation");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		getNodes_doctor_Item(listElement);
	}

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_Item(List<Element> listElement) throws Exception {
		List<RetPacsItem> rpi = new ArrayList<RetPacsItem>();
		for (Element e : listElement) {
			RetPacsItem reti = new RetPacsItem();
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String Items_code = documentItem.selectSingleNode("/abc:observation/abc:code/@code").getText();
			reti.setChargingItem_num(Items_code);
			String Itemsitem = documentItem.selectSingleNode("/abc:observation/abc:value[@code='01']/abc:originalText").getText();
			reti.setChargingItem_ms(Itemsitem);
			String Itemsitemjl = documentItem.selectSingleNode("/abc:observation/abc:value[@code='02']/abc:originalText").getText();
			reti.setChargingItem_jl(Itemsitemjl);

			String Items_base = documentItem
					.selectSingleNode("/abc:observation/abc:entryRelationship[@typeCode='SPRT']/abc:observationMedia/abc:value")
					.getText();
			reti.setBase64_bg(Items_base);
			rpi.add(reti);
		}
		this.rc.setList(rpi);
	}


}
