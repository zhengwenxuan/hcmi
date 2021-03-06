/**
 * TCIServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.TCI.client;

public class TCIServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.TCI.client.TCIService {

    public TCIServiceLocator(String urls) {
    	this.TCIPort_address=urls;
    }


    public TCIServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TCIServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TCIPort
    private java.lang.String TCIPort_address = "http://192.26.3.63:9080/RHIN/TCIService";

    public java.lang.String getTCIPortAddress() {
        return TCIPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TCIPortWSDDServiceName = "TCIPort";

    public java.lang.String getTCIPortWSDDServiceName() {
        return TCIPortWSDDServiceName;
    }

    public void setTCIPortWSDDServiceName(java.lang.String name) {
        TCIPortWSDDServiceName = name;
    }

    public com.hjw.webService.client.TCI.client.ITCI getTCIPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TCIPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTCIPort(endpoint);
    }

    public com.hjw.webService.client.TCI.client.ITCI getTCIPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.TCI.client.TCIPortBindingStub _stub = new com.hjw.webService.client.TCI.client.TCIPortBindingStub(portAddress, this);
            _stub.setPortName(getTCIPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTCIPortEndpointAddress(java.lang.String address) {
        TCIPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.TCI.client.ITCI.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.TCI.client.TCIPortBindingStub _stub = new com.hjw.webService.client.TCI.client.TCIPortBindingStub(new java.net.URL(TCIPort_address), this);
                _stub.setPortName(getTCIPortWSDDServiceName());
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
        if ("TCIPort".equals(inputPortName)) {
            return getTCIPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.tci/", "TCIService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.tci/", "TCIPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TCIPort".equals(portName)) {
            setTCIPortEndpointAddress(address);
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
