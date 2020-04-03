package com.hjw.webService.client.tj180;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ExamPicMessage;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.tj180.Bean.LisGetReqBean;
import com.hjw.webService.client.tj180.Bean.LisGetResStatusBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISResStatusMessageTj180{
   private static JdbcQueryManager jdbcQueryManager;
   static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname, String exam_num,long exam_id) {
		ResultLisBody rb = new ResultLisBody();
		Connection tjtmpconnect = null;
		TranLogTxt.liswriteEror_to_txt(logname, "req: examNum=" + exam_num+" exam_id="+exam_id);
		if(exam_id<=0){
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu = this.getExamInfoForNum(exam_num);
			exam_id=eu.getId();
		}
		
				
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String sb1 = "  select a.exam_info_id,a.req_id,a.lis_item_id,b.id as sampleid from zl_req_item a,"
						+ "sample_exam_detail b,charging_item c where a.lis_req_code=b.sample_barcode "
						+ "and b.status='W' "
						+ " and a.exam_info_id='"+exam_id+"'"
						+ "and a.exam_info_id=b.exam_info_id "
						+ "and c.id=a.charging_item_id "
						+ "and c.his_num=a.lis_item_id ";
				TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
				while (rs1.next()) {
					String req_id =rs1.getString("req_id");
					long sampleid= rs1.getLong("sampleid");
					try {
						LisGetReqBean p = new LisGetReqBean();
						p.setTestNo(req_id);
						JSONObject json = JSONObject.fromObject(p);// 将java对象转换为json对象
						String str = json.toString();// 将json对象转换为字符串
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
						String result = HttpUtil.doPost(url,p,"utf-8");						
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
			            if((result!=null)&&(result.trim().length()>0)){	   
								result = result.trim();
								JSONObject jsonobject = JSONObject.fromObject(result);
								LisGetResStatusBean resdah = new LisGetResStatusBean();
								resdah = (LisGetResStatusBean) JSONObject.toBean(jsonobject, LisGetResStatusBean.class);
								if ("200".equals(resdah.getStatus())) {
									if("1".equals(resdah.getLisState())){
									String updatesql="update sample_exam_detail set status='Y' where id='"+sampleid+"'";
									TranLogTxt.liswriteEror_to_txt(logname, "res:" + updatesql+"\r\n");
									tjtmpconnect.createStatement().execute(updatesql);		
									}
								}
							}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} 
				rs1.close();
			} catch (SQLException ex) {
				TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
			} finally {
				try {
					if (tjtmpconnect != null) {
						tjtmpconnect.close();
					}
				} catch (SQLException sqle4) {
					sqle4.printStackTrace();
				}
			}
		rb.getResultHeader().setTypeCode("AA");
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
					+ ",c.register_date,c.join_date,c.exam_times ");
			sb.append(" from exam_info c ");
			sb.append(" where c.is_Active='Y' ");		
			sb.append(" and c.exam_num = '" + exam_num + "' ");	
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			return eu;
		} 
}
