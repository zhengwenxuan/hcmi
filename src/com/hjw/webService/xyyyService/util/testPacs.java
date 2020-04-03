package com.hjw.webService.xyyyService.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;

public class testPacs {
	private static String username="hjw";
	private static String password="xyyytj";
	public static void main(String[] args) {
		getFtpFile("17092700005","B01","20170928","785778323");
	}
	public static void getFtpFile(String pacs_id,String typeCode,String date,String exam_num){
		date=date.substring(0, 4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
		if(!typeCode.equals("")){
			String url=queryUrl(typeCode,"url");
			String ftpurl=queryUrl(typeCode,"ftpurl");
			String ftpport=queryUrl(typeCode,"ftpport");
			 List<String[]> ftp=queryFtp(url,pacs_id);
			 List<String> paths=new ArrayList<String>();
			 if(ftp!=null){
				 for(int i=0;i<ftp.size();i++){
					 ftp.get(i)[0]= ftp.get(i)[0].replaceAll("\\\\", "/");
					 String server=ftpurl;
					 String filem=ftp.get(i)[0].substring( ftp.get(i)[0].indexOf("/"));
					 String name= ftp.get(i)[1];
					 paths.add(getFile(server,ftpport,filem,name,typeCode,date,exam_num,pacs_id,ftp.get(i)[2]));
				 }
			 }
		}
	}
	public static String queryUrl(String typeCode,String type){
		Connection connection = null;
		Statement statement = null;
		if(type.equals("url")){
			typeCode="XYYY_"+typeCode+"_URL";	
		}else if(type.equals("ftpurl")){
			typeCode="XYYY_"+typeCode+"_FTPURL";
		}else{
			typeCode="XYYY_"+typeCode+"_FTPPORT";
		}
		String sql="select config_value from center_configuration where config_key='"+typeCode+"'";
		String result="";
		try {
			// 读取记录数
			connection = SqlServerDatabaseSource.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=peis", "sa", "123456");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
					result=rs.getString("config_value");
			}
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return result;
	}
	public static List<String[]> queryFtp(String url,String pacs_id){
		Connection connection = null;
		Statement statement = null;
		String sql="select report_path,report_name,report_num from v_hjw where apply_id='"+pacs_id+"' and source='体检'";
		String result="";
		List<String[]> strsList=new ArrayList<String[]>();
		try {
			// 读取记录数
			connection = SqlServerDatabaseSource.getConnection(url, username, password);
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String[] strs=new String[3];
				strs[0]=rs.getString("report_path");
				strs[1]=rs.getString("report_name");
				strs[2]=rs.getString("report_num");
				strsList.add(strs);
			}
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return strsList;
	}
	
	private static String getFile(String server,String ftpport,String filem,String name,String typeCode,String date,String exam_num,String pacs_id,String num){
        File localFile=null;
        FTPClient ftp = new FTPClient();
        String filename="";
        try {  
            int reply;  
            //1.连接服务器 
            ftp.connect(server,Integer.valueOf(ftpport));
            //2.登录服务器 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
            ftp.login(username,password);  
            //3.判断登陆是否成功  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
            }  
            //4.指定要下载的目录  
            ftp.changeWorkingDirectory(filem);// 转移到FTP服务器目录 

            //5.遍历下载的目录  
            FTPFile[] fs = ftp.listFiles();  
            for (FTPFile ff : fs) {  
                //解决中文乱码问题，两次解码  
                byte[] bytes=ff.getName().getBytes("iso-8859-1");  
                String fn=new String(bytes,"utf-8");  
                if (fn.equals(name)) {
                	filename="D:"+File.separator+"picture"+File.separator+"pacs_img"+File.separator+date+File.separator+typeCode+File.separator+exam_num+File.separator+pacs_id+File.separator+pacs_id+"_"+num+".PDF";
                	localFile=new File(filename);
                	if(!localFile.getParentFile().exists()){
                		localFile.getParentFile().mkdirs();
                		localFile.createNewFile();
                	}
                	if(localFile.exists()){
                		localFile.delete();
                	}
                	OutputStream is = new FileOutputStream(localFile);  
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();   
                }  
            }  
            ftp.logout();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }
        filename=filename.substring(filename.indexOf("\\"));
		return filename;  
    }  
}
