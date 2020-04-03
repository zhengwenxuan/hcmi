package com.hjw.webService.client.acmeway;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.acmeway.bean.Result;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class ReportImageGetMessageJNYY {
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public ResultPacsBody getMessage(String exam_num, String url,String logname,boolean debug) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname,"url:"+url);
			if (StringUtil.isEmpty(exam_num)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {
				ExamInfoUserDTO eu=configService.getExamInfoForNum(exam_num);
				if ((eu==null)||(eu.getId() <= 0)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					Map<String, Object> req = new HashMap<>();
					req.put("testOrder", exam_num);
					req.put("rptType", "M6001TestReport");//M6001综合评定
					req.put("rptCode", "");
					TranLogTxt.liswriteEror_to_txt(logname, "req:"+req);
					String res = HttpUtil.doGet(url, req, "utf-8");
					Result result = new Gson().fromJson(res, Result.class);
					
					if(result.getStatus() == 0) {
//						result.getBody()
						
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("pacs调用成功");
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("登记受试者信息返回："+result.getMessage());
						TranLogTxt.liswriteEror_to_txt(logname,"登记受试者信息返回："+result.getMessage());
					}
				}
			}
		} catch (Exception ex){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + xml);
		return rb;
	}
}
