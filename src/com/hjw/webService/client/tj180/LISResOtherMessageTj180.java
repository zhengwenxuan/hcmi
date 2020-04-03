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

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.tj180.Bean.LisExamBody;
import com.hjw.webService.client.tj180.Bean.LisExamInfoResItemBean;
import com.hjw.webService.client.tj180.Bean.LisExamResBody;
import com.hjw.webService.client.tj180.Bean.LisGetReqBean;
import com.hjw.webService.client.tj180.Bean.LisGetResBean;
import com.hjw.webService.client.tj180.Bean.LisGetResItemBean;
import com.hjw.webService.client.tj180.Bean.LisLabSampleResBean;
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
public class LISResOtherMessageTj180{
   private static CommService commService;  
   private static JdbcQueryManager jdbcQueryManager;
   static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		commService = (CommService) wac.getBean("commService");
	}
	@SuppressWarnings("resource")
	public LISResOtherMessageTj180(){
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getOtherMessage(String url, String logname, String exam_num) {
		//exam_num="201801310013";
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + exam_num);
		//TranLogTxt.liswriteEror_to_txt(logname, "req:0.1");
		List<LisGetResItemBean> lgri= new ArrayList<LisGetResItemBean>();
		lgri = setLisAllRes(exam_num,url,logname);
		if(lgri.size()>0){
			setExamInfoRes(exam_num,lgri,logname);
			if(exam_num.indexOf("B")==0){
				exam_num=exam_num.substring(1,exam_num.length());
				setExamInfoRes(exam_num,lgri,logname);
			}
		}		
		return null;
	}
	
	public void setExamInfoRes(String exam_num,List<LisGetResItemBean> lgri,String logname){
		List<LisExamInfoResItemBean> lei=new ArrayList<LisExamInfoResItemBean>();
		lei=this.getExamInfoItemExamInfo(exam_num, logname);
		if(lei.size()>0){
			for(LisExamInfoResItemBean le:lei){
				for(LisGetResItemBean lri:lgri){
					System.out.println(le.getHis_num()+"  "+lri.getReportItemCode().trim());
					if(le.getHis_num().equals(lri.getReportItemCode().trim())){
						System.out.println("ok");
						ProcListResult plr = new ProcListResult();
							plr.setBar_code(le.getSamplebarcode());
						plr.setExam_num(exam_num);
						plr.setOld_exam_num(exam_num);
						plr.setLis_rep_item_code(lri.getReportItemCode());
						if(lri.getResult() == null || "".equals(lri.getResult())){
							if(lri.getReportItemName()!=null &&
									(lri.getReportItemName().trim().indexOf("阳性") != -1 || lri.getReportItemName().trim().indexOf("阴性") != -1
									|| lri.getReportItemName().trim().indexOf("弱阳性") != -1 || lri.getReportItemName().trim().indexOf("弱阴性") != -1
									|| lri.getReportItemName().trim().indexOf("-") != -1 || lri.getReportItemName().trim().indexOf("+") != -1)){
								plr.setExam_result(lri.getReportItemName());
							}
						}else{
							plr.setExam_result(lri.getResult());
						}
						
						plr.setRef_value(lri.getPrintContext());
						plr.setItem_unit(lri.getUnits());
						plr.setRef_indicator(lri.getAbnormalIndicator());

						// N：正常，L：偏低，H：偏高
						// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
//						if((lgri.getReportItemName()!=null)&&(lgri.getReportItemName().trim().indexOf("阳性")>1))
//						{
//							plr.setRef_indicator("3");
//						}else 
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
						if((lri.getTranscriptionist()==null)||("null".equals(lri.getTranscriptionist().toLowerCase()))){
							lri.setTranscriptionist("");
						}
						if((lri.getVerifiedBy()==null)||("null".equals(lri.getVerifiedBy().toLowerCase()))){
							lri.setVerifiedBy("");
						}
						plr.setExam_doctor(lri.getTranscriptionist());
						Date parse5 = parse5(lri.getResultDateTime());
						String datetime = shortFmt2(parse5);
						plr.setExam_date(datetime);
						if(lri.getVerifiedBy()==null||lri.getVerifiedBy().trim().length()<=0||"null".equals(lri.getVerifiedBy())){
							plr.setApprover(lri.getTranscriptionist());
						}
						plr.setApprove_date(datetime);
						int resflag = commService.doproc_Lis_result(plr);
					}
				}
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
				+ ",c.register_date,c.join_date,c.exam_times,ci.arch_num ");
		sb.append(" from exam_info c,customer_info ci"
				+ " where c.customer_id=ci.id  ");
		sb.append(" and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
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
	 * 得到此人的所有结果
	 * @param exam_num
	 * @param logname
	 */
	private List<LisGetResItemBean> setLisAllRes(String exam_num,String url,String logname){
		//TranLogTxt.liswriteEror_to_txt(logname, "req:0.2");
		List<LisGetResItemBean> lgri= new ArrayList<LisGetResItemBean>();
		ExamInfoUserDTO eu = new ExamInfoUserDTO();		
		//TranLogTxt.liswriteEror_to_txt(logname, "req:0.4");
		if(exam_num.indexOf("B")==0){
			exam_num=exam_num.substring(1,exam_num.length());		   
			//exam_id = eu.getId();
		}
		eu = this.getExamInfoForNum(exam_num, logname);
		LisExamBody body = new LisExamBody();
		body.setPatientId(eu.getArch_num());
		body.setStartDate(eu.getJoin_date().substring(0,10));
        body.setEndDate(DateTimeUtil.getDate2());
        
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("LIS_EXAMRESULT_SEND");
		String web_url = wcd.getConfig_url().trim();
		//String dahtype=wcd.getConfig_method().trim();//查看检查结果  1 体检180
		LisExamResultMessageTj180 mes = new LisExamResultMessageTj180();
		LisExamResBody rb = mes.getMessage(web_url, body, logname);
		
		if(rb.getStatus()!=null){
			if(rb.getSampleInfo()!=null && (rb.getSampleInfo().size()>0)){
				for(LisLabSampleResBean llr:rb.getSampleInfo()){					
					LisGetReqBean p = new LisGetReqBean();
					p.setTestNo(llr.getTESTNO());
					JSONObject json = JSONObject.fromObject(p);// 将java对象转换为json对象
					String str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
					String result = HttpUtil.doPost(url,p,"utf-8");	
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
					if ((result != null) && (result.trim().length() > 0)) {
						result = result.trim();
						JSONObject jsonobject = JSONObject.fromObject(result);
						Map classMap = new HashMap();
						classMap.put("labResultInfo", LisGetResItemBean.class);
						LisGetResBean resdah = new LisGetResBean();
						resdah = (LisGetResBean) JSONObject.toBean(jsonobject, LisGetResBean.class, classMap);
						if ("200".equals(resdah.getStatus())) {
							String joindata=eu.getJoin_date().substring(0, 10);
							joindata=joindata.replaceAll("-", "");
							long ljoin=Long.valueOf(joindata);
							for (LisGetResItemBean lgi : resdah.getLabResultInfo()) {
								try {
									long ejoin = Long.valueOf(lgi.getResultDateTime().substring(0, 8));// 20180514132019
									if (ejoin >= ljoin) {
										lgri.add(lgi);
									}
								}catch(Exception ex){
									ex.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
     return lgri;		
	}
	
	
	public List<LisExamInfoResItemBean> getExamInfoItemExamInfo(String exam_num,String logname){
		List<LisExamInfoResItemBean> lei=new ArrayList<LisExamInfoResItemBean>();
		Connection tjtmpconnect = null;
		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "  select ei.id as exam_id,ei.exam_num,eci.id as eci_id,eii.exam_num as his_num,ci.sam_demo_id "
					+ "from exam_info ei,examinfo_charging_item eci,charging_item ci,charging_item_exam_item ciei,examination_item eii"
					+ "  where ei.exam_num='"+exam_num+"' and ei.id=eci.examinfo_id and eci.charge_item_id=ci.id "
					+ " and ei.is_Active='Y' "
					//+ " and eci.exam_status='N' "
					+ " and eci.isActive='Y' "
					+ " and ci.id=ciei.charging_item_id"
					+ " and ci.isActive='Y' "
					+ "and ciei.exam_item_id=eii.id "
					+ "and eii.is_Active='Y' and (eii.exam_num is not null and len(eii.exam_num)>0) ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				try {
					String hisnumss = rs1.getString("his_num");
					if (hisnumss.indexOf("+") >= 0) {
						String[] hisnums = hisnumss.split("\\+");
						for (int i = 0; i < hisnums.length; i++) {
							LisExamInfoResItemBean lirib = new LisExamInfoResItemBean();
							lirib.setEci_id(rs1.getLong("eci_id"));
							lirib.setExam_id(rs1.getLong("exam_id"));
							lirib.setExam_num(rs1.getString("exam_num"));
							lirib.setHis_num(hisnums[i]);
							lirib.setSam_demo_id(rs1.getLong("sam_demo_id"));
							String sb2 = " select sed.sample_barcode from sample_exam_detail sed where sed.exam_info_id='"
									+ lirib.getExam_id()
									+ "' and sed.sample_id='"
									+ rs1.getLong("sam_demo_id") + "' ";
							TranLogTxt.liswriteEror_to_txt(logname,
									"res: :操作语句： " + sb2);
							ResultSet rs2 = tjtmpconnect.createStatement()
									.executeQuery(sb2);
							if (rs2.next()) {
								String samplebarcode = rs2
										.getString("sample_barcode");
								lirib.setSamplebarcode(samplebarcode);
							}
							rs2.close();
							lei.add(lirib);
						}
					} else {
						LisExamInfoResItemBean lirib = new LisExamInfoResItemBean();
						lirib.setEci_id(rs1.getLong("eci_id"));
						lirib.setExam_id(rs1.getLong("exam_id"));
						lirib.setExam_num(rs1.getString("exam_num"));
						lirib.setHis_num(hisnumss);
						lirib.setSam_demo_id(rs1.getLong("sam_demo_id"));
						String sb2 = " select sed.sample_barcode from sample_exam_detail sed where sed.exam_info_id='"
								+ lirib.getExam_id()
								+ "' and sed.sample_id='"
								+ rs1.getLong("sam_demo_id") + "' ";
						TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： "
								+ sb2);
						ResultSet rs2 = tjtmpconnect.createStatement()
								.executeQuery(sb2);
						if (rs2.next()) {
							String samplebarcode = rs2
									.getString("sample_barcode");
							lirib.setSamplebarcode(samplebarcode);
						}
						rs2.close();
						lei.add(lirib);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
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
		return lei;
	}
	
}
