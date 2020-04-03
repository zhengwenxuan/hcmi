/**
 * GWI_TJJKServicesSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.nanhua.gencode;

public interface GWI_TJJKServicesSoap_PortType extends java.rmi.Remote {
    public java.lang.String netTest() throws java.rmi.RemoteException;
    public java.lang.String cardIDCheck(java.lang.String requestString) throws java.rmi.RemoteException;
    public java.lang.String doBlance(java.lang.String strRequest) throws java.rmi.RemoteException;
    public java.lang.String cancelDoBlance(java.lang.String strRequest) throws java.rmi.RemoteException;
    public java.lang.String returnDoBlance(java.lang.String strRequest) throws java.rmi.RemoteException;
    public java.lang.String getDoBlance(java.lang.String strRequest) throws java.rmi.RemoteException;
    public java.lang.String queryItemInfo(java.lang.String strRequest) throws java.rmi.RemoteException;
}
