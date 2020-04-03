package com.hjw.webService.client.hzty;

import javax.xml.rpc.holders.StringHolder;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.hzty.Bean.RENYUANMXCXIN;
import com.hjw.webService.client.hzty.Bean.RENYUANMXCXOUT;
import com.hjw.webService.client.hzty.Bean.RyCxResultBody;
import com.hjw.webService.client.hzty.client.IHisApplay;
import com.hjw.webService.client.hzty.client.MediInfoHis;
import com.hjw.webService.client.hzty.client.MediInfoHisLocator;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 人员查询
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class RenyuanCx {
	private RENYUANMXCXIN custom=new RENYUANMXCXIN();
	
	public RenyuanCx(RENYUANMXCXIN custom){
		this.custom=custom;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public RyCxResultBody getMessage(String url,String logname) {
		RyCxResultBody rb = new RyCxResultBody();
		String tradeMsg = "";
		StringHolder tradeMsgOut=new StringHolder();;
		try {
			String tradeType="RENYUANMXCX";
			tradeMsg = JaxbUtil.convertToXml(this.custom, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+tradeMsg);
			try {
				MediInfoHis dam = new MediInfoHisLocator(url);
				IHisApplay dams = dam.getWebPoint();
				int messages = dams.runService(tradeType, tradeMsg, tradeMsgOut);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+messages+"  out:"+tradeMsgOut.value);
				try {
					RENYUANMXCXOUT rout=new RENYUANMXCXOUT();
					rout = JaxbUtil.converyToJavaBean(tradeMsgOut.value, RENYUANMXCXOUT.class);
					if(messages==0){
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("");
						rb.setControlActProcess(rout);
					}else if(messages>0){
						rb.getResultHeader().setTypeCode("AF");
						rb.getResultHeader().setText("mes:"+rout.getOUTMSG().getERRNO()+" "+ rout.getOUTMSG().getERRMSG());
						rb.setControlActProcess(rout);
					}else if(messages<0){
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("mes:"+rout.getOUTMSG().getERRNO()+" "+ rout.getOUTMSG().getERRMSG());
						rb.setControlActProcess(rout);
					}
					
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("人员信息解析返回值错误");
				}
			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("人员信息调用webservice错误");
			}

		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装人员信息 xml格式文件错误");
		}
		return rb;
	}

}
