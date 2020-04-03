package com.hjw.webService.client.bjxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.bjxy.util.BJXYhl7;
import com.hjw.webService.client.bjxy.util.QuerySqlData;
import com.hjw.webService.client.bjxy.util.XyyyClient;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;

public class LisDelInterface {
private LisMessageBody lismessage;
	
	public LisDelInterface(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	public ResultLisBody getMessage(String logname) throws IOException {
		ResultLisBody rb = new ResultLisBody();
		List<LisComponents> components=lismessage.getComponents();
		ControlActLisProcess con= new ControlActLisProcess();
		List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
		for(int i=0;i<components.size();i++){
			LisComponents liscoms= new LisComponents();
			liscoms=components.get(i);
			boolean flags=true;
			
			if(flags){
				ApplyNOBean aob= new ApplyNOBean();
				aob.setApplyNO(liscoms.getReq_no());
				appList.add(aob);
			}
		}
		con.setList(appList);
		rb.setControlActProcess(con);
		rb.getResultHeader().setTypeCode("AA");
		return rb;
	}

}
