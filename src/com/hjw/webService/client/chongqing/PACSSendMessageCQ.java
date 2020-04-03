package com.hjw.webService.client.chongqing;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.DTO.ZlReqPatinfoDTO;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.chongqing.soap.ZlHisSoapLocator;
import com.hjw.webService.client.chongqing.soap.ZlHisSoapPortType;
import com.hjw.webService.client.chongqing.util.ChongQingSetHL7;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.MenuDTO;
import com.hjw.wst.service.BatchService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.ORR_O02;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;

public class PACSSendMessageCQ{
	private PacsMessageBody lismessage;
	private static ConfigService configService;
	private static BatchService batchService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		batchService = (BatchService) wac.getBean("batchService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSSendMessageCQ(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url,String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname,"lismessage:"+new Gson().toJson(lismessage, PacsMessageBody.class));
			TranLogTxt.liswriteEror_to_txt(logname,"url:"+url);
			String exam_num = lismessage.getCustom().getExam_num();
			if (StringUtil.isEmpty(exam_num)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {
				ExamInfoUserDTO eu=this.configService.getExamInfoForNum(lismessage.getCustom().getExam_num());
				if ((eu==null)||(eu.getId() <= 0)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					ControlActPacsProcess ca = new ControlActPacsProcess();
					List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
					
					for (PacsComponents pcs : lismessage.getComponents()) {
						try {
						
						URL zlurl = new URL(url);
						String PacsSendReq = ChongQingSetHL7.SendPacs(pcs,eu,logname);//发送Pacs申请
						TranLogTxt.liswriteEror_to_txt(logname, "pacs发送申请入参:" + PacsSendReq + "\r\n");

						
						ZlHisSoapLocator soapLocator = new ZlHisSoapLocator();
						ZlHisSoapPortType portType = soapLocator.getzlHisSoapHttpSoap11Endpoint(zlurl);
						String res = portType.zlWS_HL7(PacsSendReq);
					
						
						TranLogTxt.liswriteEror_to_txt(logname, "pacs发送申请返回:" + res + "\r\n");
						ORR_O02 fromJson = new Gson().fromJson(res, ORR_O02.class);
						if (fromJson != null && fromJson.toString().length() > 10) {
							MSH msh = fromJson.getMSH();
							MSA msa = fromJson.getMSA();
							String code = msa.getAcknowledgmentCode().toString();
							if (code.equals("AA")) {
								ApplyNOBean ab= new ApplyNOBean();
								ab.setApplyNO(pcs.getReq_no());
								list.add(ab);
							} 
						}
						}catch(Exception ex){
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("pacs发送申请错误");
						}
					}
					if(list.size() > 0) {
						ca.setList(list);
						rb.setControlActProcess(ca);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("pacs调用成功");
					}
			 }
			}
		} catch (Exception ex){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "ret:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
	
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int updatezl_req_pacs_item(String exam_num,String req_id,String cicode,String logname){
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
					+"'  and charging_item_ids='"+cicode+"' and req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
				String insertsql = "insert into zl_req_pacs_item(exam_info_id,charging_item_ids,zl_pat_id,req_id,createdate) values('" 
				+ ei.getId() + "','" +cicode + "','" +zlp.getZl_pat_id() + "','"+req_id+"','"+DateTimeUtil.getDateTime()+"')";
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
