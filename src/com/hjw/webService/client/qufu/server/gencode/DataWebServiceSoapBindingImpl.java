/**
 * DataWebServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.qufu.server.gencode;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.qufu.server.ExamRequestStatus_EL_Update;
import com.hjw.webService.client.qufu.server.ExamRequestStatus_LIS_Update;
import com.hjw.webService.client.qufu.server.ExamRequestStatus_PACS_Update;
import com.hjw.webService.client.qufu.server.ExamRequest_EL_ByExam_num;
import com.hjw.webService.client.qufu.server.ExamRequest_EL_ByTime;
import com.hjw.webService.client.qufu.server.ExamRequest_LIS_ByBarcode;
import com.hjw.webService.client.qufu.server.ExamRequest_LIS_ByTime;
import com.hjw.webService.client.qufu.server.ExamRequest_PACS_ByExam_num;
import com.hjw.webService.client.qufu.server.ExamRequest_PACS_ByTime;
import com.hjw.webService.client.qufu.server.bean.RequestBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.webService.client.Bean.ThridInterfaceLog;


import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class DataWebServiceSoapBindingImpl extends ServletEndpointSupport implements com.hjw.webService.client.qufu.server.gencode.DataWebServiceSoap{
	
	private JdbcQueryManager jdbcQueryManager;
	private ConfigService configService;
	private ThridInterfaceLog til;
	
	@Override
	protected void onInit() {
		this.jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
		this.configService = (ConfigService) getWebApplicationContext().getBean("configService");
	}
	
	public String acceptMessageTest(String xmlmessage) {
		return "返回ok----参数为：" + xmlmessage;
	}
	
    public com.hjw.webService.client.qufu.server.gencode.Response checkInfoQuery(com.hjw.webService.client.qufu.server.gencode.Request request) throws java.rmi.RemoteException {
    	String message_name = "";
    	if("ExamRequest".equals(request.getRequestHeader().getMsgType())) {
    		message_name = "PF_GET_ORDER";
    	} else if("ExamRequestStatus".equals(request.getRequestHeader().getMsgType())) {
    		message_name = "PF_STATUS_UPDATE";
    	}
    	
    	til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setMessage_id(request.getRequestHeader().getMsgId());
    	til.setMessage_name(message_name);
    	til.setMessage_type("webservice");
    	til.setSender("PF");
    	til.setReceiver("PEIS");
    	til.setMessage_request(request.getRequestBody());
    	til.setFlag(2);
    	til.setXtgnb_id("0");
    	til.setMessage_inout(1);
		configService.insert_log(til);
		
//		String reqxml = JaxbUtil.convertToXmlWithCDATA(request, "^requestBody");
//		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "req:" + reqxml);
		
		RequestBody requestBody = new RequestBody(request.getRequestBody(), til);
		String msgType = request.getRequestHeader().getMsgType();
		Response response = new Response();
		
		if("ExamRequest".equals(msgType)) {//体检申请单查询接口
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "体检申请单查询" + msgType + ":" +requestBody.getEXAM_NO()
			+"("+requestBody.getSTARTTIME()+","+requestBody.getENDTIME()+")"+requestBody.getEXECDEPTID());
			
			if("0".equals(requestBody.getPROJECT_TYPE())) {//0-PACS,
				if(StringUtil.isEmpty(requestBody.getEXAM_NO().trim())) {
					ExamRequest_PACS_ByTime service = new ExamRequest_PACS_ByTime(til);
					response = service.checkInfoQuery(requestBody);
				} else {
					ExamRequest_PACS_ByExam_num service = new ExamRequest_PACS_ByExam_num(til);
					response = service.checkInfoQuery(requestBody);
				}
			} else if("1".equals(requestBody.getPROJECT_TYPE())) {//1-LIS
				if(StringUtil.isEmpty(requestBody.getEXAM_NO().trim())) {
					ExamRequest_LIS_ByTime service = new ExamRequest_LIS_ByTime(til);
					response = service.checkInfoQuery(requestBody);
				} else {
					ExamRequest_LIS_ByBarcode service = new ExamRequest_LIS_ByBarcode(til);
					response = service.checkInfoQuery(requestBody);
				}
			} else if("2".equals(requestBody.getPROJECT_TYPE())) {//2-心电
				if(StringUtil.isEmpty(requestBody.getEXAM_NO().trim())) {
					ExamRequest_EL_ByTime service = new ExamRequest_EL_ByTime(til);
					response = service.checkInfoQuery(requestBody);
				} else {
					ExamRequest_EL_ByExam_num service = new ExamRequest_EL_ByExam_num(til);
					response = service.checkInfoQuery(requestBody);
				}
			} else {
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "缺少必填字段:PACS/LIS判断字段错误————PROJECT_TYPE:"+requestBody.getPROJECT_TYPE());
				response.getResponseHeader().setErrCode("1003");
				response.getResponseHeader().setErrMessage("缺少必填字段:PACS/LIS判断字段错误————PROJECT_TYPE:"+requestBody.getPROJECT_TYPE());
			}
		} else if("ExamRequestStatus".equals(msgType)) {//体检申请单更新接口
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "体检申请单更新" + msgType + ":" 
					+requestBody.getEXAM_NO()+"-"+requestBody.getBAR_CODE()+"-"+requestBody.getFLAG());
			ExamInfoUserDTO eu = getExamInfoForNum(requestBody.getEXAM_NO());
			if ((eu == null) || (eu.getId() <= 0)) {
				response.getResponseHeader().setErrCode("5000");
				response.getResponseHeader().setErrMessage("根据体检编号【"+requestBody.getEXAM_NO()+"】查无此人。");
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据体检编号【"+requestBody.getEXAM_NO()+"】查无此人。");
			} else {
				String dep_num = checkPacs(requestBody.getEXAM_NO(), requestBody.getBAR_CODE());
				if(!StringUtil.isEmpty(dep_num)) {//PACS/心电
					if("EL".equals(dep_num)) {
						ExamRequestStatus_EL_Update service = new ExamRequestStatus_EL_Update(til);
						response = service.checkInfoQuery(requestBody);
					} else {
						ExamRequestStatus_PACS_Update service = new ExamRequestStatus_PACS_Update(til);
						response = service.checkInfoQuery(requestBody);
					}
				} else if(checkLis(eu.getId(), requestBody.getBAR_CODE())) {//LIS
					ExamRequestStatus_LIS_Update service = new ExamRequestStatus_LIS_Update(til);
					response = service.checkInfoQuery(requestBody);
				} else {
					response.getResponseHeader().setErrCode("5000");
					response.getResponseHeader().setErrMessage("根据体检编号【"+requestBody.getEXAM_NO()+"】查不到单号【"+requestBody.getBAR_CODE()+"】");
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据体检编号【"+requestBody.getEXAM_NO()+"】查不到单号【"+requestBody.getBAR_CODE()+"】");
				}
			}
		} else {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "不支持的消息类型————msgType:" + msgType);
			response.getResponseHeader().setErrCode("2000");
			response.getResponseHeader().setErrMessage("不支持的消息类型————msgType:" + msgType);
		}
		
		response.getResponseHeader().setSender(request.getRequestHeader().getReceiver());
		response.getResponseHeader().setReceiver(request.getRequestHeader().getSender());
		response.getResponseHeader().setRequestTime(DateTimeUtil.getDateTimes());
		response.getResponseHeader().setMsgType(request.getRequestHeader().getMsgType());
		response.getResponseHeader().setMsgId(request.getRequestHeader().getMsgId());
		response.getResponseHeader().setMsgPriority(request.getRequestHeader().getMsgPriority());//应答优先级暂设为和请求优先级相同
		response.getResponseHeader().setMsgVersion("1.0.0");
//		String resxml = JaxbUtil.convertToXmlWithCDATA(response, "^responseBody");
//		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:" + resxml);
		
		if("0".equals(response.getResponseHeader().getErrCode())) {
			til.setFlag(0);
			til.setMessage_response(response.getResponseBody());
		} else {
			til.setFlag(2);
			til.setMessage_response(response.getResponseHeader().getErrMessage());
		}
    	configService.update_log(til);
		return response;
	}

    private ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type,c.is_marriage,c.is_after_pay"
				+ ",c.register_date,c.join_date,c.exam_times,c.phone,c.company,c.address,a.arch_num,a.birthday,a.id_num ");
		sb.append(" from customer_info a,exam_info c ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "req:" +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
    
    private boolean checkLis(long examid, String sample_barcode) throws ServiceException {
		String sql = "select * from sample_exam_detail where exam_info_id=" + examid + " and sample_barcode='" + sample_barcode + "'";
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "sql:" + sql);
		boolean isLis = false;
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = jdbcQueryManager.getConnection();
			rs = connection.createStatement().executeQuery(sql);
			if(rs.next()) {
				isLis = true;
			}
		} catch (Exception e) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return isLis;
	}
	
	private String checkPacs(String exam_num, String req_code) throws ServiceException {
		String sql = "select d.dep_num from pacs_summary p,pacs_detail d "
				+ " where p.id = d.summary_id and p.examinfo_num = '"+exam_num+"' and p.pacs_req_code = '"+req_code+"'";
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "sql:" + sql);
		String dep_num = "";
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = jdbcQueryManager.getConnection();
			rs = connection.createStatement().executeQuery(sql);
			if(rs.next()) {
				dep_num = rs.getString("dep_num");
			}
		} catch (Exception e) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return dep_num;
	}
}
