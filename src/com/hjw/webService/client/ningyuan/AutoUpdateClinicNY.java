package com.hjw.webService.client.ningyuan;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.webService.client.xintong.bean.ITEM_INFO;
import com.hjw.webService.client.xintong.bean.RESPONSE;
import com.hjw.webService.client.xintong.bean.resHisClinic;
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;
import com.hjw.wst.DTO.ChargingSummarySingleDTO;
import com.hjw.wst.DTO.HisClinicItemDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.CommService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.hjw.wst.service.examInfoService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class AutoUpdateClinicNY {

	private static JdbcQueryManager jqm;

	private static WebserviceConfigurationService webserviceConfigurationService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jqm = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}

	public ResultHisBody getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "宁远县 自动同步his数据开始");
		ResultHisBody rb = new ResultHisBody();

		try {
			resHisClinic resHisClinic = new resHisClinic();

			TranLogTxt.liswriteEror_to_txt(logname, "==url====:" + url);

			ArrayList<HisClinicItemDTO> ClinicList = AutoHisClinic(logname);
			TranLogTxt.liswriteEror_to_txt(logname, "同步his诊疗价表数据===start===");
			insert_his_data(logname, ClinicList);
			// 更新his诊疗表
			TranLogTxt.liswriteEror_to_txt(logname, "同步his诊疗价表数据===end===");

			TranLogTxt.liswriteEror_to_txt(logname, "同步体检收费项目价表数据===start===");
			boolean fly = updateHIsPriceSynchro(logname);

			TranLogTxt.liswriteEror_to_txt(logname, "同步体检收费项目价表数据===end===," + fly + "");
			if (fly == true) {
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("收费价表同步成功!!");
			} else {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("收费价表同步失败!!");
			}

		} catch (Throwable e) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("HIS诊疗同步失败!");
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}

	// 同步数据 插入体检诊疗表
	public void insert_his_data(String logname, ArrayList<HisClinicItemDTO> clinicList) {

		Connection connect = null;
		try {
			connect = jqm.getConnection();

			String del_clinic_sql = "delete his_clinic_item";
			connect.createStatement().executeUpdate(del_clinic_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目数据列表成功!" + del_clinic_sql);

			String del_clinic_price_sql = "delete his_clinic_item_v_price_list";
			connect.createStatement().executeUpdate(del_clinic_price_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目和价表关系数据列表成功!" + del_clinic_price_sql);

			String del_price_sql = "delete his_price_list";
			connect.createStatement().executeUpdate(del_price_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧价表数据列表成功!" + del_price_sql);
			
			int errorNum = 0;
			int clinicNum = 0;
			for (HisClinicItemDTO Info : clinicList) {
				try {
					connect = jqm.getConnection();
					if (!"".equals(Info.getItem_code())) {
						String select_clinic_sql = "select * from his_clinic_item where item_code = '" + Info.getItem_code()
								+ "'";
						TranLogTxt.liswriteEror_to_txt(logname, "select_clinic_sql:" + select_clinic_sql);
						ResultSet rs = connect.createStatement().executeQuery(select_clinic_sql);
						if (!rs.next()) {
							/*String sql = "insert into his_clinic_item (item_code,item_name,price,expand1) " + "values ('"
									+ Info.getItem_code() + "','" + Info.getItem_name() + "','" + Info.getPrice() + "','"
									+ Info.getUnits() + "')";*/
							
							
							String insert_clinic_sql = "insert into his_clinic_item([item_class],[item_code],[item_name],[input_code]"
									+ ",[expand1],[expand2],[expand3],[item_status] ,[create_date],[update_date],[price],[is_active]) "
									+ " values ('1','"+Info.getItem_code()+"','"+Info.getItem_name()+"',''"
									+ ",'','1','"+Info.getUnits()+"','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"','"+Info.getPrice()+"','Y')";
							TranLogTxt.liswriteEror_to_txt(logname, "res:" + "==" + insert_clinic_sql + "\r\n");
							connect.createStatement().executeUpdate(insert_clinic_sql);
							TranLogTxt.liswriteEror_to_txt(logname, "insert_clinic_sql:" + insert_clinic_sql);

							clinicNum++;
						}

					}

				} catch (Exception e) {
					errorNum++;
					TranLogTxt.liswriteEror_to_txt(logname, "错误:" + com.hjw.interfaces.util.StringUtil.formatException(e));
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
			TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item表" + clinicNum + "条，问题数据" + errorNum + "条");

			
			
			
		} catch (SQLException e) {
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" + com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}

		
	}

	// 更新体检收费项目表 价格
	public boolean updateHIsPriceSynchro(String logname) {
		String sql = "";
		ResultSet charging_item_rs = null;
		ResultSet clinic_item_rs = null;

		Connection connection = null;
		boolean fal = false;
		try {
			connection = this.jqm.getConnection();
			sql = " SELECT  c.id,c.his_num,c.item_class,c.amount,c.hiscodeClass FROM   charging_item c  where  isActive='Y' and  his_num <> '' ";
			TranLogTxt.liswriteEror_to_txt(logname, "查询收费项目sql: " + sql);
			// 查询所有 有效的 his_num不为空的 收费项目
			charging_item_rs = connection.createStatement().executeQuery(sql);
			while (charging_item_rs.next()) {

				sql = "	SELECT price FROM his_clinic_item where	item_code = '" + charging_item_rs.getString("his_num")+ "' ";
						

				TranLogTxt.liswriteEror_to_txt(logname, "查询诊疗表sql: " + sql);

				// 查询所有 item_code==his_num 不为空的 诊疗项目
				clinic_item_rs = connection.createStatement().executeQuery(sql);

				while (clinic_item_rs.next()) {// his_num对应诊疗项目
					double amount = clinic_item_rs.getDouble("price");
					TranLogTxt.liswriteEror_to_txt(logname, "诊疗项目价格: " + amount);
					TranLogTxt.liswriteEror_to_txt(logname, "收费项目价格: " + charging_item_rs.getDouble("amount"));
					TranLogTxt.liswriteEror_to_txt(logname,
							"收费项目价格是否和诊疗价格相等: " + (amount != charging_item_rs.getDouble("amount")) + "");
					if (amount != charging_item_rs.getDouble("amount")) {
						sql = " update charging_item set  amount = " + amount + "  where  id = '"
								+ charging_item_rs.getLong("id") + "'";

						TranLogTxt.liswriteEror_to_txt(logname, "按照诊疗价格更新收费项目sql: " + sql);
						connection.createStatement().executeUpdate(sql);

						String setchargingitem = ("update set_charging_item set amount=" + amount
								+ "*itemnum,discount=10,item_amount=" + amount + "  where charging_item_id='"
								+ charging_item_rs.getLong("id") + "' ");
						TranLogTxt.liswriteEror_to_txt(logname, "res: :更改套餐与收费项目关系表的价格： " + setchargingitem);
						connection.createStatement().executeUpdate(setchargingitem);

						String setsql = "select exam_set_id from set_charging_item where charging_item_id='"
								+ charging_item_rs.getLong("id") + "'";
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + setsql);
						ResultSet rss = connection.createStatement().executeQuery(setsql);
						if (rss.next()) {
							String upsql = "update exam_set set set_discount=10,"
									+ "price=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
									+ rss.getString("exam_set_id")
									+ "'),set_amount=(select SUM(a.amount) from set_charging_item  a "
									+ "where a.exam_set_id='" + rss.getString("exam_set_id") + "') " + "where id='"
									+ rss.getString("exam_set_id") + "'";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
							connection.createStatement().execute(upsql);
						}
						rss.close();

						fal = true;

					}
				
				}

			}
			
			charging_item_rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			fal = false;
		} finally {
			try {

				if (clinic_item_rs != null) {
					clinic_item_rs.close();
				}
				if (charging_item_rs != null) {
					charging_item_rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				fal = false;
			}
		}
		return fal;
	}

	private ArrayList<HisClinicItemDTO> AutoHisClinic(String logname) {

		WebserviceConfigurationDTO wcf = new WebserviceConfigurationDTO();
		wcf = this.webserviceConfigurationService.getWebServiceConfig("HIS_DATA_APPLICATION");
		Connection conn = null;
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
		ResultSet result = null;//

		String url = wcf.getConfig_url();// oracle数据库url
		String[] split = wcf.getConfig_value().split(",");
		String user = split[0];// 获取用户名
		String password = split[1];// 获取密码

		ArrayList<HisClinicItemDTO> list = new ArrayList<HisClinicItemDTO>();

		try {
			conn = OracleDatabaseSource.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			
			//3.获得操作数据库声明
				
			
			Statement statement = conn.createStatement();
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + "开始查询his价表");
			ResultSet rs = statement.executeQuery("select * from tj_user");
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + "开始查询his价表-------------");
			while (rs.next()) {
				HisClinicItemDTO Clinic = new HisClinicItemDTO();
				Clinic.setItem_code(rs.getString(1));
				Clinic.setItem_name(rs.getString(2));
				//BigDecimal bigDecimal = rs.getBigDecimal(3);  //双精度类型
				//Clinic.setPrice(bigDecimal.doubleValue());	//转换double
				Clinic.setPrice(rs.getDouble(3));
				Clinic.setUnits(rs.getString(4));

				list.add(Clinic);

				TranLogTxt.liswriteEror_to_txt(logname, "res: :获取his关联码： " + rs.getString(1)+"res:获取his价格"+ rs.getBigDecimal(3)+"res:获取his价格"+rs.getString(3)+"res:获取his价格"+rs.getLong(3)+"res:获取his价格"+rs.getDouble(3));
			}
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + "结束查询his价表");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 执行存储过程
		return list;

	}
}
