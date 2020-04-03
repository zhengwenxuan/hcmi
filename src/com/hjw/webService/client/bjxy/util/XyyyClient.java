package com.hjw.webService.client.bjxy.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;

import oracle.sql.DATE;


public class XyyyClient {
  //private String host="localhost";
   //private int port=9008;
	private String host="10.2.30.70";
	private int port=9945;
    private static Socket socket;
    public XyyyClient()throws IOException{
        socket = new Socket(host,port);
        socket.setSoTimeout(30000);
    }
    
	public static ResultBody talk(String str,String type){
		String req1="";
		String req=str;
		String res="";
		String res1="";
		ResultBody rb = new ResultBody();
		OutputStream os=null;
		InputStream is=null;
		BufferedReader br=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			 os = socket.getOutputStream();
			str=new String(new byte[] { 0x0b })+str+new String(new byte[] { 0x1c, 0x0d });
			os.write(str.getBytes("utf-8"));
			req1=sdf.format(new Date());
			//pw.write(new String(new byte[] { 0x0b },"utf-8") + new String(str.getBytes("utf-8"),"utf-8") + new String(new byte[] { 0x1c, 0x0d },"utf-8"));
			os.flush();
			 is = socket.getInputStream();
			 br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
			String info;
			StringBuilder sb = new StringBuilder();
			
			while ((info = br.readLine()) != null) {
				sb.append(info+"\r");
				System.out.println("我是客户端，服务器说：" + info);
				if (info.contains((new String(new byte[] { 0x1C })))) {
					res1=sdf.format(new Date());
					String[] str1=sb.toString().split(new String(new byte[]{0x1C},"utf-8"));
					if(str1[0].contains((new String(new byte[]{0x0b},"utf-8")))){
		     			  String[] str2=str1[0].split(new String(new byte[]{0x0b},"utf-8"));
		     			 res=str2[1];
		     		String	 msh=res.substring(0, res.indexOf(new String(new byte[]{0x0d})));
     		    	String	 mshg=res.substring(res.indexOf(new String(new byte[]{0x0d})));
     		    	if(msh.contains("MSH")){
			  			 String[] strinfos=msh.split("\\|");
			  			 if(!strinfos[6].equals("")){
			  				strinfos[6]=strinfos[6].substring(0, 14);
			  			 }
			  			 StringBuilder sbstr=new StringBuilder();
			  			 for(int i=0;i<strinfos.length;i++){
			  				 sbstr.append(strinfos[i]);
			  				 if((i+1)!=strinfos.length){
			  					 sbstr.append("|");
			  				 }
			  			 }
			  			msh=sbstr.toString();
			  		 }
		    	     String  sendString1=msh+mshg;

		 	     			rb = BJXYResMsg.resMsg(sendString1);

		     		  }
		     		  break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			res="AE";
		}finally {
			try {
				br.close();
				is.close();
				//pw.close();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		TranLogTxt.liswriteEror_to_txt("xyyy"+type+"ClientLog",req1+"req:"+req+"\r\n"+res1+"res:"+res);
		return rb;
	}
	public static ResultHeader talkFee(String str,String type) {
		TranLogTxt.liswriteEror_to_txt("xyyy_shengqingdan",str);
		String req1="";
		String req=str;
		String res="";
		String res1="";
		OutputStream os=null;
		InputStream is=null;
		BufferedReader br=null;
		ResultHeader rb = new ResultHeader();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			 os = socket.getOutputStream();
			str=new String(new byte[] { 0x0b })+str+new String(new byte[] { 0x1c, 0x0d });
			os.write(str.getBytes("utf-8"));
			req1=sdf.format(new Date());
			//pw.write(new String(new byte[] { 0x0b },"utf-8") + new String(str.getBytes("utf-8"),"utf-8") + new String(new byte[] { 0x1c, 0x0d },"utf-8"));
			os.flush();
			 is = socket.getInputStream();
			 br = new BufferedReader(new InputStreamReader(is, Charset.forName("utf-8")));
			String info;
			StringBuilder sb = new StringBuilder();
			
			while ((info = br.readLine()) != null) {
				sb.append(info+"\r");
				System.out.println("我是客户端，服务器说：" + info);
				if (info.contains((new String(new byte[] { 0x1C })))) {
					res1=sdf.format(new Date());
					String[] str1=sb.toString().split(new String(new byte[]{0x1C},"utf-8"));
					if(str1[0].contains((new String(new byte[]{0x0b},"utf-8")))){
		     			  String[] str2=str1[0].split(new String(new byte[]{0x0b},"utf-8"));
		     			  res=str2[1];
		     			 String	 msh=res.substring(0, res.indexOf(new String(new byte[]{0x0d})));
		     		    	String	 mshg=res.substring(res.indexOf(new String(new byte[]{0x0d})));
		     		    	if(msh.contains("MSH")){
					  			 String[] strinfos=msh.split("\\|");
					  			 if(!strinfos[6].equals("")){
					  				strinfos[6]=strinfos[6].substring(0, 14);
					  			 }
					  			 StringBuilder sbstr=new StringBuilder();
					  			 for(int i=0;i<strinfos.length;i++){
					  				 sbstr.append(strinfos[i]);
					  				 if((i+1)!=strinfos.length){
					  					 sbstr.append("|");
					  				 }
					  			 }
					  			msh=sbstr.toString();
					  		 }
				    	     String  sendString1=msh+mshg;
		     			 rb = BJXYResMsg.resFeeMsg(sendString1);
		     			 
		     			TranLogTxt.liswriteEror_to_txt("xyyyLisreq","req:"+rb.getTypeCode());
		     		  }
		     		  break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			res="AE";
		}finally {
			try {
				br.close();
				is.close();
				//pw.close();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		TranLogTxt.liswriteEror_to_txt("xyyy"+type+"ClientLog",req1+"req:"+req+"\r\n"+res1+"res:"+res);
		return rb;
	}
}
