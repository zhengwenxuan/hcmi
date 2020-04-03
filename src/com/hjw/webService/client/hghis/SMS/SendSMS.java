package com.hjw.webService.client.hghis.SMS;

import com.hjw.webService.client.hghis.SMS.bean.SendTSMS;

public class SendSMS {

	
	public static void main(String[] args) {
		SendTSMS sendSMS = new SendTSMS();
		String[] mobiles = sendSMS.getMobiles();
		String[] arr=new String[6];
		mobiles=arr;
		
		
		System.err.println(mobiles.length);
	}
}
