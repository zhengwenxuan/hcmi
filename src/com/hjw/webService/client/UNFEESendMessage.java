package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ShanxiXXG.UNFEESendMessageXXG;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;
import com.hjw.webService.client.dashiqiao.UNFEESendMessageDSQ;
import com.hjw.webService.client.dbgj.UNFEESendMessageTJPT;
import com.hjw.webService.client.empty.UNFEESendMessageEmpty;
import com.hjw.webService.client.fangzheng.UNFEESendMessageZL2;
import com.hjw.webService.client.hokai.UNFEESendMessageHK;
import com.hjw.webService.client.hokai305.UNFEESendMessageHK305;
import com.hjw.webService.client.huojianwa.UNFEESendMessageHjw;
import com.hjw.webService.client.liubaxian.UNFEESendMessageLBX;
import com.hjw.webService.client.nanhua.UNFEESendMessageNH;
import com.hjw.webService.client.ningyuan.UNFEESendMessageNY;
import com.hjw.webService.client.qiyang.DelHisSendMessageQY;
import com.hjw.webService.client.tianchang.UNFEESendMessageTC;
import com.hjw.webService.client.tj180.UNFEESendMessageTj180;
import com.hjw.webService.client.zhangyeshi.UNFEESendMessageZYS;
import com.hjw.webService.client.zhonglian.UNFEESendMessageZL;
import com.hjw.webService.client.zixing.UNFEESendMessageZX;
/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.17	收费退费(加项/减项)
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class UNFEESendMessage {

	private UnFeeMessage feeMessage;
	
	public UNFEESendMessage(UnFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody feeSend(String url,String userType, boolean debug) {
		String logname="reqUNfee";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		FeeReqBody rb = new FeeReqBody();
		if ("0".equals(userType)) {//空实现，直接返回成功
			UNFEESendMessageEmpty pms = new UNFEESendMessageEmpty(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("1".equals(userType)) {
			UNFEESendMessageTJPT pms = new UNFEESendMessageTJPT(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("2".equals(userType)) {
			UNFEESendMessageZL pms = new UNFEESendMessageZL(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("6".equals(userType)) {//tj180
			UNFEESendMessageTj180 pms = new UNFEESendMessageTj180(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("8".equals(userType)) {//中联退费  2
			UNFEESendMessageZL2 pms = new UNFEESendMessageZL2(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("9".equals(userType)) {//火箭蛙方案
			UNFEESendMessageHjw pms = new UNFEESendMessageHjw(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("10".equals(userType)) {//南华-创星
			UNFEESendMessageNH pms = new UNFEESendMessageNH(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("12".equals(userType)) {//天长
			UNFEESendMessageTC pms = new UNFEESendMessageTC(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("21".equals(userType)) {//和佳-常德二院
			UNFEESendMessageHK pms = new UNFEESendMessageHK(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("21.1".equals(userType)) {//305
			UNFEESendMessageHK305 pms = new UNFEESendMessageHK305(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("17".equals(userType)) {//张掖市 --坐标
			UNFEESendMessageZYS pms = new UNFEESendMessageZYS(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("22.1".equals(userType)) {//祁阳
			DelHisSendMessageQY pms = new DelHisSendMessageQY(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("23".equals(userType)) {//资兴
			UNFEESendMessageZX pms = new UNFEESendMessageZX(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("23.1".equals(userType)) {//宁远
			UNFEESendMessageNY pms = new UNFEESendMessageNY(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("27".equals(userType)) {//大石桥市
			UNFEESendMessageDSQ pms = new UNFEESendMessageDSQ(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("31".equals(userType)) {//留坝县
			UNFEESendMessageLBX pms = new UNFEESendMessageLBX(feeMessage);
			rb = pms.getMessage(url, logname);
		}else if ("33".equals(userType)) {//山西心血管
			UNFEESendMessageXXG pms = new UNFEESendMessageXXG(feeMessage);
			rb = pms.getMessage(url, logname);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
}
