package com.hjw.webService.client.dashiqiao;

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
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeDSQ;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeMessageDSQ;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeeSends;
import com.hjw.webService.client.dashiqiao.FeeReqBean.FeesDSQ;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.domain.ChargingItem;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.19	项目减项  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessageDSQ {

	private DelFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	public DELFEESendMessageDSQ(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}
	static {
		init();
	}
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url,String logname) {
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		try {
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+feeMessage.getREQ_NO()+":"+xml);
			ResultHeader rh=getString(url,logname,this.feeMessage);
            if("AA".equals(rh.getTypeCode())){
            	ReqNo rqid = new ReqNo();
            	rqid.setREQ_NO(this.feeMessage.getREQ_NO());
            	rb.getControlActProcess().getList().add(rqid);
            	rb.getResultHeader().setTypeCode("AA");
    			rb.getResultHeader().setText("撤销收费申请成功!");
            }

		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
		return rb;
	}

	private ResultHeader  getString(String url,String logname, DelFeeMessage delfee){
		ResultHeader rh = new ResultHeader();
		ResHdMeessage rhd= new ResHdMeessage();
		
		FeeSends feeSends = new FeeSends();
		FeeMessageDSQ feeMessageDSQ = new FeeMessageDSQ();
		FeesDSQ feesDSQ = new FeesDSQ();
		ArrayList<FeeDSQ> FeeDSQList = new ArrayList<FeeDSQ>();
		
		try {
		
		ExamInfoUserDTO eu = getExamInfoForNum(delfee.getExam_num(), logname);
		List<ExaminfoChargingItemDTO> itemCodeList = delfee.getItemCodeList();
		for (int i = 0; i < itemCodeList.size(); i++) {
			ChargingItem ci = getChargingItem(itemCodeList.get(i).getItem_code(), logname);
			
			FeeDSQ feeDSQ = new FeeDSQ();
		 	feeDSQ.setPATIENT_ID(eu.getArch_num());
			feeDSQ.setEXAM_NUM(delfee.getExam_num());
			feeDSQ.setUSER_NAME(eu.getUser_name());
			feeDSQ.setVISIT_DATE(delfee.getVISIT_DATE());
			feeDSQ.setVISIT_NO("");
			feeDSQ.setSERIAL_NO("");
			feeDSQ.setORDER_CLASS("");
			feeDSQ.setORDER_NO("");
			feeDSQ.setORDER_SUB_NO("");
			feeDSQ.setITEM_NO("");
			feeDSQ.setITEM_CLASS("");
			feeDSQ.setITEM_NAME(ci.getItem_name());
			feeDSQ.setITEM_CODE(itemCodeList.get(i).getHis_num());
			feeDSQ.setITEM_SPEC(itemCodeList.get(i).getItem_amount()+"");
			feeDSQ.setUNITS("");
			feeDSQ.setREPETITION("");
			feeDSQ.setAMOUNT("1");
			feeDSQ.setORDERED_BY_DEPT("");
			feeDSQ.setORDERED_BY_DOCTOR("");
			feeDSQ.setPERFORMED_BY("");
			feeDSQ.setCLASS_ON_RCPT("");
			feeDSQ.setCOSTS("");
			feeDSQ.setCHARGES("");
			feeDSQ.setRCPT_NO("");
			feeDSQ.setCHARGE_INDICATOR("");
			feeDSQ.setCLASS_ON_RECKONING("");
			feeDSQ.setSUBJ_CODE("");
			feeDSQ.setPRICE_QUOTIETY("");
			feeDSQ.setITEM_PRICE("");
			feeDSQ.setCLINIC_NO(delfee.getPATIENT_ID());
			feeDSQ.setBILL_DATE("");
			feeDSQ.setBILL_NO("");
			feeDSQ.setSKINTEST("");
			feeDSQ.setPRESC_PSNO("");
			feeDSQ.setINSURANCE_FLAG("");
			feeDSQ.setINSURANCE_CONSTRAINED_LEVEL("");
			feeDSQ.setSKIN_SAVE("");
			feeDSQ.setSKIN_START("");
			feeDSQ.setSKIN_BATH("");
			feeDSQ.setYB_DIAG("");
			feeDSQ.setYB_DOCTOR("");
			feeDSQ.setExam_chargeItem_code(itemCodeList.get(i).getItem_code());
			feeDSQ.setCodeClass("");
			feeDSQ.setDiscount(itemCodeList.get(i).getDiscount());
			feeDSQ.setItem_Abbreviation(ci.getItem_abbreviation());
			
			FeeDSQList.add(feeDSQ);
		}
		feesDSQ.setPROJECT(FeeDSQList);
		feeMessageDSQ.setMSG_TYPE(feeMessage.getMSG_TYPE());
		feeMessageDSQ.setREQ_NO(feeMessage.getREQ_NO());

		feeMessageDSQ.setPROJECTS(feesDSQ);
         
		feeSends.setFeeMessage(feeMessageDSQ);// his缴费申请信息
		

		// his缴费申请 Lis Pacs 申请同时发送
		String json = new Gson().toJson(feeSends, FeeSends.class);

		TranLogTxt.liswriteEror_to_txt(logname, "req撤销发送给his的信息:" + json + "\r\n");
		String result = HttpUtil.doPost_Str(url, json, "utf-8");
		rhd = new Gson().fromJson(result, ResHdMeessage.class);
		TranLogTxt.liswriteEror_to_txt(logname, "reshis返回的撤销信息:" + result + "\r\n");
		
		
		if ((result != null) && (result.trim().length() > 0)) {
			if(rhd.getStatus().equals("1"))	{
				rh.setTypeCode("AA");
				rh.setText("成功!");;
			}	else{
				rh.setTypeCode("AE");
				rh.setText("接口返回失败");
			}
				
		}else{
			rh.setTypeCode("AE");
			rh.setText("接口无返回");
		}
		}catch(Exception ex){
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rh;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num, String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");
		sb.append(" and c.exam_num = '" + exam_num + "' ");
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}
	
	public ChargingItem getChargingItem(String exam_chargeItem_code, String logname) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from charging_item where item_code='"+exam_chargeItem_code+"' and isActive='Y' ");
		
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ChargingItem.class);
		ChargingItem ci = new ChargingItem();
		if((map!=null)&&(map.getList().size()>0)){
			ci= (ChargingItem)map.getList().get(0);			
		}
		return ci;
	}
	
}
