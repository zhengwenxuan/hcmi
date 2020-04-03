package com.hjw.webService.client.tianchang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeTermBean;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.huojianwa.bean.ComAccBean;
import com.hjw.wst.service.CompanyService;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Description: 发送团体缴费申请
 * @version V2.0.0.0
 */
public class FEETermSendMessageTC {
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

	public FEETermSendMessageTC(String personid,String accnum) {
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
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + personid + ":"+this.accnum+":" + xml);
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			if((personid!=null)&&(personid.trim().length()>30)){
				personid=personid.substring(0,30);
			}
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + url);
			ComAccBean accnums = new ComAccBean();
			accnums=getAcc_nums(accnum,logname);

			TranLogTxt.liswriteEror_to_txt(logname,"req:" + "准备插入病人信息，调用存储过程  Pkg_TcTjjk.p_Trade");
			CallableStatement c = connect.prepareCall("{call Pkg_TcTjjk.p_Trade(?,?,?,?)}");
			String patientinfo_param = accnums.getAcc_num()+"|"+accnums.getCom_name()+"||"
					+"|||||";
					//就诊卡号|姓名|性别|出生日期
					//|身份证号|联系电话|单位电话|单位名称|家庭地址;
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + " - Pkg_TcTjjk.p_Trade("
					+ "'p_PraseStr_Patientinfo',"//--插入病人信息
					+ "'"+patientinfo_param+"',"
					+"?,"//OUT--返回Code
					+"?)"//OUT--申请人人工号
					);
			c.setString(1,"p_PraseStr_Patientinfo");
			c.setString(2,patientinfo_param);
			c.registerOutParameter(3, java.sql.Types.INTEGER);
			c.registerOutParameter(4, java.sql.Types.VARCHAR);
			c.execute();
			c.close();
			TranLogTxt.liswriteEror_to_txt(logname,
					"res:" + accnum + ":插入病人信息，存储过程 Pkg_TcTjjk.p_Trade 执行结果————"
							+ "代码:"+c.getInt(3)+"信息:"+c.getString(4));
			
			String sql2 = "select f.charging_item_id,f.item_num,CAST(f.acc_charge as decimal(18,2)) as acc_charge,CAST(f.item_amount as decimal(18,2)) as item_amount,c.item_code,c.his_num from "
					+ "( select l.charging_item_id, eci.item_amount,"
					+ "sum(eci.itemnum) as item_num, sum(l.acc_charge) as acc_charge "
					+ "from team_account_item_list l, examinfo_charging_item eci, exam_info e "
					+ "where l.exam_num=e.exam_num and eci.examinfo_id=e.id and eci.charge_item_id=l.charging_item_id and eci.isActive='Y' "
					+ "and l.acc_num = '"+accnums.getAcc_num()+"' and l.acc_charge>0 "
					+ "group by l.charging_item_id,eci.item_amount ) f,charging_item c where  c.id=f.charging_item_id ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql2);
			List<FeeTermBean> eciList = this.jdbcQueryManager.getList(sql2, FeeTermBean.class);					
			double amt = 0.0; 
			for (FeeTermBean eci:eciList) {
				amt += eci.getAcc_charge();
			}
			String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
			String SQKS = companyService.getDatadis("SQKS").get(0).getRemark();
			
			TranLogTxt.liswriteEror_to_txt(logname,"req:" + ":1、调用存储过程  pkg_tctjjk.p_trade");
			String param = accnums.getAcc_num()+"|"+doctorid+"|"+"|"+SQKS+"|"+doctorid+"|"
					//就诊卡号|开单医生|开单日期|开单科室|执行人|
					+SQKS+"|0|01257|"+amt+"|1|"
			//执行科室|套餐医疗序号|收费项目|单价|数量|
					+"1|"+accnums.getAcc_num()+"|0|"+accnums.getAcc_num();
			//自付比例|体检编号|团体标志|体检开单收费编号
			c = connect
					.prepareCall("{call pkg_tctjjk.p_trade(?,?,?,?)}");
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + " - pkg_tctjjk.p_trade("
					+ "'p_Prasexml_Detail',"
					+ "'"+param+"',"
					+"?,"//OUT--返回Code
					+"?)"//OUT--报错信息
					);
			c.setString(1, "p_Prasexml_Detail");
			c.setString(2, param);
			c.registerOutParameter(3, java.sql.Types.INTEGER);
			c.registerOutParameter(4, java.sql.Types.VARCHAR);
			// 执行存储过程
			c.execute();
			c.close();
			
			TranLogTxt.liswriteEror_to_txt(logname,
					"res:" + accnum + ":存储过程 pkg_tctjjk.p_trade 执行结果————"+ "代码:"+c.getInt(3)+"信息:"+c.getString(4));
			if(c.getInt(3)>0) {
				rb.getResultHeader().setTypeCode("AA");
				ReqId req= new ReqId();
				req.setReq_id(accnum);
				rb.getControlActProcess().getList().add(req);
			} else {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(c.getString(4));
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

	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public ComAccBean getAcc_nums(String account_num,String logname){
		Connection tjtmpconnect = null;
		ComAccBean accnums = new ComAccBean();
		List<String> list = new ArrayList<String>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select f.batch_num,f.com_name,b.acc_num from charging_summary_group a "
					+ "left join (select c.id,c.batch_num,d.com_name from batch c,company_info d where c.company_id=d.id) f on f.id=a.batch_id"
					+ ",team_invoice_account b  "
					+ "where a.account_num=b.account_num and a.account_num='"+account_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				accnums.setAcc_num(rs1.getString("acc_num"));
				accnums.setBatch_num(rs1.getString("batch_num"));
				accnums.setCom_name(rs1.getString("com_name"));
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
