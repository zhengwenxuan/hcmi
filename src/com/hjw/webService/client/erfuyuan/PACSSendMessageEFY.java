package com.hjw.webService.client.erfuyuan;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
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
import com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceLocator;
import com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_PortType;
import com.hjw.webService.client.erfuyuan.pacsbean.ResponseEFY_PACS;
import com.hjw.webService.client.erfuyuan.pacsbean.SendEFY_PACS;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PACSSendMessageEFY{
	private PacsMessageBody lismessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSSendMessageEFY(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url,String logname,boolean debug) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
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
						String itemCode = "";
						String itemName = "";
						String exam_class = "";
						double amount = 0.0;
						for (PacsComponent pc : pcs.getPacsComponent()) {
							itemCode += (pc.getPacs_num() + ",");
							itemName += (pc.getItemName() + ",");
							amount += pc.getItemamount();
							exam_class = pc.getExam_class();
						}
						itemCode = itemCode.substring(0, itemCode.length()-1);
						itemName = itemName.substring(0, itemName.length()-1);
						SendEFY_PACS send = new SendEFY_PACS();
						send.setIHISORDER_IID(pcs.getReq_no());
						send.setCORDER_INDEX(pcs.getReq_no());
						send.setCPATWL_KEY(eu.getExam_num());
						send.setCPATIENT_ID(eu.getArch_num());
						send.setCNAME(eu.getUser_name());
						send.setCSEX(eu.getSex());
						send.setCAGE(""+eu.getAge());
						send.setDDATE_OF_BIRTH(eu.getBirthday());
						send.setCNATION(eu.getNation());
						send.setCID_NO(eu.getId_num());
						send.setCMAILING_ADDRESS(eu.getAddress());
						send.setCPHONE_NUMBER_HOME(eu.getPhone());
//						send.setCKDYQ_ID(lismessage.getDoctor().getDoctorCode());
//						send.setCKDYQ_NAME(lismessage.getDoctor().getDoctorName());
						send.setCDEPT_CODE(lismessage.getDoctor().getDept_code());
						send.setCREQ_DEPT(lismessage.getDoctor().getDept_name());
						send.setCEXAM_ITEM_NO(itemName);
						send.setCEXAM_ITEM(itemName);
						send.setCEXAM_CLASS(exam_class);
						send.setCEXAM_SCHE_DATE(DateTimeUtil.getDateTime());
						send.setCCOSTS(amount + "");
						send.setCCOSTS_FLAG(amount + "");
						send.setCreq_physician(lismessage.getDoctor().getDoctorName());
						
						String xml = JaxbUtil.convertToXml(send, true);
						TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
						
						OtherSysServiceLocator osl = new OtherSysServiceLocator(url);
						OtherSysServiceSoap_PortType oss = osl.getOtherSysServiceSoap();
						String messages = oss.sendExamApp(xml);
						TranLogTxt.liswriteEror_to_txt(logname,"res:"+messages);
						ResponseEFY_PACS response = JaxbUtil.converyToJavaBean(messages, ResponseEFY_PACS.class);
						if("0".equals(response.getResult())) {
							ApplyNOBean an = new ApplyNOBean();
							an.setApplyNO(pcs.getReq_no());	
							list.add(an);
						} else {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("WebService错误" + response.getContent());
							TranLogTxt.liswriteEror_to_txt(logname, "WebService错误" + response.getContent());
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
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
	
}
