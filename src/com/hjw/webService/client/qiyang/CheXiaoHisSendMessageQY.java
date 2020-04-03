package com.hjw.webService.client.qiyang;

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

public class CheXiaoHisSendMessageQY {
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
	public CheXiaoHisSendMessageQY(DelFeeMessage feeMessage){
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
		String xml = "";
		this.feeMessage.setMSG_TYPE("TJ604");
		TranLogTxt.liswriteEror_to_txt(logName,"req:"+feeMessage.getREQ_NO()+":"+xml);
		try {
	
			 ResultHeader rh = delhismessage(url,logName);
        if("AA".equals(rh.getTypeCode())){
        	ReqNo rqid = new ReqNo();
        	rqid.setREQ_NO(feeMessage.getREQ_NO());
        	rb.getControlActProcess().getList().add(rqid);
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
		wcf=this.webserviceConfigurationService.getWebServiceConfig("DEL_ITEM_APPLICATION");//此处需要修改为  his退费config_key
		ExamInfoUserDTO eu = configService.getExamInfoForNum(this.feeMessage.getExam_num());
		
		 List<ZlReqHisItemDTO> listhisno = gethisreqno(eu,this.feeMessage);
		 HashSet<String> codeSet = new HashSet<>();
			 for (int i = 0; i < listhisno.size(); i++) {
					if(!listhisno.get(i).getHis_req_no().equals("") || !listhisno.get(i).getHis_req_no().equals("null")){
						 System.err.println(listhisno.size()+"==================33----33-----5");
						rh = Pro_send_sick_clinic_detail_cancel(eu,wcf,this.feeMessage,listhisno.get(i).getHis_req_no());
						 codeSet.add(rh.getTypeCode());
						System.err.println(listhisno.size()+"==================33----33-----6");

						//调用his退费存储
						if(rh.getTypeCode().equals("0")){
							rh.setTypeCode("AA");
							rh.setText("his撤销申请成功"+ rh.getText());
							TranLogTxt.liswriteEror_to_txt(logname, "res:成功申请单号" + feeMessage.getREQ_NO() +"流水号:"+listhisno.get(i).getHis_req_no() + "\r\n");
						}else{
							rh.setTypeCode("AE");
							rh.setText("his撤销申请失败"+ rh.getText());
							TranLogTxt.liswriteEror_to_txt(logname, "res:成功申请单号" + feeMessage.getREQ_NO() +"流水号:"+listhisno.get(i).getHis_req_no() + "\r\n");
						}
					}
			} 
			 if(codeSet.size()==1 ){
					Iterator<String> iteratorcode = codeSet.iterator();
						while (iteratorcode.hasNext()) {
							String typecode = iteratorcode.next();
							if(typecode.equals("0")){
								rh.setTypeCode("AA");
							}else{
								rh.setTypeCode("AE");
							}
							
					}
				}else{
					rh.setTypeCode("AE");
				}
		
		
		return rh;
	}
	
	
	public List<ZlReqHisItemDTO>  gethisreqno(ExamInfoUserDTO eu, DelFeeMessage unfee) {
		String his_req_no="";
		List<ZlReqHisItemDTO> hisno_list = new ArrayList<ZlReqHisItemDTO>();
		/*Connection connection = null;
		Statement statement = null;*/
		String sql = "select distinct zrhi.his_req_no from charging_summary_single css,charging_detail_single cds,zl_req_his_item zrhi "
		 		+ " where cds.summary_id=css.id and cds.charging_item_id=zrhi.charging_item_code  and zrhi.flay='0' and css.is_active='Y' "
		 		+ " and css.req_num='"+unfee.getREQ_NO()+"' and css.exam_id='"+eu.getId()+"' and zrhi.exam_num='"+unfee.getExam_num()+"'";
		
		 
		TranLogTxt.liswriteEror_to_txt("CheXiaoHisSendMessage", "查询:" + sql + "\r\n");
		 hisno_list  =  this.jdbcQueryManager.getList(sql, ZlReqHisItemDTO.class);
		 
		  return hisno_list;
	}

	public ResultHeader Pro_send_sick_clinic_detail_cancel(ExamInfoUserDTO eu, WebserviceConfigurationDTO wcf,DelFeeMessage fee, String his_req_no) {
		ResultHeader rh = new ResultHeader();
		Connection conn = null;
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet result = null;// 
		
	    String url = wcf.getConfig_url();//oracle数据库url
		String[] split = wcf.getConfig_value().split(",");
		String user = split[0];// 获取用户名
		String password = split[1];//获取密码
		
		try {
			 System.err.println(""+"==================1111");
			 try {
				conn = OracleDatabaseSource.getConnection(url, user, password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.err.println(""+"==================2222");
			 
			
				 CallableStatement call = conn.prepareCall("{call zhiydba.zhi4_peis_wjw.Pro_send_clinic_detail_cancel(?,?,?)}");//执行存储过程
				 call.setString(1, his_req_no);//诊疗号码   申请单号
				 call.registerOutParameter(2, java.sql.Types.FLOAT);
				 call.registerOutParameter(3, java.sql.Types.LONGVARCHAR);
				 
				// 执行存储过程啊闪光灯
				call.execute();
				// 得到存储过程的输出参数值
				rh.setTypeCode(call.getString(2));
				rh.setText(call.getString(3));
			
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(e));
			rh.setTypeCode("-1");
		}
		return rh;
	}
}
