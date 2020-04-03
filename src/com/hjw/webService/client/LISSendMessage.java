package com.hjw.webService.client;

import java.io.IOException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.bdyx.LISSendMessageBDYX;
import com.hjw.webService.client.bdyx.useCode.LISSendMessageBDYX_UseCode;
import com.hjw.webService.client.bjxy.LisApplyInterface;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.cangzhoueryuan.LISSendMessageCZRY;
import com.hjw.webService.client.changan.LISSendMessageCA;
import com.hjw.webService.client.dashiqiao.LisSendMessageDSQ;
import com.hjw.webService.client.dbgj.LISSendMessageTJPT;
import com.hjw.webService.client.donghua.LISSendMessageDH;
import com.hjw.webService.client.fangzheng.LisSendMessageFZ;
import com.hjw.webService.client.guihang300.LISSendMessageGH300;
import com.hjw.webService.client.heilongjiang.LisSendMessageHLJ;
import com.hjw.webService.client.hokai.LisSendMessageHK;
import com.hjw.webService.client.hokai305.LisSendMessageHK305;
import com.hjw.webService.client.hzzbmz.LISSendMessageHZZBMZ;
import com.hjw.webService.client.insertDataToDB.LisSendMessageInsertDataToDB;
import com.hjw.webService.client.nanfeng.LISSendMessageNF;
import com.hjw.webService.client.sinosoft.LisSendMessageSinoSoft;
import com.hjw.webService.client.sxwn.LISSendMessageSxWn;
import com.hjw.webService.client.tianchang.LISSendMessageTC;
import com.hjw.webService.client.tiantan.LisSendMessageTT;
import com.hjw.webService.client.tj180.LISSendMessageTJ180;
import com.hjw.webService.client.xhhk.LISSendMessageXHHK;
import com.hjw.webService.client.xintong.LISSendMessageQH;
import com.hjw.webService.client.yichang.LISSendMessageYC;
import com.hjw.webService.client.zhonglian.LISSendMessageZL;
import com.hjw.webService.client.zhonglian.sxjt.LISSendMessageZLSXJT;
import com.hjw.webService.client.zhonglian.waijian.LISSendMessageZLWJ;
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
public class LISSendMessage {
	
	private final static String mesType="LIS_SEND";

	private LisMessageBody lismessage;

