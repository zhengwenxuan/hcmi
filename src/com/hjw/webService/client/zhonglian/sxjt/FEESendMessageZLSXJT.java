package com.hjw.webService.client.zhonglian.sxjt;

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
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.FeeRes;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
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
public class FEESendMessageZLSXJT {

	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public FEESendMessageZLSXJT(FeeMessage feeMessage) {
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

					String sql2 = "select CAST(a.personal_pay as decimal(18,2)) as personal_pay,a.itemnum,CAST(a.item_amount as decimal(18,2)) as amount,b.his_num,b.id,b.item_code,b.dep_category,c.id as examinfo_id "
							+ " from examinfo_charging_item a,charging_item b,exam_info c where a.charge_item_id=b.id"
							+ " and c.id=a.examinfo_id and c.exam_num='" + fee.getEXAM_NUM() + "'  and b.item_code='"
							+ fee.getExam_chargeItem_code() + "'  and a.pay_status<>'M' and a.isActive='Y' ";
					TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql2);
					List<ExaminfoChargingItemDTO> eciList = this.jdbcQueryManager.getList(sql2,
							ExaminfoChargingItemDTO.class);
					if (eciList.size() > 0) {
						ExaminfoChargingItemDTO eci = eciList.get(0);
						String zllb = "D";// 诊疗类别_In
						if ("21".equals(eci.getDep_category())) {// pacs
							zllb = "D";
						} else if ("131".equals(eci.getDep_category())) {// lis
							zllb = "C";
						}
						
						TranLogTxt.liswriteEror_to_txt(logname,
								"req:" + feeMessage.getREQ_NO() + ":1、调用存储过程  Zl_NewRequest");
						CallableStatement c = connect
								.prepareCall("{call Zl_NewRequest(?,?,?,?,?,?,?,?,?,?,?,?)}");
						TranLogTxt.liswriteEror_to_txt(logname, "req:Zl_NewRequest('"+zllb+"','"+fee.getPATIENT_ID()+"','"
								+fee.getEXAM_NUM()+"',"+eci.getHis_num()+",'','"+""+"','"
										+""+"','"+fee.getORDERED_BY_DEPT()+"','"+fee.getORDERED_BY_DOCTOR()+"'"
												+ ",?,?,?)");
						c.setString(1, zllb);
						c.setString(2, fee.getPATIENT_ID());
						//c.setString(3, zlreqpatinfo.getZl_tjh());
						c.setString(3, fee.getEXAM_NUM());
						c.setString(4, eci.getHis_num());// 检验项目ID_in
						c.setString(5, "");// 采集方式ID_in
						c.setString(6, "");// 标本部位_in
						c.setString(7, fee.getORDERED_BY_DEPT());
						c.setString(8, fee.getORDERED_BY_DEPT());
						c.setString(9, fee.getORDERED_BY_DOCTOR());
						c.registerOutParameter(10, java.sql.Types.VARCHAR);
						c.registerOutParameter(11, java.sql.Types.VARCHAR);
						c.registerOutParameter(12, java.sql.Types.VARCHAR);
						// 执行存储过程
						c.execute();
						// 得到存储过程的输出参数值
						String RESULT_CODE = c.getString(10);
						String ERROR_MSG = c.getString(11);
						String req_no = c.getString(12);
						c.close();
						TranLogTxt.liswriteEror_to_txt(logname,
								"res:Zl_NewRequest-" + feeMessage.getREQ_NO() + ":" + RESULT_CODE
										+ "-----" + ERROR_MSG + "-----" + req_no);
						
						if ("1".equals(RESULT_CODE)) {
							try {
								TranLogTxt.liswriteEror_to_txt(logname,
										"req:" + feeMessage.getREQ_NO() + ":2、调用存储过程  zl_SendRequest");
								/*
								 * CREATE OR REPLACE PROCEDURE
								 * Zl_SendRequest ( 
								 * 诊疗类别_In 病人医嘱记录.诊疗类别%Type, 
								 * 申请单号_in  Varchar2, 
								 * RESULT_CODE Out
								 * Varchar2, ERROR_MSG Out Varchar2,
								 * strReturn Out Varchar2 )
								 * --功能:检验申请单号。 --参数：
								 * --返回："医嘱发送号|HIS单据号"。
								 */

								CallableStatement cs = connect
										.prepareCall("{call zl_SendRequest(?,?,?,?,?)}");
								TranLogTxt.liswriteEror_to_txt(logname, "res:zl_SendRequest('"+zllb+"','"+req_no+"',?,?,?)");
								cs.setString(1, zllb);
								cs.setString(2, req_no);
								cs.registerOutParameter(3, java.sql.Types.VARCHAR);
								cs.registerOutParameter(4, java.sql.Types.VARCHAR);
								cs.registerOutParameter(5, java.sql.Types.VARCHAR);
								// 执行存储过程
								cs.executeUpdate();
								// 得到存储过程的输出参数值
								String RESULT_CODES = cs.getString(3);
								String ERROR_MSGS = cs.getString(4);
								String strReturnS = cs.getString(5);
								TranLogTxt.liswriteEror_to_txt(logname,
										"res:zl_SendRequest-" + feeMessage.getREQ_NO() + ":"
												+ RESULT_CODES + "-----" + ERROR_MSGS + "-----"
												+ strReturnS);
								if ("1".equals(RESULT_CODES)) {
									ExamInfoUserDTO eu = configService.getExamInfoForNum(fee.getEXAM_NUM());
									TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_xb体检明细费用_insert");
									c = connect.prepareCall("{call zl_xb体检明细费用_insert(?,?,?,?,?)}");
									TranLogTxt.liswriteEror_to_txt(logname,
											"zl_xb体检明细费用_insert-zl_xb体检明细费用_insert('" + req_no
													+ "','" + eu.getUser_name() + "','" + "1" + "','" + eci.getDiscount() * 10 + "','" + DateTimeUtil.getDateTime() + "')");
									c.setString(1, req_no);
									c.setString(2, eu.getUser_name());
									c.setString(3, "1");
									c.setString(4, eci.getDiscount() * 10 + "");
									c.setString(5, DateTimeUtil.getDateTime());
									
									// 执行存储过程
									c.execute();
									c.close();
									itemCodeList.add(req_no);
								} else {
									TranLogTxt.liswriteEror_to_txt(logname, "res:"
											+ feeMessage.getREQ_NO() + ": 2、lis调用zl_SendRequest错误，函数返回：="+RESULT_CODES);
								}
							} catch (Exception ex) {
								TranLogTxt.liswriteEror_to_txt(logname, "res:" + feeMessage.getREQ_NO()
										+ ": 2、lis调用zl_SendRequest错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
							}
						} else {
							TranLogTxt.liswriteEror_to_txt(logname,
									"res:" + feeMessage.getREQ_NO() + ": 1、lis调用Zl_NewRequest错误");
						}
					}
				}
			} catch (Exception ex) {
//				for (Fee fee : this.feeMessage.getPROJECTS().getPROJECT()) {
//					String exam_num = fee.getEXAM_NUM();
//					String req_no = feeMessage.getREQ_NO();
//					TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_km体检费用明细_delete");
//					CallableStatement c = connect.prepareCall("{call zl_km体检费用明细_delete(?,?)}");
//					TranLogTxt.liswriteEror_to_txt(logname,
//							"res:-zl_km体检费用明细_delete('" + exam_num + "','" + req_no + "')");
//					c.setString(1, exam_num);
//					c.setString(2, req_no);
//					// 执行存储过程
//					c.execute();
//					c.close();
//				}
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
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + feeMessage.getREQ_NO() + ":" + xml);
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
