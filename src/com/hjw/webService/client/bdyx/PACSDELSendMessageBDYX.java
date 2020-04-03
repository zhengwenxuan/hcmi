package com.hjw.webService.client.bdyx;

import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.bdyx.bean.RequestPost;
import com.hjw.webService.client.bdyx.bean.ResponsePost;
import com.hjw.webService.client.bdyx.bean.del.DeleteApplyReq;
import com.hjw.webService.client.bdyx.bean.del.Order;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class PACSDELSendMessageBDYX {

	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	static {
		init();
	}
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}
	private PacsMessageBody lismessage;
	public PACSDELSendMessageBDYX(PacsMessageBody lismessage){
		this.lismessage = lismessage;
	}
	public ResultPacsBody getMessage(String url,String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + JSONObject.fromObject(lismessage));
		ResultPacsBody rb = new ResultPacsBody();
		try {
			String exam_num = this.lismessage.getCustom().getExam_num();
			ExamInfoUserDTO eu = configService.getExamInfoForNum(exam_num);
			List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
			for (PacsComponents pcs : lismessage.getComponents()) {
				DeleteApplyReq deleteApplyReq = new DeleteApplyReq();
				deleteApplyReq.setPatientLid(eu.getPatient_id());
				deleteApplyReq.setVisitOrdNo(eu.getExam_num());
				
				String dep_inter_num = "";
				String dep_name = "";
				try {
					String sql = "select dep.dep_inter_num,dep.dep_name from department_dep dep, charging_item ci where dep.id=ci.dep_id and ci.item_code = '"+pcs.getPacsComponent().get(0).getItemCode()+"'";
					TranLogTxt.liswriteEror_to_txt(logname, "sql:"+sql);
					RowSet rs = jdbcQueryManager.getRowSet(sql);
					if(rs.next()) {
						dep_inter_num = rs.getString("dep_inter_num");
						dep_name = rs.getString("dep_name");
					}
					rs.close();
				} catch (Throwable e) {
					e.printStackTrace();
					TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				}
				Order order = new Order();
				order.setOrderLid(pcs.getReq_no());
				order.setOrderType("2");
				order.setOrderTypeName("检查类");
				order.setExecDept(dep_inter_num);
				order.setExecDeptName(dep_name);
				deleteApplyReq.getOrder().add(order);
				
				String str = new Gson().toJson(deleteApplyReq, DeleteApplyReq.class);
				TranLogTxt.liswriteEror_to_txt(logname,"lisApplyReq:"+str);
				RequestPost requestBDYX = new RequestPost();
				requestBDYX.setService_id("BS005");
				requestBDYX.setExec_uint_id("");
				requestBDYX.setOrder_exec_id(dep_inter_num);
				requestBDYX.setExtend_sub_id("");
				requestBDYX.setBody(str);
				String requestStr = JSONObject.fromObject(requestBDYX).toString();
				TranLogTxt.liswriteEror_to_txt(logname,"req:"+requestStr);
				String responseStr = HttpUtil.doPost(url,requestStr,"utf-8");
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+responseStr);
				ResponsePost responseBDYX = new Gson().fromJson(responseStr, ResponsePost.class);
				if(1 == responseBDYX.getStatus()) {
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(pcs.getReq_no());
					appList.add(an);
				}else{
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("LIS撤销申请返回错误:" + responseBDYX.getErrMsg());
					TranLogTxt.liswriteEror_to_txt(logname, "LIS撤销申请返回错误:" + responseBDYX.getErrMsg());
				}
			}
			rb.getControlActProcess().setList(appList);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("lis调用成功");
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis json格式文件错误");
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "ret:" + xml);
		return rb;
	}
}
