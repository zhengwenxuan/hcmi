package com.hjw.webService.client.zhaotong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.zhaotong.bean.DepReportBean;
import com.hjw.webService.client.zhaotong.bean.ExamReportBean;
import com.hjw.webService.client.zhaotong.bean.GetReqXMLBean;
import com.hjw.webService.client.zhaotong.bean.ItemReportBean;
import com.synjones.framework.persistence.JdbcQueryManager;

public class ExamReportDeptDetailMessageZT {

	private static ConfigService configService;
    private ThridInterfaceLog til;
    
    private static JdbcQueryManager jdbcQueryManager;

    static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public String getReportDeptDetail(String xmlStr,String logNema) {
		TranLogTxt.liswriteEror_to_txt(logNema, "req："+xmlStr);
		GetReqXMLBean xmlBean = this.getReqForReport(xmlStr, logNema);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer resXml = new StringBuffer();
		resXml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		resXml.append("<res>");
		
		
		if("success".equals(xmlBean.getCode())) {
			
			if(!StringUtil.isEmpty(xmlBean.getReportId())) {
				List<ExamReportBean> erList = this.getExamReportDetailForExamNum(xmlBean,logNema);
				if("A".equals(erList.get(0).getApprove_status())) {
					resXml.append("<resultCode>0</resultCode>");
					resXml.append("<resultDesc>SUCCESS</resultDesc>");
					if(erList.size()>0) {
						ExamReportBean er = erList.get(0);
						resXml.append("<userName>"+er.getUser_name()+"</userName>");
						resXml.append("<userGender>"+er.getSex_code()+"</userGender>");
						resXml.append("<userAge>"+er.getAge()+"</userAge>");
						resXml.append("<checkSeq>"+er.getExam_num()+"</checkSeq>");
						resXml.append("<reportId>"+er.getExam_num()+"</reportId>");
						resXml.append("<reportTitle>昭通市第一人民医院健康体检报告</reportTitle>");
						resXml.append("<checkDate>"+sdf1.format(er.getJoin_date())+"</checkDate>");
						resXml.append("<reportDate>"+sdf.format(er.getCreate_time())+"</reportDate>");
						
						//一般科室
						List<DepReportBean> listDep = this.getDeptReportInfo(er.getExam_num(),"YB",logNema);
						
						for (DepReportBean depReportBean : listDep) {
							resXml.append("<report>");
							resXml.append("<reportType>02</reportType>");
							resXml.append("<deptId>"+depReportBean.getDep_id()+"</deptId>");
							resXml.append("<deptName>"+depReportBean.getDep_name()+"</deptName>");
							resXml.append("<reportDoctorId>"+depReportBean.getExam_doctor_id()+"</reportDoctorId>");
							resXml.append("<reportDoctorName>"+depReportBean.getExam_doctor()+"</reportDoctorName>");
							resXml.append("<reportName>"+depReportBean.getDep_name()+"检查报告</reportName>");
							resXml.append("<reportResult>"+depReportBean.getExam_result()+"</reportResult>");
							
							int reportErrorNums = 0;
							List<ItemReportBean> listItem = this.getItemReportByDepid(er.getExam_num(), depReportBean.getDep_id(),"YB",logNema);
							for (ItemReportBean itemReportBean : listItem) {
								resXml.append("<item>");
								resXml.append("<itemId>"+itemReportBean.getItem_code()+"</itemId>");
								resXml.append("<itemName>"+itemReportBean.getItem_name()+"</itemName>");
								resXml.append("<testResult>"+itemReportBean.getExam_result()+"</testResult>");
								resXml.append("<itemRef></itemRef>");
								resXml.append("<itemType>03</itemType>");
								
								if("Z".equals(itemReportBean.getHealth_level())) {
									resXml.append("<quaResult>正常</quaResult>");
								}else if("Y".equals(itemReportBean.getHealth_level())) {
									resXml.append("<quaResult>异常</quaResult>");
									reportErrorNums++;
								}
								resXml.append("<unit>"+itemReportBean.getItem_unit()+"</unit>");
								resXml.append("</item>");
								
							}
							resXml.append("<reportErrorNums>"+String.valueOf(reportErrorNums)+"</reportErrorNums>");
							resXml.append("</report>");
						}
						
						//影像科
						List<DepReportBean> listDep_yx = this.getDeptReportInfo(er.getExam_num(),"YX",logNema);
						
						for (DepReportBean depReportBean_yx : listDep_yx) {
							resXml.append("<report>");
							resXml.append("<reportType>02</reportType>");
							resXml.append("<deptId>"+depReportBean_yx.getDep_id()+"</deptId>");
							resXml.append("<deptName>"+depReportBean_yx.getDep_name()+"</deptName>");
							resXml.append("<reportDoctorId>"+depReportBean_yx.getExam_doctor_id()+"</reportDoctorId>");
							resXml.append("<reportDoctorName>"+depReportBean_yx.getExam_doctor()+"</reportDoctorName>");
							resXml.append("<reportName>"+depReportBean_yx.getDep_name()+"检查报告</reportName>");
							resXml.append("<reportResult>详见明细</reportResult>");
							
							int reportErrorNums = 0;
							List<ItemReportBean> listItem_yx = this.getItemReportByDepid(er.getExam_num(), depReportBean_yx.getDep_id(),"YX",logNema);
							for (ItemReportBean itemReportBean_yx : listItem_yx) {
								resXml.append("<item>");
								resXml.append("<itemId>"+itemReportBean_yx.getItem_code()+"</itemId>");
								resXml.append("<itemName>"+itemReportBean_yx.getItem_name()+"</itemName>");
								resXml.append("<testResult>"+itemReportBean_yx.getExam_result()+"</testResult>");
								resXml.append("<itemRef></itemRef>");
								resXml.append("<itemType>03</itemType>");
								
								String quaResult = "";
								resXml.append("<quaResult>"+quaResult+"</quaResult>");
								resXml.append("<unit></unit>");
								resXml.append("</item>");
								
							}
							resXml.append("<reportErrorNums>"+String.valueOf(reportErrorNums)+"</reportErrorNums>");
							resXml.append("</report>");
						}
						
						//检验科
						List<DepReportBean> listDep_jy = this.getDeptReportInfo(er.getExam_num(),"JY",logNema);
						
						for (DepReportBean depReportBean_jy : listDep_jy) {
							resXml.append("<report>");
							resXml.append("<reportType>01</reportType>");
							resXml.append("<deptId>"+depReportBean_jy.getDep_id()+"</deptId>");
							resXml.append("<deptName>"+depReportBean_jy.getDep_name()+"</deptName>");
							resXml.append("<reportDoctorId>"+depReportBean_jy.getExam_doctor_id()+"</reportDoctorId>");
							resXml.append("<reportDoctorName>"+depReportBean_jy.getExam_doctor()+"</reportDoctorName>");
							resXml.append("<reportName>"+depReportBean_jy.getDep_name()+"检查报告</reportName>");
							resXml.append("<reportResult>"+depReportBean_jy.getExam_result()+"</reportResult>");
							int reportErrorNums = 0;
							List<ItemReportBean> listItem_jy = this.getItemReportByDepid(er.getExam_num(), depReportBean_jy.getDep_id(),"JY",logNema);
							for (ItemReportBean itemReportBean_jy : listItem_jy) {
								resXml.append("<item>");
								resXml.append("<itemId>"+itemReportBean_jy.getItem_code()+"</itemId>");
								resXml.append("<itemName>"+itemReportBean_jy.getItem_name()+"</itemName>");
								resXml.append("<testResult>"+itemReportBean_jy.getExam_result()+"</testResult>");
								resXml.append("<itemRef>"+itemReportBean_jy.getRef_value()+"</itemRef>");
								String itemType = "02";
								if("短文本型".equals(itemReportBean_jy.getItem_category())||"长文本型".equals(itemReportBean_jy.getItem_category())) {
									itemType = "02";
								}else if("数字型".equals(itemReportBean_jy.getItem_category())||"数值型".equals(itemReportBean_jy.getItem_category())) {
									itemType = "01";
								}
								
								resXml.append("<itemType>"+itemType+"</itemType>");
								String quaResult = "正常";
								
								if("0".equals(itemReportBean_jy.getRef_indicator())) {
									quaResult = "正常";
								}else if("1".equals(itemReportBean_jy.getRef_indicator())) {
									quaResult = "偏高";
									reportErrorNums++;
								}else if("2".equals(itemReportBean_jy.getRef_indicator())) {
									quaResult = "偏低";
									reportErrorNums++;
								}else {
									quaResult = "正常";
								}
								
								resXml.append("<quaResult>"+quaResult+"</quaResult>");
								resXml.append("<unit>"+itemReportBean_jy.getItem_unit()+"</unit>");
								resXml.append("</item>");
								
							}
							resXml.append("<reportErrorNums>"+String.valueOf(reportErrorNums)+"</reportErrorNums>");
							resXml.append("</report>");
						}
							
						
						
					}
				}else {
					resXml.append("<resultCode>-1</resultCode>");
					resXml.append("<resultDesc>此报告未完成审核！</resultDesc>");
				}
				
			}else {
				resXml.append("<resultCode>-1</resultCode>");
				resXml.append("<resultDesc>体检号为空！</resultDesc>");
			}
			
		}else {
			resXml.append("<resultCode>-1</resultCode>");
			resXml.append("<resultDesc>xml文件解析失败！</resultDesc>");
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
			xmlBean.setReportId(dom.selectSingleNode("req/reportId").getText());
			xmlBean.setCode("success");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			xmlBean.setCode("error");
		}
		
		return xmlBean;
	}
	
	
	public List<ExamReportBean> getExamReportDetailForExamNum(GetReqXMLBean getReqXMLBean,String logNema) {
		
		String sql = "select c.arch_num,c.user_name,c.id_num,c.sex,e.exam_num,e.final_doctor,e.age,e.join_date,es.create_time,es.final_exam_result,es.approve_status " + 
				" from customer_info c,exam_info e,exam_summary es " + 
				" where c.id = e.customer_id and es.exam_info_id = e.id and e.exam_num = '"+getReqXMLBean.getReportId()+"' " ;
		TranLogTxt.liswriteEror_to_txt(logNema, "sql："+sql);
		List<ExamReportBean> list = this.jdbcQueryManager.getList(sql, ExamReportBean.class);
		
		return list;
	}
	
