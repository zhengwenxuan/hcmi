package com.hjw.webService.client.dbgj;

import org.hsqldb.lib.StringUtil;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.DAHCustomerBean;
import com.hjw.webService.client.body.DAHResBody;
import com.hjw.webService.client.hzty.RenyuanCx;
import com.hjw.webService.client.hzty.RenyuanZc;
import com.hjw.webService.client.hzty.Bean.RENYUANMXCXIN;
import com.hjw.webService.client.hzty.Bean.RENYUANZCIN;
import com.hjw.webService.client.hzty.Bean.RyCxResultBody;
import com.hjw.webService.client.hzty.Bean.RyZcResultBody;

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
public class DAHSendMessageZJEY {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public DAHResBody getMessage(String url,DAHCustomerBean eu,String dah,String logname) {
		DAHResBody rb = new DAHResBody();
		try {
			RENYUANMXCXIN ry = new RENYUANMXCXIN();
			ry.setCHAXUNHM(eu.getId_num());
			RenyuanCx ryc = new RenyuanCx(ry);		
			
			JSONObject json = JSONObject.fromObject(ry);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:-zjtyRycxLog" + str);
			
			RyCxResultBody rzb = ryc.getMessage(url, "zjtyRycxLog");
			json = JSONObject.fromObject(rzb);// 将java对象转换为json对象
			str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "req:-zjtyRycxLog" + str);
			if ((rzb != null) && ("AA".equals(rzb.getResultHeader().getTypeCode()))
					&& (rzb.getControlActProcess().getBINGRENMX().getBINGRENXX().size() > 0)) {				
				rb.setRescode("ok");
				rb.setIdnumber(rzb.getControlActProcess().getBINGRENMX().getBINGRENXX().get(0).getBINGANH());
			} else {
				try {				
					RENYUANZCIN ryzc = new RENYUANZCIN();
					ryzc.setZHENGJIANHM(eu.getId_num());
					ryzc.setXINGMING(eu.getName());
					ryzc.setXINGBIE(eu.getSex());
					ryzc.setBINGANH(dah);
					String brid=eu.getBrid();
					if (!StringUtil.isEmpty(brid) && (brid.trim().length() > 10)) {
						brid = brid.substring(0, 10);
					}
					ryzc.setCHUSHENGRQ(brid);
					RenyuanZc ryz = new RenyuanZc(ryzc);
					json = JSONObject.fromObject(ryzc);// 将java对象转换为json对象
					str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "req:-zjtyRycxLog" + str);
					RyZcResultBody ryzcres = ryz.getMessage(url, "zjtyRycxLog");
					json = JSONObject.fromObject(ryzcres);// 将java对象转换为json对象
					str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "req:-zjtyRycxLog" + str);
					if ((ryzcres != null) && ("AA".equals(ryzcres.getResultHeader().getTypeCode()))
							&& (!StringUtil.isEmpty(ryzcres.getControlActProcess().getBINGANH()))) {
						rb.setRescode("ok");
						rb.setIdnumber(ryzcres.getControlActProcess().getBINGANH());
					} else {
						rb.setRescode("error");
						rb.setIdnumber("");
					}
				} catch (Exception ex) {
					rb.setRescode("error");
					rb.setIdnumber("");
					com.hjw.interfaces.util.StringUtil.formatException(ex);
				}
			}
		} catch (Exception ex) {
			com.hjw.interfaces.util.StringUtil.formatException(ex);
			rb.setRescode("error");
			rb.setIdnumber("");
		}
		return rb;
	}

}
