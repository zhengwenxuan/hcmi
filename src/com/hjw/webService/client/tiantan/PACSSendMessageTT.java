package com.hjw.webService.client.tiantan;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.Bean.Person;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.tiantan.bean.PacsDriverTiantan;
import com.hjw.webService.client.tiantan.bean.ResPacsBodyTiantan;
import com.hjw.wst.DTO.TreeDTO;
import com.hjw.wst.domain.sysSurvey;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 2.6 检查申请信息服务 天健 平台对接-东北国际
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class PACSSendMessageTT {
	private PacsMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public PACSSendMessageTT(PacsMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String convalue, String logname) {
		JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
		ResultPacsBody rb = new ResultPacsBody();
		
		String[] oracleurl = convalue.split("&"); // oracle 配置 his数据库
		
				List<ApplyNOBean> appList = new ArrayList<ApplyNOBean>();
				
				for (PacsComponents pcs : lismessage.getComponents()) {
					boolean falagss=true;
					for (PacsComponent pc : pcs.getPacsComponent()) {
						try {
					StringBuffer sb = new StringBuffer();
					sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
					sb.append("<REQUEST COMMAND=\"CommitApplication\">");
					sb.append("<ApplicationID>" + pcs.getReq_no() + "</ApplicationID>");// 申请单号
					sb.append("<HisPatientTypeID>3</HisPatientTypeID>");
					sb.append("<HisPatientID>" + this.lismessage.getCustom().getExam_num() + "</HisPatientID>");// 体检编号
					sb.append("<PatientIndexID></PatientIndexID>");
					sb.append("<PatientName>" + this.lismessage.getCustom().getName() + "</PatientName>");
					sb.append("<Gender>1</Gender>");// 0 未知的性别 1 男性 2 女性 9
													// 未说明的性别
					sb.append("<Birthday>" + parse0(this.lismessage.getCustom().getBirthtime()) + "</Birthday>");
					sb.append("<Phonenumber>" + this.lismessage.getCustom().getTel() + "</Phonenumber>");
					sb.append("<Address>" + this.lismessage.getCustom().getAddress() + "</Address>");
					sb.append("<RequestTime>" + DateUtil.getDateTime() + "</RequestTime>");
					sb.append("<ReqDepartmentID>" + this.lismessage.getDoctor().getDept_code() + "</ReqDepartmentID>");
					sb.append("<ReqDepartmentName>" + this.lismessage.getDoctor().getDept_name()
							+ "</ReqDepartmentName>");
					sb.append("<HospitalID></HospitalID>");
					sb.append("<ReqDoctorID>" + this.lismessage.getDoctor().getDoctorCode() + "</ReqDoctorID>");
					sb.append("<ReqDoctorName>" + this.lismessage.getDoctor().getDoctorName() + "</ReqDoctorName>");
					sb.append("<HisCheckItem></HisCheckItem >");
					sb.append("<ChiefComplaint></ChiefComplaint>");
					sb.append("<BedNo></BedNo>");
					sb.append("<Diagnose></Diagnose>");
					sb.append("<Abstracthistory></Abstracthistory>");
					PacsDriverTiantan pd= new PacsDriverTiantan();
					//pd = getDriverType(oracleurl,pc.getPacs_num(),logname);
					  pd = getshebei(oracleurl,pc.getPacs_num(),logname);
					sb.append("<DeviceTypeID>"+pd.getDrivertype_id()+"</DeviceTypeID>");//设备类型编码
					sb.append("<DeviceTypeName>"+pd.getDrivertype_name()+"</DeviceTypeName>");//设备类型名称
					sb.append("<DeviceID>"+pd.getDevice_id()+"</DeviceID>");//设备编码  分诊需要
					sb.append("<Costs></Costs>");
					sb.append("<StudyItems>");
					
							sb.append("<StudyItem>");
							sb.append("<PositionID>" +getPacsNum(oracleurl,pc.getPacs_num(),logname)+ "</PositionID>");//检查部位编码
							sb.append("<PositionName>" + pc.getPacs_num().split(",")[1] + "</PositionName>");//检查部位名称
							sb.append("<CheckMethodID>0</CheckMethodID>");
							sb.append("<CheckMethodName>无</CheckMethodName>");
							sb.append("<CheckItemCode>"+pc.getHis_num()+"</CheckItemCode>");//检查项目编码 his关联码
							sb.append("<CheckItemName>"+pc.getItemName()+"</CheckItemName>");//检查项目名称
							sb.append("</StudyItem>");
						
					sb.append("</StudyItems>");
					sb.append("<EstimateTime>" + DateUtil.getDateTime() + "</EstimateTime>");
					sb.append("<EstimateTypeID>0</EstimateTypeID>");
					sb.append("<IdNumber></IdNumber>");
					sb.append("<ExeDeptID></ExeDeptID>");
					sb.append("<ExeDeptName></ExeDeptName>");
					sb.append("<Remark></Remark>");
					sb.append("</REQUEST>");
					 TranLogTxt.liswriteEror_to_txt(logname, "pacs申请入参:" + sb.toString() + "\r\n");
					 String result = HttpUtil.doPost_Str(url, sb.toString(), "utf-8");
					  TranLogTxt.liswriteEror_to_txt(logname, "pacs申请返回:" + result + "\r\n");
					  ResPacsBodyTiantan rbcomm =new ResPacsBodyTiantan();
					if ((result != null) && (result.trim().length() > 0)) {
						result = result.trim();
						rbcomm = JaxbUtil.converyToJavaBean(result, ResPacsBodyTiantan.class);
						if("0".equals(rbcomm.getReturnCode())){
							
						}else{
							falagss=false;
						}
					}
						} catch (Exception ex) {
							falagss=false;
						}
					}
					if (falagss){
						ApplyNOBean ab = new ApplyNOBean();
						ab.setApplyNO(pcs.getReq_no());
						appList.add(ab);
					}
				}				
				rb.getResultHeader().setTypeCode("AA");
				rb.getControlActProcess().setList(appList);
		

		json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
		return rb;
	}

	private PacsDriverTiantan getshebei(String[] url, String pacs_num, String logname) {
		Connection connect = null;
		PacsDriverTiantan pd= new PacsDriverTiantan();
		try {
			String[] pacs_nums = pacs_num.split(",");
			for (int i = 0; i < pacs_nums.length; i++) {
				System.err.println(pacs_nums[i]+"========="+i);
			}
			String dburl = url[0];
			String user = url[1];
			String passwd = url[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			String sql = "select df.DEVICETYPEID,df.DEVICETYPENAME,df.DEVICEID from pacs31.V_DEVICETABLE_FORPLAT df  where df.DEVICEID ='" + pacs_nums[0] + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:执行插入="+sql);
			ResultSet rs = connect.createStatement().executeQuery(sql);
			if (rs.next()) {
				pd.setFlags(true);
				pd.setDevice_id(rs.getString("DEVICEID"));
				pd.setDrivertype_id(rs.getString("DEVICETYPEID"));
				pd.setDrivertype_name(rs.getString("DEVICETYPENAME"));
			} 
			rs.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					OracleDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return pd;
		
	}

	public static String parse0(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
		}
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 
	 * @param ps
	 * @param url
	 * @param logname
	 * @return
	 */
	private PacsDriverTiantan getDriverType(String[] url,String pacs_num,  String logname) {
		Connection connect = null;
		PacsDriverTiantan pd= new PacsDriverTiantan();
		try {
			String dburl = url[0];
			String user = url[1];
			String passwd = url[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			String sql = "select df.DEVICETYPEID,df.DEVICETYPENAME from pacs31.V_TYPE_POSITION_ITEM_FORPLAT pi,pacs31.V_DEVICETYPEINFO_FORPLAT df,pacs31.V_STUDYPOSITIONINFO_FORPLAT sf where pi.DEVICETYPEID=df.DEVICETYPEID and sf.STUDYPOSITIONID= pi.STUDYPOSITIONID and STUDYPOSITION='" + pacs_num + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:执行插入="+sql);
			ResultSet rs = connect.createStatement().executeQuery(sql);
			if (rs.next()) {
				pd.setFlags(true);
				pd.setDrivertype_id(rs.getString("DEVICETYPEID"));
				pd.setDrivertype_name(rs.getString("DEVICETYPENAME"));
			} 
			rs.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					OracleDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return pd;
	}
	
	/**
	 * 
	 * @param ps
	 * @param url
	 * @param logname
	 * @return
	 */
	private String getPacsNum(String[] url,String pacs_num,  String logname) {
		Connection connect = null;
		String pd= "";
		try {
			String[] pacs_nums = pacs_num.split(",");
			String dburl = url[0];
			String user = url[1];
			String passwd = url[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			String sql = "select STUDYPOSITIONID from pacs31.V_STUDYPOSITIONINFO_FORPLAT sf where  STUDYPOSITION='" + pacs_nums[1] + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:执行插入="+sql);
			ResultSet rs = connect.createStatement().executeQuery(sql);
			if (rs.next()) {
				pd=(rs.getString("STUDYPOSITIONID"));
			} 
			rs.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					OracleDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return pd;
	}
	
}
