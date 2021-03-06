package com.hjw.webService.client.zhonglian;

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
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.zhonglian.bean.PeisitemsPascBean;
import com.hjw.webService.client.zhonglian.bean.ZLReqPacsItemBean;
import com.hjw.webService.client.zhonglian.bean.ZLReqPatInfoBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSSendMessageZL{
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSSendMessageZL(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
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
						ControlActPacsProcess ca = new ControlActPacsProcess();
						List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
						System.out.println("体检编号：="+lismessage.getCustom().getExam_num());
						ZLReqPatInfoBean zlreqpatinfo = new ZLReqPatInfoBean();
						zlreqpatinfo = this.getzl_req_patInfo(lismessage.getCustom().getExam_num());
						if((zlreqpatinfo==null)||(StringUtil.isEmpty(zlreqpatinfo.getZl_tjh())))
						{
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("体检编号查询reqpatid为空");
						}else{
						for (PacsComponents pcs : lismessage.getComponents()) {
							System.out.println("体检项目：="+lismessage.getComponents().size());
							boolean pacsflags = true;
							connect.setAutoCommit(false);
							String req_no = "";
							ApplyNOBean an = new ApplyNOBean();
							for (PacsComponent pc : pcs.getPacsComponent()) {
								System.out.println("体检子项目：="+pcs.getPacsComponent().size());
								String zllb = "D";// 诊疗类别_In
								try {
									System.out.println("体检pacs_num()：="+pc.getPacs_num());
									PeisitemsPascBean pp = new PeisitemsPascBean();
									pp=this.getPeisitemsPascBean(url, pc.getPacs_num(),logname);
									System.out.println("体检pacs_num()：="+pc.getPacs_num());
									if(pp==null){
										pacsflags = false;
										TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
												+ ": 1、pacs调用查询检查部位和执行科室错误" +pc.getPacs_num());
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
											+zlreqpatinfo.getZl_tjh()+"',"+pc.getPacs_num()+",'','"+pp.getJcbw()+"','"
													+pp.getZxks()+"','"+lismessage.getDoctor().getDept_code()+"','"+lismessage.getDoctor().getDoctorName()+"'"
															+ ",?,?,?)");
									c.setString(1, zllb);
									c.setString(2, lismessage.getCustom().getPersonid());
									//c.setString(3, zlreqpatinfo.getZl_tjh());
									c.setString(3, lismessage.getCustom().getExam_num());
									c.setString(4, pc.getPacs_num());// 检验项目ID_in
									c.setString(5, "");// 采集方式ID_in
									c.setString(6, pp.getJcbw());// 标本部位_in
									c.setString(7, pp.getZxks());
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
											 * Zl_SendRequest ( 诊疗类别_In
											 * 病人医嘱记录.诊疗类别%Type, 申请单号_in
											 * Varchar2, RESULT_CODE Out
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
											TranLogTxt.liswriteEror_to_txt(logname,
													"res:zl_SendRequest-" + lismessage.getMessageid() + ":"
															+ RESULT_CODES + "-----" + ERROR_MSGS + "-----"
															+ strReturnS);
											if ("1".equals(RESULT_CODES)) {
												TranLogTxt.liswriteEror_to_txt(logname,
														"res:zl_SendRequest-" + lismessage.getMessageid() + ":准备写zl_req_pacs_item表");
												ZLReqPacsItemBean zlreq = new ZLReqPacsItemBean();
												zlreq.setCharging_item_ids(pc.getItemCode());
												zlreq.setExam_info_id(exam_id);
												zlreq.setPacs_req_code(pcs.getReq_no());
												zlreq.setReq_id(req_no);
												zlreq.setZl_pacs_id(pc.getPacs_num());
												zlreq.setZl_pat_id(lismessage.getCustom().getPersonid());
												pacsflags = updatezl_req_pacs_item(zlreq,logname);
												TranLogTxt.liswriteEror_to_txt(logname,
														"res:zl_SendRequest-" + lismessage.getMessageid() + ":写zl_req_pacs_item表完成");
												pacsflags=true;
											} else {
												pacsflags = false;
												TranLogTxt.liswriteEror_to_txt(logname, "res:"
														+ lismessage.getMessageid() + ": 2、pacs调用zl_SendRequest错误，函数返回：="+RESULT_CODES);
											}

										} catch (Exception ex) {
											pacsflags = false;
											TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
													+ ": 2、pacs调用zl_SendRequest错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
										}
									} else {
										pacsflags = false;
										TranLogTxt.liswriteEror_to_txt(logname,
												"res:" + lismessage.getMessageid() + ": 1、pacs调用Zl_NewRequest错误");
									}
								}
								} catch (Exception ex) {
									pacsflags = false;

									TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
											+ ": 1、pacs调用Zl_NewRequest错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
						rb.getResultHeader().setText("pacs调用成功");
					}
					} catch (Exception ex) {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("链接pacs数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
			sb1 = "select 标本部位 as bbbw,执行科室ID as zxks from getpeisitems_pasc where trim(id)='"
					+ view_num.trim() + "'";
			ResultSet rs1 = connect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": pacs调用查询检查部位和执行科室成功" +sb1);
				pp.setJcbw(rs1.getString("bbbw"));
				pp.setZxks(rs1.getString("zxks"));
				if(StringUtil.isEmpty(pp.getJcbw())) pp.setJcbw("");
				if(StringUtil.isEmpty(pp.getZxks())) pp.setZxks("");;
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": pacs调用查询检查部位和执行科室成功返回：检查部位=" +pp.getJcbw()+"----执行科室：="+pp.getZxks());
			}else{
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": pacs调用查询检查部位和执行科室错误" +sb1);
			}
			rs1.close();
		}catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": pacs调用查询检查部位和执行科室错误" +sb1);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": pacs调用查询检查部位和执行科室错误" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
	public boolean updatezl_req_pacs_item(ZLReqPacsItemBean zlreq,String logname) throws Exception{
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,pacs_req_code,charging_item_ids,zl_pat_id,zl_pacs_id,"
					+ "req_id from zl_req_pacs_item where exam_info_id='"
					+ zlreq.getExam_info_id() + "' and pacs_req_code='"+zlreq.getPacs_req_code()+"' and req_id='"+zlreq.getReq_id()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				String updatesql = "update zl_req_pacs_item set charging_item_ids='" + zlreq.getCharging_item_ids()
						+ "',zl_pat_id='" + zlreq.getZl_pat_id() + "',zl_pacs_id='" + zlreq.getZl_pacs_id()
						+ "' where  exam_info_id='"
					+ zlreq.getExam_info_id() + "' and pacs_req_code='"+zlreq.getPacs_req_code()+"' and req_id='"+zlreq.getReq_id()+"'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +updatesql);
				tjtmpconnect.createStatement().executeUpdate(updatesql);
			} else {
				String insertsql = "insert into zl_req_pacs_item(exam_info_id,pacs_req_code,charging_item_ids,zl_pat_id,zl_pacs_id,"
					+ "req_id) values('" + zlreq.getExam_info_id() + "','" + zlreq.getPacs_req_code()
						+ "','" + zlreq.getCharging_item_ids() + "','" + zlreq.getZl_pat_id() + "','" + zlreq.getZl_pacs_id()
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
