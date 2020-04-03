/**
 * OtherSysServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.erfuyuan.gencode;

public class OtherSysServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.erfuyuan.gencode.OtherSysService {

    public OtherSysServiceLocator(String url) {
    	this.OtherSysServiceSoap12_address = url;
    	this.OtherSysServiceSoap_address = url;
    }


    public OtherSysServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OtherSysServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OtherSysServiceSoap12
    private java.lang.String OtherSysServiceSoap12_address = "";//"http://172.16.0.36:6000/OtherSysService.asmx";

    public java.lang.String getOtherSysServiceSoap12Address() {
        return OtherSysServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OtherSysServiceSoap12WSDDServiceName = "OtherSysServiceSoap12";

    public java.lang.String getOtherSysServiceSoap12WSDDServiceName() {
        return OtherSysServiceSoap12WSDDServiceName;
    }

    public void setOtherSysServiceSoap12WSDDServiceName(java.lang.String name) {
        OtherSysServiceSoap12WSDDServiceName = name;
    }

    public com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_PortType getOtherSysServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OtherSysServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOtherSysServiceSoap12(endpoint);
    }

    public com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_PortType getOtherSysServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap12Stub _stub = new com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getOtherSysServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOtherSysServiceSoap12EndpointAddress(java.lang.String address) {
        OtherSysServiceSoap12_address = address;
    }


    // Use to get a proxy class for OtherSysServiceSoap
    private java.lang.String OtherSysServiceSoap_address = "http://172.16.0.36:6000/OtherSysService.asmx";

    public java.lang.String getOtherSysServiceSoapAddress() {
        return OtherSysServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OtherSysServiceSoapWSDDServiceName = "OtherSysServiceSoap";

    public java.lang.String getOtherSysServiceSoapWSDDServiceName() {
        return OtherSysServiceSoapWSDDServiceName;
    }

    public void setOtherSysServiceSoapWSDDServiceName(java.lang.String name) {
        OtherSysServiceSoapWSDDServiceName = name;
    }

    public com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_PortType getOtherSysServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OtherSysServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOtherSysServiceSoap(endpoint);
    }

    public com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_PortType getOtherSysServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_BindingStub _stub = new com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getOtherSysServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOtherSysServiceSoapEndpointAddress(java.lang.String address) {
        OtherSysServiceSoap_address = address;
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
            if (com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap12Stub _stub = new com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap12Stub(new java.net.URL(OtherSysServiceSoap12_address), this);
                _stub.setPortName(getOtherSysServiceSoap12WSDDServiceName());
                return _stub;
            }
            if (com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_BindingStub _stub = new com.hjw.webService.client.erfuyuan.gencode.OtherSysServiceSoap_BindingStub(new java.net.URL(OtherSysServiceSoap_address), this);
                _stub.setPortName(getOtherSysServiceSoapWSDDServiceName());
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
        if ("OtherSysServiceSoap12".equals(inputPortName)) {
            return getOtherSysServiceSoap12();
        }
        else if ("OtherSysServiceSoap".equals(inputPortName)) {
            return getOtherSysServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "OtherSysService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "OtherSysServiceSoap12"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "OtherSysServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OtherSysServiceSoap12".equals(portName)) {
            setOtherSysServiceSoap12EndpointAddress(address);
        }
        else 
if ("OtherSysServiceSoap".equals(portName)) {
            setOtherSysServiceSoapEndpointAddress(address);
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
