package com.hjw.webService.client.nanhua;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.nanhua.bean.NHRequest;
import com.hjw.webService.client.nanhua.bean.NHResponse;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServices;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesLocator;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.client.nanhua
     * @Description: 2.14	南华-创星
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class FEESendMessageNH {

	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public FEESendMessageNH(FeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url,String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		try {
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+feeMessage.getREQ_NO()+":"+xml);
			try {
				String exam_num=this.feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM();
				ExamInfoUserDTO eu= this.getExamInfoForNum(exam_num,logname);
				
				double charge = 0.0;
				for(Fee f:this.feeMessage.getPROJECTS().getPROJECT()){
					charge += Double.parseDouble(f.getCHARGES());
				}
				NHRequest request = new NHRequest();
				request.getData().setTjno(exam_num);
				request.getData().setName(eu.getUser_name());
				request.getData().setSex(eu.getSex());
				request.getData().setAge(""+eu.getAge());
				request.getData().setZje(""+charge);
				String strRequest = JaxbUtil.convertToXml(request, true);
				TranLogTxt.liswriteEror_to_txt(logname, "传入参数:" + strRequest);
				
				TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
				GWI_TJJKServices gwiServices = new GWI_TJJKServicesLocator(url);
				GWI_TJJKServicesSoap_PortType gwi = gwiServices.getGWI_TJJKServicesSoap();
				String messages = gwi.doBlance(strRequest);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+feeMessage.getREQ_NO()+":"+messages);
				try {
					NHResponse response = JaxbUtil.converyToJavaBean(messages, NHResponse.class);
					if("0".equals(response.getHead().getResultCode())) {
						String hisno = response.getInfo().get(0).getHisno();
						ReqId req= new ReqId();
						req.setReq_id(feeMessage.getREQ_NO());
						req.setThird_req_id(hisno);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("发送收费申请成功！");
						rb.getControlActProcess().getList().add(req);
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(response.getHead().getErrorMsg());
					}
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("费用信息解析返回值错误");
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("费用信息调用webservice错误");
			}
		} catch (Throwable ex){
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		return rb;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times,a.arch_num,a.birthday,a.id_num ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "req:" +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
}