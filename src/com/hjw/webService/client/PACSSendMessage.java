package com.hjw.webService.client;


import java.util.Calendar;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.LockCenterDateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Carestream.PACSSendMessageRK;
import com.hjw.webService.client.RADinfo.PACSSendMessageZJLD;
import com.hjw.webService.client.SanMenXia.PACSSendMessageSMX;
import com.hjw.webService.client.bdyx.PACSSendMessageBDYX;
import com.hjw.webService.client.bdyx.useCode.PACSSendMessageBDYX_UseCode;
import com.hjw.webService.client.bjxy.PacsApplyInterface;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.changan.PACSSendMessageCA;
import com.hjw.webService.client.dashiqiao.PACSSendMessageDSQ;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.webService.client.dbgj.PACSSendMessageTJPT;
import com.hjw.webService.client.donghua.PACSSendMessageDH;
import com.hjw.webService.client.erfuyuan.PACSSendMessageEFY;
import com.hjw.webService.client.fangzheng.PACSSendMessageFZ;
import com.hjw.webService.client.heilongjiang.PACSSendMessageHLJ;
import com.hjw.webService.client.hokai.PACSSendMessageHK;
import com.hjw.webService.client.hokai305.PACSSendMessageHK305;
import com.hjw.webService.client.insertDataToDB.PacsSendMessageInsertDataToDB;
import com.hjw.webService.client.nanfeng.PACSSendMessageNF;
import com.hjw.webService.client.sinosoft.PACSSendMessageSinosoft;
import com.hjw.webService.client.tiantan.PACSSendMessageTT;
import com.hjw.webService.client.tj180.PACSSendMessageTJ180;
import com.hjw.webService.client.xianning.PACSSendMessageXN;
import com.hjw.webService.client.xintong.PACSSendMessageQH;
import com.hjw.webService.client.yichang.PACSSendMessageYC;
import com.hjw.webService.client.zhonglian.PACSSendMessageZL;
import com.hjw.webService.client.zhonglian.sxjt.PACSSendMessageZLSXJT;
import com.hjw.webService.client.zhonglian.waijian.PACSSendMessageZLWJ;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSSendMessage {
	private PacsMessageBody lismessage;	
    private final String mesType="PACS_SEND";
	public PACSSendMessage(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody pacsSend(String url, String userType, boolean debug) {
		String logname = "reqPacs";
		ResultPacsBody rb = new ResultPacsBody();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String doctorid = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		if(!"0".equals(doctorid)){
			Doctor d = new Doctor();
			d=this.lismessage.getDoctor();
			d.setDoctorCode(doctorid);
			this.lismessage.setDoctor(d);
		}
		
		String doctorname = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
		if(!"0".equals(doctorname)){
			Doctor d = new Doctor();
			d=this.lismessage.getDoctor();
			d.setDoctorName(doctorname);
			this.lismessage.setDoctor(d);
		}
		
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		url = wcd.getConfig_url().trim();
		userType=wcd.getConfig_method();
		
		TranLogTxt.liswriteEror_to_txt(logname, "=== 传入参数 userType===:" + userType);
		
		if ("1".equals(userType)) {
			PACSSendMessageTJPT pms = new PACSSendMessageTJPT(lismessage);
			rb = pms.getMessage(url, logname, debug);
		}else if ("1.1".equals(userType)) {//长安医院 天健
			PACSSendMessageCA pms = new PACSSendMessageCA(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("2".equals(userType)) {//中联
			PACSSendMessageZL pms = new PACSSendMessageZL(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("2.2".equals(userType)) {//中联-陕西交通
			PACSSendMessageZLSXJT pms = new PACSSendMessageZLSXJT(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("2.3".equals(userType)) {//中联-陕西交通-外检
			PACSSendMessageZLWJ pms = new PACSSendMessageZLWJ(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("4".equals(userType)) {//PacsApplyIntemeirface
			PacsApplyInterface pms=new PacsApplyInterface(lismessage);
			rb=pms.getMessage(logname);
			JSONObject json = JSONObject.fromObject(lismessage);
			String req=json.toString();
			JSONObject json1 = JSONObject.fromObject(rb);
			String res=json1.toString();
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+req+"\r\n"+"res:"+res);
		}else if ("5".equals(userType)) {//tj180
			PACSSendMessageTJ180 pms = new PACSSendMessageTJ180(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("7".equals(userType)) {//全景锐珂
			String convalue=wcd.getConfig_value();
			PACSSendMessageRK pms = new PACSSendMessageRK(lismessage);
			rb = pms.getMessage(url,convalue, logname);
		}else if ("8".equals(userType)) {//方正平台
			PACSSendMessageFZ pms = new PACSSendMessageFZ(lismessage);
			rb = pms.getMessage(url, logname, true);
		}else if ("9".equals(userType)) {//浙江莱达信息技术有限公司
			PACSSendMessageZJLD pms = new PACSSendMessageZJLD(lismessage);
			String convalue=wcd.getConfig_value();//oracle
			rb = pms.getMessage(url,convalue, logname);
		}else if ("10".equals(userType)) {//天坛
			PACSSendMessageTT lis = new PACSSendMessageTT(lismessage);
			String convalue=wcd.getConfig_value();//oracle
			rb = lis.getMessage(url,convalue, logname);
		}else if ("11".equals(userType)) {//三门峡
			PACSSendMessageSMX pms = new PACSSendMessageSMX(lismessage);
			//String convalue=wcd.getConfig_value();//oracle
			rb = pms.getMessage(url, logname);
		}else if ("13".equals(userType)) {//二附院-英飞达
			ResHdMeessage rhd = LockCenterDateUtil.SetEaminatioinCenterDate(2020, Calendar.FEBRUARY, 05, logname);
			if(rhd.getStatus().equals("AE")){
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(rhd.getMessage());
				return rb;
			}
			PACSSendMessageEFY pms = new PACSSendMessageEFY(lismessage);
			rb = pms.getMessage(url, logname, debug);
		}else if ("14".equals(userType)) {//宜昌CDR
//			PACSSendMessageYC pms = new PACSSendMessageYC(lismessage);
//			rb = pms.getMessage(url, logname);
			PacsSendMessageInsertDataToDB lis = new PacsSendMessageInsertDataToDB(lismessage);
			rb = lis.getMessage("PACS_SEND", logname);
		}else if ("15".equals(userType)) {//咸宁
			PACSSendMessageXN pms = new PACSSendMessageXN(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("18".equals(userType)) {//青海--信通
			PACSSendMessageQH pms = new PACSSendMessageQH(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("20".equals(userType)) {//东华-长治二院
			PACSSendMessageDH pms = new PACSSendMessageDH(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("21".equals(userType)) {//和佳-常德二院
			PACSSendMessageHK pms = new PACSSendMessageHK(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("21.1".equals(userType)) {//和佳-305
			PACSSendMessageHK305 pms = new PACSSendMessageHK305(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("22".equals(userType)) {//黑龙江人民医院
			PACSSendMessageHLJ pms = new PACSSendMessageHLJ(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("24".equals(userType)) {//中科软 - 江南医院
			String config_value=wcd.getConfig_value();		
			PACSSendMessageSinosoft pms = new PACSSendMessageSinosoft(lismessage);
			rb = pms.getMessage(url,config_value, logname);
		}else if ("26".equals(userType)) {//武威肿瘤医院-北大医信
			PACSSendMessageBDYX pms = new PACSSendMessageBDYX(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("26.1".equals(userType)) {//武威肿瘤医院-北大医信-使用编码代替id
			PACSSendMessageBDYX_UseCode pms = new PACSSendMessageBDYX_UseCode(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("27".equals(userType)) {//大石桥
			PACSSendMessageDSQ pms = new PACSSendMessageDSQ(lismessage);
			rb = pms.getMessage(url, logname);
		}else if ("28".equals(userType)) {//南丰
			PACSSendMessageNF pms = new PACSSendMessageNF(lismessage);
			rb = pms.getMessage(url,logname);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb; 
	}
	
	
	/**
	 * 
	 * @param url
	 * @param userType
	 * @param flags
	 * @return
	 */
	public ResultPacsBody pacsSendImpl(String url,String userType,boolean flags) {
		String logname = "reqPacsImpl";
		ResultPacsBody rb = new ResultPacsBody();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String doctorid = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		if(!"0".equals(doctorid)){
			Doctor d = new Doctor();
			d=this.lismessage.getDoctor();
			d.setDoctorCode(doctorid);
			this.lismessage.setDoctor(d);
		}
		
		String doctorname = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
		if(!"0".equals(doctorname)){
			Doctor d = new Doctor();
			d=this.lismessage.getDoctor();
			d.setDoctorName(doctorname);
			this.lismessage.setDoctor(d);
		}
		
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		url = wcd.getConfig_url().trim();
		userType=wcd.getConfig_method();
		
		if ("10".equals(userType)) {//天坛
			PACSSendMessageTT lis = new PACSSendMessageTT(lismessage);
			rb = lis.getMessage(url,"PACS_SEND", logname);
		}else if ("14".equals(userType)) {//宜昌CDR
			PACSSendMessageYC pms = new PACSSendMessageYC(lismessage);
			rb = pms.getMessage(url, logname);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb; 
	
		
	}
}
