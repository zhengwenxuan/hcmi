package com.hjw.webService.client.xintong;


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
import com.hjw.webService.client.body.ResultHeader;
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
public class LISResStatusMessageQH{
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
		
		String result = QHResolveXML.getNodeAttVal(strbody, "abc:CUST_OUT00004/abc:controlActProcess/abc:subject/abc:message_contents","val");
		String result2 = result.replaceAll("&lt;", "<");
		String resultXML = result2.replaceAll("&gt;", ">");
		
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + resultXML);
		ResLisStatusBeanHK rb= new ResLisStatusBeanHK();
		String resmessage="";
		rb = getreqNo(logname,resultXML);
			if("AA".equals(rb.getCode())){
				
				String statuss = rb.getStatus();
				List<String> req_nums = new ArrayList<>();
				ZlReqItemDTO zlReqItem = configService.select_zl_req_item(rb.getReqno(), logname);
				ExamInfoUserDTO ei = ei=this.getExamInfoForNum(zlReqItem.getExam_info_id()+"");
				req_nums.add(zlReqItem.getLis_req_code());
				String samstatus="W";
					//操作代码和名称：分配条码(备管)(code=10)，采集(code=20),
					//寄件(待送检)(code=100),送检(code=150),送达(code=200),签收(code=250)(1..1),报告结果删除(code=260)(1..1)，标本作废(code=270)
	                 
					if("execute".equals(statuss)){//核收
						statuss="C";
						samstatus="H";
						this.configService.setExamInfoChargeItemLisStatus(req_nums, ei.getExam_num(),statuss,samstatus);
					} else {
						if("Z".equals(ei.getStatus())) {
							rb.setCode("AE");
							rb.setText("体检中心已总检，不可撤销");
						}
					}
					
					resmessage="检验状态接受成功!!";
			}else{
				resmessage="检验状态接受失败!!";
			}
	
		return resmessage;
	}
		
	
	
	

	//解析lis状态  响应信息的xml
	public ResLisStatusBeanHK getreqNo(String logname, String result){
		
		TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销22==" + "解析申请单撤销22 "+ "\r\n");
		ResLisStatusBeanHK reqno= new ResLisStatusBeanHK();
		try{
		InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
		//Map<String, String> xmlMap = new HashMap<>();
		//xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		//sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
		
		reqno.setReqno(document.selectSingleNode("EXT_MULT/EXT_INFO/REQUEST/REQ_PARAMS/BUSINESS_NO").getText());//条码号
		reqno.setStatus(document.selectSingleNode("EXT_MULT/EXT_INFO/REQUEST/REQ_PARAMS/BUSINESS_STATUS").getText());	//状态
		reqno.setMessageId(document.selectSingleNode("EXT_MULT/EXT_INFO/REQUEST/REQ_PARAMS/EXEC_DEPT_ID").getText());	//消息id
		reqno.setCode("AA");
		
		TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销22==111" + reqno.getCode()+ "\r\n");
		TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销22==222" + reqno.getText()+ "\r\n");
		
		}catch(Exception ex){
			reqno.setCode("AE");
			reqno.setText("xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
	   
		TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销33---111==" +reqno.getCode() + "\r\n");
		TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销33---222==" +reqno.getText() + "\r\n");
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
			sb.append(" and c.id = '" + exam_info_id + "' order by c.create_time desc ");	
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			return eu;
		} 
		
		
		
}
