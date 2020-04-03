package com.hjw.webService.client.cangzhoueryuan;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISSendMessageCZRY{
	private LisMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public LISSendMessageCZRY(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		ResultLisBody rb = new ResultLisBody();
		String exam_num = this.lismessage.getCustom().getExam_num();
		if (StringUtil.isEmpty(exam_num)) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检编号为空");
		} else {
			Connection connection = null;
			try {
				// 读取记录数
				connection = this.jdbcQueryManager.getConnection();
				TranLogTxt.liswriteEror_to_txt(logname,
						"req:" + lismessage.getMessageid() + ":1、调用存储过程   proc_push_lis_application");
				CallableStatement c = connection.prepareCall("{call proc_push_lis_application(?,?,?)}");
				TranLogTxt.liswriteEror_to_txt(logname,
						"proc_push_lis_application-" + exam_num + "-proc_push_lis_application('" + exam_num + "',?,?)");
				c.setString(1, exam_num);
				c.registerOutParameter(2, java.sql.Types.INTEGER);
				c.registerOutParameter(3, java.sql.Types.VARCHAR);
				// 执行存储过程
				c.execute();
				// 得到存储过程的输出参数值
				int RESULT_CODE = c.getInt(2);
				String ERROR_MSG = c.getString(3);
				c.close();
				TranLogTxt.liswriteEror_to_txt(logname, "proc_push_lis_application-" + lismessage.getMessageid() + ":"
						+ RESULT_CODE + "-----" + ERROR_MSG);
				ControlActLisProcess controlActProcess = new ControlActLisProcess();
				List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
				if (RESULT_CODE == 0) {
					for (LisComponents ls : this.lismessage.getComponents()) {
						ApplyNOBean an = new ApplyNOBean();
						an.setApplyNO(ls.getReq_no());
						list.add(an);
					}
					controlActProcess.setList(list);
					rb.setControlActProcess(controlActProcess);
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("");
				} else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(ERROR_MSG);
				}

			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}finally{
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException sqle4) {
					sqle4.printStackTrace();
				}			
			}
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
}
