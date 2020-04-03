package com.hjw.webService.client.zhangyeshi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.wst.DTO.UserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;


public class DELFEESendMessageZYS {
	
	private DelFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	
	public DELFEESendMessageZYS(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}
	
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	//操作员ID
	private static final String userId = "2";  
	
	/**
	 * 张掖市HIS作废接口
	 * @param url
	 * @param logname
	 * @return
	 */
	public FeeReqBody getMessage(String url, String logname) {
		
		
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		try {
			xml = JaxbUtil.convertToXml(this.feeMessage, true);
			TranLogTxt.liswriteEror_to_txt(logname, "传入参数==feeMessage==:" + xml);
			String danJuId = FEEResMessageZYS.getFeeId(feeMessage.getREQ_NO(), logname); 
			// http://127.0.0.1:3336/getinfo.html
			String param = "?&BillID="+danJuId+"&BillType=3&EmpId="+userId+"&InterfaceID=9";
			
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("BillID", danJuId);
			para.put("BillType", 3);
			para.put("EmpId", userId);
			para.put("InterfaceID", 9);
			
			String result = HttpUtil.doPost(url, para, "UTF-8");
			
			TranLogTxt.liswriteEror_to_txt(logname,"url路径打印=== :" +url+"参数param=== :" +param);
			
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();
				//解析XML
				ZYSResolveXML ssl = new ZYSResolveXML();
				Map<String, String> mapXML = ssl.resolveXML(result, true);
				
				TranLogTxt.liswriteEror_to_txt(logname,"发送数据后返回结果result====:" +result+"===="+mapXML.get("resultCode"));
				
				if ("1".equals(mapXML.get("resultCode"))) {
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("退费申请成功！");
				}else{
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("不识别的错误代码"+mapXML.get("resultMsg"));
				}
			} else {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("返回结果错误："+result);
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("撤销收费过程调用异常："+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		
		TranLogTxt.liswriteEror_to_txt(logname,"===getResultHeader().getTypeCode()==="+rb.getResultHeader().getTypeCode());
		
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "响应结果==:" + feeMessage.getREQ_NO() + ":" + xml);
		
		return rb;
	}
	

}
