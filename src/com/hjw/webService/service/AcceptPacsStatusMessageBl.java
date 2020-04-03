package com.hjw.webService.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.service.pacsbean.ResPacsStatusMessageBl;
import com.hjw.webService.service.pacsbean.RetPacsStatusCustome;
import com.hjw.wst.service.CommService;

public class AcceptPacsStatusMessageBl extends ServletEndpointSupport {
	private CommService commService;
	protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
	}

//	public String acceptMessageTest(String xmlmessage) {
//		return "返回ok----pacs结果回传" + xmlmessage;
//	}

	/**
	 * 
	 * @Title: accetpMessagePacs @Description: pacs
	 * 结果返回处理 @param: @return @return: String @throws
	 */
	public String accetpMessagePacs(String xmlmessage) {
		String filetpe = "respacsStatusbl";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		ResultBody frb = new ResultBody();
		String orderid = "";
		try {
			ResPacsStatusMessageBl rpm = new ResPacsStatusMessageBl(xmlmessage, true);
			RetPacsStatusCustome rc = new RetPacsStatusCustome();
			rc = rpm.rc;
			try {				
				if((rc.getExam_num()==null)||(rc.getExam_num().length()<=0)||(rc.getStatus()==null)||(rc.getStatus().length()<=0)||(rc.getPacs_summary_id().size()<=0)){
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText("pacs信息解析错误");
				}else{
					String exam_num=rc.getExam_num();
					String statuss = rc.getStatus();
					List<String> pac_nos = new ArrayList<String>(); 
					pac_nos=rc.getPacs_summary_id();
					/*代码	名称	简称
                       1	收到申请	SDSQ
                       2	已执行	YZX
                       3	初步报告	CBBG
                       4	确认报告	QRBG
                       9	其他	    QT
                       0	取消预约	SQ
                       5	作废	    ZF
                       6	已检查	YJC
                       pacs回复：
              　　　　　　　                  ２、３、４、６　不能撤销申请 2016年11月6日回复
                     */
					if(("2".equals(statuss))||("3".equals(statuss))||("4".equals(statuss))||("6".equals(statuss))){//核收
						statuss="C";
						this.commService.setExamInfoChargeItemPacsStatus(pac_nos, exam_num, statuss);
					}else if(("0".equals(statuss))||("5".equals(statuss))){//已检查
						statuss="N";
						this.commService.setExamInfoChargeItemPacsStatus(pac_nos, exam_num, statuss);
					}
					ResultHeader.setTypeCode("AA");
					ResultHeader.setText("pacs信息调用成功");
				}				
			} catch (Exception ex) {
				ex.printStackTrace();
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("pacs信息调用webservice错误");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("信息 xml解析错误");
		}
		frb.setResultHeader(ResultHeader);
		String reqxml = JaxbUtil.convertToXml(frb, true);
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + orderid + ":" + reqxml);
		return reqxml;
	}
}
