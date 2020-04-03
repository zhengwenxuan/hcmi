/**
 * CSHESBServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.Carestream.client;

public class CSHESBServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.Carestream.client.CSHESBService {

    public CSHESBServiceLocator(String url) {
    	this.CSHESBServiceSoap12_address=url;
    	this.CSHESBServiceSoap_address=url;
    }


    public CSHESBServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CSHESBServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CSHESBServiceSoap
    private java.lang.String CSHESBServiceSoap_address = "http://localhost:3440/CSHESBService.asmx";

    public java.lang.String getCSHESBServiceSoapAddress() {
        return CSHESBServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CSHESBServiceSoapWSDDServiceName = "CSHESBServiceSoap";

    public java.lang.String getCSHESBServiceSoapWSDDServiceName() {
        return CSHESBServiceSoapWSDDServiceName;
    }

    public void setCSHESBServiceSoapWSDDServiceName(java.lang.String name) {
        CSHESBServiceSoapWSDDServiceName = name;
    }

    public com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_PortType getCSHESBServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CSHESBServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCSHESBServiceSoap(endpoint);
    }

    public com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_PortType getCSHESBServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_BindingStub _stub = new com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getCSHESBServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCSHESBServiceSoapEndpointAddress(java.lang.String address) {
        CSHESBServiceSoap_address = address;
    }


    // Use to get a proxy class for CSHESBServiceSoap12
    private java.lang.String CSHESBServiceSoap12_address = "http://localhost:3440/CSHESBService.asmx";

    public java.lang.String getCSHESBServiceSoap12Address() {
        return CSHESBServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CSHESBServiceSoap12WSDDServiceName = "CSHESBServiceSoap12";

    public java.lang.String getCSHESBServiceSoap12WSDDServiceName() {
        return CSHESBServiceSoap12WSDDServiceName;
    }

    public void setCSHESBServiceSoap12WSDDServiceName(java.lang.String name) {
        CSHESBServiceSoap12WSDDServiceName = name;
    }

    public com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_PortType getCSHESBServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CSHESBServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCSHESBServiceSoap12(endpoint);
    }

    public com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_PortType getCSHESBServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.Carestream.client.CSHESBServiceSoap12Stub _stub = new com.hjw.webService.client.Carestream.client.CSHESBServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getCSHESBServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCSHESBServiceSoap12EndpointAddress(java.lang.String address) {
        CSHESBServiceSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_BindingStub _stub = new com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_BindingStub(new java.net.URL(CSHESBServiceSoap_address), this);
                _stub.setPortName(getCSHESBServiceSoapWSDDServiceName());
                return _stub;
            }
            if (com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.Carestream.client.CSHESBServiceSoap12Stub _stub = new com.hjw.webService.client.Carestream.client.CSHESBServiceSoap12Stub(new java.net.URL(CSHESBServiceSoap12_address), this);
                _stub.setPortName(getCSHESBServiceSoap12WSDDServiceName());
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
        if ("CSHESBServiceSoap".equals(inputPortName)) {
            return getCSHESBServiceSoap();
        }
        else if ("CSHESBServiceSoap12".equals(inputPortName)) {
            return getCSHESBServiceSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.carestream.cn/", "CSHESBService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.carestream.cn/", "CSHESBServiceSoap"));
            ports.add(new javax.xml.namespace.QName("http://www.carestream.cn/", "CSHESBServiceSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CSHESBServiceSoap".equals(portName)) {
            setCSHESBServiceSoapEndpointAddress(address);
        }
        else 
if ("CSHESBServiceSoap12".equals(portName)) {
            setCSHESBServiceSoap12EndpointAddress(address);
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
