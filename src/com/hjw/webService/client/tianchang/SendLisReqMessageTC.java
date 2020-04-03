package com.hjw.webService.client.tianchang;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.service.LisPacsApplicationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

public class SendLisReqMessageTC {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static LisPacsApplicationService lisPacsApplicationService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		lisPacsApplicationService = (LisPacsApplicationService) wac.getBean("lisPacsApplicationService");
	}

	public ResultHeader getMessage(String url, int days, String logName) {
		//charging_summary_single 表中包含个检和团检的个人付费部分
		String sql = "select ei.*, css.req_num as acc_num, css.charging_status as pay_status from his_pay_result hpr, charging_summary_single css, exam_info ei "
				+ " where hpr.req_no = css.req_num and css.exam_id = ei.id "
				+ " and ei.is_Active='Y' "//and cds.charging_item_id //charging_detail_single cds
				+ " and hpr.trans_flag != 1 "//trans_flag:0未处理,1:成功,2:失败
				+ " and hpr.trans_times < 3 "//已处理三次的数据不再处理
				+ " order by hpr.trans_times, hpr.pay_date ";
		TranLogTxt.liswriteEror_to_txt(logName, "sql: " + sql);
		List<ExamInfoUserDTO> userList = this.jdbcQueryManager.getList(sql, ExamInfoUserDTO.class);
		
//		StringBuffer sb = new StringBuffer();
//		sb.append("select top "+AUTO_LIS_READ_COUNT+" c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type,c.is_marriage,c.is_after_pay"
//				+ ",c.register_date,c.join_date,c.exam_times,c.phone,c.company,c.address,a.arch_num,a.birthday,a.id_num ");
//		sb.append(" from customer_info a,exam_info c ");
//		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");
//		sb.append(" and c.status != 'Z' ");//and c.exam_type = 'G'
//		sb.append(" and c.join_date >= '"+DateTimeUtil.DateDiff2(days)+"'");	
//		List<ExamInfoUserDTO> userList = this.jdbcQueryManager.getList(sb.toString(), ExamInfoUserDTO.class);
		
		String doctorid = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
		UserDTO user = new UserDTO();
		user.setWork_num(doctorid);
		user.setName(doctorname);
		String bangding = this.configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim();
		String IS_HIS_LIS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_LIS_CHECK").getConfig_value().trim();
		int success = 0;
		for (ExamInfoUserDTO es : userList) {
			if("Z".equals(es.getStatus())) {
				String msg = "体检者已总检";
				update_his_pay_result(2,msg,es.getAcc_num(),logName);
			}
			String ret = lisPacsApplicationService.lisSend(es.getExam_num(), "", user, false, bangding, IS_HIS_LIS_CHECK);
			if("ok".equals(ret.split("-")[0]) || ret.startsWith("error-没有需要发送申请的检验科室项目")) {
				update_his_pay_result(1,"",es.getAcc_num(),logName);
				success ++;
			} else {
				update_his_pay_result(2,ret,es.getAcc_num(),logName);
				TranLogTxt.liswriteEror_to_txt(logName, "自动发送LIS申请失败,体检编号:"+es.getExam_num()+",失败原因:" + ret);
			}
		}
		ResultHeader rh = new ResultHeader();
		rh.setTypeCode("AA");
		rh.setText("本次自动发送lis申请,共执行"+userList.size()+"人,成功"+success+"人");
		return rh;
	}
	
	public boolean update_his_pay_result(int trans_flag,String msg, String req_no, String logname) throws ServiceException {
		String sql = "update his_pay_result set trans_flag = "+trans_flag+", trans_date = getdate(),trans_times=(trans_times+1),note='"+msg.replaceAll("'", "''")+"' where req_no = '"+req_no+"'";
		int resflag = -1;
		Connection connection = null;
		try {
			connection = this.jdbcQueryManager.getConnection();
			resflag = connection.createStatement().executeUpdate(sql);
		} catch (Throwable e) {
			TranLogTxt.liswriteEror_to_txt(logname, "更新his_pay_result sql："+sql);
			TranLogTxt.liswriteEror_to_txt(logname, "更新his_pay_result 错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
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
}
