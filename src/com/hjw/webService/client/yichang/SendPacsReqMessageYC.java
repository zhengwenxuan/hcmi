package com.hjw.webService.client.yichang;

import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.service.LisPacsApplicationService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class SendPacsReqMessageYC {
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
		try{
			String sql = "select distinct top 10 ei.exam_num,join_date from exam_info ei, examinfo_charging_item ec, charging_item c, pacs_summary p,pacs_detail d "
					+ " where ec.charge_item_id = c.id and ei.id = ec.examinfo_id "
					+ " and ec.isActive = 'Y' and c.interface_flag = '2' "
					+ " and ec.change_item != 'C' and ec.pay_status != 'M' and ec.exam_status in ('N','D') "
					+ " and ei.status in ('D','J') and ei.is_active = 'Y' "
					+ " and ei.join_date > '"+DateTimeUtil.DateDiff2(days)+"' and ei.join_date < '"+DateTimeUtil.getDate2()+"' "
					+ " and p.id = d.summary_id and d.chargingItem_num = c.item_code "
					+ " and ec.is_application = 'N' "
					+ " order by join_date";
			TranLogTxt.liswriteEror_to_txt(logName, "sql: " + sql);
			List<ExamInfoUserDTO> userList = jdbcQueryManager.getList(sql, ExamInfoUserDTO.class);
			
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
			int success = 0;
			String IS_HIS_PACS_CHECK = configService.getCenterconfigByKey("IS_HIS_PACS_CHECK").getConfig_value().trim();
			for (ExamInfoUserDTO eu : userList) {
				String ret = lisPacsApplicationService.pacsSend(eu.getExam_num(), null, user, false,IS_HIS_PACS_CHECK);
				
				if("ok".equals(ret.split("-")[0]) || ret.startsWith("error-没有需要发送申请的影像科室项目")) {
					success ++;
				} else {
					TranLogTxt.liswriteEror_to_txt(logName, "自动发送PACS申请失败,体检编号:"+eu.getExam_num()+",失败原因:" + ret);
				}
			}
			TranLogTxt.liswriteEror_to_txt(logName, "本次自动发送pacs申请,共执行"+userList.size()+"人,成功"+success+"人");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		ResultHeader rh = new ResultHeader();
		rh.setTypeCode("AA");
		rh.setText("");
		return rh;
	}
}
