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
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.UnFeeMessage;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.domain.ChargingItem;
import com.hjw.wst.service.CommService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.hjw.wst.service.examInfoService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;
import com.synjones.framework.support.PageSupport;

public class UNFEESendMessageNY {
	private UnFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static QueryManager queryManager;
	private static PersistenceManager persistenceManager;
	private static CustomerInfoService customerInfoService;
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	private static CommService commService;   //examInfoService
	private static examInfoService examInfoService;
	public UNFEESendMessageNY(UnFeeMessage feeMessage){
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
        	ReqNo rqid = new ReqNo();
        	rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("发送退费申请成功!");
        }
	} catch (Exception ex){
	    rb.getResultHeader().setTypeCode("AE");
		rb.getResultHeader().setText("费用信息 xml格式文件错误");
	}
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
		ExamInfoUserDTO eu = configService.getExamInfoForNum(this.feeMessage.getEXAM_NUM());
		
		 List<String> itemCodeList = feeMessage.getItemCodeList();
		 HashSet<String> codeSet = new HashSet<>();
		 for (String itemcode : itemCodeList) {
			 TranLogTxt.liswriteEror_to_txt(logname, "res:itemcode" + itemcode +"长度:"+itemCodeList.size()+"\r\n");
			 ChargingItem chargingItem = getChargingItem(itemcode, logname);
			 ZlReqHisItemDTO hisItemDTO = gethisreqnofinditemid(eu, chargingItem.getId(), logname);
			 
			 if(!hisItemDTO.getHis_req_no().equals("") && !hisItemDTO.getHis_req_no().equals("null")){
					rh = Pro_send_sick_clinic_detail_cancel(eu,wcf,this.feeMessage,hisItemDTO.getHis_req_no(),logname);
					codeSet.add(rh.getTypeCode());
						
					//调用his退费存储
					if(rh.getTypeCode().equals("0") && rh.getTypeCode() != null){
						TranLogTxt.liswriteEror_to_txt(logname, "res:申请单号" + feeMessage. getREQ_NOS().getREQ_NO().get(0) +"流水号:"+hisItemDTO.getHis_req_no() + "成功失败标识:"+rh.getTypeCode()+"\r\n");
						TranLogTxt.liswriteEror_to_txt(logname, "res:examinfo_id" + eu.getId() +"收费项目id:"+chargingItem.getId()+"" + "成功失败标识:"+rh.getTypeCode()+"\r\n");
						
						updatehisreqitem(eu, hisItemDTO.getCharging_item_code(), hisItemDTO.getReq_no(), hisItemDTO.getHis_req_no(), logname);
						rh.setText("his退费申请成功"+ rh.getText());
						rh.setTypeCode("AA");
					}else{
						rh.setTypeCode("AE");
						rh.setText(chargingItem.getItem_name()+"退费申请失败"+ rh.getText());
						TranLogTxt.liswriteEror_to_txt(logname, "res:申请单号" + feeMessage. getREQ_NOS().getREQ_NO().get(0) +"流水号:"+hisItemDTO.getHis_req_no() + "成功失败标识:"+rh.getTypeCode()+"\r\n");
					}
					
				}
		 }
		 if(codeSet.size()==1){
			 TranLogTxt.liswriteEror_to_txt(logname,  "长度:"+codeSet.size()+"\r\n");
				Iterator<String> iteratorcode = codeSet.iterator();
					while (iteratorcode.hasNext()) {
						String typecode = iteratorcode.next();
						if(typecode.equals("0") && typecode!=null){
							rh.setTypeCode("AA");
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
	
	
	private int updateexaminfocharitem(ExamInfoUserDTO eu, String charging_item_code, String logname) {
		Connection tjtmpconnect = null;
		int executeUpdate=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String examinfo_charging_item_sql =" update  examinfo_charging_item set pay_status='M' where examinfo_id='"+eu.getId()+"' and charge_item_id='"+charging_item_code+"' and pay_status ='Y'  and exam_status not in ('Y','G')    and isActive='Y' ";
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
	
	public ZlReqHisItemDTO gethisreqnofinditemid(ExamInfoUserDTO eu, long charging_item_id,String logname) {
		
		List<ZlReqHisItemDTO> hisno_list = new ArrayList<ZlReqHisItemDTO>();
		String sql = " select top 1  * from zl_req_his_item where exam_num='"+eu.getExam_num()+"' and charging_item_code='"+charging_item_id+"' order by createdate desc  ";
		
		TranLogTxt.liswriteEror_to_txt(logname, "申请单号:" + feeMessage.getREQ_NOS().getREQ_NO().get(0) + "\r\n");
		TranLogTxt.liswriteEror_to_txt(logname, "通过缴费申请查询his缴费流水号和收费项目id:" + sql + "\r\n");
		 hisno_list  =  this.jdbcQueryManager.getList(sql, ZlReqHisItemDTO.class);
		 
		 
		  return hisno_list.get(0);
	}

	public ResultHeader Pro_send_sick_clinic_detail_cancel(ExamInfoUserDTO eu, WebserviceConfigurationDTO wcf,UnFeeMessage fee, String his_req_no, String logname) {
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
	
	public ChargingItem getChargingItem(String exam_chargeItem_code, String logname) {
		StringBuffer sb = new StringBuffer();
		TranLogTxt.liswriteEror_to_txt(logname, "查询收费项目表开始:" + sb.toString() + "\r\n");
		sb.append(" select * from charging_item where item_code='" + exam_chargeItem_code + "' and isActive='Y' ");

		TranLogTxt.liswriteEror_to_txt(logname, "查询收费项目表结束:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ChargingItem.class);
		ChargingItem ci = new ChargingItem();
		if ((map != null) && (map.getList().size() > 0)) {
			ci = (ChargingItem) map.getList().get(0);
		}
		return ci;
	}
	
	//退费成功后修改 flay为2
	private int updatehisreqitem(ExamInfoUserDTO eu, String charging_item_code, String req_no, String his_req_no,String logname) {
		Connection tjtmpconnect = null;
		int executeUpdate=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String zl_req_his_item_sql =" update zl_req_his_item set flay='2' where exam_num='"+eu.getExam_num()+"' and req_no='"+req_no+"' and his_req_no='"+his_req_no+"' and charging_item_code='"+charging_item_code+"' ";
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
}
