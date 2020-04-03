package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class HisGetBodyBean {
	    private String reserveId="";//	体检预约号
	    
		private List<HisGetResItemBean> hisItemsList=new ArrayList<HisGetResItemBean>();//申请单号

		public String getReserveId() {
			return reserveId;
		}

		public void setReserveId(String reserveId) {
			this.reserveId = reserveId;
		}

		public List<HisGetResItemBean> getHisItemsList() {
			return hisItemsList;
		}

		public void setHisItemsList(List<HisGetResItemBean> hisItemsList) {
			this.hisItemsList = hisItemsList;
		}

		

 }
