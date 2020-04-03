package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ExamDeleteMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.ExamGDeleteResBean;
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
public class ExamDelSendMessageTJ180 {

	private ExamDeleteMessageBody feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	   static{
	   	init();
	   	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}
		
	public ExamDelSendMessageTJ180(ExamDeleteMessageBody feeMessage){
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
			if (feeMessage.getExam_id() > 0) {
				PersonDeleteBean hbrb = this.getExamInfoForId(feeMessage.getExam_id(), logname);
				json = JSONObject.fromObject(hbrb);// 将java对象转换为json对象
				str = json.toString();// 将json对象转换为字符串
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);

				/*if ("O".equals(hbrb.getReserveType())) {// 团体
					WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
					WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
							.getBean("webserviceConfigurationService");
					url = webserviceConfigurationService.getWebServiceConfig("EXAMT_DELETE").getConfig_url().trim();
				} else if ("P".equals(hbrb.getReserveType())) {// 个人
*/					WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
					WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
							.getBean("webserviceConfigurationService");
					url = webserviceConfigurationService.getWebServiceConfig("EXAMG_DELETE").getConfig_url().trim();
				//}

				String result = HttpUtil.doPost(url, hbrb, "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
				if ((result != null) && (result.trim().length() > 0)) {
					result = result.trim();
					JSONObject jsonobject = JSONObject.fromObject(result);
					/*if ("O".equals(hbrb.getReserveType())) {// 团体
						ExamTDeleteResBean resdah = new ExamTDeleteResBean();
						resdah = (ExamTDeleteResBean) JSONObject.toBean(jsonobject, ExamTDeleteResBean.class);
						if ("200".equals(resdah.getStatus())) {
							rb.setText("");
							rb.setTypeCode("AA");
						} else {
							rb.setTypeCode("AE");
							rb.setText(resdah.getErrorinfo());
						}
					} else if ("P".equals(hbrb.getReserveType())) {// 个人
*/						ExamGDeleteResBean resdah = new ExamGDeleteResBean();
						resdah = (ExamGDeleteResBean) JSONObject.toBean(jsonobject, ExamGDeleteResBean.class);
						if ("200".equals(resdah.getStatus())) {
							rb.setText("");
							rb.setTypeCode("AA");
						} else {
							rb.setTypeCode("AE");
							rb.setText(resdah.getErrorinfo());
						}
					//}
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
	public PersonDeleteBean getExamInfoForId(long examid,String logname) throws ServiceException {
		PersonDeleteBean pb= new PersonDeleteBean();		
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
			StringBuffer sb = new StringBuffer();
			sb.append("select a.exam_num,a.exam_type,c.batch_num,d.group_num,e.com_num from exam_info a ");
			sb.append(" left join  batch c on c.id=a.batch_id ");
			sb.append(" left join group_info d on d.id=a.group_id ");
			sb.append(" left join company_info e on e.id=a.company_id ");
			sb.append("where a.id='"+examid+"' ");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs.next()) {
				pb.setGroupId(rs.getString("group_num"));
				pb.setOrganizationId(rs.getString("com_num"));
				pb.setOrgReserveId(rs.getString("batch_num"));
				pb.setReserveId(rs.getString("exam_num"));				
                if("T".equals(rs.getString("exam_type"))){
                	pb.setReserveType("O");
                }else if("G".equals(rs.getString("exam_type"))){
                	pb.setReserveType("P");
                }
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
