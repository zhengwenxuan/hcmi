package com.hjw.webService.client.xhhk;


import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.util.DateTimeUtil;

import com.hjw.service.ConfigService;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.xhhk.bean.ResponseXHHK;
import com.hjw.webService.client.xhhk.pacsbean.ItemsApplyPacsXHHK;
import com.hjw.webService.client.xhhk.pacsbean.PacsReqIn;
import com.hjw.webService.client.xhhk.pacsbean.PacsReqOut;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.PacsSendDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class PacsXDReqMessageXHHK{
   private static JdbcQueryManager jdbcQueryManager;
   private static ConfigService configService;
   static {
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public String getMessage(String strbody, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "====================================XDByExam_num====================================");
		PacsReqIn reqIn = new Gson().fromJson(strbody, PacsReqIn.class);
		ExamInfoUserDTO eu= this.configService.getExamInfoForNum(reqIn.getHISID());
		
		ResponseXHHK response = new ResponseXHHK();
		if(eu == null || eu.getId()==0) {
			TranLogTxt.liswriteEror_to_txt(logname, "error-体检编号【"+reqIn.getHISID()+"】不存在");
			response.setCode(1);
			response.setMsg("error-体检编号【"+reqIn.getHISID()+"】不存在");
		} else if ("Z".equals(eu.getStatus())) {
			response.setCode(1);
			response.setMsg("体检编号【"+reqIn.getHISID()+"】的体检者已经总检。");
			TranLogTxt.liswriteEror_to_txt(logname, "体检编号【"+reqIn.getHISID()+"】的体检者已经总检。");
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("select p.pacs_req_code,c.view_num,c.item_code,ec.item_amount,dd.dep_name as dept_name,c.item_name,dd.dep_num,ec.id,ec.pay_status,ec.amount,c.id as itemId "
					+" from examinfo_charging_item ec,pacs_summary p,pacs_detail d,department_dep dd,charging_item c"
					+" where ec.charge_item_id = c.id and p.id = d.summary_id and d.chargingItem_num = c.item_code" 
					+" and c.dep_id = dd.id and ec.isActive = 'Y' and c.interface_flag = '2' and ec.change_item != 'C'" 
					+" and ec.pay_status != 'M' and ec.exam_status in ('N','D')"
					+" and ec.examinfo_id = "+eu.getId()+" and p.examinfo_num = '"+reqIn.getHISID()+"'" );
			sb.append(" and d.dep_num in ('ECG') ");//在这里判断传不传心电
			
			boolean isBufa = false;
			if(!isBufa){
				sb.append(" and ec.is_application = 'N'");
			}
			
			TranLogTxt.liswriteEror_to_txt(logname, "查项目sql:"+sb.toString());
			List<PacsSendDTO> pacsSendList = this.jdbcQueryManager.getList(sb.toString(), PacsSendDTO.class);
			
			String noPayItems = "";
			if("N".equals(eu.getIs_after_pay())){
				for(int i=0;i<pacsSendList.size();i++){
					String IS_HIS_PACS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_PACS_CHECK").getConfig_value().trim();
					if(("N".equals(pacsSendList.get(i).getPay_status()))&&("Y".equals(IS_HIS_PACS_CHECK))){
						if(i == pacsSendList.size() - 1){
							noPayItems += pacsSendList.get(i).getItem_name();
						}else{
							noPayItems += pacsSendList.get(i).getItem_name()+",";
						}
						pacsSendList.remove(i);
					}
				}
			}
			
			if(pacsSendList.size() == 0 && "".equals(noPayItems)){
				TranLogTxt.liswriteEror_to_txt(logname, "体检号【"+eu.getExam_num()+"】没有需要发送申请的影像科室项目!");
				response.setCode(1);
				response.setMsg("体检号【"+eu.getExam_num()+"】没有需要发送申请的影像科室项目!");
			}else if(pacsSendList.size() == 0 && (!"".equals(noPayItems))){
				TranLogTxt.liswriteEror_to_txt(logname, "体检号【"+eu.getExam_num()+"】项目("+noPayItems+")未付费!");
				response.setCode(1);
				response.setMsg("体检号【"+eu.getExam_num()+"】项目("+noPayItems+")未付费!");
			}else{
				String IS_LIS_PACS_DOCTOR_NAME = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value();
				PacsReqOut reqOut = new PacsReqOut();
				reqOut.setADM_ID(eu.getExam_num());//体检号
				reqOut.setHISID(eu.getExam_num());//体检号
				reqOut.setPATNAME(eu.getUser_name());//姓名
				reqOut.setSEX(eu.getSex());//性别
				reqOut.setPAT_AGE(""+eu.getAge());//年龄
				reqOut.setBIRTHDATE(eu.getBirthday());//生日
				reqOut.setTELEPHONE(eu.getPhone());//联系电话
				for(PacsSendDTO pacssend : pacsSendList){
					ItemsApplyPacsXHHK item = new ItemsApplyPacsXHHK();
					item.setPLA_ORD_NUM(pacssend.getPacs_req_code());//申请单号
					item.setADDRESS("");//检查设备类型
					item.setACCESSION_DATE(DateTimeUtil.getDateTime());//OperateTime
					item.setADM_ID_ISS("体检");//患者来源
					item.setMODALITY("");//检查设备类型
					item.setDIRECTION(pacssend.getItem_name());//,检查项目名称
					item.setDIAGNOSIS("");//临床信息
					item.setApplyDepartMentCode(pacssend.getDept_code());//申请科室代码
					item.setREQ_SERVICE(pacssend.getDept_name());//申请科室名称
					String doctorname = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
					item.setREQ_PHY_NAME(doctorname);//申请医生姓名
					reqOut.getItems().add(item);
				}
				String json = new Gson().toJson(reqOut, PacsReqOut.class);
				response.setCode(0);
				response.setMsg(json);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+json);
			}
		}
		if(response.getCode() == 0) {
			return response.getMsg();
		}
		String json = new Gson().toJson(response, ResponseXHHK.class);
		return json;
	}
}
