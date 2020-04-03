package com.hjw.webService.client.QHYHWX;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.QHYHWX.bean.WXYuYueResDataDTO;
import com.hjw.webService.client.QHYHWX.bean.WXYuYueResPackgesDTO;
import com.hjw.webService.client.QHYHWX.bean.WXYuYueCustomerItemDto;
import com.hjw.webService.client.QHYHWX.bean.WXYuYueResDTO;
import com.hjw.wst.DTO.CustomerInfoDTO;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.DTO.SetChargingItemDTO;
import com.hjw.wst.service.BatchService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class WXYuYue {

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
		batchService = (BatchService) wac.getBean("batchService"); //customerInfoService
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}

	public String ResWXYuYue(String req, String logname, String web_meth) {

		TranLogTxt.liswriteEror_to_txt(logname, "-----------------微信预约请求开始--------------------");
		TranLogTxt.liswriteEror_to_txt(logname, "入参：" + req);

		String json = "";
		// 解析微信预约请求入参
		WXYuYueCustomerItemDto wxyyitem = new Gson().fromJson(req, WXYuYueCustomerItemDto.class);
		String id_num = wxyyitem.getIdentityCard();// 身份证号
		String patPhoto = wxyyitem.getPatPhoto();// 身份证照片
		String patName = wxyyitem.getPatName();// 姓名
		String sex = wxyyitem.getSex();// 性别
		String age = wxyyitem.getAge();// 年龄
		String packageName = wxyyitem.getPackageName();// 预约套餐（多个套餐按；隔开）

		if (id_num.equals("") || id_num.length()<=0 ) {

			
			 json = "身份证不能为空!";
		}else if(patPhoto.equals("") || patPhoto.length()<=0){
			 json = "身份证照片不能为空!";
		}else if(patName.equals("") || patName.length()<=0){
			json = "姓名不能为空!";
		}else if(sex.equals("") ||sex.length()<=0){
			json = "性别不能为空!";
		}else if(age.equals("") || age.length()<=0){
			json = "年龄不能为空!";
		}else if(packageName.equals("") || packageName.length()<=0){
			json = "套餐不能为空!";
		}else{
		
			String sql = " SELECT  id,user_name,sex,arch_num,id_num FROM   customer_info   where   is_Active='Y' and  id_num='" + id_num + "'";
			
	
			ResultSet rs;
			int C_id = 0;
			String C_user_name="";
			String C_sex="";
			String C_arch_num="";
			String C_id_num="";
			
			try {
				rs = this.jdbcQueryManager.getConnection().createStatement().executeQuery(sql);
				if (rs.next()) {
					C_id = rs.getInt(1);
					C_user_name = rs.getString(2);
					C_sex = rs.getString(3);
					C_arch_num = rs.getString(4);
					C_id_num = rs.getString(5);
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(C_id==0){
				
				json = SaveCustomerInfoAndExaminfo(json, id_num, patName, sex, age, packageName, C_id);
			}
			TranLogTxt.liswriteEror_to_txt(logname, json);
			TranLogTxt.liswriteEror_to_txt(logname, "-----------------微信预约请求结束--------------------");
			
		}

		return json;
	}

	//档案表及体检信息表插入数据
	public String SaveCustomerInfoAndExaminfo(String json, String id_num, String patName, String sex, String age,
			String packageName,int C_id) {
		
		//WeiXinExaminePay  yuyueres = new WeiXinExaminePay();
		//wxyyResData wxyyResData = new wxyyResData();
		WXYuYueResDTO yuyueres = new WXYuYueResDTO();
		WXYuYueResDataDTO wxyyResData = new  WXYuYueResDataDTO();
		WXYuYueCustomerItemDto yuyuecustomer =	new WXYuYueCustomerItemDto();
		WXYuYueResPackgesDTO ResPackges = new WXYuYueResPackgesDTO();
		if(C_id ==0){
			//插入人员档案表
			CustomerInfoDTO customer_Info = new CustomerInfoDTO();
			
			//生成新的档案号
			String arch_num = this.batchService.GetCreateID("vipno");
			
			
			customer_Info.setArch_num(arch_num);//档案号
			customer_Info.setFlag("0");
			customer_Info.setBirthday("");//出生日期
			customer_Info.setCreater((long)1);//创建人 id
			customer_Info.setId_num(id_num);
			customer_Info.setIs_Active("Y");
			customer_Info.setNation("");
			customer_Info.setSex(sex);
			customer_Info.setUpdater((long)1);
			customer_Info.setUser_name(patName);
			customer_Info.setPhone("");
			customer_Info.setAddress("");
			customer_Info.setIdtype(1);
			customer_Info.setMembership_card("");
			//customer_Info.setCreate_time();
			
			
		 try {
			 
			  C_id = this.customerInfoService.insertCustomerInfo(this.jdbcQueryManager.getConnection(), customer_Info);
			 
			 if(C_id>0){
						//插入体检信息与收费项目表
					 	ExamInfoDTO exam_info = new ExamInfoDTO();
						String exam_num_new = this.batchService.GetCreateID("exam_no");
						
						//插入体检信息表
						exam_info.setExam_num(exam_num_new);//体检号
						exam_info.setCustomer_id(C_id);//档案id
					//	exam_info.setCenter_num(centernum);
						exam_info.setIs_marriage("");//婚否
						exam_info.setAge(Long.parseLong(age));//年龄
						exam_info.setStatus("Y");//体检状态
						exam_info.setExam_type("G");// 个人（团体）类型
						exam_info.setIs_Active("Y");//是否有效
						exam_info.setAddress("");//地址
						exam_info.setPhone("");//电话
						exam_info.setPicture_path("");//照片路径
						exam_info.setIs_after_pay("");//是否后收费
						exam_info.setAppointment("N");//是否预约
						exam_info.setCompany("");//公司单位名称
						//exam_info.setCreater(long(1));
						//exam_info.setUpdater(userid);
						exam_info.setData_source("001");//数据来源
						exam_info.setIs_sampled_directly("N");//是否直接采样
						exam_info.setGetReportWay("4");//取体检报告方式
						exam_info.setOrder_id("");//订单编号
						exam_info.set_level("");//所属部门名称
						exam_info.setPosition("");//职位
						//exam_info.setCustomer_type_id("");//人员类别
						exam_info.setCustomer_type("");//体检类型
						exam_info.setRemarke("");//备注
						exam_info.setDegreeOfedu("");//文化程度
						exam_info.setOthers("");//其他
						exam_info.setGroup_index("");//分组序列
						exam_info.setChargingType("");//付费方式
						exam_info.setReportAddress("");//体检报告寄送地址
						exam_info.setEmail("");//邮箱
						//exam_info.setCompany_id("");//单位id
						//exam_info.setBatch_id("");//批次任务id
						exam_info.setEmployeeID("");//体检者工号
						exam_info.setRegister_date("");//预约日期
						exam_info.setJoin_date("");//报道（参检）日期
						//exam_info.setGroup_id("");//分组
						exam_info.setVisit_no("");//就诊号
						exam_info.setWuxuzongjian(0);//是否需要总检
						exam_info.setExam_indicator("T");//支付方式
						
						int E_exam_info_id = this.customerInfoService.insertExamInfo(this.jdbcQueryManager.getConnection(), exam_info);
						
						//入参体检信息插入成功
						if(E_exam_info_id >0){
							
							//插入体检人员与套餐表关系表
							yuyueres= 	insert_examinfo_set(patName,id_num,E_exam_info_id,packageName);
						//	ExamInfoDTO examinfo = this.customerInfoService.getExamInfoForexamId(E_exam_info_id);
							
							//插入体检信息与收费项目关系表
							int exinfo_charging_item_id  = insert_examinfo_charging_item(E_exam_info_id,packageName);
							if(yuyueres !=null && exinfo_charging_item_id>0){
								json = new Gson().toJson(yuyueres, WXYuYueResDTO.class);
							}
							
						}else{
							yuyueres.setStatus("1");
							yuyueres.setErrorMsg("失败");
							
							yuyuecustomer.setPatName(patName);
							yuyuecustomer.setIdentityCard(id_num);
							yuyuecustomer.setSex(sex);
							yuyuecustomer.setAge(age);
						
							ResPackges.setPackageName(packageName);
							ResPackges.setOrderDate(DateTimeUtil.getDateTime());
							
							wxyyResData.setPatInfo(yuyuecustomer);
							wxyyResData.setPackages(ResPackges);
							yuyueres.setData(wxyyResData);
							
							json = new Gson().toJson(yuyueres, WXYuYueResDTO.class);
							
						}
						
						
				 }
		} catch (Exception e) {
			yuyueres.setStatus("1");
			yuyueres.setErrorMsg("失败");
			
			yuyuecustomer.setPatName(patName);
			yuyuecustomer.setIdentityCard(id_num);
			yuyuecustomer.setSex(sex);
			yuyuecustomer.setAge(age);
		
			ResPackges.setPackageName(packageName);
			ResPackges.setOrderDate(DateTimeUtil.getDateTime());
			
			wxyyResData.setPatInfo(yuyuecustomer);
			wxyyResData.setPackages(ResPackges);
			yuyueres.setData(wxyyResData);
			
			json = new Gson().toJson(yuyueres, WXYuYueResDTO.class);
			e.printStackTrace();
		}
  }else{
	  	yuyueres.setStatus("1");
		yuyueres.setErrorMsg("失败");
		
		yuyuecustomer.setPatName(patName);
		yuyuecustomer.setIdentityCard(id_num);
		yuyuecustomer.setSex(sex);
		yuyuecustomer.setAge(age);
	
		ResPackges.setPackageName(packageName);
		ResPackges.setOrderDate(DateTimeUtil.getDateTime());
		
		wxyyResData.setPatInfo(yuyuecustomer);
		wxyyResData.setPackages(ResPackges);
		yuyueres.setData(wxyyResData);
		
		json = new Gson().toJson(yuyueres, WXYuYueResDTO.class);
		}
		return json;
	}
	
	
	//插入体检信息与收费项目关系表
	public int insert_examinfo_charging_item(int e_exam_info_id, String packageName) {
		
		int set_id=0;
		double set_discount=0;
		double set_amount=0;
		int examinfo_set_id=0;
		
		int examinfochargingitem_id=0;
		
	//	ExamInfo examinfo =  this.customerInfoService.getExamInfoForId((long)(e_exam_info_id));
		
		
		String[] split = packageName.split(";");
		for (int i = 0; i < split.length; i++) {
			if(!split[i].equals("") && split[i] != null){
				
			
			List<SetChargingItemDTO> SetChargingItemList = new ArrayList<>();
				String set_sql = "select  id,set_discount,set_amount from exam_set where set_name='"+split[i]+"' and is_Active='Y'";
				 try {
					 ResultSet set_rs = this.jdbcQueryManager.getConnection().createStatement().executeQuery(set_sql);
					 if (set_rs.next()) {
						 set_id = set_rs.getInt(1);//套餐id
						 set_discount=set_rs.getDouble(2);//套餐折扣
						 set_amount=set_rs.getDouble(3);//套餐金额
						}
					 set_rs.close();
					 
					 
					 
					 if(set_id>0){
						 String charging_set = " select id,charging_item_id,exam_set_id,discount,amount from set_charging_item where exam_set_id='"+set_id+"' ";
						 ResultSet charging_set_rs = this.jdbcQueryManager.getConnection().createStatement().executeQuery(charging_set);
						 while (charging_set_rs.next()) {
							 SetChargingItemDTO setChargingItemDTO = new SetChargingItemDTO();
							 setChargingItemDTO.setId(charging_set_rs.getInt(1));
							 setChargingItemDTO.setCharging_item_id(charging_set_rs.getInt(2));
							 setChargingItemDTO.setExam_set_id(charging_set_rs.getInt(3));
							 setChargingItemDTO.setSet_discountss(charging_set_rs.getInt(4));
							 setChargingItemDTO.setAmount(charging_set_rs.getInt(5));
							 SetChargingItemList.add(setChargingItemDTO);
						
								
						}
						 charging_set_rs.close();
						 
						for (int j = 0; j < SetChargingItemList.size(); j++) {
							if(SetChargingItemList.get(j) != null){
								
								String inser_examinfo_charging_item = " insert into examinfo_charging_item(examinfo_id,charge_item_id,itemnum,exam_indicator,item_amount,discount,amount,isActive,final_exam_date,pay_status,"
										+ "team_pay_status,exam_status,is_new_added,create_time,check_status,is_application,"
										+ "change_item,his_req_status,personal_pay) " + "values('" + e_exam_info_id + "','"
										+ SetChargingItemList.get(j).getCharging_item_id() + "','','G','" + ""
										+ SetChargingItemList.get(j).getAmount() + "','" + SetChargingItemList.get(j).getSet_discountss() + "','" + SetChargingItemList.get(j).getAmount() + "','Y',"
										+ "'" + DateTimeUtil.getDateTime() + "','N','N','N','0','" + DateTimeUtil.getDateTime()+ "','0','N','N','N','"+SetChargingItemList.get(j).getAmount()+"')";
										
									
								PreparedStatement preparedStatement = this.jdbcQueryManager.getConnection().prepareStatement(inser_examinfo_charging_item, Statement.RETURN_GENERATED_KEYS);
								preparedStatement.executeUpdate();
								ResultSet examinfo_set_rs = preparedStatement.getGeneratedKeys();
								if (examinfo_set_rs.next()) {
									examinfochargingitem_id =examinfo_set_rs.getInt(1);
								}
								examinfo_set_rs.close();
							}	 
						}
				
					}
					
				} catch (SQLException e) {
					 
					e.printStackTrace();
				}
				
			}
		}
		return examinfochargingitem_id;
	}

	//插入体检人员与套餐关系表
	public WXYuYueResDTO insert_examinfo_set(String c_user_name, String c_id_num, int e_exam_info_id, String packageName) {
		//WeiXinExaminePay resyuyueres = new WeiXinExaminePay();
		WXYuYueResDTO yuyueres = new WXYuYueResDTO();
		ExamInfoDTO examinfo = this.customerInfoService.getExamInfoForexamId(e_exam_info_id);
		int set_id=0;
		double set_discount=0;
		double set_amount=0;
		int examinfo_set_id=0;
		
		String[] split = packageName.split(";");
		for (int i = 0; i < split.length; i++) {
			if(!split[i].equals("") && split[i] != null){
				String set_sql = "select  id,set_discount,set_amount from exam_set where set_name='"+split[i]+"' and is_Active='Y'";
				 try {
					 ResultSet set_rs = this.jdbcQueryManager.getConnection().createStatement().executeQuery(set_sql);
					 if (set_rs.next()) {
						 set_id = set_rs.getInt(1);//套餐id
						 set_discount=set_rs.getDouble(2);//套餐折扣
						 set_amount=set_rs.getDouble(3);//套餐金额
						}
					 set_rs.close();
					 if(set_id>0){
					 
					 String inser_examinfo_set = " insert into examinfo_set values ('"+e_exam_info_id+"','"+set_id+"','G','"+set_discount+"','"+set_amount+"','Y','"+DateTimeUtil.getDateTime()+"','0','1','"+DateTimeUtil.getDateTime()+"','1','"+DateTimeUtil.getDateTime()+"') ";
					 PreparedStatement prepareStatement = this.jdbcQueryManager.getConnection().prepareStatement(inser_examinfo_set,Statement.RETURN_GENERATED_KEYS);
					 prepareStatement.executeUpdate();
					 ResultSet examinfo_set_rs = prepareStatement.getGeneratedKeys();
					 if(examinfo_set_rs.next()){
						 examinfo_set_id = examinfo_set_rs.getInt(1);
					 	}
					 examinfo_set_rs.close();
					 if(examinfo_set_id>0){
						yuyueres.setStatus("0");
						yuyueres.setErrorMsg("成功");
						WXYuYueResDataDTO wxyyResData = new  WXYuYueResDataDTO();
						WXYuYueCustomerItemDto yuyuecustomer =	new WXYuYueCustomerItemDto();
						WXYuYueResPackgesDTO Packges = new WXYuYueResPackgesDTO();
						
						yuyuecustomer.setPatName(examinfo.getUser_name());
						yuyuecustomer.setIdentityCard(examinfo.getId_num());
						yuyuecustomer.setSex(examinfo.getSex());
						yuyuecustomer.setAge(examinfo.getAge()+"");
						
						
						Packges.setPackageName(split[i]);
						Packges.setOrderDate(DateTimeUtil.getDateTime());
						
						wxyyResData.setPatInfo(yuyuecustomer);
						wxyyResData.setPackages(Packges);
						yuyueres.setData(wxyyResData);
						
						/*wxyyResData.getPatInfo().setPackageName(examinfo.getUser_name());//病人姓名
						wxyyResData.getPatInfo().setIdentityCard(examinfo.getId_num());//身份证号
						wxyyResData.getPatInfo().setSex(examinfo.getSex());//性别
						wxyyResData.getPatInfo().setAge(examinfo.getAge()+"");//年龄						
						wxyyResData.getPackages().setPackageName(split[i]);
						wxyyResData.getPackages().setOrderDate(DateTimeUtil.getDateTime());*/
						
					 }
				  }
					
				} catch (SQLException e) {
					 
					e.printStackTrace();
				}
				
			}
		}
		
		return yuyueres;
	}

	
}
