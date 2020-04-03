package com.hjw.webService.client.tiantan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.Person;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.tiantan.bean.ResLisBodyTiantan;

import net.sf.json.JSONObject;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.client.tiantan
     * @Description:  天坛Lis对接-红桥(Jnative调用dll)
     * @author: zwx  
 */
public class LisSendMessageTT{
private LisMessageBody lismessage;

	public LisSendMessageTT(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	public ResultLisBody getMessage(String url, String logName) {
		//url="http://172.28.10.97:8089/applylistest";
		ResultLisBody rb = new ResultLisBody();
		try{
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + JSONObject.fromObject(lismessage));
		TranLogTxt.liswriteEror_to_txt(logName, "url:" + url);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root>");
		  sb.append("<ID>"+this.lismessage.getCustom().getExam_num()+"</ID>");
		  sb.append("<NAME>"+this.lismessage.getCustom().getName()+"</NAME>");
		  sb.append("<KIND>T</KIND>");
		  String sexcode = "U";
			if("2".equals(this.lismessage.getCustom().getSexcode())) {
				sexcode = "F";
			} else if("1".equals(this.lismessage.getCustom().getSexcode())) {
				sexcode = "M";
			}
		  sb.append("<SEX>"+sexcode+"</SEX>");
		  sb.append("<LOCATION>");
		  sb.append("</LOCATION>");
		  sb.append("<ROOM>");
		  sb.append("</ROOM>");
		  sb.append("<BED>");
		  sb.append("</BED>");
		  sb.append("<AGE>"+this.lismessage.getCustom().getOld()+"</AGE>");
		  sb.append("<AGEUNIT>Y</AGEUNIT>");
		  sb.append("<DEPARTMENT>"+this.lismessage.getDoctor().getDept_code()+"</DEPARTMENT>");
		  sb.append("<DOCTOR>"+this.lismessage.getDoctor().getDoctorCode()+"</DOCTOR>");
		  sb.append("<DOCTORDATE>"+DateTimeUtil.getDate()+"</DOCTORDATE>");
		  sb.append("<DOCTORTIME>"+getDateTimes2()+"</DOCTORTIME>");
		  sb.append("<DIAG>体检待查</DIAG>");
		  sb.append("<PRIORITY>R</PRIORITY>");
		  sb.append("<SAMPLER>");
		  sb.append("</SAMPLER>");
		  sb.append("<SAMPLEDATE>");
		  sb.append("</SAMPLEDATE>");
		  sb.append("<SAMPLETIME>");
		  sb.append("</SAMPLETIME>");
		  sb.append("<SAMPLENOTES>");
		  sb.append("</SAMPLENOTES>");
		  sb.append("<RECEIVENOTES>");
		  sb.append("</RECEIVENOTES>");
		  sb.append("<PAT1></PAT1>");
		  sb.append("<PAT2>");
		  sb.append("</PAT2>");
		  sb.append("<PAT3>");
		  sb.append("</PAT3>");
		  sb.append("<PAT4>");
		  sb.append("</PAT4>");
		  sb.append("<PAT5>");
		  sb.append("</PAT5>");
		  sb.append("<PAT6>");
		  sb.append("</PAT6>");
		  sb.append("<PAT7>");
		  sb.append("</PAT7>");
		  sb.append("<PAT8>");
		  sb.append("</PAT8>");
		  sb.append("<PAT9>");
		  sb.append("</PAT9>");
		  sb.append("<PAT10>");
		  sb.append("</PAT10>");
		  sb.append("<PAT11>");
		  sb.append("</PAT11>");
		  sb.append("<PAT12>");
		  sb.append("</PAT12>");
		  sb.append("<PAT13>");
		  sb.append("</PAT13>");
		  sb.append("<PAT14>");
		  sb.append("</PAT14>");
		  sb.append("<PAT15>");
		  sb.append("</PAT15>");
		  sb.append("<PAT16>");
		  sb.append("</PAT16>");
		  sb.append("<PAT17>");
		  sb.append("</PAT17>");
		  sb.append("<PAT18>");
		  sb.append("</PAT18>");
		  sb.append("<PAT19>");
		  sb.append("</PAT19>");
		  sb.append("<PAT20>");
		  sb.append("</PAT20>");
		  sb.append("<PAT32>");
		  sb.append("</PAT32>");
		  sb.append("<TESTS>");
		  List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
		for (LisComponents lcs : lismessage.getComponents()) {
			List<String> itemCodeList = new ArrayList<>();
			for (LisComponent lc : lcs.getItemList()) {
				itemCodeList.add(lc.getItemCode());
				sb.append("<TESTITEM>");
				sb.append("<TESTCODE>" + lc.getItemCode() + "</TESTCODE>");
				sb.append("<HIS>" + lismessage.getCustom().getExam_num() + "" + lc.getChargingItemid() + "</HIS>");
				sb.append("</TESTITEM>");
			}
			ApplyNOBean an = new ApplyNOBean();
			an.setApplyNO(lcs.getReq_no());
			an.setItemCodeList(itemCodeList);
			appList.add(an);
		}
		  sb.append("</TESTS>");
		  sb.append("</root>");
		  TranLogTxt.liswriteEror_to_txt(logName, "req:" + sb.toString() + "\r\n");
		  String result = HttpUtil.doPost_Str(url, sb.toString(), "utf-8");
		  TranLogTxt.liswriteEror_to_txt(logName, "res:" + result + "\r\n");
		  ResLisBodyTiantan rbcomm =new ResLisBodyTiantan();
		if ((result != null) && (result.trim().length() > 0)) {
			result = result.trim();
			rbcomm = JaxbUtil.converyToJavaBean(result, ResLisBodyTiantan.class);
			if("0".equals(rbcomm.getSuccess_flag())){
				ControlActLisProcess con= new ControlActLisProcess();
				con.setList(appList);
				rb.setControlActProcess(con);
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("申请发送成功");
			}else{
				rb.getResultHeader().setText("提交检验申请失败");
				rb.getResultHeader().setTypeCode("AE");
			}
		}
		}catch(Exception ex){
			rb.getResultHeader().setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.getResultHeader().setTypeCode("AE");
		}
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + JSONObject.fromObject(rb));
		return rb;
	}
	
	public static String getDateTimes2() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		String dt = sdf.format(new Date());
		return dt;
	}

	private boolean sendPatientInfo(LieNative lieNative,Person person, Doctor doctor) {
		String sexcode = "U";
		if("2".equals(person.getSexcode())) {
			sexcode = "F";
		} else if("1".equals(person.getSexcode())) {
			sexcode = "M";
		}
		//设置病人信息
		boolean success = false;
		success = lieNative.call("LIEPatientReset");//复位清除病人信息
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 0, person.getExam_num());//0:病人ID,必填
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 1, person.getName());//1:病人姓名; 必填
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 2, "T");//2:病人类型(I病房;O门诊E急诊) 必填
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 3, sexcode);//3:病人性别(F女M男U其他) 必填
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 7, person.getBirthtime());//7:病人生日日期
		if(!success) return false;
