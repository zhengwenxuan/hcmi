package com.hjw.webService.client.erfuyuan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.DTO.ExamQueueLog;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueAddBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.erfuyuan.bean.ClientInfo_clientRegister;
import com.hjw.webService.client.erfuyuan.bean.DataContent;
import com.hjw.webService.client.erfuyuan.bean.Item_clientRegister;
import com.hjw.webService.client.erfuyuan.bean.Request_clientRegister;
import com.hjw.webService.client.erfuyuan.bean.Request_qmModifyItems;
import com.hjw.webService.client.erfuyuan.bean.Response_clientRegister;
import com.hjw.webService.client.erfuyuan.bean.Response_qmModifyItems;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class QueueAddSendMessageEFY {
	
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public QueueResBody getMessage(String url, QueueAddBean queueAddBean, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
		QueueResBody rb = new QueueResBody();
		try {
			JSONObject json = JSONObject.fromObject(queueAddBean);// 将java对象转换为json对象
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + json);
			
			ExamInfoUserDTO eu = configService.getExamInfoForExam_id(queueAddBean.getExam_id());
			boolean ret = selectExamQueueLog(eu.getExam_num(), logname);
			if(ret) {//今天已调用过第一接口
				String url2 = url.split("&")[1];
				Request_qmModifyItems request = get_qmModifyItems_Data(queueAddBean.getExam_id(), logname);
				if(StringUtil.isEmpty(request.getClientId())) {
					rb.setRestext("不调用排队接口");
					rb.setRescode("AA");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb));
					return rb;
				}
				TranLogTxt.liswriteEror_to_txt(logname, "request:" + JSONObject.fromObject(request));
				String result = HttpUtil.doPost(url2, request.covertToMap(), "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "result:" + result);
				Response_qmModifyItems response = new Gson().fromJson(result, Response_qmModifyItems.class);
				if ("0".equals(response.getStatus())) {
					DataContent dataContent = JaxbUtil.converyToJavaBean(response.getResult(), DataContent.class);
					boolean suc = updateExamQueueLog(dataContent.getClient().getCategoryid(), eu.getExam_num(), logname);
					if(suc) {
						rb.setRestext("成功");
						rb.setRescode("AA");
					} else {
						TranLogTxt.liswriteEror_to_txt(logname, "更新exam_queue_log表失败");
					}
				}else if ("12001".equals(response.getStatus())) { 
					rb.setRestext("体检者ID不正确或不存在");
					rb.setRescode("AE");
				}else if ("12002".equals(response.getStatus())) { 
					rb.setRestext("返回失败");
					rb.setRescode("AE");
				}else {
					rb.setRestext("不识别的错误代码："+response.getStatus());
					rb.setRescode("AE");
				}
			} else {//今天未调用过第一接口
				String url1 = url.split("&")[0];
				ClientInfo_clientRegister clientInfo = get_clientInfo_Data(eu, logname);
				if(clientInfo.getItems().getItem().size() == 0) {
					rb.setRestext("不调用排队接口");
					rb.setRescode("AA");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb));
					return rb;
				}
				String xmlString = JaxbUtil.convertToXmlWithOutHead(clientInfo, true);
				Request_clientRegister request= new Request_clientRegister();
				request.setXmlString(xmlString);
				
				TranLogTxt.liswriteEror_to_txt(logname, "request:" + JSONObject.fromObject(request));
				String result = HttpUtil.doPost(url1, request, "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + result);

				JSONObject jsonobject = JSONObject.fromObject(result.trim());
				Response_clientRegister resdah = (Response_clientRegister) JSONObject.toBean(jsonobject, Response_clientRegister.class);
				System.out.println("==="+resdah.getStatus()+"===");
				if ("10000".equals(resdah.getStatus())) {
					ExamQueueLog examQueueLog = new ExamQueueLog();
					examQueueLog.setExam_num(resdah.getResult().getClientId());
					examQueueLog.setQueue_no(resdah.getResult().getFirstQueue());
					boolean inserflag = configService.insertExamQueueLog(examQueueLog, logname);
					if (inserflag) {
						rb.setRestext("");
						rb.setRescode("AA");
					} else {
						rb.setRestext("排队成功，但写本地日志表exam_queue_log失败，详情请看日志文件");
						rb.setRescode("AE");
					}
				} else if ("12001".equals(resdah.getStatus())) {
					rb.setRestext("体检者ID不正确或不存在");
					rb.setRescode("AE");
				} else if ("12002".equals(resdah.getStatus())) {
					rb.setRestext("返回失败");
					rb.setRescode("AE");
				} else {
					rb.setRestext("不识别的错误代码："+resdah.getStatus());
					rb.setRescode("AE");
				}
			}
		} catch (Exception ex) {
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");
		}

		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + json);
		return rb;
	}
	
	private ClientInfo_clientRegister get_clientInfo_Data(ExamInfoUserDTO eu,String logname){
		Connection tjtmpconnect = null;
		ClientInfo_clientRegister clientInfo = new ClientInfo_clientRegister();
		clientInfo.setName(eu.getUser_name());
		clientInfo.setSex(eu.getSex());
		clientInfo.setAge(""+eu.getAge());
		clientInfo.setClientid(eu.getExam_num());
		clientInfo.setIdcard(eu.getId_num());
		clientInfo.setPhone(eu.getPhone());
		clientInfo.setRegno("");
		clientInfo.setViplevel(eu.getVipflag());
		clientInfo.setRegdate(DateTimeUtil.getDateTime());
		try {
			tjtmpconnect = jdbcQueryManager.getConnection();
			String sql = "SELECT ci.item_name "
						+ "FROM charging_item ci,examinfo_charging_item eci, exam_info ei "
						+ " where eci.charge_item_id = ci.id and ei.id = eci.examinfo_id "
						+ " and ( eci.pay_status IN ('Y', 'R') OR ( ei.is_after_pay = 'Y' AND eci.pay_status IN ('Y', 'R', 'N'))) "
						+ " AND eci.isActive = 'Y' AND ci.item_category != '耗材类型' AND ei.id = "+eu.getId();
			TranLogTxt.liswriteEror_to_txt(logname, "res:SQL操作语句==： " +sql);
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sql);
			while (rs.next()) {
				Item_clientRegister item = new Item_clientRegister();
				item.setValue(rs.getString("item_name"));
				clientInfo.getItems().getItem().add(item);
			}
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "查询异常===:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return clientInfo;
	}
	
	/**
	 * 插入exam_queue_log表
	 * @param exam_num
	 * @param queueNo
	 * @param logname
	 * @return
	 */
	private boolean selectExamQueueLog(String exam_num,String logname){
		Connection tjtmpconnect = null;
		boolean tjvip=false;
		try {
			tjtmpconnect = jdbcQueryManager.getConnection();
			String sb1 = "select * from exam_queue_log where exam_num = '"+exam_num+"' and queue_day = '"+DateTimeUtil.getDate()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "查询exam_queue_log表SQL:" +sb1);				
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			if(rs.next()) {
				tjvip=true;
			}
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:  插入数据操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return tjvip;
	}
	
	public boolean updateExamQueueLog(String queue_no, String exam_num,String logname) throws ServiceException {
		Connection connect = null;
		int resflag = -1;
		try {
			connect = jdbcQueryManager.getConnection();
			String sb1 = "update exam_queue_log set queue_no = '"+queue_no+"' where exam_num = '"+exam_num+"' and queue_day = '"+DateTimeUtil.getDate()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "更新exam_queue_log表SQL:" +sb1);				
			resflag = connect.createStatement().executeUpdate(sb1);
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:  插入数据操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return resflag>0;
	}

	private Request_qmModifyItems get_qmModifyItems_Data(long exam_id, String logname) {
		Connection tjtmpconnect = null;
		Request_qmModifyItems request= new Request_qmModifyItems();
		try {
			tjtmpconnect = jdbcQueryManager.getConnection();
			String add_sql = " SELECT ei.exam_num, ci.item_name "
					+ " FROM exam_info ei "
					+ " LEFT JOIN examinfo_charging_item eci ON ei.id = eci.examinfo_id "
					+ " LEFT JOIN charging_item ci ON eci.charge_item_id = ci.id "
					+ " WHERE ( eci.pay_status IN ('Y', 'R') OR ( ei.is_after_pay = 'Y' AND eci.pay_status IN ('Y', 'R', 'N')))"
					+ " AND eci.isActive = 'Y' "
					+ " AND ci.item_category != '耗材类型' "
					+ " AND ei.id = "+exam_id;
			TranLogTxt.liswriteEror_to_txt(logname, "add_sql： " +add_sql);
			ResultSet add_rs = tjtmpconnect.createStatement().executeQuery(add_sql);
			String itemsAdd = "";
			while (add_rs.next()) {
				if(StringUtil.isEmpty(request.getClientId())) {
					request.setClientId(add_rs.getString("exam_num"));
				}
				itemsAdd += (add_rs.getString("item_name") + ";");
			}
			add_rs.close();
			
			if(itemsAdd.length() > 1) {
				itemsAdd = itemsAdd.substring(0, itemsAdd.length()-1);
				request.setItemsAdd(itemsAdd);
			}
			String remove_sql = " SELECT ei.exam_num, ci.item_name "
					+ " FROM exam_info ei "
					+ " LEFT JOIN examinfo_charging_item eci ON ei.id = eci.examinfo_id "
					+ " LEFT JOIN charging_item ci ON eci.charge_item_id = ci.id "
					+ " WHERE eci.pay_status IN ('N', 'R', 'M')"
					+ " AND eci.isActive = 'N' "
					+ " AND ci.item_category != '耗材类型' "
					+ " AND ei.id = "+exam_id;
			TranLogTxt.liswriteEror_to_txt(logname, "remove_sql： " +remove_sql);
			ResultSet remove_rs = tjtmpconnect.createStatement().executeQuery(remove_sql);
			String itemsRemove = "";
			while (remove_rs.next()) {
				if(StringUtil.isEmpty(request.getClientId())) {
					request.setClientId(remove_rs.getString("exam_num"));
				}
				itemsRemove += (remove_rs.getString("item_name") + ";");
			}
			remove_rs.close();
			if(itemsRemove.length() > 1) {
				itemsRemove = itemsRemove.substring(0, itemsRemove.length()-1);
				request.setItemsRemove(itemsRemove);
			}
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return request;
	}
}
