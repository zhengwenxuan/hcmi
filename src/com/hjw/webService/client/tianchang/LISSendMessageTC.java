package com.hjw.webService.client.tianchang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.client.lianzhong
 * @Description: 检查申请信息服务 浙江联众-贵航贵阳
 * @author: zwx
 * @version V2.0.0.0
 */
public class LISSendMessageTC {
	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}

	public LISSendMessageTC(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "1-----------------开始lis请求--------------------");
		ResultLisBody rb = new ResultLisBody();
		Connection connect = null;
		try {
			String jsonString = JSONSerializer.toJSON(lismessage).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
			ExamInfoUserDTO eu= this.getExamInfoForNum(this.lismessage.getCustom().getExam_num(),logname);

			TranLogTxt.liswriteEror_to_txt(logname, "2-----------------lis数据库url--------------------" + url);
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "3-----------------连接成功--------------------");
			TranLogTxt.liswriteEror_to_txt(logname,"req:" + "4.准备插入病人信息，调用存储过程  Pkg_TcTjjk.p_Trade");
			CallableStatement c = connect.prepareCall("{call Pkg_TcTjjk.p_Trade(?,?,?,?)}");
			String patientinfo_param = eu.getExam_num()+"|"+eu.getUser_name()+"|"+this.lismessage.getCustom().getSexcode()+"|"+this.lismessage.getCustom().getBirthtime()
					+"|"+eu.getId_num()+"|"+eu.getPhone()+"|"+eu.getRemark1()+"|"+eu.getCompany()+"|"+eu.getAddress();
					//就诊卡号|姓名|性别|出生日期
					//|身份证号|联系电话|单位电话|单位名称|家庭地址;
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + " - Pkg_TcTjjk.p_Trade("
					+ "'p_PraseStr_Patientinfo',"//--插入病人信息
					+ "'"+patientinfo_param+"',"
					+"?,"//OUT--返回Code
					+"?)"//OUT--申请人人工号
					);
			c.setString(1,"p_PraseStr_Patientinfo");
			c.setString(2,patientinfo_param);
			c.registerOutParameter(3, java.sql.Types.INTEGER);
			c.registerOutParameter(4, java.sql.Types.VARCHAR);
			c.execute();
			c.close();
			TranLogTxt.liswriteEror_to_txt(logname,
					"res:" + lismessage.getMessageid() + ":插入病人信息，存储过程 Pkg_TcTjjk.p_Trade 执行结果————"
							+ "代码:"+c.getInt(3)+"信息:"+c.getString(4));
			
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			for (LisComponents lcs : this.lismessage.getComponents()) {
				try {
					String yblx = "";//样本类型
					String sqltext = "select remark from data_dictionary where data_name='" + lcs.getCsampleName()
							+ "' and isActive='Y' and data_type='样本类型' ";
					Connection connection = this.jdbcQueryManager.getConnection();
					Statement statement = connection.createStatement();
					ResultSet rs = statement.executeQuery(sqltext);
					if (rs.next()) {
						yblx = rs.getString("remark");
					}
					rs.close();
					statement.close();
					connection.close();
					
					String itemCodes = "";
					String itemNames = "";
					double itemamount = 0.0;
					for (LisComponent lc : lcs.getItemList()) {
						itemCodes += (lc.getItemCode() + "+");
						itemNames += (lc.getItemName() + "+");
						itemamount += lc.getItemprice();
					}
					itemCodes = itemCodes.substring(0, itemCodes.length()-1);
					itemNames = itemNames.substring(0, itemNames.length()-1);
					
					TranLogTxt.liswriteEror_to_txt(logname,"req:" + "4.准备插入条码表，调用存储过程  Pkg_TcTjjk.p_Trade");
					c = connect.prepareCall("{call Pkg_TcTjjk.p_Trade(?,?,?,?)}");
					String request_time = this.lismessage.getDoctor().getTime();
					request_time = request_time.substring(0,10)+":"+request_time.substring(10,12)+":"+request_time.substring(12,14);
					String param = lcs.getReq_no()+"|"+eu.getExam_num()+"|3|"+eu.getExam_num()+"|"+this.lismessage.getCustom().getSexcode()+"|"
					//条码|原始医嘱号|在院方式(1门诊 2 住院 3体检)|住院号|性别|
							+this.lismessage.getCustom().getBirthtime()+"|"+lismessage.getDoctor().getDept_name()+"||"+yblx+"|"+itemNames+"|"
							//出生日期|当前科室|当前床位|样本类型|检验项目|
							+itemCodes+"|"+eu.getUser_name()+"||1|0|"
							//检验项目名称|病人姓名|出院日期|打印标志|接收标志|
							+"0|"+this.lismessage.getDoctor().getDoctorCode()+"|"+request_time+"|0635||"
							//TYPEFLAG|申请医生|申请时间|采集人|采集时间|
							+"||"+itemamount+"||00743|"
							//急诊标志|备注|金额|测定周期|检验科室|
							+"||6|||"
							//病人诊断|计算机名|收费状态|取报告时间|取报告地点|
							+"|"+lcs.getReq_no()+"|0||"+eu.getExam_num()+"|"
							//QBGDT|样本号|申请模式|接收时间|住院号|
							+"|||0||"
							//婴儿标志|取消时间|取消人|JSBZ|JSNO|
							+"20110719|瞿康平|||0|"
							//EXESJ|JSZ|FSZ |采集部位|生理周期|
							+"|||"+eu.getExam_num()+"|0|"
							//确认人|确认时间|院区ID|病人ID|病人住院ID|
							+"0|"+eu.getExam_num()+"|1|||"
							//婴儿ID|就诊ID|可执行标志| SENDRY| SENDTIME|
							+"|17|2.0ml|0||"
							//备注|容器|采血量|顺序号|病案号|
							+"||";
							//特殊标志|电子手工单标志(0：手工单，1：电子单)|
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + " - Pkg_TcTjjk.p_Trade("
							+ "'p_PraseStr_Laborder',"//--插入条码表
							+ "'"+param+"',"
							+"?,"//OUT--返回Code
							+"?)"//OUT--申请人人工号
							);
					c.setString(1,"p_PraseStr_Laborder");
					c.setString(2,param);
					c.registerOutParameter(3, java.sql.Types.INTEGER);
					c.registerOutParameter(4, java.sql.Types.VARCHAR);
					c.execute();
					c.close();
					TranLogTxt.liswriteEror_to_txt(logname,
							"res:" + lismessage.getMessageid() + ":插入条码表，存储过程 Pkg_TcTjjk.p_Trade 执行结果————"
									+ "代码:"+c.getInt(3)+"信息:"+c.getString(4));
					ApplyNOBean ap = new ApplyNOBean();
					ap.setApplyNO(lcs.getReq_no());
					ap.setLis_id(lcs.getLis_id());
					ap.setBarcode(lcs.getReq_no());
					list.add(ap);
					if(c.getInt(3)>0) {
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + "修改样本为已采样，以使下次新生成条码号");
						updateSampleExamDetailByExamid(eu.getId(), lcs.getReq_no(), "Y");

						rb.getResultHeader().setTypeCode("AA");
						rb.getControlActProcess().setList(list);
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(c.getString(4));
					}
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
			}
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		TranLogTxt.liswriteEror_to_txt(logname, "6-----------------结束lis请求--------------------");
		return rb;
	}

	private ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times,c.phone,c.company,c.address,a.arch_num,a.birthday,a.id_num,ci.com_phone as remark1 ");
		sb.append(" from customer_info a,exam_info c ");
		sb.append(" left join company_info ci on ci.id = c.company_id ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "req:" +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	private void updateSampleExamDetailByExamid(long examid, String sample_barcode, String status)
			throws ServiceException {
		String sql = "update sample_exam_detail set status = '"+status+"' where exam_info_id=" + examid + " and sample_barcode='" + sample_barcode + "'";
		System.out.println(sql);
		this.jdbcPersistenceManager.executeSql(sql);
	}
}
