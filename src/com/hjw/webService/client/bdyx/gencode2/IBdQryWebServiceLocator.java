/**
 * IBdQryWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.bdyx.gencode2;

public class IBdQryWebServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.bdyx.gencode2.IBdQryWebService {

    public IBdQryWebServiceLocator(String url) {
    	this.IBdQryWebServicePort_address = url;
    }


    public IBdQryWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IBdQryWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IBdQryWebServicePort
    private java.lang.String IBdQryWebServicePort_address = "http://192.168.100.55/iih.ei.std.bd.v1.i.IBdQryWebService?p=YWNjZXNzX3Rva2VuPWI1MjgyMDM0LTkxMTEtNGVkMC1hNjExLTA2NTYwMWExOGEyNg==";

    public java.lang.String getIBdQryWebServicePortAddress() {
        return IBdQryWebServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IBdQryWebServicePortWSDDServiceName = "IBdQryWebServicePort";

    public java.lang.String getIBdQryWebServicePortWSDDServiceName() {
        return IBdQryWebServicePortWSDDServiceName;
    }

    public void setIBdQryWebServicePortWSDDServiceName(java.lang.String name) {
        IBdQryWebServicePortWSDDServiceName = name;
    }

    public com.hjw.webService.client.bdyx.gencode2.IBdQryWebServicePortType getIBdQryWebServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IBdQryWebServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIBdQryWebServicePort(endpoint);
    }

    public com.hjw.webService.client.bdyx.gencode2.IBdQryWebServicePortType getIBdQryWebServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.bdyx.gencode2.IBdQryWebServiceSoapBindingStub _stub = new com.hjw.webService.client.bdyx.gencode2.IBdQryWebServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getIBdQryWebServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIBdQryWebServicePortEndpointAddress(java.lang.String address) {
        IBdQryWebServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.bdyx.gencode2.IBdQryWebServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.bdyx.gencode2.IBdQryWebServiceSoapBindingStub _stub = new com.hjw.webService.client.bdyx.gencode2.IBdQryWebServiceSoapBindingStub(new java.net.URL(IBdQryWebServicePort_address), this);
                _stub.setPortName(getIBdQryWebServicePortWSDDServiceName());
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
        if ("IBdQryWebServicePort".equals(inputPortName)) {
            return getIBdQryWebServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://i.v1.bd.std.ei.iih/", "IBdQryWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://i.v1.bd.std.ei.iih/", "IBdQryWebServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IBdQryWebServicePort".equals(portName)) {
            setIBdQryWebServicePortEndpointAddress(address);
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
