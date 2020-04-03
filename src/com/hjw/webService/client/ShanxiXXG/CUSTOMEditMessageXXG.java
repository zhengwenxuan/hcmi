package com.hjw.webService.client.ShanxiXXG;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.ShanxiXXG.bean.CustomerXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ReqHeadXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ReqJsonXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ResBodyXXG;
import com.hjw.webService.client.ShanxiXXG.util.MD5ForXXG;
import com.hjw.webService.client.ShanxiXXG.util.UtilForXXG;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.wst.DTO.CustomerExamDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

public class CUSTOMEditMessageXXG {

	private Custom custom=new Custom();
	
	public CUSTOMEditMessageXXG(Custom custom){
		this.custom=custom;
	}
	
	private static JdbcQueryManager jdbcQueryManager;
	
	static {
		init();
	}
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public FeeResultBody getMessage(String url,String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		try {
			xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
			String[] sendL = url.split("&");
			String sendUrl = sendL[0];
			String xxg_key = sendL[1];
			String fromtype = sendL[2];
			
			ReqHeadXXG reqHead = UtilForXXG.getReqHead(xxg_key,fromtype,"XXGJD");
			String head = JSONObject.fromObject(reqHead).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req head:" + head + "\r\n");
			head = MD5ForXXG.getBase64(head);
			head = head.replaceAll("\r|\n", "");
			
			
			CustomerXXG reqbody = getReqBody(this.custom,logname);
			String body = JSONObject.fromObject(reqbody).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req body:" + body + "\r\n");
			body = MD5ForXXG.getBase64(body);
			body = URLEncoder.encode(body.replaceAll("\r|\n", ""), "utf-8");
			
			try {
				StringBuffer reqUrl = new StringBuffer();
				
				reqUrl.append(sendUrl);
				reqUrl.append("/basic/modify?head=");
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
					CustomResBean cus= new CustomResBean();
					cus.setCLINIC_NO(resbody.getPatient_id());
					cus.setPATIENT_ID(resbody.getPatient_id());
					cus.setVISIT_NO(resbody.getPatient_id());
					cus.setVISIT_DATE(DateTimeUtil.getDateTimes());
					List<CustomResBean> LIST=new ArrayList<CustomResBean>();
					LIST.add(cus);
					ControlActProcess ControlActProcess=new ControlActProcess();
					ControlActProcess.setLIST(LIST);
				    rb.getResultHeader().setTypeCode("AA");
				    rb.getResultHeader().setText("ok-修改人员成功!");
				}else {
					rb.getResultHeader().setTypeCode("AE");
				    rb.getResultHeader().setText("error-修改人员失败!");
				}
				
			}catch (Exception ex){	
				rb.getResultHeader().setTypeCode("AE");
			    rb.getResultHeader().setText("error-发送病人信息失败!");
				TranLogTxt.liswriteEror_to_txt(logname,"Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		} catch (Exception e) {
			rb.getResultHeader().setTypeCode("AE");
		    rb.getResultHeader().setText("error-操作错误!");
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
//		rb.getResultHeader().setTypeCode("AA");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getResultHeader().getText() + "\r\n");
		return rb;
	}
	
	public CustomerXXG getReqBody(Custom custom,String logname) {
		CustomerXXG customer = new CustomerXXG();
		
		customer.setPatient_id(custom.getPATIENT_ID());
		customer.setSocial_no(custom.getID_NO());
		customer.setName(custom.getNAME());
		switch(custom.getSEX()) {
		case "男":customer.setSex("1");
		break;
		case "女":customer.setSex("2");
		break;
		default :customer.setSex("");
		}
		CustomerExamDTO cus = getCustomerInfo(custom.getEXAM_NUM(),logname);
		customer.setBirth_place(custom.getBIRTH_PLACE());
		customer.setBirthday(UtilForXXG.formatBirthday(custom.getDATE_OF_BIRTH()));
		customer.setTel(cus.getPhone());
		customer.setResponse_type("自费");
		customer.setApply_opera(custom.getOPERATOR());
		customer.setEmployer_name(cus.getCompany());
		
		return customer;
	}
	
	public CustomerExamDTO getCustomerInfo(String exam_num,String logname) {
		
		String sql = "select e.exam_num,c.user_name,c.phone,com.com_name as company,e.exam_type from customer_info c,exam_info e " + 
				"left join group_info g on e.group_id = g.id " + 
				"left join batch b on g.batch_id = b.id " + 
				"left join company_info com on b.company_id = com.id  " + 
				"where e.customer_id = c.id and e.is_Active = 'Y' and e.exam_num = '"+exam_num+"'";
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sql + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, CustomerExamDTO.class);
		CustomerExamDTO cus = new CustomerExamDTO();
		if((map!=null)&&(map.getList().size()>0)){
			cus= (CustomerExamDTO)map.getList().get(0);			
		}
		return cus;
	}
	
}
