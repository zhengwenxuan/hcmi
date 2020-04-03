package com.hjw.webService.client.bjxy;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;

public class LisStatusInterface {
private LisMessageBody lismessage;
	
	public LisStatusInterface(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	public ResultLisBody getMessage(String url,String logname) {
		ResultLisBody rb = new ResultLisBody();
		List<String> list=new ArrayList<String>();
		/*list=LisInformationStr.getLisStatusList(lismessage);
		System.out.println("status:"+list.get(0));
		System.out.println("v2:"+list.get(1));*/
		return rb;
	}

}
