package com.hjw.webService.client.RADinfo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.Bean.Person;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.TreeDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 2.6 检查申请信息服务 天健 平台对接-东北国际
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class PACSSendMessageZJLD {
	private PacsMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public PACSSendMessageZJLD(PacsMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String convalue, String logname) {
		JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
		ResultPacsBody rb = new ResultPacsBody();
		String[] sqlurl = url.split("&");// pacs 数据库 sqlserver
		String[] oracleurl = convalue.split("&"); // oracle 配置 his数据库
		boolean perflag = updateHisPersion(this.lismessage.getCustom(), oracleurl, logname);
		if (perflag) {
			Connection connect = null;
			try {
				String dburl = sqlurl[0];
				String user = sqlurl[1];
				String passwd = sqlurl[2];
				connect = SqlServerDatabaseSource.getConnection(dburl, user, passwd);
				List<ApplyNOBean> appList = new ArrayList<ApplyNOBean>();
				for (PacsComponents pcs : lismessage.getComponents()) {
					for (PacsComponent pc : pcs.getPacsComponent()) {
						try {
						String exam_num = this.lismessage.getCustom().getExam_num();
						String arch_num = this.lismessage.getCustom().getArch_num();
						String charging_item_num = pc.getItemCode();
						String charging_item_name = pc.getItemName();
						long pacsSummaryId = getPacsSummaryId(exam_num,charging_item_num,logname);
						String item_pacs_code = pc.getPacs_num();// xs.getString('item_pacs_code');
						double price = 0;// xs.getString('price');
						double charge = 0;// xs.getString('charge');
						String cur_barCode = GetPacsNoHead(sqlurl, item_pacs_code, logname);
						cur_barCode = cur_barCode + "" + getCode(sqlurl,"pacs",logname);
						String cur_lsh = GetLsh(sqlurl, logname);
						TreeDTO tr = new TreeDTO();
						tr = GetJCXMDM(sqlurl, item_pacs_code, logname);
					
						String studyID = GetStudyID(sqlurl, logname);
						
							StringBuffer strSQL = new StringBuffer(
									"insert into HisJCRW(sqdh, lsh, jch, djxh, bah, jzkh, brxm, brxb, brnl, csrq, jtzz, ylxh, ylmc, sjysgh, sjysxm, sjksmc, "); // bqmc,
							strSQL.append("sjsj, djysgh, djysxm, djrq, jclxdm, jcbwdm, status, studyuid) values(");
							strSQL.append("'" + cur_barCode + "','" + cur_lsh + "','" + cur_barCode + "',");
							strSQL.append("'" + exam_num + "','" + arch_num + "','" + arch_num + "',");
							strSQL.append("'" + this.lismessage.getCustom().getName() + "','" + this.lismessage.getCustom().getSexcode() + "',");
							strSQL.append("'" + this.lismessage.getCustom().getOld() + "','"
									+ this.lismessage.getCustom().getBirthtime() + "',");
							strSQL.append(
									"'" + this.lismessage.getCustom().getAddress() + "','" + item_pacs_code + "',");
							strSQL.append("'" + charging_item_name + "','64',");
							strSQL.append(
									"'" + this.lismessage.getDoctor().getDoctorName() + "','体检中心', getdate(),");
							strSQL.append("'64+ ','" + this.lismessage.getDoctor().getDoctorName()
									+ "', convert(varchar(10), getdate(),20),");
							strSQL.append("'" + tr.getId() + "','"+tr.getText()+ "','登记','" + studyID + "')");
							TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL.toString());
							connect.createStatement().execute(strSQL.toString());
							
							String insertsql="insert into HisRWZL(jcrwlsh, rwzllx, rwzlsj) values('"+ cur_lsh + "', 1, getdate())";
							TranLogTxt.liswriteEror_to_txt(logname, "res:" + insertsql);
							connect.createStatement().execute(insertsql);
							insertPacsReq(lismessage.getCustom(),pc,pacsSummaryId,cur_barCode,logname);
							ApplyNOBean ab = new ApplyNOBean();
							ab.setApplyNO(pcs.getReq_no());
							appList.add(ab);
						} catch (Exception ex) {
							TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
						}
					}
				}
				rb.getResultHeader().setTypeCode("AA");
				rb.getControlActProcess().setList(appList);
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			} finally {
				try {
					if (connect != null) {
						OracleDatabaseSource.close(connect);
					}
				} catch (Exception sqle4) {
					sqle4.printStackTrace();
				}
			}

		} else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("插入或者更新HisPersion失败");
		}

		json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
		return rb;
	}

	/**
	 * 
	 * @param ps
	 * @param url
	 * @param logname
	 * @return
	 */
	private boolean updateHisPersion(Person ps, String[] url, String logname) {
		Connection connect = null;
		boolean flag = false;
		try {
			String dburl = url[0];
			String user = url[1];
			String passwd = url[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			String sql = "select * from PAT_MASTER_INDEX where PATIENT_ID='" + ps.getArch_num() + "'";
			ResultSet rs = connect.createStatement().executeQuery(sql);
			if (rs.next()) {
				String strSQL = "update PAT_MASTER_INDEX set name='" + ps.getName() + "',sex='" + ps.getSexname()
						+ "',DATE_OF_BIRTH=to_date('"+ ps.getBirthtime() + "','yyyymmdd') where PATIENT_ID='" + ps.getArch_num() + "'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL);
				connect.createStatement().execute(strSQL);
			} else {
				TranLogTxt.liswriteEror_to_txt(logname, "res:执行插入");
				String strSQL = "insert into PAT_MASTER_INDEX(PATIENT_ID, NAME, NAME_PHONETIC, SEX, DATE_OF_BIRTH,CITIZENSHIP,NATION,MAILING_ADDRESS,VIP_INDICATOR) values('"
						+ ps.getArch_num() + "','" + ps.getName() + "','','" + ps.getSexname() + "',to_date('"+ ps.getBirthtime() + "','yyyymmdd'),'CN', '汉族', '', 2)";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL);
				connect.createStatement().execute(strSQL);
			}
			flag = true;
			rs.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			flag = false;
		} finally {
			try {
				if (connect != null) {
					OracleDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param ps
	 * @param pc
	 * @param pacsSummaryId
	 * @param cur_barCode
	 * @param logname
	 * @return
	 */
	private boolean insertPacsReq(Person ps,PacsComponent pc,long pacsSummaryId,String cur_barCode,String logname) {		
		boolean flag = false;
		Connection connect = null;
		try {
			connect = this.jdbcQueryManager.getConnection();
				String strSQL = "insert into PacsReq(exam_num, arch_num, summary_id, charging_item_id, charging_item_name,"
						+ "charging_item_num, pacs_item_code, pacs_jch, read_flag) values('"
          + ps.getExam_num() +"','"+ps.getArch_num()+"','"+pacsSummaryId+"','"+pc.getItemId()+"','"+pc.getItemName() 
          + "','"+pc.getItemCode()+"','"+pc.getPacs_num() + "','"+cur_barCode+ "', 0)";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL);
				connect.createStatement().execute(strSQL);
			flag = true;
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			flag = false;
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param exam_num
	 * @param chargingcode
	 * @param logname
	 * @return
	 */
	private long getPacsSummaryId(String exam_num,String chargingcode,String logname){
    	Connection tjtmpconnect = null;
    	long pacs_summary_id=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.id,a.pacs_req_code,b.dep_num from pacs_summary a,pacs_detail b "
					+ "where a.examinfo_sampleId=b.examinfo_sampleId "
					+ "and a.examinfo_num='"+exam_num+"' and a.id=b.summary_id "
					+ "and b.chargingItem_num='"+chargingcode+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				pacs_summary_id=rs1.getLong("id");
			} 
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return pacs_summary_id;
    }


	/**
	 * // 根据PACS项目代码获取检查号前缀
	 * 
	 * @param mssqlurl
	 * @param item_id
	 * @return
	 */
	private String GetPacsNoHead(String mssqlurl[], String item_pacs_code, String logname) {
		String jchqz = "";
		Connection connect = null;
		try {
			String dburl = mssqlurl[0];
			String user = mssqlurl[1];
			String passwd = mssqlurl[2];
			connect = SqlServerDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + dburl+","+ user+","+ passwd);
			String strSQL = "select A.ylxh, A.ylmc, B.jclxdm, B.jclxmc, B.jchqz from HisJCXMDM A, HisJCLX B "
					+ "where A.jclxdm=B.jclxdm and A.ylxh='" + item_pacs_code + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL);
			ResultSet rs = connect.createStatement().executeQuery(strSQL);
			if (rs.next()) {
				jchqz = rs.getString("jchqz");
			}
			rs.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					SqlServerDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return jchqz;
	}

	private String GetStudyID(String mssqlurl[], String logname) {
		String studyId = "";
		Connection connect = null;
		try {
			String dburl = mssqlurl[0];
			String user = mssqlurl[1];
			String passwd = mssqlurl[2];
			connect = SqlServerDatabaseSource.getConnection(dburl, user, passwd);
			String strSQL = "select CONVERT(varchar(8), GETDATE(), 112) as sysdate";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL);
			ResultSet rs = connect.createStatement().executeQuery(strSQL);
			if (rs.next()) {
				String str=rs.getString("sysdate");
				studyId = str + "9" + getCode(mssqlurl,"studyid",logname);
			}
			rs.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					SqlServerDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return studyId;
	}
	
	private String getCode(String mssqlurl[],String types, String logname){
		Connection connect = null;
		String code="";
		try {
			String dburl = mssqlurl[0];
			String user = mssqlurl[1];
			String passwd = mssqlurl[2];
			connect = SqlServerDatabaseSource.getConnection(dburl, user, passwd);
			StringBuffer sb = new StringBuffer();
			sb.append("  declare @res int ");
			sb.append("exec P_GetSysParam_1 '" + types+ "', @res output  ");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
				CallableStatement c = connect.prepareCall("{call P_GetSysParam_1(?,?)}");
				c.setString(1, types);				
				c.registerOutParameter(2, java.sql.Types.VARCHAR);
				// 执行存储过程啊闪光灯
				c.execute();
				// 得到存储过程的输出参数值
				code = c.getString(2);
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					SqlServerDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return code;
	}

	/**
	 * // 根据PACS项目代码获取检查号前缀
	 * 
	 * @param mssqlurl
	 * @param item_id
	 * @return
	 */
	private TreeDTO GetJCXMDM(String mssqlurl[], String item_pacs_code, String logname) {
		TreeDTO tr = new TreeDTO();
		Connection connect = null;
		try {
			String dburl = mssqlurl[0];
			String user = mssqlurl[1];
			String passwd = mssqlurl[2];
			connect = SqlServerDatabaseSource.getConnection(dburl, user, passwd);
			String strSQL = "select jclxdm, jcbwdm from HisJCXMDM where ylxh='" + item_pacs_code + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL);
			ResultSet rs = connect.createStatement().executeQuery(strSQL);
			if (rs.next()) {
				String jclxdm = rs.getString("jclxdm");
				if((jclxdm!=null)&&(jclxdm.length()>0)){
					jclxdm=jclxdm.trim();
				}
				String jcbwdm = rs.getString("jcbwdm");
				if((jcbwdm!=null)&&(jcbwdm.length()>0)){
					jcbwdm=jcbwdm.trim();
				}
				tr.setText(jcbwdm);
				tr.setId(jclxdm);
			}
			rs.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					SqlServerDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return tr;
	}

	/**
	 * 
	 * @param mssqlurl
	 * @param item_id
	 * @param logname
	 * @return
	 */
	private String GetLsh(String mssqlurl[], String logname) {
		String lsh = "";
		Connection connect = null;
		try {
			String dburl = mssqlurl[0];
			String user = mssqlurl[1];
			String passwd = mssqlurl[2];
			connect = SqlServerDatabaseSource.getConnection(dburl, user, passwd);
			String strSQL = "select max(lsh)+1 lsh from HisJCRW";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + strSQL);
			ResultSet rs = connect.createStatement().executeQuery(strSQL);
			if (rs.next()) {
				lsh = rs.getString("lsh");
			}
			rs.close();
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					SqlServerDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lsh;
	}

	public static String parse5(String param) {
		Date date = new Date();
		if ((param != null) && (param.trim().length() == 8)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setLenient(false);
			try {
				date = sdf.parse(param);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
}
