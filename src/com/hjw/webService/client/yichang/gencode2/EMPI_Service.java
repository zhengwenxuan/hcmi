/**
 * EMPI_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.yichang.gencode2;

public interface EMPI_Service extends javax.xml.rpc.Service {
    public java.lang.String getEMPIAddress();

    public com.hjw.webService.client.yichang.gencode2.EmpiWebService getEMPI() throws javax.xml.rpc.ServiceException;

    public com.hjw.webService.client.yichang.gencode2.EmpiWebService getEMPI(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
