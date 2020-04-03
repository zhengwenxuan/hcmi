/**
 * CreatePersonServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.fangzheng.client;

public class CreatePersonServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.fangzheng.client.CreatePersonService {

    public CreatePersonServiceLocator(String url) {
    	this.CreatePersonServicePort_address=url;
    }


    public CreatePersonServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CreatePersonServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CreatePersonServicePort
    private java.lang.String CreatePersonServicePort_address = "http://192.168.8.212:8001/empi-ws/doc/createPerson";

    public java.lang.String getCreatePersonServicePortAddress() {
        return CreatePersonServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CreatePersonServicePortWSDDServiceName = "CreatePersonServicePort";

    public java.lang.String getCreatePersonServicePortWSDDServiceName() {
        return CreatePersonServicePortWSDDServiceName;
    }

    public void setCreatePersonServicePortWSDDServiceName(java.lang.String name) {
        CreatePersonServicePortWSDDServiceName = name;
    }

    public com.hjw.webService.client.fangzheng.client.ICreatePersonService getCreatePersonServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CreatePersonServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCreatePersonServicePort(endpoint);
    }

    public com.hjw.webService.client.fangzheng.client.ICreatePersonService getCreatePersonServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.fangzheng.client.CreatePersonServiceSoapBindingStub _stub = new com.hjw.webService.client.fangzheng.client.CreatePersonServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCreatePersonServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCreatePersonServicePortEndpointAddress(java.lang.String address) {
        CreatePersonServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.fangzheng.client.ICreatePersonService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.fangzheng.client.CreatePersonServiceSoapBindingStub _stub = new com.hjw.webService.client.fangzheng.client.CreatePersonServiceSoapBindingStub(new java.net.URL(CreatePersonServicePort_address), this);
                _stub.setPortName(getCreatePersonServicePortWSDDServiceName());
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
        if ("CreatePersonServicePort".equals(inputPortName)) {
            return getCreatePersonServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://doc.service.cxf.founder.com/", "CreatePersonService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://doc.service.cxf.founder.com/", "CreatePersonServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CreatePersonServicePort".equals(portName)) {
            setCreatePersonServicePortEndpointAddress(address);
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
