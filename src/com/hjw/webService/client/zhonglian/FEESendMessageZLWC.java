package com.hjw.webService.client.zhonglian;

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
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.FeeRes;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.zhonglian.bean.CustomerUserBean;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 2.14 挂号信息服务 天健 平台对接-东北国际
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class FEESendMessageZLWC {

	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public FEESendMessageZLWC(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String xml = "";
		xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req   111:" + feeMessage.getREQ_NO() + ":" + xml);
		Connection connect = null;
		Connection tjconnection = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + url);
			
			List<String> itemCodeList = new ArrayList<String>();
			ReqId rqid = new ReqId();
			rqid.setReq_id(feeMessage.getREQ_NO());
			List<FeeRes> feeitem = new ArrayList<FeeRes>();
			boolean feeSendFalg = false;
			try {
				TranLogTxt.liswriteEror_to_txt(logname, "req:0、调用存储过程  Zl_nextno");
				CallableStatement c = connect.prepareCall("{call Zl_nextno(?)}");
				TranLogTxt.liswriteEror_to_txt(logname, "Zl_nextno(?)");
				c.registerOutParameter(1, java.sql.Types.VARCHAR);
				// 执行存储过程
				c.execute();
				// 得到存储过程的输出参数值
				String reqNo = c.getString(1);
				c.close();
				
				for (Fee fee : this.feeMessage.getPROJECTS().getPROJECT()) {

					String sql2 = "select CAST(a.personal_pay as decimal(18,2)) as personal_pay,b.perform_dept,a.itemnum,CAST(a.item_amount as decimal(18,2)) as amount,"
							+ " b.his_num,b.id,b.item_code,b.dep_category,c.id as examinfo_id,dep.id as dep_id, "
							+ "c.register_date as exam_date,c.patient_id,c.clinic_no,ci.user_name as username,ci.sex,c.age "
							+ " from examinfo_charging_item a,charging_item b,exam_info c,department_dep dep,customer_info ci where a.charge_item_id=b.id"
							+ " and c.id=a.examinfo_id and b.dep_id = dep.id and c.customer_id = ci.id and  c.exam_num='" + fee.getEXAM_NUM() + "'  and b.his_num='"
							+ fee.getITEM_CODE() + "'  and a.pay_status<>'M' and a.isActive='Y' ";
					TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql2);
					List<ExaminfoChargingItemDTO> eciList = this.jdbcQueryManager.getList(sql2,ExaminfoChargingItemDTO.class);
					if (eciList.size() > 0) {
						ExaminfoChargingItemDTO eci = eciList.get(0);
						
						
						String sql = "select * from getchargeitems_dydz where 诊疗项目ID = '"+eci.getHis_num()+"'";
						TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql);
						ResultSet rs = connect.createStatement().executeQuery(sql);
						while(rs.next()) {
							long exam_id = eci.getExaminfo_id();
							long chargingid = eci.getId();
							String chargingcode = fee.getExam_chargeItem_code();
							String his_num = eci.getItem_name();
							String exam_num = fee.getEXAM_NUM();
							String req_no = feeMessage.getREQ_NO();
							
							int itemnum = eci.getItemnum();
							double prices = eci.getAmount();
							double amount_sh = prices * itemnum;
							double amount_yh = eci.getPersonal_pay();
							String doctor = fee.getORDERED_BY_DOCTOR();
							long shoufeiximu = rs.getLong("收费项目ID");
							int shuci = rs.getInt("收费数量");
							double biaozhundanjia = rs.getDouble("现价");
							long sqNo = rs.getLong("序号");
							String feiyongzhaiyao = rs.getString("收据费目");
							long patient_id = Long.parseLong(eci.getPatient_id());
							long clinic_no = Long.parseLong(eci.getClinic_no());
							
							String user_name = eci.getUsername();
							String sex = eci.getSex();
							int age = eci.getAge();
							long dep_id = eci.getDep_id();
							String exam_date = eci.getExam_date();
							Date register_date = sdf.parse(exam_date);
							java.sql.Date resg_date = new java.sql.Date(register_date.getTime());
							
							TranLogTxt.liswriteEror_to_txt(logname, "应收："+amount_yh+"实收："+amount_sh+"单价："+biaozhundanjia);
							TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  Zl_三方门诊费用_Insert");
							c = connect.prepareCall("{call Zl_三方门诊费用_Insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
							TranLogTxt.liswriteEror_to_txt(logname,
									"Zl_三方门诊费用_Insert-" + exam_id + "-Zl_三方门诊费用_Insert('0','" + req_no
											+ "'," + sqNo + ","+patient_id+",0,"+clinic_no+",'"
											+ user_name + "','" + sex + "'" + ",'" + age + "','',"
											+ "92,92,92,'吴梦璇','"+shoufeiximu+"','1','0',"+eci.getPerform_dept()+",'"+biaozhundanjia+"','"+biaozhundanjia+"',"
											+ "'"+biaozhundanjia+"','"+exam_date+"','"+exam_date+"','吴梦璇','"+feiyongzhaiyao+"','','','','','','','','',?,?,?)");
							c.setInt(1, 0);
							c.setString(2, reqNo);
//							c.setString(2, req_no);
							c.setLong(3, sqNo);
							c.setLong(4, patient_id);
							c.setInt(5, 0);
							c.setLong(6, clinic_no);
							c.setString(7, user_name);
							c.setString(8, sex);
							c.setString(9, String.valueOf(age));
							c.setString(10, "");
							c.setInt(11, 92);
							c.setLong(12, 92);
							c.setInt(13, 92);
							c.setString(14, "吴梦璇");
							c.setLong(15, shoufeiximu);
							c.setInt(16, shuci);
							c.setString(17, eci.getPerform_dept());//执行科室
							c.setDouble(18, biaozhundanjia);
							c.setDouble(19, biaozhundanjia);
							c.setDouble(20, biaozhundanjia);
							c.setString(21, String.valueOf(resg_date));
							c.setString(22, String.valueOf(resg_date));
							c.setString(23, "吴梦璇");
							c.setString(24, feiyongzhaiyao);
							c.setString(25, "");
							c.setString(26, "");
							c.registerOutParameter(27, java.sql.Types.VARCHAR);
							c.registerOutParameter(28, java.sql.Types.VARCHAR);
							c.registerOutParameter(29, java.sql.Types.VARCHAR);
							// 执行存储过程
							c.execute();
							
							
							// 得到存储过程的输出参数值
							String result_code = c.getString(27);
							String error_msg = c.getString(28);
							String strReturn = c.getString(29);
							c.close();
							
							if("1".equals(result_code)) {
								feeSendFalg = true;
							
								//保存his  Zl_nextno存储返回的门诊费用记录 类似于申请单号    zl_req_his_item   flay1为收费 2为撤销 3为退费
								
								tjconnection = this.jdbcQueryManager.getConnection();
								String delete = "delete zl_req_his_item where exam_num='"+exam_num+"' and  req_no='"+req_no+"' and his_req_no='"+reqNo+"' and flay='1'";
								TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句zl_req_his_item删除数据==" + delete);
								tjconnection.createStatement().executeUpdate(delete);
								 
								 
								 
							  String insertsql = " insert into zl_req_his_item (exam_num,req_no,his_req_no,createdate,flay) "
									           + "values ('"+exam_num+"','"+req_no+"','"+reqNo+"','"+DateTimeUtil.getDateTime()+"','1') "; 
							  TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句zl_req_his_item插入数据==" + insertsql);
							  tjconnection.createStatement().executeUpdate(insertsql);
								 
								
							}else {
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText(error_msg);
								break;
							}
							itemCodeList.add(req_no);
							FeeRes f = new FeeRes();
							f.setCHARGES(amount_yh + "");
							f.setExam_chargeItem_code(chargingcode);
							f.setEXAM_NUM(exam_num);
							f.setFEE_CODE(his_num);
							f.setPATIENT_ID(String.valueOf(patient_id));
							feeitem.add(f);
						}
						rs.close();
						
						
					}
				}
			} catch (Exception ex) {
//				for (Fee fee : this.feeMessage.getPROJECTS().getPROJECT()) {
//					String exam_num = fee.getEXAM_NUM();
//					String req_no = feeMessage.getREQ_NO();
//					TranLogTxt.liswriteEror_to_txt(logname, "req:1、调用存储过程  zl_km体检费用明细_delete");
//					CallableStatement c = connect.prepareCall("{call zl_km体检费用明细_delete(?,?)}");
//					TranLogTxt.liswriteEror_to_txt(logname,
//							"res:-zl_km体检费用明细_delete('" + exam_num + "','" + req_no + "')");
//					c.setString(1, exam_num);
//					c.setString(2, req_no);
//					// 执行存储过程
//					c.execute();
//					c.close();
//				}
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("费用信息解析返回值错误");
			}
			if(feeSendFalg) {
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("");
			}
			rqid.setFeeitem(feeitem);
			rb.getControlActProcess().getList().add(rqid);
			rb.setItemCodeList(itemCodeList);
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息调用webservice错误");
		}finally {
			try {
				if (connect != null) {
					connect.close();
				}
				if(tjconnection !=null ){
					tjconnection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		return rb;
	}

	public CustomerUserBean getUserInfoByExamnum(String exam_num,String logname) {
		CustomerUserBean cus = new CustomerUserBean();
		try {
			String sql = "select c.user_name,c.sex,e.age from exam_info e,customer_info c " + 
					"where e.customer_id = c.id and e.is_Active = 'Y' and c.is_Active = 'Y' "
					+ "and e.exam_num = '"+exam_num+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:getuserinfo：sql " + sql);
			List<CustomerUserBean> cusList = this.jdbcQueryManager.getList(sql,CustomerUserBean.class);
			if(cusList.size()>0) {
				cus = cusList.get(0);
			}else {
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作失败,用户信息不存在!");
			}
		}catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		
		return cus;
	}
}
