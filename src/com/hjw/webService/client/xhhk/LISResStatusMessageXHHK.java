package com.hjw.webService.client.xhhk;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.xhhk.bean.ResponseXHHK;
import com.hjw.webService.client.xhhk.lisbean.SetApplyStatus;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class LISResStatusMessageXHHK{
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
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + strbody);
		Type TypeToken = new TypeToken<List<SetApplyStatus>>() {}.getType();
	    List<SetApplyStatus> applyStatus = new Gson().fromJson(strbody, TypeToken);
		//SetApplyStatusList applyStatus = new Gson().fromJson(strbody, SetApplyStatusList.class);
		TranLogTxt.liswriteEror_to_txt(logname, "applyNo:"+applyStatus.get(0).getApplyNo()+"-itemCode:"+applyStatus.get(0).getItemCode()+"-applyStatus:"+applyStatus.get(0).getApplyStatus());
		
		ExamInfoUserDTO ei = configService.getExamInfoForBarcode(applyStatus.get(0).getApplyNo());
		List<String> req_nums = new ArrayList<>();
		req_nums.add(applyStatus.get(0).getApplyNo());
		
		ResponseXHHK response = new ResponseXHHK();
		//1 -新建、2-撤销、3-确认、4-签收、5-报告、6-复核（已确认）、6-打印 
		if(applyStatus.get(0).getApplyNo().length() <= 0 && "".equals(applyStatus.get(0).getApplyNo())|| applyStatus.get(0).getApplyNo().length()<=0  &&"".equals(applyStatus.get(0).getApplyNo())){
			response.setCode(-1);
			response.setMsg("状态通知失败  申请单条码号 项目编号缺失!!");
		}else{
			if("Z".equals(ei.getStatus())) {
				response.setCode(1);
				response.setMsg("此人已总检，不再接受状态通知");
			} else {
				if(applyStatus.get(0).getApplyStatus() == 1||applyStatus.get(0).getApplyStatus() == 2){			
				} else if(applyStatus.get(0).getApplyStatus() == 3||applyStatus.get(0).getApplyStatus() == 4) {
					this.configService.setExamInfoChargeItemLisStatus(req_nums, ei.getExam_num(),"C","W");
				} else if(applyStatus.get(0).getApplyStatus() == 5||applyStatus.get(0).getApplyStatus() == 6) {
					this.configService.setExamInfoChargeItemLisStatus(req_nums, ei.getExam_num(),"C","H");
				}
				response.setCode(0);
				response.setMsg("状态通知成功");
			}
			
		}
		
		
		String res = new Gson().toJson(response, ResponseXHHK.class);
		TranLogTxt.liswriteEror_to_txt(logname, res);
		return res;
	}
}
