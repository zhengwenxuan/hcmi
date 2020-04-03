package com.hjw.webService.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.LockCenterDateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.ShanxiXXG.FeeSendmessageXXG;
import com.hjw.webService.client.bjxy.CostApplyInterface;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.changan.FEESendMessageCA;
import com.hjw.webService.client.dashiqiao.FEESendMessageDSQ;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.webService.client.dbgj.FEESendMessageTJPT;
import com.hjw.webService.client.empty.FEESendMessageEmpty;
import com.hjw.webService.client.fangzheng.FEESendMessageZL2;
import com.hjw.webService.client.hghis.FEESendMessageHG;
import com.hjw.webService.client.hokai.FEESendMessageHK;
import com.hjw.webService.client.hokai305.FEESendMessageHK305;
import com.hjw.webService.client.huojianwa.FEESendMessageHjw;
import com.hjw.webService.client.liubaxian.FEESendMessageLBX;
import com.hjw.webService.client.nanhua.FEESendMessageNH;
import com.hjw.webService.client.ningyuan.GRHisSendMessageNY;
import com.hjw.webService.client.qiyang.GRHisSendMessageQY;
import com.hjw.webService.client.tianchang.FEESendMessageTC;
import com.hjw.webService.client.tj180.FEESendMessageTj180;
import com.hjw.webService.client.xintong.FEESendMessageQH;
import com.hjw.webService.client.zhangyeshi.FEESendMessageZYS;
import com.hjw.webService.client.zhonglian.FEESendMessageZL;
import com.hjw.webService.client.zhonglian.FEESendMessageZLWC;
import com.hjw.webService.client.zixing.GRHisSendMessageZX;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.14	挂号信息服务(HIS交费申请)
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class FEESendMessage {

	private FeeMessage feeMessage;

	public FEESendMessage(FeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody feeSend(String url,String userType, boolean debug) {
		String logname="reqFee";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		FeeResultBody rb = new FeeResultBody();
		System.out.println("--------------"+userType);
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		if(!"0".equals(doctorid)){
			List<Fee> PROJECT = new ArrayList<Fee>();
			for(Fee fee:feeMessage.getPROJECTS().getPROJECT()){
				fee.setORDERED_BY_DOCTOR(doctorid);
				PROJECT.add(fee);
			}
			feeMessage.getPROJECTS().setPROJECT(PROJECT);
		}
		
		String YB_DOCTOR = configService.getCenterconfigByKey("YB_DOCTOR").getConfig_value().trim();//医保医师
		if(!"0".equals(YB_DOCTOR)){
			String YB_DIAG = configService.getCenterconfigByKey("YB_DIAG").getConfig_value().trim();//医保诊断
			List<Fee> PROJECT = new ArrayList<Fee>();
			for(Fee fee:feeMessage.getPROJECTS().getPROJECT()){
				fee.setYB_DOCTOR(YB_DOCTOR);
				fee.setYB_DIAG(YB_DIAG);
				PROJECT.add(fee);
			}
			feeMessage.getPROJECTS().setPROJECT(PROJECT);
		}
		
		if ("0".equals(userType)) {//空实现，直接返回成功
			FEESendMessageEmpty fm = new FEESendMessageEmpty(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("1".equals(userType)) {//东北国际
			FEESendMessageTJPT fm = new FEESendMessageTJPT(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("1.1".equals(userType)) {//长安医院 天健
			FEESendMessageCA fm = new FEESendMessageCA(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("2".equals(userType)) {//中联缴费
			FEESendMessageZL fm = new FEESendMessageZL(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("2.1".equals(userType)) {//中联-武昌
			ResHdMeessage rhd = LockCenterDateUtil.SetEaminatioinCenterDate(2020, Calendar.MARCH, 31, logname);
			if(rhd.getStatus().equals("AE")){
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(rhd.getMessage());
				return rb;
			}
			FEESendMessageZLWC fm = new FEESendMessageZLWC(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if("4".equals(userType)){//北京西苑医院
			CostApplyInterface pms=new CostApplyInterface(feeMessage);
			try {
				pms.getMessage(logname);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("5".equals(userType)) {//武汉黄冈
			FEESendMessageHG fm = new FEESendMessageHG(feeMessage);
			rb = fm.getMessage(url, logname);
		}else if ("6".equals(userType)) {//tj180 缴费
			FEESendMessageTj180 fm = new FEESendMessageTj180(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("8".equals(userType)) {//中联缴费-第二种方法
			FEESendMessageZL2 fm = new FEESendMessageZL2(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("9".equals(userType)) {//火箭蛙方案
			FEESendMessageHjw fm = new FEESendMessageHjw(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("10".equals(userType)) {//南华-创星
			FEESendMessageNH fm = new FEESendMessageNH(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("12".equals(userType)) {//联众-天长
			FEESendMessageTC fm = new FEESendMessageTC(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("17".equals(userType)) {//张掖市中医院 --坐标
			FEESendMessageZYS fm = new FEESendMessageZYS(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("18".equals(userType)) {//青海--信通
			FEESendMessageQH fm = new FEESendMessageQH(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("21".equals(userType)) {//和佳-常德二院
			FEESendMessageHK fm = new FEESendMessageHK(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("21.1".equals(userType)) {//和佳-305
			FEESendMessageHK305 fm = new FEESendMessageHK305(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("22.1".equals(userType)) {//祁阳
			GRHisSendMessageQY fm = new GRHisSendMessageQY(feeMessage);//
			rb =fm.getMessage(url, logname);
		}else if ("23".equals(userType)) {//资兴
			GRHisSendMessageZX fm = new GRHisSendMessageZX(feeMessage);//
			rb =fm.getMessage(url, logname);
		}else if ("23.1".equals(userType)) {//宁远
			GRHisSendMessageNY fm = new GRHisSendMessageNY(feeMessage);//
			rb =fm.getMessage(url, logname);
		}else if ("27".equals(userType)) {//大石桥
			FEESendMessageDSQ fm = new FEESendMessageDSQ(feeMessage);//
			rb =fm.getMessage(url, logname);
		}else if ("31".equals(userType)) {//留坝县
			FEESendMessageLBX fm = new FEESendMessageLBX(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("33".equals(userType)) {//山西心血管
			ResHdMeessage rhd = LockCenterDateUtil.SetEaminatioinCenterDate(2020, Calendar.FEBRUARY, 20, logname);
			if(rhd.getStatus().equals("AE")){
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(rhd.getMessage());
				return rb;
			}
			FeeSendmessageXXG fm = new FeeSendmessageXXG(feeMessage);
			rb =fm.getMessage(url, logname);
		}else {		
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
}
