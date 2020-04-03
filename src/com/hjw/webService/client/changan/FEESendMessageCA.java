package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.RowSet;

import org.hsqldb.lib.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqHisItemDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

public class FEESendMessageCA {

	private FeeMessage feeMessage;
	private static JdbcQueryManager jqm;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jqm = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public FEESendMessageCA(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}

	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		Connection his_connect = null;
		try {
			String exam_num = feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM();
			TranLogTxt.liswriteEror_to_txt(logname,"exam_num:"+exam_num);
			ExamInfoUserDTO ei = configService.getExamInfoForNum(exam_num); 
			
			String his_dburl = url.split("&")[0];
			String his_user = url.split("&")[1];
			String his_passwd = url.split("&")[2];
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + url);
			his_connect = OracleDatabaseSource.getConnection(his_dburl, his_user, his_passwd);
			TranLogTxt.liswriteEror_to_txt(logname,"连接his数据库成功...");
//			his_connect.setAutoCommit(false);
			
			TranLogTxt.liswriteEror_to_txt(logname,"PROJECT().size:"+this.feeMessage.getPROJECTS().getPROJECT().size());
			String CHARGE_TYPE = "自费";
			double discount = this.feeMessage.getPROJECTS().getPROJECT().get(0).getDiscount();
			if(Math.abs(discount - 5.0) < 0.001) {
				CHARGE_TYPE = "TJ5.0";
			} else if(Math.abs(discount - 6.0) < 0.001) {
				CHARGE_TYPE = "TJ6.0";
			} else if(Math.abs(discount - 6.8) < 0.001) {
				CHARGE_TYPE = "TJ6.8";
			} else if(Math.abs(discount - 7.0) < 0.001) {
				CHARGE_TYPE = "TJ7.0";
			} else if(Math.abs(discount - 7.5) < 0.001) {
				CHARGE_TYPE = "TJ7.5";
			} else if(Math.abs(discount - 8.0) < 0.001) {
				CHARGE_TYPE = "TJ8.0";
			} else if(Math.abs(discount - 8.5) < 0.001) {
				CHARGE_TYPE = "TJ8.5";
			} else if(Math.abs(discount - 9.0) < 0.001) {
				CHARGE_TYPE = "TJ9.0";
			} else if(Math.abs(discount - 9.5) < 0.001) {
				CHARGE_TYPE = "TJ9.5";
			} else {
				CHARGE_TYPE = "自费";
			}
			
			if("G".equals(ei.getExam_type()) && !StringUtil.isEmpty(ei.getCompany()) && ei.getCompany().length()>3) {
				ei.setUser_name(ei.getCompany());
			}
			
			//0,获取 VISIT_DATE VISIT_NO SERIAL_NO
			String VISIT_DATE = "";
			String VISIT_NO = "";
			String SERIAL_NO = "";
			String sql = "select to_char(sysdate, 'yyyy-mm-dd') as VISIT_DATE, VISIT_NO.nextval as VISIT_NO, OUTP_ORDER_SERIAL_NO.NEXTVAL as SERIAL_NO FROM DUAL";
			TranLogTxt.liswriteEror_to_txt(logname, "获取 VISIT_DATE VISIT_NO SERIAL_NO："+sql);
			Statement statement = his_connect.createStatement();
			ResultSet rs0 = statement.executeQuery(sql);
			if(rs0.next()) {
				VISIT_DATE = rs0.getString("VISIT_DATE");
				VISIT_NO = rs0.getString("VISIT_NO");
				SERIAL_NO = rs0.getString("SERIAL_NO");
			}
			rs0.close();
			statement.close();
			TranLogTxt.liswriteEror_to_txt(logname, "从数据库获取到：VISIT_DATE="+VISIT_DATE+",VISIT_NO="+VISIT_NO+",SERIAL_NO="+SERIAL_NO);
			
			//2,写入：3.10就诊记录  CLINIC_MASTER
			String insert_CLINIC_MASTER_sql = "insert into CLINIC_MASTER (REGIST_FEE, CLINIC_FEE, OTHER_FEE, CLINIC_CHARGE, OPERATOR, "
					+ "MR_PROVIDE_INDICATOR, REGISTRATION_STATUS, REGISTERING_DATE,NAME, SEX,"
					+ " VISIT_DATE, VISIT_NO, PATIENT_ID, CHARGE_TYPE, "
					+ "VISIT_DEPT, DOCTOR, CLINIC_LABEL) values "
					+ "(0, 0, 0, 0, '体检科', "
					+ "0, 1, to_date('"+VISIT_DATE+"', 'yyyy-mm-dd'),'"+ei.getUser_name()+"','"+ei.getSex()+"',"
					+ "to_date('"+VISIT_DATE+"', 'yyyy-mm-dd'),'"+VISIT_NO+"','"+ei.getExam_num()+"','"+CHARGE_TYPE+"',"
					+ "'400701',null,'体检科（地方）')";
			TranLogTxt.liswriteEror_to_txt(logname, "2,写入：3.10就诊记录  CLINIC_MASTER: " +insert_CLINIC_MASTER_sql);
			statement = his_connect.createStatement();
			statement.executeUpdate(insert_CLINIC_MASTER_sql);
			statement.close();
			
