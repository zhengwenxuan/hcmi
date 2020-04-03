/**
 * AppIntegratorServerLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.sinosoft.webservice;

public class AppIntegratorServerLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.sinosoft.webservice.AppIntegratorServer {

    public AppIntegratorServerLocator(String url) {
    	this.AppIntegratorServerHttpSoap11Endpoint_address=url;
    	this.AppIntegratorServerHttpSoap12Endpoint_address=url;
    }


    public AppIntegratorServerLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AppIntegratorServerLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AppIntegratorServerHttpSoap12Endpoint
    private java.lang.String AppIntegratorServerHttpSoap12Endpoint_address = "http://10.10.10.16:8280/services/AppIntegratorServer.AppIntegratorServerHttpSoap12Endpoint";

    public java.lang.String getAppIntegratorServerHttpSoap12EndpointAddress() {
        return AppIntegratorServerHttpSoap12Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AppIntegratorServerHttpSoap12EndpointWSDDServiceName = "AppIntegratorServerHttpSoap12Endpoint";

    public java.lang.String getAppIntegratorServerHttpSoap12EndpointWSDDServiceName() {
        return AppIntegratorServerHttpSoap12EndpointWSDDServiceName;
    }

    public void setAppIntegratorServerHttpSoap12EndpointWSDDServiceName(java.lang.String name) {
        AppIntegratorServerHttpSoap12EndpointWSDDServiceName = name;
    }

    public com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType getAppIntegratorServerHttpSoap12Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AppIntegratorServerHttpSoap12Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAppIntegratorServerHttpSoap12Endpoint(endpoint);
    }

    public com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType getAppIntegratorServerHttpSoap12Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerSoap12BindingStub _stub = new com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerSoap12BindingStub(portAddress, this);
            _stub.setPortName(getAppIntegratorServerHttpSoap12EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAppIntegratorServerHttpSoap12EndpointEndpointAddress(java.lang.String address) {
        AppIntegratorServerHttpSoap12Endpoint_address = address;
    }


    // Use to get a proxy class for AppIntegratorServerHttpSoap11Endpoint
    private java.lang.String AppIntegratorServerHttpSoap11Endpoint_address = "http://10.10.10.16:8280/services/AppIntegratorServer.AppIntegratorServerHttpSoap11Endpoint";

    public java.lang.String getAppIntegratorServerHttpSoap11EndpointAddress() {
        return AppIntegratorServerHttpSoap11Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AppIntegratorServerHttpSoap11EndpointWSDDServiceName = "AppIntegratorServerHttpSoap11Endpoint";

    public java.lang.String getAppIntegratorServerHttpSoap11EndpointWSDDServiceName() {
        return AppIntegratorServerHttpSoap11EndpointWSDDServiceName;
    }

    public void setAppIntegratorServerHttpSoap11EndpointWSDDServiceName(java.lang.String name) {
        AppIntegratorServerHttpSoap11EndpointWSDDServiceName = name;
    }

    public com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType getAppIntegratorServerHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AppIntegratorServerHttpSoap11Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAppIntegratorServerHttpSoap11Endpoint(endpoint);
    }

    public com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType getAppIntegratorServerHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerSoap11BindingStub _stub = new com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerSoap11BindingStub(portAddress, this);
            _stub.setPortName(getAppIntegratorServerHttpSoap11EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAppIntegratorServerHttpSoap11EndpointEndpointAddress(java.lang.String address) {
        AppIntegratorServerHttpSoap11Endpoint_address = address;
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
            if (com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerSoap12BindingStub _stub = new com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerSoap12BindingStub(new java.net.URL(AppIntegratorServerHttpSoap12Endpoint_address), this);
                _stub.setPortName(getAppIntegratorServerHttpSoap12EndpointWSDDServiceName());
                return _stub;
            }
            if (com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerSoap11BindingStub _stub = new com.hjw.webService.client.sinosoft.webservice.AppIntegratorServerSoap11BindingStub(new java.net.URL(AppIntegratorServerHttpSoap11Endpoint_address), this);
                _stub.setPortName(getAppIntegratorServerHttpSoap11EndpointWSDDServiceName());
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
        if ("AppIntegratorServerHttpSoap12Endpoint".equals(inputPortName)) {
            return getAppIntegratorServerHttpSoap12Endpoint();
        }
        else if ("AppIntegratorServerHttpSoap11Endpoint".equals(inputPortName)) {
            return getAppIntegratorServerHttpSoap11Endpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://server.integrator.application.sinosoft.com", "AppIntegratorServer");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://server.integrator.application.sinosoft.com", "AppIntegratorServerHttpSoap12Endpoint"));
            ports.add(new javax.xml.namespace.QName("http://server.integrator.application.sinosoft.com", "AppIntegratorServerHttpSoap11Endpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AppIntegratorServerHttpSoap12Endpoint".equals(portName)) {
            setAppIntegratorServerHttpSoap12EndpointEndpointAddress(address);
        }
        else 
if ("AppIntegratorServerHttpSoap11Endpoint".equals(portName)) {
            setAppIntegratorServerHttpSoap11EndpointEndpointAddress(address);
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
