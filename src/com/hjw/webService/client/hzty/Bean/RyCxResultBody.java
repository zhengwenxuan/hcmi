package com.hjw.webService.client.hzty.Bean;

import com.hjw.webService.client.body.ResultHeader;


public class RyCxResultBody {

	private ResultHeader ResultHeader = new ResultHeader();

	private RENYUANMXCXOUT ControlActProcess = new RENYUANMXCXOUT();

	public ResultHeader getResultHeader() {
		return ResultHeader;
	}

	public void setResultHeader(ResultHeader resultHeader) {
		ResultHeader = resultHeader;
	}

	public RENYUANMXCXOUT getControlActProcess() {
		return ControlActProcess;
	}

	public void setControlActProcess(RENYUANMXCXOUT controlActProcess) {
		ControlActProcess = controlActProcess;
	}

}
