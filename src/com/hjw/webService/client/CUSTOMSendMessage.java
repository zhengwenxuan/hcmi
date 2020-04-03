package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.ShanxiXXG.CUSTOMSendMessageXXG;
import com.hjw.webService.client.bdyx.CUSTOMSendMessageBDYX;
import com.hjw.webService.client.bdyx.useCode.CUSTOMSendMessageBDYX_UseCode;
import com.hjw.webService.client.bjxy.CUSTOMSendMessageBJXY;
import com.hjw.webService.client.bjxy.CustomRegisterInterface;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.changan.CUSTOMSendMessageCA;
import com.hjw.webService.client.dbgj.CUSTOMSendMessageTJPT;
import com.hjw.webService.client.donghua.CUSTOMSendMessageDH;
import com.hjw.webService.client.fangzheng.CUSTOMSendMessageFZ;
import com.hjw.webService.client.hokai.CUSTOMSendMessageHK;
import com.hjw.webService.client.hokai305.CUSTOMSendMessageHK305;
import com.hjw.webService.client.insertDataToDB.CUSTOMSendMessageInsertDataToDB;
import com.hjw.webService.client.nanhua.CUSTOMSendMessageNH;
import com.hjw.webService.client.ningyuan.GetHisJZKHaoNY;
import com.hjw.webService.client.xintong.CUSTOMSendMessageQH;
import com.hjw.webService.client.yichang.CUSTOMSendMessageYC;
import com.hjw.webService.client.zhangyeshi.CUSTOMSendMessageZYS;
import com.hjw.webService.client.zhonglian.CUSTOMSendMessageZL;
import com.hjw.webService.client.zhonglian.sxjt.CUSTOMSendMessageZLSXJT;
import com.hjw.webService.client.zhonglian.waijian.CUSTOMSendMessageZLWJ;
import com.hjw.webService.client.zixing.GetHisJZKHaoZX;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

