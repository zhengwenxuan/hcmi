package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeControlActProcess;
import com.hjw.webService.client.Bean.FeeReqControlActProcess;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.UnFeeMessage;
import com.hjw.webService.client.tj180.Bean.HisReqBean;
import com.hjw.webService.client.tj180.Bean.HisResItemBean;
import com.hjw.webService.client.tj180.Bean.HisUnBodyReqBean;
import com.hjw.webService.client.tj180.Bean.HisUnReqBean;
import com.hjw.webService.client.tj180.Bean.UnFeeItemReqBean;
import com.hjw.webService.client.tj180.Bean.UnHisResBean;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 2.14 挂号信息服务 天健 平台对接-东北国际
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class UNFEESendMessageTj180 {

	private UnFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public UNFEESendMessageTj180(UnFeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url, String logname) {
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		try {
			this.feeMessage.setMSG_TYPE("TJ602");
			xml = JaxbUtil.convertToXml(this.feeMessage, true);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getMSG_TYPE() + ":" + xml);
			String exam_num =feeMessage.getEXAM_NUM();			
			HisUnBodyReqBean hbrb = new HisUnBodyReqBean();
			
			List<String> itemCodeList =feeMessage.getItemCodeList();
			hbrb.setReserveId(exam_num);
			hbrb.setItemsNum(itemCodeList.size() + "");
			List<HisUnReqBean> inf=new ArrayList<HisUnReqBean>();
			for(String itemcode:itemCodeList){
				UnFeeItemReqBean uf=getExamInfoItemForNum(exam_num,itemcode,logname);	
					HisUnReqBean hun= new HisUnReqBean();
					hun.setCodeClass(uf.getHiscodeClass());
					hun.setItemClass(uf.getHisitemClass());
					hun.setItemCode(uf.getHis_num());
					hun.setItemSpec(uf.getHisitemSpec());
					hun.setUnionProjectId(uf.getItem_code());
					hun.setUnits(uf.getHisunits());
					inf.add(hun);
				}				
				hbrb.setItemsInfo(inf);
				
				JSONObject json = JSONObject.fromObject(hbrb);// 将java对象转换为json对象
				String str = json.toString();// 将json对象转换为字符串
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
				String result = HttpUtil.doPost(url,hbrb,"utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
	            if((result!=null)&&(result.trim().length()>0)){	   
						result = result.trim();
						JSONObject jsonobject = JSONObject.fromObject(result);
						Map classMap = new HashMap();
						classMap.put("refundItemsInfo", HisResItemBean.class);
						UnHisResBean resdah = new UnHisResBean();
						resdah = (UnHisResBean) JSONObject.toBean(jsonobject, UnHisResBean.class, classMap);
						if ("200".equals(resdah.getStatus())) {
							if (!resdah.getRefundtemsNum().equals(hbrb.getItemsNum())) {
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText("回传缴费项数目个数不对，回传个数为：" + resdah.getRefundtemsNum() + "个");
							} else {
								List<String> reitemCodeList = new ArrayList<String>();
								for(HisResItemBean hib:resdah.getRefundItemsInfo()){
									reitemCodeList.add(hib.getUnionProjectId());
								}
								rb.setItemCodeList(reitemCodeList);
								rb.getResultHeader().setTypeCode("AA");
							}
						}else{
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText(resdah.getErrorinfo());
						}
					}
			
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
//		  JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
// 		   String str = json.toString();// 将json对象转换为字符串
// 		   TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		return rb;
	}

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public UnFeeItemReqBean getExamInfoItemForNum(String exam_num, String item_code,String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		UnFeeItemReqBean uf = new UnFeeItemReqBean();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append(
					" select a.examinfo_id,a.charge_item_id, a.personal_pay as amount1,c.his_num,c.hiscodeClass,c.item_code"
							+ "  from charging_item c,examinfo_charging_item a,exam_info d "
							+ "   where c.item_code='" + item_code
							+ "' and a.charge_item_id=c.id and a.isActive='Y' and a.examinfo_id=d.id and d.exam_num='"
							+ exam_num + "' ");
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs.next()) {
				uf.setAmount1(rs.getDouble("amount1"));
				uf.setCharging_item_id(rs.getLong("charge_item_id"));
				//uf.setCharging_status(rs.getString("charging_status"));
				uf.setExam_id(rs.getLong("examinfo_id"));
				uf.setHis_num(rs.getString("his_num"));
				uf.setItem_code(rs.getString("item_code"));
				uf.setHiscodeClass(rs.getString("hiscodeClass"));
				if ("1".equals(uf.getHiscodeClass())) {
					StringBuffer sb1 = new StringBuffer();
					sb1.append("select n.item_class,n.item_code,n.item_spec,n.units from his_price_list n where n.id='"+uf.getHis_num()+"' ");
					ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1.toString());
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1.toString());
					if(rs1.next()) {
						uf.setHis_num(rs1.getString("item_code"));
					   uf.setHisitemClass(rs1.getString("item_class"));
					   uf.setHisitemCode(rs1.getString("item_code"));
					   uf.setHisitemSpec(rs1.getString("item_spec"));
					   uf.setHisunits(rs1.getString("units"));
					}
					rs1.close();
				}else if ("2".equals(uf.getHiscodeClass())) {
					StringBuffer sb1 = new StringBuffer();
					sb1.append("select m.item_class,m.item_code from his_clinic_item m where m.id='"+uf.getHis_num()+"' ");
					ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1.toString());
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1.toString());
					if(rs1.next()) {
					   uf.setHis_num(rs1.getString("item_code"));
					   uf.setHisitemClass(rs1.getString("item_class"));
					   uf.setHisitemCode(rs1.getString("item_code"));
					}
					rs1.close();
				}
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
		return uf;
	}

}
