package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class UpdateDiagnosisBody {
	    private String reserveId="";//	体检预约号
	    
		private List<UpdateDiagnosisBean> diagnosisInfo=new ArrayList<UpdateDiagnosisBean>();

		public String getReserveId() {
			return reserveId;
		}

		public void setReserveId(String reserveId) {
			this.reserveId = reserveId;
		}

		public List<UpdateDiagnosisBean> getDiagnosisInfo() {
			return diagnosisInfo;
		}

		public void setDiagnosisInfo(List<UpdateDiagnosisBean> diagnosisInfo) {
			this.diagnosisInfo = diagnosisInfo;
		}

		
 }
