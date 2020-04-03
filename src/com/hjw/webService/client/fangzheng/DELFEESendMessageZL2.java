package com.hjw.webService.client.fangzheng;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
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
public class DELFEESendMessageZL2 {

	private DelFeeMessage feeMessage;
	
	public DELFEESendMessageZL2(DelFeeMessage feeMessage){
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
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			String table = url.split("&")[3];
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			String xml = "";
			xml = JaxbUtil.convertToXml(this.feeMessage, true);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
			CallableStatement c = connect.prepareCall("{call "+table+".zl_hjwtj_获取收费状态(?,?,?,?,?)}");
			TranLogTxt.liswriteEror_to_txt(logname, "res:-zl_hjwtj_获取收费状态('" + feeMessage.getPATIENT_ID() + "','"
					+ this.feeMessage.getVISIT_NO() + "','" + feeMessage.getREQ_NO() + "',?)");
			c.setLong(1, Long.valueOf(feeMessage.getPATIENT_ID()));
			c.setLong(2, Long.valueOf(feeMessage.getVISIT_NO()));
			c.setString(3, feeMessage.getREQ_NO());
			c.setString(4, "");
			c.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
			// 执行存储过程
			c.executeUpdate();
			ResultSet rs =(ResultSet) c.getObject(5);
			String sfzt = "";
			String sfztms = "未知状态";
			if (rs.next()) {
				sfzt = rs.getString("收费状态");
				sfztms = "未知状态";
				// 0 未收费 ,1 已收费 2 退费 3 被退费
				if ("0".equals(sfzt)) {
					sfztms = "未收费";
				} else if ("1".equals(sfzt)) {
					sfztms = "已收费";
				} else if ("2".equals(sfzt)) {
					sfztms = "退费";
				} else if ("3".equals(sfzt)) {
					sfztms = "被退费";
				}
			}
			rs.close();
			c.close();
			if (!"0".equals(sfzt)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("费用已经" + sfztms + ",操作不能继续！");
			} else {
				TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_删除费用");
				c = connect.prepareCall("{call "+table+".zl_hjwtj_删除费用(?,?,?)}");
				TranLogTxt.liswriteEror_to_txt(logname, "res:-zl_hjwtj_删除费用('" + this.feeMessage.getREQ_NO() + "','"
						+ feeMessage.getPATIENT_ID() + "',?)");
				c.setString(1, this.feeMessage.getREQ_NO());
				c.setLong(2, Long.valueOf(feeMessage.getPATIENT_ID()));
				c.registerOutParameter(3, java.sql.Types.INTEGER);
				// 执行存储过程
				c.execute();
				int typeid = c.getInt(3);
				TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程 zl_hjwtj_删除费用  返回：" + typeid);
				// 执行存储过程
				c.close();
				if (typeid==0) {
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("");
				} else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(typeid + ":删除错误");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	/*public ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times,a.arch_num ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} */

}
