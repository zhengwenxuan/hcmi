package com.hjw.webService.client.zhonglian.sxjt;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hjw.webService.client.zhonglian.bean.PeisitemsPascBean;
import com.hjw.webService.client.zhonglian.bean.ZLReqPacsItemBean;
import com.hjw.webService.client.zhonglian.bean.ZLReqPatInfoBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.JobDTO;
import com.hjw.wst.DTO.PacsSendDTO;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.service.CompanyService;
import com.hjw.wst.service.QueryInterfaceResultService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

public class PACSSendMessageZLSXJT{
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    private static JdbcPersistenceManager jdbcPersistenceManager;
    private static ConfigService configService;
    private static CompanyService companyService;
    private static QueryInterfaceResultService queryInterfaceResultService;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
		companyService = (CompanyService) wac.getBean("companyService");
		queryInterfaceResultService = (QueryInterfaceResultService) wac.getBean("queryInterfaceResultService");
	}
	public PACSSendMessageZLSXJT(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	public ResultPacsBody getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "lismessage:" + JSONObject.fromObject(lismessage));
		ResultPacsBody rb = new ResultPacsBody();
		String exam_num = this.lismessage.getCustom().getExam_num();
		long exam_id = 0;
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu=configService.getExamInfoForNum(exam_num);
				if ((eu==null)||(eu.getId() <= 0)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					List<PacsSendDTO> pacsSendList = getDataWithOutLis(eu, lismessage.getCustom().getContact_name(), logname);
					TranLogTxt.liswriteEror_to_txt(logname, "new lismessage:" + JSONObject.fromObject(lismessage));
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
						System.out.println("体检编号：="+eu.getExam_num());
						TranLogTxt.liswriteEror_to_txt(logname, "体检编号：="+eu.getExam_num());
						ZLReqPatInfoBean zlreqpatinfo = new ZLReqPatInfoBean();
						zlreqpatinfo = this.getzl_req_patInfo(eu.getExam_num(), logname);
						if((zlreqpatinfo==null)||(StringUtil.isEmpty(zlreqpatinfo.getZl_tjh())))
						{
							UserDTO userDTO = new UserDTO();
							userDTO.setUserid(14);
							userDTO.setCenter_num("001");
							queryInterfaceResultService.againGuahao(exam_num, userDTO);
//							rb.getResultHeader().setTypeCode("AE");
//							rb.getResultHeader().setText("体检编号查询reqpatid为空");
						}
						System.out.println("体检项目：="+lismessage.getComponents().size());
						TranLogTxt.liswriteEror_to_txt(logname, "体检项目：="+lismessage.getComponents().size());
						for (PacsComponents pcs : lismessage.getComponents()) {
							boolean pacsflags = true;
							connect.setAutoCommit(false);
							String req_no = "";
							ApplyNOBean an = new ApplyNOBean();
							for (PacsComponent pc : pcs.getPacsComponent()) {
								TranLogTxt.liswriteEror_to_txt(logname,"-----------------项目间分割线-----------------");
								String sql = "select eci.* from examinfo_charging_item eci where isActive = 'Y' and examinfo_id = "+ eu.getId() +" and charge_item_id = '"+ pc.getItemId()+"'";
								TranLogTxt.liswriteEror_to_txt(logname, "sql:"+sql);
								List<ExaminfoChargingItemDTO> eciList = jdbcQueryManager.getList(sql, ExaminfoChargingItemDTO.class);
								ExaminfoChargingItemDTO chargingItemDTO = eciList.get(0);
								
								System.out.println("体检子项目：="+pcs.getPacsComponent().size());
								String zllb = "D";// 诊疗类别_In
								try {
									System.out.println("体检pacs_num()：="+pc.getPacs_num());
									PeisitemsPascBean pp = new PeisitemsPascBean();
									pp=this.getPeisitemsPascBean(url, pc.getPacs_num(),logname);
									System.out.println("体检pacs_num()：="+pc.getPacs_num());
									if(StringUtil.isEmpty(pp.getZxks()) && !StringUtil.isEmpty(pcs.getReq_no())) {//pacs项目如果查不到执行科室，则不传
										pacsflags = false;
										TranLogTxt.liswriteEror_to_txt(logname, "res:"+"收费项目在中联库查不到执行科室：" +pc.getItemName());
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
									TranLogTxt.liswriteEror_to_txt(logname, "res:Zl_NewRequest-" + exam_id + "-Zl_NewRequest('"+zllb+"','"+eu.getPatient_id()+"','"
											+zlreqpatinfo.getZl_tjh()+"',"+pc.getPacs_num()+",'','"+pp.getJcbw()+"','"
													+pp.getZxks()+"','"+lismessage.getDoctor().getDept_code()+"','"+lismessage.getDoctor().getDoctorName()+"'"
															+ ",?,?,?)");
									c.setString(1, zllb);
									c.setString(2, eu.getPatient_id());
									//c.setString(3, zlreqpatinfo.getZl_tjh());
									c.setString(3, eu.getExam_num());
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
											cs.execute();
											cs.close();
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
												zlreq.setZl_pat_id(eu.getPatient_id());
												pacsflags = updatezl_req_pacs_item(zlreq,logname);
												TranLogTxt.liswriteEror_to_txt(logname,
														"res:zl_SendRequest-" + lismessage.getMessageid() + ":写zl_req_pacs_item表完成");
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
						
						if ("AA".equals(rb.getResultHeader().getTypeCode()))
					    {//申请发送成功
							List<ApplyNOBean> reqList = rb.getControlActProcess().getList();
							for(PacsSendDTO pacssend : pacsSendList){
								for(ApplyNOBean appBean : reqList){
									if(pacssend.getPacs_req_code().equals(appBean.getApplyNO())){
										String sql = "update examinfo_charging_item set is_application = 'Y' where id = "+ pacssend.getId();
										jdbcPersistenceManager.executeSql(sql); //修改项目状态
//										ExaminfoChargingItem item = (ExaminfoChargingItem) this.qm.load(ExaminfoChargingItem.class, pacssend.getId());
//										item.setIs_application("Y");
//										this.pm.update(item);
									}
								}
							}
						}else{
							throw new ServiceException(rb.getResultHeader().getText());
						}
					} catch (Throwable ex) {
						ex.printStackTrace();
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
	
	private List<PacsSendDTO> getDataWithOutLis(ExamInfoUserDTO eu, String charingIds, String logname) {
		PacsMessageBody pb = new PacsMessageBody();
	    pb.setId_extension("TI001");
	    pb.setMessageid("");
	    pb.setPart_name("东北国际医院");
	    pb.setCreationTime_value(DateTimeUtil.getDateTimes());
	    pb.getCustom().setArch_num(eu.getArch_num());
//	    pb.getCustom().setPersonid();
	    pb.getCustom().setPersonno(eu.getVisit_no());
	    pb.getCustom().setExam_num(eu.getExam_num());
	    pb.getCustom().setPersonidnum(eu.getId_num());
	    pb.getCustom().setPersioncode("");
	    pb.getCustom().setMc_no(eu.getMc_no());
	    pb.getCustom().setName(eu.getUser_name());
	    pb.getCustom().setSexcode(eu.getSex());
	    pb.getCustom().setComname(eu.getCompany());
	    pb.getCustom().setContact_name("");
	    pb.getCustom().setContact_tel("");
	    pb.getCustom().setEthnicGroupCode("");
	    pb.getCustom().setMeritalcode(eu.getIs_marriage());
	    pb.getCustom().setOld(new Long(eu.getAge()).intValue());
	    pb.getCustom().setTel(eu.getPhone());
	    pb.getCustom().setAddress(eu.getAddress());
	    if ("男".equals(eu.getSex()))
	    {
	      pb.getCustom().setSexcode("1");
	      pb.getCustom().setSexname("男");
	    }
	    else if ("女".equals(eu.getSex()))
	    {
	      pb.getCustom().setSexcode("2");
	      pb.getCustom().setSexname("女");
	    }
	    else
	    {
	      pb.getCustom().setSexcode("3");
	      pb.getCustom().setSexname("未知");
	    }
	    List<JobDTO> dataList = this.companyService.getDatadis("SQKS");
	    pb.getDoctor().setDept_code(((JobDTO)dataList.get(0)).getRemark());
	    pb.getDoctor().setDept_name(((JobDTO)dataList.get(0)).getName());
	    pb.getDoctor().setDoctorCode(lismessage.getDoctor().getDoctorCode());
	    pb.getDoctor().setDoctorName(lismessage.getDoctor().getDoctorName());
	    pb.getDoctor().setTime(DateTimeUtil.getDateTimes());
	    
		StringBuffer sb = new StringBuffer();
		//
		sb.append("select '' as pacs_req_code,c.view_num,c.item_code,ec.item_amount,hd.dept_code,hd.dept_name,c.his_num,c.item_name,dd.dep_num,ec.id,ec.pay_status,ec.amount,c.hiscodeClass,c.id as itemId "
				+ " from examinfo_charging_item ec,department_dep dd,charging_item c "
				+ " left join his_dict_dept hd on c.perform_dept = hd.dept_code "
				+ " where ec.charge_item_id = c.id and c.dep_id = dd.id and ec.isActive = 'Y'"
				+ " and ec.change_item != 'C' and ec.pay_status != 'M' and ec.exam_status in ('N','D')"//--and c.interface_flag = '2'
				+ " and dd.dep_category not in ('21','131')"
				+ " and ec.is_application = 'N' "
				+ " and ec.examinfo_id = "+eu.getId());
//		if ((charingIds != null) && (!"".equals(charingIds))) {
//		      sb.append(" and c.id in (" + charingIds + ")");
//		}
		
		TranLogTxt.liswriteEror_to_txt(logname, "查项目："+sb.toString());
	    List<PacsSendDTO> pacsSendList = jdbcQueryManager.getList(sb.toString(), PacsSendDTO.class);
	    
	    sb.setLength(0);
	    sb.append("select p.pacs_req_code,c.view_num,c.item_code,ec.item_amount,hd.dept_code,hd.dept_name,c.his_num,c.item_name,"
	    		+ "dd.dep_num,ec.id,ec.pay_status,ec.amount,c.hiscodeClass,c.id as itemId  from examinfo_charging_item ec,"
	    		+ "pacs_summary p,pacs_detail d,department_dep dd,charging_item c left join his_dict_dept hd "
	    		+ "on c.perform_dept = hd.dept_code where ec.charge_item_id = c.id and p.id = d.summary_id and "
	    		+ "d.chargingItem_num = c.item_code and c.dep_id = dd.id and ec.isActive = 'Y' and c.interface_flag = '2' "
	    		+ "and ec.change_item != 'C' and ec.pay_status != 'M' and ec.exam_status in ('N','D') "
	    		+ "and ec.examinfo_id = " + eu.getId() + " and p.examinfo_num = '" + eu.getExam_num() + "'");
//	    if ((charingIds != null) && (!"".equals(charingIds))) {
//	      sb.append(" and c.id in (" + charingIds + ")");
//	    }
	    sb.append(" and ec.is_application = 'N'");
	    List<PacsSendDTO> pacsSendList2 = this.jdbcQueryManager.getList(sb.toString(), PacsSendDTO.class);
	    pacsSendList.addAll(pacsSendList2);
	    
	    if ((pacsSendList.size() == 0)) {
	      return pacsSendList;
	    }
	    Map<String, PacsComponents> map = new HashMap();
	    for (PacsSendDTO pacssend : pacsSendList)
	    {
	      PacsComponents pacsCompoents = (PacsComponents)map.get(pacssend.getPacs_req_code());
	      if (pacsCompoents == null)
	      {
	        pacsCompoents = new PacsComponents();
	        pacsCompoents.setReq_no(pacssend.getPacs_req_code());
	        pacsCompoents.setDatetime(DateTimeUtil.getDate());
	      }
	      PacsComponent pc = new PacsComponent();
	      pc.setExam_class(pacssend.getDep_num());
	      pc.setItemDate(DateTimeUtil.getDate());
	      pc.setItemName(pacssend.getItem_name());
	      pc.setHis_num(pacssend.getHis_num());
	      pc.setItemCode(pacssend.getItem_code());
	      pc.setItemprice(pacssend.getItem_amount());
	      pc.setItemamount(pacssend.getAmount());
	      pc.setItemId(pacssend.getItemId());
	      pc.setItemtime(DateTimeUtil.getDateTimes());
	      pc.setPacs_num(pacssend.getView_num());
	      pc.setServiceDeliveryLocation_code(pacssend.getDept_code());
	      pc.setServiceDeliveryLocation_name(pacssend.getDept_name());
	      pc.setCode_class(pacssend.getHiscodeClass());
	      
	      pacsCompoents.setCosts(pacssend.getAmount() + pacsCompoents.getCosts());
	      pacsCompoents.getPacsComponent().add(pc);
	      
	      map.put(pacsCompoents.getReq_no(), pacsCompoents);
	    }
	    List<PacsComponents> mapValuesList = new ArrayList(map.values());
	    pb.setComponents(mapValuesList);
	    this.lismessage=pb;
	    return pacsSendList;
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
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": pacs调用查询检查部位和执行科室无结果" +sb1);
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
	
	public ZLReqPatInfoBean getzl_req_patInfo(String exam_num, String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select exam_info_id,zl_pat_id,exam_num,"
												+ "zl_mzh,zl_tjh,flag from zl_req_patInfo where exam_num='"
												+ exam_num + "'");
		TranLogTxt.liswriteEror_to_txt(logname, "sql"+sb.toString());
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
