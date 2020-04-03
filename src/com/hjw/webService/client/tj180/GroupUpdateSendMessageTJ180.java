package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.ExamUpdateResBean;
import com.hjw.webService.client.tj180.Bean.GroupItemMessageBeanTJ180;
import com.hjw.webService.client.tj180.Bean.GroupMessageBeanTJ180;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

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
public class GroupUpdateSendMessageTJ180 {
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
	public ResultHeader getMessage(String url,long groupid,String logname) {
		ResultHeader rb = new ResultHeader();
		try {
			JSONObject json = JSONObject.fromObject(groupid);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		    if(groupid>0){
		    	 GroupMessageBeanTJ180 hbrb= new GroupMessageBeanTJ180();
		    	 hbrb=getGroupForId(groupid,logname);
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
	
	public ResultHeader getMessageByBatch(String url,long batchid,String logname) {
		ResultHeader rb = new ResultHeader();
		try {
			JSONObject json = JSONObject.fromObject(batchid);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		    if(batchid>0){
		    	 List<GroupMessageBeanTJ180> hbrbList= new ArrayList<GroupMessageBeanTJ180>();
		    	 hbrbList=this.getGroupForBatchId(batchid,logname);
		    for(GroupMessageBeanTJ180 hbrb:hbrbList){
		    	try{
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
		    	}catch(Exception ex){
		    		rb.setTypeCode("AE");
					rb.setText("处理错误");
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
	public GroupMessageBeanTJ180 getGroupForId(long groupid,String logname) throws ServiceException {
		GroupMessageBeanTJ180 pb= new GroupMessageBeanTJ180();		
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
			StringBuffer sb = new StringBuffer();
			sb.append("select c.com_num,c.com_name,b.batch_num,b.batch_name,b.is_showamount,b.is_showseal,a.group_num,"
					+ "a.group_name,CAST(a.amount as decimal(18,2)) as amount,c.contact_name,c.contact_phone,c.keShi_Name,"
					+ "c.email,c.address,c.remark,"
					+ "a.creater,(CONVERT(varchar(50),a.create_time,20)) as create_time,d.work_num,a.sex,a.is_Marriage "
					+ "from group_info a "
					+ "left join user_usr d on d.id=a.creater ,batch b,company_info c"
					+ " where a.id='"+groupid+"' and a.batch_id=b.id  and b.company_id=c.id  and a.isActive='Y'");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs.next()) {
				pb.setEditDate(rs.getString("create_time"));
				pb.setEditor(rs.getString("work_num"));
				pb.setMemo(rs.getString("remark"));
				pb.setRelativeName(rs.getString("contact_name"));
				pb.setRelativePhone(rs.getString("contact_phone"));
				pb.setOrgAddress(rs.getString("address"));
				pb.setDeptManager(rs.getString("keShi_Name"));
				pb.setOrgEmail(rs.getString("email"));

				if((rs.getString("remark")!=null)&&(rs.getString("remark").trim().length()>0)){
					//pb.setMemo(StringUtil.subTextString(rs.getString("remark").trim(),48));
				}else if((rs.getString("remark")!=null)&&(rs.getString("remark").trim().length()==0)){
					//pb.setMemo("");
				}	
				
				if((rs.getString("address")!=null)&&(rs.getString("address").trim().length()>0)){
					//pb.setOrgAddress(StringUtil.subTextString(rs.getString("address").trim(),48));
				}else if((rs.getString("address")!=null)&&(rs.getString("address").trim().length()==0)){
					//pb.setOrgAddress("");
				}			

				if((rs.getString("sex")==null)||(rs.getString("sex").trim().length()<=0)){
					pb.setFitSex("0");
				}else if("男".equals(rs.getString("sex"))){
					pb.setFitSex("1");
				}else if("女".equals(rs.getString("sex"))){
					pb.setFitSex("2");
				}else{
					pb.setFitSex("0");
				}
				//0-未婚，1-已婚，2-未知
				if((rs.getString("is_Marriage")==null)||(rs.getString("is_Marriage").trim().length()<=0)){
					pb.setFitWedded("2");
				}else if(rs.getString("is_Marriage").indexOf("已婚")>=0){
					pb.setFitWedded("0");
				}else if(rs.getString("is_Marriage").indexOf("未婚")>=0){
					pb.setFitWedded("1");
				}else{
					pb.setFitWedded("2");
				}
				
                pb.setGroupId(rs.getString("group_num"));
                pb.setGroupName(rs.getString("group_name"));              
                pb.setOrganizationId(rs.getString("com_num"));
                pb.setOrganizationName(rs.getString("com_name"));
                pb.setOrgReserveId(rs.getString("batch_num"));
                pb.setTotalCosts(rs.getString("amount"));
                pb.setPriceflag(rs.getInt("is_showamount")+"");
                pb.setStampFlag(rs.getInt("is_showseal")+"");
                List<GroupItemMessageBeanTJ180> groupsInfo= new ArrayList<GroupItemMessageBeanTJ180>();//	unionProjectId	体检项目编码
                sb = new StringBuffer();
    			sb.append(" select b.item_code,CAST(a.amount as decimal(18,2)) as amount from group_charging_item a,charging_item b"
    					+ " where a.charge_item_id=b.id and a.isActive='Y' and a.group_id='"+groupid+"'");
    			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
    			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
    			while (rs1.next()) {
    				GroupItemMessageBeanTJ180 gm=new GroupItemMessageBeanTJ180();
    				gm.setUnionProjectId(rs1.getString("item_code"));
    				gm.setPrice(rs1.getString("amount"));
    				groupsInfo.add(gm);
    			}
                rs1.close();
                pb.setGroupsInfo(groupsInfo);
			}
			rs.close();
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
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public List<GroupMessageBeanTJ180> getGroupForBatchId(long batchid,String logname) throws ServiceException {
		List<GroupMessageBeanTJ180> pbList= new ArrayList<GroupMessageBeanTJ180>();		
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
			StringBuffer sb = new StringBuffer();
			sb.append("select c.com_num,c.com_name,b.batch_num,b.batch_name,b.is_showamount,b.is_showseal,a.group_num,"
					+ "a.group_name,CAST(a.amount as decimal(18,2)) as amount,c.contact_name,c.contact_phone,c.keShi_Name,"
					+ "c.email,c.address,c.remark,"
					+ "a.creater,(CONVERT(varchar(50),a.create_time,20)) as create_time,d.work_num,a.sex,a.is_Marriage,a.id "
					+ "from group_info a "
					+ "left join user_usr d on d.id=a.creater ,batch b,company_info c"
					+ " where b.id='"+batchid+"' and a.batch_id=b.id  and b.company_id=c.id and b.is_active='Y' and a.isActive='Y'");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb.toString());
			while (rs.next()) {
				GroupMessageBeanTJ180 pb = new GroupMessageBeanTJ180();
				pb.setEditDate(rs.getString("create_time"));
				pb.setEditor(rs.getString("work_num"));
				pb.setMemo(rs.getString("remark"));
				pb.setRelativeName(rs.getString("contact_name"));
				pb.setRelativePhone(rs.getString("contact_phone"));
				pb.setOrgAddress(rs.getString("address"));
				pb.setDeptManager(rs.getString("keShi_Name"));
				pb.setOrgEmail(rs.getString("email"));

				if((rs.getString("remark")!=null)&&(rs.getString("remark").trim().length()>0)){
					//pb.setMemo(StringUtil.subTextString(rs.getString("remark").trim(),48));
				}else if((rs.getString("remark")!=null)&&(rs.getString("remark").trim().length()==0)){
					//pb.setMemo("");
				}	
				
				if((rs.getString("address")!=null)&&(rs.getString("address").trim().length()>0)){
					//pb.setOrgAddress(StringUtil.subTextString(rs.getString("address").trim(),48));
				}else if((rs.getString("address")!=null)&&(rs.getString("address").trim().length()==0)){
					//pb.setOrgAddress("");
				}			

				if((rs.getString("sex")==null)||(rs.getString("sex").trim().length()<=0)){
					pb.setFitSex("0");
				}else if("男".equals(rs.getString("sex"))){
					pb.setFitSex("1");
				}else if("女".equals(rs.getString("sex"))){
					pb.setFitSex("2");
				}else{
					pb.setFitSex("0");
				}
				//0-未婚，1-已婚，2-未知
				if((rs.getString("is_Marriage")==null)||(rs.getString("is_Marriage").trim().length()<=0)){
					pb.setFitWedded("2");
				}else if(rs.getString("is_Marriage").indexOf("已婚")>=0){
					pb.setFitWedded("0");
				}else if(rs.getString("is_Marriage").indexOf("未婚")>=0){
					pb.setFitWedded("1");
				}else{
					pb.setFitWedded("2");
				}
				
                pb.setGroupId(rs.getString("group_num"));
                pb.setGroupName(rs.getString("group_name"));              
                pb.setOrganizationId(rs.getString("com_num"));
                pb.setOrganizationName(rs.getString("com_name"));
                pb.setOrgReserveId(rs.getString("batch_num"));
                pb.setTotalCosts(rs.getString("amount"));
                pb.setPriceflag(rs.getInt("is_showamount")+"");
                pb.setStampFlag(rs.getInt("is_showseal")+"");
                long groupid=rs.getLong("id");
                List<GroupItemMessageBeanTJ180> groupsInfo= new ArrayList<GroupItemMessageBeanTJ180>();//	unionProjectId	体检项目编码
                sb = new StringBuffer();
    			sb.append(" select b.item_code,CAST(a.amount as decimal(18,2)) as amount from group_charging_item a,charging_item b"
    					+ " where a.charge_item_id=b.id and a.isActive='Y' and a.group_id='"+groupid+"'");
    			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
    			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
    			while (rs1.next()) {
    				GroupItemMessageBeanTJ180 gm=new GroupItemMessageBeanTJ180();
    				gm.setUnionProjectId(rs1.getString("item_code"));
    				gm.setPrice(rs1.getString("amount"));
    				groupsInfo.add(gm);
    			}
                rs1.close();
                pb.setGroupsInfo(groupsInfo);                
                pbList.add(pb);
			}
			rs.close();
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
		return pbList;
	}
}
