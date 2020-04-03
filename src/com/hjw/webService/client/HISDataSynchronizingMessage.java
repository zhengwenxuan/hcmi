package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bdyx.HISDataSynchronizingMessageBDYX;
import com.hjw.webService.client.bdyx.useCode.HISDataSynchronizingMessageBDYX_UseCode;
import com.hjw.webService.client.bjxy.HISDataSynchronizingMessageXY;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.webService.client.changan.HISDataSynchronizingMessageCA;
import com.hjw.webService.client.dashiqiao.HISDataSynchronizingMessageDSQ;
import com.hjw.webService.client.hghis.HISDataSynchronizingMessageHG;
import com.hjw.webService.client.hokai.HISDataSynchronizingMessageHK;
import com.hjw.webService.client.hokai305.HISDataSynchronizingMessageHK305;
import com.hjw.webService.client.nanhua.HISDataSynchronizingMessageNH;
import com.hjw.webService.client.ningyuan.AutoUpdateClinicNY;
import com.hjw.webService.client.tiantan.HISDataSynchronizingMessageTT;
import com.hjw.webService.client.xintong.ManualGetClinicItemMessageQH;
import com.hjw.webService.client.zhonglian.AutoGetHisDataZLWC;
import com.hjw.webService.client.zixing.AutoUpdateClinicZX;

public class HISDataSynchronizingMessage {

	public ResultHisBody dataSend(String url,String userType,boolean flags) {
		String logname = "his_data_syn";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		ResultHisBody rb = new ResultHisBody();
		if ("1.1".equals(userType)) {//天健-长安医院
			HISDataSynchronizingMessageCA his = new HISDataSynchronizingMessageCA();
			rb = his.getMessage(url, logname);
		}else if ("4".equals(userType)) {//西苑-北大医信
			HISDataSynchronizingMessageXY his = new HISDataSynchronizingMessageXY();
			rb = his.getMessage(url, logname);
		}else if ("5".equals(userType)) {//湖北黄岗医院
			HISDataSynchronizingMessageHG his = new HISDataSynchronizingMessageHG();
			rb = his.getMessage(url, logname);
		}else if ("2.1".equals(userType)) {//中联 武昌
			AutoGetHisDataZLWC his = new AutoGetHisDataZLWC();
			rb = his.getMessage(url, logname);
		}else if ("10".equals(userType)) {//南华-创星
			HISDataSynchronizingMessageNH his = new HISDataSynchronizingMessageNH();
			rb = his.getMessage(url, logname);
		}else if ("18".equals(userType)) {//青海
			ManualGetClinicItemMessageQH his = new ManualGetClinicItemMessageQH();
			rb = his.getMessage(url, logname);

		}else if ("23".equals(userType)) {//资兴
			AutoUpdateClinicZX his = new AutoUpdateClinicZX();
			rb = his.getMessage(url, logname);

		}else if ("23.1".equals(userType)) {//宁远
			AutoUpdateClinicNY his = new AutoUpdateClinicNY();
			rb = his.getMessage(url, logname);

		}else if ("26".equals(userType)) {//武威肿瘤医院-北大医信
			HISDataSynchronizingMessageBDYX his = new HISDataSynchronizingMessageBDYX();
			rb = his.getMessage(url, logname);
		}else if ("26.1".equals(userType)) {//武威肿瘤医院-北大医信-使用编码代替id
			HISDataSynchronizingMessageBDYX_UseCode his = new HISDataSynchronizingMessageBDYX_UseCode();
			rb = his.getMessage(url, logname);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
	public String dataSendServer(String body,String userType,String logname) {
		String res =  "";
		if ("21".equals(userType)) {//和佳-常德二院
			HISDataSynchronizingMessageHK his = new HISDataSynchronizingMessageHK();
			res = his.getMessage(body, logname);
		} else if ("21.1".equals(userType)) {//和佳-305
			HISDataSynchronizingMessageHK305 his = new HISDataSynchronizingMessageHK305();
			res = his.getMessage(body, logname);
		}else if ("27".equals(userType)) {//大石桥市
			HISDataSynchronizingMessageDSQ his = new HISDataSynchronizingMessageDSQ();
			res = his.getMessage(body, logname);
		}/*else if ("10".equals(userType)) {//天坛
			HISDataSynchronizingMessageTT his = new HISDataSynchronizingMessageTT();
			res = his.getMessage(body, logname);
		}*/else{
			res = "接口无对应厂家,请检查webservice_configuration表config_method字段";
		}
		return res;
	}
}
