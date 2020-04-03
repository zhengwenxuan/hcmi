package com.hjw.webService.client.ShanxiXXG;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.ShanxiXXG.bean.FeeItemXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ReqHeadXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ReqJsonXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ResBodyXXG;
import com.hjw.webService.client.ShanxiXXG.bean.SendFeeXXG;
import com.hjw.webService.client.ShanxiXXG.util.MD5ForXXG;
import com.hjw.webService.client.ShanxiXXG.util.UtilForXXG;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class FeeSendmessageXXG {

	private FeeMessage feeMessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private static CustomerInfoService customerInfoService;
	static {
		init();
	}

	public FeeSendmessageXXG(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}
	
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = JaxbUtil.convertToXmlWithOutHead(this.feeMessage, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		
		try {
			
			String[] sendL = url.split("&");
			String sendUrl = sendL[0];
			String xxg_key = sendL[1];
			String fromtype = sendL[2];
			
			ReqHeadXXG reqHead = UtilForXXG.getReqHead(xxg_key,fromtype,"XXGJD");
			String head = JSONObject.fromObject(reqHead).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req head:" + head + "\r\n");
			head = MD5ForXXG.getBase64(head);
			head = head.replaceAll("\r|\n", "");
			
			SendFeeXXG reqBody = getReqBody(logname, feeMessage);
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
					rqid.setReq_id(this.feeMessage.getREQ_NO());
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
			
		}catch (Exception e) {
			rb.getResultHeader().setTypeCode("AE");
		    rb.getResultHeader().setText("error-操作错误!");
			TranLogTxt.liswriteEror_to_txt(logname,"发送收费申请-调用错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
//		rb.getResultHeader().setTypeCode("AA");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getResultHeader().getText() + "\r\n");
		return rb;
	}
	
	public SendFeeXXG getReqBody(String logname,FeeMessage feeMessage) {
		SendFeeXXG fee = new SendFeeXXG();
		
		List<Fee> projects = feeMessage.getPROJECTS().getPROJECT();
		
		Fee project0 = projects.get(0);
		fee.setPatient_id(project0.getPATIENT_ID());
		fee.setTimes(1);
		fee.setCharge_no(feeMessage.getREQ_NO());
		fee.setApply_unit(project0.getORDERED_BY_DEPT());
		fee.setApply_opera(project0.getORDERED_BY_DOCTOR());
		float charge_total = 0;
		List<FeeItemXXG> listcodes = new ArrayList<FeeItemXXG>();
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
		
		fee.setCharge_total(charge_total);
		fee.setListcodes(listcodes);
		
		return fee;
	}
	
	public ExaminfoChargingItemDTO getItemInfo(String exam_num,String item_code,String logname){
		Connection connection = null;
		String sql = "select c.his_num,eci.amount,c.item_code,c.item_name,d.dep_category from exam_info e,examinfo_charging_item eci " + 
				"left join charging_item c on eci.charge_item_id = c.id left join department_dep d on c.dep_id = d.id " + 
				"where eci.examinfo_id = e.id and  e.exam_num = '"+exam_num+"' and c.item_code = '"+item_code+"' ";
		TranLogTxt.liswriteEror_to_txt(logname, "res: 查项目信息操作语句： " + sql);
		ExaminfoChargingItemDTO item = new ExaminfoChargingItemDTO();
		try {
			List<ExaminfoChargingItemDTO> listEci = this.jdbcQueryManager.getList(sql, ExaminfoChargingItemDTO.class);
			if(listEci.size()>0) {
				item = listEci.get(0);
			}else {
				TranLogTxt.liswriteEror_to_txt(logname, "res: 查不到项目信息");
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname, "error: 查项目信息错误 ："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		}
		return item;
	}
	
}
