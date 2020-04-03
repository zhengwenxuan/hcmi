package com.hjw.webService.client.chongqing;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.HttpServer.HttpDownloader;
import com.hjw.service.ConfigService;
import com.hjw.util.Base64;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.Timeutils;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.chongqing.util.ChongQingSetHL7;
import com.hjw.webService.client.hokai.bean.ResPacsMessageHK;
import com.hjw.webService.service.pacsbean.RetPacsCustome;
import com.hjw.webService.service.pacsbean.RetPacsItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.persistence.JdbcQueryManager;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.ORM_O01;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;

public class PacsResMessageCQ{
	private static CommService commService;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
    
	  static {
	    	init();
	    	}
	  
	public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			commService = (CommService) wac.getBean("commService");
			configService = (ConfigService) wac.getBean("configService");
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}

	/**
	 * 
	 * @Title: accetpMessagePacs @Description: pacs
	 * 结果返回处理 @param: @return @return: String @throws
	 */
	public String getMessage(String xmlmessage,String logname) {
		ExamInfoUserDTO ei = new ExamInfoUserDTO();
		Calendar deadline = Calendar.getInstance();
		deadline.set(2099, Calendar.FEBRUARY, 23, 0, 0, 0);
		if(new Date().after(deadline.getTime())) {
			ResultBody frb = new ResultBody();
			frb.getResultHeader().setTypeCode("AE");
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			frb.getResultHeader().setText("接口已过期，请联系火箭蛙，截止日期："+df.format(deadline.getTime()));
			TranLogTxt.liswriteEror_to_txt(logname,"接口已过期，请联系火箭蛙，截止日期："+df.format(deadline.getTime()));
			String reqxml = getres(frb,ei);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + reqxml);
			return reqxml;
		}
		
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		ResultBody frb = new ResultBody();
		String orderid = "";
		try {
			
			PacsResult plr = ChongQingSetHL7.resPacs(xmlmessage,logname);//Pacs结果
			try {
				if ((plr.getReq_no() != null) && (plr.getReq_no().length() > 0)) {
					ei = this.configService.getExamInfoForReqNum(plr.getReq_no());
					if ((ei == null) || (ei.getId() <= 0)) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 查无此申请单号" + plr.getReq_no());
					} else if ("Z".equals(ei.getStatus())) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 已经总检，入账错误" + plr.getReq_no());
					} else {
						
						plr.setExam_num(ei.getExam_num());
						String datatime = DateTimeUtil.shortFmt4(new Date());
						String dept_num = this.commService.getDepNumForPacs(plr.getReq_no());
						boolean succ = this.configService.insert_pacs_result(plr);
						if (succ) {
							frb.getResultHeader().setTypeCode("AA");
							frb.getResultHeader().setText("pacs 入库成功");
						} else {
							frb.getResultHeader().setTypeCode("AE");
							frb.getResultHeader().setText("pacs 入库失败");
						}
					
					
					}
				} else {
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText("pacs信息 体检编号为空");
					frb.setResultHeader(ResultHeader);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("pacs信息调用webservice错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
				frb.setResultHeader(ResultHeader);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("pacs信息 xml解析错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		frb.setResultHeader(ResultHeader);
		ei.setExam_num("XX");
		String reqxml = getres(frb,ei);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + orderid + ":" + reqxml);
		return reqxml;
	}
	
	private String getres(ResultBody rh, ExamInfoUserDTO ei){
		StringBuffer sb=new StringBuffer();
		ACK ack = new ACK();
		//  MSH|^~\&|PEIS||PACS^5008||20151228153822||ORR^O02|ORR_O02-20151228153822.131000|P|2.4|||NE|AL|||||
		try {
			MSH msh = ack.getMSH();
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getSendingApplication().getNamespaceID().setValue("PEIS");
			msh.getReceivingApplication().getNamespaceID().setValue("PACS");
			msh.getReceivingApplication().getHd2_UniversalID().setValue("5008");
			msh.getDateTimeOfMessage().getTime().setValue(DateTimeUtil.getDateTimes());
			msh.getMessageType().getMsg1_MessageCode().setValue("ORM");
			msh.getMessageType().getMsg2_TriggerEvent().setValue("001");
			msh.getMessageControlID().setValue("ADT_A04-"+DateTimeUtil.getDateTimes());
			//msh.getMessageControlID().setValue(guid);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			msh.getAcceptAcknowledgmentType().setValue("NE");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			MSA msa = ack.getMSA();
			//  MSA|AA|ORU_R01-20151228193420.848000|29421739|||^err_msg
			msa.getMsa1_AcknowledgmentCode().setValue(rh.getResultHeader().getTypeCode());
			msa.getMsa2_MessageControlID().setValue("ORU_R01-"+DateTimeUtil.getDateTimes());
			msa.getTextMessage().setValue(ei.getExam_num());//患者id
			msa.getMsa3_TextMessage().setValue(rh.getResultHeader().getText());
		} catch (DataTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}
	
    public static String changeFormat(String date) {
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8) + " " + date.substring(8, 10) + ":" + date.substring(10, 12) + ":"
				+ date.substring(12, 14);
	}
}