	public LISSendMessage(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody lisSend(String url,String userType,boolean flags) {
		String logName="reqLis";
		TranLogTxt.liswriteEror_to_txt(logName, "----------------"+userType+"--------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		
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
		
		String deptid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室id
		if(!"0".equals(deptid)){
			Doctor d = new Doctor();
			d=this.lismessage.getDoctor();
			d.setDept_code(deptid);
			this.lismessage.setDoctor(d);
		}
		
		String deptname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		if(!"0".equals(deptname)){
			Doctor d = new Doctor();
			d=this.lismessage.getDoctor();
			d.setDept_name(deptname);
			this.lismessage.setDoctor(d);
		}
		
		String cjfs = configService.getCenterconfigByKey("IS_LIS_CJFS").getConfig_value().trim();//采集方式
		if(!"0".equals(cjfs)){
			this.lismessage.setCjfs(cjfs);
		}
		
		String zxks = configService.getCenterconfigByKey("IS_LIS_ZXKS_ID").getConfig_value().trim();//执行科室
		if(!"0".equals(zxks)){
			this.lismessage.setZxksid(zxks);
		}
		
		System.out.println("userType--0--"+userType);
		ResultLisBody rb = new ResultLisBody();
		if ("1".equals(userType)) {//东北国际 天健
			LISSendMessageTJPT lis= new LISSendMessageTJPT(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("1.1".equals(userType)) {//长安医院 天健
			LISSendMessageCA lis= new LISSendMessageCA(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("2".equals(userType)) {//中联
			LISSendMessageZL lis= new LISSendMessageZL(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("2.2".equals(userType)) {//中联-陕西交通
			LISSendMessageZLSXJT lis= new LISSendMessageZLSXJT(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("2.3".equals(userType)) {//中联-陕西交通-外检
			LISSendMessageZLWJ lis= new LISSendMessageZLWJ(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("3".equals(userType)) {//山西人民 卫宁
			System.out.println("userType--1--"+userType);
			LISSendMessageSxWn lis= new LISSendMessageSxWn(lismessage);
			rb = lis.getMessage(url, logName);
		}else if("4".equals(userType)){//北京西苑医院
			LisApplyInterface pms=new LisApplyInterface(lismessage);
			try {
				TranLogTxt.liswriteEror_to_txt(logName,"req:"+lismessage);
				rb=pms.getMessage(logName);
				JSONObject json = JSONObject.fromObject(lismessage);
				String req=json.toString();
				JSONObject json1 = JSONObject.fromObject(rb);
				String res=json1.toString();
				TranLogTxt.liswriteEror_to_txt(logName,"req:"+req+"\r\n"+"res:"+res);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if ("5".equals(userType)) {//湖州浙北明州医院
			System.out.println("userType--1--"+userType);
			LISSendMessageHZZBMZ lis= new LISSendMessageHZZBMZ(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("6".equals(userType)) {//180医院
			System.out.println("userType--1--"+userType);
			LISSendMessageTJ180 lis= new LISSendMessageTJ180(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("8".equals(userType)) {//方正平台
			LisSendMessageFZ pms = new LisSendMessageFZ(lismessage);
			rb = pms.getMessage(url, logName);
		}else if ("10".equals(userType)) {//天坛
			//LisSendMessageInsertDataToDB lis = new LisSendMessageInsertDataToDB(lismessage);
			//rb = lis.getMessage("LIS_SEND", logName);
			LisSendMessageTT lis = new LisSendMessageTT(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("11".equals(userType)) {//浙江联众-贵航贵阳300
			LISSendMessageGH300 lis = new LISSendMessageGH300(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("12".equals(userType)) {//浙江联众-天长
			LISSendMessageTC lis = new LISSendMessageTC(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("14".equals(userType)) {//宜昌CDR
//			LISSendMessageYC lis = new LISSendMessageYC(lismessage);
//			rb = lis.getMessage(url, logName);
			LisSendMessageInsertDataToDB lis = new LisSendMessageInsertDataToDB(lismessage);
			rb = lis.getMessage("LIS_SEND", logName);
		}else if ("18".equals(userType)) {//青海--信通
			LISSendMessageQH lis = new LISSendMessageQH(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("19".equals(userType)) {//银川-星华惠康
			LISSendMessageXHHK lis= new LISSendMessageXHHK(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("20".equals(userType)) {//东华-长治二院
			LISSendMessageDH lis = new LISSendMessageDH(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("21".equals(userType)) {//和佳-常德二院
			LisSendMessageHK lis = new LisSendMessageHK(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("21.1".equals(userType)) {//和佳-305
			LisSendMessageHK305 lis = new LisSendMessageHK305(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("22".equals(userType)) {//黑龙江人民医院
			LisSendMessageHLJ lis = new LisSendMessageHLJ(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("23".equals(userType)) {//沧州二院康复院区
			LISSendMessageCZRY lis = new LISSendMessageCZRY(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("24".equals(userType)) {////中科软 - 江南医院
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
			url = wcd.getConfig_url().trim();
			userType=wcd.getConfig_method();
			String config_value=wcd.getConfig_value();			
			LisSendMessageSinoSoft lis = new LisSendMessageSinoSoft(lismessage);
			rb = lis.getMessage(url,config_value, logName);
		}else if ("26".equals(userType)) {//武威肿瘤医院-北大医信
			LISSendMessageBDYX lis= new LISSendMessageBDYX(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("26.1".equals(userType)) {//武威肿瘤医院-北大医信-使用编码代替id
			LISSendMessageBDYX_UseCode lis= new LISSendMessageBDYX_UseCode(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("27".equals(userType)) {//大石桥市
			LisSendMessageDSQ lis= new LisSendMessageDSQ(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("28".equals(userType)) {//南丰
			LISSendMessageNF lis= new LISSendMessageNF(lismessage);
			rb = lis.getMessage(url, logName);
		}else {
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
	public ResultLisBody lisSendImpl(String url,String userType,boolean flags) {
		String logName="reqLisImpl";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
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
		
		String deptid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室id
		if(!"0".equals(deptid)){
			Doctor d = new Doctor();
			d=this.lismessage.getDoctor();
			d.setDept_code(deptid);
			this.lismessage.setDoctor(d);
		}
		
		String deptname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		if(!"0".equals(deptname)){
			Doctor d = new Doctor();
			d=this.lismessage.getDoctor();
			d.setDept_name(deptname);
			this.lismessage.setDoctor(d);
		}
		
		String cjfs = configService.getCenterconfigByKey("IS_LIS_CJFS").getConfig_value().trim();//采集方式
		if(!"0".equals(cjfs)){
			this.lismessage.setCjfs(cjfs);
		}
		
		String zxks = configService.getCenterconfigByKey("IS_LIS_ZXKS_ID").getConfig_value().trim();//执行科室
		if(!"0".equals(zxks)){
			this.lismessage.setZxksid(zxks);
		}
		
		System.out.println("userType--0--"+userType);
		ResultLisBody rb = new ResultLisBody();
		if ("10".equals(userType)) {//天坛
			WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
			WebserviceConfigurationDTO wcf = webserviceConfigurationService.getWebServiceConfig("LIS_SEND");
			LisSendMessageTT lis = new LisSendMessageTT(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("14".equals(userType)) {//宜昌CDR
			LISSendMessageYC lis = new LISSendMessageYC(lismessage);
			rb = lis.getMessage(url, logName);
		}else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
