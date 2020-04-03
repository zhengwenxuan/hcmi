package com.hjw.webService.client.zhaotong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.zhaotong.bean.ExamReportBean;
import com.hjw.webService.client.zhaotong.bean.GetReqXMLBean;
import com.synjones.framework.persistence.JdbcQueryManager;

public class ExamReportDetailMessageZT {

	private static ConfigService configService;
    private ThridInterfaceLog til;
    
    private static JdbcQueryManager jdbcQueryManager;

    static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public String getReportDetail(String xmlStr,String logNema) {
		TranLogTxt.liswriteEror_to_txt(logNema, "req："+xmlStr);
		GetReqXMLBean xmlBean = this.getReqForReport(xmlStr, logNema);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuffer resXml = new StringBuffer();
		resXml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		resXml.append("<res>");
		
		
		if("success".equals(xmlBean.getCode())) {
			
			if(!StringUtil.isEmpty(xmlBean.getReportId())) {
				List<ExamReportBean> erList = this.getExamReportDetailForExamNum(xmlBean,logNema);
				if("A".equals(erList.get(0).getApprove_status())) {
					resXml.append("<resultCode>0</resultCode>");
					resXml.append("<resultDesc>SUCCESS</resultDesc>");
					if(erList.size()>0) {
						if(erList.size()>0) {
							ExamReportBean er = erList.get(0);
							resXml.append("<userName>"+er.getUser_name()+"</userName>");
							resXml.append("<userGender>"+er.getSex_code()+"</userGender>");
							resXml.append("<userAge>"+er.getAge()+"</userAge>");
							resXml.append("<checkSeq>"+er.getExam_num()+"</checkSeq>");
							resXml.append("<reportId>"+er.getExam_num()+"</reportId>");
							resXml.append("<reportTitle>昭通市第一人民医院健康体检报告</reportTitle>");
							resXml.append("<checkDate>"+sdf1.format(er.getJoin_date())+"</checkDate>");
							resXml.append("<reportDate>"+sdf.format(er.getCreate_time())+"</reportDate>");
							resXml.append("<reportContent>"+rel(er.getFinal_exam_result())+"</reportContent>");
							resXml.append("<reportDoctor>"+er.getFinal_doctor()+"</reportDoctor>");
							resXml.append("<reportUrl></reportUrl>");
							
						}
					}
				}else {
					resXml.append("<resultCode>-1</resultCode>");
					resXml.append("<resultDesc>此报告未完成审核！</resultDesc>");
				}
				
			}else {
				resXml.append("<resultCode>-1</resultCode>");
				resXml.append("<resultDesc>体检号为空！</resultDesc>");
			}
			
		}else {
			resXml.append("<resultCode>-1</resultCode>");
			resXml.append("<resultDesc>xml文件解析失败！</resultDesc>");
		}
		
		
		resXml.append("</res>");
		TranLogTxt.liswriteEror_to_txt(logNema, "res:"+resXml.toString());
		return resXml.toString();
	}
	
	/**
	 * 解析获取用户体检报告记录xml
	 * @param xmlStr
	 * @param logNema
	 * @return
	 */
	public GetReqXMLBean getReqForReport(String xmlStr,String logNema) {
		
		GetReqXMLBean xmlBean = new GetReqXMLBean();
		
		try {
			InputStream is = new ByteArrayInputStream(xmlStr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document dom = sax.read(is);
			xmlBean.setReportId(dom.selectSingleNode("req/reportId").getText());
			xmlBean.setCode("success");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			xmlBean.setCode("error");
		}
		
		return xmlBean;
	}
	
    public static String replaceLine(String myString){  
        String newString=null;  
        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");  
        Matcher m = CRLF.matcher(myString);  
        if (m.find()) {  
          newString = m.replaceAll("\\\\n");  
        }  
        return newString;  
    }
    
    public static String rel(String myString) {
    	
    	String[] strl = myString.split("\n");
    	StringBuffer newStr = new StringBuffer();
    	
    	for (int i = 0; i < strl.length; i++) {
			String str = strl[i];
			if(!"".equals(str)) {
				newStr.append(str+"\\n");
			}
		}
    	
    	return newStr.toString();
    }
	
	
	public List<ExamReportBean> getExamReportDetailForExamNum(GetReqXMLBean getReqXMLBean,String logNema) {
		
		String sql = "select c.arch_num,c.user_name,c.id_num,c.sex,e.exam_num,e.final_doctor,e.age,e.join_date,es.create_time,es.final_exam_result,es.approve_status " + 
				" from customer_info c,exam_info e,exam_summary es " + 
				" where c.id = e.customer_id and es.exam_info_id = e.id and e.exam_num = '"+getReqXMLBean.getReportId()+"' " ;
		TranLogTxt.liswriteEror_to_txt(logNema, "sql："+sql);
		List<ExamReportBean> list = this.jdbcQueryManager.getList(sql, ExamReportBean.class);
		
		return list;
	}
	
	
	
}
