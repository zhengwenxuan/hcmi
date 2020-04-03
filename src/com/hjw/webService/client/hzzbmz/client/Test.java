package com.hjw.webService.client.hzzbmz.client;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HisService_Service hs = new HisService_ServiceLocator("");
		HisServiceDelegate hsd;
		try {
			hsd = hs.getHisService();
			Object o= hsd.invoke("", "", "");
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
