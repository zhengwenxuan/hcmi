package com.hjw.webService.client.tj180;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ExamItemUpdateMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.ExamItemUpdateResBean;
import com.hjw.webService.client.tj180.Bean.HisReqBean;
import com.hjw.webService.client.tj180.Bean.ItemReserveBean;
import com.hjw.webService.client.tj180.Bean.ItemReserveReqBean;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

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
public class ExamItemUpdateSendMessageTJ180 {

	private ExamItemUpdateMessageBody feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	   static{
	   	init();
	   	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}
	public ExamItemUpdateSendMessageTJ180(ExamItemUpdateMessageBody feeMessage){
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
			if (feeMessage.getExam_id() > 0) {
				ItemReserveReqBean hbrb = new ItemReserveReqBean();
				hbrb.setReserveId(this.getExam_numForId(feeMessage.getExam_id(), logname));
				if ((hbrb.getReserveId() == null) || (hbrb.getReserveId().trim().length() <= 0)) {
					rb.setTypeCode("AE");
					rb.setText("无效体检编号");
				} else {	
					ItemReserveReqBean pb= new ItemReserveReqBean();		
					pb = this.getItemListForId(feeMessage.getExam_id(), logname);
					hbrb.setItemsNum(pb.getItemsNum());
					hbrb.setItemsInfo(pb.getItemsInfo());
					json = JSONObject.fromObject(hbrb);// 将java对象转换为json对象
					str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
					String result = HttpUtil.doPost(url, hbrb, "utf-8");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
					if ((result != null) && (result.trim().length() > 0)) {
						result = result.trim();
						JSONObject jsonobject = JSONObject.fromObject(result);
						ExamItemUpdateResBean resdah = new ExamItemUpdateResBean();
						resdah = (ExamItemUpdateResBean) JSONObject.toBean(jsonobject, ExamItemUpdateResBean.class);
						if ("200".equals(resdah.getStatus())) {
							rb.setText("");
							rb.setTypeCode("AA");
						} else {
							rb.setTypeCode("AE");
							rb.setText(resdah.getErrorinfo());
						}
					} else {
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
	public String  getExam_numForId(long examid,String logname) throws ServiceException {
		String exam_num="";		
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
			StringBuffer sb = new StringBuffer();
			sb.append("select a.exam_num");
			sb.append(" from exam_info a ");			 
			sb.append(" where a.id='"+examid+"' and a.is_Active='Y'");// and a.counter_check <> 'B' ");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs1.next()) {
				exam_num=rs1.getString("exam_num");               
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
		return exam_num;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ItemReserveReqBean getItemListForId(long examid,String logname) throws ServiceException {
		ItemReserveReqBean pb= new ItemReserveReqBean();		
		Connection tjtmpconnect = null;
		List<ItemReserveBean> itemsInfo=new ArrayList<ItemReserveBean>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
			StringBuffer sb = new StringBuffer();
			sb.append("select a.exam_indicator,CAST(a.amount as decimal(18,2)) as amount,a.creater,CONVERT(varchar(50),a.create_time,20) as create_time,a.is_application,");
			sb.append("b.id as chargitem_id,b.item_code,b.item_name,b.dep_id,b.his_num,b.hiscodeClass,b.dep_category,b.interface_flag,b.item_note, ");
			sb.append("b.sex,b.examClass,b.examSubClass,b.perform_dept,b.item_seq,c.dep_num,c.dep_name,c.dep_address,c.seq_code,d.chi_name,d.work_num");
			sb.append(" from examinfo_charging_item a");
			sb.append("  left join user_usr d on d.id=a.creater");
			sb.append(" ,charging_item b");
			sb.append(" left join department_dep c on c.id=b.dep_id");
			sb.append(" where a.charge_item_id=b.id");
			sb.append(" and a.examinfo_id='"+examid+"' and a.isActive='Y'");
			sb.append(" and a.pay_status<>'M'");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb.toString());
			int itemnum=0;
			while (rs.next()) {
				itemnum=itemnum+1;;
				ItemReserveBean ib= new ItemReserveBean();
				ib.setDeptId(rs.getString("dep_num"));
				ib.setDeptName(rs.getString("dep_name"));
				ib.setDeptNo(rs.getString("seq_code"));
				ib.setUnionProjectNo(rs.getString("item_seq"));
				ib.setUnionProjectId(rs.getString("item_code"));
				ib.setUnionProjectName(rs.getString("item_name"));
				double amount=rs.getDouble("amount");
				BigDecimal bd = new BigDecimal(0);
				bd = new BigDecimal(amount);
				amount = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();		
				ib.setPrice(amount+"");
				ib.setExamNo(getExamNo(examid,rs.getLong("chargitem_id"),rs.getString("item_code"),rs.getString("dep_category"),logname));
				if("21".equals(rs.getString("dep_category"))){//pacs
					ib.setExamineType("2");
				}else if("131".equals(rs.getString("dep_category"))){//lis
					ib.setExamineType("1");
				}else{
					ib.setExamineType("");
				}
				ib.setNote(rs.getString("dep_address"));
				ib.setMemo(rs.getString("item_note"));
				if("全部".equals(rs.getString("sex"))){
					ib.setFitSex("0");
				}else if("男".equals(rs.getString("sex"))){
					ib.setFitSex("1");
				}else if("女".equals(rs.getString("sex"))){
					ib.setFitSex("2");
				}else{
					ib.setFitSex("0");
				}
				
		        if("131".equals(rs.getString("dep_category"))){//lis
					ib.setSendOrNot("0");
		        }else if("2".equals(rs.getString("interface_flag"))){
					ib.setSendOrNot("1");
				}else{
					ib.setSendOrNot("0");
				}
				
				ib.setPrintOrNot(isPrintSQD(examid,rs.getLong("chargitem_id"),logname));
				HisReqBean hb= new HisReqBean();
				hb=this.getPrices(rs.getString("his_num"), rs.getString("hiscodeClass"), logname);
				ib.setItemClass(hb.getItemClass());
				ib.setItemCode(hb.getItemCode());
				ib.setItemSpec(hb.getItemSpec());
				ib.setUnits(hb.getUnits());
				ib.setCodeClass(rs.getString("hiscodeClass"));
				ib.setChargeDept(rs.getString("perform_dept"));
				ib.setExamClass(rs.getString("examClass"));
				ib.setExamSubClass(rs.getString("examSubClass"));
				ib.setEditor(rs.getString("work_num"));
				ib.setEditDate(rs.getString("create_time"));				
				itemsInfo.add(ib);
			}
			
			rs.close();
			pb.setItemsNum(itemnum+"");
			pb.setItemsInfo(itemsInfo);
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
	public HisReqBean getPrices(String id,String code_class,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		HisReqBean hb = new HisReqBean();
		if("1".equals(code_class)){//价表
			String sql ="SELECT item_class as itemClass,item_code as itemCode,item_spec as itemSpec,units FROM his_price_list where ID='"+id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sql);
			PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, HisReqBean.class);
			if((map!=null)&&(map.getList().size()>0)){
				hb= (HisReqBean)map.getList().get(0);			
			}
		}else if("2".equals(code_class)){//诊疗项目
			String sql ="SELECT item_class as itemClass,item_code as itemCode FROM his_clinic_item where ID='"+id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sql);
			PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, HisReqBean.class);
			if((map!=null)&&(map.getList().size()>0)){
				hb= (HisReqBean)map.getList().get(0);			
			}
		}		
		return hb;
	} 
	/**
	 * 
	 * @param exam_id
	 * @param item_id
	 * @param dep_category
	 * @param logname
	 * @return
	 */
	private String getExamNo(long exam_id,long item_id,String item_code,String dep_category,String logname){
		Connection tjtmpconnect = null;
		String examNo="";
		if("21".equals(dep_category)){//pacs
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();	
			StringBuffer sb = new StringBuffer();
			sb.append("select req_id from zl_req_pacs_item where exam_info_id='"+exam_id+"' and charging_item_ids='"+item_code+"' ");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs1.next()) {
				examNo=rs1.getString("req_id");
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
		}else if("131".equals(dep_category)){//lis
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();	
				StringBuffer sb = new StringBuffer();
				sb.append("select req_id from zl_req_item where exam_info_id='"+exam_id+"' and charging_item_id='"+item_id+"' ");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
				if (rs1.next()) {
					examNo=rs1.getString("req_id");
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
		}
		return examNo;
	}
	
	
	/**
	 * 
	 * @param exam_id
	 * @param item_id
	 * @param dep_category
	 * @param logname
	 * @return
	 */
	private String isPrintSQD(long exam_id,long item_id,String logname){
		Connection tjtmpconnect = null;
		String examNo="0";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();	
			StringBuffer sb = new StringBuffer();
			sb.append("select ci.id from charging_item ci,sample_demo sd where sd.id=ci.sam_demo_id "
					+ "and sd.isPrint_req=1 and ci.id='"+item_id+"' ");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs1.next()) {
				examNo="1";
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
		
		return examNo;
	}
}
