package com.hjw.webService.client.bjxy;

import java.io.IOException;

import com.hjw.webService.client.bjxy.util.BJXYhl7;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;

public class CostDelInterface {
	private DelFeeMessage feeMessage;

	public CostDelInterface(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}
	
	public FeeResultBody getMessage() throws IOException {
		FeeResultBody rb = new FeeResultBody();
		//String str=BJXYhl7.dftP03hl7(feeMessage, "P03","MR");
		/*ResultHeader rh=AioTcpClient.send(str);
		rb.setResultHeader(rh);*/
		return rb;
	}

}
