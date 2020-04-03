package com.hjw.webService.client.tj180;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.tj180.Bean.PacsGetReqBean;
import com.hjw.webService.client.tj180.Bean.PacsGetResBean;
import com.hjw.webService.client.tj180.Bean.PacsGetResItemBean;
import com.hjw.webService.client.tj180.Bean.PacsReqBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;
import com.synjones.framework.util.DateTimeUtil;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSResMessageTj18011{
   private static CommService commService;  
   private static JdbcQueryManager jdbcQueryManager;
   static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		commService = (CommService) wac.getBean("commService");
	}
	@SuppressWarnings("resource")
	public PACSResMessageTj18011(){
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname, String exam_num) {
		ResultPacsBody rb = new ResultPacsBody();
		Connection tjtmpconnect = null;
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + exam_num);
		try {
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu = this.getExamInfoForNum(exam_num);
			List<ZlReqPacsItemDTO> zris = new ArrayList<ZlReqPacsItemDTO>();
			zris = this.getzl_req_item(eu.getId(), logname);			
			String dept_num="";
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			for (ZlReqPacsItemDTO zri : zris) {
		        String newdept_num=zri.getRemark2();
				if(!dept_num.equals(newdept_num)){
					dept_num=newdept_num;						
				try {
					PacsGetReqBean p = new PacsGetReqBean();					
					PacsReqBean lrclass = new PacsReqBean();
					lrclass = this.getExamClass(zri.getZl_pacs_id(), logname);					
					p.setExamClass(lrclass.getExamClass());
					p.setExamSubClass(lrclass.getExamSubClass());
					p.setExamineDate(zri.getRemark1());
					p.setPatientId(zri.getZl_pat_id());
					p.setExamNo(zri.getReq_id());
					JSONObject json = JSONObject.fromObject(p);// 将java对象转换为json对象
					String str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
					String result = HttpUtil.doPost(url,p,"utf-8");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result);
		            if((result!=null)&&(result.trim().length()>0)){   
							result = result.trim();
							JSONObject jsonobject = JSONObject.fromObject(result);
							Map classMap = new HashMap();
							classMap.put("examResultInfo", PacsGetResItemBean.class);
							PacsGetResBean resdah = new PacsGetResBean();
							resdah = (PacsGetResBean) JSONObject.toBean(jsonobject, PacsGetResBean.class, classMap);
							if ("200".equals(resdah.getStatus())) {	
								List<ProcPacsResult> plrList = new ArrayList<ProcPacsResult>();								
								for (PacsGetResItemBean lgri : resdah.getExamResultInfo()) {
									// N：正常，L：偏低，H：偏高
									// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
									ProcPacsResult plr= new ProcPacsResult();
									plr.setCheck_date(parse5(lgri.getResultDateTime()));
									plr.setCheck_doct(lgri.getCheckPerson());
									plr.setAudit_date(parse5(lgri.getResultDateTime()));
									plr.setAudit_doct(lgri.getOperator());
									plr.setExam_desc(lgri.getDescription());
									plr.setExam_result(lgri.getImpression());
									plr.setImg_file("");
									plr.setExamMethod(dept_num);
									plrList.add(plr);
								}
								proc_pacs_report_dbgj(eu,plrList,dept_num,logname);
								
							}
						}
		            //tjtmpconnect.commit();
				} catch (Exception ex) {
					ex.printStackTrace();
					//tjtmpconnect.rollback();
				}
				}
			}
			rb.getResultHeader().setTypeCode("AA");
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("pacs调用webservice错误");
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rb;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	private ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
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
	
	private List<ZlReqPacsItemDTO> getzl_req_item(long examInfoId,String logname) throws Exception{
		Connection tjtmpconnect = null;
		List<ZlReqPacsItemDTO> req_ids= new ArrayList<ZlReqPacsItemDTO>();
		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select req_id,zl_pacs_id,pacs_req_code,zl_pat_id,remark1,remark2,remark3 from zl_req_pacs_item where exam_info_id='" + examInfoId + "' order by remark2 ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				ZlReqPacsItemDTO ri= new ZlReqPacsItemDTO();
				ri.setReq_id(rs1.getString("req_id"));
				ri.setZl_pacs_id(rs1.getString("zl_pacs_id"));
				ri.setPacs_req_code(rs1.getString("pacs_req_code"));
				ri.setZl_pat_id(rs1.getString("zl_pat_id"));
				ri.setRemark1(rs1.getString("remark1"));
				ri.setRemark2(rs1.getString("remark2"));
				ri.setRemark3(rs1.getString("remark3"));
				req_ids.add(ri);
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
		return req_ids;
	}
	
	private PacsReqBean getExamClass(String chargitem_code,String logname){
		Connection tjtmpconnect = null;
		PacsReqBean rb= new PacsReqBean();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.examClass,a.examSubClass "
					+ " from charging_item a where a.item_code='"+chargitem_code+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
			if (rs1.next()) {
				rb.setExamClass(rs1.getString("examClass"));
				rb.setExamSubClass(rs1.getString("examSubClass"));
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
		return rb;
	}
	
	private void proc_pacs_report_dbgj(ExamInfoUserDTO eu, List<ProcPacsResult> plrList,
			String dept_num,String logname) throws Exception {
		if ((plrList != null) && (plrList.size() > 0)) {
			String shys = "";
			String tjys = "";
			String desc = "";
			String res = "";
			for (ProcPacsResult plr : plrList) {
				if(plr.getCheck_doct()!=null){
				if (shys.indexOf(plr.getCheck_doct().trim()) < 0) {
					shys = shys + "," + plr.getCheck_doct().trim();
				}
				}
			    if(plr.getAudit_doct()!=null){
				if (tjys.indexOf(plr.getAudit_doct().trim()) < 0) {
					tjys = tjys + "," + plr.getAudit_doct().trim();
				}
			    }
				desc = desc + "\r\n" + plr.getExam_desc();
				res = res + "" + plr.getExam_result();
			}

			if (shys.length() > 0)
				shys = shys.substring(1, shys.length());
			if (tjys.length() > 0)
				tjys = tjys.substring(1, tjys.length());
			Connection tjtmpconnect = null;
			List<ZlReqPacsItemDTO> req_ids = new ArrayList<ZlReqPacsItemDTO>();

			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String selectsql = "select id from view_exam_detail where exam_info_id='" + eu.getId()
						+ "' and exam_item_id=347 and dept_num='" + dept_num + "'";

				TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句：1.1 ");
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(selectsql);
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + selectsql);
				if (rs1.next()) {
					long id = rs1.getLong("id");
					String inssql = "update view_exam_detail set exam_doctor='" + tjys + "',exam_date='"
							+ plrList.get(0).getAudit_date() + "',exam_desc='" + desc + "',exam_result='" + res + "',"
							+ "center_num='001'," + "approver='" + shys + "',approve_date='"
							+ plrList.get(0).getCheck_date() + "',creater='14',create_time='"
							+ DateTimeUtil.getDateTime() + "',pacs_id='0' where id='" + id + "'";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + inssql);
					tjtmpconnect.createStatement().execute(inssql);

					String upsql = " update examinfo_charging_item set exam_status='Y' where id in ( "
							+ "select a.id from examinfo_charging_item a,charging_item b,department_dep c "
							+ "where a.charge_item_id=b.id " + "and a.isActive='Y' " + "and b.dep_id=c.id "
							+ " and a.examinfo_id='" + eu.getId() + "' "
							+ " and c.dep_num='" + dept_num + "')";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + upsql);
					tjtmpconnect.createStatement().execute(upsql);
				} else {
					String inssql = "insert into view_exam_detail(exam_info_id,exam_item_id,exam_doctor,exam_date,exam_desc,exam_result,"
							+ "center_num," + "approver,approve_date,creater,create_time,pacs_id,dept_num) values('"
							+ eu.getId() + "','347','" + tjys + "','" + plrList.get(0).getAudit_date() + "','" + desc
							+ "','" + res + "','001','" + shys + "','" + plrList.get(0).getCheck_date() + "','14','"
							+ DateTimeUtil.getDateTime() + "','0','" + dept_num + "')";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + inssql);
					tjtmpconnect.createStatement().execute(inssql);

					String upsql = " update examinfo_charging_item set exam_status='Y' where id in ( "
							+ "select a.id from examinfo_charging_item a,charging_item b,department_dep c "
							+ "where a.charge_item_id=b.id " + "and a.isActive='Y' " + "and b.dep_id=c.id "
							+ " and a.examinfo_id='" + eu.getId() + "' "
							+ " and c.dep_num='" + dept_num + "')";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + upsql);
					tjtmpconnect.createStatement().execute(upsql);
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
	}

	private static String parse5(String param) {
		Date date = null;
		if ((param != null) && (param.trim().length() == 14)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			try {
				date = sdf.parse(param);
			} catch (ParseException ex) {
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static void main(String[] args) {
		String dd = "20180302105033";
		//System.out.println(parse5(dd));

	}
	
}
