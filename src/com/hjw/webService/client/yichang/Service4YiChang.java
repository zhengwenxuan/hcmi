package com.hjw.webService.client.yichang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.interfaces.util.PacsPictureDecodeBase64Util;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.yichang.bean.caixueguan.in.JobClearSampleIn;
import com.hjw.webService.client.yichang.bean.caixueguan.in.JobReqFormIn;
import com.hjw.webService.client.yichang.bean.caixueguan.in.JobUpdateSampleIn;
import com.hjw.webService.client.yichang.bean.caixueguan.in.JobUserIn;
import com.hjw.webService.client.yichang.bean.caixueguan.out.ItemReqForm;
import com.hjw.webService.client.yichang.bean.caixueguan.out.JobOut;
import com.hjw.webService.client.yichang.bean.caixueguan.out.LabelsReqForm;
import com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic.ItemYC;
import com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic.QueryRegularApplic;
import com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic.RequestXml;
import com.hjw.webService.client.yichang.bean.cdr.server.hipReqStatusWriteBack.StatusReq;
import com.hjw.webService.client.yichang.bean.cdr.server.registExamReportCDA.RegistExamReportCDA;
import com.hjw.webService.client.yichang.bean.cdr.server.registLabReportCommonCDA.Datasurce;
import com.hjw.webService.client.yichang.bean.cdr.server.registLabReportCommonCDA.ExamResulBean;
import com.hjw.webService.client.yichang.bean.cdr.server.ret.ReturnApply;
import com.hjw.webService.client.yichang.bean.cdr.server.ret.ReturnReport;
import com.hjw.webService.client.yichang.bean.cdr.server.ret.ReturnStatus;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.LisSendDTO;
import com.hjw.wst.DTO.PacsSendDTO;
import com.hjw.wst.DTO.UserInfoDTO;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 宜昌市第一人民医院
 * 1，贴标采血管接口服务端
 * 2，PACS/心电结果推送服务端
 * @author zwx
 */
public class Service4YiChang extends ServletEndpointSupport {
	private ConfigService configService;
	private JdbcQueryManager jdbcQueryManager;
	private JdbcPersistenceManager jdbcPersistenceManager;
	private static final String logName_CaiXue = "caixueLog";
	private static final String logName_ShengQingDan = "shenqingdanService";

	protected void onInit() {
		this.configService = (ConfigService) getWebApplicationContext().getBean("configService");
		this.jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
		this.jdbcPersistenceManager = (JdbcPersistenceManager) getWebApplicationContext().getBean("jdbcPersistenceManager");
	}

	public String test(String xmlIn_test) {
		String logName = "test";
		TranLogTxt.liswriteEror_to_txt(logName, xmlIn_test.toString());
		return "返回ok----参数为：" + xmlIn_test;
	}
	
