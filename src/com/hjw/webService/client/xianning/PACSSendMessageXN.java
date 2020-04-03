package com.hjw.webService.client.xianning;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.xianning.bean.EXAMINFO;
import com.hjw.webService.client.xianning.bean.RequestParam;
import com.hjw.webService.client.xianning.bean.ResultXN;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class PACSSendMessageXN{
	private PacsMessageBody lismessage;
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
	public PACSSendMessageXN(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url,String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname,"lismessage:"+new Gson().toJson(lismessage, PacsMessageBody.class));
			TranLogTxt.liswriteEror_to_txt(logname,"url:"+url);
			String exam_num = lismessage.getCustom().getExam_num();
			if (StringUtil.isEmpty(exam_num)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {
				ExamInfoUserDTO eu=this.configService.getExamInfoForNum(lismessage.getCustom().getExam_num());
				if ((eu==null)||(eu.getId() <= 0)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					ControlActPacsProcess ca = new ControlActPacsProcess();
					List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
					
					for (PacsComponents pcs : lismessage.getComponents()) {
						RequestParam requestParam = new RequestParam();
						for (PacsComponent pc : pcs.getPacsComponent()) {
							EXAMINFO examinfo = new EXAMINFO();
							examinfo.setEXAM_CLASS(pc.getExam_class());
							examinfo.setEXAM_ITEM(pc.getItemName());
							examinfo.setEXAM_ITEM_CODE(pc.getPacs_num());
							examinfo.setEXAM_PARTS(pc.getItemName());
							requestParam.getEXAMINFO().add(examinfo);
						}
						
						requestParam.getPATIENTINFO().setEXAM_NO(pcs.getReq_no());
						requestParam.getPATIENTINFO().setPATIENT_ID(eu.getArch_num());
						requestParam.getPATIENTINFO().setNAME(eu.getUser_name());
						requestParam.getPATIENTINFO().setSEX(eu.getSex());
						requestParam.getPATIENTINFO().setDATE_OF_BIRTH(eu.getBirthday());
						requestParam.getPATIENTINFO().setAGE(""+eu.getAge());
						requestParam.getPATIENTINFO().setMAILING_ADDRESS(eu.getAddress());
						requestParam.getPATIENTINFO().setPHONE_NUMBER(eu.getPhone());
						requestParam.getPATIENTINFO().setREQ_PHYSICIAN(lismessage.getDoctor().getDoctorName());
						requestParam.getPATIENTINFO().setREQ_PHYSICIAN_CODE(lismessage.getDoctor().getDoctorCode());
//						requestParam.getPATIENTINFO().setDOCTOR_NAME();
//						requestParam.getPATIENTINFO().setDOCTOR_CODE();
						requestParam.getPATIENTINFO().setAPPLY_DATE(DateTimeUtil.getDateTime());
//						requestParam.getPATIENTINFO().setPHYS_SIGN();
//						requestParam.getPATIENTINFO().setCONSIDER();
//						requestParam.getPATIENTINFO().setREQ_MEMO();
						requestParam.getPATIENTINFO().setPERFORMED_BY(lismessage.getDoctor().getDept_code());
						requestParam.getPATIENTINFO().setPERFORMED_BY_NAME(lismessage.getDoctor().getDept_name());
						requestParam.getPATIENTINFO().setEXAM_CODE(eu.getExam_num());
						
//						String jsonString = JSONSerializer.toJSON(requestParam).toString();
						String jsonString = new Gson().toJson(requestParam, RequestParam.class);
						TranLogTxt.liswriteEror_to_txt(logname,"req:"+jsonString);
						
						String responseString = HttpUtil.doPost(url,jsonString,"utf-8");
						
						TranLogTxt.liswriteEror_to_txt(logname,"res:"+responseString);
						if (responseString != null) {
							ResultXN result = new Gson().fromJson(responseString, ResultXN.class);
							if("true".equals(result.getSuccess())) {
								ApplyNOBean an = new ApplyNOBean();
								an.setApplyNO(pcs.getReq_no());	
								list.add(an);
							} else {
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText("PACS返回错误:" + result.getMsg());
								TranLogTxt.liswriteEror_to_txt(logname, "PACS返回错误:" + result.getMsg());
							}
						} else {
							rb.getResultHeader().setTypeCode("AE");
							TranLogTxt.liswriteEror_to_txt(logname, "HTTP无返回");
						}
					}
					if(list.size() > 0) {
						ca.setList(list);
						rb.setControlActProcess(ca);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("pacs调用成功");
					}
				}
			}
		} catch (Exception ex){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "ret:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
	
}
