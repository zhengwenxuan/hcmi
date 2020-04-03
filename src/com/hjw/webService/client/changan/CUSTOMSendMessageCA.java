package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONSerializer;

public class CUSTOMSendMessageCA {

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
	
	public CUSTOMSendMessageCA(Custom custom){
		this.custom=custom;
	}
	
	public ResultBody getMessage(String url, String logname) {
		ResultBody rb = new ResultBody();
		String exam_num = this.custom.getEXAM_NUM();
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			Connection his_connect = null;
			try {
				TranLogTxt.liswriteEror_to_txt(logname, "url:"+url);
				String dburl = url.split("&")[0];
				String user = url.split("&")[1];
				String passwd = url.split("&")[2];
				his_connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
//				his_connect.setAutoCommit(false);
				
				if(this.custom.getDATE_OF_BIRTH() != null && this.custom.getDATE_OF_BIRTH().length()>10) {
					this.custom.setDATE_OF_BIRTH(this.custom.getDATE_OF_BIRTH().substring(0, 10));
				}
				
				try {
					//1,写入：3.9病人主索引  PAT_MASTER_INDEX
					String select_PAT_MASTER_INDEX_sql = "select PATIENT_ID from PAT_MASTER_INDEX where PATIENT_ID='"+this.custom.getEXAM_NUM()+"'";
					ResultSet rs1 = his_connect.createStatement().executeQuery(select_PAT_MASTER_INDEX_sql);
					if (rs1.next()) {
						String update_PAT_MASTER_INDEX_sql = "update PAT_MASTER_INDEX set NAME='" + this.custom.getNAME()
				        + "', mailing_address='" + this.custom.getMAILING_ADDRESS()
				        + "', SEX='" + this.custom.getSEX()
				        + "', CHARGE_TYPE='" + this.custom.getCHARGE_TYPE()
				        //+ "', Identity=" + this.custom.getIDENTITY()
				        + "' where PATIENT_ID='"+this.custom.getEXAM_NUM()+"'";
						TranLogTxt.liswriteEror_to_txt(logname, "1,更新：3.9病人主索引  PAT_MASTER_INDEX: " +update_PAT_MASTER_INDEX_sql);
						his_connect.createStatement().executeUpdate(update_PAT_MASTER_INDEX_sql);
					} else {
						String insert_PAT_MASTER_INDEX_sql = "insert into PAT_MASTER_INDEX(PATIENT_ID, NAME, Mailing_Address, create_date, SEX,"
								+ " OPERATOR, DATE_OF_BIRTH,  CHARGE_TYPE) values "
								+ " ('"+this.custom.getEXAM_NUM()+"','"+this.custom.getNAME()+"','"+this.custom.getMAILING_ADDRESS()+"',sysdate,'"+this.custom.getSEX()+"',"
								+ " '"+this.custom.getOPERATOR()+"',to_date('"+this.custom.getDATE_OF_BIRTH()+"', 'yyyy-mm-dd'),'自费')";
						TranLogTxt.liswriteEror_to_txt(logname, "1,写入：3.9病人主索引  PAT_MASTER_INDEX: " +insert_PAT_MASTER_INDEX_sql);
						his_connect.createStatement().executeUpdate(insert_PAT_MASTER_INDEX_sql);
					}
					rs1.close();
					
					ExamInfoUserDTO ei = configService.getExamInfoForNum(this.custom.getEXAM_NUM());
					if("G".equals(ei.getExam_type()) && !StringUtil.isEmpty(ei.getCompany()) && ei.getCompany().length()>3) {
						ei.setUser_name(ei.getCompany());
					}
					
					//2,更新：3.10就诊记录  CLINIC_MASTER
					String insert_CLINIC_MASTER_sql = "update CLINIC_MASTER set NAME = '"+ei.getUser_name()+"',SEX = '"+this.custom.getSEX()+"' where PATIENT_ID = '"+exam_num+"' ";
					TranLogTxt.liswriteEror_to_txt(logname, "2,写入：3.10就诊记录  CLINIC_MASTER: " +insert_CLINIC_MASTER_sql);
					Statement statement = his_connect.createStatement();
					statement.executeUpdate(insert_CLINIC_MASTER_sql);
					statement.close();
					
//					his_connect.commit();
					List<CustomResBean> LIST = new ArrayList<CustomResBean>();
					CustomResBean cr = new CustomResBean();
//					String PATIENT_ID = this.custom.getEXAM_NUM();
//					String CLINIC_NO = this.custom.getEXAM_NUM();
//					String VISIT_NO = this.custom.getEXAM_NUM();
//					cr.setCLINIC_NO(CLINIC_NO);
//					cr.setPATIENT_ID(PATIENT_ID);
//					cr.setVISIT_DATE();
//					cr.setVISIT_NO();
					LIST.add(cr);
					
					ControlActProcess ca = new ControlActProcess();
					ca.setLIST(LIST);
					rb.setControlActProcess(ca);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("交易成功");
				} catch (Exception ex) {
//					his_connect.rollback();
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("链接his数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				} finally {
					try {
						if (his_connect != null) {
							OracleDatabaseSource.close(his_connect);
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
			} finally {
				try {
					if (his_connect != null) {
						his_connect.close();
					}
				} catch (SQLException sqle4) {
					sqle4.printStackTrace();
				}
			}
		}
		String jsonString = JSONSerializer.toJSON(rb).toString();
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + jsonString);
		return rb;
	}
}
