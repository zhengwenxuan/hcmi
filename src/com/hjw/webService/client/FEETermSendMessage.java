package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ShanxiXXG.FEETermSendMessageXXG;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.dashiqiao.FEETermSendMessageDSQ_bak;
import com.hjw.webService.client.empty.FEETermSendMessageEmpty;
import com.hjw.webService.client.hokai.FEETermSendMessageHK_bak;
import com.hjw.webService.client.hokai305.FEETermSendMessageHK_bak305;
import com.hjw.webService.client.huojianwa.FEETermSendMessageHjw;
import com.hjw.webService.client.liubaxian.FEETermSendMessageLBX;
import com.hjw.webService.client.nanhua.FEETermSendMessageNH;
import com.hjw.webService.client.ningyuan.TTHisSendMessageNY;
import com.hjw.webService.client.qiyang.TTHisSendMessageQY;
import com.hjw.webService.client.tianchang.FEETermSendMessageTC;
import com.hjw.webService.client.zhonglian.FEETermSendMessageZL;
import com.hjw.webService.client.zixing.TTHisSendMessageZX;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.14	挂号信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class FEETermSendMessage {

	private String  accNum="";
	private String personid="";
	public FEETermSendMessage(String personid,String accNum){
		this.accNum=accNum;
		this.personid=personid;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody feeSend() {
		String logname="reqFeeTerm";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		FeeResultBody rb = new FeeResultBody();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("PAYMENT_APPLICATION");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//
		System.out.println("userType--0--"+dahtype);
		if ("0".equals(dahtype)) {//空实现，直接返回成功
			FEETermSendMessageEmpty fm = new FEETermSendMessageEmpty(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("2".equals(dahtype)) {//zhonglian
			FEETermSendMessageZL fm = new FEETermSendMessageZL(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("9".equals(dahtype)) {//火箭蛙
			FEETermSendMessageHjw fm = new FEETermSendMessageHjw(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("10".equals(dahtype)) {//南华-创星
			FEETermSendMessageNH fm = new FEETermSendMessageNH(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("12".equals(dahtype)) {//天长
			FEETermSendMessageTC fm = new FEETermSendMessageTC(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("21".equals(dahtype)) {//和佳
			FEETermSendMessageHK_bak fm = new FEETermSendMessageHK_bak(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("21.1".equals(dahtype)) {//和佳
			FEETermSendMessageHK_bak305 fm = new FEETermSendMessageHK_bak305(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("22.1".equals(dahtype)) {//祁阳
			TTHisSendMessageQY fm = new TTHisSendMessageQY(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("23".equals(dahtype)) {//资兴
			TTHisSendMessageZX fm = new TTHisSendMessageZX(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("23.1".equals(dahtype)) {//宁远
			TTHisSendMessageNY fm = new TTHisSendMessageNY(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("27".equals(dahtype)) {//大石桥
			FEETermSendMessageDSQ_bak fm = new FEETermSendMessageDSQ_bak(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("31".equals(dahtype)) {//留坝县
			FEETermSendMessageLBX fm = new FEETermSendMessageLBX(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else if ("33".equals(dahtype)) {//山西心血管
			FEETermSendMessageXXG fm = new FEETermSendMessageXXG(personid,this.accNum);
			rb =fm.getMessage(web_url, logname);
		}else {		
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
