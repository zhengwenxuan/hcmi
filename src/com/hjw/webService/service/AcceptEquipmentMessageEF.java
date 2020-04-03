package com.hjw.webService.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.Base64;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.service.bean.ResultEquipmentEF;
import com.hjw.webService.service.bean.ResultEquipmentHeader;
import com.hjw.wst.DTO.DepExamResultDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.PacsSendDTO;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.persistence.JdbcQueryManager;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 辅助设备--肺功能接口   东北国际医院
 * @author dq
 *
 */
public class AcceptEquipmentMessageEF extends ServletEndpointSupport{

	private CommService commService;
	private JdbcQueryManager jdbcQueryManager;
	private static String dep_num = "EF";

	protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
		jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
	}
	
	/**
	 * 
	 * @param CallType  
	 * @param xmlMessage
	 * @return
	 */
	public String callEF(String CallType, String xmlMessage) {
		TranLogTxt.liswriteEror_to_txt("EquipmentMessage", "req:传入信息:" + xmlMessage);
		if ("1".equals(CallType)) { //查询体检人员信息
			return getExamInfo(xmlMessage);
		} else if ("2".equals(CallType)) {//检查结果回传保存信息
			return saveResult(xmlMessage);
		} else {
			ResultEquipmentHeader ResultHeader = new ResultEquipmentHeader();
			ResultHeader.setType_code("AE");
			ResultHeader.setText("无效消息类型");
			String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
			return reqxml;
		}
	}
	/**
	 * 获取人员基本信息
	 * @param xmlMessage
	 * @return
	 */
	private String getExamInfo(String xmlMessage){
		ResultEquipmentHeader ResultHeader = new ResultEquipmentHeader();
		try {
			ResultEquipmentEF rt = JaxbUtil.converyToJavaBean(xmlMessage,ResultEquipmentEF.class);
			if(rt.getExam_num() == null || "".equals(rt.getExam_num())){
				ResultHeader.setType_code("AE");
				ResultHeader.setText("请传入体检编号!");
			}else{
				ExamInfoUserDTO examinfo = this.commService.getCustExamInfoForNum(rt.getExam_num());
				if(examinfo.getId() <= 0){
					ResultHeader.setType_code("AE");
					ResultHeader.setText("该体检信息不存在!");
				}else{
					ResultHeader.setType_code("AA");
					String itemsql = "select ec.pay_status from examinfo_charging_item ec,charging_item c,department_dep d "
							+ "where ec.charge_item_id = c.id and c.dep_id = d.id and d.dep_num = '"+dep_num+"' "
							+ "and ec.examinfo_id = '"+examinfo.getId()+"' and ec.isActive = 'Y' and ec.pay_status <> 'M' ";
					List<ExaminfoChargingItemDTO> itemList = this.jdbcQueryManager.getList(itemsql, ExaminfoChargingItemDTO.class);
					if(itemList.size() > 0){
						
						ExaminfoChargingItemDTO item = itemList.get(0);
						if(item.getPay_status().equals("N")&&"N".equals(examinfo.getIs_after_pay())){
							ResultHeader.setText("该体检者肺功能检查项目未缴费，不能检查");
						}else{
							ResultHeader.setText("处理成功");
							ResultHeader.setUser_name(examinfo.getUser_name());
							ResultHeader.setBirthdate(examinfo.getBirthday());
							ResultHeader.setId_num(examinfo.getId_num());
							ResultHeader.setAge(examinfo.getAge()+"");
							ResultHeader.setSex(examinfo.getSex());
							String sgsql = "select c.exam_result from common_exam_detail c where c.exam_info_id = '"+examinfo.getId()+"' and c.item_code = 'WL001'";
							List<DepExamResultDTO> sglist = this.jdbcQueryManager.getList(sgsql, DepExamResultDTO.class);
							if(sglist.size() > 0){
								ResultHeader.setHeight(sglist.get(0).getExam_result());
							}else{
								ResultHeader.setHeight("0");
							}
							String tzsql = "select c.exam_result from common_exam_detail c where c.exam_info_id = '"+examinfo.getId()+"' and c.item_code = 'WL002'";
							List<DepExamResultDTO> tzlist = this.jdbcQueryManager.getList(tzsql, DepExamResultDTO.class);
							if(tzlist.size() > 0){
								ResultHeader.setWeight(tzlist.get(0).getExam_result());
							}else{
								ResultHeader.setWeight("0");
							}
							String zssql = "select c.exam_result from common_exam_detail c where c.exam_info_id = '"+examinfo.getId()+"' and c.item_code = 'WL003'";
							List<DepExamResultDTO> zslist = this.jdbcQueryManager.getList(zssql, DepExamResultDTO.class);
							if(zslist.size() > 0){
								ResultHeader.setBmi(zslist.get(0).getExam_result());
							}else{
								ResultHeader.setBmi("0");
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ResultHeader.setType_code("AE");
			ResultHeader.setText("信息 xml解析错误"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
		TranLogTxt.liswriteEror_to_txt("EquipmentMessage", "req:返回信息:" + reqxml);
		return reqxml;
	}
	
	/**
	 * 回传保存检查结果
	 * @param xmlMessage
	 * @return
	 */
	private String saveResult(String xmlMessage){
		ResultEquipmentHeader ResultHeader = new ResultEquipmentHeader();
		try {
			ResultEquipmentEF rc = JaxbUtil.converyToJavaBean(xmlMessage,ResultEquipmentEF.class);
			if(rc.getExam_num() == null || "".equals(rc.getExam_num())){
				ResultHeader.setType_code("AE");
				ResultHeader.setText("请传入体检编号!");
			}else{
				ExamInfoUserDTO examinfo = this.commService.getExamInfoForNum(rc.getExam_num());
				if(examinfo.getId() <= 0){
					ResultHeader.setType_code("AE");
					ResultHeader.setText("该体检信息不存在!");
				}else if(examinfo.getStatus().equals("Z")){
					ResultHeader.setType_code("AE");
					ResultHeader.setText("该体检者已总检，不能写入结果!");
				} else {
					String itemsql = "select distinct p.pacs_req_code from examinfo_charging_item ec,charging_item c,department_dep d,pacs_summary p "
							+ "where ec.charge_item_id = c.id and c.dep_id = d.id and d.dep_num = '"+dep_num+"' "
							+ "and ec.examinfo_id = '"+examinfo.getId()+"' and ec.isActive = 'Y' and ec.pay_status <> 'M' "
							+" and p.examinfo_num = '"+examinfo.getExam_num()+"' and p.examinfo_sampleId = c.sam_demo_id";
					List<PacsSendDTO> pacs_summary = this.jdbcQueryManager.getList(itemsql, PacsSendDTO.class);
					String req_num = "";
					if(pacs_summary.size() > 0){
						req_num = pacs_summary.get(0).getPacs_req_code();
					}
					ProcPacsResult plr = new ProcPacsResult();
					plr.setExam_num(rc.getExam_num());
					plr.setPacs_req_code(req_num);
					plr.setCheck_date(rc.getExam_date());
					plr.setCheck_doct(rc.getExam_doc());
					plr.setAudit_date(rc.getApprove_date());
					plr.setAudit_doct(rc.getApprove_doc());
					plr.setExam_desc(rc.getExam_desc());
					plr.setExam_result(rc.getExam_result());

					if (rc.getImage().length() > 10) {
						
						String datatime = DateTimeUtil.shortFmt4(parse(rc.getExam_date()));
						String jdlj = this.commService.getDatadis("TPLJ").getName();
						String picpath = jdlj + "\\pacs_img\\" + datatime + "\\" + dep_num + "\\" + rc.getExam_num();
						String picname = "/pacs_img/" + datatime + "/" + dep_num + "/" + rc.getExam_num();

						File file = new File(picpath);
						if (!file.exists()) {
							file.mkdirs();
						}
						String filepath = req_num + ".jpg";
						this.generateImage(rc.getImage(),picpath +"\\" + filepath);
						plr.setImg_file(picname + "/" + filepath);
					}
					int falgint = commService.proc_pacs_report_dbgj(plr);
					if (falgint == 0) {
						ResultHeader.setType_code("AA");
						ResultHeader.setText("检查结果保存成功");
					} else {
						ResultHeader.setType_code("AE");
						ResultHeader.setText("检查结果入库失败");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ResultHeader.setType_code("AE");
			ResultHeader.setText("信息 xml解析错误"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
		TranLogTxt.liswriteEror_to_txt("EquipmentMessage", "req:返回信息:" + reqxml);
		return reqxml;
	}
	
	public static Date parse(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
		}
		return date;
	}
	
	/**
	 * @Description: 将base64编码字符串转换为图片
	 * @Author: 
	 * @CreateTime: 
	 * @param imgStr base64编码字符串
	 * @param path 图片路径-具体到文件
	 * @return
	*/
	private boolean generateImage(String imgStr, String path) {
		if (imgStr == null)
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// 解密
			byte[] b = decoder.decodeBuffer(imgStr);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * @Description: 根据图片地址转换为base64编码字符串
	 * @Author: 
	 * @CreateTime: 
	 * @return
	 */
	private static String getImageStr(String imgFile) {
	    InputStream inputStream = null;
	    byte[] data = null;
	    try {
	        inputStream = new FileInputStream(imgFile);
	        data = new byte[inputStream.available()];
	        inputStream.read(data);
	        inputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    // 加密
	    BASE64Encoder encoder = new BASE64Encoder();
	    return encoder.encode(data);
	}

}
