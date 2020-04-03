package com.hjw.webService.client.tianchang;

import java.sql.CallableStatement;
import java.sql.Connection;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessageTC {

	private DelFeeMessage feeMessage;
	
	public DELFEESendMessageTC(DelFeeMessage feeMessage){
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
			String xml = "";
			xml = JaxbUtil.convertToXml(this.feeMessage, true);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
			
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "3-----------------连接成功--------------------");
			
			TranLogTxt.liswriteEror_to_txt(logname,"req:" + ":1、调用存储过程  Pkg_TcTjjk.p_CancelPay");
			String param = feeMessage.getREQ_NO();
			CallableStatement c = connect
					.prepareCall("{call Pkg_TcTjjk.p_CancelPay(?,?,?)}");
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + " - Pkg_TcTjjk.p_CancelPay("
					+ "'"+param+"',"
					+"?,"//OUT--返回Code
					+"?)"//OUT--报错信息
					);
			c.setString(1, param);
			c.registerOutParameter(2, java.sql.Types.INTEGER);
			c.registerOutParameter(3, java.sql.Types.VARCHAR);
			// 执行存储过程
			c.execute();
			c.close();
			
			TranLogTxt.liswriteEror_to_txt(logname,
					"res:" + feeMessage.getREQ_NO() + ":存储过程 Pkg_TcTjjk.p_CancelPay 执行结果————"+ "代码:"+c.getInt(2)+"信息:"+c.getString(3));
			if(c.getInt(2)>0) {
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("减项成功！");
			} else {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(c.getString(3));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("撤销收费存储过程调用异常："+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}
	
}
