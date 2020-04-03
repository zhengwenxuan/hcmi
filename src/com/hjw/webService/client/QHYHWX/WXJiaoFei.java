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
import com.hjw.webService.client.QHYHWX.bean.WeiXinDaTa;
import com.hjw.webService.client.QHYHWX.bean.WeiXinExaminePay;
import com.hjw.wst.DTO.ChargingDetailSingleDTO;
import com.hjw.wst.DTO.ChargingSummarySingleDTO;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.ExaminfoSetDTO;
import com.hjw.wst.DTO.SetChargingItemDTO;
import com.hjw.wst.service.BatchService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class WXJiaoFei {

	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static QueryManager queryManager;
	private static PersistenceManager persistenceManager;
	private static WebserviceConfigurationService webserviceConfigurationService;
	private static CustomerInfoService customerInfoService;
	private static BatchService batchService;
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
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
		batchService = (BatchService) wac.getBean("batchService");
	}
	
	
	public String ResWXJiaoFei(String req,String logname,String web_meth) {
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------微信缴费请求开始--------------------");
		TranLogTxt.liswriteEror_to_txt(logname, "微信缴费请求入参：" + req);

		String json = "";
		
		int customer_info_id=0;
		// 解析微信缴费入参
		WeiXinDaTa WXjiaofei = new Gson().fromJson(req, WeiXinDaTa.class);
		String id_num = WXjiaofei.getIdentityCard();// 身份证号
		String patname = WXjiaofei.getPatName();// 缴费人
		String fee = WXjiaofei.getFee();// 金额
		if (id_num.equals("") || id_num.length()<=0 ) {
			 json = "身份证号不能为空!";
		}else if(patname.equals("") || patname.length()<=0){
			 json = "缴费人姓名不能为空!";
		}else if(fee.equals("") ||fee.length()<=0){
			 json = "缴费金额不能为空!";
		}else {
			// 通过身份证号查询 体检档案表
			String customer_info_sql = " select * from customer_info where  is_Active='Y' and id_num ='" + id_num + "' ";
			 try {
				 Statement statement = this.jdbcQueryManager.getConnection().createStatement();
				 ResultSet customer_info_rs = statement.executeQuery(customer_info_sql);
				
				 while (customer_info_rs.next()) {
					
					 customer_info_id = customer_info_rs.getInt(1);
				}
				 customer_info_rs.close();
				 if(customer_info_id>0){
				//通过档案号查询examinfo表
				 ExamInfoDTO  examinfo =	 getExamInfoForCustomerId(customer_info_id);
				
				 //通过体检id查询  体检于收费项目关系表
				 List<ExaminfoChargingItemDTO> eci = this.customerInfoService.getExaminfoChargingItemforExamId(examinfo.getId());
				
				 	
				 //通过体检id  查询体检于套餐关系表
				 List<ExaminfoSetDTO>  examset =  getExaminfoSetforExaminfoId(examinfo.getId());
			
				 	
				//需要  判断微信端  缴费金额 暂时未判断
				 //保存结算信息到 个人结算总表
				for (int i = 0; i < examset.size(); i++) {
					
					ChargingSummarySingleDTO chargingSummary = new ChargingSummarySingleDTO();
					
					chargingSummary.setExam_id(examinfo.getId());
					chargingSummary.setInvoice_id(0);
					chargingSummary.setCharging_status("Y");
					chargingSummary.setAmount(examset.get(i).getAmount());
					chargingSummary.setAmount1(examset.get(i).getAmount());
					chargingSummary.setDiscount(examset.get(i).getDiscount());
					chargingSummary.setIs_print_recepit("N");
					chargingSummary.setCashier(1+"");
					chargingSummary.setCash_date(DateTimeUtil.getDateTime());
					chargingSummary.setCreater(1+"");
					chargingSummary.setCreate_time(DateTimeUtil.getDateTime());
					chargingSummary.setUpdater(1+"");
					chargingSummary.setUpdate_time(DateTimeUtil.getDateTime());
					
					String req_num = this.batchService.GetCreateID("rcpt_num");
					
					chargingSummary.setReq_num(req_num);
					chargingSummary.setIs_active("Y");
					
					//插入个人结算总表
					int Summary_id= insertChargingSummarySingle(chargingSummary);
				
					if(Summary_id>0){
					 	
						 //通过套餐id  查询套餐与收费关系表
						List<SetChargingItemDTO> chargingItemforSet = getSetChargingItemforSetId(examset.get(i).getExam_set_id());
						
						 for (int j = 0; j < chargingItemforSet.size(); j++) {
							 
							 ChargingDetailSingleDTO DetailSingle = new ChargingDetailSingleDTO();
							 
							 DetailSingle.setSummary_id(Summary_id);
							 DetailSingle.setCharging_item_id(chargingItemforSet.get(j).getCharging_item_id());
							 DetailSingle.setAmount(chargingItemforSet.get(j).getAmount());
							 DetailSingle.setItem_amount(chargingItemforSet.get(j).getItem_amount());
							 DetailSingle.setDiscount(chargingItemforSet.get(j).getDiscount());
							 DetailSingle.setCreater(chargingItemforSet.get(j).getCreater()+"");
							 DetailSingle.setCreate_time(DateTimeUtil.getDateTime());
							 DetailSingle.setUpdater(chargingItemforSet.get(j).getUpdater()+"");
							 DetailSingle.setUpdate_time(DateTimeUtil.getDateTime());
								
							 //插入个人结算明细表
							int DetailSingle_id= insertChargingDetailSingle(DetailSingle);
							 
							if(DetailSingle_id>0){
								WeiXinExaminePay Resjiaofei = new WeiXinExaminePay();
								Resjiaofei.setStatus("0");
								Resjiaofei.setErrorMsg("成功");
								Resjiaofei.getData().setFee(examset.get(i).getAmount()+"");
								Resjiaofei.getData().setIdentityCard(examinfo.getId_num());
								Resjiaofei.getData().setPatName(examinfo.getUser_name());
								Resjiaofei.getData().setPayTime(DateTimeUtil.getDateTime());
								
								json = new Gson().toJson(Resjiaofei, WeiXinExaminePay.class);
								
							}else{
								WeiXinExaminePay Resjiaofei = new WeiXinExaminePay();
								Resjiaofei.setStatus("1");
								Resjiaofei.setErrorMsg("失败");
								Resjiaofei.getData().setFee(examset.get(i).getAmount()+"");
								Resjiaofei.getData().setIdentityCard(examinfo.getId_num());
								Resjiaofei.getData().setPatName(examinfo.getUser_name());
								Resjiaofei.getData().setPayTime(DateTimeUtil.getDateTime());
								
								json = new Gson().toJson(Resjiaofei, WeiXinExaminePay.class);
							}
							
							}
						
						}
					
					}
				
				 }
				
			} catch (Exception e) {
				WeiXinExaminePay Resjiaofei = new WeiXinExaminePay();
				Resjiaofei.setStatus("1");
				Resjiaofei.setErrorMsg("失败");
				Resjiaofei.getData().setFee(fee);
				Resjiaofei.getData().setIdentityCard(id_num);
				Resjiaofei.getData().setPatName(patname);
				Resjiaofei.getData().setPayTime(DateTimeUtil.getDateTime());
				
				json = new Gson().toJson(Resjiaofei, WeiXinExaminePay.class);
				e.printStackTrace();
			}
			

		}

		TranLogTxt.liswriteEror_to_txt(logname, "输出微信缴费信息" + json);
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------微信缴费请求结束--------------------");
		TranLogTxt.liswriteEror_to_txt(logname, "");// 换行

		return json;
	}

	//插入个人结算明细表
	public int insertChargingDetailSingle(ChargingDetailSingleDTO detailSingle) {
		int detailSingle_id=0;
		String detailSingle_sql = " insert into charging_detail_single (summary_id,charging_item_id,amount1,amount,discount,creater,create_time,updater,update_time)  "
				+ "values('"+detailSingle.getSummary_id()+"','"+detailSingle.getCharging_item_id()+"','"+detailSingle.getItem_amount()+"','"+detailSingle.getAmount()+"',"
				+ "'"+detailSingle.getDiscount()+"','"+detailSingle.getCreater()+"','"+detailSingle.getCreate_time()+"','"+detailSingle.getUpdater()+"','"+detailSingle.getUpdate_time()+"') ";
		
				Connection connection;
				try {
					connection = this.jdbcQueryManager.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(detailSingle_sql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.executeUpdate();
					ResultSet detailSingle_rs = preparedStatement.getGeneratedKeys();
					while (detailSingle_rs.next()) {
						
						detailSingle_id =detailSingle_rs.getInt(1);
					}
					detailSingle_rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		
		return detailSingle_id;
	}

	//插入个人结算总表
	public  int  insertChargingSummarySingle(ChargingSummarySingleDTO chargingSummary) {
		
		int Summary_id=0;
		String SummarySql = " insert into charging_summary_single (exam_id,invoice_id,charging_status,amount1,discount,amount2,is_print_recepit,cashier,cash_date,creater,create_time,updater,update_time,req_num,is_active) "
				+ "values('"+chargingSummary.getExam_id()+"','"+chargingSummary.getInvoice_id()+"','"+chargingSummary.getCharging_status()+"','"+chargingSummary.getAmount()+"','"+chargingSummary.getDiscount()+"','"+chargingSummary.getAmount1()+"','"+chargingSummary.getIs_print_recepit()+"',"
				+ "'"+chargingSummary.getCashier()+"','"+chargingSummary.getCash_date()+"','"+chargingSummary.getCreater()+"','"+chargingSummary.getCreate_time()+"','"+chargingSummary.getUpdater()+"','"+chargingSummary.getUpdate_time()+"','"+chargingSummary.getReq_num()+"','"+chargingSummary.getIs_active()+"') ";
		
				Connection connection;
				try {
					connection = this.jdbcQueryManager.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(SummarySql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.executeUpdate();
					ResultSet Summary_rs = preparedStatement.getGeneratedKeys();
					while (Summary_rs.next()) {
						
						Summary_id =Summary_rs.getInt(1);
					}
					Summary_rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		
		return Summary_id;
	}


	//查询套餐与收费项目关系表
	public List<SetChargingItemDTO> getSetChargingItemforSetId(long set_id) {
		String setchargingitemsql = " select  * from set_charging_item  where exam_set_id='"+set_id+"' ";
		List<SetChargingItemDTO> setchargingitemlist = this.jdbcQueryManager.getList(setchargingitemsql,SetChargingItemDTO.class);
		return setchargingitemlist;
	}


	//查询体检人员与套餐关系表
	public  List<ExaminfoSetDTO> getExaminfoSetforExaminfoId(long examinfo_id) {
		String examinfosetsql = " select * from examinfo_set where examinfo_id='"+examinfo_id+"' and isActive='Y'";
		List<ExaminfoSetDTO> ExaminfoSetDTOlist = this.jdbcQueryManager.getList(examinfosetsql, ExaminfoSetDTO.class);
		return ExaminfoSetDTOlist;
	}


	//通过customer_id查询exam_info表
	public ExamInfoDTO getExamInfoForCustomerId(int customer_info_id) {
	
		String examinfosql = "Select  * from exam_info e,customer_info c where e.customer_id='"+customer_info_id+"' and e.customer_id=c.id and  c.is_Active='Y' and  e.is_Active='Y' order by e.create_time desc ";
		List<ExamInfoDTO> examInfoDTOlist = this.jdbcQueryManager.getList(examinfosql, ExamInfoDTO.class);
			
		return examInfoDTOlist.get(0);
	}
}
