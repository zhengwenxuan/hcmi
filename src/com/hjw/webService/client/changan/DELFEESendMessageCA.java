package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqHisItemDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

public class DELFEESendMessageCA {

	private DelFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	public DELFEESendMessageCA(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	public FeeReqBody getMessage(String url, String logname) {
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + url);
		Connection his_connect = null;
		try {
			String his_dburl = url.split("&")[0];
			String his_user = url.split("&")[1];
			String his_passwd = url.split("&")[2];
			his_connect = OracleDatabaseSource.getConnection(his_dburl, his_user, his_passwd);
			
			String exam_num = feeMessage.getExam_num();
//			ExamInfoUserDTO ei = configService.getExamInfoForNum(exam_num); 
//			//0,获取 VISIT_DATE VISIT_NO SERIAL_NO
//			String VISIT_DATE = "";
//			String VISIT_NO = "";
//			String SERIAL_NO = "";
			
			for(ExaminfoChargingItemDTO item : this.feeMessage.getItemCodeList()) {
				List<ZlReqHisItemDTO> list = select_zl_req_his_item(feeMessage.getREQ_NO(), item.getItem_code(), logname);
//				for(ZlReqHisItemDTO zrhi : list) {
					//1,删除：3.13检查治疗医嘱明细记录OUTP_TREAT_REC
					String delete_OUTP_TREAT_RE_sql = "delete from OUTP_TREAT_REC where VISIT_DATE = to_date('"+list.get(0).getTmp2()+"', 'yyyy-mm-dd') and VISIT_NO='"+list.get(0).getTmp1()+"' and SERIAL_NO='"+list.get(0).getHis_req_no()+"' ";
					TranLogTxt.liswriteEror_to_txt(logname, "1,删除：3.13检查治疗医嘱明细记录OUTP_TREAT_REC: " +delete_OUTP_TREAT_RE_sql);
					int count = his_connect.createStatement().executeUpdate(delete_OUTP_TREAT_RE_sql);
					TranLogTxt.liswriteEror_to_txt(logname, "1,删除：3.13检查治疗医嘱明细记录OUTP_TREAT_REC: 成功删除"+count+"条");
					
					//2,删除：3.14门诊医生收费明细OUTP_ORDERS_COSTS
					String delete_OUTP_ORDERS_COSTS_sql = "delete from OUTP_ORDERS_COSTS where VISIT_DATE = to_date('"+list.get(0).getTmp2()+"', 'yyyy-mm-dd') and VISIT_NO = '"+list.get(0).getTmp1()+"' and PATIENT_ID = '"+exam_num+"' and SERIAL_NO = '"+list.get(0).getHis_req_no()+"'";
					TranLogTxt.liswriteEror_to_txt(logname, "2,删除：3.14门诊医生收费明细OUTP_ORDERS_COSTS: " +delete_OUTP_ORDERS_COSTS_sql);
					count = his_connect.createStatement().executeUpdate(delete_OUTP_ORDERS_COSTS_sql);
					TranLogTxt.liswriteEror_to_txt(logname, "2,删除：3.14门诊医生收费明细OUTP_ORDERS_COSTS: 成功删除"+count+"条");
//				}
			}
//			his_connect.commit();
			ReqNo req= new ReqNo();
			req.setREQ_NO(feeMessage.getREQ_NO());
			rb.getControlActProcess().getList().add(req);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("撤销收费申请成功");
		} catch (Throwable ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + feeMessage.getREQ_NO()+ "向his库删除数据错误：" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			try{
//				his_connect.rollback();
			}catch(Exception et){}
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("向his库删除数据错误：" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (his_connect != null) {
					his_connect.close();
				}
			} catch (Exception e) {}
		}
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		return rb;
	}
	
	public List<ZlReqHisItemDTO> select_zl_req_his_item(String req_no, String charging_item_code,String logname) throws ServiceException {
		List<ZlReqHisItemDTO> list = new ArrayList<>();
		try {
			String selectSQL = "select * from zl_req_his_item where req_no='"+req_no+"' ";
//					+ " and charging_item_code='"+charging_item_code+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "select-zl_req_his_item-list-sql:" + selectSQL);
			list = jdbcQueryManager.getList(selectSQL, ZlReqHisItemDTO.class);
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + "select_zl_req_his_item list 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		if(list.size() == 0) {
			return new ArrayList<ZlReqHisItemDTO>();
		}
		return list;
	}
}