	//3.获取检验科采血人员信息
	public String ITM2000_getSampleUser(String xmlIn_getSampleUser) {
		TranLogTxt.liswriteEror_to_txt(logName_CaiXue, "ITM2000_getSampleUser:"+xmlIn_getSampleUser);
		JobOut out = new JobOut();
		try {
			JobUserIn user = JaxbUtil.converyToJavaBean(xmlIn_getSampleUser, JobUserIn.class);
			if(user.getPassword().length() != 32) {
				out.setRet(0);
				out.setErrormsg("MD5加密后密码,长度应为32");
			} else {
				UserInfoDTO userDTO = this.configService.getUser(user.getUserid(), logName_CaiXue);
				if(userDTO.getId() == null || userDTO.getId() == 0) {
					out.setRet(0);
					out.setErrormsg("查无此用户");
				} else if(!user.getPassword().equals(userDTO.getPwd_encrypted())) {
					out.setRet(0);
					out.setErrormsg("密码错误");
				} else {
					out.setRet(1);
					out.setErrormsg("登录成功");
					out.setUserid(userDTO.getLog_Name());
					out.setUsername(userDTO.getChi_Name());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.setRet(0);
			out.setErrormsg("xml解析异常Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xmlOut = JaxbUtil.convertToXml(out, true);
		TranLogTxt.liswriteEror_to_txt(logName_CaiXue, xmlOut);
		return xmlOut;
	}
	
	//1.获取病人所有项目信息及条码信息
	public String ITM2000_getlabapply(String xmlIn_getlabapply) {
		TranLogTxt.liswriteEror_to_txt(logName_CaiXue, "ITM2000_getlabapply:"+xmlIn_getlabapply);
		JobOut out = new JobOut();
		try {
			JobReqFormIn reqForm = JaxbUtil.converyToJavaBean(xmlIn_getlabapply, JobReqFormIn.class);
			String exam_num = reqForm.getCid();
			if (StringUtil.isEmpty(exam_num)) {
				out.setRet(0);
				out.setErrormsg("传入参数为空-cid:" + exam_num);
			} else {
				ExamInfoUserDTO ei = this.configService.getExamInfoForNum(exam_num);
				if ((ei == null) || (ei.getId() <= 0)) {
					out.setRet(0);
					out.setErrormsg("根据患者卡号查无此人-cid:" + exam_num);
				} else if ("Z".equals(ei.getStatus())) {
					out.setRet(0);
					out.setErrormsg("此人已经总检-cid:" + exam_num);
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append(" select ci.Id as chargingitemId,ci.item_name,ci.exam_num,sd.remark,sd.demo_name as data_name,eci.amount,ci.item_code,eci.item_amount,"
							+ " dd.dep_name as dept_name,eci.id,eci.pay_status,sed.sample_barcode ");
					sb.append(" from examinfo_charging_item eci,sample_exam_detail sed,examResult_chargingItem er,sample_demo sd,department_dep dd,charging_item  ci"
							+ " where eci.examinfo_id = sed.exam_info_id and eci.charge_item_id = ci.id and sed.sample_id = sd.id"
//							+ " and sd.demo_category = d.id "
							+ " and ci.sam_demo_id = sd.id "
							+ " and ci.dep_id = dd.id and eci.isActive = 'Y' and eci.exam_status in ('N','D') "// and ci.interface_flag = '2'
							+ " and sed.id = er.exam_id and er.charging_id = eci.charge_item_id and er.result_type = 'sample'"
							+ " and eci.change_item != 'C' "
							+ " and eci.pay_status != 'M' "
							+ " and sed.status = 'W' "
							+ " and sed.exam_info_id ="+ ei.getId()+"");
					
//					boolean isBufa = false;
//					if(!isBufa){
//						sb.append(" and eci.is_application = 'N'");
//					}
					
					if("Y".equals(this.configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim())){
						sb.append(" and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and sed.is_binding = 1))");
					}
					
					TranLogTxt.liswriteEror_to_txt(logName_CaiXue,  "查项目sql:"+sb.toString());
					List<LisSendDTO> sendList = this.jdbcQueryManager.getList(sb.toString(), LisSendDTO.class);
					
					String noPayItems = "";
					if("N".equals(ei.getIs_after_pay())){
						String IS_HIS_LIS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_LIS_CHECK").getConfig_value();
						for(int i=0;i<sendList.size();i++){
							if(("N".equals(sendList.get(i).getPay_status()))&&("Y".equals(IS_HIS_LIS_CHECK))){
								if(i == sendList.size() - 1){
									noPayItems += sendList.get(i).getItem_name();
								}else{
									noPayItems += sendList.get(i).getItem_name()+",";
								}
								sendList.remove(i);
							}
						}
					}
					
					if(sendList.size() == 0 && "".equals(noPayItems)){
						out.setRet(0);
						out.setErrormsg("患者卡号【"+exam_num+"】没有需要发送申请的检验科室项目!");
					}else if(sendList.size() == 0 && (!"".equals(noPayItems))){
						out.setRet(0);
						out.setErrormsg("患者卡号【"+exam_num+"】项目("+noPayItems+")未付费!");
					}else{
						out.setName(ei.getUser_name());
						out.setSex(ei.getSex());
						out.setBdate(ei.getBirthday());
						out.setAge(""+ei.getAge());
						
						LabelsReqForm labels = new LabelsReqForm();
						for(LisSendDTO lissend : sendList){
							ItemReqForm itemReqForm = new ItemReqForm();
							itemReqForm.setTubeconvert(lissend.getRemark());//试管颜色
							itemReqForm.setItemname(lissend.getData_name());//项目所属组别
							itemReqForm.setNotelabel("");//采血量
							itemReqForm.setMnemotests(lissend.getItem_name());//项目名称
							itemReqForm.setItemtime(DateTimeUtil.getDateTime());//项目申请时间
							itemReqForm.setWard(lissend.getDept_name());//科室名称
							itemReqForm.setBarcodelabel(lissend.getSample_barcode());//条码号
							itemReqForm.setDestdesc("");//采血管高度 1代表100mm
							itemReqForm.setItemtype("");//报告类型
							itemReqForm.setReporttime("");//报告时间
							itemReqForm.setRemark("");//备注信息
							labels.getItem().add(itemReqForm);
						}
						out.setLabels(labels);
						out.setNumlabel(""+labels.getItem().size());
						out.setRet(1);
						out.setErrormsg("获取项目信息成功");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.setRet(0);
			out.setErrormsg("xml解析异常Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xmlOut = JaxbUtil.convertToXml(out, true);
		TranLogTxt.liswriteEror_to_txt(logName_CaiXue, xmlOut);
		return xmlOut;
	}
	
	//2.根据条码回写采样时间
	public String ITM2000_updateSampleTime(String xmlIn_updateSampleTime) {
		TranLogTxt.liswriteEror_to_txt(logName_CaiXue, "ITM2000_updateSampleTime:"+xmlIn_updateSampleTime);
		JobOut out = new JobOut();
		try {
			JobUpdateSampleIn updateSample = JaxbUtil.converyToJavaBean(xmlIn_updateSampleTime, JobUpdateSampleIn.class);
			UserInfoDTO userDTO = this.configService.getUser(updateSample.getUserID(), logName_CaiXue);
			if(userDTO.getId() == null || userDTO.getId() == 0) {
				out.setRet(0);
				out.setErrormsg("查无此用户");
			} else {
				String barcode = updateSample.getBarcode();
				if (StringUtil.isEmpty(barcode)) {
					out.setRet(0);
					out.setErrormsg("传入参数为空-barcode:" + barcode);
				} else {
					ExamInfoUserDTO ei = this.configService.getExamInfoForBarcode(barcode);
					if ((ei == null) || (ei.getId() <= 0)) {
						out.setRet(0);
						out.setErrormsg("根据条码号查无此人-barcode:" + barcode);
					} else if ("Z".equals(ei.getStatus())) {
						out.setRet(0);
						out.setErrormsg("此人已经总检-barcode:" + barcode);
					} else {
						this.configService.setExamInfoChargeItemLisStatus(barcode, "N", "Y", userDTO, updateSample.getSampletime(), logName_CaiXue);
						out.setRet(1);
						out.setErrormsg("回写采样状态成功");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.setRet(0);
			out.setErrormsg("xml解析异常Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xmlOut = JaxbUtil.convertToXml(out, true);
		TranLogTxt.liswriteEror_to_txt(logName_CaiXue, xmlOut);
		return xmlOut;
	}
	
	//4.根据条码号标本退回
	public String ITM2000_ClearSampleTime(String xmlIn_ClearSampleTime) {
		TranLogTxt.liswriteEror_to_txt(logName_CaiXue, "ITM2000_ClearSampleTime:"+xmlIn_ClearSampleTime);
		JobOut out = new JobOut();
		try {
			JobClearSampleIn clearSample = JaxbUtil.converyToJavaBean(xmlIn_ClearSampleTime, JobClearSampleIn.class);
			String barcode = clearSample.getBarcode();
			if (StringUtil.isEmpty(barcode)) {
				out.setRet(0);
				out.setErrormsg("传入参数为空-barcode:" + barcode);
			} else {
				ExamInfoUserDTO ei = this.configService.getExamInfoForBarcode(barcode);
				if ((ei == null) || (ei.getId() <= 0)) {
					out.setRet(0);
					out.setErrormsg("根据条码号查无此人-barcode:" + barcode);
				} else if ("Z".equals(ei.getStatus())) {
					out.setRet(0);
					out.setErrormsg("此人已经总检-barcode:" + barcode);
				} else {
					this.configService.setExamInfoChargeItemLisStatus(barcode, "N", "W", null, null, logName_CaiXue);
					out.setRet(1);
					out.setErrormsg("标本退回成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.setRet(0);
			out.setErrormsg("xml解析异常Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xmlOut = JaxbUtil.convertToXml(out, true);
		TranLogTxt.liswriteEror_to_txt(logName_CaiXue, xmlOut);
		return xmlOut;
	}
	
	//PACS和心电-注册检查报告
	public String registExamReportCDA(String xmlIn_registExamReportCDA) {

		//先把非体检的过滤掉，要不然日志太多了。
		if(!xmlIn_registExamReportCDA.contains("<clinicType>04</clinicType>")) {
			ReturnReport returnYC = new ReturnReport();
			returnYC.setStatus("AA");
			returnYC.setMessage("clinicType不是04，体检不接收");
			String xmlOut = JaxbUtil.convertToXml(returnYC, true);
			return xmlOut;
		}
		
		String logName = "pacsReportService";
		TranLogTxt.liswriteEror_to_txt(logName, "req:"+xmlIn_registExamReportCDA);
		ReturnReport returnYC = new ReturnReport();
		try {
			RegistExamReportCDA registExamReportCDA = JaxbUtil.converyToJavaBean(xmlIn_registExamReportCDA, RegistExamReportCDA.class);
			TranLogTxt.liswriteEror_to_txt(logName, "xml解析成功");
			try {
				PacsResult pacsResult = new PacsResult();
				if("02-09".equals(registExamReportCDA.getXmlIn_registExamReportCDA().getApplyType())) {
					//心电
					pacsResult.setTil_id(logName);
					pacsResult.setExam_num(registExamReportCDA.getXmlIn_registExamReportCDA().getDiagnosisNo());
					pacsResult.setReq_no(registExamReportCDA.getXmlIn_registExamReportCDA().getApplyNO());
					pacsResult.setPacs_checkno(registExamReportCDA.getXmlIn_registExamReportCDA().getExamSerialNo());
					pacsResult.setReg_doc(registExamReportCDA.getXmlIn_registExamReportCDA().getApplicationDoctorName());
					pacsResult.setCheck_doc(registExamReportCDA.getXmlIn_registExamReportCDA().getApplicationDoctorName());
					pacsResult.setCheck_date(registExamReportCDA.getXmlIn_registExamReportCDA().getOperationDate()+"");
					pacsResult.setReport_doc(registExamReportCDA.getXmlIn_registExamReportCDA().getReportWriterDortor());
					pacsResult.setReport_date(registExamReportCDA.getXmlIn_registExamReportCDA().getReportDate()+"");
					pacsResult.setAudit_doc(registExamReportCDA.getXmlIn_registExamReportCDA().getReportAuditDoctor());
					pacsResult.setAudit_date(registExamReportCDA.getXmlIn_registExamReportCDA().getOperationDate()+"");
					String datetime = registExamReportCDA.getXmlIn_registExamReportCDA().getOperationDate().toString().substring(0,10);
					
//					pacsResult.setClinic_symptom();
					pacsResult.setClinic_diagnose(registExamReportCDA.getXmlIn_registExamReportCDA().getCheckResult());
//					pacsResult.setStudy_body_part();
					pacsResult.setStudy_type(registExamReportCDA.getXmlIn_registExamReportCDA().getApplyDeptNo());
					pacsResult.setItem_name(registExamReportCDA.getXmlIn_registExamReportCDA().getReportContent());
					pacsResult.setPacs_item_code(registExamReportCDA.getXmlIn_registExamReportCDA().getReportNO());
					
					if (registExamReportCDA.getXmlIn_registExamReportCDA().getPdfBase64().length() > 10) {
//						/采用新型转换方式
						String picname = PacsPictureDecodeBase64Util.decodeBase64PDFByICE(pacsResult.getExam_num(), pacsResult.getReq_no(), datetime, registExamReportCDA.getXmlIn_registExamReportCDA().getPdfBase64());
						pacsResult.setIs_tran_image(0);
						pacsResult.setReport_img_path(picname);
					} else {
						pacsResult.setIs_tran_image(0);
					}
				} else {//非心电
					pacsResult.setTil_id(logName);
					pacsResult.setExam_num(registExamReportCDA.getXmlIn_registExamReportCDA().getDiagnosisNo());
					pacsResult.setReq_no(registExamReportCDA.getXmlIn_registExamReportCDA().getApplyNO());
					pacsResult.setPacs_checkno(registExamReportCDA.getXmlIn_registExamReportCDA().getExamSerialNo());
					pacsResult.setReg_doc(registExamReportCDA.getXmlIn_registExamReportCDA().getCheckDoctorSign());
					pacsResult.setCheck_doc(registExamReportCDA.getXmlIn_registExamReportCDA().getCheckDoctorSign());
					pacsResult.setCheck_date(registExamReportCDA.getXmlIn_registExamReportCDA().getOperationDate()+"");
					pacsResult.setReport_doc(registExamReportCDA.getXmlIn_registExamReportCDA().getReportWriterDortor());
					pacsResult.setReport_date(registExamReportCDA.getXmlIn_registExamReportCDA().getReportDate()+"");
					pacsResult.setAudit_doc(registExamReportCDA.getXmlIn_registExamReportCDA().getCheckDoctorSign());
					pacsResult.setAudit_date(registExamReportCDA.getXmlIn_registExamReportCDA().getOperationDate()+"");
					String datetime = registExamReportCDA.getXmlIn_registExamReportCDA().getOperationDate().toString().substring(0,10);
					
					pacsResult.setClinic_diagnose(registExamReportCDA.getXmlIn_registExamReportCDA().getDiagnosePrompt());
					pacsResult.setClinic_symptom(registExamReportCDA.getXmlIn_registExamReportCDA().getReportView());
//					pacsResult.setStudy_body_part();
					pacsResult.setStudy_type(registExamReportCDA.getXmlIn_registExamReportCDA().getApplyDeptNo());
					pacsResult.setItem_name(registExamReportCDA.getXmlIn_registExamReportCDA().getReportContent());
					pacsResult.setPacs_item_code(registExamReportCDA.getXmlIn_registExamReportCDA().getReportNO());
					
					if (registExamReportCDA.getXmlIn_registExamReportCDA().getPdfBase64().length() > 10) {
						String picname = PacsPictureDecodeBase64Util.decodeBase64PDFByYSB(pacsResult.getExam_num(), pacsResult.getReq_no(), datetime, registExamReportCDA.getXmlIn_registExamReportCDA().getPdfBase64(), 50);
						pacsResult.setIs_tran_image(0);
						pacsResult.setReport_img_path(picname);
					} else {
						pacsResult.setIs_tran_image(0);
					}
				}
				
				boolean succ = this.configService.insert_pacs_result(pacsResult);
				if (succ) {
					returnYC.setStatus("AA");
					returnYC.setMessage("交易成功");
				} else {
					returnYC.setStatus("AE");
					returnYC.setMessage("pacs 入库失败");
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logName, "Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
				returnYC.setStatus("AE");
				returnYC.setMessage("Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnYC.setStatus("AE");
			returnYC.setMessage("xml解析异常Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xmlOut = JaxbUtil.convertToXml(returnYC, true);
		TranLogTxt.liswriteEror_to_txt(logName, "res:"+xmlOut);
		return xmlOut;
	}
	
	//LIS-注册常规检验报告
	public String registLabReportCommonCDA(String xmlIn_registLabReportCommonCDA) {
		//先把非体检的过滤掉，要不然日志太多了。
		if(!xmlIn_registLabReportCommonCDA.contains("<clinicType>04</clinicType>")) {
			ReturnReport returnYC = new ReturnReport();
			returnYC.setStatus("AA");
			returnYC.setMessage("clinicType不是04，体检不接收");
			String xmlOut = JaxbUtil.convertToXml(returnYC, true);
			return xmlOut;
		}
				
		String logName = "lisReportService";
		TranLogTxt.liswriteEror_to_txt(logName, "req:"+xmlIn_registLabReportCommonCDA);
		ReturnReport returnYC = new ReturnReport();
		try {
			Datasurce datasurce = JaxbUtil.converyToJavaBean(xmlIn_registLabReportCommonCDA, Datasurce.class);
			TranLogTxt.liswriteEror_to_txt(logName, "xml解析成功");
			try {
				LisResult lisResult = new LisResult();
				lisResult.setTil_id(logName);
				lisResult.setExam_num(datasurce.getXmlIn_registLabReportCommonCDA().getDiagnosisNo());
				lisResult.setSample_barcode(datasurce.getXmlIn_registLabReportCommonCDA().getExamineApplyNo());
				lisResult.setDoctor(datasurce.getXmlIn_registLabReportCommonCDA().getInspectorName());
				lisResult.setExam_date(datasurce.getXmlIn_registLabReportCommonCDA().getExamineReportDate());
				lisResult.setSh_doctor(datasurce.getXmlIn_registLabReportCommonCDA().getReviewerName());
				boolean flagss = true;
				int seq_code = 0;
				for (ExamResulBean examResult : datasurce.getXmlIn_registLabReportCommonCDA().getExamResultList()) {
					lisResult.setLis_item_code(examResult.getOrderItemCode());
					lisResult.setLis_item_name(examResult.getOrderItemName());
					lisResult.setSeq_code(seq_code++);
					lisResult.setReport_item_code(examResult.getDetailExamProjectCode());
					lisResult.setReport_item_name(examResult.getDetailExamProjectName());
					lisResult.setItem_result(examResult.getResult());
					lisResult.setRef(examResult.getReferenceRange());
					lisResult.setItem_unit(examResult.getUnit());
					lisResult.setFlag(examResult.getAbnormalFlag());
					
					if(null != lisResult.getRef() && lisResult.getRef().contains("&lt;")) {
						lisResult.setRef(lisResult.getRef().replaceAll("&lt;", "<"));
					}
					if(null != lisResult.getRef() && lisResult.getRef().contains("&gt;")) {
						lisResult.setRef(lisResult.getRef().replaceAll("&gt;", ">"));
					}
					
					//宜昌		↑ 		↓
					//火箭蛙		H-高		L-低		N-正常			P-阳性			C-危急	HH-偏高报警	LL-偏低报警
					if ("↑".equals(lisResult.getFlag())) {
						lisResult.setFlag("H");
					} else if ("↓".equals(lisResult.getFlag())) {
						lisResult.setFlag("L");
					}
					
//					boolean succ = this.configService.insert_lis_result(lisResult);
					boolean succ = this.configService.insert_lis_result_new(lisResult);
					if (!succ) {
						flagss = false;
					}
				}
				if (flagss) {
					returnYC.setStatus("AA");
					returnYC.setMessage("交易成功");
				} else {
					returnYC.setStatus("AE");
					returnYC.setMessage("pacs 入库失败");
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logName, "Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
				returnYC.setStatus("AE");
				returnYC.setMessage("Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnYC.setStatus("AE");
			returnYC.setMessage("xml解析异常Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xmlOut = JaxbUtil.convertToXml(returnYC, true);
		TranLogTxt.liswriteEror_to_txt(logName, "res:"+xmlOut);
		return xmlOut;
	}
	
	/*//LIS-注册微生物检验报告
	public String registLabReportMicroOrganism(String xmlIn_registLabReportMicroOrganism) {
		String logName = "registLabReportMicroOrganism";
		TranLogTxt.liswriteEror_to_txt(logName, xmlIn_registLabReportMicroOrganism);
		ReturnYC returnYC = new ReturnYC();
		
		//好像是体检中心没做微生物的项目，此接口先暂不实现
		
		String xmlOut = JaxbUtil.convertToXml(returnYC, true);
		TranLogTxt.liswriteEror_to_txt(logName, xmlOut);
		return xmlOut;
	}*/
	
	/**
	 * 常规申请单查询
	 * @param requestXml pacs： <visitNo>T197180068</visitNo><visitType>04</visitType><applyType>02</applyType>
	 * @param requestXml lis：<applyCode>4366200204</applyCode><visitType>04</visitType><applyType>05</applyType>
	 * @return
	 */
	public String hipQueryRegularApplic (String requestXml) {
		String logName = logName_ShengQingDan;
		TranLogTxt.liswriteEror_to_txt(logName, "req:"+requestXml);
		requestXml = "<requestXml>"+requestXml+"</requestXml>";//平台不给传根节点，自己拼上再解析
		ReturnApply returnYC = new ReturnApply();
		try {
			RequestXml request = JaxbUtil.converyToJavaBean(requestXml, RequestXml.class);
			if(!"04".equals(request.getVisitType())) {//体检
				returnYC.setStatus("AA");
				returnYC.setMessage("visitType不是04，体检不处理");
				String xmlOut = JaxbUtil.convertToXmlWithOutHead(returnYC, true);
				xmlOut = xmlOut.replace("<return>", "").replace("</return>", "");
				return xmlOut;
			}
			if("05".equals(request.getApplyType())) {//检验申请单
				String barCode = request.getApplyCode();
				returnYC = getLisApply(barCode, logName);
			} else if(request.getApplyType().startsWith("02")) {//检查申请单
				String exam_num = request.getVisitNo();
//				if("02-05".equals()) {//CT检查
//					
//				} else if("02-03".equals(request.getApplyType())) {//DR 放射
//					
//				} else if("02-06".equals(request.getApplyType())) {//MR 磁共振
//					
//				} else if("02-01".equals(request.getApplyType())) {//超声
//					
//				} else if("02-02".equals(request.getApplyType())) {//内镜
//					
//				} else {
//					returnYC.setStatus("AE");
//					returnYC.setMessage("不识别的申请单类型:applyType-"+request.getApplyType());
//				}
				returnYC = getPacsApply(exam_num,request.getApplyType(), logName);
			} else {
				returnYC.setStatus("AE");
				returnYC.setMessage("不识别的申请单类型:applyType-"+request.getApplyType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnYC.setStatus("AE");
			returnYC.setMessage("xml解析异常Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xmlOut = JaxbUtil.convertToXmlWithOutHead(returnYC, true);
//		String xmlOut = JaxbUtil.convertToXmlWithCDATA(returnYC, "^applyContent");
		xmlOut = xmlOut.replace("<return>", "").replace("</return>", "");
		TranLogTxt.liswriteEror_to_txt(logName, "res:"+xmlOut);
		return xmlOut;
	}

	private ReturnApply getLisApply(String barCode, String logName) {
		ReturnApply returnYC = new ReturnApply();
		ExamInfoUserDTO eu = configService.getExamInfoForBarcode(barCode);
		String sexcode = "0";
		if("男".equals(eu.getSex()) || "男性".equals(eu.getSex())) {
			sexcode = "1";
		} else if("女".equals(eu.getSex()) || "女性".equals(eu.getSex())) {
			sexcode = "2";
		}
		if ((eu == null) || (eu.getId() <= 0)) {
			returnYC.setStatus("AE");
			returnYC.setMessage("根据条码号【"+barCode+"】查无此人。");
		} else if ("Z".equals(eu.getStatus())) {
			returnYC.setStatus("AE");
			returnYC.setMessage("条码号【"+barCode+"】的体检者已经总检。");
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(" select ci.Id as chargingitemId,ci.item_name,ci.exam_num,d.remark,d.data_name,eci.amount,ci.item_code,eci.item_amount,"
					+ " dd.dep_name as dept_name,eci.id,eci.pay_status,sed.sample_barcode ");
			sb.append(" from examinfo_charging_item eci,sample_exam_detail sed,examResult_chargingItem er,sample_demo sd,data_dictionary d,department_dep dd,charging_item  ci"
					+ " where eci.examinfo_id = sed.exam_info_id and eci.charge_item_id = ci.id and sed.sample_id = sd.id"
					+ " and sd.demo_category = d.id "
					+ " and ci.sam_demo_id = sd.id "
					+ " and ci.dep_id = dd.id and eci.isActive = 'Y' and eci.exam_status in ('N','D') "// and ci.interface_flag = '2'去掉此过滤，因为申请单可能已经上传到CDR
					+ " and sed.id = er.exam_id and er.charging_id = eci.charge_item_id and er.result_type = 'sample'"
					+ " and eci.change_item != 'C' and eci.pay_status != 'M' and sed.sample_barcode ='"+ barCode+"'");
			
//			boolean isBufa = false;
//			if(!isBufa){
//				sb.append(" and eci.is_application = 'N'");
//			}
			
			if("Y".equals(this.configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim())){
				sb.append(" and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and sed.is_binding = 1))");
			}
			
			TranLogTxt.liswriteEror_to_txt(logName, "查项目sql:"+sb.toString());
			List<LisSendDTO> sendList = this.jdbcQueryManager.getList(sb.toString(), LisSendDTO.class);
			
			String noPayItems = "";
			if("N".equals(eu.getIs_after_pay())){
				String IS_HIS_LIS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_LIS_CHECK").getConfig_value().trim();
				for(int i=0;i<sendList.size();i++){
					if(("N".equals(sendList.get(i).getPay_status()))&&("Y".equals(IS_HIS_LIS_CHECK))){
						if(i == sendList.size() - 1){
							noPayItems += sendList.get(i).getItem_name();
						}else{
							noPayItems += sendList.get(i).getItem_name()+",";
						}
						sendList.remove(i);
					}
				}
			}
			
			if(sendList.size() == 0 && "".equals(noPayItems)){
				returnYC.setStatus("AE");
				returnYC.setMessage("条码号【"+barCode+"】没有需要发送申请的检验科室项目!");
			}else if(sendList.size() == 0 && (!"".equals(noPayItems))){
				returnYC.setStatus("AE");
				returnYC.setMessage("条码号【"+barCode+"】项目("+noPayItems+")未付费!");
			}else{
//				String IS_LIS_PACS_DOCTOR_ID = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value();
				Map<String, QueryRegularApplic> map = new HashMap<String, QueryRegularApplic>();
				for(LisSendDTO lissend : sendList){
					QueryRegularApplic applic = (QueryRegularApplic) map.get(lissend.getSample_barcode());
					if (applic == null) {
						applic = new QueryRegularApplic();
						applic.setApplyCode(lissend.getSample_barcode());
						applic.setVisitNo(eu.getExam_num());
						applic.setVisitSqNo(eu.getArch_num());
						applic.setPatientName(eu.getUser_name());
						applic.setSexName(eu.getSex());
						applic.setIdCard(eu.getId_num());
						applic.setSexCode(sexcode);
						applic.setBirthDate(eu.getBirthday());
						applic.setAge(eu.getAge()+"");
						if(StringUtil.isEmpty(eu.getJoin_date())) {
							applic.setApplicationStartTime(DateTimeUtil.getDateTime());
						} else {
							applic.setApplicationStartTime(eu.getJoin_date());
						}
						applic.setApplicationDoctorName("吴之余");
						applic.setApplicationDeptName("体检中心");
						applic.setExecutDeptName(lissend.getDept_name());
					}
					
					ItemYC item = new ItemYC();
					item.setItemCode(lissend.getItem_code());
					item.setItemName(lissend.getItem_name());
					item.setTestItemCode(lissend.getExam_num());
					item.setSpecimenTypeName(lissend.getData_name());
					item.setItemPrice(lissend.getAmount()+"");
					item.setExaminationSiteName(lissend.getItem_name());
					applic.getItemList().getItem().add(item);
					map.put(lissend.getSample_barcode(), applic);
				}
				returnYC.setStatus("AA");
				returnYC.setMessage("SUCCESS");
				returnYC.getQueryRegularApplicList().setQueryRegularApplic(new ArrayList(map.values()));
			}
		}
		return returnYC;
	}
	
	private ReturnApply getPacsApply(String exam_num, String pt_num, String logName) {
		ReturnApply returnYC = new ReturnApply();
		ExamInfoUserDTO eu = configService.getExamInfoForNum(exam_num);
		String sexcode = "0";
		if("男".equals(eu.getSex()) || "男性".equals(eu.getSex())) {
			sexcode = "1";
		} else if("女".equals(eu.getSex()) || "女性".equals(eu.getSex())) {
			sexcode = "2";
		}
		if(eu == null || eu.getId()==0) {
			returnYC.setStatus("AE");
			returnYC.setMessage("error-体检编号【"+exam_num+"】不存在");
		} else if ("Z".equals(eu.getStatus())) {
			returnYC.setStatus("AE");
			returnYC.setMessage("体检编号【"+exam_num+"】的体检者已经总检。");
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("select p.pacs_req_code,c.view_num,c.item_code,ec.item_amount,dd.dep_name as dept_name,c.item_name,dd.dep_num,ec.id,ec.pay_status,ec.amount,c.id as itemId "
					+" from examinfo_charging_item ec,pacs_summary p,pacs_detail d,department_dep dd,charging_item c"
					+" where ec.charge_item_id = c.id and p.id = d.summary_id and d.chargingItem_num = c.item_code" 
					+" and c.dep_id = dd.id and ec.isActive = 'Y' and ec.change_item != 'C'" // and c.interface_flag = '2'
					+" and ec.pay_status != 'M' and ec.exam_status in ('N','D')"
					+" and ec.examinfo_id = "+eu.getId()+" and p.examinfo_num = '"+exam_num+"'" );
			if(!"02".equals(pt_num)) {//如果是02则返回所有pacs项目，不关联科室，如果是02-01这种，则只返回超声
				sb.append(" and dd.pt_num ='"+pt_num+"' ");
			}
			sb.append(" and p.send_status = 0 ");//增加pacs状态回传接口之后，查询接口需增加此条件
			
//			boolean isBufa = false;
//			if(!isBufa){
//				sb.append(" and ec.is_application = 'N'");
//			}
			
			TranLogTxt.liswriteEror_to_txt(logName, "查项目sql:"+sb.toString());
			List<PacsSendDTO> pacsSendList = this.jdbcQueryManager.getList(sb.toString(), PacsSendDTO.class);
			
			String noPayItems = "";
			if("N".equals(eu.getIs_after_pay())){
				for(int i=0;i<pacsSendList.size();i++){
					String IS_HIS_PACS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_PACS_CHECK").getConfig_value().trim();
					if(("N".equals(pacsSendList.get(i).getPay_status()))&&("Y".equals(IS_HIS_PACS_CHECK))){
						if(i == pacsSendList.size() - 1){
							noPayItems += pacsSendList.get(i).getItem_name();
						}else{
							noPayItems += pacsSendList.get(i).getItem_name()+",";
						}
						pacsSendList.remove(i);
					}
				}
			}
			
			if(pacsSendList.size() == 0 && "".equals(noPayItems)){
				returnYC.setStatus("AE");
				returnYC.setMessage("体检号【"+eu.getExam_num()+"】没有需要发送申请的影像科室项目!");
			}else if(pacsSendList.size() == 0 && (!"".equals(noPayItems))){
				returnYC.setStatus("AE");
				returnYC.setMessage("体检号【"+eu.getExam_num()+"】项目("+noPayItems+")未付费!");
			}else{
				String IS_LIS_PACS_DOCTOR_NAME = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value();
				Map<String, QueryRegularApplic> map = new HashMap<String, QueryRegularApplic>();
				for(PacsSendDTO pacsSend : pacsSendList){
					QueryRegularApplic applic = (QueryRegularApplic)map.get(pacsSend.getPacs_req_code());
					if (applic == null) {
						applic = new QueryRegularApplic();
						applic.setApplyCode(pacsSend.getPacs_req_code());
						applic.setVisitNo(eu.getExam_num());
						applic.setVisitSqNo(eu.getArch_num());
						applic.setPatientName(eu.getUser_name());
						applic.setSexName(eu.getSex());
						applic.setIdCard(eu.getId_num());
						applic.setSexCode(sexcode);
						applic.setBirthDate(eu.getBirthday());
						applic.setAge(eu.getAge()+"");
						if(StringUtil.isEmpty(eu.getJoin_date())) {
							applic.setApplicationStartTime(DateTimeUtil.getDateTime());
						} else {
							applic.setApplicationStartTime(eu.getJoin_date());
						}
						applic.setApplicationDoctorName("吴之余");
						applic.setApplicationDeptName("体检中心");
						applic.setDeviceTypeName(pacsSend.getDept_name());
						applic.setExecutDeptName(pacsSend.getDept_name());
					}
				      
					ItemYC item = new ItemYC();
					item.setItemCode(pacsSend.getItem_code());
					item.setItemName(pacsSend.getItem_name());
					item.setTestItemCode(pacsSend.getView_num());
					item.setSpecimenTypeName(pacsSend.getDep_num());
					item.setItemPrice(pacsSend.getAmount()+"");
					item.setExaminationSiteName(pacsSend.getItem_name());
					applic.getItemList().getItem().add(item);
					map.put(pacsSend.getPacs_req_code(), applic);
				}
				returnYC.setStatus("AA");
				returnYC.setMessage("SUCCESS");
				returnYC.getQueryRegularApplicList().setQueryRegularApplic(new ArrayList(map.values()));
			}
		}
		return returnYC;
	}
	
	/**
	 * pacs申请单状态回写
	 * @param requestXml pacs申请单确认： <applyNo>申请单号</applyNo><medicalType>04</medicalType><confirmCancelStatus>01</confirmCancelStatus>
	 * @param requestXml pacs申请单取消：<applyNo>申请单号</applyNo><medicalType>04</medicalType><confirmCancelStatus>02</confirmCancelStatus>
	 * @return
	 */
	public String hipReqStatusWriteBack (String statusReq) {
		String logName = logName_ShengQingDan;
		TranLogTxt.liswriteEror_to_txt(logName, "req:"+statusReq);
		statusReq = "<statusReq>"+statusReq+"</statusReq>";//平台不给传根节点，自己拼上再解析
		ReturnStatus returnYC = new ReturnStatus();
		try {
			StatusReq request = JaxbUtil.converyToJavaBean(statusReq, StatusReq.class);
			if(!"04".equals(request.getMedicalType())) {//体检
				returnYC.setMARK(-1);
				returnYC.setMSG("medicalType不是04，体检不处理");
				String xmlOut = JaxbUtil.convertToXmlWithOutHead(returnYC, true);
				return xmlOut;
			}
			if("01".equals(request.getConfirmCancelStatus())) {//01.确认
				String updatesql = "update pacs_summary set send_status = 1 where pacs_req_code = '"+request.getApplyNo()+"'";
				TranLogTxt.liswriteEror_to_txt(logName, "update-pacs_summary-sql:"+updatesql);
				int count = jdbcPersistenceManager.execSql(updatesql);
				if(count != 0) {
					returnYC.setMARK(1);
					returnYC.setMSG("PACS申请单回写确认状态成功");
				} else {
					returnYC.setMARK(1);
					returnYC.setMSG("不识别的applyNo:"+request.getApplyNo());
				}
			} else if("02".equals(request.getConfirmCancelStatus())) {//02.取消
				String updatesql = "update pacs_summary set send_status = 0 where pacs_req_code = '"+request.getApplyNo()+"'";
				TranLogTxt.liswriteEror_to_txt(logName, "update-pacs_summary-sql:"+updatesql);
				int count = jdbcPersistenceManager.execSql(updatesql);
				if(count != 0) {
					returnYC.setMARK(1);
					returnYC.setMSG("PACS申请单回写取消状态成功");
				} else {
					returnYC.setMARK(1);
					returnYC.setMSG("不识别的applyNo:"+request.getApplyNo());
				}
			} else {
				returnYC.setMARK(-1);
				returnYC.setMSG("不识别的confirmCancelStatus:"+request.getConfirmCancelStatus());
				TranLogTxt.liswriteEror_to_txt(logName, "不识别的confirmCancelStatus:"+request.getConfirmCancelStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnYC.setMARK(-1);
			returnYC.setMSG("xml解析异常Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xmlOut = JaxbUtil.convertToXmlWithOutHead(returnYC, true);
		TranLogTxt.liswriteEror_to_txt(logName, "res:"+xmlOut);
		return xmlOut;
	}
}
