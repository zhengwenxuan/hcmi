package com.hjw.webService.client.tiantan;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tiantan.bean.HRPBean;
import com.hjw.webService.client.tiantan.bean.HRPData;
import com.hjw.webService.client.tiantan.bean.HRPResMessage;
import com.hjw.webService.client.tiantan.soap.TJGL;
import com.hjw.webService.client.tiantan.soap.TJGLLocator;
import com.hjw.webService.client.tiantan.soap.TJGLSoap_PortType;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.HisClinicItemPriceListDTO;
import com.hjw.wst.domain.ChargingItem;
import com.hjw.wst.domain.ExaminfoChargingItem;
import com.hjw.wst.domain.sysSurvey;
import com.hjw.wst.service.ChargingItemService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

public class insertHRPDataMessageTT {

	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static ConfigService configService;
	private static ChargingItemService chargingItemService;
	private static CustomerInfoService customerInfoService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
		chargingItemService = (ChargingItemService) wac.getBean("chargingItemService");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	
	
	public ResultHeader getMessage(String url, int days, String logName) {
		ResultHeader rh = new ResultHeader();
		
		/*
		 * 查询需要上传给hrp的人员
		 */
		try {
			
			String sql1 = " select distinct ei.* from exam_info ei, customer_info ci ,examinfo_charging_item eci,charging_item c "
					+" where ei.customer_id = ci.id and ei.id = eci.examinfo_id and eci.charge_item_id = c.id "
					+" and ei.is_Active = 'Y' and ci.is_Active = 'Y' and eci.isActive = 'Y' "
					+" and eci.his_req_status = 'N' "
					//+" and eci.pay_status = 'R' "			// -- 预付费
					+" and eci.pay_status in ('R','Y') "	// -- 比较旧的版本里，登记台团体加项，eci的pay_status会默认Y，不会默认R
					//+" and eci.exam_indicator = 'T' "   	// -- 团体付费
					+" and (eci.exam_status in ('Y', 'G') or c.item_category = '耗材类型') "
					+" and ei.join_date >= '"+DateTimeUtil.DateDiff2(days)+"' order by ei.join_date ";
			
			
			TranLogTxt.liswriteEror_to_txt(logName, "sql: " + sql1);
			List<ExamInfoUserDTO> userList = this.jdbcQueryManager.getList(sql1, ExamInfoUserDTO.class);
			
			for (ExamInfoUserDTO eu : userList) {
				
				//给hrp 上传数据
				rh = 	sendHRPData(eu,logName,url);
			}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return rh;
	}
	
	/*
	 * 给hrp 上传数据
	 */
	private ResultHeader sendHRPData(ExamInfoUserDTO eu, String logname,String url) {
		ResultHeader rh = new ResultHeader();
		
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String deptid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单医生id
		//String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
		
		//根据体检号查询需要 上传至 hrp的项目
			String sql2 = " select * from examinfo_charging_item eci,charging_item c where 1=1 and eci.charge_item_id = c.id "
					+ " and eci.examinfo_id = "+eu.getId()
					+ " and eci.isActive = 'Y' "
					+ " and eci.his_req_status = 'N' "
					//+" and eci.pay_status = 'R' "			// -- 预付费
					+ " and eci.pay_status in ('R','Y') "	// -- 比较旧的版本里，登记台团体加项，eci的pay_status会默认Y，不会默认R
					//+ " and eci.exam_indicator = 'T' "	// -- 团体付费
					+ " and (eci.exam_status in ('Y', 'G') or c.item_category = '耗材类型') ";
			
			TranLogTxt.liswriteEror_to_txt(logname, "查询eci-sql:"+sql2);
			
			
			List<ExaminfoChargingItem> itemList = this.jdbcQueryManager.getList(sql2, ExaminfoChargingItem.class);
			
			
			for (ExaminfoChargingItem eci : itemList) {
				long charge_item_id = eci.getCharge_item_id();
				ChargingItem ci = chargingItemService.findChargingItem(charge_item_id);
				
				HisClinicItemPriceListDTO dto = new HisClinicItemPriceListDTO();
				dto.setClinic_item_code(ci.getHis_num());
				dto.setSystemdate(DateUtil.getDateTime());
				List<HisClinicItemPriceListDTO> jiabiao = configService.getHisjgTT(dto);
				
				if(jiabiao.size()<=0 && jiabiao!=null){
					System.err.println("通过his_num未找到价表项目"+ci.getHis_num());
					TranLogTxt.liswriteEror_to_txt(logname, "通过his_num未找到价表项目 "+ci.getHis_num());
				}else{
				
				HRPBean hrpBean = new HRPBean();
				hrpBean.setSenderID("328776976754020352");//固定值
				ArrayList<HRPData> datas = new ArrayList<>();
			
				for (HisClinicItemPriceListDTO hisDto : jiabiao) {
					
					HRPData data = new HRPData();
					
					data.setYWLSH(eu.getExam_num());
				    data.setDate(null);
					data.setOrder_date(eu.getJoin_date());
					data.setOrder_dept(deptid);
					data.setOrder_doc(doctorid);
					data.setUndrug_code(ci.getHis_num());
					data.setUndrug_name(ci.getItem_name());
					data.setItemcode(hisDto.getItem_code_p());
					data.setItenname(hisDto.getItem_name_p());
					double parseDouble = Double.parseDouble(hisDto.getAmount()+"");
					
					data.setQuantity(parseDouble+"");
					data.setPrice(hisDto.getPrice()+"");
					data.setAmount(hisDto.getPrice()+"");
					data.setPatno(eu.getArch_num());
					data.setPatname(eu.getUser_name());
					data.setOper_date(""); //--  执行时间
					data.setOper_dept(""); //-- 执行科室
					data.setOper_doc("");  //--  执行人员
					data.setCreate_Time(DateUtil.getDateTime());
					datas.add(data);
					
				}
				hrpBean.setDatas(datas);
				
				
				String json =  new GsonBuilder().serializeNulls().create().toJson(hrpBean, HRPBean.class);
				TranLogTxt.liswriteEror_to_txt(logname, "上传hrp:体检号 "+eu.getExam_num()+"==项目名称"+ci.getItem_name()+"==诊疗编码"+ci.getHis_num()+"==入参数据" + json);
				try {
				
					
					TJGL tjgl = new TJGLLocator(url.split("&&")[0]);
					TJGLSoap_PortType tjglSoap = tjgl.getTJGLSoap12();
					String insertTJDatas = tjglSoap.insertTJDatas(json);
				
					TranLogTxt.liswriteEror_to_txt(logname, "上传hrp返回:体检号 "+eu.getExam_num()+"==项目名称"+ci.getItem_name()+"==诊疗编码"+ci.getHis_num()+"==返回数据" + insertTJDatas);
					
					if(insertTJDatas!=null && insertTJDatas.length()>5){
						HRPResMessage hrpRes = new Gson().fromJson(insertTJDatas, HRPResMessage.class);
						
						if(hrpRes.getSTATUS().equals("AA")){
							rh.setTypeCode("AA");
							rh.setText(hrpRes.getMessage());
							
							
							String update_sql = "update examinfo_charging_item set his_req_status = 'Y' where id = "+eci.getId();
							jdbcPersistenceManager.executeSql(update_sql);
							
						}else{
							rh.setTypeCode("AE");
							rh.setText(hrpRes.getMessage());
						}
					}else{
						rh.setTypeCode("AE");
						rh.setText("HRP返回异常");
					}
					
					//String updateTJDatas = tj.updateTJDatas("");
				} catch (Exception e) {
					rh.setTypeCode("AE");
					rh.setText("HRP返回出现错误");
					e.printStackTrace();
				}
				
			}
				
			}
		return rh;
	}

}
