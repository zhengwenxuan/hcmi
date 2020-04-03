package com.hjw.webService.client.hokai305;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.util.DateTimeUtil;

import com.hjw.service.ConfigService;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.hokai.bean.ResCustomBeanHK;
import com.hjw.webService.client.hokai.bean.ResLisStatusBeanHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PacsResStatusMessageHK305{
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

	public String getMessage(String strbody, String logname) {
		ResLisStatusBeanHK rb= new ResLisStatusBeanHK();
		rb=getreqNo(strbody);
			if("AA".equals(rb.getCode())){
				//平台推送的状态  (不止是体检系统的项目状态)  判断 有没有此申请单
				ZlReqPacsItemDTO zlReqPacsItem = configService.select_zl_req_pacs_item(rb.getReqno(), logname);
				if(zlReqPacsItem !=null){
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + strbody);
					ExamInfoUserDTO ei=this.getExamInfoForId(""+zlReqPacsItem.getExam_info_id());
					String statuss = rb.getStatus();
					String pac_nos = rb.getReqno(); 
					
					//1：开立； 2：收费；3：预约； 4：签到； 5：已叫； 6：开始检查； 7：结束检查；  8：出报告；0：已撤销;  9：预约取消
					if("1".equals(statuss)||"2".equals(statuss)||"3".equals(statuss)||"4".equals(statuss)
							||"5".equals(statuss)||"6".equals(statuss)||"7".equals(statuss)||"8".equals(statuss)){
						statuss="C";
						this.configService.setExamInfoChargeItemPacsStatus305(pac_nos,  statuss);
					}else if(("xxx".equals(statuss))){//撤销报告
						if("Z".equals(ei.getStatus())) {
							rb.setCode("AE");
							rb.setText("体检中心已总检，不可撤销");
						}
					}
				}else{
					rb.setCode("AA");
					rb.setText("不是体检的申请单,正常返回");
				}
		}else{
			rb.setCode("AE");
			rb.setText("code状态不正确");
		}

		StringBuffer sb = new StringBuffer("");
		sb.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
		sb.append("	<id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>\n");
		sb.append("	<creationTime value=\"" + DateTimeUtil.getDateTimes() + "\"/>\n");
		sb.append("	<interactionId extension=\"S0001\"/>\n");
		sb.append("	<receiver code=\"SYS009\"/>\n");
		sb.append("	<sender code=\"SYS005\"/>\n");
		sb.append("	<!--AA成功，AE失败-->\n");
		sb.append("	<acknowledgement typeCode=\""+rb.getCode()+"\">\n");
		sb.append("	<!--患者id-->\n");
		sb.append("	<id  extension=\""+rb.getPersionid()+"\"/>\n");
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
			reqno.setReqno(document.selectSingleNode("abc:POOR_IN200902UV/abc:controlActProcess/abc:subject/abc:placerGroup/abc:observationRequest/abc:id/abc:item/@extension").getText());
			//
			reqno.setStatus(document.selectSingleNode("abc:POOR_IN200902UV/abc:controlActProcess/abc:subject/abc:placerGroup/abc:observationRequest/abc:statusCode/@code").getText());
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
