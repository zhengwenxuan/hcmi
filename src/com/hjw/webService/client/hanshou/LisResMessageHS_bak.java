package com.hjw.webService.client.hanshou;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class LisResMessageHS_bak extends ServletEndpointSupport{
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
    
	static {
	    init();
	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public String SendLabReport(String reqStr) {
		String logName ="LisRes";
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + reqStr);
		
		String ResMessage ="";
		
		try {
				LisResult lisResult =  getLisResXMLLisRes(reqStr,logName);
		    	ExamInfoUserDTO ei = new ExamInfoUserDTO();
		    	
				if(!"".equals(lisResult.getSample_barcode())) {
					ei = configService.getExamInfoForBarcode(lisResult.getSample_barcode());
					if ((ei == null) || (ei.getId() <= 0)) {

						ResMessage= "error-根据申请单号查无此人-ApplyNo：" + lisResult.getSample_barcode();
					} else if ("Z".equals(ei.getStatus())) {
						ResMessage = "error-此人已经总检，请先取消总检再回传结果";
					} else {
						
							
							boolean succ = this.configService.insert_lis_result(lisResult);
							if (succ) {
								ResMessage = "success-lis信息 入库成功";
							}else{
								ResMessage = "error-lis信息 入库错误";	
							}
						
						
					}
				} else {
					
					ResMessage = "入参中申请单号为空-ApplyNo";
				}
			
			
		} catch (Exception ex) {
			ResMessage = "error-lis信息-解析错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex);
		}
		
		TranLogTxt.liswriteEror_to_txt(logName, "入参中申请单号为空-ApplyNo"+ResMessage);
		return ResMessage;
	}

	private LisResult getLisResXMLLisRes(String xmlstr, String logName) {
		LisResult lisResult = new LisResult();
		
		try {
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
			
			lisResult.setTil_id(logName);
			lisResult.setExam_num(document.selectSingleNode("root/visitNo").getText());//体检号
			lisResult.setSample_barcode(document.selectSingleNode("root/visitNo").getText());//	条码号  申请单号
			lisResult.setDoctor(document.selectSingleNode("root/applyNos/applyNo").getText());//	检查医生
			
			lisResult.setExam_date(document.selectSingleNode("root/labTime").getText());//	检查时间
			
			lisResult.setSh_doctor(document.selectSingleNode("root/auditOperator").getText());//	审核医生
			lisResult.setLis_item_code(document.selectSingleNode("root/labDetail/masterItemCode").getText());//	LIS组合项目代码
			//private String lis_item_name="";//	LIS组合项目名称
			lisResult.setSeq_code(Integer.parseInt(document.selectSingleNode("root/labDetail/sortNo").getText()));////	顺序号
			lisResult.setReport_item_code(document.selectSingleNode("root/labDetail/itemCode").getText());//	LIS报告细项代码
			lisResult.setReport_item_name(document.selectSingleNode("root/labDetail/itemChiName").getText());//	LIS报告细项名称
			lisResult.setItem_result(document.selectSingleNode("root/labDetail/itemResult").getText());//	项目结果
			lisResult.setRef(document.selectSingleNode("root/labDetail").getText());//	参考范围  referenceHighLimit  referenceLowLimit
			lisResult.setItem_unit(document.selectSingleNode("root/labDetail/itemResultUnit").getText());//	项目单位
			
			//结果标识含义 ： H偏高、HH偏高报警、L偏低、LL偏低报警、P阳性、Q弱阳性、E错误，由LIS判断，仪器接口不用管
			//结果值标志（N正常L偏低H偏高） 汉寿
			lisResult.setFlag(document.selectSingleNode("root/labDetail/itemResultFlag").getText());//	高低标志	H-高 L-低N-正常HH-偏高报警LL-偏低报警C-危急  
			
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		return lisResult;
	}

	
}
