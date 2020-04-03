package com.hjw.webService.client.dashiqiao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.interfaces.util.LockCenterDateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.webService.client.dashiqiao.ResCusBean.CustomrDSQ;
import com.hjw.webService.client.dashiqiao.ResCusBean.ResCustomBeanDSQ;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class CUSTOMSendMessageDSQ {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static CustomerInfoService customerInfoService;
	private Custom custom=new Custom();
	private ResCustomBeanDSQ rb1= new ResCustomBeanDSQ();
	
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}
	
	public CUSTOMSendMessageDSQ(Custom custom){
		this.custom=custom;
	}
	
	
	/**
	 * 
	 * @param url
	 * @param logname
	 * @return
	 */
	public ResultBody getMessage(String url,String configvalue,String logname) {
		if(StringUtils.isEmpty(this.custom.getID_NO())) {
			this.custom.setID_NO(this.custom.getEXAM_NUM());
		}
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		long examid=examIdForExamNum(this.custom.getEXAM_NUM(),logname);
		ResultBody rb= new ResultBody();
		if(examid<=0){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("无效体检编号");
		}else{
			try {				
			ResHdMeessage rhd = LockCenterDateUtil.SetEaminatioinCenterDate(2020, Calendar.JANUARY, 25, logname);
			System.err.println(rhd.getStatus()+"===日期");
			if(rhd.getStatus().equals("AE")){
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(rhd.getMessage());
				return rb;
			}else{
						ResHdMeessage rh = setSearchString(url,this.custom,logname);
						if(rh.getStatus().equals("1")){
							//插入表
							if(rh.getResourceId().equals("0") ){
								System.err.println(this.custom.getPATIENT_ID()+"======页面查询时候用的患者id=======");
								System.err.println("个人返回的为零:"+rh.getResourceId());
								
								insert_search(examid,custom.getEXAM_NUM(),this.custom.getPATIENT_ID(),logname);
								
								CustomResBean cus= new CustomResBean();
								cus.setCLINIC_NO(this.custom.getPATIENT_ID());
								cus.setPATIENT_ID(this.custom.getPATIENT_ID());
								cus.setVISIT_NO(this.custom.getPATIENT_ID());
								cus.setVISIT_DATE(DateTimeUtil.getDateTimes());
								List<CustomResBean> LIST=new ArrayList<CustomResBean>();
								LIST.add(cus);
								ControlActProcess ControlActProcess=new ControlActProcess();
								ControlActProcess.setLIST(LIST);
							    rb.setControlActProcess(ControlActProcess);
							    rb.getResultHeader().setTypeCode("AA");
							    rb.getResultHeader().setText("ok-新增人员成功!");
							
							}else{
								System.err.println(this.custom.getPATIENT_ID()+"======页面查询时候用的患者id=======");
								System.err.println("团体返回的为正确的值:"+rh.getResourceId());
								
								insert_search(examid,custom.getEXAM_NUM(),rh.getResourceId(),logname);
								
								CustomResBean cus= new CustomResBean();
								cus.setCLINIC_NO(rh.getResourceId());
								cus.setPATIENT_ID(rh.getResourceId());
								cus.setVISIT_NO(rh.getResourceId());
								cus.setVISIT_DATE(DateTimeUtil.getDateTimes());
								List<CustomResBean> LIST=new ArrayList<CustomResBean>();
								LIST.add(cus);
								ControlActProcess ControlActProcess=new ControlActProcess();
								ControlActProcess.setLIST(LIST);
							    rb.setControlActProcess(ControlActProcess);
							    rb.getResultHeader().setTypeCode("AA");
							    rb.getResultHeader().setText("ok-新增人员成功!");
							
							}
							
						}else{
							
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("erroe-未收到平台返回的就诊号!!");
						}
			}		
						
			} catch (Exception ex){
			    rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("error-发送病人信息,平台返回就诊号失败");
				TranLogTxt.liswriteEror_to_txt(logname,"发送病人信息-调用错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}
		
		return rb;
	}
	
			/**
			 * 新增
			 * @return
			 */
	private ResHdMeessage setSearchString(String url,Custom custom,String logname){
		ResHdMeessage rh = new ResHdMeessage();
		ExamInfoUserDTO eu = getExamInfoForNum(custom.getEXAM_NUM(), logname);
		
		CustomrDSQ customrDSQ = new  CustomrDSQ();
		customrDSQ.setMSG_TYPE("");
		customrDSQ.setPATIENT_ID(eu.getArch_num());
		customrDSQ.setEXAM_NUM(custom.getEXAM_NUM());
		customrDSQ.setNAME(custom.getNAME());
		customrDSQ.setNAME_PHONETIC(custom.getNAME_PHONETIC());
		customrDSQ.setSEX(custom.getSEX());
		customrDSQ.setDATE_OF_BIRTH(custom.getDATE_OF_BIRTH());
		customrDSQ.setBIRTH_PLACE(custom.getBIRTH_PLACE());
		customrDSQ.setCITIZENSHIP(custom.getCITIZENSHIP());
		customrDSQ.setNATION(custom.getNATION());
		customrDSQ.setIDENTITY(custom.getIDENTITY());
		customrDSQ.setID_NO(custom.getID_NO());
		customrDSQ.setIDENTITY(custom.getIDENTITY());
		customrDSQ.setUNIT_IN_CONTRACT(custom.getUNIT_IN_CONTRACT());
		customrDSQ.setMAILING_ADDRESS(custom.getMAILING_ADDRESS());
		customrDSQ.setZIP_CODE(custom.getZIP_CODE());
		customrDSQ.setPHONE_NUMBER_HOME(custom.getPHONE_NUMBER_HOME());
		customrDSQ.setPHONE_NUMBER_BUSINESS(custom.getPHONE_NUMBER_BUSINESS());
		customrDSQ.setOPERATOR(custom.getOPERATOR());
		customrDSQ.setBUSINESS_ZIP_CODE(custom.getBUSINESS_ZIP_CODE());
		customrDSQ.setPHOTO(custom.getPHOTO());
		customrDSQ.setPATIENT_CLASS(custom.getPATIENT_CLASS());
		customrDSQ.setDEGREE(custom.getDEGREE());
		customrDSQ.setE_NAME(custom.getE_NAME());
		customrDSQ.setOCCUPATION(custom.getOCCUPATION());
		customrDSQ.setNATIVE_PLACE(custom.getNATIVE_PLACE());
		customrDSQ.setMAILING_ADDRESS_CODE(custom.getMAILING_ADDRESS_CODE());
		customrDSQ.setMAILING_STREET_CODE(custom.getMAILING_STREET_CODE());
		customrDSQ.setALERGY(custom.getALERGY());
		customrDSQ.setMARITAL_STATUS(custom.getMARITAL_STATUS());
		customrDSQ.setNEXT_OF_SEX(custom.getNEXT_OF_SEX());
		customrDSQ.setNEXT_OF_BATH(custom.getNEXT_OF_BATH());
		customrDSQ.setNEXT_OF_ID(custom.getNEXT_OF_ID());
		customrDSQ.setAGE(custom.getAGE());
		customrDSQ.setCHARGE_TYPE(custom.getCHARGE_TYPE());
		customrDSQ.setVISIT_DEPT(custom.getVISIT_DEPT());
		customrDSQ.setOPERATORS(custom.getOPERATORS());
		customrDSQ.setCARD_NAME(custom.getCARD_NAME());
		customrDSQ.setCARD_NO(custom.getCARD_NO());
		customrDSQ.setINVOICE_NO(custom.getINVOICE_NO());
		customrDSQ.setCLINIC_NO(custom.getPATIENT_ID());
		customrDSQ.setCLINIC_DATE_SCHEDULED(custom.getCLINIC_DATE_SCHEDULED());
		
		
		String json = new Gson().toJson(customrDSQ, CustomrDSQ.class);
		TranLogTxt.liswriteEror_to_txt(logname,"request:"+json);
		try {
			
			String result = HttpUtil.doPost_Str(url, json, "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
			if(result.length()>5 && result !=null && !result.equals("")){
				System.err.println("人员挂号新增正常返回数据了");
				 rh = new Gson().fromJson(result, ResHdMeessage.class);
			}else{
				rh.setStatus("AE");
			}
		}catch (Exception ex){	
			rh.setStatus("AE");
			TranLogTxt.liswriteEror_to_txt(logname,"发送病人信息-调用错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		
		
		
		return rh;
	}
	
	
	/**
	 * 
	 * @param xmlmessage
	 * @return
	 */
	private ResCustomBeanDSQ res_search(long exam_id,String logname){
		ResCustomBeanDSQ rcb= new ResCustomBeanDSQ();
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
			
			
			
			String sb1 = "select * from zl_req_patInfo where exam_info_id='"+exam_id+"' "
					+ " and zl_pat_id='"+persion+"' and exam_num='"+exam_num+"'   ";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if(!rs1.next()){
				String insertsql = "insert into zl_req_patInfo ( exam_info_id,zl_pat_id,exam_num,"
						+ "zl_mzh,zl_tjh,zl_djh,flag) values('" + exam_id + "','" + persion
						+ "','" + exam_num + "','" + persion + "','" + persion
						+ "','"+persion+"','0')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + insertsql + "\r\n");
				tjtmpconnect.createStatement().executeUpdate(insertsql);
			}
			
			rs1.close();
			
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
			String sb = "select id from exam_info where exam_num='" + exam_num + "'";
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
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
}
