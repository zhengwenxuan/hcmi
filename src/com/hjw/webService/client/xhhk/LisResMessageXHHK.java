package com.hjw.webService.client.xhhk;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.xhhk.bean.ResponseXHHK;
import com.hjw.webService.client.xhhk.lisbean.ItemsResultXHHK;
import com.hjw.webService.client.xhhk.lisbean.SetApplyResult;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class LisResMessageXHHK{
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

	public String getMessage(String reqStr,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + reqStr);
		ResponseXHHK response = new ResponseXHHK();
		try {
			Type TypeToken = new TypeToken<List<SetApplyResult>>() {}.getType();
		    List<SetApplyResult> applyResult = new Gson().fromJson(reqStr, TypeToken);
		    for (int i = 0; i < applyResult.size(); i++) {
		    	ExamInfoUserDTO ei = new ExamInfoUserDTO();
				if(!"".equals(applyResult.get(i).getApplyNo())) {
					ei = configService.getExamInfoForBarcode(applyResult.get(i).getApplyNo());
					if ((ei == null) || (ei.getId() <= 0)) {
						response.setCode(1);
						response.setMsg("根据申请单号查无此人-ApplyNo：" + applyResult.get(i).getApplyNo());
					} else if ("Z".equals(ei.getStatus())) {
						response.setCode(1);
						response.setMsg("此人已经总检，请先取消总检再回传结果");
					} else {
						LisResult lisResult = new LisResult();
						lisResult.setTil_id(logName);
						lisResult.setExam_num(ei.getExam_num());
						lisResult.setSample_barcode(applyResult.get(i).getApplyNo());
						lisResult.setDoctor(applyResult.get(i).getReportDoct());
						
						String ReviewTime = applyResult.get(i).getReviewTime().replace("/", "-");
						lisResult.setExam_date(ReviewTime);
						
						lisResult.setSh_doctor(applyResult.get(i).getReviewDoct());
						lisResult.setLis_item_code(applyResult.get(i).getItemCode());
						boolean flagss = true;
						int seq_code = 0;
						for (ItemsResultXHHK item : applyResult.get(i).getItems()) {
							
							lisResult.setSeq_code(seq_code++);
							lisResult.setReport_item_code(item.getResultCode());
							lisResult.setReport_item_name(item.getResultName());
							lisResult.setItem_result(item.getResultValue());
							lisResult.setRef(item.getNormalRange());
							lisResult.setItem_unit(item.getUnit());
							
							//结果标识含义 ： H偏高、HH偏高报警、L偏低、LL偏低报警、P阳性、Q弱阳性、E错误，由LIS判断，仪器接口不用管
							lisResult.setFlag(item.getResultFlag());
							
							boolean succ = this.configService.insert_lis_result(lisResult);
							if (!succ) {
								flagss = false;
							}
						}
						if (flagss) {
							TranLogTxt.liswriteEror_to_txt(logName, "lis信息 入库成功");
							response.setCode(0);
							response.setMsg("lis信息 入库成功");
						} else {
							TranLogTxt.liswriteEror_to_txt(logName, "lis信息 入库错误");
							response.setCode(1);
							response.setMsg("lis信息 入库错误");
						}
					}
				} else {
					TranLogTxt.liswriteEror_to_txt(logName, "入参中申请单号为空-ApplyNo：" + applyResult.get(i).getApplyNo());
					response.setCode(1);
					response.setMsg("入参中申请单号为空-ApplyNo：" + applyResult.get(i).getApplyNo());
				}
			}
			
		} catch (Exception ex) {
			response.setCode(1);
			response.setMsg("lis信息-json解析错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String res = new Gson().toJson(response, ResponseXHHK.class);
		TranLogTxt.liswriteEror_to_txt(logName, res);
		return res;
	}
}
