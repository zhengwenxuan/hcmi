package com.hjw.interfaces.util;

import java.util.Arrays;

public class StringUtil {
	
	 public static String subTextString(String str,int len){
	        if(str.length()<len/2)return str;
	        int count = 0;
	        StringBuffer sb = new StringBuffer();
	        String[] ss = str.split("");
	        for(int i=1;i<ss.length;i++){
	            count+=ss[i].getBytes().length>1?2:1;
	            sb.append(ss[i]);
	            if(count>=len)break;
	        }
	        //不需要显示...的可以直接return sb.toString();
	        return (sb.toString().length()<str.length())?sb.append("").toString():str;
	    }
	 
	 /**
	     * 
	         * @Title: checkString   
	         * @Description: 判断某个字符串是否在另外一个字符串数组里面   
	         * @param: @param resources
	         * @param: @param str
	         * @param: @param flagstr
	         * @param: @return      
	         * @return: boolean      
	         * @throws
	     */
	    public static boolean checkString(String resources,String flagstr,String str){
	    	if((resources==null)||(resources.trim().length()<=0)){
	    		return false;
	    	}else if((str==null)||(str.trim().length()<=0)){
	    		return false;	
	    	}else if((flagstr==null)||(flagstr.trim().length()<=0)){
	    		return false;	
	    	}else{
	    		boolean f=false;
	    		String[] res= resources.trim().split(flagstr.trim());
	    		for(String ress:res){
	    			if(str.trim().equals(ress.trim())){
	    				f=true;
	    				break;
	    			}
	    		}
	    		return f;
	    	}
	    }

	    public static String quotedStr(Object str) {
	    	if(str == null) return null;
	    	return "'"+str.toString().replaceAll("'", "''")+"'";
	    }

	    public static String formatException(Throwable e) {
	    	if(e == null) return null;
	    	return e.toString() +"\r\n"+ Arrays.toString(e.getStackTrace()).replace(",", "\r\n");
	    }
	    
	    public static boolean isEmpty(String str) {
	    	if (str != null && !"".equals(str.trim())) { 
	    		return false; 
	    	} 
	    	return true;
	    }
	    
	  
	    public static void main(String[] args) {
	    	
	          System.out.println(quotedStr("12'ttt"));  
	
	    }  
	    	  
}
