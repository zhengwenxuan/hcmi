package com.hjw.webService.client.tianchang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class FEESendMessageTC {

	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public FEESendMessageTC(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}

	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		Connection connect = null;
		try {
			if((this.feeMessage.getPROJECTS()!=null)&&(this.feeMessage.getPROJECTS().getPROJECT()!=null)&&(this.feeMessage.getPROJECTS().getPROJECT().size()>0)){
				Fee fee = this.feeMessage.getPROJECTS().getPROJECT().get(0);
				String exam_num=fee.getEXAM_NUM();
				ExamInfoUserDTO eu= this.getExamInfoForNum(exam_num,logname);
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + url);
				String dburl = url.split("&")[0];
				String user = url.split("&")[1];
				String passwd = url.split("&")[2];
				connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
				TranLogTxt.liswriteEror_to_txt(logname, "3-----------------连接成功--------------------");
				TranLogTxt.liswriteEror_to_txt(logname,"req:" + "4.准备插入病人信息，调用存储过程  Pkg_TcTjjk.p_Trade");
				CallableStatement c = connect.prepareCall("{call Pkg_TcTjjk.p_Trade(?,?,?,?)}");
				String patientinfo_param = eu.getExam_num()+"|"+eu.getUser_name()+"|"+eu.getSex()+"|"+eu.getBirthday()
						+"|"+eu.getId_num()+"|"+eu.getPhone()+"|"+eu.getRemark1()+"|"+eu.getCompany()+"|"+eu.getAddress();
						//就诊卡号|姓名|性别|出生日期
						//|身份证号|联系电话|单位电话|单位名称|家庭地址;
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + " - Pkg_TcTjjk.p_Trade("
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
						"res:" + feeMessage.getREQ_NO() + ":插入病人信息，存储过程 Pkg_TcTjjk.p_Trade 执行结果————"
								+ "代码:"+c.getInt(3)+"信息:"+c.getString(4));
				
				TranLogTxt.liswriteEror_to_txt(logname,"req:" + ":1、调用存储过程  pkg_tctjjk.p_trade");
				double charge = 0.0;
				for(Fee f:this.feeMessage.getPROJECTS().getPROJECT()){
					charge += Double.parseDouble(f.getCHARGES());
				}
				String param = eu.getExam_num()+"|"+fee.getORDERED_BY_DOCTOR()+"|"+"|"+fee.getORDERED_BY_DEPT()+"|"+fee.getPERFORMED_BY()+"|"
						//就诊卡号|开单医生|开单日期|开单科室|执行人|
						+fee.getORDERED_BY_DEPT()+"|0|01257|"+charge+"|1|"
				//执行科室|套餐医疗序号|收费项目|单价|数量|
						+"1|"+eu.getExam_num()+"|0|"+this.feeMessage.getREQ_NO();
				//自付比例|体检编号|团体标志|体检开单收费编号
				c = connect
						.prepareCall("{call pkg_tctjjk.p_trade(?,?,?,?)}");
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + " - pkg_tctjjk.p_trade("
						+ "'p_Prasexml_Detail',"
						+ "'"+param+"',"
						+"?,"//OUT--返回Code
						+"?)"//OUT--报错信息
						);
				c.setString(1, "p_Prasexml_Detail");
				c.setString(2, param);
				c.registerOutParameter(3, java.sql.Types.INTEGER);
				c.registerOutParameter(4, java.sql.Types.VARCHAR);
				// 执行存储过程
				c.execute();
				c.close();
				
				TranLogTxt.liswriteEror_to_txt(logname,
						"res:" + feeMessage.getREQ_NO() + ":存储过程 pkg_tctjjk.p_trade 执行结果————"+ "代码:"+c.getInt(3)+"信息:"+c.getString(4));
				if(c.getInt(3)>0) {
					rb.getResultHeader().setTypeCode("AA");
					ReqId req= new ReqId();
					req.setReq_id(feeMessage.getREQ_NO());
					rb.getControlActProcess().getList().add(req);
				} else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(c.getString(4));
				}
			}
		} catch (Throwable ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息调用存储过程错误");
		}finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		return rb;
	}

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
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
		
		if("男".equals(eu.getSex())) {
			eu.setSex("1");
		} else if("女".equals(eu.getSex())) {
			eu.setSex("2");
		} else {
			eu.setSex("%");
		}
		if(eu.getBirthday() != null && eu.getBirthday().length()>=10) {
			eu.setBirthday(eu.getBirthday().substring(0, 4) + eu.getBirthday().substring(5, 7) + eu.getBirthday().substring(8, 10));
		}
		return eu;
	} 
}