import net.sf.json.JSONObject;
import com.hjw.webService.client.dashiqiao.CUSTOMSendMessageDSQ;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.14	挂号信息服务
     * 						注册信息
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CUSTOMSendMessage {

	private Custom custom=new Custom();

	public CUSTOMSendMessage(Custom custom){
		this.custom=custom;
	}

	public ResultBody customSend(String url,String userType, boolean flags) {
		String logname="reqCust";
		TranLogTxt.liswriteEror_to_txt(logname, "-----------111-------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		ResultBody rb = new ResultBody();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		
		TranLogTxt.liswriteEror_to_txt(logname, "-------------开单医生ID---------"+doctorid+"--------------");
		
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("CUST_APPLICATION");
		url = wcd.getConfig_url().trim();
		String configvalue=wcd.getConfig_value();
		
		TranLogTxt.liswriteEror_to_txt(logname, "-------------userType---------"+userType+"--------------");
		
		if(!"0".equals(doctorid)){
			custom.setOPERATOR(doctorid);	
		}
		if ("1".equals(userType)) {
			CUSTOMSendMessageTJPT cs= new CUSTOMSendMessageTJPT(custom);
			rb=cs.getMessage(url, logname);
		}else if ("1.1".equals(userType)) {//长安医院 天健
			CUSTOMSendMessageCA cs= new CUSTOMSendMessageCA(custom);
			rb=cs.getMessage(url, logname);
		}else if ("2".equals(userType)) {
			CUSTOMSendMessageZL cs= new CUSTOMSendMessageZL(custom);
			rb=cs.getMessage(url, logname);
		}else if ("2.2".equals(userType)) {//中联-陕西交通
			CUSTOMSendMessageZLSXJT cs= new CUSTOMSendMessageZLSXJT(custom);
			rb=cs.getMessage(url, logname);
		}else if ("2.3".equals(userType)) {//中联-陕西交通-外检
			CUSTOMSendMessageZLWJ cs= new CUSTOMSendMessageZLWJ(custom);
			rb=cs.getMessage(url, logname);
		}else if("4.1".equals(userType)){//西苑-走平台
				CustomRegisterInterface cs= new CustomRegisterInterface(custom);
				rb=cs.getMessage(logname);	
				JSONObject json = JSONObject.fromObject(custom);
				String req=json.toString();
				JSONObject json1 = JSONObject.fromObject(rb);
				String res=json1.toString();
				TranLogTxt.liswriteEror_to_txt(logname,"req:"+req+"\r\n"+"res:"+res);
		}else if("4".equals(userType)){//西苑-直连his
			CUSTOMSendMessageBJXY cs= new CUSTOMSendMessageBJXY(custom);
			rb=cs.getMessage(url, logname);
		}else if ("8".equals(userType)) {
			CUSTOMSendMessageFZ cs= new CUSTOMSendMessageFZ(custom);
			rb=cs.getMessage(url, logname);
		}else if ("10".equals(userType)) {//南华
			CUSTOMSendMessageNH cs= new CUSTOMSendMessageNH(custom);
			rb=cs.getMessage(url, logname);
		}else if ("14".equals(userType)) {//宜昌CDR
			CUSTOMSendMessageInsertDataToDB cs = new CUSTOMSendMessageInsertDataToDB(custom);
			cs.getMessage("CUST_APPLICATION", logname);
//			CUSTOMSendMessageYC cs= new CUSTOMSendMessageYC(custom);
//			rb=cs.getMessage(url, logname);
		}else if ("17".equals(userType)) {//张掖市 --坐标
			CUSTOMSendMessageZYS cs= new CUSTOMSendMessageZYS(custom);
			rb=cs.getMessage(url, logname);
		}else if ("18".equals(userType)) {//青海 --信通
			CUSTOMSendMessageQH cs= new CUSTOMSendMessageQH(custom);
			rb=cs.getMessage(url, logname);
		}else if ("20".equals(userType)) {//东华-长治二院
			CUSTOMSendMessageDH cs= new CUSTOMSendMessageDH(custom);
			rb=cs.getMessage(url, logname);
		}else if ("21".equals(userType)) {//和佳-常德二院
			CUSTOMSendMessageHK cs= new CUSTOMSendMessageHK(custom);
			rb=cs.getMessage(url,configvalue,logname);
		}else if ("21.1".equals(userType)) {//和佳-305
			CUSTOMSendMessageHK305 cs= new CUSTOMSendMessageHK305(custom);
			rb=cs.getMessage(url,configvalue,logname);
		}else if ("23".equals(userType)) {// 资兴
			GetHisJZKHaoZX cs= new GetHisJZKHaoZX(custom);
			rb=cs.getMessage(url,configvalue,logname);
		}else if ("23.1".equals(userType)) {// 宁远
			GetHisJZKHaoNY cs= new GetHisJZKHaoNY(custom);
			rb=cs.getMessage(url,configvalue,logname);
		}else if ("26".equals(userType)) {//武威肿瘤医院-北大医信
			CUSTOMSendMessageBDYX cs= new CUSTOMSendMessageBDYX(custom);
			rb=cs.getMessage(url, logname);
		}else if ("26.1".equals(userType)) {//武威肿瘤医院-北大医信-使用编码代替id
			CUSTOMSendMessageBDYX_UseCode cs= new CUSTOMSendMessageBDYX_UseCode(custom);
			rb=cs.getMessage(url, logname);
		}else if ("27".equals(userType)) {//大石桥市
			CUSTOMSendMessageDSQ cs= new CUSTOMSendMessageDSQ(custom);
			rb=cs.getMessage(url,configvalue,logname);
		}else if ("33".equals(userType)) {//山西心血管疾病医院
			CUSTOMSendMessageXXG cs= new CUSTOMSendMessageXXG(custom);
			rb=cs.getMessage(url,logname);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
	public ResultBody customSendImpl(String url,String userType, boolean flags) {
		String logname = "reqCustImpl";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		ResultBody rb = new ResultBody();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		
		TranLogTxt.liswriteEror_to_txt(logname, "-------------开单医生ID---------"+doctorid+"--------------");
		
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("CUST_APPLICATION");
		url = wcd.getConfig_url().trim();
		String configvalue=wcd.getConfig_value();
		
		TranLogTxt.liswriteEror_to_txt(logname, "-------------userType---------"+userType+"--------------");
		
		if(!"0".equals(doctorid)){
			custom.setOPERATOR(doctorid);	
		}
		if ("14".equals(userType)) {//宜昌CDR
			CUSTOMSendMessageYC cs= new CUSTOMSendMessageYC(custom);
			rb=cs.getMessage(url, logname);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
}
