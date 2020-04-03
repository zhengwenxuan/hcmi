package com.hjw.webService.client.sxwn;


import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.sxwn.Bean.LISReport;
import com.hjw.webService.client.sxwn.Bean.LisResNewDataSet;
import com.hjw.webService.client.sxwn.client.LISWebService;
import com.hjw.webService.client.sxwn.client.LISWebServiceLocator;
import com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.service.CommService;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISResMessageSxWn{
   private CommService commService;
   
   
	@SuppressWarnings("resource")
	public LISResMessageSxWn(){
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname,String exam_num) {
		ResultLisBody rb = new ResultLisBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+ exam_num);    		
			try {
				LISWebService liswsl = new LISWebServiceLocator(url);
				LISWebServiceSoap_PortType lis = liswsl.getLISWebServiceSoap();
				String message = lis.LIS_FindReport("体检", exam_num);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+ message);	
				LisResNewDataSet lrb= new LisResNewDataSet();
				lrb = JaxbUtil.converyToJavaBean(message, LisResNewDataSet.class);
				TranLogTxt.liswriteEror_to_txt(logname,"res:1、");	
				if((lrb!=null)&&(lrb.getLISReport()!=null)&&(lrb.getLISReport().size()>0)){
					TranLogTxt.liswriteEror_to_txt(logname,"res:2、"+lrb.getLISReport().size());	
					for(LISReport lr:lrb.getLISReport()){
						ProcListResult plr = new ProcListResult();
						
						// H偏高、HH偏高报警、L偏低、LL偏低报警、P阳性、Q弱阳性、E错误，由LIS判断，仪器接口不用管
						// M就是正常
						// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
						if ("↑".equals(lr.getFlag())) {
							plr.setRef_indicator("1");
						} else if ("↓".equals(lr.getFlag())) {
							plr.setRef_indicator("2");
						} else if ("▲".equals(lr.getFlag())) {
							plr.setRef_indicator("4");
						} else if ("▼".equals(lr.getFlag())) {
							plr.setRef_indicator("4");
						} else {
							if(lr.getJG().indexOf("+")>=0){
								plr.setRef_indicator("3");
							}else if(lr.getJG().indexOf("阳性")>=0){
								plr.setRef_indicator("3");
							}else if(lr.getJG().indexOf("炎症")>=0){
								plr.setRef_indicator("3");
							}else{
								plr.setRef_indicator("0");
							}
						}
						plr.setBar_code("");
						plr.setExam_num(lr.getHZBM());
						plr.setLis_item_code(lr.getBBLXBM());
						plr.setLis_rep_item_code(lr.getXMBH()+"");
						plr.setExam_doctor(lr.getJyr());
						plr.setExam_date(lr.getCjgsj());
						plr.setExam_result(lr.getJG());
						plr.setRef_value(lr.getCKFW());
						plr.setItem_unit(lr.getDW());
						plr.setApprover(lr.getHdr());
						plr.setApprove_date(lr.getCjgsj());
						if((!StringUtil.isEmpty(plr.getExam_date()))&&(plr.getExam_date().trim().length()>0)){							
							plr.setExam_date(plr.getExam_date().replaceAll("T", " "));
							plr.setExam_date(plr.getExam_date().replaceAll("\\+", " "));
						}
						
						if((!StringUtil.isEmpty(plr.getApprove_date()))&&(plr.getApprove_date().trim().length()>0)){
							plr.setApprove_date(plr.getApprove_date().replaceAll("T", " "));
							plr.setApprove_date(plr.getApprove_date().replaceAll("\\+", " "));
						}
						TranLogTxt.liswriteEror_to_txt(logname,"res:3、");	
						WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
						CommService commService = (CommService) wac.getBean("commService");
						commService.doproc_Lis_result(plr);	
						TranLogTxt.liswriteEror_to_txt(logname,"res:4、");	
					}
				}
				TranLogTxt.liswriteEror_to_txt(logname,"res:5、");	
				rb.getResultHeader().setTypeCode("AA");

			} catch (Exception ex) {
				ex.printStackTrace();
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("lis调用webservice错误");
			}

		} catch (Exception ex){
			ex.printStackTrace();
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;			 
	}
		
}
