package com.hjw.test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.Bean.HisResBean;
import com.hjw.webService.client.tj180.Bean.HisResItemBean;

import net.sf.json.JSONObject;

public class Test {
    static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  
    static String url = "jdbc:sqlserver://192.168.8.170:1433;DatabaseName=peis";  
    static String user = "sa";  
    static String passwd = "HUOjianwa010";  
      
    public static void method() throws Exception {  
        Connection conn = null;  
        try {  
            Class.forName(driver);  
            conn = DriverManager.getConnection(url, user, passwd);  
            int op = 1;  
            // 插入  
            if (op == 0) {  
            	PreparedStatement ps = conn.prepareStatement("insert into zl_req_mq(messagetype,conttype,createtime,messages) values(?,?,?,?)");  
                ps.setString(1, "1");        
                ps.setInt(2, 0);  
                ps.setString(3, DateTimeUtil.getDateTime());  
               
                ps.setString(4, "23523236");  
                ps.executeUpdate();  
                ps.close();                  
            } else {  
                // 取出  
                PreparedStatement ps = conn.prepareStatement("select messages from zl_req_mq where id =9");  
                ResultSet rs = ps.executeQuery();  
                rs.next();  
                String md=rs.getString(1);
                rs.close();  
                ps.close();  
            }  
        } catch (Exception ex) {  
            ex.printStackTrace(System.out);  
        } finally {  
            if (null != conn)  
                conn.close();  
        }  
    }  
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
	/*	HisResBean mm =new HisResBean();
		mm.setStatus("23");
		mm.setErrorinfo("eroor");
		List<HisResItemBean> billItemsInfo = new ArrayList<HisResItemBean>();
		mm.setBillItemsInfo(billItemsInfo);
		String xml = JaxbUtil.convertToXml(mm, true);
		TranLogTxt.liswriteEror_to_txt("333333", "req:---:" + xml);
		*/
		String result="{\"billItemsInfo\":[],\"errorinfo\":\"找不到C0003858对应的价表项目\",\"billItemsNum\":\"\",\"status\":\"500\"}";
		TranLogTxt.liswriteEror_to_txt("333333", "res:" + result+"\r\n");
        if((result!=null)&&(result.trim().length()>0)){	   
				result = result.trim();
				JSONObject jsonobject = JSONObject.fromObject(result);
				Map classMap = new HashMap();
				classMap.put("billItemsInfo", HisResItemBean.class);
				HisResBean resdah = new HisResBean();
				resdah = (HisResBean) JSONObject.toBean(jsonobject, HisResBean.class, classMap);
				System.out.println(resdah.getStatus());
				if ("200".equals(resdah.getStatus())) {
					
				}
        }
	}

}
