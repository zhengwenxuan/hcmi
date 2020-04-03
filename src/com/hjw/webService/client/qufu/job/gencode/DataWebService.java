/**
 * DataWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.qufu.job.gencode;

public interface DataWebService extends javax.xml.rpc.Service {
    public java.lang.String getDataWebServiceSoapAddress();

    public com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType getDataWebServiceSoap() throws javax.xml.rpc.ServiceException;

    public com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType getDataWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getDataWebServiceSoap12Address();

    public com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType getDataWebServiceSoap12() throws javax.xml.rpc.ServiceException;

    public com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType getDataWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
