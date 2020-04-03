package com.hjw.webService.client.dashiqiao;

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
public class LISDELSendMessageDSQ {
	private LisMessageBody lismessage;
	private static ConfigService configService;
    private static JdbcQueryManager jdbcQueryManager;
    static {
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public LISDELSendMessageDSQ(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		ResultLisBody rb = new ResultLisBody();
		try {
			JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (LisComponents comps : lismessage.getComponents()) {
					
					ResultHeader rhone = lisSendMessage(url,comps,logname);
					if("AA".equals(rhone.getTypeCode())){
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

	private ResultHeader lisSendMessage(String url,LisComponents comps, String logname) {
		
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		ResultHeader rhone= new ResultHeader();
		
		try {
			ExamInfoUserDTO eu = configService.getExamInfoForBarcode(comps.getReq_no());
		
			

			FeeSends feeSends = new FeeSends();
			LisReqSends lisSendReq = getLisSendReq(logname, comps.getReq_no(), eu);
			List<LisReq> lisReq = lisSendReq.getLisReq();
			
			for (int i = 0; i < lisReq.size(); i++) {
				lisReq.get(i).setDoctorname(doctorname);
				lisReq.get(i).setPatient_id(eu.getArch_num());
				lisReq.get(i).setAmount(1);
			}
			
			feeSends.setLisReqSends(lisSendReq);
				
			String json = new Gson().toJson(feeSends, FeeSends.class);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + json + "\r\n");
			String result = HttpUtil.doPost_Str(url,json, "utf-8");
			
			ResHdMeessage rhd = new Gson().fromJson(result, ResHdMeessage.class);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
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
	
	public LisReqSends getLisSendReq(String logname, String req_no, ExamInfoUserDTO eu) {
		LisReqSends reqSends = new LisReqSends();
		//ArrayList<LisReq> lisReqList = new ArrayList<LisReq>();
		//
			StringBuffer sb = new StringBuffer();
			sb.append("  select ec.exam_indicator,c.item_abbreviation,e.exam_num,e.patient_id as clinic_no,c.Id as chargingitemId,c.item_name,ec.amount,c.his_num,    ");
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
			sb.append("  and s.sample_barcode ='" + req_no + "'        ");
			sb.append("  and ec.is_application = 'Y'                   ");
			sb.append("  and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and s.is_binding = 1))                 ");

			try {
				TranLogTxt.liswriteEror_to_txt(logname, "查询所有需要撤销lis申请的项目:" + sb.toString() + "\r\n");
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
	
	
	

}
