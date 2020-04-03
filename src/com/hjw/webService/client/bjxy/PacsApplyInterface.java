package com.hjw.webService.client.bjxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.bjxy.util.BJXYhl7;
import com.hjw.webService.client.bjxy.util.XyyyClient;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;


public class PacsApplyInterface {
	private PacsMessageBody lismessage;
	
	public PacsApplyInterface(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	public ResultPacsBody getMessage(String logname){
		ResultPacsBody rb = new ResultPacsBody();
		if(this.lismessage.getCustom().getPersonid()==null||this.lismessage.getCustom().getPersonid().trim().length()<=0)
		{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("体检对应病人id无效");
		} else {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String doctorid = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		ControlActPacsProcess con= new ControlActPacsProcess();
		List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
		if(!"0".equals(doctorid)){
			Doctor d = new Doctor();
			d=this.lismessage.getDoctor();
			d.setDoctorCode(doctorid);
			this.lismessage.setDoctor(d);
		}
		List<PacsComponents> components=lismessage.getComponents();
		for(int i=0;i<lismessage.getComponents().size();i++){
			boolean flags=true;
			PacsComponents liscoms=new PacsComponents();
			for(int j=0;j<lismessage.getComponents().get(i).getPacsComponent().size();j++){
				String str=BJXYhl7.omgO19hl7(lismessage, "O19","NW",i,j,logname);
				liscoms=components.get(i);
				ResultHeader rh;
				try {
					rh = new XyyyClient().talkFee(str,"PACSAPPLY");
						if(!rh.getTypeCode().equals("AA")){
							flags=false;
							break;
						}
						String strs=BJXYhl7.dftP03hl7(lismessage, "P03","MC",i,j,logname);
						new XyyyClient().talkFee(strs,"PACSFEE");
						
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
		}
		return rb;
	}
}
