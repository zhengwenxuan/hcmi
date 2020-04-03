package com.hjw.webService.client.SanMenXia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 三门峡-彩超-锦源
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class PACSSendMessageSMX {
	private PacsMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public PACSSendMessageSMX(PacsMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
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
						String passwd = "";
						if(url.split("&").length > 2) {
							passwd = url.split("&")[2];
						}
						connect = SqlServerDatabaseSource.getConnection(dburl, user, passwd);
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + url);
						ControlActPacsProcess ca = new ControlActPacsProcess();
						List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
						TranLogTxt.liswriteEror_to_txt(logname,"体检编号：="+lismessage.getCustom().getExam_num());
						
						for (PacsComponents pcs : lismessage.getComponents()) {
							TranLogTxt.liswriteEror_to_txt(logname,"体检项目：="+lismessage.getComponents().size());
							connect.setAutoCommit(false);
							ApplyNOBean an = new ApplyNOBean();
							TranLogTxt.liswriteEror_to_txt(logname,"体检子项目：="+pcs.getPacsComponent().size());
							for (PacsComponent pc : pcs.getPacsComponent()) {
								try {
									TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
									+ ": 准备执行 basic_info_add_his");
									
									/*说明：HIS通过存储过程basic_info_add_his往JYPACS登记新检查记录

									传入参数：
											@name = N'张三’, 传入姓名
											@sex = N'女', 传入性别（男、女）
											@age = 23, 传入年龄,int
											@agetype = N'岁', 传入年龄单位(岁、月、周、天)
									        @subexamtype = N'彩超', 传入子检查类别（根据工作站设置，见注释1,必传项目）
											@exampart = N'心脏', 传入检查部位
									        @examdatetime = N'2011-08-15 08:01:59', 传入患者被安排的检查时间（DateTime型，格式”YYYY-mm-DD HH:mm:SS” ,必传项目）
									        @regdatetime = N'2011-08-15', 传入执行登记的时间（DateTime型，格式”YYYY-mm-DD HH:mm:SS” ,必传项目）
											@linchuangzhenduan = N'lczd', 传入临床诊断（病情诊断）
											@lianxifangshi = N'13805210001', 传入患者联系方式（地址电话等）
											@beizhu = N'定期复查', 传入备注信息
											@menzhenhao = N'mzh', 传入门诊号
											@zhuyuanhao = N'zyh', 传入住院号
											@bingqu = N'bq', 传入病区
											@chuanghao = N'ch', 传入床号
											@examstatus = N'待检查', 传入固定值’待检查’ ,必传项目
											@dengjishi = N'HIS', 传入固定值’HIS’ ,必传项目
											@hisno = N'', 传入HIS查询号，如检查号、门诊号、住院号、申请单号等，供HIS查询检查结果。
									        @shenqingdanhao = N'', 传入HIS查询号，如检查号、门诊号、住院号、申请单号等，供HIS查询检查结果（可以和hisno相同或不同）。
											@modality = N'US', 传入固定值’US’（注意大写）,必传项目
										    @feibie = N'自费', 传入费别（自费、公费等）
									        @base_examtype_id = 1, 传入检查类别ID（根据工作站设置，见注释2）,必传项目
									        @base_subexamtype_id = 34, 传入子检查类别ID（根据工作站设置，见注释3）,必传项目
											@callstatus = N'', 传入空值’’, 传入NULL,必传项目
											@examtype = N'超声检查', 传入固定值’超声检查’ ,必传项目
									        @shenqingkeshi =N'内科',传入申请科室
									        @shenqingyisheng =N'张',传入申请医生
									        @cost =100.50 ,传入检查费，如无，传入0,必传项目
*/
								
								TranLogTxt.liswriteEror_to_txt(logname,
										"req:" + lismessage.getMessageid() + ":1、调用存储过程  basic_info_add_his");
								CallableStatement c = connect
										.prepareCall("{call basic_info_add_his(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
								TranLogTxt.liswriteEror_to_txt(logname, "res:basic_info_add_his-" + exam_id + "-basic_info_add_his('"
										+lismessage.getCustom().getName()+"','"
										+lismessage.getCustom().getSexname()+"',"
										+lismessage.getCustom().getOld()+",'岁','体检','"
										+pc.getItemName()+"','"
										+ new java.sql.Date(System.currentTimeMillis())+"','"
										+ new java.sql.Date(System.currentTimeMillis())+"','','"
										+lismessage.getCustom().getTel()+"','','','','','','暂停检查','HIS','"
										+eu.getExam_num()+ "','"
										+pcs.getReq_no()+"','US','体检',1,93,'null','超声检查','体检','"
										+this.lismessage.getDoctor().getDoctorName()+"',"
										+pc.getItemamount()+")");
								c.setString(1, lismessage.getCustom().getName());
								c.setString(2, lismessage.getCustom().getSexname());
								c.setInt(3,lismessage.getCustom().getOld());
								c.setString(4, "岁");
								c.setString(5, "体检");
								c.setString(6, pc.getItemName());
								c.setDate(7, new java.sql.Date(System.currentTimeMillis()));
								c.setDate(8, new java.sql.Date(System.currentTimeMillis()));
								c.setString(9, "");
								c.setString(10, lismessage.getCustom().getTel());
								c.setString(11, "");
								c.setString(12, "");
								c.setString(13, "");
								c.setString(14, "");
								c.setString(15, "");
								c.setString(16, "暂停检查");
								c.setString(17, "HIS");
								c.setString(18, eu.getExam_num());
								c.setString(19, pcs.getReq_no());
								c.setString(20, "US");
								c.setString(21, "体检");
								c.setInt(22, 1);
								c.setInt(23, 93);
								c.setString(24, "null");
								c.setString(25, "超声检查");
								c.setString(26, "体检");
								c.setString(27, this.lismessage.getDoctor().getDoctorName());
								c.setDouble(28, pc.getItemamount());
								
								// 执行存储过程
								c.execute();
								
								c.close();
								TranLogTxt.liswriteEror_to_txt(logname,
										"res:调用存储过程  basic_info_add_his-" + lismessage.getMessageid() + ":发送申请成功");
								
							
								} catch (Exception ex) {
									TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
											+ ": 1、pacs调用存储过程  basic_info_add_his错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
								}								
							}
								an.setApplyNO(pcs.getReq_no());	
								list.add(an);
								connect.commit();									
						}
						ca.setList(list);
						rb.setControlActProcess(ca);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("pacs调用成功");
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
	
}
