package com.hjw.webService.client.donghua;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.RowSet;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.donghua.bean.PEOrd.OrdInfo_Request;
import com.hjw.webService.client.donghua.bean.PEOrd.OrdInfo_Response;
import com.hjw.webService.client.donghua.bean.PEOrd.Request_PEOrd;
import com.hjw.webService.client.donghua.bean.PEOrd.Response_PEOrd;
import com.hjw.webService.client.donghua.gencode.WebPEService;
import com.hjw.webService.client.donghua.gencode.WebPEServiceLocator;
import com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_PortType;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.BatchService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

public class PACSSendMessageDH{
	private PacsMessageBody lismessage;
	private static ConfigService configService;
	private static BatchService batchService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		batchService = (BatchService) wac.getBean("batchService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSSendMessageDH(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url,String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname,"lismessage:"+new Gson().toJson(lismessage, PacsMessageBody.class));
			TranLogTxt.liswriteEror_to_txt(logname,"url:"+url);
			String exam_num = lismessage.getCustom().getExam_num();
			if (StringUtil.isEmpty(exam_num)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {
				ExamInfoUserDTO eu=this.configService.getExamInfoForNum(lismessage.getCustom().getExam_num());
				if ((eu==null)||(eu.getId() <= 0)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					ControlActPacsProcess ca = new ControlActPacsProcess();
					List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
					
					for (PacsComponents pcs : lismessage.getComponents()) {
						//2.2. 体检发送医嘱信息
						TranLogTxt.liswriteEror_to_txt(logname,"2.2. 体检发送医嘱信息-开始====");
						Request_PEOrd request_PEOrd = new Request_PEOrd();
						request_PEOrd.setRegNo(eu.getArch_num());//体检登记号
						request_PEOrd.setAdmNo(eu.getExam_num());//体检就诊号 标识此次体检的ID
						request_PEOrd.setRegDateTime(eu.getJoin_date());//体检登记时间 YYYY-MM-DD hh:mm:ss
//						peOrd.setSendFlag();//申请单状态
						for (PacsComponent pc : pcs.getPacsComponent()) {
							ChargingItemDTO chargingItem = batchService.findChargeItemById(pc.getItemCode());
							OrdInfo_Request ordInfo = new OrdInfo_Request();
							ordInfo.setOrdID(chargingItem.getId()+"-"+eu.getId()+"-"+DateTimeUtil.shortFmt6(new Date()));
							ordInfo.setOrdCode(chargingItem.getHis_num());
							ordInfo.setDeptCode(this.lismessage.getDoctor().getDept_code());
							String pt_num = configService.getDep_inter_num(logname, pc.getExam_class());
							ordInfo.setRecDeptCode(pt_num);
							ordInfo.setUserCode(this.lismessage.getDoctor().getDoctorCode());
							ordInfo.setFlag("RIS");
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
								ZlReqPacsItemDTO zri = new ZlReqPacsItemDTO();
								zri.setExam_info_id(eu.getId());
								zri.setCharging_item_ids(ordInfo.getOrdID().split("-")[0]);
								zri.setPacs_req_code(pcs.getReq_no());
								zri.setReq_id(ordInfo.getOrdRowID());
								configService.insert_zl_req_pacs_item(zri,logname);
							}
							
							ApplyNOBean ap = new ApplyNOBean();
							ap.setApplyNO(pcs.getReq_no());
//							ap.setLis_id(lcs.getLis_id());
//							ap.setBarcode(lcs.getReq_no());
							list.add(ap);
						} else {
							TranLogTxt.liswriteEror_to_txt(logname,"2.2. 体检发送医嘱信息-失败，对方返回："+response_PEOrd.getResultContent());
						}
					}
					if(list.size() > 0) {
						ca.setList(list);
						rb.setControlActProcess(ca);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("pacs调用成功");
					}
				}
			}
		} catch (Exception ex){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "ret:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
}
