package com.hjw.webService.client.zhangyeshi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeReqControlActProcess;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;
import com.synjones.framework.persistence.JdbcQueryManager;

public class UNFEESendMessageZYS {
	
	private UnFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public UNFEESendMessageZYS(UnFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}
	
	
	//操作员ID
	private static final String userId = "2";  
	
	
	/**
	 * 已缴费 退费申请
	 * @param url
	 * @param logname
	 * @return
	 */
	public FeeReqBody getMessage(String url, String logname) {
		
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		
		try {
			xml = JaxbUtil.convertToXml(this.feeMessage, true);
			
			TranLogTxt.liswriteEror_to_txt(logname, "EXAM_NUM===:" + feeMessage.getEXAM_NUM() + "入参数===:" + xml);
			
			List<ReqNo> okList = new ArrayList<ReqNo>();
			
			for (String reqNo : this.feeMessage.getREQ_NOS().getREQ_NO()) {
				
				TranLogTxt.liswriteEror_to_txt(logname, "传入的单据号==="+reqNo);
				
				String danJuId = FEEResMessageZYS.getFeeId(reqNo, logname); 
				
				//http://127.0.0.1:3336/getinfo.html
				//url  = url + "?&BillID="+danJuId+"&BillType=2&EmpId="+userId+"&InterfaceID=9";
				String param = "?&BillID="+danJuId+"&BillType=3&EmpId="+userId+"&InterfaceID=9";
				
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("BillID", danJuId);
				para.put("BillType", 3);
				para.put("EmpId", userId);
				para.put("InterfaceID", 9);
				
				TranLogTxt.liswriteEror_to_txt(logname,"url路径打印=== :" +url+"参数param=== :" +param);
				
				String result = HttpUtil.doPost(url, para, "UTF-8");
				
				TranLogTxt.liswriteEror_to_txt(logname,"发送数据后返回结果result====:" +result);
				
				if ((result != null) && (result.trim().length() > 0)) {
					result = result.trim();
					//解析XML
					ZYSResolveXML ssl = new ZYSResolveXML();
					Map<String, String> mapXML = ssl.resolveXML(result, true);
					
					TranLogTxt.liswriteEror_to_txt(logname,"发送数据后返回结果resultCode====:" +mapXML.get("resultCode"));
					
					if ("1".equals(mapXML.get("resultCode"))) {
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("已缴费退费申请成功！");
						ReqNo req = new ReqNo();
						req.setREQ_NO(reqNo);
						okList.add(req);
					}else if("0".equals(mapXML.get("resultCode"))) { 
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(mapXML.get("resultMsg"));
					}else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("不识别的错误代码"+mapXML.get("resultMsg"));
					}
				} else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("返回结果错误："+result);
				}
				
			}
			//退费申请
			FeeReqControlActProcess controlActProcess = new FeeReqControlActProcess();
			controlActProcess.setList(okList);
			rb.setControlActProcess(controlActProcess);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("已缴费退费过程调用异常："+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		
		TranLogTxt.liswriteEror_to_txt(logname,"===getResultHeader().getTypeCode()==="+rb.getResultHeader().getTypeCode()+"===resultMsg==="+rb.getResultHeader().getText());
		
		return rb;
	}
	

}
