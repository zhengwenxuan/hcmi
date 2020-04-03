package com.hjw.webService.client.nanhua;

import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeReqControlActProcess;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;
import com.hjw.webService.client.nanhua.bean.NHRequest;
import com.hjw.webService.client.nanhua.bean.NHResponse;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServices;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesLocator;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.client.nanhua
     * @Description: 2.14	南华-创星 收费退费
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class UNFEESendMessageNH {

	private UnFeeMessage feeMessage;
	
	public UNFEESendMessageNH(UnFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url,String logName) {
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		try {
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logName,"req:"+feeMessage.getRCPT_NO()+":"+xml);
			try {
				TranLogTxt.liswriteEror_to_txt(logName, "url:" + url);
				GWI_TJJKServices gwiServices = new GWI_TJJKServicesLocator(url);
				GWI_TJJKServicesSoap_PortType gwi = gwiServices.getGWI_TJJKServicesSoap();
				
				List<ReqNo> okList = new ArrayList<ReqNo>();
				for (String reqNo : this.feeMessage.getREQ_NOS().getREQ_NO()) {
					String exam_num=this.feeMessage.getEXAM_NUM();
					NHRequest request = new NHRequest();
					request.getData().setTjno(exam_num);
					request.getData().setHisno(reqNo);
					String strRequest = JaxbUtil.convertToXml(request, true);
					TranLogTxt.liswriteEror_to_txt(logName, "传入参数:" + strRequest);
					String messages = gwi.getDoBlance(strRequest);
					TranLogTxt.liswriteEror_to_txt(logName,"res:收费查询服务GetDoBlance返回结果"+messages);
					NHResponse response = JaxbUtil.converyToJavaBean(messages, NHResponse.class);
					if("0".equals(response.getHead().getResultCode())) {
						if(1 == response.getInfo().get(0).getHissfzt()) {//1已收费，0未收费
							NHRequest tf_request = new NHRequest();
							tf_request.getData().setHisfph(response.getInfo().get(0).getHisfph());
							tf_request.getData().setTfsqr(response.getInfo().get(0).getName());
							tf_request.getData().setTfsqrq(DateTimeUtil.getDateTime());
							String tf_strRequest = JaxbUtil.convertToXml(tf_request, true);
							TranLogTxt.liswriteEror_to_txt(logName, "传入参数:" + tf_strRequest);
							
							String tf_messages = gwi.returnDoBlance(tf_strRequest);
							TranLogTxt.liswriteEror_to_txt(logName,"res:退费申请服务ReturnDoBlance返回结果:"+tf_messages);
							try {
								NHResponse tf_response = JaxbUtil.converyToJavaBean(tf_messages, NHResponse.class);
								if("0".equals(tf_response.getHead().getResultCode())) {
									ReqNo req = new ReqNo();
									req.setREQ_NO(reqNo);
									okList.add(req);
								} else {
									rb.getResultHeader().setTypeCode("AE");
									rb.getResultHeader().setText(response.getHead().getErrorMsg());
								}
							} catch (Exception ex) {
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText("费用信息解析返回值错误");
							}
						} else if("2".equals(response.getHead().getResultCode())) {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("订单已退费，不能退费");
						} else {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("订单未收费，不能退费");
						}
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(response.getHead().getErrorMsg());
					}
				}
				if(!okList.isEmpty()) {
					FeeReqControlActProcess controlActProcess = new FeeReqControlActProcess();
					controlActProcess.setList(okList);
					rb.setControlActProcess(controlActProcess);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("退费已经成功");
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
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + feeMessage.getRCPT_NO() + ":" + xml);
		return rb;
	}

}
