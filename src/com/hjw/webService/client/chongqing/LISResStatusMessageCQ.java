package com.hjw.webService.client.chongqing;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.chongqing.util.ChongQingSetHL7;
import com.hjw.webService.client.hokai.bean.ResCustomBeanHK;
import com.hjw.webService.client.hokai.bean.ResLisStatusBeanHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISResStatusMessageCQ{
   private static JdbcQueryManager jdbcQueryManager;
   private static ConfigService configService;
   static {
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public String getMessage(String strbody, String logname) {
		ExamInfoUserDTO ei=new ExamInfoUserDTO();
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + strbody);
		ResLisStatusBeanHK rb= new ResLisStatusBeanHK();
		 rb = ChongQingSetHL7.resLisStatus(strbody,logname);//lis状态
		 rb=getreqNo(strbody);
		if("AA".equals(rb.getCode())){
//			ResCustomBeanHK rc =res_search(rb.getPersionid(),logname);
			if("AA".equals(rb.getCode())){
				 ei = this.getExamInfoForNum(rb.getPersionid());
				String statuss = rb.getStatus();
				List<String> req_nums = new ArrayList<>();
//				ZlReqItemDTO zlReqItem = configService.select_zl_req_item(rb.getReqno(), logname);
				req_nums.add(rb.getReqno());
				String samstatus="W";
					//操作代码和名称：分配条码(备管)(code=10)，采集(code=20),
					//寄件(待送检)(code=100),送检(code=150),送达(code=200),签收(code=250)(1..1),报告结果删除(code=260)(1..1)，标本作废(code=270)
	                 
					if(("IP".equals(statuss))||("100".equals(statuss))||("150".equals(statuss))||("200".equals(statuss))||("250".equals(statuss))){//核收
						statuss="C";
						samstatus="H";
						this.configService.setExamInfoChargeItemLisStatus(req_nums, ei.getExam_num(),statuss,samstatus);
					} else if(("260".equals(statuss))||("270".equals(statuss))) {
						if("Z".equals(ei.getStatus())) {
							rb.setCode("AE");
							rb.setText("体检中心已总检，不可撤销");
						}
					}
			}
		}
		String resmessage = getres(rb, ei);
		return resmessage;
	}
		
	private ResLisStatusBeanHK getreqNo(String xmlstr){
		ResLisStatusBeanHK reqno= new ResLisStatusBeanHK();
		try{
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			Map<String, String> xmlMap = new HashMap<>();
			xmlMap.put("abc", "urn:hl7-org:v3");
			SAXReader sax = new SAXReader();
			sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			reqno.setPersionid(document.selectSingleNode("abc:POLB_IN224000UV01/abc:controlActProcess/abc:subject/abc:observationReport/abc:recordTarget/abc:patient/abc:id/abc:item/@extension").getText());// 获取根节点;
			reqno.setReqno(document.selectSingleNode("abc:POLB_IN224000UV01/abc:controlActProcess/abc:subject/abc:observationReport/abc:id/abc:item/@extension").getText());
			//操作代码和名称：分配条码(备管)(code=10)，采集(code=20),
			//寄件(待送检)(code=100),送检(code=150),送达(code=200),签收(code=250)(1..1),报告结果删除(code=260)(1..1)，标本作废(code=270)
			reqno.setStatus(document.selectSingleNode("abc:POLB_IN224000UV01/abc:controlActProcess/abc:subject/abc:observationReport/abc:specimen/abc:specimen/abc:subjectOf1/abc:specimenProcessStep/abc:verifier/abc:modeCode/@code").getText());	
			reqno.setMessageId(document.selectSingleNode("abc:POLB_IN224000UV01/abc:id/@extension").getText());
			reqno.setCode("AA");
		}catch(Exception ex){
			reqno.setCode("AE");
			reqno.setText("xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return reqno;
		
	}
	
	/**
	 * 
	 * @param xmlmessage
	 * @return
	 */
	private ResCustomBeanHK res_search(String zl_pat_id,String logname){
		ResCustomBeanHK rcb= new ResCustomBeanHK();
		rcb.setCode("AE");
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,zl_pat_id,exam_num,"
					+ "zl_mzh,zl_tjh,flag from zl_req_patInfo where zl_pat_id='"
					+ zl_pat_id + "'";
			 TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1 + "\r\n");
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				rcb.setCode("AA");
				rcb.setExaminfo_id(rs1.getString("exam_info_id"));
			}
			rs1.close();
        }catch(Exception ex){
			
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rcb;
	}
		/**
		 * 
		 * @param url
		 * @param view_num
		 * @return
		 */
		public ExamInfoUserDTO getExamInfoForNum(String exam_info_id) throws ServiceException {
			StringBuffer sb = new StringBuffer();
			sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
					+ ",c.register_date,c.join_date,c.exam_times ");
			sb.append(" from exam_info c ");
			sb.append(" where c.is_Active='Y' and c.status != 'Z' ");		
			sb.append(" and c.patient_id = '" + exam_info_id + "' order by c.create_time desc ");	
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			return eu;
		} 
		
		private String getres(ResLisStatusBeanHK rb, ExamInfoUserDTO ei){
			StringBuffer sb=new StringBuffer();
			ACK ack = new ACK();
			//  MSH|^~\&|PEIS||PACS^5008||20151228153822||ORR^O02|ORR_O02-20151228153822.131000|P|2.4|||NE|AL|||||
			try {
				MSH msh = ack.getMSH();
				msh.getFieldSeparator().setValue("|");
				msh.getEncodingCharacters().setValue("^~\\&");
				msh.getSendingApplication().getNamespaceID().setValue("LIS");
				msh.getReceivingApplication().getNamespaceID().setValue("PEIS");
				msh.getReceivingApplication().getHd2_UniversalID().setValue("4007");
				msh.getDateTimeOfMessage().getTime().setValue(DateTimeUtil.getDateTimes());
				msh.getMessageType().getMsg1_MessageCode().setValue("ORM");
				msh.getMessageType().getMsg2_TriggerEvent().setValue("001");
				msh.getMessageControlID().setValue("ORM_001-"+DateTimeUtil.getDateTimes());
				//msh.getMessageControlID().setValue(guid);
				msh.getProcessingID().getProcessingID().setValue("P");
				msh.getVersionID().getVersionID().setValue("2.4");
				msh.getAcceptAcknowledgmentType().setValue("NE");
				msh.getApplicationAcknowledgmentType().setValue("AL");
				MSA msa = ack.getMSA();
				//  MSA|AA|ORU_R01-20151228193420.848000|29421739|||^err_msg
				msa.getMsa1_AcknowledgmentCode().setValue(rb.getCode());
				msa.getMsa2_MessageControlID().setValue("ORU_R01-"+DateTimeUtil.getDateTimes());
				msa.getTextMessage().setValue(ei.getExam_num());//患者id
				msa.getMsa3_TextMessage().setValue(rb.getText());
			} catch (DataTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return sb.toString();
		}
}
