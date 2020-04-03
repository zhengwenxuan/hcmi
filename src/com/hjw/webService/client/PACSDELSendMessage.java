package com.hjw.webService.client;

import java.io.IOException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Carestream.PACSDELSendMessageRk;
import com.hjw.webService.client.RADinfo.PACSDELSendMessageZJLD;
import com.hjw.webService.client.SanMenXia.PACSDELSendMessageSMX;
import com.hjw.webService.client.bdyx.PACSDELSendMessageBDYX;
import com.hjw.webService.client.bdyx.useCode.PACSDELSendMessageBDYX_UseCode;
import com.hjw.webService.client.bjxy.PacsDelInterface;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.changan.PACSDELSendMessageCA;
import com.hjw.webService.client.dashiqiao.PACSDELSendMessageDSQ;
import com.hjw.webService.client.dbgj.PACSDELSendMessageTJPT;
import com.hjw.webService.client.donghua.PACSDELSendMessageDH;
import com.hjw.webService.client.empty.PACSDELSendMessageEmpty;
import com.hjw.webService.client.erfuyuan.PACSDELSendMessageEFY;
import com.hjw.webService.client.fangzheng.PACSDELSendMessageFZ;
import com.hjw.webService.client.heilongjiang.PACSDELSendMessageHLJ;
import com.hjw.webService.client.hokai.PACSDELSendMessageHK;
import com.hjw.webService.client.hokai305.PACSDELSendMessageHK305;
import com.hjw.webService.client.nanfeng.PACSDelSendMessageNF;
import com.hjw.webService.client.sinosoft.PACSDELSendMessageSinoSoft;
import com.hjw.webService.client.tiantan.PacsDELSendMessageTT;
import com.hjw.webService.client.tj180.PACSDELSendMessageTj180;
import com.hjw.webService.client.xianning.PACSDELSendMessageXN;
import com.hjw.webService.client.xintong.PACSDELSendMessageQH;
import com.hjw.webService.client.zhonglian.PACSDELSendMessageZL;
import com.hjw.webService.client.zhonglian.PACSDELSendMessageZLWC;
import com.hjw.webService.client.zhonglian.sxjt.PACSDELSendMessageZLSXJT;
import com.hjw.webService.client.zhonglian.waijian.PACSDELSendMessageZLWJ;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

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
public class PACSDELSendMessage {
	private PacsMessageBody lismessage;	
	private final String mesType="PACS_DEL";
	public PACSDELSendMessage(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody pacsSend(String url,String userType, boolean debug) {
		String logName="reqPacsDel";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		url = wcd.getConfig_url().trim();
		userType=wcd.getConfig_method();	
		
		ResultPacsBody rb = new ResultPacsBody();
		if ("0".equals(userType)) {//空实现，直接返回成功
			PACSDELSendMessageEmpty lisdel= new PACSDELSendMessageEmpty(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("1".equals(userType)) {
			PACSDELSendMessageTJPT lisdel= new PACSDELSendMessageTJPT(lismessage);
			rb = lisdel.getMessage(url, logName,debug);
		}else if ("1.1".equals(userType)) {//长安医院 天健
			PACSDELSendMessageCA lisdel= new PACSDELSendMessageCA(lismessage);
			rb = lisdel.getMessage(url, logName,debug);
		}else if ("2".equals(userType)) {//中联
			PACSDELSendMessageZL lisdel= new PACSDELSendMessageZL(lismessage);
			rb = lisdel.getMessage(url, logName,debug);
		}else if ("2.1".equals(userType)) {//中联-武昌
			PACSDELSendMessageZLWC lisdel= new PACSDELSendMessageZLWC(lismessage);
			rb = lisdel.getMessage(url, logName,debug);
		}else if ("2.2".equals(userType)) {//中联-陕西省交通医院
			PACSDELSendMessageZLSXJT lisdel= new PACSDELSendMessageZLSXJT(lismessage);
			rb = lisdel.getMessage(url, logName,debug);
		}else if ("2.3".equals(userType)) {//中联-陕西交通-外检
			PACSDELSendMessageZLWJ lisdel= new PACSDELSendMessageZLWJ(lismessage);
			rb = lisdel.getMessage(url, logName,debug);
		}else if ("4".equals(userType)) {
			PacsDelInterface pms=new PacsDelInterface(lismessage);
			try {
				rb=pms.getMessage(logName);
				JSONObject json = JSONObject.fromObject(lismessage);
				String req=json.toString();
				JSONObject json1 = JSONObject.fromObject(rb);
				String res=json1.toString();
				TranLogTxt.liswriteEror_to_txt(logName,"req:"+req+"\r\n"+"res:"+res);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if ("5".equals(userType)) {//tj180
			PACSDELSendMessageTj180 lisdel= new PACSDELSendMessageTj180(lismessage);
			rb = lisdel.getMessage(url, logName,debug);
		}else if ("7".equals(userType)) {//全景锐珂
			String convalue=wcd.getConfig_value();
			PACSDELSendMessageRk lisdel= new PACSDELSendMessageRk(lismessage);
			rb = lisdel.getMessage(url,convalue, logName,debug);
		}else if ("8".equals(userType)) {//方正
			PACSDELSendMessageFZ lisdel= new PACSDELSendMessageFZ(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("9".equals(userType)) {//浙江莱达信息技术有限公司
			PACSDELSendMessageZJLD lisdel= new PACSDELSendMessageZJLD(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("10".equals(userType)) {//天坛
			 PacsDELSendMessageTT lis = new PacsDELSendMessageTT(lismessage);
				rb = lis.getMessage(url, logName);
		}else if ("11".equals(userType)) {//三门峡
			PACSDELSendMessageSMX lisdel= new PACSDELSendMessageSMX(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("13".equals(userType)) {//西安交大第二附属医院
			PACSDELSendMessageEFY lisdel= new PACSDELSendMessageEFY(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("15".equals(userType)) {//咸宁
			PACSDELSendMessageXN lisdel= new PACSDELSendMessageXN(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("18".equals(userType)) {//青海 == 信通
			PACSDELSendMessageQH lisdel= new PACSDELSendMessageQH(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("20".equals(userType)) {//东华-长治二院
			PACSDELSendMessageDH lisdel= new PACSDELSendMessageDH(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("21".equals(userType)) {//和佳-常德二院
			PACSDELSendMessageHK lisdel= new PACSDELSendMessageHK(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("21.1".equals(userType)) {//和佳-305
			PACSDELSendMessageHK305 lisdel= new PACSDELSendMessageHK305(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("22".equals(userType)) {//黑龙江人民医院
			PACSDELSendMessageHLJ lisdel= new PACSDELSendMessageHLJ(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("24".equals(userType)) {//中科软 - 江南医院
			String config_value=wcd.getConfig_value();		
			PACSDELSendMessageSinoSoft lisdel= new PACSDELSendMessageSinoSoft(lismessage);
			rb = lisdel.getMessage(url,config_value,logName);
		}else if ("26".equals(userType)) {//武威肿瘤医院-北大医信
			PACSDELSendMessageBDYX lisdel= new PACSDELSendMessageBDYX(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("26.1".equals(userType)) {//武威肿瘤医院-北大医信-使用编码代替id
			PACSDELSendMessageBDYX_UseCode lisdel= new PACSDELSendMessageBDYX_UseCode(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("27".equals(userType)) {//大石桥
			PACSDELSendMessageDSQ lisdel= new PACSDELSendMessageDSQ(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else if ("28".equals(userType)) {//大石桥
			PACSDelSendMessageNF lisdel= new PACSDelSendMessageNF(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
	
	public ResultPacsBody pacsSendImpl(String url,String userType, boolean debug) {
		String logName="reqPacsDel";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		url = wcd.getConfig_url().trim();
		userType=wcd.getConfig_method();	
		ResultPacsBody rb= new ResultPacsBody();
		 if ("10".equals(userType)) {//天坛
			 PacsDELSendMessageTT lis = new PacsDELSendMessageTT(lismessage);
			rb = lis.getMessage(url, logName);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
