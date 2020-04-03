package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.ShanxiXXG.CUSTOMEditMessageXXG;
import com.hjw.webService.client.bdyx.CUSTOMEDITSendMessageBDYX;
import com.hjw.webService.client.bdyx.useCode.CUSTOMEDITSendMessageBDYX_UseCode;
import com.hjw.webService.client.bjxy.CUSTOMEDITSendMessageBJXY;
import com.hjw.webService.client.bjxy.CustomEditInterface;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.changan.CUSTOMEDITSendMessageCA;
import com.hjw.webService.client.dbgj.CUSTOMEDITSendMessageTJPT;
import com.hjw.webService.client.xintong.CUSTOMEDITSendMessageQH;
import com.hjw.webService.client.hokai.CUSTOMEditSendMessageHK;
import com.hjw.webService.client.hokai305.CUSTOMEditSendMessageHK305;
import com.hjw.webService.client.nanhua.CUSTOMEDITSendMessageNH;
import com.hjw.webService.client.sinosoft.CUSTOMEditSendMessageSinoSoft;
import com.hjw.webService.client.yichang.CUSTOMEDITSendMessageYC;
import com.hjw.webService.client.zhonglian.CUSTOMEDITSendMessageZl;
import com.hjw.webService.client.zhonglian.sxjt.CUSTOMEDITSendMessageZLSXJT;
import com.hjw.webService.client.zhonglian.waijian.CUSTOMEDITSendMessageZLWJ;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

import net.sf.json.JSONObject;
/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.18	个人信息修改
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CUSTOMEDITSendMessage {
	private Custom custom=new Custom();

	public CUSTOMEDITSendMessage(Custom custom){
		this.custom=custom;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody customSend(String url, String userType, boolean debug) {
		String logname = "reqCustEdit";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		FeeResultBody rb = new FeeResultBody();
		
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("CUST_APPLICATION");
		url = wcd.getConfig_url().trim();
		String configvalue=wcd.getConfig_value();
		if(!"0".equals(doctorid)){
			custom.setOPERATOR(doctorid);
		}
		if ("1".equals(userType)) {
			CUSTOMEDITSendMessageTJPT cust = new CUSTOMEDITSendMessageTJPT(custom);
			rb = cust.getMessage(url, logname);
		}else if ("1.1".equals(userType)) {//长安医院 天健
			CUSTOMEDITSendMessageCA cust = new CUSTOMEDITSendMessageCA(custom);
			rb = cust.getMessage(url, logname);
		}else if ("2".equals(userType)) {//
			CUSTOMEDITSendMessageZl cust = new CUSTOMEDITSendMessageZl(custom);
			rb = cust.getMessage(url, logname);
		}else if ("2.2".equals(userType)) {//中联-陕西交通
			CUSTOMEDITSendMessageZLSXJT cust = new CUSTOMEDITSendMessageZLSXJT(custom);
			rb = cust.getMessage(url, logname);
		}else if ("2.3".equals(userType)) {//中联-陕西交通-外检
			CUSTOMEDITSendMessageZLWJ cust = new CUSTOMEDITSendMessageZLWJ(custom);
			rb = cust.getMessage(url, logname);
		}else if("4.1".equals(userType)){//西苑-走平台			
			CustomEditInterface cs= new CustomEditInterface(custom);
			rb=cs.getMessage(logname);	
			JSONObject json = JSONObject.fromObject(custom);
			String req=json.toString();
			JSONObject json1 = JSONObject.fromObject(rb);
			String res=json1.toString();
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+req+"\r\n"+"res:"+res);
		}else if("4".equals(userType)){//西苑-直连his
			CUSTOMEDITSendMessageBJXY cust = new CUSTOMEDITSendMessageBJXY(custom);
			rb = cust.getMessage(url, logname);
		}else if ("10".equals(userType)) {//南华
			CUSTOMEDITSendMessageNH cust = new CUSTOMEDITSendMessageNH(custom);
			rb = cust.getMessage(url, logname);
		}else if ("14".equals(userType)) {//宜昌CDR
			CUSTOMEDITSendMessageYC cust = new CUSTOMEDITSendMessageYC(custom);
			rb = cust.getMessage(url, logname);
		}else if ("18".equals(userType)) {//青海--信通
			CUSTOMEDITSendMessageQH cust = new CUSTOMEDITSendMessageQH(custom);
			rb = cust.getMessage(url, logname);
		}else if ("21".equals(userType)) {//和佳-常德二院
			CUSTOMEditSendMessageHK cs= new CUSTOMEditSendMessageHK(custom);
			rb=cs.getMessage(url,configvalue, logname);
		}else if ("21.1".equals(userType)) {//和佳-305
			CUSTOMEditSendMessageHK305 cs= new CUSTOMEditSendMessageHK305(custom);
			rb=cs.getMessage(url,configvalue, logname);
		}else if ("24".equals(userType)) {//中科软 - 江南医院
			CUSTOMEditSendMessageSinoSoft cs= new CUSTOMEditSendMessageSinoSoft(custom);
			rb=cs.getMessage(url,configvalue, logname);
		}else if ("26".equals(userType)) {//武威肿瘤医院-北大医信
			CUSTOMEDITSendMessageBDYX cust = new CUSTOMEDITSendMessageBDYX(custom);
			rb = cust.getMessage(url, logname);
		}else if ("26.1".equals(userType)) {//武威肿瘤医院-北大医信-使用编码代替id
			CUSTOMEDITSendMessageBDYX_UseCode cust = new CUSTOMEDITSendMessageBDYX_UseCode(custom);
			rb = cust.getMessage(url, logname);
		}else if ("33".equals(userType)) {//山西心血管
			CUSTOMEditMessageXXG cust = new CUSTOMEditMessageXXG(custom);
			rb = cust.getMessage(url, logname);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
