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
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.interfaces.util.LockCenterDateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeDSQ;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeMessageDSQ;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeSends;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeesDSQ;
import com.hjw.webService.client.dashiqiao.LisReqBean.LisReq;
import com.hjw.webService.client.dashiqiao.LisReqBean.LisReqSends;
import com.hjw.webService.client.dashiqiao.PacsReqBean.PacsReq;
import com.hjw.webService.client.dashiqiao.PacsReqBean.PacsReqSends;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.domain.ChargingItem;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 2.14 挂号信息服务 天健 平台对接-东北国际
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class FEESendMessageDSQ {

	private FeeMessage feeMessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private static CustomerInfoService customerInfoService;
	static {
		init();
	}

	public FEESendMessageDSQ(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		try {
			this.feeMessage.setMSG_TYPE("TJ602");
			xml = JaxbUtil.convertToXml(this.feeMessage, true);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
			//加日期锁  提前10天提示  用户
			ResHdMeessage rhm = LockCenterDateUtil.SetEaminatioinCenterDate(2019, 10, 5, logname);
			if(rhm.getStatus().equals("AE")){
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(rhm.getMessage());
				return rb;
			}else{
				ResHdMeessage rh = getString(url, logname, this.feeMessage);
				if ("AA".equals(rh.getStatus())) {
					ReqId rqid = new ReqId();
					rqid.setReq_id(this.feeMessage.getREQ_NO());
					rb.getControlActProcess().getList().add(rqid);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("发送收费申请成功!");
					


				} else {
					rb.getResultHeader().setTypeCode(rh.getStatus());
					rb.getResultHeader().setText(rh.getMessage());
				}
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息格式文件错误");
		}
		return rb;
	}

	private ResHdMeessage getString(String url, String logname, FeeMessage feeMessage) {
		ResHdMeessage rh = new ResHdMeessage();

		FeeSends feeSends = new FeeSends();
		ExamInfoUserDTO eu = getExamInfoForNum(feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM(), logname);
		TranLogTxt.liswriteEror_to_txt(logname, "进入拼接收费请求数据方法55555:" + "" + "\r\n");
		FeeMessageDSQ feeMessageDSQ = new FeeMessageDSQ();
		FeesDSQ feesDSQ = new FeesDSQ();
		ArrayList<FeeDSQ> FeeDSQList = new ArrayList<FeeDSQ>();
		List<Fee> project = feeMessage.getPROJECTS().getPROJECT();
		for (int i = 0; i < project.size(); i++) {

			FeeDSQ feeDSQ = new FeeDSQ();
			ChargingItem ci = getChargingItem(project.get(i).getExam_chargeItem_code(), logname); // item_abbreviation
			ExaminfoChargingItemDTO eci = configService.getExaminfoChargingItem(eu.getId(), ci.getId(), logname);
			
			feeDSQ.setPATIENT_ID(eu.getArch_num());
			feeDSQ.setEXAM_NUM(project.get(i).getEXAM_NUM());
			feeDSQ.setUSER_NAME(project.get(i).getUSER_NAME());
			feeDSQ.setVISIT_DATE(project.get(i).getVISIT_DATE());
			feeDSQ.setVISIT_NO(project.get(i).getVISIT_NO());
			feeDSQ.setSERIAL_NO(project.get(i).getSERIAL_NO());
			feeDSQ.setORDER_CLASS(project.get(i).getORDER_CLASS());
			feeDSQ.setORDER_NO(project.get(i).getORDER_NO());
			feeDSQ.setORDER_SUB_NO(project.get(i).getORDER_SUB_NO());
			feeDSQ.setITEM_NO(project.get(i).getITEM_NO());
			feeDSQ.setITEM_CLASS(project.get(i).getITEM_CLASS());
			feeDSQ.setITEM_NAME(project.get(i).getITEM_NAME());
			feeDSQ.setITEM_CODE(project.get(i).getITEM_CODE());
			feeDSQ.setITEM_SPEC(project.get(i).getITEM_SPEC());
			feeDSQ.setUNITS(project.get(i).getUNITS());
			feeDSQ.setREPETITION(project.get(i).getREPETITION());
			feeDSQ.setAMOUNT(eci.getItemnum()+"");
			feeDSQ.setORDERED_BY_DEPT(project.get(i).getORDERED_BY_DEPT());
			feeDSQ.setORDERED_BY_DOCTOR(project.get(i).getORDERED_BY_DOCTOR());
			feeDSQ.setPERFORMED_BY(project.get(i).getPERFORMED_BY());
			feeDSQ.setCLASS_ON_RCPT(project.get(i).getCLASS_ON_RCPT());
			feeDSQ.setCOSTS(project.get(i).getCOSTS());
			feeDSQ.setCHARGES(project.get(i).getCHARGES());
			feeDSQ.setRCPT_NO(project.get(i).getRCPT_NO());
			feeDSQ.setCHARGE_INDICATOR(project.get(i).getCHARGE_INDICATOR());
			feeDSQ.setCLASS_ON_RECKONING(project.get(i).getCLASS_ON_RECKONING());
			feeDSQ.setSUBJ_CODE(project.get(i).getSUBJ_CODE());
			feeDSQ.setPRICE_QUOTIETY(project.get(i).getPRICE_QUOTIETY());
			feeDSQ.setITEM_PRICE(project.get(i).getITEM_PRICE());
			feeDSQ.setCLINIC_NO(project.get(i).getCLINIC_NO());
			feeDSQ.setBILL_DATE(project.get(i).getBILL_DATE());
			feeDSQ.setBILL_NO(project.get(i).getBILL_NO());
			feeDSQ.setSKINTEST(project.get(i).getSKINTEST());
			feeDSQ.setPRESC_PSNO(project.get(i).getPRESC_PSNO());
			feeDSQ.setINSURANCE_FLAG(project.get(i).getINSURANCE_FLAG());
			feeDSQ.setINSURANCE_CONSTRAINED_LEVEL(project.get(i).getINSURANCE_CONSTRAINED_LEVEL());
			feeDSQ.setSKIN_SAVE(project.get(i).getSKIN_SAVE());
			feeDSQ.setSKIN_START(project.get(i).getSKIN_START());
			feeDSQ.setSKIN_BATH(project.get(i).getSKIN_BATH());
			feeDSQ.setYB_DIAG(project.get(i).getYB_DIAG());
			feeDSQ.setYB_DOCTOR(project.get(i).getYB_DOCTOR());
			feeDSQ.setExam_chargeItem_code(project.get(i).getExam_chargeItem_code());
			feeDSQ.setCodeClass(project.get(i).getCodeClass());
			feeDSQ.setDiscount(project.get(i).getDiscount());
			feeDSQ.setItem_Abbreviation(ci.getItem_abbreviation());

			FeeDSQList.add(feeDSQ);

		}

		feesDSQ.setPROJECT(FeeDSQList);
		feeMessageDSQ.setMSG_TYPE(feeMessage.getMSG_TYPE());
		feeMessageDSQ.setREQ_NO(feeMessage.getREQ_NO());

		feeMessageDSQ.setPROJECTS(feesDSQ);

		feeSends.setFeeMessage(feeMessageDSQ);// his缴费申请信息
		
		TranLogTxt.liswriteEror_to_txt(logname, "拼接收费请求数据feeSends 对象结束:" + "" + "\r\n");

		// his缴费申请 
		String json = new Gson().toJson(feeSends, FeeSends.class);

		TranLogTxt.liswriteEror_to_txt(logname, "req发送给his的收费信息:" + json + "\r\n");

		try {
			TranLogTxt.liswriteEror_to_txt(logname, "调用还是收费接口:" + url + "\r\n");
			String result = HttpUtil.doPost_Str(url, json, "utf-8");
			rh = new Gson().fromJson(result, ResHdMeessage.class);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");

			if (rh != null && rh.getStatus().equals("1")) {
				rh.setStatus("AA");

				rh.setMessage("HIS缴费申请发送成功");
				String req_NO = feeMessageDSQ.getREQ_NO();
			//	updateeciisapp(this.feeMessage.getREQ_NO(), logname);

			} else {
				rh.setStatus("AE");
				rh.setMessage("HIS缴费申请发送失败");
			}
		} catch (Exception ex) {
			rh.setStatus("AE");
			rh.setMessage("HIS缴费申请发送错误");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex) + "\r\n");

		}
		return rh;
	}

	private void updateeciisapp(String req_NO, String logname) {
		System.err.println("申请单号=====updateeciisapp:"+req_NO);
		Connection connection = null;
		try {
			connection = this.jdbcQueryManager.getConnection();
			String sb1 = "select cds.charging_item_id,css.exam_id,css.id,css.charging_status from charging_summary_single css,charging_detail_single cds where "
					+ " css.is_Active='Y' and cds.summary_id=css.id and css.req_num = '" + req_NO + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + sb1);
			ResultSet rs = connection.createStatement().executeQuery(sb1);
			while (rs.next()) {
				int examinfo_id = rs.getInt("exam_id");
				int charging_item_id = rs.getInt("charging_item_id");

				String updatesql = "update examinfo_charging_item set is_application='Y' where  examinfo_id='"
						+ examinfo_id + "' and charge_item_id='" + charging_item_id + "' "
						+ "and is_application='N'  and isActive='Y'";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + updatesql);
				connection.createStatement().execute(updatesql);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}

	}

	// 查询pacs申请信息
	public PacsReqSends getPacsSendReq(String logname, FeeMessage feeMessage) {

		ExamInfoUserDTO eu = getExamInfoForNum(feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM(), logname);
		PacsReqSends reqSends = new PacsReqSends();
		ArrayList<PacsReq> pacsReqList = new ArrayList<PacsReq>();
		// reqSends.setLisReq(lisReq);
		List<Fee> fees = feeMessage.getPROJECTS().getPROJECT();
		for (int i = 0; i < fees.size(); i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(
					" select c.item_abbreviation,e.exam_num,e.patient_id,c.id as chargingitemId,c.item_name,ec.amount,c.his_num,              ");
			sb.append(
					" c.item_code,ec.item_amount,ec.id as eci_id,ec.pay_status,c.hiscodeClass,p.pacs_req_code,dd.dep_num  ");
			sb.append(
					" from examinfo_charging_item ec,exam_info e,                                                         ");
			sb.append(" pacs_summary p,                          ");
			sb.append(" pacs_detail d,                           ");
			sb.append(" department_dep dd,                       ");
			sb.append(" charging_item c                          ");
			sb.append(" left join his_dict_dept hd               ");
			sb.append(" on c.perform_dept = hd.dept_code         ");
			sb.append(" where ec.charge_item_id = c.id           ");
			sb.append(" and p.id = d.summary_id                  ");
			sb.append(" and ec.examinfo_id=e.id                  ");
			sb.append(" and d.chargingItem_num = c.item_code     ");
			sb.append(" and c.dep_id = dd.id                     ");
			sb.append(" and ec.isActive = 'Y'                             ");
			sb.append(" and c.interface_flag = '2'                        ");
			sb.append(" and ec.change_item != 'C'                         ");
			sb.append(" and ec.pay_status != 'M'                          ");
			sb.append(" and ec.exam_status in ('N','D')                   ");
			sb.append(" and ec.examinfo_id = '" + eu.getId() + "'                        ");
			sb.append(" and p.examinfo_num = '" + fees.get(i).getEXAM_NUM() + "'                 ");
			sb.append(" and c.item_code in ('" + fees.get(i).getExam_chargeItem_code() + "')        ");
			sb.append(" and ec.is_application = 'N'                       ");

			try {
				TranLogTxt.liswriteEror_to_txt(logname, "查询所有需要发送pacs申请的项目:" + sb.toString() + "\r\n");
				List<PacsReq> pacsreqs = this.jdbcQueryManager.getList(sb.toString(), PacsReq.class);

				for (int j = 0; j < pacsreqs.size(); j++) {
					PacsReq pacsreq = new PacsReq();
					pacsreq = pacsreqs.get(j);
					pacsReqList.add(pacsreq);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		reqSends.setPacsReq(pacsReqList);

		return reqSends;
	}

	// 查询lis申请信息
	public LisReqSends getLisSendReq(String logname, FeeMessage feeMessage) {
		ExamInfoUserDTO eu = getExamInfoForNum(feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM(), logname);
		LisReqSends reqSends = new LisReqSends();
		ArrayList<LisReq> lisReqList = new ArrayList<LisReq>();
		//
		List<Fee> fees = feeMessage.getPROJECTS().getPROJECT();
		for (int i = 0; i < fees.size(); i++) {
			StringBuffer sb = new StringBuffer();
			sb.append("  select c.item_abbreviation,e.exam_num,e.patient_id,c.Id as chargingitemId,c.item_name,ec.amount,c.his_num,    ");
			sb.append("  c.item_code,ec.item_amount,ec.id as eci_id,ec.pay_status,c.hiscodeClass ,s.sample_barcode ");
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
			sb.append("  and ec.examinfo_id ='" + eu.getId() + "'                     ");
			sb.append("  and c.item_code in ('" + fees.get(i).getExam_chargeItem_code() + "')    ");
			sb.append("  and ec.is_application = 'N'                                                               ");
			sb.append("  and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and s.is_binding = 1))                 ");

			try {
				TranLogTxt.liswriteEror_to_txt(logname, "查询所有需要发送lis申请的项目:" + sb.toString() + "\r\n");
				List<LisReq> lisreqs = this.jdbcQueryManager.getList(sb.toString(), LisReq.class);
				for (int j = 0; j < lisreqs.size(); j++) {
					LisReq lisReq = new LisReq();
					lisReq = lisreqs.get(j);
					lisReqList.add(lisReq);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		reqSends.setLisReq(lisReqList);

		return reqSends;
	}

	public ExamInfoUserDTO getExamInfoForNum(String exam_num, String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");
		sb.append(" and c.exam_num = '" + exam_num + "' ");
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}

	public ChargingItem getChargingItem(String exam_chargeItem_code, String logname) {
		StringBuffer sb = new StringBuffer();
		TranLogTxt.liswriteEror_to_txt(logname, "查询简称开始:" + sb.toString() + "\r\n");
		sb.append(" select * from charging_item where item_code='" + exam_chargeItem_code + "' and isActive='Y' ");

		TranLogTxt.liswriteEror_to_txt(logname, "查询简称结束:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ChargingItem.class);
		ChargingItem ci = new ChargingItem();
		if ((map != null) && (map.getList().size() > 0)) {
			ci = (ChargingItem) map.getList().get(0);
		}
		return ci;
	}

	/*
	 * public void insert_search(long examid,String exam_num,String
	 * persion,String logname){ long exam_id=examid; Connection tjtmpconnect =
	 * null; try { tjtmpconnect = this.jdbcQueryManager.getConnection(); String
	 * insertsql =
	 * " insert into zl_req_item ( exam_info_id,charging_item_id,zl_pat_id,lis_item_id,req_id,lis_req_code,createdate) "
	 * ; TranLogTxt.liswriteEror_to_txt(logname, "res:" + insertsql + "\r\n");
	 * tjtmpconnect.createStatement().executeUpdate(insertsql); }catch(Exception
	 * ex){
	 * 
	 * }finally { try { if (tjtmpconnect != null) { tjtmpconnect.close(); } }
	 * catch (SQLException sqle4) { sqle4.printStackTrace(); } } }
	 */
}
