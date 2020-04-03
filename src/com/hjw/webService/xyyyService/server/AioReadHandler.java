package com.hjw.webService.xyyyService.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.xyyyService.util.MessageTypeStart;


//这里的参数型号，受调用它的函数决定。这里是受客户端socket.read调用
public class AioReadHandler implements CompletionHandler<Integer,ByteBuffer>
{ 
    private AsynchronousSocketChannel socket; 
    private String logname="xyAllLog";
    private final int slen=204800;
    public  byte[] bmsg=new byte[slen];  
    private int stflag=0;
    public AioReadHandler(AsynchronousSocketChannel socket) { 
        this.socket = socket; 
    }     
    private CharsetDecoder decoder = Charset.forName("iso8859-1").newDecoder();  
    
    @Override
	public void completed(Integer i, ByteBuffer buf) {
		if (i > 0) {
			buf.flip();
			if(buf != null) {
				try {
					String msg="";
					
					//TranLogTxt.liswriteEror_to_txt(logname,buf.position()+"-"+buf.limit()+"-"+buf.hasRemaining());
					msg = decoder.decode(buf).toString();
					//TranLogTxt.liswriteEror_to_txt(logname,buf.position()+"-"+buf.limit()+"-"+buf.hasRemaining());
					TranLogTxt.liswriteEror_to_txt(logname, "收到" + socket.getRemoteAddress().toString() + "的消息:" + msg);
					//TranLogTxt.liswriteEror_to_txt(logname, buf.limit()+"");
					getMsg(buf,i);       
					buf.compact();
				} catch (Exception e) {
					e.printStackTrace();
				}
				socket.read(buf, buf, this);
			}
			//TranLogTxt.liswriteEror_to_txt(logname, "收到消息:" + msg);
			/*try {
				write(socket);
			} catch (Exception ex) {
				Logger.getLogger(AioReadHandler.class.getName()).log(Level.SEVERE, null, ex);
			}*/
		} else if (i == -1) {
			try {
				System.out.println("客户端断线:" + socket.getRemoteAddress().toString());
				buf = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
         System.out.println("cancelled"); 
    }
 
     //不是CompletionHandler的方法
    public void write(AsynchronousSocketChannel socket,String msg){
    	if(msg.length()>0){
    	try{
         		    	     String  sendString1=msg;
         		    	     TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+sendString1+"\r\n");
         		 	     	 sendString1=doSomething(sendString1,logname);
         		    	     StringBuilder sb=new StringBuilder();
         		    	     sb.append(new String(new byte[] { 0x0b }));
         		    	     sb.append(sendString1);
         		    	     sb.append(new String(new byte[] { 0x1c, 0x0d }));
         		    	     ByteBuffer clientBuffer=ByteBuffer.wrap(sb.toString().getBytes("UTF-8"));      
         		    	     socket.write(clientBuffer, clientBuffer, new AioWriteHandler(socket));
    				
		} catch (Exception e) {
			 TranLogTxt.liswriteEror_to_txt(logname,"\r\n"+com.hjw.interfaces.util.StringUtil.formatException(e)+"\r\n");
			e.printStackTrace();
		}
    	}
    }
	  private  String doSomething(String ret,String logname) {
	       String str=MessageTypeStart.startType(ret,logname);
	        return str;
	    }

	  private void getMsg(ByteBuffer byteBuffer,int len){			
	        	try{
	        		for(int i=0;i<byteBuffer.limit();i++) { 
	        		    byte by = byteBuffer.get(i);
	       			//System.out.println(m);
	       			if(by==0x0b){	       				
	       				stflag=0;
	       				bmsg=new byte[204800];
	       				//bmsg[stflag]=by;
	       				//stflag++;
	       				
	       			}else if (by==0x1c){
	       				//bmsg[stflag]=by;	
	       				String msg="";
	       				//System.out.println(stflag);
	       				msg =bytesToStringCN(bmsg).trim();
	       				//System.out.println(msg);
	       				TranLogTxt.liswriteEror_to_txt(logname, "完整信息:" + msg);		       				       				
	       				write(socket,msg);
	       				stflag=0;       				
	       				bmsg=new byte[204800]; 	       				
	       			}else{
	       				bmsg[stflag]=by;
	       				stflag++;
	       			}
	       		}
	        	}catch(Exception ex){
	        		ex.printStackTrace();
	        	}	       		
	        }
	        
	        public static String bytesToStringCN(byte[] b) {
	        	String resu="";
	        	try{
	        	  CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();   
	        	  ByteBuffer clientBuffer=ByteBuffer.wrap(b);   
	        	  resu=decoder.decode(clientBuffer).toString();  
	        	}catch(Exception ex){
	        		
	        	}
	    		return resu;
	    	}
}
