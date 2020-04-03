/**
 * IApiServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.bdyx.gencode;

public class IApiServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.bdyx.gencode.IApiService {

    public IApiServiceLocator(String url) {
    	this.IApiServicePort_address = url;
    }


    public IApiServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IApiServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IApiServicePort
    private java.lang.String IApiServicePort_address = "http://192.168.100.55/iih.gswuweizlh.ei.api.i.IApiService?p=YWNjZXNzX3Rva2VuPWI1MjgyMDM0LTkxMTEtNGVkMC1hNjExLTA2NTYwMWExOGEyNg==";

    public java.lang.String getIApiServicePortAddress() {
        return IApiServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IApiServicePortWSDDServiceName = "IApiServicePort";

    public java.lang.String getIApiServicePortWSDDServiceName() {
        return IApiServicePortWSDDServiceName;
    }

    public void setIApiServicePortWSDDServiceName(java.lang.String name) {
        IApiServicePortWSDDServiceName = name;
    }

    public com.hjw.webService.client.bdyx.gencode.IApiServicePortType getIApiServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IApiServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIApiServicePort(endpoint);
    }

    public com.hjw.webService.client.bdyx.gencode.IApiServicePortType getIApiServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.bdyx.gencode.IApiServiceSoapBindingStub _stub = new com.hjw.webService.client.bdyx.gencode.IApiServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getIApiServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIApiServicePortEndpointAddress(java.lang.String address) {
        IApiServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.bdyx.gencode.IApiServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.bdyx.gencode.IApiServiceSoapBindingStub _stub = new com.hjw.webService.client.bdyx.gencode.IApiServiceSoapBindingStub(new java.net.URL(IApiServicePort_address), this);
                _stub.setPortName(getIApiServicePortWSDDServiceName());
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
        if ("IApiServicePort".equals(inputPortName)) {
            return getIApiServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://i.api.ei.gswuweizlh.iih/", "IApiService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://i.api.ei.gswuweizlh.iih/", "IApiServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IApiServicePort".equals(portName)) {
            setIApiServicePortEndpointAddress(address);
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
