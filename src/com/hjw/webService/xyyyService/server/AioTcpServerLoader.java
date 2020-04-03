package com.hjw.webService.xyyyService.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AioTcpServerLoader implements ServletContextListener{
		private AioTcpServer tcpServer;
		@Override
		public void contextDestroyed(ServletContextEvent arg0) {
			if(null!=tcpServer && !tcpServer.isInterrupted())  
	        {  
				return;
	        }  
		}

		@Override
		public void contextInitialized(ServletContextEvent arg0) {
			if(null==tcpServer)  
	        {   
				try {
			         //新建线程类 
					//tcpServer=new AioTcpServer(8877);
					tcpServer=new AioTcpServer(8899);
					//启动线程  
					tcpServer.start(); 
				} catch (Exception e) {
					e.printStackTrace();
				}  
	        }
		}

}
