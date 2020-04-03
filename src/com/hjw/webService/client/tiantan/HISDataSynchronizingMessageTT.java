package com.hjw.webService.client.tiantan;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.HisClinicItemPriceDTO;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.wst.domain.HisClinicItem;
import com.hjw.wst.domain.HisPriceList;
import com.hjw.wst.model.ChargingItemModel;
import com.hjw.wst.service.ChargingItemService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class HISDataSynchronizingMessageTT extends ServletEndpointSupport{

	private static ConfigService configService;
	private static ChargingItemService chargingItemService;
	private List<HisClinicItem> clinicList = new ArrayList<HisClinicItem>();
	private List<HisClinicItemPriceDTO> clinicPriceList = new ArrayList<HisClinicItemPriceDTO>();
	private List<HisPriceList> priceList = new ArrayList<HisPriceList>();
	private static JdbcQueryManager jdbcQueryManager;
	
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		chargingItemService = (ChargingItemService) wac.getBean("chargingItemService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public String HIPMessageServer(String action, String messages) {
		String logname="hisDataSyn";
		TranLogTxt.liswriteEror_to_txt(logname, "入参:" +messages+"类型"+action);
		StringBuffer sb = new StringBuffer("");
		ResultHisBody rb = new ResultHisBody();
		String uuid = UUID.randomUUID().toString().toLowerCase();
		if(action.equals("HLHT_UPDATE_UNDRUG")){
			try {
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+messages);
				rb = parseString(messages, logname);
				if("AA".equals(rb.getResultHeader().getTypeCode())) {
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setSourceMsgId(uuid);
					rb.getResultHeader().setText("数据同步成功!");
				}
			} catch (Throwable e) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			}
			sb.append(" <?xml version=\"1.0\" encoding=\"utf-8\"?>                                                  ");
			sb.append("                                                                                             ");
			sb.append(" <Response>                                                                                  ");
			sb.append("   <id root=\"2.16.156.10011.0\" extension=\""+uuid+"\"/>        ");
			sb.append("   <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                  ");
			sb.append("   <messageTypeCode code=\"ZDY\"/>                                                           ");
			sb.append("   <!--发送者OID -->                                                                         ");
			sb.append("   <sender typeCode=\"SND\">                                                                 ");
			sb.append("     <device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                  ");
			sb.append("       <id root=\"2.16.156.10011.0.1.1\" extension=\"162\"/>                                ");
			sb.append("     </device>                                                                               ");
			sb.append("   </sender>                                                                                 ");
			sb.append("   <!--接收者OID-->                                                                          ");
			sb.append("   <receiver typeCode=\"RCV\">                                                               ");
			sb.append("     <device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                  ");
			sb.append("       <id root=\"2.16.156.10011.0.1.2\" extension=\"KTIP\"/>                                ");
			sb.append("     </device>                                                                               ");
			sb.append("   </receiver>                                                                               ");
			sb.append("   <!--typeCode 为处理结果，AA 表示成功 AE 表示失败-->                                       ");
			sb.append("   <acknowledgement typeCode=\"AA\">                                                         ");
			sb.append("     <targetMessage>                                                                         ");
			sb.append("       <!--请求的消息ID-->                                                                   ");
			sb.append("       <id root=\"2.16.156.10011.0\" extension=\""+uuid+"\"/>    ");
			sb.append("     </targetMessage>                                                                        ");
			sb.append("     <acknowledgementDetail>                                                                 ");
			sb.append("       <!--处理结果说明-->                                                                   ");
			sb.append("       <text>获取成功</text>                                                                 ");
			sb.append("     </acknowledgementDetail>                                                                ");
			sb.append("   </acknowledgement>                                                                        ");
			
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb.toString());
			return sb.toString();
		}else{
			
			
			
			sb.append(" <?xml version=\"1.0\" encoding=\"utf-8\"?>                                                  ");
			sb.append("                                                                                             ");
			sb.append(" <Response>                                                                                  ");
			sb.append("   <id root=\"2.16.156.10011.0\" extension=\""+uuid+"\"/>        ");
			sb.append("   <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                  ");
			sb.append("   <messageTypeCode code=\"ZDY\"/>                                                           ");
			sb.append("   <!--发送者OID -->                                                                         ");
			sb.append("   <sender typeCode=\"SND\">                                                                 ");
			sb.append("     <device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                  ");
			sb.append("       <id root=\"2.16.156.10011.0.1.1\" extension=\"162\"/>                                ");
			sb.append("     </device>                                                                               ");
			sb.append("   </sender>                                                                                 ");
			sb.append("   <!--接收者OID-->                                                                          ");
			sb.append("   <receiver typeCode=\"RCV\">                                                               ");
			sb.append("     <device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                  ");
			sb.append("       <id root=\"2.16.156.10011.0.1.2\" extension=\"KTIP\"/>                                ");
			sb.append("     </device>                                                                               ");
			sb.append("   </receiver>                                                                               ");
			sb.append("   <!--typeCode 为处理结果，AA 表示成功 AE 表示失败-->                                       ");
			sb.append("   <acknowledgement typeCode=\"AE\">                                                         ");
			sb.append("     <targetMessage>                                                                         ");
			sb.append("       <!--请求的消息ID-->                                                                   ");
			sb.append("       <id root=\"2.16.156.10011.0\" extension=\""+uuid+"\"/>    ");
			sb.append("     </targetMessage>                                                                        ");
			sb.append("     <acknowledgementDetail>                                                                 ");
			sb.append("       <!--处理结果说明-->                                                                   ");
			sb.append("       <text>失败</text>                                                                 ");
			sb.append("     </acknowledgementDetail>                                                                ");
			sb.append("   </acknowledgement>                                                                        ");
			
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb.toString());
			return sb.toString();
		}
		
	
	}
	
	private ResultHisBody parseString(String xmlstr, String logname){
		ResultHisBody rb= new ResultHisBody();
		try{
			
			
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			//Map<String, String> xmlMap = new HashMap<>();
			//xmlMap.put("abc", "urn:hl7-org:v3");
			SAXReader sax = new SAXReader();
			//sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			//解析价表同步入参
			String modify_type = document.selectSingleNode("Request/Body/modify_type").getText();//1新增，2修改，0删除
			String c_itemcode = document.selectSingleNode("Request/Body/undrug_code").getText();//诊疗项目代码
			String c_itemname = document.selectSingleNode("Request/Body/undrug_name").getText();//诊疗项目名称
			String c__price = document.selectSingleNode("Request/Body/undrug_price").getText();//诊疗项目价格
			String specs = document.selectSingleNode("Request/Body/specs").getText();//规格
			String unit = document.selectSingleNode("Request/Body/unit").getText();//单位
			String dept_code = document.selectSingleNode("Request/Body/dept_code").getText();//执行科室代码
			String dept_name = document.selectSingleNode("Request/Body/dept_name").getText();//执行科室名称
			String oper_date = document.selectSingleNode("Request/Body/oper_date").getText();//维护时间
			String stop_flag = document.selectSingleNode("Request/Body/stop_flag").getText();//停用标志
			HisClinicItem hisclinic = new HisClinicItem();//his诊疗表
			
			if(stop_flag.equals("0")){
				hisclinic.setItem_status("Y");
			}else{
				hisclinic.setItem_status("N");
			}
			
		
			
			hisclinic.setItem_code(c_itemcode);//诊疗编码
			hisclinic.setItem_class("C");//诊疗类别   没传
			hisclinic.setItem_name(c_itemname);
			hisclinic.setExpand1(c__price);//诊疗项目价格
			hisclinic.setExpand2(specs);//规格
			hisclinic.setExpand3(unit);//单位
			hisclinic.setInput_code("");//没传
			clinicList.add(hisclinic);
			
			
		
			Element root = document.getRootElement();
			Node items = root.selectSingleNode("/Request/Body/items/item");
			//System.err.println("++++++++"+items.asXML());
			List<Element> item = items.getParent().elements();
			//Element element = (Element) root.element("items");
			//List<Element> item = element.elements("item");
			for (int i = 0; i < item.size(); i++) {
				
				HisPriceList hisprice = new HisPriceList();//his 价表
				
				String p_itemcode = item.get(i).selectSingleNode("itemcode").getText();//物价项目编码
				String p_itemname = item.get(i).selectSingleNode("itemname").getText();//物价项目名称
				String p_itemprice = item.get(i).selectSingleNode("itemprice").getText();//物件项目单价
				
				String p_itemamount= item.get(i).selectSingleNode("quantity").getText();//数量
				
				hisprice.setItem_code(p_itemcode);
				hisprice.setItem_class("C");// 没传
				hisprice.setItem_name(p_itemname);
				hisprice.setItem_spec("");//specs
				hisprice.setUnits("");//unit
				

		 		String 	startDate="1901-01-01 00:00:00.000";
		 		String 	stopDate="9999-12-31 23:59:59.000";
				
				hisprice.setStart_date(setdate(startDate));
				hisprice.setStop_date(setdate(stopDate));
				
				hisprice.setPrice(Double.parseDouble(p_itemprice));
				
				priceList.add(hisprice);
				
				HisClinicItemPriceDTO his_clinic_price = new HisClinicItemPriceDTO();//关系表
				his_clinic_price.setClinic_item_class("C");
				his_clinic_price.setClinic_item_code(c_itemcode);
				his_clinic_price.setCharge_item_classs("C");
				his_clinic_price.setCharge_item_no(1);
				his_clinic_price.setCharge_item_code(p_itemcode);
				his_clinic_price.setCharge_item_spec("");//规格  specs
				his_clinic_price.setUnits("");//单位 unit
				his_clinic_price.setAmount(Long.parseLong(p_itemamount));//数量
			
				clinicPriceList.add(his_clinic_price);
			}
			
			
			if(modify_type.equals("1")){//新增  
				
				insertClinicAndPrice(clinicList,priceList,clinicPriceList,logname);
				
			}else if(modify_type.equals("2")){//修改
				
				updateClinicAndPrice(clinicList,priceList,clinicPriceList,logname);
				
			}else if(modify_type.equals("0")){//删除
				
				deleteClinicAndPrice(clinicList,priceList,clinicPriceList,logname);
			}
			
			//ChargingItemModel model = new ChargingItemModel();
			
			//同步价表和套餐
 		boolean fly = updateHIsPriceSynchro(logname);
 		TranLogTxt.liswriteEror_to_txt(logname, "同步体检收费项目价表数据===end===,"+fly+"");
		if(fly==true){
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("收费价表同步成功!!");
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("收费价表同步失败!!");
		}
			/*rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("收费价表同步成功!!");*/
		}catch(Exception ex){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rb;
	}
	
	

	

	//删除 诊疗 价表 关系表数据
	 private void deleteClinicAndPrice(List<HisClinicItem> clinicList2, List<HisPriceList> priceList2,
			List<HisClinicItemPriceDTO> clinicPriceList2, String logname) {
		
		 for (HisClinicItem hisClinicItem : clinicList2) {
			
			 deleteClinic(hisClinicItem,logname);
		}
		 
		 for (HisPriceList hisPrice : priceList2) {
			
			 deletePrice(hisPrice,logname);
		}
		 
		 for (HisClinicItemPriceDTO hisClinicItemPriceDTO : clinicPriceList2) {
			
			 deleteClinicPrice(hisClinicItemPriceDTO,logname);
		}
		 
	}

	 //删除关系表数据
	private void deleteClinicPrice(HisClinicItemPriceDTO hisClinicItemPriceDTO, String logname) {
		Connection connection = null;
		   try {
			connection = this.jdbcQueryManager.getConnection();
			 //北京天坛医院 没有itemclass  默认为C
			String delsql = " delete his_clinic_item_v_price_list where clinic_item_code='"+hisClinicItemPriceDTO.getClinic_item_code()+"' "
					+ "  and charge_item_code='"+hisClinicItemPriceDTO.getCharge_item_code()+"' and charge_item_class='C' "
					+ "  and clinic_item_class='C' ";
			connection.createStatement().executeUpdate(delsql);
			TranLogTxt.liswriteEror_to_txt(logname,"res: " + delsql);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		  
	}

	//删除价表数据
	private void deletePrice(HisPriceList hisPrice, String logname) {
		
		Connection connection = null;
		   try {
			 //北京天坛医院 没有itemclass  默认为C
			connection = this.jdbcQueryManager.getConnection();
			String delsql =" delete his_price_list where item_code='"+hisPrice.getItem_code()+"'  and item_class='C' ";
			TranLogTxt.liswriteEror_to_txt(logname,"res: " + delsql);
			connection.createStatement().executeUpdate(delsql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	//删除 诊疗表数据
	private void deleteClinic(HisClinicItem hisClinicItem, String logname) {
		
		
		Connection connection = null;
		   try {
			connection = this.jdbcQueryManager.getConnection();
			//北京天坛医院 没有itemclass  默认为C
			String delsql = " delete his_clinic_item where item_code='"+hisClinicItem.getItem_code()+"' and item_class='C'  ";
			TranLogTxt.liswriteEror_to_txt(logname,"res: " + delsql);
			connection.createStatement().executeUpdate(delsql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	//更新 诊疗 价表 关系表数据
	private void updateClinicAndPrice(List<HisClinicItem> clinicList2, List<HisPriceList> priceList2,
			List<HisClinicItemPriceDTO> clinicPriceList2, String logname) {
		for (HisClinicItem hisClinicItem : clinicList2) {
			//更新诊疗表数据
			configService.updateClinicListTT(logname, hisClinicItem.getItem_code(), hisClinicItem.getItem_class(), hisClinicItem.getInput_code(),hisClinicItem.getItem_status(),hisClinicItem.getExpand1(),hisClinicItem.getItem_name());
		}
		
		for (HisPriceList hisPriceList : priceList2) {
			
			//更新价表数据
			configService.updatePriceList(logname, hisPriceList.getItem_code(), hisPriceList.getPrice()+"");
		}
		
		for (HisClinicItemPriceDTO hisClinicItemPriceDTO : clinicPriceList2) {
			
			//更新关系表数据
			configService.updateHisClinicItemVPriceList(logname, hisClinicItemPriceDTO.getClinic_item_class(), hisClinicItemPriceDTO.getClinic_item_code(), hisClinicItemPriceDTO.getCharge_item_classs(), hisClinicItemPriceDTO.getCharge_item_code(), hisClinicItemPriceDTO.getAmount()+"", hisClinicItemPriceDTO.getUnits());
		}
		
		
		
	}
	//插入 诊疗 价表 关系表数据
	private void insertClinicAndPrice(List<HisClinicItem> clinicList2, List<HisPriceList> priceList2,
			List<HisClinicItemPriceDTO> clinicPriceList2, String logname) {
		// TODO Auto-generated method stub
		configService.insertClinicList(logname, clinicList2);
		configService.insertPriceList(logname, priceList2);
		configService.insertClinicPriceList(logname, clinicPriceList2);
		
	}

	//更新体检收费项目表 价格
		private boolean updateHIsPriceSynchro(String logname) {
			/////////////////// copy to AutoGetHisDataNH.java_updateHIsPriceSynchro() start //////////////////////////////////////////////////////
			String sql = "";
			ResultSet charging_item_rs = null;
			ResultSet clinic_item_rs = null;
			
			Connection connection = null;
			boolean fal = false;
			try {
				connection = this.jdbcQueryManager.getConnection();
				sql = " SELECT  c.id,c.his_num,c.item_class,c.amount,c.hiscodeClass,c.item_name FROM   charging_item c  where  isActive='Y' and  his_num <> '' ";
				TranLogTxt.liswriteEror_to_txt(logname,"查询收费项目sql: " + sql);
				//查询所有  有效的 his_num不为空的 收费项目
				charging_item_rs = connection.createStatement().executeQuery(sql);
				while (charging_item_rs.next()) {
					
					
						sql = "	SELECT expand1,item_name FROM his_clinic_item where	item_code = '" + charging_item_rs.getString("his_num") +"' ";
							
						TranLogTxt.liswriteEror_to_txt(logname,"查询诊疗表sql: " + sql);
						
						//查询所有 item_code==his_num 不为空的 诊疗项目
						clinic_item_rs = connection.createStatement().executeQuery(sql);
						
						while (clinic_item_rs.next()) {//his_num对应诊疗项目
							double amount = clinic_item_rs.getDouble("expand1");
							String item_name = clinic_item_rs.getString("item_name");
							TranLogTxt.liswriteEror_to_txt(logname,"诊疗项目价格: " + amount);
							TranLogTxt.liswriteEror_to_txt(logname,"诊疗项目名称: " + item_name);
							TranLogTxt.liswriteEror_to_txt(logname,"收费项目价格: " + charging_item_rs.getDouble("amount"));
							TranLogTxt.liswriteEror_to_txt(logname,"收费项目价格是否和诊疗价格相等: " + (amount != charging_item_rs.getDouble("amount"))+"");
							
							
							if (amount != charging_item_rs.getDouble("amount")) {
								
								sql = " update charging_item set  amount = " + amount + "   where  id = '" + charging_item_rs.getLong("id") + "'";
								
								TranLogTxt.liswriteEror_to_txt(logname,"按照诊疗价格更新收费项目sql: " + sql);
								connection.createStatement().executeUpdate(sql);
								
							String setchargingitem =	("update set_charging_item set amount=" + amount
								+ "*itemnum,discount=10,item_amount=" + amount
								+ "  where charging_item_id='" + charging_item_rs.getLong("id") + "' ");
							TranLogTxt.liswriteEror_to_txt(logname, "res: :更改套餐与收费项目关系表的价格： " + setchargingitem);
							connection.createStatement().executeUpdate(setchargingitem);
							
							
							String setsql = "select exam_set_id from set_charging_item where charging_item_id='"
									+ charging_item_rs.getLong("id") + "'";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + setsql);
							ResultSet rss = connection.createStatement().executeQuery(setsql);
							if (rss.next()) {
								String upsql = "update exam_set set set_discount=10,"
										+ "price=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
										+ rss.getString("exam_set_id") + "'),set_amount=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
										+ rss.getString("exam_set_id") + "') " + "where id='" + rss.getString("exam_set_id") + "'";
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
								connection.createStatement().execute(upsql);										
							}
							rss.close();
							
							fal = true;
								
							}
						}
						
						
				}
				charging_item_rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				fal = false;
			} finally {
				try {
					
					if (clinic_item_rs != null) {
						clinic_item_rs.close();
					}
					if (charging_item_rs != null) {
						charging_item_rs.close();
					}
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					fal = false;
				}
			}
			return fal;
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
