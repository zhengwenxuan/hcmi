package com.hjw.webService.client.zhonglian.sxjt;

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
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.18	个人信息修改  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CUSTOMEDITSendMessageZLSXJT {
	private Custom custom=new Custom();
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
	public CUSTOMEDITSendMessageZLSXJT(Custom custom){
		this.custom=custom;
	}
	
	

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String exam_num = this.custom.getEXAM_NUM();
		long exam_id = 0;
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			Connection tjconnect = null;
			try {
				ExamInfoUserDTO eu = configService.getExamInfoForNum(exam_num);
				
				tjconnect = this.jdbcQueryManager.getConnection();
				String sb = "select id from exam_info where exam_num='" + exam_num + "'";
				ResultSet rs = tjconnect.createStatement().executeQuery(sb);
				if (rs.next()) {
					exam_id = rs.getLong("id");
				}
				rs.close();
				if (exam_id <= 0) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					Connection connect = null;
					try {
						String dburl = url.split("&")[0];
						String user = url.split("&")[1];
						String passwd = url.split("&")[2];
						connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
						connect.setAutoCommit(false);

						try {
							/*
							 * CREATE OR REPLACE PROCEDURE Zl_NewPatient ( 姓名_in
							 * 病人信息.姓名%Type, 性别_in 病人信息.性别%Type, 年龄_in
							 * 病人信息.年龄%Type, 门诊号_in 病人信息.门诊号%Type:= Null, 体检号_in
							 * 病人信息.健康号%Type:= Null, 身份证号_in 病人信息.身份证号%Type:=
							 * Null, 电话号码_in 病人信息.家庭电话%Type:= Null, 地址_in
							 * 病人信息.家庭地址%Type:= Null, 工作单位_in 病人信息.家庭地址%Type:=
							 * Null, RESULT_CODE Out Varchar2, ERROR_MSG Out
							 * Varchar2, strReturn Out Varchar2 )
							 * --功能:创建病人档案，并返回病人iD、门诊号、体检号。 --参数：
							 * --返回："病人iD|门诊号|体检号"。
							 */

							if(this.custom.getDATE_OF_BIRTH()!=null && this.custom.getDATE_OF_BIRTH().length()>10) {
								this.custom.setDATE_OF_BIRTH(this.custom.getDATE_OF_BIRTH().substring(0, 10));
							}
							
							TranLogTxt.liswriteEror_to_txt(logname,
									"req:" + exam_id + "-" + exam_num + ":1、调用存储过程  Zl_NewPatient");
							CallableStatement c = connect.prepareCall("{call Zl_NewPatient(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
							TranLogTxt.liswriteEror_to_txt(logname, "res:Zl_NewPatient-" + exam_id + "-Zl_NewPatient('"+this.custom.getNAME()+"','"+this.custom.getSEX()+"','"
									+this.custom.getAGE()+"',"+this.custom.getCLINIC_NO()+",'','"+eu.getId_num()+"','"
											+eu.getPhone()+"','"+eu.getAddress()+"','"+eu.getIs_marriage()+"','"+eu.getCompany()+"'"
													+ ",'"+this.custom.getEXAM_NUM()+"','"+this.custom.getDATE_OF_BIRTH()+"',?,?,?)");
							c.setString(1, this.custom.getNAME());
							c.setString(2, this.custom.getSEX());
							c.setString(3, this.custom.getAGE());
							c.setString(4, this.custom.getCLINIC_NO());
							c.setString(5, "");
							c.setString(6, eu.getId_num());
							c.setString(7, eu.getPhone());
							c.setString(8, eu.getAddress());
							c.setString(9, eu.getIs_marriage());
							c.setString(10, eu.getCompany());
							c.setString(11, this.custom.getEXAM_NUM());
							java.sql.Date buydate=java.sql.Date.valueOf(this.custom.getDATE_OF_BIRTH());
							c.setDate(12, buydate);
							c.registerOutParameter(13, java.sql.Types.VARCHAR);
							c.registerOutParameter(14, java.sql.Types.VARCHAR);
							c.registerOutParameter(15, java.sql.Types.VARCHAR);
							// 执行存储过程
							c.execute();
							// 得到存储过程的输出参数值
							String RESULT_CODE = c.getString(13);
							String ERROR_MSG = c.getString(14);
							String strReturn = c.getString(15);
						    c.close();
							TranLogTxt.liswriteEror_to_txt(logname, "res:Zl_NewPatient-" + exam_id + "-" + exam_num
									+ ":" + RESULT_CODE + "-----" + ERROR_MSG + "-----" + strReturn);

							if ("0".equals(RESULT_CODE)) {
								if ((StringUtil.isEmpty(strReturn)) || (strReturn.split("\\|").length != 3)) {
									rb.getResultHeader().setTypeCode("AE");
									rb.getResultHeader().setText("pacs调用Zl_NewPatient错误返回参数错误-" + RESULT_CODE + "-----" + ERROR_MSG + "-----" + strReturn);
								} else {
									String PATIENT_ID = strReturn.split("\\|")[0];
									String CLINIC_NO = strReturn.split("\\|")[1];
									String retexam_num = strReturn.split("\\|")[2];
									String VISIT_NO = "";
									String VISIT_DATE = DateTimeUtil.getDate2();

									Connection tjtmpconnect = null;
									try {
										tjtmpconnect = this.jdbcQueryManager.getConnection();
										String sb1 = "select exam_info_id,zl_pat_id,exam_num,"
												+ "zl_mzh,zl_tjh,flag from zl_req_patInfo where exam_info_id='"
												+ exam_id + "' and zl_pat_id='"+custom.getPATIENT_ID()+"'";
										ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
										if (rs1.next()) {
											String updatesql = "update zl_req_patInfo set zl_pat_id='" + PATIENT_ID
													+ "',zl_mzh='" + CLINIC_NO + "',zl_tjh='" + retexam_num
													+ "' where  exam_info_id='" + exam_id + "'  and zl_pat_id='"+PATIENT_ID+"'";
											tjtmpconnect.createStatement().executeUpdate(updatesql);
										} else {
											String insertsql = "insert into zl_req_patInfo ( exam_info_id,zl_pat_id,exam_num,"
													+ "zl_mzh,zl_tjh,flag) values('" + exam_id + "','" + PATIENT_ID
													+ "','" + exam_num + "','" + CLINIC_NO + "','" + retexam_num
													+ "','0')";
											tjtmpconnect.createStatement().executeUpdate(insertsql);
										}
										rs1.close();
										List<CustomResBean> LIST = new ArrayList<CustomResBean>();
										CustomResBean cr = new CustomResBean();
										cr.setCLINIC_NO(CLINIC_NO);
										cr.setPATIENT_ID(PATIENT_ID);
										cr.setVISIT_DATE(VISIT_DATE);
										cr.setVISIT_NO(VISIT_NO);
										LIST.add(cr);
										rb.setCustom(LIST);
										rb.getResultHeader().setTypeCode("AA");
										rb.getResultHeader().setText("交易成功");
										connect.commit();
									} catch (SQLException ex) {
										connect.rollback();
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
							} else {
								connect.rollback();
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText("pacs调用Zl_NewPatient错误返回标志错误-" + RESULT_CODE);
							}

						} catch (Exception ex) {
							connect.rollback();
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("链接pacs数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
							TranLogTxt.liswriteEror_to_txt(logname, "res:Zl_NewPatient-" + exam_id + "-" + exam_num
									+ ":" +com.hjw.interfaces.util.StringUtil.formatException(ex));
						} finally {
							try {
								if (connect != null) {
									OracleDatabaseSource.close(connect);
								}
							} catch (Exception sqle4) {
								sqle4.printStackTrace();
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("解析链接串错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
						TranLogTxt.liswriteEror_to_txt(logname, "res:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("链接体检数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				TranLogTxt.liswriteEror_to_txt(logname, "res:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			} finally {
				try {
					if (tjconnect != null) {
						tjconnect.close();
					}
				} catch (SQLException sqle4) {
					sqle4.printStackTrace();
				}
			}
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + xml);
		return rb;
	}

}
