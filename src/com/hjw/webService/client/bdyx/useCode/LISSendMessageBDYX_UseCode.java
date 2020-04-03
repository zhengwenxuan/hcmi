package com.hjw.webService.client.bdyx.useCode;

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
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.bdyx.bean.RequestPost;
import com.hjw.webService.client.bdyx.bean.ResponsePost;
import com.hjw.webService.client.bdyx.bean.lis.req.LabOrderDto;
import com.hjw.webService.client.bdyx.bean.lis.req.LisApplyReq;
import com.hjw.webService.client.bdyx.bean.lis.req.LisReq;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;

import net.sf.json.JSONObject;

public class LISSendMessageBDYX_UseCode {

	private LisMessageBody lismessage;
	private static ConfigService configService;
	private static CustomerInfoService customerInfoService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}
	
	public LISSendMessageBDYX_UseCode(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	public ResultLisBody getMessage(String url,String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + JSONObject.fromObject(lismessage));
		TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
		ResultLisBody rb = new ResultLisBody();
		
		String exam_num = this.lismessage.getCustom().getExam_num();
		ExamInfoUserDTO eu = configService.getExamInfoForNum(exam_num);
		if(StringUtil.isEmpty(eu.getPatient_id())) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("没有病人id，直接返回失败");
			TranLogTxt.liswriteEror_to_txt(logname, "没有病人id，直接返回失败");
			String xml = JaxbUtil.convertToXml(rb, true);
			TranLogTxt.liswriteEror_to_txt(logname, "ret:" + xml);
			return rb;
		}
		try {
			String sexcode = "0";
			if("男".equals(eu.getSex()) || "男性".equals(eu.getSex())) {
				sexcode = "1";
			} else if("女".equals(eu.getSex()) || "女性".equals(eu.getSex())) {
				sexcode = "2";
			}
			String birthDate = this.lismessage.getCustom().getBirthtime();
			if(!StringUtil.isEmpty(birthDate) && birthDate.length()==8) {
				birthDate = birthDate + "000000";
			}
			List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
			for (LisComponents lcs : this.lismessage.getComponents()) {
				LisApplyReq lisApplyReq = new LisApplyReq();
				LisReq lisReq = new LisReq();
				lisReq.setRequestNo(lcs.getReq_no());

				lisApplyReq.setPatientLid(eu.getPatient_id());
				lisApplyReq.setVisitNo(eu.getArch_num());
				lisApplyReq.setVisitOrdNo(eu.getExam_num());
				lisApplyReq.setPatientName(eu.getUser_name());
				lisApplyReq.setTelephone(eu.getPhone());
				lisApplyReq.setGenderCode(sexcode);
				lisApplyReq.setBirthTime(birthDate);
				lisApplyReq.setAge(eu.getAge()+"");
//				lisApplyReq.setRequestDoctor(lismessage.getDoctor().getDoctorCode());
//				lisApplyReq.setRequestDoctorName(lismessage.getDoctor().getDoctorName());
				lisApplyReq.setRequestDept(lismessage.getDoctor().getDept_code());
				lisApplyReq.setRequestDeptName(lismessage.getDoctor().getDept_name());
				for (LisComponent lc : lcs.getItemList()) {
					LabOrderDto labOrder = new LabOrderDto();
					long chargingItemid = Long.parseLong(lc.getChargingItemid());
					ChargingItemDTO chargingItem = customerInfoService.getChargingItemForId(chargingItemid);
					
					labOrder.setOrderLid(lcs.getReq_no()+"-"+chargingItem.getItem_code());
					labOrder.setItemCode(lc.getItemCode());
					labOrder.setItemName(lc.getItemName());
					labOrder.setItemPrice(""+lc.getItemprice());
					labOrder.setSampleType(lc.getSpecimenNatural());
					labOrder.setSampleTypeName(lc.getSpecimenNaturalname());
					labOrder.setDeliveryId(lc.getServiceDeliveryLocation_code());
					labOrder.setDeliveryName(lc.getServiceDeliveryLocation_name());
					labOrder.setSpecimenid(lcs.getReq_no());
					lisReq.getLabOrderDto().add(labOrder);
				}
				
				lisApplyReq.getRequestList().add(lisReq);
				String str = new Gson().toJson(lisApplyReq, LisApplyReq.class);
				TranLogTxt.liswriteEror_to_txt(logname,"lisApplyReq:"+str);
				
				RequestPost requestBDYX = new RequestPost();
				requestBDYX.setService_id("BS006");
				requestBDYX.setExec_uint_id(lcs.getItemList().get(0).getServiceDeliveryLocation_code());
				requestBDYX.setOrder_exec_id("029");
				requestBDYX.setExtend_sub_id("");
				requestBDYX.setBody(str);
				String requestStr = JSONObject.fromObject(requestBDYX).toString();
				TranLogTxt.liswriteEror_to_txt(logname,"req:"+requestStr);
				String responseStr = HttpUtil.doPost(url,requestStr,"utf-8");
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+responseStr);
				ResponsePost responseBDYX = new Gson().fromJson(responseStr, ResponsePost.class);
				if(1 == responseBDYX.getStatus()) {
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(lcs.getReq_no());
					appList.add(an);
				}else{
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("LIS返回错误:" + responseBDYX.getErrMsg());
					TranLogTxt.liswriteEror_to_txt(logname, "LIS返回错误:" + responseBDYX.getErrMsg());
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(appList);
			rb.getResultHeader().setText("申请发送成功");
		} catch (Exception ex){
			ex.printStackTrace();
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "ret:" + xml);
		return rb;
	}
}
