package com.hjw.webService.client.qiyang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.CommService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.hjw.wst.service.examInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;
import com.synjones.framework.support.PageSupport;

public class GRHisSendMessageQY {
	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static QueryManager queryManager;
	private static PersistenceManager persistenceManager;
	private static CustomerInfoService customerInfoService;
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	private static CommService commService;   //examInfoService
	private static examInfoService examInfoService;
	private static Calendar checkDay;
	

	static{
    	init();
    	}
    
    public GRHisSendMessageQY(FeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	public static void init(){
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
	
	
	public FeeResultBody getMessage(String url,String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		try {
			
			/*Calendar deadline = Calendar.getInstance();
			deadline.set(2019, Calendar.AUGUST, 13, 0, 0, 0);
			TranLogTxt.liswriteEror_to_txt(logname,"接口截止日期："+deadline.getTime());
			if(new Date().after(deadline.getTime())) {
				rb.getResultHeader().setTypeCode("AE");
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				rb.getResultHeader().setText("接口已过期，请联系火箭蛙，截止日期："+df.format(deadline.getTime()));
				TranLogTxt.liswriteEror_to_txt(logname,"接口已过期，请联系火箭蛙，截止日期："+df.format(deadline.getTime()));
			
			return rb;*/
			Calendar deadline = Calendar.getInstance();
			SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
			//JANUARY一月	FEBRUARY二月		MARCH三月		APRIL四月		MAY五月			JUNE六月
			//JULY七月		AUGUST八月		SEPTEMBER九月	OCTOBER十月		NOVEMBER十一月	DECEMBER十二月
			deadline.set(2019, Calendar.AUGUST, 18, 0, 0, 0);
			String viewDateStr = df.format(deadline.getTime());
			if(new Date().after(deadline.getTime())) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
				TranLogTxt.liswriteEror_to_txt(logname,"接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
				return rb;
			}
			
			if(checkDay == null) {
				checkDay = Calendar.getInstance(); checkDay.add(Calendar.DATE, -1);
			}
			Calendar today = Calendar.getInstance(); today.set(Calendar.HOUR, 0); today.set(Calendar.MINUTE, 0); today.set(Calendar.SECOND, 0);
			if(today.after(checkDay)) {//每天仅检查一遍
				checkDay = today;
				Connection connection = null;
				try {
					//每次先将旧的通知信息打到日志文件
					connection = jdbcQueryManager.getConnection();
					String sql = " select notices from examinatioin_center ";
					ResultSet rs = connection.createStatement().executeQuery(sql);
					String notices="";
					while (rs.next()) {
						notices = rs.getString("notices");
					}
					TranLogTxt.liswriteEror_to_txt(logname, "原来的notices是:"+notices);
					//判断系统到期时间，提前10天提醒客户
					Calendar alertDate = deadline;
					alertDate.add(Calendar.DATE, -10);
					if(new Date().after(alertDate.getTime())) {
						String noticesNew = "系统到期时间为:"+viewDateStr+"，请尽快联系火箭蛙销售人员!!";
						String updatesql = " update examinatioin_center set notices='"+noticesNew+"' ";
						connection.createStatement().executeUpdate(updatesql);
						TranLogTxt.liswriteEror_to_txt(logname, updatesql);
					} else {
						String updatesql = " update examinatioin_center set notices='' ";
						connection.createStatement().executeUpdate(updatesql);
						TranLogTxt.liswriteEror_to_txt(logname, updatesql);
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					connection.close();
				}
			
			
			this.feeMessage.setMSG_TYPE("TJ602");
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+feeMessage.getREQ_NO()+":"+xml);//此处his收费 申请单
			
			ResultHeader rh=getString(url,logname);
			if("0".equals(rh.getTypeCode())){
            	ReqId rqid = new ReqId();
            	rqid.setReq_id(this.feeMessage.getREQ_NO());
            	rb.getControlActProcess().getList().add(rqid);
            	rb.getResultHeader().setTypeCode("AA");
    			rb.getResultHeader().setText("HIS费用信息 申请成功!");
            } else {
            	 rb.getResultHeader().setTypeCode("AE");
     			 rb.getResultHeader().setText(rh.getText());
            }
			
			}
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("HIS费用信息 发送失败");
		}
		return rb;
	}

	private ResultHeader getString(String url, String logname) {
		ResultHeader rh= new ResultHeader();
		Connection connection = null;
		WebserviceConfigurationDTO wcf=new WebserviceConfigurationDTO();
		wcf=this.webserviceConfigurationService.getWebServiceConfig("PAYMENT_APPLICATION");
		
		String exam_num=this.feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM();
		String req_NO = feeMessage.getREQ_NO();
        ExamInfoUserDTO eu= new ExamInfoUserDTO();
		eu=getExamInfoForNum(exam_num,logname);
		List<Fee> feelist = feeMessage.getPROJECTS().getPROJECT();
		HashSet<String> codeSet = new HashSet<>();
		 for (int i = 0; i < feelist.size(); i++) {

			 rh = Pro_send_sick_clinic_detail(eu,wcf,feelist.get(i),logname);//
			 
			 TranLogTxt.liswriteEror_to_txt(logname, "res:" +"流水号:"+ rh.getSourceMsgId()+"成功失败标识:"+rh.getTypeCode()+"\r\n");
			 codeSet.add(rh.getTypeCode());
			 //保存  记账流水号
			
			ChargingItemDTO itemDTO = getchargingitem(feelist.get(i).getITEM_CODE(),feelist.get(i).getExam_chargeItem_code(),logname);
			 
			 
		 		String his_sql ="insert into zl_req_his_item (exam_num,req_no,charging_item_code,his_req_no,createdate,flay,his_num) "
		 		+ "values ('"+feelist.get(i).getEXAM_NUM()+"','"+req_NO+"','"+itemDTO.getId()+"','"+rh.getSourceMsgId()+"','"+DateTimeUtil.getDateTime()+"','"+rh.getTypeCode()+"','"+feelist.get(i).getITEM_CODE()+"');";
		 		 TranLogTxt.liswriteEror_to_txt(logname, "his收费成功后插入zl_req_his_item:"+his_sql);
		 		
		 		
				try {
					connection = this.jdbcQueryManager.getConnection();
					connection.createStatement().executeUpdate(his_sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						if(connection != null) {
							connection.close();
						}
					} catch (SQLException e) {
					}
				}
		 		//this.jpm.execSql(his_sql);
		 		 
				/*if(rh.getTypeCode().equals("0")){
					rh.setTypeCode("0");
					rh.setText("his发送成功"+ rh.getText());
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + rh.getText() + "\r\n");
					
					//修改his发送状态 his_req_status
					String sql = "select eci.* from examinfo_charging_item eci,exam_info ei , charging_item ci "
							+ "where eci.examinfo_id=ei.id and eci.charge_item_id=ci.id and eci.isActive='Y' and eci.his_req_status='N' and ei.exam_num='"+fee.getEXAM_NUM()+"' and ci.item_code='"+fee.getITEM_CODE()+"'";
					
					System.err.println(sql+"==================1012.01");
					List<ExaminfoChargingItem> listitem = this.jdbcQueryManager.getList(sql, ExaminfoChargingItem.class);
					System.err.println(sql+"================1012.02");
					 System.err.println("==================1013");
					if(listitem.size() != 0){
						for (int i = 0; i < listitem.size(); i++) {
							listitem.get(0).setHis_req_status("Y");
							 System.err.println("==================1014");
							this.persistenceManager.update(listitem.get(0));
							 System.err.println("==================1015");
						}
					}
					
				}else{
					rh.setTypeCode("-1");
					rh.setText("发送his收费申请失败"+ rh.getText());
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + rh.getText() + "\r\n");
					
				}*/
		
		}
		 
		if(codeSet.size()==1 ){
			Iterator<String> iteratorcode = codeSet.iterator();
				while (iteratorcode.hasNext()) {
					String typecode = iteratorcode.next();
					if(typecode.equals("0")){
						rh.setTypeCode("0");
					}else{
						rh.setTypeCode("-1");
					}
					
			}
		}else{
			rh.setTypeCode("-1");
		}
		
		return rh;
	}
	public ChargingItemDTO getchargingitem(String item_CODE, String exam_charging_item_code, String logname) {
		String sql = "select id from charging_item where his_num='"+item_CODE+"' and item_code='"+exam_charging_item_code+"' ";
		TranLogTxt.liswriteEror_to_txt(logname, "查询收费项目信息:" + sql + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ChargingItemDTO.class);
		 ChargingItemDTO item = new ChargingItemDTO();
		if((map!=null)&&(map.getList().size()>0)){
			item= (ChargingItemDTO)map.getList().get(0);			
		}
		return item;
	}

	/**
	 * 查询人员基本信息
	 * @param exam_num
	 * @param logname
	 * @return
	 * @throws ServiceException
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.company,c.batch_id,c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "查询人员信息:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	
public ResultHeader Pro_send_sick_clinic_detail(ExamInfoUserDTO eu,WebserviceConfigurationDTO wcf,Fee fee, String logname) {
		
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
				System.err.println("==================55");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
				 CallableStatement call = conn.prepareCall("{call zhiydba.zhi4_peis_wjw.Pro_send_sick_clinic_detail(?,?,?,?,?,?,?,?,?,?)}");//执行存储过程
				 call.setString(1, eu.getPatient_id());//诊疗号码
				 call.setString(2, eu.getUser_name());//客户姓名
				 call.setString(3, fee.getITEM_CODE());//体检项目编码
				 call.setString(4, fee.getITEM_NAME());//体检项目名称
				 call.setString(5, fee.getCHARGES());//总金额
				 if(eu.getCompany().equals("") && eu.getCompany()==null){
					 call.setString(6, "");//单位名称
					 System.err.println("单位名称");
				 }else{
					 call.setString(6, eu.getCompany());//单位名称 
				 }
				
				 if(Long.valueOf(eu.getBatch_id()).equals("") && Long.valueOf(eu.getBatch_id()) == null){
					 call.setString(7, "");//批次
				 }else{
					 call.setLong(7, eu.getBatch_id());//批次
				 }
				 
				 
				 TranLogTxt.liswriteEror_to_txt(logname, "His收费申请入参:" +"诊疗号码:"+ eu.getPatient_id()+"姓名:"+eu.getUser_name()+"项目编码:"+fee.getITEM_CODE()+"\r\n");
			 	 TranLogTxt.liswriteEror_to_txt(logname, "His收费申请入参:" +"项目名称:"+ fee.getITEM_NAME()+"金额:"+fee.getCHARGES()+"批次:"+ eu.getBatch_id()+"单位名称  :"+eu.getCompany()+"\r\n");
				
				 
				 call.registerOutParameter(8, java.sql.Types.LONGVARCHAR);
				 call.registerOutParameter(9, java.sql.Types.FLOAT);
				 call.registerOutParameter(10,java.sql.Types.LONGVARCHAR);
				// 执行存储过程啊闪光灯
				call.execute();
				// 得到存储过程的输出参数值
				rh.setSourceMsgId(call.getString(8));
				
				rh.setTypeCode(call.getString(9));
				
				rh.setText(call.getString(10));
				 TranLogTxt.liswriteEror_to_txt(logname, "His收费申请输出:" +"流水号:"+ rh.getSourceMsgId()+"标识:"+rh.getTypeCode()+"描述:"+ rh.getText()+"\r\n");
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(e));
			rh.setTypeCode("-1");
		}
		return rh;
	}
	
}
