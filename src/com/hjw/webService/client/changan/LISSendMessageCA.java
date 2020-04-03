package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.UserDTO;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class LISSendMessageCA {
	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static ConfigService configService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public LISSendMessageCA(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "1-----------------开始lis请求--------------------");
		ResultLisBody rb = new ResultLisBody();
		Connection his_connect = null;
		try {
			String jsonString = JSONSerializer.toJSON(lismessage).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
			ExamInfoUserDTO eu= configService.getExamInfoForNum(this.lismessage.getCustom().getExam_num());
			
			if("T".equals(eu.getExam_type())) {
//				团检者也要触发写入5个表：
//				3.10就诊记录  CLINIC_MASTER
//				3.11门诊病历记录OUTP_MR
//				3.12门诊医嘱主记录OUTP_ORDERS
//				3.13检查治疗医嘱明细记录OUTP_TREAT_REC
//				3.14门诊医生收费明细OUTP_ORDERS_COSTS
				TranLogTxt.liswriteEror_to_txt(logname, "团检调用收费申请接口开始");
				UserDTO user = new UserDTO();
				String message = configService.paymentApplicationT(this.lismessage.getCustom().getExam_num(), user);
				TranLogTxt.liswriteEror_to_txt(logname, "团检插入5个表，返回信息："+message);
			}

			TranLogTxt.liswriteEror_to_txt(logname, "2-----------------lis数据库url--------------------" + url);
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			his_connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "3-----------------连接成功--------------------");
			
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			for (LisComponents lcs : this.lismessage.getComponents()) {
				try {
					double needPay = 0.0;
					double realPay = 0.0;
					for (LisComponent lc : lcs.getItemList()) {
						needPay += lc.getItemprice();
						realPay += lc.getItemamount();
					}
					
					String curDate = "";
					String test_no = "";
					String sql = "select to_char(sysdate, 'yymmdd') as curDate, test_no.nextval as test_no from dual";
					Statement statement = his_connect.createStatement();
					ResultSet rs0 = statement.executeQuery(sql);
					if(rs0.next()) {
						curDate = rs0.getString("curDate");
						test_no = rs0.getString("test_no");
					}
					rs0.close();
					statement.close();
					String curTest_No = curDate + "00" + test_no;
					TranLogTxt.liswriteEror_to_txt(logname, "获取 curTest_No："+sql);
					TranLogTxt.liswriteEror_to_txt(logname, "从数据库获取到：curTest_No="+curTest_No);
					
					//1,写入：3.15检验主记录  LAB_TEST_MASTER
					String insert_LAB_TEST_MASTER_sql = "insert into LAB_TEST_MASTER (TEST_NO, PRIORITY_INDICATOR, PATIENT_ID, NAME, CHARGE_TYPE,"
							+ " SEX, AGE, SUBJECT, SPECIMEN, REQUESTED_DATE_TIME,"
							+ " ORDERING_DEPT, ORDERING_PROVIDER, PERFORMED_BY, RESULT_STATUS, COSTS,"
							+ " CHARGES, BILLING_INDICATOR,VISIT_ID,test_cause) values "
							+ " ('"+curTest_No+"', '0', '"+this.lismessage.getCustom().getExam_num()+"', '"+eu.getUser_name()+"', '自费',"
							+ " '"+eu.getSex()+"','"+eu.getAge()+"','体检','"+lcs.getCsampleName()+"',sysdate, "
					        + " '"+this.lismessage.getDoctor().getDept_code()+"','"+this.lismessage.getDoctor().getDoctorName()+"',null,null,'"+realPay+"',"
					        + " '"+needPay+"',1,0,'体检')";
					TranLogTxt.liswriteEror_to_txt(logname, "1,写入：3.15检验主记录  LAB_TEST_MASTER: " +insert_LAB_TEST_MASTER_sql);
					statement = his_connect.createStatement();
					statement.executeUpdate(insert_LAB_TEST_MASTER_sql);
					statement.close();
					
					for (int i=0; i<lcs.getItemList().size(); i++) {
						//2,写入：3.16检验项目 LAB_TEST_ITEMS
						LisComponent lc = lcs.getItemList().get(i);
						String insert_LAB_TEST_ITEMS_sql = "insert into LAB_TEST_ITEMS (TEST_NO, ITEM_NO, ITEM_NAME, ITEM_CODE) values "
								+ " ('"+curTest_No+"',"+(i+1)+",'"+lc.getItemName()+"','"+lc.getHis_num()+"')";
						TranLogTxt.liswriteEror_to_txt(logname, "2,写入：3.16检验项目 LAB_TEST_ITEMS: " +insert_LAB_TEST_ITEMS_sql);
						statement = his_connect.createStatement();
						statement.executeUpdate(insert_LAB_TEST_ITEMS_sql);
						statement.close();
					}
					
					ZlReqItemDTO zri = new ZlReqItemDTO();
					zri.setExam_info_id(eu.getId());
					zri.setLis_req_code(lcs.getReq_no());
					zri.setReq_id(curTest_No);
					configService.insert_zl_req_item(zri, logname);
					
					ApplyNOBean ap = new ApplyNOBean();
					ap.setApplyNO(lcs.getReq_no());
					ap.setLis_id(lcs.getLis_id());
					ap.setBarcode(lcs.getReq_no());
					list.add(ap);
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(list);
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("连接数据库失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		TranLogTxt.liswriteEror_to_txt(logname, "6-----------------结束lis请求--------------------");
		return rb;
	}
}
