package com.hjw.webService.client.empty;

import java.util.ArrayList;
import java.util.List;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeResultBody;

public class FEETermSendMessageEmpty {
	private String accnum="";

	public FEETermSendMessageEmpty(String personid,String accnum) {
		this.accnum = accnum;
	}

	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		
		rb.getResultHeader().setTypeCode("AA");
		rb.getResultHeader().setText("");
		ReqId r= new ReqId();
		r.setReq_id(accnum);
		List<ReqId> list=new ArrayList<ReqId>();
		list.add(r);
		rb.getControlActProcess().setList(list);;
		
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + xml);
		return rb;
	}
}
