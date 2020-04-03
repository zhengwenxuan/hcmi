package com.hjw.webService.client.sinosoft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.DTO.ZlReqPatinfoDTO;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.sinosoft.bean.ResLisBean;
import com.hjw.webService.client.sinosoft.webservice.AppIntegratorServer;
import com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerLocator;
import com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.MenuDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PACSSendMessageSinosoft{
	private PacsMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSSendMessageSinosoft(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String appKey, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname,
					"lismessage:" + new Gson().toJson(lismessage, PacsMessageBody.class));
			TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
			String exam_num = lismessage.getCustom().getExam_num();
			if (StringUtil.isEmpty(exam_num)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {

				ControlActPacsProcess ca = new ControlActPacsProcess();
				List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
				boolean rhone = pacsSendMessage(url, lismessage.getComponents(), appKey, logname);
				if (rhone) {
				for (PacsComponents pcs : lismessage.getComponents()) {
							ApplyNOBean ab = new ApplyNOBean();
							ab.setApplyNO(pcs.getReq_no());
							list.add(ab);
				}
				}
				if (list.size() > 0) {
					ca.setList(list);
					rb.setControlActProcess(ca);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("pacs调用成功");
				}
			}
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "ret:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
	
	
	private boolean pacsSendMessage(String url,List<PacsComponents> pacslist,String appKey,String logname) {
		
		//1，调用his获取申请单号
		TranLogTxt.liswriteEror_to_txt(logname, "1，调用pacs申请-开始");
		boolean flag = true;
		try {
	    	StringBuffer sb0=new StringBuffer();	                                                                                                                 
			sb0.append("<exchange>");
			sb0.append("<funcid>SD08.00.001.04</funcid>                               ");
			sb0.append("<group>                                                 ");
			sb0.append("<HRC00.02>                                              ");

			for (PacsComponents compts :pacslist) {
			for(PacsComponent pcs : compts.getPacsComponent()) {		    
			sb0.append("<row>                                                   ");
			sb0.append("<DE01.00.014.01>"+this.lismessage.getCustom().getArch_num()+"</DE01.00.014.01>                 ");
			sb0.append("<DE01.00.010.15></DE01.00.010.15>           ");//个人ID号版本 未知
			sb0.append("<DE01.00.010.16></DE01.00.010.16>           ");//个人ID号类别 未知
			sb0.append("<DE02.01.039.00>"+this.lismessage.getCustom().getName()+"</DE02.01.039.00>                   ");
			sb0.append("<DE02.01.040.00>"+this.lismessage.getCustom().getSexname()+"</DE02.01.040.00>                   ");
			sb0.append("<DE02.01.026.00>"+this.lismessage.getCustom().getOld()+"</DE02.01.026.00>                   ");
			sb0.append("<DE02.01.005.01>"+DateUtil.shortFmt3(DateUtil.strToDateLong2(this.lismessage.getCustom().getBirthtime()))+"</DE02.01.005.01>               ");
			sb0.append("<DE01.00.021.00></DE01.00.021.00>               ");
			sb0.append("<DE02.01.031.00>01</DE02.01.031.00>       ");//患者身份证件所属类别在特定编码体系中的代码
			sb0.append("<DE02.01.030.00></DE02.01.030.00>       ");
			sb0.append("<DE02.01.010.00>"+this.lismessage.getCustom().getTel()+"</DE02.01.010.00>               ");
			sb0.append("<DE02.01.060.00>体检</DE02.01.060.00>               ");//门诊、住院、体检
			sb0.append("<DE01.00.008.00>"+compts.getReq_no()+"</DE01.00.008.00>               ");
			sb0.append("<DE52.01.401.00></DE52.01.401.00>               ");//检查目的 未知
			sb0.append("<DE02.01.039.01>"+this.lismessage.getDoctor().getDoctorName()+"</DE02.01.039.01>               ");
			sb0.append("<DE08.10.026.00>"+this.lismessage.getDoctor().getDept_name()+"</DE08.10.026.00>               ");
			sb0.append("<DE08.10.026.01>" +pcs.getExam_class() + " </DE08.10.026.01>               ");
			sb0.append("<DE53.99.402.00></DE53.99.402.00>                 ");
			sb0.append("<DE06.00.220.00>"+DateUtil.getDate2()+"</DE06.00.220.00>               ");
			sb0.append("<DE01.00.010.00></DE01.00.010.00>                 ");
			sb0.append("<DE01.00.014.00></DE01.00.014.00>                 ");
			sb0.append("<DE01.00.013.00>"+this.lismessage.getCustom().getExam_num()+"</DE01.00.013.00>                 ");
			sb0.append("<DE08.10.054.00></DE08.10.054.00>                   ");
			sb0.append("<DE01.00.026.00></DE01.00.026.00>                   ");			
			sb0.append("<DE56.00.402.00></DE56.00.402.00>               ");//发票号码 必须填写
			sb0.append("<DE04.30.019.00>"+pcs.getPacs_num()+"</DE04.30.019.00>               ");//项目编号
			sb0.append("<DE04.30.019.01>"+pcs.getItemName()+"</DE04.30.019.01>               ");
			sb0.append("<DE09.00.266.00>"+pcs.getItemamount()+"</DE09.00.266.00>               ");
			sb0.append("<DE06.00.241.00>1</DE06.00.241.00>               ");//项目数量
			sb0.append("<DE01.00.018.00>"+pcs.getServiceDeliveryLocation_code()+"</DE01.00.018.00>       ");
			sb0.append("<DE04.30.018.00>D</DE04.30.018.00>               ");//  ??不清楚 检查类别
			sb0.append("<DE06.00.187.00>"+pcs.getItemName()+"</DE06.00.187.00>               ");//？？检查子类 不清楚
			sb0.append("<DE04.01.117.01></DE04.01.117.01>                   ");
			sb0.append("<DE04.01.117.00></DE04.01.117.00>                   ");
			sb0.append("<DE05.01.024.00></DE05.01.024.00>               ");
			sb0.append("<DE05.01.024.01></DE05.01.024.01>               ");
			sb0.append("<DE04.30.015.00></DE04.30.015.00>           ");
			sb0.append("<DE09.00.119.00></DE09.00.119.00>               ");
			sb0.append("<DE42.02.003.00></DE42.02.003.00>               ");
			sb0.append("<DE52.01.403.00></DE52.01.403.00>               ");
			sb0.append("<DE08.10.052.00></DE08.10.052.00>           ");//组织机构代码 未知
			sb0.append("</row>                                                  ");
			}
			}
			sb0.append("</HRC00.02>                                             ");
			sb0.append("<head>                                                  ");
			sb0.append("<row>                                                   ");			
			sb0.append("<appKey>"+appKey+"</appKey>                   ");
			sb0.append("</row>                                                  ");
			sb0.append("</head>                                                 ");
			sb0.append("</group>                                                ");
			sb0.append("</exchange>                                             ");		
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
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, com.hjw.interfaces.util.StringUtil.formatException(ex));
			flag = false;
		}
		
		return flag;
	}
	
		
	
	/**
	 * 
	 * @param logname2 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int updatezl_req_pacs_item(String exam_num,String req_no, String req_id,String cicode,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		ZlReqPatinfoDTO zlp = new ZlReqPatinfoDTO();
		zlp=getzl_patinfoFromNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "delete from zl_req_pacs_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_ids='"+cicode+"' and req_id='"+req_no+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
				String insertsql = "insert into zl_req_pacs_item(exam_info_id,pacs_req_code,charging_item_ids,zl_pat_id,req_id,createdate) values('" 
				+ ei.getId() + "','" + req_id + "','" +cicode + "','" +zlp.getZl_pat_id() + "','"+req_no+"','"+DateTimeUtil.getDateTime()+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +insertsql);				
				preparedStatement = tjtmpconnect.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.executeUpdate();
				ResultSet rs = null;
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next())
					lisid = rs.getInt(1);
				
				rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		return lisid;
	}
	
	//根据体检号查询zl_reqpatinfo表信息
	private ZlReqPatinfoDTO getzl_patinfoFromNum(String exam_num){
		StringBuffer sb = new StringBuffer();
		sb.append("select * from zl_req_patInfo where exam_num= '" + exam_num + "'");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1,10000,ZlReqPatinfoDTO.class);
		ZlReqPatinfoDTO zlreq = new ZlReqPatinfoDTO();
		
		if((map!=null)&&(map.getList().size()>0)){
			zlreq= (ZlReqPatinfoDTO)map.getList().get(0);			
		}
		return zlreq;
		
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
	 * 标本类型编码
	 * @param url
	 * @param view_num
	 * @return
	 */
	public MenuDTO getOrderExecType(String cicode,String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		MenuDTO eu= new MenuDTO();
		PreparedStatement preparedStatement = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.remark from charging_item a where a.item_code='"+cicode+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				eu.setText(rs1.getString("remark"));
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
		if((eu==null)||(eu.getText()==null)){
			eu.setValue("OT");
			eu.setText("");
		}else if(eu.getText().trim().length()<=0){
			eu.setValue("OT");
		}else if("超声".equals(eu.getText().trim())){
			eu.setValue("US");
		}else if("CT".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("CT");
		}else if("DR".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("DR");
		}else if("MRI".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("MRI");
		}else if("内窥镜".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("ES");
		}else if("ECT".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("NM");
		}else if("彩色多普勒".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("CD");
		}else if("双多普勒".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("DD");
		}else if("数字胃肠".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("RF");
		}else if("SPECT".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("ST");
		}else if("雷射表面扫描".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("LS");
		}else if("病理".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("DG");
		}else if("PET".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("PT");
		}else if("X线".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("XA");
		}else if("乳腺".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("MG");
		}else if("心电图".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("ECG");
		}else if("放疗图像".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("RTIMAGE");
		}else{
			eu.setValue("OT");
		}
		return eu;
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
	
	
	
}
