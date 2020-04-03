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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.tj180.Bean.HisReqBean;
import com.hjw.webService.client.tj180.Bean.PacsBodyReqBean;
import com.hjw.webService.client.tj180.Bean.PacsReqBean;
import com.hjw.webService.client.tj180.Bean.PacsResBean;
import com.hjw.webService.client.tj180.Bean.PacsResItemBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSSendMessageTJ180 {

	private PacsMessageBody pacsmessage;
	private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSSendMessageTJ180(PacsMessageBody pacsmessage){
		this.pacsmessage=pacsmessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		String xml = "";
		try {
			JSONObject json = JSONObject.fromObject(pacsmessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + pacsmessage.getMessageid() + ":" + xml);
			rb.getResultHeader().setTypeCode("AA");
			List<ApplyNOBean> listanb = new ArrayList<ApplyNOBean>();
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu = this.getExamInfoForNum(pacsmessage.getCustom().getExam_num(), logname);
			long exam_id = eu.getId();
			String exam_num=pacsmessage.getCustom().getExam_num();
			
			long oldexam_id =eu.getId();
			ExamInfoUserDTO oldeu = eu;
			String oldexam_num=exam_num;
			
			if(oldexam_num.indexOf("B")==0){
				oldexam_num=oldexam_num.substring(1,oldexam_num.length());
			    oldeu = this.getExamInfoForNum(oldexam_num, logname);
				oldexam_id = oldeu.getId();
			}
			
			try {
				PacsBodyReqBean lbqb = new PacsBodyReqBean();
				lbqb.setCustomerPatientId(eu.getArch_num());
				lbqb.setOrderDept(this.pacsmessage.getDoctor().getDept_code());
				lbqb.setReserveId(oldexam_num);
				lbqb.setReserveType("P");
				lbqb.setOrderDoctor(eu.getKaidanrens());
				List<PacsReqBean> itemsInfo = new ArrayList<PacsReqBean>();// 项目情况
				int countitem = 0;
				for (PacsComponents comps : pacsmessage.getComponents()) {
					for (PacsComponent comp : comps.getPacsComponent()) {
						countitem++;
						PacsReqBean lr = new PacsReqBean();
						HisReqBean hqb = this.getPrices(comp.getHis_num(), comp.getCode_class(), logname);
						lr.setItemCode(hqb.getItemCode());
						lr.setPerformedBy(comp.getServiceDeliveryLocation_code());
						PacsReqBean lrclass = new PacsReqBean();
						lrclass = this.getExamClass(comp.getItemCode(), logname);
						lr.setExamClass(lrclass.getExamClass());
						lr.setExamSubClass(lrclass.getExamSubClass());
						lr.setUnionProjectId(comp.getItemCode());
						lr.setExamNo(getExamNumByReq(oldexam_id, comp.getItemCode(), comps.getReq_no()));
						itemsInfo.add(lr);
					}
				}
				lbqb.setItemsNum(countitem + "");
				lbqb.setItemsInfo(itemsInfo);
				try {
					json = JSONObject.fromObject(lbqb);// 将java对象转换为json对象
					str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
					String result = HttpUtil.doPost(url, lbqb, "utf-8");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
					if ((result != null) && (result.trim().length() > 0)) {
						result = result.trim();
						JSONObject jsonobject = JSONObject.fromObject(result);
						Map classMap = new HashMap();
						classMap.put("examItemsInfo", PacsResItemBean.class);
						PacsResBean resdah = new PacsResBean();
						resdah = (PacsResBean) JSONObject.toBean(jsonobject, PacsResBean.class, classMap);
						if ("200".equals(resdah.getStatus())) {
							// boolean success=false;
							for (PacsResItemBean lri : resdah.getExamItemsInfo()) {
								try {
									String jcxmbm_num = "";
									String dclass = "";
									String xclass = "";
									String datetime = "";
									String req_no = "";
									boolean flags = false;
									for (PacsComponents comps : pacsmessage.getComponents()) {
										for (PacsComponent comp : comps.getPacsComponent()) {
											if (comp.getItemCode().equals(lri.getUnionProjectId())) {
												jcxmbm_num = comp.getItemCode();
												datetime = parse5(comp.getItemDate());
												dclass = comp.getExam_class();
												xclass = comp.getExam_class();
												req_no = comps.getReq_no();
												flags = true;
												break;
											}
										}
										if (flags) {
											break;
										}
									}
									if (flags && (jcxmbm_num != null) && (jcxmbm_num.trim().length() > 0)) {
										// success=true;
										updatezl_req_pacs_item(oldexam_id, lbqb.getCustomerPatientId(),
												lri.getUnionProjectId(), req_no, lri.getExamNo(), jcxmbm_num, datetime,
												dclass, xclass, logname);
										ApplyNOBean an = new ApplyNOBean();
										an.setApplyNO(req_no);
										listanb.add(an);
									}

								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				rb.getControlActProcess().setList(listanb);
			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("lis调用webservice错误");
			}

		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "re1:" + str);
		return rb;
	}
	

		/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_code
	 * @param lis_req_no
	 * @param req_id
	 * @param logname
	 * @return
	 * @throws Exception
	 */
	public boolean updatezl_req_pacs_item(long exam_info_id,String patid,String chargitem_code,String lis_req_no,
			String req_id,String jcxmbm_num,String datetime,String examClass,String examSubClass,String logname) throws Exception{
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,pacs_req_code,charging_item_ids,"
					+ "req_id from zl_req_pacs_item where exam_info_id='"
					+ exam_info_id + "' and pacs_req_code='"+lis_req_no+"' and charging_item_ids='"+chargitem_code+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + pacsmessage.getMessageid()+ ":操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				String updatesql = "update zl_req_pacs_item set req_id='" + req_id
						+ "',zl_pacs_id='"+jcxmbm_num+"',zl_pat_id='"+patid+"',remark1='"+datetime+"',remark2='"+examClass+"',remark3='"
						+examSubClass+"',createdate='"
						+DateTimeUtil.getDateTime()+"' where   exam_info_id='"
					+ exam_info_id + "' and pacs_req_code='"+lis_req_no+"' and charging_item_ids='"+chargitem_code+"'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + pacsmessage.getMessageid()+ ":操作语句： " +updatesql);
				tjtmpconnect.createStatement().executeUpdate(updatesql);
			} else {
				String insertsql = "insert into zl_req_pacs_item(exam_info_id,pacs_req_code,charging_item_ids,"
						+ "req_id,zl_pacs_id,zl_pat_id,remark1,remark2,remark3,createdate) values('" +exam_info_id + "','" + lis_req_no
						+ "','" + chargitem_code + "','"+req_id+"','"+jcxmbm_num+"','"+patid+"','"+datetime+"','"+examClass+"','"+examSubClass+"','"
						+DateTimeUtil.getDateTime()+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + pacsmessage.getMessageid()+ ":操作语句： " +insertsql);
				tjtmpconnect.createStatement().executeUpdate(insertsql);
			}
			rs1.close();
			return true;
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + pacsmessage.getMessageid()+ ": zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
			return false;
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
	
	public String getExamNumByReq(long exam_info_id,String chargitem_code,String lis_req_no){
		Connection tjtmpconnect = null;
		String examNo = "";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,pacs_req_code,charging_item_ids,"
					+ "req_id from zl_req_pacs_item where exam_info_id='"
					+ exam_info_id + "' and pacs_req_code='"+lis_req_no+"' and charging_item_ids='"+chargitem_code+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				examNo = rs1.getString("req_id");
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
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type,"
				+ "c.register_date,c.join_date,c.exam_times,a.arch_num,uu.chi_name as kaidanrens ");
		sb.append(" from exam_info c left join user_usr uu on uu.id=c.kaidanren ,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y'  ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + pacsmessage.getMessageid()+ ":操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
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
	
	public PacsReqBean getExamClass(String chargitem_code,String logname){
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
	
	public static String parse5(String param) {
			Date date = new Date();
			if ((param != null) && (param.trim().length() == 8)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				sdf.setLenient(false);
				try {
					date = sdf.parse(param);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
	}
		
}
