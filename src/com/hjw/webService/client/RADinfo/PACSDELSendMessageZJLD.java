package com.hjw.webService.client.RADinfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
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
public class PACSDELSendMessageZJLD {
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSDELSendMessageZJLD(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + url);
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			ControlActPacsProcess ca = new ControlActPacsProcess();
			List<ApplyNOBean> appList = new ArrayList<ApplyNOBean>();
			for (PacsComponents pcs : lismessage.getComponents()) {
				boolean falgs=true;
				for(PacsComponent pc:pcs.getPacsComponent()){					
					try {
						String pacs_jch = Getpacs_jch(url.split("&"),lismessage.getCustom().getExam_num(),pc.getItemCode(),logname);					
						String strSQL = "select lsh, status from HisJCRW where jch='" 
					      + pacs_jch + "' and ylxh='" +pc.getItemCode()+"'";
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL);						
						ResultSet rs = connect.createStatement().executeQuery(strSQL);
						if (rs.next()) {
							String lsh = rs.getString("lsh");
							if("登记".equals(rs.getString("status"))){
								String insertsql="insert into HisRWZL(jcrwlsh, rwzllx, rwzlsj) values('" + lsh + "', 10, getdate())";
								TranLogTxt.liswriteEror_to_txt(logname, "res:" + insertsql);		
								connect.createStatement().execute(strSQL.toString());
							}							
						}
						rs.close();						
					} catch (Exception ex) {
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
						falgs=false;
					}
				}
				
				if(falgs){
				ApplyNOBean ab = new ApplyNOBean();
				ab.setApplyNO(pcs.getReq_no());
				appList.add(ab);
				}
			}
			ca.setList(appList);
			rb.setControlActProcess(ca);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("pacs调用成功");
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("链接pacs数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
	
	/**
	 * 
	 * @param mssqlurl
	 * @param item_id
	 * @param logname
	 * @return
	 */
	private String Getpacs_jch(String mssqlurl[],String exam_num,String pacsItemCode,String logname) {
		String lsh = "";
		Connection connect = null;
		try {
			String dburl = mssqlurl[0];
			String user = mssqlurl[1];
			String passwd = mssqlurl[2];
			connect = SqlServerDatabaseSource.getConnection(dburl, user, passwd);
			String strSQL = "select pacs_jch from PacsReq where exam_num='" +exam_num+"' and pacs_item_code='" +pacsItemCode+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL);
			ResultSet rs = connect.createStatement().executeQuery(strSQL);
			if (rs.next()) {
				lsh = rs.getString("pacs_jch");
			}
			rs.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					OracleDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lsh;
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
