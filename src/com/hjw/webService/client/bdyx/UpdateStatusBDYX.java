package com.hjw.webService.client.bdyx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bdyx.bean.status.OrderStatusInf;
import com.hjw.webService.client.bdyx.bean.status.StatusReq;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class UpdateStatusBDYX {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
    public ResultHeader getMessage(String body, String logname){
    	
    	ResultHeader rb = new ResultHeader();
    	StatusReq statusReq = new Gson().fromJson(body, StatusReq.class);
    	try {
	    	for(OrderStatusInf orderStatusInf : statusReq.getOrderStatusInf()) {
	    		if("50".equals(orderStatusInf.getOrderStatus()) && "检验类".equals(orderStatusInf.getOrderTypeName())) {//标本已签收
	    			String barcode = orderStatusInf.getOrderLid().split("-")[0];
	    			List<String> req_nums = new ArrayList<>();
	    			req_nums.add(barcode);
	    			ExamInfoUserDTO eu = configService.getExamInfoForBarcode(barcode);
	    			configService.setExamInfoChargeItemLisStatus(req_nums, eu.getExam_num(), "C", "H");
	    			rb.setTypeCode("AA");
	    			rb.setText("修改状态成功");
	    		} else if("".equals(orderStatusInf.getOrderStatus()) && "检查类".equals(orderStatusInf.getOrderTypeName())) {//
	    			String req_num = orderStatusInf.getOrderLid();
	    			configService.setExamInfoChargeItemPacsStatus(req_num, "C");
	    			rb.setTypeCode("AA");
	    			rb.setText("修改状态成功");
	    		}
	    	}
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		rb.setTypeCode("AE");
    		rb.setText("修改状态失败-Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
    	}
		String xml = JaxbUtil.convertToXmlWithOutHead(rb, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"res:"+xml);
    	return rb;    	
    }
    
}
