package com.hjw.webService.job;

import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.CUSTOMSendMessage;
import com.hjw.webService.client.LISDELSendMessage;
import com.hjw.webService.client.LISSendMessage;
import com.hjw.webService.client.PACSDELSendMessage;
import com.hjw.webService.client.PACSSendMessage;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.ThridReq;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 *  自动发送第三方申请
 */
public class AutoSendThridReqMessage {
	
	private static boolean doing = false;
	private String logName = "autoThridReq";
	
	public ResultHeader getMessage() {
		ResultHeader rh = new ResultHeader();
		try {
			if(!doing) {//如果上一个作业正在处理中，则此作业直接返回
				doing = true;
				
				TranLogTxt.liswriteEror_to_txt(logName, "--------------------" + DateTimeUtil.getDate() + "--------------------");
				WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
				JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
				JdbcPersistenceManager jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
				WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
				ConfigService configService = (ConfigService) wac.getBean("configService");
				
				String sql = "select top 20 id,exam_info_id,exam_num,arch_num,req_no,readFlag,read_time,creater,create_time,databody,notices,errornum,ser_config_key "
						+ " from thrid_req tr "
						+ " where 1=1 "
						+ " and tr.readFlag != 1 "//readFlag:0未处理,1:成功,2:失败
						+ " and tr.errornum < 3 "//已处理三次的数据不再处理
						+ " order by tr.readFlag,tr.create_time ";
				TranLogTxt.liswriteEror_to_txt(logName, "sql: " + sql);
				List<ThridReq> thridReqList = jdbcQueryManager.getList(sql, ThridReq.class);
				//int success = 0;
				for(ThridReq thridReq : thridReqList) {
					try {
						if ("LIS_SEND".equals(thridReq.getSer_config_key())) {
							WebserviceConfigurationDTO wcf = new WebserviceConfigurationDTO();
							wcf = webserviceConfigurationService.getWebServiceConfig("LIS_SEND");
							String web_url = wcf.getConfig_url().trim();
							String web_meth = wcf.getConfig_method().trim();
							
							LisMessageBody pb = new Gson().fromJson(thridReq.getDatabody(), LisMessageBody.class);
							LISSendMessage pm = new LISSendMessage(pb);
							ResultLisBody rb = pm.lisSendImpl(web_url, web_meth, true);
							if ("AA".equals(rb.getResultHeader().getTypeCode())) {// 申请发送成功
								configService.update_thrid_req(1, "", thridReq.getId(), logName);
								//success++;
								/*List<ApplyNOBean> reqList = rb.getControlActProcess().getList();
						for (LisComponents liscoms : pb.getComponents()) {
							for (ApplyNOBean appBean : reqList) {
								if (liscoms.getReq_no().equals(appBean.getApplyNO())) {
									for (LisComponent item : liscoms.getItemList()) {
										String update_sql = "update examinfo_charging_item set is_application = 'Y' "
												+ " where charge_item_id = " + item.getChargingItemid()
												+ " and examinfo_id = " + thridReq.getExam_info_id()
												+ " and isActive = 'Y'";
										jdbcPersistenceManager.executeSql(update_sql); // 修改项目状态
									}
								}
							}
						}*/
							} else {
								configService.update_thrid_req(2, rb.getResultHeader().getText(), thridReq.getId(), logName);
							}
							TranLogTxt.liswriteEror_to_txt(logName, rb.getResultHeader().getText());
						} else if ("LIS_DEL".equals(thridReq.getSer_config_key())) {
							WebserviceConfigurationDTO wcf = new WebserviceConfigurationDTO();
							wcf = webserviceConfigurationService.getWebServiceConfig("LIS_DEL");
							String web_url = wcf.getConfig_url().trim();
							String web_meth = wcf.getConfig_method().trim();
							
							LisMessageBody pb = new Gson().fromJson(thridReq.getDatabody(), LisMessageBody.class);
							LISDELSendMessage pm = new LISDELSendMessage(pb);
							ResultLisBody rb = pm.lisSendImpl(web_url, web_meth, false);
							if ("AA".equals(rb.getResultHeader().getTypeCode())) {// 申请发送成功
								configService.update_thrid_req(1, "", thridReq.getId(), logName);
//						//success++;
//						List<ApplyNOBean> reqList = rb.getControlActProcess().getList();
//						for (LisComponents liscoms : pb.getComponents()) {
//							for (ApplyNOBean appBean : reqList) {
//								if (liscoms.getReq_no().equals(appBean.getApplyNO())) {
//									String update_sql = "update examinfo_charging_item set is_application = 'N' where examinfo_id = "
//											+ thridReq.getExam_info_id()
//											+ " and charge_item_id in(select ec.charging_id from sample_exam_detail s,examResult_chargingItem ec "
//											+ " where s.id = ec.exam_id and ec.result_type = 'sample' and s.sample_barcode = '"
//											+ liscoms.getReq_no() + "' and isActive = 'Y')";
//									jdbcPersistenceManager.executeSql(update_sql); // 修改项目状态
//								}
//							}
//						}
							} else {
								configService.update_thrid_req(2, rb.getResultHeader().getText(), thridReq.getId(), logName);
							}
							TranLogTxt.liswriteEror_to_txt(logName, rb.getResultHeader().getText());
						}else if ("PACS_SEND".equals(thridReq.getSer_config_key())) {
							WebserviceConfigurationDTO wcf = new WebserviceConfigurationDTO();
							wcf = webserviceConfigurationService.getWebServiceConfig("PACS_SEND");
							String web_url = wcf.getConfig_url().trim();
							String web_meth = wcf.getConfig_method().trim();
							
							PacsMessageBody pb = new Gson().fromJson(thridReq.getDatabody(), PacsMessageBody.class);
							PACSSendMessage pm = new PACSSendMessage(pb);
							ResultPacsBody rb = pm.pacsSendImpl(web_url, web_meth, true);
							if ("AA".equals(rb.getResultHeader().getTypeCode())) {// 申请发送成功
								configService.update_thrid_req(1, "", thridReq.getId(), logName);
//						//success++;
//						List<ApplyNOBean> reqList = rb.getControlActProcess().getList();
//						for (LisComponents liscoms : pb.getComponents()) {
//							for (ApplyNOBean appBean : reqList) {
//								if (liscoms.getReq_no().equals(appBean.getApplyNO())) {
//									for (LisComponent item : liscoms.getItemList()) {
//										String update_sql = "update examinfo_charging_item set is_application = 'Y' "
//												+ " where charge_item_id = " + item.getChargingItemid()
//												+ " and examinfo_id = " + thridReq.getExam_info_id()
//												+ " and isActive = 'Y'";
//										jdbcPersistenceManager.executeSql(update_sql); // 修改项目状态
//									}
//								}
//							}
//						}
							} else {
								configService.update_thrid_req(2, rb.getResultHeader().getText(), thridReq.getId(), logName);
							}
							TranLogTxt.liswriteEror_to_txt(logName, rb.getResultHeader().getText());
						} else if ("PACS_DEL".equals(thridReq.getSer_config_key())) {
							WebserviceConfigurationDTO wcf = new WebserviceConfigurationDTO();
							wcf = webserviceConfigurationService.getWebServiceConfig("PACS_DEL");
							String web_url = wcf.getConfig_url().trim();
							String web_meth = wcf.getConfig_method().trim();
							
							PacsMessageBody pb = new Gson().fromJson(thridReq.getDatabody(), PacsMessageBody.class);
							PACSDELSendMessage pm = new PACSDELSendMessage(pb);
							ResultPacsBody rb = pm.pacsSendImpl(web_url, web_meth, true);
							if ("AA".equals(rb.getResultHeader().getTypeCode())) {// 申请发送成功
								configService.update_thrid_req(1, "", thridReq.getId(), logName);
//						//success++;
//						List<ApplyNOBean> reqList = rb.getControlActProcess().getList();
//						for (LisComponents liscoms : pb.getComponents()) {
//							for (ApplyNOBean appBean : reqList) {
//								if (liscoms.getReq_no().equals(appBean.getApplyNO())) {
//									for (LisComponent item : liscoms.getItemList()) {
//										String update_sql = "update examinfo_charging_item set is_application = 'Y' "
//												+ " where charge_item_id = " + item.getChargingItemid()
//												+ " and examinfo_id = " + thridReq.getExam_info_id()
//												+ " and isActive = 'Y'";
//										jdbcPersistenceManager.executeSql(update_sql); // 修改项目状态
//									}
//								}
//							}
//						}
							} else {
								configService.update_thrid_req(2, rb.getResultHeader().getText(), thridReq.getId(), logName);
							}
							TranLogTxt.liswriteEror_to_txt(logName, rb.getResultHeader().getText());
						} else if ("CUST_APPLICATION".equals(thridReq.getSer_config_key())) {
							WebserviceConfigurationDTO wcf = new WebserviceConfigurationDTO();
							wcf = webserviceConfigurationService.getWebServiceConfig("CUST_APPLICATION");
							String web_url = wcf.getConfig_url().trim();
							String web_meth = wcf.getConfig_method().trim();
							
							Custom pb = new Gson().fromJson(thridReq.getDatabody(), Custom.class);
							System.out.println(pb.getEXAM_NUM());
							CUSTOMSendMessage cm = new CUSTOMSendMessage(pb);
							ResultBody rb = cm.customSendImpl(web_url, web_meth, true);
							if ("AA".equals(rb.getResultHeader().getTypeCode())) {
								String update_sql = "update exam_info set visit_date = '"+rb.getControlActProcess().getLIST().get(0).getVISIT_DATE()+"',"
										+ "visit_no='"+rb.getControlActProcess().getLIST().get(0).getVISIT_NO()+"',clinic_no='"+rb.getControlActProcess().getLIST().get(0).getCLINIC_NO()+"',"
										+ "patient_id='"+rb.getControlActProcess().getLIST().get(0).getPATIENT_ID()+"' where id ="+thridReq.getExam_info_id();
								jdbcPersistenceManager.executeSql(update_sql);
								TranLogTxt.liswriteEror_to_txt(logName, "update_sql:"+update_sql);
								configService.update_thrid_req(1, "", thridReq.getId(), logName);
							} else {
								configService.update_thrid_req(2, rb.getResultHeader().getText(), thridReq.getId(), logName);
							}
							TranLogTxt.liswriteEror_to_txt(logName, rb.getResultHeader().getText());
						}
					}catch(Exception ex){
						TranLogTxt.liswriteEror_to_txt(logName,"错误提示："+com.hjw.interfaces.util.StringUtil.formatException(ex));
						configService.update_thrid_req(2, "Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex), thridReq.getId(), logName);
					}
				}
				doing = false;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logName,"错误提示："+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rh;
	}
}
