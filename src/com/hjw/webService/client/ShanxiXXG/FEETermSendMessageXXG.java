package com.hjw.webService.client.ShanxiXXG;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.CUSTOMSendMessage;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.FeeTermBean;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.ShanxiXXG.bean.FeeItemXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ReqHeadXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ReqJsonXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ResBodyXXG;
import com.hjw.webService.client.ShanxiXXG.bean.SendFeeXXG;
import com.hjw.webService.client.ShanxiXXG.util.MD5ForXXG;
import com.hjw.webService.client.ShanxiXXG.util.UtilForXXG;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.huojianwa.bean.ComAccBean;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 发送团体缴费申请
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class FEETermSendMessageXXG {
	private String accnum="";
	private String personid="";
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public FEETermSendMessageXXG(String personid,String accnum) {
		this.accnum = accnum;
		this.personid=personid;
	}

	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + personid + ":"+this.accnum+":" + xml);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + url);
			if((personid!=null)&&(personid.trim().length()>30)){
				personid=personid.substring(0,30);
			}
			ComAccBean accnums = getAcc_nums(accnum,logname);
			String[] sendL = url.split("&");
			String sendUrl = sendL[0];
			String xxg_key = sendL[1];
			String fromtype = sendL[2];
			
			ReqHeadXXG reqHead = UtilForXXG.getReqHead(xxg_key,fromtype,"XXGJD");
			String head = JSONObject.fromObject(reqHead).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req head:" + head + "\r\n");
			head = MD5ForXXG.getBase64(head);
			head = head.replaceAll("\r|\n", "");
			
			SendFeeXXG reqBody = getReqBody(logname, accnums);
			String body = JSONObject.fromObject(reqBody).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req body:" + body + "\r\n");
			body = MD5ForXXG.getBase64(body);
			body = URLEncoder.encode(body.replaceAll("\r|\n", ""), "utf-8");
			
			try {
				StringBuffer reqUrl = new StringBuffer();
				reqUrl.append(sendUrl);
				reqUrl.append("/charge/apply?head=");
				reqUrl.append(head);
				reqUrl.append("&body=");
				reqUrl.append(body);
				
				TranLogTxt.liswriteEror_to_txt(logname, "req url:" + reqUrl.toString() + "\r\n");
				String result = HttpUtil.doPost(reqUrl.toString(), "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "res result:" + result + "\r\n");
				
				ReqJsonXXG resJson = new Gson().fromJson(result, ReqJsonXXG.class);
				String resBody = MD5ForXXG.getFromBase64(resJson.getBody());
				TranLogTxt.liswriteEror_to_txt(logname, "res body:" + resBody + "\r\n");
				ResBodyXXG resbody = new Gson().fromJson(resBody, ResBodyXXG.class);
				
				if("1".equals(resbody.getCode())) {
					ReqId rqid = new ReqId();
					rqid.setReq_id(accnum);
					rb.getControlActProcess().getList().add(rqid);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("发送收费申请成功!");
				}else {
					rb.getResultHeader().setTypeCode("AE");
				    rb.getResultHeader().setText(resbody.getMsg());
				    TranLogTxt.liswriteEror_to_txt(logname, resbody.getMsg());
				}
				
			}catch (Exception ex){
				rb.getResultHeader().setTypeCode("AE");
			    rb.getResultHeader().setText("error-发送收费申请失败!");
				TranLogTxt.liswriteEror_to_txt(logname,"发送收费申请-调用错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		} catch (Exception ex) {
			try{
			}catch(Exception ed){}
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息解析返回值错误");
		}
		
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + xml);
		return rb;
	}

	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public ComAccBean getAcc_nums(String account_num,String logname){
		Connection tjtmpconnect = null;
		ComAccBean accnums = new ComAccBean();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select f.batch_num,f.com_name,b.acc_num,f.com_num from charging_summary_group a "
					+ "left join (select c.id,c.batch_num,c.invoice_title as com_name,d.com_num from batch c,company_info d where c.company_id=d.id) f on f.id=a.batch_id"
					+ ",team_invoice_account b  "
					+ "where a.account_num=b.account_num and a.account_num='"+account_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				accnums.setAcc_num(rs1.getString("acc_num"));
				accnums.setBatch_num(rs1.getString("batch_num"));
				accnums.setCom_name(rs1.getString("com_name"));
				accnums.setCom_num(rs1.getString("com_num"));
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return accnums;
	}
	
	public SendFeeXXG getReqBody(String logname, ComAccBean accnums) {
		SendFeeXXG fee = new SendFeeXXG();
		
		fee.setTimes(1);
		fee.setCharge_no(accnums.getAcc_num());
//		fee.setApply_unit();
//		fee.setApply_opera();
		float charge_total = 0;
		
		String sql2 = "select f.charging_item_id,f.item_num,CAST(f.acc_charge as decimal(18,2)) as acc_charge"
				+ ",CAST(f.item_amount as decimal(18,2)) as item_amount,c.item_code,c.his_num,c.dep_category from "
				+ "( select l.charging_item_id, eci.item_amount,"
				+ "sum(eci.itemnum) as item_num, sum(l.acc_charge) as acc_charge "
				+ "from team_account_item_list l, examinfo_charging_item eci, exam_info e "
				+ "where l.exam_num=e.exam_num and eci.examinfo_id=e.id and eci.charge_item_id=l.charging_item_id and eci.isActive='Y' "
				+ "and l.acc_num = '"+accnums.getAcc_num()+"' and l.acc_charge>0 "
				+ "group by l.charging_item_id,eci.item_amount ) f,charging_item c where  c.id=f.charging_item_id ";
		TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql2);
		List<FeeTermBean> eciList = this.jdbcQueryManager.getList(sql2, FeeTermBean.class);
		
		List<FeeItemXXG> listcodes = new ArrayList<FeeItemXXG>();
		for (FeeTermBean eci:eciList) {
			FeeItemXXG feeItemXXG = new FeeItemXXG();
			feeItemXXG.setCode(eci.getHis_num());
//			feeItemXXG.setItem_name(eci.getItem_name());
			feeItemXXG.setCharge_single((float)eci.getAcc_charge());
			switch(eci.getDep_category()) {
				case "17":feeItemXXG.setFlag("jc");
				break;
				case "21":feeItemXXG.setFlag("jy");
				break;
				case "131":feeItemXXG.setFlag("jc");
				break;
				default:feeItemXXG.setFlag("jc");
			}
			feeItemXXG.setFlag("tj");
			listcodes.add(feeItemXXG);
			//long chargingid = eci.getCharging_item_id();
			//double prices = eci.getItem_amount();
			charge_total += eci.getAcc_charge();
		}
		/*
		for (int i = 0; i < projects.size(); i++) {
			String exam_num = projects.get(i).getEXAM_NUM();
			String item_code = projects.get(i).getExam_chargeItem_code();
			ExaminfoChargingItemDTO eci = getItemInfo(exam_num,item_code,logname);
			
			FeeItemXXG feeItemXXG = new FeeItemXXG();
			feeItemXXG.setCode(eci.getHis_num());
			feeItemXXG.setItem_name(eci.getItem_name());
			feeItemXXG.setCharge_single((float)eci.getAmount());
			switch(eci.getDep_category()) {
			case "17":feeItemXXG.setFlag("jc");
			break;
			case "21":feeItemXXG.setFlag("jy");
			break;
			case "131":feeItemXXG.setFlag("jc");
			break;
			default:feeItemXXG.setFlag("jc");
			}
			listcodes.add(feeItemXXG);
			charge_total = charge_total + (float)eci.getAmount();
		}
		 */
		
		//单位作为一个人来挂号
		Custom st = new Custom();
		st.setEXAM_NUM(accnums.getAcc_num());
		st.setNAME(accnums.getCom_name()+"-"+accnums.getBatch_num());
		st.setNAME_PHONETIC("");
		st.setSEX("");// 男 女
		st.setDATE_OF_BIRTH("");
		st.setBIRTH_PLACE("");
		st.setNATION("");
		st.setCITIZENSHIP("");
		st.setID_NO(accnums.getCom_num());
		st.setIDENTITY("一般人员");
		st.setUNIT_IN_CONTRACT("");
		st.setMAILING_ADDRESS("");
		st.setZIP_CODE("");
		st.setPHONE_NUMBER_BUSINESS("");
		st.setPHONE_NUMBER_HOME("");
		st.setNEXT_OF_KIN("");
		st.setRELATIONSHIP("");
		st.setNEXT_OF_KIN_ADDR("");
		st.setNEXT_OF_KIN_PHONE("");
		st.setNEXT_OF_KIN_ZIP_CODE("");
		st.setOPERATOR("");
		st.setBUSINESS_ZIP_CODE("");
		st.setPHOTO("");
		st.setPATIENT_CLASS("");
		st.setDEGREE("");
		st.setE_NAME("");
		st.setOCCUPATION("");
		st.setNATIVE_PLACE("");
		st.setMAILING_ADDRESS_CODE("");
		st.setMAILING_STREET_CODE("");
		st.setALERGY("");
		st.setMARITAL_STATUS("");
		st.setNEXT_OF_SEX("");
		st.setVISIT_DEPT("");
		st.setOPERATORS("");
		st.setCARD_NAME("");
		st.setCARD_NO("1");// 1-非院内卡挂号，院内卡挂号就传卡号
		st.setINVOICE_NO("");
		st.setCLINIC_NO("");
		st.setCLINIC_DATE_SCHEDULED("");
		st.setCHARGE_TYPE("自费");
		st.setAGE("0");
		st.setNEXT_OF_BATH("");
		st.setCHARGE_TYPE("2");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = configService.getWebServiceConfig("CUST_APPLICATION");
		String web_url = wcd.getConfig_url().trim();
		String web_meth = wcd.getConfig_method().trim();
		CUSTOMSendMessage csm = new CUSTOMSendMessage(st);
		ResultBody fr = new ResultBody();
		fr = csm.customSend(web_url, web_meth, true);
		TranLogTxt.liswriteEror_to_txt(logname,"挂号:"+fr.getResultHeader().getTypeCode()+"-"+fr.getResultHeader().getText());
		fee.setListcodes(listcodes);
		fee.setPatient_id(fr.getControlActProcess().getLIST().get(0).getPATIENT_ID());
		
		fee.setCharge_total(charge_total);
		
		return fee;
	}
}
