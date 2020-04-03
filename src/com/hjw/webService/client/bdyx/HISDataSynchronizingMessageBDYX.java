package com.hjw.webService.client.bdyx;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bdyx.bean.DataBDYX;
import com.hjw.webService.client.bdyx.bean.ResponseData;
import com.hjw.webService.client.bdyx.gencode2.IBdQryWebService;
import com.hjw.webService.client.bdyx.gencode2.IBdQryWebServiceLocator;
import com.hjw.webService.client.bdyx.gencode2.IBdQryWebServicePortType;
import com.hjw.webService.client.body.ResultHisBody;
import com.synjones.framework.persistence.JdbcQueryManager;

public class HISDataSynchronizingMessageBDYX {

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
			TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
			
			Connection connect = null;
			try {
				connect = jqm.getConnection();
				String del_clinic_sql = "delete his_clinic_item";
				connect.createStatement().executeUpdate(del_clinic_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目数据列表成功!");
			} catch (SQLException e) {
				e.printStackTrace();
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
			
			for(int index=0; index<10; index++) {
				String req = ""
						+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						+ "<request>"
						+ "<itfcode>016</itfcode>"
						+ "<pageinfo>"
						+ "<pagesize>5000</pagesize>"
						+ "<pageindex>"+index+"</pageindex>"
						+ "</pageinfo>"
						+ "</request>";//016	诊疗项目 	V_BD_TREAT
				
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + req);
				IBdQryWebService iBdQryWebService = new IBdQryWebServiceLocator(url);
				IBdQryWebServicePortType iBdQryWebServicePort = iBdQryWebService.getIBdQryWebServicePort();
				String responseStr = iBdQryWebServicePort.findData(req);
				TranLogTxt.liswriteEror_to_txt(logname, "responseStr:" + responseStr);
				
				ResponseData responseBDYX = JaxbUtil.converyToJavaBean(responseStr, ResponseData.class);
				if(0 == responseBDYX.getCode()) {
					int errorNum = 0;
					int clinicNum = 0;
					if(responseBDYX.getDatalist().getData() != null) {
						for(DataBDYX data : responseBDYX.getDatalist().getData()) {
							String insert_clinic_sql = "";
							try {
								if("Y".equals(data.getFg_active())) {
									connect = jqm.getConnection();
									insert_clinic_sql = "insert into his_clinic_item([item_class],[item_code],[item_name],[input_code]"
											+ ",[expand1],[expand2],[expand3],[item_status] ,[create_date],[update_date],price,is_active) "
											+ " values ('','"+data.getCode()+"','"+data.getName().replaceAll("'", "''")+"','"+data.getPycode().replaceAll("'", "''")+"'"
											+ ",'','','','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"',"+data.getPrice_std()+ ",'Y')";
									connect.createStatement().executeUpdate(insert_clinic_sql);
									clinicNum++;
								}
							} catch (Exception e) {
								TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目sql--"+insert_clinic_sql);
								errorNum++;
								e.printStackTrace();
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
						TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item表"+clinicNum+"条，问题数据"+errorNum+"条");
					} else {
						TranLogTxt.liswriteEror_to_txt(logname, "=======数据同步结束======");
					}
				} else {
					TranLogTxt.liswriteEror_to_txt(logname, "对方返回："+responseBDYX.getMsg());
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("数据同步成功!");
		} catch (Throwable e) {
			e.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		return rb;
	}
}