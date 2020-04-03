package com.hjw.webService.client.dbgj.client;

public class Test {
	
	public static void main(String[] args) {
		String url="http://192.168.111.46:8088/services/Mirth";
		try{
			DefaultAcceptMessageService dam = new DefaultAcceptMessageServiceLocator(url);
			DefaultAcceptMessage dams = dam.getDefaultAcceptMessagePort();
			String messages = dams.acceptMessage("String ddd");
			System.out.print(messages);
			}catch(Exception ex){
				ex.printStackTrace();
			}
	}	
}
