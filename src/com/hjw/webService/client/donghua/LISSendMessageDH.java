package com.hjw.webService.client.donghua;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.donghua.bean.LinkLabNoWithOrdRowId.Request_LinkLabNoWithOrdRowId;
import com.hjw.webService.client.donghua.bean.LinkLabNoWithOrdRowId.Response_LinkLabNoWithOrdRowId;
import com.hjw.webService.client.donghua.bean.PEOrd.OrdInfo_Request;
import com.hjw.webService.client.donghua.bean.PEOrd.OrdInfo_Response;
import com.hjw.webService.client.donghua.bean.PEOrd.Request_PEOrd;
import com.hjw.webService.client.donghua.bean.PEOrd.Response_PEOrd;
import com.hjw.webService.client.donghua.gencode.WebPEService;
import com.hjw.webService.client.donghua.gencode.WebPEServiceLocator;
import com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_PortType;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class LISSendMessageDH {
	private LisMessageBody lismessage;
	private static ConfigService configService;
	private static CustomerInfoService customerInfoService;
	private static JdbcQueryManager jdbcQueryManager;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public LISSendMessageDH(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	public ResultLisBody getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "url:"+url);
		String jsonString = JSONSerializer.toJSON(lismessage).toString();
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
		ExamInfoUserDTO eu= configService.getExamInfoForNum(this.lismessage.getCustom().getExam_num());
		ResultLisBody rb = new ResultLisBody();

		List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
		for (LisComponents lcs : this.lismessage.getComponents()) {
			try {
				//2.2. 体检发送医嘱信息
				TranLogTxt.liswriteEror_to_txt(logname,"2.2. 体检发送医嘱信息-开始====");
				Request_PEOrd request_PEOrd = new Request_PEOrd();
				request_PEOrd.setRegNo(eu.getArch_num());//体检登记号
				request_PEOrd.setAdmNo(eu.getExam_num());//体检就诊号 标识此次体检的ID
				request_PEOrd.setRegDateTime(eu.getJoin_date());//体检登记时间 YYYY-MM-DD hh:mm:ss
//				peOrd.setSendFlag();//申请单状态
				for (LisComponent lc : lcs.getItemList()) {
					long chargingItemid = Long.parseLong(lc.getChargingItemid());
					ChargingItemDTO chargingItem = customerInfoService.getChargingItemForId(chargingItemid);
					OrdInfo_Request ordInfo = new OrdInfo_Request();
					ordInfo.setOrdID(lc.getChargingItemid()+"-"+eu.getId()+"-"+DateTimeUtil.shortFmt6(new Date()));
					ordInfo.setOrdCode(chargingItem.getHis_num());
					ordInfo.setDeptCode(this.lismessage.getDoctor().getDept_code());
					ordInfo.setRecDeptCode(this.lismessage.getZxksid());
					ordInfo.setUserCode(this.lismessage.getDoctor().getDoctorCode());
					ordInfo.setFlag("LIS");
					request_PEOrd.getOrdList().getOrdInfo().add(ordInfo);
				}
				
				String requestStr = JaxbUtil.convertToXmlWithOutHead(request_PEOrd, true);
				TranLogTxt.liswriteEror_to_txt(logname,"request:"+requestStr);
				WebPEService WebPEService = new WebPEServiceLocator(url);
				WebPEServiceSoap_PortType webPEServiceSoap = WebPEService.getWebPEServiceSoap();
				String responseStr = webPEServiceSoap.savePEOrdInfo(requestStr);
				TranLogTxt.liswriteEror_to_txt(logname,"response:"+responseStr);
				Response_PEOrd response_PEOrd = JaxbUtil.converyToJavaBean(responseStr, Response_PEOrd.class);
				
				if("0".equals(response_PEOrd.getResultCode())) {
					TranLogTxt.liswriteEror_to_txt(logname,"2.2. 体检发送医嘱信息-成功====");
					for(OrdInfo_Response ordInfo : response_PEOrd.getOrdList().getOrdInfo()) {
						ZlReqItemDTO zri = new ZlReqItemDTO();
						zri.setExam_info_id(eu.getId());
						zri.setCharging_item_id(ordInfo.getOrdID().split("-")[0]);
						zri.setLis_req_code(lcs.getReq_no());
						zri.setReq_id(ordInfo.getOrdRowID());
						configService.insert_zl_req_item(zri,logname);
						
						//2.3. 关联检验号和医嘱号
						TranLogTxt.liswriteEror_to_txt(logname,"关联检验号和医嘱号-开始====");
						Request_LinkLabNoWithOrdRowId request_Link = new Request_LinkLabNoWithOrdRowId();
						request_Link.setLabNo(lcs.getReq_no());
						request_Link.setOrdRowIds(ordInfo.getOrdRowID());
						String request_LinkStr = JaxbUtil.convertToXmlWithOutHead(request_Link, true);
						TranLogTxt.liswriteEror_to_txt(logname,"request_LinkStr:"+request_LinkStr);
						WebPEService = new WebPEServiceLocator(url);
						webPEServiceSoap = WebPEService.getWebPEServiceSoap();
						String response_LinkStr = webPEServiceSoap.sendLinkLabNoWithOrdRowId(request_LinkStr);
						TranLogTxt.liswriteEror_to_txt(logname,"response_LinkStr:"+response_LinkStr);
						Response_LinkLabNoWithOrdRowId response_Link = JaxbUtil.converyToJavaBean(response_LinkStr, Response_LinkLabNoWithOrdRowId.class);
						if("0".equals(response_Link.getResultCode())) {
							TranLogTxt.liswriteEror_to_txt(logname,"关联检验号和医嘱号-成功====");
						} else {
							TranLogTxt.liswriteEror_to_txt(logname,"2.3. 关联检验号和医嘱号-失败，对方返回："+response_Link.getResultContent());
						}
					}
					ApplyNOBean ap = new ApplyNOBean();
					ap.setApplyNO(lcs.getReq_no());
//					ap.setLis_id(lcs.getLis_id());
//					ap.setBarcode(lcs.getReq_no());
					list.add(ap);
				} else {
					TranLogTxt.liswriteEror_to_txt(logname,"2.2. 体检发送医嘱信息-失败，对方返回："+response_PEOrd.getResultContent());
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}
		if(list.size() > 0) {
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(list);
		}
		
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb));
		TranLogTxt.liswriteEror_to_txt(logname, "6-----------------结束lis请求--------------------");
		return rb;
	}
}
