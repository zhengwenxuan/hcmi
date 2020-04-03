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
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.dashiqiao.FeeReqBean.ComAccBeanDSQ;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.wst.service.CompanyService;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Description: 发送团体缴费申请
 * @version V2.0.0.0
 */
public class FEETermSendMessageDSQ {
	private String accnum="";
	private String personid="";
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static CompanyService companyService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		companyService = (CompanyService) wac.getBean("companyService");
	}

	public FEETermSendMessageDSQ(String personid,String accnum) {
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
		
		
		
		ResHdMeessage rhd = new ResHdMeessage();
		ResultHeader  rh= new ResultHeader();
		try{
		String json = new Gson().toJson(cb, ComAccBeanDSQ.class);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + json + "\r\n");
		
		String result = HttpUtil.doPost_Str(url,json, "utf-8");
		
		
		if ((result != null) && (result.trim().length() > 0)) {
			
			rhd = new Gson().fromJson(result, ResHdMeessage.class);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
			if(rhd.getStatus().equals("1")){
				rh.setTypeCode("AA");
				rh.setText("接口发送成功");
			}else{
				rh.setTypeCode("AA");
				rh.setText("接口发送成功,his收费失败");
			}
			
		}else{
			rh.setTypeCode("AE");
			rh.setText("接口无返回");
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
			String sb1 = " select f.batch_num,f.com_name,a.account_num,a.amount1,a.amount2,c.invoice_num from charging_summary_group a "
					+ "left join (select c.id,c.batch_num,d.com_name from batch c,company_info d where c.company_id=d.id) f on f.id=a.batch_id, "
					+ "	charging_invoice_single c where c.id=a.invoice_id and a.account_num='"+account_num+"' and a.is_active='Y'  ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
		
				accnums.setACC_NUM(rs1.getString("account_num"));
				accnums.setCOMPANY_BATCH_NO(rs1.getString("batch_num"));
				accnums.setCOMPANY_NAME(rs1.getString("com_name"));
				accnums.setTOTAL_GROSS1(""+rs1.getDouble("amount1"));
				accnums.setTOTAL_GROSS2(""+rs1.getDouble("amount2"));
				accnums.setCLINIC_NO(rs1.getString("invoice_num"));
				
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
	
	
	
	
	
	
	
	
}
