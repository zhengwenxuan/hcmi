package com.hjw.webService.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.jinyu.body.ResultBodyJY;
import com.hjw.webService.service.lisbean.ResLisStatusMessage;
import com.hjw.webService.service.lisbean.RetLisStatusCustome;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class AcceptLisStatusMessage extends ServletEndpointSupport {
	private CommService commService;
	private static JdbcQueryManager jdbcQueryManager;
	protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
		jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
	}

	public String acceptMessageTest(String xmlmessage) {
		return "返回ok----lis结果回传" + xmlmessage;
	}

    /**
     * 
         * @Title: accetpMessageLis   
         * @Description: TODO(这里用一句话描述这个方法的作用)   
         * @param: @param xmlmessage
         * @param: @return      
         * @return: String      
         * @throws
     */
	public String accetpMessageLis(String xmlmessage) {
		String filetpe = "reslisstatus";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		ResultBody frb = new ResultBody();
		String orderid = "";
		try {
			ResLisStatusMessage rpm = new ResLisStatusMessage(xmlmessage, true);
			RetLisStatusCustome rc = new RetLisStatusCustome();
			rc = rpm.rc;
			if((rc.getExam_num()==null)||(rc.getExam_num().length()<=0)||(rc.getStatus()==null)||(rc.getStatus().length()<=0)||(rc.getSample_barcode().size()<=0)){
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("lis信息解析错误");
			}else{
				String exam_num=rc.getExam_num();
				String statuss = rc.getStatus();
				String samstatus="W";
				List<String> pac_nos = new ArrayList<String>(); 
				pac_nos=rc.getSample_barcode();
				/*代码	名称	简称
                1	收到申请	SDSQ
                2	已执行	YZX
                3	初步报告	CBBG
                4	确认报告	QRBG
                9	其他	QT
                0	取消预约	SQ
                5	作废	ZF
                6	已检查	YJC
                lis回复：
              　　　　　　　状态只２、３、４、６　不能撤销申请 2016年11月6日回复
                                         状态只传1、4、5，且1和4 不能撤销 2016年11月7日回复                    
              */
				if(("1".equals(statuss))||("2".equals(statuss))||("3".equals(statuss))||("4".equals(statuss))){//核收
					statuss="C";
					samstatus="H";
					this.commService.setExamInfoChargeItemLisStatus(pac_nos, exam_num, statuss,samstatus);
				}else if("6".equals(statuss)){//已检查
					statuss="Y";
					samstatus="E";
					this.commService.setExamInfoChargeItemLisStatus(pac_nos, exam_num, statuss,samstatus);
				}else if("5".equals(statuss)){//已检查
					statuss="N";
					samstatus="W";
					this.commService.setExamInfoChargeItemLisStatus(pac_nos, exam_num, statuss,samstatus);
				}				
				ResultHeader.setTypeCode("AA");
				ResultHeader.setText("lis信息调用成功");
			}
		} catch (Exception ex) {
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("lis信息 xml解析错误");
		}
		frb.setResultHeader(ResultHeader);
		String reqxml = JaxbUtil.convertToXml(frb, true);
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + orderid + ":" + reqxml);
		return reqxml;
	}
	
	
}
