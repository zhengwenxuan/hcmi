package com.hjw.webService.client.bjxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.bjxy.util.BJXYhl7;
import com.hjw.webService.client.bjxy.util.XyyyClient;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;

public class LisApplyInterface {
private LisMessageBody lismessage;
	
	public LisApplyInterface(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	public ResultLisBody getMessage(String logname) throws IOException {
		ResultLisBody rb = new ResultLisBody();
		if(this.lismessage.getCustom().getPersonid()==null||this.lismessage.getCustom().getPersonid().trim().length()<=0)
		{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检对应病人id无效");
		} else {
			List<LisComponents> components = lismessage.getComponents();
			ControlActLisProcess con = new ControlActLisProcess();
			List<ApplyNOBean> appList = new ArrayList<ApplyNOBean>();
			for (int i = 0; i < components.size(); i++) {
				LisComponents liscoms = new LisComponents();
				liscoms = components.get(i);
				List<LisComponent> component = liscoms.getItemList();
				boolean flags = true;
				for (int j = 0; j < component.size(); j++) {

					String str = BJXYhl7.omlO21hl7(lismessage, "O21", "SC", i, j, logname);//NW
					ResultHeader rh = new XyyyClient().talkFee(str, "LISAPPLY");

					if (!"AA".equals(rh.getTypeCode())) {
						flags = false;
						break;
					}
				}
				if (flags) {
					ApplyNOBean aob = new ApplyNOBean();
					aob.setApplyNO(liscoms.getReq_no());
					appList.add(aob);
				}
			}
			con.setList(appList);
			rb.setControlActProcess(con);
			rb.getResultHeader().setTypeCode("AA");
		}
		return rb;
	}
}
