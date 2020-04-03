package com.hjw.webService.client.ningyuan;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.bean.ResCustomBeanHK;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetHisJZKHaoNY {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private Custom custom=new Custom();
	private ResCustomBeanHK rb1= new ResCustomBeanHK();
	private static WebserviceConfigurationService webserviceConfigurationService;
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	
	public GetHisJZKHaoNY(Custom custom){
		this.custom=custom;
	}
	
	
	/**
	 * 
	 * @param url
	 * @param logname
	 * @return
	 */
	public ResultBody getMessage(String url,String configvalue,String logname) {
		ResultBody rb= new ResultBody();
		if(StringUtils.isEmpty(this.custom.getID_NO())) {
			this.custom.setID_NO(this.custom.getEXAM_NUM());
		}
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		long examid=examIdForExamNum(this.custom.getEXAM_NUM(),logname);
		if(examid<=0){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("无效体检编号");
		}else{
			try {				
				ResCustomBeanHK rcb= new ResCustomBeanHK();
				ResultHeader liushuihao = this.getHisZlkHao(logname,custom);
				 rcb = res_search(examid, logname);
				 TranLogTxt.liswriteEror_to_txt(logname, "查询本地是否已经有此数据:"+rcb.getCode());
				 if(rcb.getCode().equals("AE")){
					 if("0".equals(liushuihao.getTypeCode())){
						 TranLogTxt.liswriteEror_to_txt(logname, "his返回的标识:"+liushuihao.getTypeCode());
							insert_search(examid,custom.getEXAM_NUM(),liushuihao.getSourceMsgId(),logname);
							//插入表
							CustomResBean cus= new CustomResBean();
							cus.setCLINIC_NO(liushuihao.getSourceMsgId());
							cus.setPATIENT_ID(liushuihao.getSourceMsgId());
							cus.setVISIT_NO(liushuihao.getSourceMsgId());
							cus.setVISIT_DATE(DateTimeUtil.getDateTimes());
							List<CustomResBean> LIST=new ArrayList<CustomResBean>();
							LIST.add(cus);
							ControlActProcess ControlActProcess=new ControlActProcess();
							ControlActProcess.setLIST(LIST);
						    rb.setControlActProcess(ControlActProcess);
						    rb.getResultHeader().setTypeCode("AA");
						    rb.getResultHeader().setText(liushuihao.getText());
						
							
						}else{
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("查询接口返回错误");
						} 
				 }else{
					 	rb.getResultHeader().setTypeCode("AA");
				 }
				
			} catch (Exception ex){
			    rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("发送病人信息-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
				TranLogTxt.liswriteEror_to_txt(logname,"发送病人信息-调用错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}
		
		return rb;
	}
	
	
	
	

	public ResultHeader  getHisZlkHao(String logname, Custom custom){
		
		WebserviceConfigurationDTO wcf=new WebserviceConfigurationDTO();
		wcf=this.webserviceConfigurationService.getWebServiceConfig("PAYMENT_APPLICATION");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 															  
			 	CallableStatement  call = conn.prepareCall("{call zhiydba.zhi4_peis_wjw.pro_create_ic_card(?,?,?,?,?,?,?,?)}");//执行存储过程
			 	 TranLogTxt.liswriteEror_to_txt(logname, "获取流水号入参:" +"客户姓名:"+ custom.getNAME()+"性别:"+custom.getSEX()+"联系方式 :"+custom.getPHONE_NUMBER_HOME()+"\r\n");
			 	 TranLogTxt.liswriteEror_to_txt(logname, "获取流水号入参:" +"身份证号:"+ custom.getID_NO()+"地址:"+custom.getBIRTH_PLACE()+"出生日期 :"+custom.getDATE_OF_BIRTH()+"\r\n");
			 	
			 	
			 	call.setString(1, custom.getNAME());//客户姓名
				 
				 if(custom.getSEX().equals("") && custom.getSEX()!=null){
					 if(custom.getSEX().equals("男")){
						 custom.setSEX("0");
					 }else{
						 custom.setSEX("1");
					 }
				 }
				 call.setString(2, custom.getSEX());//性别
				 call.setString(3, custom.getPHONE_NUMBER_HOME());//联系方式 
				 call.setString(4, custom.getID_NO());//身份证号
				 call.setString(5, custom.getBIRTH_PLACE());//地址
				
				 
				 call.registerOutParameter(6, java.sql.Types.LONGVARCHAR);
				 call.registerOutParameter(7, java.sql.Types.FLOAT);
				 call.registerOutParameter(8,java.sql.Types.LONGVARCHAR);
				 
				// 执行存储过程啊闪光灯
				call.execute();
				// 得到存储过程的输出参数值
				
				
				rh.setSourceMsgId(call.getString(6));
				rh.setTypeCode(call.getString(7));//错误编码(0成功，-1失败)
				rh.setText(call.getString(8));//错误信息描述
			
			
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(e));
			rh.setTypeCode("-1");
		}
		 TranLogTxt.liswriteEror_to_txt(logname, "res  his系统返回的输出参数:" +"诊疗卡号:"+ rh.getSourceMsgId()+"成功失败标识:"+rh.getTypeCode()+"消息说明:"+rh.getText()+"\r\n");
		return rh;
	}
	
	
	
	/**
	 * 
	 * @param xmlmessage
	 * @return
	 */
	private ResCustomBeanHK res_search(long exam_id,String logname){
		ResCustomBeanHK rcb= new ResCustomBeanHK();
		rcb.setCode("AE");
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,zl_pat_id,exam_num,"
					+ "zl_mzh,zl_tjh,flag from zl_req_patInfo where exam_info_id='"
					+ exam_id + "'";
			 TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1 + "\r\n");
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				rcb.setCode("AA");
				rcb.setPersionid(rs1.getString("zl_pat_id"));
			}
			rs1.close();
        }catch(Exception ex){
        	rcb.setCode("AE");
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rcb;
	}
	
	/**
	 * 
	 * @param xmlmessage
	 * @return
	 */
	private void insert_search(long examid,String exam_num,String persion,String logname){
		long exam_id=examid;
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			
			String insertsql = "insert into zl_req_patInfo ( exam_info_id,zl_pat_id,exam_num,"
					+ "zl_mzh,zl_tjh,zl_djh,flag) values('" + exam_id + "','" + persion
					+ "','" + exam_num + "','" + persion + "','" + persion
					+ "','"+persion+"','0')";
			TranLogTxt.liswriteEror_to_txt(logname, "insert zl_req_patInfo_sql:" + insertsql + "\r\n");
			tjtmpconnect.createStatement().executeUpdate(insertsql);
        }catch(Exception ex){
			
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param exam_num
	 * @return
	 */
	public long examIdForExamNum(String exam_num,String logname){
		long exam_id=0;
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb = " select id from exam_info where exam_num='" + exam_num + "' and  is_Active='Y' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb + "\r\n");
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb);
			if (rs.next()) {
				exam_id = rs.getLong("id");
			}
			rs.close();
		}catch(Exception ex){
			
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return exam_id;
	}
	
}

