package com.hjw.webService.client.heilongjiang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.PinyinUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class PACSSendMessageHLJ {
	private PacsMessageBody lismessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSSendMessageHLJ(PacsMessageBody lismessage){
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
							for (PacsComponent pc : pcs.getPacsComponent()) {
								try {
									/*
									 * 名称   exam_master_n
										1	n_test_no        in NVARCHAR2, --申请单号
					                    2   exam_num         in NVARCHAR2, --体检号
					                    3   exam_class       in NVARCHAR2, ---检查类型  US CT MR 放射线 消化腔镜
					                    4   vid              in NUMBER, ---就诊标识  插入0
					                    5   pat_name         in NVARCHAR2, ---患者姓名
					                    6   isex             in NVARCHAR2, ---性别 男/女
					                    7   DATE_OF_BIRTH    in date, ---出生日期
					                    8   exam_sub_class   in NVARCHAR2, ---检查部位
					                    9   iage             in NVARCHAR2, ---年龄
					                    10  n_patient_source in NVARCHAR2, ---患者来源插入体检
					                    11  n_req_dept       in NVARCHAR2, ---开单科室 插入10125001
					                    12  n_req_physician  in NVARCHAR2, ---开单医生  插入W1753
					                    13  result_status    in NVARCHAR2, ---执行状态插入20
					                    14  n_result_status  in NVARCHAR2, ---计价状态插入1
					                    15  name_phonetic    in NVARCHAR2, ---患者拼音
					                    16  n_item_no        in NUMBER, ---项目序号
					                    17  n_item_name      in NVARCHAR2, ---项目名称
					                    18  n_item_code      in NVARCHAR2, ---项目代码
					                    19  result_code      out integer,---变量 空   正常 0，异常1
					                    20  error_message    out varchar2--异常返回变量 空
									 */
//									int item_seq = getitemSeq(pc.getItemId()+"");
									String pinyin = PinyinUtil.getTheAllMathedPinYin(lismessage.getCustom().getName());
									CallableStatement c = connect.prepareCall("{call exam_master_n(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
									TranLogTxt.liswriteEror_to_txt(logname, "res:执行存储过程" + exam_id + "-exam_master_n('"+pcs.getReq_no()+"','"+eu.getExam_num()+"','"
											+pc.getExam_class()+"',0,'"+lismessage.getCustom().getName()+"','"+lismessage.getCustom().getSexname()+"','"
											+lismessage.getCustom().getBirthtime()+"','"+pc.getItemName()+"','"+lismessage.getCustom().getOld()+"','体检','10125001','W1753','20','1','"+pinyin+"','1',"
															+ "'"+pc.getItemName()+"','"+pc.getPacs_num()+"',?,?)");
									c.setString(1, pcs.getReq_no());//申请单号
									c.setString(2, eu.getExam_num());//体检号
									c.setString(3, pc.getExam_class());//检查类型  US CT MR 放射线 消化腔镜
									c.setInt(4, 0);//就诊标识  插入0
									c.setString(5, lismessage.getCustom().getName());//患者姓名
									c.setString(6, lismessage.getCustom().getSexname());// 性别 男/女
//									c.setString(7, lismessage.getCustom().getBirthtime());//出生日期
									c.setDate(7, strToDate(lismessage.getCustom().getBirthtime()));//出生日期
									c.setString(8, pc.getItemName());//检查部位
									c.setString(9, lismessage.getCustom().getOld()+"");//年龄
									c.setString(10, "体检");//患者来源插入体检
									c.setString(11, "10125001");//开单科室 插入10125001
									c.setString(12, "W1753");//开单医生  插入W1753
									c.setString(13, "20");//执行状态插入20
									c.setString(14, "1");//计价状态插入1
									c.setString(15, pinyin);//患者拼音
									c.setInt(16, 1);//项目序号
									c.setString(17, pc.getItemName());//项目名称
									c.setString(18, pc.getPacs_num());//项目代码
									c.registerOutParameter(19, java.sql.Types.INTEGER);
									c.registerOutParameter(20, java.sql.Types.VARCHAR);
									// 执行存储过程
									c.execute();
									// 得到存储过程的输出参数值
									int RESULT_CODE = c.getInt(19);
									String ERROR_MSG = c.getString(20);
									c.close();
									TranLogTxt.liswriteEror_to_txt(logname,"res:执行存储过程返回" + lismessage.getMessageid() + ":result_code-" + RESULT_CODE+ "，error_message-" + ERROR_MSG);

									if (RESULT_CODE == 1) {
										pacsflags = false;
										TranLogTxt.liswriteEror_to_txt(logname,
												"res:" + lismessage.getMessageid() + ": 1、pacs调用exam_master_n返回错误:"+ERROR_MSG);
									}
								} catch (Exception ex) {
									pacsflags = false;
									TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
											+ ": 1、pacs调用exam_master_n出错：" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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

	private java.sql.Date strToDate(String strDate) {
		String str = strDate;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		java.util.Date d = null;
		try {
			d = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		java.sql.Date date = new java.sql.Date(d.getTime());
		return date;
	}
}
