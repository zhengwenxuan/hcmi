package com.hjw.webService.client.nanhua;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.nanhua.bean.NHRequest;
import com.hjw.webService.client.nanhua.bean.NHResponse;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServices;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesLocator;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.client.nanhua  
     * @Description:	项目减项  南华-创星
     * @author: zwx    
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessageNH {

	private DelFeeMessage feeMessage;
	
	public DELFEESendMessageNH(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url,String logname) {
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		try {
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+feeMessage.getREQ_NO()+":"+xml);
			try {
				NHRequest request = new NHRequest();
				request.getData().setHisno(feeMessage.getREQ_NO());
				String strRequest = JaxbUtil.convertToXml(request, true);
				TranLogTxt.liswriteEror_to_txt(logname, "传入参数:" + strRequest);
				
				TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
				GWI_TJJKServices gwiServices = new GWI_TJJKServicesLocator(url);
				GWI_TJJKServicesSoap_PortType gwi = gwiServices.getGWI_TJJKServicesSoap();
				String messages = gwi.cancelDoBlance(strRequest);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+feeMessage.getREQ_NO()+":"+messages);
				try {
					NHResponse response = JaxbUtil.converyToJavaBean(messages, NHResponse.class);
					if("0".equals(response.getHead().getResultCode())) {
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("减项成功！");
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(response.getHead().getErrorMsg());
					}
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("费用信息解析返回值错误");
				}
			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("费用信息调用webservice错误");
			}

		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		return rb;
	}

}
