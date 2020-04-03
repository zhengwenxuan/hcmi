package com.hjw.webService.client.dashiqiao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.webService.client.dashiqiao.ResCusBean.CustomerExam;
import com.hjw.webService.client.dashiqiao.ResCusBean.Entry;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class FEEResMessageDSQ{
   private static JdbcQueryManager jdbcQueryManager;
   private static ConfigService configService;
   private static WebserviceConfigurationService webserviceConfigurationService;
   static {
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public String getMessage(String strbody, String logname) {
		Connection tjtmpconnect = null;
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + strbody);
		
		CustomerExam Customer = new Gson().fromJson(strbody, CustomerExam.class);
		String Persionid="";
		String his_num="";
		List<Entry> entryList = Customer.getEntry();
		for (int i = 0; i < entryList.size(); i++) {
			if(entryList.get(i).getResource().getResourceType().equals("Encounter")){
				
				 Persionid = Customer.getEntry().get(i).getResource().getExtension().get(0).getValueString();
			}else if(entryList.get(i).getResource().getResourceType().equals("ServiceRequest")){
				 his_num = Customer.getEntry().get(i).getResource().getCode().getCoding().get(0).getCode();
				
			}
		}
		
				ResHdMeessage rh = new ResHdMeessage();
			if(Persionid.equals("") && Persionid.length()<=0 && his_num.equals("") && his_num.length()<=0){

				rh.setStatus("0");
				rh.setMessage("就诊号和关联码不能为空");
			}else{
				ExamInfoUserDTO ei = ei=this.getExamInfoForNum(Persionid,logname);
				if(ei != null){
					try {
						tjtmpconnect = this.jdbcQueryManager.getConnection();
						String sb1 = " select eci.examinfo_id,eci.charge_item_id,eci.pay_status,eci.id as eci_id "
								+ " from charging_item c, exam_info e,examinfo_charging_item eci "
								+ " where eci.charge_item_id=c.id  and eci.examinfo_id=e.id  and e.is_Active='Y'  "
								+ " and eci.isActive='Y' and eci.pay_status !='Y'  "
								+ " and e.patient_id='"+Persionid+"' and c.his_num='"+his_num+"' ";
						TranLogTxt.liswriteEror_to_txt(logname, "查询 操作语句： " + sb1);
						ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
						while (rs.next()) {
							long examinfo_id=rs.getLong("examinfo_id");
							long charge_item_id = rs.getLong("charge_item_id");
							long eci_id = rs.getLong("eci_id");
							String pay_status = rs.getString("pay_status");
								
							if("N".equals(pay_status)) {
									String updatesql=" update examinfo_charging_item  set pay_status='Y' where  isActive='Y' "
											+ "and id='"+eci_id+"' and examinfo_id='"+examinfo_id+"' and charge_item_id='"+charge_item_id+"' ";
									TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + updatesql);
									tjtmpconnect.createStatement().execute(updatesql);
									
									//根据收费项目 修改eci的pay_status状态  后期可以判断  eci所有的收费状态 再修改 个人结算表
									/*updatesql="update charging_summary_single set charging_status='Y' where id='"+summary_id+"'";
									TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + updatesql);*/
									
									rh.setStatus("1");
									rh.setMessage("状态通知成功");
							} else {
									TranLogTxt.liswriteEror_to_txt(logname, "收费状态修改失败："+ei.getExam_num()+"-"+his_num);
									rh.setStatus("0");
									rh.setMessage("收费状态错误：不是待收费状态");
								}
							
						}
					} catch (Exception e) {
						TranLogTxt.liswriteEror_to_txt(logname, "ex:" + com.hjw.interfaces.util.StringUtil.formatException(e));
						e.printStackTrace();
						rh.setStatus("0");
						rh.setMessage(com.hjw.interfaces.util.StringUtil.formatException(e));
					} finally {
						try {
							if (tjtmpconnect != null) {
								tjtmpconnect.close();
							}
						} catch (SQLException sqle4) {
							sqle4.printStackTrace();
						}
					}
				}else{
					
					rh.setStatus("0");
					rh.setMessage("体检号不存在");
				}
			}
				
		String json = new Gson().toJson(rh, ResHdMeessage.class);
		TranLogTxt.liswriteEror_to_txt(logname, json);
		return json;
	}
		
	
	
	

	/**
		 * 
		 * @param logname 
		 * @param url
		 * @param view_num
		 * @return
		 */
		public ExamInfoUserDTO getExamInfoForNum(String exam_info_id, String logname) throws ServiceException {
			StringBuffer sb = new StringBuffer();
			sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
					+ ",c.register_date,c.join_date,c.exam_times ");
			sb.append(" from exam_info c ");
			sb.append(" where c.is_Active='Y' and c.status != 'Z' ");		
			sb.append(" and c.patient_id = '" + exam_info_id + "' order by c.create_time desc ");	
			 TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb + "\r\n");
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			return eu;
		} 
}
