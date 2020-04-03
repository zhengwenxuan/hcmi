package com.hjw.webService.client.guihang300;

import java.sql.CallableStatement;
import java.sql.Connection;
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
import com.hjw.wst.DTO.SampleDemoDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.client.lianzhong
     * @Description: 2.7	检查申请撤销信息服务  浙江联众-贵航贵阳
     * @author: zwx
     * @version V2.0.0.0
 */
public class LISDELSendMessageGH300{
	private LisMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public LISDELSendMessageGH300(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname, boolean debug) {
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
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			for (LisComponents lcs : this.lismessage.getComponents()) {
				try {
					SampleDemoDTO sd = getSamID(lcs.getCsampleName(), logname);
					if(null == sd.getDemo_indicator() || "".equals(sd.getDemo_indicator())) {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("样本"+lcs.getCsampleName()+"未维护LIS方的样本ID");
						connect.close();
						return rb;
					}
					String itemNames = "";
					String itemCodes = "";
					for (LisComponent lc : lcs.getItemList()) {
						itemNames += (lc.getItemName() + "+");
						itemCodes += (lc.getItemCode() + "+");
					}
					itemNames = itemNames.substring(0, itemNames.length()-1);
					itemCodes = itemCodes.substring(0, itemCodes.length()-1);
					String birthday = this.lismessage.getCustom().getBirthtime();
					birthday = birthday.substring(0,4) + "/" + birthday.substring(4,6) + "/" + birthday.substring(6,8);
					TranLogTxt.liswriteEror_to_txt(logname,"req:" + "4.准备调用存储过程  prc_tijian_jiekou_300");
					CallableStatement c = connect
							.prepareCall("{call prc_tijian_jiekou_300(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + " - prc_tijian_jiekou_300('"
							+lcs.getReq_no()+"','"//--条码号
							+"3"+"','"//--病人类型--体检固定为3
							+eu.getExam_num()+"','"//--体检号
							+this.lismessage.getCustom().getSexcode()+"','"//--性别 男--1；女 --2
							+birthday+"','"//--出生日期
							+"1031"+"','"//--开单科室-默认体检科，固定写入代码1031
							+sd.getDemo_indicator()+"','"//--样本类型代码
							+itemNames+"','"//--检验项目（大项）
							+itemCodes+"','"//--检验项目代码，多项以'+'号连接
							+this.lismessage.getCustom().getName()+"','"//--体检人姓名
							+this.lismessage.getDoctor().getDoctorCode()+"','"//--申请人人工号
							+new java.sql.Timestamp(System.currentTimeMillis())+"','"//--申请时间
							+sd.getRemark()+"','"//--执行科室代码
							+"体检"+"','"//--备注 固定传入“体检”
							+"6"+"','"//--状态 固定传入“6”
							+eu.getCompany()+"','"//--暂传入单位名称，如个人体检，则写入 “个人体检”，如果是单位体检，写入范例“2018年某某单位体检”
							+eu.getExam_num()+"','"//--暂传入体检人编号
							+"?,"//OUT--返回Code
							+"?)"//OUT--申请人人工号
							);
					c.setLong(1, Long.parseLong(lcs.getReq_no()));
					c.setString(2, "3");
					c.setString(3, eu.getExam_num());
					c.setInt(4, Integer.parseInt(this.lismessage.getCustom().getSexcode()));
					c.setString(5, birthday);
					c.setString(6, "1031");
					c.setString(7, sd.getDemo_indicator());
					c.setString(8, itemNames);
					c.setString(9, itemCodes);
					c.setString(10, this.lismessage.getCustom().getName());
					c.setString(11, this.lismessage.getDoctor().getDoctorCode());
					c.setTimestamp(12, new java.sql.Timestamp(System.currentTimeMillis()));
					c.setString(13, sd.getRemark());
					c.setString(14, "体检");
					c.setString(15, "6");
					c.setString(16, eu.getCompany());
					c.setString(17, eu.getExam_num());
					c.registerOutParameter(18, java.sql.Types.VARCHAR);
					c.registerOutParameter(19, java.sql.Types.VARCHAR);
					c.execute();
					c.close();
					TranLogTxt.liswriteEror_to_txt(logname,
							"res:" + lismessage.getMessageid() + ":存储过程 prc_tijian_jiekou_300 执行结果————"
									+ "，代码："+c.getString(18)+"信息："+c.getString(19));
							ApplyNOBean ap = new ApplyNOBean();
							ap.setApplyNO(lcs.getReq_no());
							ap.setLis_id(lcs.getLis_id());
							ap.setBarcode(lcs.getReq_no());
							list.add(ap);
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(list);
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		return rb;
	}
	
	public SampleDemoDTO getSamID(String demo_name,String logname) throws ServiceException {
		String sql = "select * from sample_demo where demo_name = '"+demo_name+"'";
		TranLogTxt.liswriteEror_to_txt(logname, "sql:" +sql);
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, SampleDemoDTO.class);
		SampleDemoDTO sd = new SampleDemoDTO();
		if((map!=null)&&(map.getList().size()>0)){
			sd= (SampleDemoDTO)map.getList().get(0);			
		}
		return sd;
	}

	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type"
				+ ",CASE WHEN c.exam_type = 'G' THEN '个人体检' ELSE (CONVERT(nvarchar(4),year(getdate()))+'年'+c.company+'体检') END AS company" 
				//接口要求：-- 暂传入单位名称，如个人体检，则写入 “个人体检”，如果是单位体检，写入范例“2018年某某单位体检”
				+ ",c.register_date,c.join_date,c.exam_times,c.company,a.arch_num,a.birthday,a.id_num ");
		sb.append(" from exam_info c,customer_info a ");
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
}
