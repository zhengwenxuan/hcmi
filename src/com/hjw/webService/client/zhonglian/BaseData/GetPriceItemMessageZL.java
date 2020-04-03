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
import com.hjw.webService.client.Bean.HisSynLogBean;
import com.hjw.webService.client.Bean.PriceItemInfo;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.service.Databean.Price;
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
public class GetPriceItemMessageZL {
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
	
	public GetPriceItemMessageZL(){
		
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getPriceItem(String url,String charset,String logname) {
		this.charset=charset;
		ResultHeader res=new ResultHeader();
		List<PriceItemInfo> priceItemInfo = new ArrayList<PriceItemInfo>();
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
			
            String str="select item_class,item_code,item_name,units,item_spec,price,is_active from his_price_list where is_active='Y' ";
            ResultSet rs1 = connect.createStatement().executeQuery(str);
			if (rs1.next()) {
				PriceItemInfo ci = new PriceItemInfo();
				ci.setPriceClass(rs1.getString("item_class"));
				ci.setPriceCode(rs1.getString("item_code"));
				ci.setPriceName(rs1.getString("item_name"));
				ci.setPrice(rs1.getDouble("price"));
				ci.setPriceSpec(rs1.getString("item_spec"));
				ci.setPriceUnits(rs1.getString("units"));
				priceItemInfo.add(ci);
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
			insertPriceItem(priceItemInfo,logname);
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
	private void insertPriceItem(List<PriceItemInfo> deptInfo,String logname) throws Exception {
		List<Price> depList=new ArrayList<Price>();
		for(PriceItemInfo c:deptInfo){
			Price dt= new Price();
			dt.setAction("1");
			dt.setInput_code("");
			dt.setItem_class(c.getPriceClass());
			dt.setItem_code(c.getPriceCode());
			dt.setItem_name(c.getPriceName());
			dt.setUnits(c.getPriceUnits());
			dt.setPrice(c.getPrice());
			dt.setPrefer_price("0");
			dt.setStart_date("2017-01-01 12:00:00");
			dt.setEnter_date("2117-01-01 12:00:00");
			dt.setItem_spec(c.getPriceSpec());
			dt.setPrice(c.getPrice());
			depList.add(dt);			
		}
		savePrice_comm(depList,logname);		
	}
	
	private void savePrice_comm(List<Price> priceList, String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		try {			
			if ((priceList != null) && (priceList.size() > 0)) {
				String sql="";
				try {
					tjtmpconnect = this.jdbcQueryManager.getConnection();
				    sql = "update his_price_list set is_active='N',update_date='" + DateTimeUtil.getDateTime()
						+ "'";
				    tjtmpconnect.createStatement().execute(sql);
				    TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
				} catch (Exception ex) {
						tjtmpconnect.rollback();
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
			for (Price price : priceList) {
				try {
					tjtmpconnect = this.jdbcQueryManager.getConnection();
					tjtmpconnect.setAutoCommit(false);
					if ((price.getStop_date() == null) || (price.getStop_date().trim().length() <= 0)) {
						price.setStop_date(DateTimeUtil.DateAdd2(365 * 100));
					}
					if (price.getItem_name().indexOf("'") >= 0) {
						price.setItem_name(price.getItem_name().replaceAll("'", "''"));
					}

					if (price.getMemo().indexOf("'") >= 0) {
						price.setMemo(price.getMemo().replaceAll("'", "''"));
					}

					if (price.getMemo().indexOf("'") >= 0) {
						price.setMemo(price.getMemo().replaceAll("'", "''"));
					}

					String startdate = DateTimeUtil.shortFmt2(DateTimeUtil.parse(price.getStart_date()));
					price.setStart_date(startdate);
					StringBuffer sb = new StringBuffer();
					sb.append("select id,item_class,item_name,price from his_price_list where item_class='" + price.getItem_class()
							+ "' and item_code='" + price.getItem_code() + "' and item_spec='" + price.getItem_spec()
							+ "' and units='" + price.getUnits() + "'  ");
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb.toString());
					ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
					if (rs1.next()) {
						long picid = rs1.getLong("id");
						String item_class=rs1.getString("item_class");
						
						BigDecimal bd = new BigDecimal(0);
						bd = new BigDecimal(rs1.getDouble("price"));
						double oldprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						bd = new BigDecimal(price.getPrice());
						double newoldprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							if (oldprice != newoldprice) {
								sql = "update his_price_list set item_name='" + price.getItem_name() + "',price="
										+ price.getPrice() + ",is_active='Y' where id='" + picid + "'";
								tjtmpconnect.createStatement().execute(sql);
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);

								sb = new StringBuffer();
								sb.append("update charging_item set amount=" + price.getPrice()
										+ ",isActive='Y',item_class='" + item_class + "'  where his_num='" + picid
										+ "' and hiscodeClass='1' ");
								tjtmpconnect.createStatement().execute(sb.toString());
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb.toString());

								String chargsql = "select id from charging_item where isActive='Y' and his_num='"
										+ picid + "' and hiscodeClass='1'";
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + chargsql);
								ResultSet rsc = tjtmpconnect.createStatement().executeQuery(chargsql);
								while (rsc.next()) {
									tjtmpconnect.createStatement()
											.execute("update set_charging_item set amount=" + price.getPrice()
													+ "*itemnum,discount=10,item_amount=" + price.getPrice()
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
							} else {
								sql = "update his_price_list set is_active='Y' where id='" + picid + "'";
								tjtmpconnect.createStatement().execute(sql);
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
							}
					} else {
						sql = "insert into his_price_list(item_class,item_code,item_name,"
								+ "item_spec,units,price,prefer_price,performed_by,input_code,"
								+ "class_on_inp_rcpt,class_on_outp_rcpt,class_on_reckoning,"
								+ "subj_code,memo,start_date,stop_date,create_date,update_date,is_active) values('"
								+ price.getItem_class() + "','" + price.getItem_code() + "'" + ",'"
								+ price.getItem_name() + "','" + price.getItem_spec() + "','" + price.getUnits() + "',"
								+ price.getPrice() + "," + price.getPrefer_price() + ",'" + price.getPerformed_by()
								+ "','" + price.getInput_code() + "'" + ",'" + price.getClass_on_inp_rcpt() + "','"
								+ price.getClass_on_outp_rcpt() + "','" + price.getClass_on_reckoning() + "'" + ",'"
								+ price.getSubj_code() + "','" + price.getMemo() + "','" + startdate + "','"
								+ price.getStop_date() + "','" + DateTimeUtil.getDateTime() + "','"
								+ DateTimeUtil.getDateTime() + "','Y')";
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
						tjtmpconnect.createStatement().execute(sql);

					}
					rs1.close();
					tjtmpconnect.commit();
				} catch (Exception ex) {
					tjtmpconnect.rollback();
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

		// 通过charging_item查询是否和诊疗项目一致
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			//通过charging_item查询是否和下面的一致
			String str = "select a.id,a.his_num,a.item_code,a.item_name,a.hiscodeClass from charging_item a where a.hiscodeClass='1' and isActive='Y' and a.his_num IS not NULL and LEN(a.his_num)>0";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + str);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(str);
			if (rs1.next()) {
				long chargid=rs1.getLong("id");
				String his_num = rs1.getString("his_num");
				String sql1="select id,item_code,item_name from his_price_list where ID='"+his_num+"' and is_active='N'";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql1);
				ResultSet rs2 = tjtmpconnect.createStatement().executeQuery(sql1);
				if (rs2.next()) {
					String sql2="update charging_item set his_num='',hiscodeClass='',his_flag=1  where id="+chargid+"";
					tjtmpconnect.createStatement().execute(sql2);
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql2);
					HisSynLogBean hsb = new HisSynLogBean();
					hsb.setHiscodeClass("1");
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
			
		} catch (Exception ex) {
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
		

		try {
			boolean falgs = false;
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String str = "select id,item_code,item_name from his_price_list where is_active='N' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + str);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(str);
			while (rs1.next()) {
				String sql1 = "select a.id,a.his_num,a.hiscodeClass,a.item_code,a.item_name,a.item_code from charging_item a where a.hiscodeClass='1' and a.isActive='Y' and a.his_num='"
						+ rs1.getString("id") + "'  ";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql1);
				ResultSet rs2 = tjtmpconnect.createStatement().executeQuery(sql1);
				if (rs2.next()) {
					long chargid = rs1.getLong("id");
					String sql2 = "update charging_item set his_num='',hiscodeClass='' ,his_flag=1 where id=" + chargid + "";
					tjtmpconnect.createStatement().execute(sql2);
					falgs = true;
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql2);
					HisSynLogBean hsb = new HisSynLogBean();
					hsb.setHiscodeClass("1");
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
				sendZNX("价表有无效项，请查询并确认", PRICECHECK_CHECKER, 14, logname);
			}

			/*falgs = false;
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			str = "select id from his_price_list where is_active='Y' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + str);
			rs1 = tjtmpconnect.createStatement().executeQuery(str);
			if (rs1.next()) {
				falgs = true;
			}
			rs1.close();

			if (falgs) {
				sendZNX("价表有新增项，请查询并确认", PRICECHECK_CHECKER, 14, logname);
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