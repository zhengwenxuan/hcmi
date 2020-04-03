package com.hjw.webService.client.dashiqiao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeSends;
import com.hjw.webService.client.dashiqiao.PacsReqBean.PacsReq;
import com.hjw.webService.client.dashiqiao.PacsReqBean.PacsReqSends;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.BatchService;
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
public class PACSDELSendMessageDSQ {
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    private static BatchService batchService; 
    private static ConfigService configService;
    static {
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		batchService = (BatchService) wac.getBean("batchService");
		configService = (ConfigService) wac.getBean("configService");
	
	}
	public PACSDELSendMessageDSQ(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (PacsComponents comps : lismessage.getComponents()) {
				ResultHeader rhone = lisSendMessage(url, comps, logname);
				if ("AA".equals(rhone.getTypeCode())) {
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

	private ResultHeader lisSendMessage(String url,PacsComponents comps, String logname) {
		
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		
		ResultHeader rhone= new ResultHeader();
		String req_no = comps.getReq_no();
		rhone.setTypeCode("AE");
		ExamInfoUserDTO eu = configService.getExamInfoForBarcode(comps.getReq_no());
		try{

			FeeSends feeSends = new FeeSends();
			PacsReqSends pacsSendReq = getPacsSendReq(logname, req_no, eu.getExam_num(), eu.getId());
			List<PacsReq> pacsReq = pacsSendReq.getPacsReq();
			
			for (int i = 0; i < pacsReq.size(); i++) {
				pacsReq.get(i).setDoctorname(doctorname);
				pacsReq.get(i).setPatient_id(eu.getArch_num());
				pacsReq.get(i).setAmount(1);
			}
			
			feeSends.setPacsReqSends(pacsSendReq);
				
			String json = new Gson().toJson(feeSends, FeeSends.class);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + json + "\r\n");
			String result = HttpUtil.doPost_Str(url,json, "utf-8");
			
			ResHdMeessage rhd = new Gson().fromJson(result, ResHdMeessage.class);
			
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();				
				if(rhd.getStatus().equals("1")){
					rhone.setTypeCode("AA");
				}
			}
		
		}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		
		return rhone;
	}

	// 查询pacs申请信息
	public PacsReqSends getPacsSendReq(String logname, String req_no, String exam_num, long examinfo_id) {

		PacsReqSends reqSends = new PacsReqSends();
	//	ArrayList<PacsReq> pacsReqList = new ArrayList<PacsReq>();
			StringBuffer sb = new StringBuffer();
			sb.append(" select ec.exam_indicator, c.item_abbreviation,e.exam_num,e.patient_id as clinic_no,c.id as chargingitemId,c.item_name,ec.amount,c.his_num,              ");
			sb.append(" c.item_code,ec.amount as item_amount,ec.id as eci_id,ec.pay_status,c.hiscodeClass,p.pacs_req_code,dd.dep_num  ");
			sb.append(" from examinfo_charging_item ec,exam_info e,                                                         ");
			sb.append(" pacs_summary p,             ");
			sb.append(" pacs_detail d,              ");
			sb.append(" department_dep dd,          ");
			sb.append(" charging_item c             ");
			sb.append(" left join his_dict_dept hd  ");
			sb.append(" on c.perform_dept = hd.dept_code     ");
			sb.append(" where ec.charge_item_id = c.id       ");
			sb.append(" and p.id = d.summary_id              ");
			sb.append(" and ec.examinfo_id=e.id              ");
			sb.append(" and d.chargingItem_num = c.item_code     ");
			sb.append(" and c.dep_id = dd.id                 ");
			sb.append(" and ec.isActive = 'Y'                ");
			sb.append(" and c.interface_flag = '2'           ");
			sb.append(" and ec.change_item != 'C'            ");
			sb.append(" and ec.pay_status != 'M'             ");
			sb.append(" and ec.exam_status in ('N','D')      ");
			sb.append(" and p.pacs_req_code = '"+req_no+"'   ");
			sb.append(" and ec.examinfo_id = " + examinfo_id + " and p.examinfo_num = '" + exam_num + "' ");
			sb.append(" and ec.is_application = 'Y'                       ");

			try {
				TranLogTxt.liswriteEror_to_txt(logname, "查询所有需要撤销pacs申请的项目:" + sb.toString() + "\r\n");
				List<PacsReq> pacsreqs = this.jdbcQueryManager.getList(sb.toString(), PacsReq.class);

				reqSends.setPacsReq(pacsreqs);

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
	public ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
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
