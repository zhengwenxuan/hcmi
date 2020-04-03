package com.hjw.webService.client.SanMenXia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.7	三门峡-彩超-锦源:检查申请撤销信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSDELSendMessageSMX {
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSDELSendMessageSMX(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = "";
			if(url.split("&").length > 2) {
				passwd = url.split("&")[2];
			}
			connect = SqlServerDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + url);
			ControlActPacsProcess ca = new ControlActPacsProcess();
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			TranLogTxt.liswriteEror_to_txt(logname,"体检编号：="+lismessage.getCustom().getExam_num());
			
			for (PacsComponents pcs : lismessage.getComponents()) {
				TranLogTxt.liswriteEror_to_txt(logname,"体检项目：="+lismessage.getComponents().size());
				connect.setAutoCommit(false);
				ApplyNOBean an = new ApplyNOBean();
				TranLogTxt.liswriteEror_to_txt(logname,"体检子项目：="+pcs.getPacsComponent().size());
				try {
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
					+ ": 准备执行 basic_info_delete");
					/*
					 *  @brly nvarchar(10),--病人来源，门诊、住院、体检，此处传入 体检
						@sqdh nvarchar(50),--申请单号
				        @modality  nvarchar(40),--检查类型，超声为 US、内镜为 SC
				        返回值：      
        @RetStr nvarchar(50)  OUTPUT  --返回值说明：删除成功为 '成功'，失败为 检查状态+诊室名（如：一诊室检查中、二诊室已检查），未发现登记信息为  '不存在'
        不成功的原因：1没有找到记录；2已经被诊室打开（请让该诊室关闭这条登记信息再删除）
					 */
					TranLogTxt.liswriteEror_to_txt(logname,
							"req:" + lismessage.getMessageid() + ":1、调用存储过程  basic_info_delete");
					CallableStatement c = connect
							.prepareCall("{?=call basic_info_delete(?,?,?,?)}");
					TranLogTxt.liswriteEror_to_txt(logname, "res:basic_info_delete-" + lismessage.getCustom().getExam_num() + "-basic_info_delete('"
							+"体检"+"','"
							+pcs.getReq_no()+"','"
							+"US"+"','"
							+ "?')");
					c.setString(2, "体检");
					c.setString(3, pcs.getReq_no());
					c.setString(4, "US");
					c.registerOutParameter(5, java.sql.Types.VARCHAR);
					
					c.registerOutParameter(1, java.sql.Types.INTEGER);
					// 执行存储过程
					c.execute();
					int ret = c.getInt(1);
					String msg = c.getString(5);
					c.close();
					TranLogTxt.liswriteEror_to_txt(logname,
							"res:调用存储过程  basic_info_delete-" + lismessage.getMessageid() + ":撤销申请已发送...");
					if(ret == 0) {
						List<String> itemCodeList = new ArrayList<>();
						for (PacsComponent pc : pcs.getPacsComponent()) {
							itemCodeList.add(pc.getItemCode());
						}
						an.setApplyNO(pcs.getReq_no());
						an.setItemCodeList(itemCodeList);
						list.add(an);
						ca.setList(list);
						rb.setControlActProcess(ca);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText(msg);
						TranLogTxt.liswriteEror_to_txt(logname,"pacs撤销申请成功");
					} else if(ret == -1) {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("失败-"+msg);
						TranLogTxt.liswriteEror_to_txt(logname, "撤回失败！请联系该诊室关闭检查后再试！");
					} else if(ret == -2) {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(msg+"-未发现登记信息");
						TranLogTxt.liswriteEror_to_txt(logname, msg+"-未发现登记信息");
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(msg+"-不识别的存储过程返回值："+ret);
						TranLogTxt.liswriteEror_to_txt(logname, msg+"-不识别的存储过程返回值："+ret);
					}
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()
							+ ": 1、pacs调用存储过程  basic_info_delete错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}								
				connect.commit();									
			}
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("链接pacs数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					OracleDatabaseSource.close(connect);
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		return rb;
	}
}
