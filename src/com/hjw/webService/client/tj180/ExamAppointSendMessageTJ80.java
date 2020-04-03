package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ExamAppMessageBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.tj180.Bean.ExamAppointMessageBody;
import com.hjw.webService.client.tj180.Bean.ResultExamAppointBean;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  病理
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class ExamAppointSendMessageTJ80{
	private ExamAppMessageBody examAppMessageBody;
	private static JdbcQueryManager jdbcQueryManager;
	    static{
	    	init();
	    	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}
	public ExamAppointSendMessageTJ80(ExamAppMessageBody examAppMessageBody){
		this.examAppMessageBody=examAppMessageBody;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultBody sendMessage(String url, String logname) {
		ResultBody rb = new ResultBody();
		try {
			JSONObject json = JSONObject.fromObject(examAppMessageBody);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			String examNo = getzl_req_pacs_item(examAppMessageBody.getExam_id(),
					examAppMessageBody.getPacs_reqno(), logname);
			if ((examNo == null) || (examNo.length() <= 0)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("查询不到检查流水号");
			} else {
				ExamAppointMessageBody ea= new ExamAppointMessageBody();
				ea.setClinSymp(examAppMessageBody.getClinSymp());
				ea.setExamNo(examNo);
				ea.setPhysSign(examAppMessageBody.getPhysSign());
				ea.setRelevantDiag(examAppMessageBody.getRelevantDiag());
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
				
				json = JSONObject.fromObject(ea);// 将java对象转换为json对象
				str = json.toString();// 将json对象转换为字符串
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
				
				String result = HttpUtil.doPost(url, ea, "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
				if ((result != null) && (result.trim().length() > 0)) {
					result = result.trim();
					JSONObject jsonobject = JSONObject.fromObject(result);
					ResultExamAppointBean resdah = new ResultExamAppointBean();
					resdah = (ResultExamAppointBean) JSONObject.toBean(jsonobject, ResultExamAppointBean.class);
					if ("200".equals(resdah.getStatus())) {
						rb.getResultHeader().setTypeCode("AA");
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(resdah.getErrorInfo());
					}
				}
			}
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;
	}	
	
	/**
	 * 
	 * @param exam_info_id
	 * @param pacs_req_no
	 * @param logname
	 * @return
	 * @throws Exception
	 */
	public String getzl_req_pacs_item(long exam_info_id,String pacs_req_no,String logname) throws Exception{
		Connection tjtmpconnect = null;
		String examNo="";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select req_id from zl_req_pacs_item where exam_info_id='"
					+ exam_info_id + "' and pacs_req_code='"+pacs_req_no+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:--:操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				examNo=rs1.getString("req_id");
			} 
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:---: zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return examNo;
	}
}
