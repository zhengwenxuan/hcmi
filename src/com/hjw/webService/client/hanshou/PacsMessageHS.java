package com.hjw.webService.client.hanshou;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom.input.SAXBuilder;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.HttpServer.HttpDownloader;
import com.hjw.interfaces.util.PdfToJpg;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.hanshou.bean.ExamInfoUserDTOHS;
import com.hjw.webService.client.hanshou.bean.GetReqBean;
import com.hjw.webService.client.hokai.bean.ResLisStatusBeanHK;
import com.hjw.webService.client.xhhk.bean.ResponseXHHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.PacsSendDTO;
import com.hjw.wst.DTO.PacsSummaryDTO;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PacsMessageHS extends ServletEndpointSupport {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static CommService commService;
	private static Calendar checkDay;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		commService = (CommService) wac.getBean("commService");
	}

	public String GetPacsApplyInfo(String strbody) {
		
		String logname="PacsReq";
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();// 开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();// 开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();// 开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();// 开单科室名称
		String examClass = "";//检查类别
		GetReqBean reqBean = getExamNumPacsReq(strbody, logname);
		
		String ResMessage = "";
		
		if(reqBean.getCode().equals("Success")){
			if (!reqBean.getExam_num().equals("") && reqBean.getExam_num() != null && !reqBean.getServerName().equals("") && reqBean.getServerName() != null) {
				ExamInfoUserDTO eu = getExamInfoForNumPacsReq(reqBean.getExam_num(), logname);
				ResponseXHHK response = new ResponseXHHK();
				if (eu == null || eu.getId() == 0) {
					TranLogTxt.liswriteEror_to_txt(logname, "error-体检编号【" + eu.getExam_num() + "】不存在");

					ResMessage = "error:体检号不存在";
				} else if ("Z".equals(eu.getStatus())) {

					ResMessage = "error:体检者已经总检";
					TranLogTxt.liswriteEror_to_txt(logname, "体检编号" + eu.getExam_num() + "】的体检者已经总检。");
				} else {
					StringBuilder sb = new StringBuilder();

					StringBuilder sb1 = new StringBuilder();

					sb.append(
							"select c.his_num,p.pacs_req_code,c.view_num,c.item_code,ec.item_amount,dd.dep_name as dept_name,c.item_name,dd.dep_num,ec.id,ec.pay_status,ec.amount,c.id as itemId "
									+ " from examinfo_charging_item ec,pacs_summary p,pacs_detail d,department_dep dd,charging_item c"
									+ " where ec.charge_item_id = c.id and p.id = d.summary_id and d.chargingItem_num = c.item_code"
									+ " and c.dep_id = dd.id and ec.isActive = 'Y' and c.interface_flag = '2' and ec.change_item != 'C'"
									+ " and ec.pay_status != 'M' and ec.exam_status in ('N','D')" + " and ec.examinfo_id = "
									+ eu.getId() + " and p.examinfo_num = '" + eu.getExam_num() + "'");
					sb.append(" and ec.is_application = 'N'");
					if(reqBean.getServerName().equals("GetEssApplyInfo")){
						//内镜体检单  dep_num根据数据库内 实际的dep_num 修改
						examClass = "ES";
						sb.append(" and dd.dep_num='ES'  ");
					}else if(reqBean.getServerName().equals("GetGmsApplyInfo")){
						//病理体检单
						examClass = "BL";
						sb.append(" and dd.dep_num='BL'  ");
					}else if(reqBean.getServerName().equals("GetRisApplyInfo")){
						//放射体检单
						examClass = "DX";
						sb.append(" and dd.dep_num in ('DX','CT','MR') ");
					}else if(reqBean.getServerName().equals("GetUisApplyInfo")){
						//超声体检单
						examClass = "US";
						sb.append(" and dd.dep_num='US'  ");
						
					}else{
						//所传服务名不存在  不返回申请单信息
						sb.append(" and dd.dep_num='xxxxxxxxxx'  ");
					}

					TranLogTxt.liswriteEror_to_txt(logname, "查项目sql:" + sb.toString());
					List<PacsSendDTO> pacsSendList = this.jdbcQueryManager.getList(sb.toString(), PacsSendDTO.class);

					String noPayItems = "";
					if ("N".equals(eu.getIs_after_pay())) {
						for (int i = 0; i < pacsSendList.size(); i++) {
							String IS_HIS_PACS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_PACS_CHECK")
									.getConfig_value().trim();
							if (("N".equals(pacsSendList.get(i).getPay_status())) && ("Y".equals(IS_HIS_PACS_CHECK))) {
								if (i == pacsSendList.size() - 1) {
									noPayItems += pacsSendList.get(i).getItem_name();
								} else {
									noPayItems += pacsSendList.get(i).getItem_name() + ",";
								}
								pacsSendList.remove(i);
							}
						}
					}

					if (pacsSendList.size() == 0 && "".equals(noPayItems)) {

						ResMessage = "error:体检号【" + eu.getExam_num() + "】没有需要发送申请的影像科室项目!";
						TranLogTxt.liswriteEror_to_txt(logname, "体检号【" + eu.getExam_num() + "】没有需要发送申请的影像科室项目!");

					} else if (pacsSendList.size() == 0 && (!"".equals(noPayItems))) {

						ResMessage = "error:体检号【" + eu.getExam_num() + "】项目(" + noPayItems + ")未付费!";
						TranLogTxt.liswriteEror_to_txt(logname, "体检号【" + eu.getExam_num() + "】项目(" + noPayItems + ")未付费!");

					} else {
						sb1.append("<root>");
						sb1.append("<returnContents>");
						for (int i = 0; i < pacsSendList.size(); i++) {
							
							sb1.append("<returnContent>");
							sb1.append("<visitType>3</visitType>");
							sb1.append("<patientId>"+eu.getExam_num()+"</patientId>");// eu.getPatient_id()
																										// 病人ID
							sb1.append("<visitNo>" + eu.getExam_num() + "</visitNo>");
							sb1.append("<visitNum></visitNum>");
							sb1.append("<icCardNo></icCardNo>");
							sb1.append("<safetyNo></safetyNo>");
							sb1.append("<emergencyFlag>0</emergencyFlag>");
							sb1.append("<patientSource>体检</patientSource>");
							sb1.append("<patientName>" + eu.getUser_name() + "</patientName>");
							sb1.append("<sex>" + eu.getSex() + "</sex>");
							sb1.append("<age>" + eu.getAge() + "</age>");
							sb1.append("<patientBirthDay>" + eu.getBirthday() + "</patientBirthDay>");
							sb1.append("<identityCardNo>" + eu.getId_num() + "</identityCardNo>");
							sb1.append("<nation></nation>");
							sb1.append("<country></country>");
							sb1.append("<addRess></addRess>");
							sb1.append("<sickWorkCompany></sickWorkCompany>");
							sb1.append("<phone>" + eu.getPhone() + "</phone>");
							sb1.append("<visitDeptCode></visitDeptCode>");
							sb1.append("<visitDept></visitDept>");
							sb1.append("<visitOperator></visitOperator>");
							sb1.append("<diagnoseCode></diagnoseCode>");
							sb1.append("<diagnoseName></diagnoseName>");
							sb1.append("<bedNo></bedNo>");
							sb1.append("<sickSynptom></sickSynptom>");
							sb1.append("<clinicInfo></clinicInfo>");
							sb1.append("<abstractHistory></abstractHistory>");
							sb1.append("<applyNo>" + pacsSendList.get(i).getPacs_req_code()
									+ "</applyNo>");
							sb1.append("<applyStatus></applyStatus>");
							sb1.append("<applyDept>体检中心</applyDept>                    ");
							sb1.append("<applyDeptName>体检中心</applyDeptName>            ");
							sb1.append("<applyOperator></applyOperator>                ");
							sb1.append("<applyDate>" + DateTimeUtil.getDateTimes()
									+ "</applyDate>");
							sb1.append("<executeDept></executeDept>");
							sb1.append("<executeDeptName></executeDeptName>");
							sb1.append("<examItems>");
							sb1.append("<examItem>");
							sb1.append("<examMethod></examMethod>");
							sb1.append("<examClass>"+pacsSendList.get(i).getDep_num()+"</examClass>");
							sb1.append("<clinicItemID>" + pacsSendList.get(i).getView_num()
									+ "</clinicItemID>");
							sb1.append("<clinicItemName>" + pacsSendList.get(i).getItem_name()
									+ "</clinicItemName>");
							sb1.append("<charges>" + pacsSendList.get(i).getItem_amount()
									+ "</charges>");
							sb1.append("<examPart></examPart>");
							sb1.append("<examComments></examComments>");
							sb1.append("</examItem>");
							sb1.append("</examItems>");
							sb1.append("</returnContent>");
						}
						sb1.append("</returnContents>");
						sb1.append("</root>");

						TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1.toString());
						return sb1.toString();
					}
				}
			} else {
				ResMessage = "error:体检号或服务号,不能为空!";
			}
		}else{
			ResMessage="error:获取申请单出现错误!";
		}
		
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + ResMessage);
		return ResMessage;
	}

	public GetReqBean getExamNumPacsReq(String xmlstr, String logName) {

		GetReqBean ReqBean = new GetReqBean();
		String exam_num = "";
		String service_name = "";
		try {
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束

			ReqBean.setServerName(document.selectSingleNode("root/serverName").getText());// 服务名
			ReqBean.setExam_num(document.selectSingleNode("root/visitNo").getText());// 体检号
			ReqBean.setCode("Success");

		} catch (Exception ex) {
			ReqBean.setCode("error");
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logName, "res:xml解析错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return ReqBean;
	}

	public ExamInfoUserDTO getExamInfoForNumPacsReq(String exam_num, String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");
		sb.append(" and c.exam_num = '" + exam_num + "' ");
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}

	
	public String SendPacsUisReport(String strbody) {
		String logName="PacsRes";
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + strbody);
		
		String ResMessage="";
		PacsResult pacsResult =  getPacsResXMLPacsRes(strbody,logName);
		try {
			if((pacsResult.getStatus()+"").equals("0")){
				if ((pacsResult.getReq_no() != null) && (pacsResult.getReq_no().trim().length() > 0)) {
					String[] req_nums = pacsResult.getReq_no().split(",");
					for(int i=0;i<req_nums.length;i++) {
						ExamInfoUserDTOHS ei = new ExamInfoUserDTOHS();
						ei = this.getExamInfoForReqNumPacsRes(req_nums[i]);
						if ((ei == null) || (ei.getId() <= 0)) {
							
							
							ResMessage="error-pacs信息 查无此申请单号" + req_nums[i];
							break;
						} else if ("Z".equals(ei.getStatus())) {
					
							ResMessage="error-pacs信息 已经总检，入库错误" + req_nums[i];
							break;
						} else {
							pacsResult.setReq_no(req_nums[i]);
							boolean succ = this.configService.insert_pacs_result(pacsResult);
							if (succ) {
								
								ResMessage="success-入库成功" ;
							} else {
								ResMessage="error-入库失败" ;
								break;
							}
						}
					}
					
				} else {
					ResMessage="error-pacs信息 体检编号为空" ;
				}
			}else if((pacsResult.getStatus()+"").equals("3")){
				ResMessage="error-pacs信息 接口已过期，请联系火箭蛙。" ;
			}else {
				ResMessage="error-pacs信息 xml解析错误" ;
			}
			
		} catch (Exception ex) {
			
			ResMessage="error-pacs信息 xml解析错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex) ;
		}
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + ResMessage);
		return ResMessage;
	}
	public ExamInfoUserDTOHS getExamInfoForReqNumPacsRes(String req_nums) throws ServiceException{
		String sql = " select m.id,m.age,m.exam_num,m.status,m.exam_type "
				+ " ,m.register_date,m.join_date,m.exam_times "
				+ " ,n.user_name,n.id_num,n.sex,n.birthday,n.phone,dep.dep_num "
				+ " from examinfo_charging_item a,exam_info m,customer_info n, "
				+ " pacs_summary b,pacs_detail c,charging_item d,department_dep dep " + " where b.pacs_req_code='" + req_nums
				+ "' and c.summary_id=b.id and c.chargingItem_num=d.item_code "
				+ " and d.id=a.charge_item_id and a.examinfo_id=m.id "
				+ " and m.exam_num=b.examinfo_num and a.isActive='Y' "
				+ " and m.customer_id = n.id"
				+ " and d.dep_id = dep.id ";
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ExamInfoUserDTOHS.class);
		ExamInfoUserDTOHS eu = new ExamInfoUserDTOHS();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTOHS)map.getList().get(0);			
		}
		return eu;
	}
	public PacsResult getPacsResXMLPacsRes(String xmlstr, String logname) {
		PacsResult pacsResult = new PacsResult();
		try {
			boolean flay=false;
			
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			/////////////////////////////日期限制及体检系统通知功能-开始/////////////////////////////
			Calendar deadline = Calendar.getInstance();
			SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
			//JANUARY一月	FEBRUARY二月		MARCH三月		APRIL四月		MAY五月			JUNE六月
			//JULY七月		AUGUST八月		SEPTEMBER九月	OCTOBER十月		NOVEMBER十一月	DECEMBER十二月
			deadline.set(2020, Calendar.JANUARY, 31, 0, 0, 0);
			String viewDateStr = df.format(deadline.getTime());
			if(new Date().after(deadline.getTime())) {
			/*rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口已过期，请联系火箭蛙，截止日期："+viewDateStr);*/
			flay=true;
			TranLogTxt.liswriteEror_to_txt(logname,"接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
		//	return rb;
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
				connection.close();
			}
			}
			
			
			pacsResult.setTil_id(logname);
			pacsResult.setExam_num("");
			String req_nums = "";
			
			Element root = document.getRootElement();
			Element element = (Element) root.element("applyNos");
			List<Element> applyNoEle = element.elements("applyNo");
			
			for (int i = 0; i < applyNoEle.size(); i++) {
				if(i==applyNoEle.size()-1) {
					req_nums = req_nums+applyNoEle.get(i).getText();
				}else {
					req_nums = req_nums+applyNoEle.get(i).getText()+",";
				}
			}
			pacsResult.setReq_no(req_nums);//
			pacsResult.setExam_num(document.selectSingleNode("root/visitNo").getText());
			pacsResult.setPacs_checkno(document.selectSingleNode("root/pacsNo").getText());//报告id
			pacsResult.setItem_name(document.selectSingleNode("root/itemName").getText());//检查项目名称
			pacsResult.setReg_doc(document.selectSingleNode("root/examOperator").getText());//记录医生姓名	
			pacsResult.setCheck_doc(document.selectSingleNode("root/examOperator").getText());//检查医生姓名	
			pacsResult.setCheck_date(document.selectSingleNode("root/examTime").getText());//检查时间	
			pacsResult.setReport_doc(document.selectSingleNode("root/reportOperator").getText());//报告医生	
			pacsResult.setReport_date(document.selectSingleNode("root/reportTime").getText());//报告时间	
			pacsResult.setAudit_doc(document.selectSingleNode("root/auditOperator").getText());//审核医师	pdfPath
			pacsResult.setAudit_date(document.selectSingleNode("root/auditTime").getText());//审核时间	
			String pdfurl = document.selectSingleNode("root/pdfPath").getText();//pdf  路径
			String datatime = DateTimeUtil.shortFmt4(new Date());
			String req_num0 = pacsResult.getReq_no().split(",")[0];
			ExamInfoUserDTOHS ei = getExamInfoForReqNumPacsRes(req_num0);
			
			if(pdfurl.length()>0){
				
				String picpath = this.commService.getDatadis("TPLJ").getName();
				File f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();

				picpath = picpath + "\\pacs_img";
				String picname = "/pacs_img";
				f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();
				picpath = picpath + "\\" + datatime;
				picname = picname + "/" + datatime;
				f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();

				picpath = picpath + "\\" + ei.getDep_num();
				picname = picname + "/" + ei.getDep_num();
				f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();

				picpath = picpath + "\\" + ei.getExam_num();
				picname = picname + "/" + ei.getExam_num();
				f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();

				
				String pdfpath = picpath + ".pdf";
				String jpgpath = picpath + ".jpg";
			
				FileOutputStream fos = null;
				try {
					if (new HttpDownloader(pdfurl, pdfpath, logname).download()) {
						File file = new File(pdfpath);
						if (file.exists() && file.isFile()) {
							pacsResult.setImg_path(picname+ ".jpg");
							pacsResult.setReport_img_path(picname+ ".jpg");
							String fileName = file.getName();
							TranLogTxt.liswriteEror_to_txt(logname,
									"res:文件----" + fileName + "的大小是：" + file.length());
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (fos != null) {
						fos.close();
					}
				}
			
			
				PdfToJpg pjpg = new PdfToJpg();
				int picnum = pjpg.pdf2jpg(pdfpath, picpath);
			
			}
				
			pacsResult.setClinic_diagnose(document.selectSingleNode("root/examResult").getText());//结论
			pacsResult.setClinic_symptom(document.selectSingleNode("root/visitStateDesc").getText());//结论描述
			pacsResult.setStudy_body_part(document.selectSingleNode("root/examPart").getText());//部位
			
			pacsResult.setPacs_item_code(document.selectSingleNode("root/itemCode").getText());//检查项目编码 his_num item_code
			pacsResult.setIs_tran_image(0);
			
			if(flay){
				pacsResult.setStatus(3);
			}else{
				pacsResult.setStatus(0);
			}
		
			}catch(Exception ex){
				pacsResult.setStatus(1);
				ex.printStackTrace();
			}
		return pacsResult;
	}
	
	
	public String UpdatePacsUisApplyInfo(String strbody) {
		String logname="PacsStatus";
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + strbody);
		ResLisStatusBeanHK rb= new ResLisStatusBeanHK();
		rb=getreqNoPacsStatus(strbody);
		if("Success".equals(rb.getCode())){
			
			String[] req_nums = rb.getReqno().split(",");
			for(int i=0;i<req_nums.length;i++) {
				String req_num = req_nums[i];
				PacsSummaryDTO psDTO = this.getPacsSummaryInfo(rb.getMessageId(), req_num);
				if(psDTO!=null) {
					String statuss = rb.getStatus();
					
					String samstatus="W";
					//操作代码和名称：分配条码(备管)(code=10)，采集(code=20),
					//1 条码已打印，11 条码已取消，2已采样，22取消采样，3标本送出，33取消送出，4已核收，44取消核收，5已检验，55取消检验，6已报告，66取消报告，7 已审核, 77取消审核，8 报告已打印,9 已作废
					ExamInfoUserDTO ei = this.getExamInfoForNumPacsStatus(rb.getMessageId());
					if(("1".equals(statuss))||("2".equals(statuss))||("3".equals(statuss))||("4".equals(statuss))||("5".equals(statuss)) || ("6".equals(statuss)) || ("7".equals(statuss)) ||("8".equals(statuss))){//核收
						statuss="C";
						this.configService.setExamInfoChargeItemPacsStatus(req_num,statuss);
					} else if(("33".equals(statuss))) {
						
						ExaminfoChargingItemDTO eciDTO = this.getExamstatusByReqno(req_num);
						if(eciDTO!=null) {
							if(!"Y".equals(eciDTO.getExam_status())) {
								statuss="N";
								this.configService.setExamInfoChargeItemPacsStatus(req_num,statuss);
							}else {
								rb.setCode("error");
								rb.setText("error:"+req_nums+"申请单号已检查，不可撤销！");
								break;
							}
						}
					} else if(("9".equals(statuss))) {
						if("Z".equals(ei.getStatus())) {
							rb.setCode("error");
							rb.setText("error:体检中心已总检，不可撤销！");
							break;
						}
					}
				}else {
					rb.setCode("error");
					rb.setText("error:申请单号或体检号无效！");
					break;
				}
				
			}
		}
	
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getCode()+"----"+rb.getText());
		if("error".equals(rb.getCode())) {
			return rb.getText();
		}
		return rb.getCode();
	}
		
	public ResLisStatusBeanHK getreqNoPacsStatus(String xmlstr){
		ResLisStatusBeanHK reqno= new ResLisStatusBeanHK();
		
		try{
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			reqno.setPersionid(document.selectSingleNode("root/patientId").getText());//患者id
			reqno.setMessageId(document.selectSingleNode("root/visitNo").getText());//体检号
			reqno.setStatus(document.selectSingleNode("root/applyStatus").getText());//申请单状态
			String req_nums = "";
			
			Element root = document.getRootElement();
			Element element = (Element) root.element("applyNos");
			List<Element> applyNoEle = element.elements("applyNo");
			
			for (int i = 0; i < applyNoEle.size(); i++) {
				if(i==applyNoEle.size()-1) {
					req_nums = req_nums+applyNoEle.get(i).getText();
				}else {
					req_nums = req_nums+applyNoEle.get(i).getText()+",";
				}
			}
			
			reqno.setReqno(req_nums);//申请单号
			reqno.setCode("Success");
		}catch(Exception ex){
				reqno.setCode("error");
				reqno.setText("error:xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return reqno;
		
	}
	
		/**
		 * 
		 * @param url
		 * @param view_num
		 * @return
		 */
		public ExamInfoUserDTO getExamInfoForNumPacsStatus(String exam_info_id) throws ServiceException {
			StringBuffer sb = new StringBuffer();
			sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
					+ ",c.register_date,c.join_date,c.exam_times ");
			sb.append(" from exam_info c ");
			sb.append(" where c.is_Active='Y' ");		
			sb.append(" and c.exam_num = '" + exam_info_id + "' order by c.create_time desc ");	
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = null;
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			return eu;
		} 
		
		public PacsSummaryDTO getPacsSummaryInfo(String exam_info_id,String pacs_req_code) throws ServiceException {
			StringBuffer sb = new StringBuffer();
			sb.append(" select ps.id,ps.examinfo_num,ps.pacs_req_code,ps.examinfo_name from pacs_summary ps " + 
					" where ps.examinfo_num = '"+exam_info_id+"' and pacs_req_code = '"+pacs_req_code+"' ");
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, PacsSummaryDTO.class);
			PacsSummaryDTO psDTO = null;
			if((map!=null)&&(map.getList().size()>0)){
				psDTO= (PacsSummaryDTO)map.getList().get(0);			
			}
			return psDTO;
		}
		
		
		public ExaminfoChargingItemDTO getExamstatusByReqno(String pacs_req_code) throws ServiceException {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT a.id,a.exam_status FROM examinfo_charging_item a,exam_info m,pacs_summary b,pacs_detail c,charging_item d " + 
					" WHERE b.pacs_req_code = '"+pacs_req_code+"' AND c.summary_id=b.id AND c.chargingItem_num=d.item_code " + 
					" AND d.id=a.charge_item_id AND a.examinfo_id=m.id AND m.exam_num=b.examinfo_num AND a.isActive='Y' "  );
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExaminfoChargingItemDTO.class);
			ExaminfoChargingItemDTO eciDTO = null;
			if((map!=null)&&(map.getList().size()>0)){
				eciDTO= (ExaminfoChargingItemDTO)map.getList().get(0);			
			}
			return eciDTO;
		}
		
		
		
}
