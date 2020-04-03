package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.UserDTO;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONSerializer;

public class PACSSendMessageCA{
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    private static JdbcPersistenceManager jdbcPersistenceManager;
    private static ConfigService configService;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	public PACSSendMessageCA(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		String exam_num = this.lismessage.getCustom().getExam_num();
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			ExamInfoUserDTO eu = configService.getExamInfoForNum(exam_num);
			eu=configService.getExamInfoForNum(lismessage.getCustom().getExam_num());
			if ((eu==null)||(eu.getId() <= 0)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {
				Connection his_connect = null;
				try {
					String jsonString = JSONSerializer.toJSON(lismessage).toString();
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
					
					if("T".equals(eu.getExam_type())) {
//						团检者也要触发写入5个表：
//						3.10就诊记录  CLINIC_MASTER
//						3.11门诊病历记录OUTP_MR
//						3.12门诊医嘱主记录OUTP_ORDERS
//						3.13检查治疗医嘱明细记录OUTP_TREAT_REC
//						3.14门诊医生收费明细OUTP_ORDERS_COSTS
						
						TranLogTxt.liswriteEror_to_txt(logname, "团检调用收费申请接口开始");
						UserDTO user = new UserDTO();
						String message = configService.paymentApplicationT(this.lismessage.getCustom().getExam_num(), user);
						TranLogTxt.liswriteEror_to_txt(logname, "团检插入5个表，返回信息："+message);
					}
					
					String dburl = url.split("&")[0];
					String user = url.split("&")[1];
					String passwd = url.split("&")[2];
					his_connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + url);
					ControlActPacsProcess ca = new ControlActPacsProcess();
					List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
					System.out.println("体检编号：="+lismessage.getCustom().getExam_num());
					
					for (PacsComponents pcs : lismessage.getComponents()) {
						System.out.println("体检项目：="+lismessage.getComponents().size());
						ApplyNOBean an = new ApplyNOBean();
						
						String exam_no = "";
						String sql = "select exam_no_seq.nextval as exam_no from dual";
						ResultSet rs0 = his_connect.createStatement().executeQuery(sql);
						if(rs0.next()) {
							exam_no = rs0.getString("exam_no");
						}
						TranLogTxt.liswriteEror_to_txt(logname, "获取 curTest_No："+sql);
						TranLogTxt.liswriteEror_to_txt(logname, "从数据库获取到：exam_no="+exam_no);
						
						String dep_inter_num = "";
						String pacs_exam_class = "";
						sql = "select dep_inter_num,pacs_exam_class from department_dep where dep_num = '" + pcs.getPacsComponent().get(0).getExam_class() + "' ";	
						TranLogTxt.liswriteEror_to_txt(logname,"查询 EXAM_CLASS 和 PERFORMED_BY -sql:"+sql);
						try {
							RowSet rs = jdbcQueryManager.getRowSet(sql);
							if(rs.next()) {
								dep_inter_num = rs.getString("dep_inter_num");
								pacs_exam_class = rs.getString("pacs_exam_class");
							}
							rs.close();
						} catch (SQLException ex) {
							TranLogTxt.liswriteEror_to_txt(logname,"Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
						}
						
						String demo_name = "";
						String select_demo_name_sql = "select srd.demo_name from charging_item ci,sample_report_demo srd "
								+ " where ci.sam_report_demo_id = srd.id and ci.item_code = '"+pcs.getPacsComponent().get(0).getItemCode()+"'";
						TranLogTxt.liswriteEror_to_txt(logname,"查询 select_demo_name_sql:"+select_demo_name_sql);
						try {
							RowSet rs = jdbcQueryManager.getRowSet(select_demo_name_sql);
							if(rs.next()) {
								demo_name = rs.getString("demo_name");
							}
							rs.close();
						} catch (SQLException ex) {
							TranLogTxt.liswriteEror_to_txt(logname,"Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
						}
						
						//1,写入：3.1检查预约记录 EXAM_APPOINTS
						String insert_EXAM_APPOINTS_sql = "insert into EXAM_APPOINTS (EXAM_NO,PATIENT_ID,NAME,SEX,DATE_OF_BIRTH,"
								+ " CHARGE_TYPE,MAILING_ADDRESS,PHONE_NUMBER,EXAM_CLASS,"
								+ " EXAM_MODE,PERFORMED_BY,REQ_DATE_TIME,REQ_DEPT,REQ_PHYSICIAN,"
								+ " COSTS,CHARGES,SCHEDULED_DATE,EXAM_SUB_CLASS,VISIT_ID,PATIENT_SOURCE) values "
								+ " ('"+exam_no+"','"+exam_num+"','"+lismessage.getCustom().getName()+"','"+lismessage.getCustom().getSexname()+"',to_date('"+eu.getBirthday()+"', 'yyyy-mm-dd hh24:mi:ss'),"
								+ " '自费','"+eu.getAddress()+"','"+eu.getPhone()+"','"+pacs_exam_class+"', "
								+ " 'B','"+dep_inter_num+"',to_date('"+DateTimeUtil.getDateTime()+"', 'yyyy-mm-dd hh24:mi:ss'),'400701','"+lismessage.getDoctor().getDoctorName()+"',"
								+ " '"+pcs.getCosts()+"','"+pcs.getCosts()+"',null,'"+demo_name+"','0','3')";
						TranLogTxt.liswriteEror_to_txt(logname, "1,写入：3.3检查主记录 EXAM_APPOINTS: " +insert_EXAM_APPOINTS_sql);
						Statement statement = his_connect.createStatement();
						statement.executeUpdate(insert_EXAM_APPOINTS_sql);
						statement.close();
						
						//2,写入：3.3检查主记录 EXAM_MASTER
//						String insert_EXAM_MASTER_sql = "insert into EXAM_MASTER (EXAM_NO,PATIENT_ID,NAME,SEX,DATE_OF_BIRTH,"
//								+ "COSTS,CHARGES,CHARGE_INDICATOR,CHARGE_TYPE,PERFORMED_BY,"
//								+ "REQ_DATE_TIME,RESULT_STATUS,EXAM_CLASS,PATIENT_SOURCE,EXAM_SUB_CLASS,VISIT_ID) values "
//								+ "('"+xxx+"','"+exam_num+"','"+lismessage.getCustom().getName()+"','"+lismessage.getCustom().getSexname()+"',to_date('"+eu.getBirthday()+"', 'yyyy-mm-dd hh24:mi:ss'),"
//								+ "'"+0+"','"+0+"','1','自费','"+dep_inter_num+"',"
//								+ "to_date('"+DateTimeUtil.getDateTime()+"', 'yyyy-mm-dd hh24:mi:ss'),'2','"+pacs_exam_class+"','3','"+demo_name+"','0')";
//						TranLogTxt.liswriteEror_to_txt(logname, "2,写入：3.3检查主记录 EXAM_MASTER: " +insert_EXAM_MASTER_sql);
//						his_connect.createStatement().executeUpdate(insert_EXAM_MASTER_sql);
						
						for (int i=0; i<pcs.getPacsComponent().size(); i++) {
							PacsComponent pc = pcs.getPacsComponent().get(i);
							System.out.println("体检子项目：="+pcs.getPacsComponent().size());
							//3,写入：3.2检查项目记录 EXAM_ITEMS
							String insert_EXAM_ITEMS_sql = "insert into EXAM_ITEMS (EXAM_NO,EXAM_ITEM_NO,EXAM_ITEM,EXAM_ITEM_CODE,COSTS) values "
									+ "('"+exam_no+"',"+(i+1)+",'"+pc.getItemName()+"','"+pc.getHis_num()+"',"+pc.getItemprice()+")";
							TranLogTxt.liswriteEror_to_txt(logname, "3,写入：3.2检查项目记录 EXAM_ITEMS: " +insert_EXAM_ITEMS_sql);
							statement = his_connect.createStatement();
							statement.executeUpdate(insert_EXAM_ITEMS_sql);
							statement.close();
							
							String select_his_price_list_sql ="select distinct p.item_class as item_class_p,p.item_code as item_code_p,"
									+" p.item_name as item_name_p,p.input_code as input_code_p,p.item_spec,p.units,p.price,p.prefer_price,p.performed_by,"
									+" p.class_on_inp_rcpt,p.class_on_outp_rcpt,p.class_on_reckoning,p.subj_code,p.memo,cp.*"
									+" from charging_item c,his_clinic_item_v_price_list cp,his_price_list p"
									+" where '"+pc.getHis_num()+"'=c.his_num and c.his_num = cp.clinic_item_code and p.item_code = cp.charge_item_code "
									+" and c.item_class = cp.clinic_item_class and cp.charge_item_class = p.item_class"
									+" and '"+DateTimeUtil.getDateTime()+"'>=p.start_date and  '"+DateTimeUtil.getDateTime()+"'<=p.stop_date ";
							TranLogTxt.liswriteEror_to_txt(logname,"查询 select_his_price_list_sql:"+select_his_price_list_sql);
							try {
								RowSet rs = jdbcQueryManager.getRowSet(select_his_price_list_sql);
								int j=0;
								while(rs.next()) {
									j++;
									//4,写入：检查计价项目exam_bill_items
									String insert_exam_bill_items_sql = "insert into exam_bill_items (EXAM_NO, EXAM_ITEM_NO, CHARGE_ITEM_NO, PATIENT_ID, VISIT_ID,"
											+ " ITEM_CLASS, ITEM_NAME, ITEM_CODE, ITEM_SPEC, AMOUNT, "
											+ " UNITS, ORDERED_BY, PERFORMED_BY, COSTS, CHARGES,"
											+ " BILLING_DATE_TIME, OPERATOR_NO, VERIFIED_INDICATOR)"
											+ " VALUES ('"+exam_no+"', "+(i+1)+", '"+j+"','"+exam_num+"', '0',"
											+ " '"+rs.getString("item_class_p")+"', '"+rs.getString("item_name_p")+"', '"+rs.getString("item_code_p")+"', '"+rs.getString("item_spec")+"', '"+rs.getInt("amount")+"',"
											+ " '"+rs.getString("units")+"', '400701', '"+dep_inter_num+"', '"+rs.getInt("amount")*rs.getDouble("prefer_price")+"', '"+rs.getInt("amount")*rs.getDouble("prefer_price")+"', "
											+ " to_date('"+DateTimeUtil.getDateTime()+"','yyyy-mm-dd hh24:mi:ss'), '8883', '0')";
									TranLogTxt.liswriteEror_to_txt(logname, "4,写入：检查计价项目exam_bill_items:" +insert_exam_bill_items_sql);
									statement = his_connect.createStatement();
									statement.executeUpdate(insert_exam_bill_items_sql);
									statement.close();
								}
								rs.close();
							} catch (SQLException ex) {
								TranLogTxt.liswriteEror_to_txt(logname,"Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
							}
						}
						
						an.setApplyNO(pcs.getReq_no());	
						an.setBarcode(exam_no);	
						list.add(an);
						
//						ZlReqPacsItemDTO zrpi = new ZlReqPacsItemDTO();
//						zrpi.setExam_info_id(eu.getId());
//						zrpi.setPacs_req_code(pcs.getReq_no());
//						zrpi.setReq_id(curTest_No);
//						configService.insert_zl_req_pacs_item(zrpi, logname);
					}
					ca.setList(list);
					rb.setControlActProcess(ca);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("pacs调用成功");
					if ("AA".equals(rb.getResultHeader().getTypeCode()))
				    {//申请发送成功
						List<ApplyNOBean> reqList = rb.getControlActProcess().getList();
						for(ApplyNOBean appBean : reqList){
							String sql = "update pacs_summary set pacs_req_code = '"+appBean.getBarcode()+"' where pacs_req_code = '"+appBean.getApplyNO()+"'";
							TranLogTxt.liswriteEror_to_txt(logname, "修改单号:" +sql);
							this.jdbcPersistenceManager.executeSql(sql); //修改单号
							
//							sql = "update OUTP_TREAT_REC set APPOINT_NO = '"+appBean.getBarcode()+"' where APPOINT_NO = '"+appBean.getApplyNO()+"'";
//							TranLogTxt.liswriteEror_to_txt(logname, "修改OUTP_TREAT_REC:" +sql);
//							Statement statement = his_connect.createStatement();
//							statement.executeUpdate(sql);
//							statement.close();
							
//							ExaminfoChargingItem item = (ExaminfoChargingItem) this.qm.load(ExaminfoChargingItem.class, pacssend.getId());
//							item.setIs_application("Y");
//							this.pm.update(item);
						}
					}
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("链接pacs数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
					TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
				} finally {
					try {
						if (his_connect != null) {
							OracleDatabaseSource.close(his_connect);
						}
					} catch (Exception sqle4) {
						sqle4.printStackTrace();
					}
				}
			}
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
	
}
