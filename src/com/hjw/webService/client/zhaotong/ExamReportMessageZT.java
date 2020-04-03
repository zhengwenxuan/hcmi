package com.hjw.webService.client.zhaotong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.zhaotong.bean.ExamReportBean;
import com.hjw.webService.client.zhaotong.bean.GetReqXMLBean;
import com.synjones.framework.persistence.JdbcQueryManager;

public class ExamReportMessageZT {

	private static ConfigService configService;
    private ThridInterfaceLog til;
    private static Calendar checkDay;
    
    private static JdbcQueryManager jdbcQueryManager;

    static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public String getReport(String xmlStr,String logNema) {
		TranLogTxt.liswriteEror_to_txt(logNema, "req："+xmlStr);
		GetReqXMLBean xmlBean = this.getReqForReport(xmlStr, logNema);
		TranLogTxt.liswriteEror_to_txt(logNema, "req:"+xmlStr);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuffer resXml = new StringBuffer();
		resXml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		resXml.append("<res>");
		
		boolean useFlag = getUseLimit(logNema);
		if(!useFlag) {
			if("success".equals(xmlBean.getCode())) {
				
				if(!StringUtil.isEmpty(xmlBean.getHealthCardNo())) {
					List<ExamReportBean> erList = this.getExamReportForIdnum(xmlBean,logNema);
					if("A".equals(erList.get(0).getApprove_status())) {
						resXml.append("<resultCode>0</resultCode>");
						resXml.append("<resultDesc>SUCCESS</resultDesc>");
						if(erList.size()>0) {
							resXml.append("<report>");
							for (ExamReportBean er : erList) {
								resXml.append("<reportId>"+er.getExam_num()+"</reportId>");
								resXml.append("<patientName>"+er.getUser_name()+"</patientName>");
								resXml.append("<patientAge>"+er.getAge()+"</patientAge>");
								resXml.append("<gender>"+er.getSex()+"</gender>");
								
								if(er.getPhone().equals("") || er.getPhone() !=null){
									resXml.append("<phone>"+er.getPhone()+"</phone>");
								}else{
									resXml.append("<phone></phone>");
								}
								resXml.append("<reportTitle>"+"昭通市第一人民医院健康体检报告"+"</reportTitle>");
								resXml.append("<idCardNo>"+er.getId_num()+"</idCardNo>");
								resXml.append("<hospitalId>"+"100156001"+"</hospitalId>");
								resXml.append("<hospitalName>"+"昭通市第一人民医院"+"</hospitalName>");
								resXml.append("<checkDate>"+sdf1.format(er.getJoin_date())+"</checkDate>");
								resXml.append("<reportDate>"+sdf.format(er.getCreate_time())+"</reportDate>");
								
							}
							resXml.append("</report>");
						}
					}else {
						resXml.append("<resultCode>-1</resultCode>");
						resXml.append("<resultDesc>此报告未完成审核！</resultDesc>");
					}
					
				}else {
					resXml.append("<resultCode>-1</resultCode>");
					resXml.append("<resultDesc>健康卡号码或身份证号为空！</resultDesc>");
				}
				
			}else {
				resXml.append("<resultCode>-1</resultCode>");
				resXml.append("<resultDesc>xml文件解析失败！</resultDesc>");
			}
		}else {
			resXml.append("<resultCode>-1</resultCode>");
			resXml.append("<resultDesc>接口已过期，请联系火箭蛙！</resultDesc>");
		}
		
		resXml.append("</res>");
		TranLogTxt.liswriteEror_to_txt(logNema, "res:"+resXml.toString());
		return resXml.toString();
	}
	
	/**
	 * 解析获取用户体检报告记录xml
	 * @param xmlStr
	 * @param logNema
	 * @return
	 */
	public GetReqXMLBean getReqForReport(String xmlStr,String logNema) {
		
		GetReqXMLBean xmlBean = new GetReqXMLBean();
		
		try {
			InputStream is = new ByteArrayInputStream(xmlStr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document dom = sax.read(is);
			xmlBean.setHealthCardNo(dom.selectSingleNode("req/healthCardNo").getText());
			xmlBean.setStartDate(dom.selectSingleNode("req/startDate").getText());
			xmlBean.setEndDate(dom.selectSingleNode("req/endDate").getText());
			xmlBean.setCode("success");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			xmlBean.setCode("error");
		}
		
		return xmlBean;
	}
	
	
	public List<ExamReportBean> getExamReportForIdnum(GetReqXMLBean getReqXMLBean,String logNema) {
		
		String sql = "select c.arch_num,c.user_name,c.id_num,c.sex,e.exam_num,e.age,e.phone,e.join_date,es.create_time,es.approve_status from customer_info c,exam_info e,exam_summary es " + 
				" where c.id = e.customer_id and es.exam_info_id = e.id and c.id_num = '"+getReqXMLBean.getHealthCardNo()+"' " ;
		if(!StringUtil.isEmpty(getReqXMLBean.getStartDate())) {
			sql+=" and es.create_time >= '"+getReqXMLBean.getStartDate()+" 00:00:00.000' ";
		}
		if(!StringUtil.isEmpty(getReqXMLBean.getEndDate())) {
			sql+=" and es.create_time <= '"+getReqXMLBean.getEndDate()+" 23:59:59.999' ";
		}
		TranLogTxt.liswriteEror_to_txt(logNema, "sql："+sql);
		List<ExamReportBean> list = this.jdbcQueryManager.getList(sql, ExamReportBean.class);
		
		return list;
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
			deadline.set(2020, Calendar.APRIL, 10, 0, 0, 0);
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
	
}
