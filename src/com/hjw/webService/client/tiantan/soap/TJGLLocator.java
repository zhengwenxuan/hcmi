/**
 * TJGLLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.tiantan.soap;

public class TJGLLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.tiantan.soap.TJGL {

    public TJGLLocator(String url) {
    	this.TJGLSoap_address = url;
    }


    public TJGLLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TJGLLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TJGLSoap
    private java.lang.String TJGLSoap_address = "http://10.0.103.38/TTYY_HRP/HRPWebservice/services/TJGL.asmx?wsdl";

    public java.lang.String getTJGLSoapAddress() {
        return TJGLSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TJGLSoapWSDDServiceName = "TJGLSoap";

    public java.lang.String getTJGLSoapWSDDServiceName() {
        return TJGLSoapWSDDServiceName;
    }

    public void setTJGLSoapWSDDServiceName(java.lang.String name) {
        TJGLSoapWSDDServiceName = name;
    }

    public com.hjw.webService.client.tiantan.soap.TJGLSoap_PortType getTJGLSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TJGLSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTJGLSoap(endpoint);
    }

    public com.hjw.webService.client.tiantan.soap.TJGLSoap_PortType getTJGLSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.tiantan.soap.TJGLSoap_BindingStub _stub = new com.hjw.webService.client.tiantan.soap.TJGLSoap_BindingStub(portAddress, this);
            _stub.setPortName(getTJGLSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTJGLSoapEndpointAddress(java.lang.String address) {
        TJGLSoap_address = address;
    }


    // Use to get a proxy class for TJGLSoap12
    private java.lang.String TJGLSoap12_address = "http://10.0.103.38/TTYY_HRP/HRPWebservice/services/TJGL.asmx?wsdl";

    public java.lang.String getTJGLSoap12Address() {
        return TJGLSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TJGLSoap12WSDDServiceName = "TJGLSoap12";

    public java.lang.String getTJGLSoap12WSDDServiceName() {
        return TJGLSoap12WSDDServiceName;
    }

    public void setTJGLSoap12WSDDServiceName(java.lang.String name) {
        TJGLSoap12WSDDServiceName = name;
    }

    public com.hjw.webService.client.tiantan.soap.TJGLSoap_PortType getTJGLSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TJGLSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTJGLSoap12(endpoint);
    }

    public com.hjw.webService.client.tiantan.soap.TJGLSoap_PortType getTJGLSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.tiantan.soap.TJGLSoap12Stub _stub = new com.hjw.webService.client.tiantan.soap.TJGLSoap12Stub(portAddress, this);
            _stub.setPortName(getTJGLSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTJGLSoap12EndpointAddress(java.lang.String address) {
        TJGLSoap12_address = address;
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
            if (com.hjw.webService.client.tiantan.soap.TJGLSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.tiantan.soap.TJGLSoap_BindingStub _stub = new com.hjw.webService.client.tiantan.soap.TJGLSoap_BindingStub(new java.net.URL(TJGLSoap_address), this);
                _stub.setPortName(getTJGLSoapWSDDServiceName());
                return _stub;
            }
            if (com.hjw.webService.client.tiantan.soap.TJGLSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.tiantan.soap.TJGLSoap12Stub _stub = new com.hjw.webService.client.tiantan.soap.TJGLSoap12Stub(new java.net.URL(TJGLSoap12_address), this);
                _stub.setPortName(getTJGLSoap12WSDDServiceName());
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
        if ("TJGLSoap".equals(inputPortName)) {
            return getTJGLSoap();
        }
        else if ("TJGLSoap12".equals(inputPortName)) {
            return getTJGLSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("KaiserHRP", "TJGL");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("KaiserHRP", "TJGLSoap"));
            ports.add(new javax.xml.namespace.QName("KaiserHRP", "TJGLSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TJGLSoap".equals(portName)) {
            setTJGLSoapEndpointAddress(address);
        }
        else 
if ("TJGLSoap12".equals(portName)) {
            setTJGLSoap12EndpointAddress(address);
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
