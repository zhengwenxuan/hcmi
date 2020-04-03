package com.hjw.webService.client.nanfeng;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.nanfeng.bean.GuidNf;
import com.hjw.webService.client.nanfeng.gencode.WSInterface;
import com.hjw.webService.client.nanfeng.gencode.WSInterfaceLocator;
import com.hjw.webService.client.nanfeng.gencode.WSInterfacePortType;
import com.hjw.webService.client.nanfeng.util.GetGUID;
import com.hjw.webService.client.nanfeng.util.NanfenHL7;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONSerializer;

public class LISSendMessageNF {

	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static Calendar checkDay;
	
	private static String certificate = "xuMp+IMHvlYA3s34dkHQEWi8PL7CgdC9";

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}

	public LISSendMessageNF(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}
	
	
	public ResultLisBody getMessage(String url, String logname){
		
		ResultLisBody rb = new ResultLisBody();
		String reqTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String jsonString = JSONSerializer.toJSON(lismessage).toString();
		TranLogTxt.liswriteEror_to_txt(logname, reqTime + "  req                    :             " + jsonString);
		
		boolean useFlag = getUseLimit(logname);
		if(!useFlag) {
			try {
				List<LisComponents> components = lismessage.getComponents();
				ControlActLisProcess con = new ControlActLisProcess();
				List<ApplyNOBean> appList = new ArrayList<ApplyNOBean>();
				StringBuilder msgHeader = new StringBuilder();
				
				String guid = GetGUID.getGUID();
				
				msgHeader.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
				msgHeader.append("<root>");
				msgHeader.append("<serverName>SendClinicApply</serverName>");
				msgHeader.append("<format>HL7v2</format>");
				msgHeader.append("<callOperator></callOperator>");
				msgHeader.append("<certificate>"+certificate+"</certificate>");
				msgHeader.append("<msgNo>"+guid+"</msgNo>");
				msgHeader.append("<sendTime>"+reqTime+"</sendTime>");
				msgHeader.append("<sendCount>0</sendCount>");
				msgHeader.append("</root>");
				
				boolean flags = true;
				String reqStr = "";
				TranLogTxt.liswriteEror_to_txt(logname, "  components.size():" + components.size());
				for (int i = 0; i < components.size(); i++) {
					LisComponents liscoms = new LisComponents();
					liscoms = components.get(i);
					List<LisComponent> component = liscoms.getItemList();
					for (int j = 0; j < component.size(); j++) {
						reqStr = NanfenHL7.OrmO01hl7_getLis(lismessage, liscoms, j, "NW", logname, guid);// NW
						TranLogTxt.liswriteEror_to_txt(logname, "  req_msgHeader:" + msgHeader.toString());
						TranLogTxt.liswriteEror_to_txt(logname, "  req_msgBody:" + reqStr);
						String res = "";
						try {
							WSInterface wsInterface = new WSInterfaceLocator(url);
							WSInterfacePortType wsInterfacePortType = wsInterface.getWSInterfaceHttpSoap11Endpoint();
							res = wsInterfacePortType.callInterface(msgHeader.toString(), reqStr);
//							res = "MSH|^~&|PEIS||RIS||2019101015||ACK|f791da16-fe1b-4bd6-9df0-177d2624f178|P|2.4\r\n" + 
//									"MSA|AE|f791da16-fe1b-4bd6-9df0-177d2624f178|error:error:安全凭证校验失败【凭证号为空!】";
							TranLogTxt.liswriteEror_to_txt(logname, "  res:" + res);
							String res_code = NanfenHL7.getResCode(res);
							if (res_code.contains("error")) {
								String[] resL = res_code.split(":");
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText(resL[resL.length - 1]);
								flags = false;
								break;
							}
						} catch (Exception e) {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("error:请求服务SendClinicApply失败！");
							TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
							flags = false;
							break;
						}
			
					}
					if (flags) {
						ApplyNOBean aob = new ApplyNOBean();
						aob.setApplyNO(liscoms.getReq_no());
						appList.add(aob);				
					}else {
						break;
					}
				}
					
				if (flags) {
					con.setList(appList);
					rb.setControlActProcess(con);
					rb.getResultHeader().setTypeCode("AA");
				}else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("error:请求服务SendClinicApply失败！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("操作错误");
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			}
		}else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口已过期，请联系火箭蛙！");
		}
		
		
	
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getResultHeader().getTypeCode()+rb.getResultHeader().getText());
		return rb;
	}
	
	/**
	 * 获取日期限制使用权限
	 * @param logname
	 * @return
	 */
	public boolean getUseLimit(String logname) {
		boolean flay = false;
		try {

			///////////////////////////// 日期限制及体检系统通知功能-开始/////////////////////////////
			Calendar deadline = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			// JANUARY一月 FEBRUARY二月 MARCH三月 APRIL四月 MAY五月 JUNE六月
			// JULY七月 AUGUST八月 SEPTEMBER九月 OCTOBER十月 NOVEMBER十一月 DECEMBER十二月
			deadline.set(2020, Calendar.FEBRUARY, 10, 0, 0, 0);
			String viewDateStr = df.format(deadline.getTime());
			if (new Date().after(deadline.getTime())) {
				/*
				 * rb.getResultHeader().setTypeCode("AE");
				 * rb.getResultHeader().setText("接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
				 */
				flay = true;
				TranLogTxt.liswriteEror_to_txt(logname, "接口已过期，请联系火箭蛙，截止日期：" + viewDateStr);
				// return rb;
			}

			if (checkDay == null) {
				checkDay = Calendar.getInstance();
				checkDay.add(Calendar.DATE, -1);
			}
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			if (today.after(checkDay)) {// 每天仅检查一遍
				checkDay = today;
				Connection connection = null;
				try {
					// 每次先将旧的通知信息打到日志文件
					connection = jdbcQueryManager.getConnection();
					String sql = " select notices from examinatioin_center ";
					ResultSet rs = connection.createStatement().executeQuery(sql);
					String notices = "";
					while (rs.next()) {
						notices = rs.getString("notices");
					}
					TranLogTxt.liswriteEror_to_txt(logname, "原来的notices是:" + notices);
					// 判断系统到期时间，提前10天提醒客户
					Calendar alertDate = deadline;
					alertDate.add(Calendar.DATE, -10);
					if (new Date().after(alertDate.getTime())) {
						String noticesNew = "系统到期时间为:" + viewDateStr + "，请尽快联系火箭蛙销售人员!!";
						String updatesql = " update examinatioin_center set notices='" + noticesNew + "' ";
						connection.createStatement().executeUpdate(updatesql);
						TranLogTxt.liswriteEror_to_txt(logname, updatesql);
					} else {
						String updatesql = " update examinatioin_center set notices='' ";
						connection.createStatement().executeUpdate(updatesql);
						TranLogTxt.liswriteEror_to_txt(logname, updatesql);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					connection.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flay;
	}
	
	public String getGUID() {
		
		String sql = "select newid() as guid ";
		
		List<GuidNf> guidL = this.jdbcQueryManager.getList(sql, GuidNf.class);
		System.out.println(guidL.get(0).getGuid());
		return guidL.get(0).getGuid();
	}
	
}
