package com.hjw.webService.client.haoyitong;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hsqldb.lib.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ArmyPatInfoBody;
import com.hjw.webService.client.Bean.ArmyPatInfoReq;
import com.hjw.webService.client.Bean.ArmyReserveBean;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class WenJuanGetMessageHYT{
	
	private static ConfigService configService;
	
	 static{
		   	init();
		   	}
			public static void init(){
				WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
				configService = (ConfigService) wac.getBean("configService");
			}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage(String url,long examinfo_id,String logname) {		
		ResultHeader rh=new ResultHeader();		
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "-----------------获取好医通问卷数据--开始------------------");
			if (examinfo_id > 0) {
//				ExamInfoUserDTO eu = this.configService.getExamInfoForExam_id(examinfo_id);
//				this.model.setExam_num(eu.getExam_num());
//				this.model.setArch_num(eu.getArch_num());
//				
//				String WXQZJ_URL = this.customerInfoService.getCenterconfigByKey("WXQZJ_URL").getConfig_value().trim();//获取微信前置机ip端口
//				String url = WXQZJ_URL + "getQuestionnaire.action?identity="+eu.getId_num();
//				TranLogTxt.liswriteEror_to_txt(logname, "url:"+url);
//				String body = HttpUtil.doGet(url, "utf-8");
//				TranLogTxt.liswriteEror_to_txt(logname, "body:"+body);
//				if(StringUtil.isEmpty(body)) {
//					return SUCCESS;
//				}
//				Questionnaire[] questionnaires = new Gson().fromJson(body, new Questionnaire[0].getClass());
//				this.model.setQuestionnaire(questionnaires[0]);
//				Answer[] answers = new Gson().fromJson(questionnaires[0].getAnswers(), new Answer[0].getClass());
//				Recommend[] recommends = new Gson().fromJson(questionnaires[0].getRecommends(), new Recommend[0].getClass());
//				this.model.setAnswers(answers);
//				this.model.setRecommends(recommends);
			}
			
			TranLogTxt.liswriteEror_to_txt(logname, "-----------------获取好医通问卷数据--结束------------------");
			TranLogTxt.liswriteEror_to_txt(logname,"");//换行
		} catch (Exception ex) {
			ex.printStackTrace();
			rh.setTypeCode("AE");
        	rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rh;
	}
}
