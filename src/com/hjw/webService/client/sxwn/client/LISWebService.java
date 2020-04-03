/**
 * LISWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.sxwn.client;

public interface LISWebService extends javax.xml.rpc.Service {
    public java.lang.String getLISWebServiceSoapAddress();

    public com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType getLISWebServiceSoap() throws javax.xml.rpc.ServiceException;

    public com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType getLISWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getLISWebServiceSoap12Address();

    public com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType getLISWebServiceSoap12() throws javax.xml.rpc.ServiceException;

    public com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType getLISWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
