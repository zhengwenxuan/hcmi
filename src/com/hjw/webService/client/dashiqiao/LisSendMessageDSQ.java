package com.hjw.webService.client.dashiqiao;

import java.sql.Connection;
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
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeSends;
import com.hjw.webService.client.dashiqiao.LisReqBean.LisReq;
import com.hjw.webService.client.dashiqiao.LisReqBean.LisReqSends;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
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
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LisSendMessageDSQ{
private LisMessageBody lismessage;
private static JdbcQueryManager jdbcQueryManager;
private static CustomerInfoService customerInfoService;
private static ConfigService configService;
static {
	init();
}

public static void init() {
	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	configService = (ConfigService) wac.getBean("configService");
	
}

	public LisSendMessageDSQ(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url,String logname) {
		ResultLisBody rb = new ResultLisBody();
		JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		try {			
			String exam_num = lismessage.getCustom().getExam_num();
			ExamInfoUserDTO eu = getExamInfoForNum(exam_num, logname);
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (LisComponents comps : lismessage.getComponents()) {
				
				ResultHeader rhone= new ResultHeader();
				rhone=this.lisSendMessage(url, comps, logname,eu);
				if("AA".equals(rhone.getTypeCode())){
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
		return rb;
	}

	private ResultHeader lisSendMessage(String url,LisComponents comps,String logname, ExamInfoUserDTO eu) {
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		String exam_num = lismessage.getCustom().getExam_num();
		ResultHeader rhone= new ResultHeader();
		try {
		
			String req_no = comps.getReq_no();
			FeeSends feeSends = new FeeSends();
			LisReqSends lisSendReq = getLisSendReq(logname, req_no,eu);
			List<LisReq> lisReq = lisSendReq.getLisReq();
			
			for (int i = 0; i < lisReq.size(); i++) {
				lisReq.get(i).setDoctorname(doctorname);
				lisReq.get(i).setPatient_id(eu.getArch_num());
				lisReq.get(i).setExam_type(eu.getExam_type());
				long charid = lisReq.get(i).getChargingitemid();
				
				if(eu.getExam_type().equals("T")){
					int itemnum = getitemnum(exam_num, charid, logname);
					lisReq.get(i).setAmount(itemnum);
					
				}else{
					lisReq.get(i).setAmount(1);
				}
				
			}
			
			feeSends.setLisReqSends(lisSendReq);
			
			String json = new Gson().toJson(feeSends, FeeSends.class);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + json + "\r\n");
			String result = HttpUtil.doPost_Str(url,json, "utf-8");
			ResHdMeessage rh = new Gson().fromJson(result, ResHdMeessage.class);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
			if (result != null && rh.getStatus().equals("1") ) {
				rhone.setTypeCode("AA");
			}
		}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
	
		return rhone;
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
	
	
	
	
	public LisReqSends getLisSendReq(String logname, String req_no, ExamInfoUserDTO eu) {
		LisReqSends reqSends = new LisReqSends();
		//ArrayList<LisReq> lisReqList = new ArrayList<LisReq>();
		//
			StringBuffer sb = new StringBuffer();
			sb.append("  select ec.exam_indicator, c.item_abbreviation,e.exam_num,e.patient_id as clinic_no,c.Id as chargingitemId,c.item_name,ec.amount,c.his_num,    ");
			sb.append("  c.item_code,ec.amount as item_amount,ec.id as eci_id,ec.pay_status,c.hiscodeClass ,s.sample_barcode ");
			sb.append("  from examinfo_charging_item ec,exam_info e,   ");
			sb.append("  sample_exam_detail s,             ");
			sb.append("  examResult_chargingItem er,       ");
			sb.append("  sample_demo sd,                   ");
			sb.append("  data_dictionary d,                ");
			sb.append("  charging_item  c                  ");
			sb.append("  left join his_dict_dept hd        ");
			sb.append("  on c.perform_dept = hd.dept_code              ");
			sb.append("  where ec.examinfo_id = s.exam_info_id         ");
			sb.append("  and ec.charge_item_id = c.id        ");
			sb.append("  and s.sample_id = sd.id             ");
			sb.append("  and sd.demo_category = d.id         ");
			sb.append("  and ec.examinfo_id=e.id             ");
			sb.append("  and ec.isActive = 'Y'               ");
			sb.append("  and c.interface_flag = '2'                    ");
			sb.append("  and ec.exam_status in ('N','D')               ");
			sb.append("  and s.id = er.exam_id                         ");
			sb.append("  and er.charging_id = ec.charge_item_id        ");
			sb.append("  and er.result_type = 'sample'                 ");
			sb.append("  and ec.change_item != 'C'                     ");
			sb.append("  and ec.pay_status != 'M'                      ");
			sb.append("  and e.exam_num ='" + eu.getExam_num() + "'    ");
			sb.append("  and ec.examinfo_id ='" + eu.getId() + "'      ");
			sb.append("  and s.sample_barcode='"+req_no+"' and ec.is_application = 'N'                                                               ");
			sb.append("  and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and s.is_binding = 1))                 ");

			try {
				TranLogTxt.liswriteEror_to_txt(logname, "查询所有需要发送lis申请的项目:" + sb.toString() + "\r\n");
				List<LisReq> lisreqs = this.jdbcQueryManager.getList(sb.toString(), LisReq.class);
				/*for (int j = 0; j < lisreqs.size(); j++) {
					LisReq lisReq = new LisReq();
					lisReq = lisreqs.get(j);
					lisReqList.add(lisReq);
				}*/
				reqSends.setLisReq(lisreqs);
			} catch (Exception e) {
				e.printStackTrace();
			}


		return reqSends;
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
