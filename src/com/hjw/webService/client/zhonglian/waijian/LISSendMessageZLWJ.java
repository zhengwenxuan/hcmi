package com.hjw.webService.client.zhonglian.waijian;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.zhonglian.bean.PeisitemsPascBean;
import com.hjw.webService.client.zhonglian.bean.ZLReqItemBean;
import com.hjw.webService.client.zhonglian.bean.ZLReqPatInfoBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.service.QueryInterfaceResultService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

public class LISSendMessageZLWJ{
	private LisMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    private static QueryInterfaceResultService queryInterfaceResultService;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		queryInterfaceResultService = (QueryInterfaceResultService) wac.getBean("queryInterfaceResultService");
	}
	public LISSendMessageZLWJ(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	public ResultLisBody getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "lismessage:" + JSONObject.fromObject(lismessage));
		ResultLisBody rb = new ResultLisBody();
		String exam_num = this.lismessage.getCustom().getExam_num();
		long exam_id = 0;
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu=this.getExamInfoForNum(lismessage.getCustom().getExam_num());
				if ((eu==null)||(eu.getId() <= 0)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					exam_id=eu.getId();
					Connection connect = null;
					try {
						String dburl = url.split("&")[0];
						String user = url.split("&")[1];
						String passwd = url.split("&")[2];
						connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + url);
						ControlActLisProcess ca = new ControlActLisProcess();
						List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
						System.out.println("体检编号：="+lismessage.getCustom().getExam_num());
						ZLReqPatInfoBean zlreqpatinfo = new ZLReqPatInfoBean();
						zlreqpatinfo = this.getzl_req_patInfo(lismessage.getCustom().getExam_num());
						if((zlreqpatinfo==null)||(StringUtil.isEmpty(zlreqpatinfo.getZl_tjh())))
						{
							UserDTO userDTO = new UserDTO();
							userDTO.setUserid(14);
							userDTO.setCenter_num("001");
							queryInterfaceResultService.againGuahao(lismessage.getCustom().getExam_num(), userDTO);
//							rb.getResultHeader().setTypeCode("AE");
//							rb.getResultHeader().setText("体检编号查询reqpatid为空");
						}
						for (LisComponents pcs : lismessage.getComponents()) {
							System.out.println("体检项目：="+lismessage.getComponents().size());
							boolean pacsflags = true;
							connect.setAutoCommit(false);
							String req_no = "";
							ApplyNOBean an = new ApplyNOBean();
							for (LisComponent pc : pcs.getItemList()) {
								TranLogTxt.liswriteEror_to_txt(logname,"-----------------项目间分割线-----------------");
								String sql = "select eci.* from examinfo_charging_item eci where isActive = 'Y' and examinfo_id = "+ eu.getId() +" and charge_item_id = '"+ pc.getChargingItemid()+"'";
								TranLogTxt.liswriteEror_to_txt(logname, "sql:"+sql);
								List<ExaminfoChargingItemDTO> eciList = this.jdbcQueryManager.getList(sql, ExaminfoChargingItemDTO.class);
								ExaminfoChargingItemDTO chargingItemDTO = eciList.get(0);
								
								System.out.println("体检子项目：="+pcs.getItemList().size());
								String zllb = "C";// 诊疗类别_In
								try {
									System.out.println("体检item_code()：="+pc.getItemCode());
									PeisitemsPascBean pp = new PeisitemsPascBean();
									pp=this.getPeisitemsPascBean(url, pc.getItemCode(),logname);
									if(pp==null){
										pacsflags = false;
										TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
												+ ": 1、lis调用查询检查部位和执行科室错误" +pc.getItemCode());
									}else{
										TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
										+ ": 准备执行 Zl_NewRequest");
									/*
									 * 检验申请：zl_NewRequest (保存申请单号) CREATE OR
									 * REPLACE PROCEDURE Zl_NewRequest ( 
									 * 诊疗类别_In 病人医嘱记录.诊疗类别%Type, 
									 * 病人ID_in   病人医嘱记录.病人id%Type, 
									 * 体检号_in 病人信息.健康号%Type,
									 * 检验项目ID_in Varchar2,  pacs_num
									 * 采集方式ID_in 病人医嘱记录.诊疗项目id%Type, 空
									 * 标本部位_in Varchar2, dm.QryLisTemp.SQL.Text :
									    = 'select 标本部位 from getpeisitems_pasc where ID='+ QuotedStr(QryItem.FieldByName('view_num').AsString);
									 * 执行科室ID_in 病人医嘱记录.开嘱科室id%Type:,  dm.QryLisTemp.SQL.Text :
									 * = 'select 执行科室ID from getpeisitems_pasc where ID=' + QuotedStr(QryItem.FieldByName('view_num').AsString);
									 * 开单科室ID_in 病人医嘱记录.开嘱科室id%Type:=Null, 
									 * 开单人_in  病人医嘱记录.开嘱医生%Type, 
									 * RESULT_CODE Out Varchar2, 
									 * ERROR_MSG Out Varchar2,
									 * strReturn Out Varchar2 ) 
									 * --功能:检验申请。 --参数：
									 * --返回："申请单号"。
									 */
									
									TranLogTxt.liswriteEror_to_txt(logname,
											"req:" + lismessage.getMessageid() + ":1、调用存储过程  Zl_NewRequest");
									CallableStatement c = connect
											.prepareCall("{call Zl_NewRequest(?,?,?,?,?,?,?,?,?,?,?,?)}");
									TranLogTxt.liswriteEror_to_txt(logname, "res:Zl_NewRequest-" + exam_id + "-Zl_NewRequest('"+zllb+"','"+lismessage.getCustom().getPersonid()+"','"
											+zlreqpatinfo.getZl_tjh()+"',"+pc.getItemCode()+",'','"+pp.getJcbw()+"','"
													+pp.getZxks()+"','"+lismessage.getDoctor().getDept_code()+"','"+lismessage.getDoctor().getDoctorName()+"'"
															+ ",?,?,?)");
									c.setString(1, zllb);
									c.setString(2, lismessage.getCustom().getPersonid());
									//c.setString(3, zlreqpatinfo.getZl_tjh());
									c.setString(3, lismessage.getCustom().getExam_num());
									c.setString(4, pc.getItemCode());// 检验项目ID_in
									c.setString(5, this.lismessage.getCjfs());// 采集方式ID_in
									c.setString(6, pp.getJcbw());// 标本部位_in
									c.setString(7, this.lismessage.getZxksid());
									c.setString(8, lismessage.getDoctor().getDept_code());
									c.setString(9, lismessage.getDoctor().getDoctorName());
									c.registerOutParameter(10, java.sql.Types.VARCHAR);
									c.registerOutParameter(11, java.sql.Types.VARCHAR);
									c.registerOutParameter(12, java.sql.Types.VARCHAR);
									// 执行存储过程
									c.execute();
									// 得到存储过程的输出参数值
									String RESULT_CODE = c.getString(10);
									String ERROR_MSG = c.getString(11);
									req_no = c.getString(12);
									c.close();
									TranLogTxt.liswriteEror_to_txt(logname,
											"res:Zl_NewRequest-" + lismessage.getMessageid() + ":" + RESULT_CODE
													+ "-----" + ERROR_MSG + "-----" + req_no);

									if ("1".equals(RESULT_CODE)) {
										try {
											TranLogTxt.liswriteEror_to_txt(logname,
													"req:" + lismessage.getMessageid() + ":2、调用存储过程  zl_SendRequest");
											/*
											 * CREATE OR REPLACE PROCEDURE
											 * Zl_SendRequest ( 
											 * 诊疗类别_In 病人医嘱记录.诊疗类别%Type, 
											 * 申请单号_in  Varchar2, 
											 * RESULT_CODE Out
											 * Varchar2, ERROR_MSG Out Varchar2,
											 * strReturn Out Varchar2 )
											 * --功能:检验申请单号。 --参数：
											 * --返回："医嘱发送号|HIS单据号"。
											 */

											CallableStatement cs = connect
													.prepareCall("{call zl_SendRequest(?,?,?,?,?)}");
											TranLogTxt.liswriteEror_to_txt(logname, "res:zl_SendRequest-" + exam_id + "-zl_SendRequest('"+zllb+"','"+req_no+"',?,?,?)");
											cs.setString(1, zllb);
											cs.setString(2, req_no);
											cs.registerOutParameter(3, java.sql.Types.VARCHAR);
											cs.registerOutParameter(4, java.sql.Types.VARCHAR);
											cs.registerOutParameter(5, java.sql.Types.VARCHAR);
											// 执行存储过程
											cs.executeUpdate();
											// 得到存储过程的输出参数值
											String RESULT_CODES = cs.getString(3);
											String ERROR_MSGS = cs.getString(4);
											String strReturnS = cs.getString(5);
											cs.close();
											TranLogTxt.liswriteEror_to_txt(logname,
													"res:zl_SendRequest-" + lismessage.getMessageid() + ":"
															+ RESULT_CODES + "-----" + ERROR_MSGS + "-----"
															+ strReturnS);
											if ("1".equals(RESULT_CODES)) {
												TranLogTxt.liswriteEror_to_txt(logname,
														"res:zl_SendRequest-" + lismessage.getMessageid() + ":准备写zl_req_item表");
												ZLReqItemBean zlreq = new ZLReqItemBean();
												zlreq.setCharging_item_id(pc.getChargingItemid());
												zlreq.setExam_info_id(exam_id);
												zlreq.setLis_req_code(pcs.getReq_no());
												zlreq.setLis_item_id(pc.getItemCode()); //pcs.getReq_no()
												zlreq.setReq_id(req_no);
												zlreq.setZl_pat_id(lismessage.getCustom().getPersonid());
												pacsflags = updatezl_req_item(zlreq,logname);
												TranLogTxt.liswriteEror_to_txt(logname,
														"res:zl_SendRequest-" + lismessage.getMessageid() + ":写zl_req_item表完成");
												pacsflags=true;
												
												if("G".equals(chargingItemDTO.getExam_indicator())) {
													TranLogTxt.liswriteEror_to_txt(logname,
															"req:" + lismessage.getMessageid() + ":3、调用存储过程 zl_xb体检明细费用_insert");
													/*
													 * 医嘱id_In：与（Zl_NewRequest）返回的申请单号一致不能为null。
													 * 操作员姓名_In	体检插入的操作员姓名，不为null。
													 * 类别_in       Varchar2，判断个人和团体，1为个人2为团体
													 * 折扣率        Number,体检传入的打折率 如：五折为50，八折为80
													 * 收费日期：    不能为null。
													 */
													CallableStatement cst = connect
															.prepareCall("{call zl_xb体检明细费用_insert(?,?,?,?,?)}");
													TranLogTxt.liswriteEror_to_txt(logname, "res:zl_xb体检明细费用_insert-" + exam_id + "-zl_xb体检明细费用_insert('"
															+req_no+"','"+lismessage.getDoctor().getDoctorName()+"','1','"+chargingItemDTO.getDiscount()*10 + "','"+pc.getItemtime()+"')");
													cst.setString(1, req_no);
													cst.setString(2, lismessage.getDoctor().getDoctorName());
													cst.setString(3, "1");
													cst.setString(4, chargingItemDTO.getDiscount()*10 + "");
													cst.setString(5, pc.getItemtime());
													cst.execute();
													cst.close();
													TranLogTxt.liswriteEror_to_txt(logname,
															"res:zl_xb体检明细费用_insert-执行成功");
												} /*else if("T".equals(chargingItemDTO.getExam_indicator())) {
													TranLogTxt.liswriteEror_to_txt(logname,
															"req:" + lismessage.getMessageid() + ":3、调用存储过程 zl_xb体检明细费用_insert");
													
													 * 医嘱id_In：与（Zl_NewRequest）返回的申请单号一致不能为null。
													 * 操作员姓名_In	体检插入的操作员姓名，不为null。
													 * 类别_in       Varchar2，判断个人和团体，1为个人2为团体
													 * 折扣率        Number,体检传入的打折率 如：五折为50，八折为80
													 * 收费日期：    不能为null。
													 
													CallableStatement cst = connect
															.prepareCall("{call zl_xb体检明细费用_insert(?,?,?,?,?)}");
													TranLogTxt.liswriteEror_to_txt(logname, "res:zl_xb体检明细费用_insert-" + exam_id + "-zl_xb体检明细费用_insert('"
															+req_no+"','"+lismessage.getDoctor().getDoctorName()+"','2','"+chargingItemDTO.getDiscount()*10 + "','"+pc.getItemtime()+"')");
													cst.setString(1, req_no);
													cst.setString(2, lismessage.getDoctor().getDoctorName());
													cst.setString(3, "1");
													cst.setString(4, chargingItemDTO.getDiscount()*10 + "");
													cst.setString(5, pc.getItemtime());
													cst.execute();
													cst.close();
													TranLogTxt.liswriteEror_to_txt(logname,
															"res:zl_xb体检明细费用_insert-执行成功");
												}*/
											} else {
												pacsflags = false;
												TranLogTxt.liswriteEror_to_txt(logname, "res:"
														+ lismessage.getMessageid() + ": 2、lis调用zl_SendRequest错误，函数返回：="+RESULT_CODES);
											}

										} catch (Exception ex) {
											pacsflags = false;
											TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
													+ ": 2、lis调用zl_SendRequest错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
										}
									} else {
										pacsflags = false;
										TranLogTxt.liswriteEror_to_txt(logname,
												"res:" + lismessage.getMessageid() + ": 1、lis调用Zl_NewRequest错误");
									}
								}
								} catch (Exception ex) {
									pacsflags = false;

									TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
											+ ": 1、lis调用Zl_NewRequest错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
								}
								
							}
							
							if (!pacsflags) {
								connect.rollback();
							} else {		
								an.setApplyNO(pcs.getReq_no());	
								list.add(an);
								connect.commit();									
							}
						}
						ca.setList(list);
						rb.setControlActProcess(ca);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("lis调用成功");
					} catch (Exception ex) {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("链接lis数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
						TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
					} finally {
						try {
							if (connect != null) {
								OracleDatabaseSource.close(connect);
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
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	private PeisitemsPascBean getPeisitemsPascBean(String url,String view_num,String logname){
		Connection connect = null;
		PeisitemsPascBean pp = new PeisitemsPascBean();
		String sb1="";
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			sb1 = "select 检查部位 as bbbw from getchargeitems_lis where trim(诊疗项目ID)='"
					+ view_num.trim() + "'";
			ResultSet rs1 = connect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": lis调用查询检查部位成功" +sb1);
				pp.setJcbw(rs1.getString("bbbw"));
				if(StringUtil.isEmpty(pp.getJcbw())) pp.setJcbw("");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": lis调用查询检查部位成功返回：检查部位=" +pp.getJcbw());
			}else{
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": lis调用查询检查部位错误" +sb1);
			}
			rs1.close();
		}catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": lis调用查询检查部位错误" +sb1);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": lis调用查询检查部位错误" +com.hjw.interfaces.util.StringUtil.formatException(ex));
			} finally {
				try {
					if (connect != null) {
						OracleDatabaseSource.close(connect);
					}
				} catch (Exception sqle4) {
					sqle4.printStackTrace();
				}
			}
		return pp;
	}
	
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times ");
		sb.append(" from exam_info c ");
		sb.append(" where c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	public ZLReqPatInfoBean getzl_req_patInfo(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select exam_info_id,zl_pat_id,exam_num,"
												+ "zl_mzh,zl_tjh,flag from zl_req_patInfo where exam_num='"
												+ exam_num + "'");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ZLReqPatInfoBean.class);
		ZLReqPatInfoBean eu = new ZLReqPatInfoBean();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ZLReqPatInfoBean)map.getList().get(0);			
		}
		return eu;
	} 
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public boolean updatezl_req_item(ZLReqItemBean zlreq,String logname) throws Exception{
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,charging_item_id,zl_pat_id,lis_item_id,"
					+ "req_id from zl_req_item where exam_info_id='"
					+ zlreq.getExam_info_id() + "' and lis_req_code='"+zlreq.getLis_req_code()+"' and req_id='"+zlreq.getReq_id()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				String updatesql = "update zl_req_item set charging_item_id='" + zlreq.getCharging_item_id()
						+ "',zl_pat_id='" + zlreq.getZl_pat_id() + "',lis_item_id='" + zlreq.getLis_item_id()
						+ "' where  exam_info_id='"
					+ zlreq.getExam_info_id() + "' and lis_req_code='"+zlreq.getLis_req_code()+"'  and req_id='"+zlreq.getReq_id()+"'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +updatesql);
				tjtmpconnect.createStatement().executeUpdate(updatesql);
			} else {
				String insertsql = "insert into zl_req_item(exam_info_id,charging_item_id,lis_req_code,zl_pat_id,lis_item_id,"
					+ "req_id) values('" + zlreq.getExam_info_id() + "','" + zlreq.getCharging_item_id() + "','"+zlreq.getLis_req_code()+"','" 
						+ zlreq.getZl_pat_id() + "','" + zlreq.getLis_item_id()
						+ "','"+zlreq.getReq_id()+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +insertsql);
				tjtmpconnect.createStatement().executeUpdate(insertsql);
			}
			rs1.close();
			return true;
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
			return false;
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
