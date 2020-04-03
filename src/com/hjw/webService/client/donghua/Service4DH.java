package com.hjw.webService.client.donghua;

import java.util.List;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.donghua.bean.ResponseDH;
import com.hjw.webService.client.donghua.bean.lis.ReportMsg;
import com.hjw.webService.client.donghua.bean.lis.Request_ReportMsgLis;
import com.hjw.webService.client.donghua.bean.lis.ResultMsg;
import com.hjw.webService.client.donghua.bean.pacs.Request_ReportPacs;
import com.hjw.webService.client.donghua.bean.pacs.ReturnReport;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 东华-长治二院-接收lis/pacs报告接口
 * @author zwx
 */
public class Service4DH extends ServletEndpointSupport {
	private ConfigService configService;
	private CustomerInfoService customerInfoService;
	private JdbcQueryManager jdbcQueryManager;
	private ThridInterfaceLog til;

	protected void onInit() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		this.configService = (ConfigService) wac.getBean("configService");
		this.customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
		this.jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public String acceptMessageTest(String xmlmessage) {
		return "返回ok----参数为：" + xmlmessage;
	}

	//2.5. 检验报告（体检系统打条码）
	public String SendReportMsg(String req) {
		String logname = "Service4DH-LIS";
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+req);
		ResponseDH responseDH = new ResponseDH();
		try {
			Request_ReportMsgLis request = JaxbUtil.converyToJavaBean(req, Request_ReportMsgLis.class);
			boolean flagss = true;
			for(ReportMsg report : request.getReportMsgs().getReportMsg()) {
				LisResult lisResult = new LisResult();
				try {
					ExamInfoUserDTO eu= configService.getExamInfoForBarcode(report.getLabNo());
					lisResult.setExam_num(eu.getExam_num());//	体检号
					
					ZlReqItemDTO zlReqItem = configService.select_zl_req_item(report.getOrdID(), logname);
					lisResult.setLis_item_code(zlReqItem.getCharging_item_id());//LIS组合项目代码
					
					long chargingItemid = Long.parseLong(zlReqItem.getCharging_item_id());
					ChargingItemDTO chargingItem = customerInfoService.getChargingItemForId(chargingItemid);
					lisResult.setLis_item_name("["+chargingItem.getItem_name()+"]");//LIS组合项目名称
				} catch(Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "关联zl_req_item表失败，继续入库...");
				}
				lisResult.setTil_id(logname);//	第三方通讯日志表id	thrid_interface_log表id简称
				lisResult.setSample_barcode(report.getLabNo());//条码号
				lisResult.setLis_item_name(lisResult.getLis_item_name() +report.getOrdID());//LIS组合项目名称
				lisResult.setDoctor(report.getEntryUser());//检查医生
				lisResult.setSh_doctor(report.getAuthUser());//审核医生
				for(ResultMsg resultMsg : report.getResultMsgs().getResultMsg()) {
					lisResult.setReport_item_code(resultMsg.getTestCode());//LIS报告细项代码
					lisResult.setReport_item_name(resultMsg.getTestName());//LIS报告细项名称
					lisResult.setItem_result(resultMsg.getResult());//项目结果
					lisResult.setItem_unit(resultMsg.getUnits());//项目单位
					lisResult.setFlag(resultMsg.getResultFlag());//高低标志	H-高 L-低N-正常HH-偏高报警LL-偏低报警C-危急
					lisResult.setRef(resultMsg.getRanges());//参考范围
					int seq_code = Integer.parseInt(resultMsg.getSequence());
					lisResult.setSeq_code(seq_code);//顺序号
					lisResult.setExam_date(report.getEntryDate()+" "+report.getEntryTime());//检查时间
					boolean succ = this.configService.insert_lis_result(lisResult);
					if (!succ) {
						flagss = false;
					}
//					for(OrgResultMsg orgResultMsg : resultMsg.getOrgResultMsgs().getOrgResultMsg()) {
//						orgResultMsg.get
//					}
				}
			}
			if (flagss) {
				TranLogTxt.liswriteEror_to_txt(logname, "lis报告 入库成功");
				responseDH.setResultCode("0");
				responseDH.setResultContent("lis报告 入库成功");
			} else {
				TranLogTxt.liswriteEror_to_txt(logname,"lis报告 入库失败");
				responseDH.setResultCode("1");
				responseDH.setResultContent("lis报告 入体检库失败");
			}
		} catch (Exception e) {
			responseDH.setResultCode("1");
			responseDH.setResultContent("lis报告 入体检库异常："+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String responseStr = JaxbUtil.convertToXmlWithOutHead(responseDH, true);
		TranLogTxt.liswriteEror_to_txt(logname,"response:"+responseStr);
		return responseStr;
	}
	
	//2.4. 检查报告
	public String ReturnReports(String req) {
		String logname = "Service4DH-PACS";
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+req);
		ResponseDH responseDH = new ResponseDH();
		try {
			Request_ReportPacs request = JaxbUtil.converyToJavaBean(req, Request_ReportPacs.class);
			List<ReturnReport> reportList = request.getReturnReports().getReturnReport();
			boolean flagss = true;
			for(ReturnReport report : reportList) {
				ZlReqPacsItemDTO zlReqPacsItem = configService.select_zl_req_pacs_item(report.getOrdRowID(), logname);
				ExamInfoUserDTO eu= configService.getExamInfoForExam_id(zlReqPacsItem.getExam_info_id());
				long chargingItemid = Long.parseLong(zlReqPacsItem.getCharging_item_ids());
				ChargingItemDTO chargingItem = customerInfoService.getChargingItemForId(chargingItemid);
				PacsResult pacsResult = new PacsResult();
				pacsResult.setReq_no(zlReqPacsItem.getPacs_req_code());//	申请单号	
				pacsResult.setPacs_checkno(report.getStudyNo());//	PACS报告ID	
				pacsResult.setExam_num(eu.getExam_num());//	体检号	
				pacsResult.setTil_id(logname);//	第三方通讯日志主表id	thrid_interface_log表id简称
				pacsResult.setItem_name(chargingItem.getItem_name());//	项目名称	多个项目名称以英文逗号隔开
				pacsResult.setPacs_item_code(zlReqPacsItem.getCharging_item_ids());//	PACS检查项目代码	多个项目代码以英文逗号隔开
//				pacsResult.setstudy_type="";//	检查类别	US(超声)MR(核磁)CT(CT检查)XX(射线检查)
//				pacsResult.setstudy_body_part="";//	检查部位	多个部位以英文逗号隔开
				pacsResult.setClinic_diagnose(report.getDiagnose());//	检查结论	
				pacsResult.setClinic_symptom(report.getExamSee());//	检查描述	
//				pacsResult.setclinic_advice="";//	医师意见	
//				pacsResult.setis_abnormal="";//	阳性状态	N-正常 Y-阳性报告 C-危急
				pacsResult.setReport_img_path(report.getImageFile());//	报告图片路径	多路径以^隔开，如：20180512\report_1.jpg^20180512\report_2.jpg
//				pacsResult.setimg_path="";//	检查图片(报告内图，俗称小图)	多路径以^隔开，如：20180512\1.jpg^20180512\2.jpg
				pacsResult.setReg_doc(report.getGetDoc());//	记录医生姓名	
				pacsResult.setCheck_doc(report.getReportDoc());//	检查医生姓名	
				pacsResult.setCheck_date(report.getReportDate()+" "+report.getReportTime());//	检查时间	
				pacsResult.setReport_doc(report.getReportDoc());//	报告医生	
				pacsResult.setReport_date(report.getReportDate()+" "+report.getReportTime());//	报告时间	
				pacsResult.setAudit_doc(report.getAuditDoc());//	审核医师	
				pacsResult.setAudit_date(report.getAuditDate()+" "+report.getAuditTime());//	审核时间	
//				pacsResult.setis_tran_image=1;//	是否取图	默认为1,0 - 不取,1 - 取
//				pacsResult.setis_report_image=1;//	是否为报告图	0 - 图片不包含文字报告	1 - 图片为整个报告	默认为1
				boolean succ = this.configService.insert_pacs_result(pacsResult);
				if (!succ) {
					flagss = false;
				}
			}
			if (flagss) {
				TranLogTxt.liswriteEror_to_txt(logname, "pacs报告 入库成功");
				responseDH.setResultCode("0");
				responseDH.setResultContent("pacs报告 入库成功");
			} else {
				TranLogTxt.liswriteEror_to_txt(logname,"pacs报告 入库失败");
				responseDH.setResultCode("1");
				responseDH.setResultContent("pacs报告 入体检库失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			responseDH.setResultCode("1");
			responseDH.setResultContent("pacs报告 入体检库异常："+com.hjw.interfaces.util.StringUtil.formatException(e));
		}	
		String responseStr = JaxbUtil.convertToXmlWithOutHead(responseDH, true);
		TranLogTxt.liswriteEror_to_txt(logname,"response:"+responseStr);
		return responseStr;
	}
}
