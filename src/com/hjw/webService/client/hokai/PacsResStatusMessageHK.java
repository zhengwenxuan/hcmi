package com.hjw.webService.client.hokai;


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

import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.interfaces.util.HttpUtil;

import com.hjw.util.DateTimeUtil;

import com.hjw.service.ConfigService;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ExamPicMessage;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.hokai.bean.ResCustomBeanHK;
import com.hjw.webService.client.hokai.bean.ResLisStatusBeanHK;
import com.hjw.webService.client.tj180.Bean.LisGetReqBean;
import com.hjw.webService.client.tj180.Bean.LisGetResStatusBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PacsResStatusMessageHK{
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
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + strbody);
		ResLisStatusBeanHK rb= new ResLisStatusBeanHK();
		rb=getreqNo(strbody);
		if("3".equals(rb.getPatienttypeCode())) {
			if("AA".equals(rb.getCode())){
				ZlReqPacsItemDTO zlReqPacsItem = configService.select_zl_req_pacs_item(rb.getReqno(), logname);
				ExamInfoUserDTO ei=this.getExamInfoForId(""+zlReqPacsItem.getExam_info_id());
				String statuss = rb.getStatus();
				String pac_nos = rb.getReqno(); 
				
				//新登记(01)，已检查(02)，已报告(03)，已审核(04), 已撤销(05)
				if("01".equals(statuss)||("02".equals(statuss))||("03".equals(statuss))||("04".equals(statuss))){
					statuss="C";
					
					this.configService.setExamInfoChargeItemPacsStatus(pac_nos,  statuss);
				}else if(("05".equals(statuss))){//已检查
					if("Z".equals(ei.getStatus())) {
						rb.setCode("AE");
						rb.setText("体检中心已总检，不可撤销");
					}
					
				}
			}
		}

		StringBuffer sb = new StringBuffer("");
		sb.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
		sb.append("	<id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>\n");
		sb.append("	<creationTime value=\"" + DateTimeUtil.getDateTimes() + "\"/>\n");
		sb.append("	<interactionId extension=\"S0078\" root=\"2.16.840.1.113883.1.6\"/>\n");
		sb.append("	<processingCode code=\"P\"/>\n");
		sb.append("	<processingModeCode/>\n");
		sb.append("	<acceptAckCode code=\"AL\"/>\n");
		sb.append("	<receiver typeCode=\"RCV\">\n");
		sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("			<id>\n");
		sb.append("				<item extension=\"SYS001\"/>\n");
		sb.append("			</id>\n");
		sb.append("		</device>\n");
		sb.append("	</receiver>\n");
		sb.append("	<sender typeCode=\"SND\">\n");
		sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("			<id>\n");
		sb.append("				<item extension=\"SYS009\"/>\n");
		sb.append("			</id>\n");
		sb.append("		</device>\n");
		sb.append("	</sender>\n");
		sb.append("	<!--AA成功，AE失败-->\n");
		sb.append("	<acknowledgement typeCode=\""+rb.getCode()+"\">\n");
		sb.append("		<!--请求消息ID-->\n");
		sb.append("		<targetMessage>\n");
		sb.append("			<id extension=\""+rb.getMessageId()+"\"/>\n");
		sb.append("		</targetMessage>\n");
		sb.append("		<acknowledgementDetail>\n");
		sb.append("			<text value=\""+rb.getText()+"\"/>\n");
		sb.append("		</acknowledgementDetail>\n");
		sb.append("	</acknowledgement>\n");
		sb.append("</MCCI_IN000002UV01>\n");
		return sb.toString();

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
			reqno.setYizhuId(document.selectSingleNode("abc:POOR_IN200902UV/abc:controlActProcess/abc:subject/abc:placerGroup/abc:component2/abc:observationRequest/abc:actOrderid/abc:item/@extension").getText());
			reqno.setPatienttypeCode(document.selectSingleNode("abc:POOR_IN200902UV/abc:controlActProcess/abc:subject/abc:placerGroup/abc:component2/abc:observationRequest/abc:patientType/abc:patienttypeCode/@code").getText());
			reqno.setReqno(document.selectSingleNode("abc:POOR_IN200902UV/abc:controlActProcess/abc:subject/abc:placerGroup/abc:component2/abc:observationRequest/abc:actOrderid/abc:item/@extension").getText());
			//操作代码和名称：分配条码(备管)(code=10)，采集(code=20),
			//寄件(待送检)(code=100),送检(code=150),送达(code=200),签收(code=250)(1..1),报告结果删除(code=260)(1..1)，标本作废(code=270)
			reqno.setStatus(document.selectSingleNode("abc:POOR_IN200902UV/abc:controlActProcess/abc:subject/abc:placerGroup/abc:component2/abc:observationRequest/abc:component1/abc:processStep/abc:code/@code").getText());
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
		public ExamInfoUserDTO getExamInfoForId(String exam_info_id) throws ServiceException {
			StringBuffer sb = new StringBuffer();
			sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
					+ ",c.register_date,c.join_date,c.exam_times ");
			sb.append(" from exam_info c ");
			sb.append(" where c.is_Active='Y' ");		
			sb.append(" and c.patient_id = '" + exam_info_id + "' ");	
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			return eu;
		} 
}