//		success = lieNative.call("LIEPatientSet", 8, person.getName());//8:病人生日时间
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 9, person.getOld()+"");//9:年龄;必填
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 10, "Y");//10:年龄单位;(Y岁M月D天) 必填
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 17, DateTimeUtil.getDate());//17:生成日期必填,取当天日期
		if(!success) return false;
//		success = lieNative.call("LIEPatientSet", 18, DateTimeUtil.getDateTimes2().substring(0,4));//18:生成时间必填,取当天时间
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 19, doctor.getDept_code());//19:开单科室;代码 必填
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 20, doctor.getDoctorCode());//20:开单医生代码; 必填
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 21, doctor.getTime().substring(0, 8));//21:开单日期; 必填YYYYMMDD
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 22, doctor.getTime().substring(8, 12));//22:开单时间; 必填HHMM
		if(!success) return false;
//		success = lieNative.call("LIEPatientSet", 23, person.getName());//23:诊断; 必填
		if(!success) return false;
		success = lieNative.call("LIEPatientSet", 24, "R");//24:级别R常规;S急诊必填
		if(!success) return false;
//		success = lieNative.call("LIEPatientSet", 25, person.getName());//25:采样人ID
		if(!success) return false;
//		success = lieNative.call("LIEPatientSet", 26, person.getName());//26:采样人日期
		if(!success) return false;
//		success = lieNative.call("LIEPatientSet", 27, person.getName());//27:采样人时间
		if(!success) return false;
//		success = lieNative.call("LIEPatientSet", 110, person.getName());//110:标本
		//......
		return success;
	}
}
