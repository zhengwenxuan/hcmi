package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class LISDELSendMessageCA{
	private LisMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    private static JdbcPersistenceManager jdbcPersistenceManager;
    private static ConfigService configService;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	public LISDELSendMessageCA(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	public ResultLisBody getMessage(String url, String logname, boolean debug) {
		TranLogTxt.liswriteEror_to_txt(logname, "1-----------------开始lis撤销申请--------------------");
		ResultLisBody rb = new ResultLisBody();
		Connection his_connect = null;
		try {
			String jsonString = JSONSerializer.toJSON(lismessage).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
			ExamInfoUserDTO eu= configService.getExamInfoForNum(this.lismessage.getCustom().getExam_num());

			TranLogTxt.liswriteEror_to_txt(logname, "2-----------------lis数据库url--------------------" + url);
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			his_connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "3-----------------连接成功--------------------");
			
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			for (LisComponents lcs : this.lismessage.getComponents()) {
				try {
					List<ZlReqItemDTO> zlReqItemList = configService.select_zl_req_item(eu.getId(),lcs.getReq_no(), logname);
					ZlReqItemDTO zri = new ZlReqItemDTO();
					if(zlReqItemList.size() > 0) {
						zri = zlReqItemList.get(0);
					} else {
						TranLogTxt.liswriteEror_to_txt(logname, "zl_req_item中查不到");
					}
					
					//0,先查询lis视图，判断lis是否已经把项目信息获取走了
					Connection lis_connect = null;
					try{
						String select_lis_view_sql = "select * from v_lis_tj_require_items where code_record like '%"+eu.getExam_num()+"|0|"+zri.getReq_id()+"|%'";//此视图提供最近90天的体检申请记录
						TranLogTxt.liswriteEror_to_txt(logname, "select_lis_view_sql:"+select_lis_view_sql);
						String lisURL = configService.getCenterconfigByKey("LIS_URL").getConfig_value();
						lis_connect = SqlServerDatabaseSource.getConnection(lisURL.split("&")[0], lisURL.split("&")[1], lisURL.split("&")[2]);
						ResultSet rs = lis_connect.createStatement().executeQuery(select_lis_view_sql);
						if(rs.next()) {
							TranLogTxt.liswriteEror_to_txt(logname, "lis视图中有此纪录，证明lis已获取走，所以撤销失败");
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("lis视图中有此纪录，证明lis已获取走，所以撤销失败");
							return rb;
						}
					} catch (Exception ex) {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("连接数据库失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
					} finally {
						if(lis_connect != null) {
							lis_connect.close();
						}
					}
					
					//1,删除对应记录：3.15检验主记录  LAB_TEST_MASTER
					String delete_LAB_TEST_MASTER_sql = "delete LAB_TEST_MASTER where TEST_NO = '"+zri.getReq_id()+"' and PATIENT_ID = '"+this.lismessage.getCustom().getExam_num()+"'";
					TranLogTxt.liswriteEror_to_txt(logname, "1,删除对应记录：3.15检验主记录  LAB_TEST_MASTER: " +delete_LAB_TEST_MASTER_sql);
					int ret = his_connect.createStatement().executeUpdate(delete_LAB_TEST_MASTER_sql);
					TranLogTxt.liswriteEror_to_txt(logname, "执行删除语句返回-ret:"+ret);
										
					//2,此表不做处理：3.16检验项目 LAB_TEST_ITEMS
					
					ApplyNOBean ap = new ApplyNOBean();
					ap.setApplyNO(lcs.getReq_no());
					ap.setLis_id(lcs.getLis_id());
					ap.setBarcode(lcs.getReq_no());
					list.add(ap);
				} catch (Throwable ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(list);
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("连接数据库失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			if(his_connect != null) {
				try {
					his_connect.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		TranLogTxt.liswriteEror_to_txt(logname, "6-----------------结束lis撤销申请--------------------");
		return rb;
	}
	
//	private String updateSampleExamDetailByExamid(long examid, String sample_barcode, String status) throws ServiceException {
//		String barcode_new = this.batchService.GetCreateID("barcode");
//		String sql = "update sample_exam_detail set status = '"+status+"',sample_barcode ='"+barcode_new+"'  where exam_info_id=" + examid + " and sample_barcode='" + sample_barcode + "'";
//		System.out.println(sql);
//		this.jdbcPersistenceManager.executeSql(sql);
//		return barcode_new;
//	}
//	
//	private void update_examinfo_charging_item(long examid, String sample_barcode, String status) throws ServiceException {
//		String sql = "update examinfo_charging_item set is_application = '"+status+"' where examinfo_id = "+examid
//		+ " and charge_item_id in(select ec.charging_id from sample_exam_detail s,examResult_chargingItem ec "
//		+ " where s.id = ec.exam_id and ec.result_type = 'sample' and s.sample_barcode = '"+sample_barcode+"')";
//		this.jdbcPersistenceManager.executeSql(sql);
//	}
}
