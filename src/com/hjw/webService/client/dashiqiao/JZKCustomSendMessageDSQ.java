package com.hjw.webService.client.dashiqiao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.JzkCustom;
import com.hjw.webService.client.body.YbCustomMessage;
import com.hjw.webService.client.dashiqiao.ResCusBean.CustomerExam;
import com.hjw.webService.client.dashiqiao.ResCusBean.Entry;
import com.hjw.webService.client.dashiqiao.ResCusBean.ResCustomBeanDSQ;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.21	就诊卡查询服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class JZKCustomSendMessageDSQ {

	private YbCustomMessage custom;
	
	public JZKCustomSendMessageDSQ(YbCustomMessage custom){
		this.custom=custom;
	}  

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public JzkCustom getMessage(String url,String logname) {
		JzkCustom rb = new JzkCustom();
		String xml = "";
		try {
			this.custom.setMSG_TYPE("TJ608");
			xml = JaxbUtil.convertToXml(this.custom, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+this.custom.getMC_NO());
			ResCustomBeanDSQ rcb = this.searchPatid(url, logname,this.custom.getMC_NO());
			if(rcb.getCode().equals("1")){
				rb.setPATIENT_ID(rcb.getPersionid());
				rb.setNAME(rcb.getCustomername());  
				rb.setSEX(rcb.getSex());
				rb.setDATE_OF_BIRTH(rcb.getCustoemrbirthDate());
				rb.setRESULT("1");//返回成功为0，失败为失败信息
			}/*else if(rcb.getCode().equals("0")){
				rb.setPATIENT_ID(this.custom.getMC_NO());
				rb.setRESULT("0");
			}*/else{
				rb.setPATIENT_ID(this.custom.getMC_NO());
				rb.setRESULT("AE");
			}

		} catch (Exception ex){
			rb.setRESULT("3");
		}
		return rb;
	}
private ResCustomBeanDSQ searchPatid(String url,String logname, String Patid){	
		
		ResCustomBeanDSQ rcb = new ResCustomBeanDSQ();
		url +=Patid;
		TranLogTxt.liswriteEror_to_txt(logname, "res--url地址:" + url + "\r\n");
		try {
			//体检发送his就诊卡号  查询人员信息是否存在
			String result = HttpUtil.doGet(url,"utf-8");
			
			TranLogTxt.liswriteEror_to_txt(logname, "res查询到的json数据:" + result + "\r\n");
			CustomerExam customerexam = new Gson().fromJson(result, CustomerExam.class);

			String persionid="";	
			String customername="";
			String customersex="";
			String custoemrbirthDate="";
			

				List<Entry> entry = customerexam.getEntry();
				for (int i = 0; i < entry.size(); i++) {
				 if(entry.get(i).getResource().getResourceType().equals("Patient")){
						 customername = entry.get(i).getResource().getName().get(0).getText();
						
						 customersex="女";
						if(entry.get(i).getResource().getGender().equals("male")){
							 customersex = "男";
						}else{
							 customersex = "女";
						}
						
						 custoemrbirthDate = entry.get(i).getResource().getBirthDate();
					}
				}
				
				System.err.println("查询患者信息成功");
				rcb.setCode("1");
				rcb.setPersionid(Patid);
				rcb.setCustomername(customername);
				rcb.setSex(customersex);
				rcb.setCustoemrbirthDate(custoemrbirthDate);
				
		} catch (Exception e) {
			rcb.setCode("AE");
			e.printStackTrace();
		}
		
		
		  return rcb;
	}

}
