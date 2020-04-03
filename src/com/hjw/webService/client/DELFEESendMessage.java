package com.hjw.webService.client;

import java.io.IOException;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ShanxiXXG.DELFEESendMessageXXG;
import com.hjw.webService.client.bjxy.CostDelInterface;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.changan.DELFEESendMessageCA;
import com.hjw.webService.client.dashiqiao.DELFEESendMessageDSQ;
import com.hjw.webService.client.dbgj.DELFEESendMessageTJPT;
import com.hjw.webService.client.empty.DELFEESendMessageEmpty;
import com.hjw.webService.client.fangzheng.DELFEESendMessageZL2;
import com.hjw.webService.client.hghis.DELFEESendMessageHG;
import com.hjw.webService.client.hokai.DELFEESendMessageHK;
import com.hjw.webService.client.hokai305.DELFEESendMessageHK305;
import com.hjw.webService.client.huojianwa.DELFEESendMessageHjw;
import com.hjw.webService.client.liubaxian.DELFEESendMessageLBX;
import com.hjw.webService.client.nanhua.DELFEESendMessageNH;
import com.hjw.webService.client.ningyuan.DELFEESendMessageNY;
import com.hjw.webService.client.qiyang.CheXiaoHisSendMessageQY;
import com.hjw.webService.client.tianchang.DELFEESendMessageTC;
import com.hjw.webService.client.tj180.DELFEESendMessageTj180;
import com.hjw.webService.client.xintong.FEEDELSendMessageQH;
import com.hjw.webService.client.zhangyeshi.DELFEESendMessageZYS;
import com.hjw.webService.client.zhonglian.DELFEESendMessageZL;
import com.hjw.webService.client.zhonglian.DELFEESendMessageZLWC;
import com.hjw.webService.client.zixing.DELFEESendMessageZX;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.19	项目作废/删除
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessage {

	private DelFeeMessage feeMessage;
	
	public DELFEESendMessage(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody feeSend(String url, String userType, boolean debug) {
		String logname = "reqDelFee";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		FeeReqBody rb = new FeeReqBody();
		if ("0".equals(userType)) {//空实现，直接返回成功
			DELFEESendMessageEmpty dsm = new DELFEESendMessageEmpty(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("1".equals(userType)) {
			DELFEESendMessageTJPT dsm = new DELFEESendMessageTJPT(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("1.1".equals(userType)) {//长安医院 天健
			DELFEESendMessageCA fm = new DELFEESendMessageCA(feeMessage);
			rb =fm.getMessage(url, logname);
		}else if ("2".equals(userType)) {//中联
			DELFEESendMessageZL dsm = new DELFEESendMessageZL(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("2.1".equals(userType)) {//中联武昌
			DELFEESendMessageZLWC dsm = new DELFEESendMessageZLWC(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if("4".equals(userType)){//北京西苑医院
			CostDelInterface pms=new CostDelInterface(feeMessage);
			try {
				pms.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if("5".equals(userType)){//湖北黄岗
			DELFEESendMessageHG delhg = new DELFEESendMessageHG(feeMessage);
			rb = delhg.getMessage(url, logname);
		}else if ("6".equals(userType)) {//tj80
			DELFEESendMessageTj180 dsm = new DELFEESendMessageTj180(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("8".equals(userType)) {//中联2
			DELFEESendMessageZL2 dsm = new DELFEESendMessageZL2(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("9".equals(userType)) {//火箭蛙
			DELFEESendMessageHjw dsm = new DELFEESendMessageHjw(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("10".equals(userType)) {//南华-创星
			DELFEESendMessageNH dsm = new DELFEESendMessageNH(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("12".equals(userType)) {//天长
			DELFEESendMessageTC dsm = new DELFEESendMessageTC(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("17".equals(userType)) {//张掖市中医院 --坐标
			DELFEESendMessageZYS dsm = new DELFEESendMessageZYS(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("18".equals(userType)) {//青海
			FEEDELSendMessageQH dsm = new FEEDELSendMessageQH(feeMessage);
			rb = dsm.getMessage(url, logname);
		}
		else if ("21".equals(userType)) {//和佳-常德二院
			DELFEESendMessageHK dsm = new DELFEESendMessageHK(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("21.1".equals(userType)) {//和佳-305
			DELFEESendMessageHK305 dsm = new DELFEESendMessageHK305(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("22.1".equals(userType)) {//祁阳
			CheXiaoHisSendMessageQY dsm = new CheXiaoHisSendMessageQY(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("23".equals(userType)) {//资兴
			DELFEESendMessageZX dsm = new DELFEESendMessageZX(feeMessage);//
			rb = dsm.getMessage(url, logname);
		}else if ("23.1".equals(userType)) {//宁远
			DELFEESendMessageNY dsm = new DELFEESendMessageNY(feeMessage);//
			rb = dsm.getMessage(url, logname);
		}else if ("27".equals(userType)) {//大石桥
			DELFEESendMessageDSQ dsm = new DELFEESendMessageDSQ(feeMessage);//
			rb = dsm.getMessage(url, logname);
		}else if ("31".equals(userType)) {//留坝县
			DELFEESendMessageLBX dsm = new DELFEESendMessageLBX(feeMessage);
			rb = dsm.getMessage(url, logname);
		}else if ("33".equals(userType)) {//山西心血管
			DELFEESendMessageXXG dsm = new DELFEESendMessageXXG(feeMessage);
			rb = dsm.getMessage(url, logname);
		}
		else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
