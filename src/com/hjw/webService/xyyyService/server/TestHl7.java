package com.hjw.webService.xyyyService.server;

import java.nio.ByteBuffer;

import com.hjw.webService.xyyyService.util.MessageTypeStart;

public class TestHl7 {

	public static void main(String[] args) {
		String str="MSH|^~\\&|LIS|10|ESB|10|20180604143425489||OUL^R24^OUL_R24|4512346|P|2.5.1|||AL|AL|CHN|UNICODE UTF-8\r"
				+"PID|||000844484600^^^^21~^^^^51~^^^^1~^^^^25||何晓|||||||||||\r"
				+"PV1|1|T|^^^10||||00223^张  晋||||||||||||^1|||||||||||||||||||||||||0000\r"
				+"OBR|1|9401751^LIS^09201806013171^LIS||301^免疫定量报告||20180604111116|20180604111116|20180604141438||||||||00223^张  晋||||||20180604142745|||2|||||||00000&全院|51931&杜娟|51835&吴晶^^^临床检验\r"
				+"SPM|1|||C^血清\r"
				+"OBX|0|ST|4040010101^*癌胚抗原测定^^CEA^*癌胚抗原测定||1.82|^ug/L|0.0000-10.0000||||0|||20180604142745\r"
				+"OBX|1|ST|4040020101^*甲胎蛋白测定^^AFP^*甲胎蛋白测定||1.96|^ug/L|0.0000-20.0000||||0|||20180604142745\r"
				+"OBX|2|ST|4040050101^*总前列腺特异性抗原测定^^TPSA^*总前列腺特异性抗原测定(TPSA)||1.34|^ug/L|0.0000-4.4000||||0|||20180604142745\r"
				+"OBX|3|ST|4040060101^游离前列腺特异性抗原测定^^FPSA^游离前列腺特异性抗原测定(FPSA)||0.33|^ug/L|0.0000-1.3000||||0|||20180604142745\r"
				+"OBX|4|ST|4040070101^游离和总前列腺特异抗原比值^^F/TPSA^游离和总前列腺特异抗原比值||0.25||0.0000-1.0000||||0|||20180604142745\r"
                +"\r"
		+"MSH|^~\\&|LIS|10|ESB|10|20180604143425||OUL^R24^OUL_R24|4512348|P|2.5.1|||AL|AL|CHN|UNICODE UTF-8\r"
		+"PID|||000844482900^^^^21~^^^^51~^^^^1~^^^^25||温建国|||||||||||\r"
		+"PV1|1|T|^^^10||||00223^张  晋||||||||||||^1|||||||||||||||||||||||||0000\r"
		+"OBR|1|9401752^LIS^09201806013273^LIS||301^免疫定量报告||\r";
		try {
   		 String sendString=str;
   		 String msh="";
   		 String mshg="";
   		 String[] p=new String[]{};
   		 String[] pstrs=new String[]{};
   		 if(sendString.contains(new String(new byte[]{0X1C}))){
   			 p=sendString.split(new String(new byte[]{0X1C}));
   			 for(int k=0;k<=p.length-1;k++){
   				 if(p[k].contains(new String(new byte[]{0x0b}))){
   					pstrs= p[k].split(new String(new byte[]{0x0b}));
   					 if(pstrs[1].contains(new String(new byte[]{0x0d}))){
        		    		 msh=pstrs[1].substring(0, pstrs[1].indexOf(new String(new byte[]{0x0d})));
        		    		 mshg=pstrs[1].substring(pstrs[1].indexOf(new String(new byte[]{0x0d})));
        		    		 }
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
        		    	 //   TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+sendString1+"\r\n");
        		 	     	// sendString1=doSomething(sendString1,"2111sss");
        		 	     	//sendString1=MessageTypeStart.startType(sendString1,"2111sss");
        		 	     		  
        		 	     	  System.out.println(sendString1);
        		    	       StringBuilder sb=new StringBuilder();
        		    	       sb.append(new String(new byte[] { 0x0b }));
        		    	       sb.append(sendString1);
        		    	       sb.append(new String(new byte[] { 0x1c, 0x0d }));
        		    	        ByteBuffer clientBuffer=ByteBuffer.wrap(sb.toString().getBytes("UTF-8"));        
        		    	      //  socket.write(clientBuffer, clientBuffer, new AioWriteHandler(socket));
   				 }
    		       
   			 }
   		 }    		
		} catch (Exception e) {
			e.printStackTrace();
		}
   	}
	

}
