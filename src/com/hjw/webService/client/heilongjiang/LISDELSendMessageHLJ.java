package com.hjw.webService.client.heilongjiang;

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
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class LISDELSendMessageHLJ {
	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public LISDELSendMessageHLJ(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		ResultLisBody rb = new ResultLisBody();
		JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		String exam_num = this.lismessage.getCustom().getExam_num();
		long exam_id = 0;
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			Connection connect = null;
			try {
				String dburl = url.split("&")[0];
				String user = url.split("&")[1];
				String passwd = url.split("&")[2];
				connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + url);
				List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
				for (LisComponents pcs : lismessage.getComponents()) {
					boolean pacsflags = true;
					connect.setAutoCommit(false);
					ApplyNOBean an = new ApplyNOBean();
					for (LisComponent pc : pcs.getItemList()) {
						try {
							/*
							 * 名称     lis_request_delete(                                   
							 * 			1		  n_test_no     in varchar2,----条码号
                                        2         exam_num      in varchar2,----体检号
                                        3         pat_name      in varchar2,-----姓名
                                        4         n_item_no     in NUMBER,---项目序号
                                        5         n_item_code in varchar2,------关联码
                                        6         n_item_name   in varchar2,-----项目名称
                                        7         result_code   out integer,------ - 变量 空
                                        8         error_message out varchar2 ------变量 空 异常信息返回


							 */
							int item_seq = getitemSeq(pc.getChargingItemid());
							CallableStatement c = connect
									.prepareCall("{call lis_request_delete(?,?,?,?,?,?,?,?)}");
							TranLogTxt.liswriteEror_to_txt(logname, "res:执行存储过程" + exam_id + "-lis_request_delete('"
									+ pcs.getReq_no() + "','" + lismessage.getCustom().getExam_num() + "','"
									+ lismessage.getCustom().getName() + "','"+item_seq+"',"
									+ "'" + pc.getItemCode() + "','" + pc.getItemName() + "',?,?)");
							c.setString(1, pcs.getReq_no());// 申请单号
							c.setString(2, lismessage.getCustom().getExam_num());// 体检号
							c.setString(3, lismessage.getCustom().getName());// 患者姓名
							c.setInt(4, item_seq);// 项目序号
							c.setString(5, pc.getItemCode());// 项目代码--关联码
							c.setString(6, pc.getItemName());// 项目名称
							
							c.registerOutParameter(7, java.sql.Types.INTEGER);
							c.registerOutParameter(8, java.sql.Types.VARCHAR);
							// 执行存储过程
							c.execute();
							// 得到存储过程的输出参数值
							int RESULT_CODE = c.getInt(7);
							String ERROR_MSG = c.getString(8);
							c.close();
							TranLogTxt.liswriteEror_to_txt(logname, "res:执行存储过程返回" + lismessage.getMessageid()
									+ ":result_code-" + RESULT_CODE + "，error_message-" + ERROR_MSG);

							if (RESULT_CODE == 1) {
								pacsflags = false;
								TranLogTxt.liswriteEror_to_txt(logname,
										"res:" + lismessage.getMessageid() + ": 1、pacs调用lis_request_delete错误:" + ERROR_MSG);
							}else{
								updateappstatus(exam_num,pc.getChargingItemid());
							}
						} catch (Exception ex) {
							pacsflags = false;
							TranLogTxt.liswriteEror_to_txt(logname,
									"res:" + lismessage.getMessageid() + ": 1、lis调用lis_request_delete错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
				rb.getResultHeader().setTypeCode("AA");
//				rb.getControlActProcess().setList(list);
				rb.getResultHeader().setText("lis调用成功");

			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("链接lis数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
	
	private int getitemSeq(String itemid){
		Connection tjtmpconnect = null;
		int lisitemid =0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select item_abbreviation from charging_item a where id='"+itemid+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				lisitemid = rs1.getInt("item_abbreviation");
			}
			rs1.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisitemid;
	}
	
	private void updateappstatus(String exam_num,String itemid){
		Connection connection = null;
		try {
		    connection = this.jdbcQueryManager.getConnection();
		    String sql = "update examinfo_charging_item set is_application = 'N' where examinfo_id = "
		    		+ "(select e.id from exam_info e where e.exam_num = '"+exam_num+"') and charge_item_id = "+itemid;
			int rs = connection.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
}
