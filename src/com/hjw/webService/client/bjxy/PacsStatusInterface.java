package com.hjw.webService.client.bjxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;

public class PacsStatusInterface {
private PacsMessageBody lismessage;
	
	public PacsStatusInterface(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	public ResultPacsBody getMessage(String url,String logName, boolean debug) throws IOException {
		ResultPacsBody rb = new ResultPacsBody();
		List<String> list=new ArrayList<String>();
		/*list=PacsInformationStr.getPacsApplyList(lismessage);
		System.out.println("status:"+list.get(0));
		System.out.println("v2:"+list.get(1));*/
		return rb;
	}
}
