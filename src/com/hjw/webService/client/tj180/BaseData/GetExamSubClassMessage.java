package com.hjw.webService.client.tj180.BaseData;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.ExamSubClassInfo;
import com.hjw.webService.client.tj180.Bean.ResExamSubClassBean;
import com.hjw.wst.service.DataService;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 中金 上传 体检报告信息
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class GetExamSubClassMessage {
    private String msgname="resExamSubClass";
    private String charset="utf-8";
    private static JdbcQueryManager jdbcQueryManager;
    private static DataService dataService;
	
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		dataService = (DataService) wac.getBean("dataService");
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getData(String url,String charset,String msgname) {
		this.msgname=msgname;
		this.charset=charset;
		ResultHeader res=new ResultHeader();
		try {
			
			HttpClient httpClient = new DefaultHttpClient();  
			HttpGet httpPost = new HttpGet(url);	
	        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
	        
	        HttpResponse response=httpClient.execute(httpPost);            
            if(response != null){
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                	String result="";
                    result = EntityUtils.toString(resEntity,charset);
                    TranLogTxt.liswriteEror_to_txt(msgname, "res:" + result);
                    result = result.trim();
    				JSONObject jsonobject = JSONObject.fromObject(result);
	
					Map classMap = new HashMap();
					classMap.put("examSubClassInfo", ExamSubClassInfo.class);	 
					ResExamSubClassBean rdb= new ResExamSubClassBean();
					rdb = (ResExamSubClassBean)JSONObject.toBean(jsonobject,ResExamSubClassBean.class,classMap);
					if(rdb==null){
						res.setTypeCode("AE");
	    				res.setText(url  +" 返回无返回");
					}else if(!"200".equals(rdb.getStatus())){
						res.setTypeCode("AE");
	    				res.setText(rdb.getErrorinfo());	
					}else{
						insertExamSubClass(rdb.getExamSubClassInfo(),msgname);
						res.setTypeCode("AA");
	    				res.setText("");	
					}
                }else{
                	res.setTypeCode("AE");
    				res.setText(url  +" 返回无返回");
                }
            }else{
            	res.setTypeCode("AE");
				res.setText(url  +" 返回无返回");
            }			
		} catch (Exception ex) {
			res.setTypeCode("AE");
			res.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return res;
	}
	
	/**
	 * 
	 * @param deptInfo
	 * @param res
	 * @return
	 * @throws Exception
	 */
	/**
	 * 
	 * @param deptInfo
	 * @param res
	 * @return
	 * @throws Exception
	 */
	private void insertExamSubClass(List<ExamSubClassInfo> deptInfo, String logname) throws Exception {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			for (ExamSubClassInfo c : deptInfo) {
				if ((c.getExamSubClassName() != null) && (c.getExamSubClassName().trim().length() > 0)) {
					String sb1 = "SELECT id FROM pacs_examSubClass where exam_subcode_code='"+c.getExamSubClassName().trim()+"' ";
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb1);
					ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
					if (!rs1.next()) {
						sb1 = "insert into pacs_examSubClass(exam_subcode_code,exam_subclass_name,createdate) values('"
					+c.getExamSubClassName().trim()+"'"
								+ ",'"+c.getExamSubClassName().trim()+"','"+DateTimeUtil.getDateTime()+"') ";
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb1);
						tjtmpconnect.createStatement().execute(sb1);
					}
					rs1.close();
				}
			}
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}

	}
	
}
