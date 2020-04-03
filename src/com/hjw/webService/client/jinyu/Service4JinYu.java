package com.hjw.webService.client.jinyu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.interfaces.util.LockCenterDateUtil;
import com.hjw.interfaces.util.PacsPictureDecodeBase64Util;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.bdyx.bean.pacs.res.ExamItem;
import com.hjw.webService.client.bdyx.bean.pacs.res.PacsResultBDYX;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.webService.client.jinyu.body.Data;
import com.hjw.webService.client.jinyu.body.ResultBodyJY;
import com.hjw.webService.client.jinyu.lisbean.LisItems;
import com.hjw.webService.client.jinyu.lisbean.PatientInfoJY;
import com.hjw.webService.client.jinyu.lisbean.ResLisMessageJY;
import com.hjw.webService.client.jinyu.lisbean.RetLisChargeItemJY;
import com.hjw.webService.client.jinyu.lisbean.RetLisCustomeJY;
import com.hjw.webService.client.jinyu.lisbean.RetLisItemJY;
import com.hjw.webService.client.jinyu.lisbean.SubItems;
import com.hjw.webService.service.pacsbean.RetPacsItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminationItemDTO;
import com.hjw.wst.DTO.LisSendDTO;
import com.hjw.wst.DTO.PacsSendDTO;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.service.ChargingItemService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

public class Service4JinYu extends ServletEndpointSupport {
	private ConfigService configService;
	private ChargingItemService chargingItemService;
	private JdbcQueryManager jdbcQueryManager;

	protected void onInit() {
		this.configService = (ConfigService) getWebApplicationContext().getBean("configService");
		this.chargingItemService = (ChargingItemService) getWebApplicationContext().getBean("chargingItemService");
		this.jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
	}

	public String acceptMessageTest(String xmlmessage) {
		return "返回ok----参数为：" + xmlmessage;
	}

	/**
	 * 
	 * @Title: lisgetMessageJY //GetLisRequest
	 * @Description: 金域通过“条码号”【参数：医院条码】从该接口获取LIS的病人信息和医嘱信息
	 * @param: String 医院条码
	 * @return @return: String 成功或失败标志以XML形式返回。成功：<response><code>0</code><message>标本信息XML</message></response>
	 */
	public String GetLisRequest(String HospSampleID) {
		String logname = "GetLisRequest";
		ResultBodyJY frb = new ResultBodyJY();
		ResHdMeessage rm = LockCenterDateUtil.SetEaminatioinCenterDate(2020, Calendar.DECEMBER, 1, logname);
		if("AE".equals(rm.getStatus())) {
			frb.setCode(1);
			frb.setMessage(rm.getMessage());
			TranLogTxt.liswriteEror_to_txt(logname, rm.getMessage());
			String reqxml = JaxbUtil.convertToXmlWithCDATA(frb, "^message");
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + reqxml);
			return reqxml;
		}
		
