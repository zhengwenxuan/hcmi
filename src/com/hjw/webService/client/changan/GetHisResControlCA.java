package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqHisItemDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.service.LisPacsApplicationService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetHisResControlCA {
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

	public ResultHeader getMessage(String url,int days, String logName) {
		String datetime = DateTimeUtil.DateDiff2(days);
		Connection tjtmpconnect = null;
		ResultHeader rh= new ResultHeader();
		try {
			tjtmpconnect = jdbcQueryManager.getConnection();
			String sb1 = "select distinct a.exam_id,b.exam_num,a.req_num,a.id "
					+ " from charging_summary_single a,exam_info b,examinfo_charging_item eci "
					+ " where a.exam_id=b.id "
					+ " and b.is_Active='Y' "
					+ " and a.charging_status='R' "
					+ " and b.id = eci.examinfo_id and eci.exam_indicator != 'T' and eci.pay_status not in ('Y', 'M') "
					+ " and CONVERT(varchar(50),a.create_time,23)>= '"+datetime+"' order by b.exam_num ";
			TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + sb1);
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs.next()) {
				String exam_num = rs.getString("exam_num");
				long exam_id=rs.getLong("exam_id");
				long summary_id = rs.getLong("id");
				String req_num = rs.getString("req_num");
				boolean hisstatus = getHisStatus(url,req_num,logName);
				if(hisstatus){
					String updatesql="update examinfo_charging_item set pay_status='Y' where id in "
							+ " ( select b.id from charging_detail_single a,examinfo_charging_item b "
							+ " where a.summary_id='"+summary_id+"' and b.charge_item_id=a.charging_item_id and b.examinfo_id='"+exam_id+"'"
							+ " and b.pay_status <> 'M ' and b.isActive = 'Y' )";
					TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
					tjtmpconnect.createStatement().execute(updatesql);
					updatesql="update charging_summary_single set charging_status='Y' where id='"+summary_id+"'";
					TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
					tjtmpconnect.createStatement().execute(updatesql);
					
					//发送lis、pacs申请
					String doctorid = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value().trim();//开单医生id
					String doctorname = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
					UserDTO user = new UserDTO();
					user.setWork_num(doctorid);
					user.setName(doctorname);
					String bangding = configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim();
					String lis = configService.getCenterconfigByKey("IS_HIS_LIS_CHECK").getConfig_value().trim();
					String pacs = configService.getCenterconfigByKey("IS_PACS_INTERFACE").getConfig_value().trim();
					if ("Y".equals(lis) && "N".equals(bangding)) {
						try {
							String ret = lisPacsApplicationService.lisSend(exam_num, null, user, false, bangding,lis);
							TranLogTxt.liswriteEror_to_txt(logName, "发送lis申请返回： " + ret);
						}  catch (Exception ex) {
							TranLogTxt.liswriteEror_to_txt(logName, "发送lis申请： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
						}
					}
					if ("Y".equals(pacs)) {
						try {
							String ret = lisPacsApplicationService.pacsSend(exam_num, null, user, false,pacs);
							TranLogTxt.liswriteEror_to_txt(logName, "发送lis申请返回： " + ret);
						}  catch (Exception ex) {
							TranLogTxt.liswriteEror_to_txt(logName, "发送pacs申请： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
						}
					}
				}
			}
			rs.close();
			rh.setTypeCode("AA");
			rh.setText("");
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logName, "res: :  操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rh;
	}
	
	private boolean getHisStatus(String url,String reqno,String logName){
		Connection tjtmpconnect = null;
		boolean hisstatus=false;
		try {
			String select_zl_req_his_item_sql = " select * from zl_req_his_item "
			+ " where req_no = '"+reqno+"' "
			+ " order by createdate";
			TranLogTxt.liswriteEror_to_txt(logName, "select_zl_req_his_item_sql:" + select_zl_req_his_item_sql);
			List<ZlReqHisItemDTO> list = jdbcQueryManager.getList(select_zl_req_his_item_sql, ZlReqHisItemDTO.class);
			
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];	
			TranLogTxt.liswriteEror_to_txt(logName, "req:" + url);
			tjtmpconnect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			
			for(ZlReqHisItemDTO zrhi : list) {
				String select_OUTP_TREAT_REC_sql = "select * from OUTP_TREAT_REC "
					+ " where VISIT_DATE = to_date('"+zrhi.getTmp2()+"', 'yyyy-mm-dd') "
					+ " and VISIT_NO='"+zrhi.getTmp1()+"' and SERIAL_NO='"+zrhi.getHis_req_no()+"' and ITEM_NO = '"+zrhi.getHis_num()+"' "
					+ " and CHARGE_INDICATOR = '1'";
				TranLogTxt.liswriteEror_to_txt(logName, "select_OUTP_TREAT_REC_sql:" + select_OUTP_TREAT_REC_sql);
				ResultSet rs = tjtmpconnect.createStatement().executeQuery(select_OUTP_TREAT_REC_sql);
				if(rs.next()) {//已收费
					rs.close();
					return true;
				}
				rs.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logName,"res:"+reqno+":"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return hisstatus;
	}
}
