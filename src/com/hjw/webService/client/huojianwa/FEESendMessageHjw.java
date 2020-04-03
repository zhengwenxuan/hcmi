package com.hjw.webService.client.huojianwa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;

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
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class FEESendMessageHjw {

	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public FEESendMessageHjw(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		Connection his_connect = null;
		Connection hjw_connect = null;
		try {
			if((this.feeMessage.getPROJECTS()!=null)&&(this.feeMessage.getPROJECTS().getPROJECT()!=null)&&(this.feeMessage.getPROJECTS().getPROJECT().size()>0)){
				String exam_num=this.feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM();
				ExamInfoUserDTO eu= this.getExamInfoForNum(exam_num,logname);
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + url);
				
				//调用his存储过程建档
				try {
					String his_dburl = url.split("&")[0];
					String his_user = url.split("&")[1];
					String his_passwd = url.split("&")[2];
					his_connect = OracleDatabaseSource.getConnection(his_dburl, his_user, his_passwd);
					TranLogTxt.liswriteEror_to_txt(logname,"req:" + ":1、调用存储过程  prc_tijian_bingrenxx_300");
					java.sql.Date birthDay = null;
					if(eu.getBirthday() != null && !"".equals(eu.getBirthday())) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						birthDay = new java.sql.Date(formatter.parse(eu.getBirthday().substring(0,10)).getTime());
					} else {
						birthDay = new java.sql.Date(System.currentTimeMillis());
					}
					CallableStatement c = his_connect
							.prepareCall("{call prc_tijian_bingrenxx_300(?,?,?,?,?,?,?)}");
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + " - prc_tijian_bingrenxx_300('"
							+eu.getUser_name()+"','"
							+eu.getSex()+"','"
							+birthDay+"','"
//							+eu.getId_num()
							+"','"
							+eu.getExam_num()+"',?,?)");
					c.setString(1, eu.getUser_name());
					c.setString(2, eu.getSex());
					c.setDate(3, birthDay);
					c.setString(4, "");//(雷双：prm_shenfenzh in varchar2 身份证号  这个取消掉，不给HIS传了。)
					c.setString(5, eu.getExam_num());
					c.registerOutParameter(6, java.sql.Types.VARCHAR);
					c.registerOutParameter(7, java.sql.Types.VARCHAR);
					// 执行存储过程
					c.execute();
					c.close();
					TranLogTxt.liswriteEror_to_txt(logname,
							"res:" + feeMessage.getREQ_NO() + ":存储过程 prc_tijian_bingrenxx_300 执行成功!");
				} catch (Throwable ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + feeMessage.getREQ_NO()
					+ ": 1、调用存储过程 prc_tijian_bingrenxx_300 错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				} finally {
					try {
						if (his_connect != null) {
							his_connect.close();
						}
					} catch (Exception e) {
					}
				}
				
				//往中间表写入数据
				hjw_connect = jdbcQueryManager.getConnection();
				hjw_connect.setAutoCommit(false);
				
				String del_Pat_Info_sql="delete from Pat_Info where req_no='"+feeMessage.getREQ_NO()+"' and chargeFlag='0'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +del_Pat_Info_sql);	
				hjw_connect.createStatement().executeUpdate(del_Pat_Info_sql);
				
//				String sb1 = "select patID from Pat_Info where patID='" + eu.getExam_num()+ "' ";
//				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
//				ResultSet rs1 = connect.createStatement().executeQuery(sb1);
//				if (!rs1.next()) {
				String insert_Pat_Info_sql = "insert into Pat_Info(patID,vipid,patName,sex,birth,ptFlag,regDate,req_no,ChargeFlag,id_num) values('" 
						+ eu.getExam_num() + "','" +eu.getArch_num() + "','"
						+eu.getUser_name()+"','"+eu.getSex()+"','"+eu.getBirthday()+"','P','"+DateTimeUtil.getDateTime()+"','" + feeMessage.getREQ_NO() + "','0','"+eu.getId_num()+"')";
						TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +insert_Pat_Info_sql);	
				hjw_connect.createStatement().executeUpdate(insert_Pat_Info_sql);
//				}
//				rs1.close();
				
				String delsql="delete from Pat_Charge_List where req_no='"+feeMessage.getREQ_NO()+"' and chargeFlag='0'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +delsql);	
				hjw_connect.createStatement().executeUpdate(delsql);
				double price = 0.0;
				double charge = 0.0;
				for(Fee f:this.feeMessage.getPROJECTS().getPROJECT()){
					price += Double.parseDouble(f.getITEM_PRICE());
					charge += Double.parseDouble(f.getCHARGES());
				}
				Fee f = this.feeMessage.getPROJECTS().getPROJECT().get(0);
				String insertsql = "insert into Pat_Charge_List(req_no,PatID,itemCode,hisItemCode,hisItemName,"
						+ "itemNum,oderDoctNo,oderDeptNo,deptNo,price,charge,rate,chargeFlag,readFlag) values('"+feeMessage.getREQ_NO()+"','" 
						+f.getEXAM_NUM() + "','"+f.getExam_chargeItem_code() + "','"
						+f.getITEM_CODE()+"','体检费',1,'"+f.getUSER_NAME()+"','"+f.getORDERED_BY_DEPT()+"','"
						+f.getPERFORMED_BY()+"',"+price+","+charge+","+f.getDiscount()+",'0','0')";
				
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +insertsql);	
				hjw_connect.createStatement().executeUpdate(insertsql);
				
				ReqId req= new ReqId();
				req.setReq_id(feeMessage.getREQ_NO());
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("");
				rb.getControlActProcess().getList().add(req);
				hjw_connect.commit();
			}
		} catch (Throwable ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			try{
			hjw_connect.rollback();
			}catch(Exception et){}
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息调用webservice错误");
		}finally {
			try {
				if (hjw_connect != null) {
					hjw_connect.setAutoCommit(true);
				}
				if (his_connect != null) {
					his_connect.close();
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
				+ ",c.register_date,c.join_date,c.exam_times,a.arch_num,a.birthday,a.id_num ");
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
