package com.hjw.webService.client.bjxy;

import java.io.IOException;

import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;

public class CostApplyInterface {
	private FeeMessage feeMessage;

	public CostApplyInterface(FeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}
	
	public FeeResultBody getMessage(String logname) throws IOException {
		FeeResultBody rb = new FeeResultBody();
		//String str=BJXYhl7.dftP03hl7(feeMessage, "P03","MC");
		//ResultHeader rh=new XyyyClient().talkFee(str);
		//rb.setResultHeader(rh);
		return rb;
	}

}
