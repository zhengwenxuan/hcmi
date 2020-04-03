package com.hjw.webService.client.huojianwa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeReqControlActProcess;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.17	收费退费
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class UNFEESendMessageHjw{
	private UnFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public UNFEESendMessageHjw(UnFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url, String logname) {
		FeeReqBody rb = new FeeReqBody();
		Connection connect = null;
		String xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getEXAM_NUM() + ":" + xml);
		try {
			connect = jdbcQueryManager.getConnection();
			connect.setAutoCommit(false);
			List<ReqNo> okList = new ArrayList<ReqNo>();
			for (String reqNo : this.feeMessage.getREQ_NOS().getREQ_NO()) {
				String pi_sql = "select patID  from Pat_Info where patID='" + feeMessage.getEXAM_NUM() + "' and req_no='"+reqNo+"' and chargeFlag!='1'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + pi_sql);
				ResultSet pi_rs = connect.createStatement().executeQuery(pi_sql);
				if (pi_rs.next()) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("状态不是已收费，不能退费！");
					pi_rs.close();
					connect.rollback();
					return rb;
				}
				pi_rs.close();
								
				String sb1 = "update Pat_Info set chargeFlag='2' where patID='" + feeMessage.getEXAM_NUM() + "' and req_no='"+reqNo+"' and chargeFlag='1'";					
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
				connect.createStatement().execute(sb1);
				sb1 = "update Pat_Charge_list set chargeFlag='2' where req_no='"+reqNo+"'";					
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
				connect.createStatement().execute(sb1);		
				ReqNo req = new ReqNo();
				req.setREQ_NO(reqNo);
				okList.add(req);
			}
			FeeReqControlActProcess controlActProcess = new FeeReqControlActProcess();
			controlActProcess.setList(okList);
			rb.setControlActProcess(controlActProcess);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("退费已经成功");
			connect.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			try{
				connect.rollback();
			}catch(Exception et){}
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		} finally {
			try {
				if (connect != null) {
					connect.setAutoCommit(true);
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rb;
	}
}
