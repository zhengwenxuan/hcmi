package com.hjw.webService.client.zixing;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.bean.TeamAccBeanHK;
import com.hjw.webService.client.hokai.bean.TeamItemBeanHK;
import com.hjw.webService.client.huojianwa.bean.ComAccBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.CompanyService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class TTHisSendMessageZX {

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

	public TTHisSendMessageZX(String personid,String accnum) {
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
		ResultHeader rh=getString(url,logname);
		if(rh.getTypeCode().equals("AA")){
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("his团体结算成功!!");
			
			
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("his团体结算是失败!!");
		}
		
		return rb;
	}

	private ResultHeader getString(String url, String logname) {
		ResultHeader rh= new ResultHeader();
		Connection connection = null;
		WebserviceConfigurationDTO wcf=new WebserviceConfigurationDTO();
		wcf=this.webserviceConfigurationService.getWebServiceConfig("PAYMENT_APPLICATION");
		
		ComAccBean cb= new ComAccBean();
		cb=this.getAcc_nums(accnum, logname);//团体信息
		List<TeamAccBeanHK> tblist= new ArrayList<TeamAccBeanHK>();
		tblist= this.getAccnumList(this.accnum, logname);
		
		rh = Pro_send_sick_clinic_detail(wcf,cb,tblist,logname);
		 TranLogTxt.liswriteEror_to_txt(logname, "His团体结算申请返回:" +"流水号:"+ rh.getSourceMsgId()+"成功失败标识:"+rh.getTypeCode()+"描述:"+rh.getText()+"\r\n");
	 	// TranLogTxt.liswriteEror_to_txt(logname, "His收费申请入参:" +"项目名称:"+ fee.getITEM_NAME()+"金额:"+fee.getCHARGES()+"批次:"+ eu.getBatch_id()+"单位名称  :"+eu.getCompany()+"\r\n");
		
		if(rh.getTypeCode().equals("0")){
			String his_sql ="insert into zl_req_term_his_item values('"+cb.getBatch_num()+"','"+cb.getAcc_num()+"','"+rh.getSourceMsgId()+"','"+DateTimeUtil.getDateTime()+"','"+rh.getTypeCode()+"','"+cb.getCom_name()+"')";
			try {
				TranLogTxt.liswriteEror_to_txt(logname, "团体结算成功插入zl_req_term_his_item表"+his_sql);
				connection = jdbcQueryManager.getConnection();
				connection.createStatement().executeUpdate(his_sql);
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}finally {
				try {
					if(connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
				}
			}
			rh.setTypeCode("AA");
			rh.setText("his成功");
			
		}else{
			rh.setTypeCode("AE");
			rh.setText("his结账失败");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "团体结算成功返回给体检系统"+"成功失败标识"+rh.getTypeCode()+"流水号:"+rh.getSourceMsgId());
		return rh;
	}
	
	
	private ResultHeader Pro_send_sick_clinic_detail(WebserviceConfigurationDTO wcf, ComAccBean cb, List<TeamAccBeanHK> tblist, String logname) {
		
		
		ResultHeader rh = new ResultHeader();
		Connection conn = null;
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet result = null;// 
		
	    String url = wcf.getConfig_url();//oracle数据库url
		String[] split = wcf.getConfig_value().split(",");
		String user = split[0];// 获取用户名
		String password = split[1];//获取密码
		
		
		
		try {
			conn = OracleDatabaseSource.getConnection(url, user, password);
			
			/*CallableStatement call = conn.prepareCall("{call zhiydba.zhi4_peis_wjw.Pro_send_fee_team(?,?,?,?,?,?,?,?,?,?)}");//执行存储过程
			 call.setString(1, "");//--诊疗卡号(没有诊疗卡号，传入空值)
			 call.setString(2, cb.getCom_name());//-客户姓名
			 call.setString(3, "6031393");//--体检项目编码  	  祁阳his固定写死的
			 call.setString(4, "体检费");//--体检项目名称     祁阳his固定写死的
			 call.setString(5, cb.getAmount2());//--总金额
			 call.setString(6, cb.getCom_name());//--单位名称
			 call.setString(7, cb.getBatch_num());//--批次
			 
			 TranLogTxt.liswriteEror_to_txt(logname, "His团体结算入参:" +"诊疗号码:"+ ""+"客户姓名:"+cb.getCom_name()+"项目编码:"+""+"\r\n");
		 	 TranLogTxt.liswriteEror_to_txt(logname, "His团体结算入参:" +"项目名称:"+ ""+"金额:"+ cb.getAmount2()+"单位名称:"+ cb.getCom_name()+"批次  :"+cb.getBatch_num()+"\r\n");
			*/
			CallableStatement call = conn.prepareCall("{call zhiydba.zhi4_peis_wjw.Pro_send_fee_team(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");//执行存储过程
			System.err.println(cb.getCom_name()+"=单位==");
			System.err.println(cb.getAmount2()+"=金额==");
			System.err.println(cb.getBatch_num()+"=批次==");
			 call.setString(1, "");//--诊疗卡号(没有诊疗卡号，传入空值)
			 call.setString(2, cb.getCom_name());//--团检单位的单位名称
			 call.setString(3, "6031393");//--体检项目编码  祁阳his固定写死的
			 call.setString(4, "体检费");//--体检项目名称     祁阳his固定写死的
			 call.setString(5, cb.getAmount2());//--总金额
			 call.setString(6, "1");//--单位名称  祁阳固定传数量1  不知道啥意思
			 call.setString(7, cb.getBatch_num());//--批次
			 call.setString(8, "");// -- 团检中个人姓名(没有不填)
			 call.setString(9, "");//-- 团检中个人电话号码(没有不填)
			 call.setString(10, "");// -- 团检中个人性别(没有不填)
			 call.setString(11, "");// -- 团检中个人出生年月(格式yyyy-mm-dd) 
			 
			 
			 System.err.println("=====流水号====");
			 call.registerOutParameter(8, java.sql.Types.LONGVARCHAR);//--his记账流水号
			
			 call.registerOutParameter(9, java.sql.Types.FLOAT);// --错误编码(0成功，-1失败)
			
			 call.registerOutParameter(10,java.sql.Types.LONGVARCHAR);// --错误信息
			
			 
			 call.execute();
			 
			 rh.setSourceMsgId(call.getString(8));
			 rh.setTypeCode(call.getString(9));
			 rh.setText(call.getString(10));
			 
			 TranLogTxt.liswriteEror_to_txt(logname, "团体结算调入存储的输出参数"+"流水号"+rh.getSourceMsgId()+"成功失败标识:"+rh.getTypeCode()+"描述:"+rh.getText());

			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(e));
			rh.setTypeCode("-1");
		}
		
		
		return rh;
	}

	public ComAccBean getAcc_nums(String account_num,String logname){
		Connection tjtmpconnect = null;
		ComAccBean accnums = new ComAccBean();
		List<String> list = new ArrayList<String>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select f.batch_num,f.com_name,b.acc_num,a.amount1,a.amount2 from charging_summary_group a "
					+ "left join (select c.id,c.batch_num,d.com_name from batch c,company_info d where c.company_id=d.id) f on f.id=a.batch_id"
					+ ",team_invoice_account b  "
					+ "where a.account_num=b.account_num and a.account_num='"+account_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				accnums.setAcc_num(rs1.getString("acc_num"));
				accnums.setBatch_num(rs1.getString("batch_num"));
				accnums.setCom_name(rs1.getString("com_name"));
				accnums.setAmount1(rs1.getString("amount1"));
				accnums.setAmount2(rs1.getString("amount2"));
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
	
	public List<TeamAccBeanHK> getAccnumList(String account_num,String logname){
		Connection tjtmpconnect = null;
		List<TeamAccBeanHK> tblist= new ArrayList<TeamAccBeanHK>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select distinct exam_num,acc_num from team_account_exam_list where acc_num in("
					+ "select distinct acc_num from team_invoice_account where account_num='"+account_num+"')";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				TeamAccBeanHK  tb= new TeamAccBeanHK();
				tb.setAcc_num(rs1.getString("acc_num"));
				tb.setExam_num(rs1.getString("exam_num"));
				tblist.add(tb);
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
		
		return tblist;
	}
	
	public List<TeamItemBeanHK> getAccItemList(TeamAccBeanHK tb){
		Connection tjtmpconnect = null;
		List<TeamItemBeanHK> tblist= new ArrayList<TeamItemBeanHK>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select ta.price,ta.acc_charge,ci.his_num,ci.item_name from team_account_item_list ta,"
					+ "charging_item ci where ta.acc_num='"+tb.getAcc_num()+"' and ta.exam_num='"+tb.getExam_num()+"' "
					+ "and ci.id=ta.charging_item_id";
			TranLogTxt.liswriteEror_to_txt("", "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				TeamItemBeanHK  tbi= new TeamItemBeanHK();
				tbi.setAcc_charge(rs1.getDouble("acc_charge"));
				tbi.setHis_num(rs1.getString("his_num"));
				tbi.setItem_name(rs1.getString("item_name"));
				tbi.setPrice(rs1.getDouble("price"));
				tblist.add(tbi);
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt("", "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		return tblist;
	}
	
	public ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		//TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
}
