package com.hjw.webService.client.dashiqiao;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.PacsPictureDecodeBase64Util;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.xhhk.bean.ResponseXHHK;
import com.hjw.webService.client.xhhk.pacsbean.XDReportIn;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class PacsResMessageDSQ{
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
    
	static {
    	init();
    }
	  
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public String getMessage(String strbody,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + strbody);
		XDReportIn reportIn = new Gson().fromJson(strbody, XDReportIn.class);
		ResponseXHHK response = new ResponseXHHK();
		try {
			String req_no = reportIn.getFKey();
			if ((req_no != null) && (req_no.trim().length() > 0)) {
				ExamInfoUserDTO ei = new ExamInfoUserDTO();
				ei = this.configService.getExamInfoForReqNum(req_no);
				if ((ei == null) || (ei.getId() <= 0)) {
					response.setCode(1);
					response.setMsg("pacs信息 查无此申请单号" + req_no);
				} else if ("Z".equals(ei.getStatus())) {
					response.setCode(1);
					response.setMsg("pacs信息 已经总检，入账错误" + req_no);
				} else {
					PacsResult pacsResult = new PacsResult();
					pacsResult.setTil_id(logName);
					pacsResult.setExam_num(ei.getExam_num());
					pacsResult.setReq_no(req_no);
					pacsResult.setPacs_checkno(reportIn.getReportID());
					pacsResult.setReg_doc(reportIn.getReportDoc());
					pacsResult.setCheck_doc(reportIn.getCheckDoc());
					pacsResult.setCheck_date(reportIn.getCheckTime());
					pacsResult.setReport_doc(reportIn.getReportDoc());
					pacsResult.setReport_date(reportIn.getCheckTime());
					pacsResult.setAudit_doc(reportIn.getCheckDoc());
					pacsResult.setAudit_date(reportIn.getCheckTime());
					pacsResult.setImg_path(reportIn.getImage());
					
					if (reportIn.getImage().length() > 10) {
						String picname = PacsPictureDecodeBase64Util.decodeBase64JPG(ei.getExam_num(), req_no, DateUtil.getDateTimes(), reportIn.getImage());
						
						pacsResult.setIs_tran_image(0);
						pacsResult.setImg_path(picname);
						pacsResult.setReport_img_path(picname);
					} else {
						pacsResult.setIs_tran_image(0);
					}
					
					pacsResult.setClinic_diagnose(reportIn.getReportResult());
					pacsResult.setClinic_symptom(reportIn.getReportDes());
//					pacsResult.setStudy_body_part();
//					pacsResult.setStudy_type();
//					pacsResult.setItem_name();
					pacsResult.setPacs_item_code(reportIn.getCheckProjectCode());
					pacsResult.setIs_tran_image(0);
					
					boolean succ = this.configService.insert_pacs_result(pacsResult);
					if (succ) {
						response.setCode(0);
						response.setMsg("交易成功");
					} else {
						response.setCode(1);
						response.setMsg("pacs 入库失败");
					}
				}
			} else {
				response.setCode(1);
				response.setMsg("pacs信息 体检编号为空");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setCode(1);
			response.setMsg("pacs信息 xml解析错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String json = new Gson().toJson(response, ResponseXHHK.class);
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + json);
		return json;
	}
	
}
