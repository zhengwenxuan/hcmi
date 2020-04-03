package com.hjw.webService.service;

public class Test {

	public static void main(String[] args) {
	
		try {
			AcceptSgtzMessage rpm = new AcceptSgtzMessage();
			String  rc = rpm.doServer("VPGST_BJRQCTKJYXGS_PATIENTINFO", "1705110012");
            System.out.println(rc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
