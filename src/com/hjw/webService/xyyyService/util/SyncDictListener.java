package com.hjw.webService.xyyyService.util;

import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class SyncDictListener implements ServletContextListener{
	private SyncDictThread syncDict;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(null!=syncDict)  
        {  
			return;
        }  
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		if(null==syncDict)  
        {   
			try {
				Timer t = new Timer(); // 建立Timer对象
				syncDict=new SyncDictThread();
				Calendar cal = Calendar.getInstance();   
				cal.set(Calendar.HOUR_OF_DAY, 24);  
				cal.set(Calendar.MINUTE, 0);  
				cal.set(Calendar.SECOND, 0);  
				t.schedule(syncDict, cal.getTime(),24*60*60*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}  
        }
	}

}
