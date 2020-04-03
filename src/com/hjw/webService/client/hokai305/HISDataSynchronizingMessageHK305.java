package com.hjw.webService.client.hokai305;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.hjw.interfaces.util.DateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.wst.DTO.HisClinicItemPriceListDTO;
import com.hjw.wst.domain.HisClinicItem;
import com.hjw.wst.domain.HisPriceList;
import com.hjw.wst.model.ChargingItemModel;
import com.hjw.wst.service.ChargingItemService;

public class HISDataSynchronizingMessageHK305 {

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
	 		
	 		if("CLINIC_VS_CHARGE".equals(messageType)) {//诊疗费用关系字典
	 			HisClinicItemPriceListDTO hisClinicItemPriceListDTO = new HisClinicItemPriceListDTO();
	 			HisClinicItemPriceDTO hisClinicItemPriceDTO = new HisClinicItemPriceDTO();
	 			
	 			String clinicItemClass =document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:clinicItemClass/@value").getText();
	 			String clinicItemCode =document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:clinicItemCode/@value").getText();
	 			String chargeItemNo =document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:chargeItemNo/@value").getText();
	 			String chargeItemclass =document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:chargeItemlass/@value").getText();
	 			String chargeItemCode =document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:chargeItemCode/@value").getText();
	 			String amount =document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:amount/@value").getText();
	 			String units =document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:units/@value").getText();
	 			String chargeItemSpec =document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:chargeItemSpec/@value").getText();
	 			
	 			hisClinicItemPriceDTO.setClinic_item_class(clinicItemClass);
	 			hisClinicItemPriceDTO.setClinic_item_code(clinicItemCode);
	 			hisClinicItemPriceDTO.setCharge_item_no(Integer.parseInt(chargeItemNo));
	 			hisClinicItemPriceDTO.setCharge_item_classs(chargeItemclass);
	 			hisClinicItemPriceDTO.setCharge_item_code(chargeItemCode);
	 			hisClinicItemPriceDTO.setAmount(Long.parseLong(amount));
	 			hisClinicItemPriceDTO.setUnits(units);
	 			hisClinicItemPriceDTO.setCharge_item_spec(chargeItemSpec);
	 			
	 			this.clinicPriceList.add(hisClinicItemPriceDTO);
	 			TranLogTxt.liswriteEror_to_txt(logname, "CLINIC_VS_CHARGE诊疗关系:" + ""+ "\r\n");
	 			boolean hisPricecount = configService.getHisClinicItemVPriceList(logname,clinicItemClass,clinicItemCode,chargeItemclass,chargeItemCode);
	 			if(hisPricecount){
	 				configService.updateHisClinicItemVPriceList(logname,clinicItemClass,clinicItemCode,chargeItemclass,chargeItemCode,amount,units);
	 			}else{
	 				configService.insertClinicPriceList(logname, this.clinicPriceList);
	 				
	 			}
	 			
	 			
	 		} else if("PRICE_LIST".equals(messageType.toUpperCase())) {//收费项目调价字典PRVS_IN000001UV01
	 			String price_code = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:itemCode/@value").getText();
	 			String itemClass = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:itemClass/@value").getText();
	 			String itemName = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:itemName/@value").getText();
	 			String price = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:price/@value").getText();
	 			String preferPrice = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:preferPrice/@value").getText();
	 			String units = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:units/@value").getText();
	 			String itemSpec = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:itemSpec/@value").getText();
	 			
	 			String startDate = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:startDate/@value").getText();
	 			String stopDate = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:stopDate/@value").getText();
	 			
		 		if(startDate.equals("") || startDate ==null){
		 			startDate="1901-01-01 00:00:00.000";
		 		}
		 			
		 		if(stopDate.equals("") || stopDate ==null){
		 			stopDate="9999-12-31 23:59:59.000";
		 		}
	 			Date startprice = setdate(startDate);
	 			Date stopprice = setdate(stopDate);
	 			
	 			DateUtil.parse();
	 		
	 			TranLogTxt.liswriteEror_to_txt(logname, "PRICE_LIST价表:" + ""+ "\r\n");
	 			boolean hisPricecount = configService.getHisPriceList(logname,price_code,itemClass);
	 			if(hisPricecount){
	 				configService.updatePriceList(logname, price_code, price);
	 				
	 			}else{
	 				HisPriceList hisprice = new HisPriceList();
	 				hisprice.setItem_code(price_code);
	 				hisprice.setItem_class(itemClass);
	 				hisprice.setPrice(Double.parseDouble(price));
	 				hisprice.setPrefer_price(Double.parseDouble(preferPrice));
	 				hisprice.setItem_name(itemName);
	 				hisprice.setUnits(units);
	 				hisprice.setItem_spec(itemSpec);
	 				hisprice.setStart_date(startprice);
	 				hisprice.setStop_date(stopprice);
	 				this.priceList.add(hisprice);
	 				
	 				configService.insertPriceList(logname, this.priceList);
	 			}
	 			
	 			ChargingItemModel model = new ChargingItemModel();
	 			chargingItemService.updateHIsPriceSynchro(model);
	 		} else if ("CLINIC_ITEM_DICT".equals(messageType.toUpperCase())){
	 			HisClinicItem clinic = new HisClinicItem();
	 			String clinic_itemclass = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:class/@code").getText();
	 			String clinic_code = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:code/@code").getText();
	 			String clinic_name = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:desc/@value").getText();
	 			String clinic_input_code = document.selectSingleNode("abc:PRVS_IN000001UV01/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:valueSet/abc:valueSetItems/abc:inputcode/@code").getText();
				
				clinic.setItem_code(clinic_code);
				clinic.setItem_name(clinic_name);
				clinic.setItem_class(clinic_itemclass);
				clinic.setInput_code(clinic_input_code);
				this.clinicList.add(clinic);
				
				TranLogTxt.liswriteEror_to_txt(logname, "CLINIC_ITEM_DICT诊疗表:" + ""+ "\r\n");
				boolean hisPricecount = configService.getHisClinicItem(logname,clinic_code,clinic_itemclass,clinic_input_code);
				if(hisPricecount){
	 				configService.updateClinicList(logname, clinic_code, clinic_itemclass,clinic_input_code);
	 				
	 			}else{
	 				
	 				configService.insertClinicList(logname, this.clinicList);
	 			}
				
	 		}else {
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
	

	
	 public  Date setdate(String rq) {
	    	Date date = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				date = sdf.parse(rq);
			} catch (ParseException ex) {
			}
			//sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	
			return date;
		}

}
