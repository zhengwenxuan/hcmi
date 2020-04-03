package com.hjw.webService.client.dashiqiao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeSends;
import com.hjw.webService.client.dashiqiao.PacsReqBean.PacsReq;
import com.hjw.webService.client.dashiqiao.PacsReqBean.PacsReqSends;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.MenuDTO;
import com.hjw.wst.service.BatchService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PACSSendMessageDSQ{
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
	public PACSSendMessageDSQ(PacsMessageBody lismessage){
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
			
			String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
			String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
			String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
			String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		
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
					
						try {
						PacsReqSends pacsSendReq = getPacsSendReq(logname,eu.getExam_num(),eu.getId());
						List<PacsReq> pacsReq = pacsSendReq.getPacsReq();
						for (int i = 0; i < pacsReq.size(); i++) {
							pacsReq.get(i).setDoctorname(doctorname);
							pacsReq.get(i).setPatient_id(eu.getArch_num());
							pacsReq.get(i).setExam_type(eu.getExam_type());
							
							long charid = pacsReq.get(i).getChargingitemid();
							if(eu.getExam_type().equals("T")){
								
								int getitemnum = getitemnum(exam_num, charid, logname);
								pacsReq.get(i).setAmount(getitemnum);
							}else{
								
								pacsReq.get(i).setAmount(1);
							}
							
							
						}
						FeeSends feeSends = new FeeSends();
						feeSends.setPacsReqSends(pacsSendReq);
						
						String json = new Gson().toJson(feeSends, FeeSends.class);
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + json + "\r\n");
						String result = HttpUtil.doPost_Str(url,json, "utf-8");
						
						ResHdMeessage rh = new Gson().fromJson(result, ResHdMeessage.class);
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
						if (rh != null) {
							ResultHeader rhone= new ResultHeader();
							if("1".equals(rh.getStatus())){
								for (PacsComponents pcs : lismessage.getComponents()) {
									rb.getResultHeader().setTypeCode("AA");
									ApplyNOBean ab= new ApplyNOBean();
									ab.setApplyNO(pcs.getReq_no());
									list.add(ab);
								}
								
							}
						}
						}catch(Exception ex){
							rb.getResultHeader().setTypeCode("AE");
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
	
	
	
	
	// 查询pacs申请信息
		public PacsReqSends getPacsSendReq(String logname,String exam_num, long examinfo_id) {

			PacsReqSends reqSends = new PacsReqSends();
		//	ArrayList<PacsReq> pacsReqList = new ArrayList<PacsReq>();
				StringBuffer sb = new StringBuffer();
				sb.append(" select ec.exam_indicator,c.item_abbreviation,e.exam_num,e.patient_id as clinic_no,c.id as chargingitemId,c.item_name,ec.amount,c.his_num,              ");
				sb.append(" c.item_code,ec.amount as item_amount,ec.id as eci_id,ec.pay_status,c.hiscodeClass,p.pacs_req_code,dd.dep_num  ");
				sb.append(" from examinfo_charging_item ec,exam_info e,                                                         ");
				sb.append(" pacs_summary p, ");
				sb.append(" pacs_detail d, ");
				sb.append(" department_dep dd, ");
				sb.append(" charging_item c ");
				sb.append(" left join his_dict_dept hd    ");
				sb.append(" on c.perform_dept = hd.dept_code  ");
				sb.append(" where ec.charge_item_id = c.id  ");
				sb.append(" and p.id = d.summary_id   ");
				sb.append(" and ec.examinfo_id=e.id   ");
				sb.append(" and d.chargingItem_num = c.item_code     ");
				sb.append(" and c.dep_id = dd.id   ");
				sb.append(" and ec.isActive = 'Y'   ");
				sb.append(" and c.interface_flag = '2' ");
				sb.append(" and ec.change_item != 'C' ");
				sb.append(" and ec.pay_status != 'M'  ");
				sb.append(" and ec.exam_status in ('N','D')  ");
				sb.append(" and ec.examinfo_id = " + examinfo_id + " and p.examinfo_num = '" + exam_num + "' ");
				sb.append(" and ec.is_application = 'N' ");

				try {
					TranLogTxt.liswriteEror_to_txt(logname, "查询所有需要发送pacs申请的项目:" + sb.toString() + "\r\n");
					List<PacsReq> pacsreqs = this.jdbcQueryManager.getList(sb.toString(), PacsReq.class);

					reqSends.setPacsReq(pacsreqs);
					/*for (int j = 0; j < pacsreqs.size(); j++) {
						PacsReq pacsreq = new PacsReq();
						pacsreq = pacsreqs.get(j);
						pacsReqList.add(pacsreq);
					}*/

				} catch (Exception e) {
					e.printStackTrace();
				}



			return reqSends;
		}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
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
	private int getitemnum(String exam_num, long id, String logname) {
		Connection connection = null;
		
		int itemnum=1;
		try {
			connection = this.jdbcQueryManager.getConnection();
			String sql = " select  distinct ci.id,gci.itemnum from examinfo_charging_item eci,exam_info ei,group_info g ,group_charging_item gci,charging_item ci "
					+ " where eci.exam_indicator='T' and ei.exam_num='"+exam_num+"'  "
					+ " and eci.charge_item_id='"+id+"' "
					+ " and ei.id=eci.examinfo_id "
					+ " and g.id=ei.group_id  "
					+ " and eci.charge_item_id=gci.charge_item_id "
					+ " and gci.group_id=ei.group_id "
					+ " and ci.id=eci.charge_item_id "
					+ " and eci.charge_item_id=gci.charge_item_id "
					+ " and eci.isActive='Y' "
					+ " and eci.his_req_status='N'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sql);
			ResultSet rs = connection.createStatement().executeQuery(sql);
			
			if (rs.next()) {
				itemnum = rs.getInt("itemnum");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return itemnum;
	}
	
}