	public List<DepReportBean> getDeptReportInfo(String exam_num,String dep_type,String logNema){
		
		String sql ="";
		if("YB".equals(dep_type)) {
			sql = "select ei.exam_num,edr.exam_doctor,edr.exam_result_summary as exam_result,dep.id as dep_id,dep.dep_name from exam_dep_result edr,exam_info ei,department_dep dep " + 
					"where edr.exam_info_id = ei.id and edr.dep_id = dep.id  " + 
					"and ei.exam_num = '"+exam_num+"' and ei.status = 'Z' and dep.dep_category = '17' " ;
		}else if("YX".equals(dep_type)) {
			sql = "select b.dep_id,b.dep_num,b.dep_name,b.exam_doctor,b.exam_num from " + 
					"(select a.*,v.exam_doctor,v.exam_result from " + 
					"(select ei.id ei_id,ei.exam_num,ei.final_doctor,ei.status,dep.dep_num,dep.id as dep_id,dep.dep_name,ci.item_code,ci.item_name,ci.sam_demo_id AS item_id,dep.dep_category " + 
					"from exam_info ei,charging_item ci,department_dep dep,examinfo_charging_item eci " + 
					"where ci.dep_id = dep.id and eci.examinfo_id = ei.id and eci.charge_item_id = ci.id " + 
					"and ei.exam_num = '"+exam_num+"' and ei.status = 'Z' and dep.dep_category = '21' and eci.exam_status = 'Y' ) a " + 
					"LEFT JOIN pacs_summary p ON a.exam_num = p.examinfo_num and p.examinfo_sampleId = a.item_id " + 
					"LEFT JOIN view_exam_detail v ON v.pacs_id = p.id  ) b group by b.dep_id,b.dep_num,b.dep_name,b.exam_doctor,b.exam_num ";
		}else if("JY".equals(dep_type)) {
			sql = "select b.dep_id,b.dep_num,b.dep_name from " + 
					"(select a.*,e.exam_doctor,e.exam_result,e.ref_value,e.item_unit,e.ref_indicator AS health_level from " + 
					"(select ei.exam_num,dd.id as dep_id,dd.dep_num,dd.dep_name,ci.id AS c_id,e.item_name,e.id AS item_id,ei.id,e.seq_code,ci.item_seq,dd.dep_category,eci.exam_date " + 
					"FROM exam_info ei,examinfo_charging_item eci,charging_item ci,department_dep dd,charging_item_exam_item cit,examination_item e " + 
					"WHERE ei.id = eci.examinfo_id AND eci.charge_item_id = ci.id AND ci.dep_id = dd.id AND ci.id = cit.charging_item_id " + 
					"AND cit.exam_item_id = e.id AND eci.isActive = 'Y' AND eci.exam_status = 'Y' AND ei.is_Active = 'Y' " + 
					"AND dd.dep_category='131' AND ci.item_category != '耗材类型' AND ei.exam_num = '"+exam_num+"') a  " + 
					"LEFT JOIN exam_result_detail e ON e.exam_info_id = a.id " + 
					"AND e.charging_item_id = a.c_id AND e.exam_item_id = a.item_id ) b group by b.dep_id, b.dep_num,b.dep_name  ";
		}
		TranLogTxt.liswriteEror_to_txt(logNema, "sql："+sql);
		List<DepReportBean> list = this.jdbcQueryManager.getList(sql, DepReportBean.class);
		
		return list;
	}
	
