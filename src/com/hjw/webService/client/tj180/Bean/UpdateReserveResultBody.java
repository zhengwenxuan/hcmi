package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class UpdateReserveResultBody {
	    private String reserveId="";//	体检预约号
	    
		private List<UpdateReserveResultBean> resultInfo=new ArrayList<UpdateReserveResultBean>();

		public String getReserveId() {
			return reserveId;
		}

		public void setReserveId(String reserveId) {
			this.reserveId = reserveId;
		}

		public List<UpdateReserveResultBean> getResultInfo() {
			return resultInfo;
		}

		public void setResultInfo(List<UpdateReserveResultBean> resultInfo) {
			this.resultInfo = resultInfo;
		}      
		
 }
