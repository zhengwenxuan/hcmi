package com.hjw.webService.client.bdyx.useCode;

import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.bdyx.bean.RequestPost;
import com.hjw.webService.client.bdyx.bean.ResponsePost;
import com.hjw.webService.client.bdyx.bean.pacs.req.ExaminationApplication;
import com.hjw.webService.client.bdyx.bean.pacs.req.ExaminationOrderDto;
import com.hjw.webService.client.bdyx.bean.pacs.req.PacsApplyReq;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class PACSSendMessageBDYX_UseCode{
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
	public PACSSendMessageBDYX_UseCode(PacsMessageBody lismessage){
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
				ExamInfoUserDTO eu=configService.getExamInfoForNum(lismessage.getCustom().getExam_num());
				if(StringUtil.isEmpty(eu.getPatient_id())) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("没有病人id，直接返回失败");
					TranLogTxt.liswriteEror_to_txt(logname, "没有病人id，直接返回失败");
					String xml = JaxbUtil.convertToXml(rb, true);
					TranLogTxt.liswriteEror_to_txt(logname, "ret:" + xml);
					return rb;
				}
				if ((eu==null)||(eu.getId() <= 0)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					ControlActPacsProcess ca = new ControlActPacsProcess();
					List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
					
					String sexcode = "0";
					if("男".equals(eu.getSex()) || "男性".equals(eu.getSex())) {
						sexcode = "1";
					} else if("女".equals(eu.getSex()) || "女性".equals(eu.getSex())) {
						sexcode = "2";
					}
					String birthDate = this.lismessage.getCustom().getBirthtime();
					if(!StringUtil.isEmpty(birthDate) && birthDate.length()==8) {
						birthDate = birthDate + "000000";
					}
					
					
					for (PacsComponents pcs : lismessage.getComponents()) {
						String dep_inter_num = "";
						String pt_exam_code = "";
						String pt_exam_name = "";
						String dep_name = "";
						try {
							String sql = "select dep_inter_num, pt_exam_code, pt_exam_name, dep_name from department_dep where dep_num = '"+pcs.getPacsComponent().get(0).getExam_class()+"'";
							TranLogTxt.liswriteEror_to_txt(logname, "sql:"+sql);
							RowSet rs = jdbcQueryManager.getRowSet(sql);
							if(rs.next()) {
								dep_inter_num = rs.getString("dep_inter_num");
								pt_exam_code = rs.getString("pt_exam_code");
								pt_exam_name = rs.getString("pt_exam_name");
								dep_name = rs.getString("dep_name");
							}
							rs.close();
						} catch (Throwable e) {
							e.printStackTrace();
							TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
						}
						
						PacsApplyReq pacsApplyReq = new PacsApplyReq();
						ExaminationApplication examinationApplication = new ExaminationApplication();
						String itemNames = "";
						String itemCodes = "";
						for (PacsComponent pc : pcs.getPacsComponent()) {
							itemNames += (pc.getItemName()+",");
							itemCodes += (pc.getPacs_num()+",");
						}
						itemNames = itemNames.substring(0, itemNames.length()-1);
						itemCodes = itemCodes.substring(0, itemCodes.length()-1);
						
						ExaminationOrderDto examinationOrder = new ExaminationOrderDto();
						examinationOrder.setOrderLid(pcs.getReq_no());
						examinationOrder.setItemName(itemNames);
						examinationOrder.setItemCode(itemCodes);
						examinationOrder.setItemClassCode(pt_exam_code);
						examinationOrder.setItemClassName(pt_exam_name);
						examinationOrder.setExamMethodName(itemNames);
						examinationApplication.getExaminationOrderDtos().add(examinationOrder);
						
						examinationApplication.setRequestNo(pcs.getReq_no());
						examinationApplication.setExecutiveDept(pcs.getPacsComponent().get(0).getServiceDeliveryLocation_code());
						examinationApplication.setExecutiveDeptName(pcs.getPacsComponent().get(0).getServiceDeliveryLocation_name());
						
						pacsApplyReq.getExaminationApplications().add(examinationApplication);
						pacsApplyReq.setPatientLid(eu.getPatient_id());
						pacsApplyReq.setVisitNo(eu.getArch_num());
						pacsApplyReq.setVisitOrdNo(eu.getExam_num());
						pacsApplyReq.setIdentityCard(eu.getId_num());
						pacsApplyReq.setPatientName(eu.getUser_name());
						pacsApplyReq.setTelNum(eu.getPhone());
						pacsApplyReq.setGenderCode(sexcode);
						pacsApplyReq.setBirthDate(birthDate);
						pacsApplyReq.setAddress(eu.getAddress());
						pacsApplyReq.setAge(""+eu.getAge());
//						pacsApplyReq.setOrderPerson(lismessage.getDoctor().getDoctorCode());
//						pacsApplyReq.setOrderPersonName(lismessage.getDoctor().getDoctorName());
						pacsApplyReq.setOrderDept(lismessage.getDoctor().getDept_code());
						pacsApplyReq.setOrderDeptName(lismessage.getDoctor().getDept_name());
						String str = new Gson().toJson(pacsApplyReq, PacsApplyReq.class);
						TranLogTxt.liswriteEror_to_txt(logname,"pacsApplyReq:"+str);
						
						RequestPost requestBDYX = new RequestPost();
						requestBDYX.setService_id("BS002");
						requestBDYX.setExec_uint_id(pcs.getPacsComponent().get(0).getServiceDeliveryLocation_code());
						requestBDYX.setOrder_exec_id(dep_inter_num);
						requestBDYX.setExtend_sub_id("");
						requestBDYX.setBody(str);
						String requestStr = JSONObject.fromObject(requestBDYX).toString();
						TranLogTxt.liswriteEror_to_txt(logname,"req:"+requestStr);
						String responseStr = HttpUtil.doPost(url,requestStr,"utf-8");
						TranLogTxt.liswriteEror_to_txt(logname,"res:"+responseStr);
						if (responseStr != null) {
							ResponsePost responseBDYX = new Gson().fromJson(responseStr, ResponsePost.class);
							if(1 == responseBDYX.getStatus()) {
								ApplyNOBean an = new ApplyNOBean();
								an.setApplyNO(pcs.getReq_no());	
								list.add(an);
							} else {
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText("PACS返回错误:" + responseBDYX.getErrMsg());
								TranLogTxt.liswriteEror_to_txt(logname, "PACS返回错误:" + responseBDYX.getErrMsg());
							}
						} else {
							rb.getResultHeader().setTypeCode("AE");
							TranLogTxt.liswriteEror_to_txt(logname, "HTTP无返回");
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
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "ret:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
}
