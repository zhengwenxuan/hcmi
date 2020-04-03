package com.hjw.webService.client.yichang;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.RowSet;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.Bean.Person;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.yichang.bean.cdr.client.header.PRPA_IN000001UV03;
import com.hjw.webService.client.yichang.bean.cdr.client.hipPushRequestFormToXD.HIPMessageServer;
import com.hjw.webService.client.yichang.bean.cdr.client.hipPushRequestFormToXD.ResponseXD;
import com.hjw.webService.client.yichang.bean.cdr.client.requestForm.ItemCDRYC;
import com.hjw.webService.client.yichang.bean.cdr.client.requestForm.RegistRequestForm;
import com.hjw.webService.client.yichang.bean.cdr.client.ret.DataSourceCDR;
import com.hjw.webService.client.yichang.bean.cdr.client.ret.DataSourceXD;
import com.hjw.webService.client.yichang.gencode.SERVDelegate;
import com.hjw.webService.client.yichang.gencode.SERVService;
import com.hjw.webService.client.yichang.gencode.SERVServiceLocator;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONSerializer;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Description: pacs申请信息服务 宜昌CDR
 * @author: zwx
 * @version V2.0.0.0
 */
public class PACSSendMessageYC {
	private PacsMessageBody lismessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private static Calendar checkDay;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public PACSSendMessageYC(PacsMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
/////////////////////////////日期限制及体检系统通知功能-开始/////////////////////////////
		Calendar deadline = Calendar.getInstance();
		SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//JANUARY一月	FEBRUARY二月		MARCH三月		APRIL四月		MAY五月			JUNE六月
		//JULY七月		AUGUST八月		SEPTEMBER九月	OCTOBER十月		NOVEMBER十一月	DECEMBER十二月
		deadline.set(2020, Calendar.AUGUST, 31, 22, 0, 0);
		String viewDateStr = df.format(deadline.getTime());
		if(new Date().after(deadline.getTime())) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
			TranLogTxt.liswriteEror_to_txt(logname,"接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
			return rb;
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
				TranLogTxt.liswriteEror_to_txt(logname, "原来的notices是:"+notices);
				//判断系统到期时间，提前10天提醒客户
				Calendar alertDate = deadline;
				alertDate.add(Calendar.DATE, -10);
				if(new Date().after(alertDate.getTime())) {
					String noticesNew = "系统到期时间为:"+viewDateStr+"，请尽快联系火箭蛙销售人员!!";
					String updatesql = " update examinatioin_center set notices='"+noticesNew+"' ";
					connection.createStatement().executeUpdate(updatesql);
					TranLogTxt.liswriteEror_to_txt(logname, updatesql);
				} else {
					String updatesql = " update examinatioin_center set notices='' ";
					connection.createStatement().executeUpdate(updatesql);
					TranLogTxt.liswriteEror_to_txt(logname, updatesql);
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
/////////////////////////////日期限制及体检系统通知功能-结束/////////////////////////////
		
		String jsonString = JSONSerializer.toJSON(lismessage).toString();
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
		
		String exam_num = this.lismessage.getCustom().getExam_num();
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			ExamInfoUserDTO eu = configService.getExamInfoForNum(lismessage.getCustom().getExam_num());
			if ((eu==null)||(eu.getId() <= 0)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {
				//发送申请-pacs
				try {
					List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
					for (PacsComponents pcs : this.lismessage.getComponents()) {
						if("EL".equals(pcs.getPacsComponent().get(0).getExam_class())) {//心电的不走cdr
							HIPMessageServer hIPMessageServer = getHIPMessageServer(pcs, eu, logname);
							PRPA_IN000001UV03 reqMessage = new PRPA_IN000001UV03(eu.getExam_num());
							reqMessage.getControlActProcess().getSubject().getRequest().setHIPMessageServer(hIPMessageServer);
							String xml = JaxbUtil.convertToXmlWithCDATA(reqMessage, "^message");
							SERVService servService = new SERVServiceLocator(url);
							SERVDelegate servDelegate = servService.getSERVPort();
							TranLogTxt.liswriteEror_to_txt(logname,"reqRequestFormXD:"+xml);
							String resRequestFormXD = servDelegate.HIPMessageServer("hipPushRequestFormToXD", xml);
							TranLogTxt.liswriteEror_to_txt(logname,"resRequestFormXD:"+resRequestFormXD);	
							DataSourceXD resData = new DataSourceXD(resRequestFormXD, true);
							if("0".equals(resData.getStatus())) {
								ApplyNOBean an = new ApplyNOBean();
								an.setApplyNO(pcs.getReq_no());	
								list.add(an);
								rb.getControlActProcess().setList(list);
								rb.getResultHeader().setTypeCode("AA");
								rb.getResultHeader().setText(this.lismessage.getCustom().getExam_num()+"-"+this.lismessage.getCustom().getName()+"-"+"pacs申请发送成功");
								TranLogTxt.liswriteEror_to_txt(logname,this.lismessage.getCustom().getExam_num()+"-"+this.lismessage.getCustom().getName()+"-"+"pacs申请发送成功");
							} else {
								TranLogTxt.liswriteEror_to_txt(logname,"AE:"+resData.getMessage());
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText(resData.getMessage());
							}
						} else {//除心电外，都发到CDR
							RegistRequestForm requestForm = getRequestReqData(pcs, eu, logname);
							PRPA_IN000001UV03 reqRequestForm = new PRPA_IN000001UV03(eu.getExam_num());
							reqRequestForm.getControlActProcess().getSubject().getRequest().setRegistRequestForm(requestForm);
							String xml = JaxbUtil.convertToXml(reqRequestForm, true);
							SERVService servService = new SERVServiceLocator(url);
							SERVDelegate servDelegate = servService.getSERVPort();
							TranLogTxt.liswriteEror_to_txt(logname,"reqRequestForm:"+xml);
							String resRequestForm = servDelegate.HIPMessageServer("registRequestForm", xml);
							TranLogTxt.liswriteEror_to_txt(logname,"resRequestForm:"+resRequestForm);	
							DataSourceCDR resData = new DataSourceCDR(resRequestForm, true);
							if("AA".equals(resData.getStatus())) {
								ApplyNOBean an = new ApplyNOBean();
								an.setApplyNO(pcs.getReq_no());	
								list.add(an);
								rb.getControlActProcess().setList(list);
								rb.getResultHeader().setTypeCode("AA");
								rb.getResultHeader().setText(this.lismessage.getCustom().getExam_num()+"-"+this.lismessage.getCustom().getName()+"-"+"pacs申请发送成功");
								TranLogTxt.liswriteEror_to_txt(logname,this.lismessage.getCustom().getExam_num()+"-"+this.lismessage.getCustom().getName()+"-"+"pacs申请发送成功");
							} else {
								TranLogTxt.liswriteEror_to_txt(logname,"AE:"+resData.getMessage());
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText(resData.getMessage());
							}
						}
					}
				} catch (Exception ex){
				    rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("发送pacs申请-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
					TranLogTxt.liswriteEror_to_txt(logname,"发送pacs申请-调用webservice错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
			}
		}
		jsonString = JSONSerializer.toJSON(rb).toString();
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + jsonString);
		return rb;
	}
	
	private HIPMessageServer getHIPMessageServer(PacsComponents pcs, ExamInfoUserDTO ei, String logname) {
//		Person custom = this.lismessage.getCustom();
//		String sexcode = "";
//		if("男".equals(custom.getSexname()) || "男性".equals(custom.getSexname())) {
//			sexcode = "1";
//			custom.setSexname("男性");
//		} else if("女".equals(custom.getSexname()) || "女性".equals(custom.getSexname())) {
//			sexcode = "2";
//			custom.setSexname("女性");
//		} else {
//			sexcode = "0";
//			custom.setSexname("未知的性别");
//		}
//		String birthday = "";
//		if(!StringUtil.isEmpty(ei.getBirthday())) {
//			birthday = ei.getBirthday().replace(" ", "T");
//		}
		
		ResponseXD response = new ResponseXD();
		response.getFindOrd().setName(ei.getUser_name());
		response.getFindOrd().setGender(ei.getSex());
		response.getFindOrd().setBirthday(ei.getBirthday());
		response.getFindOrd().setAge(ei.getAge()+"岁");
		response.getFindOrd().setPatientCode(ei.getExam_num());
		response.getFindOrd().setAddress(ei.getAddress());
		response.getFindOrd().setTelephone(ei.getPhone());
//		response.getFindOrd().setApplyTime();
		response.getFindOrd().setBarcode(pcs.getReq_no());
		response.getFindOrd().setExamCode(pcs.getReq_no());
		response.getFindOrd().setOrderItemCode(pcs.getPacsComponent().get(0).getItemCode());
		response.getFindOrd().setOrderItemName(pcs.getPacsComponent().get(0).getItemName());
		response.getFindOrd().setItemPrice(""+pcs.getPacsComponent().get(0).getItemprice());
		
		String responseXml = JaxbUtil.convertToXmlWithOutHead(response, true);
		
		HIPMessageServer hIPMessageServer = new HIPMessageServer();
		hIPMessageServer.setMessage(responseXml);
		return hIPMessageServer;
	}

	private RegistRequestForm getRequestReqData(PacsComponents pcs,ExamInfoUserDTO ei, String logname) {
		Person custom = this.lismessage.getCustom();
		String sexcode = "";
		if("男".equals(custom.getSexname()) || "男性".equals(custom.getSexname())) {
			sexcode = "1";
			custom.setSexname("男性");
		} else if("女".equals(custom.getSexname()) || "女性".equals(custom.getSexname())) {
			sexcode = "2";
			custom.setSexname("女性");
		} else {
			sexcode = "0";
			custom.setSexname("未知的性别");
		}
		String birthday = "";
		if(!StringUtil.isEmpty(ei.getBirthday())) {
			birthday = ei.getBirthday().replace(" ", "T");
		}
		
		RegistRequestForm requestForm = new RegistRequestForm();
		requestForm.getRegistRequestReq().setApplyNo(pcs.getReq_no());
		requestForm.getRegistRequestReq().setApplyType(getpt_numBydep_num(pcs.getPacsComponent().get(0).getExam_class(), logname));
		requestForm.getRegistRequestReq().setDiagnosisNo(ei.getArch_num());
		requestForm.getRegistRequestReq().setDiagnosisSerialNo(this.lismessage.getCustom().getExam_num());
//		requestForm.getRegistRequestReq().setEquipmentTypeCode();
//		requestForm.getRegistRequestReq().setEquipmentTypeName();
//		requestForm.getRegistRequestReq().setDocumentTypeCode();
//		requestForm.getRegistRequestReq().setDocumentTypeName();
		requestForm.getRegistRequestReq().setLocalId(pcs.getReq_no());
//		requestForm.getRegistRequestReq().setDocumentTitle();
		requestForm.getRegistRequestReq().setPatientName(this.lismessage.getCustom().getName());
		requestForm.getRegistRequestReq().setPatientLocalId(this.lismessage.getCustom().getExam_num());
		requestForm.getRegistRequestReq().setPatientPhonenumber(this.lismessage.getCustom().getTel());
		requestForm.getRegistRequestReq().setSexCode(sexcode);
		requestForm.getRegistRequestReq().setSexName(custom.getSexname());
		requestForm.getRegistRequestReq().setAge(custom.getOld()+"岁");
		requestForm.getRegistRequestReq().setBirthdate(birthday);
//		requestForm.getRegistRequestReq().setSpecimenCollectDatetime();
//		requestForm.getRegistRequestReq().setPatientSource();
//		requestForm.getRegistRequestReq().setApplicationStartTime();
//		requestForm.getRegistRequestReq().setApplicationDeptName();
//		requestForm.getRegistRequestReq().setApplicationDeptCode();
//		requestForm.getRegistRequestReq().setApplicationDoctorName();
//		requestForm.getRegistRequestReq().setApplicationDoctorCode();
//		requestForm.getRegistRequestReq().setExecutiveDepartmentName();
//		requestForm.getRegistRequestReq().setExecutiveDepartmentCode();
//		requestForm.getRegistRequestReq().setNursingStationName();
//		requestForm.getRegistRequestReq().setNursingStationCode();
//		requestForm.getRegistRequestReq().setApplyReason();
//		requestForm.getRegistRequestReq().setApplyContent();
//		requestForm.getRegistRequestReq().setHistorySummary();
//		requestForm.getRegistRequestReq().setClinicalDiagnosis();
//		requestForm.getRegistRequestReq().setSpecimenType();
//		requestForm.getRegistRequestReq().setSpecimenTypeName();
//		requestForm.getRegistRequestReq().setSpecimenCollecCode();
//		requestForm.getRegistRequestReq().setSpecimenCollecName();
//		requestForm.getRegistRequestReq().setOrganz();
//		requestForm.getRegistRequestReq().setOrganizationCode();
//		requestForm.getRegistRequestReq().setBedNo();
//		requestForm.getRegistRequestReq().setUrgentFlag();
//		requestForm.getRegistRequestReq().setApplyExecutiveDatetime();
		
		for (PacsComponent pc : pcs.getPacsComponent()) {
			ItemCDRYC item = new ItemCDRYC();
			item.setItemCode(pc.getItemCode());
			item.setItemName(pc.getItemName());
			item.setItemPrice(""+pc.getItemprice());
//			item.setExaminationSiteName(lc.getTargetSiteCode());
			requestForm.getRegistRequestReq().getItemList().add(item);
		}
//		requestForm.getRegistRequestReq().setXmlDocument();
//		requestForm.getRegistRequestReq().setFileBase64Content();
//		requestForm.getRegistRequestReq().setFileType();
//		requestForm.getRegistRequestReq().setOrderRemarks();
//		requestForm.getRegistRequestReq().setCodeExpand();
		return requestForm;
	}
	
	public String getpt_numBydep_num(String dep_num, String logname) throws ServiceException {
		String sql = "select pt_num from department_dep where dep_num = '" + dep_num + "' ";	
		TranLogTxt.liswriteEror_to_txt(logname,"sql:"+sql);
		String pt_num = "";
		try {
			RowSet rs = this.jdbcQueryManager.getRowSet(sql);
			if(rs.next()) {
				pt_num = rs.getString("pt_num");
			}
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		} 
		return pt_num;
	}
}
