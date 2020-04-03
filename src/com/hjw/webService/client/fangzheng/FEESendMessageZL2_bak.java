package com.hjw.webService.client.fangzheng;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 2.14 挂号信息服务 天健 平台对接-东北国际
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class FEESendMessageZL2_bak {

	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public FEESendMessageZL2_bak(FeeMessage feeMessage) {
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
			String table = url.split("&")[3];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + url);
			String cfh=this.getCFH(url, logname);
			List<String> itemCodeList = new ArrayList<String>();
			ReqId rqid = new ReqId();
			rqid.setReq_id(feeMessage.getREQ_NO());
			rqid.setThird_req_id(cfh);
			if((cfh!=null)&&(cfh.trim().length()>0)){
				
			List<FeeRes> feeitem = new ArrayList<FeeRes>();
			String PATIENT_ID = "";
			String username="";
			try {
				
				for (Fee fee : this.feeMessage.getPROJECTS().getPROJECT()) {
					ExamInfoUserDTO ei= new ExamInfoUserDTO();
					ei=this.getExamInfoForNum(fee.getEXAM_NUM());
					PATIENT_ID=fee.getPATIENT_ID();
					username=fee.getUSER_NAME();
					/*String sql2 = "select CAST(a.personal_pay as decimal(18,2)) as personal_pay,a.itemnum,CAST(a.item_amount as decimal(18,2)) as amount,b.his_num as item_name,b.id,b.item_code,b.dep_category,c.id as examinfo_id "
							+ " from examinfo_charging_item a,charging_item b,exam_info c where a.charge_item_id=b.id"
							+ " and c.id=a.examinfo_id and c.exam_num='" + fee.getEXAM_NUM() + "'  and b.item_code='"
							+ fee.getExam_chargeItem_code() + "'  and a.pay_status<>'M' and a.isActive='Y' ";
					TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql2);
					List<ExaminfoChargingItemDTO> eciList = this.jdbcQueryManager.getList(sql2,
							ExaminfoChargingItemDTO.class);
					if (eciList.size() > 0) {*/
						//ExaminfoChargingItemDTO eci = eciList.get(0);
						//long exam_id = eci.getExaminfo_id();
						//long chargingid = eci.getId();
						//String chargingcode = fee.getExam_chargeItem_code();
						//String his_num = eci.getItem_name();
						//String exam_num = fee.getEXAM_NUM();
						String req_no = feeMessage.getREQ_NO();
						String sqNo = fee.getITEM_NO();
						//int itemnum = eci.getItemnum();
						//double prices = eci.getAmount();
						//double amount = prices * itemnum;
						//double amt = eci.getPersonal_pay();
						String doctor = fee.getORDERED_BY_DOCTOR();
						//String req_id = "";
						

						TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_保存费用记录_Insert");
						CallableStatement c = connect.prepareCall("{call "+table+".zl_hjwtj_保存费用记录_Insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
						TranLogTxt.liswriteEror_to_txt(logname,fee.getEXAM_NUM() + "-zl_hjwtj_保存费用记录_Insert('"+cfh+"','" + sqNo + "','" 
						+ fee.getPATIENT_ID() + "','" + fee.getCLINIC_NO() + "','" 
								+ ei.getUser_name() + "','" + ei.getSex() + "','" + ei.getAge() + "','普通','"
										+fee.getORDERED_BY_DEPT()+"','"+fee.getORDERED_BY_DEPT()
										+"','"+fee.getORDERED_BY_DOCTOR()+"','"+fee.getITEM_CODE()+"','体检中心','',0,'" + fee.getAMOUNT() + "','"+fee.getPERFORMED_BY()+"','" + fee.getITEM_PRICE() + "','"
										+ fee.getCOSTS() + "','" + fee.getCHARGES() + "'" + ",'" +new java.sql.Date(System.currentTimeMillis())+ "','" 
										+ new java.sql.Date(System.currentTimeMillis()) + "','',0,'',0,'','"
										+ fee.getUSER_NAME() + "','"+doctor+"',?)");
						c.setString(1, cfh);
						c.setFloat(2, Float.valueOf(sqNo));
						c.setFloat(3, Float.valueOf(fee.getPATIENT_ID()));
						c.setFloat(4, Float.valueOf(fee.getCLINIC_NO()));
						c.setString(5, ei.getUser_name());
						c.setString(6, ei.getSex());
						c.setString(7, ei.getAge()+"");
						c.setString(8, "普通");
						c.setFloat(9,Float.valueOf(fee.getORDERED_BY_DEPT()));
						c.setFloat(10,Float.valueOf(fee.getORDERED_BY_DEPT()));
						c.setString(11, fee.getORDERED_BY_DOCTOR());
						c.setFloat(12, Float.valueOf(fee.getITEM_CODE()));
						c.setString(13,"体检中心");
						c.setString(14, "");
						c.setFloat(15,0);
						c.setFloat(16, Float.valueOf(fee.getAMOUNT()));
						c.setFloat(17, Float.valueOf( fee.getPERFORMED_BY()));
						c.setFloat(18, Float.valueOf(fee.getITEM_PRICE()));
						c.setFloat(19, Float.valueOf(fee.getCOSTS()));
						c.setFloat(20, Float.valueOf(fee.getCHARGES()));
						c.setDate(21, new java.sql.Date(System.currentTimeMillis()));
						c.setDate(22, new java.sql.Date(System.currentTimeMillis()));
						c.setString(23,"");
						c.setFloat(24,0);
						
						c.setString(25,"");
						c.setFloat(26,0);
						c.setString(27,"");	
						c.setString(28, fee.getUSER_NAME());
						c.setString(29, doctor);
						// 执行存储过程
						c.registerOutParameter(30, java.sql.Types.FLOAT);
						// 执行存储过程
						c.execute();
						float reqs = c.getInt(24);
						c.close();
						TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_保存费用记录_Insert  返回："+reqs);
						if(reqs==0){
							
						
						itemCodeList.add(req_no);
						FeeRes f = new FeeRes();
						f.setCHARGES(fee.getCHARGES());
						//f.setExam_chargeItem_code(chargingcode);
						f.setEXAM_NUM(fee.getEXAM_NUM());
						f.setFEE_CODE(fee.getITEM_CODE());
						f.setPATIENT_ID(fee.getPATIENT_ID());
						feeitem.add(f);
						}
					}
				//}
				//成功
				TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_确认费用");
				CallableStatement c = connect.prepareCall("{call "+table+".zl_hjwtj_确认费用(?,?,?,?,?,)}");
				TranLogTxt.liswriteEror_to_txt(logname,"zl_hjwtj_确认费用('"+cfh+"','" + PATIENT_ID + "','"
								+ username + "','"+DateTimeUtil.getDateTime()+"',?)");
				c.setString(1, cfh);							
				c.setLong(2, Long.valueOf(PATIENT_ID));
				c.setString(3, username);
				c.setString(4, DateTimeUtil.getDateTime());							
				// 执行存储过程
				c.registerOutParameter(5, java.sql.Types.INTEGER);
				// 执行存储过程
				c.execute();
				int reqqrs = c.getInt(5);
				TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_确认费用  返回："+reqqrs);
				c.close();				
			} catch (Exception ex) {
				
					TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_删除费用");
					CallableStatement c = connect.prepareCall("{call "+table+".zl_hjwtj_删除费用(?,?,?)}");
					TranLogTxt.liswriteEror_to_txt(logname,
							"res:-zl_hjwtj_删除费用('" + cfh + "','" + PATIENT_ID + "')");
					c.setString(1, cfh);
					c.setLong(2, Long.valueOf(PATIENT_ID));
					c.registerOutParameter(3, java.sql.Types.INTEGER);
					// 执行存储过程
					c.execute();
					int reqqrs =c.getInt(3);
					c.close();
					TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程   zl_hjwtj_确认费用取消  返回："+reqqrs);
					
					c = connect.prepareCall("{call "+table+".zl_hjwtj_确认费用取消(?,?,?,?,?,)}");
					TranLogTxt.liswriteEror_to_txt(logname,"zl_hjwtj_确认费用取消('"+cfh+"','" + PATIENT_ID + "','"
									+ username + "','"+DateTimeUtil.getDateTime()+"',?)");
					c.setString(1, cfh);							
					c.setLong(2, Long.valueOf(PATIENT_ID));
					c.setString(3, username);
					c.setDate(4, new java.sql.Date(System.currentTimeMillis()));							
					// 执行存储过程
					c.registerOutParameter(5, java.sql.Types.INTEGER);
					// 执行存储过程
					c.execute();
					reqqrs = c.getInt(5);
					TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程 zl_hjwtj_确认费用取消  返回："+reqqrs);
					c.close();				
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("费用信息解析返回值错误");
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("");
			rqid.setFeeitem(feeitem);
			rb.getControlActProcess().getList().add(rqid);
			rb.setItemCodeList(itemCodeList);
			}
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
	
	/**
	 * 获取处方号
	 * @param url
	 * @param logname
	 * @return
	 */
	public String getCFH(String url, String logname) {
		String cfh = "";
		Connection connect = null;
		Statement statement = null;  
		  
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			String table = url.split("&")[3];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用函数  "+table+".zl_hjwtj_get获取处方号");
			 statement = connect.createStatement();  
			CallableStatement c = connect.prepareCall("{?=call "+table+".zl_hjwtj_get获取处方号()}");
			TranLogTxt.liswriteEror_to_txt(logname, ""+table+".zl_hjwtj_get获取处方号()");
			c.registerOutParameter(1, oracle.jdbc.OracleTypes.VARCHAR);
			// 执行存储过程
			c.execute();			
            cfh=c.getString(1);
			c.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}finally{
			try {
			if (connect != null) {
				connect.close();
			}
		} catch (SQLException sqle4) {
			sqle4.printStackTrace();
		}
		}
		return cfh;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times,a.arch_num ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
}