	public List<ItemReportBean> getItemReportByDepid(String exam_num,int dep_id,String dep_type,String logNema){
		String sql = "";
		if("YB".equals(dep_type)) {
			sql = "select ei.exam_num,dd.id as dep_id,dd.dep_name, " + 
					"ex.item_num as item_code,ex.item_name , ed.exam_doctor,ed.exam_date,ed.exam_result,ex.item_unit, ed.health_level " + 
					"from exam_info ei,examinfo_charging_item eci  " + 
					"left join charging_item ci on eci.charge_item_id = ci.id  " + 
					"left join department_dep dd on ci.dep_id = dd.id  " + 
					"left join charging_item_exam_item cie on cie.charging_item_id = ci.id  " + 
					"left join examination_item ex on cie.exam_item_id = ex.id  " + 
					"left join common_exam_detail ed on ed.exam_item_id = ex.id  " + 
					"where ei.id = eci.examinfo_id and ed.exam_info_id = ei.id  " + 
					"and ei.exam_num = '"+exam_num+"' and dd.id = "+dep_id+" and dd.dep_category = '17' and ei.status = 'Z' ";
		}else if("YX".equals(dep_type)) {
			sql = "select a.*,v.exam_doctor,v.exam_result from  " + 
					"(select ei.exam_num,ei.final_doctor,ei.status,dep.id as dep_id, " + 
					"dep.dep_name,ci.item_code,ci.item_name,ci.sam_demo_id AS sample_id " + 
					" from exam_info ei,charging_item ci,department_dep dep,examinfo_charging_item eci   " + 
					" where ci.dep_id = dep.id and eci.examinfo_id = ei.id  " + 
					" and eci.charge_item_id = ci.id and ei.exam_num = '"+exam_num+"'  " + 
					" and ei.status = 'Z' and dep.dep_category = '21' and eci.exam_status = 'Y'  and dep.id = "+dep_id+") a   " + 
					" LEFT JOIN pacs_summary p ON a.exam_num = p.examinfo_num and p.examinfo_sampleId = a.sample_id   " + 
					" LEFT JOIN view_exam_detail v ON v.pacs_id = p.id ";
		}else if("JY".equals(dep_type)) {
			sql = "select distinct erd.exam_info_id, erd.exam_item_id,ex.item_num as item_code, ex.item_name, " + 
					"erd.exam_doctor,erd.exam_date,erd.exam_result,erd.item_unit,erd.ref_value,erd.ref_indicator,ex.item_category  " + 
					" from exam_result_detail erd left join exam_info ei on erd.exam_info_id = ei.id  " + 
					" left join examination_item ex on erd.exam_item_id = ex.id  " + 
					" where ei.exam_num = '"+exam_num+"' order by  erd.exam_item_id asc";
		}
		TranLogTxt.liswriteEror_to_txt(logNema, "sql："+sql);
		List<ItemReportBean> list = this.jdbcQueryManager.getList(sql, ItemReportBean.class);
		
		return list;
	}
	
}
