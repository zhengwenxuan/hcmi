/**
 * AppIntegratorServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.sinosoft.webservice;

public interface AppIntegratorServer extends javax.xml.rpc.Service {
    public java.lang.String getAppIntegratorServerHttpSoap12EndpointAddress();

    public com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType getAppIntegratorServerHttpSoap12Endpoint() throws javax.xml.rpc.ServiceException;

    public com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType getAppIntegratorServerHttpSoap12Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getAppIntegratorServerHttpSoap11EndpointAddress();

    public com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType getAppIntegratorServerHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException;

    public com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType getAppIntegratorServerHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
