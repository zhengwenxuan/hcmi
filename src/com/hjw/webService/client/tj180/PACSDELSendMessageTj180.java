package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.7	检查申请撤销信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSDELSendMessageTj180 {
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSDELSendMessageTj180(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logName, boolean debug) {
		ResultPacsBody rb = new ResultPacsBody();
		ControlActPacsProcess ca = new ControlActPacsProcess();
		List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
		for (PacsComponents pcs : lismessage.getComponents()) {
			try {
				delzl_req_pacs_item(pcs.getReq_no());
				ApplyNOBean an = new ApplyNOBean();
				an.setApplyNO(pcs.getReq_no());
				list.add(an);
			}catch(Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logName,
						"res:" + lismessage.getMessageid() + ": 1、pacs调用zlhis.b_PeisInterface.CancelSendRequest错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}
		ca.setList(list);
		rb.setControlActProcess(ca);
		rb.getResultHeader().setTypeCode("AA");
		rb.getResultHeader().setText("pacs调用成功");
		JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + str);
		return rb;
	}
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public boolean delzl_req_pacs_item(String pacreqcode) throws Exception {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String insertsql = "delete from zl_req_pacs_item where pacs_req_code='" + pacreqcode + "'";
			tjtmpconnect.createStatement().executeUpdate(insertsql);
			return true;
		} catch (SQLException ex) {
			return false;
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
}
