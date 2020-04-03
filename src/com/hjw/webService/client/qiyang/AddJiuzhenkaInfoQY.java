package com.hjw.webService.client.qiyang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import oracle.jdbc.driver.OracleCallableStatement;
import oracle.jdbc.driver.OracleTypes;

public class AddJiuzhenkaInfoQY {
	private static JdbcQueryManager jdbcQueryManager;
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public ResultHeader  getCardInfpro_create_ic_cardo(String user_name,String sex,String phone,String id_num,String address,WebserviceConfigurationDTO wcf){
		ResultHeader rh = new ResultHeader();
		Connection conn = null;
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet result = null;// 
		
	    String url = wcf.getConfig_url();//oracle数据库url
		String[] split = wcf.getConfig_value().split(",");
		String user = split[0];// 获取用户名
		String password = split[1];//获取密码
		
		try {
			
			 try {
				conn = OracleDatabaseSource.getConnection(url, user, password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
			 	CallableStatement  call = conn.prepareCall("{call zhiydba.zhi4_peis_wjw.pro_create_ic_card(?,?,?,?,?,?,?,?)}");//执行存储过程
				 call.setString(1, user_name);//客户姓名
				 call.setString(2, sex);//性别
				 call.setString(3, phone);//联系方式
				 call.setString(4, address);//地址
				 call.setString(5, id_num);//身份证号
				
				 
				 call.registerOutParameter(6, java.sql.Types.LONGVARCHAR);
				 call.registerOutParameter(7, java.sql.Types.FLOAT);
				 call.registerOutParameter(8,java.sql.Types.LONGVARCHAR);
				 
				// 执行存储过程啊闪光灯
				call.execute();
				// 得到存储过程的输出参数值
				
				
				rh.setSourceMsgId(call.getString(6));
				rh.setTypeCode(call.getString(7));//错误编码(0成功，-1失败)
				rh.setText(call.getString(8));//错误信息描述
			
			
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(e));
			rh.setTypeCode("-1");
		}
		 TranLogTxt.liswriteEror_to_txt("jiuzhenkaxinxi", "res:" +"诊疗卡号:"+ rh.getSourceMsgId()+"成功失败标识:"+rh.getTypeCode()+"消息说明:"+rh.getText()+"\r\n");
		return rh;
	}
}
