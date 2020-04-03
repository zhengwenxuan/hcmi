package com.hjw.webService.client.tj180;

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

import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.ExamUpdateResBean;
import com.hjw.webService.client.tj180.Bean.GroupItemMessageBeanTJ180;
import com.hjw.webService.client.tj180.Bean.GroupMessageBeanTJ180;
import com.hjw.webService.client.tj180.Bean.PriceItemInfo;
import com.hjw.webService.client.tj180.Bean.PriceUpdateItemInfo;
import com.hjw.webService.client.tj180.Bean.ResPriceItemBean;
import com.hjw.webService.client.tj180.Bean.ResUpdatePriceItemBean;
import com.hjw.webService.service.Databean.Price;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.14	挂号信息服务   天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PriceUpdateSendMessageTJ180 {
	private String charset="utf-8";
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
		public ResultHeader getPriceItem(String url,String charset,String logname) {
			charset=charset;
			ResultHeader res=new ResultHeader();
			try {			
				HttpClient httpClient = new DefaultHttpClient();  
				HttpGet httpPost = new HttpGet(url);	
		        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
		        String datetimes=getPriceUpdate(1,logname);
		        com.hjw.webService.client.tj180.Bean.PriceUPdateGetBean pg= new com.hjw.webService.client.tj180.Bean.PriceUPdateGetBean();
		        pg.setLastDate(datetimes);
		        JSONObject json = JSONObject.fromObject(pg);// 将java对象转换为json对象
				String str = json.toString();// 将json对象转换为字符串
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
				
				String result = HttpUtil.doPost(url,pg,"utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
	            if((result!=null)&&(result.trim().length()>0)){
					result = result.trim();
					JSONObject jsonobject = JSONObject.fromObject(result);
		
						Map classMap = new HashMap();
						classMap.put("priceItemInfo", PriceUpdateItemInfo.class);	 
						ResUpdatePriceItemBean rdb= new ResUpdatePriceItemBean();
						rdb = (ResUpdatePriceItemBean)JSONObject.toBean(jsonobject,ResUpdatePriceItemBean.class,classMap);
						if(rdb==null){
							res.setTypeCode("AE");
		    				res.setText(url  +" 返回无返回");
						}else if(!"200".equals(rdb.getStatus())){
							res.setTypeCode("AE");
		    				res.setText(rdb.getErrorinfo());	
						}else{
							insertPriceItem(rdb.getPriceItemInfo(),logname);
							res.setTypeCode("AA");
		    				res.setText("");
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
		private void insertPriceItem(List<PriceUpdateItemInfo> deptInfo,String logname) throws Exception {
			List<Price> depList=new ArrayList<Price>();
			for(PriceUpdateItemInfo c:deptInfo){
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
			setPriceUpdate(1,logname);
		}
		
		private void savePrice_comm(List<Price> priceList, String logname) throws ServiceException {
			Connection tjtmpconnect = null;
			try {			
				if ((priceList != null) && (priceList.size() > 0)) {
					String sql="";
					/*String sql = "update his_price_list set is_active='N',update_date='" + DateTimeUtil.getDateTime()
							+ "'";
					tjtmpconnect.createStatement().execute(sql);
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);*/
					
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
								+ "' and units='" + price.getUnits() + "' and item_name='"+price.getItem_name()+"' ");
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
							if(oldprice!=newoldprice){
							sql = "update his_price_list set item_name='" + price.getItem_name() + "',price="
									+ price.getPrice() + ",is_active='Y' where id='" + picid + "'";
							tjtmpconnect.createStatement().execute(sql);
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);

							sb = new StringBuffer();
							sb.append("update charging_item set amount=" + price.getPrice()
									+ ",isActive='Y',item_class='"+item_class+"'  where his_num='" + picid + "' and hiscodeClass='1' ");
							tjtmpconnect.createStatement().execute(sb.toString());
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb.toString());
							
							String chargsql="select id from charging_item where isActive='Y' and his_num='" + picid + "' and hiscodeClass='1'";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + chargsql);
							ResultSet rsc = tjtmpconnect.createStatement().executeQuery(chargsql);
							while (rsc.next()) {
								tjtmpconnect.createStatement().execute("update set_charging_item set amount=" + price.getPrice()
									+ "*itemnum,discount=10,item_amount=" + price.getPrice()
									+ "  where charging_item_id='" + rsc.getInt("id") + "' ");							
								String setsql="select exam_set_id from set_charging_item where charging_item_id='" + rsc.getInt("id") + "'";
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + setsql);
								ResultSet rss = tjtmpconnect.createStatement().executeQuery(setsql);
								if (rss.next()) {
									String upsql="update exam_set set set_discount=10,"
											+ "set_amount=(select SUM(a.amount) from set_charging_item  a "
											+ "where a.exam_set_id='"+rss.getString("exam_set_id")+"') "
											+ "where id='"+rss.getString("exam_set_id")+"'";
									TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
									tjtmpconnect.createStatement().execute(upsql);								
								}							
								rss.close();
							}
							rsc.close();
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
							tjtmpconnect.createStatement().execute(sql);
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
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
			/*try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				//通过charging_item查询是否和下面的一致
				String str = "select a.id,a.his_num,a.hiscodeClass from charging_item a where a.hiscodeClass='1' and isActive='Y' and a.his_num IS not NULL and LEN(a.his_num)>0";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + str);
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(str);
				if (rs1.next()) {
					long chargid=rs1.getLong("id");
					String his_num = rs1.getString("his_num");
					String sql1="select id from his_price_list where ID='"+his_num+"'  and is_active='N'";
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql1);
					ResultSet rs2 = tjtmpconnect.createStatement().executeQuery(sql1);
					if (rs2.next()) {
						String sql2="update charging_item set his_num='',hiscodeClass='' where id="+chargid+"";
						tjtmpconnect.createStatement().execute(sql2);
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql2);
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
				String str = "select id from his_price_list where is_active='N' ";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + str);
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(str);
				while (rs1.next()) {
					falgs = true;
					String sql2 = "update charging_item set his_num='',hiscodeClass='',amount=0 where hiscodeClass='1' and his_num='"
							+ rs1.getString("id") + "' ";
					tjtmpconnect.createStatement().execute(sql2);
					TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql2);
				}
				rs1.close();
				String PRICECHECK_CHECKER = configService.getCenterconfigByKey("PRICECHECK_CHECKER").getConfig_value()
						.trim();// 项目价格异动通知人id，逗号隔开

				if (falgs) {
					sendZNX("价表有无效项，请查询并确认", PRICECHECK_CHECKER, 14, logname);
				}

				falgs = false;
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
			}*/
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
		
		public String getPriceUpdate(int types,String logname) {
			Connection tjtmpconnect = null;
			String updatetime=DateUtil.getDateTime();
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String sql = "select updatetype,CONVERT(varchar(100), updatedatetime, 20) as updatedatetime from his_update where  updatetype='"+types+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
				PreparedStatement preparedStatement = null;
				ResultSet rs = tjtmpconnect.createStatement().executeQuery(sql);
				int itemnum=0;
   			    if (rs.next()) {
   			    	updatetime = rs.getString("updatedatetime");
				}
				rs.close();
				
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
			return updatetime;
		}
		
		public String setPriceUpdate(int types,String logname) {
			Connection tjtmpconnect = null;
			String updatetime=DateUtil.getDateTime();
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String sql = "update his_update set updatedatetime=GETDATE() where  updatetype='"+types+"')";
				tjtmpconnect.createStatement().execute(sql);
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
				
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
			return updatetime;
		}
}
