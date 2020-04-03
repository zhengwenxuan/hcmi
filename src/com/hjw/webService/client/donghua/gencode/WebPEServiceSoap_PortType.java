/**
 * WebPEServiceSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.donghua.gencode;

public interface WebPEServiceSoap_PortType extends java.rmi.Remote {
    public java.lang.String savePEOrdInfo(java.lang.String input) throws java.rmi.RemoteException;
    public java.lang.String savePatAdmInfo(java.lang.String input) throws java.rmi.RemoteException;
    public java.lang.String saveStopPEOrdInfo(java.lang.String input) throws java.rmi.RemoteException;
    public java.lang.String sendLinkLabNoWithOrdRowId(java.lang.String input) throws java.rmi.RemoteException;
}
