package com.hjw.webService.client.xhhk;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.xhhk.bean.ResponseXHHK;
import com.hjw.webService.client.xhhk.lisbean.CreateApply;
import com.hjw.webService.client.xhhk.lisbean.HttpClientUtil;
import com.hjw.webService.client.xhhk.lisbean.ItemsApplyLisXHHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;

import net.sf.json.JSONObject;

public class LISSendMessageXHHK {

	private LisMessageBody lismessage;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public LISSendMessageXHHK(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	public ResultLisBody getMessage(String url,String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + JSONObject.fromObject(lismessage));
		TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
		ResultLisBody rb = new ResultLisBody();
		
		String exam_num = this.lismessage.getCustom().getExam_num();
		ExamInfoUserDTO eu = configService.getExamInfoForNum(exam_num);
		int sexcode = 1;
		if("女".equals(eu.getSex())) {
			sexcode = 2;
		}
		List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
		try {
			for (LisComponents lcs : this.lismessage.getComponents()) {
				CreateApply createApply = new CreateApply();
				createApply.setApplyNo(lcs.getReq_no());//申请单号 提供给医生、患者的申请单编号
				createApply.setHospitalCode("");//送检医院编码
				createApply.setHospitalName("");//送检医院名称
				createApply.setPatNo(eu.getExam_num());//门诊号/住院号
				createApply.setPatType(4);//病人类型
				createApply.setFeeType("");//病人费别
				createApply.setIdCard(eu.getId_num());//身份证号
				createApply.setPatName(eu.getUser_name());//病人姓名
				createApply.setSex(sexcode);//性别1 男、2 女
				createApply.setAge(""+eu.getAge());//年龄 单位：岁
				createApply.setBirthday(eu.getBirthday());//出生日期 1990-02-28
				createApply.setTelephone(eu.getPhone());//手机号
				createApply.setAddress(eu.getAddress());//地址
				createApply.setDiagnosisCode("");//诊断编码
				createApply.setDiagnosisName("");//诊断名称
				createApply.setWardCode("");//病区编码
				createApply.setWardName("");//病区名称
				createApply.getBedNo();//病人床号
				createApply.setDeptCode(lismessage.getDoctor().getDept_code());//科室编码
				createApply.setDeptName(lismessage.getDoctor().getDept_name());//科室名称
				createApply.setDoctCode(lismessage.getDoctor().getDoctorCode());//医生编码
				createApply.setDoctName(lismessage.getDoctor().getDoctorName());//医生名称
				createApply.setTimeStamp(DateTimeUtil.getDateTime());//Datetime申请时间 2018-06-23 10:11:12
				createApply.setSampleNo(lcs.getReq_no());//样本编码
				createApply.setSampleType("");//样本类型
				for (LisComponent lc : lcs.getItemList()) {
					ItemsApplyLisXHHK item = new ItemsApplyLisXHHK();
					item.setItemCode(lc.getItemCode());//检验项目编号
					item.setItemName(lc.getItemName());//检验项目名称
	//				item.setSampleNo();//样本编号
					item.setSampleText(lc.getSpecimenNaturalname());//样本说明
					item.setPrice(lc.getItemprice());//项目价格 115.2元
					createApply.getItems().add(item);
				}
				String jsonStr = new Gson().toJson(createApply, CreateApply.class);
				TranLogTxt.liswriteEror_to_txt(logname, "入参:" + jsonStr);
				//String   result = HttpUtil.doPostSSL(url, jsonStr, "utf-8");
				 String result = HttpClientUtil.doPostSSLHttps(url, jsonStr, "utf-8");
				if(result == null){
					rb.getResultHeader().setTypeCode("AE");
					rb.getControlActProcess().setList(appList);
					TranLogTxt.liswriteEror_to_txt(logname, "返回为空的时候 输出" + result);
					rb.getResultHeader().setText("申请发送失败");
				}
				
				TranLogTxt.liswriteEror_to_txt(logname, "返回:" + result);
				ResponseXHHK response = new Gson().fromJson(result, ResponseXHHK.class);
				try {
					if(response.getCode() == 0) {
						ApplyNOBean an = new ApplyNOBean();
						an.setApplyNO(lcs.getReq_no());
						appList.add(an);
						rb.getResultHeader().setTypeCode("AA");
						rb.getControlActProcess().setList(appList);
						rb.getResultHeader().setText("申请发送成功");
					}else{
						rb.getResultHeader().setTypeCode("AE");
						rb.getControlActProcess().setList(appList);
						rb.getResultHeader().setText("申请发送失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ControlActLisProcess con= new ControlActLisProcess();
			con.setList(appList);
			rb.setControlActProcess(con);
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(appList);
			rb.getResultHeader().setText("申请发送成功");
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		TranLogTxt.liswriteEror_to_txt(logname, "主系统:" + rb.getResultHeader().getTypeCode()+"==="+rb.getResultHeader().getText());
		
		return rb;
	}
	
	
	
	
}
