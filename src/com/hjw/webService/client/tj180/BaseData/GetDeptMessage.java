package com.hjw.webService.client.tj180.BaseData;


import java.util.ArrayList;
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

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.DeptInfo;
import com.hjw.webService.client.tj180.Bean.ResDeptBean;
import com.hjw.webService.service.Databean.Dept;
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
public class GetDeptMessage {
    private String msgname="getdept";
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
	
	public GetDeptMessage(){
		
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getDept(String url,String charset,String msgname) {
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
					classMap.put("deptInfo", DeptInfo.class);	 
					ResDeptBean rdb= new ResDeptBean();
					rdb = (ResDeptBean)JSONObject.toBean(jsonobject,ResDeptBean.class,classMap);
					if(rdb==null){
						res.setTypeCode("AE");
	    				res.setText(url  +" 返回无返回");
					}else if(!"200".equals(rdb.getStatus())){
						res.setTypeCode("AE");
	    				res.setText(rdb.getErrorinfo());	
					}else{
						res=insertdept(rdb.getDeptInfo(),res);
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
	private ResultHeader insertdept(List<DeptInfo> deptInfo,ResultHeader res) throws Exception {
		List<Dept> depList=new ArrayList<Dept>();
		for(DeptInfo dinfo:deptInfo){
			Dept dt= new Dept();
			dt.setAction("2");
			dt.setDept_code(dinfo.getDeptCode());
			dt.setDept_name(dinfo.getDeptName());
			dt.setInput_code(dinfo.getInputCode());
			depList.add(dt);			
		}
		String messeage = dataService.saveHisDept(depList);
		if(messeage.split("-")[0].equals("ok")){
			res.setTypeCode("AA");
			res.setText("交易成功");
		}else{
			res.setTypeCode("AE");
			res.setText(messeage.split("-")[1]);
		}
		return res;
	}
	
}
