package com.hjw.webService.client.zhonglian;

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
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeTermBean;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeResultBody;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 发送团体缴费申请
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class FEETermSendMessageZL {
	private String accnum="";
	private String personid="";
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public FEETermSendMessageZL(String personid,String accnum) {
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
			String accNUms=getAcc_nums(accnum,logname);			
			TranLogTxt.liswriteEror_to_txt(logname,
					"req:" + personid + ":1、调用存储过程  Zl_NewPatient");
			CallableStatement c = connect.prepareCall("{call Zl_NewPatient(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			TranLogTxt.liswriteEror_to_txt(logname, "res:-Zl_NewPatient('"+personid+"','男','20','','','','','','',''"
									+ ",'"+accnum+"',?,?,?)");
			c.setString(1, personid);
			c.setString(2, "男");
			c.setString(3,"20");
			c.setString(4, "");
			c.setString(5, "");
			c.setString(6, "");
			c.setString(7, "");
			c.setString(8, "");
			c.setString(9, "");
			c.setString(10, "");
			c.setString(11, accnum);
			c.registerOutParameter(12, java.sql.Types.VARCHAR);
			c.registerOutParameter(13, java.sql.Types.VARCHAR);
			c.registerOutParameter(14, java.sql.Types.VARCHAR);
			
			// 执行存储过程
			c.execute();
			// 得到存储过程的输出参数值
			String RESULT_CODE = c.getString(12);
			String ERROR_MSG = c.getString(13);
			String strReturn = c.getString(14);
			c.close();
			System.out.println(RESULT_CODE);
			TranLogTxt.liswriteEror_to_txt(logname, "res:Zl_NewPatient-:" + RESULT_CODE + "-----" + ERROR_MSG + "-----" + strReturn);

			
			int sqNo =0;
				
					String sql2 = "select f.charging_item_id,f.item_num,CAST(f.acc_charge as decimal(18,2)) as acc_charge,CAST(f.item_amount as decimal(18,2)) as item_amount,c.item_code,c.his_num from "
							+ "( select l.charging_item_id, eci.item_amount,"
							+ "sum(eci.itemnum) as item_num, sum(l.acc_charge) as acc_charge "
							+ "from team_account_item_list l, examinfo_charging_item eci, exam_info e "
							+ "where l.exam_num=e.exam_num and eci.examinfo_id=e.id and eci.charge_item_id=l.charging_item_id and eci.isActive='Y' "
							+ "and l.acc_num in "+accNUms+" and l.acc_charge>0 "
							+ "group by l.charging_item_id,eci.item_amount ) f,charging_item c where  c.id=f.charging_item_id ";
					TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql2);
					List<FeeTermBean> eciList = this.jdbcQueryManager.getList(sql2,
							FeeTermBean.class);
					try {
					for (FeeTermBean eci:eciList) {
						sqNo=sqNo+1;
						//long chargingid = eci.getCharging_item_id();
						//String chargingcode = eci.getItem_code();
						String his_num = eci.getHis_num();
						String exam_num = accnum;
						String req_no = accnum;						
						int itemnum = eci.getItem_num();
						double prices = eci.getItem_amount();
						double amount = eci.getAcc_charge();
						double amt = eci.getAcc_charge();
						String doctor = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
						String req_id = "";					

						TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_KM体检费用明细_Insert");
						c = connect.prepareCall("{call zl_KM体检费用明细_Insert(?,?,?,?,?,?,?,?,?,?,?)}");
						TranLogTxt.liswriteEror_to_txt(logname,
								"zl_KM体检费用明细_Insert-" + accnum + "-zl_KM体检费用明细_Insert('" + exam_num + "','" + req_no
										+ "','" + sqNo + "'," + his_num + ",'" + itemnum + "','" + prices + "','"
										+ amount + "','" + amt + "'" + ",'" + DateTimeUtil.getDateTime() + "','"
										+ doctor + "','" + req_id + "')");
						c.setString(1, exam_num);
						c.setString(2, req_no);
						c.setString(3, sqNo+"");
						c.setString(4, his_num);
						c.setInt(5, itemnum);
						c.setDouble(6, prices);
						c.setDouble(7, amount);
						c.setDouble(8, amt);
						c.setDate(9,new java.sql.Date(System.currentTimeMillis()));
						c.setString(10, doctor);
						c.setString(11, req_id);
						// 执行存储过程
						c.execute();
					}
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("");
					ReqId r= new ReqId();
					r.setReq_id(accnum);
					List<ReqId> list=new ArrayList<ReqId>();
					list.add(r);
					rb.getControlActProcess().setList(list);;
				} catch (Exception ex) {
						String exam_num = accnum;
						String req_no = accnum;			
						TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_km体检费用明细_delete");
						c = connect.prepareCall("{call zl_km体检费用明细_delete(?,?)}");
						TranLogTxt.liswriteEror_to_txt(logname, "res:-zl_km体检费用明细_delete('" + exam_num
								+ "','" + req_no + "')");
						c.setString(1, exam_num);
						c.setString(2, req_no);
						// 执行存储过程
						c.execute();
						c.close();

					TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("费用信息解析返回值错误");
				}
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息调用webservice错误");
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
	public String getAcc_nums(String account_num,String logname){
		Connection tjtmpconnect = null;
		String accnums = "";
		List<String> list = new ArrayList<String>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select b.acc_num from charging_summary_group a,team_invoice_account b "
					+ "where a.account_num=b.account_num and a.account_num='"+account_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				list.add(rs1.getString("acc_num"));
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
		if((list!=null)&&(list.size()>0)){
			accnums =list.toString().replaceAll("\\[", "(");
			accnums =accnums.replaceAll("\\]", ")");
		}
		return accnums;
	}

	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_code
	 * @param lis_req_no
	 * @param req_id
	 * @param logname
	 * @return
	 * @throws Exception
	 */
	public String getzl_req_pacs_item_req_id(long exam_info_id, String chargitem_code, String lis_req_no,
			String logname) {
		Connection tjtmpconnect = null;
		String req_id = "";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select req_id from zl_req_pacs_item where exam_info_id='" + exam_info_id
					+ "' and pacs_req_code='" + lis_req_no + "' and charging_item_ids='" + chargitem_code + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				req_id = rs1.getString("req_id");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:zl_req_pacs_item 操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return req_id;
	}

}
