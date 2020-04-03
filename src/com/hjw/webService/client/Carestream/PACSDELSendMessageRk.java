package com.hjw.webService.client.Carestream;

import java.util.ArrayList;
import java.util.List;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.Carestream.Bean.RKReturnXml;
import com.hjw.webService.client.Carestream.client.CSHESBService;
import com.hjw.webService.client.Carestream.client.CSHESBServiceLocator;
import com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_PortType;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessage;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageService;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageServiceLocator;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.7	检查申请撤销信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSDELSendMessageRk{

	private PacsMessageBody lismessage;
	
	public PACSDELSendMessageRk(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url,String convalue,String logname, boolean debug) {
		ResultPacsBody rb = new ResultPacsBody();
		List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
		//String[] tokenurls= convalue.split("&");  //系统标记&院区代码&医院代码
		 JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		try {
		for (PacsComponents pcs : lismessage.getComponents()) {
			StringBuffer sb = new StringBuffer("");
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<xmlMessage>");

			sb.append("<RemoteAccNo><![CDATA[" + pcs.getReq_no() + "]]></RemoteAccNo>");
			sb.append("<HospID><![CDATA[057101]]></HospID>");
			sb.append("<Status><![CDATA[0]]></Status>");			
			sb.append("</xmlMessage>");
			//发送申请
			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+lismessage.getMessageid()+":"+sb.toString());
			try {
				CSHESBService  dam = new CSHESBServiceLocator(url);
				CSHESBServiceSoap_PortType dams = dam.getCSHESBServiceSoap();
				String messages = dams.callESB("SyncCancleOrder",sb.toString());
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+lismessage.getMessageid()+":"+messages);
					RKReturnXml rt=new RKReturnXml();					
					rt = JaxbUtil.converyToJavaBean(messages, RKReturnXml.class);
					if("1".equals(rt.getResultCode())){
						ApplyNOBean an= new ApplyNOBean();
						an.setApplyNO(pcs.getReq_no());
						anList.add(an);
					}
			
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname,"req:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}		
		ControlActPacsProcess cap = new ControlActPacsProcess();
		cap.setList(anList);
		rb.setControlActProcess(cap);
		rb.getResultHeader().setTypeCode("AA");
		} catch (Exception ex){
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装pacs xml格式文件错误");
		}
		return rb;
	}

}
