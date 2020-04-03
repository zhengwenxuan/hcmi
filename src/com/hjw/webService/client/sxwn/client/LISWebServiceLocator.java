/**
 * LISWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.sxwn.client;

public class LISWebServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.sxwn.client.LISWebService {

    public LISWebServiceLocator(String url) {
    	this.LISWebServiceSoap12_address=url;
    	this.LISWebServiceSoap_address=url;
    }


    public LISWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LISWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LISWebServiceSoap
    private java.lang.String LISWebServiceSoap_address = "http://111.111.111.52:8008/LISWebService.asmx";

    public java.lang.String getLISWebServiceSoapAddress() {
        return LISWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LISWebServiceSoapWSDDServiceName = "LISWebServiceSoap";

    public java.lang.String getLISWebServiceSoapWSDDServiceName() {
        return LISWebServiceSoapWSDDServiceName;
    }

    public void setLISWebServiceSoapWSDDServiceName(java.lang.String name) {
        LISWebServiceSoapWSDDServiceName = name;
    }

    public com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType getLISWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LISWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLISWebServiceSoap(endpoint);
    }

    public com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType getLISWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.sxwn.client.LISWebServiceSoap_BindingStub _stub = new com.hjw.webService.client.sxwn.client.LISWebServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getLISWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLISWebServiceSoapEndpointAddress(java.lang.String address) {
        LISWebServiceSoap_address = address;
    }


    // Use to get a proxy class for LISWebServiceSoap12
    private java.lang.String LISWebServiceSoap12_address = "http://111.111.111.52:8008/LISWebService.asmx";

    public java.lang.String getLISWebServiceSoap12Address() {
        return LISWebServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LISWebServiceSoap12WSDDServiceName = "LISWebServiceSoap12";

    public java.lang.String getLISWebServiceSoap12WSDDServiceName() {
        return LISWebServiceSoap12WSDDServiceName;
    }

    public void setLISWebServiceSoap12WSDDServiceName(java.lang.String name) {
        LISWebServiceSoap12WSDDServiceName = name;
    }

    public com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType getLISWebServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LISWebServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLISWebServiceSoap12(endpoint);
    }

    public com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType getLISWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.sxwn.client.LISWebServiceSoap12Stub _stub = new com.hjw.webService.client.sxwn.client.LISWebServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getLISWebServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLISWebServiceSoap12EndpointAddress(java.lang.String address) {
        LISWebServiceSoap12_address = address;
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
            if (com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.sxwn.client.LISWebServiceSoap_BindingStub _stub = new com.hjw.webService.client.sxwn.client.LISWebServiceSoap_BindingStub(new java.net.URL(LISWebServiceSoap_address), this);
                _stub.setPortName(getLISWebServiceSoapWSDDServiceName());
                return _stub;
            }
            if (com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.sxwn.client.LISWebServiceSoap12Stub _stub = new com.hjw.webService.client.sxwn.client.LISWebServiceSoap12Stub(new java.net.URL(LISWebServiceSoap12_address), this);
                _stub.setPortName(getLISWebServiceSoap12WSDDServiceName());
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
        if ("LISWebServiceSoap".equals(inputPortName)) {
            return getLISWebServiceSoap();
        }
        else if ("LISWebServiceSoap12".equals(inputPortName)) {
            return getLISWebServiceSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "LISWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "LISWebServiceSoap"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "LISWebServiceSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("LISWebServiceSoap".equals(portName)) {
            setLISWebServiceSoapEndpointAddress(address);
        }
        else 
if ("LISWebServiceSoap12".equals(portName)) {
            setLISWebServiceSoap12EndpointAddress(address);
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
