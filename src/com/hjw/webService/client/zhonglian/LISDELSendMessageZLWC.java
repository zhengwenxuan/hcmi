package com.hjw.webService.client.zhonglian;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.synjones.framework.exception.ServiceException;
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
public class LISDELSendMessageZLWC {
	private LisMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public LISDELSendMessageZLWC(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logName, boolean debug) {
		ResultLisBody rb = new ResultLisBody();
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];	
			TranLogTxt.liswriteEror_to_txt(logName, "req:" + lismessage.getMessageid() + ":" + url);
			ControlActLisProcess ca = new ControlActLisProcess();
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			for (LisComponents pcs : lismessage.getComponents()) {
				List<String> listno = new ArrayList<String>();
				listno = getzl_req_pacs_item(pcs.getReq_no());
				boolean lisdelfalg=true;
				for(String req_id:listno){
				try {
					connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
					TranLogTxt.liswriteEror_to_txt(logName,
							"req:" + lismessage.getMessageid() + ":1、调用存储过程  zl_CancelSendRequest");					
					TranLogTxt.liswriteEror_to_txt(logName, "res:zl_CancelSendRequest-" + lismessage.getCustom().getExam_num() + "-"+"zl_CancelSendRequest('"+req_id+"',?,?,?)");
					CallableStatement c = connect.prepareCall("{call zl_CancelSendRequest(?,?,?,?)}");
					c.setString(1, req_id);
					c.registerOutParameter(2, java.sql.Types.VARCHAR);
					c.registerOutParameter(3, java.sql.Types.VARCHAR);
					c.registerOutParameter(4, java.sql.Types.VARCHAR);
					// 执行存储过程
					c.execute();
					// 得到存储过程的输出参数值
					String RESULT_CODE = c.getString(2);
					String ERROR_MSG = c.getString(3);
					String req_no = c.getString(4);
					c.close();
					TranLogTxt.liswriteEror_to_txt(logName,
							"res:zl_CancelSendRequest-" + lismessage.getMessageid() + ":" + RESULT_CODE
									+ "-----" + ERROR_MSG + "-----" + req_no);
					if ("1".equals(RESULT_CODE)) {
						
					} else {
						lisdelfalg = false;
						TranLogTxt.liswriteEror_to_txt(logName,
								"res:" + lismessage.getMessageid() + ": 1、lis调用zl_CancelSendRequest错误");
					}
				}catch(Exception ex) {
					lisdelfalg=false;
					TranLogTxt.liswriteEror_to_txt(logName,
							"res:" + lismessage.getMessageid() + ": 1、lis调用zl_CancelSendRequest错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}				
				 if(!lisdelfalg){
					break;					
				  }		
			}
				if(lisdelfalg){
				  delzl_req_lis_item(pcs.getReq_no(),logName);
				  ApplyNOBean an = new ApplyNOBean();
				  an.setApplyNO(pcs.getReq_no());
				  list.add(an);
				}
			}
			ca.setList(list);
			rb.setControlActProcess(ca);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("lis调用成功");
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("链接lis数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public List<String> getzl_req_pacs_item(String pacreqcode) throws ServiceException {
		Connection tjtmpconnect = null;
		List<String> list = new ArrayList<String>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
		String sb1 ="select exam_info_id,lis_req_code,charging_item_id,zl_pat_id,lis_item_id,"
				+ "req_id from zl_req_item where lis_req_code='"+pacreqcode+"'";
		ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
		while (rs1.next()) {
			list.add(rs1.getString("req_id"));
		}
		rs1.close();
	} catch (SQLException ex) {
		ex.printStackTrace();
	} finally {
		try {
			if (tjtmpconnect != null) {
				tjtmpconnect.close();
			}
		} catch (SQLException sqle4) {
			sqle4.printStackTrace();
		}
	}
		return list;
	} 
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public boolean delzl_req_lis_item(String pacreqcode,String logName) throws Exception {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String insertsql = "delete from zl_req_item where lis_req_code='" + pacreqcode + "'";
			tjtmpconnect.createStatement().executeUpdate(insertsql);
			TranLogTxt.liswriteEror_to_txt(logName,
					"req:" + lismessage.getMessageid() + ":"+insertsql);
			return true;
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logName,
					"req:" + lismessage.getMessageid() + ":"+com.hjw.interfaces.util.StringUtil.formatException(ex));
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
