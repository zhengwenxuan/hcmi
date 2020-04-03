package com.hjw.webService.client.zhonglian;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqHisItemDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bdyx.bean.del.Order;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.19	项目减项  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessageZLWC {

	private DelFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static ConfigService configService;
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
		
	}

	
	public DELFEESendMessageZLWC(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url,String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, url);
		FeeReqBody rb = new FeeReqBody();
		Connection connect = null;
		Connection tjconnection=null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			
			
			
			long exam_id=0;
			String exam_num="";
			
			
			String sql = "select exam_id from charging_summary_single where req_num='"+this.feeMessage.getREQ_NO()+"' and is_active='Y' ";
			TranLogTxt.liswriteEror_to_txt(logname, sql);
			tjconnection = jdbcQueryManager.getConnection();
			ResultSet rs = tjconnection.createStatement().executeQuery(sql);
			if(rs.next()) {
				exam_id = rs.getLong("exam_id");
				String sql2 = " select exam_num from exam_info where id='"+exam_id+"' and is_Active='Y'";
				TranLogTxt.liswriteEror_to_txt(logname, sql2);
				ResultSet rs2 = tjconnection.createStatement().executeQuery(sql2);
				if(rs2.next()){
					exam_num=rs2.getString("exam_num");
				}
				
			}
			
			System.err.println(exam_num);
			tjconnection.close();
				//zl_req_his_item  flay  1 为收费 2为撤销 3退费
				String insertsql= "select his_req_no from zl_req_his_item where req_no='"+this.feeMessage.getREQ_NO()+"' and exam_num='"+exam_num+"' and flay='1' ";
				TranLogTxt.liswriteEror_to_txt(logname, insertsql);
				List<ZlReqHisItemDTO> list = jdbcQueryManager.getList(insertsql, ZlReqHisItemDTO.class);
				
				if(list!=null && list.size()>0){
					TranLogTxt.liswriteEror_to_txt(logname, "req:1、Zl_三方门诊记帐_Delet"+"4,"+list.get(0).getHis_req_no()+","+exam_num+"吴梦璇");
					CallableStatement c = connect.prepareCall("{call Zl_三方门诊记帐_Delete(?,?,?,?)}");
					
					c.setInt(1, 4);//体检4 门诊1
					c.setString(2, list.get(0).getHis_req_no());//
					c.setString(3,exam_num);//体检号
					c.setString(4,"吴梦璇");//操作员
					// 执行存储过程
					c.execute();
					c.close();
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("");
					
					
					
					String updatesql = "update zl_req_his_item set flay='2' where req_no='"+this.feeMessage.getREQ_NO()+"' and exam_num='"+exam_num+"' and flay='1' ";
					jdbcPersistenceManager.execSql(updatesql);
					
				}else{
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("zl_req_his_item表内未查询数据");
				}
			
				
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
			TranLogTxt.liswriteEror_to_txt(logname, com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rb;
	}

}
