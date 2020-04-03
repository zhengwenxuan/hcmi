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

import com.hjw.DTO.ZlReqPatinfoDTO;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.JaxbUtil;
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

public class LisSendMessageSinoSoft{
private LisMessageBody lismessage;
private static JdbcQueryManager jdbcQueryManager;
private static CustomerInfoService customerInfoService;
static {
	init();
}

public static void init() {
	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
   }

	public LisSendMessageSinoSoft(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String appKey, String logname) {
		ResultLisBody rb = new ResultLisBody();
		JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		try {
			List<ApplyNOBean> anList = new ArrayList<ApplyNOBean>();
			boolean rhone = this.lisSendMessage(url, appKey, logname);
			if (rhone) {
			for (LisComponents comps : lismessage.getComponents()) {
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
				}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
			}else{
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("组装lis xml格式文件错误");
			}
			
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;
	}

	private boolean lisSendMessage(String url, String appKey, String logname) {
		// 1，调用his获取申请单号
		TranLogTxt.liswriteEror_to_txt(logname, "1，调用lis申请单-开始");
		boolean flag = true;		
			try {
				StringBuffer sb = new StringBuffer("");
				sb.append("<exchange>");
				sb.append("<funcid>SD07.00.001.04</funcid>");
				sb.append("<group>");
				sb.append("<HRC00.02>");
				String datetime=DateUtil.getDate2();
				for (LisComponents comps : lismessage.getComponents()) {
					for (LisComponent comp : comps.getItemList()) {
						//ChargingItemDTO chargingItem = customerInfoService.getChargingItemForId(Long.valueOf(comp.getChargingItemid()));
				
				sb.append("<row>");
				sb.append("<VISITIDATE>"+datetime+"</VISITIDATE> ");
				sb.append("<VISITID></VISITID> ");
				sb.append("<CARDNO></CARDNO> ");// 卡号
				sb.append("<INVOICEID></INVOICEID> ");// 发票号
				sb.append("<PBARCODE>" + this.lismessage.getCustom().getExam_num() + "</PBARCODE> ");
				sb.append("<PTYPE>体检</PTYPE> ");// 门诊、住院、体检
				sb.append("<NAME>" + this.lismessage.getCustom().getName() + "</NAME> ");
				sb.append("<GENDER>" + this.lismessage.getCustom().getSexname() + "</GENDER> ");
				sb.append("<AGE>" + this.lismessage.getCustom().getOld() + "</AGE> ");
				sb.append("<AGEUNIT>年</AGEUNIT> ");
				sb.append("<BIRTHDAY>" + DateUtil.shortFmt3(DateUtil.strToDateLong2(this.lismessage.getCustom().getBirthtime())) + "</BIRTHDAY> ");// yyyy-mm-dd
				sb.append("<FEETYPE>A</FEETYPE> ");// 费用类型字典：TJ0005
													// A:自费；YLBX:医疗保险；LYGZG:职工医保；LYGNH:合作医疗；LYGJM:居民医保；LYGSJPT:合疗异地。
				sb.append("<DEPTCODE>"+this.lismessage.getDoctor().getDept_code()+"</DEPTCODE> ");// 2453
				sb.append("<DEPT>"+this.lismessage.getDoctor().getDept_name()+"</DEPT> ");// 神经内科
				sb.append("<CLINICDIAGNOSE></CLINICDIAGNOSE> ");
				sb.append("<DIAGICD></DIAGICD> ");
				sb.append("<OTHERINFO></OTHERINFO> ");
				sb.append("<ORDROWID>" + comps.getReq_no() + "</ORDROWID> ");//
				sb.append("<ORDROWINDEX>"+comp.getChargingItemid()+"</ORDROWINDEX> ");// 若 ordrowid
															// 不能表示唯一，需要给
															// 出项目序号以保证数据唯一性
				sb.append("<STATUS>0</STATUS> ");// 必填 0 新申请
				sb.append("<DOCTORCODE>"+this.lismessage.getDoctor().getDoctorCode()+"</DOCTORCODE> ");
				sb.append("<DOCTOR>"+this.lismessage.getDoctor().getDoctorName()+"</DOCTOR> ");
				sb.append("<ORDTIME>" + DateTimeUtil.getDateTime() + "</ORDTIME> ");
				sb.append("<ORDEXELOCCODE>233</ORDEXELOCCODE>");
				sb.append("<ORDEXELOC>生化室</ORDEXELOC> ");// 执行科室
				//sb.append("<ORDEXELOCCODE>" + comp.getServiceDeliveryLocation_code() + "</ORDEXELOCCODE>");
				//sb.append("<ORDEXELOC>" + comp.getServiceDeliveryLocation_code() + "</ORDEXELOC> ");// 执行科室
				sb.append("<ISEM>0</ISEM> ");// 1 加急； 0 常规
				sb.append("<SAMPLECODE>" + comp.getSpecimenNatural()+ "</SAMPLECODE> ");
				sb.append("<SAMPLENAME>" + comp.getSpecimenNaturalname() + "</SAMPLENAME> ");// 标本名称
				sb.append("<ORDCODE>" + comp.getItemCode() + "</ORDCODE> ");
				sb.append("<ORDNAME>" + comp.getItemName() + "</ORDNAME> ");
				sb.append("<QTY></QTY> ");// 数量
				sb.append("<FEE></FEE> ");// 收费金额
				sb.append("<ISFEE></ISFEE> ");// 收费标识 1 表示已收费
				sb.append("<HOSPITAL>江南医院本部</HOSPITAL> ");
				sb.append("<HOSPITALCODE>H0002</HOSPITALCODE> ");// 开单医院编码
				sb.append("</row>");
					}
				}
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
	private ExamInfoUserDTO getSamDemo(String chargid,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select b.demo_num as exam_num,b.demo_name as arch_num from charging_item a,sample_demo b where a.sam_demo_id=b.id and a.id='"+chargid+"'");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb.toString());
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
	 * @param req_no 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int updatezl_req_item(String exam_num, String req_no,String req_id,String ciid,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		ZlReqPatinfoDTO zlp = new ZlReqPatinfoDTO();
		zlp=getzl_patinfoFromNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "delete from zl_req_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_id='"+ciid+"' and req_id='"+req_no+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
				String insertsql = "insert into zl_req_item(exam_info_id,charging_item_id,zl_pat_id,lis_item_id,req_id,lis_req_code,createdate) values('" 
				+ ei.getId() + "','" +ciid + "','" +zlp.getZl_pat_id() + "','"+2+"','"+req_no+"','"+req_id+"','"+DateTimeUtil.getDateTime()+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +insertsql);				
				preparedStatement = tjtmpconnect.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.executeUpdate();
				ResultSet rs = null;
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next())
					
					lisid = rs.getInt(1);
				
				rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int getzl_req_Lis_item(String exam_num,String req_id,String cicode,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
							
				String sb1 = "select a.id from zl_req_item a where a.exam_info_id='"+ei.getId()+"' and a.charging_item_id='"
				+cicode+"' and a.req_id='"+req_id+"' ";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
				tjtmpconnect.createStatement().execute(sb1);
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
				if (rs1.next()) {
					lisid=rs1.getInt("id");
				}
				rs1.close();
				
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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

}
