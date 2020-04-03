/**
 * CollectionInterfaceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.tj180.client;

public class CollectionInterfaceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.tj180.client.CollectionInterface {

    public CollectionInterfaceLocator(String url) {
    	this.CollectionInterfaceSoap12_address=url;
    	this.CollectionInterfaceSoap_address=url;
    }


    public CollectionInterfaceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CollectionInterfaceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CollectionInterfaceSoap12
    private java.lang.String CollectionInterfaceSoap12_address = "http://10.180.180.184:4417/CollectionInterface.asmx";

    public java.lang.String getCollectionInterfaceSoap12Address() {
        return CollectionInterfaceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CollectionInterfaceSoap12WSDDServiceName = "CollectionInterfaceSoap12";

    public java.lang.String getCollectionInterfaceSoap12WSDDServiceName() {
        return CollectionInterfaceSoap12WSDDServiceName;
    }

    public void setCollectionInterfaceSoap12WSDDServiceName(java.lang.String name) {
        CollectionInterfaceSoap12WSDDServiceName = name;
    }

    public com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_PortType getCollectionInterfaceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CollectionInterfaceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCollectionInterfaceSoap12(endpoint);
    }

    public com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_PortType getCollectionInterfaceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.tj180.client.CollectionInterfaceSoap12Stub _stub = new com.hjw.webService.client.tj180.client.CollectionInterfaceSoap12Stub(portAddress, this);
            _stub.setPortName(getCollectionInterfaceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCollectionInterfaceSoap12EndpointAddress(java.lang.String address) {
        CollectionInterfaceSoap12_address = address;
    }


    // Use to get a proxy class for CollectionInterfaceSoap
    private java.lang.String CollectionInterfaceSoap_address = "http://10.180.180.184:4417/CollectionInterface.asmx";

    public java.lang.String getCollectionInterfaceSoapAddress() {
        return CollectionInterfaceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CollectionInterfaceSoapWSDDServiceName = "CollectionInterfaceSoap";

    public java.lang.String getCollectionInterfaceSoapWSDDServiceName() {
        return CollectionInterfaceSoapWSDDServiceName;
    }

    public void setCollectionInterfaceSoapWSDDServiceName(java.lang.String name) {
        CollectionInterfaceSoapWSDDServiceName = name;
    }

    public com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_PortType getCollectionInterfaceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CollectionInterfaceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCollectionInterfaceSoap(endpoint);
    }

    public com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_PortType getCollectionInterfaceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_BindingStub _stub = new com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getCollectionInterfaceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCollectionInterfaceSoapEndpointAddress(java.lang.String address) {
        CollectionInterfaceSoap_address = address;
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
            if (com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.tj180.client.CollectionInterfaceSoap12Stub _stub = new com.hjw.webService.client.tj180.client.CollectionInterfaceSoap12Stub(new java.net.URL(CollectionInterfaceSoap12_address), this);
                _stub.setPortName(getCollectionInterfaceSoap12WSDDServiceName());
                return _stub;
            }
            if (com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_BindingStub _stub = new com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_BindingStub(new java.net.URL(CollectionInterfaceSoap_address), this);
                _stub.setPortName(getCollectionInterfaceSoapWSDDServiceName());
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
        if ("CollectionInterfaceSoap12".equals(inputPortName)) {
            return getCollectionInterfaceSoap12();
        }
        else if ("CollectionInterfaceSoap".equals(inputPortName)) {
            return getCollectionInterfaceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "CollectionInterface");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "CollectionInterfaceSoap12"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "CollectionInterfaceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CollectionInterfaceSoap12".equals(portName)) {
            setCollectionInterfaceSoap12EndpointAddress(address);
        }
        else 
if ("CollectionInterfaceSoap".equals(portName)) {
            setCollectionInterfaceSoapEndpointAddress(address);
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
