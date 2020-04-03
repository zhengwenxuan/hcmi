package com.hjw.webService.client.sinosoft;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.sinosoft.bean.ResLisBean;
import com.hjw.webService.client.sinosoft.webservice.AppIntegratorServer;
import com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerLocator;
import com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.7	检查申请撤销信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISDELSendMessageSinoSoft {
	private LisMessageBody lismessage;
	private static ConfigService configService;
    private static JdbcQueryManager jdbcQueryManager;
    private static CustomerInfoService customerInfoService;
    static {
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}
	public LISDELSendMessageSinoSoft(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url,String appKey, String logname) {
		ResultLisBody rb = new ResultLisBody();
		try {
			JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (LisComponents comps : lismessage.getComponents()) {
					boolean rhone = lisSendMessage(url,comps,appKey,logname);
					if(rhone){
						ApplyNOBean an = new ApplyNOBean();
						an.setApplyNO(comps.getReq_no());
						anList.add(an);
					}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex){
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logname, com.hjw.interfaces.util.StringUtil.formatException(ex));
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;
	}

	private boolean lisSendMessage(String url,LisComponents comps,String appKey,String logname) {
		// 1，调用his获取申请单号
		TranLogTxt.liswriteEror_to_txt(logname, "1，调用lis申请单-开始");
		boolean flag = true;
		for (LisComponent comp : comps.getItemList()) {
			ChargingItemDTO chargingItem = customerInfoService
					.getChargingItemForId(Long.valueOf(comp.getChargingItemid()));
			try {
				StringBuffer sb = new StringBuffer("");
				sb.append("<exchange>");
				sb.append("<funcid>SD01.00.010.37</funcid>");
				sb.append("<group>");
				sb.append("<HRC00.02>");
				sb.append("<row>");
				sb.append("<IMTECODE>" + comp.getItemCode()+ "</IMTECODE> ");
				sb.append("<ITEMNAME>" + comp.getItemName()+ "</ITEMNAME> ");

				sb.append("<OPPERSONID>" + this.lismessage.getDoctor().getDoctorCode() + "</OPPERSONID> ");
				sb.append("<OPPERSON>" + this.lismessage.getDoctor().getDept_name()+ "</OPPERSON> ");
				sb.append("<EXECTIME>" + DateTimeUtil.getDateTime() + "</EXECTIME> ");
				sb.append("<APPLYID>"+comps.getReq_no()+"</APPLYID> ");
				sb.append("<QTY></QTY> ");//退费数量 必填
				sb.append("<PATIENTID>"+this.lismessage.getCustom().getPersonid()+"</PATIENTID> ");
				sb.append("<PTYPE>体检</PTYPE> ");// 门诊、住院、体检
				sb.append("<ORDROWID>"+comps.getReq_no()+"</ORDROWID> ");//申请单号码
				sb.append("<OPTYPE>0</OPTYPE> ");//0 表示取消，1 表示退费
				
				sb.append("</row>");
				sb.append("</HRC00.02>");
				sb.append("<head>");
				sb.append("<row>");
				sb.append("<appKey>" + appKey + "</appKey>");// appKey -- 必填
				sb.append("</row>");
				sb.append("</head>");
				sb.append("</group>");
				sb.append("</exchange>");
				TranLogTxt.liswriteEror_to_txt(logname, "ret:" + sb.toString());
				AppIntegratorServer ast = new AppIntegratorServerLocator(url);
				AppIntegratorServerPortType aspt = ast.getAppIntegratorServerHttpSoap11Endpoint();
				String result = aspt.HIPMessageServer("EVENT", sb.toString());
				TranLogTxt.liswriteEror_to_txt(logname, result);
				ResLisBean rslb = JaxbUtil.converyToJavaBean(result, ResLisBean.class);
				if ("0".equals(rslb.getErrcode()))//// 1：成功 0：失败
				{
					flag = false;
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname, com.hjw.interfaces.util.StringUtil.formatException(ex));
				flag = false;
			}
		}
		return flag;
	}
	

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getHISDJH(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT zl_djh as others,zl_tjh as visit_no,zl_mzh as clinic_no FROM zl_req_patInfo where exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);	
		}
		return eu;
	} 
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	private ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times ");
		sb.append(" from exam_info c ");
		sb.append(" where c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public double getChargingAmt(String id) throws ServiceException {
		Connection tjtmpconnect = null;
		double lisitemid =0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select amount from charging_item a where id='"+id+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				lisitemid = rs1.getDouble("amount");
			}
			rs1.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisitemid;
	} 
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public long updatezl_req_item(String exam_num,String req_id,String ciid,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		long lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select id from zl_req_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_id='"+ciid+"' and req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				lisid=rs1.getLong("id");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisid;
	}

}
