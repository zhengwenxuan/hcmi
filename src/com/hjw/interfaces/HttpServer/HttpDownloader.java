package com.hjw.interfaces.HttpServer;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.hjw.util.TranLogTxt;

public class HttpDownloader{
	    private static final String REMOTE_FILE_URL = "http://localhost/25051940i6ou.pdf";
	    private static final String LOCAL_FILE_PATH = "D:/some.pdf"; // 改成你保存 文件的路径
		 
	    private String remoteFileUrl;
	    private String localFilePath;
	    private String logname;
	    
	    public static void main(String[] args) {
	        new HttpDownloader(REMOTE_FILE_URL, LOCAL_FILE_PATH,"ddd").download();
	    }

	 
	    public HttpDownloader(String remoteFileUrl, String localFilePath,String logname) {
	        this.remoteFileUrl = remoteFileUrl;
	        this.localFilePath = localFilePath;
	        this.logname=logname;
	    }
	 
	    public boolean download() {
	    	boolean writeflag=false;
	    	TranLogTxt.liswriteEror_to_txt(logname, "res:文件路径"+remoteFileUrl);
	    	TranLogTxt.liswriteEror_to_txt(logname, "res:文件路径"+localFilePath);
	        try {
	            URL url = new URL(remoteFileUrl);
	 
	            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	            httpURLConnection.setConnectTimeout(5 * 1000); // 5000 毫秒内没有连接上 则放弃连接
	            httpURLConnection.connect(); // 连接
	            TranLogTxt.liswriteEror_to_txt(logname, "res: 连接 URL 成功");
	            double fileLenght = httpURLConnection.getContentLength();
	            //System.out.println("文件大小：" + (fileLenght / 1024.0) + " KB");
	            TranLogTxt.liswriteEror_to_txt(logname, "res:文件大小：" + (fileLenght / 1024.0) + " KB");
				try (DataInputStream dis = new DataInputStream(httpURLConnection.getInputStream());
						FileOutputStream fos = new FileOutputStream(localFilePath)) {
					byte[] buf = new byte[10240]; // 根据实际情况可以 增大 buf 大小
					for (int readSize; (readSize = dis.read(buf)) > 0;) {
						fos.write(buf, 0, readSize);
					}		
					writeflag=true;
					TranLogTxt.liswriteEror_to_txt(logname, "res:下载完毕");
				}catch(IOException ex) {
					TranLogTxt.liswriteEror_to_txt(logname, "res:下载时出错" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
	            httpURLConnection.disconnect();
	        } catch (IOException ex) {
	            System.out.println("URL 不存在或者连接超时");
	            TranLogTxt.liswriteEror_to_txt(logname, "res:URL 不存在或者连接超时"+com.hjw.interfaces.util.StringUtil.formatException(ex));
	        }
	        TranLogTxt.liswriteEror_to_txt(logname, "res:下载返回" + writeflag);
	        return writeflag;
	    }
	    
	   
	}