package com.hjw.webService.client;

import java.io.IOException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bdyx.LISDELSendMessageBDYX;
import com.hjw.webService.client.bdyx.useCode.LISDELSendMessageBDYX_UseCode;
import com.hjw.webService.client.bjxy.LisDelInterface;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.cangzhoueryuan.LISDELSendMessageCZRY;
import com.hjw.webService.client.changan.LISDELSendMessageCA;
import com.hjw.webService.client.dashiqiao.LISDELSendMessageDSQ;
import com.hjw.webService.client.dbgj.LISDELSendMessageTJPT;
import com.hjw.webService.client.donghua.LISDELSendMessageDH;
import com.hjw.webService.client.empty.LISDELSendMessageEmpty;
import com.hjw.webService.client.fangzheng.LISDELSendMessageFZ;
import com.hjw.webService.client.guihang300.LISDELSendMessageGH300;
import com.hjw.webService.client.heilongjiang.LISDELSendMessageHLJ;
import com.hjw.webService.client.hokai.LISDELSendMessageHK;
import com.hjw.webService.client.hokai305.LISDELSendMessageHK305;
import com.hjw.webService.client.hzzbmz.LISDELSendMessageHZZBMZ;
import com.hjw.webService.client.jsjg.LISDELSendMessageJSJG;
import com.hjw.webService.client.nanfeng.LISDelSendMessageNF;
import com.hjw.webService.client.sinosoft.LISDELSendMessageSinoSoft;
import com.hjw.webService.client.sinosoft.LisSendMessageSinoSoft;
import com.hjw.webService.client.sxwn.LISDELSendMessageSxWn;
import com.hjw.webService.client.tianchang.LISDELSendMessageTC;
import com.hjw.webService.client.tiantan.LISDELSendMessageTT;
import com.hjw.webService.client.tj180.LISDELSendMessageTj180;
import com.hjw.webService.client.xhhk.LISDELSendMessageXHHK;
import com.hjw.webService.client.xintong.LISDELSendMessageQH;
import com.hjw.webService.client.zhonglian.LISDELSendMessageZLLF;
import com.hjw.webService.client.zhonglian.LISDELSendMessageZLWC;
import com.hjw.webService.client.zhonglian.sxjt.LISDELSendMessageZLSXJT;
import com.hjw.webService.client.zhonglian.waijian.LISDELSendMessageZLWJ;
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
public class LISDELSendMessage {
	private final static String mesType="LIS_DEL";
	private LisMessageBody lismessage;
	
