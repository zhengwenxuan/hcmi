package com.hjw.webService.client.chongqing;

import com.hjw.util.DateTimeUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.hsqldb.lib.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.chongqing.soap.ZlHisSoapLocator;
import com.hjw.webService.client.chongqing.soap.ZlHisSoapPortType;
import com.hjw.webService.client.chongqing.soap.ZlHisSoapSoap11BindingStub;
import com.hjw.webService.client.chongqing.util.ChongQingSetHL7;
import com.hjw.webService.client.chongqing.util.PinyinUtil;
import com.hjw.webService.client.hokai.bean.ResCustomBeanHK;
import com.hjw.webService.client.xhhk.pacsbean.XDReportIn;
import com.synjones.framework.persistence.JdbcQueryManager;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.datatype.ID;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;
import ca.uhn.hl7v2.model.v251.message.ORM_O01;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

public class CUSTOMSendMessageCQ {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private Custom custom = new Custom();
	private ResCustomBeanHK rb1 = new ResCustomBeanHK();

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public CUSTOMSendMessageCQ(Custom custom) {
		this.custom = custom;
	}

	/*
	 * 根据数据字典id查询民族
	 */
	public Custom dataidFromRemark(Custom custom) {

		Connection sjzd = null;
		try {
			sjzd = jdbcQueryManager.getConnection();
			String sb = "select remark from  data_dictionary where id='" + custom.getNATION() + "'";
			ResultSet minzu = sjzd.createStatement().executeQuery(sb);
			if (minzu.next()) {
				String mzhz = minzu.getString("remark");
				custom.setNATION(mzhz);

			}
			sjzd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return custom;

	}

	/**
	 * 
	 * @param url
	 * @param logname
	 * @return
	 */
	public ResultBody getMessage(String url, String configvalue, String logname) {
		if (StringUtils.isEmpty(this.custom.getID_NO())) {
			this.custom.setID_NO(this.custom.getEXAM_NUM());
		}
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		long examid = examIdForExamNum(this.custom.getEXAM_NUM(), logname);
		ResultBody rb = new ResultBody();
		if (examid <= 0) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("无效体检编号");
		} else {
			try {

				ResCustomBeanHK rcb = setSearchString(url, logname);
				if ("AA".equals(rcb.getCode())) {
					//insert_search(examid, custom.getEXAM_NUM(), rcb.getPersionid(), logname);
					// 插入表
					CustomResBean cus = new CustomResBean();
					
					//===不返回患者id
					//cus.setCLINIC_NO(rcb.getPersionid());
					//cus.setPATIENT_ID(rcb.getPersionid());
					//cus.setVISIT_NO(rcb.getPersionid());
					
					
					cus.setVISIT_DATE(DateTimeUtil.getDateTimes());
					List<CustomResBean> LIST = new ArrayList<CustomResBean>();
					LIST.add(cus);
					ControlActProcess ControlActProcess = new ControlActProcess();
					ControlActProcess.setLIST(LIST);
					rb.setControlActProcess(ControlActProcess);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("患者信息注册成功");
				} else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("查询接口返回错误");
				}
			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(
						"发送患者注册信息-调用webservice错误Exception：" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				TranLogTxt.liswriteEror_to_txt(logname,
						"发送患者注册信息-调用错误Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}

		return rb;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	private ResCustomBeanHK setSearchString(String url,String logname) {
		ResCustomBeanHK rcb = new ResCustomBeanHK();
		Custom remark = dataidFromRemark(custom);
		try {
			URL zlurl = new URL(url);
			
			String CusetomerSendReq = ChongQingSetHL7.setCustomerSend(this.custom,logname);//患者信息新增接口

			ZlHisSoapLocator soapLocator = new ZlHisSoapLocator();
			ZlHisSoapPortType portType = soapLocator.getzlHisSoapHttpSoap11Endpoint(zlurl);
			String res = portType.zlWS_HL7(CusetomerSendReq);

			

			ACK fromJson = new Gson().fromJson(res, ACK.class);
			TranLogTxt.liswriteEror_to_txt(logname, "人员注册返回数据:" + fromJson.toString() + "\r\n");
			if (fromJson != null && fromJson.toString().length() > 10) {
				MSH msh = fromJson.getMSH();
				MSA msa = fromJson.getMSA();
				String code = msa.getAcknowledgmentCode().toString();
				if (code.equals("AA")) {
					rcb.setCode("AA");
				} else {
					rcb.setCode("AE");
				}
			}

		} catch (Exception e) {
			rcb.setCode("AE");
			e.printStackTrace();
		}
		return rcb;
	}

	/**
	 * 
	 * @param xmlmessage
	 * @return
	 */
	private ResCustomBeanHK res_search(long exam_id, String logname) {
		ResCustomBeanHK rcb = new ResCustomBeanHK();
		rcb.setCode("AE");
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,zl_pat_id,exam_num,"
					+ "zl_mzh,zl_tjh,flag from zl_req_patInfo where exam_info_id='" + exam_id + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1 + "\r\n");
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				rcb.setCode("AA");
				rcb.setPersionid(rs1.getString("zl_pat_id"));
			}
			rs1.close();
		} catch (Exception ex) {

		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rcb;
	}

	/**
	 * 
	 * @param xmlmessage
	 * @return
	 */
	private void insert_search(long examid, String exam_num, String persion, String logname) {
		long exam_id = examid;
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String insertsql = "insert into zl_req_patInfo ( exam_info_id,zl_pat_id,exam_num,"
					+ "zl_mzh,zl_tjh,zl_djh,flag) values('" + exam_id + "','" + persion + "','" + exam_num + "','"
					+ persion + "','" + persion + "','" + persion + "','0')";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + insertsql + "\r\n");
			tjtmpconnect.createStatement().executeUpdate(insertsql);
		} catch (Exception ex) {

		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param exam_num
	 * @return
	 */
	public long examIdForExamNum(String exam_num, String logname) {
		long exam_id = 0;
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb = "select id from exam_info where exam_num='" + exam_num + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb + "\r\n");
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb);
			if (rs.next()) {
				exam_id = rs.getLong("id");
			}
			rs.close();
		} catch (Exception ex) {

		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return exam_id;
	}

}