			//3,写入：3.11门诊病历记录OUTP_MR
			String insert_OUTP_MR_sql = "insert into OUTP_MR (VISIT_DATE, VISIT_NO, PATIENT_ID, BODY_EXAM, DOCTOR) values("
					+ "to_date('"+VISIT_DATE+"', 'yyyy-mm-dd'),'"+VISIT_NO+"','"+exam_num+"','体检',null)";
			TranLogTxt.liswriteEror_to_txt(logname, "写入：3.11门诊病历记录OUTP_MR: " +insert_OUTP_MR_sql);
			statement = his_connect.createStatement();
			statement.executeUpdate(insert_OUTP_MR_sql);
			statement.close();
			
			//4,写入：3.12门诊医嘱主记录OUTP_ORDERS
			String insert_OUTP_ORDERS_sql = "insert into OUTP_ORDERS (VISIT_DATE, VISIT_NO, PATIENT_ID, SERIAL_NO, ORDERED_BY, DOCTOR, Doctor_no) values("
					+ "to_date('"+VISIT_DATE+"', 'yyyy-mm-dd'),'"+VISIT_NO+"', '"+exam_num+"','"+SERIAL_NO+"','200601','郭宏林', 'GUOHLYWB')";
			TranLogTxt.liswriteEror_to_txt(logname, "4,写入：3.12门诊医嘱主记录OUTP_ORDERS: " +insert_OUTP_ORDERS_sql);
			statement = his_connect.createStatement();
			statement.executeUpdate(insert_OUTP_ORDERS_sql);
			statement.close();
			
