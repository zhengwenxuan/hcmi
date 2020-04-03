package com.hjw.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.sql.RowSet;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.DTO.CenterConfigurationDTO;
import com.hjw.DTO.ExamQueueLog;
import com.hjw.DTO.HisClinicItemPriceDTO;
import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.FEESendMessage;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.Fees;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.Bean.ThridReq;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.HisClinicItemPriceListDTO;
import com.hjw.wst.DTO.JobDTO;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.DTO.UserInfoDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.config.GetNumContral;
import com.hjw.wst.domain.ChargingDetailSingle;
import com.hjw.wst.domain.ChargingItem;
import com.hjw.wst.domain.ChargingSummarySingle;
import com.hjw.wst.domain.CustomerInfo;
import com.hjw.wst.domain.ExamInfo;
import com.hjw.wst.domain.ExaminfoChargingItem;
import com.hjw.wst.domain.HisClinicItem;
import com.hjw.wst.domain.HisPriceList;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.wst.service.impl
 * @Description: 体检单位管理
 * @author: yangm
 * @date: 2016年7月4日 上午9:37:08
 * @version V2.0.0.0
 */
public class ConfigServiceImpl implements ConfigService {
	private JdbcQueryManager jqm;
	private JdbcPersistenceManager jpm;
	
	public void setJdbcQueryManager(JdbcQueryManager jqm) {
		this.jqm = jqm;
	}
	public void setJdbcPersistenceManager(JdbcPersistenceManager jpm) {
		this.jpm = jpm;
	}
	
	/**
	 * 
	 * @Title: getCenterconfigByKey @Description: 通过key获取 CenterConfigurationDTO
	 * 配置 @param: @param keys @param: @return @param: @throws
	 * ServiceException @return: CenterConfigurationDTO @throws
	 */
	public CenterConfigurationDTO getCenterconfigByKey(String keys) throws ServiceException {
		String sql = "SELECT center_name,config_key,config_value,is_active,common"
				+ "  FROM center_configuration where config_key='" + keys + "' and is_active='Y' ";
		PageSupport map = this.jqm.getList(sql, 1, 50000, CenterConfigurationDTO.class);
		CenterConfigurationDTO list = new CenterConfigurationDTO();
		if ((map != null) && (map.getList() != null) && (map.getList().size() > 0)) {
			list = (CenterConfigurationDTO) map.getList().get(0);
		}
		return list;
	}

