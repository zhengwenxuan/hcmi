package com.hjw.webService.client.hzty.Bean;

import com.hjw.webService.client.body.ResultHeader;


public class RyZcResultBody {

	private ResultHeader ResultHeader = new ResultHeader();

	private RENYUANZCOUT ControlActProcess = new RENYUANZCOUT();

	public ResultHeader getResultHeader() {
		return ResultHeader;
	}

	public void setResultHeader(ResultHeader resultHeader) {
		ResultHeader = resultHeader;
	}

	public RENYUANZCOUT getControlActProcess() {
		return ControlActProcess;
	}

	public void setControlActProcess(RENYUANZCOUT controlActProcess) {
		ControlActProcess = controlActProcess;
	}

}
