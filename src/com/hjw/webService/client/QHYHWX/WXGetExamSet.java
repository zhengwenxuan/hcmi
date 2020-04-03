package com.hjw.webService.client.QHYHWX;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.QHYHWX.bean.WeiXinItemm;
import com.hjw.webService.client.QHYHWX.bean.WeiXinPackgesDTO;
import com.hjw.webService.client.QHYHWX.bean.WeiXinSetDTO;
import com.hjw.wst.DTO.ExamSetDTO;
import com.hjw.wst.domain.ChargingItem;
import com.hjw.wst.service.CollectFeesService;
import com.hjw.wst.service.CommService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.DataService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.hjw.wst.service.examInfoService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class WXGetExamSet {

	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static QueryManager queryManager;
	private static PersistenceManager persistenceManager;
	private static WebserviceConfigurationService webserviceConfigurationService;
	

	static{
    	init();
    	}
	
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		queryManager = (QueryManager) wac.getBean("queryManager");
		persistenceManager = (PersistenceManager) wac.getBean("persistenceManager");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	
	public String getPackageExamSet(String req,String logname,String web_meth) {
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------收到获取微信套餐请求--------------------");
		
		TranLogTxt.liswriteEror_to_txt(logname, "微信缴费请求入参：" + req);

		List<WeiXinPackgesDTO> WXpacklist = new ArrayList<>();

		// 查询 有效的 体检套餐
		String exam_set_sql = " select * from exam_set where is_Active='Y' ";
		List<ExamSetDTO> examsetlist = this.jdbcQueryManager.getList(exam_set_sql, ExamSetDTO.class);
		WeiXinSetDTO weiXinSetDTO = new WeiXinSetDTO();
		for (int i = 0; i < examsetlist.size(); i++) {
			weiXinSetDTO.setStatsu("0");
			weiXinSetDTO.setErrorMsg("成功");

			examsetlist.get(i).getId();

			WeiXinPackgesDTO packgesDTO = new WeiXinPackgesDTO();
			packgesDTO.setPackageName(examsetlist.get(i).getSet_name());// 套餐名称

			packgesDTO.setPrice(examsetlist.get(i).getSet_amount() + "");// 套餐价格
			packgesDTO.setDescription("");// 套餐描述
			packgesDTO.setPictures("");// 套餐配图
			packgesDTO.setNotice("");// 注意事项

			// 查询套餐 包含的收费项目
			String exam_set_charging_sql = " select * from charging_item where id in( select charging_item_id from set_charging_item where exam_set_id='"
					+ examsetlist.get(i).getId() + "'  ) ";
			List<ChargingItem> charginglist = this.jdbcQueryManager.getList(exam_set_charging_sql, ChargingItem.class);

			List<WeiXinItemm> WeiXinItemm = new ArrayList<>();

			for (int j = 0; j < charginglist.size(); j++) {

				WeiXinItemm xinItemm = new WeiXinItemm();
				xinItemm.setItemName(charginglist.get(j).getItem_name());
				xinItemm.setItemDescription("");
				WeiXinItemm.add(xinItemm);
			}

			packgesDTO.setItems(WeiXinItemm);
			WXpacklist.add(packgesDTO);

			weiXinSetDTO.setPackages(WXpacklist);

		}

		
		String json = new Gson().toJson(weiXinSetDTO, WeiXinSetDTO.class);
		System.err.println(json);

		TranLogTxt.liswriteEror_to_txt(logname, "输出体检套餐列表及项目" + json);
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------结束获取微信套餐请求--------------------");

	
		return json;
	}
}
