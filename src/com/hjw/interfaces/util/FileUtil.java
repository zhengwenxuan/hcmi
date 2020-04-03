package com.hjw.interfaces.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
	  /** 
      * 根据byte数组，生成文件 
      */  
    public static boolean saveFile(byte[] bfile, String filePath,String fileName) {  
       BufferedOutputStream bos = null;  
       FileOutputStream fos = null;  
	   File file = null;  
	   boolean fileflag=false;
	   try {  
	       File dir = new File(filePath);  
	       if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
	           dir.mkdirs();  
	       }  
	       file = new File(filePath+"\\"+fileName);  
	       fos = new FileOutputStream(file);  
	       bos = new BufferedOutputStream(fos);  
	       bos.write(bfile);  
	       fileflag=true;
	   } catch (Exception e) {  
	       e.printStackTrace();  
	   } finally {  
	       if (bos != null) {  
	           try {  
	               bos.close();  
	           } catch (IOException e1) {  
	               e1.printStackTrace();  
	           }  
	       }  
	       if (fos != null) {  
	           try {  
	               fos.close();  
	           } catch (IOException e1) {  
	               e1.printStackTrace();  
	           }  
	       }  
	   }
	   return fileflag;
    }	
    
    /**
     * 根据文件流判断图片类型
     * @param fis
     * @return jpg/png/gif/bmp
     */
	public static String getPicType(byte[] fis) {
		String TYPE_GIF = "gif";
		String TYPE_PNG = "png";
		String TYPE_BMP = "bmp";
		String TYPE_JPG = "jpg";
		String TYPE_UNKNOWN = "unknown";
		// 读取文件的前几个字节来判断图片格式
		try {
			byte[] b = new byte[4];
			for (int i = 0; i < 4; i++) {
				b[i] = fis[i];
			}

			String type = bytesToHexString(b).toUpperCase();
			if (type.contains("FFD8FF")) {
				return TYPE_JPG;
			} else if (type.contains("89504E47")) {
				return TYPE_PNG;
			} else if (type.contains("47494638")) {
				return TYPE_GIF;
			} else if (type.contains("424D")) {
				return TYPE_BMP;
			} else {
				return TYPE_UNKNOWN;
			}
		} catch (Exception ex) {
			return TYPE_UNKNOWN;
		}
	}
	
	 
    /**
     * byte数组转换成16进制字符串
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src){    
           StringBuilder stringBuilder = new StringBuilder();    
           if (src == null || src.length <= 0) {    
               return null;    
           }    
           for (int i = 0; i < src.length; i++) {    
               int v = src[i] & 0xFF;    
               String hv = Integer.toHexString(v);    
               if (hv.length() < 2) {    
                   stringBuilder.append(0);    
               }    
               stringBuilder.append(hv);    
           }    
           return stringBuilder.toString();    
       }
    
    /**
     * 
     * @param buffer
     * @param filePath
     * @return
     */
    public static boolean bytesToFile(byte[] buffer, final String filePath){
        boolean filename=false;
        File file = new File(filePath);
        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;
        try {
            output = new FileOutputStream(file);
            bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(buffer);
            filename=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(null!=bufferedOutput){
                try {
                    bufferedOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(null != output){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    return filename;
    }
}
