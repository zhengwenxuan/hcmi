package com.hjw.webService.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.haijie.lisbean.ResLisMessageHJ;
import com.hjw.webService.client.haijie.lisbean.RetLisCustomeHJ;
import com.hjw.webService.client.haijie.lisbean.RetLisItemHJ;
import com.hjw.webService.service.lisbean.ResLisMessage;
import com.hjw.webService.service.lisbean.RetLisChargeItem;
import com.hjw.webService.service.lisbean.RetLisCustome;
import com.hjw.webService.service.lisbean.RetLisItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class AcceptLisMessage extends ServletEndpointSupport {
	private CommService commService;
	private static JdbcQueryManager jdbcQueryManager;

	protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
		jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
	}

	public String acceptMessageTest(String xmlmessage) {
		return "返回ok----lis结果回传" + xmlmessage;
	}

	/**
	 * 
	 * @Title: accetpMessageLis @Description: Lis
	 *         结果返回处理 @param: @return @return: String @throws
	 */
	public ResultHeader accetpMessageLis(String xmlmessage) {
		String filetpe = "reslis";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		String orderid = "";
		try {
			ResLisMessage rpm = new ResLisMessage(xmlmessage, true);
			RetLisCustome rc = new RetLisCustome();
			rc = rpm.rc;
			if ((rc == null) || (rc.getListRetLisChargeItem() == null) || (rc.getListRetLisChargeItem().size() <= 0)) {
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("lis信息解析为空");
			} else {
				String exam_num = rc.getExam_num();
				if ((exam_num != null) && (exam_num.trim().length() > 0)) {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					ei = this.commService.getExamInfoForNum(exam_num);
					if ((ei == null) || (ei.getId() <= 0)) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("lis信息 查无此人，入库错误" + exam_num);
					} else if ("Z".equals(ei.getStatus())) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("lis信息 已经总检，入账错误" + exam_num);
					} else {
						ProcListResult plr = new ProcListResult();
						plr.setExam_num(exam_num);
						plr.setBar_code(rc.getSample_barcode());
						plr.setExam_doctor(rc.getDoctor_name_bg());
						plr.setExam_date(rc.getDoctor_time_bg());
						plr.setApprover(rc.getDoctor_name_sh());
						plr.setApprove_date(rc.getDoctor_time_sh());
						boolean flagss = true;
						for (RetLisChargeItem rlcharg : rc.getListRetLisChargeItem()) {
							plr.setLis_item_code(rlcharg.getChargingItem_num());
							for (RetLisItem rlis : rlcharg.getListRetLisItem()) {
								plr.setLis_rep_item_code(rlis.getItem_id());
								plr.setExam_result(rlis.getValues());
								plr.setRef_value(rlis.getValue_fw());
								plr.setItem_unit(rlis.getValues_dw());
								plr.setRef_indicator(rlis.getValue_ycbz());

								// H偏高、HH偏高报警、L偏低、LL偏低报警、P阳性、Q弱阳性、E错误，由LIS判断，仪器接口不用管
								// M就是正常
								// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
								if ("H".equals(plr.getRef_indicator())) {
									plr.setRef_indicator("1");
								} else if ("HH".equals(plr.getRef_indicator())) {
									plr.setRef_indicator("4");
								} else if ("L".equals(plr.getRef_indicator())) {
									plr.setRef_indicator("2");
								} else if ("LL".equals(plr.getRef_indicator())){
									plr.setRef_indicator("4");
								} else if ("P".equals(plr.getRef_indicator())) {
									plr.setRef_indicator("3");
								} else if ("Q".equals(plr.getRef_indicator())) {
									plr.setRef_indicator("3");
								} else if ("E".equals(plr.getRef_indicator())) {
									plr.setRef_indicator("5");
								} else if ("M".equals(plr.getRef_indicator())) {
									plr.setRef_indicator("0");
								} else if ("N".equals(plr.getRef_indicator())) {
									plr.setRef_indicator("0");
								} else {
									plr.setRef_indicator("0");
								}

								int resflag = this.commService.doproc_Lis_result(plr);
								if (resflag != 0) {
									flagss = false;
								}
							}

						}
						if (flagss) {
							ResultHeader.setTypeCode("AA");
							ResultHeader.setText("lis信息 入库成功");
						} else {
							ResultHeader.setTypeCode("AE");
							ResultHeader.setText("lis信息 入库错误");
						}
					}
				} else {
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText("lis信息 体检编号为空");
				}
			}
		} catch (Exception ex) {
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("lis信息 xml解析错误");
		}
		String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + orderid + ":" + reqxml);
		return ResultHeader;
	}
	
	/**
	 * 
	 * @Title: accetpMessageLisHJ @Description: Lis海捷
	 *         结果返回处理 @param: @return @return: String @throws
	 */
	public ResultHeader accetpMessageLisHJ(String xmlmessage) {
		String filetpe = "reslisHJ";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		String orderid = "";
		if(xmlmessage.startsWith("xml|")) {
			xmlmessage = xmlmessage.substring(4);
		}
		try {
			ResLisMessageHJ rpm = new ResLisMessageHJ(xmlmessage, true);
			RetLisCustomeHJ rc = rpm.rc;
			if ((rc == null) || (rc.getListRetLisItem() == null) || (rc.getListRetLisItem().size() <= 0)) {
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("lis信息解析为空");
			} else {
				String sample_barcode = rc.getSample_barcode();
				if ((sample_barcode != null) && (sample_barcode.trim().length() > 0)) {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					ei = this.commService.getExamInfoForBarcode(sample_barcode);
					if ((ei == null) || (ei.getId() <= 0)) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("lis信息 根据条码号查无此人，入库错误" + sample_barcode);
					} else if ("Z".equals(ei.getStatus())) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("lis信息 此人已经总检，入库错误" + sample_barcode);
					} else {
						ProcListResult plr = new ProcListResult();
						plr.setBar_code(rc.getSample_barcode());
						plr.setExam_num(ei.getExam_num());
						plr.setExam_doctor(rc.getDoctor_name_bg());
						plr.setExam_date(rc.getDoctor_time_bg());
						plr.setApprover(rc.getDoctor_name_sh());
						plr.setApprove_date(rc.getDoctor_time_sh());
						boolean flagss = true;
						for (RetLisItemHJ rlis : rc.getListRetLisItem()) {
							plr.setLis_rep_item_code(rlis.getItem_id());
							plr.setExam_result(rlis.getValues());
							plr.setRef_value(rlis.getValue_fw());
							plr.setItem_unit(rlis.getValues_dw());
							
							// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
							if ("偏高".equals(rlis.getValue_zt())) {
								plr.setRef_indicator("1");
							} else if ("偏低".equals(rlis.getValue_zt())) {
								plr.setRef_indicator("2");
							} else if ("阳性".equals(rlis.getValue_zt())) {
								plr.setRef_indicator("3");
							} else {
								plr.setRef_indicator("0");
							}
							
							int resflag = this.commService.doproc_Lis_result(plr);
							if (resflag != 0) {
								flagss = false;
							}
						}
							
						if (flagss) {
							ResultHeader.setTypeCode("AA");
							ResultHeader.setText("lis信息 入库成功");
						} else {
							ResultHeader.setTypeCode("AE");
							ResultHeader.setText("lis信息 入库错误");
						}
					}
				} else {
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText("lis信息 条码号为空");
				}
			}
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("lis信息 xml解析错误");
		}
		String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + orderid + ":" + reqxml);
		return ResultHeader;
	}
	
}
