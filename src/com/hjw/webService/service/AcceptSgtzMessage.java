package com.hjw.webService.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.service.bean.SgtZInMsg;
import com.hjw.webService.service.bean.SgtZMsg;
import com.hjw.webService.service.bean.SgtZRequestInMsg;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CommService;

import net.sf.json.JSONArray;

public class AcceptSgtzMessage extends ServletEndpointSupport {
	
	private CommService commService;

	protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
	}

   /**
    * 
    * @param type
    * @param msg
    * @return
    */
   public String doServer(String type,String msg)throws Exception{	   
	   TranLogTxt.liswriteEror_to_txt("sgtzlog","req:"+type);	
	   if("VPGST_BJRQCTKJYXGS_PATIENTINFO".equals(type)){//查询	
		   TranLogTxt.liswriteEror_to_txt("sgtzlog","req:"+msg);		   
		   SgtZInMsg sgtz=new SgtZInMsg();
		   if(!StringUtil.isEmpty(msg)){
		     ExamInfoUserDTO ei= new ExamInfoUserDTO();
		     ei=this.commService.getCustExamInfoForNum(msg);
		     if((ei!=null)&&(ei.getId()>0)){
		    	 sgtz.setName(ei.getUser_name()); 
		    	 sgtz.setGender(ei.getSex());
		    	 sgtz.setScanID(ei.getExam_num());
		    	 sgtz.setBirthday(ei.getBirthday());
		    	 sgtz.setPatientID(ei.getExam_num());
		     }
		   }
		    ObjectMapper mapper = new ObjectMapper();   
	        String json=mapper.writeValueAsString(sgtz);  
	        TranLogTxt.liswriteEror_to_txt("sgtzlog","res:"+json);	
		   return json;
		} else if ("VPGST_BJRQCTKJYXGS_HWREQUEST".equals(type)) {// 入账
			TranLogTxt.liswriteEror_to_txt("sgtzlog", "req:" + msg);
			/*
			 * if((!StringUtil.isEmpty(msg))&&(msg.length()>10)){
			 * msg=msg.substring(1,msg.length()-1); }
			 */
			SgtZMsg resmsg = new SgtZMsg();
			try {
				msg = msg.replaceAll("\\\\", "");
				SgtZRequestInMsg sgtz = new SgtZRequestInMsg();
				ObjectMapper mapper = new ObjectMapper();
				sgtz = mapper.readValue(msg, SgtZRequestInMsg.class);
				resmsg = this.commService.sgtzImpdo(sgtz);
			} catch (Exception ex) {
				resmsg.setStatus("ERROR");
				resmsg.setMsg(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(resmsg);
			TranLogTxt.liswriteEror_to_txt("sgtzlog", "res:" + json);
			return json;
		}else{
		  return null; 
	   }	 
   }
 
}
