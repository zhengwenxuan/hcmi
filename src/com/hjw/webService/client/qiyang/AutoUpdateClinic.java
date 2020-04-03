package com.hjw.webService.client.qiyang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.wst.DTO.HisClinicItemDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.CommService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.hjw.wst.service.examInfoService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class AutoUpdateClinic {

	private static JdbcQueryManager jdbcQueryManager;
	private static CustomerInfoService customerInfoService;
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	private static CommService commService;   //examInfoService
	private static examInfoService examInfoService;
	private QueryManager qm;
	private JdbcQueryManager jqm;
	private JdbcPersistenceManager jpm;
	private PersistenceManager pm;
	
	
	
	public QueryManager getQm() {
		return qm;
	}


	public void setQm(QueryManager qm) {
		this.qm = qm;
	}


	public JdbcQueryManager getJqm() {
		return jqm;
	}


	public void setJqm(JdbcQueryManager jqm) {
		this.jqm = jqm;
	}


	public JdbcPersistenceManager getJpm() {
		return jpm;
	}


	public void setJpm(JdbcPersistenceManager jpm) {
		this.jpm = jpm;
	}


	public PersistenceManager getPm() {
		return pm;
	}


	public void setPm(PersistenceManager pm) {
		this.pm = pm;
	}


	static{
    	init();
    	}
    
    

	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
		configService = (ConfigService) wac.getBean("configService");
		commService = (CommService) wac.getBean("commService");  //
		examInfoService = (examInfoService) wac.getBean("examInfoService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	
	
	private void UpdateHisClinicItem(){
		
		ArrayList<HisClinicItemDTO> HisClinic = AutoHisClinic();
		for (int i = 0; i < HisClinic.size(); i++) {
			
			String sql = "insert into his_clinic_item (item_code,item_name,price,expand1) "
					+ "values ('"+HisClinic.get(i).getItem_code()+"','"+HisClinic.get(i).getItem_name()+"','"+HisClinic.get(i).getPrice()+"','"+HisClinic.get(i).getUnits()+"')";
			TranLogTxt.liswriteEror_to_txt("更新诊疗", "res:" + "=="+sql +"\r\n");
			this.jpm.execSql(sql);
		}
		
		
	}
	
	private ArrayList<HisClinicItemDTO> AutoHisClinic(){
		
		WebserviceConfigurationDTO wcf=new WebserviceConfigurationDTO();
		wcf=this.webserviceConfigurationService.getWebServiceConfig("PAYMENT_APPLICATION");
		Connection conn = null;
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet result = null;// 
		
	    String url = wcf.getConfig_url();//oracle数据库url
		String[] split = wcf.getConfig_value().split(",");
		String user = split[0];// 获取用户名
		String password = split[1];//获取密码
		
	
		ArrayList<HisClinicItemDTO> list = new ArrayList<HisClinicItemDTO>();
		
		try {
			conn = OracleDatabaseSource.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 try {
			CallableStatement call = conn.prepareCall("{call select * from zhi4_peis_wjw.v_get_clinic_dict}");
			
			ResultSet rs = call.executeQuery();
			
			while (rs.next()) {
				
				HisClinicItemDTO Clinic = new HisClinicItemDTO();
				Clinic.setItem_code(rs.getString(1));
				Clinic.setItem_name(rs.getString(2));
				Clinic.setPrice(rs.getLong(3));
				Clinic.setUnits(rs.getString(4));
				
				list.add(Clinic);
				
				
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行存储过程
		return list;
		
	}
}
