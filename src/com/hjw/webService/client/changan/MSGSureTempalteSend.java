package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.EncryptUtils;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.MSGSendBean;
import com.hjw.webService.client.body.MSGResBody;
import com.hjw.webService.client.changan.Bean.MSGSureTempalteResBean;
import com.hjw.webService.client.changan.Bean.MSGSureTempalteSendBean;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class MSGSureTempalteSend {

	private static JdbcQueryManager jdbcQueryManager;
	static{
		init(); 
		 }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public MSGResBody getMessage(WebserviceConfigurationDTO wcd, MSGSendBean eu, String logname) {
		MSGResBody rb = new MSGResBody();
		try {
			JSONObject json = JSONObject.fromObject(eu);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			String url = wcd.getConfig_value();
			String wcdConfigUrl = wcd.getConfig_url();
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);

			Connection tjtmpconnect = null;			
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String sb1 = "select a.id,a.template_id,a.sms_note,a.sms_phone,a.sms_date,a.send_user,a.sms_status,a.sms_amount,"
						+ "a.sms_batch,a.sms_type,a.user_id,a.sms_time,a.user_type,u.chi_name,u.log_name from crm_sms_send a "
						+ "  left join user_usr u on u.id=a.send_user "
						+ "  where a.sms_batch='"+eu.getBatchCode()+"' and a.sms_status!=1 ";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb1);
				ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
				while (rs.next()) {
					try {
						StringBuffer s = new StringBuffer();
						s.append(rs.getString("id") + "\n,");
						s.append(rs.getString("template_id") + "\n,");
						s.append(rs.getString("sms_note") + "\n,");
						s.append(rs.getString("sms_phone") + "\n,");
						s.append(rs.getString("sms_date") + "\n,");
						s.append(rs.getLong("send_user") + "\n,");
						s.append(rs.getInt("sms_status") + "\n,");
						s.append(rs.getInt("sms_type") + "\n,");
						s.append(rs.getLong("user_id") + "\n,");
						s.append(rs.getInt("user_type") + "\n,");
						s.append(rs.getString("chi_name") + "\n,");
						s.append(rs.getString("log_name") + "\n,");
						String smssendid = rs.getString("id");
						TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句:" + s.toString());
						String template_id = rs.getString("template_id");
						String sms_note = rs.getString("sms_note");
						String sms_phone = rs.getString("sms_phone");
						String sms_date = rs.getString("sms_date");
						long send_user = rs.getLong("send_user");
						int sms_status = rs.getInt("sms_status");
						int sms_type = rs.getInt("sms_type");
						long user_id = rs.getLong("user_id");
						int user_type = rs.getInt("user_type");
						String send_user_name = rs.getString("chi_name");
						String send_user_code = rs.getString("log_name");
						TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句：手机号" + sms_phone + "---" + (sms_phone != null)
								+ "--" + (sms_phone.trim().length() == 11));
						
						
						Map<String,String> sms_map = getSmsTemplateInfo(template_id, logname);
						
						MSGSureTempalteSendBean mq = new MSGSureTempalteSendBean();
						String apiAccount=getAppAccount(wcdConfigUrl, 3);
						String appId=getAppAccount(wcdConfigUrl, 2);
						String apiKey=getAppAccount(wcdConfigUrl, 1);
						String reqUrl=getAppAccount(wcdConfigUrl, 4);
						long timeStamp=System.currentTimeMillis();
						String sign=EncryptUtils.encryptMD5(apiAccount+apiKey+timeStamp);
						String param="";
						param=getMsg_Param(sms_note, user_id+"", logname);
						mq.setApiAccount(apiAccount);
						mq.setAppId(appId);
						mq.setSign(sign);
						mq.setTimeStamp(String.valueOf(timeStamp));
						mq.setTemplateId(sms_map.get("zy_templateId"));
						mq.setSingerid(sms_map.get("zy_singerId"));
						mq.setMobile(sms_phone);
						mq.setParam(param);
//						mq.setUserDate("");
//						mq.setExtNumber("");
//						mq.setStatusPushAddr("");

						json = JSONObject.fromObject(mq);// 将java对象转换为json对象
						String mqstr = json.toString();// 将json对象转换为字符串
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + sms_phone + "-" + mqstr);
						Map<String,Object> mapStr = new HashMap<String,Object>();
						mapStr.put("reqUrl", reqUrl);
						mapStr.put("reqJson", json);
						String result = HttpUtil.doPostParam(url, mapStr, "utf-8");
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
						System.out.println(logname+":   res:" + result);
						if ((result != null) && (result.trim().length() > 0)) {
							result = result.trim();
							JSONObject jsonobject = JSONObject.fromObject(result);
							MSGSureTempalteResBean resdah = new MSGSureTempalteResBean();
							resdah = (MSGSureTempalteResBean) JSONObject.toBean(jsonobject, MSGSureTempalteResBean.class);
							TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
							if (resdah.getStatus()==0) {
								rb.setRestext("");
								rb.setRescode("AA");
								rb.setIdnumber("");
								tjtmpconnect.createStatement().execute("update crm_sms_send set sms_status=1,sms_fal_notice='' where id='"
												+ smssendid + "'");
							} else {
								rb.setRestext("");
								rb.setRescode("AE");
								rb.setIdnumber("");
								tjtmpconnect.createStatement().execute("update crm_sms_send set sms_status=2,sms_fal_notice='" + resdah.getDesc()
												+ "' where id='" + smssendid + "'");
							}
						}else {
							rb.setRestext("");
							rb.setRescode("AE");
							rb.setIdnumber("");
						}
					}catch(Exception ex){
						TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
						rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
						rb.setRescode("AE");
						rb.setIdnumber("");
						ex.printStackTrace();
					}
				}
				rs.close();
			} catch (SQLException ex) {
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
				rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
				rb.setRescode("AE");
				rb.setIdnumber("");
			} finally {
				try {
					if (tjtmpconnect != null) {
						tjtmpconnect.close();
					}
				} catch (SQLException sqle4) {
					sqle4.printStackTrace();
				}
			}

		} catch (Exception ex) {
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");
			rb.setIdnumber("");
		}

		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "re1:" + str);
		return rb;
	}
	
	/**
	 * 获取短信模板表信息
	 * @param template_id
	 * @param logname
	 * @return
	 */
	public Map<String,String> getSmsTemplateInfo(String template_id,String logname){
		Map<String,String> reMap = new HashMap<String,String>();

		Connection catmpconnect = null;
		try {
			catmpconnect = this.jdbcQueryManager.getConnection();
				StringBuffer sb = new StringBuffer();
				sb.append("select id,sms_name,sms_note,zy_templateId,zy_singerId from crm_sms_base_template where id= '"+template_id+"'");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb.toString());		
				ResultSet rssms = catmpconnect.createStatement().executeQuery(sb.toString());
				if (rssms.next()) {
					reMap.put("sms_note", rssms.getString("sms_note"));
					reMap.put("zy_templateId", rssms.getString("zy_templateId"));
					reMap.put("zy_singerId", rssms.getString("zy_singerId"));
				}
				rssms.close();				
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (catmpconnect != null) {
					catmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return reMap;
	}
	
	/**
	 * 根据模板内容匹配param参数
	 * @param msg
	 * @param user_id
	 * @param logname
	 * @return
	 */
	public String getMsg_Param(String sms_note,String user_id,String logname) {
		Connection catmpconnect = null;
		String reParam = "";
		try {
			catmpconnect = this.jdbcQueryManager.getConnection();
				StringBuffer sb = new StringBuffer();
				sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type,c.register_date,c.join_date,");
				sb.append("c.exam_times,a.arch_num,a.sex,a.user_name,a.birthday ");
				sb.append(" from exam_info c,customer_info a ");
				sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
				sb.append(" and c.id = '" + user_id + "' ");	
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb.toString());		
				ResultSet rsuser = catmpconnect.createStatement().executeQuery(sb.toString());
				if (rsuser.next()) {
					String regex = "(?<=\\{)[^\\}]+";
			        Pattern pattern = Pattern.compile (regex);
			        Matcher matcher = pattern.matcher (sms_note);
			        StringBuffer param = new StringBuffer();
			        while (matcher.find ())
			        {
			            String paramName = matcher.group ();
			            if("name".equals(paramName)) {
			            	param.append(rsuser.getString("user_name").trim()+",");
			            }else if("sex".equals(paramName)) {
			            	param.append(rsuser.getString("sex").trim()+",");
			            }else if("age".equals(paramName)) {
			            	param.append(rsuser.getString("age").trim()+",");
			            }else if("birthday".equals(paramName)) {
			            	String tiem = rsuser.getString("birthday").trim();	
			            	param.append(tiem.substring(0,10)+",");
			            }
			        }
			        if(param.length()>0) {
			        	param.deleteCharAt(param.length()-1);
			            reParam = param.toString();
			        }
				}
				rsuser.close();				
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (catmpconnect != null) {
					catmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return reParam;
	}
	
	/**
	 * 根据config_url获取apiAccount、apiId、apiKey
	 * @param wcdConfigUrl
	 * @param index
	 * @return
	 */
	public String getAppAccount(String wcdConfigUrl,int index) {
		String reStr = "sd";
		String[] accountL = wcdConfigUrl.split("&");
		try {
			reStr = accountL[accountL.length-index];
		} catch (Exception e) {
			// TODO: handle exception
		}
		return reStr;
	}
	
	
}
