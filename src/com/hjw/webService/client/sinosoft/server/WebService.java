/**
 * WebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.sinosoft.server;

public interface WebService extends javax.xml.rpc.Service {
    public java.lang.String getWebServiceSoapAddress();

    public com.hjw.webService.client.sinosoft.server.WebServiceSoap getWebServiceSoap() throws javax.xml.rpc.ServiceException;

    public com.hjw.webService.client.sinosoft.server.WebServiceSoap getWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
