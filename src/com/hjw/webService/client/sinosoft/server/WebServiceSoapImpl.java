/**
 * WebServiceSoapImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.sinosoft.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.DTO.FileDTO;
import com.hjw.interfaces.FTPServer.PdfFtpHelper;
import com.hjw.interfaces.util.FileUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class WebServiceSoapImpl  extends ServletEndpointSupport implements com.hjw.webService.client.sinosoft.server.WebServiceSoap{
	
	private CommService commService;
	private static JdbcQueryManager jdbcQueryManager;

	 protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
		jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
	}

    public java.lang.String HIPMessageClient(java.lang.String message) throws java.rmi.RemoteException {
    	String filetpe = "resall";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + message);
		String msg = "<exchange><errcode>0</errcode><errmsg>操作失败</errmsg></exchange>";// 1：成功
																				// 0：失败
		String funcid="";
		try {
			InputStream is = new ByteArrayInputStream(message.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			funcid = document.selectSingleNode("/exchange/funcid").getText();// 申请单号
		}catch(Exception ex){
			TranLogTxt.liswriteEror_to_txt(filetpe, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		if("SD08.00.004.02".equals(funcid)){ //检查状态推送
			return accetpstatuMessagePacs_sinoSoft(message);
		}else if("SD08.00.003.12".equals(funcid)){ //检查报告推送
			return accetpMessagePacs_sinoSoft(message);
		}else if("SD07.00.004.04".equals(funcid)){ //检验状态推送			
			return accetpstatuMessageLis_sinoSoft(message);
		}else if("SD07.00.003.05".equals(funcid)){ //检验结果推送
			return accetpMessageLis_sinoSoft(message);
		}else{
			msg = "<exchange><errcode>0</errcode><errmsg>消息无效</errmsg></exchange>";
			return msg;
		}
    }

    
    /**
     * 
         * @Title: accetpMessageLis   
         * @Description: lis状态回传 中科软 - 江南医院
         * @param: @param xmlmessage
         * @param: @return      
         * @return: String      
         * @throws
     */
	public String accetpstatuMessageLis_sinoSoft(String xmlmessage) {
		String filetpe = "reslisstatus";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		String msg = "<exchange><errcode>0</errcode><errmsg>操作失败</errmsg></exchange>";// 1：成功
																						// 0：失败
		try {
			InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String pac_nos = document.selectSingleNode("/exchange/group/HRC00.02/row/APPLYID").getText();// 申请单号
			String statuss = document.selectSingleNode("/exchange/group/HRC00.02/row/STATUS").getText();// 10：护士打印条码；
																						// 30：检验科签收计费；
																						// 35：检验科上机；
																						// 40：检验科审核报告。

			if ((statuss == null) || (statuss.length() <= 0)) {
				msg = "<exchange><errcode>0</errcode><errmsg>状态无效</errmsg></exchange>";// 1：成功
																						// 0：失败
			} else if ((pac_nos == null) || (pac_nos.length() <= 0)) {
				msg = "<exchange><errcode>0</errcode><errmsg>申请单号无效</errmsg></exchange>";// 1：成功
																							// 0：失败
			} else {
				String exam_num = getExamInfoForLisNum(pac_nos,filetpe);
				if (StringUtil.isEmpty(exam_num)) {
					msg = "<exchange><errcode>0</errcode><errmsg>申请号查询不到体检者</errmsg></exchange>";// 1：成功
																									// 0：失败
				} else {

					String samstatus = "W";
					List<String> req_nums = new ArrayList<String>();
					req_nums.add(pac_nos);
					/*
					 * 代码 名称 简称 1 收到申请 SDSQ 2 已执行 YZX 3 初步报告 CBBG 4 确认报告 QRBG 9
					 * 其他 QT 0 取消预约 SQ 5 作废 ZF 6 已检查 YJC lis回复： 状态只２、３、４、６
					 * 不能撤销申请 2016年11月6日回复 状态只传1、4、5，且1和4 不能撤销 2016年11月7日回复
					 */
					if (("10".equals(statuss)) || ("30".equals(statuss)) || ("35".equals(statuss))) {// 核收
						statuss = "C";
						samstatus = "H";
						this.commService.setExamInfoChargeItemLisStatus(req_nums, exam_num, statuss, samstatus);
						msg = "<exchange><errcode>1</errcode><errmsg></errmsg></exchange>";// 1：成功
																							// 0：失败
					} else if ("40".equals(statuss)) {// 已检查
						statuss = "N";
						samstatus = "E";
						this.commService.setExamInfoChargeItemLisStatus(req_nums, exam_num, statuss, samstatus);
						msg = "<exchange><errcode>1</errcode><errmsg></errmsg></exchange>";// 1：成功
																							// 0：失败
					}
				}
			}
		} catch (Exception ex) {
			msg = "<exchange><errcode>0</errcode><errmsg>" + com.hjw.interfaces.util.StringUtil.formatException(ex) + "</errmsg></exchange>";// 1：成功
																									// 0：失败
		}
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + msg);
		return msg;
	}
	
	/**
	 *  中科软 - 江南医院 lis结果回传
	 * @param xmlmessage
	 * @return
	 */
	public String accetpMessageLis_sinoSoft(String xmlmessage) {
		String filetpe = "reslis";
		String msg = "<exchange><errcode>0</errcode><errmsg>操作失败</errmsg></exchange>";// 1：成功
																			// 0：失败
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		try {
			ProcListResult plr = new ProcListResult();
			InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			Node root = document.selectSingleNode("/exchange/group/HRC00.02/row"); 
			List<Element> listElement = root.getParent().elements();// 所有一级子节点的list
			boolean falags=true;
			for (Element e : listElement) {// 遍历所有一级子节点
				// System.out.println(e.asXML());
				plr.setBar_code(e.selectSingleNode("APPLYID").getText());// 申请单号
				plr.setExam_doctor(e.selectSingleNode("TESTPERSON").getText());// 检验医生
				plr.setExam_date(e.selectSingleNode("TESTTIME").getText());
				plr.setApprove_date(e.selectSingleNode("REVIEWEDTIME").getText());
				plr.setApprover(e.selectSingleNode("REPORTPERSON").getText());
                String item_name=e.selectSingleNode("ITEM_CH_NAME").getText();
				plr.setExam_result(e.selectSingleNode("RESULT").getText());
				plr.setRef_value(e.selectSingleNode("REFENCE").getText());
				plr.setItem_unit(e.selectSingleNode("ITEM_UNIT").getText());
				plr.setLis_item_code(e.selectSingleNode("IMTECODE").getText());
				plr.setLis_rep_item_code(e.selectSingleNode("ITEM_EN_NAME").getText());// 无细项目关联吗
																						// 目前lis按照体检细想编号维护的关联编码
				String ztbs = e.selectSingleNode("RESULTFLAG").getText();// 1：低；2：正常；3：高；4：不判断
				String sfwj = e.selectSingleNode("ISCRI").getText();// 0：非危急值
																	// 1：危急值
				// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
				if ("1".equals(sfwj)) {
					plr.setRef_indicator("4");
				} else if ("1".equals(ztbs)) {
					plr.setRef_indicator("2");
				} else if ("2".equals(ztbs)) {
					plr.setRef_indicator("0");
				} else if ("3".equals(ztbs)) {
					plr.setRef_indicator("1");
				}else{
					plr.setRef_indicator("0");
				}
				plr.setExam_num(getExamInfoForLisNum(plr.getBar_code(), filetpe));
				/*LisResult lisResult = new LisResult();
				lisResult.setTil_id("999");
				lisResult.setExam_num(plr.getExam_num());
				lisResult.setSample_barcode(plr.getBar_code());
				lisResult.setDoctor(plr.getExam_doctor());
				lisResult.setExam_date(plr.getExam_date());
				lisResult.setSh_doctor(plr.getApprover());
				lisResult.setReport_item_code(plr.getLis_item_code());
				lisResult.setReport_item_name(item_name);
				
				lisResult.setSeq_code(seq_code++);
				lisResult.setItem_result(plr.getExam_result());
				lisResult.setRef(plr.getRef_value());
				lisResult.setItem_unit(plr.getItem_unit());
				lisResult.setFlag(plr.getRef_indicator());
				boolean succ = this.configService.insert_lis_result(lisResult);
				if (!succ) {
					falags = false;
				}	*/				
				int resflag = this.commService.doproc_Lis_result(plr);
				if (resflag != 0) {
					resflag = this.commService.doproc_Lis_result(plr);
					if (resflag != 0) {
						falags = false;
					}
				}
			}
			if(!falags){
				msg = "<exchange><errcode>0</errcode><errmsg>入库失败</errmsg></exchange>";// 1：成功
			}else{
				msg = "<exchange><errcode>1</errcode><errmsg></errmsg></exchange>";// 1：成功
			}
		} catch (Exception ex) {
			msg = "<exchange><errcode>0</errcode><errmsg>" + com.hjw.interfaces.util.StringUtil.formatException(ex) + "</errmsg></exchange>";// 1：成功
			// 0：失败
		}
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + msg);
		return msg;
	}
	
	
	/**
	 * 
	 * @Title: accetpMessagePacs @Description: pacs
	 * 状态处理 @param: @return @return: String @throws
	 */
	public String accetpstatuMessagePacs_sinoSoft(String xmlmessage) {
		String filetpe = "respacsStatus";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		String msg = "<exchange><errcode>0</errcode><errmsg>操作失败</errmsg></exchange>";// 1：成功
		// 0：失败
		try {
			InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String req_no = document.selectSingleNode("/exchange/group/HRC00.02/row/DE01.00.008.00").getText();// 申请单号
			String statuss = document.selectSingleNode("/exchange/group/HRC00.02/row/DE06.00.290.02").getText();//00：检查开立
			//10：检查提交
			//20：检查预约
			//30：检查登记
			//35：检查取消
			//40：检查开始
			//50：图像到达
			//60：初步报告
			//70：审核时间发布 

            
			if ((statuss == null) || (statuss.length() <= 0)) {
				msg = "<exchange><errcode>0</errcode><errmsg>状态无效</errmsg></exchange>";// 1：成功
																						// 0：失败
			} else if ((req_no == null) || (req_no.length() <= 0)) {
				msg = "<exchange><errcode>0</errcode><errmsg>申请单号无效</errmsg></exchange>";// 1：成功
																							// 0：失败
			} else {
					 String exam_num=getExamInfoForPacsNum(req_no,filetpe);
					 if (StringUtil.isEmpty(exam_num)) {
							msg = "<exchange><errcode>0</errcode><errmsg>申请号查询不到体检者</errmsg></exchange>";// 1：成功
																											// 0：失败
						} else {
					List<String> pac_nos = new ArrayList<String>(); 
					pac_nos.add(req_no);
					
					/*00：检查开立
					10：检查提交
					20：检查预约
					30：检查登记
					35：检查取消
					40：检查开始
					50：图像到达
					60：初步报告
					70：审核时间发布 */

					if(("10".equals(statuss))||("20".equals(statuss))){//核收
						statuss="C";
						this.commService.setExamInfoChargeItemPacsStatus(pac_nos, exam_num, statuss);
						msg = "<exchange><errcode>1</errcode><errmsg></errmsg></exchange>";// 1：成功
						// 0：失败
					}else if(("30".equals(statuss))||("40".equals(statuss))||("50".equals(statuss))||("60".equals(statuss))||("70".equals(statuss))){//已检查
						statuss="N";
						this.commService.setExamInfoChargeItemPacsStatus(pac_nos, exam_num, statuss);
						msg = "<exchange><errcode>1</errcode><errmsg></errmsg></exchange>";// 1：成功
						// 0：失败
					}
				}				
			}
		} catch (Exception ex) {
			msg = "<exchange><errcode>0</errcode><errmsg>" + com.hjw.interfaces.util.StringUtil.formatException(ex) + "</errmsg></exchange>";// 1：成功
			// 0：失败
		}
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + msg);
		return msg;
	}
	
	/**
	 * 
	 * @Title: accetpMessagePacs @Description: pacs
	 * 结果返回处理 @param: @return @return: String @throws
	 */
	public String accetpMessagePacs_sinoSoft(String xmlmessage) {
		String filetpe = "respacs";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		String msg = "<exchange><errcode>0</errcode><errmsg>操作失败</errmsg></exchange>";// 1：成功
		// 0：失败
		try {
			InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			ProcPacsResult plr = new ProcPacsResult();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			plr.setPacs_req_code(document.selectSingleNode("/exchange/group/HRC00.02/row/DE01.00.008.01").getText());// 申请单号
			plr.setAudit_date(document.selectSingleNode("/exchange/group/HRC00.02/row/DE04.50.133.03").getText());
			plr.setAudit_doct(document.selectSingleNode("/exchange/group/HRC00.02/row/DE01.00.099.03").getText());//录入者
			plr.setCheck_date(document.selectSingleNode("/exchange/group/HRC00.02/row/DE04.50.133.03").getText());
			plr.setCheck_doct(document.selectSingleNode("/exchange/group/HRC00.02/row/DE01.03.023.00").getText());//检查医生
			plr.setExamMethod(document.selectSingleNode("/exchange/group/HRC00.02/row/DE04.50.139.00").getText());
			plr.setBodyPart(document.selectSingleNode("/exchange/group/HRC00.02/row/DE04.50.140.00").getText());
			plr.setExam_desc(document.selectSingleNode("/exchange/group/HRC00.02/row/DE51.05.099.00").getText());
			plr.setExam_num(getExamInfoForPacsNum(document.selectSingleNode("/exchange/group/HRC00.02/row/DE01.00.008.01").getText(),filetpe));
			plr.setExam_result(document.selectSingleNode("/exchange/group/HRC00.02/row/DE51.05.097.00").getText());
			
				if ((plr.getExam_num() != null) && (plr.getExam_num().trim().length() > 0)) {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					ei = this.commService.getExamInfoForNum(plr.getExam_num());
					if ((ei == null) || (ei.getId() <= 0)) {
						msg = "<exchange><errcode>0</errcode><errmsg>pacs信息 查无此人，入库错误</errmsg></exchange>";// 1：成功
					} else if ("Z".equals(ei.getStatus())) {
						msg = "<exchange><errcode>0</errcode><errmsg>pacs信息 已经总检，入账错误</errmsg></exchange>";// 1：成功
					} else {
						int pic_num = 0;
						String file_img="";
						String datatime = com.hjw.util.DateTimeUtil.getDate();
                        String imgurl=document.selectSingleNode("/exchange/group/HRC00.02/row/DE53.99.403.01").getText();//图片url地址
						plr.setImg_file(imgurl);
                        if (plr.getImg_file().length() > 10) {
							String picpath = this.commService.getDatadis("TPLJ").getName();
							pic_num++;
							File f = new File(picpath);
							if (!f.exists() && !f.isDirectory())
								f.mkdir();

							picpath = picpath + "\\pacs_img";
							String picname = "/pacs_img";
							f = new File(picpath);
							if (!f.exists() && !f.isDirectory())
								f.mkdir();
							picpath = picpath + "\\" + datatime;
							picname = picname + "/" + datatime;
							f = new File(picpath);
							if (!f.exists() && !f.isDirectory())
								f.mkdir();

							/*picpath = picpath + "\\" + dept_num;
							picname = picname + "/" + dept_num;
							f = new File(picpath);
							if (!f.exists() && !f.isDirectory())
								f.mkdir();*/

							picpath = picpath + "\\" + plr.getExam_num();
							picname = picname + "/" + plr.getExam_num();
							f = new File(picpath);
							if (!f.exists() && !f.isDirectory())
								f.mkdir();

							picpath = picpath + "\\" + plr.getPacs_req_code();
							picname = picname + "/" + plr.getPacs_req_code();
							f = new File(picpath);
							if (!f.exists() && !f.isDirectory())
								f.mkdir();
							// String filepath = pacs_summary_id + "-" +
							// Timeutils.getFileData() + "_"+ pic_num + ".jpg";
							// String jpgpath = pic_num+".jpg";
							String pdfpath = picpath + "\\" + pic_num + ".jpg";
							picpath = picpath + "\\" + pic_num;
							picname = picname + "/" + pic_num;
							TranLogTxt.liswriteEror_to_txt(filetpe, "图片路径： " + imgurl);
							FileOutputStream fos = null;
							PdfFtpHelper pdfh = null;
							try{
								pdfh=new PdfFtpHelper();
								FileDTO fd= pdfh.urlDownloadFile(imgurl);
								if (fd.isFlag()) {
									boolean ff =FileUtil.bytesToFile(fd.getBytes(),pdfpath);
									if(ff){
										file_img =  picname + ".jpg";
										TranLogTxt.liswriteEror_to_txt(filetpe, "图片名称： " + file_img);
									}
								}
							} catch (Exception ex) {
								TranLogTxt.liswriteEror_to_txt(filetpe, "图片保存： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
								ex.printStackTrace();
							} finally {
								if (fos != null) {
									fos.close();
								}
							}
							TranLogTxt.liswriteEror_to_txt(filetpe, "res::操作语句： " + file_img);
						}
				plr.setImg_file(file_img);
				
				int falgint = commService.proc_pacs_report_dbgj(plr);
				if (falgint == 0) {
					msg = "<exchange><errcode>1</errcode><errmsg></errmsg></exchange>";// 1：成功		
				} else {
					msg = "<exchange><errcode>0</errcode><errmsg>pacs信息 体检编号为空</errmsg></exchange>";// 1：成功				
				}
					}
				}else{
					msg = "<exchange><errcode>0</errcode><errmsg>查无此人</errmsg></exchange>";// 1：成功	
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = "<exchange><errcode>0</errcode><errmsg>pacs信息调用webservice错误</errmsg></exchange>";// 1：成功	
			}
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + msg);
		return msg;
	}
	
	

		
	/**
	 * 
	 * @param sample_barcode
	 * @return
	 * @throws ServiceException
	 */
	private String getExamInfoForLisNum(String sample_barcode,String filetpe) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select ei.exam_num from sample_exam_detail sa ,exam_info ei where sa.exam_info_id=ei.id "
				+ "and sa.sample_barcode='"+sample_barcode+"' ");	
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		String exam_num ="";
		if((map!=null)&&(map.getList().size()>0)){
			exam_num= ((ExamInfoUserDTO)map.getList().get(0)).getExam_num();			
		}
		return exam_num;
	} 
	
	
	/**
	 * 
	     * @Title: getExamInfoForReqNum   
	     * @Description: TODO(这里用一句话描述这个方法的作用)   
	     * @param: @param req_nums
	     * @param: @return
	     * @param: @throws ServiceException      
	     * @return: ExamInfoUserDTO      
	     * @throws
	 */
	public String getExamInfoForPacsNum(String req_nums,String filetpe) throws ServiceException{
		String exam_num="";
		String sql = " select m.id,m.age,m.exam_num,m.status,m.exam_type "
				+ " ,m.register_date,m.join_date,m.exam_times "
				+ " ,n.user_name,n.id_num,n.sex,n.birthday,n.phone "
				+ " from examinfo_charging_item a,exam_info m,customer_info n "
				+ " ,pacs_summary b,pacs_detail c,charging_item d" + " where b.pacs_req_code='" + req_nums
				+ "' and c.summary_id=b.id and c.chargingItem_num=d.item_code "
				+ " and d.id=a.charge_item_id and a.examinfo_id=m.id "
				+ " and m.exam_num=b.examinfo_num and a.isActive='Y' "
				+ " and m.customer_id = n.id";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + sql);
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);	
			exam_num=eu.getExam_num();
		}
		return exam_num;
	}
    
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		sb.append("<exchange>");
		sb.append("  <funcid>SD08.00.003.12</funcid>");
		sb.append("  <errcode>1</errcode>");
		sb.append("  <group>");
		sb.append("    <HRC00.02>");
		sb.append("      <row>");
		sb.append("        <DE02.99.401.99>");
		sb.append("        </DE02.99.401.99>");
		sb.append("        <DE04.30.019.00>");
		sb.append("        </DE04.30.019.00>");
		sb.append("        <DE02.01.031.00>");
		sb.append("        </DE02.01.031.00>");
		sb.append("        <DE01.00.084.00>肺部低剂量  1.00  Br40  S3</DE01.00.084.00>");
		sb.append("        <DE01.00.078.00>5292</DE01.00.078.00>");
		sb.append("        <DE02.01.025.00>");
		sb.append("        </DE02.01.025.00>");
		sb.append("        <DE01.03.049.00>李杰</DE01.03.049.00>");
		sb.append("        <DE01.00.080.00>5292</DE01.00.080.00>");
		sb.append("        <DE06.00.220.00>");
		sb.append("        </DE06.00.220.00>");
		sb.append("        <DE01.00.026.00>");
		sb.append("        </DE01.00.026.00>");
		sb.append("        <DE02.01.060.00>");
		sb.append("        </DE02.01.060.00>");
		sb.append("        <DE04.30.021.00>胸部低剂量CT</DE04.30.021.00>");
		sb.append("        <DE04.50.134.00>2019/6/12 8:14:07</DE04.50.134.00>");
		sb.append("        <DE08.10.054.00>");
		sb.append("        </DE08.10.054.00>");
		sb.append("        <DE01.00.010.06>");
		sb.append("        </DE01.00.010.06>");
		sb.append("        <DE01.00.010.04>体检中心</DE01.00.010.04>");
		sb.append("        <DE01.00.010.03>");
		sb.append("        </DE01.00.010.03>");
		sb.append("        <DE55.02.099.00>胸部CT扫描未见明显异常。</DE55.02.099.00>");
		sb.append("        <DE01.00.010.01>1906110048</DE01.00.010.01>");
		sb.append("        <DE09.00.000.11>");
		sb.append("        </DE09.00.000.11>");
		sb.append("        <DE08.50.019.00>CT</DE08.50.019.00>");
		sb.append("        <DE09.00.000.12>2019-06-12 00:00:00</DE09.00.000.12>");
		sb.append("        <DE04.30.018.00>肺窗：双肺肺纹理清晰，其分布未见明显异常，双肺实质内未见异常密度影。气管居中，主气管、支气管开口通畅。纵隔窗：双侧胸廓对称，心影形态正常，心包未见积液，纵隔内及两肺门未见明显肿大淋巴结影。双侧胸膜光整、双侧胸腔未见积液。</DE04.30.018.00>");
		sb.append("        <DE02.01.009.01>");
		sb.append("        </DE02.01.009.01>");
		sb.append("        <DE02.01.005.01>");
		sb.append("        </DE02.01.005.01>");
		sb.append("        <DE01.00.079.00>");
		sb.append("        </DE01.00.079.00>");
		sb.append("        <DE01.00.083.00>CT</DE01.00.083.00>");
		sb.append("        <DE21.01.001.02>体检中心</DE21.01.001.02>");
		sb.append("        <DE51.05.097.00>胸部CT扫描未见明显异常。</DE51.05.097.00>");
		sb.append("        <DE02.01.026.00>43</DE02.01.026.00>");
		sb.append("        <DE21.01.001.01>");
		sb.append("        </DE21.01.001.01>");
		sb.append("        <DE02.01.040.00>男</DE02.01.040.00>");
		sb.append("        <DE53.99.403.00>http://10.10.10.21:8081/WEBSEARCH.ASPX?Reqno='01906110369'</DE53.99.403.00>");
		sb.append("        <DE08.50.006.00>肺部低剂量  1.00  Br40  S3</DE08.50.006.00>");
		sb.append("        <DE04.30.022.00>");
		sb.append("        </DE04.30.022.00>");
		sb.append("        <DE51.05.087.00>");
		sb.append("        </DE51.05.087.00>");
		sb.append("        <DE06.00.187.02>");
		sb.append("        </DE06.00.187.02>");
		sb.append("        <DE01.00.010.16>");
		sb.append("        </DE01.00.010.16>");
		sb.append("        <DE04.50.139.00>");
		sb.append("        </DE04.50.139.00>");
		sb.append("        <DE01.00.010.15>");
		sb.append("        </DE01.00.010.15>");
		sb.append("        <DE01.00.010.11>");
		sb.append("        </DE01.00.010.11>");
		sb.append("        <DE04.50.131.02>");
		sb.append("        </DE04.50.131.02>");
		sb.append("        <DE04.30.015.00>");
		sb.append("        </DE04.30.015.00>");
		sb.append("        <DE04.50.131.01>");
		sb.append("        </DE04.50.131.01>");
		sb.append("        <DE01.00.082.00>");
		sb.append("        </DE01.00.082.00>");
		sb.append("        <DE02.01.010.00>13806145996</DE02.01.010.00>");
		sb.append("        <DE51.05.098.00>肺窗：双肺肺纹理清晰，其分布未见明显异常，双肺实质内未见异常密度影。气管居中，主气管、支气管开口通畅。纵隔窗：双侧胸廓对称，心影形态正常，心包未见积液，纵隔内及两肺门未见明显肿大淋巴结影。双侧胸膜光整、双侧胸腔未见积液。</DE51.05.098.00>");
		sb.append("        <DE02.01.010.01>");
		sb.append("        </DE02.01.010.01>");
		sb.append("        <DE04.50.141.00>胸部低剂量CT</DE04.50.141.00>");
		sb.append("        <DE01.03.024.00>");
		sb.append("        </DE01.03.024.00>");
		sb.append("        <DE01.00.021.00>");
		sb.append("        </DE01.00.021.00>");
		sb.append("        <DE08.10.052.00>");
		sb.append("        </DE08.10.052.00>");
		sb.append("        <DE01.00.018.01>2119</DE01.00.018.01>");
		sb.append("        <DE01.00.014.00>");
		sb.append("        </DE01.00.014.00>");
		sb.append("        <DE42.01.099.00>普通</DE42.01.099.00>");
		sb.append("        <DE04.50.132.00>2019/6/12 20:53:37</DE04.50.132.00>");
		sb.append("        <DE01.00.014.01>");
		sb.append("        </DE01.00.014.01>");
		sb.append("        <DE02.99.401.00>辛国华</DE02.99.401.00>");
		sb.append("        <DE04.30.020.00>A000335</DE04.30.020.00>");
		sb.append("        <DE51.05.099.00>肺窗：双肺肺纹理清晰，其分布未见明显异常，双肺实质内未见异常密度影。气管居中，主气管、支气管开口通畅。纵隔窗：双侧胸廓对称，心影形态正常，心包未见积液，纵隔内及两肺门未见明显肿大淋巴结影。双侧胸膜光整、双侧胸腔未见积液。</DE51.05.099.00>");
		sb.append("        <DE01.00.081.00>1.3.12.2.1107.5.99.3.106853.30000019061207221290000000265</DE01.00.081.00>");
		sb.append("        <DE04.50.140.00>胸部低剂量CT</DE04.50.140.00>");
		sb.append("        <DE01.03.048.00>");
		sb.append("        </DE01.03.048.00>");
		sb.append("        <DE01.00.008.00>");
		sb.append("        </DE01.00.008.00>");
		sb.append("        <DE01.00.008.01>01906110369</DE01.00.008.01>");
		sb.append("        <DE01.03.023.00>曾凡荣</DE01.03.023.00>");
		sb.append("        <DE01.00.001.02>");
		sb.append("        </DE01.00.001.02>");
		sb.append("        <DE01.00.099.04>2019-06-12 00:00:00</DE01.00.099.04>");
		sb.append("        <DE01.00.099.05>5292</DE01.00.099.05>");
		sb.append("        <DE01.00.099.00>体检中心</DE01.00.099.00>");
		sb.append("        <DE01.00.099.01>体检中心</DE01.00.099.01>");
		sb.append("        <DE01.00.099.02>070001</DE01.00.099.02>");
		sb.append("        <DE01.00.099.03>李杰</DE01.00.099.03>");
		sb.append("        <DE04.50.133.03>2019/6/12 20:53:37</DE04.50.133.03>");
		sb.append("        <DE06.00.290.01>");
		sb.append("        </DE06.00.290.01>");
		sb.append("        <DE04.30.017.00>");
		sb.append("        </DE04.30.017.00>");
		sb.append("        <DE02.01.052.00>");
		sb.append("        </DE02.01.052.00>");
		sb.append("        <DE01.00.015.00>");
		sb.append("        </DE01.00.015.00>");
		sb.append("        <DE02.01.039.00>左冬胜</DE02.01.039.00>");
		sb.append("      </row>");
		sb.append("    </HRC00.02>");
		sb.append("    <head>");
		sb.append("      <row>");
		sb.append("        <DE02.01.060.00>体检</DE02.01.060.00>");
		sb.append("      </row>");
		sb.append("    </head>");
		sb.append("  </group>");
		sb.append("</exchange>");
		WebServiceSoapImpl t = new WebServiceSoapImpl();
		t.accetpMessagePacs_sinoSoft(sb.toString());
	}
}
