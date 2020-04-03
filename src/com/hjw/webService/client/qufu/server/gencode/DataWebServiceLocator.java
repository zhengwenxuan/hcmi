/**
 * DataWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.qufu.server.gencode;

public class DataWebServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.qufu.server.gencode.DataWebService {

    public DataWebServiceLocator() {
    }


    public DataWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DataWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DataWebServiceSoap
    private java.lang.String DataWebServiceSoap_address = "http://192.168.99.153:3281/checkInfoQuery";

    public java.lang.String getDataWebServiceSoapAddress() {
        return DataWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DataWebServiceSoapWSDDServiceName = "DataWebServiceSoap";

    public java.lang.String getDataWebServiceSoapWSDDServiceName() {
        return DataWebServiceSoapWSDDServiceName;
    }

    public void setDataWebServiceSoapWSDDServiceName(java.lang.String name) {
        DataWebServiceSoapWSDDServiceName = name;
    }

    public com.hjw.webService.client.qufu.server.gencode.DataWebServiceSoap getDataWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DataWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDataWebServiceSoap(endpoint);
    }

    public com.hjw.webService.client.qufu.server.gencode.DataWebServiceSoap getDataWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.qufu.server.gencode.DataWebServiceSoapBindingStub _stub = new com.hjw.webService.client.qufu.server.gencode.DataWebServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getDataWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDataWebServiceSoapEndpointAddress(java.lang.String address) {
        DataWebServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.qufu.server.gencode.DataWebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.qufu.server.gencode.DataWebServiceSoapBindingStub _stub = new com.hjw.webService.client.qufu.server.gencode.DataWebServiceSoapBindingStub(new java.net.URL(DataWebServiceSoap_address), this);
                _stub.setPortName(getDataWebServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DataWebServiceSoap".equals(inputPortName)) {
            return getDataWebServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.shats.com/DataReceive", "DataWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.shats.com/DataReceive", "DataWebServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DataWebServiceSoap".equals(portName)) {
            setDataWebServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
