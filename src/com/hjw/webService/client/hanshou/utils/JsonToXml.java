package com.hjw.webService.client.hanshou.utils;

import net.sf.json.JSONObject;

public class JsonToXml {

	
	
	public static void main(String[] args) {
		String jsonStr = "{\"p\":{\"p1\":{\"p2\":{\"id\":2222,\"name\":\"xiao2\"}},\"name\":\"xiao3\"},\"id2\":\"iii\",\"name2\":\"ffff\"}";
		System.out.println("jsonStr:"+jsonStr);
		System.out.println(Json2XmlString(jsonStr));
	}
	
	
	public static String Json2XmlString(String jsonStr){
 		System.out.println("json转换成xmlString:");
 		JSONObject json = JSONObject.fromObject(jsonStr);
 		StringBuffer sb = new StringBuffer("<xml>");
     	for(Object key : json.keySet()){
     		sb.append("<").append(key).append(">");     		
     		Object value = json.get(key);
     		sb.append(iteraorJson(value));
     		sb.append("</").append(key).append(">");
     	}
     	sb.append("</xml>");
     	return sb.toString();
     }


	private static Object iteraorJson(Object value) {
		StringBuffer sb = new StringBuffer("");
 		if((value.toString().contains(":"))){
 			JSONObject json = ((JSONObject)value);
 			for(Object key : json.keySet()){
    			sb.append("<").append(key).append(">");
    			Object value2 = json.get(key);
    			sb.append(iteraorJson(value2));
    			sb.append("</").append(key).append(">");
    		} 			
 		}else{
 			sb.append(value);
 		} 		
 		return sb.toString();

	}

}
