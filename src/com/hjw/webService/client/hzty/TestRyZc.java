package com.hjw.webService.client.hzty;

import com.hjw.webService.client.hzty.Bean.RENYUANZCIN;
import com.hjw.webService.client.hzty.Bean.RyZcResultBody;

public class TestRyZc {
	public static void main(String[] args) {
		RENYUANZCIN ry = new RENYUANZCIN();
		ry.setZHENGJIANHM("612322197910141716");
		ry.setXINGMING("杨明");
		ry.setXINGBIE("男");
		ry.setCHUSHENGRQ("1979-10-14");

		RenyuanZc ryc = new RenyuanZc(ry);

		RyZcResultBody rzb = ryc.getMessage("http://192.168.254.176:9099/MediInfoHis.svc", "testryzc");
	}

}
