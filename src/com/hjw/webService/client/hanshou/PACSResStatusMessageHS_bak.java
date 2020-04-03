package com.hjw.webService.client.hanshou;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.hokai.bean.ResLisStatusBeanHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSResStatusMessageHS_bak extends ServletEndpointSupport{
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

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public String getMessage(String strbody) {
		String logname="PacsStatus";
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + strbody);
		ResLisStatusBeanHK rb= new ResLisStatusBeanHK();
		rb=getreqNo(strbody);
			if("AA".equals(rb.getCode())){
				ExamInfoUserDTO ei = ei=this.getExamInfoForNum(rb.getPersionid());
				String statuss = rb.getStatus();
				List<String> req_nums = new ArrayList<>();
				req_nums.add(rb.getReqno());
				String samstatus="W";
					//操作代码和名称：分配条码(备管)(code=10)，采集(code=20),
					//1 条码已打印，11 条码已取消，2已采样，22取消采样，3标本送出，33取消送出，4已核收，44取消核收，5已检验，55取消检验，6已报告，66取消报告，7 已审核, 77取消审核，8 报告已打印,9 已作废
	                 
					if(("1".equals(statuss))||("2".equals(statuss))||("3".equals(statuss))||("4".equals(statuss))||("5".equals(statuss)) || ("6".equals(statuss)) || ("7".equals(statuss)) ||("8".equals(statuss))){//核收
						statuss="C";
						samstatus="H";
						this.configService.setExamInfoChargeItemLisStatus(req_nums, ei.getExam_num(),statuss,samstatus);
					} else if(("9".equals(statuss))) {
						if("Z".equals(ei.getStatus())) {
							rb.setCode("AE");
							rb.setText("体检中心已总检，不可撤销");
						}
					}
			}
		
			
		return rb.getCode();
	}
		
	private ResLisStatusBeanHK getreqNo(String xmlstr){
		ResLisStatusBeanHK reqno= new ResLisStatusBeanHK();
		
		try{
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			reqno.setPersionid(document.selectSingleNode("root/patientId").getText());//患者id
			reqno.setMessageId(document.selectSingleNode("root/visitNo").getText());//体检号
			reqno.setReqno(document.selectSingleNode("root/applyNos/applyNo").getText());//申请单号
			reqno.setStatus(document.selectSingleNode("root/applyStatus").getText());//申请单状态
			
			reqno.setCode("AA");
		}catch(Exception ex){
				reqno.setCode("AE");
				reqno.setText("xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return reqno;
		
	}
	
		/**
		 * 
		 * @param url
		 * @param view_num
		 * @return
		 */
		public ExamInfoUserDTO getExamInfoForNum(String exam_info_id) throws ServiceException {
			StringBuffer sb = new StringBuffer();
			sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
					+ ",c.register_date,c.join_date,c.exam_times ");
			sb.append(" from exam_info c ");
			sb.append(" where c.is_Active='Y' and c.status != 'Z' ");		
			sb.append(" and c.patient_id = '" + exam_info_id + "' order by c.create_time desc ");	
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			return eu;
		} 
}
