package com.hjw.webService.client.zhonglian.BaseData;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ClinicItemInfo;
import com.hjw.webService.client.Bean.HisSynLogBean;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.service.Databean.DiagnosisItem;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 中金 上传 体检报告信息
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class GetClinicItemMessageZl{
    private String msgname="getdept";
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
	
	public GetClinicItemMessageZl(){
		
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getClinicItem(String url,String charset,String logname) {
		this.charset=charset;
		ResultHeader res=new ResultHeader();
		Connection connect = null;
		List<ClinicItemInfo> rcib=new ArrayList<ClinicItemInfo>();
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
			
            String str="select item_class,item_code,item_name,price,is_active from his_clinic_item where is_active='Y' ";
            ResultSet rs1 = connect.createStatement().executeQuery(str);
			if (rs1.next()) {
				ClinicItemInfo ci = new ClinicItemInfo();
				ci.setClinicClass(rs1.getString("item_class"));
				ci.setClinicCode(rs1.getString("item_code"));
				ci.setClinicName(rs1.getString("item_name"));
				ci.setPrice(rs1.getDouble("price"));
				rcib.add(ci);
			}
           	rs1.close();
		} catch (Exception ex) {
			res.setTypeCode("AE");
			res.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		try{
		insertClinicItem(rcib,logname);
		}catch(Exception ex){
			ex.printStackTrace();
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
	private void insertClinicItem(List<ClinicItemInfo> deptInfo,String logname) throws Exception {
		List<DiagnosisItem> depList=new ArrayList<DiagnosisItem>();
		for(ClinicItemInfo c:deptInfo){
			DiagnosisItem dt= new DiagnosisItem();
			dt.setAction("1");
			dt.setInput_code(c.getInputCode());
			dt.setItem_class(c.getClinicClass());
			dt.setItem_code(c.getClinicCode());
			dt.setItem_name(c.getClinicName());
			dt.setPrice(c.getPrice());
			depList.add(dt);			
		}
		saveDiagnosisItem_comm(depList,logname);		
	}	
	
	public void saveDiagnosisItem_comm(List<DiagnosisItem> diagnosisItemList, String logname)
			throws ServiceException {
		Connection tjtmpconnect = null;
		try {
			
			/*
			 * String delstr = "delete from his_clinic_item";
			 * TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + delstr);
			 * tjtmpconnect.createStatement().execute(delstr);
			 */
			if ((diagnosisItemList != null) && (diagnosisItemList.size() > 0)) {
				String sql = "";
				try {
					tjtmpconnect = this.jdbcQueryManager.getConnection();
				    sql = "update his_clinic_item set is_active='N',update_date='" + DateTimeUtil.getDateTime()+ "'";
				    tjtmpconnect.createStatement().execute(sql);
				    TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);		
				} catch (SQLException ex) {
					tjtmpconnect.rollback();
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}finally {
					try {
						if (tjtmpconnect != null) {
							tjtmpconnect.close();
						}
					} catch (SQLException sqle4) {
						sqle4.printStackTrace();
					}
				}
				for (DiagnosisItem diagnosisItem : diagnosisItemList) {
					try {
						tjtmpconnect = this.jdbcQueryManager.getConnection();
						tjtmpconnect.setAutoCommit(false);
						if (diagnosisItem.getItem_name().indexOf("'") >= 0) {
							diagnosisItem.setItem_name(diagnosisItem.getItem_name().replaceAll("'", "''"));
						}
						if (diagnosisItem.getExpand1().indexOf("'") >= 0) {
							diagnosisItem.setExpand1(diagnosisItem.getExpand1().replaceAll("'", "''"));
						}
						if (diagnosisItem.getExpand2().indexOf("'") >= 0) {
							diagnosisItem.setExpand2(diagnosisItem.getExpand2().replaceAll("'", "''"));
						}
						if (diagnosisItem.getExpand3().indexOf("'") >= 0) {
							diagnosisItem.setExpand3(diagnosisItem.getExpand3().replaceAll("'", "''"));
						}
						StringBuffer sb = new StringBuffer();
						sb.append("select id,item_class,price,item_name from his_clinic_item where item_class='"
								+ diagnosisItem.getItem_class() + "' and item_code='" + diagnosisItem.getItem_code()
								+ "' ");
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb.toString());
						ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
						if (rs1.next()) {
							long picid = rs1.getLong("id");
							
							BigDecimal bd = new BigDecimal(0);
							bd = new BigDecimal(rs1.getDouble("price"));
							double oldprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

							bd = new BigDecimal(diagnosisItem.getPrice());
							double newoldprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							if (oldprice != newoldprice) {
								String item_class = rs1.getString("item_class");
								sql = "update his_clinic_item set item_name='" + diagnosisItem.getItem_name()
										+ "',price=" + diagnosisItem.getPrice() + ",input_code='"
										+ diagnosisItem.getInput_code() + "',is_active='Y',update_date='"
										+ DateTimeUtil.getDateTime() + "' where id='" + picid + "'";
								tjtmpconnect.createStatement().execute(sql);
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);

								sb = new StringBuffer();
								sb.append("update charging_item set amount=" + diagnosisItem.getPrice()
										+ ",isActive='Y',item_class='" + item_class + "'  where his_num='" + picid
										+ "' and hiscodeClass='2'");
								tjtmpconnect.createStatement().execute(sb.toString());
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb.toString());

								String chargsql = "select id from charging_item where isActive='Y' and his_num='"
										+ picid + "' and hiscodeClass='2'";
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + chargsql);
								ResultSet rsc = tjtmpconnect.createStatement().executeQuery(chargsql);
								while (rsc.next()) {
									tjtmpconnect.createStatement()
											.execute("update set_charging_item set amount=" + diagnosisItem.getPrice()
													+ "*itemnum,discount=10,item_amount=" + diagnosisItem.getPrice()
													+ "  where charging_item_id='" + rsc.getInt("id") + "' ");
									String setsql = "select exam_set_id from set_charging_item where charging_item_id='"
											+ rsc.getInt("id") + "'";
									TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + setsql);
									ResultSet rss = tjtmpconnect.createStatement().executeQuery(setsql);
									if (rss.next()) {
										String upsql = "update exam_set set set_discount=10,"
												+ "price=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
												+ rss.getString("exam_set_id") + "'),set_amount=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
												+ rss.getString("exam_set_id") + "') " + "where id='" + rss.getString("exam_set_id") + "'";
										TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
										tjtmpconnect.createStatement().execute(upsql);
									}
									rss.close();
								}
								rsc.close();
							}else{
								sql = "update his_clinic_item set is_active='Y' where id='"
										+ picid + "'";
								tjtmpconnect.createStatement().execute(sql);
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
							}
						} else {
							sql = "insert into his_clinic_item (item_class,item_code,item_name,input_code,expand1,expand2,"
									+ "expand3,item_status,create_date,update_date,price,is_active) values('"
									+ diagnosisItem.getItem_class() + "','" + diagnosisItem.getItem_code() + "'" + ",'"
									+ diagnosisItem.getItem_name() + "','" + diagnosisItem.getInput_code() + "','"
									+ diagnosisItem.getExpand1() + "'" + ",'" + diagnosisItem.getExpand2() + "','"
									+ diagnosisItem.getExpand3() + "','" + diagnosisItem.getItem_status() + "','"
									+ DateTimeUtil.getDateTime() + "','" + DateTimeUtil.getDateTime() + "',"
									+ diagnosisItem.getPrice() + ",'Y')";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
							tjtmpconnect.createStatement().execute(sql);
						}
						rs1.close();
						tjtmpconnect.commit();
						
					} catch (SQLException ex) {
						tjtmpconnect.rollback();
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
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

		// 通过charging_item查询是否和诊疗项目一致
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String str = "select a.id,a.his_num,a.item_code,a.item_name,a.hiscodeClass from charging_item a where a.hiscodeClass='2' and isActive='Y' and a.his_num IS not NULL and LEN(a.his_num)>0";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + str);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(str);
			if (rs1.next()) {
				long chargid = rs1.getLong("id");
				String his_num = rs1.getString("his_num");
				String sql1 = "select id,item_code,item_name from his_clinic_item where ID='" + his_num + "'  and is_active='N' ";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql1);
				ResultSet rs2 = tjtmpconnect.createStatement().executeQuery(sql1);
				if (rs2.next()) {
					String sql2 = "update charging_item set his_num='',hiscodeClass='',his_flag=1 where id=" + chargid + "";
					tjtmpconnect.createStatement().execute(sql2);
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql2);
					HisSynLogBean hsb = new HisSynLogBean();
					hsb.setHiscodeClass("2");
					hsb.setItem_id(chargid);
					hsb.setItem_code(rs1.getString("item_code"));
					hsb.setItem_name(rs1.getString("item_name"));
					hsb.setOld_his_item_code(rs2.getString("item_code"));
					hsb.setOld_his_item_name(rs2.getString("item_name"));
					updateHisSynLog(hsb,logname);		
				}
				rs2.close();
			}
			rs1.close();

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

		// 通过charging_item查询是否和诊疗项目一致

		try {
			boolean falgs = false;
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String str = "select id,item_code,item_name from his_clinic_item where is_active='N' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + str);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(str);
			while (rs1.next()) {
				String sql1 = "select a.id,a.his_num,a.hiscodeClass,a.item_code,a.item_name,a.item_code from charging_item a where a.hiscodeClass='2' and a.isActive='Y' and a.his_num='"
						+ rs1.getString("id") + "'  ";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql1);
				ResultSet rs2 = tjtmpconnect.createStatement().executeQuery(sql1);
				if (rs2.next()) {
					long chargid = rs2.getLong("id");
					String sql2 = "update charging_item set his_num='',hiscodeClass='' ,his_flag=1 where id=" + chargid + "";
					tjtmpconnect.createStatement().execute(sql2);
					falgs = true;
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql2);
					HisSynLogBean hsb = new HisSynLogBean();
					hsb.setHiscodeClass("2");
					hsb.setItem_id(chargid);
					hsb.setItem_code(rs2.getString("item_code"));
					hsb.setItem_name(rs2.getString("item_name"));
					hsb.setOld_his_item_code(rs1.getString("item_code"));
					hsb.setOld_his_item_name(rs1.getString("item_name"));
					updateHisSynLog(hsb,logname);	
				}
				rs2.close();
			}
			rs1.close();
			String PRICECHECK_CHECKER = configService.getCenterconfigByKey("PRICECHECK_CHECKER").getConfig_value()
					.trim();// 项目价格异动通知人id，逗号隔开

			if (falgs) {
				sendZNX("诊疗项目有无效项，请查询并确认", PRICECHECK_CHECKER, 14, logname);
			}

			/*falgs = false;
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			str = "select id from his_clinic_item where is_active='Y' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + str);
			rs1 = tjtmpconnect.createStatement().executeQuery(str);
			if (rs1.next()) {
				falgs = true;
			}
			rs1.close();

			if (falgs) {
				sendZNX("诊疗项目有新增项，请查询并确认", PRICECHECK_CHECKER, 14, logname);
			}*/

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
	
	/**
	 * 
	 * @param infocon
	 * @param tousers
	 * @param fromuser
	 * @param logname
	 */
	public void sendZNX(String infocon, String tousers, long fromuser, String logname) {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sql = "insert into system_informs(inform_content,valid_date,is_active,creater,create_time,updater,"
					+ "update_time) values('" + infocon + "',null,'Y','" + fromuser + "','" + DateTimeUtil.getDateTime()
					+ "','" + fromuser + "','" + DateTimeUtil.getDateTime() + "')";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
			PreparedStatement preparedStatement = null;
			preparedStatement = tjtmpconnect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.executeUpdate();
			ResultSet rs = null;
			rs = preparedStatement.getGeneratedKeys();
			int retId = 0;
			if (rs.next()) {
				retId = rs.getInt(1);
			}
			rs.close();
			preparedStatement.close();
			String[] users = tousers.split(",");
			for (int i = 0; i < users.length; i++) {
				sql = "insert into system_informs_user(informs_id,user_id,reader_flag,reader_time,"
						+ "creater,create_time,updater,update_time) values('" + retId + "','" + users[i]
						+ "','0',null,'" + fromuser + "','" + DateTimeUtil.getDateTime() + "','" + fromuser + "','"
						+ DateTimeUtil.getDateTime() + "')";
				tjtmpconnect.createStatement().execute(sql);
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
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
	
	/**
	 * 
	 * @param infocon
	 * @param tousers
	 * @param fromuser
	 * @param logname
	 */
	public void updateHisSynLog(HisSynLogBean hsb, String logname) {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sql = "select id,item_id,item_code,item_name,hiscodeClass,old_his_item_code,old_his_item_name,create_time "
					+ " from his_syn_log where item_id='"+hsb.getItem_id()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sql);
			if (rs1.next()) {
					String sql2 = "update his_syn_log set item_id='"+hsb.getItem_id()+"',item_code='"+hsb.getItem_code()
					+"' ,item_name='"+hsb.getItem_name()+"',hiscodeClass='"+hsb.getHiscodeClass()
					+"',old_his_item_code='"+hsb.getOld_his_item_code()+"',old_his_item_name='"+hsb.getOld_his_item_name()
					+"',create_time=getdate()  where id=" + hsb.getItem_id() + "";
					tjtmpconnect.createStatement().execute(sql2);
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql2);
			}else{
				String sql2 = "insert into his_syn_log(item_id,item_code,item_name,hiscodeClass,"
						+ "old_his_item_code,old_his_item_name,create_time) values('" + hsb.getItem_id() + "','"+hsb.getItem_code()
					+"','"+hsb.getItem_name()+"','"+hsb.getHiscodeClass()+"','"+hsb.getOld_his_item_code()+"','"+hsb.getOld_his_item_name()
					+"',getdate())";
				tjtmpconnect.createStatement().execute(sql2);
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql2);
			}
			rs1.close();
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
