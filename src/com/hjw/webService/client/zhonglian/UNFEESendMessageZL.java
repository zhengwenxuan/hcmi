package com.hjw.webService.client.zhonglian;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeReqControlActProcess;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.17	收费退费
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class UNFEESendMessageZL {

	private UnFeeMessage feeMessage;
	
	public UNFEESendMessageZL(UnFeeMessage feeMessage){
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
		String dburl = url.split("&")[0];
		String user = url.split("&")[1];
		String passwd = url.split("&")[2];
		String table = url.split("&")[3];
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
		try {
			//connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			List<ReqNo> okList = new ArrayList<ReqNo>();
			for (String reqNo : this.feeMessage.getREQ_NOS().getREQ_NO()) {					
						ReqNo req = new ReqNo();
						req.setREQ_NO(reqNo);
						okList.add(req);
			}
			FeeReqControlActProcess controlActProcess = new FeeReqControlActProcess();
			controlActProcess.setList(okList);
			rb.setControlActProcess(controlActProcess);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("退费已经成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
		return rb;
	}
}