			int m = 1;
			for(Fee fee : this.feeMessage.getPROJECTS().getPROJECT()) {
				if(!"1".equals(fee.getCHARGE_INDICATOR())) {//不是团检项目才写入此中间表，此中间表用于撤销申请和收费状态回传
					ZlReqHisItemDTO zrhi = new ZlReqHisItemDTO();
					zrhi.setExam_num(exam_num);
					zrhi.setReq_no(feeMessage.getREQ_NO());
					zrhi.setCharging_item_code(fee.getExam_chargeItem_code());
					zrhi.setPrice_list_code(fee.getITEM_CODE());
					zrhi.setHis_num(m+"");
					zrhi.setHis_req_no(SERIAL_NO);
					zrhi.setTmp1(VISIT_NO);
					zrhi.setTmp2(VISIT_DATE);
					insert_zl_req_his_item(zrhi, logname);
				}
				
				String select_pacs_req_code_sql = "select p.pacs_req_code from pacs_summary p, pacs_detail d where p.id=d.summary_id and d.examinfo_num = '"+exam_num+"' and d.chargingItem_num = '"+fee.getExam_chargeItem_code()+"'";
				TranLogTxt.liswriteEror_to_txt(logname, "根据收费项目code查询pacs申请单号 " +select_pacs_req_code_sql);
				
				RowSet rs = jqm.getRowSet(select_pacs_req_code_sql);
				String pacs_req_code = "";
				if(rs.next()) {
					pacs_req_code = rs.getString("pacs_req_code");
				}
				rs.close();
				
				sql = "select d.dep_inter_num from charging_item ci,department_dep d where ci.dep_id = d.id and ci.item_code = '"+fee.getExam_chargeItem_code()+"'";	
				TranLogTxt.liswriteEror_to_txt(logname,"查-dep_inter_num-sql:"+sql);
				try {
					rs = this.jqm.getRowSet(sql);
					if(rs.next()) {
						fee.setPERFORMED_BY(rs.getString("dep_inter_num"));
					}
					rs.close();
				} catch (SQLException ex) {
					TranLogTxt.liswriteEror_to_txt(logname,"Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
				
				if(StringUtil.isEmpty(fee.getPERFORMED_BY())) {
					fee.setPERFORMED_BY("400701");
				}
				//1,写入：3.13检查治疗医嘱明细记录OUTP_TREAT_REC
				String insert_OUTP_TREAT_RE_sql = "insert into OUTP_TREAT_REC (VISIT_DATE, VISIT_NO, SERIAL_NO, ITEM_NO, ITEM_CLASS,"
						+ " ITEM_CODE, ITEM_NAME, ITEM_SPEC, UNITS, AMOUNT, "
						+ " PERFORMED_BY, COSTS, CHARGES, CHARGE_INDICATOR, APPOINT_NO) values "
						+ "(to_date('"+VISIT_DATE+"', 'yyyy-mm-dd'), '"+VISIT_NO+"', '"+SERIAL_NO+"', '"+m+"','"+fee.getITEM_CLASS()+"',"
						+ " '"+fee.getITEM_CODE()+"','"+fee.getITEM_NAME()+"','"+fee.getITEM_SPEC()+"','"+fee.getUNITS()+"','"+fee.getAMOUNT()+"',"
						+ " '"+fee.getPERFORMED_BY()+"',"+fee.getCOSTS()+","+fee.getCHARGES()+",'"+fee.getCHARGE_INDICATOR()+"','"+pacs_req_code+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "1,写入：3.13检查治疗医嘱明细记录OUTP_TREAT_REC: " +insert_OUTP_TREAT_RE_sql);
				statement = his_connect.createStatement();
				statement.executeUpdate(insert_OUTP_TREAT_RE_sql);
				statement.close();
				
				//2,写入：3.14门诊医生收费明细OUTP_ORDERS_COSTS
				String insert_OUTP_ORDERS_COSTS_sql = "INSERT INTO OUTP_ORDERS_COSTS (VISIT_DATE, VISIT_NO, PATIENT_ID, SERIAL_NO, ORDER_CLASS,"
						+ " ORDER_NO, ORDER_SUB_NO, ITEM_NO, ITEM_CLASS, ITEM_CODE, "
						+ " ITEM_NAME, ITEM_SPEC, AMOUNT, UNITS, ORDERED_BY_DEPT, "
						+ " ORDERED_BY_DOCTOR, PERFORMED_BY, COSTS, CHARGES, CHARGE_INDICATOR ) values "
						+ " (to_date('"+VISIT_DATE+"', 'yyyy-mm-dd'),'"+VISIT_NO+"','"+fee.getEXAM_NUM()+"','"+SERIAL_NO+"','"+fee.getITEM_CLASS()+"',"
						+ " '"+m+"','1','"+m+"','"+fee.getITEM_CLASS()+"','"+fee.getITEM_CODE()+"',"
						+ " '"+fee.getITEM_NAME()+"','"+fee.getITEM_SPEC()+"','"+fee.getAMOUNT()+"','"+fee.getUNITS()+"','400701',"
						+ " '"+fee.getORDERED_BY_DOCTOR()+"','"+fee.getPERFORMED_BY()+"','"+fee.getCOSTS()+"','"+fee.getCHARGES()+"','"+fee.getCHARGE_INDICATOR()+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "2,写入：3.14门诊医生收费明细OUTP_ORDERS_COSTS: " +insert_OUTP_ORDERS_COSTS_sql);
				statement = his_connect.createStatement();
				statement.executeUpdate(insert_OUTP_ORDERS_COSTS_sql);
				statement.close();
				
				m++;
			}
//			his_connect.commit();
			ReqId req= new ReqId();
			req.setReq_id(feeMessage.getREQ_NO());
			rb.getControlActProcess().getList().add(req);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("发送收费申请成功");
		} catch (Throwable ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + feeMessage.getREQ_NO()+ "向his库写入数据错误：" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			try{
//				his_connect.rollback();
			}catch(Exception et){}
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("向his库写入数据错误：" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (his_connect != null) {
					his_connect.close();
				}
			} catch (Exception e) {}
		}
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		return rb;
	}
	
	public int insert_zl_req_his_item(ZlReqHisItemDTO zrhi,String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jqm.getConnection();
//			String sb1 = "delete from zl_req_his_item where exam_info_id='"+zri.getExam_info_id()
//					+"'  and charging_item_id='"+zri.getCharging_item_id()+"' and lis_req_code='"+zri.getLis_req_code()+"'";
//			TranLogTxt.liswriteEror_to_txt(logname, "delete-zl_req_item-sql:" +sb1);
//			tjtmpconnect.createStatement().execute(sb1);
			
			String insertsql = "insert into [zl_req_his_item] ([exam_num],[req_no],[charging_item_code],[price_list_code],[his_num],[his_req_no],"
					+ "[createdate],[tmp1],[tmp2]) values ("
					+ " '"+zrhi.getExam_num()+"', '"+zrhi.getReq_no()+"', '"+zrhi.getCharging_item_code()+"','"+zrhi.getPrice_list_code()+"','"+zrhi.getHis_num()+"', '"+zrhi.getHis_req_no()+"', "
					+ "getdate(), '"+zrhi.getTmp1()+"', '"+zrhi.getTmp2()+"')";
			TranLogTxt.liswriteEror_to_txt(logname, "insert-zl_req_his_item-sql:" +insertsql);
			preparedStatement = tjtmpconnect.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.executeUpdate();
			ResultSet rs = null;
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next())
				lisid = rs.getInt(1);
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "zl_req_his_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisid;
	}
}
