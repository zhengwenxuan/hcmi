package com.hjw.webService.client.hzty;

import com.hjw.webService.client.hzty.Bean.RENYUANMXCXIN;
import com.hjw.webService.client.hzty.Bean.RENYUANZCIN;
import com.hjw.webService.client.hzty.Bean.RyCxResultBody;
import com.hjw.webService.client.hzty.Bean.RyZcResultBody;

public class TestRyCx {
	public static void main(String[] args) {
		RENYUANMXCXIN ry = new RENYUANMXCXIN();
		ry.setCHAXUNHM("612322197710141716");		
		RenyuanCx ryc = new RenyuanCx(ry);
		RyCxResultBody rzb = ryc.getMessage("http://192.168.254.176:9099/MediInfoHis.svc", "testrycx");
	}

}
