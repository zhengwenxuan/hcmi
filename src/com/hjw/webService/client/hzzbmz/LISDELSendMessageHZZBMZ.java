package com.hjw.webService.client.hzzbmz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.7	检查申请撤销信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISDELSendMessageHZZBMZ{
	private LisMessageBody lismessage;

	public LISDELSendMessageHZZBMZ(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logName) {
		ResultLisBody rb = new ResultLisBody();
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logName, "req:" + lismessage.getMessageid() + ":" + url);				
			ControlActLisProcess ca = new ControlActLisProcess();
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();		
			for (LisComponents pcs : lismessage.getComponents()) {
				String requisition_id = pcs.getReq_no();
				String sb1 = "select id,charge_state,sqdzt from his_requisition2 where outpatient_id='" + lismessage.getCustom().getExam_num() + "' and requisition_id='"
						+ requisition_id + "' ";
				TranLogTxt.liswriteEror_to_txt(logName, "res:" + lismessage.getMessageid() + ":操作语句： " + sb1);
				ResultSet rs1 = connect.createStatement().executeQuery(sb1);
				boolean deleteflag=true;
				if (rs1.next()) {
					int sqdzt = rs1.getInt("sqdzt");
					if (sqdzt>=2) {
						deleteflag=false;	
					}
				}
				rs1.close();
				if(deleteflag){
					String updatesql = "update his_requisition2 set sqdzt=-1 where where outpatient_id='" + lismessage.getCustom().getExam_num() + "' and requisition_id='"
						+ requisition_id + "' ";
					TranLogTxt.liswriteEror_to_txt(logName, "res:" + lismessage.getMessageid() + ":操作语句： " + updatesql);
					connect.createStatement().executeUpdate(updatesql);
					ApplyNOBean an = new ApplyNOBean();	
					an.setApplyNO(pcs.getReq_no());
					list.add(an);
				}				  
			}
			ca.setList(list);
			rb.setControlActProcess(ca);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("");
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("操作错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logName, "res:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					OracleDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}

}
