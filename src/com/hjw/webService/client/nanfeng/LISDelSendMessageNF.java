package com.hjw.webService.client.nanfeng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.nanfeng.gencode.WSInterface;
import com.hjw.webService.client.nanfeng.gencode.WSInterfaceLocator;
import com.hjw.webService.client.nanfeng.gencode.WSInterfacePortType;
import com.hjw.webService.client.nanfeng.util.GetGUID;
import com.hjw.webService.client.nanfeng.util.NanfenHL7;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONSerializer;

public class LISDelSendMessageNF {

	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	
	private static String certificate = "xuMp+IMHvlYA3s34dkHQEWi8PL7CgdC9";

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}

	public LISDelSendMessageNF(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}
	
	public ResultLisBody getMessage(String url,String logname){
		
		ResultLisBody rb = new ResultLisBody();
		String reqTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String jsonString = JSONSerializer.toJSON(lismessage).toString();
		TranLogTxt.liswriteEror_to_txt(logname, reqTime + "  req:" + jsonString);

		try {
			List<LisComponents> components = lismessage.getComponents();
			ControlActLisProcess con = new ControlActLisProcess();
			List<ApplyNOBean> appList = new ArrayList<ApplyNOBean>();
			StringBuilder msgHeader = new StringBuilder();
			
			String guid = GetGUID.getGUID();
			
			msgHeader.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			msgHeader.append("<root>");
			msgHeader.append("<serverName>CancelClinicApply</serverName>");
			msgHeader.append("<format>HL7v2</format>");
			msgHeader.append("<callOperator></callOperator>");
			msgHeader.append("<certificate>"+certificate+"</certificate>");
			msgHeader.append("<msgNo>"+guid+"</msgNo>");
			msgHeader.append("<sendTime>"+reqTime+"</sendTime>");
			msgHeader.append("<sendCount>0</sendCount>");
			msgHeader.append("</root>");
			
			boolean flags = true;
			String reqStr = "";
			for (int i = 0; i < components.size(); i++) {
				LisComponents liscoms = new LisComponents();
				liscoms = components.get(i);

				reqStr = NanfenHL7.OrmO01hl7_cancelLis(lismessage, liscoms, i, "CA", logname, guid);// NW
				TranLogTxt.liswriteEror_to_txt(logname, "  req_msgHeader:" + msgHeader.toString());
				TranLogTxt.liswriteEror_to_txt(logname, "  req_msgBody:" + reqStr);
				String res = "";
				try {
					WSInterface wsInterface = new WSInterfaceLocator(url);
					WSInterfacePortType wsInterfacePortType = wsInterface.getWSInterfaceHttpSoap11Endpoint();
					res = wsInterfacePortType.callInterface(msgHeader.toString(), reqStr);
//					res = "MSH|^~&|PEIS||RIS||2019101015||ACK|f791da16-fe1b-4bd6-9df0-177d2624f178|P|2.4\r\n" + 
//							"MSA|AE|f791da16-fe1b-4bd6-9df0-177d2624f178|error:error:安全凭证校验失败【凭证号为空!】";
					TranLogTxt.liswriteEror_to_txt(logname, "  res:" + res);
					String res_code = NanfenHL7.getResCode(res);
					if (res_code.contains("error")) {
						String[] resL = res_code.split(":");
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(resL[resL.length - 1]);
						flags = false;
						break;
					}
				} catch (Exception e) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("error:请求服务SendClinicApply失败！");
					flags = false;
				}
	
				ApplyNOBean aob = new ApplyNOBean();
				aob.setApplyNO(liscoms.getReq_no());
				appList.add(aob);
			}
			if (flags) {
				con.setList(appList);
				rb.setControlActProcess(con);
				rb.getResultHeader().setTypeCode("AA");
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("操作错误");
		}
		
	
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getResultHeader().getTypeCode()+rb.getResultHeader().getText());
		return rb;
	}
	
	
}
