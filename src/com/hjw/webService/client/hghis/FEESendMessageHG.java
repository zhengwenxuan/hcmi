package com.hjw.webService.client.hghis;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;

public class FEESendMessageHG {
	private FeeMessage feeMessage;
	public FEESendMessageHG(FeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url,String logname) {
		FeeResultBody rb = new FeeResultBody();
		Connection orecal_connect = null;
		Statement statement = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			orecal_connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			statement = orecal_connect.createStatement();
			boolean flags = true;
			orecal_connect.setAutoCommit(false);
			try {
				TranLogTxt.liswriteEror_to_txt(logname, "req:开始往HIS写入申请信息!");
				List<Fee> list = feeMessage.getPROJECTS().getPROJECT();
				for(Fee fee : list){
					String sql = "insert into mzys_treatment(OUTPATIENTNO,NAME,ORDERDOCTOR,ORDERTIME,ITEMNO,"
							+ "EXPENSESNO,PRACE,QUANTITY,EXPENSES,DEPARTMENT,DEPARTMENT1,STATIONNO,"
							+ "healthcheckno,healthselffee,groupid, TJBATCHNO,TJUNITNAME) "
							+ "values('"+fee.getVISIT_NO()+"','"+fee.getUSER_NAME()+"','"+fee.getYB_DOCTOR()+"','"+fee.getVISIT_DATE()+"','"+fee.getITEM_CODE()+"',"
							+ "'"+fee.getSUBJ_CODE()+"','"+fee.getITEM_PRICE()+"','"+fee.getAMOUNT()+"','"+fee.getCHARGES()+"','"+fee.getORDERED_BY_DEPT()+"','"+fee.getPERFORMED_BY()+"','1',"
							+ "'"+fee.getEXAM_NUM()+"','"+fee.getINSURANCE_FLAG()+"','"+fee.getBILL_NO()+"','"+fee.getRCPT_NO()+"','"+fee.getSERIAL_NO()+"')";
					TranLogTxt.liswriteEror_to_txt(logname, "req:插入sql:"+sql);
					statement.executeUpdate(sql);
				}
				TranLogTxt.liswriteEror_to_txt(logname, "req:缴费申请信息写入成功!");
			} catch (Exception e) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				flags = false;
			}
			if (!flags) {
				orecal_connect.rollback();
			} else {		
				orecal_connect.commit();
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("缴费申请成功!");
			}
		} catch (Exception e) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("链接his数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {
				if (statement != null){
					statement.close();
				}
				if (orecal_connect != null) {
					OracleDatabaseSource.close(orecal_connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rb;
	}
}
