package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.synjones.framework.persistence.JdbcQueryManager;

public class PACSDELSendMessageCA {
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSDELSendMessageCA(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	public ResultPacsBody getMessage(String url, String logname, boolean debug) {
		ResultPacsBody rb = new ResultPacsBody();
		String exam_num = this.lismessage.getCustom().getExam_num();
		Connection his_connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			his_connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + url);
			ControlActPacsProcess ca = new ControlActPacsProcess();
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			for (PacsComponents pcs : lismessage.getComponents()) {
				System.out.println("体检项目：="+lismessage.getComponents().size());
				ApplyNOBean an = new ApplyNOBean();
				
				//1,删除：3.1检查预约记录 EXAM_APPOINTS
				String delete_EXAM_APPOINTS_sql = "delete from EXAM_APPOINTS where EXAM_NO = '"+pcs.getReq_no()+"' and PATIENT_ID = '"+exam_num+"' ";
				TranLogTxt.liswriteEror_to_txt(logname, "1,删除：3.1检查预约记录 EXAM_APPOINTS: " +delete_EXAM_APPOINTS_sql);
				his_connect.createStatement().executeUpdate(delete_EXAM_APPOINTS_sql);
				
//				//2,删除：3.3检查主记录 EXAM_MASTER
//				String delete_EXAM_MASTER_sql = "delete from EXAM_MASTER where EXAM_NO = '"+pcs.getReq_no()+"' and PATIENT_ID = '"+exam_num+"' ";
//				TranLogTxt.liswriteEror_to_txt(logname, "2,删除：3.3检查主记录 EXAM_MASTER: " +delete_EXAM_MASTER_sql);
//				his_connect.createStatement().executeUpdate(delete_EXAM_MASTER_sql);
				
				for (int i=0; i<pcs.getPacsComponent().size(); i++) {
					PacsComponent pc = pcs.getPacsComponent().get(i);
					System.out.println("体检子项目：="+pcs.getPacsComponent().size());
					//3,删除：3.2检查项目记录 EXAM_ITEMS
					String delete_EXAM_ITEMS_sql = "delete from EXAM_ITEMS where EXAM_NO = '"+pcs.getReq_no()+"' ";
					TranLogTxt.liswriteEror_to_txt(logname, "3,删除：3.2检查项目记录 EXAM_ITEMS: " +delete_EXAM_ITEMS_sql);
					his_connect.createStatement().executeUpdate(delete_EXAM_ITEMS_sql);
				}
				
				an.setApplyNO(pcs.getReq_no());
				list.add(an);
				
//				ZlReqPacsItemDTO zrpi = new ZlReqPacsItemDTO();
//				zrpi.setExam_info_id(eu.getId());
//				zrpi.setPacs_req_code(pcs.getReq_no());
//				zrpi.setReq_id(curTest_No);
//				configService.delete_zl_req_pacs_item(zrpi, logname);
			}
			ca.setList(list);
			rb.setControlActProcess(ca);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("pacs调用成功");
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("链接pacs数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (his_connect != null) {
					OracleDatabaseSource.close(his_connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
}
