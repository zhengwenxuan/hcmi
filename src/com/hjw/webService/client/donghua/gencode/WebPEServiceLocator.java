/**
 * WebPEServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.donghua.gencode;

public class WebPEServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.donghua.gencode.WebPEService {

    public WebPEServiceLocator(String url) {
    	this.WebPEServiceSoap_address = url;
    }


    public WebPEServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WebPEServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WebPEServiceSoap
    private java.lang.String WebPEServiceSoap_address = "http://172.16.1.116/csp/i-pe/DHC.PE.BS.WebPEService.cls";

    public java.lang.String getWebPEServiceSoapAddress() {
        return WebPEServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WebPEServiceSoapWSDDServiceName = "WebPEServiceSoap";

    public java.lang.String getWebPEServiceSoapWSDDServiceName() {
        return WebPEServiceSoapWSDDServiceName;
    }

    public void setWebPEServiceSoapWSDDServiceName(java.lang.String name) {
        WebPEServiceSoapWSDDServiceName = name;
    }

    public com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_PortType getWebPEServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WebPEServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWebPEServiceSoap(endpoint);
    }

    public com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_PortType getWebPEServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_BindingStub _stub = new com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getWebPEServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWebPEServiceSoapEndpointAddress(java.lang.String address) {
        WebPEServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_BindingStub _stub = new com.hjw.webService.client.donghua.gencode.WebPEServiceSoap_BindingStub(new java.net.URL(WebPEServiceSoap_address), this);
                _stub.setPortName(getWebPEServiceSoapWSDDServiceName());
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
        if ("WebPEServiceSoap".equals(inputPortName)) {
            return getWebPEServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org", "WebPEService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org", "WebPEServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WebPEServiceSoap".equals(portName)) {
            setWebPEServiceSoapEndpointAddress(address);
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
