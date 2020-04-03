package com.hjw.webService.client.tj180.BaseData;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.PriceAndClinicInfo;
import com.hjw.webService.client.tj180.Bean.PriceItemInfo;
import com.hjw.webService.client.tj180.Bean.ResPriceAndClinicBean;
import com.hjw.webService.client.tj180.Bean.ResPriceItemBean;
import com.hjw.webService.service.Databean.DiagnosisPrice;
import com.hjw.webService.service.Databean.Price;
import com.synjones.framework.exception.ServiceException;
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
public class GetPriceAndClinicMessage {
    private String charset="utf-8";
    private static JdbcQueryManager jdbcQueryManager;
    private static ConfigService configService;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();		
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public GetPriceAndClinicMessage(){
		
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getPriceAndClinic(String url,String charset,String msgname) {
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
					classMap.put("clinicPriceInfo", PriceAndClinicInfo.class);	 
					ResPriceAndClinicBean rdb= new ResPriceAndClinicBean();
					rdb = (ResPriceAndClinicBean)JSONObject.toBean(jsonobject,ResPriceAndClinicBean.class,classMap);
					if(rdb==null){
						res.setTypeCode("AE");
	    				res.setText(url  +" 返回无返回");
					}else if(!"200".equals(rdb.getStatus())){
						res.setTypeCode("AE");
	    				res.setText(rdb.getErrorinfo());	
					}else{
						insertPriceAndClinic(rdb.getClinicPriceInfo(),msgname);
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
	private void insertPriceAndClinic(List<PriceAndClinicInfo> priceList,String logname) throws Exception {
		
		Connection tjtmpconnect = null;
		try {			
			if ((priceList != null) && (priceList.size() > 0)) {

			for (PriceAndClinicInfo price : priceList) {
				try {
					tjtmpconnect = this.jdbcQueryManager.getConnection();
		
						StringBuffer sb = new StringBuffer();
						sb.append("select clinic_item_class,clinic_item_code,charge_item_no,"
								+ "charge_item_class,charge_item_code,charge_item_spec,amount,units,backbill_rule"
								+ " from his_clinic_item_v_price_list where charge_item_class='"+price.getPriceClass()
								+"' and charge_item_code='"+price.getPriceCode()+"'and charge_item_spec='"
								+price.getPriceSpec()+"'and units='"+price.getPriceUnits()+"' and clinic_item_class='"+price.getClinicClass()+"' and clinic_item_code='"
								+price.getClinicCode()+"' ");
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb.toString());
						ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
						if (rs1.next()) {
							if(rs1.getInt("amount")!=price.getAmount()){
							String sql = "update his_clinic_item_v_price_list set amount = '"
									+ price.getAmount() + "',update_date='"+DateTimeUtil.getDateTime()+"' "
									+ "where  charge_item_class='"+price.getPriceClass()
								+"' and charge_item_code='"+price.getPriceCode()+"'and charge_item_spec='"
								+price.getPriceSpec()+"'and units='"+price.getPriceUnits()+"' and clinic_item_class='"+price.getClinicClass()+"' and clinic_item_code='"
								+price.getClinicCode()+"' ";							
							tjtmpconnect.createStatement().execute(sql);
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
							}
						} else {
							String sql = "insert into his_clinic_item_v_price_list("
									+ "clinic_item_class,clinic_item_code,charge_item_class,charge_item_code,charge_item_no,"
									+ "charge_item_spec,amount,units,backbill_rule,create_date,update_date) values('" + price.getClinicClass()
									+ "','" + price.getClinicCode() + "','" + price.getPriceClass() + "','" 
									+ price.getPriceCode() + "','','"
									+ price.getPriceSpec() + "','" + price.getAmount() + "','" + price.getPriceUnits() + "','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"')";
							tjtmpconnect.createStatement().execute(sql);
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
						}					
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
					ex.printStackTrace();
				}finally {
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
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} 
	}
	
	
}
