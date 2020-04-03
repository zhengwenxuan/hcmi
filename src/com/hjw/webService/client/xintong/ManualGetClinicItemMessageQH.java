package com.hjw.webService.client.xintong;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.webService.client.xintong.bean.ITEM_INFO;
import com.hjw.webService.client.xintong.bean.ITEM_LIST;
import com.hjw.webService.client.xintong.bean.RESPONSE;
import com.hjw.webService.client.xintong.bean.resHisClinic;
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class ManualGetClinicItemMessageQH {
	private static JdbcQueryManager jqm;
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jqm = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}


	public ResultHisBody getMessage(String url, String logname) {
		String gettokens = Gettoken.Gettokens(url,logname);
		TranLogTxt.liswriteEror_to_txt(logname, "青海 自动同步his数据开始");
		ResultHisBody rb = new ResultHisBody();
		
		try {
			resHisClinic resHisClinic = new resHisClinic();
			
			HIPMessageServiceService dam = new HIPMessageServiceServiceLocator(url);
			IHIPMessageService dams = dam.getHIPMessageServicePort();
			
			TranLogTxt.liswriteEror_to_txt(logname,"==url====:"+url);
			
			String resXml = getXML(url,logname,gettokens);
			
			TranLogTxt.liswriteEror_to_txt(logname,"传入参数==sendXml==:"+resXml);
			
			String messages = dams.HIPMessageServer2016Ext("XT70008","", resXml);
			TranLogTxt.liswriteEror_to_txt(logname,"返回诊疗价表信息==sendXml==:"+messages);
			
			//xml转换对象
			TranLogTxt.liswriteEror_to_txt(logname,"xml转换对象开始==:"+"");
			RESPONSE Clinic = JaxbUtil.converyToJavaBean(messages, RESPONSE.class);
			TranLogTxt.liswriteEror_to_txt(logname,"xml转换对象结束==:"+"");
			
			if(Clinic.getRETURN_CODE().equals("1") && Clinic.getRETURN_CODE() !=null){
				TranLogTxt.liswriteEror_to_txt(logname,"获取转换后的对象属性==:"+"");
				
				TranLogTxt.liswriteEror_to_txt(logname, "同步his诊疗价表数据===start===");
				//更新his诊疗表
				insert_his_data(logname, Clinic);
				TranLogTxt.liswriteEror_to_txt(logname, "同步his诊疗价表数据===end===");
				
				
				TranLogTxt.liswriteEror_to_txt(logname, "同步体检收费项目价表数据===start===");
				boolean fly = updateHIsPriceSynchro(logname);
				
				TranLogTxt.liswriteEror_to_txt(logname, "同步体检收费项目价表数据===end===,"+fly+"");
				if(fly==true){
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("收费价表同步成功!!");
				}
				
			}else{
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("诊疗项目同步失败!!");
			}
			
		
		} catch (Throwable e) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("HIS诊疗同步失败!");
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}

	
	//同步数据 插入体检诊疗表
	private static void insert_his_data(String logname, RESPONSE clinic) {
		
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
		for(ITEM_INFO Info : clinic.getITEM_LIST().getITEM_INFO()) {
			try {
				connect = jqm.getConnection();
				if(!"".equals(Info.getITEM_CODE())) {
					String select_clinic_sql = "select * from his_clinic_item where item_code = '"+Info.getITEM_CODE()+"'";
					TranLogTxt.liswriteEror_to_txt(logname, "select_clinic_sql:" +select_clinic_sql);
					ResultSet rs = connect.createStatement().executeQuery(select_clinic_sql);
					if(!rs.next()) {
						String insert_clinic_sql = "insert into his_clinic_item([item_class],[item_code],[item_name],[input_code]"
								+ ",[expand1],[expand2],[expand3],[item_status] ,[create_date],[update_date],[price],[is_active]) "
								+ " values ('"+Info.getPACK_FLAG()+"','"+Info.getITEM_ID()+"','"+Info.getITEM_NAME()+"',''"
								+ ",'"+Info.getSPEC_NAME()+"','"+Info.getCHARGE_UNIT()+"','"+Info.getOP_CRG_TYPE()+"','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"','"+Info.getUNIT_PRICE()+"','Y')";
//						TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目sql--"+insert_clinic_sql);
						connect.createStatement().executeUpdate(insert_clinic_sql);
						TranLogTxt.liswriteEror_to_txt(logname, "insert_clinic_sql:" +insert_clinic_sql);
						
						clinicNum++;
					}
					
					
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
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item表"+clinicNum+"条，问题数据"+errorNum+"条");
		
/////////////////// copy to AutoGetHisDataNH.java_insert_his_data() end //////////////////////////////////////////////////////
	}
	
	//更新体检收费项目表 价格
	private boolean updateHIsPriceSynchro(String logname) {
		/////////////////// copy to AutoGetHisDataNH.java_updateHIsPriceSynchro() start //////////////////////////////////////////////////////
		String sql = "";
		ResultSet charging_item_rs = null;
		ResultSet clinic_item_rs = null;
		
		Connection connection = null;
		boolean fal = false;
		try {
			connection = this.jqm.getConnection();
			sql = " SELECT  c.id,c.his_num,c.item_class,c.amount,c.hiscodeClass,c.item_name,c.item_code FROM   charging_item c  where  isActive='Y' and  his_num <> '' ";
			TranLogTxt.liswriteEror_to_txt(logname,"查询收费项目sql: " + sql);
			//查询所有  有效的 his_num不为空的 收费项目
			charging_item_rs = connection.createStatement().executeQuery(sql);
			while (charging_item_rs.next()) {
				
				
					sql = "	SELECT price,item_name,is_active FROM his_clinic_item where	item_code = '" + charging_item_rs.getString("his_num") +"' ";
						
					TranLogTxt.liswriteEror_to_txt(logname,"查询诊疗表sql: " + sql);
					
					//查询所有 item_code==his_num 不为空的 诊疗项目
					clinic_item_rs = connection.createStatement().executeQuery(sql);
					
					while (clinic_item_rs.next()) {//his_num对应诊疗项目
						double amount = clinic_item_rs.getDouble("price");
						String item_name = clinic_item_rs.getString("item_name");
						String is_active = clinic_item_rs.getString("is_active");
						TranLogTxt.liswriteEror_to_txt(logname,"诊疗项目价格: " + amount);
						TranLogTxt.liswriteEror_to_txt(logname,"收费项目价格: " + charging_item_rs.getDouble("amount"));
						TranLogTxt.liswriteEror_to_txt(logname,"收费项目价格是否和诊疗价格相等: " + (amount != charging_item_rs.getDouble("amount"))+"");
						
						TranLogTxt.liswriteEror_to_txt(logname,"诊疗项目名称: " + item_name);
						TranLogTxt.liswriteEror_to_txt(logname,"收费项目价格: " + charging_item_rs.getString("item_name"));
						TranLogTxt.liswriteEror_to_txt(logname,"收费项目价格是否和诊疗价格相等: " + (item_name.equals(charging_item_rs.getString("item_name")))+"");
						
						if(is_active.equals("Y")){//诊疗项目是否停用
						
						if (amount != charging_item_rs.getDouble("amount")) {
							
							sql = " update charging_item set  amount = " + amount + "   where  id = '" + charging_item_rs.getLong("id") + "'";
							
							TranLogTxt.liswriteEror_to_txt(logname,"按照诊疗价格更新收费项目sql: " + sql);
							connection.createStatement().executeUpdate(sql);
							
						String setchargingitem =	("update set_charging_item set amount=" + amount
							+ "*itemnum,discount=10,item_amount=" + amount
							+ "  where charging_item_id='" + charging_item_rs.getLong("id") + "' ");
						TranLogTxt.liswriteEror_to_txt(logname, "res: :更改套餐与收费项目关系表的价格： " + setchargingitem);
						connection.createStatement().executeUpdate(setchargingitem);
						
						
						String setsql = "select exam_set_id from set_charging_item where charging_item_id='"
								+ charging_item_rs.getLong("id") + "'";
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + setsql);
						ResultSet rss = connection.createStatement().executeQuery(setsql);
						if (rss.next()) {
							String upsql = "update exam_set set set_discount=10,"
									+ "price=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
									+ rss.getString("exam_set_id") + "'),set_amount=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
									+ rss.getString("exam_set_id") + "') " + "where id='" + rss.getString("exam_set_id") + "'";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
							connection.createStatement().execute(upsql);										
						}
						rss.close();
						
						fal = true;
							
						//同步项目名称时候  不同步眼科C0004169  皮肤检查C0004200
						}else if(!item_name.equals(charging_item_rs.getString("item_name")) && !charging_item_rs.getString("item_code").equals("C0004169") &&!charging_item_rs.getString("item_code").equals("C0004200")){
							
								sql = " update charging_item set  item_name = '" + item_name + "'   where  id = '" + charging_item_rs.getLong("id") + "'";
	               				TranLogTxt.liswriteEror_to_txt(logname,"按照诊疗项目名称更新收费项目名称sql: " + sql);
								connection.createStatement().executeUpdate(sql);
								
								
								fal = true;
							
						}else{
							fal = true;	
						}
						
					}else{
							//如果项目 停用修改eci表内 对应的项目为 无效
							/*String ecisql = " select eci.id from examinfo_charging_item eci,exam_info e "
									      + " where e.id=eci.examinfo_id and eci.charge_item_id='" + charging_item_rs.getLong("id") + "' and eci.isActive='Y  and eci.exam_status !='Y' ";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + ecisql);
							ResultSet ecirs = connection.createStatement().executeQuery(ecisql);
							
							while (ecirs.next()) {
								
								String upecisql = " update examinfo_charging_item set isActive='N' where id='"+ecirs.getLong("id")+"' ";
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upecisql);
								connection.createStatement().execute(upecisql);	
							}
							
							ecirs.close();*/
							
							//如果项目 停用  删除套餐与项目关系表内的 对应的项目 
							String delsetchargingsql = " delete  set_charging_item where  charging_item_id='"+charging_item_rs.getLong("id")+"' ";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + delsetchargingsql);
							connection.createStatement().executeUpdate(delsetchargingsql);
							//如果项目停用   修改套餐价格
							
							String setsql = "select exam_set_id from set_charging_item where charging_item_id='"
									+ charging_item_rs.getLong("id") + "'";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + setsql);
							ResultSet rss = connection.createStatement().executeQuery(setsql);
							if (rss.next()) {
								String upsetsql = "update exam_set set set_discount=10,"
										+ "price=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
										+ rss.getString("exam_set_id") + "'),set_amount=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
										+ rss.getString("exam_set_id") + "') " + "where id='" + rss.getString("exam_set_id") + "'";
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsetsql);
								connection.createStatement().execute(upsetsql);										
							}
							rss.close();
							
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
		/////////////////// copy to AutoGetHisDataNH.java_updateHIsPriceSynchro() start //////////////////////////////////////////////////////
		}
	
	//发送  诊疗查询请求
	/**
	 * @param url
	 * @param logname
	 * @return
	 */
	private  String getXML(String url, String logname,String gettokens) {
		
		StringBuffer bufferXml = new StringBuffer();
		
		bufferXml.append("<REQUEST>");
		bufferXml.append("<TOKENID>"+gettokens+"</TOKENID>");
		bufferXml.append("<SENDER>HJW</SENDER>");
		bufferXml.append("<REQ_MSGID>"+UUID.randomUUID().toString().toLowerCase()+"</REQ_MSGID>");
		bufferXml.append("<ORG_ID>RSS20140108000000001</ORG_ID>");
		bufferXml.append("</REQUEST>");
		
		return bufferXml.toString();
	}
	
	
}
