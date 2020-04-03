package com.hjw.webService.client.hokai305;


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
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ExamPicMessage;
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
public class LISResStatusMessageHK305{
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
		ResLisStatusBeanHK rb= new ResLisStatusBeanHK();
		rb=getreqNo(strbody);

		if("AA".equals(rb.getCode())){
			//平台推送的状态  (不止是体检系统的项目状态)  判断 有没有此申请单
			ZlReqItemDTO zlReqItem = configService.select_zl_req_item(rb.getReqno(), logname);
			if(zlReqItem !=null){
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + strbody);
				ExamInfoUserDTO ei = ei=this.getExamInfoForNum(rb.getPersionid());
				String statuss = rb.getStatus();
				List<String> req_nums = new ArrayList<>();
				req_nums.add(zlReqItem.getLis_req_code());
				String samstatus="W";
					//1：开立； 2：收费； 3：签到； 4已叫  5：采集； 6：送检； 7：样本退回； 8：核收； 9：出报告；0：已撤销
					if(("1".equals(statuss))||("2".equals(statuss))||("5".equals(statuss))||("6".equals(statuss))||("8".equals(statuss))){//核收
						statuss="C";
						samstatus="H";
						this.configService.setExamInfoChargeItemLisStatus(req_nums, ei.getExam_num(),statuss,samstatus);
					} else if(("7".equals(statuss))||("9".equals(statuss)) || ("0".equals(statuss))) {
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
		
		sb.append("	<receiver  code=\"SYS009\"/>\n");
		
		sb.append("	<sender    code=\"SYS003\"/>\n");
	
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
			reqno.setPersionid(document.selectSingleNode("abc:POLB_IN224000UV01/abc:controlActProcess/abc:subject/abc:observationReport/abc:recordTarget/abc:patient/abc:id/abc:item/@extension").getText());// 获取根节点;
			reqno.setReqno(document.selectSingleNode("abc:POLB_IN224000UV01/abc:controlActProcess/abc:subject/abc:observationReport/abc:id/abc:item/@extension").getText());
			reqno.setStatus(document.selectSingleNode("abc:POLB_IN224000UV01/abc:controlActProcess/abc:subject/abc:observationReport/abc:statusCode/@code").getText());	
			reqno.setMessageId(document.selectSingleNode("abc:POLB_IN224000UV01/abc:id/@extension").getText());
			reqno.setCode("AA");
		}catch(Exception ex){
				reqno.setCode("AE");
				reqno.setText("xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return reqno;
		
	}
	
	/*public static void main(String[] args) {
		StringBuffer sb = new StringBuffer("");
		sb.append("");
		sb.append("<POLB_IN224000UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  ");
		sb.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->");
		sb.append("    <id extension=\"22a0f9e0-4454-11dc-a6be-3603d6866807\"/>   ");
		sb.append("    <!-- 消息创建时间(1..1) -->");
		sb.append("    <creationTime value=\"20111129220000\"/> ");
		sb.append("    <!-- 服务编码，S0063代表检验状态信息更新(1..1)-->");
		sb.append("    <interactionId extension=\"S0063\"/> ");
		sb.append("    <!-- 接受者(1..1) -->  ");
		sb.append("    <receiver code=\"接收系统编码\"/>");
		sb.append("    <!-- 发送者(1..1) -->  ");
		sb.append("    <sender code=\"发送系统编码\"/>  ");
		sb.append("    <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">");
		sb.append("        <subject typeCode=\"SUBJ\">  ");
		sb.append("            <observationReport classCode=\"GROUPER\" moodCode=\"EVN\"> ");
		sb.append("                <id> ");
		sb.append("                    <!--电子申请单编号(1..1)-->  ");
		sb.append("                    <item extension=\"1903194770\" root=\"2.16.156.10011.1.24\"/>");
		sb.append("                </id>");
		sb.append("                <!-- 操作日期(0..1) -->  ");
		sb.append("                <time value=\"20170102070000\"/> ");
		sb.append("                <!--操作者签名编码/名称-CA(0..1)-->  ");
		sb.append("                <signatureCode code=\"S\" value=\"李医生\"/>   ");
		sb.append("                <!-- 操作人编码/名称 (0..1) -->  ");
		sb.append("                <performer code=\"PRF\" displayName=\"李医生\"/> ");
		sb.append("                <!--执行科室 --> ");
		sb.append("                <location code=\"091977060\" displayName=\"呼吸内科\"> ");
		sb.append("                    <window code=\"窗口代码\" displayName=\"窗口名称\"/>   ");
		sb.append("                </location>");
		sb.append("                <!--申请单状态(0..1)-->  ");
		sb.append("                <statusCode code=\"状态编码\" value=\"状态名称\">");
		sb.append("                    <!--撤销或拒收时的原因描述(0..1)-->");
		sb.append("                    <originalText value=\"撤销或拒收原因描述\"/> ");
		sb.append("                </statusCode>  ");
		sb.append("                <!-- 标本信息-有标本状态时有此节点(0..1) -->   ");
		sb.append("                <specimen classCode=\"SPEC\">");
		sb.append("                    <!--标本ID/或者条码ID(0..1)-->   ");
		sb.append("                    <id extension=\"8190319004\"/>   ");
		sb.append("                    <!--标本类别代码(0..1)-->");
		sb.append("                    <code code=\"1\" displayName=\"标本类别名称\"/>");
		sb.append("                    <!--标本描述(0..1)-->");
		sb.append("                    <text value=\"描述\"/> ");
		sb.append("                </specimen>");
		sb.append("                <!--记录对象(0..1)-->");
		sb.append("                <recordTarget contextControlCode=\"OP\" typeCode=\"RCT\">  ");
		sb.append("                    <patient classCode=\"PAT\">  ");
		sb.append("                        <!--PatientID(0..1)-->   ");
		sb.append("                        <id>   ");
		sb.append("                            <item extension=\"100152\" root=\"2.16.156.10011.0.2.2\"/> ");
		sb.append("                        </id>  ");
		sb.append("                        <!--患者姓名(0..1)-->");
		sb.append("                        <name value=\"王五\"/>   ");
		sb.append("                        <!--性别(0..1)-->");
		sb.append("                        <administrativeGenderCode code=\"1\" displayName=\"男性\"/>  ");
		sb.append("                    </patient> ");
		sb.append("                </recordTarget>");
		sb.append("            </observationReport> ");
		sb.append("        </subject> ");
		sb.append("    </controlActProcess>   ");
		sb.append("</POLB_IN224000UV01> ");
		new LISResStatusMessageHK305().getMessage(sb.toString(), "a");
	}*/
	
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
}
