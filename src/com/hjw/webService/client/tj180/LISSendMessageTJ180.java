package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.tj180.Bean.HisReqBean;
import com.hjw.webService.client.tj180.Bean.LisBodyReqBean;
import com.hjw.webService.client.tj180.Bean.LisReqBean;
import com.hjw.webService.client.tj180.Bean.LisResBean;
import com.hjw.webService.client.tj180.Bean.LisResItemBean;
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
public class LISSendMessageTJ180 {

	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public LISSendMessageTJ180(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		ResultLisBody rb = new ResultLisBody();

		try {
			JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			rb.getResultHeader().setTypeCode("AA");
			List<ApplyNOBean> listanb = new ArrayList<ApplyNOBean>();
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu = this.getExamInfoForNum(lismessage.getCustom().getExam_num(), logname);
			long exam_id = eu.getId();
			
			long oldexam_id =eu.getId();
			ExamInfoUserDTO oldeu = eu;
			String oldexam_num=lismessage.getCustom().getExam_num();
			
			if(oldexam_num.indexOf("B")==0){
				oldexam_num=oldexam_num.substring(1,oldexam_num.length());
			    oldeu = this.getExamInfoForNum(oldexam_num, logname);
				oldexam_id = oldeu.getId();
			}
			
			try {
				LisBodyReqBean lbqb = new LisBodyReqBean();
				lbqb.setCustomerPatientId(eu.getArch_num());
				lbqb.setOrderDept(this.lismessage.getDoctor().getDept_code());
				lbqb.setReserveId(oldexam_num);
				lbqb.setOrderDoctor(eu.getKaidanrens());
				if("T".equals(eu.getExam_type().trim())){
					lbqb.setReserveType("O");
				}else{
					lbqb.setReserveType("P");
				}
				
				int countitem = 0;
				List<LisReqBean> itemsInfo = new ArrayList<LisReqBean>();// 项目情况
				for (LisComponents comps : lismessage.getComponents()) {

					for (LisComponent comp : comps.getItemList()) {
						countitem++;
						LisReqBean lr = new LisReqBean();
						HisReqBean hqb = this.getPrices(comp.getHis_num(), comp.getCode_class(), logname);
						lr.setItemCode(hqb.getItemCode());
						lr.setPerformedBy(comp.getServiceDeliveryLocation_code());
						lr.setSampleClass(comp.getSpecimenNatural());
						lr.setUnionProjectId(comp.getXtItemCode());
						lr.setTestNo(getlisitemid(oldexam_id, comp.getChargingItemid(), comps.getReq_no(),logname));
						lr.setLisState(getlisEciforFeeflag(exam_id, comp.getChargingItemid(),logname));
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
						classMap.put("labItemsInfo", LisResItemBean.class);
						LisResBean resdah = new LisResBean();
						resdah = (LisResBean) JSONObject.toBean(jsonobject, LisResBean.class, classMap);
						if ("200".equals(resdah.getStatus())) {
							for(LisResItemBean lri : resdah.getLabItemsInfo()){
								if(!"".equals(lri.getErrorInfo())){
									rb.getResultHeader().setTypeCode("AE");
									rb.getResultHeader().setText(lri.getErrorInfo());
									TranLogTxt.liswriteEror_to_txt(logname, "res:testNo=" + lri.getTestNo() + ",错误消息="+lri.getErrorInfo()+"\r\n");
									return rb;
								}
							}
							//boolean success = false;
							for (LisResItemBean lri : resdah.getLabItemsInfo()) {
								try{
									String jyxmbm_num = "";
									String req_no = "";
									String charing_id = "";
									boolean flagss = false;
									for (LisComponents comps : lismessage.getComponents()) {
										for (LisComponent comp : comps.getItemList()) {
											if (comp.getXtItemCode().equals(lri.getUnionProjectId())) {
												jyxmbm_num = comp.getHis_num();
												charing_id = comp.getChargingItemid();
												flagss = true;
												req_no = comps.getReq_no();
												break;
											}
										}
										if (flagss) {
											break;
										}
									}
									if (flagss && (jyxmbm_num != null) && (jyxmbm_num.trim().length() > 0)) {
										updatezl_req_item(oldexam_id, charing_id, req_no, lri.getTestNo(),
												jyxmbm_num, logname);
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
				ex.printStackTrace();
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("lis调用webservice错误");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
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
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type,"
				+ "c.register_date,c.join_date,c.exam_times,a.arch_num,uu.chi_name as kaidanrens ");
		sb.append(" from exam_info c  left join user_usr uu on uu.id=c.kaidanren ,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 

	/**
	 * 
	 * @param exam_info_id 体检id
	 * @param chargitem_id 收费项目id
	 * @param lis_req_no  体检申请单号
	 * @param req_id  收费项目对应第三方系统返回的id
	 * @param logname 日志名称
	 * @param jyxmbm_num//第三方检验项目编码
	 * @return
	 * @throws Exception
	 */
	public boolean updatezl_req_item(long exam_info_id,String chargitem_id,String lis_req_no,String req_id,String jyxmbm_num,String logname) throws Exception{
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,charging_item_id,zl_pat_id,lis_item_id,"
					+ "req_id from zl_req_item where exam_info_id='"
					+ exam_info_id + "' and charging_item_id='"+chargitem_id+"'";
					//+ exam_info_id + "' and lis_req_code='"+lis_req_no+"' and charging_item_id='"+chargitem_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				String updatesql = "update zl_req_item set req_id='" +req_id
						+ "',lis_item_id='"+jyxmbm_num+"',createdate='"
						+DateTimeUtil.getDateTime()+"',lis_req_code='"+lis_req_no+"' where  exam_info_id='"
					+ exam_info_id + "' and charging_item_id='"+chargitem_id+"'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +updatesql);
				tjtmpconnect.createStatement().executeUpdate(updatesql);
			} else {
				String insertsql = "insert into zl_req_item(exam_info_id,charging_item_id,lis_item_id,lis_req_code,"
					+ "req_id,createdate) values('" + exam_info_id + "','" + chargitem_id + "','"+jyxmbm_num+"','"+lis_req_no+"','"+req_id+"','"
						+DateTimeUtil.getDateTime()+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +insertsql);
				tjtmpconnect.createStatement().executeUpdate(insertsql);
			}
			rs1.close();
			return true;
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
	
	public String getlisitemid(long exam_info_id,String chargitem_id,String lis_req_no,String logname){
		Connection tjtmpconnect = null;
		String lisitemid = "";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,charging_item_id,zl_pat_id,lis_item_id,"
					+ "req_id from zl_req_item where exam_info_id='"
					+ exam_info_id + "' and charging_item_id='"+chargitem_id+"'";
					//+ exam_info_id + "' and lis_req_code='"+lis_req_no+"' and charging_item_id='"+chargitem_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				lisitemid = rs1.getString("req_id");
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
		return lisitemid;
	}
	
	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @return
	 */
	public String getlisEciforFeeflag(long exam_info_id,String chargitem_id,String logname){
		Connection tjtmpconnect = null;
		String lisitemid = "9";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select pay_status,team_pay,personal_pay,team_pay_status from"
					+ " examinfo_charging_item where examinfo_id='"+exam_info_id+"' and charge_item_id='"+chargitem_id+"' and isActive='Y' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				String pay_status = rs1.getString("pay_status");
				if("R".equals(pay_status)){
					lisitemid="0";
				}else if("Y".equals(pay_status)){
					lisitemid="0";
				}else if("N".equals(pay_status)){
					lisitemid="9";
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
		return lisitemid;
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
}
