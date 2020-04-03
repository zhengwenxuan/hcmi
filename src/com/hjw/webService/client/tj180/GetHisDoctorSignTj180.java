package com.hjw.webService.client.tj180;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.Base64;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.Bean.DoctorSignBean;
import com.hjw.webService.client.tj180.Bean.ResDoctorSign;
import com.hjw.wst.domain.WebUserInfo;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class GetHisDoctorSignTj180 {

	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public String getMessage(String url, WebUserInfo user, String logName) {

		
		
		  boolean flay = false;
		String message = "";
		
		try {
			DoctorSignBean signBean = new DoctorSignBean();
			signBean.setLoginUser(user.getLog_Name().toUpperCase());
			
			JSONObject json = JSONObject.fromObject(signBean);
			
			TranLogTxt.liswriteEror_to_txt(logName, "req--url===:" + url + "\r\n");
			TranLogTxt.liswriteEror_to_txt(logName, "req--loginUser传的json串===:" + json + "\r\n");
			
			String res = HttpUtil.doPost(url, json, "utf-8");
			
			TranLogTxt.liswriteEror_to_txt(logName, "res调用获取签名返回===:" + res + "\r\n");
			ResDoctorSign DoctorSign = new Gson().fromJson(res, ResDoctorSign.class);
			
			Connection connection = null;
			
			
			
			if(res.length()>0  && res.trim().length()>0){
				if (DoctorSign.getStatus().equals("200") && !DoctorSign.getStatus().equals("")) {
					
					String status = DoctorSign.getStatus();
					String loginUser = DoctorSign.getLoginUser();
					String userName = DoctorSign.getUserName();
					String sign = DoctorSign.getSign();
					String userId = DoctorSign.getUserId();
					String errorInfo = DoctorSign.getErrorInfo();

					String path="";
					if (sign != null && !sign.equals("") && sign.length()>0) {
						try {
							 path = decodeBase64JPG(user.getId()+"", sign);
							flay = true;
						} catch (IOException e) {
							flay = false;
							e.printStackTrace();
						}

					} else {
						flay = false;
					}

					if (flay) {
						// 读取记录数

						try {
							String sql = "  update user_usr set signpicpath='" + path + "' where id='" + user.getId() + "' ";
							
							TranLogTxt.liswriteEror_to_txt(logName, "update user_usr==path=:" + path + "\r\n");
							TranLogTxt.liswriteEror_to_txt(logName, "update user_usr==sql=:" + sql + "\r\n");
							
							connection = this.jdbcQueryManager.getConnection();
							connection.createStatement().executeUpdate(sql);
							flay = true;
						} catch (SQLException e) {
							flay = false;
							e.printStackTrace();

						} finally {
							try {
								connection.close();
							} catch (SQLException e) {
								
								e.printStackTrace();
							}
						}

					}
				}
				
			}else{
				flay=false;
			}
		} catch (Exception e) {
			flay=false;
		}
		if (flay) {
			message = "ok-获取HIS签名成功!";
		} else {
			message = "no-获取HIS签名失败!请手动上传签名!";
		}
		

		return message;
	}
	
	
	public  String decodeBase64JPG(String userid,String base64Str) throws IOException {
		String picpath ="D:\\picture\\usersign-picture";
		String picname = "usersign-picture";
		File f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdirs();
		
		String jpgname = userid +".jpg";
		picpath = picpath + "\\" + jpgname;
		picname = picname + "/" + jpgname;
		FileOutputStream fos = null;
		try {
			f = new File(picpath);
			if (f.exists() && f.isFile())
				f.delete();
			fos = new FileOutputStream(picpath);
			// 用FileOutputStream 的write方法写入字节数组
			byte[] bmpfiledata64 = Base64.base64Decode(base64Str);
			fos.write(bmpfiledata64);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		return picname;
	}
}
