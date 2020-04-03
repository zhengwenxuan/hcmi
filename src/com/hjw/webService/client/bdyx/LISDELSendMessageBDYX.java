package com.hjw.webService.client.bdyx;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.bdyx.bean.RequestPost;
import com.hjw.webService.client.bdyx.bean.ResponsePost;
import com.hjw.webService.client.bdyx.bean.del.DeleteApplyReq;
import com.hjw.webService.client.bdyx.bean.del.Order;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.BatchService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

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
public class LISDELSendMessageBDYX {

	private LisMessageBody lismessage;
	private static ConfigService configService;
    private static JdbcQueryManager jdbcQueryManager;
    private static JdbcPersistenceManager jdbcPersistenceManager;
    private static BatchService batchService;
    static {
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		batchService = (BatchService) wac.getBean("batchService");
	}
	
	public LISDELSendMessageBDYX(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	public ResultLisBody getMessage(String url,String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + JSONObject.fromObject(lismessage));
		ResultLisBody rb = new ResultLisBody();
		try {
			String exam_num = this.lismessage.getCustom().getExam_num();
			ExamInfoUserDTO eu = configService.getExamInfoForNum(exam_num);
			List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
			for (LisComponents lcs : lismessage.getComponents()) {
				DeleteApplyReq deleteApplyReq = new DeleteApplyReq();
				deleteApplyReq.setPatientLid(eu.getPatient_id());
				deleteApplyReq.setVisitOrdNo(eu.getExam_num());
				
				String sql = " select distinct ec.id,c.item_code from examinfo_charging_item ec,sample_exam_detail s,charging_item  c "
					+ " where ec.exam_num = s.exam_num and ec.charge_item_id = c.id and s.sample_id = c.sam_demo_id and ec.isActive = 'Y' "
					+ " and c.interface_flag = '2' and ec.is_application = 'Y' and ec.change_item != 'C' and ec.pay_status != 'M' "
					+ " and s.sample_barcode = '"+lcs.getReq_no()+"'";
				Connection connection = null;
				try {
					connection = jdbcQueryManager.getConnection();
					TranLogTxt.liswriteEror_to_txt(logname, "根据条码查询收费项目： sql："+sql);
					ResultSet rs = connection.createStatement().executeQuery(sql);
					if(rs.next()) {
						String item_code = rs.getString("item_code");
						Order order = new Order();
						order.setOrderLid(lcs.getReq_no()+"-"+item_code);
						order.setOrderType("3");
						order.setOrderTypeName("化验类");
						order.setExecDept("029");
						order.setExecDeptName("检验科");
						deleteApplyReq.getOrder().add(order);
					}
				} catch (Throwable e) {
					TranLogTxt.liswriteEror_to_txt(logname, "插入pacs_result sql："+sql);
					TranLogTxt.liswriteEror_to_txt(logname, "插入pacs_result 错误："+com.hjw.interfaces.util.StringUtil.formatException(e));
					e.printStackTrace();
				} finally {
					try {
						if(connection != null) {
							connection.close();
						}
					} catch (SQLException e) {
					}
				}
				String str = new Gson().toJson(deleteApplyReq, DeleteApplyReq.class);
				TranLogTxt.liswriteEror_to_txt(logname,"lisApplyReq:"+str);
				RequestPost requestBDYX = new RequestPost();
				requestBDYX.setService_id("BS005");
				requestBDYX.setExec_uint_id("");
				requestBDYX.setOrder_exec_id("029");
				requestBDYX.setExtend_sub_id("");
				requestBDYX.setBody(str);
				String requestStr = JSONObject.fromObject(requestBDYX).toString();
				TranLogTxt.liswriteEror_to_txt(logname,"req:"+requestStr);
				String responseStr = HttpUtil.doPost(url,requestStr,"utf-8");
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+responseStr);
				ResponsePost responseBDYX = new Gson().fromJson(responseStr, ResponsePost.class);
				if(1 == responseBDYX.getStatus()) {
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(lcs.getReq_no());
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