	@Override
	public void insert_log(ThridInterfaceLog til) throws ServiceException {
		if(til.getMessage_id() != null) {
			til.setMessage_id(til.getMessage_id().replaceAll("'", "''"));
		}
		if(til.getMessage_request() != null) {
			til.setMessage_request(til.getMessage_request().replaceAll("'", "''"));
		}
		if(til.getMessage_response() != null) {
			til.setMessage_response(til.getMessage_response().replaceAll("'", "''"));
		}
		if(til.getSys_request() != null) {
			til.setSys_request(til.getSys_request().replaceAll("'", "''"));
		}
		if(til.getSys_respones() != null) {
			til.setSys_respones(til.getSys_respones().replaceAll("'", "''"));
		}
		String sql= " INSERT INTO thrid_interface_log (id,req_no,exam_no,message_id,message_name,"
				+ "message_date,message_type,sender,receiver,message_request,"
				+ "message_response,flag,sys_request,sys_respones,xtgnb_id,message_inout) VALUES ("
				+ "'"+til.getId()+"','"+til.getReq_no()+"','"+til.getExam_no()+"','"+til.getMessage_id()+"','"+til.getMessage_name()+"'"
				+ ",'"+til.getMessage_date()+"','"+til.getMessage_type()+"','"+til.getSender()+"','"+til.getReceiver()+"','"+til.getMessage_request()+"'"
				+ ",'"+til.getMessage_response()+"',"+til.getFlag()+",'"+til.getSys_request()+"','"+til.getSys_respones()+"','"+til.getXtgnb_id()+"',"+til.getMessage_inout()+")";
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt("insert_log_error", "插入日志主表sql："+sql);
			TranLogTxt.liswriteEror_to_txt("insert_log_error", "插入日志主表错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
	}
	
	@Override
	public void insert_message_log(String til_id, int seq_code, String lmessage) throws ServiceException {
		lmessage = lmessage.replaceAll("'", "''");
		String ldate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		String sql= " INSERT INTO thrid_interface_message_log (id,til_id,seq_code,ldate,lmessage) VALUES "
				+ "('"+UUID.randomUUID().toString().replaceAll("-", "")+"','"+til_id+"',"+seq_code+",'"+ldate+"','"+lmessage+"')";
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt("insert_message_log_error", "插入日志明细表sql："+sql);
			TranLogTxt.liswriteEror_to_txt("insert_message_log_error", "插入日志明细表错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
	}
	
	@Override
	public void update_log(ThridInterfaceLog til) throws ServiceException {
		if(til.getMessage_id() != null) {
			til.setMessage_id(til.getMessage_id().replaceAll("'", "''"));
		}
		if(til.getMessage_request() != null) {
			til.setMessage_request(til.getMessage_request().replaceAll("'", "''"));
		}
		if(til.getMessage_response() != null) {
			til.setMessage_response(til.getMessage_response().replaceAll("'", "''"));
		}
		if(til.getSys_request() != null) {
			til.setSys_request(til.getSys_request().replaceAll("'", "''"));
		}
		if(til.getSys_respones() != null) {
			til.setSys_respones(til.getSys_respones().replaceAll("'", "''"));
		}
		String sql= " UPDATE thrid_interface_log SET req_no = '"+til.getReq_no()+"',exam_no = '"+til.getExam_no()+"',"
				+ "message_id = '"+til.getMessage_id()+"',message_name = '"+til.getMessage_name()+"',message_date = '"+til.getMessage_date()+"',"
				+ "message_type = '"+til.getMessage_type()+"',sender = '"+til.getSender()+"',receiver = '"+til.getReceiver()+"',"
				+ "message_request = '"+til.getMessage_request()+"',message_response = '"+til.getMessage_response()+"',"
				+ "flag = '"+til.getFlag()+"',sys_request = '"+til.getSys_request()+"',sys_respones = '"+til.getSys_respones()+"',"
				+ "xtgnb_id = '"+til.getXtgnb_id()+"',message_inout = '"+til.getMessage_inout()+"'"
				+ " WHERE id = '"+til.getId()+"'";
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt("update_log_error", "更新日志主表sql："+sql);
			TranLogTxt.liswriteEror_to_txt("update_log_error", "更新日志主表错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
	}
	
	@Override
	public boolean insert_lis_result(LisResult lr) throws ServiceException {
		int resflag = -1;
		if(lr.getLis_item_name() != null) {
			lr.setLis_item_name(lr.getLis_item_name().replaceAll("'", "''"));
		}
		if(lr.getReport_item_name() != null) {
			lr.setReport_item_name(lr.getReport_item_name().replaceAll("'", "''"));
		}
		if(lr.getItem_result() != null) {
			lr.setItem_result(lr.getItem_result().replaceAll("'", "''"));
		}
		if(lr.getItem_unit() != null) {
			lr.setItem_unit(lr.getItem_unit().replaceAll("'", "''"));
		}
		if(lr.getRef() != null) {
			lr.setRef(lr.getRef().replaceAll("'", "''"));
		}
		if(lr.getNote() != null) {
			lr.setNote(lr.getNote().replaceAll("'", "''"));
		}
		String sql= " INSERT INTO lis_result (exam_num,til_id,sample_barcode,lis_item_code, "
				+ " lis_item_name,report_item_code,report_item_name,exam_date,item_result, "
				+ " item_unit,flag,ref,create_time,seq_code,doctor, "
				+ " sh_doctor,note,read_flag) VALUES "
				+ " ('"+lr.getExam_num()+"','"+lr.getTil_id()+"','"+lr.getSample_barcode()+"','"+lr.getLis_item_code()+"', "
				+ " '"+lr.getLis_item_name()+"','"+lr.getReport_item_code()+"','"+lr.getReport_item_name()+"','"+lr.getExam_date()+"','"+lr.getItem_result()+"', "
				+ " '"+lr.getItem_unit()+"','"+lr.getFlag()+"','"+lr.getRef()+"','"+lr.getCreate_time()+"','"+lr.getSeq_code()+"','"+lr.getDoctor()+"', "
				+ " '"+lr.getSh_doctor()+"','"+lr.getNote()+"','"+lr.getRead_flag()+"')";
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			TranLogTxt.liswriteEror_to_txt("insert_lis_result_error", "插入lis_result sql："+sql);
			resflag = connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt("insert_lis_result_error", "插入lis_result sql："+sql);
			TranLogTxt.liswriteEror_to_txt("insert_lis_result_error", "插入lis_result 错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
		return resflag>0;
	}
	
	@Override
	public boolean insert_lis_result_new(LisResult lr) throws ServiceException {
		int resflag = -1;
		if(lr.getLis_item_name() != null) {
			lr.setLis_item_name(lr.getLis_item_name().replaceAll("'", "''"));
		}
		if(lr.getReport_item_name() != null) {
			lr.setReport_item_name(lr.getReport_item_name().replaceAll("'", "''"));
		}
		if(lr.getItem_result() != null) {
			lr.setItem_result(lr.getItem_result().replaceAll("'", "''"));
		}
		if(lr.getItem_unit() != null) {
			lr.setItem_unit(lr.getItem_unit().replaceAll("'", "''"));
		}
		if(lr.getRef() != null) {
			lr.setRef(lr.getRef().replaceAll("'", "''"));
		}
		if(lr.getNote() != null) {
			lr.setNote(lr.getNote().replaceAll("'", "''"));
		}
		String sql= " INSERT INTO lis_result_new (exam_num,til_id,sample_barcode,lis_item_code, "
				+ " lis_item_name,report_item_code,report_item_name,exam_date,item_result, "
				+ " item_unit,flag,ref,create_time,seq_code,doctor, "
				+ " sh_doctor,note,read_flag) VALUES "
				+ " ('"+lr.getExam_num()+"','"+lr.getTil_id()+"','"+lr.getSample_barcode()+"','"+lr.getLis_item_code()+"', "
				+ " '"+lr.getLis_item_name()+"','"+lr.getReport_item_code()+"','"+lr.getReport_item_name()+"','"+lr.getExam_date()+"','"+lr.getItem_result()+"', "
				+ " '"+lr.getItem_unit()+"','"+lr.getFlag()+"','"+lr.getRef()+"','"+lr.getCreate_time()+"','"+lr.getSeq_code()+"','"+lr.getDoctor()+"', "
				+ " '"+lr.getSh_doctor()+"','"+lr.getNote()+"','"+lr.getRead_flag()+"')";
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			resflag = connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt("insert_lis_result_error", "插入lis_result sql："+sql);
			TranLogTxt.liswriteEror_to_txt("insert_lis_result_error", "插入lis_result 错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
		return resflag>0;
	}
	
	@Override
	public boolean insert_pacs_result(PacsResult pr) throws ServiceException {
		int resflag = -1;
		if(pr.getItem_name() != null) {
			pr.setItem_name(pr.getItem_name().replaceAll("'", "''"));
		}
		if(pr.getStudy_body_part() != null) {
			pr.setStudy_body_part(pr.getStudy_body_part().replaceAll("'", "''"));
		}
		if(pr.getClinic_diagnose() != null) {
			pr.setClinic_diagnose(pr.getClinic_diagnose().replaceAll("'", "''"));
		}
		if(pr.getClinic_symptom() != null) {
			pr.setClinic_symptom(pr.getClinic_symptom().replaceAll("'", "''"));
		}
		if(pr.getClinic_advice() != null) {
			pr.setClinic_advice(pr.getClinic_advice().replaceAll("'", "''"));
		}
		if(pr.getNote() != null) {
			pr.setNote(pr.getNote().replaceAll("'", "''"));
		}
		String sql= " INSERT INTO PACS_RESULT (req_no,pacs_checkno,exam_num,til_id,item_name, "
				+ " pacs_item_code,study_type,study_body_part,clinic_diagnose,clinic_symptom, "
				+ " clinic_advice,is_abnormal,report_img_path,img_path,study_state, "
				+ " reg_doc,check_doc,check_date,report_doc,report_date, "
				+ " audit_doc,audit_date,note,status, "
				+ " is_tran_image,is_report_image,create_time) VALUES ("
				+ " '"+pr.getReq_no()+"','"+pr.getPacs_checkno()+"','"+pr.getExam_num()+"','"+pr.getTil_id()+"','"+pr.getItem_name()+"', "
				+ " '"+pr.getPacs_item_code()+"','"+pr.getStudy_type()+"','"+pr.getStudy_body_part()+"','"+pr.getClinic_diagnose()+"','"+pr.getClinic_symptom()+"', "
				+ " '"+pr.getClinic_advice()+"','"+pr.getIs_abnormal()+"','"+pr.getReport_img_path()+"','"+pr.getImg_path()+"','"+pr.getStudy_state()+"', "
				+ " '"+pr.getReg_doc()+"','"+pr.getCheck_doc()+"','"+pr.getCheck_date()+"','"+pr.getReport_doc()+"','"+pr.getReport_date()+"', "
				+ " '"+pr.getAudit_doc()+"','"+pr.getAudit_date()+"','"+pr.getNote()+"','"+pr.getStatus()+"',"
				+ " '"+pr.getIs_tran_image()+"','"+pr.getIs_report_image()+"','"+pr.getCreate_time()+"')";
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			TranLogTxt.liswriteEror_to_txt("insert_pacs_result", "插入pacs_result sql："+sql);
			resflag = connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt("insert_pacs_result_error", "插入pacs_result sql："+sql);
			TranLogTxt.liswriteEror_to_txt("insert_pacs_result_error", "插入pacs_result 错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
		return resflag>0;
	}
	
	public ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.*,a.arch_num,a.user_name,a.id_num,a.sex,a.birthday,a.nation,a.phone,a.address,a.email ");
		sb.append(" from customer_info a,exam_info c ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");
		PageSupport map = this.jqm.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	public ExamInfoUserDTO getExamInfoForBarcode(String sample_barcode) throws ServiceException{
		String sql = " select ci.user_name,ci.id_num,ci.sex,ci.birthday,ci.phone,ci.address,ei.is_marriage, "
				+ " ei.id,ei.age,ei.exam_num,ei.status,ei.exam_type,ei.register_date,ei.join_date,ei.exam_times "
				+ " from sample_exam_detail sed,exam_info ei, customer_info ci "
				+ " where sed.sample_barcode='" + sample_barcode+ "' and sed.exam_info_id=ei.id and ei.customer_id = ci.id";
		PageSupport map = this.jqm.getList(sql, 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	public ExamInfoUserDTO getExamInfoForBarcode305(String sample_barcode) throws ServiceException{
		String sql = " select zri.req_id,ci.user_name,ci.id_num,ci.sex,ci.birthday,ci.phone, "
				+ " ei.id,ei.age,ei.exam_num,ei.status,ei.exam_type,ei.register_date,ei.join_date,ei.exam_times "
				+ " from sample_exam_detail sed,exam_info ei, customer_info ci , zl_req_item zri"
				+ " where sed.sample_barcode='" + sample_barcode+ "' and sed.exam_info_id=ei.id and ei.customer_id = ci.id  and zri.lis_req_code=sed.sample_barcode";
		PageSupport map = this.jqm.getList(sql, 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	
	@Override
	public ExamInfoUserDTO getExamInfoForReqNum305(String req_nums) {
		String sql = " select m.id,m.age,m.exam_num,m.status,m.exam_type "
				+ " ,m.register_date,m.join_date,m.exam_times "
				+ " ,n.user_name,n.id_num,n.sex,n.birthday,n.phone "
				+ " from examinfo_charging_item a,exam_info m,customer_info n "
				+ " ,pacs_summary b,pacs_detail c,charging_item d,zl_req_pacs_item zrpi " + " where zrpi.req_id='" + req_nums
				+ "' and c.summary_id=b.id and c.chargingItem_num=d.item_code "
				+ " and d.id=a.charge_item_id and a.examinfo_id=m.id "
				+ " and m.exam_num=b.examinfo_num  and zrpi.pacs_req_code=b.pacs_req_code and a.isActive='Y' "
				+ " and m.customer_id = n.id";
		PageSupport map = this.jqm.getList(sql, 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	public ExamInfoUserDTO getExamInfoForReqNum(String req_nums) throws ServiceException{
		String sql = " select m.id,m.age,m.exam_num,m.status,m.exam_type "
				+ " ,m.register_date,m.join_date,m.exam_times "
				+ " ,n.user_name,n.id_num,n.sex,n.birthday,n.phone "
				+ " from examinfo_charging_item a,exam_info m,customer_info n "
				+ " ,pacs_summary b,pacs_detail c,charging_item d " + " where b.pacs_req_code='" + req_nums
				+ "' and c.summary_id=b.id and c.chargingItem_num=d.item_code "
				+ " and d.id=a.charge_item_id and a.examinfo_id=m.id "
				+ " and m.exam_num=b.examinfo_num and a.isActive='Y' "
				+ " and m.customer_id = n.id";
		PageSupport map = this.jqm.getList(sql, 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	public ExamInfoUserDTO getExamInfoForExam_id(long exam_id) throws ServiceException{
		StringBuffer sb = new StringBuffer();
		sb.append("select c.*,( SELECT vip_name FROM config_exam_vip WHERE id = c.vipflag ) AS vipflag"
				+ ",a.arch_num,a.user_name,a.id_num,a.sex,a.birthday,a.nation,a.phone,a.address,a.email ");
		sb.append(" from customer_info a,exam_info c ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.id = " + exam_id);	
		PageSupport map = this.jqm.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	@Override
	public void setExamInfoChargeItemPacsStatus305(String req_nums, String status) {
		// 插入数据库
				Connection connection = null;
				Statement statement = null;
				try {
					// 读取记录数
					connection = this.jqm.getConnection();
					// connection.setAutoCommit(false);
						String sql = " select a.id,a.exam_status from examinfo_charging_item a,exam_info m"
								+ ",pacs_summary b,pacs_detail c,charging_item d,zl_req_pacs_item zrpi" + " where zrpi.req_id in ('" + req_nums.trim().replace(",", "','")
								+ "') and c.summary_id=b.id and c.chargingItem_num=d.item_code "
								+ "and d.id=a.charge_item_id and a.examinfo_id=m.id "
								+ "and m.exam_num=b.examinfo_num and zrpi.pacs_req_code=b.pacs_req_code and a.isActive='Y'";
						TranLogTxt.liswriteEror_to_txt("updatePacsStatus", "select-sql1："+sql);
						statement = connection.createStatement();
						ResultSet rs = statement.executeQuery(sql);
						// 处理结果
						while (rs.next()) {
							long exam_id = rs.getLong("id");
							String exam_status = rs.getString("exam_status");
							if (!"Y".equals(exam_status)) {
								sql = "update examinfo_charging_item set exam_status='" + status + "' where id=" + exam_id + "";
								TranLogTxt.liswriteEror_to_txt("updatePacsStatus", "update-sql1："+sql);
								connection.createStatement().executeUpdate(sql);
							}
						}
						rs.close();
					// connection.commit();
				} catch (Exception ex) {
					/*
					 * try { connection.rollback(); } catch (SQLException e) {
					 * 
					 * }
					 */
					ex.printStackTrace();
				} finally {
					try {
						if (statement != null) {
							statement.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException sqle4) {
						sqle4.printStackTrace();
					}
				}
		
	}
	
	
	public void setExamInfoChargeItemPacsStatus(String req_nums, String status) throws ServiceException {
		// 插入数据库
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = this.jqm.getConnection();
			// connection.setAutoCommit(false);
				String sql = " select a.id,a.exam_status from examinfo_charging_item a,exam_info m"
						+ ",pacs_summary b,pacs_detail c,charging_item d" + " where b.pacs_req_code in ('" + req_nums.trim().replace(",", "','")
						+ "') and c.summary_id=b.id and c.chargingItem_num=d.item_code "
						+ "and d.id=a.charge_item_id and a.examinfo_id=m.id "
						+ "and m.exam_num=b.examinfo_num and a.isActive='Y'";
				TranLogTxt.liswriteEror_to_txt("updatePacsStatus", "select-sql1："+sql);
				statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				// 处理结果
				while (rs.next()) {
					long exam_id = rs.getLong("id");
					String exam_status = rs.getString("exam_status");
					if (!"Y".equals(exam_status)) {
						sql = "update examinfo_charging_item set exam_status='" + status + "' where id=" + exam_id + "";
						TranLogTxt.liswriteEror_to_txt("updatePacsStatus", "update-sql1："+sql);
						connection.createStatement().executeUpdate(sql);
					}
				}
				rs.close();
			// connection.commit();
		} catch (Exception ex) {
			/*
			 * try { connection.rollback(); } catch (SQLException e) {
			 * 
			 * }
			 */
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	public void setExamInfoChargeItemPacsStatus(String req_nums, String pacsItemCode, String status) throws ServiceException {
		// 插入数据库
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = this.jqm.getConnection();

			String sql = " select a.id,a.exam_status from examinfo_charging_item a,exam_info m "
					+ " ,pacs_summary b,pacs_detail c,charging_item d" + " where b.pacs_req_code in ('" + req_nums.trim().replace(",", "','") + "') "
					+ " and c.summary_id=b.id and c.chargingItem_num=d.item_code "
					+ " and d.id=a.charge_item_id and a.examinfo_id=m.id "
					+ " and m.exam_num=b.examinfo_num and a.isActive='Y' "
					+ " and d.view_num = '" + pacsItemCode + "'";
			TranLogTxt.liswriteEror_to_txt("updatePacsStatus", "select-sql2："+sql);
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			// 处理结果
			while (rs.next()) {
				long exam_id = rs.getLong("id");
				String exam_status = rs.getString("exam_status");
				if (!"Y".equals(exam_status)) {
					sql = "update examinfo_charging_item set exam_status='" + status + "' where id=" + exam_id + "";
					TranLogTxt.liswriteEror_to_txt("updatePacsStatus", "update-sql2："+sql);
					connection.createStatement().executeUpdate(sql);
				}
			}
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	public void setExamInfoChargeItemLisStatus(List<String> req_nums, String exam_num, String status, String samstatus)
			throws ServiceException {
		// 插入数据库
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = this.jqm.getConnection();
			// connection.setAutoCommit(false);
			for (String pac_no : req_nums) {
				String sql = "select e.id as examid,e.exam_status as examstau,a.id as samid,a.status as samsta "
						+ "from sample_exam_detail a," + "exam_info b,examResult_chargingItem c,"
						+ "examinfo_charging_item e  where a.sample_barcode='" + pac_no + "' "
						+ "and a.exam_info_id=b.id and b.exam_num='" + exam_num + "' "
						+ "and a.id=c.exam_id  and c.charging_id=e.charge_item_id "
						+ "and e.examinfo_id=b.id and c.result_type = 'sample'";
				TranLogTxt.liswriteEror_to_txt("updateLisStatus", "select-sql："+sql);
				statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				// 处理结果
				while (rs.next()) {
					long exam_id = rs.getLong("examid");
					String exam_status = rs.getString("examstau");
					if (!"Y".equals(exam_status)) {
						sql = "update examinfo_charging_item set exam_status='" + status + "' where id=" + exam_id + "";
						TranLogTxt.liswriteEror_to_txt("updateLisStatus", "update-sql1-1："+sql);
						connection.createStatement().executeUpdate(sql);

						long samid = rs.getLong("samid");
						// String samsta = rs.getString("samsta");
						sql = "update sample_exam_detail set status='" + samstatus + "' where id=" + samid + "";
						TranLogTxt.liswriteEror_to_txt("updateLisStatus", "update-sql1-2："+sql);
						connection.createStatement().executeUpdate(sql);
					}
				}
				rs.close();
			}
			// connection.commit();
		} catch (Exception ex) {
			/*
			 * try { connection.rollback(); } catch (SQLException e) {
			 * 
			 * }
			 */
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	public void setExamInfoChargeItemLisStatus(String sample_barcode, String status, String samstatus, UserInfoDTO user, String check_date, String logName)
			throws ServiceException {
		// 插入数据库
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = this.jqm.getConnection();
			// connection.setAutoCommit(false);
			String sql = "select e.id as examid,e.exam_status as examstau,a.id as samid,a.status as samsta "
					+ "from sample_exam_detail a," + "exam_info b,examResult_chargingItem c,"
					+ "examinfo_charging_item e  where a.sample_barcode='" + sample_barcode + "' "
					+ "and a.exam_info_id=b.id "
					+ "and a.id=c.exam_id  and c.charging_id=e.charge_item_id "
					+ "and e.examinfo_id=b.id and c.result_type = 'sample'";
			TranLogTxt.liswriteEror_to_txt(logName, "select-sql："+sql);
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			// 处理结果
			while (rs.next()) {
				long exam_id = rs.getLong("examid");
				String exam_status = rs.getString("examstau");
				if (!"Y".equals(exam_status)) {
					sql = "update examinfo_charging_item set exam_status='" + status + "' where id=" + exam_id + "";
					TranLogTxt.liswriteEror_to_txt(logName, "update-sql1-1："+sql);
					connection.createStatement().executeUpdate(sql);
					
					long samid = rs.getLong("samid");
					// String samsta = rs.getString("samsta");
					sql = "update sample_exam_detail set status='" + samstatus + "'";
					if(user == null) {
						sql += ", check_id=0, check_doctor=null, check_date=null ";
					} else {
						sql += ", check_id = "+user.getId()+", check_doctor = '"+user.getChi_Name()+"',check_date = '"+check_date+"' ";
					}
					sql += " where id=" + samid;
					
					TranLogTxt.liswriteEror_to_txt(logName, "update-sql1-2："+sql);
					connection.createStatement().executeUpdate(sql);
				}
			}
			rs.close();
			// connection.commit();
		} catch (Exception ex) {
			/*
			 * try { connection.rollback(); } catch (SQLException e) {
			 * 
			 * }
			 */
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logName, "Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	public void setExamInfoChargeItemLisStatus(String req_num, String itemCodes, String status, String samstatus)
			throws ServiceException {
		// 插入数据库
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = this.jqm.getConnection();
			// connection.setAutoCommit(false);

			String sql = " select d.item_code, e.id as examid,e.exam_status as examstau,a.id as samid,a.status as samsta "
					+ " from sample_exam_detail a,exam_info b,examResult_chargingItem c,charging_item d, "
					+ " examinfo_charging_item e  where a.sample_barcode='"+req_num+"' "
					+ " and a.exam_info_id=b.id "
					+ " and a.id=c.exam_id and c.charging_id=d.id "
					+ " and e.examinfo_id=b.id and e.charge_item_id=d.id ";
			if(!StringUtil.isEmpty(itemCodes)) {
				sql += " and d.item_code in ('"+itemCodes.replaceAll(",", "','")+"') ";
			}
			TranLogTxt.liswriteEror_to_txt("updateLisStatus", "select-sql："+sql);
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			// 处理结果
			while (rs.next()) {
				long exam_id = rs.getLong("examid");
				String exam_status = rs.getString("examstau");
				if (!"Y".equals(exam_status)) {
					sql = "update examinfo_charging_item set exam_status='" + status + "' where id=" + exam_id + "";
					TranLogTxt.liswriteEror_to_txt("updateLisStatus", "update-sql2-1："+sql);
					connection.createStatement().executeUpdate(sql);
					
					long samid = rs.getLong("samid");
					// String samsta = rs.getString("samsta");
					sql = "update sample_exam_detail set status='" + samstatus + "' where id=" + samid + "";
					TranLogTxt.liswriteEror_to_txt("updateLisStatus", "update-sql2-2："+sql);
					connection.createStatement().executeUpdate(sql);
				}
			}
			rs.close();
			// connection.commit();
		} catch (Exception ex) {
			/*
			 * try { connection.rollback(); } catch (SQLException e) {
			 * 
			 * }
			 */
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	public UserInfoDTO getUser(String user_log_name, String logName) {
		String sql = "select u.id,u.chi_name,u.log_name,u.pwd_encrypted from user_usr u where u.log_name = '"+user_log_name+"' and u.is_active = 'Y'";
		TranLogTxt.liswriteEror_to_txt(logName, "sql： " +sql);
		List<UserInfoDTO> list = this.jqm.getList(sql, UserInfoDTO.class);
		if(list == null || list.isEmpty()) {
			return new UserInfoDTO();
		}
		return list.get(0);
	}
	
	public boolean insert_thrid_req(ThridReq tr) throws ServiceException {
		int resflag = -1;
		if(tr.getDatabody() != null) {
			tr.setDatabody(tr.getDatabody().replaceAll("'", "''"));
		}
		if(tr.getNotices() != null) {
			tr.setNotices(tr.getNotices().replaceAll("'", "''"));
		}
		String sql= " INSERT INTO thrid_req (exam_info_id,exam_num,arch_num,req_no,readFlag,read_time,creater,create_time,"
				+ "databody,notices,errornum,ser_config_key) VALUES ("
				+ "'"+tr.getExam_info_id()+"','"+tr.getExam_num()+"','"+tr.getArch_num()+"','"+tr.getReq_no()+"','"+tr.getReadFlag()+"'"
				+ ",'"+tr.getRead_time()+"','"+tr.getCreater()+"','"+tr.getCreate_time()+"','"+tr.getDatabody()+"','"+tr.getNotices()+"'"
				+ ",'"+tr.getErrornum()+"','"+tr.getSer_config_key()+"')";
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			resflag = connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt("insert_thrid_req_error", "插入第三方申请表sql："+sql);
			TranLogTxt.liswriteEror_to_txt("insert_thrid_req_error", "插入第三方申请表错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
		return resflag>0;
	}
	
	public boolean update_thrid_req(int readFlag,String notices, int id, String logname) throws ServiceException {
		String sql = "update thrid_req set readFlag = "+readFlag+", read_time = getdate(),errornum=(errornum+1),notices='"+notices.replaceAll("'", "''")+"' where id = '"+id+"'";
		int resflag = -1;
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			resflag = connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt(logname, "更新thrid_req sql："+sql);
			TranLogTxt.liswriteEror_to_txt(logname, "更新thrid_req 错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
		return resflag>0;
	}
	
	/**
	 * 插入exam_queue_log表
	 */
	public boolean insertExamQueueLog(ExamQueueLog eql, String logname) throws ServiceException {
		Connection connect = null;
		int resflag = -1;
		try {
			connect = this.jqm.getConnection();
			String sb1 = "insert exam_queue_log (exam_num,queue_no,dept_num,queue_day,queue_date) values('"
			+eql.getExam_num()+"','"+eql.getQueue_no()+"','"+eql.getDept_num()+"','"+DateTimeUtil.getDate()+"','"+DateTimeUtil.getDateTime()+"') ";
			TranLogTxt.liswriteEror_to_txt(logname, "插入exam_queue_log表SQL:" +sb1);				
			resflag = connect.createStatement().executeUpdate(sb1);
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:  插入数据操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return resflag>0;
	}
	
	public int insert_zl_req_item(ZlReqItemDTO zri,String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jqm.getConnection();
			String sb1 = "delete from zl_req_item where exam_info_id='"+zri.getExam_info_id()
					+"'  and charging_item_id='"+zri.getCharging_item_id()+"' and lis_req_code='"+zri.getLis_req_code()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "delete-zl_req_item-sql:" +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
			String insertsql = "insert into zl_req_item(exam_info_id,charging_item_id,req_id,lis_req_code,createdate) values('" 
			+ zri.getExam_info_id() + "','" +zri.getCharging_item_id() + "','"+zri.getReq_id()+"','"+zri.getLis_req_code()+"',getdate())";
			TranLogTxt.liswriteEror_to_txt(logname, "insert-zl_req_item-sql:" +insertsql);				
			preparedStatement = tjtmpconnect.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.executeUpdate();
			ResultSet rs = null;
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next())
				lisid = rs.getInt(1);
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "zl_req_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
	
	public int insert_zl_req_pacs_item(ZlReqPacsItemDTO item,String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jqm.getConnection();
			String sb1 = "delete from zl_req_pacs_item where exam_info_id='"+item.getExam_info_id()
			+"'  and charging_item_ids='"+item.getCharging_item_ids()+"' and pacs_req_code='"+item.getPacs_req_code()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "delete-zl_req_pacs_item-sql:" +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
			String insertsql = "insert into zl_req_pacs_item(exam_info_id,charging_item_ids,req_id,pacs_req_code,createdate) values('" 
					+ item.getExam_info_id() + "','" +item.getCharging_item_ids() + "','"+item.getReq_id()+"','"+item.getPacs_req_code()+"',getdate())";
			TranLogTxt.liswriteEror_to_txt(logname, "insert-zl_req_pacs_item-sql:" +insertsql);				
			preparedStatement = tjtmpconnect.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.executeUpdate();
			ResultSet rs = null;
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next())
				lisid = rs.getInt(1);
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
	
	public List<ZlReqItemDTO> select_zl_req_item(long exam_info_id, String lis_req_code,String logname) throws ServiceException {
		List<ZlReqItemDTO> list = new ArrayList<>();
		try {
			String selectSQL = "select * from zl_req_item where exam_info_id='"+exam_info_id+"' and lis_req_code='"+lis_req_code+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "select-zl_req_item-list-sql:" + selectSQL);
			list = this.jqm.getList(selectSQL, ZlReqItemDTO.class);
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "select_zl_req_item list 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return list;
	}
	
	public List<ZlReqPacsItemDTO> select_zl_req_pacs_item(long exam_info_id, String pacs_req_code,String logname) throws ServiceException {
		List<ZlReqPacsItemDTO> list = new ArrayList<>();
		try {
			String selectSQL = "select * from zl_req_pacs_item where exam_info_id='"+exam_info_id+"' and pacs_req_code='"+pacs_req_code+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "select-zl_req_pacs_item-list-sql:" + selectSQL);
			list = this.jqm.getList(selectSQL, ZlReqPacsItemDTO.class);
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "select_zl_req_pacs_item list 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return list;
	}
	
	public ZlReqItemDTO select_zl_req_item(String req_id, String logname) throws ServiceException {
		List<ZlReqItemDTO> list = new ArrayList<>();
		try {
			String selectSQL = "select * from zl_req_item where req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "select-zl_req_item-sql:" + selectSQL);
			list = this.jqm.getList(selectSQL, ZlReqItemDTO.class);
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "zl_req_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		if(list.isEmpty()) {
			return new ZlReqItemDTO();
		}
		return list.get(0);
	}
	
	public ZlReqItemDTO select_zl_req_item_by_id(String id, String logname) throws ServiceException {
		List<ZlReqItemDTO> list = new ArrayList<>();
		try {
			String selectSQL = "select * from zl_req_item where id='"+id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "select-zl_req_item-sql:" + selectSQL);
			list = this.jqm.getList(selectSQL, ZlReqItemDTO.class);
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "zl_req_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		if(list.isEmpty()) {
			return new ZlReqItemDTO();
		}
		return list.get(0);
	}
	
	public ZlReqPacsItemDTO select_zl_req_pacs_item(String req_id, String logname) throws ServiceException {
		List<ZlReqPacsItemDTO> list = new ArrayList<>();
		try {
			String selectSQL = "select * from zl_req_pacs_item where req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "select-zl_req_pacs_item-sql:" + selectSQL);
			list = this.jqm.getList(selectSQL, ZlReqPacsItemDTO.class);
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		if(list.isEmpty()) {
			return new ZlReqPacsItemDTO();
		}
		return list.get(0);
	}
	
	public ZlReqPacsItemDTO select_zl_req_pacs_item_by_id(String id, String logname) throws ServiceException {
		List<ZlReqPacsItemDTO> list = new ArrayList<>();
		try {
			String selectSQL = "select * from zl_req_pacs_item where req_id='"+id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "select-zl_req_pacs_item-sql:" + selectSQL);
			list = this.jqm.getList(selectSQL, ZlReqPacsItemDTO.class);
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		if(list.isEmpty()) {
			return new ZlReqPacsItemDTO();
		}
		return list.get(0);
	}
	
	public ChargingItemDTO findChargeItemByHis_num(String his_num) throws ServiceException {
		ChargingItemDTO ci = new ChargingItemDTO();
		StringBuffer sql = new StringBuffer("select id,dep_id,sam_demo_id,sam_report_demo_id,"
				+ "item_code,item_name,item_pinyin,item_category,sex,amount,dep_category,"
				+ "isOnlyApplyOrReport,item_seq,guide_category,his_num,exam_num,view_num,"
				+ "isActive,creater,create_time,updater,update_time,calculation_amount,"
				+ "interface_flag,item_type,charge_inter_num,item_abbreviation "
				+ "From charging_item where his_num = '"
				+ his_num + "'");
		sql.append("  order by update_time desc");
		PageSupport map = this.jqm.getList(sql.toString(), 1, 1000, ChargingItemDTO.class);
		if ((map != null) && (map.getList() != null))
			ci = (ChargingItemDTO) map.getList().get(0);

		return ci;
	}
	
	@Override
	public List<HisClinicItemPriceListDTO> getHisjg(HisClinicItemPriceListDTO dto) throws ServiceException {
		String sql="SELECT	p.item_class	as   item_class_p,p.item_code  as  item_code_p,p.item_name  as item_name_p,"
				+"p.units,p.item_spec,p.price,hcp.amount	 from his_clinic_item_v_price_list   hcp,his_price_list  p,his_clinic_item   c"
				+" where   hcp.charge_item_code=p.item_code   and  hcp.clinic_item_code =c.item_code" 
				+" and  hcp.charge_item_class=p.item_class  and  hcp.clinic_item_class=c.item_class  "
//				+ " and  hcp.units=p.units  and hcp.charge_item_spec=p.item_spec" 
//				+"  and  '"+dto.getSystemdate()+"'>=p.start_date   and  '"+dto.getSystemdate()+"'<=p.stop_date"
			    +" and 	hcp.clinic_item_code='"+dto.getClinic_item_code()+"'";
			    if(!StringUtil.isEmpty(dto.getBody_part()) && !StringUtil.isEmpty(dto.getMethod())) {
			    	sql += " and hcp.body_part = '"+dto.getBody_part()+"' and hcp.method = '"+dto.getMethod()+"'";
			    }
		sql += "  order by  p.item_name";
	    List<HisClinicItemPriceListDTO> li =this.jqm.getList(sql,HisClinicItemPriceListDTO.class);
	    return li;
	}
	
	public List<HisClinicItemPriceListDTO> getHisjg305(HisClinicItemPriceListDTO dto) throws ServiceException {
		String sql=" SELECT	p.item_class	as   item_class_p,p.item_code  as  item_code_p,p.item_name  as item_name_p,"
				+" p.units,p.price,hcp.amount	 from his_clinic_item_v_price_list   hcp,his_price_list  p,his_clinic_item   c"
				+" where   hcp.charge_item_code=p.item_code   and  hcp.clinic_item_code =c.item_code" 
				+" and  hcp.charge_item_class=p.item_class  and  hcp.clinic_item_class=c.item_class  "
				+" and  hcp.units=p.units  and hcp.charge_item_spec=p.item_spec" 
				+" and  '"+dto.getSystemdate()+"'>=p.start_date   and  '"+dto.getSystemdate()+"'<=p.stop_date"
			    +" and 	hcp.clinic_item_code='"+dto.getClinic_item_code()+"'";
			   /* if(!StringUtil.isEmpty(dto.getBody_part()) && !StringUtil.isEmpty(dto.getMethod())) {
			    	sql += " and hcp.body_part = '"+dto.getBody_part()+"' and hcp.method = '"+dto.getMethod()+"'";
			    }*/
		sql += "  order by  p.item_name";
	    List<HisClinicItemPriceListDTO> li =this.jqm.getList(sql,HisClinicItemPriceListDTO.class);
	    return li;
	}
	
	public List<HisClinicItemPriceListDTO> getHisjgTT(HisClinicItemPriceListDTO dto) throws ServiceException {
		String sql=" SELECT	p.item_class	as   item_class_p,p.item_code  as  item_code_p,p.item_name  as item_name_p,"
				+" p.units,p.price,hcp.amount	 from his_clinic_item_v_price_list   hcp,his_price_list  p,his_clinic_item   c"
				+" where   hcp.charge_item_code=p.item_code   and  hcp.clinic_item_code =c.item_code" 
				+" and  hcp.charge_item_class=p.item_class  and  hcp.clinic_item_class=c.item_class  "
				+" and  hcp.units=p.units  and hcp.charge_item_spec=p.item_spec "
			//	+ "and c.item_status='Y' " 
				+" and  '"+dto.getSystemdate()+"'>=p.start_date   and  '"+dto.getSystemdate()+"'<=p.stop_date"
			    +" and 	hcp.clinic_item_code='"+dto.getClinic_item_code()+"'";
			   /* if(!StringUtil.isEmpty(dto.getBody_part()) && !StringUtil.isEmpty(dto.getMethod())) {
			    	sql += " and hcp.body_part = '"+dto.getBody_part()+"' and hcp.method = '"+dto.getMethod()+"'";
			    }*/
		sql += "  order by  p.item_name";
	    List<HisClinicItemPriceListDTO> li =this.jqm.getList(sql,HisClinicItemPriceListDTO.class);
	    return li;
	}
	
	
	public void insertClinicPriceList(String logname, List<HisClinicItemPriceDTO> clinicPriceList) {
		Connection connect = null;
		String del_clinic_price_sql = "";
		try {
			connect = jqm.getConnection();
			HisClinicItemPriceDTO hisClinicItemPriceDTO = clinicPriceList.get(0);
			del_clinic_price_sql = "delete his_clinic_item_v_price_list where clinic_item_code = '"+hisClinicItemPriceDTO.getClinic_item_code()+"'";
			if(!StringUtil.isEmpty(hisClinicItemPriceDTO.getBody_part()) && !StringUtil.isEmpty(hisClinicItemPriceDTO.getMethod())) {
				del_clinic_price_sql += " and body_part = '"+hisClinicItemPriceDTO.getBody_part()+"' and method = '"+hisClinicItemPriceDTO.getMethod()+"'";
			}
			if(!StringUtil.isEmpty(hisClinicItemPriceDTO.getCharge_item_classs())) {
				del_clinic_price_sql += " and CHARGE_ITEM_CLASS = '"+hisClinicItemPriceDTO.getCharge_item_classs()+"'";
			}
			if(!StringUtil.isEmpty(hisClinicItemPriceDTO.getCharge_item_spec())) {
				del_clinic_price_sql += " and CHARGE_ITEM_SPEC = '"+hisClinicItemPriceDTO.getCharge_item_spec()+"'";
			}
			if(!StringUtil.isEmpty(hisClinicItemPriceDTO.getUnits())) {
				del_clinic_price_sql += " and units = '"+hisClinicItemPriceDTO.getUnits()+"'";
			}
			connect.createStatement().executeUpdate(del_clinic_price_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目和价表关系数据列表成功!");
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表和诊疗项目关系，错误sql-"+del_clinic_price_sql);
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int errorNum = 0;
		int successNum = 0;
		for(HisClinicItemPriceDTO clinicPrice : clinicPriceList) {
			String insert_clinic_price_sql = "";
			try {
				connect = jqm.getConnection();
//				String select_clinic_price_sql = "select * from his_clinic_item_v_price_list where clinic_item_code = '"+clinicPrice.getClinic_item_code()
//				+"' and charge_item_code = '"+clinicPrice.getCharge_item_code()+"'";
//				if(!StringUtil.isEmpty(clinicPrice.getBody_part()) && !StringUtil.isEmpty(clinicPrice.getMethod())) {
//					select_clinic_price_sql += " and body_part = '"+clinicPrice.getBody_part()+"' and method = '"+clinicPrice.getMethod()+"'";
//				}
//				if(!StringUtil.isEmpty(clinicPrice.getCharge_item_classs())) {
//					select_clinic_price_sql += " and CHARGE_ITEM_CLASS = '"+clinicPrice.getCharge_item_classs()+"'";
//				}
//				if(!StringUtil.isEmpty(clinicPrice.getCharge_item_spec())) {
//					select_clinic_price_sql += " and CHARGE_ITEM_SPEC = '"+clinicPrice.getCharge_item_spec()+"'";
//				}
//				if(!StringUtil.isEmpty(clinicPrice.getUnits())) {
//					select_clinic_price_sql += " and units = '"+clinicPrice.getUnits()+"'";
//				}
//				ResultSet rs = connect.createStatement().executeQuery(select_clinic_price_sql);
//				if(!rs.next()) {
					insert_clinic_price_sql = "insert into his_clinic_item_v_price_list([clinic_item_class],[clinic_item_code],[charge_item_no]"
							+ ",[charge_item_class],[charge_item_code],[charge_item_spec],"
							+ "[amount],[units],[backbill_rule],[create_date],[update_date],[body_part],[method])"
							+ " values("+quotedStr(clinicPrice.getClinic_item_class())+","+quotedStr(clinicPrice.getClinic_item_code())+","+quotedStr(clinicPrice.getCharge_item_no())
							+ ","+quotedStr(clinicPrice.getCharge_item_classs())+","+quotedStr(clinicPrice.getCharge_item_code())+","+quotedStr(clinicPrice.getCharge_item_spec())+","
							+clinicPrice.getAmount()+","+quotedStr(clinicPrice.getUnits())+",'',"+quotedStr(DateTimeUtil.getDateTime())+","+quotedStr(DateTimeUtil.getDateTime())+","+quotedStr(clinicPrice.getBody_part())+","+quotedStr(clinicPrice.getMethod())+")";
					//TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表和诊疗项目关系sql--"+insert_clinic_price_sql);
					connect.createStatement().executeUpdate(insert_clinic_price_sql);
					successNum++;
//				} else {
//					TranLogTxt.liswriteEror_to_txt(logname, "未执行插入-HIS价表和诊疗项目关系sql--"+select_clinic_price_sql);
//				}
			} catch (Throwable e) {
				errorNum++;
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表和诊疗项目关系，错误sql-"+insert_clinic_price_sql);
			} finally {
				try {
					if (connect != null) {
						connect.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item_v_price_list表"+ successNum+ "条，问题数据"+errorNum+"条");
	}
	
	public void insertClinicPriceList_direct(String logname, List<HisClinicItemPriceDTO> clinicPriceList) {
		Connection connect = null;
		int errorNum = 0;
		int successNum = 0;
		for(HisClinicItemPriceDTO clinicPrice : clinicPriceList) {
			String insert_clinic_price_sql = "";
			try {
				connect = jqm.getConnection();
				insert_clinic_price_sql = "insert into his_clinic_item_v_price_list([clinic_item_class],[clinic_item_code],[charge_item_no]"
						+ ",[charge_item_class],[charge_item_code],[charge_item_spec],"
						+ "[amount],[units],[backbill_rule],[create_date],[update_date],[body_part],[method])"
						+ " values("+quotedStr(clinicPrice.getClinic_item_class())+","+quotedStr(clinicPrice.getClinic_item_code())+","+quotedStr(clinicPrice.getCharge_item_no())
						+ ","+quotedStr(clinicPrice.getCharge_item_classs())+","+quotedStr(clinicPrice.getCharge_item_code())+","+quotedStr(clinicPrice.getCharge_item_spec())+","
						+clinicPrice.getAmount()+","+quotedStr(clinicPrice.getUnits())+",'',"+quotedStr(DateTimeUtil.getDateTime())+","+quotedStr(DateTimeUtil.getDateTime())+","+quotedStr(clinicPrice.getBody_part())+","+quotedStr(clinicPrice.getMethod())+")";
				//TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表和诊疗项目关系sql--"+insert_clinic_price_sql);
				connect.createStatement().executeUpdate(insert_clinic_price_sql);
				successNum++;
			} catch (Throwable e) {
				errorNum++;
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表和诊疗项目关系，错误sql-"+insert_clinic_price_sql);
			} finally {
				try {
					if (connect != null) {
						connect.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item_v_price_list表"+ successNum+ "条，问题数据"+errorNum+"条");
	}
	
	public void insertClinicList(String logname, List<HisClinicItem> clinicList) {
		Connection connect = null;
		int errorNum = 0;
		int successNum = 0;
		for(HisClinicItem clinic : clinicList) {
			String insert_clinic_sql = "";
			try {
				connect = jqm.getConnection();
				String select_clinic_sql = "select * from his_clinic_item where item_code = '"+clinic.getItem_code()+"'";
				ResultSet rs = connect.createStatement().executeQuery(select_clinic_sql);
				if(!rs.next()) {
					insert_clinic_sql = "insert into his_clinic_item([item_class],[item_code],[item_name],[input_code]"
							+ ",[expand1],[expand2],[expand3],[item_status] ,[create_date],[update_date]) "
							+ " values ("+quotedStr(clinic.getItem_class())+","+quotedStr(clinic.getItem_code())+","+quotedStr(clinic.getItem_name())+"," + quotedStr(clinic.getInput_code())+","
							+ quotedStr(clinic.getExpand1())+","+quotedStr(clinic.getExpand2())+","+quotedStr(clinic.getExpand3())+","+quotedStr(clinic.getItem_status())+","+quotedStr(DateTimeUtil.getDateTime())+","+quotedStr(DateTimeUtil.getDateTime())+")";
					//TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目sql--"+insert_clinic_sql);
					connect.createStatement().executeUpdate(insert_clinic_sql);
					successNum++;
				} else {
					TranLogTxt.liswriteEror_to_txt(logname, "未执行插入-HIS诊疗项目sql--"+select_clinic_sql);
				}
			} catch (Throwable e) {
				errorNum++;
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目，错误sql-"+insert_clinic_sql);
			} finally {
				try {
					if (connect != null) {
						connect.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item表"+ successNum+ "条，问题数据"+errorNum+"条");
	}
	
	public void insertClinicList_direct(String logname, List<HisClinicItem> clinicList) {
		Connection connect = null;
		int errorNum = 0;
		int successNum = 0;
		for(HisClinicItem clinic : clinicList) {
			String insert_clinic_sql = "";
			try {
				connect = jqm.getConnection();
				insert_clinic_sql = "insert into his_clinic_item([item_class],[item_code],[item_name],[input_code]"
						+ ",[expand1],[expand2],[expand3],[item_status] ,[create_date],[update_date]) "
						+ " values ("+quotedStr(clinic.getItem_class())+","+quotedStr(clinic.getItem_code())+","+quotedStr(clinic.getItem_name())+"," + quotedStr(clinic.getInput_code())+","
						+ quotedStr(clinic.getExpand1())+","+quotedStr(clinic.getExpand2())+","+quotedStr(clinic.getExpand3())+","+quotedStr(clinic.getItem_status())+","+quotedStr(DateTimeUtil.getDateTime())+","+quotedStr(DateTimeUtil.getDateTime())+")";
				//TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目sql--"+insert_clinic_sql);
				connect.createStatement().executeUpdate(insert_clinic_sql);
				successNum++;
			} catch (Throwable e) {
				errorNum++;
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目，错误sql-"+insert_clinic_sql);
			} finally {
				try {
					if (connect != null) {
						connect.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item表"+ successNum+ "条，问题数据"+errorNum+"条");
	}
	
	public void insertPriceList(String logname, List<HisPriceList> priceList) {
		Connection connect = null;
		int errorNum = 0;
		int successNum = 0;
		for(HisPriceList price : priceList) {
			String del_price_sql = "";
			try {
				connect = jqm.getConnection();
				del_price_sql = "delete his_price_list where item_code = '"+price.getItem_code()+"' ";
				connect.createStatement().executeUpdate(del_price_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧价表数据列表成功!");
			} catch (Throwable e) {
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "删除HIS价表，错误sql-"+del_price_sql);
			} finally {
				try {
					if (connect != null) {
						connect.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			String insert_price_sql = "";
			try {
				connect = jqm.getConnection();
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");  
				String Start_date; 
				if(price.getStart_date() ==null || price.getStart_date().equals("") ){
					Start_date="1901-01-01 00:00:00.000";
				}else{
					Start_date=sdf.format(price.getStart_date());
				}
				
				String Stop_date; 
				if(price.getStop_date() ==null || price.getStop_date().equals("") ){
					Stop_date="9999-12-31 23:59:59.000";
				}else{
					Stop_date = sdf.format(price.getStop_date());
				}
//				String select_clinic_price_sql = "select * from his_price_list where item_code = '"+price.getItem_code()+"' ";
//				if(!StringUtil.isEmpty(price.getItem_class())) {
//					select_clinic_price_sql += " and item_class = '"+price.getItem_class()+"'";
//				}
//				if(!StringUtil.isEmpty(price.getItem_spec())) {
//					select_clinic_price_sql += " and item_spec = '"+price.getItem_spec()+"'";
//				}
//				if(!StringUtil.isEmpty(price.getUnits())) {
//					select_clinic_price_sql += " and units = '"+price.getUnits()+"'";
//				}
			//	GETDATE(),'9999-12-31 23:59:59.000'
//				ResultSet rs = connect.createStatement().executeQuery(select_clinic_price_sql);
//				if(!rs.next()) {
					insert_price_sql = "insert into his_price_list([item_class],[item_code],[item_name],[item_spec],[units],[price]"
							+ ",[prefer_price],[performed_by],[input_code],[class_on_inp_rcpt],[class_on_outp_rcpt],[class_on_reckoning]"
							+ ",[subj_code],[memo],[start_date],[stop_date],[create_date],[update_date]) "
							+ "values("+quotedStr(price.getItem_class())+","+quotedStr(price.getItem_code())+","+quotedStr(price.getItem_name())+","+quotedStr(price.getItem_spec())+","+quotedStr(price.getUnits())+","+quotedStr(price.getPrice())+""
							+ ","+quotedStr(price.getPrefer_price())+","+quotedStr(price.getPerformed_by())+","+quotedStr(price.getInput_code())+","+quotedStr(price.getClass_on_inp_rcpt())+","+quotedStr(price.getClass_on_outp_rcpt())+","+quotedStr(price.getClass_on_reckoning())+""
							+ ","+quotedStr(price.getSubj_code())+","+quotedStr(price.getMemo())+","+quotedStr(Start_date)+","+quotedStr(Stop_date)+","+quotedStr(DateTimeUtil.getDateTime())+","+quotedStr(DateTimeUtil.getDateTime())+")";
					//TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表sql--"+insert_price_sql);
					TranLogTxt.liswriteEror_to_txt(logname, "未执行插入-HIS价表sql--"+insert_price_sql);
					connect.createStatement().executeUpdate(insert_price_sql);
					successNum++;
//				} else {
//					TranLogTxt.liswriteEror_to_txt(logname, "未执行插入-HIS价表sql--"+select_clinic_price_sql);
//				}
			} catch (Throwable e) {
				errorNum++;
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表，错误sql-"+insert_price_sql);
			} finally {
				try {
					if (connect != null) {
						connect.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入insert_price_sql表"+ successNum+ "条，问题数据"+errorNum+"条");
	}
	
	public void insertPriceList_direct(String logname, List<HisPriceList> priceList) {
		Connection connect = null;
		int errorNum = 0;
		int successNum = 0;
		for(HisPriceList price : priceList) {
			String insert_price_sql = "";
			try {
				connect = jqm.getConnection();
				String stop_date = "";
				if(price.getStop_date() == null) {
					stop_date = "'9999-12-31 23:59:59'";
				} else {
					stop_date = quotedStr(price.getStop_date());
				}
				insert_price_sql = "insert into his_price_list([item_class],[item_code],[item_name],[item_spec],[units],[price]"
						+ ",[prefer_price],[performed_by],[input_code],[class_on_inp_rcpt],[class_on_outp_rcpt],[class_on_reckoning]"
						+ ",[subj_code],[memo],[start_date],[stop_date],[create_date],[update_date]) "
						+ "values("+quotedStr(price.getItem_class())+","+quotedStr(price.getItem_code())+","+quotedStr(price.getItem_name())+","+quotedStr(price.getItem_spec())+","+quotedStr(price.getUnits())+","+quotedStr(price.getPrice())+""
						+ ","+quotedStr(price.getPrefer_price())+","+quotedStr(price.getPerformed_by())+","+quotedStr(price.getInput_code())+","+quotedStr(price.getClass_on_inp_rcpt())+","+quotedStr(price.getClass_on_outp_rcpt())+","+quotedStr(price.getClass_on_reckoning())+""
						+ ","+quotedStr(price.getSubj_code())+","+quotedStr(price.getMemo())+","+quotedStr(price.getStart_date())+","+stop_date+","+quotedStr(DateTimeUtil.getDateTime())+","+quotedStr(DateTimeUtil.getDateTime())+")";
				//TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表sql--"+insert_price_sql);
				connect.createStatement().executeUpdate(insert_price_sql);
				successNum++;
			} catch (Throwable e) {
				errorNum++;
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表，错误sql-"+insert_price_sql);
			} finally {
				try {
					if (connect != null) {
						connect.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入insert_price_sql表"+ successNum+ "条，问题数据"+errorNum+"条");
	}
	
	public void updatePriceList(String logname, String price_code, String price) {
		Connection connect = null;
		String update_price_sql = "";
		try {
			connect = jqm.getConnection();
			update_price_sql = "update his_price_list set price="+price+",prefer_price="+price+" where item_code = '"+price_code+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "更新HIS价表sql--"+update_price_sql);
			connect.createStatement().executeUpdate(update_price_sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "更新HIS价表，错误sql-"+update_price_sql);
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功更新update_price_sql表");
	}
	
	public void delete_his_data(String logname) {
		Connection connect = null;
		try {
			connect = jqm.getConnection();
			
			String del_clinic_sql = "delete his_clinic_item";
			connect.createStatement().executeUpdate(del_clinic_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目数据列表成功!");
			
			String del_clinic_price_sql = "delete his_clinic_item_v_price_list";
			connect.createStatement().executeUpdate(del_clinic_price_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目和价表关系数据列表成功!");
			
			String del_price_sql = "delete his_price_list";
			connect.createStatement().executeUpdate(del_price_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧价表数据列表成功!");
		} catch (SQLException e) {
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {
				if (connect != null){
					connect.close();
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	public static String quotedStr(Object str) {
    	if(str == null) return null;
    	return "'"+str.toString().replaceAll("'", "''")+"'";
    }

	public void jdbcsaveChargingSummarySingle(ChargingSummarySingle css) {
		
		String sql="insert into charging_summary_single (exam_id,req_num,charging_status,amount1,amount2,discount,cashier,cash_date,is_print_recepit,is_active,creater,create_time,updater,update_time) "
				+ "values ('"+css.getExam_id()+"','"+css.getReq_num()+"','"+css.getCharging_status()+"','"+css.getAmount1()+"','"+css.getAmount2()+"','"+css.getDiscount()+"','"+css.getCashier()+"',"
				+ "'"+DateTimeUtil.shortFmt2(css.getCash_date())+"','"+css.getIs_print_recepit()+"','"+css.getIs_active()+"','"+css.getCreater()+"','"+DateTimeUtil.shortFmt2(css.getCreate_time())+"','"+css.getUpdater()+"','"+DateTimeUtil.shortFmt2(css.getUpdate_time())+"')";
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}


	public void jdbcsaveChargingDetailSingle(ChargingDetailSingle cds) {
		/*ChargingDetailSingle chargingDetailSingle = new ChargingDetailSingle();
		chargingDetailSingle.setAmount(item.getItem_amount());
		chargingDetailSingle.setAmount1(item.getPersonal_pay());
		chargingDetailSingle.setCharging_item_id(item.getCharge_item_id());
		chargingDetailSingle.setDiscount(item.getDiscount());
		chargingDetailSingle.setCreater(user.getUserid());
		chargingDetailSingle.setCreate_time(DateTimeUtil.parse());
		chargingDetailSingle.setUpdater(user.getUserid());
		chargingDetailSingle.setUpdate_time(DateTimeUtil.parse());*/
		
		String sql="insert into charging_detail_single(amount,amount1,charging_item_id,discount,creater,create_time,updater,update_time,summary_id)  values  "
				+ "('"+cds.getAmount()+"','"+cds.getAmount1()+"','"+cds.getCharging_item_id()+"','"+cds.getDiscount()+"','"+cds.getCreater()+"',"
				+ "'"+DateTimeUtil.shortFmt2(cds.getCreate_time())+"','"+cds.getUpdater()+"','"+DateTimeUtil.shortFmt2(cds.getUpdate_time())+"','"+cds.getSummary_id()+"')";
		Connection connection = null;
		try {
			connection = this.jqm.getConnection();
			connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	@Override
	public ExaminfoChargingItemDTO getExaminfoChargingItem(long examinfo_id,long charge_item_id,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select * ");
		sb.append(" from examinfo_charging_item ec ");
		sb.append(" where ec.isActive='Y' ");		
		sb.append(" and ec.examinfo_id = '" + examinfo_id + "' and charge_item_id = '"+charge_item_id+"' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jqm.getList(sb.toString(), 1, 10000, ExaminfoChargingItemDTO.class);
		ExaminfoChargingItemDTO ec = new ExaminfoChargingItemDTO();
		if((map!=null)&&(map.getList().size()>0)){
			ec= (ExaminfoChargingItemDTO)map.getList().get(0);			
		}
		return ec;
	}
	
	/**
	 * 插入exam_queue_log表
	 * @param exam_num
	 * @param queueNo
	 * @param logname
	 * @return
	 */
	public boolean selectExamQueueLog(String exam_num,String logname){
		Connection tjtmpconnect = null;
		boolean tjvip=false;
		try {
			tjtmpconnect = jqm.getConnection();
			String sb1 = "select * from exam_queue_log where exam_num = '"+exam_num+"'";//每个体检号在exam_queue_log表只对应1条，无论哪天。
//					+ " and queue_day = '"+DateTimeUtil.getDate()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "查询exam_queue_log表SQL:" +sb1);				
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			if(rs.next()) {
				tjvip=true;
			}
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:  插入数据操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return tjvip;
	}
	
	public boolean updateExamQueueLog(String queue_no, String exam_num,String logname) throws ServiceException {
		Connection connect = null;
		int resflag = -1;
		try {
			connect = jqm.getConnection();
			String sb1 = "update exam_queue_log set queue_no = '"+queue_no+"' where exam_num = '"+exam_num+"' and queue_day = '"+DateTimeUtil.getDate()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "更新exam_queue_log表SQL:" +sb1);				
			resflag = connect.createStatement().executeUpdate(sb1);
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:  插入数据操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return resflag>0;
	}
	
	public boolean hasItem(long examinfo_id,String logname) {
		boolean hasItem = false;
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = jqm.getConnection();
			String sql = "SELECT ci.item_name "
						+ "FROM charging_item ci,examinfo_charging_item eci, exam_info ei "
						+ " where eci.charge_item_id = ci.id and ei.id = eci.examinfo_id "
						+ " and ( eci.pay_status IN ('Y', 'R') OR ( ei.is_after_pay = 'Y' AND eci.pay_status IN ('Y', 'R', 'N'))) "
						+ " AND eci.isActive = 'Y' AND ci.item_category != '耗材类型' AND ei.id = "+examinfo_id;
			TranLogTxt.liswriteEror_to_txt(logname, "res:SQL操作语句==： " +sql);
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sql);
			if (rs.next()) {
				hasItem = true;
			}
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "查询异常===:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return hasItem;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public WebserviceConfigurationDTO getWebServiceConfig(String configKey) throws ServiceException {
		String sql ="select * from webservice_configuration where config_key = '"+configKey+"'";
		List<WebserviceConfigurationDTO> list = this.jqm.getList(sql, WebserviceConfigurationDTO.class);
		if(list.size() != 0){
			return list.get(0);
		}
		return null;
	}
	
	//此接口只发送团检的项目
	public String paymentApplicationT(String examNum, UserDTO user) throws ServiceException {
		//查询webservice地址
		WebserviceConfigurationDTO wcf=new WebserviceConfigurationDTO();
		wcf=getWebServiceConfig("PAYMENT_APPLICATION");
		//根据体检号查询体检信息
		List<ExamInfo> examList = this.jqm.getList("select * from exam_info e where e.exam_num = '"+examNum+"'", ExamInfo.class);
		if(examList.size() == 0){
			return "error-该体检信息不存在!";
		}
		ExamInfo examInfo = examList.get(0);
		String IS_HIS_EXAMBRID_CHECK = this.getCenterconfigByKey("IS_HIS_EXAMBRID_CHECK").getConfig_value().trim();
		if ("Y".equals(IS_HIS_EXAMBRID_CHECK)) {
			if(examInfo.getVisit_no() == null || "".equals(examInfo.getVisit_no())
				|| examInfo.getVisit_date() == null || "".equals(examInfo.getVisit_date())){
				return "error-没有就诊卡号或就诊日期，不能发送缴费申请!";
			}
		}
		
//		//查询人员信息
		List<CustomerInfo> cusList = this.jqm.getList("select * from customer_info c where c.id = "+examInfo.getCustomer_id(), CustomerInfo.class);
		CustomerInfo customerInfo = cusList.get(0);
		String sql = "select * from examinfo_charging_item ec where "
				+ "ec.isActive = 'Y' and ec.his_req_status = 'N' and ec.examinfo_id = "+examInfo.getId()
				+ " and ec.exam_indicator = 'T' and ec.pay_status = 'R'";
		
		TranLogTxt.liswriteEror_to_txt("reqFee", "根据体检id查询需要申请的项目:"+sql);
		//根据体检号查询需要申请的项目
		List<ExaminfoChargingItem> itemList = this.jqm.getList(sql, ExaminfoChargingItem.class);
		if(itemList.size() == 0){
			return "error-该体检者不存在需要发缴费申请的项目!";
		}
		
		Double amount = 0.0;  //计算总金额
		List<Fee> feeList = new ArrayList<Fee>(); //发送申请项目集合
		List<ChargingDetailSingle> detailList = new ArrayList<ChargingDetailSingle>();  //结算明细
		
		for(ExaminfoChargingItem item : itemList) {
			String charging_item_sql = "select * from charging_item where id = "+item.getCharge_item_id();
			List list = this.jqm.getList(charging_item_sql, ChargingItem.class);
			ChargingItem charitem = (ChargingItem)list.get(0);
//			ChargingItem charitem = (ChargingItem) this.qm.load(ChargingItem.class,item.getCharge_item_id()); 
			
			amount += item.getPersonal_pay();
			//组装明细表
			ChargingDetailSingle chargingDetailSingle = new ChargingDetailSingle();
			chargingDetailSingle.setAmount(item.getItem_amount());
			chargingDetailSingle.setAmount1(item.getPersonal_pay());
			chargingDetailSingle.setCharging_item_id(item.getCharge_item_id());
			chargingDetailSingle.setDiscount(item.getDiscount());
			chargingDetailSingle.setCreater(user.getUserid());
			chargingDetailSingle.setCreate_time(DateTimeUtil.parse());
			chargingDetailSingle.setUpdater(user.getUserid());
			chargingDetailSingle.setUpdate_time(DateTimeUtil.parse());
			detailList.add(chargingDetailSingle);
			
			String HIS_FEE_TYPE = this.getCenterconfigByKey("HIS_FEE_TYPE").getConfig_value();
			if(HIS_FEE_TYPE != null && "1".equals(HIS_FEE_TYPE.trim())){
				Fee fee = new Fee();				
				fee.setPATIENT_ID(examInfo.getPatient_id());
				fee.setEXAM_NUM(examInfo.getExam_num());
				fee.setVISIT_DATE(examInfo.getVisit_date());
				fee.setVISIT_NO(examInfo.getVisit_no());
				fee.setSERIAL_NO("");
				fee.setORDER_CLASS("");
				fee.setORDER_NO("1");
				fee.setORDER_SUB_NO("1");
				fee.setITEM_NO("1");
				fee.setITEM_CLASS("");
				fee.setITEM_NAME(charitem.getItem_name());
				fee.setITEM_CODE(charitem.getHis_num());
				fee.setITEM_SPEC("");
				fee.setUNITS("");
				fee.setREPETITION("1");
				
				fee.setORDERED_BY_DEPT(this.getDatadis("SQKS").get(0).getRemark());
				fee.setORDERED_BY_DOCTOR(user.getName());
				fee.setUSER_NAME(user.getWork_num());
				fee.setPERFORMED_BY(charitem.getPerform_dept());
				fee.setCLASS_ON_RCPT("");
				fee.setITEM_PRICE(charitem.getAmount().toString());
				fee.setRCPT_NO("");
				fee.setCHARGE_INDICATOR("0");
				fee.setCLASS_ON_RECKONING("");
				fee.setSUBJ_CODE("");
				fee.setPRICE_QUOTIETY("");
				fee.setCLINIC_NO(examInfo.getClinic_no());
				fee.setBILL_DATE("");
				fee.setBILL_NO("");
				fee.setSKINTEST("");// 皮试标志
				fee.setPRESC_PSNO(""); //皮试结果
				fee.setINSURANCE_FLAG("0"); //适用医保内外标志：0自费，1医保，2免费
				fee.setINSURANCE_CONSTRAINED_LEVEL("");// 公费用药级别
				fee.setSKIN_SAVE("");//皮试记录时间
				fee.setSKIN_START("");//皮试时间
				fee.setSKIN_BATH("");//药品批号
				fee.setCHARGES(item.getPersonal_pay()+"");//实收金额
				fee.setExam_chargeItem_code(charitem.getItem_code());
				
				if("T".equals(item.getExam_indicator())) {
					fee.setCHARGE_INDICATOR("1");
				}
				
				feeList.add(fee);
			}else{
				String sql1 ="select distinct c.item_class as item_class_c,c.item_code as item_code_c,c.item_name as item_name_c,c.item_status,"
						+" c.input_code as input_code_c,c.expand1,c.expand2,c.expand3,p.item_class as item_class_p,p.item_code as item_code_p,"
						+" p.item_name as item_name_p,p.input_code as input_code_p,p.item_spec,p.units,p.price,p.prefer_price,p.performed_by,"
						+" p.class_on_inp_rcpt,p.class_on_outp_rcpt,p.class_on_reckoning,p.subj_code,p.memo,cp.*"
						+" from his_clinic_item c,his_clinic_item_v_price_list cp,his_price_list p,charging_item i"
						+" where c.item_code = cp.clinic_item_code and p.item_code = cp.charge_item_code and c.item_code = i.his_num "
						+" and c.item_class = i.item_class and c.item_class = cp.clinic_item_class and cp.charge_item_class = p.item_class"
						+" and i.id = "+item.getCharge_item_id()+" and i.item_class = '"+charitem.getItem_class()+"'";
				if(examInfo.getVisit_date() != null && !"".equals(examInfo.getVisit_date())){
					sql1 += " and  '"+examInfo.getVisit_date()+"'>=p.start_date   and  '"+examInfo.getVisit_date()+"'<=p.stop_date ";
				}
				
				if(!"5".equals(wcf.getConfig_method())){
					sql1 += " and cp.charge_item_spec = p.item_spec and cp.units = p.units";
				}
				
				TranLogTxt.liswriteEror_to_txt("reqFee", "根据项目信息查询价表数据:"+sql1);
				List<HisClinicItemPriceListDTO> hisList = this.jqm.getList(sql1, HisClinicItemPriceListDTO.class);
				
				if(hisList.size() == 0){
					TranLogTxt.liswriteEror_to_txt("reqFee", "error-收费项目"+charitem.getItem_name()+"找不到HIS价表信息!");
					return "error-收费项目"+charitem.getItem_name()+"找不到HIS价表信息!";
				}
				
				for(HisClinicItemPriceListDTO hiscp : hisList){
					Fee fee = new Fee();				
					fee.setPATIENT_ID(examInfo.getPatient_id());
					fee.setEXAM_NUM(examInfo.getExam_num());
					fee.setVISIT_DATE(examInfo.getVisit_date());
					fee.setVISIT_NO(examInfo.getVisit_no());
					fee.setSERIAL_NO("");
					fee.setORDER_CLASS(hiscp.getItem_class_c());
					fee.setORDER_NO("1");
					fee.setORDER_SUB_NO("1");
					fee.setITEM_NO("1");
					fee.setITEM_CLASS(hiscp.getItem_class_p());
					fee.setITEM_NAME(hiscp.getItem_name_p());
					fee.setITEM_CODE(hiscp.getItem_code_p());
					fee.setITEM_SPEC(hiscp.getItem_spec());
					fee.setUNITS(hiscp.getUnits());
					fee.setREPETITION("1");
					
					fee.setORDERED_BY_DEPT(this.getDatadis("SQKS").get(0).getRemark());
					fee.setORDERED_BY_DOCTOR(user.getName());
					fee.setUSER_NAME(user.getWork_num());
					fee.setPERFORMED_BY(charitem.getPerform_dept());
					fee.setCLASS_ON_RCPT(hiscp.getCharge_item_class());
					fee.setITEM_PRICE(hiscp.getPrice().toString());
					fee.setRCPT_NO("");
					fee.setCHARGE_INDICATOR("0");
					fee.setCLASS_ON_RECKONING(hiscp.getClass_on_reckoning());
					fee.setSUBJ_CODE(hiscp.getSubj_code());
					fee.setPRICE_QUOTIETY("");
					fee.setITEM_PRICE(hiscp.getPrice().toString());
					fee.setCLINIC_NO(examInfo.getClinic_no());
					fee.setBILL_DATE("");
					fee.setBILL_NO("");
					fee.setSKINTEST("");// 皮试标志
					fee.setPRESC_PSNO(""); //皮试结果
					fee.setINSURANCE_FLAG("0"); //适用医保内外标志：0自费，1医保，2免费
					fee.setINSURANCE_CONSTRAINED_LEVEL("");// 公费用药级别
					fee.setSKIN_SAVE("");//皮试记录时间
					fee.setSKIN_START("");//皮试时间
					fee.setSKIN_BATH("");//药品批号
					fee.setExam_chargeItem_code(charitem.getItem_code());
					
					if("1".equals(wcf.getConfig_method().trim())){//东北国际
						fee.setAMOUNT(hiscp.getAmount().toString());
						double charges = hiscp.getAmount()*hiscp.getPrice();
						fee.setCHARGES(charges+"");//实收金额
						fee.setCOSTS(charges+"");  //计价金额
					}else{
						fee.setAMOUNT(hiscp.getAmount().toString());
						double charges = hiscp.getAmount()*hiscp.getPrice();
						fee.setCHARGES(charges+"");//实收金额
						fee.setCOSTS(charges+"");  //计价金额
					}
					
					if("T".equals(item.getExam_indicator())) {
						fee.setCHARGE_INDICATOR("1");
					}
					
					feeList.add(fee);
				}
			}
		}
		TranLogTxt.liswriteEror_to_txt("reqFee","组装数据成功...");
		
		//保存个人收费总表记录
		ChargingSummarySingle chargingSummarySingle = new ChargingSummarySingle();
		
		chargingSummarySingle.setExam_id(examInfo.getId());
		chargingSummarySingle.setReq_num(GetNumContral.getInstance().getParamNum("rcpt_num"));
		chargingSummarySingle.setCharging_status("R");//预付费为R
		chargingSummarySingle.setAmount1(Double.parseDouble(String.format("%.2f", amount)));
		chargingSummarySingle.setAmount2(Double.parseDouble(String.format("%.2f", amount)));
		chargingSummarySingle.setDiscount(10.00);
		chargingSummarySingle.setCashier(user.getUserid());
		chargingSummarySingle.setCash_date(DateTimeUtil.parse());
		chargingSummarySingle.setIs_print_recepit("N");
		chargingSummarySingle.setIs_active("Y");
		chargingSummarySingle.setCreater(user.getUserid());
		chargingSummarySingle.setCreate_time(DateTimeUtil.parse());
		chargingSummarySingle.setUpdater(user.getUserid());
		chargingSummarySingle.setUpdate_time(DateTimeUtil.parse());
		chargingSummarySingle.setDaily_status("0");
		
		this.jdbcsaveChargingSummarySingle(chargingSummarySingle);  //保存收费总表
		
		for(ChargingDetailSingle chargingDetailSingle : detailList){//遍历保存明细数据
			chargingDetailSingle.setSummary_id(chargingSummarySingle.getId());
			this.jdbcsaveChargingDetailSingle(chargingDetailSingle);
		}
		TranLogTxt.liswriteEror_to_txt("reqFee","保存收费表成功...");
		
		//开始发送申请
		
		FeeMessage fm = new FeeMessage();
		fm.setREQ_NO(chargingSummarySingle.getReq_num());
		Fees fees = new Fees();
		fees.setPROJECT(feeList);
		fm.setPROJECTS(fees);
		
		FEESendMessage fsm= new FEESendMessage(fm);		
		FeeResultBody frb= new FeeResultBody();
		TranLogTxt.liswriteEror_to_txt("reqFee","准备发送收费申请...");
		frb = fsm.feeSend(wcf.getConfig_url().trim(),wcf.getConfig_method().trim(), true);
		TranLogTxt.liswriteEror_to_txt("reqFee","收费申请返回："+frb.getResultHeader().getText());
		System.out.println(frb.getResultHeader().getTypeCode() + "-" + frb.getResultHeader().getText());
		
		if("AA".equals(frb.getResultHeader().getTypeCode())){//申请发送成功
			for(ExaminfoChargingItem examitem : itemList){
				String update_sql = "update examinfo_charging_item set his_req_status = 'Y' where id = "+examitem.getId();
				jpm.executeSql(update_sql);
			}
			return "ok-缴费申请发送成功!";
		}else{
			throw new ServiceException(frb.getResultHeader().getText());
		}
	}
	
	public List<JobDTO> getDatadis(String data_code)throws ServiceException {
		String sqltext = "select id,data_name,remark,data_code_children from data_dictionary where data_code='" + data_code
				+ "' and isActive='Y' order by seq_code";
		Connection connection = null;
		Statement statement = null;
		int count = 0;
		List<JobDTO> list = new ArrayList<JobDTO>();
		try {
			// 读取记录数
			connection = this.jqm.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sqltext);
			while (rs.next()) {
				JobDTO jd =new JobDTO();
				jd.setId(rs.getString("id"));
				jd.setName(rs.getString("data_name"));
				jd.setRemark(rs.getString("remark"));
				jd.setData_code_children(rs.getString("data_code_children"));
				list.add(jd);
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return list;
	}
	
	public String getDep_inter_num(String logname, String dep_num) {
		String dep_inter_num = "";
		try {
			String sql = "select dep_inter_num from department_dep where dep_num = '"+dep_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "sql:"+sql);
			RowSet rs = jqm.getRowSet(sql);
			if(rs.next()) {
				dep_inter_num = rs.getString("dep_inter_num");
			}
			rs.close();
		} catch (Throwable e) {
			e.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		return dep_inter_num;
	}
	@Override
	public boolean getHisPriceList(String logname, String price_code, String itemClass) {
		Connection tjtmpconnect = null;
		boolean pricecount=false;
		try {
			tjtmpconnect = jqm.getConnection();
			String sb1 = " select * from his_price_list where item_code='"+price_code+"' and item_class='"+itemClass+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "查询his_price_list表SQL:" +sb1);				
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			if(rs.next()) {
				pricecount=true;
			}
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:查询his_price_list表SQL失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return pricecount;
	}
	
	
	@Override
	public boolean getHisClinicItem(String logname, String item_code, String itemClass,String inputcode) {
		Connection tjtmpconnect = null;
		boolean pricecount=false;
		try {
			tjtmpconnect = jqm.getConnection();
			String sb1 = " select * from his_clinic_item where item_code='"+item_code+"' and item_class='"+itemClass+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "查询his_clinic_item表SQL:" +sb1);				
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			if(rs.next()) {
				pricecount=true;
			}
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:查询hhis_clinic_item表SQL失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return pricecount;
	}
	@Override
	public void updateClinicList(String logname, String clinic_code, String clinic_itemclass,String clinic_input_code) {
		Connection connect = null;
		String update_clinic_sql = "";
		try {
			connect = jqm.getConnection();
			update_clinic_sql = " update his_clinic_item set item_class='"+clinic_itemclass+"',input_code='"+clinic_input_code+"' where item_code = '"+clinic_code+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "更新HIS诊疗sql--"+update_clinic_sql);
			connect.createStatement().executeUpdate(update_clinic_sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "更新HIS诊疗，错误sql-"+update_clinic_sql);
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功更新update_clinic_sqll表");
		
	}
	
	
	@Override
	public void updateClinicListTT(String logname, String clinic_code, String clinic_itemclass,String clinic_input_code,String clinic_item_stats,String price,String item_name) {
		Connection connect = null;
		String update_clinic_sql = "";
		try {
			connect = jqm.getConnection();
			update_clinic_sql = " update his_clinic_item set item_class='"+clinic_itemclass+"',input_code='"+clinic_input_code+"',item_status='"+clinic_item_stats+"',item_name='"+item_name+"',expand1='"+price+"' where item_code = '"+clinic_code+"' ";
		
			TranLogTxt.liswriteEror_to_txt(logname, "更新HIS诊疗sql--"+update_clinic_sql);
			connect.createStatement().executeUpdate(update_clinic_sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "更新HIS诊疗，错误sql-"+update_clinic_sql);
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功更新update_clinic_sqll表");
		
	}
	
	@Override
	public boolean getHisClinicItemVPriceList(String logname, String clinicItemClass, String clinicItemCode,String chargeItemclass, String chargeItemCode) {
		Connection tjtmpconnect = null;
		boolean clinicpricecount=false;
		try {
			tjtmpconnect = jqm.getConnection();
			String sb1 = " select  * from his_clinic_item_v_price_list where clinic_item_class='"+clinicItemClass+"' and clinic_item_code='"+clinicItemCode+"' and charge_item_class='"+chargeItemclass+"' and charge_item_code='"+chargeItemCode+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "查询his_clinic_item_v_price_list表SQL:" +sb1);				
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			if(rs.next()) {
				clinicpricecount=true;
			}
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:查询his_clinic_item_v_price_list表SQL失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return clinicpricecount;
	}
	@Override
	public void updateHisClinicItemVPriceList(String logname, String clinicItemClass, String clinicItemCode,String chargeItemclass, String chargeItemCode,String amount,String units) {
		Connection connect = null;
		String update_ClinicItemVPrice = "";
		try {
			connect = jqm.getConnection();
			update_ClinicItemVPrice = " update his_clinic_item_v_price_list set amount='"+amount+"',units='"+units+"' where clinic_item_class='"+clinicItemClass+"' and clinic_item_code='"+clinicItemCode+"' and charge_item_class='"+chargeItemclass+"' and charge_item_code='"+chargeItemCode+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "更新HIS诊疗和价表关系表sql--"+update_ClinicItemVPrice);
			connect.createStatement().executeUpdate(update_ClinicItemVPrice);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "更新HIS诊疗和价表关系表，错误sql-"+update_ClinicItemVPrice);
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功更新update_ClinicItemVPrice表");
		
	}
}
