package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.StringUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ExamUpdateMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.ExamUpdateResBean;
import com.hjw.webService.client.tj180.Bean.PersonReserveBean;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.util.DateTimeUtil;

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
public class ExamUpdateSendMessageTJ180 {

	private ExamUpdateMessageBody feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	   static{
	   	init();
	   	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}
	public ExamUpdateSendMessageTJ180(ExamUpdateMessageBody feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage(String url, String logname) {
		ResultHeader rb = new ResultHeader();
		try {
			JSONObject json = JSONObject.fromObject(feeMessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		    if(feeMessage.getExam_id()>0){
				PersonReserveBean hbrb =this.getExamInfoForId(feeMessage.getExam_id(),logname);
				if ((hbrb.getReserveId() == null) || (hbrb.getReserveId().trim().length() <= 0)) {
					rb.setTypeCode("AE");
					rb.setText("无效体检编号");
				} else {
					json = JSONObject.fromObject(hbrb);// 将java对象转换为json对象
					str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
					
					String result = HttpUtil.doPost(url,hbrb,"utf-8");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
		            if((result!=null)&&(result.trim().length()>0)){
						result = result.trim();
						JSONObject jsonobject = JSONObject.fromObject(result);
						ExamUpdateResBean resdah = new ExamUpdateResBean();
						resdah = (ExamUpdateResBean) JSONObject.toBean(jsonobject, ExamUpdateResBean.class);
						if ("200".equals(resdah.getStatus())) {						
							rb.setText("");	
							rb.setTypeCode("AA");
						}else{
							rb.setTypeCode("AE");
							rb.setText(resdah.getErrorinfo());	
						}
		            }else{
						rb.setTypeCode("AE");
						rb.setText("系统返回错误");
		            }
				}
		    }else{
		    	rb.setTypeCode("AE");
				rb.setText("无效体检编号");	
		    }
		} catch (Exception ex) {
			rb.setTypeCode("AE");
			rb.setText("处理错误");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb).toString());
		return rb;
	}

	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public PersonReserveBean getExamInfoForId(long examid,String logname) throws ServiceException {
		PersonReserveBean pb= new PersonReserveBean();		
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
			StringBuffer sb = new StringBuffer();
			sb.append("select a.exam_num,a.batch_id,a.group_id,a.company_id,a.status,");
			sb.append("a.exam_type,CONVERT(varchar(50),a.join_date,20) as join_date,CONVERT(varchar(50),a.create_time,20) as create_time,b.arch_num,");
			sb.append("c.batch_num,c.batch_name,d.group_num,d.group_name,e.com_num,a.remarke,");
			sb.append("e.com_name,f.chi_name,f.log_name as work_num,g.chi_name as kaidanren,a.introducer, ");
			sb.append("(select sum(g.personal_pay+g.team_pay) as amt from examinfo_charging_item g where g.examinfo_id=a.id and g.isActive='Y' and g.pay_status<>'M') as amt");
			sb.append(" from exam_info a ");
			sb.append("left join  batch c on c.id=a.batch_id ");
			sb.append("left join group_info d on d.id=a.group_id ");
			sb.append("left join company_info e on e.id=a.company_id ");
			sb.append("left join user_usr f on f.id=a.creater ");
			sb.append("left join user_usr g on g.id=a.kaidanren ");
			sb.append(",customer_info b where a.customer_id=b.id ");
			sb.append(" and a.id='"+examid+"' and a.is_Active='Y'");// and a.counter_check <> 'B' ");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs1.next()) {
				pb.setCustomerPatientId(rs1.getString("arch_num"));
				pb.setEditDate(rs1.getString("create_time"));
				pb.setEditor(rs1.getString("work_num"));
				if("Y".equals(rs1.getString("status"))){//预约
				  pb.setEnabledFlag("9");//体检状态 未体检：9，体检中：1  
				}else{
				  pb.setEnabledFlag("1");
				}
                pb.setExamineDate(rs1.getString("join_date")); 
                if((pb.getExamineDate()==null)||(pb.getExamineDate().trim().length()<=0)){
                	 pb.setExamineDate(DateTimeUtil.getDateTime());
                }
                pb.setGroupId(rs1.getString("group_num"));
                pb.setGroupName(rs1.getString("group_name"));
                pb.setIncomeFlag("4");//收费状态 未收费：0，团队记账：4
                pb.setMemo(rs1.getString("remarke"));
                pb.setOrderedByDoctor(rs1.getString("kaidanren"));
                pb.setOrganizationId(rs1.getString("com_num"));
                pb.setOrganizationName(rs1.getString("com_name"));
                pb.setOrgReserveId(rs1.getString("batch_num"));
                pb.setRecommend(rs1.getString("introducer"));//介绍人
                pb.setRecommendTTM("");//TTM介绍人
                
                if((pb.getRecommend()!=null)&&(pb.getRecommend().trim().length()>4)){
                	pb.setRecommend(StringUtil.subTextString(pb.getRecommend().trim(),4));
    			}else if((pb.getRecommend()!=null)&&(pb.getRecommend().trim().length()==0)){
    				pb.setRecommend("");
    			}	
                
                pb.setReserveId(rs1.getString("exam_num"));
                if("T".equals(rs1.getString("exam_type"))){
                	pb.setReserveType("O");
                }else if("G".equals(rs1.getString("exam_type"))){
                	pb.setReserveType("P");
                }
                pb.setTotalCost(rs1.getDouble("amt")+"");
			}
			rs1.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		pb = getZGTJ(examid,pb,logname);
		return pb;
	}
	
	public PersonReserveBean getZGTJ(long examid,PersonReserveBean pb,String logname) throws ServiceException {	
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
			StringBuffer sb = new StringBuffer();
			sb.append("select e.id,e.exam_type,e.isshowtable,e.isaffixedcode,d.data_code_children"
					+ " from exam_info e,data_dictionary d where  d.id = e.customer_type and e.id='"+examid+"'");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs1.next()) {
				if("ZGTJ".equals(rs1.getString("data_code_children"))){
					if(rs1.getInt("isaffixedcode")==0){
						pb.setMemo("|招工|");
					}else if(rs1.getInt("isaffixedcode")==1){
						pb.setMemo("|招工|贴|");
					}
				}
                if("T".equals(rs1.getString("exam_type"))){
                	if(rs1.getInt("isaffixedcode")==1){
                		pb.setExamineFlag("7");
                	}else{
                		pb.setExamineFlag("3");
                	}
                }else{
                	pb.setExamineFlag("3");
                }
               
			}
			rs1.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return pb;
	}
}
