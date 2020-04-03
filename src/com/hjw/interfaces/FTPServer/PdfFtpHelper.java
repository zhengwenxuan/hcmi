package com.hjw.interfaces.FTPServer;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import com.hjw.DTO.FileDTO; 
  
/** 
 * 
 * @author xxj 
 */  
public class PdfFtpHelper implements Closeable {  
    private FTPClient ftp = null;  
    boolean _isLogin = false;  
    public static PdfFtpHelper getInstance() {  
        return new PdfFtpHelper();  
    }  
      
    /** 
     *  
     * ftp 匿名登录 
     * @param ip ftp服务地址 
     * @param port 端口号 
     * @param uname 用户名 
     * @param pass 密码 
     */  
    public boolean login(String ip,int port){  
        //如果没有设置ftp用户可将username设为anonymous，密码为任意字符串  
        return login(ip, port,"anonymous","");  
    }   
    /** 
     *  
     * ftp登录 
     * @param ip ftp服务地址 
     * @param port 端口号 
     * @param uname 用户名 
     * @param pass 密码 
     * @param workingDir ftp 根目目录 
     */  
    public boolean login(String ip,int port, String uname, String pass) {  
        ftp = new FTPClient();  
//      boolean flag=false;  
        try {  
            // 连接  
            ftp.connect(ip,port);  
            _isLogin = ftp.login(uname, pass);  

            // 检测连接是否成功  
            int reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                System.err.println("FTP服务器拒绝连接 ");  
                return false;  
            }  
            return true;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            return false;  
        }  
    }  
   
   
    /** 
     *  
     * ftp上传文件 (使用inputstream) 
     * @param localFileName 待上传文件 
     * @param ftpDirName ftp 目录名 
     * @param ftpFileName ftp目标文件 
     * @return true||false 
     */  
    public boolean uploadFile(FileInputStream uploadInputStream  
            ,String ftpDirName  
            , String ftpFileName) {  
                 
        try {  
            // 设置上传目录(没有则创建)  
            if(!createDir(ftpDirName)){  
                throw new RuntimeException("切入FTP目录失败："+ftpDirName);  
            }  
            ftp.setBufferSize(1024);  
            //解决上传中文 txt 文件乱码  
            ftp.setControlEncoding("GBK");  
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);   
            conf.setServerLanguageCode("zh");    
  
  
            // 设置文件类型（二进制）  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);  
            // 上传  
            String fileName = new String(ftpFileName.getBytes("GBK"),"iso-8859-1");  
            if(ftp.storeFile(fileName, uploadInputStream)){  
                uploadInputStream.close();  
               
                return true;  
            }  
              
            return false;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        } finally {  
        }  
    } 
    
    /** 
     * 下载文件 
     * @param ftpDirName ftp目录名 
     * @param ftpFileName ftp文件名 
     * @param localFileFullName 本地文件名 
     * @return 
     *  @author xxj 
     */  
	public FileDTO downloadFile(String ftpDirName, String ftpFileName,boolean filenameindir) {
		FileDTO fd = new FileDTO();
		try {
			if ("".equals(ftpDirName))
				ftpDirName = "/";
			String dir = new String(ftpDirName.getBytes("GBK"), "iso-8859-1");
			String fileName = new String(ftpFileName.getBytes("GBK"), "iso-8859-1");
			String remoteAbsoluteFile = dir + "/" + fileName;
            if(filenameindir){
            	remoteAbsoluteFile = dir;
            }			
			//System.out.print(remoteAbsoluteFile);
			InputStream in = null;
			// 下载文件
			ftp.setBufferSize(1024);
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(ftp.BINARY_FILE_TYPE);
			ftp.setRemoteVerificationEnabled(false);
			in = ftp.retrieveFileStream(remoteAbsoluteFile);
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();  
			int ch;  
			while ((ch = in.read()) != -1) {  
			 bytestream.write(ch);  
			}  
			byte imgdata[] = bytestream.toByteArray();  
			fd.setFlag(true);
			fd.setBytes(imgdata);
			bytestream.close();  
			System.out.println("下载成功!" + imgdata.length);
			// in.read(bytes);
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			fd.setFlag(false);
		}
		return fd;
	}  
	
	public FileDTO downloadFile(String ftpDirName, String ftpFileName) {
		FileDTO fd = new FileDTO();
		try {
			if ("".equals(ftpDirName))
				ftpDirName = "/";
			String dir = new String(ftpDirName.getBytes("GBK"), "iso-8859-1");
			String fileName = new String(ftpFileName.getBytes("GBK"), "iso-8859-1");
			String remoteAbsoluteFile = dir + "/" + fileName;
           /* if(filenameindir){
            	remoteAbsoluteFile = dir;
            }*/			
			//System.out.print(remoteAbsoluteFile);
			InputStream in = null;
			// 下载文件
			ftp.setBufferSize(1024);
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(ftp.BINARY_FILE_TYPE);
			ftp.setRemoteVerificationEnabled(false);
			in = ftp.retrieveFileStream(remoteAbsoluteFile);
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();  
			int ch;  
			while ((ch = in.read()) != -1) {  
			 bytestream.write(ch);  
			}  
			byte imgdata[] = bytestream.toByteArray();  
			fd.setFlag(true);
			fd.setBytes(imgdata);
			bytestream.close();  
			System.out.println("下载成功!" + imgdata.length);
			// in.read(bytes);
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			fd.setFlag(false);
		}
		return fd;
	}  
	
	/** 
     * 下载文件 
     * @param ftpDirName ftp目录名 
     * @param ftpFileName ftp文件名 
     * @param localFileFullName 本地文件名 
     * @return 
     *  @author xxj 
     */  
	public FileDTO urlDownloadFile(String urlpath) {
		String SerUrl=null;
		String tempDrvs="/";
		String fileName="";
		int port=21;
			SerUrl=urlpath.substring(urlpath.indexOf("//")+2 , urlpath.indexOf("/",urlpath.indexOf("//")+2));
			if(urlpath.indexOf("/",urlpath.indexOf("//")+2) < urlpath.lastIndexOf("/")) {
				tempDrvs=urlpath.substring(urlpath.indexOf("/",urlpath.indexOf("//")+2)+1,urlpath.lastIndexOf("/"));
			}
			String ftpCurDrv=tempDrvs.replace("/","\\");
            
		if(SerUrl.indexOf(":")!=-1) {
			port=Integer.parseInt(SerUrl.substring(SerUrl.indexOf(":")+1));
			SerUrl=SerUrl.substring(0,SerUrl.indexOf(":"));
				
		}		
		fileName=urlpath.substring(urlpath.lastIndexOf("/")+1);

		FileDTO fd = new FileDTO();
		try {
			boolean f =this.login(SerUrl, port);
			if(f){
			//System.out.print(remoteAbsoluteFile);
			InputStream in = null;
			// 下载文件
			ftp.setBufferSize(1024);
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(ftp.BINARY_FILE_TYPE);
			ftp.setRemoteVerificationEnabled(false);
			ftp.changeWorkingDirectory(ftpCurDrv);
        	in=ftp.retrieveFileStream(fileName);
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();  
			int ch;  
			while ((ch = in.read()) != -1) {  
			 bytestream.write(ch);  
			}  
			byte imgdata[] = bytestream.toByteArray();  
			fd.setFlag(true);
			fd.setBytes(imgdata);
			bytestream.close();  
			System.out.println("下载成功!" + imgdata.length);
			// in.read(bytes);
			in.close();
			}else{
				fd.setFlag(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fd.setFlag(false);
		}
		return fd;
	}  
  
    public static byte[] input2byte(InputStream inStream) throws IOException {
        byte[] in2b = new byte[inStream.available()];
        inStream.read(in2b);
        return in2b;
    }
    /** 
     *  
     * 删除ftp上的文件 
     *  
     * @param ftpFileName 
     * @return true || false 
     */  
    public boolean removeFile(String ftpFileName) {  
        boolean flag = false;  
       try {  
            ftpFileName = new String(ftpFileName.getBytes("GBK"),"iso-8859-1");  
            flag = ftp.deleteFile(ftpFileName);  
            
            return flag;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return false;  
        }  
    }
    
    /** 
     * 删除空目录 
     * @param dir 
     * @return 
     */  
    public boolean removeDir(String dir){  
       /* if(StringExtend.startWith(dir, "/"))  
            dir="/"+dir;  */
        try {  
            String d = new String(dir.toString().getBytes("GBK"),"iso-8859-1");           
            return ftp.removeDirectory(d);            
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  
    /** 
     * 创建目录(有则切换目录，没有则创建目录) 
     * @param dir 
     * @return 
     */  
    public boolean createDir(String dir){  
      /*  if(StringExtend.isNullOrEmpty(dir))  
            return true;  */
        String d;  
        try {  
            //目录编码，解决中文路径问题  
            d = new String(dir.toString().getBytes("GBK"),"iso-8859-1");  
            //尝试切入目录  
            if(ftp.changeWorkingDirectory(d))  
                return true;  
           /* dir = StringExtend.trimStart(dir, "/");  
            dir = StringExtend.trimEnd(dir, "/");  */
            String[] arr =  dir.split("/");  
            StringBuffer sbfDir=new StringBuffer();  
            //循环生成子目录  
            for(String s : arr){  
                sbfDir.append("/");  
                sbfDir.append(s);  
                //目录编码，解决中文路径问题  
                d = new String(sbfDir.toString().getBytes("GBK"),"iso-8859-1");  
                //尝试切入目录  
                if(ftp.changeWorkingDirectory(d))  
                    continue;  
                if(!ftp.makeDirectory(d)){  
                    System.out.println("[失败]ftp创建目录："+sbfDir.toString());  
                    return false;  
                }  
                System.out.println("[成功]创建ftp目录："+sbfDir.toString());  
            }  
            //将目录切换至指定路径  
            return ftp.changeWorkingDirectory(d);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  
      
  
  
    /** 
     * 
     * 销毁ftp连接 
     * 
     */  
    private void closeFtpConnection() {  
        _isLogin = false;  
        if (ftp != null) {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.logout();  
                    ftp.disconnect();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
  
  
    /** 
     * 
     * 销毁ftp连接 
     * 
     */  
    @Override  
    public void close() {  
        this.closeFtpConnection();  
    }
    
    /* 
     * 从FTP服务器下载文件 
     *  
     * @param ftpHost FTP IP地址 
     *  
     * @param ftpUserName FTP 用户名 
     *  
     * @param ftpPassword FTP用户名密码 
     *  
     * @param ftpPort FTP端口 
     *  
     * @param ftpPath FTP服务器中文件所在路径 格式： ftptest/aa 
     *  
     * @param localPath 下载到本地的位置 格式：H:/download 
     *  
     * @param fileName 文件名称 
     */  
    public void downloadFtpFile(String ftpPath, String localPath,  
            String fileName) {  
        try {  
        	ftp.setControlEncoding("UTF-8"); // 中文支持  
        	ftp.setFileType(FTPClient.BINARY_FILE_TYPE);  
        	ftp.enterLocalPassiveMode();  
        	ftp.changeWorkingDirectory(ftpPath);  
            File localFile = new File(localPath + File.separatorChar + fileName);
            if(localFile.exists()){
            	localFile.createNewFile();
            }
            OutputStream os = new FileOutputStream(localFile);  
            ftp.retrieveFile(fileName, os);  
            os.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}  
