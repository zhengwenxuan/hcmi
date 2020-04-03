package com.hjw.webService.client.bjxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.bjxy.util.BJXYhl7;
import com.hjw.webService.client.bjxy.util.XyyyClient;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;

public class PacsDelInterface {
private PacsMessageBody lismessage;
	
	public PacsDelInterface(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	public ResultPacsBody getMessage(String logname) throws IOException {
		ResultPacsBody rb = new ResultPacsBody();
		ControlActPacsProcess con= new ControlActPacsProcess();
		List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
		List<PacsComponents> components=lismessage.getComponents();
		for(int i=0;i<lismessage.getComponents().size();i++){
			boolean flags=true;
			PacsComponents liscoms=new PacsComponents();
			for(int j=0;j<lismessage.getComponents().get(i).getPacsComponent().size();j++){
				String strs=BJXYhl7.dftP03hl7(lismessage, "P03","MR",i,j,logname);
				liscoms=components.get(i);
				ResultHeader rh;
				try {
					new XyyyClient().talkFee(strs,"PACSDELLFEE");
					String str=BJXYhl7.omgO19hl7(lismessage, "O19","CA",i,j,logname);
					rh = new XyyyClient().talkFee(str,"PACSDELAPPLY");
					if(!rh.getTypeCode().equals("AA")){
						flags=false;
						break;
					}
					
				} catch (IOException e) {
					rh=new ResultHeader();
					rh.setTypeCode("AE");
					rh.setText("发送失败");
				}
			}
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
