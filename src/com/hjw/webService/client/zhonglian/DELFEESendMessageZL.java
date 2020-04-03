package com.hjw.webService.client.zhonglian;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.19	项目减项  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessageZL {

	private DelFeeMessage feeMessage;
	
	public DELFEESendMessageZL(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url,String logname) {
		FeeReqBody rb = new FeeReqBody();
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			String sql = "select * from zl_km体检人员收费情况 where 单据号='" + this.feeMessage.getREQ_NO() + "' and 体检号='"
					+ this.feeMessage.getExam_num() + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + feeMessage.getREQ_NO() + ":" + sql);
			ResultSet rs = connect.createStatement().executeQuery(sql);
			if (rs.next()) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("已经缴费，不能删除");
			} else {
				TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_km体检费用明细_delete");
				CallableStatement c = connect.prepareCall("{call zl_km体检费用明细_delete(?,?)}");
				TranLogTxt.liswriteEror_to_txt(logname, "res:-zl_km体检费用明细_delete('" + this.feeMessage.getExam_num()
						+ "','" + feeMessage.getREQ_NO() + "')");
				c.setString(1, this.feeMessage.getExam_num());
				c.setString(2, feeMessage.getREQ_NO());
				// 执行存储过程
				c.execute();
				c.close();

				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("");
			}
			rs.close();
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
