package com.hjw.webService.client.nanhua;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.webService.client.nanhua.bean.Info;
import com.hjw.webService.client.nanhua.bean.NHRequest;
import com.hjw.webService.client.nanhua.bean.NHResponse;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServices;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesLocator;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType;
import com.synjones.framework.persistence.JdbcQueryManager;

public class HISDataSynchronizingMessageNH {

	private static JdbcQueryManager jqm;
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jqm = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public ResultHisBody getMessage(String url, String logname) {
		ResultHisBody rb = new ResultHisBody();
		try {
			NHRequest request = new NHRequest();
			request.getData().setCxrq(DateTimeUtil.getDateTime());
			request.getData().setQshm("1");
			request.getData().setJshm("20000");//组合项和细项一共大概12000条，给个20000应该够用
			String strRequest = JaxbUtil.convertToXml(request, true);
			TranLogTxt.liswriteEror_to_txt(logname, "传入参数:" + strRequest);
			
			TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
			GWI_TJJKServices gwiServices = new GWI_TJJKServicesLocator(url);
			GWI_TJJKServicesSoap_PortType gwi = gwiServices.getGWI_TJJKServicesSoap();
			String messages = gwi.queryItemInfo(strRequest);
			TranLogTxt.liswriteEror_to_txt(logname,"res:"+messages);
			NHResponse response = JaxbUtil.converyToJavaBean(messages, NHResponse.class);
			if("0".equals(response.getHead().getResultCode())) {
/////////////////// copy to AutoGetHisDataNH.java_insert_his_data() start //////////////////////////////////////////////////////
				Connection connect = null;
				try {
					connect = jqm.getConnection();
					
					String del_clinic_sql = "delete his_clinic_item";
					connect.createStatement().executeUpdate(del_clinic_sql);
					TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目数据列表成功!");
					
					String del_clinic_price_sql = "delete his_clinic_item_v_price_list";
					connect.createStatement().executeUpdate(del_clinic_price_sql);
					TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目和价表关系数据列表成功!");
					
					String del_price_sql = "delete his_price_list";
					connect.createStatement().executeUpdate(del_price_sql);
					TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧价表数据列表成功!");
				} catch (SQLException e) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
					TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				} finally {
					try {
						if (connect != null){
							connect.close();
						}
					} catch (Exception sqle4) {
						sqle4.printStackTrace();
					}
				}
				
				int errorNum = 0;
				int clinicNum = 0;
				int clinic_price_listNum = 0;
				int priceNum = 0;
				for(Info Info : response.getInfo()) {
					try {
						connect = jqm.getConnection();
						if(!"".equals(Info.getZhItemId())) {
							String select_clinic_sql = "select * from his_clinic_item where item_code = '"+Info.getZhItemId()+"'";
							ResultSet rs = connect.createStatement().executeQuery(select_clinic_sql);
							if(!rs.next()) {
								String insert_clinic_sql = "insert into his_clinic_item([item_class],[item_code],[item_name],[input_code]"
										+ ",[expand1],[expand2],[expand3],[item_status] ,[create_date],[update_date],[price],[is_active]) "
										+ " values ('D','"+Info.getZhItemId()+"','"+Info.getZhItemName()+"',''"
										+ ",'"+Info.getNum()+"','"+Info.getZhItemdw()+"','','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"','"+Info.getZhItemdj()+"','Y')";
//								TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目sql--"+insert_clinic_sql);
								connect.createStatement().executeUpdate(insert_clinic_sql);
								clinicNum++;
							}
							
							String insert_clinic_price_sql = "insert into his_clinic_item_v_price_list([clinic_item_class],[clinic_item_code],[charge_item_no]"
									+ ",[charge_item_class],[charge_item_code],[charge_item_spec],[amount],[units],[backbill_rule],[create_date],[update_date])"
									+ " values('D','"+Info.getZhItemId()+"',''"
									+ ",'D','"+Info.getMxItemId()+"','','"+Info.getNum()+"','"+Info.getMxItemdw()+"','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"')";
//							TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表和诊疗项目关系sql--"+insert_clinic_price_sql);
							connect.createStatement().executeUpdate(insert_clinic_price_sql);
							clinic_price_listNum++;
						}
						
						String select_clinic_price_sql = "select * from his_price_list where item_code = '"+Info.getMxItemId()+"'";
						ResultSet rs = connect.createStatement().executeQuery(select_clinic_price_sql);
						if(!rs.next()) {
							String insert_price_sql = "insert into his_price_list([item_class],[item_code],[item_name],[item_spec],[units],[price]"
									+ ",[prefer_price],[performed_by],[input_code],[class_on_inp_rcpt],[class_on_outp_rcpt],[class_on_reckoning]"
									+ ",[subj_code],[memo],[start_date],[stop_date],[create_date],[update_date],[is_active],[expand1]) "
									+ "values('D','"+Info.getMxItemId()+"','"+Info.getMxItemName()+"','','"+Info.getMxItemdw()+"','"+Info.getMxItemdj()+"'"
									+ ",'"+Info.getMxItemdj()+"','','','','',''"
									+ ",'','','"+DateTimeUtil.getDateTime()+"','9999-12-31 23:59:59.000','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"','Y','"+Info.getMxItemId()+"')";
//							TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表sql--"+insert_price_sql);
							connect.createStatement().executeUpdate(insert_price_sql);
							priceNum++;
						}
					} catch (Exception e) {
						errorNum++;
						TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
					} finally {
						try {
							if (connect != null) {
								connect.close();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item表"+clinicNum+"条，"
						+ "his_clinic_item_v_price_list表"+ clinic_price_listNum+ "条，his_price_list表"+priceNum+"条，问题数据"+errorNum+"条");
				
/////////////////// copy to AutoGetHisDataNH.java_insert_his_data() end //////////////////////////////////////////////////////
			} else {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(response.getHead().getErrorMsg());
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("数据同步成功!");
		} catch (Throwable e) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		return rb;
	}
}
