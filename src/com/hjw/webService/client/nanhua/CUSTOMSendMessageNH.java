package com.hjw.webService.client.nanhua;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.DB2DatabaseSource;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
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
public class CUSTOMSendMessageNH {

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
	
	public CUSTOMSendMessageNH(Custom custom){
		this.custom=custom;
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultBody getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname,"-----------------------南华附一修改病人信息接口-----------------------");
		ResultBody rb = new ResultBody();
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
						TranLogTxt.liswriteEror_to_txt(logname,url);
						connect = DB2DatabaseSource.getConnection(dburl, user, passwd);
						connect.setAutoCommit(false);

						try {
							/*
							 * CREATE PROCEDURE "DB2INST1"."TJ_UPDATE_INFP"
								 (IN "TJ_NO" VARCHAR(20), 
								  IN "V_VALUE" VARCHAR(20), 
								  IN "FLAG" VARCHAR(20)
								 ) 
							 * --参数：TJ_NO:体检号,V_VALUE:数据值（姓名，性别，生日）,FLAG:字段名（'hzxm','sex','birth'）
							 * 
							 */
							TranLogTxt.liswriteEror_to_txt(logname,custom.getNAME()+","+custom.getSEX()+","+custom.getDATE_OF_BIRTH());
							if(!StringUtil.isEmpty(custom.getNAME())) {
								TranLogTxt.liswriteEror_to_txt(logname,
										"req:" + exam_id + "-" + exam_num + ":1、调用存储过程  TJ_UPDATE_INFO");
								CallableStatement c = connect.prepareCall("{call DB2INST1.TJ_UPDATE_INFO(?,?,?)}");
								TranLogTxt.liswriteEror_to_txt(logname, "TJ_UPDATE_INFO-" + exam_num + "-TJ_UPDATE_INFO('"+this.custom.getEXAM_NUM()+"','"+this.custom.getNAME()+"','hzxm')");
								c.setString(1, exam_num);
								c.setString(2, this.custom.getNAME());
								c.setString(3, "hzxm");
								c.execute();
								c.close();
							}
							if(!StringUtil.isEmpty(custom.getSEX())) {
								TranLogTxt.liswriteEror_to_txt(logname,
										"req:" + exam_id + "-" + exam_num + ":1、调用存储过程  TJ_UPDATE_INFO");
								CallableStatement c = connect.prepareCall("{call DB2INST1.TJ_UPDATE_INFO(?,?,?)}");
								TranLogTxt.liswriteEror_to_txt(logname, "TJ_UPDATE_INFO-" + exam_num + "-TJ_UPDATE_INFO('"+this.custom.getEXAM_NUM()+"','"+this.custom.getSEX()+"','hzxm')");
								c.setString(1, exam_num);
								c.setString(2, this.custom.getSEX());
								c.setString(3, "sex");
								c.execute();
								c.close();
							}
							if(!StringUtil.isEmpty(custom.getDATE_OF_BIRTH())) {
								TranLogTxt.liswriteEror_to_txt(logname,
										"req:" + exam_id + "-" + exam_num + ":1、调用存储过程  TJ_UPDATE_INFO");
								CallableStatement c = connect.prepareCall("{call DB2INST1.TJ_UPDATE_INFO(?,?,?)}");
								TranLogTxt.liswriteEror_to_txt(logname, "TJ_UPDATE_INFO-" + exam_num + "-TJ_UPDATE_INFO('"+this.custom.getEXAM_NUM()+"','"+this.custom.getDATE_OF_BIRTH()+" 00:00:00','hzxm')");
								c.setString(1, exam_num);
								c.setString(2, this.custom.getDATE_OF_BIRTH()+" 00:00:00");
								c.setString(3, "birth");
								c.execute();
								c.close();
							}

							rb.getResultHeader().setTypeCode("AA");
							rb.getResultHeader().setText("交易成功");
							connect.commit();

						} catch (Exception ex) {
							connect.rollback();
							rb.getResultHeader().setTypeCode("AA");
							rb.getResultHeader().setText("交易成功");
							TranLogTxt.liswriteEror_to_txt(logname, "res:TJ_UPDATE_INFO-" + exam_id + "-" + exam_num
									+ ":" +com.hjw.interfaces.util.StringUtil.formatException(ex));
						} finally {
							try {
								if (connect != null) {
									DB2DatabaseSource.close(connect);
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
}
