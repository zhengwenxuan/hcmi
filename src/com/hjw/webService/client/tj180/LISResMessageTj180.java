package com.hjw.webService.client.tj180;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.CenterConfigurationDTO;
import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ExamPicMessage;
import com.hjw.webService.client.HisLabSampleMessage;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.tj180.Bean.HisLabSampleBody;
import com.hjw.webService.client.tj180.Bean.HisLabSampleResBean;
import com.hjw.webService.client.tj180.Bean.HisLabSampleResBody;
import com.hjw.webService.client.tj180.Bean.LisGetReqBean;
import com.hjw.webService.client.tj180.Bean.LisGetResBean;
import com.hjw.webService.client.tj180.Bean.LisGetResItemBean;
import com.hjw.webService.client.tj180.Bean.LisGetResItemOtherBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.CommService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISResMessageTj180{
   private static CommService commService;  
   private static JdbcQueryManager jdbcQueryManager;
   private static WebserviceConfigurationService webserviceConfigurationService;
   private static ConfigService configService;
   static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		webserviceConfigurationService= (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		commService = (CommService) wac.getBean("commService");
		configService = (ConfigService) wac.getBean("configService");
	}
	@SuppressWarnings("resource")
	public LISResMessageTj180(){
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname, String exam_num) {
		ResultLisBody rb = new ResultLisBody();
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + exam_num);
		//TranLogTxt.liswriteEror_to_txt(logname, "req:0.1");
		setZL_REQ_ITEM(exam_num,logname);
		//TranLogTxt.liswriteEror_to_txt(logname, "req:1.0");
		try {
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu = this.getExamInfoForNum(exam_num,logname);
			long exam_id = eu.getId();			
			ExamInfoUserDTO oldeu = eu;
			long oldexam_id =eu.getId();
			String oldexam_num=exam_num;
			
			boolean isbjflag=false;
			
			if(oldexam_num.indexOf("B")==0){				
				oldexam_num=oldexam_num.substring(1,oldexam_num.length());				
				oldeu = this.getExamInfoForNum(oldexam_num,logname);
				oldexam_id = oldeu.getId();
				isbjflag=true;
			}
			
			List<ZlReqItemDTO> zris = new ArrayList<ZlReqItemDTO>();
			zris = this.getzl_req_item(oldexam_id, logname);
			
			for (ZlReqItemDTO zri : zris) {
				try {
					LisGetReqBean p = new LisGetReqBean();
					p.setTestNo(zri.getReq_id());
					JSONObject json = JSONObject.fromObject(p);// 将java对象转换为json对象
					String str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
					String result = HttpUtil.doPost(url,p,"utf-8");
					
					/*LisGetResBean o = new LisGetResBean();
					LisGetResItemBean y = new LisGetResItemBean();
					y.setAbnormalIndicator("L");
				    y.setPrintContext("11.8-17.2");
				    y.setReportItemCode("1118");
				    y.setReportItemName("血小板体积分布宽度");
				    y.setResult("15.55");
				    y.setResultDateTime("20151012092112");
				    y.setTestNo("180111640853");
				    y.setTranscriptionist(null);
				    y.setUnits("%");
				    o.getLabResultInfo().add(y);
				    o.setErrorinfo("");
				    o.setStatus("200");								    
				    json = JSONObject.fromObject(o);// 将java对象转换为json对象
				     result = json.toString();// 将json对象转换为字符串*/
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
		            if((result!=null)&&(result.trim().length()>0)){	   
							result = result.trim();
							JSONObject jsonobject = JSONObject.fromObject(result);
							Map classMap = new HashMap();
							classMap.put("labResultInfo", LisGetResItemBean.class);
							LisGetResBean resdah = new LisGetResBean();
							resdah = (LisGetResBean) JSONObject.toBean(jsonobject, LisGetResBean.class, classMap);
							if ("200".equals(resdah.getStatus())) {
								ProcListResult plr = new ProcListResult();
								plr.setBar_code(zri.getLis_req_code());
								plr.setExam_num(oldexam_num);
								//plr.setOld_exam_num(exam_num);

								for (LisGetResItemBean lgri : resdah.getLabResultInfo()) {
									// N：正常，L：偏低，H：偏高
									// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
									//plr.setLis_item_code(lgri.getReportItemCode());
									plr.setLis_rep_item_code(lgri.getReportItemCode());
//									if((lgri.getReportItemName()!=null)&&(lgri.getReportItemName().trim().indexOf("阳性")>1))
//									{
//										plr.setExam_result(lgri.getReportItemName()+" "+lgri.getResult());
//									}else{
//										plr.setExam_result(lgri.getResult());
//									}
									if(lgri.getResult() == null || "".equals(lgri.getResult())){
										if(lgri.getReportItemName()!=null &&
												(lgri.getReportItemName().trim().indexOf("阳性") != -1 || lgri.getReportItemName().trim().indexOf("阴性") != -1
												|| lgri.getReportItemName().trim().indexOf("弱阳性") != -1 || lgri.getReportItemName().trim().indexOf("弱阴性") != -1
												|| lgri.getReportItemName().trim().indexOf("-") != -1 || lgri.getReportItemName().trim().indexOf("+") != -1)){
											plr.setExam_result(lgri.getReportItemName());
										}else{
											continue;
										}
									}else{
										plr.setExam_result(lgri.getResult());
									}
									
									plr.setRef_value(lgri.getPrintContext());
									plr.setItem_unit(lgri.getUnits());
									plr.setRef_indicator(lgri.getAbnormalIndicator());

									// N：正常，L：偏低，H：偏高
									// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
//									if((lgri.getReportItemName()!=null)&&(lgri.getReportItemName().trim().indexOf("阳性")>1))
//									{
//										plr.setRef_indicator("3");
//									}else 
									if ("H".equals(plr.getRef_indicator())) {
										plr.setRef_indicator("1");
									} else if ("L".equals(plr.getRef_indicator())) {
										plr.setRef_indicator("2");
									} else if ("N".equals(plr.getRef_indicator())) {
										plr.setRef_indicator("0");
									} else {
										if(plr.getExam_result().indexOf("阳性") != -1 
												|| plr.getExam_result().indexOf("弱阳性") != -1 
												|| plr.getExam_result().indexOf("+") != -1){
											plr.setRef_indicator("3");
										}else{
											plr.setRef_indicator("0");
										}
									}
									if((lgri.getTranscriptionist()==null)||("null".equals(lgri.getTranscriptionist().toLowerCase()))){
										lgri.setTranscriptionist("");
									}
									if((lgri.getVerifiedBy()==null)||("null".equals(lgri.getVerifiedBy().toLowerCase()))){
										lgri.setVerifiedBy("");
									}
									plr.setExam_doctor(lgri.getTranscriptionist());
									Date parse5 = parse5(lgri.getResultDateTime());
									String datetime = shortFmt2(parse5);
									plr.setExam_date(datetime);
									if(lgri.getVerifiedBy()==null||lgri.getVerifiedBy().trim().length()<=0||"null".equals(lgri.getVerifiedBy())){
										plr.setApprover(lgri.getTranscriptionist());
									}
									plr.setApprover(lgri.getVerifiedBy());
									plr.setApprove_date(datetime);
									int resflag = commService.doproc_Lis_result(plr);
									TranLogTxt.liswriteEror_to_txt(logname, "res:1.000=" + zri.getCharging_item_id());
									TranLogTxt.liswriteEror_to_txt(logname, "res:1.00=" + zri.getCharging_item_id());
									 if("506".equals(zri.getCharging_item_id())){	
										TranLogTxt.liswriteEror_to_txt(logname, "res:1.0=" + zri.getCharging_item_id());
							            getLisOther(oldexam_id,zri.getCharging_item_id(),2134,lgri.getInspectionId(),logname);
							           }
									if(isbjflag){
										ProcListResult plrnew = new ProcListResult();
										plrnew.setBar_code(getReqNO(exam_id,plr.getLis_rep_item_code(),logname));
										plrnew.setExam_num(exam_num);										
										plrnew.setLis_rep_item_code(plr.getLis_rep_item_code());
										plrnew.setExam_result(plr.getExam_result());
										plrnew.setRef_value(plr.getRef_value());
										plrnew.setItem_unit(plr.getItem_unit());
										plrnew.setRef_indicator(plr.getRef_indicator());
										plrnew.setExam_doctor(plr.getExam_doctor());
										plrnew.setExam_date(plr.getExam_date());
										plrnew.setApprover(plr.getApprover());
										plrnew.setApprove_date(plr.getApprove_date());
										TranLogTxt.liswriteEror_to_txt(logname, "res:1.000=" + zri.getCharging_item_id());
										commService.doproc_Lis_result(plrnew);
										TranLogTxt.liswriteEror_to_txt(logname, "res:1.00=" + zri.getCharging_item_id());
									if ("506".equals(zri.getCharging_item_id())) {
										TranLogTxt.liswriteEror_to_txt(logname, "res:1.0=" + zri.getCharging_item_id());
										getLisOther(exam_id, zri.getCharging_item_id(), 2134, lgri.getInspectionId(),
												logname);
									}
									}
									TranLogTxt.liswriteEror_to_txt(logname, "res:存储过程返回=" + resflag);
								}
							}
						}
		           
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
			rb.getResultHeader().setTypeCode("AA");
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("lis调用webservice错误");
		}
		CenterConfigurationDTO cc=new CenterConfigurationDTO();
		cc=configService.getCenterconfigByKey("IS_EXAMPIC_RESOURCE");
		String IS_EXAMPIC_RESOURCE= cc.getConfig_value().trim();//
		if ("Y".equals(IS_EXAMPIC_RESOURCE)) {
			ExamPicMessage epm = new ExamPicMessage();
			epm.send(exam_num);
		}
		return rb;
	}
	
	/**
	 * 
	 * @param examid 体检编号
	 * @param item_id 细项id
	 * @param spid 交易流水号
	 * @param logname
	 */
	private void getLisOther(long examid,String charging_item_id,long item_id,String spid,String logname){
		TranLogTxt.liswriteEror_to_txt(logname, "res:1.2");
		Connection tjtmpconnect = null;
		try{
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("LIS_OTHER_URL");
		String web_url = wcd.getConfig_url().trim();		
		web_url=web_url+"?INSPECTION_ID="+spid;
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + web_url);		
		String result = HttpUtil.doGet(web_url,"utf-8");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
        if((result!=null)&&(result.trim().length()>0)){	  
        	result = result.trim();
			JSONObject jsonobject = JSONObject.fromObject(result);
			LisGetResItemOtherBean resdah = new LisGetResItemOtherBean();
			resdah = (LisGetResItemOtherBean) JSONObject.toBean(jsonobject, LisGetResItemOtherBean.class);
        	if(resdah!=null&&(resdah.getDIAGNOSIS_ADVICE()!=null||resdah.getTEG()!=null)){
        		String res="";
					if (resdah.getDIAGNOSIS_ADVICE() != null && resdah.getDIAGNOSIS_ADVICE().trim().length() > 0) {
						res = resdah.getDIAGNOSIS_ADVICE().trim();
						if (resdah.getTEG() != null && resdah.getTEG().trim().length() > 0) {
							res = res + "\r\n" + resdah.getTEG().trim();
						}
					} else {
						if (resdah.getTEG() != null && resdah.getTEG().trim().length() > 0) {
							res = resdah.getTEG().trim();
						}
					}
        		tjtmpconnect = this.jdbcQueryManager.getConnection();
    			String sb1 = "select id from exam_result_detail where exam_info_id='"+examid+"' and exam_item_id='"+item_id+"' ";
    			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
    			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
    			if (!rs1.next()) {    				
                   String insertsql ="insert into exam_result_detail(exam_info_id, exam_item_id, exam_doctor, exam_date, "
                   		+ "exam_result, ref_indicator, center_num, approver, approve_date, charging_item_id) "
                   		+ "values('"+examid+"','"+item_id+"','系统','"+DateTimeUtil.getDateTime()+"','"+res
                   		+"','0','001','系统','"+DateTimeUtil.getDateTime()+"', '"+charging_item_id+"')";
                   TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +insertsql);
					tjtmpconnect.createStatement().executeUpdate(insertsql);
    			}else{
    				long id= rs1.getLong("id");
    				 String insertsql ="update exam_result_detail set exam_info_id='"+examid+"', exam_item_id='"
    			                +item_id+"', exam_doctor='系统', exam_date='"+DateTimeUtil.getDateTime()+"', "
    	                   		+ "exam_result='"+res+"', ref_indicator='0', center_num='001', approver='系统', approve_date='"+
    			              DateTimeUtil.getDateTime()+"', charging_item_id='"+charging_item_id+"' where id='"+id+"' ";
    	            TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +insertsql);
    						tjtmpconnect.createStatement().executeUpdate(insertsql);
    			}
    			rs1.close();
        	}
        }
		} catch(Exception ex){
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex)+"\r\n");
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
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times ");
		sb.append(" from exam_info c ");
		sb.append(" where c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	public List<ZlReqItemDTO> getzl_req_item(long examInfoId,String logname) throws Exception{
		Connection tjtmpconnect = null;
		List<ZlReqItemDTO> req_ids= new ArrayList<ZlReqItemDTO>();
		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select req_id,charging_item_id,lis_item_id,lis_req_code from zl_req_item where exam_info_id='" + examInfoId + "' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				ZlReqItemDTO ri= new ZlReqItemDTO();
				ri.setReq_id(rs1.getString("req_id"));
				ri.setCharging_item_id(rs1.getString("charging_item_id"));
				ri.setLis_item_id(rs1.getString("lis_item_id"));
				ri.setLis_req_code(rs1.getString("lis_req_code"));
				req_ids.add(ri);
			} 
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return req_ids;
	}

	public static Date parse5(String param) {
		Date date = null;
		if ((param != null) && (param.trim().length() == 14)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			try {
				date = sdf.parse(param);
			} catch (ParseException ex) {
			}
		}
		return date;
	}
	
	public static String shortFmt2(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 * 申请单号转换
	 * @param exam_num
	 * @param logname
	 */
	private void setZL_REQ_ITEM(String exam_num,String logname){
		//TranLogTxt.liswriteEror_to_txt(logname, "req:0.2");
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		eu = this.getExamInfoForNum(exam_num, logname);
		long exam_id = eu.getId();
		//TranLogTxt.liswriteEror_to_txt(logname, "req:0.3");
		ExamInfoUserDTO oldeu = eu;
		String oldexam_num=exam_num;
		//TranLogTxt.liswriteEror_to_txt(logname, "req:0.4");
		if(oldexam_num.indexOf("B")==0){
			exam_num=oldexam_num.substring(1,oldexam_num.length());
		    eu = this.getExamInfoForNum(oldexam_num, logname);
			exam_id = oldeu.getId();
		}		
		//TranLogTxt.liswriteEror_to_txt(logname, "req:0.5");
		HisLabSampleBody hsb= new HisLabSampleBody();
		hsb.setReserveId(exam_num);
		HisLabSampleMessage hsm= new HisLabSampleMessage();
		HisLabSampleResBody hlr= new HisLabSampleResBody();
		//TranLogTxt.liswriteEror_to_txt(logname, "req:0.6");
		hlr=hsm.sampleSend(hsb);
		TranLogTxt.liswriteEror_to_txt(logname, "1.1--"+hlr.getStatus());
		if ("AA".equals(hlr.getStatus())) {
			//TranLogTxt.liswriteEror_to_txt(logname, "1.2");
			if ((hlr.getSampleInfo() != null) && (hlr.getSampleInfo().size() > 0)) {
				//TranLogTxt.liswriteEror_to_txt(logname, "1.3");
				for (HisLabSampleResBean sinfo : hlr.getSampleInfo()) {
					//TranLogTxt.liswriteEror_to_txt(logname, "1.4");
					String item_code = sinfo.getUNIONPROJECTID();
					String req_id = sinfo.getTESTNO();
					//TranLogTxt.liswriteEror_to_txt(logname, "1.5"+item_code+"  "+req_id);
					if (!getzl_req_item(exam_id, item_code, req_id, logname)) {
						//TranLogTxt.liswriteEror_to_txt(logname, "1.6"+item_code+"  "+req_id+"  "+exam_id);
						insertzl_req_item(exam_id, item_code, req_id, logname);
					}
				}
			}
		}
		
	}
	
	public String getReqNO(long exam_id,String lisitemid,String logname){		
		Connection tjtmpconnect = null;
		String req_ids= "";		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select s.sample_barcode, ci.id from examinfo_charging_item eci, charging_item ci, charging_item_exam_item cei," +
					"examination_item ei, examination_item_vs_lis l, sample_exam_detail s, examResult_chargingItem eri" +
					" where eci.examinfo_id="+exam_id+" and eci.charge_item_id=ci.id and ei.id=l.exam_item_id " +
							"and eci.charge_item_id=cei.charging_item_id and cei.exam_item_id=ei.id " +
							"and s.exam_info_id=eci.examinfo_id and s.id=eri.exam_id and eri.result_type='sample' " +
							"and eri.charging_id=eci.charge_item_id and l.lis_item_id='"+lisitemid+"' and eci.isActive='Y' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				req_ids=rs1.getString("sample_barcode");
			} 
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return req_ids;
	}
	
	public boolean getzl_req_item(long examInfoId,String item_code,String req_id,String logname){
		Connection tjtmpconnect = null;
		boolean req_ids= false;		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select ci.item_code,* from zl_req_item zr,charging_item ci "
					+ "where  ci.id=zr.charging_item_id and zr.exam_info_id='"+examInfoId
					+"' and ci.item_code='"+item_code+"' and zr.req_id='"+req_id+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				req_ids=true;
			} 
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return req_ids;
	}
	
	
	public boolean insertzl_req_item(long examInfoId,String item_code,String req_id,String logname){
		Connection tjtmpconnect = null;
		boolean req_ids= false;		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select ci.id,ci.sam_demo_id,ci.hiscodeClass from charging_item  ci where ci.item_code='"+item_code+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				long item_id=rs1.getLong("id");
				String samdemo_id=rs1.getString("sam_demo_id");
				String hisCodeClass= rs1.getString("hiscodeClass");				
				
				String sb2 = " select sed.sample_barcode from sample_exam_detail sed where sed.exam_info_id='"
				+examInfoId+"' and sed.sample_id='"+samdemo_id+"' ";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb2);
				ResultSet rs2 = tjtmpconnect.createStatement().executeQuery(sb2);
				if (rs2.next()) {
					String samplebarcode=rs2.getString("sample_barcode");	
					String lis_item_id="";
					if ("2".equals(hisCodeClass)) {
						String sb3 = "  select hci.id from charging_item ci,his_clinic_item hci where ci.item_code='"
								+ item_code + "' and ci.his_num=hci.id ";
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb3);
						ResultSet rs3 = tjtmpconnect.createStatement().executeQuery(sb3);
						if (rs3.next()) {
							lis_item_id = rs3.getString("id");
						}
						rs3.close();
					} else {
						String sb3 = "select hci.id from charging_item ci,his_price_list hci where ci.item_code='"
								+ item_code + "' and ci.his_num=hci.id ";
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb3);
						ResultSet rs3 = tjtmpconnect.createStatement().executeQuery(sb3);
						if (rs3.next()) {
							lis_item_id = rs3.getString("id");
						}
						rs3.close();
					}
					
					if(lis_item_id.trim().length()>0){
						String insertsql = "insert into zl_req_item(exam_info_id,charging_item_id,lis_item_id,lis_req_code,"
								+ "req_id,createdate) values('" + examInfoId + "','" + item_id + "','"+lis_item_id+"','"
								+samplebarcode+"','"+req_id+"','"+DateTimeUtil.getDateTime()+"')";
							TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +insertsql);
							tjtmpconnect.createStatement().executeUpdate(insertsql);
					}
				} 
				rs2.close();
			} 
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return req_ids;
	}	
}
