package com.hjw.webService.client.QHYHWX;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.QHYHWX.bean.WXJieGuoResCustomerDTO;
import com.hjw.webService.client.QHYHWX.bean.WeiXinExaminePay;
import com.hjw.webService.client.QHYHWX.bean.WeiXinReqUpdateCustomer;
import com.hjw.webService.client.QHYHWX.bean.WeiXinResUpdateCustomer;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.domain.CustomerInfo;
import com.hjw.wst.domain.ExamInfo;
import com.hjw.wst.service.BatchService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class WXUpdateCustomerInfo {

	
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static QueryManager queryManager;
	private static PersistenceManager persistenceManager;
	private static WebserviceConfigurationService webserviceConfigurationService;
	private static BatchService batchService;
	private static CustomerInfoService customerInfoService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		queryManager = (QueryManager) wac.getBean("queryManager");
		persistenceManager = (PersistenceManager) wac.getBean("persistenceManager");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		batchService = (BatchService) wac.getBean("batchService"); //customerInfoService
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}
	
	
	
	public  String WXResUpdatemessage(String req, String logname, String web_meth){
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------微信更新个人信息资料开始--------------------");
		TranLogTxt.liswriteEror_to_txt(logname, "微信更新个人信息资料入参：" + req);
		String json="";
		int customer_info_id=0;
		
		
		WeiXinReqUpdateCustomer updateCustomer = new Gson().fromJson(req, WeiXinReqUpdateCustomer.class);
		
		String id_num = updateCustomer.getIdentityCard();
		String user_name = updateCustomer.getPatName();
		String sex = updateCustomer.getSex();
		String age = updateCustomer.getAge();
		String workPlace = updateCustomer.getWorkPlace();
		String patPhoto = updateCustomer.getPatPhoto();
		
		
		if(id_num.equals("") || id_num.length()<=0){
			json="身份证号不能为空";
		}else if(user_name.equals("") || user_name.length()<=0){
			json="名称不能为空";
		}else if(sex.equals("") || sex.length()<=0){
			json="性别不能为空";
		}else if(age.equals("") || age.length()<=0){
			json="年龄不能为空";
		}else if(patPhoto.equals("") || patPhoto.length()<=0){
			json="身份证照片不能为空";
		}else{
			
			
			String customer_info_sql = " select * from customer_info where  is_Active='Y' and id_num ='" + id_num + "' ";
			List<CustomerInfo> customerInfoDTOlist = this.jdbcQueryManager.getList(customer_info_sql, CustomerInfo.class);
					 
					 if(customerInfoDTOlist.get(0).getId()>0){
						 
						 
						 @SuppressWarnings("unused")
						int c_faly = updateCustomerInfo(customerInfoDTOlist.get(0).getId(),sex,user_name);
						 
						 @SuppressWarnings("unused")
						int e_faly = updateexaminfo(customerInfoDTOlist.get(0).getId(),age,workPlace);
						
						if(c_faly>0 && e_faly>0){
							
							WeiXinResUpdateCustomer resupdate = new WeiXinResUpdateCustomer();
							WXJieGuoResCustomerDTO resupdedata = new WXJieGuoResCustomerDTO();
							resupdedata.setAge(age);
							resupdedata.setIdentityCard(id_num);
							resupdedata.setPatName(user_name);
							resupdedata.setSex(sex);
							resupdedata.setWorkPlace(workPlace);
							resupdedata.setDate(DateTimeUtil.getDateTime());
							
							resupdate.setErrorMsg("成功");
							resupdate.setStatus("0");
							
							
							resupdate.setData(resupdedata);
							
							
							json = new Gson().toJson(resupdate, WeiXinResUpdateCustomer.class);
							
						}
						
						
					 
					 }else{
						 WeiXinResUpdateCustomer resupdate = new WeiXinResUpdateCustomer();
							WXJieGuoResCustomerDTO resupdedata = new WXJieGuoResCustomerDTO();
							resupdedata.setAge(age);
							resupdedata.setIdentityCard(id_num);
							resupdedata.setPatName(user_name);
							resupdedata.setSex(sex);
							resupdedata.setWorkPlace(workPlace);
							resupdedata.setDate(DateTimeUtil.getDateTime());
							
							resupdate.setErrorMsg("失败");
							resupdate.setStatus("1");
							
							
							resupdate.setData(resupdedata);
							
							
							json = new Gson().toJson(resupdate, WeiXinResUpdateCustomer.class);
					 }
					 
					 
			}
		
		//}	
		
		TranLogTxt.liswriteEror_to_txt(logname, "微信更新个人信息资料输出" + json);
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------微信更新个人信息资料结束--------------------");
		TranLogTxt.liswriteEror_to_txt(logname, "");// 换行
		return json;
		
		
	}
	
	



	private int updateCustomerInfo(long customer_ifno_id, String sex, String user_name) {
		int faly=0;
		String customer_info_sql = "  update customer_info set sex='"+sex+"',user_name='"+user_name+"' where id='"+customer_ifno_id+"' and is_Active='Y' ";
				Connection connection;
				try {
					connection = this.jdbcQueryManager.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(customer_info_sql);
					int executeUpdate = preparedStatement.executeUpdate();
				if(executeUpdate>0){
					faly=executeUpdate;
				}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		return faly;
	}



	private int updateexaminfo(long customer_info_id,String age ,String company) {
		int faly=0;
		String examinfo_sql = " update exam_info set age='"+age+"',company='"+company+"' where customer_id='"+customer_info_id+"' and is_Active='Y' ";
				Connection connection;
				try {
					connection = this.jdbcQueryManager.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(examinfo_sql);
					int executeUpdate = preparedStatement.executeUpdate();
				if(executeUpdate>0){
					faly=executeUpdate;
				}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		
		return faly;
		
	}



	public ExamInfoUserDTO getexaminfochargingitemuser(long customer_id){
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select c.id,a.birthday,a.user_name,a.arch_num,a.id_num,a.sex,c.age,c.exam_num,c.is_marriage,"
						+ "c.position,c._level,c.group_id,n.group_name,c.remarke,c.others,c.status,c.employeeID");
		sb.append(",c.is_need_barcode,c.is_need_guide,c.phone,c.join_date,c.status,n.group_name,m.dep_name,"
				+ "c.register_date,c.exam_times,c.exam_type,x.type_name as customer_type_name,x.type_name as tjlxname,c.company_id,"
				+ "z.company_name,z.batch_name,z.contract_num,z.validity_date,c.address,c.batch_id,c.company,k.batch_name,c.final_date ");
		sb.append(" from customer_info a,exam_info c ");
		sb.append(" left join examinfo_batch b on b.examinfo_id=c.id ");
		sb.append(" left join customer_type x on x.id=c.customer_type_id ");
		sb.append("  left join data_dictionary y on y.data_code='TJLX' and y.id=c.customer_type  ");
		sb.append(" left join group_info  n on n.id=c.group_id ");
		sb.append(" left join batch  k on k.id=c.batch_id ");
		sb.append(" left join company_department  m on m.id = c._level  ");
		sb.append(" left join contract z on z.batch_id=c.batch_id and z.company_id= c.company_id ");
		sb.append(" where c.customer_id=a.id ");
		sb.append(" and a.id='" + customer_id + "' ");
		List<ExamInfoUserDTO> eiList = this.jdbcQueryManager.getList(sb.toString(), ExamInfoUserDTO.class);
		ExamInfoUserDTO ei = new ExamInfoUserDTO();
		if ((eiList != null) && (eiList.size() > 0)) {
			ei = eiList.get(0);
	}
		return ei;
	}	
}
