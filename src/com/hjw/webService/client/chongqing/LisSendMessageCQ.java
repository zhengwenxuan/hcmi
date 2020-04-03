package com.hjw.webService.client.chongqing;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.DTO.ZlReqPatinfoDTO;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.chongqing.soap.ZlHisSoapLocator;
import com.hjw.webService.client.chongqing.soap.ZlHisSoapPortType;
import com.hjw.webService.client.chongqing.util.ChongQingSetHL7;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import ca.uhn.hl7v2.model.v251.message.ORL_O22;
import ca.uhn.hl7v2.model.v251.message.ORR_O02;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import net.sf.json.JSONObject;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 2.6 检查申请信息服务 天健 平台对接-东北国际
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class LisSendMessageCQ {
	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static CustomerInfoService customerInfoService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}

	public LisSendMessageCQ(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		ResultLisBody rb = new ResultLisBody();
		JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		try {
			List<ApplyNOBean> anList = new ArrayList<ApplyNOBean>();
			for (LisComponents comps : lismessage.getComponents()) {
				ResultHeader rhone = new ResultHeader();
				rhone = this.lisSendMessage(url, comps, logname, lismessage);
				if ("AA".equals(rhone.getTypeCode())) {
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;
	}

	private ResultHeader lisSendMessage(String url, LisComponents comps, String logname, LisMessageBody lismessage) {

		ResultHeader rhone = new ResultHeader();
		try {

			String LisSendReq = ChongQingSetHL7.SendLis(comps, logname, lismessage);// 发送Pacs申请

			TranLogTxt.liswriteEror_to_txt(logname, "Lis发送申请入参:" + LisSendReq + "\r\n");

			URL zlurl = new URL(url);
			ZlHisSoapLocator soapLocator = new ZlHisSoapLocator();
			ZlHisSoapPortType portType = soapLocator.getzlHisSoapHttpSoap11Endpoint(zlurl);
			String res = portType.zlWS_HL7(LisSendReq);

			TranLogTxt.liswriteEror_to_txt(logname, "LIs发送申请返回:" + res + "\r\n");
			ORL_O22 fromJson = new Gson().fromJson(res, ORL_O22.class);
			if (fromJson != null && fromJson.toString().length() > 10) {
				MSH msh = fromJson.getMSH();
				MSA msa = fromJson.getMSA();
				String code = msa.getAcknowledgmentCode().toString();
				if (code.equals("AA")) {
					rhone.setTypeCode("AA");
				} else {
					rhone.setTypeCode("AE");
				}
			} else {
				rhone.setTypeCode("AE");
			}
		} catch (Exception ex) {
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}

		return rhone;
	}

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getHISDJH(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"SELECT zl_djh as others,zl_tjh as visit_no,zl_mzh as clinic_no FROM zl_req_patInfo where exam_num = '"
						+ exam_num + "' ");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	private ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type" + ",c.register_date,c.join_date,c.exam_times ");
		sb.append(" from exam_info c ");
		sb.append(" where c.is_Active='Y' ");
		sb.append(" and c.exam_num = '" + exam_num + "' ");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}

	// 根据体检号查询zl_reqpatinfo表信息
	private ZlReqPatinfoDTO getzl_patinfoFromNum(String exam_num) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from zl_req_patInfo where exam_num= '" + exam_num + "'");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ZlReqPatinfoDTO.class);
		ZlReqPatinfoDTO zlreq = new ZlReqPatinfoDTO();

		if ((map != null) && (map.getList().size() > 0)) {
			zlreq = (ZlReqPatinfoDTO) map.getList().get(0);
		}
		return zlreq;

	}

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	private ExamInfoUserDTO getSamDemo(String chargid, String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select b.demo_num as exam_num,b.demo_name as arch_num from charging_item a,sample_demo b where a.sam_demo_id=b.id and a.id='"
						+ chargid + "'");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":操作语句： " + sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public double getChargingAmt(String id) throws ServiceException {
		Connection tjtmpconnect = null;
		double lisitemid = 0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select amount from charging_item a where id='" + id + "'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				lisitemid = rs1.getDouble("amount");
			}
			rs1.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisitemid;
	}

	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int updatezl_req_item(String exam_num, String req_id, String ciid, String logname) {
		ExamInfoUserDTO ei = new ExamInfoUserDTO();
		ei = this.getExamInfoForNum(exam_num);
		ZlReqPatinfoDTO zlp = new ZlReqPatinfoDTO();
		zlp = getzl_patinfoFromNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid = 0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "delete from zl_req_item where  exam_info_id='" + ei.getId() + "'  and charging_item_id='"
					+ ciid + "' and req_id='" + req_id + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":操作语句： " + sb1);
			tjtmpconnect.createStatement().execute(sb1);

			String insertsql = "insert into zl_req_item(exam_info_id,charging_item_id,zl_pat_id,lis_item_id,req_id,lis_req_code,createdate) values('"
					+ ei.getId() + "','" + ciid + "','" + zlp.getZl_pat_id() + "','" + 2 + "','" + req_id + "','"
					+ req_id + "','" + DateTimeUtil.getDateTime() + "')";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":操作语句： " + insertsql);
			preparedStatement = tjtmpconnect.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.executeUpdate();
			ResultSet rs = null;
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next())

				lisid = rs.getInt(1);

			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ": zl_req_item 操作失败"
					+ com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisid;
	}

	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int getzl_req_Lis_item(String exam_num, String req_id, String cicode, String logname) {
		ExamInfoUserDTO ei = new ExamInfoUserDTO();
		ei = this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid = 0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();

			String sb1 = "select a.id from zl_req_item a where a.exam_info_id='" + ei.getId()
					+ "' and a.charging_item_id='" + cicode + "' and a.req_id='" + req_id + "' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":操作语句： " + sb1);
			tjtmpconnect.createStatement().execute(sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				lisid = rs1.getInt("id");
			}
			rs1.close();

		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ": zl_req_item 操作失败"
					+ com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisid;
	}

}
