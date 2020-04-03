package com.hjw.webService.client.fangzheng;

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
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.ResultBody;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.14	挂号信息服务  中联对接
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CUSTOMSendMessageFZ {
	private Custom custom=new Custom();
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public CUSTOMSendMessageFZ(Custom custom){
		this.custom=custom;
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultBody getMessage(String url, String logname) {
		ResultBody rb = new ResultBody();
		String exam_num = this.custom.getEXAM_NUM();
		long exam_id = 0;
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			Connection tjconnect = null;
			try {
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
						String table = url.split("&")[3];
						connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
						try {
							String RESULT_CODE=getSearchPerid(url,logname);
							if ((StringUtil.isEmpty(RESULT_CODE)) || (RESULT_CODE.split("\\|").length != 2)) {
								if((this.custom.getDATE_OF_BIRTH()!=null)&&(this.custom.getDATE_OF_BIRTH().length()>10)){
									this.custom.setDATE_OF_BIRTH(this.custom.getDATE_OF_BIRTH().substring(0, 10));
								}
							TranLogTxt.liswriteEror_to_txt(logname,
									"req:" + exam_id + "-" + exam_num + ":1、调用存储过程  Zl_tj_患者建档");
							CallableStatement c = connect.prepareCall("{call "+table+".zl_hjwtj_患者建档(?,?,?,?,?,?,?,?,?,?,?,?)}");
							TranLogTxt.liswriteEror_to_txt(logname, "zl_hjwtj_患者建档-" + exam_id + "-"+table+".zl_hjwtj_患者建档('"+this.custom.getID_NO()+"','"+this.custom.getNAME()+"','"
							+this.custom.getSEX()+"','"
									+this.custom.getDATE_OF_BIRTH()+"','','','"+this.custom.getNEXT_OF_KIN()+"','"
											+this.custom.getNEXT_OF_KIN_PHONE()+"','"+this.custom.getMAILING_ADDRESS()+"','','"+this.custom.getOPERATOR()+"'"
													+ ",?)");
							c.setString(1, this.custom.getID_NO());
							c.setString(2, this.custom.getNAME());
							c.setString(3, this.custom.getSEX());
							java.sql.Date buydate=java.sql.Date.valueOf(this.custom.getDATE_OF_BIRTH());
							c.setDate(4, buydate);
							c.setString(5, "");
							c.setString(6, "普通");//费别
							c.setString(7, this.custom.getNEXT_OF_KIN());
							c.setString(8, this.custom.getNEXT_OF_KIN_PHONE());
							c.setString(9, this.custom.getMAILING_ADDRESS());
							c.setString(10, "");
							c.setString(11, this.custom.getOPERATOR());							
							c.registerOutParameter(12, java.sql.Types.VARCHAR);							
							// 执行存储过程
							c.execute();
							// 得到存储过程的输出参数值
							RESULT_CODE = c.getString(12);
							c.close();					
							TranLogTxt.liswriteEror_to_txt(logname, "zl_hjwtj_患者建档-" + exam_id + "-" + exam_num
									+ ":" + RESULT_CODE );		
							}
							if ((StringUtil.isEmpty(RESULT_CODE)) || (RESULT_CODE.split("\\|").length != 2)) {
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText("创建档案错误返回-" +  RESULT_CODE);
							}else{
								String PATIENT_ID = RESULT_CODE.split("\\|")[0];
								String clinic_no  = RESULT_CODE.split("\\|")[1]; //门诊号
							TranLogTxt.liswriteEror_to_txt(logname,
									"req:" + exam_id + "-" + exam_num + ":2、调用存储过程  zl_hjwtj_患者挂号");
							CallableStatement c = connect.prepareCall("{call "+table+".zl_hjwtj_患者挂号(?,?,?,?)}");
							TranLogTxt.liswriteEror_to_txt(logname, "zl_hjwtj_患者挂号-" + exam_id + "-zl_hjwtj_患者挂号('','"+PATIENT_ID+"','870',?)");
							c.setString(1, "");
							c.setFloat(2, Float.valueOf(PATIENT_ID));
							c.setString(3, "870");								
							c.registerOutParameter(4, java.sql.Types.VARCHAR);							
							// 执行存储过程
							c.execute();
							// 得到存储过程的输出参数值
							String strReturn = c.getString(4);
							c.close();
							TranLogTxt.liswriteEror_to_txt(logname, "zl_hjwtj_患者挂号-" + exam_id + "-" + exam_num
									+ ":" + strReturn );
								if ((StringUtil.isEmpty(strReturn)) || (strReturn.split("\\|").length != 2)) {
									rb.getResultHeader().setTypeCode("AE");
									rb.getResultHeader().setText("挂号错误返回参数错误-" +  strReturn);
								} else {
									
									String retexam_num = strReturn.split("\\|")[1];
									String sjdh=strReturn.split("\\|")[0];//收据单号
									String visit_no = retexam_num;//就诊号或挂号id
									String VISIT_DATE = DateTimeUtil.getDate2();
									Connection tjtmpconnect = null;
									try {
										tjtmpconnect = this.jdbcQueryManager.getConnection();
										String sb1 = "select exam_info_id,zl_pat_id,exam_num,"
												+ "zl_mzh,zl_tjh,flag from zl_req_patInfo where exam_info_id='"
												+ exam_id + "'";
										ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
										if (rs1.next()) {
											String updatesql = "update zl_req_patInfo set zl_pat_id='" + PATIENT_ID
													+ "',zl_mzh='" + clinic_no + "',zl_tjh='" + visit_no
													+ "',zl_djh='"+sjdh+"' where  exam_info_id='" + exam_id + "'";
											tjtmpconnect.createStatement().executeUpdate(updatesql);
										} else {
											String insertsql = "insert into zl_req_patInfo ( exam_info_id,zl_pat_id,exam_num,"
													+ "zl_mzh,zl_tjh,zl_djh,flag) values('" + exam_id + "','" + PATIENT_ID
													+ "','" + exam_num + "','" + clinic_no + "','" + visit_no
													+ "','"+sjdh+"','0')";
											tjtmpconnect.createStatement().executeUpdate(insertsql);
										}
										rs1.close();
										List<CustomResBean> LIST = new ArrayList<CustomResBean>();
										CustomResBean cr = new CustomResBean();
										cr.setCLINIC_NO(clinic_no);
										cr.setPATIENT_ID(PATIENT_ID);
										cr.setVISIT_DATE(VISIT_DATE);
										cr.setVISIT_NO(visit_no);
										LIST.add(cr);

										ControlActProcess ca = new ControlActProcess();
										ca.setLIST(LIST);
										rb.setControlActProcess(ca);
										rb.getResultHeader().setTypeCode("AA");
										rb.getResultHeader().setText("交易成功");
										//connect.commit();
									} catch (SQLException ex) {
										//connect.rollback();
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

						} catch (Exception ex) {
							//connect.rollback();
							rb.getResultHeader().setTypeCode("AE");							
							rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		return rb;
	}
	
	/**
	 * 获取处方号
	 * @param url
	 * @param logname
	 * @return
	 */
	public String getSearchPerid(String url, String logname) {
		String cfh = "";
		Connection connect = null;
		try {
			ResultSet rs = null; 
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			String table = url.split("&")[3];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_hjwtj_查询患者");
			CallableStatement c = connect.prepareCall("{call "+table+".zl_hjwtj_查询患者(?,?,?,?,?)}");
			TranLogTxt.liswriteEror_to_txt(logname, "zl_hjwtj_查询患者('"+this.custom.getID_NO()+"','"
			+this.custom.getNAME()+"','"+this.custom.getSEX()+"','"+this.custom.getDATE_OF_BIRTH()+"',?)");
			c.setString(1, this.custom.getID_NO());
			c.setString(2, this.custom.getNAME());
			c.setString(3, this.custom.getSEX());
			java.sql.Date buydate=java.sql.Date.valueOf(this.custom.getDATE_OF_BIRTH());
			c.setDate(4, buydate);
			c.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
			// 执行存储过程
			c.execute();
			 //取的结果集的方式二：
              rs = (ResultSet) c.getObject(5);
              if(rs.next()){
            	  cfh=rs.getString("病人ID")+"|"+rs.getString("门诊号");
              }
              rs.close();
			 c.close();
		} catch (Exception ex) {

			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return cfh;
	}
	
}
