package com.hjw.webService.client.haijie.queue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.haijie.util.TokenUtil;

	public class QueueHttpUtil {

	    private static final String QUEUE_SERVER_URL = "http://localhost:8050/api";
	    private static final String CHARSET = "UTF-8";
	    //private static final String OPERATOR_CODE = "admin";
	    //private static final Long APPOINTMENT_ID = 136777L;
	    private static final String APPOINTMENT_CODE = "0136455";

	    private static String token = "";

	    private static String readResponseString(HttpURLConnection conn) {
	        BufferedReader reader = null;
	        try {
	            StringBuilder ret;
	            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
	            String line = reader.readLine();
	            if (line != null) {
	                ret = new StringBuilder();
	                ret.append(line);
	            } else {
	                return "";
	            }

	            while ((line = reader.readLine()) != null) {
	                ret.append('\n').append(line);
	            }
	            return ret.toString();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

	    public void getToken(String baseurl,String user,String passwd,String logname) throws Exception {
	        //String apiUrl = QUEUE_SERVER_URL + "/operator/login";

//	        String loginData = "{code:'" + user + "', password:'"+passwd+"'}";
//	        TranLogTxt.liswriteEror_to_txt(logname, "res:" + baseurl);
//	        TranLogTxt.liswriteEror_to_txt(logname, "res:" + loginData);
//	        URL url = new URL(baseurl);
//	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//	        conn.setRequestMethod("POST");
//	        conn.setDoOutput(true);
//	        conn.setDoInput(true);
//
//	        conn.setConnectTimeout(19000);
//	        conn.setReadTimeout(19000);
//
//	        conn.setRequestProperty("Content-Type", "application/json");
//	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
//
//	        conn.connect();
//
//	        OutputStream out = conn.getOutputStream();
//	        out.write(loginData.getBytes(CHARSET));
//	        out.flush();
//	        out.close();
//
//	        Map<String, List<String>> map = conn.getHeaderFields();
//
//	        token = map.get("loginToken").get(0);
//
//	        System.out.println("token:\t" + token);
	    	String myIP = InetAddress.getLocalHost().getHostAddress();
	    	TranLogTxt.liswriteEror_to_txt(logname, "myIP:" + myIP);
	    	token = TokenUtil.createToken(user, myIP);
	    	TranLogTxt.liswriteEror_to_txt(logname, "token:" + token);
	    	System.out.println("token:\t" + token);
	    }

	    public String addToQueue(String apiUrl,String OPERATOR_CODE,String uncodestr) throws Exception {
	        //String apiUrl = QUEUE_SERVER_URL + "/queue/addToQueue/" + APPOINTMENT_ID;

	        URL url = new URL(apiUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);

	        conn.setConnectTimeout(19000);
	        conn.setReadTimeout(19000);

	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

	        conn.setRequestProperty("userCode", OPERATOR_CODE);
	        conn.setRequestProperty("loginToken", token);

	        conn.connect();

	        String result = readResponseString(conn);
	        System.out.println("result:\t" + result);

	        Map<String, List<String>> map = conn.getHeaderFields();
	        token = map.get("loginToken").get(0);
	        System.out.println("token:\t" + token);
	        return result;
	    }

	    public String updateAppointmentInfo(String apiUrl,String OPERATOR_CODE,String uncodestr) throws Exception {
	        //String apiUrl = QUEUE_SERVER_URL + "/queue/updateAppointmentInfo/" + APPOINTMENT_ID;

	        URL url = new URL(apiUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);

	        conn.setConnectTimeout(19000);
	        conn.setReadTimeout(19000);

	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

	        conn.setRequestProperty("userCode", OPERATOR_CODE);
	        conn.setRequestProperty("loginToken", token);

	        conn.connect();

	        String result = readResponseString(conn);
	        System.out.println("result:\t" + result);

	        Map<String, List<String>> map = conn.getHeaderFields();
	        token = map.get("loginToken").get(0);
	        System.out.println("token:\t" + token);
	        return result;
	    }

	    public String next(String apiUrl,String OPERATOR_CODE,String exam_id,String uncodestr) throws Exception {
	        //String apiUrl = QUEUE_SERVER_URL + "/queue/nextQueue";

	        String nextData = "{code:'" + exam_id + "'}";

	        URL url = new URL(apiUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	        conn.setDoInput(true);

	        conn.setConnectTimeout(19000);
	        conn.setReadTimeout(19000);

	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

	        conn.setRequestProperty("userCode", OPERATOR_CODE);
	        conn.setRequestProperty("loginToken", token);

	        conn.connect();

	        OutputStream out = conn.getOutputStream();
	        out.write(nextData.getBytes(CHARSET));
	        out.flush();
	        out.close();

	        String result = readResponseString(conn);
	        System.out.println(result);

	        Map<String, List<String>> map = conn.getHeaderFields();
	        token = map.get("loginToken").get(0);
	        System.out.println("token:\t" + token);
	        return result;
	    }

	    public static void main(String[] args) throws Exception {
	    	String tokenurl="http://IP:Port/api/operator/login&admin&admin";
	    	String[] tokenurls= tokenurl.split("&");
	    	System.out.println(tokenurls[0]);
			System.out.println(tokenurls[1]);
			
	       /* getToken();
	        Thread.sleep(100);
	        addToQueue();
	        Thread.sleep(100);
	        findQueueByAppointment();
	        Thread.sleep(100);
	        updateAppointmentInfo();
	        Thread.sleep(100);
	        next();
	        Thread.sleep(100);
	        updateAppointmentInfo();
	        Thread.sleep(100);
	        next();
	        Thread.sleep(100);
	        findQueueByAppointment();
	        Thread.sleep(100);
	        next();
	        Thread.sleep(100);
	        next();
	        Thread.sleep(100);
	        next();
	        Thread.sleep(100);
	        findQueueByAppointment();
	        Thread.sleep(100);
	        next();
	        Thread.sleep(100);
	        next();
	        Thread.sleep(100);
	        next();*/
	    }
	}

