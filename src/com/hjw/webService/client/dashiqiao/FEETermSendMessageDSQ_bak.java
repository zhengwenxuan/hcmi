package com.hjw.webService.client.dashiqiao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.dashiqiao.FeeReqBean.ComAccBeanDSQ;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeDSQ;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeMessageDSQ;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeSends;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeesDSQ;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.webService.client.dashiqiao.ResCusBean.CustomrDSQ;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.CompanyService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Description: 发送团体缴费申请
 * @version V2.0.0.0
 */
public class FEETermSendMessageDSQ_bak {
	private String accnum="";
	private String personid="";
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static CompanyService companyService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		companyService = (CompanyService) wac.getBean("companyService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}

	public FEETermSendMessageDSQ_bak(String personid,String accnum) {
		this.accnum = accnum;
		this.personid=personid;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":"+this.accnum+":" + xml);
		try {
			ResultHeader rh = getString(url,logname);
			if(rh.getTypeCode().equals("AA")){
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("团体结算成功");
			}else{
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("团体结算失败");
			}
			
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "Exception： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息解析返回值错误");
		}
		
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + xml);
		return rb;
	}
	
	private ResultHeader getString(String url,String logname){
		ComAccBeanDSQ cb= new ComAccBeanDSQ();
		cb=this.getAcc_nums(accnum, logname);
		
		ResHdMeessage rhd = setSearchString(url, cb, logname);
		
		ResultHeader  rh= new ResultHeader();
		try{
			if(rhd.getStatus().equals("1") && !rhd.getResourceId().equals("0")){
				cb.setCLINIC_NO(rhd.getResourceId());
			
				FeeSends feeSends = new FeeSends();
				FeeMessageDSQ feeMessageDSQ = new FeeMessageDSQ();
				FeesDSQ feesDSQ = new FeesDSQ();
				ArrayList<FeeDSQ> FeeDSQList = new ArrayList<FeeDSQ>();
				FeeDSQ feeDSQ = new FeeDSQ();
				
				
				feeDSQ.setPATIENT_ID(cb.getACCOUNT_NUM());
				feeDSQ.setEXAM_NUM(cb.getACC_NUM());
				feeDSQ.setUSER_NAME(cb.getCOMPANY_NAME());
				feeDSQ.setVISIT_DATE(DateTimeUtil.getDateTime());
				feeDSQ.setVISIT_NO(rhd.getResourceId());
				feeDSQ.setSERIAL_NO("");
				feeDSQ.setORDER_CLASS("");
				feeDSQ.setORDER_NO("");
				feeDSQ.setORDER_SUB_NO("");
				feeDSQ.setITEM_NO("");
				feeDSQ.setITEM_CLASS("");
				feeDSQ.setITEM_NAME("查体费");
				feeDSQ.setITEM_CODE("123");
				feeDSQ.setITEM_SPEC("");
				feeDSQ.setUNITS("");
				feeDSQ.setREPETITION("");
				feeDSQ.setAMOUNT("1");
				feeDSQ.setORDERED_BY_DEPT("");
				feeDSQ.setORDERED_BY_DOCTOR("");
				feeDSQ.setPERFORMED_BY("");
				feeDSQ.setCLASS_ON_RCPT("");
				feeDSQ.setCOSTS(cb.getTOTAL_GROSS1());
				feeDSQ.setCHARGES(cb.getTOTAL_GROSS2());
				feeDSQ.setRCPT_NO("");
				feeDSQ.setCHARGE_INDICATOR("");
				feeDSQ.setCLASS_ON_RECKONING("");
				feeDSQ.setSUBJ_CODE("");
				feeDSQ.setPRICE_QUOTIETY("");
				feeDSQ.setITEM_PRICE("");
				feeDSQ.setCLINIC_NO(rhd.getResourceId());
				feeDSQ.setBILL_DATE("");
				feeDSQ.setBILL_NO("");
				feeDSQ.setSKINTEST("");
				feeDSQ.setPRESC_PSNO("");
				feeDSQ.setINSURANCE_FLAG("");
				feeDSQ.setINSURANCE_CONSTRAINED_LEVEL("");
				feeDSQ.setSKIN_SAVE("");
				feeDSQ.setSKIN_START("");
				feeDSQ.setSKIN_BATH("");
				feeDSQ.setYB_DIAG("");
				feeDSQ.setYB_DOCTOR("");
				feeDSQ.setExam_chargeItem_code("");
				feeDSQ.setCodeClass("");
				feeDSQ.setDiscount(0);
				feeDSQ.setItem_Abbreviation("CTF");
				
				FeeDSQList.add(feeDSQ);
				feesDSQ.setPROJECT(FeeDSQList);
				feeMessageDSQ.setMSG_TYPE("");
				feeMessageDSQ.setREQ_NO("");

				feeMessageDSQ.setPROJECTS(feesDSQ);

				feeSends.setFeeMessage(feeMessageDSQ);// his缴费申请信息
				
				String json = new Gson().toJson(feeSends, FeeSends.class);
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + json + "\r\n");
				
				String result = HttpUtil.doPost_Str(url,json, "utf-8");
				
				
				if ((result != null) && (result.trim().length() > 0)) {
					
					rhd = new Gson().fromJson(result, ResHdMeessage.class);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
					if(rhd.getStatus().equals("1")){
						rh.setTypeCode("AA");
						rh.setText("获取HIS流水号成功");
						insert_search(cb, logname);
						
					}else{
						rh.setTypeCode("AE");
						rh.setText("接口发送成功,his收费失败");
					}
					
				}else{
					rh.setTypeCode("AE");
					rh.setText("接口发送成功,his收费失败");
				}
			}else{
				rh.setTypeCode("AE");
				rh.setText("获取HIS流水号失败");
			}
			
		}catch(Exception ex){
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		
		
		return rh;
	}
		



	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public ComAccBeanDSQ getAcc_nums(String account_num,String logname){
		Connection tjtmpconnect = null;
		ComAccBeanDSQ accnums = new ComAccBeanDSQ();
		List<String> list = new ArrayList<String>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select f.batch_num,f.com_name,b.acc_num,b.account_num,a.amount1,a.amount2 from charging_summary_group a "
					+ "left join (select c.id,c.batch_num,d.com_name from batch c,company_info d where c.company_id=d.id) f on f.id=a.batch_id"
					+ ",team_invoice_account b  "
					+ "where a.account_num=b.account_num and a.account_num='"+account_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
		
				accnums.setACC_NUM(rs1.getString("acc_num"));//结算单号
				accnums.setACCOUNT_NUM(rs1.getString("account_num"));//申请单号
				accnums.setCOMPANY_BATCH_NO(rs1.getString("batch_num"));//批次
				accnums.setCOMPANY_NAME(rs1.getString("com_name"));//单位名称
				accnums.setTOTAL_GROSS1(""+rs1.getDouble("amount1"));//应收金额
				accnums.setTOTAL_GROSS2(""+rs1.getDouble("amount2"));//实收金额
				
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		return accnums;
	}
	
	
	
			/**
			 * 新增
			 * @return
			 */
