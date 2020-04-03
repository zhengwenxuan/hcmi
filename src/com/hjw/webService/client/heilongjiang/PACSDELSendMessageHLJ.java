package com.hjw.webService.client.heilongjiang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;

import net.sf.json.JSONObject;

public class PACSDELSendMessageHLJ {

	private PacsMessageBody lismessage;
	private static ConfigService configService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	public PACSDELSendMessageHLJ(PacsMessageBody lismessage){
		this.lismessage = lismessage;
	}
	public ResultPacsBody getMessage(String url,String logname) {
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
						for (PacsComponents pcs : lismessage.getComponents()) {
							boolean pacsflags = true;
							connect.setAutoCommit(false);
							ApplyNOBean an = new ApplyNOBean();
								try {
									/*
									 * 名称  exam_delete
										1	  exam_num    in varchar2, ---体检号
                                        2     n_test_no   in   varchar2,----申请单号
                                        3     result_code     out integer,--变量空
                                        4     error_message   out varchar2---异常返回  空

									 */
									
									CallableStatement c = connect.prepareCall("{call exam_delete(?,?,?,?)}");
									TranLogTxt.liswriteEror_to_txt(logname, "res:执行存储过程" + exam_id + "-exam_delete('"+eu.getExam_num()+"','"+pcs.getReq_no()+"',?,?)");
									
									c.setString(1, eu.getExam_num());//体检号
									c.setString(2, pcs.getReq_no());//申请单号
									c.registerOutParameter(3, java.sql.Types.INTEGER);
									c.registerOutParameter(4, java.sql.Types.VARCHAR);
									// 执行存储过程
									c.execute();
									// 得到存储过程的输出参数值
									int RESULT_CODE = c.getInt(3);
									String ERROR_MSG = c.getString(4);
									c.close();
									TranLogTxt.liswriteEror_to_txt(logname,"res:执行存储过程返回" + lismessage.getMessageid() + ":result_code-" + RESULT_CODE+ "，error_message-" + ERROR_MSG);

									if (RESULT_CODE == 1) {
										pacsflags = false;
										TranLogTxt.liswriteEror_to_txt(logname,
												"res:" + lismessage.getMessageid() + ": 1、pacs调用exam_delete错误:"+ERROR_MSG);
									}
								} catch (Exception ex) {
									pacsflags = false;
									TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
											+ ": 1、pacs调用exam_delete错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
}
