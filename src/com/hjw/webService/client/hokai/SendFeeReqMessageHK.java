package com.hjw.webService.client.hokai;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.DTO.HisReqResult;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.Fees;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.config.GetNumContral;
import com.hjw.wst.domain.ChargingDetailSingle;
import com.hjw.wst.domain.ChargingSummarySingle;
import com.hjw.wst.domain.ExaminfoChargingItem;
import com.hjw.wst.service.CompanyService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

public class SendFeeReqMessageHK {
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static ConfigService configService;
	private static CompanyService companyService;
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
		companyService = (CompanyService) wac.getBean("companyService");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}

	public ResultHeader getMessage(String url, int days, String logName) {
		String sql = "select distinct ei.* from exam_info ei, customer_info ci ,examinfo_charging_item eci,charging_item c "
				+" where ei.customer_id = ci.id and ei.id = eci.examinfo_id and eci.charge_item_id = c.id "
				+" and ei.is_Active = 'Y' and ci.is_Active = 'Y' and eci.isActive = 'Y' "
				+" and eci.his_req_status = 'N' "
				//+" and eci.pay_status = 'R' "// -- 预付费
				+" and eci.pay_status in ('R','Y') "// -- 比较旧的版本里，登记台团体加项，eci的pay_status会默认Y，不会默认R
				+" and eci.exam_indicator = 'T' "// -- 团体付费
				+ " and (eci.exam_status in ('Y', 'G') or c.item_category = '耗材类型') "
				+" and ei.join_date >= '"+DateTimeUtil.DateDiff2(days)+"' order by ei.join_date ";
		TranLogTxt.liswriteEror_to_txt(logName, "sql: " + sql);
		List<ExamInfoUserDTO> userList = this.jdbcQueryManager.getList(sql, ExamInfoUserDTO.class);

		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
		UserDTO user = new UserDTO();
		user.setWork_num(doctorid);
		user.setName(doctorname);
		WebserviceConfigurationDTO wcf=new WebserviceConfigurationDTO();
		wcf=this.webserviceConfigurationService.getWebServiceConfig("PAYMENT_APPLICATION");
		
		int success = 0;
		for (ExamInfoUserDTO es : userList) {
			HisReqResult hrr = sendFeeReq(es, user, wcf, logName);
			if(hrr.getTrans_flag() == 1) {
				insert_his_req_result(hrr, logName);
				success ++;
			} else {
				insert_his_req_result(hrr, logName);
				TranLogTxt.liswriteEror_to_txt(logName, "自动发送HIS申请失败,体检编号:"+es.getExam_num()+",失败原因:" + hrr.getNote());
			}
		}
		ResultHeader rh = new ResultHeader();
		rh.setTypeCode("AA");
		rh.setText("本次自动发送lis申请,共执行"+userList.size()+"人,成功"+success+"人");
		return rh;
	}
	
	private HisReqResult sendFeeReq(ExamInfoUserDTO eu, UserDTO user, WebserviceConfigurationDTO wcf, String logName) {
		HisReqResult hrr = new HisReqResult();
		hrr.setExam_num(eu.getExam_num());
		try{
			
//		//查询人员信息
			String sql = "select * from examinfo_charging_item eci,charging_item c where 1=1 and eci.charge_item_id = c.id "
					+ " and eci.examinfo_id = "+eu.getId()
					+ " and eci.isActive = 'Y' "
					+ " and eci.his_req_status = 'N' "
					//+" and eci.pay_status = 'R' "// -- 预付费
					+" and eci.pay_status in ('R','Y') "// -- 比较旧的版本里，登记台团体加项，eci的pay_status会默认Y，不会默认R
					+ " and eci.exam_indicator = 'T' "// -- 团体付费
					+ " and (eci.exam_status in ('Y', 'G') or c.item_category = '耗材类型') ";
			
			TranLogTxt.liswriteEror_to_txt(logName, "查询eci-sql:"+sql);
			
			//根据体检号查询需要申请的项目
			List<ExaminfoChargingItem> itemList = this.jdbcQueryManager.getList(sql, ExaminfoChargingItem.class);
			if(itemList.size() == 0){
				hrr.setTrans_flag(2);
				hrr.setNote("该体检者不存在需要发缴费申请的项目!");
				return hrr;
			}
			
			Double amount = 0.0;  //计算总金额
			List<Fee> feeList = new ArrayList<Fee>(); //发送申请项目集合
			List<ChargingDetailSingle> detailList = new ArrayList<ChargingDetailSingle>();  //结算明细
			
			for(ExaminfoChargingItem item : itemList) {
				ChargingItemDTO charitem = customerInfoService.getChargingItemForId(item.getCharge_item_id());
//			ChargingItem charitem = (ChargingItem) this.jdbcQueryManager.getList(arg0, arg1)load(ChargingItem.class,); 
				
				amount += item.getPersonal_pay();
				//组装明细表
				ChargingDetailSingle chargingDetailSingle = new ChargingDetailSingle();
				chargingDetailSingle.setAmount(item.getItem_amount());
				chargingDetailSingle.setAmount1(item.getPersonal_pay());
				chargingDetailSingle.setCharging_item_id(item.getCharge_item_id());
				chargingDetailSingle.setDiscount(item.getDiscount());
				chargingDetailSingle.setCreater(user.getUserid());
				chargingDetailSingle.setCreate_time(DateTimeUtil.parse());
				chargingDetailSingle.setUpdater(user.getUserid());
				chargingDetailSingle.setUpdate_time(DateTimeUtil.parse());
				detailList.add(chargingDetailSingle);
				
				Fee fee = new Fee();				
				fee.setPATIENT_ID(eu.getPatient_id());
				fee.setEXAM_NUM(eu.getExam_num());
				fee.setVISIT_DATE(eu.getVisit_date());
				fee.setVISIT_NO(eu.getVisit_no());
				fee.setSERIAL_NO("");
				fee.setORDER_CLASS("");
				fee.setORDER_NO("1");
				fee.setORDER_SUB_NO("1");
				fee.setITEM_NO("1");
				fee.setITEM_CLASS("");
				fee.setITEM_NAME(charitem.getItem_name());
				fee.setITEM_CODE(charitem.getHis_num());
				fee.setITEM_SPEC("");
				fee.setUNITS("");
				fee.setREPETITION("1");
				
				fee.setORDERED_BY_DEPT(companyService.getDatadis("SQKS").get(0).getRemark());
				fee.setORDERED_BY_DOCTOR(user.getName());
				fee.setUSER_NAME(user.getWork_num());
				fee.setPERFORMED_BY(charitem.getPerform_dept());
				fee.setCLASS_ON_RCPT("");
				fee.setITEM_PRICE(charitem.getAmount().toString());
				fee.setRCPT_NO("");
				fee.setCHARGE_INDICATOR("0");
				fee.setCLASS_ON_RECKONING("");
				fee.setSUBJ_CODE("");
				fee.setPRICE_QUOTIETY("");
				fee.setCLINIC_NO(eu.getClinic_no());
				fee.setBILL_DATE("");
				fee.setBILL_NO("");
				fee.setSKINTEST("");// 皮试标志
				fee.setPRESC_PSNO(""); //皮试结果
				fee.setINSURANCE_FLAG("0"); //适用医保内外标志：0自费，1医保，2免费
				fee.setINSURANCE_CONSTRAINED_LEVEL("");// 公费用药级别
				fee.setSKIN_SAVE("");//皮试记录时间
				fee.setSKIN_START("");//皮试时间
				fee.setSKIN_BATH("");//药品批号
				fee.setCHARGES(item.getTeam_pay()+"");//实收金额
				fee.setExam_chargeItem_code(charitem.getItem_code());
				fee.setDiscount(item.getDiscount());
				feeList.add(fee);
			}

			//保存个人收费总表记录
			ChargingSummarySingle chargingSummarySingle = new ChargingSummarySingle();
			
			chargingSummarySingle.setExam_id(eu.getId());
			chargingSummarySingle.setReq_num(GetNumContral.getInstance().getParamNum("rcpt_num"));
			chargingSummarySingle.setCharging_status("R");//预付费为R
			chargingSummarySingle.setAmount1(Double.parseDouble(String.format("%.2f", amount)));
			chargingSummarySingle.setAmount2(Double.parseDouble(String.format("%.2f", amount)));
			chargingSummarySingle.setDiscount(10.00);
			chargingSummarySingle.setCashier(user.getUserid());
			chargingSummarySingle.setCash_date(DateTimeUtil.parse());
			chargingSummarySingle.setIs_print_recepit("N");
			chargingSummarySingle.setIs_active("Y");
			chargingSummarySingle.setCreater(user.getUserid());
			chargingSummarySingle.setCreate_time(DateTimeUtil.parse());
			chargingSummarySingle.setUpdater(user.getUserid());
			chargingSummarySingle.setUpdate_time(DateTimeUtil.parse());
			//chargingSummarySingle.setDaily_status("0");
			configService.jdbcsaveChargingSummarySingle(chargingSummarySingle); //保存收费总表
			for(ChargingDetailSingle chargingDetailSingle : detailList){//遍历保存明细数据
				chargingDetailSingle.setSummary_id(chargingSummarySingle.getId());
				configService.jdbcsaveChargingDetailSingle(chargingDetailSingle);
			}
			
			//开始发送申请
			FeeMessage fm = new FeeMessage();
			fm.setREQ_NO(chargingSummarySingle.getReq_num());
			Fees fees = new Fees();
			fees.setPROJECT(feeList);
			fm.setPROJECTS(fees);
			
			FEETermSendMessageHK fsm= new FEETermSendMessageHK(fm);		
			FeeResultBody frb= new FeeResultBody();
			frb = fsm.getMessage(wcf.getConfig_url().trim(),logName);
			System.out.println(frb.getResultHeader().getTypeCode() + "-" + frb.getResultHeader().getText());
			hrr.setReq_no(chargingSummarySingle.getReq_num());
			hrr.setItem_nums(itemList.size());
			hrr.setMessage_request(new Gson().toJson(fm, FeeMessage.class));
			if("AA".equals(frb.getResultHeader().getTypeCode())){//申请发送成功
				for(ExaminfoChargingItem examitem : itemList){
					String update_sql = "update examinfo_charging_item set his_req_status = 'Y' where id = "+examitem.getId();
					jdbcPersistenceManager.executeSql(update_sql);
				}
				hrr.setTrans_flag(1);
				hrr.setMessage_response(frb.getResultHeader().getText());
				return hrr;
			}else{
				hrr.setTrans_flag(2);
				hrr.setNote(frb.getResultHeader().getText());
				hrr.setMessage_response(frb.getResultHeader().getText());
				TranLogTxt.liswriteEror_to_txt(logName, frb.getResultHeader().getText());
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logName, com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
			hrr.setTrans_flag(2);
			hrr.setNote(com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		return hrr;
	}

	public boolean insert_his_req_result(HisReqResult hrr, String logname) throws ServiceException {
		String sql = "INSERT INTO [his_req_result] ([req_no] ,[message_id] ,[exam_num] ,[item_nums] ,"
				+ "[trans_flag] ,[create_time] ,[note] ,[message_request] ,[message_response]) "
				+ " VALUES ('"+hrr.getReq_no()+"', '"+hrr.getMessage_id()+"', '"+hrr.getExam_num()+"', "+hrr.getItem_nums()+", "
				+hrr.getTrans_flag()+", '"+hrr.getCreate_time()+"' ,'"+hrr.getNote()+"' ,'"+hrr.getMessage_request()+"' ,'"+hrr.getMessage_response()+"')";
		int resflag = -1;
		Connection connection = null;
		try {
			connection = this.jdbcQueryManager.getConnection();
			resflag = connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt(logname, "插入his_req_result sql："+sql);
			TranLogTxt.liswriteEror_to_txt(logname, "插入his_req_result 错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
		return resflag>0;
	}
}
