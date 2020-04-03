package com.hjw.webService.client.dashiqiao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.dashiqiao.LisResBean.LisRes;
import com.hjw.webService.client.dashiqiao.LisResBean.entry;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.webService.client.dashiqiao.ResCusBean.Extension;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.domain.ChargingItem;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class LisResMessageDSQ{
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static Calendar checkDay;
	static {
	    init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public String getMessage(String reqStr, String logName) {
		
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + reqStr);
		ResHdMeessage response = new ResHdMeessage();
		LisRes lisres = new Gson().fromJson(reqStr, LisRes.class);
		try {
			
			
			boolean flay=false;
			/////////////////////////////日期限制及体检系统通知功能-开始/////////////////////////////
			Calendar deadline = Calendar.getInstance();
			SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
			//JANUARY一月	FEBRUARY二月		MARCH三月		APRIL四月		MAY五月			JUNE六月
			//JULY七月		AUGUST八月		SEPTEMBER九月	OCTOBER十月		NOVEMBER十一月	DECEMBER十二月
			deadline.set(2019, Calendar.SEPTEMBER, 15, 0, 0, 0);
			String viewDateStr = df.format(deadline.getTime());
			if(new Date().after(deadline.getTime())) {
				//response.getResultHeader().setTypeCode("AE");
			//	response.getResultHeader().setText("接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
				flay=true;
				TranLogTxt.liswriteEror_to_txt(logName,"接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
			//return response;
			}
			
			if(checkDay == null) {
			checkDay = Calendar.getInstance(); checkDay.add(Calendar.DATE, -1);
			}
			Calendar today = Calendar.getInstance(); today.set(Calendar.HOUR, 0); today.set(Calendar.MINUTE, 0); today.set(Calendar.SECOND, 0);
			if(today.after(checkDay)) {//每天仅检查一遍
			checkDay = today;
			Connection connection = null;
			try {
				//每次先将旧的通知信息打到日志文件
				connection = jdbcQueryManager.getConnection();
				String sql = " select notices from examinatioin_center ";
				ResultSet rs = connection.createStatement().executeQuery(sql);
				String notices="";
				while (rs.next()) {
					notices = rs.getString("notices");
				}
				TranLogTxt.liswriteEror_to_txt(logName, "原来的notices是:"+notices);
				//判断系统到期时间，提前10天提醒客户
				Calendar alertDate = deadline;
				alertDate.add(Calendar.DATE, -10);
				if(new Date().after(alertDate.getTime())) {
					String noticesNew = "系统到期时间为:"+viewDateStr+"，请尽快联系火箭蛙销售人员!!";
					String updatesql = " update examinatioin_center set notices='"+noticesNew+"' ";
					connection.createStatement().executeUpdate(updatesql);
					TranLogTxt.liswriteEror_to_txt(logName, updatesql);
				} else {
					String updatesql = " update examinatioin_center set notices='' ";
					connection.createStatement().executeUpdate(updatesql);
					TranLogTxt.liswriteEror_to_txt(logName, updatesql);
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				connection.close();
			}
			}
			
			
			
			String patidtype = "";
			String patid = "";
			String Lis_item_code ="";
			String Exam_date="";
			String Create_time="";
			List<entry> entry = lisres.getEntry();
			for (int i = 0; i < entry.size(); i++) {
				LisResult lisResult = new LisResult();
				ExamInfoUserDTO ei = new ExamInfoUserDTO();
				if (!"".equals(entry.get(i).getResource())) {

					if (entry.get(i).getResource().getResourceType().equals("DiagnosticReport")) {
						List<Extension> extensionList = entry.get(i).getResource().getExtension();

						lisResult.setSample_barcode("");// 条码号

						
						
						ChargingItem chargingItem = getChargingItem(entry.get(i).getResource().getCode().getCoding().get(0).getCode(), logName);
						Lis_item_code = chargingItem.getItem_code();
						System.err.println("大项编码==" + entry.get(i).getResource().getCode().getCoding().get(0).getCode());
						
						

						String[] split = entry.get(i).getResource().getEffectiveDateTime().split("T");
						 Exam_date = split[0].toString();
						 Create_time = split[0].toString();
						/*lisResult.setExam_date(split[0].toString()); // 开始时间
						lisResult.setCreate_time(split[0].toString());*/
						for (int j = 0; j < extensionList.size(); j++) {

							if (extensionList.get(j).getUrl().equals("extension_checkup_no")) {
								patid = extensionList.get(j).getValueString();
								System.err.println("patid=====" + patid);
							} else if (extensionList.get(j).getUrl().equals("extension_checkup_type")) {
								patidtype = extensionList.get(j).getValueString();
								System.err.println("patidtype=====" + patidtype);
							} else {
							}

						}
					} else if(entry.get(i).getResource().getResourceType().equals("Observation")){
						lisResult.setReport_item_name(entry.get(i).getResource().getCode().getText());// 小项名称
						
						lisResult.setReport_item_code(entry.get(i).getResource().getCode().getCoding().get(0).getCode());// 小项码
						
						System.err.println("小项编码==" + entry.get(i).getResource().getCode().getCoding().get(0).getCode());
						lisResult.setItem_unit("");// 项目单位
						lisResult.setItem_result(entry.get(i).getResource().getValueString());// 项目结果
						lisResult.setFlag("");
						if(flay){
							lisResult.setRead_flag(3);//接口到期时
						}else{
							lisResult.setRead_flag(0);
						}

						lisResult.setRef(entry.get(i).getResource().getReferenceRange().get(0).getHigh().getValue()
								+ "~" + entry.get(i).getResource().getReferenceRange().get(0).getLow().getValue());
					}else{}

					System.err.println(patid + patidtype);
					ei = this.getExamInfoForNum(patid, logName, patidtype);
					if (ei != null) {
						if ("Z".equals(ei.getStatus())) {
							response.setStatus("0");
							response.setMessage("此人已经总检，请先取消总检再回传结果");
						} else {

							boolean flagss = true;
							// int seq_code = 0;

							// 结果标识含义 ：
							// H偏高、HH偏高报警、L偏低、LL偏低报警、P阳性、Q弱阳性、E错误，由LIS判断，仪器接口不用管
							// lisResult.setFlag(item.getResultFlag());
							lisResult.setExam_num(ei.getExam_num());
							lisResult.setLis_item_code(Lis_item_code);// 大项编码
							lisResult.setCreate_time(Create_time);
							lisResult.setExam_date(Exam_date);
							boolean succ = this.configService.insert_lis_result(lisResult);
							if (!succ) {
								flagss = false;
							}
							if (flagss) {
								TranLogTxt.liswriteEror_to_txt(logName, "lis信息 入库成功");
								response.setStatus("1");
								response.setMessage("lis信息 入库成功");
							} else {
								TranLogTxt.liswriteEror_to_txt(logName, "lis信息 入库错误");
								response.setStatus("0");
								response.setMessage("lis信息 入库错误");
							}

						}

				}
			}
			
			

			}
		} catch (Exception ex) {
			response.setStatus("0");
			response.setMessage("lis信息-json解析错误:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			ex.printStackTrace();
		}
		String res = new Gson().toJson(response, ResHdMeessage.class);
		return res;

	}

	
	
	public ExamInfoUserDTO getExamInfoForNum(String exam_num, String logname, String pattype) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");
		if (pattype.equals("个人")) {
			sb.append(" and c.patient_id = '" + exam_num + "' ");
		} else {
			sb.append(" and c.exam_num = '" + exam_num + "' ");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}
	
	
	public ChargingItem getChargingItem(String exam_chargeItem_code, String logname) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from charging_item where his_num='" + exam_chargeItem_code + "' and isActive='Y' ");
		TranLogTxt.liswriteEror_to_txt(logname, "查询item_code:" + sb.toString() + "\r\n");

		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ChargingItem.class);
		ChargingItem ci = new ChargingItem();
		if ((map != null) && (map.getList().size() > 0)) {
			ci = (ChargingItem) map.getList().get(0);
		}
		return ci;
	}

}