private ResHdMeessage setSearchString(String url,ComAccBeanDSQ cb,String logname){
		WebserviceConfigurationDTO wcf=new WebserviceConfigurationDTO();
		ResHdMeessage rh = new ResHdMeessage();
	//	ExamInfoUserDTO eu = getExamInfoForNum(custom.getEXAM_NUM(), logname);
		
		CustomrDSQ customrDSQ = new  CustomrDSQ();
		customrDSQ.setMSG_TYPE("");
		customrDSQ.setPATIENT_ID(cb.getCOMPANY_BATCH_NO());
		customrDSQ.setEXAM_NUM(cb.getACC_NUM());
		customrDSQ.setNAME(cb.getCOMPANY_NAME());
		customrDSQ.setNAME_PHONETIC("");
		customrDSQ.setSEX("");
		customrDSQ.setDATE_OF_BIRTH("");
		customrDSQ.setBIRTH_PLACE("");
		customrDSQ.setCITIZENSHIP("");
		customrDSQ.setNATION("");
		customrDSQ.setIDENTITY("");
		customrDSQ.setID_NO("");
		customrDSQ.setIDENTITY("");
		customrDSQ.setUNIT_IN_CONTRACT("");
		customrDSQ.setMAILING_ADDRESS("");
		customrDSQ.setZIP_CODE("");
		customrDSQ.setPHONE_NUMBER_HOME("");
		customrDSQ.setPHONE_NUMBER_BUSINESS("");
		customrDSQ.setOPERATOR("");
		customrDSQ.setBUSINESS_ZIP_CODE("");
		customrDSQ.setPHOTO("");
		customrDSQ.setPATIENT_CLASS("");
		customrDSQ.setDEGREE("");
		customrDSQ.setE_NAME("");
		customrDSQ.setOCCUPATION("");
		customrDSQ.setNATIVE_PLACE("");
		customrDSQ.setMAILING_ADDRESS_CODE("");
		customrDSQ.setMAILING_STREET_CODE("");
		customrDSQ.setALERGY("");
		customrDSQ.setMARITAL_STATUS("");
		customrDSQ.setNEXT_OF_SEX("");
		customrDSQ.setNEXT_OF_BATH("");
		customrDSQ.setNEXT_OF_ID("");
		customrDSQ.setAGE("");
		customrDSQ.setCHARGE_TYPE("");
		customrDSQ.setVISIT_DEPT("");
		customrDSQ.setOPERATORS("");
		customrDSQ.setCARD_NAME("");
		customrDSQ.setCARD_NO("");
		customrDSQ.setINVOICE_NO("");
		customrDSQ.setCLINIC_NO("");
		customrDSQ.setCLINIC_DATE_SCHEDULED("");
		
		
		String json = new Gson().toJson(customrDSQ, CustomrDSQ.class);
		TranLogTxt.liswriteEror_to_txt(logname,"request:"+json);
		
		
		wcf=this.webserviceConfigurationService.getWebServiceConfig("JZKCUST_APPLICATION");
		
		String result = HttpUtil.doPost_Str(wcf.getConfig_url(), json, "utf-8");
		
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
		if(result.length()>5 && result !=null && !result.equals("")){
			System.err.println("人员挂号新增正常返回数据了");
			 rh = new Gson().fromJson(result, ResHdMeessage.class);
			 rh.setMessage("获取HIS流水号成功");
		}else{
			rh.setStatus("AE");
			rh.setMessage("获取HIS流水号失败");
		}
		
		
		return rh;
		}
	
	
		private void insert_search(ComAccBeanDSQ cb, String logname) {
			Connection tjtmpconnect = null;
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				
				String insertsql = "insert into zl_req_term_his_item values ('" + cb.getCOMPANY_BATCH_NO()+ "','" + cb.getACC_NUM()
						+ "','"+cb.getACCOUNT_NUM()+"','" + DateTimeUtil.getDateTime() + "','0','" + cb.getCLINIC_NO() + "','" + cb.getCOMPANY_NAME() + "')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + insertsql + "\r\n");
				tjtmpconnect.createStatement().executeUpdate(insertsql);
			} catch (Exception ex) {

			} finally {
				try {
					if (tjtmpconnect != null) {
						tjtmpconnect.close();
					}
				} catch (SQLException sqle4) {
					sqle4.printStackTrace();
				}
			}
		}
	
}
