package com.hjw.webService.client.hghis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.wsdl.Types;

import oracle.jdbc.OracleTypes;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.MzghBaseInfoDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class JiuzhenkaInfo {
	private static JdbcQueryManager jdbcQueryManager;
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	/**
	 * 获取就诊卡人员信息
	 * @param card
	 * @param url
	 * @param logname
	 * @return
	 */
	public MzghBaseInfoDTO  getCardInfo(String card,String url,String logname){
		Connection orecal_connect = null;
		Connection peis_connect = null;
		Statement statement = null;
	    MzghBaseInfoDTO m_li = new MzghBaseInfoDTO();
		try {
			System.out.println("获取配置");
			TranLogTxt.liswriteEror_to_txt(logname, "req:开始读取就诊卡信息");
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			System.out.println("配置"+dburl);
			System.out.println("配置"+user);
			System.out.println("配置"+passwd);
			orecal_connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			peis_connect = jdbcQueryManager.getConnection();
			//IDENTITYNO   --IDCARDINFO
			String sql = "SELECT OUTPATIENTNO,NAME,SEX,BIRTH,AGE,ADDRESS,LXFS,IDENTITYNO FROM   mzgh_baseinfo WHERE   OUTPATIENTNO="+card;// 预编译语句，“？”代表参数
			TranLogTxt.liswriteEror_to_txt(logname, "req:开始读取就诊卡信息"+sql);
			statement = orecal_connect.createStatement();
			ResultSet result = statement.executeQuery(sql);
					while (result.next()){
						 
				         StringBuffer  ou = new StringBuffer();
				         ou.append("就诊卡号"+result.getLong("OUTPATIENTNO"));
				         ou.append("姓名"+result.getString("NAME"));
				         ou.append("性别"+result.getInt("SEX"));
				         ou.append("出生日期"+result.getLong("BIRTH"));
				         ou.append("年龄"+result.getInt("AGE"));
				         ou.append("地址"+result.getString("ADDRESS"));
				         ou.append("电话"+result.getString("LXFS"));
				         ou.append("身份证"+result.getString("IDENTITYNO"));
				         System.out.println(ou.toString());
				         
					     m_li.setOUTPATIENTNO(result.getLong("OUTPATIENTNO"));
					     m_li.setNAME(result.getString("NAME"));
					     m_li.setSEX(result.getInt("SEX"));
					     m_li.setAGE(result.getInt("AGE"));
					     m_li.setADDRESS(result.getString("ADDRESS"));
					     m_li.setLXFS(result.getString("LXFS"));
					     m_li.setIDCARDINFO(result.getString("IDENTITYNO"));
					     if(m_li.getIDCARDINFO()!=null && !"".equals(m_li.getIDCARDINFO())){
						    	String idnum = m_li.getIDCARDINFO();
						    	StringBuffer st = new StringBuffer();
					    	 	
					    	 	st.append(idnum.substring(6, 10)+"-");
					    	 	st.append(idnum.substring(10, 12)+"-");
					    	 	st.append(idnum.substring(12,14));
					    	 	
					    	 	 m_li.setBIRTH(st.toString());
					     } else {
					    	String birth =   DateTimeUtil.getDateByAge(m_li.getAGE());
					    	 m_li.setBIRTH(birth);
					     }
					 	TranLogTxt.liswriteEror_to_txt(logname, "就诊卡信息:" +ou.toString());
					}
			}catch (Exception e) {
				e.printStackTrace();
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +"连接数据库失败"+com.hjw.interfaces.util.StringUtil.formatException(e));
			} finally {
				try {
					if (statement != null){
						statement.close();
					}
					if (orecal_connect != null) {
						OracleDatabaseSource.close(orecal_connect);
					}
					if (peis_connect != null){
						peis_connect.close();
					}
				} catch (Exception sqle4) {
					sqle4.printStackTrace();
				}
			}
			return m_li;
		}
	/*
	 * 给his发送就诊卡人员信息进行绑定
	 */
	public String setHisCardInfo(String url,String logname,ExamInfoDTO info){
		Connection orecal_connect = null;
		CallableStatement castm = null;
		String ou = "";
	    String log = "";
			try {
					TranLogTxt.liswriteEror_to_txt(logname+2, "req:发给his发送就诊卡人员信息--------------------");
					String dburl = url.split("&")[0];
					String user = url.split("&")[1];
					String passwd = url.split("&")[2];
					orecal_connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
					String sql = "  call RegisterCard(?,?,?,?,?,?,?,?,?,?)";
					castm = orecal_connect.prepareCall(sql);
					log += " req:地址:"+dburl+"\r\n";
					log += "req:sql:"+sql+"\r\n";
					log += "req:参数2:"+info.toString()+"\r\n";
					TranLogTxt.liswriteEror_to_txt(logname+2,log);
					System.out.println("1");
					
					
					castm.setLong(1,info.getaOutpatientNo());	//卡号AOutpatientNo  
					castm.setString(2,info.getaPatName());	//姓名APatName 
					castm.setLong(3,info.getaSex());		//ASex number,--性别 1男 2女  必填
					castm.setDate(4,new java.sql.Date(info.getaBirthday().getTime()));
					castm.setString(5,info.getaAddres());		//varchar2,--地址 必填
					castm.setString(6,info.getaIDCard());		//AIDCard varchar2,--身份证号
					castm.setString(7,info.getaTel());		//ATel  varchar2,--电话
					castm.setLong(8,info.getaOperator());						//AOperator number,--登记人
					castm.setDouble(9,info.getaPrice());						//卡价格填写为0
					castm.registerOutParameter(10,OracleTypes.VARCHAR);  
					castm.execute();
					ou = castm.getString(10);
					log = "res:"+ou+"\r\n";
					TranLogTxt.liswriteEror_to_txt(logname+2,log);
			}catch (Exception e) {
				e.printStackTrace();
				TranLogTxt.liswriteEror_to_txt(logname+2, "res:" +"错误---"+com.hjw.interfaces.util.StringUtil.formatException(e));
			} finally {
				try {
					if (castm != null) {
						castm.close();
					}
					if(orecal_connect!=null){
						orecal_connect.close();
					}
				} catch (Exception sqle4) {
					sqle4.printStackTrace();
				}
			}
		return ou;
	}
}
