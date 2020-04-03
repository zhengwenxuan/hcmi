package com.hjw.webService.client.nanfeng.util;

import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.nanfeng.bean.GuidNf;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetGUID {
	
	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}
	
	public static String getGUID() {
		
		String sql = "select newid() as guid ";
		
		List<GuidNf> guidL = jdbcQueryManager.getList(sql, GuidNf.class);
		System.out.println(guidL.get(0).getGuid());
		return guidL.get(0).getGuid();
	}

}
