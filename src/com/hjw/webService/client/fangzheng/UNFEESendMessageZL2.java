package com.hjw.webService.client.fangzheng;

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
import com.hjw.webService.client.Bean.FeeReqControlActProcess;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.17	收费退费
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class UNFEESendMessageZL2 {

	private UnFeeMessage feeMessage;
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
	
	public UNFEESendMessageZL2(UnFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url, String logname) {
		FeeReqBody rb = new FeeReqBody();
		Connection connect = null;
		String dburl = url.split("&")[0];
		String user = url.split("&")[1];
		String passwd = url.split("&")[2];
		String table = url.split("&")[3];
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
		String xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getEXAM_NUM() + ":" + xml);
		try {
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu = this.getExamInfoForNum(this.feeMessage.getEXAM_NUM(), logname);
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			List<ReqNo> okList = new ArrayList<ReqNo>();
			for (String reqNo : this.feeMessage.getREQ_NOS().getREQ_NO()) {
				CallableStatement c = connect.prepareCall("{call "+table+".zl_hjwtj_获取收费状态(?,?,?,?,?)}");
				TranLogTxt.liswriteEror_to_txt(logname,
						"res:-zl_hjwtj_获取收费状态('" + eu.getPatient_id() + "','" + eu.getVisit_no() + "','" + reqNo + "',?)");
				c.setLong(1, Long.valueOf(eu.getPatient_id()));
				c.setLong(2, Long.valueOf(eu.getVisit_no()));
				c.setString(3, reqNo);
				c.setString(4, "");
				// 执行存储过程
				c.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
				// 执行存储过程
				c.executeUpdate();
				ResultSet rs =(ResultSet) c.getObject(5);
				String sfzt = "";
				if (rs.next()) {
					sfzt = rs.getString("收费状态");					
				}
				TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_获取收费状态 返回"+sfzt);
				rs.close();
				c.close();
				if ("1".equals(sfzt)) {
					String czyxm = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();// 操作员
					TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_确认费用取消");
					c = connect.prepareCall("{call "+table+".zl_hjwtj_确认费用取消(?,?,?,?)}");
					TranLogTxt.liswriteEror_to_txt(logname,
							"res:-zl_hjwtj_确认费用取消('" + reqNo + "','" + eu.getPatient_id() + "','" + czyxm + "','"+new java.sql.Date(System.currentTimeMillis())+"')");
					c.setString(1, reqNo);
					c.setLong(2, Long.valueOf(eu.getPatient_id()));
					c.setString(3, czyxm);
					c.setDate(4, new java.sql.Date(System.currentTimeMillis()));
					// 执行存储过程
					c.execute();
					TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程 zl_hjwtj_确认费用取消  调用成功");
					// 执行存储过程
					c.close();
						ReqNo req = new ReqNo();
						req.setREQ_NO(reqNo);
						okList.add(req);
				}
			}
			FeeReqControlActProcess controlActProcess = new FeeReqControlActProcess();
			controlActProcess.setList(okList);
			rb.setControlActProcess(controlActProcess);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("退费已经成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rb;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type,"
				+ "c.register_date,c.join_date,c.exam_times,a.arch_num,c.clinic_no,c.visit_no,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
}
