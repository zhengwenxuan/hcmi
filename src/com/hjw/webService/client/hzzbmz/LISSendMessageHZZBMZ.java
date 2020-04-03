package com.hjw.webService.client.hzzbmz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.Person;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.hzzbmz.client.Exception;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 湖州浙北明州医院 lis对接
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class LISSendMessageHZZBMZ {
	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public LISSendMessageHZZBMZ(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		ResultLisBody rb = new ResultLisBody();
		String exam_num = this.lismessage.getCustom().getExam_num();
		// long exam_id = 0;
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu = this.getExamInfoForNum(lismessage.getCustom().getExam_num());
			if ((eu == null) || (eu.getId() <= 0)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {
				// exam_id = eu.getId();
				Connection connect = null;
				try {
					String dburl = url.split("&")[0];
					String user = url.split("&")[1];
					String passwd = url.split("&")[2];
					connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + url);
					ControlActLisProcess ca = new ControlActLisProcess();
					List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
					System.out.println("体检编号：=" + lismessage.getCustom().getExam_num());
					for (LisComponents pcs : lismessage.getComponents()) {
						System.out.println("体检项目：=" + lismessage.getComponents().size());
						boolean pacsflags = true;
						try {
							connect.setAutoCommit(false);
							ApplyNOBean an = new ApplyNOBean();
							for (LisComponent pc : pcs.getItemList()) {
								try {
									pacsflags = inserthis_requisition(connect, lismessage.getCustom(),
											lismessage.getDoctor(), pc, eu.getArch_num(), pcs.getReq_no(), logname);
								} catch (Exception ex) {
									pacsflags = false;
									TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
											+ ": 1、lis调用Zl_NewRequest错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
									break;
								}
							}
							if (!pacsflags) {
								connect.rollback();
							} else {
								an.setApplyNO(pcs.getReq_no());
								list.add(an);
								connect.commit();
							}
						} catch (SQLException ex) {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("链接lis数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
							TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
						}
					}
					ca.setList(list);
					rb.setControlActProcess(ca);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("lis调用成功");
				} catch (java.lang.Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("链接lis数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				} finally {
					try {
						if (connect != null) {
							OracleDatabaseSource.close(connect);
						}
					} catch (java.lang.Exception sqle4) {
						sqle4.printStackTrace();
					}
				}
			}
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,b.arch_num,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times ");
		sb.append(" from exam_info c,customer_info b ");
		sb.append(" where c.customer_id=b.id and c.is_Active='Y' ");
		sb.append(" and c.exam_num = '" + exam_num + "' ");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}

	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public boolean inserthis_requisition(Connection conn, Person custom, Doctor doctor, LisComponent lc,
			String arch_num, String req_no, String logname) throws Exception {
		boolean flags = false;
		try {
			String his_id = req_no + "" + lc.getItemCode();
			String requisition_id = req_no;
			String sb1 = "select id,sqdzt from his_requisition2 where outpatient_id='"
					+ custom.getExam_num() + "' and requisition_id='" + requisition_id + "' and charge_item_id='"
					+ lc.getItemCode() + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":操作语句： " + sb1);
			ResultSet rs1 = conn.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				int sqdzt = rs1.getInt("sqdzt");
				if (sqdzt == 0) {
					TranLogTxt.liswriteEror_to_txt(logname, "res:发送申请ok！");
					flags = true;
				} else if (sqdzt < 2) {
					String updatesql = "update his_requisition2 set sqdzt=-1,charge_state='1' where "
							+ " requisition_id='" + requisition_id + "' and outpatient_id='" + custom.getExam_num()
							+ "' and charge_item_id='" + lc.getItemCode() + "'";
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":操作语句： " + updatesql);
					conn.createStatement().executeUpdate(updatesql);

					String slsql = "select SEQ_HIS_REQUISITION2_ID.nextval as id from dual";
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":操作语句： " + slsql);
					long xulie = 0;
					ResultSet xuliers1 = conn.createStatement().executeQuery(slsql);
					if (xuliers1.next()) {
						xulie = xuliers1.getLong("id");
					}
					xuliers1.close();
					if (xulie > 0) {
						String insertsql = "insert into his_requisition2(ID,his_id,requisition_id,patient_type,outpatient_id,inpatient_id,"
								+ "patient_name,patient_sex,patient_age,"
								+ "requisition_dept,requisition_time,requisition_person,charge_item_id,charge_name,charge,"
								+ "charge_state,requisition_state,charge_time,sqdzt) values(" + xulie + ",'" + his_id
								+ "','" + requisition_id + "','2','" + custom.getExam_num() + "','" + arch_num + "','"
								+ custom.getName() + "','" + custom.getSexcode() + "','" + custom.getOld() + "','"
								+ doctor.getDept_code() + "',to_date('" + DateTimeUtil.getDateTimes()
								+ "','yyyymmddhh24miss'),'" + doctor.getDoctorCode() + "','" + lc.getItemCode() + "','"
								+ lc.getItemName() + "'," + lc.getItemamount() + ",'0','0',to_date('"
								+ DateTimeUtil.getDateTimes() + "','yyyymmddhh24miss'),0)";
						TranLogTxt.liswriteEror_to_txt(logname,
								"res:" + lismessage.getMessageid() + ":操作语句： " + insertsql);
						conn.createStatement().executeUpdate(insertsql);
						// 执行以下操作，对方触发器处理过程。
						insertsql = "update his_requisition2 set charge_state='1' where id=" + xulie;
						TranLogTxt.liswriteEror_to_txt(logname,
								"res:" + lismessage.getMessageid() + ":操作语句： " + insertsql);
						conn.createStatement().executeUpdate(insertsql);
						TranLogTxt.liswriteEror_to_txt(logname, "res:发送申请ok！");
						flags = true;
					} else {
						flags = false;
					}

				} else {
					flags = false;
				}
			} else {
				String slsql = "select SEQ_HIS_REQUISITION2_ID.nextval as id from dual";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":操作语句： " + slsql);
				long xulie = 0;
				ResultSet xuliers1 = conn.createStatement().executeQuery(slsql);
				if (xuliers1.next()) {
					xulie = xuliers1.getLong("id");
				}
				xuliers1.close();
				if (xulie > 0) {
					String insertsql = "insert into his_requisition2(ID,his_id,requisition_id,patient_type,outpatient_id,inpatient_id,"
							+ "patient_name,patient_sex,patient_age,"
							+ "requisition_dept,requisition_time,requisition_person,charge_item_id,charge_name,charge,"
							+ "charge_state,requisition_state,sqdzt,charge_time) values("+xulie+",'"
							+ his_id + "','" + requisition_id + "','2','" + custom.getExam_num() + "','" + arch_num
							+ "','" + custom.getName() + "','" + custom.getSexcode() + "','" + custom.getOld() + "','"
							+ doctor.getDept_code() + "',to_date('" + DateTimeUtil.getDateTimes()
							+ "','yyyymmddhh24miss'),'" + doctor.getDoctorCode() + "','" + lc.getItemCode() + "','"
							+ lc.getItemName() + "'," + lc.getItemamount() + ",'0','0',0,to_date('"
								+ DateTimeUtil.getDateTimes() + "','yyyymmddhh24miss'))";
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":操作语句： " + insertsql);
					conn.createStatement().executeUpdate(insertsql);
					// 执行以下操作，对方触发器处理过程。
					insertsql = "update his_requisition2 set charge_state='1' where id=" + xulie;
					TranLogTxt.liswriteEror_to_txt(logname,
							"res:" + lismessage.getMessageid() + ":操作语句： " + insertsql);
					conn.createStatement().executeUpdate(insertsql);
					TranLogTxt.liswriteEror_to_txt(logname, "res:发送申请ok！");
					
					flags = true;
				} else {
					flags = false;
				}
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ": 操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			flags = false;
		}
		return flags;
	}

}
