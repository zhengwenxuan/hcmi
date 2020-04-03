package com.hjw.webService.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.hsqldb.lib.StringUtil;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.Timeutils;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Carestream.Bean.RKReturnXml;
import com.hjw.webService.service.pacsbean.Carestream.UpdateOrderReport;
import com.hjw.webService.service.pacsbean.Carestream.UpdateOrderStatus;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;

public class UVESB extends ServletEndpointSupport {
	private CommService commService;

	protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
	}

	/**
	 * 
	 * @Title: accetpMessagePacs @Description: pacs
	 *         结果返回处理 @param: @return @return: String @throws
	 */
	public String CallESB(String CallType, String xmlMessage) {
		TranLogTxt.liswriteEror_to_txt("accountpacsLog", "req:传入信息:" + xmlMessage);
		if ("UpdateOrderStatus".equals(CallType)) {
			return UpdateOrderStatus(xmlMessage);
		} else if ("UpdateOrderReport".equals(CallType)) {
			return UpdateOrderReport(xmlMessage);
		} else {
			RKReturnXml ResultHeader = new RKReturnXml();
			ResultHeader.setResultCode("0");
			ResultHeader.setResultInfo("无效消息类型");
			String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
			return reqxml;
		}
	}

	private String UpdateOrderStatus(String xmlmessage) {
		String filetpe = "respacsStatusRK";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		RKReturnXml ResultHeader = new RKReturnXml();
		try {
			UpdateOrderStatus rt = new UpdateOrderStatus();
			rt = JaxbUtil.converyToJavaBean(xmlmessage, UpdateOrderStatus.class);
			try {
				if ((rt.getRemoteAccNo() == null) || (rt.getRemoteAccNo().length() <= 0) || (rt.getStatus() == null)
						|| (rt.getStatus().length() <= 0)) {
					ResultHeader.setResultCode("0");
					ResultHeader.setResultInfo("pacs信息解析错误");
				} else {
					/*
					 * 10-登记完成 14-检查完成 32-报告审核 0-科室撤销医嘱
					 */

					String statuss = "";
					if ("10".equals(rt.getStatus())) {// 核收
//						statuss = "C";
//						this.commService.setExamInfoChargeItemPacsStatus(rt.getRemoteAccNo(), statuss);
					} else if (("14".equals(rt.getStatus())) || ("32".equals(rt.getStatus()))) {// 已检查
//						statuss = "Y";
//						this.commService.setExamInfoChargeItemPacsStatus(rt.getRemoteAccNo(), statuss);
					}
					ResultHeader.setResultCode("1");
					ResultHeader.setResultInfo("");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				ResultHeader.setResultCode("0");
				ResultHeader.setResultInfo("pacs信息调用webservice错误"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ResultHeader.setResultCode("0");
			ResultHeader.setResultInfo("信息 xml解析错误"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:返回信息:" + reqxml);
		return reqxml;
	}

	private String UpdateOrderReport(String xmlMessage) {
		String filetpe = "respacsRK";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlMessage);
		RKReturnXml ResultHeader = new RKReturnXml();
		try {
			UpdateOrderReport rc = new UpdateOrderReport();
			rc = JaxbUtil.converyToJavaBean(xmlMessage, UpdateOrderReport.class);
			int pic_num = 1;
			String reqnum = rc.getRemoteAccNo();
			if ((reqnum != null) && (reqnum.trim().length() > 0)) {
				ExamInfoUserDTO ei = new ExamInfoUserDTO();
				ei = this.commService.getExamInfoForReqNum(reqnum);
				if ((ei == null) || (ei.getId() <= 0)) {
					ResultHeader.setResultCode("0");
					ResultHeader.setResultInfo("pacs信息 查无此人，入库错误");
				} else if ("Z".equals(ei.getStatus())) {
					ResultHeader.setResultCode("0");
					ResultHeader.setResultInfo("pacs信息 已经总检，入账错误");
				} else {
					String exam_num = ei.getExam_num();
					ProcPacsResult plr = new ProcPacsResult();
					plr.setExam_num(exam_num);
					plr.setPacs_req_code(rc.getRemoteAccNo());
					plr.setCheck_date(rc.getApproveDT());
					plr.setCheck_doct(rc.getApproveDoc());
					plr.setAudit_date(rc.getSubmitDT());
					plr.setAudit_doct(rc.getSubmitDoc());
					plr.setExam_desc(rc.getDescription());
					plr.setExam_result(rc.getComment());
					String datatime = rc.getSubmitDT();
					String depnum = this.commService.getDepNumForPacs(rc.getRemoteAccNo());
					String picpath = this.commService.getDatadis("TPLJ").getName();
					boolean flagss = true;

					if ((rc.getImageUrl() != null) && (rc.getImageUrl().trim().length() > 0)) {
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

						picpath = picpath + "\\" + depnum;
						picname = picname + "/" + depnum;
						f = new File(picpath);
						if (!f.exists() && !f.isDirectory())
							f.mkdir();

						picpath = picpath + "\\" + exam_num;
						picname = picname + "/" + exam_num;
						f = new File(picpath);
						if (!f.exists() && !f.isDirectory())
							f.mkdir();

						picpath = picpath + "\\" + rc.getRemoteAccNo();
						picname = picname + "/" + rc.getRemoteAccNo();
						f = new File(picpath);
						if (!f.exists() && !f.isDirectory())
							f.mkdir();
						String filepath = rc.getRemoteAccNo() + "-" + Timeutils.getFileData() + "_" + pic_num + ".pdf";
						String jpgpath = rc.getRemoteAccNo() + "_" + pic_num;
						picpath = picpath + "\\" + jpgpath;
						picname = picname + "/" + jpgpath;

						this.getHttpJpg(rc.getImageUrl().trim(), picpath);

						String file_img = picname + "_" + 1 + ".jpg";
						plr.setImg_file(file_img);
					}
					int falgint = commService.proc_pacs_report_dbgj(plr);
					if (falgint != 0) {
						flagss = false;
					}
					if (flagss) {
						ResultHeader.setResultCode("1");
						ResultHeader.setResultInfo("交易成功");
					} else {
						ResultHeader.setResultCode("0");
						ResultHeader.setResultInfo("pacs 入库失败");
					}
				}
			} else {
				ResultHeader.setResultCode("0");
				ResultHeader.setResultInfo("pacs信息 体检编号为空");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			ResultHeader.setResultCode("0");
			ResultHeader.setResultInfo("信息 xml解析错误");
		}
		String reqxml = JaxbUtil.convertToXml(ResultHeader, true);
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:返回:" + reqxml);
		return reqxml;

	}

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	private void getHttpJpg(String filePath, String picpath) {
		byte[] byteArray = null;
		HttpURLConnection connection = null;
		if (!StringUtil.isEmpty(filePath)) {

			try {
				URL url = new URL(filePath);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(5 * 1000);
				InputStream in = connection.getInputStream();
				try {
					byteArray = readInputStream(in);
					File imageFile = new File(picpath);
					// 创建输出流
					FileOutputStream outStream = new FileOutputStream(imageFile);
					// 写入数据
					outStream.write(byteArray);
					// 关闭输出流
					outStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (IOException e2) {
				e2.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}
	}

	private static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024]; // 创建一个Buffer字符串
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		inStream.close(); // 关闭输入流
		return outStream.toByteArray(); // 把outStream里的数据写入内存
	}
}
