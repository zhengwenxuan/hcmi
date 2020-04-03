package com.hjw.webService.client.huojianwa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessageHjw {

	private DelFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public DELFEESendMessageHjw(DelFeeMessage feeMessage){
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
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
			connect = jdbcQueryManager.getConnection();
			connect.setAutoCommit(false);
			String xml = "";
			xml = JaxbUtil.convertToXml(this.feeMessage, true);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
			
			String pi_sql = "select patID  from Pat_Info where patID='" + feeMessage.getExam_num() + "' and req_no='"+feeMessage.getREQ_NO()+"' and chargeFlag!='0'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + pi_sql);
			ResultSet pi_rs = connect.createStatement().executeQuery(pi_sql);
			if (pi_rs.next()) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("费用已经处理,操作不能继续！");
				pi_rs.close();
				return rb;
			}
			pi_rs.close();
//			double price = 0.0;
//			double charge = 0.0;
//			List<ExaminfoChargingItemDTO> itemCodeList = this.feeMessage.getItemCodeList();
//			for(ExaminfoChargingItemDTO eci : itemCodeList) {
//				price += eci.getItem_amount();
//				charge += eci.getAmount();
//			}
//			String updSql = "update Pat_Charge_List set Price = (Price - "+price+"), Charge = (Charge - "+charge+") where req_no ='" + feeMessage.getREQ_NO() + "' ";					
//			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + updSql);
//			connect.createStatement().execute(updSql);
			
			String del_pcl_sql = "delete from Pat_Charge_List where req_no ='" + feeMessage.getREQ_NO() + "'";					
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + del_pcl_sql);
			connect.createStatement().execute(del_pcl_sql);
			String del_pi_sql = "delete from Pat_Info where req_no ='" + feeMessage.getREQ_NO() + "' and chargeFlag='0'";					
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + del_pi_sql);
			connect.createStatement().execute(del_pi_sql);
			
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("减项成功！");
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
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}
	
}
