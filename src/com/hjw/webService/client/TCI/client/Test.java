package com.hjw.webService.client.TCI.client;

public class Test {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		TCIService tcis= new TCIServiceLocator("http://192.26.3.63:9080/RHIN/TCIService");
		ITCI itci=tcis.getTCIPort();
		itci.g_Apply(null,null,null,null,null);
	}

}