		TranLogTxt.liswriteEror_to_txt(logname, "HospSampleID:" + HospSampleID);
		String orderid = "";
		try {
			if (StringUtil.isEmpty(HospSampleID)) {
				frb.setCode(1);
				frb.setMessage("lis传入参数为空 HospSampleID:" + HospSampleID);
				TranLogTxt.liswriteEror_to_txt(logname, "lis传入参数为空 HospSampleID:" + HospSampleID);
			} else {
				boolean isLIS = true;
				ExamInfoUserDTO ei = this.configService.getExamInfoForBarcode(HospSampleID);
				if ((ei == null) || (ei.getId() <= 0)) {
					isLIS = false;
					ei = this.configService.getExamInfoForReqNum(HospSampleID);
				}
				
				if ((ei == null) || (ei.getId() <= 0)) {
					frb.setCode(1);
					frb.setMessage("lis信息 根据条码号查无此人，入库错误。HospSampleID:" + HospSampleID);
					TranLogTxt.liswriteEror_to_txt(logname, "lis信息 根据条码号查无此人，入库错误。HospSampleID:" + HospSampleID);
				} else if ("Z".equals(ei.getStatus())) {
					frb.setCode(1);
					frb.setMessage("lis信息 此人已经总检，入库错误。HospSampleID:" + HospSampleID);
					TranLogTxt.liswriteEror_to_txt(logname, "lis信息 此人已经总检，入库错误。HospSampleID:" + HospSampleID);
				} else {
					String doctorname = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
					PatientInfoJY patientInfo = new PatientInfoJY();
					patientInfo.setLis_Barcode(HospSampleID);
					patientInfo.setPat_id(ei.getExam_num());
					patientInfo.setPat_no(ei.getExam_num());
					patientInfo.setPat_name(ei.getUser_name());
//					patientInfo.setPat_bedno();
//					patientInfo.setBlood_time();
					patientInfo.setPat_sex(ei.getSex());
					patientInfo.setPat_birthday(ei.getBirthday());
					patientInfo.setPat_age(ei.getAge());
					patientInfo.setPat_ageunit("岁");
					patientInfo.setPat_tel(ei.getPhone());
					patientInfo.setDept_name("体检中心");
					patientInfo.setDoctor_name(doctorname);
//					patientInfo.setDoctor_tel();
					patientInfo.setClinical_diag("体检");
//					patientInfo.setSamp_name();
//					patientInfo.setPat_from();
					
					ArrayList<LisItems> ci_list = new ArrayList<>();
					if(isLIS) {
						StringBuilder sb = new StringBuilder();
						sb.append(" select c.Id as chargingitemId,c.item_name,c.exam_num,d.remark,d.data_name,ec.amount,c.his_num,c.item_code,ec.item_amount,"
								+ " hd.dept_code,hd.dept_name,ec.id,ec.pay_status,s.sample_barcode ");
						sb.append("from examinfo_charging_item ec,sample_exam_detail s,examResult_chargingItem er,sample_demo sd,data_dictionary d,charging_item  c"
								+" left join his_dict_dept hd on c.perform_dept = hd.dept_code"
								+" where ec.examinfo_id = s.exam_info_id and ec.charge_item_id = c.id and s.sample_id = sd.id"
								+" and sd.demo_category = d.id "
//							+ "and c.sam_demo_id = sd.id "
+ "and ec.isActive = 'Y'  and ec.exam_status in ('N','D') " //and c.interface_flag = '2' 金域的维护的都是0
+" and s.id = er.exam_id and er.charging_id = ec.charge_item_id and er.result_type = 'sample'"
+" and ec.change_item != 'C' and ec.pay_status != 'M' and s.sample_barcode ='"+ HospSampleID+"'");
						
						boolean isBufa = false;
						if(!isBufa){
							sb.append(" and ec.is_application = 'N'");
						}
						
						if("Y".equals(this.configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim())){
							sb.append(" and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and s.is_binding = 1))");
						}
						
						TranLogTxt.liswriteEror_to_txt(logname, "sql : " + sb.toString());
						
						List<LisSendDTO> sendList = this.jdbcQueryManager.getList(sb.toString(), LisSendDTO.class);
						String noPayItems = "";
						if("N".equals(ei.getIs_after_pay())){
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
							TranLogTxt.liswriteEror_to_txt(logname, "error-没有需要发送申请的检验科室项目!");
							return "error-没有需要发送申请的检验科室项目!";
						}else if(sendList.size() == 0 && (!"".equals(noPayItems))){
							TranLogTxt.liswriteEror_to_txt(logname, "error-项目("+noPayItems+")未付费,未发送申请!");
							return "error-项目("+noPayItems+")未付费,未发送申请!";
						}
						for(LisSendDTO lissend : sendList){
							LisItems lisItems = new LisItems();
							lisItems.setLis_item_code(lissend.getItem_code());
							lisItems.setLis_item_name(lissend.getItem_name());
							
							ArrayList<SubItems> ei_list = new ArrayList<>();
							List<ExaminationItemDTO> examinationItems = this.chargingItemService.getChargingItemExamItem(lissend.getChargingitemId());
							for(ExaminationItemDTO examinationItem : examinationItems) {
								SubItems subItems = new SubItems();
								subItems.setLis_subitem_code(examinationItem.getItem_num());
								subItems.setLis_subitem_name(examinationItem.getItem_name());
								ei_list.add(subItems);
							}
							lisItems.setSubItems(ei_list);
							ci_list.add(lisItems);
						}
					} else {
						StringBuilder sb = new StringBuilder();
						sb.append("select p.pacs_req_code,c.view_num,c.item_code,ec.item_amount,hd.dept_code,hd.dept_name,c.his_num,c.item_name,"
					    		+ "dd.dep_num,ec.id,ec.pay_status,ec.amount,c.hiscodeClass,c.id as itemId  from examinfo_charging_item ec,"
					    		+ "pacs_summary p,pacs_detail d,department_dep dd,charging_item c left join his_dict_dept hd "
					    		+ "on c.perform_dept = hd.dept_code where ec.charge_item_id = c.id and p.id = d.summary_id and "
					    		+ "d.chargingItem_num = c.item_code and c.dep_id = dd.id and ec.isActive = 'Y' and c.interface_flag = '2' "
					    		+ "and ec.change_item != 'C' and ec.pay_status != 'M' and ec.exam_status in ('N','D') "
					    		+ "and ec.examinfo_id = " + ei.getId() + " and p.pacs_req_code = '" + HospSampleID + "'");
					    boolean isBufa = false;
					    if (!isBufa) {
					      sb.append(" and ec.is_application = 'N'");
					    }
					    TranLogTxt.liswriteEror_to_txt(logname, "根据体检id和体检编号查询需要发送影像申请的项目:"+sb.toString());
					    List<PacsSendDTO> pacsSendList = this.jdbcQueryManager.getList(sb.toString(), PacsSendDTO.class);
					    String noPayItems = "";
					    if ("N".equals(ei.getIs_after_pay())) {
					    	String IS_HIS_PACS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_PACS_CHECK").getConfig_value().trim();
					      for (int i = 0; i < pacsSendList.size(); i++) {
					        if (("N".equals(((PacsSendDTO)pacsSendList.get(i)).getPay_status())) && ("Y".equals(IS_HIS_PACS_CHECK)))
					        {
					          if (i == pacsSendList.size() - 1) {
					            noPayItems = noPayItems + ((PacsSendDTO)pacsSendList.get(i)).getItem_name();
					          } else {
					            noPayItems = noPayItems + ((PacsSendDTO)pacsSendList.get(i)).getItem_name() + ",";
					          }
					          pacsSendList.remove(pacsSendList.get(i));
					          i--;
					        }
					      }
					    }
					    if ((pacsSendList.size() == 0) && ("".equals(noPayItems))) {
					      return "error-没有需要发送申请的影像科室项目!";
					    }
				    
					    if ((pacsSendList.size() == 0) && (!"".equals(noPayItems))) {
					      return "error-项目(" + noPayItems + ")未付费,未发送申请!";
					    }
						for(PacsSendDTO pacssend : pacsSendList){
							LisItems lisItems = new LisItems();
							lisItems.setLis_item_code(pacssend.getItem_code());
							lisItems.setLis_item_name(pacssend.getItem_name());
							
							ArrayList<SubItems> ei_list = new ArrayList<>();
							List<ExaminationItemDTO> examinationItems = this.chargingItemService.getChargingItemExamItem(pacssend.getItemId());
							for(ExaminationItemDTO examinationItem : examinationItems) {
								SubItems subItems = new SubItems();
								subItems.setLis_subitem_code(examinationItem.getItem_num());
								subItems.setLis_subitem_name(examinationItem.getItem_name());
								ei_list.add(subItems);
							}
							lisItems.setSubItems(ei_list);
							ci_list.add(lisItems);
						}
					}
					
					patientInfo.setLisItems(ci_list);
					
					TranLogTxt.liswriteEror_to_txt(logname, "patientInfo");
					Data data = new Data();
					data.setPatientInfo(patientInfo);
					String message = JaxbUtil.convertToXml(data, true);
//					System.out.println(message);
					TranLogTxt.liswriteEror_to_txt(logname, "message:"+message);
					frb.setCode(0);
					frb.setMessage(message);
				}
			}
		} catch (Throwable ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			frb.setCode(1);
			frb.setMessage("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String reqxml = JaxbUtil.convertToXmlWithCDATA(frb, "^message");
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + orderid + ":" + reqxml);
		return reqxml;
	}
	

