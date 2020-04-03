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
public class FEESendMessageZL2 {

	private FeeMessage feeMessage;
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

	public FEESendMessageZL2(FeeMessage feeMessage) {
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
			 float ysje=0;
			 float ssje=0;
			 String exam_num="";
			 String CLINIC_NO="";
			 ExamInfoUserDTO ei= new ExamInfoUserDTO();
			 String ORDERED_BY_DEPT="";
			 String ORDERED_BY_DOCTOR="";
			 String PERFORMED_BY="";
			 String czy="";
			 String doctor = "";
			try {
				FeeRes f = new FeeRes();
				for (Fee fee : this.feeMessage.getPROJECTS().getPROJECT()) {
					
					ei=this.getExamInfoForNum(fee.getEXAM_NUM());
					PATIENT_ID=fee.getPATIENT_ID();
					username=fee.getUSER_NAME();
					exam_num=fee.getEXAM_NUM();
					CLINIC_NO=fee.getVISIT_NO();
						String req_no = feeMessage.getREQ_NO();
						String sqNo = fee.getITEM_NO();
						doctor = fee.getORDERED_BY_DOCTOR();
                         ysje=ysje+Float.valueOf(fee.getAMOUNT());
                         ssje=ssje+Float.valueOf(fee.getCHARGES());
                         ORDERED_BY_DEPT=fee.getORDERED_BY_DEPT();
                         ORDERED_BY_DOCTOR=fee.getORDERED_BY_DOCTOR();
                         PERFORMED_BY=fee.getPERFORMED_BY();
                         czy=fee.getUSER_NAME();
                         itemCodeList.add(req_no);
         				
         				f.setCHARGES(fee.getCHARGES());
         				//f.setExam_chargeItem_code(chargingcode);
         				f.setEXAM_NUM(fee.getEXAM_NUM());
         				f.setFEE_CODE(fee.getITEM_CODE());
         				f.setPATIENT_ID(fee.getPATIENT_ID());
					}
				
				String czyid= configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//操作员
				String czyxm= configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//操作员
				
				TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_保存费用记录_Insert");
				CallableStatement c = connect.prepareCall("{call "+table+".zl_hjwtj_保存费用记录_Insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				TranLogTxt.liswriteEror_to_txt(logname,exam_num + "-zl_hjwtj_保存费用记录_Insert('"+cfh+"','" + 1 + "','" 
				+ PATIENT_ID + "','" + CLINIC_NO + "','" 
						+ ei.getUser_name() + "','" + ei.getSex() + "','" + ei.getAge() + "','普通','"
								+ORDERED_BY_DEPT+"','"+ORDERED_BY_DEPT
								+"','"+czyxm+"','177907','','',1,1,'"+ORDERED_BY_DEPT+"','" + ssje + "','"
								+ ssje + "','" + ssje + "'" + ",'" +new java.sql.Date(System.currentTimeMillis())+ "','" 
								+ new java.sql.Date(System.currentTimeMillis()) + "','',0,'',0,'','"
								+ czyxm + "','"+czyid+"',?)");
				c.setString(1, cfh);
				c.setFloat(2, 1);
				c.setFloat(3, Float.valueOf(PATIENT_ID));
				c.setFloat(4, Float.valueOf(CLINIC_NO));
				c.setString(5, ei.getUser_name());
				c.setString(6, ei.getSex());
				c.setString(7, ei.getAge()+"");
				c.setString(8, "普通");
				c.setFloat(9,Float.valueOf(ORDERED_BY_DEPT));
				c.setFloat(10,Float.valueOf(ORDERED_BY_DEPT));
				c.setString(11, czyxm);
				c.setFloat(12, 177907);
				c.setString(13,"");
				c.setString(14, "");
				c.setFloat(15,1);
				c.setFloat(16, 1);
				c.setFloat(17, Float.valueOf(ORDERED_BY_DEPT));
				c.setFloat(18, Float.valueOf(ssje));
				c.setFloat(19, Float.valueOf(ssje));
				c.setFloat(20, Float.valueOf(ssje));
				java.sql.Date datetime=new java.sql.Date(System.currentTimeMillis());
				c.setDate(21, datetime);
				c.setDate(22, datetime);
				c.setString(23,"");
				c.setFloat(24,0);
				
				c.setString(25,"");
				c.setFloat(26,0);
				c.setString(27,"");	
				c.setString(28, czyxm);
				c.setString(29, czyid);
				// 执行存储过程
				c.registerOutParameter(30, java.sql.Types.FLOAT);
				// 执行存储过程
				c.execute();
				float reqs = c.getInt(30);
				c.close();
				TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_保存费用记录_Insert  返回："+reqs);
				if(reqs==0){				
				  feeitem.add(f);
				}
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("");
				rqid.setFeeitem(feeitem);
				rb.getControlActProcess().getList().add(rqid);
				rb.setItemCodeList(itemCodeList);	
			} catch (Exception ex) {				
					
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("费用信息解析返回值错误");
			}
			
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
 
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			String table = url.split("&")[3];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用函数 "+table+".zl_hjwtj_get获取处方号");
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
