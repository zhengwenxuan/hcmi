package com.hjw.webService.client.qiyang;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.MzghBaseInfoDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.model.ImpCustomerInfoModel;
import com.synjones.framework.persistence.JdbcQueryManager;

import oracle.jdbc.driver.OracleCallableStatement;

public class GetJiuzhenkaInfoQY {
	private static JdbcQueryManager jdbcQueryManager;
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public MzghBaseInfoDTO  getCardInfoPro_get_sickinfo(String jiuzhenka,WebserviceConfigurationDTO wcf){
		MzghBaseInfoDTO jzk = new MzghBaseInfoDTO();
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
			 
			 	 CallableStatement call = conn.prepareCall("{call zhiydba.zhi4_peis_wjw.pro_get_sickinfo(?,?,?,?)}");//执行存储过程
			 	 call.setString(1, jiuzhenka);//就诊卡号
			 	 System.err.println(jiuzhenka+"======");
				 call.registerOutParameter(2,oracle.jdbc.OracleTypes.CURSOR);
				 call.registerOutParameter(3, java.sql.Types.FLOAT);
				 call.registerOutParameter(4, java.sql.Types.LONGVARCHAR);
				 
				// 执行存储过程啊闪光灯
				call.execute();
				// 得到存储过程的输出参数值
				
				ResultSet rs = ((OracleCallableStatement)call).getCursor(2);
				TranLogTxt.liswriteEror_to_txt("qiyang", "res:" + "=="+call.getObject(3) +"\r\n");
				
				while (rs.next()){
					TranLogTxt.liswriteEror_to_txt("qiyang", "res:" + rs.getString(1)+"=="+rs.getString(2) + "=="+rs.getString(2) +"=="+rs.getString(3) +"=="+rs.getString(4) +"\r\n");
					jzk.setNAME(rs.getString("Name"));
					if(rs.getString("Sex").equals("男")){
						jzk.setSEX(1);
					}else{
						jzk.setSEX(0);
					}
					jzk.setBIRTH(rs.getString("Birthday"));
					jzk.setLXFS(rs.getString("Telephone"));
					jzk.setIDCARDINFO(rs.getString("Id_card_no"));
					
				}
				
				
				jzk.setTypecode(call.getString(3));
				jzk.setText(call.getString(4));
				
		} catch (SQLException e) {
			
			e.printStackTrace();
			jzk.setText(com.hjw.interfaces.util.StringUtil.formatException(e));
			jzk.setTypecode("-1");
		}
		 TranLogTxt.liswriteEror_to_txt("jiuzhenkaxinxi", "res:" +"姓名:"+ jzk.getNAME()+"性别:"+jzk.getSEX()+"联系方式:"+jzk.getLXFS()+"身份证号:"+jzk.getIDCARDINFO()+"\r\n");
		return jzk;
	}
}