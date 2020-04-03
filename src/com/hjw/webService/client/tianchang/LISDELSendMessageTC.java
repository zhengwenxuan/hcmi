package com.hjw.webService.client.tianchang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.SampleDemoDTO;
import com.hjw.wst.service.BatchService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.client.lianzhong
     * @Description: 2.7	检查申请撤销信息服务  浙江联众-贵航贵阳
     * @author: zwx
     * @version V2.0.0.0
 */
public class LISDELSendMessageTC{
	private LisMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    private static JdbcPersistenceManager jdbcPersistenceManager;
    private static BatchService batchService;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		batchService = (BatchService) wac.getBean("batchService");
	}
	public LISDELSendMessageTC(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname, boolean debug) {
		TranLogTxt.liswriteEror_to_txt(logname, "1-----------------开始lis撤销申请--------------------");
		ResultLisBody rb = new ResultLisBody();
		Connection connect = null;
		try {
			String jsonString = JSONSerializer.toJSON(lismessage).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
			ExamInfoUserDTO eu= this.getExamInfoForNum(this.lismessage.getCustom().getExam_num(),logname);

			TranLogTxt.liswriteEror_to_txt(logname, "2-----------------lis数据库url--------------------" + url);
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "3-----------------连接成功--------------------");

			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			for (LisComponents lcs : this.lismessage.getComponents()) {
				try {
					TranLogTxt.liswriteEror_to_txt(logname,"req:" + "4.准备删除条码表，调用存储过程  Pkg_TcTjjk.p_Trade");
					CallableStatement c = connect.prepareCall("{call Pkg_TcTjjk.p_Trade(?,?,?,?)}");
					String param = lcs.getReq_no();//条码号
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + " - Pkg_TcTjjk.p_Trade("
							+ "'p_Delete_Laborder',"//--删除条码表
							+ "'"+param+"',"
							+"?,"//OUT--返回Code
							+"?)"//OUT--申请人人工号
							);
					c.setString(1,"p_Delete_Laborder");
					c.setString(2,param);
					c.registerOutParameter(3, java.sql.Types.INTEGER);
					c.registerOutParameter(4, java.sql.Types.VARCHAR);
					c.execute();
					c.close();
					TranLogTxt.liswriteEror_to_txt(logname,
							"res:" + lismessage.getMessageid() + ":删除条码表，存储过程 Pkg_TcTjjk.p_Trade 执行结果————"
									+ "代码:"+c.getInt(3)+"信息:"+c.getString(4));
					if(c.getInt(3)>0) {
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + "还原样本为未采样");
						String barcode_new = updateSampleExamDetailByExamid(eu.getId(), lcs.getReq_no(), "W");
						update_examinfo_charging_item(eu.getId(), barcode_new, "N");
						
						ApplyNOBean ap = new ApplyNOBean();
						ap.setApplyNO(lcs.getReq_no());
						ap.setLis_id(lcs.getLis_id());
						ap.setBarcode(lcs.getReq_no());
						list.add(ap);
						rb.getResultHeader().setTypeCode("AA");
						rb.getControlActProcess().setList(list);
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(c.getString(4));
					}
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
			}
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		TranLogTxt.liswriteEror_to_txt(logname, "6-----------------结束lis撤销申请--------------------");
		return rb;
	}
	
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times,c.phone,c.company,c.address,a.arch_num,a.birthday,a.id_num,ci.com_phone as remark1 ");
		sb.append(" from customer_info a,exam_info c ");
		sb.append(" left join company_info ci on ci.id = c.company_id ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "req:" +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	private String updateSampleExamDetailByExamid(long examid, String sample_barcode, String status) throws ServiceException {
		String barcode_new = this.batchService.GetCreateID("barcode");
		String sql = "update sample_exam_detail set status = '"+status+"',sample_barcode ='"+barcode_new+"'  where exam_info_id=" + examid + " and sample_barcode='" + sample_barcode + "'";
		System.out.println(sql);
		this.jdbcPersistenceManager.executeSql(sql);
		return barcode_new;
	}
	
	private void update_examinfo_charging_item(long examid, String sample_barcode, String status) throws ServiceException {
		String sql = "update examinfo_charging_item set is_application = '"+status+"' where examinfo_id = "+examid
		+ " and charge_item_id in(select ec.charging_id from sample_exam_detail s,examResult_chargingItem ec "
		+ " where s.id = ec.exam_id and ec.result_type = 'sample' and s.sample_barcode = '"+sample_barcode+"')";
		this.jdbcPersistenceManager.executeSql(sql);
	}
}
