package com.hjw.webService.client.tiantan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
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
import com.hjw.wst.service.ChargingItemService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.opensymphony.util.DataUtil;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

public class updateHRPDataMessageTT {

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
	
	
	public ResultHeader getMessage(String url, int days, String logname) {
		
		ResultHeader rh = new ResultHeader();
		
		String sql1 = " select distinct ei.* from exam_info ei, customer_info ci ,examinfo_charging_item eci,charging_item c "
				+" where ei.customer_id = ci.id and ei.id = eci.examinfo_id and eci.charge_item_id = c.id "
				+" and ei.is_Active = 'Y' and ci.is_Active = 'Y' and eci.isActive = 'Y' "
				+" and eci.his_req_status = 'Y' "
				//+" and eci.pay_status = 'R' "		  // -- 预付费
				+" and eci.pay_status in ('R','Y') "  // -- 比较旧的版本里，登记台团体加项，eci的pay_status会默认Y，不会默认R
				//+ "and ei.is_report_print='Y' "		  //--  是否打印
				+ "and ei.status='Z' "				  //--  总检状态
				//+" and eci.exam_indicator = 'T' "   // -- 团体付费
				+" and (eci.exam_status in ('Y', 'G') or c.item_category = '耗材类型') "
				+" and ei.join_date >= '"+DateTimeUtil.DateDiff2(days)+"' order by ei.join_date ";
		
		
		TranLogTxt.liswriteEror_to_txt(logname, "sql: " + sql1);
		List<ExamInfoUserDTO> userList = this.jdbcQueryManager.getList(sql1, ExamInfoUserDTO.class);
		
		HRPBean hrpBean = new HRPBean();
		hrpBean.setSenderID("328776976754020352");//固定值
		ArrayList<HRPData> datas = new ArrayList<>();
		
		for (ExamInfoUserDTO eu : userList) {
			HRPData data = new HRPData();
			data.setYWLSH(eu.getExam_num());
			Date parse = DateUtil.parse(eu.getFinal_date());
			data.setDate(parse);
			datas.add(data);
			
			
		}
		hrpBean.setDatas(datas);
		
		
		String json = new Gson().toJson(hrpBean, HRPBean.class);
		
		TranLogTxt.liswriteEror_to_txt(logname, "更新hrp:入参 "+ json);
		
		try {
			TJGL tjgl = new TJGLLocator(url.split("&&")[0]);
			TJGLSoap_PortType tjglSoap = tjgl.getTJGLSoap12();
			
			String updateTJDatas = tjglSoap.updateTJDatas(json);
			
			if(updateTJDatas!=null && updateTJDatas.length()>5){
				
				HRPResMessage hrpRes = new Gson().fromJson(updateTJDatas, HRPResMessage.class);
				
				if(hrpRes.getSTATUS().equals("AA")){
					rh.setTypeCode("AA");
					rh.setText(hrpRes.getMessage());
				}else{
					rh.setTypeCode("AE");
					rh.setText(hrpRes.getMessage());
				}
			}else{
				rh.setTypeCode("AE");
				rh.setText("HRP返回异常");
			}
			
		} catch (Exception e) {
			rh.setTypeCode("AE");
			rh.setText("HRP返回出现错误");
			e.printStackTrace();
		}
		
		return rh;
		
	}
}
