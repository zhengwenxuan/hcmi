package com.hjw.webService.client.ShanxiXXG;

import java.net.URLEncoder;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.Bean.ReqUnNo;
import com.hjw.webService.client.ShanxiXXG.bean.DelFeeXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ReqHeadXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ReqJsonXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ResBodyXXG;
import com.hjw.webService.client.ShanxiXXG.util.MD5ForXXG;
import com.hjw.webService.client.ShanxiXXG.util.UtilForXXG;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class UNFEESendMessageXXG {
		
	private UnFeeMessage feeMessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	public UNFEESendMessageXXG(UnFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}
	static {
	   	init();
	}
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}
	
	public FeeReqBody getMessage(String url,String logname) {
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		
		try {
			xml = JaxbUtil.convertToXmlWithOutHead(this.feeMessage, true);		
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
			String[] sendL = url.split("&");
			String sendUrl = sendL[0];
			String xxg_key = sendL[1];
			String fromtype = sendL[2];
			
			ReqUnNo reqNos = this.feeMessage.getREQ_NOS();
			List<String> reqnos = reqNos.getREQ_NO();
			
			for (int i = 0; i < reqnos.size(); i++) {
				
				ReqHeadXXG reqHead = UtilForXXG.getReqHead(xxg_key,fromtype,"XXGUH");
				String head = JSONObject.fromObject(reqHead).toString();
				TranLogTxt.liswriteEror_to_txt(logname, "req head:" + head + "\r\n");
				head = MD5ForXXG.getBase64(head);
				head = head.replaceAll("\r|\n", "");
				
				DelFeeXXG reqBody = getReqBody(reqnos.get(i), logname);
				String body = JSONObject.fromObject(reqBody).toString();
				TranLogTxt.liswriteEror_to_txt(logname, "req body:" + body + "\r\n");
				body = MD5ForXXG.getBase64(body);
				body = URLEncoder.encode(body.replaceAll("\r|\n", ""), "utf-8");
				
				try {
					StringBuffer reqUrl = new StringBuffer();
					reqUrl.append(sendUrl);
					reqUrl.append("/charge/back?head=");
					reqUrl.append(head);
					reqUrl.append("&body=");
					reqUrl.append(body);
					
//					ReqJsonXXG reqJson = new ReqJsonXXG();
//					reqJson.setHead(head);
//					reqJson.setBody(body);
//					String json = JSONObject.fromObject(reqJson).toString();
					TranLogTxt.liswriteEror_to_txt(logname, "req url:" + reqUrl.toString() + "\r\n");
					String result = HttpUtil.doPost(reqUrl.toString(), "utf-8");
					TranLogTxt.liswriteEror_to_txt(logname, "res result:" + result + "\r\n");
					
					ReqJsonXXG resJson = new Gson().fromJson(result, ReqJsonXXG.class);
					String resBody = MD5ForXXG.getFromBase64(resJson.getBody());
					TranLogTxt.liswriteEror_to_txt(logname, "res body:" + resBody + "\r\n");
					ResBodyXXG resbody = new Gson().fromJson(resBody, ResBodyXXG.class);
					
					if("1".equals(resbody.getCode())) {
						ReqNo rqid = new ReqNo();
		            	rqid.setREQ_NO(reqnos.get(i));
		            	rb.getControlActProcess().getList().add(rqid);
		            	rb.getResultHeader().setTypeCode("AA");
		    			rb.getResultHeader().setText("ok-发送退费申请成功!");
					}else {
						rb.getResultHeader().setTypeCode("AE");
					    rb.getResultHeader().setText("error-发送退费申请失败1!");
					}
				}catch (Exception ex){
					rb.getResultHeader().setTypeCode("AE");
				    rb.getResultHeader().setText("error-发送退费申请失败2!");
					TranLogTxt.liswriteEror_to_txt(logname,"Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
			}
		} catch (Exception e) {
			rb.getResultHeader().setTypeCode("AE");
		    rb.getResultHeader().setText("error-操作错误!");
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getResultHeader().getText() + "\r\n");
		return rb;
	}
	
	public DelFeeXXG getReqBody(String req_no,String logname) {
		DelFeeXXG  delfee = new DelFeeXXG();
		delfee.setCharge_no(req_no);
		delfee.setApply_opera("");
		return delfee;
	}
	
}
