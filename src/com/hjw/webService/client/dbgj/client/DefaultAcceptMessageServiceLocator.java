/**
 * DefaultAcceptMessageServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.dbgj.client;

public class DefaultAcceptMessageServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.dbgj.client.DefaultAcceptMessageService {

    public DefaultAcceptMessageServiceLocator(String url) {
    	this.DefaultAcceptMessagePort_address=url;
    }


    public DefaultAcceptMessageServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DefaultAcceptMessageServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DefaultAcceptMessagePort
    private java.lang.String DefaultAcceptMessagePort_address = "http://192.168.111.46:8088/services/Mirth";

    public java.lang.String getDefaultAcceptMessagePortAddress() {
        return DefaultAcceptMessagePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DefaultAcceptMessagePortWSDDServiceName = "DefaultAcceptMessagePort";

    public java.lang.String getDefaultAcceptMessagePortWSDDServiceName() {
        return DefaultAcceptMessagePortWSDDServiceName;
    }

    public void setDefaultAcceptMessagePortWSDDServiceName(java.lang.String name) {
        DefaultAcceptMessagePortWSDDServiceName = name;
    }

    public com.hjw.webService.client.dbgj.client.DefaultAcceptMessage getDefaultAcceptMessagePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DefaultAcceptMessagePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDefaultAcceptMessagePort(endpoint);
    }

    public com.hjw.webService.client.dbgj.client.DefaultAcceptMessage getDefaultAcceptMessagePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.dbgj.client.DefaultAcceptMessagePortBindingStub _stub = new com.hjw.webService.client.dbgj.client.DefaultAcceptMessagePortBindingStub(portAddress, this);
            _stub.setPortName(getDefaultAcceptMessagePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDefaultAcceptMessagePortEndpointAddress(java.lang.String address) {
        DefaultAcceptMessagePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.dbgj.client.DefaultAcceptMessage.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.dbgj.client.DefaultAcceptMessagePortBindingStub _stub = new com.hjw.webService.client.dbgj.client.DefaultAcceptMessagePortBindingStub(new java.net.URL(DefaultAcceptMessagePort_address), this);
                _stub.setPortName(getDefaultAcceptMessagePortWSDDServiceName());
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
        if ("DefaultAcceptMessagePort".equals(inputPortName)) {
            return getDefaultAcceptMessagePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.connectors.connect.mirth.com/", "DefaultAcceptMessageService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.connectors.connect.mirth.com/", "DefaultAcceptMessagePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DefaultAcceptMessagePort".equals(portName)) {
            setDefaultAcceptMessagePortEndpointAddress(address);
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
