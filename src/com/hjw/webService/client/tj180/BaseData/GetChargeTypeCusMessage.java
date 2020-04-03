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
import com.hjw.webService.client.tj180.Bean.ChargeCusItemInfo;
import com.hjw.webService.client.tj180.Bean.ChargeTypeItemInfo;
import com.hjw.webService.client.tj180.Bean.ResChargeCusBean;
import com.hjw.webService.client.tj180.Bean.ResChargeTypeBean;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 中金 上传 体检报告信息
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class GetChargeTypeCusMessage {
	private String msgname = "getChargeType";
	private String charset = "utf-8";
	private static JdbcQueryManager jdbcQueryManager;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public GetChargeTypeCusMessage() {

	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getChargeCus(String url, String charset, String msgname) {
		this.msgname = msgname;
		this.charset = charset;
		ResultHeader res = new ResultHeader();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpPost = new HttpGet(url);
			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
			HttpResponse response = httpClient.execute(httpPost);
			
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					String result = "";
					//result="{\"chargeTypeInfo\"=[{\"identityName\"=\"地方\", \"chargeTypeName\"=\"农村居民\"}, {\"identityName\"=\"武警人员\", \"chargeTypeName\"=\"武警人员\"}, {\"identityName\"=\"普通军属\", \"chargeTypeName\"=\"农村居民\"}, {\"identityName\"=\"义务兵\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"士官\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"战士\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"外宾\", \"chargeTypeName\"=\"自费\"}, {\"identityName\"=\"地方\", \"chargeTypeName\"=\"自费\"}, {\"identityName\"=\"军以上干部\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"师职干部\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"团以下干部\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"免减费家属\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"城镇职工\", \"chargeTypeName\"=\"职工医保\"}, {\"identityName\"=\"城镇居民\", \"chargeTypeName\"=\"居民医保\"}, {\"identityName\"=\"农民\", \"chargeTypeName\"=\"新农合\"}, {\"identityName\"=\"地方\", \"chargeTypeName\"=\"体检\"}, {\"identityName\"=\"公务员\", \"chargeTypeName\"=\"职工医保\"}, {\"identityName\"=\"团职干部\", \"chargeTypeName\"=\"军队医改\"}, {\"identityName\"=\"普通军属\", \"chargeTypeName\"=\"自费\"}, {\"identityName\"=\"普通军属\", \"chargeTypeName\"=\"新农合\"}, {\"identityName\"=\"普通军属\",\"chargeTypeName\"=\"职工医保\"}, {\"identityName\"=\"普通军属\", \"chargeTypeName\"=\"眼科农保\"}, {\"identityName\"=\"地方\",\"chargeTypeName\"=\"眼科农保\"}, {\"identityName\"=\"农民\",\"chargeTypeName\"=\"眼科农保\"}, {\"identityName\"=\"城乡居民\",\"chargeTypeName\"=\"居民医保\"}, {\"identityName\"=\"普通军属\",\"chargeTypeName\"=\"居民医保\"}, {\"identityName\"=\"义务兵\",\"chargeTypeName\"=\"体检\"}], \"errorinfo\"=\"\", \"status\"=\"200\"}";
					result = EntityUtils.toString(resEntity, charset);
					TranLogTxt.liswriteEror_to_txt(msgname, "res:" + result);
					result = result.trim();
					JSONObject jsonobject = JSONObject.fromObject(result);

					Map classMap = new HashMap();
					classMap.put("chargeTypeInfo", ChargeCusItemInfo.class);
					ResChargeCusBean rdb = new ResChargeCusBean();
					rdb = (ResChargeCusBean) JSONObject.toBean(jsonobject, ResChargeCusBean.class, classMap);
					if (rdb == null) {
						res.setTypeCode("AE");
						res.setText(url + " 返回无返回");
					} else if (!"200".equals(rdb.getStatus())) {
						res.setTypeCode("AE");
						res.setText(rdb.getErrorinfo());
					} else {
						insertChargeCusItem(rdb.getChargeTypeInfo(), msgname);
						res.setTypeCode("AA");
						res.setText("");
					}
				} else {
					res.setTypeCode("AE");
					res.setText(url + " 返回无返回");
				}
			} else {
				res.setTypeCode("AE");
				res.setText(url + " 返回无返回");
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
	private void insertChargeCusItem(List<ChargeCusItemInfo> deptInfo, String logname) throws Exception {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String delstr="delete from charging_customer";
			tjtmpconnect.createStatement().execute(delstr);
			for (ChargeCusItemInfo c : deptInfo) {
				if ((c.getChargeTypeName() != null) && (c.getChargeTypeName().trim().length() > 0)) {
                    boolean chargflag=false;
                    long chargid=0;                    
                    
					String sb1 = "SELECT id FROM data_dictionary where data_code='SFTYPE' and isActive='Y'  and data_name='"
							+ c.getChargeTypeName().trim() + "'";
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb1);
					ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
					if (rs1.next()) {
						chargflag=true;
						chargid=rs1.getLong("id");
					}
					rs1.close();
					
					boolean custflag=false;
                    long custid=0;
					sb1 = "SELECT id FROM customer_type where type_name='"
							+ c.getIdentityName().trim() + "'";
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb1);
					rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
					if (rs1.next()) {
						custflag=true;
						custid=rs1.getLong("id");
					}
					rs1.close();
					if(chargflag&&custflag){
						sb1 = "insert into charging_customer (chargingType_id,chargingType_name,customerType_id,"
								+ "customerType_name,is_action) values('"+chargid+"'"
								+ ",'"+c.getChargeTypeName()+"','"+custid+"','"+c.getIdentityName()+"','Y') ";
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb1);
						tjtmpconnect.createStatement().execute(sb1);
					}
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
