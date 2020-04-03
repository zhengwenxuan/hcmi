package com.hjw.webService.client.QHYHWX;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jfree.date.DateUtilities;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.function.ARRAY;
import com.google.gson.Gson;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.QHYHWX.bean.WXJieGuoReqDTO;
import com.hjw.webService.client.QHYHWX.bean.WXJieGuoResChargingItemDTO;
import com.hjw.webService.client.QHYHWX.bean.WXJieGuoResChargingItem_examitemDTO;
import com.hjw.webService.client.QHYHWX.bean.WXJieGuoResCustomerDTO;
import com.hjw.webService.client.QHYHWX.bean.WXJieGuoResDTO;
import com.hjw.webService.client.QHYHWX.bean.WXJieGuoResDataDTO;
import com.hjw.webService.client.QHYHWX.bean.WeiXinExaminePay;
import com.hjw.wst.DTO.DepExamResultDTO;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.service.BatchService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class WXJianChaJieGuo {

	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static QueryManager queryManager;
	private static PersistenceManager persistenceManager;
	private static WebserviceConfigurationService webserviceConfigurationService;
	private static BatchService batchService;
	private static CustomerInfoService customerInfoService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		queryManager = (QueryManager) wac.getBean("queryManager");
		persistenceManager = (PersistenceManager) wac.getBean("persistenceManager");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		batchService = (BatchService) wac.getBean("batchService");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}
	
	public String ResWXJieGuo(String req, String logname, String web_meth) {
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------微信预约请求开始--------------------");
		TranLogTxt.liswriteEror_to_txt(logname, "入参：" + req);

		String json = "";
		int customer_info_id=0;
		// 解析微信端获取请求入参
				WXJieGuoReqDTO wxjieguo = new Gson().fromJson(req, WXJieGuoReqDTO.class);
				String id_num = wxjieguo.getIdentityCard();// 身份证号
				String teamId = wxjieguo.getTeamId();// 团体id
				String examDate = wxjieguo.getDate();// 检查日期(不传日期默认返回所有体检)
				

				if (id_num.equals("") || id_num.length()<=0 ) {
					 json = "身份证不能为空!";
				}else{
					
					String customer_info_sql = " select * from customer_info where  is_Active='Y' and id_num ='" + id_num + "' ";
					 try {
						Statement statement = this.jdbcQueryManager.getConnection().createStatement();
						ResultSet customer_info_rs = statement.executeQuery(customer_info_sql);
						
						 while (customer_info_rs.next()) {
								
							 customer_info_id = customer_info_rs.getInt(1);
						}
						 customer_info_rs.close();
						 
						 if(customer_info_id>0){
							 
							//通过档案号查询examinfo表
							 ExamInfoDTO  examinfo = getExamInfoForCustomerId(customer_info_id);
							 ExamInfoUserDTO examInfoUserDTO = getexaminfochargingitemuser(examinfo.getId());
							 //通过体检id查询  体检于收费项目关系表
							 List<ExaminfoChargingItemDTO> eci = this.customerInfoService.getExaminfoChargingItemforExamId(examinfo.getId());
							
							WXJieGuoResDTO jieGuoResDTO = new WXJieGuoResDTO();
							 WXJieGuoResDataDTO data = new WXJieGuoResDataDTO();
							 ArrayList<WXJieGuoResChargingItemDTO> packages = new ArrayList<>();
							 ArrayList<WXJieGuoResChargingItem_examitemDTO> itemlist = new ArrayList<>();
							
							 for (int i = 0; i < eci.size(); i++) {
								WXJieGuoResChargingItemDTO chargingItemDTO = new WXJieGuoResChargingItemDTO();
								List<DepExamResultDTO> hyResultList = getHyResultList(examinfo.getExam_num(),eci.get(i).getCharge_item_id(),eci.get(i).getExaminfo_id());
								
								 for (int j = 0; j < hyResultList.size(); j++) {
									 
										 WXJieGuoResChargingItem_examitemDTO ChargingItem_examitemDTO = new WXJieGuoResChargingItem_examitemDTO();
									
												 ChargingItem_examitemDTO.setDoctorAdvice("建议");
												 ChargingItem_examitemDTO.setItemName(hyResultList.get(j).getItem_name());
												 ChargingItem_examitemDTO.setItemPrice("未知");
												 ChargingItem_examitemDTO.setItemResult(hyResultList.get(j).getExam_result());
												 ChargingItem_examitemDTO.setReference(hyResultList.get(j).getRef_value());
												 itemlist.add(ChargingItem_examitemDTO);
										 
										 if(eci.get(i).getItem_name().equals(hyResultList.get(j).getDep_name()) && (eci.get(i).getCharge_item_id()+"").equals(hyResultList.get(j).getCharge_item_id()+"")){
											 	chargingItemDTO.setExamineTime(DateTimeUtil.getDateTimes());
												chargingItemDTO.setFee(eci.get(i).getItem_amount()+"");
												chargingItemDTO.setPackageName(eci.get(i).getItem_name());
												chargingItemDTO.setMasterDoctor(eci.get(i).getExam_doctor_name());
												chargingItemDTO.setSummary(""); 
												chargingItemDTO.setItems(itemlist);
										}
										
									
								}
								 
								
								packages.add(chargingItemDTO);
							}
							 
							 WXJieGuoResCustomerDTO PatInfo = new WXJieGuoResCustomerDTO();
							 PatInfo.setAge(examinfo.getAge()+"");
							 PatInfo.setDate(DateTimeUtil.getDateTime());
							 PatInfo.setIdentityCard(examinfo.getId_num());
							 PatInfo.setPatName(examinfo.getUser_name());
							 PatInfo.setSex(examinfo.getSex());
							 PatInfo.setWorkPlace("工作单位");
							 data.setPatInfo(PatInfo);
							 data.setPackages(packages);
							 
							 jieGuoResDTO.setErrorMsg("成功");
							 jieGuoResDTO.setStatus("0");
							 jieGuoResDTO.setData(data);
							
							
							 json = new Gson().toJson(jieGuoResDTO, WXJieGuoResDTO.class);
								
						
							 
						 }
						 
						 
						 
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
				}
		
		
		TranLogTxt.liswriteEror_to_txt(logname,"结果："+ json);
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------微信预约请求结束--------------------");
		
		return json;
	}
	
	private List<DepExamResultDTO> getPtResultList(String exam_num) {
	String sql =" select * from (select dd.dep_name,e.item_name,e.id as item_id,ei.id,dd.seq_code, e.seq_code as e_seq_code,dd.dep_category,'' as exam_result, '' as exam_doctor,eci.exam_date   "
			+ " from exam_info ei,examinfo_charging_item eci,charging_item ci,department_dep dd, charging_item_exam_item cit,examination_item e "
			+ " where ei.id = eci.examinfo_id "
			+ " and  eci.charge_item_id = ci.id "
			+ " and ci.dep_id = dd.id  "
			+ " and ci.id = cit.charging_item_id "
			+ " and cit.exam_item_id = e.id "
			+ " and eci.pay_status <> 'M'  "
			+ " and eci.isActive = 'Y'"
			+ " and eci.exam_status <> 'G' "
			+ " and ei.is_Active = 'Y' "
			+ " and dd.dep_category='17'  "
			+ " and ci.item_category != '耗材类型' "
			+ " and ei.exam_num = '"+exam_num+"' union all "
			+ " select a.dep_name,a.item_name,a.item_id,a.id,a.seq_code,a.e_seq_code,a.dep_category, e.exam_result_summary as exam_result,e.exam_doctor,a.exam_date "
			+ " from (select distinct dd.id as dep_id,dd.dep_name, '科室结论' as item_name,0 as item_id,ei.id,dd.seq_code,100000 as e_seq_code,dd.dep_category,eci.exam_date  "
			+ " from exam_info ei,examinfo_charging_item eci,charging_item ci,department_dep dd "
			+ " where  ei.id = eci.examinfo_id "
			+ " and eci.charge_item_id = ci.id "
			+ " and ci.dep_id = dd.id "
			+ " and  eci.pay_status <> 'M' "
			+ " and eci.isActive = 'Y' "
			+ " and eci.exam_status <> 'G' "
			+ " and ei.is_Active = 'Y' "
			+ " and dd.dep_category='17' "
			+ " and ci.item_category != '耗材类型' "
			+ " and ei.exam_num = '"+exam_num+"') a  "
			+ " left join exam_dep_result e "
			+ " on a.dep_id = e.dep_id "
			+ " and a.id = e.exam_info_id union all "
			+ " select a.dep_name,a.item_name,a.item_id,a.id,a.seq_code,a.e_seq_code,a.dep_category, e.suggestion as exam_result,e.exam_doctor,a.exam_date "
			+ " from (select distinct dd.id as dep_id,dd.dep_name, '科室建议' as item_name,-1 as item_id,ei.id,dd.seq_code,10000 as e_seq_code,dd.dep_category,eci.exam_date  "
			+ " from exam_info ei,examinfo_charging_item eci,charging_item ci,department_dep dd "
			+ " where  ei.id = eci.examinfo_id "
			+ " and eci.charge_item_id = ci.id "
			+ " and ci.dep_id = dd.id "
			+ " and  eci.pay_status <> 'M' "
			+ " and eci.isActive = 'Y' "
			+ " and eci.exam_status <> 'G' "
			+ " and ei.is_Active = 'Y' "
			+ " and dd.dep_category='17' "
			+ " and ci.item_category != '耗材类型' "
			+ " and ei.exam_num = '"+exam_num+"') a  "
			+ " left join exam_dep_result e "
			+ " on a.dep_id = e.dep_id "
			+ " and a.id = e.exam_info_id) a "
			+ " order by a.seq_code,a.e_seq_code ";
	
	
	List<DepExamResultDTO> PtList = this.jdbcQueryManager.getList(sql, DepExamResultDTO.class);
	return PtList;
	}

	private List<DepExamResultDTO> getHyResultList(String exam_num,  long charging_item_id,long exam_info_id) {
		String sql =" select a.amount,a.item_amount,a.discount,a.charge_item_id, a.dep_name,a.item_name,a.item_id,a.id,a.dep_category,e.exam_result,e.ref_value,e.item_unit,e.exam_doctor,e.ref_indicator as health_level,a.exam_date "
				+ " from (select eci.amount,eci.discount,eci.item_amount,eci.examinfo_id,eci.charge_item_id, ci.item_name as dep_name,e.item_name,e.id as item_id,ei.id,e.seq_code,ci.item_seq,dd.dep_category,eci.exam_date,ci.id c_id "
				+ " from exam_info ei,examinfo_charging_item eci,charging_item ci,department_dep dd,charging_item_exam_item cit,examination_item e "
				+ " where ei.id = eci.examinfo_id "
				+ " and eci.charge_item_id = ci.id "
				+ " and ci.dep_id = dd.id "
				+ " and ci.id = cit.charging_item_id "
				+ " and cit.exam_item_id = e.id "
				+ " and eci.pay_status <> 'M' "
				+ " and eci.isActive = 'Y' "
				+ " and eci.exam_status <> 'G' "
				+ " and ei.is_Active = 'Y' "
				+ " and dd.dep_category='131' "
				+ " and eci.examinfo_id = '"+exam_info_id+"' "
				+ " and eci.charge_item_id = '"+charging_item_id+"' "
				+ " and ei.exam_num = '"+exam_num+"') a "
				+ " left join exam_result_detail e "
				+ " on e.exam_info_id = a.id "
				+ " and e.charging_item_id = a.c_id "
				+ " and e.exam_item_id = a.item_id "
				+ " order by a.item_seq,a.seq_code ";
		
		List<DepExamResultDTO> HyList = this.jdbcQueryManager.getList(sql, DepExamResultDTO.class);
		return HyList;
	}

	private List<DepExamResultDTO> getYxResultList(String exam_num) {
		String sql = " select * from "
				+ " (select a.dep_name,a.item_name,v.id as item_id,a.id,a.seq_code,a.dep_category, v.exam_desc as exam_result,v.exam_doctor,a.exam_date,1 as code,a.item_seq,a.sam_demo_id "
				+ " from  (select ci.item_name as dep_name,'影像检查' as item_name,ci.sam_demo_id  ,ei.id,ei.exam_num,dd.seq_code,dd.dep_category,ci.item_seq,eci.exam_date "
				+ " from exam_info ei,examinfo_charging_item eci, charging_item ci,department_dep dd "
				+ " where ei.id = eci.examinfo_id "
				+ " and eci.charge_item_id = ci.id "
				+ " and  ci.dep_id = dd.id "
				+ " and eci.pay_status <> 'M' "
				+ " and eci.isActive = 'Y' "
				+ " and eci.exam_status <> 'G' "
				+ " and  ei.is_Active = 'Y' "
				+ " and dd.dep_category='21' "
				+ " and ei.exam_num = '"+exam_num+"') a "
				+ " left join pacs_summary p  "
				+ " on a.sam_demo_id = p.examinfo_sampleId "
				+ " and a.exam_num = p.examinfo_num "
				+ " left join view_exam_detail v "
				+ " on  v.pacs_id = p.id ) a "
				+ " order by a.seq_code,a.item_seq,a.code ";
		
		List<DepExamResultDTO> YxList = this.jdbcQueryManager.getList(sql, DepExamResultDTO.class);
		return YxList;
	}

		//通过customer_id查询exam_info表
		public ExamInfoDTO getExamInfoForCustomerId(int customer_info_id) {
		
			String examinfosql = "Select  * from exam_info e,customer_info c where e.customer_id='"+customer_info_id+"' and e.customer_id=c.id and  c.is_Active='Y' and  e.is_Active='Y' order by e.create_time desc ";
			List<ExamInfoDTO> examInfoDTOlist = this.jdbcQueryManager.getList(examinfosql, ExamInfoDTO.class);
				
			return examInfoDTOlist.get(0);
		}
		
		public ExamInfoUserDTO getexaminfochargingitemuser(long exam_info_id){
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select c.id,a.birthday,a.user_name,a.arch_num,a.id_num,a.sex,c.age,c.exam_num,c.is_marriage,"
							+ "c.position,c._level,c.group_id,n.group_name,c.remarke,c.others,c.status,c.employeeID");
			sb.append(",c.is_need_barcode,c.is_need_guide,c.phone,c.join_date,c.status,n.group_name,m.dep_name,"
					+ "c.register_date,c.exam_times,c.exam_type,x.type_name as customer_type_name,x.type_name as tjlxname,c.company_id,"
					+ "z.company_name,z.batch_name,z.contract_num,z.validity_date,c.address,c.batch_id,c.company,k.batch_name,c.final_date ");
			sb.append(" from customer_info a,exam_info c ");
			sb.append(" left join examinfo_batch b on b.examinfo_id=c.id ");
			sb.append(" left join customer_type x on x.id=c.customer_type_id ");
			sb.append("  left join data_dictionary y on y.data_code='TJLX' and y.id=c.customer_type  ");
			sb.append(" left join group_info  n on n.id=c.group_id ");
			sb.append(" left join batch  k on k.id=c.batch_id ");
			sb.append(" left join company_department  m on m.id = c._level  ");
			sb.append(" left join contract z on z.batch_id=c.batch_id and z.company_id= c.company_id ");
			sb.append(" where c.customer_id=a.id ");
			sb.append(" and c.id='" + exam_info_id + "' ");
			List<ExamInfoUserDTO> eiList = this.jdbcQueryManager.getList(sb.toString(), ExamInfoUserDTO.class);
			ExamInfoUserDTO ei = new ExamInfoUserDTO();
			if ((eiList != null) && (eiList.size() > 0)) {
				ei = eiList.get(0);
		}
			return ei;
		}	
		
}