	/**
	 * 
	 * @Title: accetpMessageLisJY   /AffirmRequest
	 * @Description: 金域通过AffirmRequest接口发送确认获取成功的信息
	 * @param: @param hospSampleID	医院条码号
	 * @param: @param itemCode	需确认的项目代码，可以为空。多个项目代码时，以逗号分隔。
	 * @return: String	成功或失败标志。成功或失败标志以XML形式返回
	 * @throws
	 */
	public String AffirmRequest(String HospSampleID, String ItemCode) {
		String logname = "AffirmRequest";
		TranLogTxt.liswriteEror_to_txt(logname, "HospSampleID:" + HospSampleID);
		TranLogTxt.liswriteEror_to_txt(logname, "ItemCode:" + ItemCode);
		ResultBodyJY frb = new ResultBodyJY();
		String orderid = "";
		try {
			if(StringUtil.isEmpty(HospSampleID)){// || StringUtil.isEmpty(ItemCode)
				frb.setCode(1);
				frb.setMessage("lis传入参数错误 HospSampleID:" + HospSampleID+",ItemCode:" + ItemCode);
				TranLogTxt.liswriteEror_to_txt(logname, "lis传入参数错误 HospSampleID:" + HospSampleID+",ItemCode:" + ItemCode);
			}else{
				boolean isLIS = true;
				ExamInfoUserDTO ei = this.configService.getExamInfoForBarcode(HospSampleID);
				if ((ei == null) || (ei.getId() <= 0)) {
					isLIS = false;
				}
				if(isLIS) {
					this.configService.setExamInfoChargeItemLisStatus(HospSampleID, ItemCode, "C","H");
				} else {
					this.configService.setExamInfoChargeItemPacsStatus(HospSampleID, ItemCode, "C");
				}
			}
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			frb.setCode(1);
			frb.setMessage("lis信息入库错误");
		}
		String reqxml = JaxbUtil.convertToXml(frb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + orderid + ":" + reqxml);
		return reqxml;
	}
	
