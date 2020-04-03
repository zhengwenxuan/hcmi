package com.hjw.webService.client.ningyuan;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqHisItemDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.CommService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.hjw.wst.service.examInfoService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class DELFEESendMessageNY {
	private DelFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static QueryManager queryManager;
	private static PersistenceManager persistenceManager;
	private static CustomerInfoService customerInfoService;
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	private static CommService commService;   //examInfoService
	private static examInfoService examInfoService;
	public DELFEESendMessageNY(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}
	static {
	   	init();
	}
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		queryManager = (QueryManager) wac.getBean("queryManager");
		persistenceManager = (PersistenceManager) wac.getBean("persistenceManager");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
		configService = (ConfigService) wac.getBean("configService");
		commService = (CommService) wac.getBean("commService");  //
		examInfoService = (examInfoService) wac.getBean("examInfoService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	public FeeReqBody getMessage(String url, String logName) {
		FeeReqBody rb = new FeeReqBody();
		this.feeMessage.setMSG_TYPE("TJ604");
		try {
		
			 ResultHeader rh = delhismessage(url,logName);
        if("AA".equals(rh.getTypeCode())){
        	 TranLogTxt.liswriteEror_to_txt(logName,  "退费接口返回rh:"+rh.getTypeCode()+"\r\n");
        	ReqNo rqid = new ReqNo();
        	rqid.setREQ_NO(this.feeMessage.getREQ_NO());
        	rb.getControlActProcess().getList().add(rqid);
        	rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("撤销收费申请成功!");
        }

	} catch (Exception ex){
	    rb.getResultHeader().setTypeCode("AE");
		rb.getResultHeader().setText("费用信息 xml格式文件错误");
	}
		 TranLogTxt.liswriteEror_to_txt(logName,  "退费rb:"+rb.getResultHeader().getTypeCode()+"\r\n");
	return rb;
	}
	/**
	 * his退费方法
	 * @param url
	 * @param logname
	 * @return
	 */
	private ResultHeader delhismessage(String url, String logname) {
		ResultHeader rh= new ResultHeader();
		
		WebserviceConfigurationDTO wcf=new WebserviceConfigurationDTO();
		wcf=this.webserviceConfigurationService.getWebServiceConfig("PAYMENT_APPLICATION");//此处需要修改为  his退费config_key
		ExamInfoUserDTO eu = configService.getExamInfoForNum(this.feeMessage.getExam_num());
		
		 List<ZlReqHisItemDTO> listhisno = gethisreqno(eu,this.feeMessage,logname);
		 
		 HashSet<String> codeSet = new HashSet<>();
			 for (int i = 0; i < listhisno.size(); i++) {
					if(!listhisno.get(i).getHis_req_no().equals("") && !listhisno.get(i).getHis_req_no().equals("null")){
						rh = Pro_send_sick_clinic_detail_cancel(eu,wcf,this.feeMessage,listhisno.get(i).getHis_req_no(),logname);
						
						//调用his退费存储
						if(rh.getTypeCode().equals("0") && rh.getTypeCode() != null){
							updateexaminfocharitem(eu, listhisno.get(i).getCharging_item_code(), logname);
							updatehisreqitem(eu,listhisno.get(i).getCharging_item_code(),listhisno.get(i).getReq_no(),listhisno.get(i).getHis_req_no(),logname);
							codeSet.add(rh.getTypeCode());
							rh.setTypeCode("AA");
							rh.setText("his撤销申请成功"+ rh.getText());
							TranLogTxt.liswriteEror_to_txt(logname, "res:申请单号" + feeMessage.getREQ_NO() +"流水号:"+listhisno.get(i).getHis_req_no() + "成功失败标识:"+rh.getTypeCode()+"\r\n");
						}else{
							rh.setTypeCode("AE");
							rh.setText("his撤销申请失败"+ rh.getText());
							TranLogTxt.liswriteEror_to_txt(logname, "res:申请单号" + feeMessage.getREQ_NO() +"流水号:"+listhisno.get(i).getHis_req_no() + "成功失败标识:"+rh.getTypeCode()+"\r\n");
						}
					}
			} 
			 if(codeSet.size()==1 ){
				 TranLogTxt.liswriteEror_to_txt(logname,  "长度:"+codeSet.size()+"\r\n");
					Iterator<String> iteratorcode = codeSet.iterator();
						while (iteratorcode.hasNext()) {
							String typecode = iteratorcode.next();
							if(typecode.equals("0") && typecode!=null){
								rh.setTypeCode("AA");
								updatesumcharsig(eu,feeMessage.getREQ_NO(),logname);
							}else{
								rh.setTypeCode("AE");
							}
							
					}
				}else{
					rh.setTypeCode("AE");
				}
		
			 TranLogTxt.liswriteEror_to_txt(logname,  "退费rh:"+rh.getTypeCode()+"\r\n");
		return rh;
	}
	
	//撤销成功后 修改flay 为1
	private int updatehisreqitem(ExamInfoUserDTO eu, String charging_item_code, String req_no, String his_req_no,String logname) {
		Connection tjtmpconnect = null;
		int executeUpdate=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String zl_req_his_item_sql =" update zl_req_his_item set flay='1' where exam_num='"+eu.getExam_num()+"' and req_no='"+req_no+"' and his_req_no='"+his_req_no+"' and charging_item_code='"+charging_item_code+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "修改zl_req_his_item_sql:flay状态:examinfo_charging_item_sql:" + zl_req_his_item_sql + "\r\n");
			 executeUpdate = tjtmpconnect.createStatement().executeUpdate(zl_req_his_item_sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		return  executeUpdate;
		
	}
	private int updatesumcharsig(ExamInfoUserDTO eu, String req_no, String logname) {
		Connection tjtmpconnect = null;
		int executeUpdate=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String charging_summary_single_sql =" update charging_summary_single set is_active='N' where req_num='"+req_no+"' and exam_id='"+eu.getId()+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "修改个人结算表isactive状态:examinfo_charging_item_sql:" + charging_summary_single_sql + "\r\n");
			 executeUpdate = tjtmpconnect.createStatement().executeUpdate(charging_summary_single_sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		return  executeUpdate;
		
	}
	public List<ZlReqHisItemDTO>  gethisreqno(ExamInfoUserDTO eu, DelFeeMessage delfee,String logname) {
		List<ZlReqHisItemDTO> hisno_list = new ArrayList<ZlReqHisItemDTO>();
		/*Connection connection = null;
		Statement statement = null;*/
		String sql = "select distinct zrhi.his_req_no,zrhi.charging_item_code,zrhi.req_no from charging_summary_single css,charging_detail_single cds,zl_req_his_item zrhi "
		 		+ " where cds.summary_id=css.id and cds.charging_item_id=zrhi.charging_item_code  and zrhi.flay='0' and css.is_active='Y' "
		 		+ " and css.req_num='"+feeMessage.getREQ_NO()+"' and css.exam_id='"+eu.getId()+"' and zrhi.exam_num='"+feeMessage.getExam_num()+"'";
		
		TranLogTxt.liswriteEror_to_txt(logname, "申请单号:" + feeMessage.getREQ_NO() + "\r\n");
		TranLogTxt.liswriteEror_to_txt(logname, "通过缴费申请查询his缴费流水号:" + sql + "\r\n");
		 hisno_list  =  this.jdbcQueryManager.getList(sql, ZlReqHisItemDTO.class);
		 
		  return hisno_list;
	}

	public ResultHeader Pro_send_sick_clinic_detail_cancel(ExamInfoUserDTO eu, WebserviceConfigurationDTO wcf,DelFeeMessage fee, String his_req_no, String logname) {
		ResultHeader rh = new ResultHeader();
		Connection conn = null;
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet result = null;// 
	    String url = wcf.getConfig_url();//oracle数据库url
		String[] split = wcf.getConfig_value().split(",");
		String user = split[0];// 获取用户名
		String password = split[1];//获取密码
		
		try {
			 try {
				conn = OracleDatabaseSource.getConnection(url, user, password);
			} catch (Exception e) {
				rh.setTypeCode("-1");
				e.printStackTrace();
			}
			 
			
				 CallableStatement call = conn.prepareCall("{call zhiydba.zhi4_peis_wjw.Pro_send_clinic_detail_cancel(?,?,?)}");//执行存储过程
				 call.setString(1, his_req_no);//诊疗号码   申请单号
				 call.registerOutParameter(2, java.sql.Types.FLOAT);
				 call.registerOutParameter(3, java.sql.Types.LONGVARCHAR);
				 
				 
				 TranLogTxt.liswriteEror_to_txt(logname, "res:his退费入参流水号:" + his_req_no +"\r\n");
				// 执行存储过程啊闪光灯
				call.execute();
				// 得到存储过程的输出参数值
				rh.setTypeCode(call.getString(2));
				rh.setText(call.getString(3));
				TranLogTxt.liswriteEror_to_txt(logname, "res:his退费申请返回:" +"成功失败标识:"+rh.getTypeCode()+"描述:"+rh.getText()+"\r\n");
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(e));
			rh.setTypeCode("-1");
		}
		return rh;
	}
	private int updateexaminfocharitem(ExamInfoUserDTO eu, String charging_item_code, String logname) {
		Connection tjtmpconnect = null;
		int executeUpdate=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String examinfo_charging_item_sql =" update examinfo_charging_item set his_req_status = 'N' where  isActive='Y' and exam_status!='Y' and examinfo_id='"+eu.getId()+"' and charge_item_id='"+charging_item_code+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "修改eci表内项目结算状态:examinfo_charging_item_sql:" + examinfo_charging_item_sql + "\r\n");
			 executeUpdate = tjtmpconnect.createStatement().executeUpdate(examinfo_charging_item_sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		return  executeUpdate;
		
	}
}
