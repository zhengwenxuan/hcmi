package com.hjw.webService.client.zhonglian.waijian;

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
public class LISDELSendMessageZLWJ {
	private LisMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public LISDELSendMessageZLWJ(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logName, boolean debug) {
		TranLogTxt.liswriteEror_to_txt(logName, "lismessage:" + JSONObject.fromObject(lismessage));
		ResultLisBody rb = new ResultLisBody();
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];	
			String table = url.split("&")[3];		
			TranLogTxt.liswriteEror_to_txt(logName, "req:" + lismessage.getMessageid() + ":" + url);
			ControlActLisProcess ca = new ControlActLisProcess();
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			for (LisComponents pcs : lismessage.getComponents()) {
				TranLogTxt.liswriteEror_to_txt(logName,"-----------------项目间分割线-----------------");
				List<String> listno = new ArrayList<String>();
				listno = getzl_req_pacs_item(pcs.getReq_no());
				boolean lisdelfalg=true;
				for(String req_id:listno){
				try {
					connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
					
					TranLogTxt.liswriteEror_to_txt(logName, "req:1、调用存储过程  zl_xb体检费用明细_delete");
					CallableStatement cs = connect.prepareCall("{call "+table+".zl_xb体检费用明细_delete(?,?)}");
					TranLogTxt.liswriteEror_to_txt(logName, "req:-"+table+".zl_xb体检费用明细_delete('" + lismessage.getCustom().getExam_num()
							+ "','" + listno + "')");
					cs.setString(1, lismessage.getCustom().getExam_num());
					cs.setString(2, req_id);
					// 执行存储过程
					cs.execute();
					cs.close();
				}catch(Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
					TranLogTxt.liswriteEror_to_txt(logName,
							"res:" + lismessage.getMessageid() + ": 1、lis调用存储过程 zl_xb体检费用明细_delete错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
				try {
					//10.取消检验申请发送：（申请返回的申请单号）
					TranLogTxt.liswriteEror_to_txt(logName,
							"req:" + lismessage.getMessageid() + ":1、调用存储过程  CancelSendRequest");					
					TranLogTxt.liswriteEror_to_txt(logName, "res:"+table+".CancelSendRequest-" + lismessage.getCustom().getExam_num() + "-"+table+".CancelSendRequest('"+req_id+"')");
					CallableStatement c = connect.prepareCall("{call "+table+".CancelSendRequest(?)}");
					c.setString(1, req_id);
					// 执行存储过程
					c.execute();
					c.close();
					
					//8.作废检验医嘱申请：(申请返回的申请单号)
					TranLogTxt.liswriteEror_to_txt(logName,
							"req:" + lismessage.getMessageid() + ":1、调用存储过程  CancelRequest");					
					TranLogTxt.liswriteEror_to_txt(logName, "res:"+table+".CancelRequest-" + lismessage.getCustom().getExam_num() + "-"+table+".CancelRequest('"+req_id+"')");
					CallableStatement statement = connect.prepareCall("{call "+table+".CancelRequest(?)}");
					statement.setString(1, req_id);
					// 执行存储过程
					statement.execute();
					statement.close();
					
				}catch(Exception ex) {
					lisdelfalg=false;
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
					TranLogTxt.liswriteEror_to_txt(logName,
							"res:" + lismessage.getMessageid() + ": 1、lis调用存储过程错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
				TranLogTxt.liswriteEror_to_txt(logName,"lisdelfalg:"+lisdelfalg);
				 if(!lisdelfalg){
					break;					
				  }		
			}
				TranLogTxt.liswriteEror_to_txt(logName,"lisdelfalg:"+lisdelfalg);
				if(lisdelfalg){
				  delzl_req_lis_item(pcs.getReq_no(),logName);
				  ApplyNOBean an = new ApplyNOBean();
				  an.setApplyNO(pcs.getReq_no());
				  list.add(an);
				}
			}
			if(list.size() > 0) {
				ca.setList(list);
				rb.setControlActProcess(ca);
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("lis调用成功");
			}
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
			String deletesql = "delete from zl_req_item where lis_req_code='" + pacreqcode + "'";
			tjtmpconnect.createStatement().executeUpdate(deletesql);
			TranLogTxt.liswriteEror_to_txt(logName,
					"req:" + lismessage.getMessageid() + ":"+deletesql);
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
