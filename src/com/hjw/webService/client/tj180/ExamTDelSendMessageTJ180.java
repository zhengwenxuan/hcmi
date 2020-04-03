package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ExamDeleteMessageBody;
import com.hjw.webService.client.body.ExamTDeleteMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.ExamGDeleteResBean;
import com.hjw.webService.client.tj180.Bean.ExamTDeleteResBean;
import com.hjw.webService.client.tj180.Bean.GroupDeleteBean;
import com.hjw.webService.client.tj180.Bean.PersonDeleteBean;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.14	挂号信息服务   天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class ExamTDelSendMessageTJ180 {

	private ExamTDeleteMessageBody feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	   static{
	   	init();
	   	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}
		
	public ExamTDelSendMessageTJ180(ExamTDeleteMessageBody feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage(String url, String logname) {
		ResultHeader rb = new ResultHeader();
		try {
			JSONObject json = JSONObject.fromObject(feeMessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			if (feeMessage.getBarch_id() > 0) {
				GroupDeleteBean hbrb = this.getBatchForId(feeMessage.getBarch_id(), logname);
				json = JSONObject.fromObject(hbrb);// 将java对象转换为json对象
				str = json.toString();// 将json对象转换为字符串
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);

				String result = HttpUtil.doPost(url, hbrb, "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
				if ((result != null) && (result.trim().length() > 0)) {
					result = result.trim();
					JSONObject jsonobject = JSONObject.fromObject(result);
					ExamTDeleteResBean resdah = new ExamTDeleteResBean();
					resdah = (ExamTDeleteResBean) JSONObject.toBean(jsonobject, ExamTDeleteResBean.class);
					if ("200".equals(resdah.getStatus())) {
						rb.setText("");
						rb.setTypeCode("AA");
					} else {
						rb.setTypeCode("AE");
						rb.setText(resdah.getErrorinfo());
					    }
				} else {
					rb.setTypeCode("AE");
					rb.setText("系统返回错误");
				}
			}else{
		    	rb.setTypeCode("AE");
				rb.setText("无效体检编号");	
		    }
		} catch (Exception ex) {
			rb.setTypeCode("AE");
			rb.setText("处理错误");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb).toString());
		return rb;
	}

	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public GroupDeleteBean getBatchForId(long examid,String logname) throws ServiceException {
		GroupDeleteBean pb= new GroupDeleteBean();		
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
			StringBuffer sb = new StringBuffer();
			sb.append("select a.batch_num from batch a ");
			sb.append("where a.id='"+examid+"' ");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs.next()) {
				pb.setOrgReserveId(rs.getString("batch_num"));
			}
			rs.close();
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
		return pb;
	}
}
