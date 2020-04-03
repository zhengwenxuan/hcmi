package com.hjw.webService.client.hokai;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.HisClinicItemPriceDTO;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.wst.DTO.HisClinicItemPriceListDTO;
import com.hjw.wst.domain.HisClinicItem;
import com.hjw.wst.domain.HisPriceList;
import com.hjw.wst.model.ChargingItemModel;
import com.hjw.wst.service.ChargingItemService;

public class HISDataSynchronizingMessageHK {

	private static ConfigService configService;
	private static ChargingItemService chargingItemService;
	private List<HisClinicItem> clinicList = new ArrayList<HisClinicItem>();
	private List<HisClinicItemPriceDTO> clinicPriceList = new ArrayList<HisClinicItemPriceDTO>();
	private List<HisPriceList> priceList = new ArrayList<HisPriceList>();
	
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		chargingItemService = (ChargingItemService) wac.getBean("chargingItemService");
	}
	
	public String getMessage(String messages, String logname) {
		ResultHisBody rb = new ResultHisBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname,"res:"+messages);
			rb = parseString(messages, logname);
			if("AA".equals(rb.getResultHeader().getTypeCode())) {
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("数据同步成功!");
			}
		} catch (Throwable e) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		StringBuffer sb = new StringBuffer("");
		sb.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
		sb.append("	<id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>\n");
		sb.append("	<creationTime value=\"" + DateTimeUtil.getDateTimes() + "\"/>\n");
		sb.append("	<interactionId extension=\"S0076\" root=\"2.16.840.1.113883.1.6\"/>\n");
		sb.append("	<processingCode code=\"P\"/>\n");
		sb.append("	<processingModeCode/>\n");
		sb.append("	<acceptAckCode code=\"AL\"/>\n");
		sb.append("	<receiver typeCode=\"RCV\">\n");
		sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("			<id>\n");
		sb.append("				<item extension=\"SYS001\"/>\n");
		sb.append("			</id>\n");
		sb.append("		</device>\n");
		sb.append("	</receiver>\n");
		sb.append("	<sender typeCode=\"SND\">\n");
		sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("			<id>\n");
		sb.append("				<item extension=\"SYS009\"/>\n");
		sb.append("			</id>\n");
		sb.append("		</device>\n");
		sb.append("	</sender>\n");
		sb.append("	<!--AA成功，AE失败-->\n");
		sb.append("	<acknowledgement typeCode=\""+rb.getResultHeader().getTypeCode()+"\">\n");
		sb.append("		<!--请求消息ID-->\n");
		sb.append("		<targetMessage>\n");
		sb.append("			<id extension=\""+rb.getResultHeader().getSourceMsgId()+"\"/>\n");
		sb.append("		</targetMessage>\n");
		sb.append("		<acknowledgementDetail>\n");
		sb.append("			<text value=\""+rb.getResultHeader().getText()+"\"/>\n");
		sb.append("		</acknowledgementDetail>\n");
		sb.append("	</acknowledgement>\n");
		sb.append("</MCCI_IN000002UV01>\n");
		return sb.toString();
	}
	
	private ResultHisBody parseString(String xmlstr, String logname){
		ResultHisBody rb= new ResultHisBody();
		try{
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			Map<String, String> xmlMap = new HashMap<>();
			xmlMap.put("abc", "urn:hl7-org:v3");
			SAXReader sax = new SAXReader();
			sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			rb.getResultHeader().setSourceMsgId(document.selectSingleNode("abc:PRVS_IN000001UV01/abc:id/@extension").getText());
			String messageType = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:code/@code").getText();// 获取根节点
	 		
	 		if("CONSULTATIONFEE".equals(messageType)) {//诊疗费用关系字典
	 			HisClinicItem clinic = new HisClinicItem();
	 			HisClinicItemPriceListDTO hisClinicItemPriceListDTO = new HisClinicItemPriceListDTO();
				Node node = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:ItemType");
				if(node != null) {
//					String clinic_class = node.getText();
//					clinic.setItem_class(clinic_class);
				}
				node = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:ItemCode");
				if(node != null) {
					String clinic_code = node.getText();
					clinic.setItem_code(clinic_code);
					hisClinicItemPriceListDTO.setClinic_item_code(clinic_code);
				}
				node = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:ItemName");
				if(node != null) {
					String clinic_name = node.getText();
					clinic.setItem_name(clinic_name);
				}
				node = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:ItemInspectPosition");
				if(node != null) {
					String body_part = node.getText();
					hisClinicItemPriceListDTO.setBody_part(body_part);
				}
				node = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:ItemInspectMethod");
				if(node != null) {
					String method = node.getText();
					hisClinicItemPriceListDTO.setMethod(method);
				}
				this.clinicList.add(clinic);
				
				Node Items = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:items/abc:Item");// 获取根节点
				//System.out.println("当前节点的内容：" + Items.asXML());// 当前节点名称
				List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
				
//				hisClinicItemPriceListDTO.setClinic_item_class(clinic_class);
				getNodes_price_list(listElement, hisClinicItemPriceListDTO);
				
				configService.insertClinicList(logname, this.clinicList);
				configService.insertClinicPriceList(logname, this.clinicPriceList);
				configService.insertPriceList(logname, this.priceList);
	 		} else if("PRICEADJUSTMENT".equals(messageType.toUpperCase())) {//收费项目调价字典
	 			String price_code = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:code").getText();
	 			String price = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:price").getText();
	 			configService.updatePriceList(logname, price_code, price);
	 			ChargingItemModel model = new ChargingItemModel();
	 			chargingItemService.updateHIsPriceSynchro(model);
	 		} else {
	 			rb.getResultHeader().setText("不支持的字典类型："+messageType.toUpperCase());
	 		}
			rb.getResultHeader().setTypeCode("AA");
		}catch(Exception ex){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rb;
	}
	
	@SuppressWarnings("unchecked")
	private void getNodes_price_list(List<Element> listElement, HisClinicItemPriceListDTO hisClinicItemPriceListDTO)  throws Exception {	
		for(Element e:listElement){
			HisClinicItemPriceDTO clinicPrice = new HisClinicItemPriceDTO();
//			clinicPrice.setClinic_item_class(hisClinicItemPriceListDTO.getClinic_item_class());
			clinicPrice.setClinic_item_code(hisClinicItemPriceListDTO.getClinic_item_code());
			clinicPrice.setBody_part(hisClinicItemPriceListDTO.getBody_part());
			clinicPrice.setMethod(hisClinicItemPriceListDTO.getMethod());
			HisPriceList price = new HisPriceList();
			
			String componentxml=e.asXML();
			//System.out.println("当前节点的内容：" + e.asXML());// 当前节点名称
			if(componentxml.indexOf("<Item")>=0){
				try{
			//System.out.println("当前节点名称：" + e.getName());// 当前节点名称

			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			 Map<String, String> xmlMap = new HashMap<>();
		 		xmlMap.put("abc", "urn:hl7-org:v3");
		 		SAXReader sax = new SAXReader();
		 		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
			Document documentItem = sax.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String price_code = documentItem.selectSingleNode("/abc:Item/abc:FeesCode").getText();
			price.setItem_code(price_code);
			clinicPrice.setCharge_item_code(price_code);
			String price_name = documentItem.selectSingleNode("/abc:Item/abc:FeesName").getText();
			price.setItem_name(price_name);
			String Number = documentItem.selectSingleNode("/abc:Item/abc:Number").getText();
			clinicPrice.setAmount(Long.parseLong(Number));
			String DefaultPrice = documentItem.selectSingleNode("/abc:Item/abc:Price").getText();
			price.setPrefer_price(Double.parseDouble(DefaultPrice));
			String hisPrice = documentItem.selectSingleNode("/abc:Item/abc:Price").getText();
			price.setPrice(Double.parseDouble(hisPrice));
			
			this.priceList.add(price);
			this.clinicPriceList.add(clinicPrice);
			
			//String isChangePrice = documentItem.selectSingleNode("/abc:Item/abc:IsChangePrice").getText();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
}
