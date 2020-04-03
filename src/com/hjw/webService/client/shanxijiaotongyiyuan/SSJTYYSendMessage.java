package com.hjw.webService.client.shanxijiaotongyiyuan;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.MSGSendBean;
import com.hjw.webService.client.body.MSGResBody;
import com.hjw.webService.client.tj180.Bean.MSGReqBean;
import com.hjw.webService.client.tj180.Bean.MSGResBean;
import com.synjones.framework.persistence.JdbcQueryManager;
import net.sf.json.JSONObject;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  陕西交通医院短信接口
     * @author: zr
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class SSJTYYSendMessage {
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
	public MSGResBody getMessage(String url, MSGSendBean eu, String logname) {
		MSGResBody rb = new MSGResBody();
		try {
			JSONObject json = JSONObject.fromObject(eu);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
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
					try{
					StringBuffer  s = new StringBuffer();
					 s.append(rs.getString("id")+"\n,");
					 s.append(rs.getString("template_id")+"\\n,");
					 s.append(rs.getString("sms_note")+"\n,");
					 s.append(rs.getString("sms_phone")+"\n,");
					 s.append(rs.getString("sms_date")+"\n,");
					 s.append(rs.getLong("send_user")+"\n,");
					 s.append(rs.getInt("sms_status")+"\n,");
					 s.append(rs.getInt("sms_type")+"\n,");
					 s.append(rs.getLong("user_id")+"\n,");
					 s.append(rs.getInt("user_type")+"\n,");
					 s.append(rs.getString("chi_name")+"\n,");
					 s.append(rs.getString("log_name")+"\n,");
					 s.append(rs.getString("sms_time")+"\n,");
					String smssendid=rs.getString("id");
					 TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句:"+s.toString());
					String template_id =rs.getString("template_id");
					String sms_note=rs.getString("sms_note");
					String sms_phone=rs.getString("sms_phone");
					String sms_date =rs.getString("sms_date");
					long send_user =rs.getLong("send_user");
					int sms_status=rs.getInt("sms_status");
					int sms_type=rs.getInt("sms_type");
					long user_id= rs.getLong("user_id");
					int user_type= rs.getInt("user_type");
					String send_user_name=rs.getString("chi_name");
					String send_user_code=rs.getString("log_name");
					String sms_tiem = rs.getString("sms_time");
					 String mess="";
					 TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句：手机号"+sms_phone+"---"+(sms_phone != null)+"--"+(sms_phone.trim().length() == 11));
						if (sms_phone != null && sms_phone.trim().length() == 11) {
							if (user_type==1) { // 1表示体检体统用户								
								mess = getmsg_exam(sms_note, user_id + "", logname);
							} else if (user_type==0) {// 0标示体检中心工作人员
								mess = getmsg_user(sms_note, user_id + "", logname);
							}
						}
					if((mess!="")&&(mess.trim().length()>0)){
						MSGReqBean mq= new MSGReqBean();
						mq.setMSG_CONTENT(mess);
						mq.setDB_USER(send_user_code);
						mq.setUSER_NAME(send_user_name);
						mq.getPHONE_LIST().add(sms_phone);
						mq.setSms_tiem(sms_tiem);
						mq.setSms_type(sms_type);
						json = JSONObject.fromObject(mq);// 将java对象转换为json对象
						String mqstr = json.toString();// 将json对象转换为字符串
						TranLogTxt.liswriteEror_to_txt(logname, "res:"+sms_phone+"-" + mqstr);
						String result = HttpUtil.doPost(url,mq,"utf-8");
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + result +"\r\n");
						if((result!=null)&&(result.trim().length()>0)){	       
						result = result.trim();
						JSONObject jsonobject = JSONObject.fromObject(result);
						if ("FALSE".equals(jsonobject.getString("R"))) {
							rb.setRestext("");
							rb.setRescode("AE");
							rb.setIdnumber("");
							tjtmpconnect.createStatement().execute("update crm_sms_send set sms_status=2,sms_fal_notice='"+jsonobject.getString("I")+"' where id='"+smssendid+"'");
						}else{
							rb.setRestext("");
							rb.setRescode("AA");
							rb.setIdnumber("");
							tjtmpconnect.createStatement().execute("update crm_sms_send set sms_status=1,sms_fal_notice='"+jsonobject.getString("I")+"' where id='"+smssendid+"'");	
						}
						}
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
	
	private String getmsg_user(String sms_note,String user_id,String logname){
		Connection tjtmpconnect = null;
		String mds = "";
		List<String> str= new ArrayList<String>();
		str=getGJCList();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
		/*	String sb1 = "select sms_note from crm_sms_base_template where id='"+template_id+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if(rs1.next()) {*/
				String username=sms_note;
				String sbuser = "select id,chi_name,phone_num from user_usr  where id='"+user_id+"'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sbuser);
				ResultSet rsuser = tjtmpconnect.createStatement().executeQuery(sbuser);
				if (rsuser.next()) {
					mds= username.replaceAll("\\{name\\}", rsuser.getString("chi_name").trim());
				}
				rsuser.close();
			/*}*/
			/*rs1.close();*/
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return mds;
	}

	private String getmsg_exam(String sms_note,String user_id,String logname){
		Connection tjtmpconnect = null;
		String mds = "";
		List<String> str= new ArrayList<String>();
		str=getGJCList();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
				String username=sms_note;
				StringBuffer sb = new StringBuffer();
				sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type,c.register_date,c.join_date,");
				sb.append("c.exam_times,a.arch_num,a.sex,a.user_name,a.birthday ");
				sb.append(" from exam_info c,customer_info a ");
				sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");
				sb.append(" and c.id = '" + user_id + "' ");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb.toString());
				ResultSet rsuser = tjtmpconnect.createStatement().executeQuery(sb.toString());
				if (rsuser.next()) {
					mds= username.replaceAll("\\{name\\}",rsuser.getString("user_name").trim());
					mds= mds.replaceAll("\\{sex\\}", rsuser.getString("sex").trim());
					mds= mds.replaceAll("\\{age\\}", rsuser.getString("age").trim());
					String tiem = rsuser.getString("birthday").trim();
					mds= mds.replaceAll("\\{birthday\\}",tiem.substring(0,10));
				}
				rsuser.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return mds;
	}

	private List<String> getGJCList(){
		List<String> str= new ArrayList<String>();
		str.add("name");
		str.add("sex");

		return str;
	}

}
