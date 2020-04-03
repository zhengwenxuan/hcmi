package com.hjw.webService.client.zhonglian.sxjt;

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
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.PacsSendDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONArray;
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
public class PACSDELSendMessageZLSXJT {
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    private static JdbcPersistenceManager jdbcPersistenceManager;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}
	public PACSDELSendMessageZLSXJT(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logName, boolean debug) {
		TranLogTxt.liswriteEror_to_txt(logName, "lismessage:" + JSONObject.fromObject(lismessage));
		ResultPacsBody rb = new ResultPacsBody();
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			String table = url.split("&")[3];	
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logName, "req:" + url);
			
			List<PacsSendDTO> pacsSendList = getDataWithOutLis(lismessage.getCustom().getExam_num(), lismessage.getCustom().getContact_name(), logName);
			TranLogTxt.liswriteEror_to_txt(logName, "lismessage:" + JSONArray.fromObject(pacsSendList));
			List<ApplyNOBean> appList = new ArrayList<ApplyNOBean>();
			for(PacsSendDTO pacssend : pacsSendList) {
				TranLogTxt.liswriteEror_to_txt(logName,"-----------------项目间分割线-----------------");
				String req_id = "";
				req_id = getzl_req_pacs_item(pacssend.getItem_code(), pacssend.getId());
				try {
					TranLogTxt.liswriteEror_to_txt(logName, "req:1、调用存储过程  zl_xb体检费用明细_delete");
					CallableStatement cs = connect.prepareCall("{call "+table+".zl_xb体检费用明细_delete(?,?)}");
					TranLogTxt.liswriteEror_to_txt(logName, "req:-"+table+".zl_xb体检费用明细_delete('" + lismessage.getCustom().getExam_num()
							+ "','" + req_id + "')");
					cs.setString(1, lismessage.getCustom().getExam_num());
					cs.setString(2, req_id);
					// 执行存储过程
					cs.execute();
					cs.close();
				}catch(Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
					TranLogTxt.liswriteEror_to_txt(logName,
							"res:调用存储过程  zl_xb体检费用明细_delete 错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
				try {
					//10.取消检验申请发送：（申请返回的申请单号）
					TranLogTxt.liswriteEror_to_txt(logName,
							"req:1、调用存储过程  CancelSendRequest");					
					TranLogTxt.liswriteEror_to_txt(logName, "res:"+table+".CancelSendRequest-" + lismessage.getCustom().getExam_num() + "-zlhis.b_PeisInterface.CancelSendRequest('"+req_id+"')");
					CallableStatement c = connect.prepareCall("{call "+table+".CancelSendRequest(?)}");
					c.setString(1, req_id);
					// 执行存储过程
					c.execute();
					c.close();
				
					//8.作废检验医嘱申请：(申请返回的申请单号)
					TranLogTxt.liswriteEror_to_txt(logName,
							"req:2、调用存储过程  CancelRequest");					
					TranLogTxt.liswriteEror_to_txt(logName, "res:"+table+".CancelRequest-" + lismessage.getCustom().getExam_num() + "-zlhis.b_PeisInterface.CancelRequest('"+req_id+"')");
					CallableStatement statement = connect.prepareCall("{call "+table+".CancelRequest(?)}");
					statement.setString(1, req_id);
					// 执行存储过程
					statement.execute();
					statement.close();
					
					//处理zl_req_pacs_item表数据
					boolean delflag = delzl_req_pacs_item(pacssend.getItem_code(), pacssend.getId());
					if(delflag) {
						String sql = "update examinfo_charging_item set is_application = 'N' where examinfo_id = "+pacssend.getId()
									+ "and charge_item_id = "+pacssend.getItemId() +" and isActive='Y' and is_application = 'Y' ";
						jdbcPersistenceManager.executeSql(sql); //修改项目状态
						
						ApplyNOBean an= new ApplyNOBean();
						an.setApplyNO(pacssend.getPacs_req_code());
						appList.add(an);
					}
				}catch(Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
					TranLogTxt.liswriteEror_to_txt(logName,
							"res:调用存储过程CancelSendRequest/CancelRequest Exception" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
			}
			if(appList.size() > 0 || lismessage.getComponents().isEmpty()) {
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("撤销pacs申请调用成功");
			}
			
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("链接pacs数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + xml);
		return rb;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public String getzl_req_pacs_item(String charging_item_ids, long exam_info_id) throws ServiceException {
		Connection tjtmpconnect = null;
		String req_id="";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
		String sb1 ="select exam_info_id,pacs_req_code,charging_item_ids,zl_pat_id,zl_pacs_id,"
				+ "req_id from zl_req_pacs_item where charging_item_ids='"+charging_item_ids+"' and exam_info_id = "+exam_info_id;
		ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
		if (rs1.next()) {
			req_id=rs1.getString("req_id");
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
		return req_id;
	} 
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public boolean delzl_req_pacs_item(String charging_item_ids, long exam_info_id) throws Exception {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = jdbcQueryManager.getConnection();
			String insertsql = "delete from zl_req_pacs_item where charging_item_ids='"+charging_item_ids+"' and exam_info_id = "+exam_info_id;
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
	
	private List<PacsSendDTO> getDataWithOutLis(String exam_num, String charingIds, String logname) {
		StringBuffer sb = new StringBuffer();
		sb.append("select '' as pacs_req_code,c.view_num,c.item_code,c.item_name,c.id as itemId,ei.id "
				+ " from examinfo_charging_item ec,department_dep dd,charging_item c "
				//+ " left join his_dict_dept hd on c.perform_dept = hd.dept_code"
				+ " ,exam_info ei  "
				+ " where ec.charge_item_id = c.id and c.dep_id = dd.id and ec.isActive = 'Y'"
				//+ " and ec.change_item != 'C' and ec.pay_status != 'M' and ec.exam_status in ('N','D')"//--and c.interface_flag = '2'
				+ " and dd.dep_category != '131'"
				+ " and ei.id = ec.examinfo_id "
				+ " and ec.is_application = 'Y' "
				+ " and ei.exam_num = '"+exam_num+"'");
		if ((charingIds != null) && (!"".equals(charingIds))) {
		      sb.append(" and c.id in (" + charingIds + ")");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "查项目："+sb.toString());
	    List<PacsSendDTO> pacsSendList = jdbcQueryManager.getList(sb.toString(), PacsSendDTO.class);
	    return pacsSendList;
	}
}
