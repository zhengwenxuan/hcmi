package com.hjw.webService.client.sinosoft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.sinosoft.bean.ResLisBean;
import com.hjw.webService.client.sinosoft.webservice.AppIntegratorServer;
import com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerLocator;
import com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType;
import com.hjw.wst.DTO.ExamInfoUserDTO;
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
public class PACSDELSendMessageSinoSoft {
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static {
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");	
	}
	public PACSDELSendMessageSinoSoft(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url,String appKey, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (PacsComponents comps : lismessage.getComponents()) {
				boolean rhone = lisSendMessage(url, comps,appKey, logname);
				if (rhone) {
					ApplyNOBean an = new ApplyNOBean();					
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		return rb;
	}

	private boolean lisSendMessage(String url,PacsComponents comps,String appKey, String logname) {
		boolean flag = true;
		try{
			StringBuffer sb0 = new StringBuffer("");		
			sb0.append("<exchange>               ");
			sb0.append("<funcid>SD01.00.010.34</funcid>                  ");
			sb0.append("<group>                                          ");
			sb0.append("<HRC00.02>                                       ");
			sb0.append("<row>                                            ");
			sb0.append("<DE01.00.030.00>"+comps.getReq_no()+"</DE01.00.030.00>        ");
			sb0.append("<DE01.00.001.02>"+this.lismessage.getCustom().getArch_num()+"</DE01.00.001.02>         ");
			sb0.append("<DE02.01.060.00>3</DE02.01.060.00>        ");
			sb0.append("<DE02.01.044.03>1</DE02.01.044.03>        ");//1：取消申请单
			sb0.append("<DE04.30.018.00>1</DE04.30.018.00>        ");//1：医嘱作废（住院医嘱作废触发为该状态） 2：退费（医技退费操作）
			sb0.append("<DE02.01.039.01>"+this.lismessage.getDoctor().getDoctorName()+"</DE02.01.039.01>      ");
			sb0.append("<DE02.01.039.00>"+this.lismessage.getDoctor().getDoctorName()+"</DE02.01.039.00>      ");
			sb0.append("<DE06.00.095.00>" + DateTimeUtil.getDateTime() + "</DE06.00.095.00>        ");
			sb0.append("<DE06.00.289.00>D</DE06.00.289.00>      ");//D：检查 
			sb0.append("</row>                                           ");
			sb0.append("</HRC00.02>                                      ");
			sb0.append("<head>                                           ");
			sb0.append("<row>                                            ");
			sb0.append("<appKey>"+appKey+"</appKey>                 ");
			sb0.append("</row>                                           ");
			sb0.append("</head>                                          ");
			sb0.append("</group>                                         ");
			sb0.append("</exchange>                                    ");
			TranLogTxt.liswriteEror_to_txt(logname, "ret:" + sb0.toString());
			AppIntegratorServer ast = new AppIntegratorServerLocator(url);
			AppIntegratorServerPortType aspt = ast.getAppIntegratorServerHttpSoap11Endpoint();
			String result = aspt.HIPMessageServer("EVENT", sb0.toString());
			TranLogTxt.liswriteEror_to_txt(logname, result);
			ResLisBean rslb = JaxbUtil.converyToJavaBean(result, ResLisBean.class);
			if ("0".equals(rslb.getErrcode()))//// 1：成功 0：失败
			{
				flag = false;
			}
		}catch(Exception ex){
			flag = false;
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
	public ZlReqPacsItemDTO updatezl_req_item(String exam_num,String req_id,String ciid,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ZlReqPacsItemDTO zpcs = new ZlReqPacsItemDTO();
		ei=this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		long lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select id,req_id from zl_req_pacs_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_ids='"+ciid+"' and pacs_req_code='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				lisid=rs1.getLong("id");
				zpcs.setId(Integer.parseInt(lisid+""));
				zpcs.setReq_id(rs1.getString("req_id"));
				
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return zpcs;
	}
	
	/**
	 * 获取医嘱执行分类编码
	 * @param url
	 * @param view_num
	 * @return
	 */
	public String getOrderExecId(String cicode,String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		String eu="";
		PreparedStatement preparedStatement = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.remark from charging_item a where a.item_code='"+cicode+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				eu = rs1.getString("remark");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		if(eu==null){
			eu="0000";
		}else if(eu.trim().length()<=0){
			eu="0000";
		}else if("超声".equals(eu.trim())){
			eu="0101";
		}else if("CT".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("DR".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("MRI".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("内窥镜".equals(eu.trim().toUpperCase())){
			eu="0105";
		}else if("数字胃肠".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("病理".equals(eu.trim().toUpperCase())){
			eu="0104";
		}else if("PET".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("X线".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("乳腺".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("心电".equals(eu.trim().toUpperCase())){
			eu="0106";
		}else{
			eu="0000";
		}
		return eu;
	} 
}
