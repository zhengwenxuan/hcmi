package com.hjw.webService.client.nanfeng;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.hanshou.bean.ExamInfoUserDTOHS;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class ServiceReport4NF  extends ServletEndpointSupport {

	private static ConfigService configService;
	private static CustomerInfoService customerInfoService;
	private static JdbcQueryManager jdbcQueryManager;
	private static Calendar checkDay;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public String ReceiveLisReport(String reqStr) {
		
		String logName = "LisReport";
//		reqStr = qukongge(reqStr).trim();
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + reqStr);
		String resMessage = "";
		String reStatus = "0";
		String reStr = "";
		List<LisResult> listL = null;
		
		try {
			listL = getLisReportForXml(reqStr,logName);
			TranLogTxt.liswriteEror_to_txt(logName, "listL:" + listL.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logName, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e1));
			reStatus = "-1";
			reStr = "error-lis报告数据解析失败！";
			resMessage = resMessage("ReceiveLisReportResponse",reStatus,reStr);
			TranLogTxt.liswriteEror_to_txt(logName, "res:" +resMessage);
			return resMessage;
		}
			
			for (int i=0;i<listL.size();i++) {
				LisResult lisResult = listL.get(i);
				ExamInfoUserDTO ei = new ExamInfoUserDTO();
				if(!"".equals(lisResult.getSample_barcode())) {
					ei = configService.getExamInfoForBarcode(lisResult.getSample_barcode());
					if ((ei == null) || (ei.getId() <= 0)) {
						reStatus = "-1";
						reStr = "error-根据样本号查无此人-sample_barcode：" + lisResult.getSample_barcode();
						break;
					} else if ("Z".equals(ei.getStatus())) {
						reStatus = "-1";
						reStr = "error-此人已经总检，请先取消总检再回传结果";
						break;
					} else {
						
						boolean succ = this.configService.insert_lis_result(lisResult);
						if (succ) {
							reStatus = "0";
							reStr = "success-lis信息 入库成功！";
						}else{
							reStatus = "-1";
							reStr = "error-lis信息 入库失败！";
							break;
						}
					}
				}else {
					TranLogTxt.liswriteEror_to_txt(logName, "error:" +"样本号sample_barcode为空！");
					reStatus = "-1";
					reStr = "error-样本号sample_barcode为空！";
					break;
				}
			}
			
		
		resMessage = resMessage("ReceiveLisReportResponse",reStatus,reStr);
		TranLogTxt.liswriteEror_to_txt(logName, "res:" +resMessage);
		return resMessage;
	}
	
	
	public static String qukongge(String xmlstr) {
		
		xmlstr = xmlstr.replace("\t", "");
		xmlstr = xmlstr.replace("\n", "");
		
		return xmlstr;
	}
	
	/**
	 * 解析lis报告数据
	 * @param xmlStr
	 * @param logName
	 * @return
	 * @throws DocumentException
	 * @throws UnsupportedEncodingException
	 */
	public List<LisResult> getLisReportForXml(String xmlStr,String logName) throws Exception{
		
		InputStream is = new ByteArrayInputStream(xmlStr.getBytes("utf-8"));
		SAXReader sax = new SAXReader();
		Document doc = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
		Element root = doc.getRootElement();
		Element element = (Element) root.element("labDetails");
		List<Element> labEle = element.elements("labDetail");
		
		List<LisResult> ListL = new ArrayList<LisResult>();
		
		for (int i = 0; i < labEle.size(); i++) {
			LisResult lisResult = new LisResult();
			
			Element elem = labEle.get(i);
			
			lisResult.setExam_num(doc.selectSingleNode("root/patientId").getText());   //体检号
			lisResult.setSample_barcode(doc.selectSingleNode("root/barcodeNo").getText());   //条码号
			lisResult.setLis_item_code(doc.selectSingleNode("root/str3").getText());
			lisResult.setLis_item_name(doc.selectSingleNode("root/reportTitle").getText());
			
			lisResult.setReport_item_code(elem.element("itemCode").getText());
			lisResult.setReport_item_name(elem.element("itemChiName").getText());
			lisResult.setExam_date(doc.selectSingleNode("root/labTime").getText());
			lisResult.setItem_result(elem.element("itemResult").getText());
			String itemResultUnit = "";
			try {
				itemResultUnit = elem.element("itemResultUnit").getText();
			} catch (Exception e) {
				// TODO: handle exception
			}
			lisResult.setItem_unit(itemResultUnit);
			lisResult.setFlag(elem.element("itemResultFlag").getText());
			String referenceDesc = "";
			try {
				referenceDesc = elem.element("referenceDesc").getText();
			} catch (Exception e) {
				// TODO: handle exception
			}
			lisResult.setRef(referenceDesc);
			String ss = elem.element("billSubNo").getText();
			System.out.println(ss);
			lisResult.setSeq_code(Integer.parseInt(ss.trim()));
			lisResult.setDoctor(doc.selectSingleNode("root/reportOperator").getText());
			lisResult.setSh_doctor(doc.selectSingleNode("root/auditOperator").getText());
			lisResult.setNote(doc.selectSingleNode("root/remrk2").getText());
			lisResult.setRead_flag(Integer.parseInt(doc.selectSingleNode("root/validFlag").getText()));
			ListL.add(lisResult);
		}
		
		
		
		return ListL;
	}
	
	public String resMessage(String receiveheader,String reStatus,String reStr) {
		
		StringBuffer res = new StringBuffer();
		
		res.append("<"+receiveheader+">");
		res.append("<root>");
		res.append("<message></message>");
		res.append("<ret_desc>"+reStr+"</ret_desc>");
		res.append("<ret_value>"+reStatus+"</ret_value>");
		res.append("</root>");
		res.append("</"+receiveheader+">");

		return res.toString();
	}
	
	
	
	public String ReceivePacsReport(String reqStr) {
		
		String logName = "PacsReport";
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + reqStr);
		String resMessage = "";
		String reStatus = "0";
		String reStr = "";
		PacsResult pacsResult = null;
		
		try {
			pacsResult = getPacsReportForXml(reqStr,logName);
		} catch (UnsupportedEncodingException | DocumentException e1) {
			e1.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logName, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e1));
			reStatus = "-1";
			reStr = "error-pacs报告数据解析失败！";
			resMessage = resMessage("ReceiveLisReportResponse",reStatus,reStr);
			TranLogTxt.liswriteEror_to_txt(logName, "res:" +resMessage);
			return resMessage;
		}
			
			ExamInfoUserDTOHS ei = new ExamInfoUserDTOHS();
			if(!"".equals(pacsResult.getReq_no())) {
				ei = this.getExamInfoForReqNumPacsRes(pacsResult.getReq_no());
				if ((ei == null) || (ei.getId() <= 0)) {
					reStatus = "-1";
					reStr = "error-pacs信息 查无此申请单号:" + pacsResult.getReq_no();
				} else if ("Z".equals(ei.getStatus())) {
					reStatus = "-1";
					reStr = "error-此人已经总检，请先取消总检再回传结果";
				} else {
					
					boolean succ = this.configService.insert_pacs_result(pacsResult);
					if (succ) {
						reStatus = "0";
						reStr = "success-pacs信息 入库成功！";
					}else{
						reStatus = "-1";
						reStr = "error-lis信息 入库失败！";
					}
				}
			}else {
				reStatus = "-1";
				reStr = "error-申请单号req_no为空！";
			}
			
		
		resMessage = resMessage("ReceiveLisReportResponse",reStatus,reStr);
		TranLogTxt.liswriteEror_to_txt(logName, "res:" +resMessage);
		return resMessage;
	}
	
	
	/**
	 * 解析pacs报告数据
	 * @param xmlStr
	 * @param logName
	 * @return
	 * @throws DocumentException
	 * @throws UnsupportedEncodingException
	 */
	public PacsResult getPacsReportForXml(String xmlStr,String logName) throws DocumentException, UnsupportedEncodingException {
		
		InputStream is = new ByteArrayInputStream(xmlStr.getBytes("utf-8"));
		SAXReader sax = new SAXReader();
		Document doc = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
		List<PacsResult> ListP = new ArrayList<PacsResult>();
		PacsResult pacsResult = new PacsResult();
		
		pacsResult.setExam_num(doc.selectSingleNode("root/patientId").getText());   //体检号
		pacsResult.setReq_no(doc.selectSingleNode("root/hisBillNo").getText());   //申请单号
		pacsResult.setPacs_checkno(doc.selectSingleNode("root/pacsBillNo").getText());   //报告id
		pacsResult.setItem_name(doc.selectSingleNode("root/reportTitle").getText());   //项目名称
		pacsResult.setPacs_item_code(doc.selectSingleNode("root/examClinicCode").getText());   //PACS检查项目代码
		pacsResult.setStudy_type(doc.selectSingleNode("root/examDevice").getText());   //检查类别
		pacsResult.setStudy_body_part(doc.selectSingleNode("root/examPartDesc").getText());   //检查部位
		pacsResult.setClinic_diagnose(doc.selectSingleNode("root/visitStateDesc").getText());   //检查结论
		pacsResult.setClinic_symptom(doc.selectSingleNode("root/examImgDesc").getText());   //检查描述
		pacsResult.setReport_img_path(doc.selectSingleNode("root/reportPdfurl").getText());   //报告图片路径
		pacsResult.setStudy_state(5);   //检查状态
		pacsResult.setReg_doc(doc.selectSingleNode("root/registerOperator").getText());   //记录医生姓名
		pacsResult.setCheck_doc(doc.selectSingleNode("root/examOperator").getText());   //检查医生姓名
		pacsResult.setCheck_date(doc.selectSingleNode("root/examTime").getText());   //检查时间
		pacsResult.setReport_doc(doc.selectSingleNode("root/reportOperator").getText());   //报告医生
		pacsResult.setReport_date(doc.selectSingleNode("root/reportTime").getText());   //报告时间
		pacsResult.setAudit_doc(doc.selectSingleNode("root/auditOperator").getText());   //审核医生
		pacsResult.setAudit_date(doc.selectSingleNode("root/auditTime").getText());   //审核时间
		pacsResult.setNote(doc.selectSingleNode("root/sourceSystemCode").getText());   //备注
		pacsResult.setStatus(Integer.parseInt(doc.selectSingleNode("root/validFlag").getText()));   //报告读取标识
		pacsResult.setIs_tran_image('1');   //是否取图（可以默认1）
		pacsResult.setIs_report_image(Integer.parseInt(doc.selectSingleNode("root/validFlag").getText()));   //是否为报告图（可以默认1）
		
		
		return pacsResult;
	}
	
	public ExamInfoUserDTOHS getExamInfoForReqNumPacsRes(String req_nums) throws ServiceException{
		String sql = " select m.id,m.age,m.exam_num,m.status,m.exam_type "
				+ " ,m.register_date,m.join_date,m.exam_times "
				+ " ,n.user_name,n.id_num,n.sex,n.birthday,n.phone,dep.dep_num "
				+ " from examinfo_charging_item a,exam_info m,customer_info n, "
				+ " pacs_summary b,pacs_detail c,charging_item d,department_dep dep " + " where b.pacs_req_code='" + req_nums
				+ "' and c.summary_id=b.id and c.chargingItem_num=d.item_code "
				+ " and d.id=a.charge_item_id and a.examinfo_id=m.id "
				+ " and m.exam_num=b.examinfo_num and a.isActive='Y' "
				+ " and m.customer_id = n.id"
				+ " and d.dep_id = dep.id ";
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ExamInfoUserDTOHS.class);
		ExamInfoUserDTOHS eu = new ExamInfoUserDTOHS();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTOHS)map.getList().get(0);			
		}
		return eu;
	}
	
}
