package com.hjw.webService.client.tj180.BaseData;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.LisGetReqBean;
import com.hjw.webService.client.tj180.Bean.LisGetResStatusBean;
import com.hjw.webService.client.tj180.Bean.PriceItemInfo;
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
public class GetLisStatusMessage {
    private String msgname="getlissattus";
    private String charset="utf-8";
    private static JdbcQueryManager jdbcQueryManager;
	
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();		
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public GetLisStatusMessage(){
		
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getPriceItem(String url,String charset,String logname) {
		ResultHeader rb = new ResultHeader();
		Connection tjtmpconnect = null;
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String sb1 = "  select a.exam_info_id,a.req_id,a.lis_item_id,b.id as sampleid from zl_req_item a,"
						+ "sample_exam_detail b,charging_item c where a.lis_req_code=b.sample_barcode "
						+ "and b.status='W' "
						+ "and a.exam_info_id=b.exam_info_id "
						+ "and c.id=a.charging_item_id "
						+ "and c.his_num=a.lis_item_id "
						+ "and CONVERT(varchar(50),b.create_time,23)='"+DateTimeUtil.getDate2()+"' ";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
				while (rs1.next()) {
					String req_id =rs1.getString("req_id");
					long sampleid= rs1.getLong("sampleid");
					try {
						LisGetReqBean p = new LisGetReqBean();
						p.setTestNo(req_id);
						JSONObject json = JSONObject.fromObject(p);// 将java对象转换为json对象
						String str = json.toString();// 将json对象转换为字符串
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
						String result = HttpUtil.doPost(url,p,"utf-8");						
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
			            if((result!=null)&&(result.trim().length()>0)){	   
								result = result.trim();
								JSONObject jsonobject = JSONObject.fromObject(result);
								LisGetResStatusBean resdah = new LisGetResStatusBean();
								resdah = (LisGetResStatusBean) JSONObject.toBean(jsonobject, LisGetResStatusBean.class);
								if ("200".equals(resdah.getStatus())) {
									if("1".equals(resdah.getLisState())){
									String updatesql="update sample_exam_detail set b.status='W' where id='"+sampleid+"'";
									TranLogTxt.liswriteEror_to_txt(logname, "res:" + updatesql+"\r\n");
									tjtmpconnect.createStatement().execute(updatesql);	
									}
								}
							}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} 
				rs1.close();
			} catch (SQLException ex) {
				TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
			} finally {
				try {
					if (tjtmpconnect != null) {
						tjtmpconnect.close();
					}
				} catch (SQLException sqle4) {
					sqle4.printStackTrace();
				}
			}			
		rb.setTypeCode("AA");
		return rb;
	}
}
