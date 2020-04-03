package com.hjw.webService.client.zhonglian;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.FeeRes;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 2.14 挂号信息服务 天健 平台对接-东北国际
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class FEESendMessageZL {

	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public FEESendMessageZL(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + url);
			List<String> itemCodeList = new ArrayList<String>();
			ReqId rqid = new ReqId();
			rqid.setReq_id(feeMessage.getREQ_NO());
			List<FeeRes> feeitem = new ArrayList<FeeRes>();
			try {
				for (Fee fee : this.feeMessage.getPROJECTS().getPROJECT()) {

					String sql2 = "select CAST(a.personal_pay as decimal(18,2)) as personal_pay,a.itemnum,CAST(a.item_amount as decimal(18,2)) as amount,b.his_num as item_name,b.id,b.item_code,b.dep_category,c.id as examinfo_id "
							+ " from examinfo_charging_item a,charging_item b,exam_info c where a.charge_item_id=b.id"
							+ " and c.id=a.examinfo_id and c.exam_num='" + fee.getEXAM_NUM() + "'  and b.his_num='"
							+ fee.getITEM_CODE() + "'  and a.pay_status<>'M' and a.isActive='Y' ";
					TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql2);
					List<ExaminfoChargingItemDTO> eciList = this.jdbcQueryManager.getList(sql2,
							ExaminfoChargingItemDTO.class);
					if (eciList.size() > 0) {
						ExaminfoChargingItemDTO eci = eciList.get(0);
						long exam_id = eci.getExaminfo_id();
						long chargingid = eci.getId();
						String chargingcode = fee.getExam_chargeItem_code();
						String his_num = eci.getItem_name();
						String exam_num = fee.getEXAM_NUM();
						String req_no = feeMessage.getREQ_NO();
						String sqNo = fee.getITEM_NO();
						int itemnum = eci.getItemnum();
						double prices = eci.getAmount();
						double amount = prices * itemnum;
						double amt = eci.getPersonal_pay();
						String doctor = fee.getORDERED_BY_DOCTOR();
						String req_id = "";
						if ("21".equals(eci.getDep_category())) {// pacs
							req_id = getzl_req_pacs_item_req_id(exam_id, chargingcode, req_no, logname);
						} else if ("131".equals(eci.getDep_category())) {// lis
							req_id = getzl_req_item_req_id(exam_id, chargingid, req_no, logname);
						}

						TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_KM体检费用明细_Insert");
						CallableStatement c = connect.prepareCall("{call zl_KM体检费用明细_Insert(?,?,?,?,?,?,?,?,?,?,?)}");
						TranLogTxt.liswriteEror_to_txt(logname,
								"zl_KM体检费用明细_Insert-" + exam_id + "-zl_KM体检费用明细_Insert('" + exam_num + "','" + req_no
										+ "','" + sqNo + "','" + his_num + "','" + itemnum + "','" + prices + "','"
										+ amount + "','" + amt + "'" + ",'" + DateTimeUtil.getDateTime() + "','"
										+ doctor + "','" + req_id + "')");
						c.setString(1, exam_num);
						c.setString(2, req_no);
						c.setString(3, sqNo);
						c.setString(4, his_num);
						c.setInt(5, itemnum);
						c.setDouble(6, prices);
						c.setDouble(7, amount);
						c.setDouble(8, amt);
						c.setDate(9, new java.sql.Date(System.currentTimeMillis()));
						c.setString(10, doctor);
						c.setString(11, req_id);
						// 执行存储过程
						c.execute();
						c.close();
						itemCodeList.add(req_no);
						FeeRes f = new FeeRes();
						f.setCHARGES(amt + "");
						f.setExam_chargeItem_code(chargingcode);
						f.setEXAM_NUM(exam_num);
						f.setFEE_CODE(his_num);
						f.setPATIENT_ID("");
						feeitem.add(f);
					}
				}
			} catch (Exception ex) {
				for (Fee fee : this.feeMessage.getPROJECTS().getPROJECT()) {
					String exam_num = fee.getEXAM_NUM();
					String req_no = feeMessage.getREQ_NO();
					TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_km体检费用明细_delete");
					CallableStatement c = connect.prepareCall("{call zl_km体检费用明细_delete(?,?)}");
					TranLogTxt.liswriteEror_to_txt(logname,
							"res:-zl_km体检费用明细_delete('" + exam_num + "','" + req_no + "')");
					c.setString(1, exam_num);
					c.setString(2, req_no);
					// 执行存储过程
					c.execute();
					c.close();
				}
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("费用信息解析返回值错误");
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("");
			rqid.setFeeitem(feeitem);
			rb.getControlActProcess().getList().add(rqid);
			rb.setItemCodeList(itemCodeList);
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息调用webservice错误");
		}finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
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
	public String getzl_req_item_req_id(long exam_info_id, long chargitem_id, String lis_req_no, String logname) {
		Connection tjtmpconnect = null;
		String req_id = "";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select req_id from zl_req_item where exam_info_id='" + exam_info_id + "' and charging_item_id='" + chargitem_id + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				req_id = rs1.getString("req_id");
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
		return req_id;
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
					+ "' and charging_item_ids='" + chargitem_code + "'";
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
