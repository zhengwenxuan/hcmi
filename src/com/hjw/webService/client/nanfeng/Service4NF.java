package com.hjw.webService.client.nanfeng;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
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
import com.hjw.webService.client.nanfeng.bean.ItemResultMsgNF;
import com.hjw.webService.client.nanfeng.bean.ReportMsgNF;
import com.hjw.webService.client.nanfeng.util.NanfenHL7;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 东华-长治二院-接收lis/pacs报告接口
 * @author zwx
 */
public class Service4NF extends ServletEndpointSupport {
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
	public String SendReportMsg_LIS(String req) {
		String logname = "Service4NF-LIS";
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+req);
		System.out.println(req);
		req = req.replaceAll ("\n", "\r\n");
//		req = "MSH|^~\\&|LIS||PEIS||20140604150202||ORM^O01| f791da16-fe1b-4bd6-9df0-177d2624f178|P|2.5.1\r\n" + 
//				"PID|||0003852856^^^^PI||王江明^Wang Jiang Ming||||||||||\r\n" + 
//				"PV1||I|0241^^1007|||||||||||||||214046068001\r\n" + 
//				"ORC|SC|122530309|||||||||||\r\n" + 
//				"OBR||122530309|pacs01200|0315^CT检查|||||||||||||||||||||I";
		String resultMSG = "";
		String resStatus = "AA";
		String error = "success:成功！";
		try {
			List<ReportMsgNF> msgnftList = NanfenHL7.getMSGHl7_ORM(req);
			
			for (ReportMsgNF reportMsgNF : msgnftList) {
				
				ExamInfoUserDTO ei = ei=this.getExamInfoForNumLisStatus(reportMsgNF.getExam_num());
				if(ei!=null) {
					
					ExaminfoChargingItemDTO eciDTO = this.getExamstatusByReqno(reportMsgNF.getLabNo(), ei.getExam_num());
					if(eciDTO!=null) {
						boolean updFlag = true;
						List<ItemResultMsgNF> irmList = reportMsgNF.getResMsg();
						for (ItemResultMsgNF itemResultMsgNF : irmList) {
							List<String> req_nums = new ArrayList<>();
							String exam_status = itemResultMsgNF.getExam_status();
							if("S".equals(exam_status)||"F".equals(exam_status)||"Z-SR".equals(exam_status)) {
								String statuss = "C";
								String samstatus = "H";
								req_nums.add(reportMsgNF.getLabNo());
								this.configService.setExamInfoChargeItemLisStatus(req_nums, ei.getExam_num(),statuss,samstatus);
							}else {
								updFlag = false;
								resStatus = "AE";
								error = "error:回传状态不在范围内！";
								break;
							}
						}
						if(!updFlag) {
							
							break;
						}
					}else {
						resStatus = "AE";
						error = "error:申请单号"+reportMsgNF.getLabNo()+"无效！";
						break;
					}
				}else {
					resStatus = "AE";
					error = "error:体检号"+reportMsgNF.getExam_num()+"对应的体检信息不存在！";
					break;
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			resStatus = "AE";
			error = "error:操作错误！";
		}
		resultMSG = NanfenHL7.createResultMsg(resStatus, error, logname);
		TranLogTxt.liswriteEror_to_txt(logname,"resultMSG:"+resultMSG);
		return resultMSG;
	}
	
	//2.4. 检查报告
	public String SendReportMsg_PACS(String req) {
		String logname = "Service4NF-LIS";
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+req);
		String resultMSG = "";
		String resStatus = "AA";
		String error = "success:成功！";
		try {
			List<ReportMsgNF> msgnftList = NanfenHL7.getMSGHl7_ORM(req);
			
			for (ReportMsgNF reportMsgNF : msgnftList) {
				
				ExamInfoUserDTO ei = ei=this.getExamInfoForNumLisStatus(reportMsgNF.getExam_num());
				if(ei!=null) {
					
					ExaminfoChargingItemDTO eciDTO = this.getExamstatusByReqno(reportMsgNF.getLabNo(), ei.getExam_num());
					if(eciDTO!=null) {
						boolean updFlag = true;
						List<ItemResultMsgNF> irmList = reportMsgNF.getResMsg();
						for (ItemResultMsgNF itemResultMsgNF : irmList) {
							String exam_status = itemResultMsgNF.getExam_status();
							if("S".equals(exam_status)||"F".equals(exam_status)||"Z-SR".equals(exam_status)) {
								String statuss = "C";
								this.configService.setExamInfoChargeItemPacsStatus(reportMsgNF.getLabNo(), statuss);
							}else {
								updFlag = false;
								resStatus = "AE";
								error = "error:回传状态不在范围内！";
								break;
							}
						}
						if(!updFlag) {
							
							break;
						}
					}else {
						resStatus = "AE";
						error = "error:申请单号"+reportMsgNF.getLabNo()+"无效！";
						break;
					}
				}else {
					resStatus = "AE";
					error = "error:体检号"+reportMsgNF.getExam_num()+"对应的体检信息不存在！";
					break;
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		resultMSG = NanfenHL7.createResultMsg(resStatus, error, logname);
		TranLogTxt.liswriteEror_to_txt(logname,"resultMSG:"+resultMSG);
		return resultMSG;
	}
	
	public ExamInfoUserDTO getExamInfoForNumLisStatus(String exam_info_id) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times ");
		sb.append(" from exam_info c ");
		sb.append(" where c.is_Active='Y' and c.status != 'Z' ");		
		sb.append(" and c.exam_num = '" + exam_info_id + "' order by c.create_time desc ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = null;
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	public ExaminfoChargingItemDTO getExamstatusByReqno(String sample_barcode,String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT e.id AS examid,e.exam_status AS examstau,a.id AS samid,a.status AS samsta " + 
				" FROM sample_exam_detail a,exam_info b,examResult_chargingItem c,examinfo_charging_item e " + 
				" WHERE a.sample_barcode='"+sample_barcode+"' AND a.exam_info_id=b.id AND b.exam_num='"+exam_num+"' " + 
				" AND a.id=c.exam_id AND c.charging_id=e.charge_item_id AND e.examinfo_id=b.id AND c.result_type = 'sample' "  );
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExaminfoChargingItemDTO.class);
		ExaminfoChargingItemDTO eciDTO = null;
		if((map!=null)&&(map.getList().size()>0)){
			eciDTO= (ExaminfoChargingItemDTO)map.getList().get(0);			
		}
		return eciDTO;
	}
	
}
