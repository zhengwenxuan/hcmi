package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class DeptBriefBody {
	    private String reserveId="";//	体检预约号
	    
		private List<DeptBriefBean> briefInfo=new ArrayList<DeptBriefBean>();

		public String getReserveId() {
			return reserveId;
		}

		public void setReserveId(String reserveId) {
			this.reserveId = reserveId;
		}

		public List<DeptBriefBean> getBriefInfo() {
			return briefInfo;
		}

		public void setBriefInfo(List<DeptBriefBean> briefInfo) {
			this.briefInfo = briefInfo;
		}

		
 }