	public LISDELSendMessage(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody lisSend(String url,String userType,boolean flags) {
		String logName="reqLisDel";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");

		TranLogTxt.liswriteEror_to_txt(logName, "=== 传入参数 userType===:" + userType);
		
		ResultLisBody rb = new ResultLisBody();
		 if ("0".equals(userType)) {//空实现，直接返回成功
				LISDELSendMessageEmpty lisdel= new LISDELSendMessageEmpty(lismessage);
				rb = lisdel.getMessage(url, logName);
		}else if ("1".equals(userType)) {//东北国际 天健平台
			LISDELSendMessageTJPT lisdel= new LISDELSendMessageTJPT(lismessage);
			rb = lisdel.getMessage(url, logName);
		}else if ("1.1".equals(userType)) {//长安医院 天健
			LISDELSendMessageCA lisdel= new LISDELSendMessageCA(lismessage);
			rb = lisdel.getMessage(url, logName,true);
		}else if ("2".equals(userType)) {//中联-临汾
			LISDELSendMessageZLLF lisdel= new LISDELSendMessageZLLF(lismessage);
			rb = lisdel.getMessage(url, logName,true);
		}else if ("2.1".equals(userType)) {//中联-武昌
			LISDELSendMessageZLWC lisdel= new LISDELSendMessageZLWC(lismessage);
			rb = lisdel.getMessage(url, logName,true);
		}else if ("2.2".equals(userType)) {//中联-陕西省交通医院
			LISDELSendMessageZLSXJT lisdel= new LISDELSendMessageZLSXJT(lismessage);
			rb = lisdel.getMessage(url, logName,true);
		}else if ("2.3".equals(userType)) {//中联-陕西交通-外检
			LISDELSendMessageZLWJ lisdel= new LISDELSendMessageZLWJ(lismessage);
			rb = lisdel.getMessage(url, logName,true);
		}else if ("3".equals(userType)) {//山西人民 卫宁			
			LISDELSendMessageSxWn lisdel= new LISDELSendMessageSxWn(lismessage);
			rb = lisdel.getMessage(url, logName,true);
		}else if("4".equals(userType)){//北京西苑医院
			LisDelInterface pms=new LisDelInterface(lismessage);
			try {
				rb = pms.getMessage(logName);
				JSONObject json = JSONObject.fromObject(lismessage);
				String req=json.toString();
				JSONObject json1 = JSONObject.fromObject(rb);
				String res=json1.toString();
				TranLogTxt.liswriteEror_to_txt(logName,"req:"+req+"\r\n"+"res:"+res);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}else if ("5".equals(userType)) {//湖州浙北明州医院		
			LISDELSendMessageHZZBMZ lisdel= new LISDELSendMessageHZZBMZ(lismessage);
			rb = lisdel.getMessage(url, logName);
		}else if ("6".equals(userType)) {//tj180	
			LISDELSendMessageTj180 lisdel= new LISDELSendMessageTj180(lismessage);
			rb = lisdel.getMessage(url, logName,true);
		}else if ("8".equals(userType)) {//方正	
			LISDELSendMessageFZ lisdel= new LISDELSendMessageFZ(lismessage);
			rb = lisdel.getMessage(url, logName,true);
		}else if ("10".equals(userType)) {//天坛	
			//LisSendMessageInsertDataToDB lisdel= new LisSendMessageInsertDataToDB(lismessage);
			//rb = lisdel.getMessage("LIS_DEL", logName);
			LISDELSendMessageTT lisdel= new LISDELSendMessageTT(lismessage);
			rb = lisdel.getMessage(url, logName);
		}else if ("11".equals(userType)) {//浙江联众-贵航贵阳300
			LISDELSendMessageGH300 lis = new LISDELSendMessageGH300(lismessage);
			rb = lis.getMessage(url, logName,true);
		}else if ("12".equals(userType)) {//浙江联众-天长
			LISDELSendMessageTC lis = new LISDELSendMessageTC(lismessage);
			rb = lis.getMessage(url, logName,true);
		}else if ("16".equals(userType)) {//江苏省省级机关医院-柯林布瑞-HSB平台-LIS接口
			LISDELSendMessageJSJG lis = new LISDELSendMessageJSJG(lismessage);
			rb = lis.getMessage(url, logName,true);

		}else if ("18".equals(userType)) {//青海--信通
			LISDELSendMessageQH pms = new LISDELSendMessageQH(lismessage);
			rb = pms.getMessage(url, logName);

		}else if ("19".equals(userType)) {//银川-星华惠康
			LISDELSendMessageXHHK lisdel= new LISDELSendMessageXHHK(lismessage);
			rb = lisdel.getMessage(url, logName);

		}else if ("20".equals(userType)) {//东华-长治二院
			LISDELSendMessageDH lisdel= new LISDELSendMessageDH(lismessage);
			rb = lisdel.getMessage(url, logName);
		}else if ("21".equals(userType)) {//和佳-常德二院
			LISDELSendMessageHK lis = new LISDELSendMessageHK(lismessage);
			rb = lis.getMessage(url, logName,true);
		}else if ("21.1".equals(userType)) {//和佳-305
			LISDELSendMessageHK305 lis = new LISDELSendMessageHK305(lismessage);
			rb = lis.getMessage(url, logName,true);
		}else if ("22".equals(userType)) {//黑龙江人民医院
			LISDELSendMessageHLJ lis = new LISDELSendMessageHLJ(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("23".equals(userType)) {//沧州二院康复院区
			LISDELSendMessageCZRY lis = new LISDELSendMessageCZRY(lismessage);
			rb = lis.getMessage(url, logName);
		}else if ("24".equals(userType)) {//中科软 - 江南医院
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
			url = wcd.getConfig_url().trim();
			userType=wcd.getConfig_method();
			String config_value=wcd.getConfig_value();			
			LISDELSendMessageSinoSoft lis = new LISDELSendMessageSinoSoft(lismessage);
			rb = lis.getMessage(url,config_value, logName);
		}else if ("26".equals(userType)) {//武威肿瘤医院-北大医信
			LISDELSendMessageBDYX lisdel= new LISDELSendMessageBDYX(lismessage);
			rb = lisdel.getMessage(url, logName);
		}else if ("26.1".equals(userType)) {//武威肿瘤医院-北大医信-使用编码代替id
			LISDELSendMessageBDYX_UseCode lisdel= new LISDELSendMessageBDYX_UseCode(lismessage);
			rb = lisdel.getMessage(url, logName);
		}else if ("27".equals(userType)) {//大石桥
			LISDELSendMessageDSQ lisdel= new LISDELSendMessageDSQ(lismessage);
			rb = lisdel.getMessage(url, logName);
		}else if ("28".equals(userType)) {//南丰
			LISDelSendMessageNF lisdel= new LISDelSendMessageNF(lismessage);
			rb = lisdel.getMessage(url,logName);
		}else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		
		return rb;
	}
	
	public ResultLisBody lisSendImpl(String url,String userType,boolean flags) {
		String logName="reqLisDel";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		ResultLisBody rb = new ResultLisBody();
		if ("10".equals(userType)) {//天坛	
			LISDELSendMessageTT lisdel= new LISDELSendMessageTT(lismessage);
			rb = lisdel.getMessage(url, logName);
		} else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		
		return rb;
	}
}
