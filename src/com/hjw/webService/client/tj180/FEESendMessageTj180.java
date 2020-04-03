package com.hjw.webService.client.tj180;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.test.Test;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.FeeControlActProcess;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.tj180.Bean.HisBodyReqBean;
import com.hjw.webService.client.tj180.Bean.HisReqBean;
import com.hjw.webService.client.tj180.Bean.HisResBean;
import com.hjw.webService.client.tj180.Bean.HisResItemBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.14	挂号信息服务   天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class FEESendMessageTj180 {

	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	   static{
	   	init();
	   	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}
	public FEESendMessageTj180(FeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		try {
			this.feeMessage.setMSG_TYPE("TJ602");
			xml = JaxbUtil.convertToXml(this.feeMessage, true);
			TranLogTxt.liswriteEror_to_txt(logname, "req:---:" + xml);

			HisBodyReqBean hbrb = new HisBodyReqBean();
			boolean flag = true;
			List<HisReqBean> itemsInfo = new ArrayList<HisReqBean>();
			
			for (Fee fee : feeMessage.getPROJECTS().getPROJECT()) {
				if (flag) {
					String exam_num = fee.getEXAM_NUM();
					ExamInfoUserDTO ei = this.getExamInfoForNum(exam_num);
					hbrb.setCustomerPatientId(ei.getArch_num());
					hbrb.setReserveId(exam_num);
					hbrb.setOrderDept(fee.getORDERED_BY_DEPT());
					hbrb.setItemsNum(feeMessage.getPROJECTS().getPROJECT().size() + "");
					flag = false;
				}
				HisReqBean hqb = new HisReqBean();
				hqb = this.getPrices(fee.getITEM_CODE(), fee.getCodeClass(),logname);
				hqb.setChargeDept(fee.getPERFORMED_BY());
				hqb.setCodeClass(fee.getCodeClass());				
				hqb.setUnionProjectId(fee.getExam_chargeItem_code());
				itemsInfo.add(hqb);
			}
			hbrb.setItemsInfo(itemsInfo);
			JSONObject json = JSONObject.fromObject(hbrb);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
			String result = HttpUtil.doPost(url,hbrb,"utf-8");
			//String result="{\"billItemsInfo\":[],\"errorInfo\":\"找不到C0003858对应的价表项目\",\"billItemsNum\":\"\",\"status\":\"500\"}";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
            if((result!=null)&&(result.trim().length()>0)){	   
					result = result.trim();
					JSONObject jsonobject = JSONObject.fromObject(result);
					Map classMap = new HashMap();
					classMap.put("billItemsInfo", HisResItemBean.class);
					HisResBean resdah = new HisResBean();
					resdah = (HisResBean) JSONObject.toBean(jsonobject, HisResBean.class, classMap);
					if ("200".equals(resdah.getStatus())) {
						if (!resdah.getBillItemsNum().trim().equals(hbrb.getItemsNum())) {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("回传缴费项数目个数不对，回传个数为：" + resdah.getBillItemsNum() + "个");
						} else {
							List<String> itemCodeList = new ArrayList<String>();
							
							for(HisResItemBean hi:resdah.getBillItemsInfo())
							{
								itemCodeList.add(hi.getUnionProjectId());
							}
							rb.setItemCodeList(itemCodeList);
							rb.getResultHeader().setTypeCode("AA");
						}
					}else{
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(resdah.getErrorinfo());	
					}
				}

		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "re1:" + str);
		return rb;
	}

	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times,a.arch_num ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public HisReqBean getPrices(String id,String code_class,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		HisReqBean hb = new HisReqBean();
		if("1".equals(code_class)){//价表
			String sql ="SELECT item_class as itemClass,item_code as itemCode,item_spec as itemSpec,units FROM his_price_list where ID='"+id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sql);
			PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, HisReqBean.class);
			if((map!=null)&&(map.getList().size()>0)){
				hb= (HisReqBean)map.getList().get(0);			
			}
		}else if("2".equals(code_class)){//诊疗项目
			String sql ="SELECT item_class as itemClass,item_code as itemCode FROM his_clinic_item where ID='"+id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sql);
			PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, HisReqBean.class);
			if((map!=null)&&(map.getList().size()>0)){
				hb= (HisReqBean)map.getList().get(0);			
			}
		}
		
		return hb;
	} 
	
	
	public static void main(String[] args)throws Exception {
		String result="{\"billItemsInfo\":\"null\",\"errorInfo\":\"找不到C0003858对应的价表项目\",\"billItemsNum\":\"\",\"status\":\"500\"}";
		TranLogTxt.liswriteEror_to_txt("333333", "res:" + result+"\r\n");
        if((result!=null)&&(result.trim().length()>0)){	   
				result = result.trim();
				JSONObject jsonobject = JSONObject.fromObject(result);
				Map classMap = new HashMap();
				classMap.put("billItemsInfo", HisResItemBean.class);
				HisResBean resdah = new HisResBean();
				resdah = (HisResBean) JSONObject.toBean(jsonobject, HisResBean.class, classMap);
				System.out.println(resdah.getStatus());
				if ("200".equals(resdah.getStatus())) {
					
				}
        }
	}
	
}
