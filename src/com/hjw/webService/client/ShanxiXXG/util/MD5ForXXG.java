package com.hjw.webService.client.ShanxiXXG.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

import com.hjw.util.EncryptUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MD5ForXXG {

	
	public static String getToken(String xxg_key,String fromtype,String time) {
		String token = "";
		String key_token = xxg_key+fromtype+time;
		token = EncryptUtils.encryptMD5(key_token);
		return token;
	}
	
	public static String getSessionId(String reqType) {
		StringBuffer seesionid = new StringBuffer();
		String time = String.valueOf(new Date().getTime());
		int randomnum = getRandomNum();
		seesionid = seesionid.append(reqType);
		seesionid = seesionid.append(time);
		seesionid = seesionid.append(randomnum);
		return seesionid.toString();
	}
	
	/**
          * 随机生成六位数验证码
     * @return
     */
    public static int getRandomNum(){
        Random r = new Random();
        return r.nextInt(900000)+100000;//(int)(Math.random()*999999)
    }
    
    // 加密
 	public static String getBase64(String str) {
 		byte[] b = null;
 		String s = null;
 		try {
 			b = str.getBytes("utf-8");
 		} catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		if (b != null) {
 			s = new BASE64Encoder().encode(b);
 		}
 		return s;
 	}

 	// 解密
 	public static String getFromBase64(String s) {
 		byte[] b = null;
 		String result = null;
 		if (s != null) {
 			BASE64Decoder decoder = new BASE64Decoder();
 			try {
 				b = decoder.decodeBuffer(s);
 				result = new String(b, "utf-8");
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 		}
 		return result;
 	}
    
	public static void main(String[] args) {
		String xxg_key = "1234567";
		String time = String.valueOf(new Date().getTime());
		String fromtype = "sijdfowjfiwsfsdfffffefgdderegxzxss";
		System.out.println(getToken(xxg_key,fromtype,time));
		
		System.out.println(getSessionId("XXGNP"));
	}
	
}
