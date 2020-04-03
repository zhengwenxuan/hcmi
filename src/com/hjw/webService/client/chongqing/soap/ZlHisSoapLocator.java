/**
 * ZlHisSoapLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.chongqing.soap;

public class ZlHisSoapLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.chongqing.soap.ZlHisSoap {

    public ZlHisSoapLocator() {
    }


    public ZlHisSoapLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ZlHisSoapLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for zlHisSoapHttpSoap11Endpoint
    private java.lang.String zlHisSoapHttpSoap11Endpoint_address = "http://40.168.0.70:8091/services/zlHisSoap.zlHisSoapHttpSoap11Endpoint";

    public java.lang.String getzlHisSoapHttpSoap11EndpointAddress() {
        return zlHisSoapHttpSoap11Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String zlHisSoapHttpSoap11EndpointWSDDServiceName = "zlHisSoapHttpSoap11Endpoint";

    public java.lang.String getzlHisSoapHttpSoap11EndpointWSDDServiceName() {
        return zlHisSoapHttpSoap11EndpointWSDDServiceName;
    }

    public void setzlHisSoapHttpSoap11EndpointWSDDServiceName(java.lang.String name) {
        zlHisSoapHttpSoap11EndpointWSDDServiceName = name;
    }

    public com.hjw.webService.client.chongqing.soap.ZlHisSoapPortType getzlHisSoapHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(zlHisSoapHttpSoap11Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getzlHisSoapHttpSoap11Endpoint(endpoint);
    }

    public com.hjw.webService.client.chongqing.soap.ZlHisSoapPortType getzlHisSoapHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.chongqing.soap.ZlHisSoapSoap11BindingStub _stub = new com.hjw.webService.client.chongqing.soap.ZlHisSoapSoap11BindingStub(portAddress, this);
            _stub.setPortName(getzlHisSoapHttpSoap11EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setzlHisSoapHttpSoap11EndpointEndpointAddress(java.lang.String address) {
        zlHisSoapHttpSoap11Endpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.chongqing.soap.ZlHisSoapPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.chongqing.soap.ZlHisSoapSoap11BindingStub _stub = new com.hjw.webService.client.chongqing.soap.ZlHisSoapSoap11BindingStub(new java.net.URL(zlHisSoapHttpSoap11Endpoint_address), this);
                _stub.setPortName(getzlHisSoapHttpSoap11EndpointWSDDServiceName());
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
        if ("zlHisSoapHttpSoap11Endpoint".equals(inputPortName)) {
            return getzlHisSoapHttpSoap11Endpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "zlHisSoap");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "zlHisSoapHttpSoap11Endpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("zlHisSoapHttpSoap11Endpoint".equals(portName)) {
            setzlHisSoapHttpSoap11EndpointEndpointAddress(address);
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