	/**
	 * 
	 * @Title: accetpMessageLisJY //UploadLisRepData
	 * @Description: 金域通过UploadLisRepData接口回传项目检验结果和报告单
	 * @param: ResultXML为Base64编码后的XML字符串(编码格式为UTF-8)
	 * @return @return: String	成功或失败标志。成功或失败标志以XML形式返回
	 */
	public String UploadLisRepData(String ResultXML) {
		String logname = "UploadLisRepData";
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + ResultXML);
		ResultBodyJY frb = new ResultBodyJY();
		String orderid = "";
		try {
			ResLisMessageJY rpm = new ResLisMessageJY(ResultXML, true);
			RetLisCustomeJY rc = rpm.rc;
			if ((rc == null) || (rc.getRtlischarge_map() == null) || (rc.getRtlischarge_map().size() <= 0)) {
				TranLogTxt.liswriteEror_to_txt(logname, "lis信息解析为空");
				frb.setCode(1);
				frb.setMessage("lis信息解析为空");
			} else {
				String exam_num = rc.getExam_num();
				if ((exam_num != null) && (exam_num.trim().length() > 0)) {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					ei = this.configService.getExamInfoForNum(exam_num);
					if ((ei == null) || (ei.getId() <= 0)) {
						frb.setCode(1);
						frb.setMessage("lis信息 根据条码号查无此人，入库错误:" + exam_num);
						TranLogTxt.liswriteEror_to_txt(logname, "lis信息 根据条码号查无此人，入库错误:" + exam_num);
					} else if ("Z".equals(ei.getStatus())) {
						frb.setCode(1);
						frb.setMessage("lis信息 此人已经总检，入库错误:" + exam_num);
						TranLogTxt.liswriteEror_to_txt(logname, "lis信息 此人已经总检，入库错误:" + exam_num);
					} else {
						boolean isLIS = true;
						ExamInfoUserDTO eiLis = this.configService.getExamInfoForBarcode(rc.getSample_barcode());
						if ((eiLis == null) || (eiLis.getId() <= 0)) {
							isLIS = false;
						}
						if(isLIS) {
							ProcListResult plr = new ProcListResult();
							plr.setBar_code(rc.getSample_barcode());
							plr.setExam_num(exam_num);
							plr.setExam_doctor(rc.getDoctor_name_bg());
							plr.setExam_date(rc.getDoctor_time_bg());
							plr.setApprover(rc.getDoctor_name_sh());
							plr.setApprove_date(rc.getDoctor_time_sh());
							boolean flagss = true;
							for (RetLisChargeItemJY rlcharg : rc.getRtlischarge_map().values()) {
								plr.setLis_item_code(rlcharg.getChargingItem_num());
								for (RetLisItemJY rlis : rlcharg.getListRetLisItem()) {
									plr.setLis_rep_item_code(rlis.getItem_id());
									plr.setExam_result(rlis.getValues());
									plr.setRef_value(rlis.getValue_fw());
									plr.setItem_unit(rlis.getValues_dw());
									plr.setRef_indicator(rlis.getValue_ycbz());
									
									//金域：
//								↑表示超过了参考值上限
//								↓表示超过了参考值下限
//								↑↑表示该结果是危急值，需要进行危急值报告
//								↓↓表示该结果是危急值，需要进行危急值报告
//								●表示超过了临床可报告范围
//								△ 表示结果需要双录上
//								▲表示双录结果不一致
//								■表示病人资料未录入或未审核
//								○表示结果超出了线性范围
//								☆本报告取代x月X日报告单号为XXx的报告单(终止报告单时自动加在建议与解释处)
//								!表示有人修改了病人资料
//								数据浅黄色: 表示终止了报告单，数据返至实验中
									
									// P阳性、Q弱阳性、E错误，由LIS判断，仪器接口不用管
									// M就是正常
									// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
									if ("↑".equals(plr.getRef_indicator())) {
										plr.setRef_indicator("1");
									} else if ("↑↑".equals(plr.getRef_indicator())) {
										plr.setRef_indicator("4");
									} else if ("↓".equals(plr.getRef_indicator())) {
										plr.setRef_indicator("2");
									} else if ("↓↓".equals(plr.getRef_indicator())) {
										plr.setRef_indicator("4");
									} else {
										plr.setRef_indicator("0");
									}
									
									int resflag = doproc_Lis_result_JinYu(plr);
									if (resflag != 0) {
										flagss = false;
									}
								}
							}
							if (flagss) {
								frb.setCode(0);
								frb.setMessage("lis信息 入库成功");
								TranLogTxt.liswriteEror_to_txt(logname, "lis信息 入库成功");
							} else {
								frb.setCode(1);
								frb.setMessage("lis信息 入库错误");
								TranLogTxt.liswriteEror_to_txt(logname, "lis信息 入库错误");
							}
						} else {
							PacsResult pacsResult = new PacsResult();
					    	try {
					    		String reportDate = rc.getDoctor_time_bg();
					    		if(StringUtil.isEmpty(reportDate) || reportDate.length()!=19) {
					    			pacsResult.setNote("reportDate不符合规范");
					    			pacsResult.setStatus(3);
					    		}
					    		
					    		String req_code = rc.getSample_barcode();
					    		pacsResult.setTil_id(logname);
					    		pacsResult.setExam_num(exam_num);
					    		pacsResult.setReq_no(req_code);
					    		pacsResult.setPacs_checkno(req_code);
					    		pacsResult.setReg_doc(rc.getDoctor_name_bg());
					    		pacsResult.setCheck_doc(rc.getDoctor_name_bg());
					    		pacsResult.setCheck_date(rc.getDoctor_time_bg());
					    		pacsResult.setReport_doc(rc.getDoctor_name_bg());
					    		pacsResult.setReport_date(rc.getDoctor_time_bg());
					    		pacsResult.setAudit_doc(rc.getDoctor_name_sh());
					    		pacsResult.setAudit_date(rc.getDoctor_time_sh());
					    		
					    		RetPacsItem retPacsItem = rc.getList().get(0);
					    		pacsResult.setClinic_symptom(retPacsItem.getChargingItem_jl());
					    		pacsResult.setClinic_diagnose(retPacsItem.getChargingItem_ms());
//					    		pacsResult.setStudy_body_part();
//					    		pacsResult.setStudy_type();
//					    		pacsResult.setItem_name(examItem.getItemName());
//					    		pacsResult.setPacs_item_code(examItem.getItemCode());
					    		
					    		if (retPacsItem.getBase64_bg().length() > 10) {
					    			String picname = "";
					    			//
					    			TranLogTxt.liswriteEror_to_txt(logname, "req_code:"+req_code);
					    			String datetime = reportDate.substring(0,10).replaceAll("-", "");
					    			picname = PacsPictureDecodeBase64Util.decodeBase64JPG(exam_num, req_code, datetime, retPacsItem.getBase64_bg());
					    			pacsResult.setIs_tran_image(0);
					    			pacsResult.setReport_img_path(picname);
					    		} else {
					    			pacsResult.setIs_tran_image(0);
					    			pacsResult.setNote("docImageContent不符合规范");
					    			pacsResult.setStatus(3);
					    		}
					    	} catch (Exception e) {
								e.printStackTrace();
								frb.setCode(1);
								frb.setMessage("Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
								TranLogTxt.liswriteEror_to_txt(logname, "Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
								pacsResult.setStatus(3);
								pacsResult.setNote("Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
							}
					    	boolean succ = this.configService.insert_pacs_result(pacsResult);
					    	if (succ) {
					    		frb.setCode(0);
								frb.setMessage("pacs信息 入库成功");
								TranLogTxt.liswriteEror_to_txt(logname, "pacs信息 入库成功");
					    	} else {
					    		frb.setCode(1);
								frb.setMessage("pacs信息 入库错误");
								TranLogTxt.liswriteEror_to_txt(logname, "pacs信息 入库错误");
					    	}
						}
					}
				} else {
					frb.setCode(1);
					frb.setMessage("lis信息 条码号为空");
					TranLogTxt.liswriteEror_to_txt(logname, "lis信息 条码号为空");
				}
			}
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			frb.setCode(1);
			frb.setMessage("lis信息 xml解析错误");
		}
		String reqxml = JaxbUtil.convertToXml(frb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + orderid + ":" + reqxml);
		return reqxml;
	}

	private int doproc_Lis_result_JinYu(ProcListResult pr)  throws ServiceException {
		// 信息入正式库
		int typeid = 1;
		Connection conn = null;
		try {

			/*
			 * @bar_code varchar(20), --条码号
			 * 
			 * @exam_num String, --体检Num
			 * 
			 * @lis_item_code varchar(20), --收费项目代码
			 * 
			 * @lis_rep_item_code varchar(20), --检查项目编码
			 * 
			 * @exam_doctor varchar(20), --检验（报告）医生
			 * 
			 * @exam_date varchar(20), --检验日期
			 * 
			 * @exam_result varchar(100), --检验结果
			 * 
			 * @ref_value varchar(400), --参考范围
			 * 
			 * @item_unit varchar(80), --单位
			 * 
			 * @ref_indicator varchar(4), --高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
			 * 
			 * @approver varchar(20), --审核医生
			 * 
			 * @approve_date varchar(20), --审核日期
			 * 
			 * @error int out --返回值(0:成功) proc_Lis_result()
			 */

			conn = this.jdbcQueryManager.getConnection();
			CallableStatement c = conn.prepareCall("{call proc_Lis_result_JY(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			c.setString(1, pr.getBar_code());
			c.setString(2, pr.getExam_num());
			c.setString(3, pr.getLis_item_code());
			c.setString(4, pr.getLis_rep_item_code());
			c.setString(5, pr.getExam_doctor());
			c.setString(6, pr.getExam_date());
			c.setString(7, pr.getExam_result());
			c.setString(8, pr.getRef_value());
			c.setString(9, pr.getItem_unit());
			c.setString(10, pr.getRef_indicator());
			c.setString(11, pr.getApprover());
			c.setString(12, pr.getApprove_date());
			c.registerOutParameter(13, java.sql.Types.INTEGER);

			// 执行存储过程啊闪光灯
			c.execute();
			// 得到存储过程的输出参数值
			typeid = c.getInt(13);
			
			StringBuffer sb= new StringBuffer();
			sb.append("  declare @res int ");
			sb.append("exec proc_Lis_result_JY '"+pr.getBar_code()+"','"+pr.getExam_num()+"','"
			+pr.getLis_item_code()+"','"+pr.getLis_rep_item_code()+"','"+pr.getExam_doctor()+"','"
			+pr.getExam_date()+"','"+pr.getExam_result()+"','"+pr.getRef_value()+"','"
			+pr.getItem_unit()+"','"+pr.getRef_indicator()+"','"+pr.getApprover()+"','"
			+pr.getApprove_date()+"', @res output  ");
			if(typeid!=0){
				TranLogTxt.liswriteEror_to_txt("lisproc_error", "res:" + sb.toString());
			}else{
				TranLogTxt.liswriteEror_to_txt("lisproc_success", "res:" + sb.toString());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return typeid;
	}
}
