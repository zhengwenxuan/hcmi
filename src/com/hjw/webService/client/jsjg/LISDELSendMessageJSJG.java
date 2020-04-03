package com.hjw.webService.client.jsjg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.client.jsjg
     * @Description: 2.7	江苏省省级机关医院-柯林布瑞-HSB平台-LIS接口
     * @author: zwx
     * @version V2.0.0.0
 */
public class LISDELSendMessageJSJG{
	private LisMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public LISDELSendMessageJSJG(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	public ResultLisBody getMessage(String url, String logname, boolean debug) {
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------开始lis撤销申请--------------------");
		ResultLisBody rb = new ResultLisBody();
		try {
			String jsonString = JSONSerializer.toJSON(lismessage).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
			ExamInfoUserDTO eu= this.getExamInfoForNum(this.lismessage.getCustom().getExam_num(),logname);

			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			List<String> errList = new ArrayList<String>();
			for (LisComponents lcs : this.lismessage.getComponents()) {
				//检查已采样不可撤销
				if(checkSample(eu.getId(), lcs.getReq_no())) {
					ApplyNOBean ap = new ApplyNOBean();
					ap.setApplyNO(lcs.getReq_no());
					ap.setLis_id(lcs.getLis_id());
					ap.setBarcode(lcs.getReq_no());
					list.add(ap);
				} else {
					errList.add(lcs.getReq_no());
				}
			}
			if(!list.isEmpty()) {
				rb.getResultHeader().setTypeCode("AA");
				rb.getControlActProcess().setList(list);
				if(!errList.isEmpty()) {
					rb.getResultHeader().setText("条码号【"+errList.toString()+"】已采样，不可撤销LIS申请");
					TranLogTxt.liswriteEror_to_txt(logname,"条码号【"+errList.toString()+"】已采样，不可撤销LIS申请");
				}
			} else {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("条码号【"+errList.toString()+"】已采样，不可撤销LIS申请");
				TranLogTxt.liswriteEror_to_txt(logname,"条码号【"+errList.toString()+"】已采样，不可撤销LIS申请");
			}
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		TranLogTxt.liswriteEror_to_txt(logname, "-----------------结束lis撤销申请--------------------");
		return rb;
	}
	
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times,c.phone,c.company,c.address,a.arch_num,a.birthday,a.id_num,ci.com_phone as remark1 ");
		sb.append(" from customer_info a,exam_info c ");
		sb.append(" left join company_info ci on ci.id = c.company_id ");
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
	
	private boolean checkSample(long examid, String sample_barcode) throws ServiceException {
		boolean ret = false;
		String sql = "select * from sample_exam_detail where exam_info_id=" + examid + " and sample_barcode='" + sample_barcode + "' and status = 'W'";
		System.out.println(sql);
		RowSet rowSet = this.jdbcQueryManager.getRowSet(sql);
		if(ret) {
			try {
				ret = rowSet.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
}
