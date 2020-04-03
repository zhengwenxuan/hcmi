/**
 * MediInfoHisLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.hzty.client;

public class MediInfoHisLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.hzty.client.MediInfoHis {
	
    public MediInfoHisLocator(String url) {
    	this.WebPoint_address=url;
    }


    public MediInfoHisLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MediInfoHisLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WebPoint
    private java.lang.String WebPoint_address = "http://192.168.254.176:9099/MediInfoHis.svc";

    public java.lang.String getWebPointAddress() {
        return WebPoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WebPointWSDDServiceName = "WebPoint";

    public java.lang.String getWebPointWSDDServiceName() {
        return WebPointWSDDServiceName;
    }

    public void setWebPointWSDDServiceName(java.lang.String name) {
        WebPointWSDDServiceName = name;
    }

    public com.hjw.webService.client.hzty.client.IHisApplay getWebPoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WebPoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWebPoint(endpoint);
    }

    public com.hjw.webService.client.hzty.client.IHisApplay getWebPoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.hzty.client.WebPointStub _stub = new com.hjw.webService.client.hzty.client.WebPointStub(portAddress, this);
            _stub.setPortName(getWebPointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWebPointEndpointAddress(java.lang.String address) {
        WebPoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.hzty.client.IHisApplay.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.hzty.client.WebPointStub _stub = new com.hjw.webService.client.hzty.client.WebPointStub(new java.net.URL(WebPoint_address), this);
                _stub.setPortName(getWebPointWSDDServiceName());
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
        if ("WebPoint".equals(inputPortName)) {
            return getWebPoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "MediInfoHis");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "WebPoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WebPoint".equals(portName)) {
            setWebPointEndpointAddress(address);
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
