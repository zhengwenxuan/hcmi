package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ArmyPatInfoBody;
import com.hjw.webService.client.Bean.ArmyPatInfoReq;
import com.hjw.webService.client.Bean.ArmyReserveBean;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class ArmyGetMessageTj180{
	
	private static JdbcQueryManager jdbcQueryManager;
	
	 static{
		   	init();
		   	}
			public static void init(){
				WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
				jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
			}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ArmyPatInfoBody getMessage(String url,long batchid,String logname) {		
		ArmyPatInfoBody resdah=new ArmyPatInfoBody();		
		try {			
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + batchid);
			ArmyPatInfoReq ap=this.getArmyPatInfoReq(batchid, logname);
			if(ap==null||ap.getOrganizationOutpId()==null||ap.getOrganizationOutpId().trim().length()<=0){
				resdah.setStatus("AE");
				resdah.setErrorInfo("无效批次或者无效合同单位代码");
			}else{
				JSONObject json = JSONObject.fromObject(ap);// 将java对象转换为json对象
				String str = json.toString();// 将json对象转换为字符串
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
				String result = HttpUtil.doPost(url,ap,"utf-8");
				//String result="{\"status\":\"200\",\"patNum\": \"1\",\"customerInfo\":[{\"customerPatientId\": \"1234567\" , \"customerName\": \"吴某某\",\"phone\":\"12345678901\", \"customerSSid\": \"\",\"customerIdentityNo\":\"350500000\",\"customerSex\":\"女\",\"customerBirthday\": \"1980-01-01\", \"address\":\"XXX省XXX市\",\"customerBirthPlace\":\"350500\", \"customerNation\": \"汉族\", \"customerIdentity\":\"地方\",\"customerChargeType\":\"自费\", \"unitInContract\": \"43\",\"customerWeb\":\"已婚\",\"customerCardNo\":\"T123456\",\"customerOrganization\":\"XX部队\"}],\"errorInfo\": \"\"}";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
	            if((result!=null)&&(result.trim().length()>0)){	                    
		                    result = result.trim();
		    				JSONObject jsonobject = JSONObject.fromObject(result);
		    				Map classMap = new HashMap();
							classMap.put("customerInfo", ArmyReserveBean.class);	 
							resdah = (ArmyPatInfoBody)JSONObject.toBean(jsonobject,ArmyPatInfoBody.class,classMap);
							if("200".equals(resdah.getStatus())){
								if(resdah.getCustomerInfo()==null || resdah.getCustomerInfo().size()<=0){
									resdah.setStatus("AE");
									resdah.setErrorInfo("接口返回数据为空");
								}else{
									resdah.setStatus("AA");
								}
							}else{
								resdah.setStatus("AE");
							}
		                }else{
		                	resdah.setStatus("AE");
		                	resdah.setErrorInfo(url  +" 返回无返回");
		                }
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
			resdah.setStatus("AE");
        	resdah.setErrorInfo(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return resdah;
	}
	
	/**
	 * 
	 * @param barchid
	 * @param logname
	 * @return
	 */
	private ArmyPatInfoReq getArmyPatInfoReq(long barchid,String logname){
		Connection tjtmpconnect = null;
		ArmyPatInfoReq ap = new ArmyPatInfoReq();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select ci.assigned_unit_code,ci.com_num,ci.com_name,b.batch_num,b.batch_name "
					+ "from company_info ci,batch b where b.company_id=ci.id and b.id='"+barchid+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				ap.setOrganizationId(rs1.getString("com_num"));
				ap.setOrganizationName(rs1.getString("com_name"));
				ap.setOrganizationOutpId(rs1.getString("assigned_unit_code"));
			}
			rs1.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return ap;
	}
}
