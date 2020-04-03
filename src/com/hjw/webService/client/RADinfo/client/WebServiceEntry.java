/**
 * WebServiceEntry.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.RADinfo.client;

public interface WebServiceEntry extends java.rmi.Remote {
    public java.lang.String invoke(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String[] arg4) throws java.rmi.RemoteException, com.hjw.webService.client.RADinfo.client.Exception;
    public void startWs() throws java.rmi.RemoteException;
    public java.lang.String transportData(java.lang.String arg0, java.lang.String arg1, int arg2, java.lang.String arg3) throws java.rmi.RemoteException, com.hjw.webService.client.RADinfo.client.Exception;
}
